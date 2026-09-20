package online.kingdomkeys.kingdomkeys.world;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.effects.ModMobEffects;
import online.kingdomkeys.kingdomkeys.encounter.EncounterContext;
import online.kingdomkeys.kingdomkeys.encounter.EncounterInstance;
import online.kingdomkeys.kingdomkeys.encounter.RoomEncounter;
import online.kingdomkeys.kingdomkeys.entity.mob.ApprenticeEntity;
import online.kingdomkeys.kingdomkeys.entity.mob.Dueller;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCShowInformation;
import online.kingdomkeys.kingdomkeys.network.stc.SCShowMessagesPacket;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.Room;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.modifiers.RoomModifier;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.modifiers.RoomModifierType;

import java.util.*;

public class TrainingHandler {
    private static final Map<UUID, Lesson> LESSONS = new HashMap<>();
    private static final double LEAVE_RANGE = 48.0D;
    private static final int GROUND_SEARCH = 4;

    private record Lesson(RoomEncounter encounter, Arena arena, EncounterInstance instance) {
    }

    private static class Arena implements EncounterContext {
        private final LivingEntity master;
        private final UUID pupil;
        private final int radius;
        private final int points;

        private final int level;

        private EncounterInstance instance;

        private final List<LivingEntity> spawned = new ArrayList<>();

        private LivingEntity adopted;

        Arena(LivingEntity master, Player pupil, int radius, int points, int level) {
            this.master = master;
            this.pupil = pupil.getUUID();
            this.radius = radius;
            this.points = points;
            this.level = level;
        }

        void stepInto(LivingEntity copy) {
            copy.moveTo(master.getX(), master.getY(), master.getZ(), master.getYRot(), master.getXRot());
            copy.setYHeadRot(master.getYHeadRot());
            copy.setYBodyRot(master.getYRot());
        }

        void removeSpawned() {
            spawned.forEach(entity -> {
                if (!entity.isAlive()) {
                    return;
                }

                // An adopted opponent was already living here before the lesson and will be after
                // it, so they are stood down rather than cleared away
                if (entity != adopted) {
                    entity.discard();
                } else if (entity instanceof Dueller dueller && !dueller.isSettled()) {
                    dueller.loseDuel();
                }
            });
            spawned.clear();
        }

        @Override
        public LivingEntity adopt(EntityType<?> entityType) {
            if (adopted != null || master.getType() != entityType) {
                return null;
            }

            adopted = master;
            return master;
        }

        boolean holds(LivingEntity entity) {
            return spawned.contains(entity);
        }

        boolean lostItsOpponent() {
            return adopted instanceof Dueller dueller && !dueller.isDuelling() && !dueller.isSettled();
        }

        @Override
        public Optional<EncounterInstance> getEncounter() {
            return Optional.ofNullable(instance);
        }

        @Override
        public List<BlockPos> getSpawnPoints() {
            List<BlockPos> ring = new ArrayList<>();
            LevelReader level = master.level();
            BlockPos centre = master.blockPosition();

            for (int i = 0; i < points; i++) {
                double angle = (Math.PI * 2D / points) * i;
                int x = centre.getX() + (int) Math.round(Math.cos(angle) * radius);
                int z = centre.getZ() + (int) Math.round(Math.sin(angle) * radius);

                ring.add(groundAt(level, new BlockPos(x, centre.getY(), z)));
            }

            return ring;
        }

        private static BlockPos groundAt(LevelReader level, BlockPos from) {
            for (int drop = 0; drop <= GROUND_SEARCH; drop++) {
                BlockPos candidate = from.below(drop);
                if (standable(level, candidate)) {
                    return candidate;
                }
            }

            for (int rise = 1; rise <= GROUND_SEARCH; rise++) {
                BlockPos candidate = from.above(rise);
                if (standable(level, candidate)) {
                    return candidate;
                }
            }

            return from;
        }

        private static boolean standable(LevelReader level, BlockPos pos) {
            BlockState below = level.getBlockState(pos.below());
            return !below.isAir() && level.getBlockState(pos).isAir() && level.getBlockState(pos.above()).isAir();
        }

        @Override
        public <T extends RoomModifier> List<T> getModifiers(RoomModifierType<?> type) {
            // Modifiers are a property of a room, and there is no room here
            return List.of();
        }

        @Override
        public List<Player> getParticipants(ServerLevel level) {
            Player player = level.getPlayerByUUID(pupil);
            return player == null ? List.of() : List.of(player);
        }

        @Override
        public Optional<Room> getRoom() {
            return Optional.empty();
        }

        @Override
        public int getSpawnLevel() {
            return level > RoomEncounter.DYNAMIC_LEVEL ? level : EncounterContext.super.getSpawnLevel();
        }

        @Override
        public int getBaseLevel() {
            if (level > RoomEncounter.DYNAMIC_LEVEL) {
                return level;
            }

            Player player = master.level().getPlayerByUUID(pupil);

            if (player != null) {
                PlayerData playerData = PlayerData.get(player);
                if (playerData != null) {
                    return playerData.getLevel();
                }
            }

            return 1;
        }

        @Override
        public void onSpawn(LivingEntity entity) {
            // Nothing to dress or place: an adopted opponent is already themselves and already here
            if (entity == adopted && entity instanceof Dueller dueller) {
                dueller.beginDuel(this.master.level().getPlayerByUUID(pupil), getBaseLevel());
            }

            spawned.add(entity);
        }
    }

    public static boolean isTraining(Player player) {
        return LESSONS.containsKey(player.getUUID());
    }

    public static EncounterContext contextOf(LivingEntity entity) {
        for (Lesson lesson : LESSONS.values()) {
            if (lesson.arena().spawned.contains(entity)) {
                return lesson.arena();
            }
        }

        return null;
    }

    public static boolean start(ServerPlayer pupil, LivingEntity master, RoomEncounter encounter) {
        if (encounter == null || LESSONS.containsKey(pupil.getUUID())) {
            return false;
        }

        if (!encounter.canStart(PlayerData.get(pupil))) {
            return false;
        }

        int level = master instanceof ApprenticeEntity apprentice ? apprentice.getApprenticeLevel() : encounter.getLevel();

        Arena arena = new Arena(master, pupil, encounter.getArenaRadius(), encounter.getSpawnPoints(), level);
        EncounterInstance instance = encounter.getEncounter().type().createInstance(encounter);
        arena.instance = instance;

        LESSONS.put(pupil.getUUID(), new Lesson(encounter, arena, instance));

        // The plaque, the way a mission announces itself
        encounter.getInfo().ifPresent(key -> PacketHandler.sendTo(new SCShowInformation(key), pupil));

        instance.start(arena, pupil.serverLevel());

        // An arena with no radius means the fight happens on the spot, so whatever the wave spawned
        // takes over the place of the one who set it instead of appearing next to them. An opponent
        // the wave adopted is already standing in that place, being the one who set it
        if (encounter.getArenaRadius() == 0) {
            arena.spawned.forEach(spawned -> {
                if (spawned != arena.adopted) {
                    arena.stepInto(spawned);
                }
            });
        }

        return true;
    }

    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent.Post event) {
        if (!(event.getEntity() instanceof ServerPlayer pupil)) {
            return;
        }

        Lesson lesson = LESSONS.get(pupil.getUUID());
        if (lesson == null) {
            return;
        }

        ServerLevel level = pupil.serverLevel();

        // Beaten, gone, or walked away from the master: nothing left to teach
        if (!pupil.isAlive() || pupil.isSpectator() || !lesson.arena().master.isAlive()
                || lesson.arena().master.level() != level
                || pupil.distanceToSqr(lesson.arena().master) > LEAVE_RANGE * LEAVE_RANGE) {
            abandon(pupil, Strings.Training_Left);
            return;
        }

        if (!lesson.instance().isComplete() && lesson.arena().lostItsOpponent()) {
            abandon(pupil, Strings.Training_Left);
            return;
        }

        lesson.instance().tick(lesson.arena(), level);

        if (lesson.instance().isComplete()) {
            finish(pupil, lesson);
        }
    }

    public static void beaten(LivingEntity opponent) {
        for (Lesson lesson : LESSONS.values()) {
            if (lesson.arena().holds(opponent)) {
                lesson.instance().setComplete();
                return;
            }
        }
    }

    private static void finish(ServerPlayer pupil, Lesson lesson) {
        LESSONS.remove(pupil.getUUID());
        lesson.arena().removeSpawned();

        PlayerData playerData = PlayerData.get(pupil);

        boolean firstClear = lesson.encounter().getGrants().map(flag -> playerData == null || !playerData.hasFlag(flag)).orElse(true);

        if (playerData != null) {
            if (lesson.encounter().getLux() > 0) {
                playerData.addLux(lesson.encounter().getLux());
            }
            if (lesson.encounter().getExperience() > 0) {
                playerData.addExperience(pupil, lesson.encounter().getExperience(), false, true);
            }
            // Grant this encounter's grants into the player's data
            lesson.encounter().getGrants().ifPresent(playerData::addFlag);

            PacketHandler.sendTo(new SCSyncPlayerData(pupil), pupil);
        }

        if (firstClear && !lesson.encounter().getRewards().isEmpty()) {
            Utils.giveItems(pupil, true, lesson.encounter().getRewards().stream().map(ItemStack::copy).toList());
        }

        announce(pupil, Strings.Training_Won, Strings.Training_Won_Sub);
    }

    public static void abandon(ServerPlayer pupil, String titleKey) {
        Lesson lesson = LESSONS.remove(pupil.getUUID());

        if (lesson == null) {
            return;
        }

        lesson.arena().removeSpawned();
        announce(pupil, titleKey, "");
    }

    public static float trainingBlow(Player pupil, DamageSource source, float damage) {
        if (!(pupil instanceof ServerPlayer serverPupil) || !LESSONS.containsKey(pupil.getUUID())) {
            return damage;
        }

        if (pupil.hasEffect(ModMobEffects.KO)) {
            abandon(serverPupil, Strings.Training_Lost);
            return damage;
        }

        if (damage < pupil.getHealth() + pupil.getAbsorptionAmount()) {
            return damage;
        }

        Utils.knockOut(pupil);
        abandon(serverPupil, Strings.Training_Lost);
        return 0F;
    }

    @SubscribeEvent
    public void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        forget(event.getEntity());
    }

    @SubscribeEvent
    public void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        forget(event.getEntity());
    }

    public static void forget(Player player) {
        Lesson lesson = LESSONS.remove(player.getUUID());

        if (lesson != null) {
            lesson.arena().removeSpawned();
        }
    }

    private static void announce(ServerPlayer pupil, String titleKey, String subtitleKey) {
        PacketHandler.sendTo(new SCShowMessagesPacket(List.of(new Utils.Title(titleKey, subtitleKey, 4, 50, 10))), pupil);
    }
}

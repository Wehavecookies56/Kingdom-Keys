package online.kingdomkeys.kingdomkeys.dialogue;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.entity.LightPortalEntity;
import online.kingdomkeys.kingdomkeys.world.dimension.daybreak_town.DaybreakTownDimension;
import online.kingdomkeys.kingdomkeys.encounter.RoomEncounter;
import online.kingdomkeys.kingdomkeys.entity.mob.ForetellerEntity;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCOpenForetellerScreen;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.kingdomkeys.kingdomkeys.world.TrainingHandler;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry.ModJsonRegistries;

import java.util.List;
import java.util.Optional;

public interface DialogueAction {
    void run(ServerPlayer player, LivingEntity speaker);

    default boolean ends() {
        return false;
    }

    MapCodec<? extends DialogueAction> codec();

    Type<? extends DialogueAction> type();

    Codec<DialogueAction> CODEC = ModDialogue.ACTIONS.byNameCodec().dispatch(DialogueAction::type, Type::codec);

    record Type<T extends DialogueAction>(MapCodec<T> codec) { }

    /** The master's own shop, in the currency of his union. */
    record OpenShop() implements DialogueAction {

        public static final MapCodec<OpenShop> CODEC = MapCodec.unit(OpenShop::new);

        @Override
        public void run(ServerPlayer player, LivingEntity speaker) {
            if (!(speaker instanceof ForetellerEntity master)) {
                return;
            }

            PlayerData data = PlayerData.get(player);
            if (data == null) {
                return;
            }

            PacketHandler.sendTo(new SCOpenForetellerScreen(master.getUnion(), data.serializeNBT(player.level().registryAccess())), player);
        }

        @Override
        public boolean ends() {
            return true;
        }

        @Override
        public MapCodec<OpenShop> codec() {
            return CODEC;
        }

        @Override
        public Type<OpenShop> type() {
            return ModDialogue.OPEN_SHOP.get();
        }
    }

    /** Sets the player on a lesson or a bout, by name, out of whichever registry holds it. */
    record StartEncounter(boolean duel, ResourceLocation encounter) implements DialogueAction {
        public static final MapCodec<StartEncounter> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Codec.BOOL.optionalFieldOf("duel", false).forGetter(StartEncounter::duel),
                ResourceLocation.CODEC.fieldOf("encounter").forGetter(StartEncounter::encounter)
            ).apply(instance, StartEncounter::new)
        );

        @Override
        public void run(ServerPlayer player, LivingEntity speaker) {
            RoomEncounter lesson = (duel ? ModJsonRegistries.DUEL_ENCOUNTER : ModJsonRegistries.TRAINING_ENCOUNTER).get().getValue(encounter);
            if (lesson != null) {
                TrainingHandler.start(player, speaker, lesson);
            }
        }

        @Override
        public boolean ends() {
            return true;
        }

        @Override
        public MapCodec<StartEncounter> codec() {
            return CODEC;
        }

        @Override
        public Type<StartEncounter> type() {
            return ModDialogue.START_ENCOUNTER.get();
        }
    }

    /** Hands something over. Goes to the overflow if there is no room for it. */
    record GiveItem(ItemStack item) implements DialogueAction {
        public static final MapCodec<GiveItem> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                ItemStack.CODEC.fieldOf("item").forGetter(GiveItem::item)
            ).apply(instance, GiveItem::new)
        );

        @Override
        public void run(ServerPlayer player, LivingEntity speaker) {
            Utils.giveItems(player, true, List.of(item.copy()));
        }

        @Override
        public MapCodec<GiveItem> codec() {
            return CODEC;
        }

        @Override
        public Type<GiveItem> type() {
            return ModDialogue.GIVE_ITEM.get();
        }
    }

    record SetFlag(ResourceLocation flag, boolean set) implements DialogueAction {
        public static final MapCodec<SetFlag> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                ResourceLocation.CODEC.fieldOf("flag").forGetter(SetFlag::flag),
                Codec.BOOL.optionalFieldOf("set", true).forGetter(SetFlag::set)
            ).apply(instance, SetFlag::new)
        );

        @Override
        public void run(ServerPlayer player, LivingEntity speaker) {
            PlayerData data = PlayerData.get(player);
            if (data == null) {
                return;
            }

            boolean changed = set ? data.addFlag(flag) : data.removeFlag(flag);

            if (changed) {
                PacketHandler.sendTo(new SCSyncPlayerData(player), player);
            }
        }

        @Override
        public MapCodec<SetFlag> codec() {
            return CODEC;
        }

        @Override
        public Type<SetFlag> type() {
            return ModDialogue.SET_FLAG.get();
        }
    }

    record OpenPortal(ResourceLocation dimension, Optional<BlockPos> pos, Optional<ResourceLocation> until) implements DialogueAction {
        public static final MapCodec<OpenPortal> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                ResourceLocation.CODEC.fieldOf("dimension").forGetter(OpenPortal::dimension),
                BlockPos.CODEC.optionalFieldOf("pos").forGetter(OpenPortal::pos),
                ResourceLocation.CODEC.optionalFieldOf("until").forGetter(OpenPortal::until)
            ).apply(instance, OpenPortal::new)
        );

        @Override
        public void run(ServerPlayer player, LivingEntity speaker) {
            ResourceKey<Level> key = ResourceKey.create(Registries.DIMENSION, dimension);

            if (player.getServer() == null || player.getServer().getLevel(key) == null) {
                return;
            }

            // With no coordinate written down, a master's door comes out at his own post: he is taking you to where he holds court, not to the middle of a town you have never seen
            DaybreakTownDimension.Post post = pos.isEmpty() && speaker instanceof ForetellerEntity master ? DaybreakTownDimension.postFor(master.getUnion()) : null;

            if (pos.isEmpty() && post == null) {
                return;
            }

            Vec3 here = beside(player, speaker);
            Vec3 arrival = post != null ? post.pos() : new Vec3(pos.get().getX() + 0.5D, pos.get().getY(), pos.get().getZ() + 0.5D);
            float arrivalYaw = post != null ? post.yaw() : speaker.getYRot();

            LightPortalEntity outward = new LightPortalEntity(speaker.level(), here, arrival, key, arrivalYaw, player.getUUID());

            if (!speaker.level().addFreshEntity(outward)) {
                return;
            }

            // Nothing is left standing on the far side: several pupils' doors piled on one master's post would be a mess. The way home is opened there and then, when it is asked for
            until.ifPresent(outward::staysUntil);

            // This is the door that leads away, so this is the one that remembers where from
            outward.recordsOrigin();

            speaker.level().playSound(null, outward.blockPosition(), SoundEvents.BEACON_ACTIVATE, SoundSource.AMBIENT, 0.7F, 1.6F);

            // He waits beside it, and goes when you go
            outward.setGreeter(speaker);
        }

        @Override
        public boolean ends() {
            return true;
        }

        @Override
        public MapCodec<OpenPortal> codec() {
            return CODEC;
        }

        @Override
        public Type<OpenPortal> type() {
            return ModDialogue.OPEN_PORTAL.get();
        }
    }

    record ReturnHome() implements DialogueAction {
        public static final MapCodec<ReturnHome> CODEC = MapCodec.unit(ReturnHome::new);

        @Override
        public void run(ServerPlayer player, LivingEntity speaker) {
            PlayerData data = PlayerData.get(player);

            if (data == null || data.getReturnLocation() == null) {
                return;
            }

            ResourceKey<Level> home = data.getReturnDimension();

            if (player.getServer() == null || player.getServer().getLevel(home) == null) {
                return;
            }

            LightPortalEntity way = new LightPortalEntity(speaker.level(), beside(player, speaker), data.getReturnLocation(), home, player.getYRot(), player.getUUID());

            if (speaker.level().addFreshEntity(way)) {
                speaker.level().playSound(null, way.blockPosition(), SoundEvents.BEACON_ACTIVATE, SoundSource.AMBIENT, 0.7F, 1.6F);
            }
        }

        @Override
        public boolean ends() {
            return true;
        }

        @Override
        public MapCodec<ReturnHome> codec() {
            return CODEC;
        }

        @Override
        public Type<ReturnHome> type() {
            return ModDialogue.RETURN_HOME.get();
        }
    }

    /** Far enough in front of the speaker that he is not standing inside his own doorway. */
    double AHEAD = 2.5D;

    static Vec3 beside(ServerPlayer player, LivingEntity speaker) {
        Vec3 facing = speaker.position().subtract(player.position());
        facing = facing.lengthSqr() < 1.0E-4D ? speaker.getLookAngle() : facing.normalize();

        return speaker.position().add(facing.x * AHEAD, 0.0D, facing.z * AHEAD);
    }

    /** Ends it. An answer with no node to go to would end anyway; this says so out loud. */
    record Close() implements DialogueAction {
        public static final MapCodec<Close> CODEC = MapCodec.unit(Close::new);

        @Override
        public void run(ServerPlayer player, LivingEntity speaker) {
        }

        @Override
        public boolean ends() {
            return true;
        }

        @Override
        public MapCodec<Close> codec() {
            return CODEC;
        }

        @Override
        public Type<Close> type() {
            return ModDialogue.CLOSE.get();
        }
    }
}

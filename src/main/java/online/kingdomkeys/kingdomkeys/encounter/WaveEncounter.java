package online.kingdomkeys.kingdomkeys.encounter;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.ArrayListDeque;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.saveddata.SavedData;
import net.neoforged.neoforge.event.EventHooks;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.data.CastleOblivionData;
import online.kingdomkeys.kingdomkeys.data.GlobalData;
import online.kingdomkeys.kingdomkeys.entity.mob.BaseKHEntity;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCShowMessagesPacket;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry.ModEncounterTypes;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.modifiers.RoomModifier;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static online.kingdomkeys.kingdomkeys.lib.Strings.COEncounter_Wave;

public class WaveEncounter implements Encounter {

    /** What a wave does when the encounter does not say otherwise. */
    public static final int DEFAULT_SPAWN_DELAY = 14;

    List<Wave> waves;
    int intervalTicks;
    boolean shuffleWaveOrder;
    int spawnDelayTicks;

    public static final MapCodec<WaveEncounter> CODEC = RecordCodecBuilder.mapCodec(waveEncounterInstance ->
        waveEncounterInstance.group(
                Wave.CODEC.listOf().fieldOf("waves").forGetter(WaveEncounter::getWaves),
                Codec.INT.fieldOf("interval_ticks").forGetter(WaveEncounter::getIntervalTicks),
                Codec.BOOL.optionalFieldOf("shuffle_order", false).forGetter(WaveEncounter::shuffleWaveOrder),
                Codec.INT.optionalFieldOf("spawn_delay_ticks", DEFAULT_SPAWN_DELAY).forGetter(WaveEncounter::getSpawnDelayTicks)
        ).apply(waveEncounterInstance, WaveEncounter::new)
    );

    public WaveEncounter(List<Wave> waves, int intervalTicks, boolean shuffleWaveOrder) {
        this(waves, intervalTicks, shuffleWaveOrder, DEFAULT_SPAWN_DELAY);
    }

    public WaveEncounter(List<Wave> waves, int intervalTicks, boolean shuffleWaveOrder, int spawnDelayTicks) {
        this.waves = waves;
        this.intervalTicks = intervalTicks;
        this.shuffleWaveOrder = shuffleWaveOrder;
        this.spawnDelayTicks = spawnDelayTicks;
    }

    public int getSpawnDelayTicks() {
        return spawnDelayTicks;
    }

    public boolean shuffleWaveOrder() {
        return shuffleWaveOrder;
    }

    public int getIntervalTicks() {
        return intervalTicks;
    }

    public List<Wave> getWaves() {
        return waves;
    }

    @Override
    public MapCodec<? extends Encounter> codec() {
        return CODEC;
    }

    @Override
    public EncounterType<?, ?> type() {
        return ModEncounterTypes.WAVE.get();
    }

    public static class Handler implements EncounterHandler<WaveEncounter, State> {

        @Override
        public State createState() {
            return new State();
        }

        Queue<BlockPos> spawnPoints;

        @Override
        public void start(WaveEncounter encounter, State state, EncounterInstance instance, EncounterContext context, ServerLevel level) {
            if (encounter.shuffleWaveOrder) {
                state.shuffleOrder(encounter.waves.size());
            }

            //spawn first wave

            createSpawnPointQueue(context);

            state.setMobsRemaining(encounter.getWaves().stream().mapToInt(Wave::size).sum());
            KingdomKeys.LOGGER.debug("Wave encounter started with {} mobs total", state.getMobsRemaining());

            spawnWave(instance, encounter, state, context, level);
        }

        @Override
        public void tick(WaveEncounter encounter, State state, EncounterInstance instance, EncounterContext context, ServerLevel level) {
            if (spawnPoints == null) {
                createSpawnPointQueue(context);
            }

            trickle(encounter, state, context, level);

            //TODO interval ticks

            if (state.getMobsRemaining() > 0 || state.currentWave < encounter.getWaves().size()) {
                if (state.getCurrentlySpawned() <= 0) {
                    state.nextWave();
                    spawnWave(instance, encounter, state, context, level);
                }
            } else {
                instance.setComplete();
            }
        }

        @Override
        public void end(WaveEncounter encounter, State state, EncounterInstance instance, EncounterContext context, ServerLevel level) {

        }

        public void createSpawnPointQueue(EncounterContext context) {
            spawnPoints = context.getSpawnPoints().stream().collect(Collectors.toCollection(ArrayListDeque::new));
        }

        public BlockPos getSpawnPoint() {
            BlockPos next = spawnPoints.poll();
            spawnPoints.offer(next);
            return next;
        }

        public void trickle(WaveEncounter encounter, State state, EncounterContext context, ServerLevel level) {
            if (state.currentWave >= encounter.getWaves().size()) {
                return;
            }

            Wave wave = encounter.getWaves().get(state.getWaveIndex());
            int wait = encounter.getSpawnDelayTicks();

            if (state.getArrived() >= wave.size() || (wait > 0 && state.waitForArrival())) {
                return;
            }

            do {
                place(wave.spawns().get(state.getArrived()).value(), wave, state, context, level);
                state.arrived(wait);
            } while (wait <= 0 && state.getArrived() < wave.size());
        }

        public void place(EntityType<?> entityType, Wave wave, State state, EncounterContext context, ServerLevel level) {
            LivingEntity spawned = (LivingEntity) entityType.create(level);
            BlockPos spawnPoint = getSpawnPoint();

            if (spawned == null) {
                KingdomKeys.LOGGER.error("Failed to spawn {}", entityType);
                state.removeCurrentSpawn();
                return;
            }

            context.getRoom().ifPresent(room -> {
                room.addEntityToCache(spawned);
            });
            GlobalData globalData = GlobalData.get(spawned);
            globalData.setCastleOblivionMarker(true);
            globalData.setLevel(context.getSpawnLevel());
            context.onSpawn(spawned);
            wave.onSpawn(context, spawned);
            spawned.moveTo((double)spawnPoint.getX() + 0.5, spawnPoint.getY(), (double)spawnPoint.getZ() + 0.5, Mth.wrapDegrees(level.random.nextFloat() * 360.0F), 0.0F);
            level.addFreshEntityWithPassengers(spawned);

            SoundEvent arrival = spawned instanceof BaseKHEntity kh && kh.arrivalSound() != null ? kh.arrivalSound() : ModSounds.portal.get();
            level.playSound(null, spawnPoint, arrival, SoundSource.HOSTILE, 2, 1);
            if (spawned instanceof Mob spawnedMob) {
                EventHooks.finalizeMobSpawn(spawnedMob, level, level.getCurrentDifficultyAt(spawned.blockPosition()), MobSpawnType.TRIAL_SPAWNER, null);
            }
            KingdomKeys.LOGGER.debug("Spawned {}", spawned);
            CastleOblivionData.InteriorData.get(level).ifPresent(SavedData::setDirty);
        }

        public void spawnWave(EncounterInstance instance, WaveEncounter encounter, State state, EncounterContext context, ServerLevel level) {
            if (state.getCurrentlySpawned() <= 0) {
                if (state.currentWave < encounter.getWaves().size()) {
                    KingdomKeys.LOGGER.debug("Spawning wave {}", state.currentWave);
                    Wave currentWave = encounter.getWaves().get(state.getWaveIndex());
                    if (state.getWaveIndex() > 0) {
                        Wave prevWave = encounter.getWaves().get(state.getWaveIndex()-1);
                        context.getParticipants(level).forEach(player -> {
                            prevWave.onEnd(context, player);
                        });
                    }
                    context.getParticipants(level).forEach(player -> {
                        List<Utils.Title> message = List.of(
                                new Utils.Title(Utils.translateToLocal(COEncounter_Wave) + " " + (state.currentWave + 1), "")
                        );
                        PacketHandler.sendTo(new SCShowMessagesPacket(message), (ServerPlayer) player);
                        currentWave.onStart(context, player);
                    });
                    // The wave is counted in full right now, but its members walk in one at a time from
                    // here on: see trickle(). Counting them up front is what keeps the encounter from
                    // deciding the wave is already cleared while half of it is still on its way.
                    state.beginArrivals();
                    state.spawnMobs(currentWave.size());
                    CastleOblivionData.InteriorData.get(level).ifPresent(SavedData::setDirty);
                } else {
                    context.getParticipants(level).forEach(player -> {
                        encounter.getWaves().getLast().onEnd(context, player);
                    });
                    instance.setComplete();
                }
            }
        }

    }

    public record Wave(List<Holder<EntityType<?>>> spawns, List<RoomModifier> modifiers) {
        public static final Codec<Wave> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                BuiltInRegistries.ENTITY_TYPE.holderByNameCodec().listOf().fieldOf("spawns").forGetter(Wave::spawns),
                RoomModifier.CODEC.listOf().optionalFieldOf("modifiers", new ArrayList<>()).forGetter(Wave::modifiers)
            ).apply(instance, Wave::new)
        );

        public Wave(List<Holder<EntityType<?>>> spawns, RoomModifier... modifiers) {
            this(spawns, Arrays.stream(modifiers).toList());
        }

        public int size() {
            return spawns.size();
        }

        public void forEach(Consumer<EntityType<?>> entityType) {
            for (Holder<EntityType<?>> entityTypeHolder : spawns) {
                entityType.accept(entityTypeHolder.value());
            }
        }

        public void onStart(EncounterContext context, Player player) {
            modifiers.forEach(modifier -> {
                modifier.onEnter(context, player);
            });
        }

        public void onEnd(EncounterContext context, Player player) {
            modifiers.forEach(modifier -> {
                modifier.onExit(context, player);
            });
        }

        public void onSpawn(EncounterContext context, LivingEntity spawned) {
            modifiers.forEach(modifier -> {
                modifier.onSpawn(context, spawned);
            });
        }
    }

    public static class State implements Encounter.State {

        private int currentWave, mobsRemaining, currentlySpawned;
        private int arrived, untilNextArrival;
        long waveEndTime;
        List<Integer> shuffledOrder = new ArrayList<>();

        public static final Codec<State> CODEC = RecordCodecBuilder.create(stateInstance ->
                stateInstance.group(
                        Codec.INT.fieldOf("current_wave").forGetter(State::getCurrentWave),
                        Codec.INT.listOf().optionalFieldOf("shuffled_order", new ArrayList<>()).forGetter(o -> o.shuffledOrder),
                        Codec.LONG.fieldOf("wave_end_time").forGetter(State::getWaveEndTime),
                        Codec.INT.fieldOf("mobs_remaining").forGetter(State::getMobsRemaining),
                        Codec.INT.fieldOf("currently_spawned").forGetter(State::getCurrentlySpawned),
                        Codec.INT.optionalFieldOf("arrived", 0).forGetter(State::getArrived),
                        Codec.INT.optionalFieldOf("until_next_arrival", 0).forGetter(o -> o.untilNextArrival)
                ).apply(stateInstance, State::new)
        );

        public State() {
            this.currentWave = 0;
        }

        public int getWaveIndex() {
            if (shuffledOrder.isEmpty()) {
                return currentWave;
            } else {
                if (currentWave >= shuffledOrder.size()) {
                    throw new IllegalStateException("Current wave exceeds total wave count");
                }
                return shuffledOrder.get(currentWave);
            }
        }

        public void shuffleOrder(int wavesSize) {
            shuffledOrder = IntStream.range(0, wavesSize).boxed().collect(Collectors.toList());
            Collections.shuffle(shuffledOrder);
        }

        private State(int currentWave, List<Integer> shuffledOrder, long waveEndTime, int mobsRemaining, int currentlySpawned, int arrived, int untilNextArrival) {
            this.currentWave = currentWave;
            this.shuffledOrder = shuffledOrder;
            this.waveEndTime = waveEndTime;
            this.mobsRemaining = mobsRemaining;
            this.currentlySpawned = currentlySpawned;
            this.arrived = arrived;
            this.untilNextArrival = untilNextArrival;
        }

        /** How many of the current wave have actually been placed in the room. */
        public int getArrived() {
            return arrived;
        }

        /** Opens a fresh wave: nobody has walked in yet, and the first one does so at once. */
        public void beginArrivals() {
            arrived = 0;
            untilNextArrival = 0;
        }

        /** Counts down towards the next arrival; true while the wave should hold where it is. */
        public boolean waitForArrival() {
            if (untilNextArrival <= 0) {
                return false;
            }

            untilNextArrival--;
            return true;
        }

        public void arrived(int wait) {
            arrived++;
            untilNextArrival = wait;
        }

        public int getCurrentWave() {
            return currentWave;
        }

        public void nextWave() {
            currentWave++;
        }

        public long getWaveEndTime() {
            return waveEndTime;
        }

        public int getMobsRemaining() {
            return mobsRemaining;
        }

        public int getCurrentlySpawned() {
            return currentlySpawned;
        }

        public void setMobsRemaining(int mobsRemaining) {
            this.mobsRemaining = mobsRemaining;
        }

        public void setCurrentlySpawned(int currentlySpawned) {
            this.currentlySpawned = currentlySpawned;
        }

        public void spawnMobs(int toSpawn) {
            if (mobsRemaining > 0) {
                currentlySpawned += Math.min(toSpawn, mobsRemaining);
                mobsRemaining -= currentlySpawned;
            }
        }

        public void removeCurrentSpawn() {
            currentlySpawned--;
        }
    }
}

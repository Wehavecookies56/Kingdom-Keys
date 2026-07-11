package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.encounter;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
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
import online.kingdomkeys.kingdomkeys.data.CastleOblivionData;
import online.kingdomkeys.kingdomkeys.data.GlobalData;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCShowMessagesPacket;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry.ModEncounterTypes;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.Room;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.modifiers.RoomModifier;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class WaveEncounter implements Encounter {

    List<Wave> waves;
    int intervalTicks;
    boolean shuffleWaveOrder;

    public static final MapCodec<WaveEncounter> CODEC = RecordCodecBuilder.mapCodec(waveEncounterInstance ->
        waveEncounterInstance.group(
                Wave.CODEC.listOf().fieldOf("waves").forGetter(WaveEncounter::getWaves),
                Codec.INT.fieldOf("interval_ticks").forGetter(WaveEncounter::getIntervalTicks),
                Codec.BOOL.optionalFieldOf("shuffle_order").forGetter(o -> Optional.of(o.shuffleWaveOrder))
        ).apply(waveEncounterInstance, WaveEncounter::new)
    );

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private WaveEncounter(List<Wave> waves, int intervalTicks, Optional<Boolean> shuffleWaveOrder) {
        this.waves = waves;
        this.intervalTicks = intervalTicks;
        this.shuffleWaveOrder = shuffleWaveOrder.orElse(false);
    }

    public WaveEncounter(List<Wave> waves, int intervalTicks, boolean shuffleWaveOrder) {
        this.waves = waves;
        this.intervalTicks = intervalTicks;
        this.shuffleWaveOrder = shuffleWaveOrder;
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
        public void start(WaveEncounter encounter, State state, EncounterInstance instance, Room room, ServerLevel level) {
            if (encounter.shuffleWaveOrder) {
                state.shuffleOrder(encounter.waves.size());
            }

            //spawn first wave

            createSpawnPointQueue(room);

            room.setMobsRemaining(encounter.getWaves().stream().mapToInt(Wave::size).sum());
            KingdomKeys.LOGGER.debug("Wave encounter started with {} mobs total", room.getMobsRemaining());

            spawnWave(instance, encounter, state, room, level);
        }

        @Override
        public void tick(WaveEncounter encounter, State state, EncounterInstance instance, Room room, ServerLevel level) {
            if (spawnPoints == null) {
                createSpawnPointQueue(room);
            }

            //TODO interval ticks

            if (room.getMobsRemaining() > 0 || state.currentWave < encounter.getWaves().size()) {
                if (room.getCurrentlySpawned() <= 0) {
                    state.nextWave();
                    spawnWave(instance, encounter, state, room, level);
                }
            } else {
                instance.setComplete();
            }
        }

        @Override
        public void end(WaveEncounter encounter, State state, EncounterInstance instance, Room room, ServerLevel level) {

        }

        public void createSpawnPointQueue(Room room) {
            spawnPoints = room.getSpawnPoints().stream().collect(Collectors.toCollection(ArrayListDeque::new));
        }

        public BlockPos getSpawnPoint() {
            BlockPos next = spawnPoints.poll();
            spawnPoints.offer(next);
            return next;
        }

        public void spawnWave(EncounterInstance instance, WaveEncounter encounter, State state, Room room, ServerLevel level) {
            if (room.getCurrentlySpawned() <= 0) {
                if (state.currentWave < encounter.getWaves().size()) {
                    KingdomKeys.LOGGER.debug("Spawning wave {}", state.currentWave);
                    Wave currentWave = encounter.getWaves().get(state.getWaveIndex());
                    if (state.getWaveIndex() > 0) {
                        Wave prevWave = encounter.getWaves().get(state.getWaveIndex()-1);
                        Room.getPlayersInRoom(level.getServer(), room).forEach(player -> {
                            prevWave.onEnd(room, player);
                        });
                    }
                    Room.getPlayersInRoom(level.getServer(), room).forEach(player -> {
                        List<Utils.Title> message = List.of(
                                new Utils.Title("co.encounter.wave", ""+(state.currentWave + 1))
                        );
                        PacketHandler.sendTo(new SCShowMessagesPacket(message), (ServerPlayer) player);
                        currentWave.onStart(room, player);
                    });
                    currentWave.forEach(entityType -> {
                        LivingEntity spawned = (LivingEntity) entityType.create(level);
                        BlockPos spawnPoint = getSpawnPoint();
                        if (spawned != null) {
                            room.addEntityToCache(spawned);
                            GlobalData globalData = GlobalData.get(spawned);
                            globalData.setCastleOblivionMarker(true);
                            globalData.setLevel(((room.parentFloor+1) * 10) + Utils.randomWithRange(-3, 3));
                            room.modifierOnSpawn(spawned);
                            currentWave.onSpawn(room, spawned);
                            spawned.moveTo((double)spawnPoint.getX() + 0.5, spawnPoint.getY(), (double)spawnPoint.getZ() + 0.5, Mth.wrapDegrees(level.random.nextFloat() * 360.0F), 0.0F);
                            level.addFreshEntityWithPassengers(spawned);
                            if (spawned instanceof Mob spawnedMob) {
                                EventHooks.finalizeMobSpawn(spawnedMob, level, level.getCurrentDifficultyAt(spawned.blockPosition()), MobSpawnType.TRIAL_SPAWNER, null);
                            }
                            KingdomKeys.LOGGER.debug("Spawned {}", spawned);
                        } else {
                            KingdomKeys.LOGGER.error("Failed to spawn {}", entityType);
                            room.removeCurrentSpawn();
                        }
                    });
                    room.spawnMobs(currentWave.size());
                    CastleOblivionData.InteriorData.get(level).ifPresent(SavedData::setDirty);
                } else {
                    Room.getPlayersInRoom(level.getServer(), room).forEach(player -> {
                        encounter.getWaves().getLast().onEnd(room, player);
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
                RoomModifier.CODEC.listOf().optionalFieldOf("modifiers").forGetter(o -> Optional.ofNullable(o.modifiers()))
            ).apply(instance, Wave::new)
        );


        @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
        public Wave(List<Holder<EntityType<?>>> spawns, Optional<List<RoomModifier>> modifiers) {
            this(spawns, modifiers.orElse(new ArrayList<>()));
        }

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

        public void onStart(Room room, Player player) {
            modifiers.forEach(modifier -> {
                modifier.onEnter(room, player);
            });
        }

        public void onEnd(Room room, Player player) {
            modifiers.forEach(modifier -> {
                modifier.onExit(room, player);
            });
        }

        public void onSpawn(Room room, LivingEntity spawned) {
            modifiers.forEach(modifier -> {
                modifier.onSpawn(room, spawned);
            });
        }
    }

    public static class State implements EncounterState {

        private int currentWave;
        long waveEndTime;
        List<Integer> shuffledOrder = new ArrayList<>();

        public static final Codec<State> CODEC = RecordCodecBuilder.create(stateInstance ->
                stateInstance.group(
                        Codec.INT.fieldOf("current_wave").forGetter(State::getCurrentWave),
                        Codec.INT.listOf().optionalFieldOf("shuffled_order").forGetter(o -> Optional.ofNullable(o.shuffledOrder)),
                        Codec.LONG.fieldOf("wave_end_time").forGetter(State::getWaveEndTime)
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

        @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
        private State(int currentWave, Optional<List<Integer>> shuffledOrder, long waveEndTime) {
            this.currentWave = currentWave;
            this.shuffledOrder = shuffledOrder.orElse(new ArrayList<>());
            this.waveEndTime = waveEndTime;
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
    }
}

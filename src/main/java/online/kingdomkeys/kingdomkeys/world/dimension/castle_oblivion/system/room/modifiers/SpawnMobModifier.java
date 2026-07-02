package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.modifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.neoforged.neoforge.event.EventHooks;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry.ModRoomModifiers;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.Room;

import java.util.Optional;

public class SpawnMobModifier implements RoomModifier {

    Holder<EntityType<?>> entityType;
    CompoundTag additionalData;

    public static final MapCodec<SpawnMobModifier> CODEC = RecordCodecBuilder.mapCodec(instance ->
        instance.group(
            BuiltInRegistries.ENTITY_TYPE.holderByNameCodec().fieldOf("entity").forGetter(SpawnMobModifier::getEntityType),
            CompoundTag.CODEC.optionalFieldOf("additional_data").forGetter(o -> Optional.ofNullable(o.getAdditionalData()))
        ).apply(instance, SpawnMobModifier::new)
    );


    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public SpawnMobModifier(Holder<EntityType<?>> entityType, Optional<CompoundTag> additionalData) {
        this.entityType = entityType;
        this.additionalData = additionalData.orElse(new CompoundTag());
    }

    @Override
    public void onGenerate(Room room, ServerLevel level) {
        if (!room.getSpawnPoints().isEmpty()) {
            BlockPos spawnPoint = room.getSpawnPoints().getFirst();
            additionalData.putString("id", entityType.getKey().location().toString());
            Entity spawned = entityType.value().create(level);
            spawned.load(additionalData);
            spawned.moveTo((double)spawnPoint.getX() + 0.5, spawnPoint.getY(), (double)spawnPoint.getZ() + 0.5, Mth.wrapDegrees(level.random.nextFloat() * 360.0F), 0.0F);
            level.addFreshEntityWithPassengers(spawned);
            if (spawned instanceof Mob spawnedMob) {
                EventHooks.finalizeMobSpawn(spawnedMob, level, level.getCurrentDifficultyAt(spawned.blockPosition()), MobSpawnType.TRIAL_SPAWNER, null);
            }
            KingdomKeys.LOGGER.debug("Spawned {}", spawned);
        }
    }

    private Holder<EntityType<?>> getEntityType() {
        return entityType;
    }

    private CompoundTag getAdditionalData() {
        return additionalData;
    }

    @Override
    public MapCodec<? extends RoomModifier> codec() {
        return CODEC;
    }

    @Override
    public RoomModifierType<? extends RoomModifier> type() {
        return ModRoomModifiers.SPAWN.get();
    }
}

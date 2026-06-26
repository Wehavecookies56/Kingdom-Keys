package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.modifiers;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.neoforged.neoforge.event.EventHooks;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.Room;


public class SpawnMobModifier extends RoomModifierBase {

    Holder<EntityType<?>> entityType;
    CompoundTag additionalData;

    public SpawnMobModifier(ResourceLocation registryName, Holder<EntityType<?>> entityType, CompoundTag additionalData) {
        super(registryName);
        this.entityType = entityType;
        this.additionalData = additionalData;
    }

    public SpawnMobModifier(ResourceLocation registryName, Holder<EntityType<?>> entityType) {
        this(registryName, entityType, new CompoundTag());
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

    public static CompoundTag createMoogleInv(ResourceLocation inv) {
        CompoundTag tag = new CompoundTag();
        tag.putString("inv", inv.toString());
        tag.putBoolean("stationary", true);
        return tag;
    }
}

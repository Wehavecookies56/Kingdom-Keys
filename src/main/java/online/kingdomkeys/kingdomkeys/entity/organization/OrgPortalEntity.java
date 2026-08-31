package online.kingdomkeys.kingdomkeys.entity.organization;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;

public class OrgPortalEntity extends Entity implements IEntityWithComplexSpawn {

    int maxTicks = 100;
    float radius = 0.5F;

    BlockPos destinationPos;
    ResourceKey<Level> destinationDim;
    boolean shouldTeleport;

    public OrgPortalEntity(EntityType<? extends Entity> type, Level world) {
        super(type, world);
        this.blocksBuilding = true;
    }

    public OrgPortalEntity(Level world, BlockPos spawnPos, BlockPos destinationPos, ResourceKey<Level> destinationDim, boolean shouldTP) {
        super(ModEntities.TYPE_ORG_PORTAL.get(), world);
        this.setPos(spawnPos.getX() + 0.5, spawnPos.getY(), spawnPos.getZ() + 0.5);
        this.destinationPos = destinationPos;
        this.destinationDim = destinationDim;
        this.shouldTeleport = shouldTP;
    }

    @Override
    public void tick() {
        if (this.tickCount > maxTicks) {
            this.remove(RemovalReason.KILLED);
        }
        level().addParticle(ParticleTypes.DRAGON_BREATH, getX() - 1 + random.nextDouble() * 2, getY() + random.nextDouble() * 4, getZ() - 1 + random.nextDouble() * 2, 0.0D, 0.0D, 0.0D);

        if (!level().isClientSide && shouldTeleport && destinationPos != null && !destinationPos.equals(BlockPos.ZERO)) {
            for (Entity t : level().getEntities(this, getBoundingBox().inflate(radius, radius, radius))) {
                if (t instanceof OrgPortalEntity) {
                    continue;
                }

                if (!this.isAlive()) {
                    break;
                }

                teleport(t);
            }
        }

        super.tick();
    }

    private void teleport(Entity entity) {
        ServerLevel destinationLevel = level().getServer().getLevel(destinationDim);

        if (destinationLevel == null) {
            return;
        }

        double yOffset = entity.getY() - this.getY();
        Vec3 destination = new Vec3(destinationPos.getX() + 0.5, destinationPos.getY() + 1 + yOffset, destinationPos.getZ() + 0.5);

        boolean isPlayer = entity instanceof ServerPlayer;

        if (entity instanceof ServerPlayer player) {
            PlayerData playerData = PlayerData.get(player);

            //If destination is the ROD lock the player there, otherwise unlock
            if (playerData != null) {
                playerData.setRespawnROD(destinationDim.location().getPath().equals("realm_of_darkness"));
                PacketHandler.sendTo(new SCSyncPlayerData(player), player);
            }
        }

        Vec3 velocity = isPlayer ? Vec3.ZERO : entity.getDeltaMovement();

        if (entity.level().dimension().equals(destinationDim)) {
            entity.teleportTo(destination.x, destination.y, destination.z);
            entity.setDeltaMovement(velocity);
            entity.hasImpulse = !isPlayer;
        } else {
            entity.changeDimension(new DimensionTransition(destinationLevel, destination, velocity, entity.getYRot(), entity.getXRot(), pEntity -> {}));
        }
    }

    public int getMaxTicks() {
        return maxTicks;
    }

    public void setMaxTicks(int maxTicks) {
        this.maxTicks = maxTicks;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
    }

    @Override
    public void writeSpawnData(RegistryFriendlyByteBuf buffer) {
        buffer.writeBoolean(destinationPos != null);

        if (destinationPos != null) {
            buffer.writeBlockPos(destinationPos);
            buffer.writeUtf(destinationDim.location().toString(), 100);
            buffer.writeBoolean(shouldTeleport);
        }
    }

    @Override
    public void readSpawnData(RegistryFriendlyByteBuf buffer) {
        boolean hasDestination = buffer.readBoolean();

        if (hasDestination) {
            destinationPos = buffer.readBlockPos();
            destinationDim = ResourceKey.create(Registries.DIMENSION, KingdomKeys.rl(buffer.readUtf(100)));
            shouldTeleport = buffer.readBoolean();
        } else {
            destinationPos = null;
            destinationDim = null;
            shouldTeleport = false;
        }
    }

}


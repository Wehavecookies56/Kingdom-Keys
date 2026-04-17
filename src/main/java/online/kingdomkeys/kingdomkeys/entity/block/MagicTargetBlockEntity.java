package online.kingdomkeys.kingdomkeys.entity.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import online.kingdomkeys.kingdomkeys.block.MagicTargetBlock;
import online.kingdomkeys.kingdomkeys.entity.MagicTargetEntity;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

import java.util.UUID;

public class MagicTargetBlockEntity extends BlockEntity {

    private UUID entityUUID;
    private int cooldown = 0;

    public MagicTargetBlockEntity(BlockPos pos, BlockState state) {
        super(ModEntities.TYPE_MAGIC_TARGET_TE.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, MagicTargetBlockEntity be) {
        if (level.isClientSide)
            return;

        if (!(level instanceof ServerLevel server))
            return;

        if (be.entityUUID == null) {
            be.spawnEntity();
            return;
        }

        Entity entity = server.getEntity(be.entityUUID);

        if (!(entity instanceof MagicTargetEntity target)) {
            be.spawnEntity();
            return;
        }

        if (target.getLinkedBlock() == null || !target.getLinkedBlock().equals(pos)) {
            target.setLinkedBlock(pos);
        }

        if (be.cooldown > 0) {
            be.cooldown--;

            if (be.cooldown == 0) {
                level.setBlock(pos, state.setValue(MagicTargetBlock.OUTPUT_POWER, 0), 3);
            }
        }
    }

    public void spawnEntity() {
        if (!(level instanceof ServerLevel server))
            return;

        if (entityUUID != null) {
            Entity old = server.getEntity(entityUUID);
            if (old != null)
                old.discard();
        }

        MagicTargetEntity entity = new MagicTargetEntity(server);
        entity.setLinkedBlock(worldPosition);

        server.addFreshEntity(entity);

        this.entityUUID = entity.getUUID();

        updateEntityPosition();
        setChanged();
    }

    public void updateEntityPosition() {
        if (!(level instanceof ServerLevel server))
            return;
        if (entityUUID == null)
            return;

        Entity e = server.getEntity(entityUUID);
        if (!(e instanceof MagicTargetEntity entity)) return;

        BlockPos pos = worldPosition;

        Direction dir = level.getBlockState(worldPosition).getValue(MagicTargetBlock.FACING);

        double offset = 0.5 + 0.501;
        double x = pos.getX() + 0.5 + dir.getStepX() * offset;
        double y = pos.getY() + dir.getStepY() * offset;
        double z = pos.getZ() + 0.5 + dir.getStepZ() * offset;

        entity.setPos(x, y, z);
    }

    public void onMagicHit(float power) {
        if (level == null)
            return;

        int redstone = Math.min(15, (int) power);
        level.setBlock(worldPosition, getBlockState().setValue(MagicTargetBlock.OUTPUT_POWER, redstone), 3);

        cooldown = 20;
        setChanged();
    }

    public void removeEntity() {
        if (!(level instanceof ServerLevel server) || entityUUID == null) return;

        Entity e = server.getEntity(entityUUID);
        if (e != null) {
            e.discard();
        }
    }

    @Override
    public void loadAdditional(CompoundTag pTag, HolderLookup.Provider registries) {
        super.loadAdditional(pTag, registries);
        if (pTag.hasUUID("TargetEntity")) {
            entityUUID = pTag.getUUID("TargetEntity");
        }

        cooldown = pTag.getInt("Cooldown");
    }

    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider registries) {
        if (entityUUID != null) {
            pTag.putUUID("TargetEntity", entityUUID);
        }

        pTag.putInt("Cooldown", cooldown);
        super.saveAdditional(pTag, registries);
    }


}
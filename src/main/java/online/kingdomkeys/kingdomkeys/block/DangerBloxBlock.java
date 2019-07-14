package online.kingdomkeys.kingdomkeys.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class DangerBloxBlock extends BaseBlock {

    /** Smaller collision box otherwise {@link #onEntityCollision(BlockState, World, BlockPos, Entity)} doesn't trigger */
    private static final VoxelShape collisionShape = Block.makeCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 15.0D, 15.0D);
    private float damage = 3.0F;

    public DangerBloxBlock(String name, Properties properties) {
        super(name, properties);
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        return collisionShape;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (entity instanceof LivingEntity) {
            LivingEntity entityLiving = (LivingEntity) entity;
            if (ItemStack.areItemStacksEqual(entityLiving.getItemStackFromSlot(EquipmentSlotType.FEET), ItemStack.EMPTY)) {
                entity.attackEntityFrom(DamageSource.MAGIC, damage);
            }
        } else {
            entity.attackEntityFrom(DamageSource.MAGIC, damage);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onBlockClicked(BlockState state, World world, BlockPos pos, PlayerEntity player) {
        player.attackEntityFrom(DamageSource.MAGIC, damage);
    }
}

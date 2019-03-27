package online.kingdomkeys.kingdomkeys.block;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class BlockDangerBlox extends BlockBase {

    /** Smaller collision box otherwise {@link #onEntityCollision(IBlockState, World, BlockPos, Entity)} doesn't trigger */
    private static final VoxelShape collisionShape = Block.makeCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 15.0D, 15.0D);
    private float damage = 3.0F;

    public BlockDangerBlox(String name, Properties properties) {
        super(name, properties);
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getCollisionShape(IBlockState state, IBlockReader world, BlockPos pos) {
        return collisionShape;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onEntityCollision(IBlockState state, World world, BlockPos pos, Entity entity) {
        if (entity instanceof EntityLivingBase) {
            EntityLivingBase entityLiving = (EntityLivingBase) entity;
            if (ItemStack.areItemStacksEqual(entityLiving.getItemStackFromSlot(EntityEquipmentSlot.FEET), ItemStack.EMPTY)) {
                entity.attackEntityFrom(DamageSource.MAGIC, damage);
            }
        } else {
            entity.attackEntityFrom(DamageSource.MAGIC, damage);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onBlockClicked(IBlockState state, World world, BlockPos pos, EntityPlayer player) {
        player.attackEntityFrom(DamageSource.MAGIC, damage);
    }
}

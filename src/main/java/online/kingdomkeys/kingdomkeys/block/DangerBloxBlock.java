package online.kingdomkeys.kingdomkeys.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class DangerBloxBlock extends BaseBlock {

    /** Smaller collision box otherwise {@link #entityInside(BlockState, Level, BlockPos, Entity)} doesn't trigger */
    private static final VoxelShape collisionShape = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 15.0D, 15.0D);
    private float damage = 3.0F;

    public DangerBloxBlock(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return collisionShape;
    }

    @Override
    public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
        if (entity instanceof LivingEntity) {
            LivingEntity entityLiving = (LivingEntity) entity;
            if (ItemStack.matches(entityLiving.getItemBySlot(EquipmentSlot.FEET), ItemStack.EMPTY)) {
                entity.hurt(entity.damageSources().magic(), damage);
            }
        } else {
            entity.hurt(entity.damageSources().magic(), damage);
        }
    }

    @Override
    public void attack(BlockState state, Level world, BlockPos pos, Player player) {
        player.hurt(player.damageSources().magic(), damage);
    }
}

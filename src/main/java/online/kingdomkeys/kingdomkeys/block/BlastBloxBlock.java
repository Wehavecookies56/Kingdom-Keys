package online.kingdomkeys.kingdomkeys.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import online.kingdomkeys.kingdomkeys.entity.block.BlastBloxEntity;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.util.SetBlockStateFlags;

import javax.annotation.Nullable;

/**
 * Some parts copied from {@link net.minecraft.block.TNTBlock}
 */
public class BlastBloxBlock extends BaseBlock {

    /** Smaller collision box otherwise {@link #onEntityCollision(BlockState, World, BlockPos, Entity)} doesn't trigger */
    private static final VoxelShape collisionShape = Block.makeCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 15.0D, 15.0D);

    public BlastBloxBlock(Properties properties) {
        super(properties);
    }

    //Explode if powered when placed
    @SuppressWarnings("deprecation")
    @Override
    public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (oldState.getBlock() != state.getBlock()) {
            if (worldIn.isBlockPowered(pos)) {
                this.explode(worldIn, pos);
                worldIn.removeBlock(pos, false);
            }
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        //Trigger explosion with redstone power
        if (worldIn.isBlockPowered(pos)) {
            this.explode(worldIn, pos);
            worldIn.removeBlock(pos, false);
        }
    }

    //Explode when broken by the player
    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!worldIn.isRemote() && !player.isCreative() && player.getHeldItemMainhand().getItem() != Items.FEATHER) {
            this.explode(worldIn, pos);
        }
        super.onBlockHarvested(worldIn, pos, state, player);
    }

    @Override
    public void onExplosionDestroy(World worldIn, BlockPos pos, Explosion explosionIn) {
        super.onExplosionDestroy(worldIn, pos, explosionIn);
    }

    /**
     * Explode without an entity triggering it
     * @param world The world
     * @param pos The position
     */
    public void explode(World world, BlockPos pos) {
        this.explode(world, pos, (LivingEntity)null);
    }

    /**
     * Spawn the primed entity and play the sound
     * @param world The world
     * @param pos The position
     * @param igniter The entity that triggered it
     */
    private void explode(World world, BlockPos pos, @Nullable LivingEntity igniter) {
        if (!world.isRemote) {
            BlastBloxEntity entity = new BlastBloxEntity(ModEntities.TYPE_BLAST_BLOX.get(), world, (double)((float)pos.getX() + 0.5F), (double)pos.getY(), (double)((float)pos.getZ() + 0.5F), igniter);
            world.addEntity(entity);
            world.playSound((PlayerEntity) null, entity.getPosition().getX(), entity.getPosition().getY(), entity.getPosition().getZ(), SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onBlockClicked(BlockState state, World worldIn, BlockPos pos, PlayerEntity player) {
        //Explode when clicked unless the player is holding a feather in their main hand
        ItemStack held = player.getHeldItemMainhand();
        Item item = held.getItem();
        if (item != Items.FEATHER) {
            this.explode(worldIn, pos, player);
            worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), SetBlockStateFlags.BLOCK_UPDATE.getValue() | SetBlockStateFlags.SEND_TO_CLIENT.getValue() | SetBlockStateFlags.RERENDER_ON_MAIN.getValue());
        }
    }

    //Ignited by flint and steel or fire charges otherwise doesn't ignite when right clicked
    @SuppressWarnings("deprecation")
    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        ItemStack stack = player.getHeldItem(hand);
        Item item = stack.getItem();
        if (item != Items.FLINT_AND_STEEL && item != Items.FIRE_CHARGE) {
            return super.onBlockActivated(state, worldIn, pos, player, hand, hit);
        } else {
            this.explode(worldIn, pos, player);
            worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), SetBlockStateFlags.BLOCK_UPDATE.getValue() | SetBlockStateFlags.SEND_TO_CLIENT.getValue() | SetBlockStateFlags.RERENDER_ON_MAIN.getValue());
            if (item == Items.FLINT_AND_STEEL) {
                stack.damageItem(1, player, consumer -> {
                    consumer.sendBreakAnimation(hand);
                });
            } else {
                stack.shrink(1);
            }
        }
        return ActionResultType.SUCCESS;
    }

    //Explode when collided with an entity
    @SuppressWarnings("deprecation")
    @Override
    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
        if (!worldIn.isRemote) {
            this.explode(worldIn, pos, entityIn instanceof LivingEntity ? (LivingEntity) entityIn : null);
            worldIn.removeBlock(pos, false);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        return collisionShape;
    }

    //Explode when walked on by an entity
    @Override
    public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {
        this.explode(worldIn, pos, entityIn instanceof LivingEntity ? (LivingEntity) entityIn : null);
        worldIn.removeBlock(pos, false);
    }

    //Disable being dropped by an explosion
    @SuppressWarnings("deprecation")
    @Override
    public boolean canDropFromExplosion(Explosion explosionIn) {
        return false;
    }
}

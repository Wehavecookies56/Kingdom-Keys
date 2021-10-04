package online.kingdomkeys.kingdomkeys.block;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.entity.block.BlastBloxEntity;
import online.kingdomkeys.kingdomkeys.util.SetBlockStateFlags;

/**
 * Some parts copied from {@link net.minecraft.blocks}
 */
public class BlastBloxBlock extends BaseBlock {

    /** Smaller collision box otherwise {@link #onEntityCollision(BlockState, World, BlockPos, Entity)} doesn't trigger */
    private static final VoxelShape collisionShape = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 15.0D, 15.0D);

    public BlastBloxBlock(Properties properties) {
        super(properties);

    }

    //Explode if powered when placed
    @SuppressWarnings("deprecation")
    @Override
    public void onPlace(BlockState state, Level worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (oldState.getBlock() != state.getBlock()) {
            if (worldIn.hasNeighborSignal(pos)) {
                this.explode(worldIn, pos);
                worldIn.removeBlock(pos, false);
            }
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        //Trigger explosion with redstone power
        if (worldIn.hasNeighborSignal(pos)) {
            this.explode(worldIn, pos);
            worldIn.removeBlock(pos, false);
        }
    }

    //Explode when broken by the player
    @Override
    public void playerWillDestroy(Level worldIn, BlockPos pos, BlockState state, Player player) {
        if (!worldIn.isClientSide() && !player.isCreative() && player.getMainHandItem().getItem() != Items.FEATHER) {
            this.explode(worldIn, pos);
        }
        super.playerWillDestroy(worldIn, pos, state, player);
    }

    @Override
    public void wasExploded(Level worldIn, BlockPos pos, Explosion explosionIn) {
        super.wasExploded(worldIn, pos, explosionIn);
    }

    /**
     * Explode without an entity triggering it
     * @param world The world
     * @param pos The position
     */
    public void explode(Level world, BlockPos pos) {
        this.explode(world, pos, (LivingEntity)null);
    }

    /**
     * Spawn the primed entity and play the sound
     * @param world The world
     * @param pos The position
     * @param igniter The entity that triggered it
     */
    private void explode(Level world, BlockPos pos, @Nullable LivingEntity igniter) {
        if (!world.isClientSide) {
            BlastBloxEntity entity = new BlastBloxEntity(ModEntities.TYPE_BLAST_BLOX.get(), world, (double)((float)pos.getX() + 0.5F), (double)pos.getY(), (double)((float)pos.getZ() + 0.5F), igniter);
            world.addFreshEntity(entity);
            world.playSound((Player) null, entity.blockPosition().getX(), entity.blockPosition().getY(), entity.blockPosition().getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void attack(BlockState state, Level worldIn, BlockPos pos, Player player) {
        //Explode when clicked unless the player is holding a feather in their main hand
        ItemStack held = player.getMainHandItem();
        Item item = held.getItem();
        if (item != Items.FEATHER) {
            this.explode(worldIn, pos, player);
            worldIn.setBlock(pos, Blocks.AIR.defaultBlockState(), SetBlockStateFlags.BLOCK_UPDATE.getValue() | SetBlockStateFlags.SEND_TO_CLIENT.getValue() | SetBlockStateFlags.RERENDER_ON_MAIN.getValue());
        }
    }

    //Ignited by flint and steel or fire charges otherwise doesn't ignite when right clicked
    @SuppressWarnings("deprecation")
    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack stack = player.getItemInHand(hand);
        Item item = stack.getItem();
        if (item != Items.FLINT_AND_STEEL && item != Items.FIRE_CHARGE) {
            return super.use(state, worldIn, pos, player, hand, hit);
        } else {
            this.explode(worldIn, pos, player);
            worldIn.setBlock(pos, Blocks.AIR.defaultBlockState(), SetBlockStateFlags.BLOCK_UPDATE.getValue() | SetBlockStateFlags.SEND_TO_CLIENT.getValue() | SetBlockStateFlags.RERENDER_ON_MAIN.getValue());
            if (item == Items.FLINT_AND_STEEL) {
                stack.hurtAndBreak(1, player, consumer -> {
                    consumer.broadcastBreakEvent(hand);
                });
            } else {
                stack.shrink(1);
            }
        }
        return InteractionResult.SUCCESS;
    }

    //Explode when collided with an entity
    @SuppressWarnings("deprecation")
    @Override
    public void entityInside(BlockState state, Level worldIn, BlockPos pos, Entity entityIn) {
        if (!worldIn.isClientSide) {
            this.explode(worldIn, pos, entityIn instanceof LivingEntity ? (LivingEntity) entityIn : null);
            worldIn.removeBlock(pos, false);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return collisionShape;
    }

    //Explode when walked on by an entity
    @Override
    public void stepOn(Level worldIn, BlockPos pos, BlockState p_152433_, Entity entityIn) {
        this.explode(worldIn, pos, entityIn instanceof LivingEntity ? (LivingEntity) entityIn : null);
        worldIn.removeBlock(pos, false);
    }

    //Disable being dropped by an explosion
    @SuppressWarnings("deprecation")
    @Override
    public boolean dropFromExplosion(Explosion explosionIn) {
        return false;
    }
}

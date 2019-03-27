package online.kingdomkeys.kingdomkeys.block;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import online.kingdomkeys.kingdomkeys.entity.EntityBlastBloxPrimed;
import online.kingdomkeys.kingdomkeys.utils.SetBlockStateFlags;

import javax.annotation.Nullable;

/**
 * Some parts copied from {@link net.minecraft.block.BlockTNT}
 */
public class BlockBlastBlox extends BlockBase {

    /** Smaller collision box otherwise {@link #onEntityCollision(IBlockState, World, BlockPos, Entity)} doesn't trigger */
    private static final VoxelShape collisionShape = Block.makeCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 15.0D, 15.0D);

    public BlockBlastBlox(String name, Properties properties) {
        super(name, properties);
    }

    //Explode if powered when placed
    @SuppressWarnings("deprecation")
    @Override
    public void onBlockAdded(IBlockState state, World worldIn, BlockPos pos, IBlockState oldState) {
        if (oldState.getBlock() != state.getBlock()) {
            if (worldIn.isBlockPowered(pos)) {
                this.explode(worldIn, pos);
                worldIn.removeBlock(pos);
            }
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        //Trigger explosion with redstone power
        if (worldIn.isBlockPowered(pos)) {
            this.explode(worldIn, pos);
            worldIn.removeBlock(pos);
        }
    }

    //Explode when broken by the player
    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
        if (!worldIn.isRemote() && !player.isCreative()) {
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
        this.explode(world, pos, (EntityLivingBase)null);
    }

    /**
     * Spawn the primed entity and play the sound
     * @param world The world
     * @param pos The position
     * @param igniter The entity that triggered it
     */
    private void explode(World world, BlockPos pos, @Nullable EntityLivingBase igniter) {
        if (!world.isRemote) {
            EntityBlastBloxPrimed entity = new EntityBlastBloxPrimed(world, (double)((float)pos.getX() + 0.5F), (double)pos.getY(), (double)((float)pos.getZ() + 0.5F), igniter);
            world.spawnEntity(entity);
            world.playSound((EntityPlayer)null, entity.posX, entity.posY, entity.posZ, SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onBlockClicked(IBlockState state, World worldIn, BlockPos pos, EntityPlayer player) {
        //Explode when clicked unless the player is holding a feather in their main hand
        ItemStack held = player.getHeldItemMainhand();
        Item item = held.getItem();
        if (item != Items.FEATHER) {
            this.explode(worldIn, pos, player);
            //
            worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), SetBlockStateFlags.BLOCK_UPDATE.getValue() | SetBlockStateFlags.SEND_TO_CLIENT.getValue() | SetBlockStateFlags.RERENDER_ON_MAIN.getValue());
        }
    }

    //Ignited by flint and steel or fire charges otherwise doesn't ignite when right clicked
    @SuppressWarnings("deprecation")
    @Override
    public boolean onBlockActivated(IBlockState state, World worldIn, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);
        Item item = stack.getItem();
        if (item != Items.FLINT_AND_STEEL && item != Items.FIRE_CHARGE) {
            return super.onBlockActivated(state, worldIn, pos, player, hand, side, hitX, hitY, hitZ);
        } else {
            this.explode(worldIn, pos, player);
            worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), SetBlockStateFlags.BLOCK_UPDATE.getValue() | SetBlockStateFlags.SEND_TO_CLIENT.getValue() | SetBlockStateFlags.RERENDER_ON_MAIN.getValue());
            if (item == Items.FLINT_AND_STEEL) {
                stack.damageItem(1, player);
            } else {
                stack.shrink(1);
            }
        }
        return true;
    }

    //Explode when collided with an entity
    @SuppressWarnings("deprecation")
    @Override
    public void onEntityCollision(IBlockState state, World worldIn, BlockPos pos, Entity entityIn) {
        if (!worldIn.isRemote) {
            this.explode(worldIn, pos, entityIn instanceof EntityLivingBase ? (EntityLivingBase)entityIn : null);
            worldIn.removeBlock(pos);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getCollisionShape(IBlockState state, IBlockReader world, BlockPos pos) {
        return collisionShape;
    }

    //Explode when walked on by an entity
    @Override
    public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {
        this.explode(worldIn, pos, entityIn instanceof EntityLivingBase ? (EntityLivingBase)entityIn : null);
        worldIn.removeBlock(pos);
    }

    //Disable being dropped by an explosion
    @Override
    public boolean canDropFromExplosion(Explosion explosionIn) {
        return false;
    }
}

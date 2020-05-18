package online.kingdomkeys.kingdomkeys.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Block.Properties;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper.Dir;
import online.kingdomkeys.kingdomkeys.entity.block.PairBloxEntity;

import javax.annotation.Nullable;

public class PairBloxBlock extends BaseBlock {

    public static final BooleanProperty PAIR = BooleanProperty.create("pair");

    public PairBloxBlock(Properties properties) {
        super(properties);
        this.setDefaultState(this.getDefaultState().with(PAIR, true));
    }
    
    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(PAIR);
    }
    
    @Override
    public void onBlockClicked(BlockState state, World worldIn, BlockPos pos, PlayerEntity player) {
    	worldIn.setBlockState(pos, Blocks.AIR.getDefaultState());
    	PairBloxEntity pairEntity = new PairBloxEntity(worldIn, pos.getX(), pos.getY(), pos.getZ(), true);
    	float velocity = 0.5F;
		switch (MathHelper.floor(player.rotationYaw * 4.0F / 360.0F + 0.5D) & 3) {
		case 0:
	    	pairEntity.setMotion(0, -velocity, velocity);
	    	break;
		case 1:
			pairEntity.setMotion(-velocity, -velocity, 0);
	    	break;
		case 2:
			pairEntity.setMotion(0, -velocity, -velocity);
	    	break;
		case 3:
			pairEntity.setMotion(velocity, -velocity, 0);
	    	break;
    		
    	}
    	worldIn.addEntity(pairEntity);
    	super.onBlockClicked(state, worldIn, pos, player);
    } 

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean isMoving) {

    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {

    }

   /* @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        worldIn.setBlockState(pos, state.with(ACTIVE, worldIn.isBlockPowered(pos)));
    }*/

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(PAIR, true);
    }

    @Override
    public boolean shouldCheckWeakPower(BlockState state, IWorldReader world, BlockPos pos, Direction side) {
        return true;
    }

   /* @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (player.isCrouching()) {
            worldIn.setBlockState(pos, state.with(ATTRACT, !state.get(ATTRACT)));
            String message = state.get(ATTRACT) ? "message.magnet_blox.repel" : "message.magnet_blox.attract";
            message = I18n.format(message);
            TextFormatting formatting = state.get(ATTRACT) ? TextFormatting.BLUE : TextFormatting.RED;
            message = formatting + message;
            player.sendStatusMessage(new StringTextComponent(message), true);
        } else {
            int newRange = state.get(RANGE) + 1;
            if (state.get(RANGE) == max) {
                newRange = min;
            }
            worldIn.setBlockState(pos, state.with(RANGE, newRange));
            //TODO translate
            player.sendStatusMessage(new TranslationTextComponent("message.magnet_blox.range", newRange), true);
        }
        return ActionResultType.SUCCESS;
    }*/

   /* @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return ModEntities.TYPE_MAGNET_BLOX.get().create();
    }*/

}

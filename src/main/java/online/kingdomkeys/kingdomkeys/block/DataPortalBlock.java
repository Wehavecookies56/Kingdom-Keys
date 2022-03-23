package online.kingdomkeys.kingdomkeys.block;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import online.kingdomkeys.kingdomkeys.command.KKDimensionCommand;
import online.kingdomkeys.kingdomkeys.entity.mob.MarluxiaEntity;
import online.kingdomkeys.kingdomkeys.world.dimension.ModDimensions;
import online.kingdomkeys.kingdomkeys.world.utils.BaseTeleporter;

public class DataPortalBlock extends BaseBlock {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;

	private static final VoxelShape collisionShapeEW = Block.makeCuboidShape(5.0D, 0.0D, -8.0D, 11.0D, 32.0D, 24.0D);
	private static final VoxelShape collisionShapeNS = Block.makeCuboidShape(-8.0D, 0.0D, 5.0D, 24.0D, 32.0D, 11.0D);

	public DataPortalBlock(Properties properties) {
		super(properties);
		this.setDefaultState(this.getDefaultState().with(FACING, Direction.NORTH));
	}
	
	@Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
    }
	
	@Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        //builder.add(FACING, BIG);
        builder.add(FACING);
    }

	@Override
	public VoxelShape getCollisionShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
		return getShape(state,world,pos,context);
	}
	
	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		if(!worldIn.isRemote) {
			RegistryKey<World> dimension = ModDimensions.STATION_OF_SORROW;
			BlockPos coords = KKDimensionCommand.getWorldCoords(player, dimension);
			player.changeDimension(player.getServer().getWorld(dimension), new BaseTeleporter(coords.getX(), coords.getY(), coords.getZ()));
			player.sendMessage(new TranslationTextComponent("You have been teleported to " + dimension.getLocation().toString()), Util.DUMMY_UUID);
			MarluxiaEntity marluxia = new MarluxiaEntity(player.world);
			marluxia.onInitialSpawn((ServerWorld)player.world, player.world.getDifficultyForLocation(marluxia.getPosition()), SpawnReason.COMMAND, (ILivingEntityData)null, (CompoundNBT)null);
			player.world.addEntity(marluxia);
			marluxia.setPosition(player.getPosX(), player.getPosY(), player.getPosZ() - 6);
		}
		return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
	}
	
	@Deprecated
	   public BlockRenderType getRenderType(BlockState state) {
	      return BlockRenderType.MODEL;
	}
	
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
    	if(state.get(FACING) == Direction.NORTH || state.get(FACING) == Direction.SOUTH) {
			return collisionShapeNS;
		} else {
			return collisionShapeEW;
		}
    }
}

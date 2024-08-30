package online.kingdomkeys.kingdomkeys.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import online.kingdomkeys.kingdomkeys.data.ModData;
import online.kingdomkeys.kingdomkeys.command.DimensionCommand;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.entity.mob.MarluxiaEntity;
import online.kingdomkeys.kingdomkeys.world.dimension.ModDimensions;

import javax.annotation.Nullable;

public class DataPortalBlock extends BaseBlock implements INoDataGen {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;

	private static final VoxelShape collisionShapeEW = Block.box(0.0D, 0.0D, -5.0D, 16.0D, 48.0D, 21.0D);
	private static final VoxelShape collisionShapeNS = Block.box(-5.0D, 0.0D, 0.0D, 21.0D, 48.0D, 16.0D);

	public DataPortalBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH));
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(FACING);
	}

	@Override
	public VoxelShape getCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
		return getShape(pState, pLevel, pPos, pContext);
	}

	@Override
	public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
		PlayerData playerData = PlayerData.get(player);
		if(playerData != null) {
			playerData.setReturnDimension(player);
			playerData.setReturnLocation(player);
		}
		if(!worldIn.isClientSide) {
			ResourceKey<Level> dimension = ModDimensions.STATION_OF_SORROW;
			BlockPos coords = DimensionCommand.getWorldCoords(player, dimension);
			player.changeDimension(new DimensionTransition(player.getServer().getLevel(dimension), new Vec3(coords.getX(), coords.getY(), coords.getZ()), Vec3.ZERO, player.getYRot(), player.getXRot(), (entity) -> {}));
			player.sendSystemMessage(Component.translatable("You have been teleported to " + dimension.location()));
			MarluxiaEntity marluxia = new MarluxiaEntity(player.level());
			marluxia.finalizeSpawn((ServerLevel)player.level(), player.level().getCurrentDifficultyAt(marluxia.blockPosition()), MobSpawnType.COMMAND, null);
			player.level().addFreshEntity(marluxia);
			marluxia.setPos(player.getX(), player.getY(), player.getZ() - 6);
		}
		return super.useItemOn(stack, state, worldIn, pos, player, handIn, hit);
	}

	@Override
	public RenderShape getRenderShape(BlockState pState) {
		return RenderShape.MODEL;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
		if(state.getValue(FACING) == Direction.NORTH || state.getValue(FACING) == Direction.SOUTH) {
			return collisionShapeNS;
		} else {
			return collisionShapeEW;
		}
	}
}

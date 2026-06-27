package online.kingdomkeys.kingdomkeys.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
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
import online.kingdomkeys.kingdomkeys.data.CastleOblivionData;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.item.card.WorldCardItem;
import online.kingdomkeys.kingdomkeys.world.dimension.ModDimensions;

import javax.annotation.Nullable;

public class SoADoorBlock extends BaseBlock implements INoDataGen{
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

	private static final VoxelShape collisionShapeEW = Block.box(5.0D, 0.0D, -8.0D, 11.0D, 32.0D, 24.0D);
	private static final VoxelShape collisionShapeNS = Block.box(-8.0D, 0.0D, 5.0D, 24.0D, 32.0D, 11.0D);

	public SoADoorBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH));
	}


	@Override
	protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		if(!level.isClientSide && hand == InteractionHand.MAIN_HAND) {
			boolean hasAccess = false;
			if (!stack.isEmpty() && stack.getItem() instanceof WorldCardItem) {
				hasAccess = true;
			} else {
				CastleOblivionData.ExteriorData coData = CastleOblivionData.ExteriorData.get(level.getServer());
				if(coData.getInterior(player.getUUID()) != null){
					hasAccess = true;
				}
			}

			if(hasAccess) {
				ServerPlayer sPlayer = (ServerPlayer) player;
				player.displayClientMessage(Component.translatable("co.door_succeed"),true);

				ResourceKey<Level> resourcekey = ModDimensions.CASTLE_OBLIVION;
				ServerLevel serverlevel = level.getServer().getLevel(resourcekey);
				if (serverlevel != null) {
					sPlayer.changeDimension(new DimensionTransition(serverlevel, new Vec3(-2, 88, -167), Vec3.ZERO, 0,0, entity -> {
						entity.setXRot(0);
						entity.setYRot(0);
					}));
				}
			} else {
				player.displayClientMessage(Component.translatable("co.door_failed"),true);
			}
		}
		return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
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
	public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return getShape(state,world,pos,context);
	}

	@Override
	public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
		super.animateTick(state, world, pos, random);
	}
	
	@Deprecated
	public RenderShape getRenderShape(BlockState state) {
	      return RenderShape.MODEL;
	}
	
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
    	if(state.getValue(FACING) == Direction.NORTH || state.getValue(FACING) == Direction.SOUTH) {
			return collisionShapeNS;
		} else {
			return collisionShapeEW;
		}
    }
}

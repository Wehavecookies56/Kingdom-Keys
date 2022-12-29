package online.kingdomkeys.kingdomkeys.block;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import online.kingdomkeys.kingdomkeys.capability.CastleOblivionCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.entity.block.CardDoorTileEntity;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCOpenCODoorGui;
import online.kingdomkeys.kingdomkeys.network.stc.SCOpenSynthesisGui;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.Room;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.RoomData;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.RoomUtils.RoomPos;

public class CardDoorBlock extends BaseBlock implements EntityBlock {

	private static final VoxelShape collisionShapeE = Block.box(0.0D, 0.0D, -8.0D, 6.0D, 32.0D, 24.0D);
	private static final VoxelShape collisionShapeN = Block.box(-8.0D, 0.0D, 10.0D, 24.0D, 32.0D, 16.0D);
	private static final VoxelShape collisionShapeW = Block.box(10.0D, 0.0D, -8.0D, 16.0D, 32.0D, 24.0D);
	private static final VoxelShape collisionShapeS = Block.box(-8.0D, 0.0D, 0.0D, 24.0D, 32.0D, 6.0D);

	public static final DirectionProperty FACING = BlockStateProperties.FACING;
	// If block was placed by player: false
	public static final BooleanProperty GENERATED = BooleanProperty.create("generated");
	// false = room; true = world
	public static final BooleanProperty TYPE = BooleanProperty.create("type");

	public static final BooleanProperty OPEN = BooleanProperty.create("open");

	public CardDoorBlock(Properties properties) {
		super(properties);
		registerDefaultState(this.defaultBlockState().setValue(GENERATED, false).setValue(OPEN, false));
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite()).setValue(GENERATED, false);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(FACING);
		builder.add(GENERATED);
		builder.add(TYPE);
		builder.add(OPEN);
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
    	//state.setValue(OPEN, true);
    	//System.out.println("OPEN? "+state.getValue(OPEN));
		if (!level.isClientSide) {

			if (state.getValue(GENERATED)) {
				if (state.getValue(TYPE)) {
					// world card selection gui
					// set world for floor
					// create first room from lobby
					// transport into room
				} else {
					CastleOblivionCapabilities.ICastleOblivionInteriorCapability cap = ModCapabilities.getCastleOblivionInterior(level);
					if (cap != null) {
						CardDoorTileEntity te = (CardDoorTileEntity) level.getBlockEntity(pos);
						if (te != null) {
							System.out.println("Size: "+te.getParentRoom().getParentFloor(level).getRooms().size());
							System.out.println((level.isClientSide ? "Client" : "Server") + ": Num:" + te.getNumber() + " Open? " + te.isOpen());
							PacketHandler.sendTo(new SCOpenCODoorGui(te.getBlockPos()), (ServerPlayer) player);
						}
					}
				}
			} else {
				// open card gui
				// set card?
				// transport to door with same card?
				// maybe just link 2 doors together somehow
			}
		}
		return super.use(state, level, pos, player, hand, hit);
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
		return ModEntities.TYPE_CARD_DOOR.get().create(pPos, pState);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
		if (state.getValue(GENERATED)) {
			if (entity instanceof Player player) {
				CastleOblivionCapabilities.ICastleOblivionInteriorCapability cap = ModCapabilities.getCastleOblivionInterior(level);
				if (cap != null) {
					CardDoorTileEntity te = (CardDoorTileEntity) level.getBlockEntity(pos);
					if (te != null) {
						System.out.println((level.isClientSide ? "Client" : "Server") + ": Num:" + te.getNumber() + " Open? " + te.isOpen());
						if (te.isOpen()) { // If it's closed always open gui
							// TELEPORT PLAYER
							RoomData data = te.getParentRoom().getParentFloor(level).getAdjacentRoom(te.getParentRoom(), te.getDirection().opposite()).getFirst();
							Room newRoom = data.getGenerated();
							if (newRoom != null) {
								BlockPos destination = newRoom.doorPositions.get(te.getDirection().opposite());
								destination = destination.offset(te.getDirection().opposite().toMCDirection().getNormal().multiply(2));
								player.teleportTo(destination.getX(), destination.getY(), destination.getZ());
							}
						}
					}
				}
			}
		}
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return getShape(state, world, pos, context);
	}

	@Deprecated
	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		switch(state.getValue(FACING)) {
		case NORTH:
			return collisionShapeN;
		case EAST:
			return collisionShapeE;
		case SOUTH:
			return collisionShapeS;
		case WEST:
			return collisionShapeW;
		default:
			return collisionShapeS;
		}
	}
}

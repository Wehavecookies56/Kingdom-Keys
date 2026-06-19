package online.kingdomkeys.kingdomkeys.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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
import net.neoforged.neoforge.common.NeoForge;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.api.event.CastleOblivionEvent;
import online.kingdomkeys.kingdomkeys.data.CastleOblivionData;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.entity.block.CardDoorTileEntity;
import online.kingdomkeys.kingdomkeys.item.card.CardCategory;
import online.kingdomkeys.kingdomkeys.item.card.WorldCardItem;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCOpenCODoorGui;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCastleOblivionInteriorData;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.CastleOblivionHandler;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.floor.Floor;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.*;

import javax.annotation.Nullable;
import java.util.EnumMap;
import java.util.HashSet;

public class CardDoorBlock extends BaseBlock implements EntityBlock, INoDataGen {

	private static final VoxelShape collisionShapeE = Block.box(0.0D, 0.0D, -8.0D, 6.0D, 32.0D, 24.0D);
	private static final VoxelShape collisionShapeN = Block.box(-8.0D, 0.0D, 10.0D, 24.0D, 32.0D, 16.0D);
	private static final VoxelShape collisionShapeW = Block.box(10.0D, 0.0D, -8.0D, 16.0D, 32.0D, 24.0D);
	private static final VoxelShape collisionShapeS = Block.box(-8.0D, 0.0D, 0.0D, 24.0D, 32.0D, 6.0D);

	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	// If block was placed by player: false
	public static final BooleanProperty GENERATED = BooleanProperty.create("generated");

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
		builder.add(OPEN);
	}

	@Override
	protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
    	//state.setValue(OPEN, true);
		if (!level.isClientSide) {
			if (state.getValue(GENERATED)) {
				return CastleOblivionData.InteriorData.get((ServerLevel) level).map(interiorData -> {
					CardDoorTileEntity te = (CardDoorTileEntity) level.getBlockEntity(pos);
					if (te != null && te.getParentRoom() != null && !te.isLocked()) {
						//TODO check type
						// world card selection gui
						// set world for floor
						// create first room from entrance
						// transport into room
						if (te.getCurrentCriteria() == null || te.getCurrentCriteria().isEmpty()) {
							te.setCurrentCriteria(te.getData().getCardCriteria());
						}
						switch (te.getData().getType()) {
							case NORMAL -> {
								if (te.getDestinationRoom() != null) {
									KingdomKeys.LOGGER.debug("Size: {}", te.getParentRoom().getParentFloor((ServerLevel) level).getRooms().size());
									KingdomKeys.LOGGER.debug("{}: Num:{} Open? {}", level.isClientSide ? "Client" : "Server", te.getData(), te.isOpen());
									PacketHandler.sendTo(new SCOpenCODoorGui(te.getBlockPos()), (ServerPlayer) player);
								}
							}
							case FIXED -> {
								if (!te.isOpen()) {
									//FIXED not open means GUI should open to use cards to unlock
								}
								//if open do nothing because door is to fixed room so it cannot be changed
							}
							case HALL -> {
								if (!te.isOpen()) {
									//TODO open world card gui
									if (player.getItemInHand(hand).getItem() instanceof WorldCardItem item) {
										te.getParentRoom().getParentFloor((ServerLevel) level).setWorldCard(item);
										te.openDoor(true);
										CastleOblivionHandler.createFirstRoom(player, te);
									}
								}
								//if open do nothing because world card has been set
							}
							case KEY -> {
								//Key door requires key card + others, once open it cannot be interacted with
								if (te.getDestinationRoom() != null) {
									PacketHandler.sendTo(new SCOpenCODoorGui(te.getBlockPos()), (ServerPlayer) player);
								}
							}
						}
						return ItemInteractionResult.sidedSuccess(false);
					}
					return ItemInteractionResult.FAIL;
				}).orElseGet(() -> {
					// open card gui
					// set card?
					// transport to door with same card?
					// maybe just link 2 doors together somehow
					return ItemInteractionResult.sidedSuccess(false);
				});
			}
		}
		return ItemInteractionResult.sidedSuccess(level.isClientSide);
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
		return ModEntities.TYPE_CARD_DOOR.get().create(pPos, pState);
	}

	@Override
	public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
		if(!level.isClientSide) {
			if (state.getValue(GENERATED)) {
				if (entity instanceof Player player) {
					CastleOblivionData.InteriorData.get((ServerLevel) level).ifPresent(interiorData -> {
						CardDoorTileEntity te = (CardDoorTileEntity) level.getBlockEntity(pos);
						if (te != null && te.getParentRoom() != null && te.isOpen() && !te.isLocked()) {
							if (te.getDestinationRoom() != null) {
								RoomData data = te.getDestinationRoom();
								data.getGenerated().ifPresent(newRoom -> {
									KingdomKeys.LOGGER.debug(te.getDestinationRoom().getGenerated().get().getType().getTranslationKey());
									if (!NeoForge.EVENT_BUS.post(new CastleOblivionEvent.PlayerChangeRoomEvent(interiorData.getRoomAtPos(te.getBlockPos()), newRoom, player)).isCanceled()) {
										BlockPos destination = newRoom.doors.get(te.getDirection().opposite()).pos();
										destination = destination.offset(te.getDirection().toMCDirection().getNormal().multiply(2));
										player.teleportTo(destination.getX(), destination.getY(), destination.getZ());
										//player.moveTo(destination.getX(), destination.getY(), destination.getZ());
										PacketHandler.sendTo(new SCSyncCastleOblivionInteriorData(interiorData, level), (ServerPlayer) player);
									}
								});
							} else if (te.getData().getType() != DoorData.Type.EXIT && te.getData().getType() != DoorData.Type.ENTRANCE) {
								KingdomKeys.LOGGER.error("Door [{}] missing destination room of type {}, something has gone wrong", te.getBlockPos().toShortString(), te.getData().getType());
							} else {
								Floor currFloor = interiorData.getFloorByID(te.getParentRoom().getParentID());
								if (te.getData().getType() == DoorData.Type.ENTRANCE) {
									if (te.getParentRoom().getParentID() == 0) {
										//on first floor so exit
										CastleOblivionHandler.exitCastleOblivion(currFloor, te.getParentRoom().getGenerated().get(), player);
										//currFloor.floorExited(player);
									} else {
										//not on first floor so go to previous floor
										Floor prevFloor = interiorData.getFloorByID(te.getParentRoom().getParentID()-1);
										prevFloor.getExitRoom().getGenerated().ifPresent(destRoom -> {
											BlockPos destination = destRoom.getExitDoor();
											destination = destination.offset(level.getBlockState(destination).getValue(FACING).getNormal().multiply(2));
											player.teleportTo(destination.getX(), destination.getY(), destination.getZ());
											NeoForge.EVENT_BUS.post(new CastleOblivionEvent.PlayerChangeRoomEvent(te.getParentRoom().getGenerated().get(), destRoom, player));
											NeoForge.EVENT_BUS.post(new CastleOblivionEvent.PlayerChangeFloorEvent(currFloor, prevFloor, te.getParentRoom().getGenerated().get(), destRoom, player));
											//currFloor.floorExited(player);
											//prevFloor.floorEntered(player);
											PacketHandler.sendTo(new SCSyncCastleOblivionInteriorData(interiorData, level), (ServerPlayer) player);
										});
									}
								} else if (te.getData().getType() == DoorData.Type.EXIT) {
									Room destRoom;
									Floor nextFloor;
									//create new floor if at top floor
									if (interiorData.getFloors().size() == currFloor.getFloorID()+1) {
										destRoom = RoomGenerator.INSTANCE.generateNewFloor((ServerLevel) level);
										nextFloor = interiorData.getFloorByID(destRoom.parentFloor);
									} else {
										nextFloor = interiorData.getFloorByID(te.getParentRoom().getParentID()+1);
										destRoom = nextFloor.getRoom(RoomPos.ZERO).getGenerated().get();
									}
									BlockPos entranceDoor = destRoom.doors.get(RoomDirection.SOUTH).pos();
									entranceDoor = entranceDoor.offset(level.getBlockState(entranceDoor).getValue(FACING).getNormal().multiply(2));
									if(player.level() instanceof ServerLevel sl) {
										player.teleportTo(sl, entranceDoor.getX(), entranceDoor.getY(), entranceDoor.getZ(), new HashSet<>(), 0, 0);
									}

									NeoForge.EVENT_BUS.post(new CastleOblivionEvent.PlayerChangeRoomEvent(te.getParentRoom().getGenerated().get(), destRoom, player));
									NeoForge.EVENT_BUS.post(new CastleOblivionEvent.PlayerChangeFloorEvent(currFloor, nextFloor, te.getParentRoom().getGenerated().get(), destRoom, player));
									//currFloor.floorExited(player);
									//nextFloor.floorEntered(player);
									PacketHandler.sendTo(new SCSyncCastleOblivionInteriorData(interiorData, level), (ServerPlayer) player);
								}
							}
						}
					});
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

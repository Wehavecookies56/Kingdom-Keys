package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.data.CastleOblivionData;
import online.kingdomkeys.kingdomkeys.entity.block.CardDoorTileEntity;
import online.kingdomkeys.kingdomkeys.item.card.CardCategory;
import online.kingdomkeys.kingdomkeys.item.card.MapCardItem;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCastleOblivionInteriorData;
import online.kingdomkeys.kingdomkeys.network.stc.SCUpdateCORooms;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.DoorData;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.Room;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.RoomGenerator;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.RoomType;

import java.util.EnumMap;
import java.util.Optional;

public record CSGenerateRoom(ItemStack stack, BlockPos pos) implements Packet {

	public static final Type<CSGenerateRoom> TYPE = new Type<>(KingdomKeys.rl("cs_generate_room"));

	public static final StreamCodec<RegistryFriendlyByteBuf, CSGenerateRoom> STREAM_CODEC = StreamCodec.composite(
			ItemStack.STREAM_CODEC,
			CSGenerateRoom::stack,
			BlockPos.STREAM_CODEC,
			CSGenerateRoom::pos,
			CSGenerateRoom::new
	);

	@Override
	public void handle(IPayloadContext context) {
		Player player = context.player();
		Level level = player.level();

		CastleOblivionData.InteriorData.get((ServerLevel) level).ifPresent(interiorData -> {
			CardDoorTileEntity te = (CardDoorTileEntity) player.level().getBlockEntity(pos);
			if (te != null) {
				te.getParentRoom().getParentFloor((ServerLevel) level).getAdjacentRoom(te.getParentRoom(), te.getDirection()).ifPresent(data -> {
					if (!stack.isEmpty() || data.getFixedType().isPresent()) {
						RoomType type = ((MapCardItem) stack.getItem()).getRoomType();
						if (data.getFixedType().isPresent()) {
							type = data.getFixedType().get();
						}
						Room currentRoom = interiorData.getRoomAtPos(pos);
						int cardValue = MapCardItem.getCardValue(stack);
						int currentValue = currentRoom.getValueUsed();
						Optional<EnumMap<CardCategory, DoorData.CardCriteria>> oldCriteria = data.getGenerated().map(oldRoom -> {
							BlockPos oldDoorPos = data.getGenerated().get().doors.get(te.getDirection().opposite()).pos();
							CardDoorTileEntity oldTE = (CardDoorTileEntity) level.getBlockEntity(oldDoorPos);
							if (oldTE != null) {
								return oldTE.getData().getCardCriteria();
							}
                            return null;
                        });
						Room newRoom = RoomGenerator.INSTANCE.generateRoom((ServerLevel) level, data, type, currentRoom, te.getDirection(), cardValue);
						if (newRoom != null) {
							BlockPos destination = newRoom.doors.get(te.getDirection().opposite()).pos();
							CardDoorTileEntity destTe = (CardDoorTileEntity) level.getBlockEntity(destination);
							te.openDoor(true);
							te.getDestinationRoom().setGenerated(newRoom);
							currentRoom.setValueUsed(cardValue);
							destTe.openDoor(true);
							destTe.setDestinationRoom(te.getParentRoom());
							if (te.getData().getType() == DoorData.Type.NORMAL) {
								te.getData().generateCardCriteria(cardValue);
								oldCriteria.ifPresentOrElse(criteria -> {
									destTe.getData().generateCardCriteria(criteria);
								}, () -> {
									destTe.getData().generateCardCriteria(currentValue);
								});
								destTe.setCurrentCriteria(destTe.getData().getCardCriteria());
							}

							interiorData.setDirty();
							level.sendBlockUpdated(pos, level.getBlockState(pos), level.getBlockState(pos), Block.UPDATE_CLIENTS);
							level.sendBlockUpdated(destination, level.getBlockState(destination), level.getBlockState(destination), Block.UPDATE_CLIENTS);

							PacketHandler.sendTo(new SCSyncCastleOblivionInteriorData(interiorData, level), (ServerPlayer) player);
							PacketHandler.sendTo(new SCUpdateCORooms(interiorData.getFloorByID(currentRoom.parentFloor).getRooms()), (ServerPlayer) player);
						}
						//player.teleportTo(destination.getX(), destination.getY(), destination.getZ());
					}
				});

			}
		});
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}

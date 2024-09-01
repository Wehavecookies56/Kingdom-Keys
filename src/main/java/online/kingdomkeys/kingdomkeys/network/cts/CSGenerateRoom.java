package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.Map;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.data.CastleOblivionData;
import online.kingdomkeys.kingdomkeys.data.ModData;
import online.kingdomkeys.kingdomkeys.entity.block.CardDoorTileEntity;
import online.kingdomkeys.kingdomkeys.item.card.MapCardItem;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.*;

public record CSGenerateRoom(ItemStack stack, int slot, BlockPos pos) implements Packet {

	public static final Type<CSGenerateRoom> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "cs_generate_room"));

	public static final StreamCodec<RegistryFriendlyByteBuf, CSGenerateRoom> STREAM_CODEC = StreamCodec.composite(
			ItemStack.STREAM_CODEC,
			CSGenerateRoom::stack,
			ByteBufCodecs.INT,
			CSGenerateRoom::slot,
			BlockPos.STREAM_CODEC,
			CSGenerateRoom::pos,
			CSGenerateRoom::new
	);

	@Override
	public void handle(IPayloadContext context) {
		Player player = context.player();
		Level level = player.level();

		CastleOblivionData.InteriorData cap = CastleOblivionData.InteriorData.get((ServerLevel) level);
		CardDoorTileEntity te = (CardDoorTileEntity) player.level().getBlockEntity(pos);

		RoomType type = ((MapCardItem)stack.getItem()).getRoomType();
		Room currentRoom = cap.getRoomAtPos(player.level(), pos);
		RoomData data = te.getParentRoom().getParentFloor(level).getAdjacentRoom(te.getParentRoom(), te.getDirection().opposite()).getFirst();
		Room newRoom = RoomGenerator.INSTANCE.generateRoom(data, type, player, currentRoom, te.getDirection().opposite(), false);
		for (Map.Entry<RoomUtils.Direction, BlockPos> doors : newRoom.doorPositions.entrySet()) {
			CardDoorTileEntity doorTE = (CardDoorTileEntity) level.getBlockEntity(doors.getValue());
			if (doorTE != null) {
				if (doorTE.getParentRoom() != null) {
					if (doorTE.getParentRoom().getDoor(doors.getKey().opposite()) != null) {
						if (doorTE.getParentRoom().getDoor(doors.getKey().opposite()).isOpen()) {
							doorTE.openDoor(null);
						}
					}
				}
			}
		}
		BlockPos destination = newRoom.doorPositions.get(te.getDirection().opposite());
		CardDoorTileEntity destTe = (CardDoorTileEntity) level.getBlockEntity(destination);
		te.openDoor(te.getDirection());
		te.getParentRoom().getDoor(te.getDirection().opposite()).open();
		// System.out.println(te.getNumber());
		destTe.openDoor(te.getDirection().opposite());
		destTe.setDestinationRoom(te.getParentRoom());
		// System.out.println(destTe.getNumber());

		player.getInventory().getItem(slot).shrink(1);

		//player.teleportTo(destination.getX(), destination.getY(), destination.getZ());
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}

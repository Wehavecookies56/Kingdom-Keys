package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.data.CastleOblivionData;
import online.kingdomkeys.kingdomkeys.entity.block.CardDoorTileEntity;
import online.kingdomkeys.kingdomkeys.item.card.MapCardItem;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCastleOblivionInteriorData;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.*;

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
		Room currentRoom = cap.getRoomAtPos(pos);
		RoomData data = te.getParentRoom().getParentFloor((ServerLevel) level).getAdjacentRoom(te.getParentRoom(), te.getDirection());
		Room newRoom = RoomGenerator.INSTANCE.generateRoom((ServerLevel) level, data, type, currentRoom, te.getDirection());
		BlockPos destination = newRoom.doors.get(te.getDirection().opposite()).pos();
		CardDoorTileEntity destTe = (CardDoorTileEntity) level.getBlockEntity(destination);
		te.openDoor(true);
		destTe.openDoor(true);
		destTe.setDestinationRoom(te.getParentRoom());
		player.getInventory().getItem(slot).shrink(1);

		PacketHandler.sendTo(new SCSyncCastleOblivionInteriorData(cap, level), (ServerPlayer) player);

		//player.teleportTo(destination.getX(), destination.getY(), destination.getZ());
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}

package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.Map;
import java.util.function.Supplier;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.data.CastleOblivionData;
import online.kingdomkeys.kingdomkeys.data.ModData;
import online.kingdomkeys.kingdomkeys.entity.block.CardDoorTileEntity;
import online.kingdomkeys.kingdomkeys.item.card.MapCardItem;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.*;

public class CSGenerateRoom {
	
	ItemStack stack;
	int slot;
	BlockPos pos;
	
	public CSGenerateRoom() {}
	
	public CSGenerateRoom(ItemStack stack, int slot, BlockPos pos) {
		this.stack = stack;
		this.slot = slot;
		this.pos = pos;
	}
	
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeItem(stack);
		buffer.writeInt(slot);
		buffer.writeBlockPos(pos);
	}

	public static CSGenerateRoom decode(FriendlyByteBuf buffer) {
		CSGenerateRoom msg = new CSGenerateRoom();
		msg.stack = buffer.readItem();
		msg.slot = buffer.readInt();
		msg.pos = buffer.readBlockPos();
		return msg;
	}

	public static void handle(CSGenerateRoom message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			Player player = ctx.get().getSender();
			Level level = player.level();

            CastleOblivionData.ICastleOblivionInteriorCapability cap = ModData.getCastleOblivionInterior(level);
            CardDoorTileEntity te = (CardDoorTileEntity) player.level().getBlockEntity(message.pos);
			
			RoomType type = ((MapCardItem)message.stack.getItem()).getRoomType();
			Room currentRoom = cap.getRoomAtPos(player.level(), message.pos);
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
            
            player.getInventory().getItem(message.slot).shrink(1);

		//	player.teleportTo(destination.getX(), destination.getY(), destination.getZ());
		});
		ctx.get().setPacketHandled(true);
	}

}

package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.function.Supplier;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.capability.CastleOblivionCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.entity.block.CardDoorTileEntity;
import online.kingdomkeys.kingdomkeys.item.card.MapCardItem;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.Room;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.RoomData;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.RoomGenerator;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.RoomType;

public class CSGenerateRoom {
	
	ItemStack stack;
	BlockPos pos;
	
	public CSGenerateRoom() {}
	
	public CSGenerateRoom(Item item, BlockPos pos) {
		this.stack = new ItemStack(item);
		this.pos = pos;
	}
	
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeItem(stack);
		buffer.writeBlockPos(pos);
	}

	public static CSGenerateRoom decode(FriendlyByteBuf buffer) {
		CSGenerateRoom msg = new CSGenerateRoom();
		msg.stack = buffer.readItem();
		msg.pos = buffer.readBlockPos();
		return msg;
	}

	public static void handle(CSGenerateRoom message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			Player player = ctx.get().getSender();
			Level level = player.level;
            CastleOblivionCapabilities.ICastleOblivionInteriorCapability cap = ModCapabilities.getCastleOblivionInterior(level);
            MapCardItem card = (MapCardItem)message.stack.getItem();
            CardDoorTileEntity te = (CardDoorTileEntity) player.level.getBlockEntity(message.pos);
			
			RoomType type = card.getRoomType();
			Room currentRoom = cap.getRoomAtPos(message.pos);
			RoomData data = te.getParentRoom().getParentFloor(level).getAdjacentRoom(te.getParentRoom(), te.getDirection().opposite()).getFirst();
			// generate should go on the GUI packet
			Room newRoom = RoomGenerator.INSTANCE.generateRoom(data, type, player, currentRoom, te.getDirection().opposite(), false);
			BlockPos destination = newRoom.doorPositions.get(te.getDirection().opposite());
            CardDoorTileEntity destTe = (CardDoorTileEntity) level.getBlockEntity(destination);
            System.out.println(destTe.getNumber());
            destTe.openDoor(null, currentRoom, null);
			player.teleportTo(destination.getX(), destination.getY(), destination.getZ());
		});
		ctx.get().setPacketHandled(true);
	}

}

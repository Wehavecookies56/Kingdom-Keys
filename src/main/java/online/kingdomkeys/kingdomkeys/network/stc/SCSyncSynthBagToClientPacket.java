package online.kingdomkeys.kingdomkeys.network.stc;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

public class SCSyncSynthBagToClientPacket {

	List<ItemStack> itemList = new ArrayList<ItemStack>(56);

	public SCSyncSynthBagToClientPacket() {
	}

	public SCSyncSynthBagToClientPacket(IItemHandler itemHandler) {
		System.out.println(itemHandler.getSlots());
		for (int i = 0; i < itemHandler.getSlots(); i++) {
			// System.out.println(i);
			ItemStack item = itemHandler.getStackInSlot(i);
			// System.out.println(item);
			itemList.add(i, item);
		}
	}

	public void encode(PacketBuffer buffer) {
		int size = itemList.size();
		buffer.writeInt(size);
		for (int i = 0; i < size; i++) {
			buffer.writeItemStack(itemList.get(i));
		}
	}

	public static SCSyncSynthBagToClientPacket decode(PacketBuffer buffer) {
		SCSyncSynthBagToClientPacket msg = new SCSyncSynthBagToClientPacket();

		int size = buffer.readInt();
		for (int i = 0; i < size; i++) {
			msg.itemList.add(i, buffer.readItemStack());
		}

		return msg;
	}

	public static void handle(final SCSyncSynthBagToClientPacket message, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			IItemHandler props = KingdomKeys.proxy.getClientPlayer().getHeldItemMainhand().getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
			for (int i = 0; i < message.itemList.size(); i++) {
				// System.out.println(i+"- "+message.itemList.get(i));
				// System.out.println(message.itemList.get(i).getCount());
				props.getStackInSlot(i).setCount(0);
				props.insertItem(i, message.itemList.get(i), false);
			}
		});
		ctx.get().setPacketHandled(true);
	}

}

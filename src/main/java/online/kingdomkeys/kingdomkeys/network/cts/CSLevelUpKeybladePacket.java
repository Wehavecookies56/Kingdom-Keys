package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.function.Supplier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.item.KeybladeItem;
import online.kingdomkeys.kingdomkeys.item.KeychainItem;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;
import online.kingdomkeys.kingdomkeys.synthesis.material.Material;

public class CSLevelUpKeybladePacket {

	int index;
	
	public CSLevelUpKeybladePacket() {

	}
	
	public CSLevelUpKeybladePacket(int index) {
		this.index = index;
	}

	public void encode(PacketBuffer buffer) {
		buffer.writeInt(index);
	}

	public static CSLevelUpKeybladePacket decode(PacketBuffer buffer) {
		CSLevelUpKeybladePacket msg = new CSLevelUpKeybladePacket();
		msg.index = buffer.readInt();
		return msg;
	}

	public static void handle(CSLevelUpKeybladePacket message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			PlayerEntity player = ctx.get().getSender();
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			
			ItemStack stack = player.inventory.getStackInSlot(message.index);
			KeychainItem kcItem = (KeychainItem) stack.getItem();
			KeybladeItem item = (KeybladeItem) kcItem.getKeyblade();
			System.out.println(item.getKeybladeLevel(stack));
			Iterator<Entry<Material, Integer>> itMats = item.data.getLevelData(item.getKeybladeLevel(stack)).getMaterialList().entrySet().iterator();
			boolean hasMaterials = true;
			while(itMats.hasNext()) { //Check if the player has the materials (checked serverside just in case)
				Entry<Material, Integer> m = itMats.next();
				System.out.println(m.getKey().getMaterialName()+" x"+m.getValue());
				if(playerData.getMaterialAmount(m.getKey()) < m.getValue()) {
					hasMaterials = false;
				}
			}
			
			if(hasMaterials) { //If the player has the materials substract them and give the item
			Iterator<Entry<Material, Integer>> ite = item.data.getLevelData(item.getKeybladeLevel(stack)).getMaterialList().entrySet().iterator();
				while(ite.hasNext()) {
					Entry<Material, Integer> m = ite.next();
					playerData.removeMaterial(m.getKey(), m.getValue());
				}
				kcItem.setKeybladeLevel(stack, kcItem.getKeybladeLevel(stack)+1);
			}
			PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayerEntity)player);	
		});
		ctx.get().setPacketHandled(true);
	}

}

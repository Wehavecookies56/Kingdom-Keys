package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.item.KeybladeItem;
import online.kingdomkeys.kingdomkeys.item.KeychainItem;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;
import online.kingdomkeys.kingdomkeys.synthesis.material.Material;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class CSLevelUpKeybladePacket {

	ItemStack stack;
	
	public CSLevelUpKeybladePacket() {

	}
	
	public CSLevelUpKeybladePacket(ItemStack stack) {
		this.stack = stack;
	}

	public void encode(FriendlyByteBuf buffer) {
		buffer.writeItem(stack);
	}

	public static CSLevelUpKeybladePacket decode(FriendlyByteBuf buffer) {
		CSLevelUpKeybladePacket msg = new CSLevelUpKeybladePacket();
		msg.stack = buffer.readItem();
		return msg;
	}

	public static void handle(CSLevelUpKeybladePacket message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			Player player = ctx.get().getSender();
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			
			ItemStack stack = message.stack.copy();
			KeychainItem kcItem = (KeychainItem) stack.getItem();
			KeybladeItem item = (KeybladeItem) kcItem.getKeyblade();
			Iterator<Entry<Material, Integer>> itMats = item.data.getLevelData(item.getKeybladeLevel(stack)).getMaterialList().entrySet().iterator();
			boolean hasMaterials = true;
			while(itMats.hasNext()) { //Check if the player has the materials (checked serverside just in case)
				Entry<Material, Integer> m = itMats.next();
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
				player.getInventory().setItem(ClientUtils.getSlotFor(player.getInventory(), message.stack), stack);
			}
			PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayer)player);	
		});
		ctx.get().setPacketHandled(true);
	}

}

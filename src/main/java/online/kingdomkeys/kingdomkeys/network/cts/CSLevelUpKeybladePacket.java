package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.data.ModData;
import online.kingdomkeys.kingdomkeys.item.KeybladeItem;
import online.kingdomkeys.kingdomkeys.item.KeychainItem;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;
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
			IPlayerData playerData = ModData.getPlayer(player);
			
			ItemStack stack = message.stack.copy();
			KeychainItem kcItem = (KeychainItem) stack.getItem();
			KeybladeItem item = kcItem.getKeyblade();
			Iterator<Entry<Material, Integer>> itMats = item.data.getLevelData(item.getKeybladeLevel(stack)).getMaterialList().entrySet().iterator();
			boolean hasMaterials = true;
			while(itMats.hasNext()) { //Check if the player has the materials (checked serverside just in case)
				Entry<Material, Integer> m = itMats.next();
				if(playerData.getMaterialAmount(m.getKey()) < m.getValue()) {
					hasMaterials = false;
				}
			}
			
			if(hasMaterials) { //If the player has the materials substract them and give the item
                for (Entry<Material, Integer> m : item.data.getLevelData(item.getKeybladeLevel(stack)).getMaterialList().entrySet()) {
                    playerData.removeMaterial(m.getKey(), m.getValue());
                }
				kcItem.setKeybladeLevel(stack, kcItem.getKeybladeLevel(stack)+1);

				//Sync that level to the keyblade
				int id = Utils.findSummoned(player.getInventory(),stack);
				if(id > -1){
					ItemStack summonedKeyblade = player.getInventory().getItem(id);
					if(summonedKeyblade!= null && summonedKeyblade.getItem() instanceof KeybladeItem kbItem)
						kbItem.setKeybladeLevel(summonedKeyblade, kcItem.getKeybladeLevel(stack));
				}
				UUID keybladeID = Utils.getKeybladeID(stack);
				if (keybladeID != null) {
					ResourceLocation slot = null;
					for (Entry<ResourceLocation, ItemStack> entry : playerData.getEquippedKeychains().entrySet()) {
						if (keybladeID.equals(Utils.getKeybladeID(entry.getValue()))) {
							slot = entry.getKey();
						}
					}
					if (slot != null) {
						playerData.equipKeychain(slot, stack);
					} else {
						player.getInventory().setItem(player.getInventory().findSlotMatchingItem(message.stack), stack);
					}
				}
			}
			PacketHandler.sendTo(new SCSyncPlayerData(playerData), (ServerPlayer)player);
		});
		ctx.get().setPacketHandled(true);
	}

}

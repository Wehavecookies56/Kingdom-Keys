package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.UUID;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.item.KeybladeItem;
import online.kingdomkeys.kingdomkeys.item.KeychainItem;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;
import online.kingdomkeys.kingdomkeys.synthesis.material.Material;
import online.kingdomkeys.kingdomkeys.util.Utils;

public record CSLevelUpKeybladePacket(ItemStack stack) implements Packet {

	public static final Type<CSLevelUpKeybladePacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "cs_level_up_keyblade"));

	public static final StreamCodec<RegistryFriendlyByteBuf, CSLevelUpKeybladePacket> STREAM_CODEC = StreamCodec.composite(
			ItemStack.STREAM_CODEC,
			CSLevelUpKeybladePacket::stack,
			CSLevelUpKeybladePacket::new
	);

	@Override
	public void handle(IPayloadContext context) {
		Player player = context.player();
		PlayerData playerData = PlayerData.get(player);

		ItemStack stack = stack().copy();
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
					player.getInventory().setItem(player.getInventory().findSlotMatchingItem(stack()), stack);
				}
			}
		}
		PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer)player);
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}

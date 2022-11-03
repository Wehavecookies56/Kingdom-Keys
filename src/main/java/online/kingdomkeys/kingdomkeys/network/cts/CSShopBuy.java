package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.IWorldCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.entity.SpawningMode;
import online.kingdomkeys.kingdomkeys.item.KeychainItem;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncWorldCapability;
import online.kingdomkeys.kingdomkeys.synthesis.shop.ShopItem;
import online.kingdomkeys.kingdomkeys.synthesis.shop.ShopListRegistry;

import java.util.List;
import java.util.function.Supplier;

public class CSShopBuy {

	ResourceLocation inv;
	ItemStack itemStack;

	public CSShopBuy() {
	}

	public CSShopBuy(ResourceLocation inv, ItemStack itemStack) {
		this.inv = inv;
		this.itemStack = itemStack;
	}

	public void encode(FriendlyByteBuf buffer) {
		buffer.writeResourceLocation(this.inv);
		buffer.writeItem(this.itemStack);
	}

	public static CSShopBuy decode(FriendlyByteBuf buffer) {
		CSShopBuy msg = new CSShopBuy();
		msg.inv = buffer.readResourceLocation();
		msg.itemStack = buffer.readItem();
		return msg;
	}

	public static void handle(CSShopBuy message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			Player player = ctx.get().getSender();
			if(player.getInventory().getFreeSlot() > -1) {
				IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);

				List<ShopItem> list = ShopListRegistry.getInstance().getRegistry().get(message.inv).getList();
				ShopItem item = null;
				for(ShopItem shopItem : list) {
					Item it = shopItem.getResult();

					if(it instanceof KeychainItem) {
						it = ((KeychainItem)it).getKeyblade();
					}
					
					if(ItemStack.isSame(new ItemStack(it,shopItem.getAmount()), message.itemStack)) {
						item = shopItem;
						break;
					}
					
				}
				boolean enoughMunny = playerData.getMunny() >= item.getCost();
				boolean enoughTier = ModConfigs.requireSynthTier ? playerData.getSynthLevel() >= item.getTier() : true;
				
				if(enoughMunny && enoughTier) { //If the player has the materials substract them and give the item
					playerData.setMunny(playerData.getMunny() - item.getCost());
					playerData.addSynthExperience(10 + item.getTier()*2);
										
					Item i = item.getResult();
					
					int amount = item.getAmount();
					player.getInventory().add(new ItemStack(i,amount));
					
					if(i instanceof KeychainItem && ModConfigs.heartlessSpawningMode == SpawningMode.AFTER_KEYCHAIN) {
						IWorldCapabilities worldData = ModCapabilities.getWorld(player.level);
						worldData.setHeartlessSpawnLevel(1);
						PacketHandler.sendToAllPlayers(new SCSyncWorldCapability(worldData));
					}
				}
				PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayer)player);
			}
		});
		ctx.get().setPacketHandled(true);
	}

}

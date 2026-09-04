package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.entity.SpawningMode;
import online.kingdomkeys.kingdomkeys.item.KeychainItem;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncWorldData;
import online.kingdomkeys.kingdomkeys.synthesis.shop.Currency;
import online.kingdomkeys.kingdomkeys.synthesis.shop.ForetellerShop;
import online.kingdomkeys.kingdomkeys.synthesis.shop.ShopItem;
import online.kingdomkeys.kingdomkeys.synthesis.shop.ShopListRegistry;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.List;

public record CSShopBuy(ResourceLocation inv, ItemStack itemStack) implements Packet {

	public static final Type<CSShopBuy> TYPE = new Type<>(KingdomKeys.rl("cs_shop_buy"));

	public static final StreamCodec<RegistryFriendlyByteBuf, CSShopBuy> STREAM_CODEC = StreamCodec.composite(
			ResourceLocation.STREAM_CODEC, CSShopBuy::inv,
			ItemStack.STREAM_CODEC, CSShopBuy::itemStack,
			CSShopBuy::new
	);

	@Override
	public void handle(IPayloadContext context) {
		Player player = context.player();
		if(player.getInventory().getFreeSlot() > -1) {
			PlayerData playerData = PlayerData.get(player);

			List<ShopItem> list = ShopListRegistry.getInstance().getRegistry().get(inv).getList();
			ShopItem item = null;
			for(ShopItem shopItem : list) {
				Item it = shopItem.getResult();

				if(it instanceof KeychainItem) {
					it = ((KeychainItem)it).getKeyblade();
				}

				if(ItemStack.isSameItem(new ItemStack(it,shopItem.getAmount()), itemStack)) {
					item = shopItem;
					break;
				}

			}

			boolean requireTier = ModConfigs.SERVER.requireSynthTierShop.get();
			int synthLevel = playerData.getSynthLevel();

			boolean tierOk = !requireTier || item.getTier() <= synthLevel;
			boolean matsOk = item.getMatReq() <= playerData.getTotalMaterialAmount(item.getResult());

			boolean validItem = item.requireAll() ? (tierOk && matsOk) : (tierOk || matsOk);

			Currency currency = item.getCurrency();
			int price = ForetellerShop.priceFor(item, playerData, inv);
			boolean canAfford = currency.held(playerData) >= price;
			if (validItem && canAfford) {
				currency.charge(playerData, price, (ServerPlayer) player);

				if(ModConfigs.SERVER.getExpFromShop.get())
					playerData.addSynthExperience(10 + item.getTier()*2);

				Item i = item.getResult();

				int amount = item.getAmount();
				Utils.addToBagOrInventory(player, new ItemStack(i, amount));

				if(i instanceof KeychainItem && ModConfigs.heartlessSpawningMode == SpawningMode.AFTER_KEYCHAIN) {
					WorldData worldData = WorldData.get(player.getServer());
					worldData.setHeartlessSpawnLevel(1);
					PacketHandler.sendToAll(new SCSyncWorldData(player.getServer()));
				}
			}
			PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer)player);
		}
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}

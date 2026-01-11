package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
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
import online.kingdomkeys.kingdomkeys.synthesis.shop.sell.SellItem;
import online.kingdomkeys.kingdomkeys.synthesis.shop.sell.SellListRegistry;

import java.util.List;

public record CSShopSell(ItemStack itemStack, int amount) implements Packet {

	public static final Type<CSShopSell> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "cs_shop_sell"));

	public static final StreamCodec<RegistryFriendlyByteBuf, CSShopSell> STREAM_CODEC = StreamCodec.composite(
			ItemStack.STREAM_CODEC,
			CSShopSell::itemStack,
            ByteBufCodecs.INT,
            CSShopSell::amount,
			CSShopSell::new
	);

	@Override
	public void handle(IPayloadContext context) {
		Player player = context.player();
		if(player.getInventory().getFreeSlot() > -1) {
			PlayerData playerData = PlayerData.get(player);

			List<SellItem> list = SellListRegistry.getInstance().getRegistry().get(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "sell")).getList();
            SellItem item = null;
			for(SellItem shopItem : list) {
				Item it = shopItem.getResult();

				if(it instanceof KeychainItem) {
					it = ((KeychainItem)it).getKeyblade();
				}

				if(ItemStack.isSameItem(new ItemStack(it,amount), itemStack)) {
					item = shopItem;
					break;
				}

			}

            playerData.setMunny(playerData.getMunny() + item.getPrice());

            Item i = item.getResult();

            player.getInventory().add(new ItemStack(i,amount));
			PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer)player);
		}
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}

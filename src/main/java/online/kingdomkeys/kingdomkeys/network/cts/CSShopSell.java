package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCOpenSellScreen;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;
import online.kingdomkeys.kingdomkeys.synthesis.shop.sell.SellItem;
import online.kingdomkeys.kingdomkeys.synthesis.shop.sell.SellListRegistry;

import java.util.List;

public record CSShopSell(int slot, int amount, String inv, String name, int moogle) implements Packet {

	public static final Type<CSShopSell> TYPE = new Type<>(KingdomKeys.rl("cs_shop_sell"));

	public static final StreamCodec<RegistryFriendlyByteBuf, CSShopSell> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
			CSShopSell::slot,
            ByteBufCodecs.INT,
            CSShopSell::amount,
            ByteBufCodecs.STRING_UTF8,
            CSShopSell::inv,
            ByteBufCodecs.STRING_UTF8,
            CSShopSell::name,
            ByteBufCodecs.INT,
            CSShopSell::moogle,
			CSShopSell::new
	);

	@Override
	public void handle(IPayloadContext context) {
		Player player = context.player();
		PlayerData playerData = PlayerData.get(player);

		List<SellItem> list = SellListRegistry.getInstance().getRegistry().get(KingdomKeys.rl("sell")).getList();
        ItemStack playerStack = player.getInventory().getItem(slot);

        SellItem item = null;
		for(SellItem shopItem : list) {
			Item it = shopItem.getResult();

			if(ItemStack.isSameItem(new ItemStack(it), playerStack)) {
				item = shopItem;
				break;
			}

		}

        if(item != null && playerStack.getCount() >= amount) {
            playerData.setMunny(playerData.getMunny() + item.getPrice() * amount, (ServerPlayer) player);
            player.getInventory().getItem(slot).setCount(player.getInventory().getItem(slot).getCount() - amount);
            PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer) player);
            PacketHandler.sendTo(new SCOpenSellScreen(playerData.serializeNBT(player.level().registryAccess()), inv, name, moogle), (ServerPlayer) player);
        }
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}

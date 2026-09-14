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
		ItemStack sold = player.getInventory().getItem(slot).copy();

        SellItem item = null;
		for(SellItem shopItem : list) {
			Item it = shopItem.getResult();

			if(ItemStack.isSameItem(new ItemStack(it), sold)) {
				item = shopItem;
				break;
			}

		}

		// A client asking for nothing, or for a negative amount that would pay out and hand the goods back
		if (item == null || amount <= 0 || count(player, sold) < amount) {
			return;
		}

		playerData.setMunny(playerData.getMunny() + item.getPrice() * amount, (ServerPlayer) player);
		take(player, sold, amount);
		PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer) player);
		PacketHandler.sendTo(new SCOpenSellScreen(playerData.serializeNBT(player.level().registryAccess()), inv, name, moogle), (ServerPlayer) player);
	}

	private static int count(Player player, ItemStack sold) {
		int total = 0;

		for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
			ItemStack stack = player.getInventory().getItem(i);

			if (ItemStack.isSameItemSameComponents(stack, sold)) {
				total += stack.getCount();
			}
		}

		return total;
	}

	private static void take(Player player, ItemStack sold, int amount) {
		int left = amount;

		for (int i = 0; i < player.getInventory().getContainerSize() && left > 0; i++) {
			ItemStack stack = player.getInventory().getItem(i);

			if (ItemStack.isSameItemSameComponents(stack, sold)) {
				int taken = Math.min(left, stack.getCount());
				stack.shrink(taken);
				left -= taken;
			}
		}
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}

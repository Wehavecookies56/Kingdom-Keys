package online.kingdomkeys.kingdomkeys.synthesis.shop;

import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.lib.Union;

public class ForetellerShop {
	public static final int OUTSIDER_MULTIPLIER = 3;

	private static final String PREFIX = "foreteller/";

	private ForetellerShop() {
	}

	public static String shopFor(Union union) {
		return KingdomKeys.MODID + ":" + PREFIX + union.getSerializedName();
	}

	public static Union unionOf(ResourceLocation inv) {
		if (inv == null || !inv.getNamespace().equals(KingdomKeys.MODID))
			return Union.NONE;

		String path = inv.getPath();
		if (!path.startsWith(PREFIX))
			return Union.NONE;

		String name = path.substring(PREFIX.length());
		for (Union union : Union.choosable()) {
			if (union.getSerializedName().equals(name)) {
				return union;
			}
		}
		return Union.NONE;
	}

	public static int priceFor(ShopItem item, PlayerData playerData, ResourceLocation inv) {
		Union shopUnion = unionOf(inv);

		if (shopUnion == Union.NONE || playerData == null || shopUnion == playerData.getUnion())
			return item.getCost();

		return item.getCost() * OUTSIDER_MULTIPLIER;
	}
}

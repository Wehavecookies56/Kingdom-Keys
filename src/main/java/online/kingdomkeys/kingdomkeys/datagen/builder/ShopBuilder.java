package online.kingdomkeys.kingdomkeys.datagen.builder;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import online.kingdomkeys.kingdomkeys.synthesis.shop.Currency;

import java.util.function.Supplier;

public class ShopBuilder {

	private static final int NO_MAT_REQ = -1;

	private final JsonArray array = new JsonArray();

	public ShopBuilder names(String namesListPath) {
		JsonObject obj = new JsonObject();
		obj.addProperty("names", namesListPath);
		array.add(obj);
		return this;
	}

	public ShopBuilder item(Supplier<? extends Item> item, int amount, int tier, int cost) {
		return item(item, amount, tier, cost, Currency.MUNNY);
	}

	// Paid in something other than munny
	public ShopBuilder item(Supplier<? extends Item> item, int amount, int tier, int cost, Currency currency) {
		return item(item, amount, tier, cost, currency, NO_MAT_REQ, false);
	}

	public ShopBuilder item(Supplier<? extends Item> item, int amount, int tier, int cost, int matReq) {
		return item(item, amount, tier, cost, Currency.MUNNY, matReq, false);
	}

	public ShopBuilder item(Supplier<? extends Item> item, int amount, int tier, int cost, Currency currency, int matReq, boolean requireAll) {
		JsonObject obj = new JsonObject();
		obj.addProperty("item", registryName(item));
		obj.addProperty("amount", amount);
		obj.addProperty("tier", tier);
		obj.addProperty("cost", cost);

		if (currency != Currency.MUNNY) {
			obj.addProperty("currency", currency.getSerializedName());
		}
		if (matReq != NO_MAT_REQ) {
			obj.addProperty("mat_req", matReq);
		}
		if (requireAll) {
			obj.addProperty("condition", "all");
		}

		array.add(obj);
		return this;
	}

	private String registryName(Supplier<? extends Item> item) {
		return BuiltInRegistries.ITEM.getKey(item.get()).toString();
	}

	public JsonArray build() {
		return array;
	}
}

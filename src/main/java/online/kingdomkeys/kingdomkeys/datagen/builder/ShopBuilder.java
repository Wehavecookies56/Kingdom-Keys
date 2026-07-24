package online.kingdomkeys.kingdomkeys.datagen.builder;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

import java.util.function.Supplier;

public class ShopBuilder {

	private final JsonArray array = new JsonArray();

	public ShopBuilder names(String namesListPath) {
		JsonObject obj = new JsonObject();
		obj.addProperty("names", namesListPath);
		array.add(obj);
		return this;
	}

	public ShopBuilder item(Supplier<? extends Item> item, int amount, int tier, int cost) {
		JsonObject obj = new JsonObject();
		obj.addProperty("item", registryName(item));
		obj.addProperty("amount", amount);
		obj.addProperty("tier", tier);
		obj.addProperty("cost", cost);
		array.add(obj);
		return this;
	}

	public ShopBuilder item(Supplier<? extends Item> item, int amount, int tier, int cost, int matReq) {
		JsonObject obj = new JsonObject();
		obj.addProperty("item", registryName(item));
		obj.addProperty("amount", amount);
		obj.addProperty("tier", tier);
		obj.addProperty("cost", cost);
		obj.addProperty("mat_req", matReq);
		array.add(obj);
		return this;
	}

	public ShopBuilder itemRequireAll(Supplier<? extends Item> item, int amount, int tier, int cost, int matReq) {
		JsonObject obj = new JsonObject();
		obj.addProperty("item", registryName(item));
		obj.addProperty("amount", amount);
		obj.addProperty("tier", tier);
		obj.addProperty("cost", cost);
		obj.addProperty("mat_req", matReq);
		obj.addProperty("condition", "all");
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

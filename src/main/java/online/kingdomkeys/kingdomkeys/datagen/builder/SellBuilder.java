package online.kingdomkeys.kingdomkeys.datagen.builder;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class SellBuilder {

	private final JsonArray array = new JsonArray();

	public SellBuilder item(Supplier<? extends Item> item, int price) {
		return item(item.get(), price);
	}

	public SellBuilder item(Item item, int price) {
		JsonObject obj = new JsonObject();
		obj.addProperty("item", BuiltInRegistries.ITEM.getKey(item).toString());
		obj.addProperty("price", price);
		array.add(obj);
		return this;
	}

	public JsonArray build() {
		return array;
	}
}

package online.kingdomkeys.kingdomkeys.datagen.builder;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import online.kingdomkeys.kingdomkeys.lib.KKSupplier;

import java.util.function.Supplier;

public class LevelingBuilder {

	private final JsonObject root = new JsonObject();
	private final JsonObject levels = new JsonObject();
	private JsonObject current;

	public LevelingBuilder() {
		root.addProperty("version", 3);
		root.add("levels", levels);
	}

	public LevelingBuilder level(int lvl) {
		String key = String.valueOf(lvl);
		current = levels.has(key) ? levels.getAsJsonObject(key) : new JsonObject();
		levels.add(key, current);
		return this;
	}

	public LevelingBuilder str(int value) {
		current.addProperty("str", value);
		return this;
	}

	public LevelingBuilder def(int value) {
		current.addProperty("def", value);
		return this;
	}

	public LevelingBuilder mag(int value) {
		current.addProperty("mag", value);
		return this;
	}

	public LevelingBuilder maxHp(int value) {
		current.addProperty("maxhp", value);
		return this;
	}

	public LevelingBuilder maxMp(int value) {
		current.addProperty("maxmp", value);
		return this;
	}

	public LevelingBuilder ap(int value) {
		current.addProperty("ap", value);
		return this;
	}

	public LevelingBuilder maxArmors(int value) {
		current.addProperty("max_armors", value);
		return this;
	}

	public LevelingBuilder maxMagics(int value) {
		current.addProperty("max_magics", value);
		return this;
	}

	public LevelingBuilder maxItems(int value) {
		current.addProperty("max_items", value);
		return this;
	}

	public LevelingBuilder maxAccessories(int value) {
		current.addProperty("max_accessories", value);
		return this;
	}

	public LevelingBuilder abilities(KKSupplier<?>... refs) {
		addToArray("abilities", toRegistryNames(refs));
		return this;
	}

	public LevelingBuilder item(Supplier<? extends Item> item, int amount) {
		return item(new ItemStack(item.get(), amount));
	}

	public LevelingBuilder item(ItemStack stack) {
		JsonArray array = current.has("items") ? current.getAsJsonArray("items") : new JsonArray();
		JsonElement encoded = ItemStack.CODEC.encodeStart(JsonOps.INSTANCE, stack).getPartialOrThrow(msg -> new IllegalStateException("Failed to encode item grant " + stack + ": " + msg));
		array.add(encoded);
		current.add("items", array);
		return this;
	}

	private String[] toRegistryNames(KKSupplier<?>[] refs) {
		String[] names = new String[refs.length];
		for (int i = 0; i < refs.length; i++) {
			names[i] = refs[i].location().toString();
		}
		return names;
	}

	private void addToArray(String key, String[] names) {
		if (names == null || names.length == 0) return;
		JsonArray array = current.has(key) ? current.getAsJsonArray(key) : new JsonArray();
		for (String name : names) {
			boolean present = false;
			for (JsonElement existing : array) {
				if (existing.getAsString().equals(name)) {
					present = true;
					break;
				}
			}
			if (!present)
				array.add(name);
		}
		current.add(key, array);
	}

	public JsonObject build() {
		return root;
	}
}
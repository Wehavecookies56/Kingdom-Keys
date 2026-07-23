package online.kingdomkeys.kingdomkeys.datagen.builder;

import com.google.gson.JsonObject;

public class LimitBuilder {
	private final JsonObject root = new JsonObject();

	public LimitBuilder cost(int value) {
		root.addProperty("cost", value);
		return this;
	}

	public LimitBuilder cooldown(int value) {
		root.addProperty("cooldown", value);
		return this;
	}

	public LimitBuilder damageMultiplier(float value) {
		root.addProperty("dmg_mult", value);
		return this;
	}

	public JsonObject build() {
		return root;
	}
}

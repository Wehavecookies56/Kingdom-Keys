package online.kingdomkeys.kingdomkeys.datagen.builder;

import com.google.gson.JsonObject;

public class ShotlockBuilder {

	private final JsonObject root = new JsonObject();

	public ShotlockBuilder cooldown(int value) {
		root.addProperty("cooldown", value);
		return this;
	}

	public ShotlockBuilder max(int value) {
		root.addProperty("max", value);
		return this;
	}

	public ShotlockBuilder damageMultiplier(float value) {
		root.addProperty("dmg_mult", value);
		return this;
	}

	public ShotlockBuilder element(String value) {
		root.addProperty("element", value);
		return this;
	}

	public JsonObject build() {
		return root;
	}
}

package online.kingdomkeys.kingdomkeys.datagen.builder;

import com.google.gson.JsonObject;

public class ShotlockBuilder {

	private final JsonObject root = new JsonObject();

	public ShotlockBuilder cooldown(int value) {
		root.addProperty("cooldown", value);
		return this;
	}

	public ShotlockBuilder cooldownMax(int value) {
		root.addProperty("cooldown_max", value);
		return this;
	}

	public ShotlockBuilder maxLocks(int value) {
		root.addProperty("max", value);
		return this;
	}

	public ShotlockBuilder damageMultiplier(float value) {
		root.addProperty("dmg_mult", value);
		return this;
	}

	public ShotlockBuilder damageMultiplierMax(float value) {
		root.addProperty("dmg_mult_max", value);
		return this;
	}

	public ShotlockBuilder maxExp(int value) {
		root.addProperty("max_exp", value);
		return this;
	}

	public ShotlockBuilder maxLevel(int value) {
		root.addProperty("max_level", value);
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

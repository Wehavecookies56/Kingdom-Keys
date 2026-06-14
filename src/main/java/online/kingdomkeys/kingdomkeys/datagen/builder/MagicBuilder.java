package online.kingdomkeys.kingdomkeys.datagen.builder;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;

public class MagicBuilder {

	private final JsonObject root = new JsonObject();

	public MagicBuilder cost(int value) {
		root.addProperty("cost", value);
		return this;
	}

	public MagicBuilder castTime(int value) {
		root.addProperty("casttime", value);
		return this;
	}

	public MagicBuilder cooldown(int value) {
		root.addProperty("cooldown", value);
		return this;
	}

	public MagicBuilder damageMultiplier(float value) {
		root.addProperty("dmg_mult", value);
		return this;
	}

	public MagicBuilder damageMultiplier(float value, float max) {
		root.addProperty("dmg_mult", value);
		root.addProperty("dmg_mult_max", max);
		return this;
	}

	public MagicBuilder lockOn(boolean value) {
		root.addProperty("magic_lock_on", value);
		return this;
	}

	public MagicBuilder maxExp(int value) {
		root.addProperty("max_exp", value);
		return this;
	}

	public MagicBuilder maxExpLevel(int value) {
		root.addProperty("max_lvl", value);
		return this;
	}

	public MagicBuilder nextTier(ResourceLocation value, ResourceLocation magicRC) {
		root.addProperty("next_tier", value.toString());
		root.addProperty("magic_rc", magicRC.toString());
		return this;
	}

	public JsonObject build() {
		return root;
	}
}
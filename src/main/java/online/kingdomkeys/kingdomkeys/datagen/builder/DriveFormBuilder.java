package online.kingdomkeys.kingdomkeys.datagen.builder;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import online.kingdomkeys.kingdomkeys.lib.KKSupplier;

public class DriveFormBuilder {

	private final JsonObject root = new JsonObject();

	public DriveFormBuilder cost(int value) {
		root.addProperty("cost", value);
		return this;
	}

	public DriveFormBuilder ap(int value) {
		root.addProperty("ap", value);
		return this;
	}

	public DriveFormBuilder strMult(float value) {
		root.addProperty("str_mult", value);
		return this;
	}

	public DriveFormBuilder magMult(float value) {
		root.addProperty("mag_mult", value);
		return this;
	}

	public DriveFormBuilder speedMult(float value) {
		root.addProperty("speed_mult", value);
		return this;
	}

	public DriveFormBuilder canGoAnti(boolean value) {
		root.addProperty("can_go_anti", value);
		return this;
	}

	public DriveFormBuilder canUseMagic(boolean value) {
		root.addProperty("can_use_magic", value);
		return this;
	}

	public DriveFormBuilder levelUp(int... costs) {
		JsonArray array = new JsonArray();
		for (int cost : costs) array.add(cost);
		root.add("level_up", array);
		return this;
	}

	public DriveFormBuilder abilities(KKSupplier<?>... refs) {
		root.add("abilities", toArray(refs));
		return this;
	}

	public DriveFormBuilder baseLevelUpAbilities(KKSupplier<?>... refs) {
		root.add("base_levelup_abilities", toArray(refs));
		return this;
	}

	public DriveFormBuilder driveFormLevelUpAbilities(KKSupplier<?>... refs) {
		root.add("driveform_levelup_abilities", toArray(refs));
		return this;
	}

	private JsonArray toArray(KKSupplier<?>[] refs) {
		JsonArray array = new JsonArray();
		for (KKSupplier<?> ref : refs) {
			array.add(ref == null ? "" : ref.location().toString());
		}
		return array;
	}

	public JsonObject build() {
		return root;
	}
}

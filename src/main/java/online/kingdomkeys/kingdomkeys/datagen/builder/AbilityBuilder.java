package online.kingdomkeys.kingdomkeys.datagen.builder;

import com.google.gson.JsonObject;
import online.kingdomkeys.kingdomkeys.ability.Ability.AbilityType;

public class AbilityBuilder {
	private final JsonObject root = new JsonObject();

	public AbilityBuilder apCost(int value) {
		root.addProperty("ap_cost", value);
		return this;
	}

	public AbilityBuilder type(AbilityType type) {
		root.addProperty("type", type.toString());
		return this;
	}

	public AbilityBuilder order(int value) {
		root.addProperty("order", value);
		return this;
	}

	public AbilityBuilder exclusionGroup(String group) {
		root.addProperty("exclusion_group", group);
		return this;
	}

	public JsonObject build() {
		return root;
	}
}

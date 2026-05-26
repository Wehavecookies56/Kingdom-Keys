package online.kingdomkeys.kingdomkeys.datagen.builder;

import com.google.gson.JsonObject;

public class MagicBuilder {

	private final JsonObject root = new JsonObject();

	public LevelBuilder level(int level) {
		return new LevelBuilder(this, level);
	}

	public JsonObject build() {
		return root;
	}

	public static class LevelBuilder {

		private final MagicBuilder parent;
		private final int level;

		private final JsonObject obj = new JsonObject();

		public LevelBuilder(MagicBuilder parent, int level) {
			this.parent = parent;
			this.level = level;
		}

		public LevelBuilder cost(int value) {
			obj.addProperty("cost", value);
			return this;
		}

		public LevelBuilder castTime(int value) {
			obj.addProperty("casttime", value);
			return this;
		}

		public LevelBuilder cooldown(int value) {
			obj.addProperty("cooldown", value);
			return this;
		}

		public LevelBuilder damageMultiplier(float value) {
			obj.addProperty("dmg_mult", value);
			return this;
		}

		public LevelBuilder lockOn(boolean value) {
			obj.addProperty("magic_lock_on", value);
			return this;
		}

		public LevelBuilder maxExp(int value) {
			obj.addProperty("max_exp", value);
			return this;
		}

		public MagicBuilder end() {
			parent.root.add(String.valueOf(level), obj);
			return parent;
		}
	}
}
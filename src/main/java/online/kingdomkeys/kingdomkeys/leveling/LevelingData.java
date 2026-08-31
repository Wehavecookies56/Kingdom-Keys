package online.kingdomkeys.kingdomkeys.leveling;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class LevelingData {
	public static final int MAX_LEVEL = 100;

	private static final int LEVELS = MAX_LEVEL + 1;

	int[] str = new int[LEVELS];
	int[] mag = new int[LEVELS];
	int[] def = new int[LEVELS];
	int[] ap = new int[LEVELS];
	int[] maxhp = new int[LEVELS];
	int[] maxmp = new int[LEVELS];
	ResourceLocation[][] abilities = new ResourceLocation[LEVELS][5];
	ItemStack[][] items = new ItemStack[LEVELS][];
	int[] maxAccessories = new int[LEVELS];
	int[] maxArmors = new int[LEVELS];
	int[] maxMagics = new int[LEVELS];
	int version;

	public LevelingData() {}

	public int getStr(int lvl) {
		return str[lvl];
	}

	public void setStr(int lvl, int amount) {
		this.str[lvl] = amount;
	}

	public int getMag(int lvl) {
		return mag[lvl];
	}

	public void setMag(int lvl, int amount) {
		this.mag[lvl] = amount;
	}

	public int getDef(int lvl) {
		return def[lvl];
	}

	public void setDef(int lvl, int amount) {
		this.def[lvl] = amount;
	}

	public int getMaxAP(int lvl) {
		return ap[lvl];
	}

	public void setAP(int lvl, int amount) {
		this.ap[lvl] = amount;
	}

	public int getMaxHp(int lvl) {
		return maxhp[lvl];
	}

	public void setMaxHp(int lvl, int amount) {
		this.maxhp[lvl] = amount;
	}

	public int getMaxMp(int lvl) {
		return maxmp[lvl];
	}

	public void setMaxMp(int lvl, int amount) {
		this.maxmp[lvl] = amount;
	}

	public ResourceLocation[] getAbilities(int lvl) {
		return abilities[lvl];
	}

	public void setAbilities(int lvl, ResourceLocation[] abilities) {
		this.abilities[lvl] = abilities;
	}

	public ItemStack[] getItems(int lvl) {
		return items[lvl] == null ? new ItemStack[0] : items[lvl];
	}

	public void setItems(int lvl, ItemStack[] items) {
		this.items[lvl] = items;
	}

	public int getMaxAccessories(int lvl) {
		return maxAccessories[lvl];
	}

	public void setMaxAccessories(int lvl, int amount) {
		this.maxAccessories[lvl] = amount;
	}

	public int getMaxArmors(int lvl) {
		return maxArmors[lvl];
	}

	public void setMaxArmors(int lvl, int amount) {
		this.maxArmors[lvl] = amount;
	}

	public int getMaxMagics(int lvl) {
		return maxMagics[lvl];
	}

	public void setMaxMagics(int lvl, int amount) {
		this.maxMagics[lvl] = amount;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}
}

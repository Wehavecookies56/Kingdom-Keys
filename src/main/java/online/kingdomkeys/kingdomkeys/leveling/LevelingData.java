package online.kingdomkeys.kingdomkeys.leveling;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class LevelingData {
	public static final int DEFAULT_MAX_LEVEL = 100;

	final int maxLevel;

	int[] str, mag, def, ap, maxhp, maxmp;
	ResourceLocation[][] abilities;
	ItemStack[][] items;
	int[] maxAccessories, maxArmors, maxMagics, maxItems;
	int version;

	public LevelingData(int maxLevel) {
		this.maxLevel = maxLevel;
		int levels = maxLevel + 1;
		str = new int[levels];
		mag = new int[levels];
		def = new int[levels];
		ap = new int[levels];
		maxhp = new int[levels];
		maxmp = new int[levels];
		abilities = new ResourceLocation[levels][5];
		items = new ItemStack[levels][];
		maxAccessories = new int[levels];
		maxArmors = new int[levels];
		maxMagics = new int[levels];
		maxItems = new int[levels];
	}

	public int getMaxLevel() {
		return maxLevel;
	}

	private boolean has(int lvl) {
		return lvl >= 0 && lvl <= maxLevel;
	}

	public int getStr(int lvl) {
		return has(lvl) ? str[lvl] : 0;
	}

	public void setStr(int lvl, int amount) {
		this.str[lvl] = amount;
	}

	public int getMag(int lvl) {
		return has(lvl) ? mag[lvl] : 0;
	}

	public void setMag(int lvl, int amount) {
		this.mag[lvl] = amount;
	}

	public int getDef(int lvl) {
		return has(lvl) ? def[lvl] : 0;
	}

	public void setDef(int lvl, int amount) {
		this.def[lvl] = amount;
	}

	public int getMaxAP(int lvl) {
		return has(lvl) ? ap[lvl] : 0;
	}

	public void setAP(int lvl, int amount) {
		this.ap[lvl] = amount;
	}

	public int getMaxHp(int lvl) {
		return has(lvl) ? maxhp[lvl] : 0;
	}

	public void setMaxHp(int lvl, int amount) {
		this.maxhp[lvl] = amount;
	}

	public int getMaxMp(int lvl) {
		return has(lvl) ? maxmp[lvl] : 0;
	}

	public void setMaxMp(int lvl, int amount) {
		this.maxmp[lvl] = amount;
	}

	public ResourceLocation[] getAbilities(int lvl) {
		return has(lvl) ? abilities[lvl] : new ResourceLocation[0];
	}

	public void setAbilities(int lvl, ResourceLocation[] abilities) {
		this.abilities[lvl] = abilities;
	}

	public ItemStack[] getItems(int lvl) {
		return !has(lvl) || items[lvl] == null ? new ItemStack[0] : items[lvl];
	}

	public void setItems(int lvl, ItemStack[] items) {
		this.items[lvl] = items;
	}

	public int getMaxAccessories(int lvl) {
		return has(lvl) ? maxAccessories[lvl] : 0;
	}

	public void setMaxAccessories(int lvl, int amount) {
		this.maxAccessories[lvl] = amount;
	}

	public int getMaxArmors(int lvl) {
		return has(lvl) ? maxArmors[lvl] : 0;
	}

	public void setMaxArmors(int lvl, int amount) {
		this.maxArmors[lvl] = amount;
	}

	public int getMaxMagics(int lvl) {
		return has(lvl) ? maxMagics[lvl] : 0;
	}

	public void setMaxMagics(int lvl, int amount) {
		this.maxMagics[lvl] = amount;
	}

	public int getMaxItems(int lvl) {
		return has(lvl) ? maxItems[lvl] : 0;
	}

	public void setMaxItems(int lvl, int amount) {
		this.maxItems[lvl] = amount;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}
}

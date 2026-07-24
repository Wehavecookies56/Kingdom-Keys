package online.kingdomkeys.kingdomkeys.leveling;

import net.minecraft.resources.ResourceLocation;

/**
 * Stores the data loaded from the leveling datapack
 */
public class LevelingData {
	int[] str = new int[101];
	int[] mag = new int[101];
	int[] def = new int[101];
	int[] ap = new int[101];
	int[] maxhp = new int[101];
	int[] maxmp = new int[101];
	ResourceLocation[][] abilities = new ResourceLocation[101][5];
	ItemGrant[][] items = new ItemGrant[101][];
	int[] maxAccessories = new int[101];
	int[] maxArmors = new int[101];
	int[] maxMagics = new int[101];
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

	public ItemGrant[] getItems(int lvl) {
		return items[lvl] == null ? new ItemGrant[0] : items[lvl];
	}

	public void setItems(int lvl, ItemGrant[] items) {
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

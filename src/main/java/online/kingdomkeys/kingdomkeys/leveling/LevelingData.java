package online.kingdomkeys.kingdomkeys.leveling;

import net.minecraft.resources.ResourceLocation;

import java.util.List;

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
	ResourceLocation[][] shotlocks = new ResourceLocation[101][5];
	ResourceLocation[][] spells = new ResourceLocation[101][5];
	int[] maxAccessories = new int[101];
	int[] maxArmors = new int[101];
	int[] maxMagics = new int[101];
	int version;

	public LevelingData() {

	}

	public LevelingData(int ver, int level, int str, int mag, int def, int ap, int maxhp, int maxmp, List<ResourceLocation> abilities, List<ResourceLocation> shotlocks, List<ResourceLocation> spells, int maxAccessories, int maxArmors, int maxSpells) {
		this.version = ver;
		this.mag[level] = mag;
		this.def[level] = def;
		this.str[level] = str;
		this.ap[level] = ap;
		this.maxhp[level] = maxhp;
		this.maxmp[level] = maxmp;
		this.abilities[level] = (ResourceLocation[]) abilities.toArray();
		this.shotlocks[level] = (ResourceLocation[]) shotlocks.toArray();
		this.spells[level] = (ResourceLocation[]) spells.toArray();
		this.maxAccessories[level] = maxAccessories;
		this.maxArmors[level] = maxArmors;
		this.maxMagics[level] = maxSpells;
	}

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

	public ResourceLocation[] getShotlocks(int lvl) {
		return shotlocks[lvl];
	}

	public void setShotlocks(int lvl, ResourceLocation[] shotlocks) {
		this.shotlocks[lvl] = shotlocks;
	}
	
	public ResourceLocation[] getSpells(int lvl) {
		return spells[lvl];
	}
	
	public void setSpells(int lvl, ResourceLocation[] spells) {
		this.spells[lvl] = spells;
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

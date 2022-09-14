package online.kingdomkeys.kingdomkeys.leveling;

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
	String[][] abilities = new String[101][5];
	String[][] shotlocks = new String[101][5];
	String[][] spells = new String[101][5];

	public LevelingData() {

	}

	public LevelingData(int level, int str, int mag, int def, int ap, int maxhp, int maxmp, List<String> abilities, List<String> shotlocks, List<String> spells) {
		this.mag[level] = mag;
		this.def[level] = def;
		this.str[level] = str;
		this.ap[level] = ap;
		this.maxhp[level] = maxhp;
		this.maxmp[level] = maxmp;
		this.abilities[level] = (String[]) abilities.toArray();
		this.shotlocks[level] = (String[]) shotlocks.toArray();
		this.spells[level] = (String[]) spells.toArray();
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

	public String[] getAbilities(int lvl) {
		return abilities[lvl];
	}

	public void setAbilities(int lvl, String[] abilities) {
		this.abilities[lvl] = abilities;
	}

	public String[] getShotlocks(int lvl) {
		return shotlocks[lvl];
	}

	public void setShotlocks(int lvl, String[] shotlocks) {
		this.shotlocks[lvl] = shotlocks;
	}
	
	public String[] getSpells(int lvl) {
		return spells[lvl];
	}
	
	public void setSpells(int lvl, String[] spells) {
		this.spells[lvl] = spells;
	}

}

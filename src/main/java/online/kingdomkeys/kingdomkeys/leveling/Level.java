package online.kingdomkeys.kingdomkeys.leveling;


import net.minecraft.resources.ResourceLocation;

public class Level {

	ResourceLocation name;
	int maxLevel;

	// String translationKey;

	private LevelingData data;

	public Level(ResourceLocation registryName) {
		this.name = registryName;
		this.maxLevel = 100;
	}

	public void setLevelingData(LevelingData data) {
		this.data = data;
	}

	public LevelingData getLevelingData() {
		return data;
	}

	public String getName() {
		return name.toString();
	}

	public int getStr(int level) {
		return data.getStr(level);
	}

	public int getMag(int level) {
		return data.getMag(level);
	}

	public int getDef(int level) {
		return data.getDef(level);
	}

	public int getMaxAP(int level) {
		return data.getMaxAP(level);
	}

	public int getMaxHp(int level) {
		return data.getMaxHp(level);
	}

	public int getMaxMp(int level) {
		return data.getMaxMp(level);
	}

	public String[] getAbilities(int level) {
		return data.getAbilities(level);
	}

	public String[] getShotlocks(int level) {
		return data.getShotlocks(level);
	}
	
	public String[] getSpells(int level) {
		return data.getSpells(level);
	}
	
	public int getMaxAccessories(int level) {
		return data.getMaxAccessories(level);
	}
	
	public int getMaxArmors(int level) {
		return data.getMaxArmors(level);
	}

	public ResourceLocation getRegistryName() {
		return name;
	}

}
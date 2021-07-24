package online.kingdomkeys.kingdomkeys.leveling;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class Level extends ForgeRegistryEntry<Level> {

	String name;
	int maxLevel;

	// String translationKey;

	private LevelingData data;

	public Level(ResourceLocation registryName) {
		this.name = registryName.toString();
		this.maxLevel = 100;
		setRegistryName(registryName);
		// translationKey = "level." + getRegistryName().getPath() + ".name";
	}

	public Level(String registryName) {
		this(new ResourceLocation(registryName));
	}

	public void setLevelingData(LevelingData data) {
		this.data = data;
	}

	public LevelingData getLevelingData() {
		return data;
	}

	public String getName() {
		return name;
	}

	// public String getTranslationKey() { return translationKey; }

	public int getStr(int level) {
		return data.getStr(level);
	}

	public int getMag(int level) {
		return data.getMag(level);
	}

	public int getDef(int level) {
		return data.getDef(level);
	}

	public int getAP(int level) {
		return data.getAP(level);
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

}
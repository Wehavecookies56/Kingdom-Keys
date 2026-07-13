package online.kingdomkeys.kingdomkeys.leveling;


import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.lib.KKRegistryObject;

public class Level implements KKRegistryObject {

	ResourceLocation name;
	int maxLevel;

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

	public ResourceLocation[] getAbilities(int level) {
		return data.getAbilities(level);
	}

	public ResourceLocation[] getShotlocks(int level) {
		return data.getShotlocks(level);
	}
	
	public ResourceLocation[] getSpells(int level) {
		return data.getSpells(level);
	}
	
	public int getMaxAccessories(int level) {
		return data.getMaxAccessories(level);
	}
	
	public int getMaxArmors(int level) {
		return data.getMaxArmors(level);
	}

	public int getMaxMagics(int level) {
		return data.getMaxMagics(level);
	}

	public int getVersion() {
		return data.getVersion();
	}

	@Override
	public ResourceLocation getRegistryName() {
		return name;
	}

}
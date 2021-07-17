package online.kingdomkeys.kingdomkeys.leveling;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.registries.ForgeRegistryEntry;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.driveform.DriveFormData;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;

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
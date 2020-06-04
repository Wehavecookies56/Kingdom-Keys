package online.kingdomkeys.kingdomkeys.driveform;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.registries.ForgeRegistryEntry;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.lib.Utils;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;

public abstract class DriveForm extends ForgeRegistryEntry<DriveForm> {
	// Level 0-7 (0 unused)
	public static final float[] VALOR_JUMP_BOOST = { 0, 0.02F, 0.02F, 0.03F, 0.03F, 0.04F, 0.04F, 0.05F };
	public static final float[] MASTER_AERIAL_DODGE_BOOST = { 0, 1, 1, 1.2F, 1.2F, 1.4F, 1.4F, 1.6F };
	public static final float[] FINAL_JUMP_BOOST = { 0, 0.02F, 0.02F, 0.025F, 0.025F, 0.03F, 0.03F, 0.04F };
	public static final float[] FINAL_GLIDE = { 0, -0.12F, -0.12F, -0.08F, -0.08F, -0.04F, -0.04F, -0.01F };
	String name;
	int driveCost;
	int ap;
	int[] levelUpCosts;// {0,X,X,X,X,X,X}
	int maxLevel;

	public DriveForm(String registryName) {
		this.name = registryName;
		this.maxLevel = 7;
		setRegistryName(registryName);
	}

	public String getName() {
		return name;
	}

	public int getDriveCost() {
		return driveCost;
	}

	public int getFormAntiPoints() {
		return ap;
	}

	public int[] getLevelUpCosts() {
		return levelUpCosts;
	}

	public abstract String getBaseAbilityForLevel(int driveFormLevel);

	public abstract String getDFAbilityForLevel(int driveFormLevel); // TODO make the ability registry

	public int getLevelUpCost(int level) {
		return levelUpCosts[level - 1];
	}

	public int getLevelFromExp(int exp) {
		for (int i = 0; i < levelUpCosts.length; i++) {
			if (levelUpCosts[i] > exp) {
				return i;
			}
		}
		return getMaxLevel();
	}

	public int getMaxLevel() {
		return maxLevel;
	}

	public void initDrive(PlayerEntity player) {
		IPlayerCapabilities props = ModCapabilities.get(player);
		props.setActiveDriveForm(getName());
		int cost = ModDriveForms.registry.getValue(new ResourceLocation(getName())).getDriveCost();
		props.remDP(cost);
		props.setFP(200 + props.getDriveFormLevel(props.getActiveDriveForm()) * 100);
		// Summon Keyblades
		props.setAntiPoints(props.getAntiPoints() + getFormAntiPoints());
		player.world.playSound(player, player.getPosition(), ModSounds.drive.get(), SoundCategory.MASTER, 1.0f, 1.0f);
		PacketHandler.syncToAllAround(player, props);
	}

	public void updateDrive(PlayerEntity player) {
		IPlayerCapabilities props = ModCapabilities.get(player);
		if (props.getFP() > 0) {
			props.setFP(props.getFP() - 0.4);
		} else {
			endDrive(player);
		}
		// Consume FP
		// Check if FP <= 0 then end
	}

	public void endDrive(PlayerEntity player) {
		IPlayerCapabilities props = ModCapabilities.get(player);
		props.setActiveDriveForm("");
		player.world.playSound(player, player.getPosition(), ModSounds.unsummon.get(), SoundCategory.MASTER, 1.0f, 1.0f);
		PacketHandler.syncToAllAround(player, props);
	}

}
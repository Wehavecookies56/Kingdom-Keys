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

public class DriveForm extends ForgeRegistryEntry<DriveForm> {

	String name;
	int cost;
	int antiPoints;

	public DriveForm(String registryName, int cost, int antiPoints) {
		this.name = registryName;
		this.cost = cost;
		this.antiPoints = antiPoints;
		setRegistryName(registryName);
	}

	public String getName() {
		return name;
	}

	public int getCost() {
		return cost;
	}

	public int getFormAntiPoints() {
		return antiPoints;
	}

	public void initDrive(PlayerEntity player) {
		IPlayerCapabilities props = ModCapabilities.get(player);
		props.setActiveDriveForm(getName());
		int cost = ModDriveForms.registry.getValue(new ResourceLocation(getName())).getCost();
		props.remDP(cost);
		props.setFP(200 + Utils.getDriveFormLevel(props.getDriveFormsMap(), props.getActiveDriveForm()) * 100);
		// Summon Keyblades
		props.setAntiPoints(props.getAntiPoints() + getFormAntiPoints());
		player.world.playSound(player, player.getPosition(), ModSounds.drive.get(), SoundCategory.MASTER, 1.0f, 1.0f);
		PacketHandler.syncToAllAround(player, props);
	}

	public void updateDrive(PlayerEntity player) {
		IPlayerCapabilities props = ModCapabilities.get(player);
		if (props.getFP() > 0) {
			props.setFP(props.getFP() - 1);
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
package online.kingdomkeys.kingdomkeys.driveform;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.registries.ForgeRegistryEntry;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;

public class DriveForm extends ForgeRegistryEntry<DriveForm> {

	String name;
	int cost;

	public DriveForm(String registryName, int cost) {
		this.name = registryName;
		this.cost = cost;
		setRegistryName(registryName);
	}

	public String getName() {
		return name;
	}

	public int getCost() {
		return cost;
	}

	public void initDrive(PlayerEntity player) {
		IPlayerCapabilities props = ModCapabilities.get(player);
		props.setDriveForm(getName());
		//Set drive form points
		
		player.world.playSound(player, player.getPosition(), ModSounds.drive.get(), SoundCategory.MASTER, 1.0f, 1.0f);
		PacketHandler.syncToAllAround(player, props);
	}

	public void updateDrive(PlayerEntity player) {
		//Consume FP
		//Check if FP <= 0 then end
	}
	
	public void endDrive(PlayerEntity player) {
		IPlayerCapabilities props = ModCapabilities.get(player);
		props.setDriveForm("");
		player.world.playSound(player, player.getPosition(), ModSounds.unsummon.get(), SoundCategory.MASTER, 1.0f, 1.0f);
		PacketHandler.syncToAllAround(player, props);

	}

}
package online.kingdomkeys.kingdomkeys.driveform;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;

public class DriveFormAnti extends DriveForm {

	public DriveFormAnti(String registryName, int order, ResourceLocation skinRL, boolean hasKeychain, boolean baseGrowth) {
		super(registryName, order, hasKeychain, baseGrowth);
		this.color = new float[] { 0F, 0F, 0F };
		this.skinRL = skinRL;
	}
	
	@Override
	public String getBaseAbilityForLevel(int driveFormLevel) {
		return null;	
	}

	@Override
	public String getDFAbilityForLevel(int driveFormLevel) {
		return null;
	}

	@Override
	public boolean displayInCommandMenu(Player player) {
		return ModCapabilities.getPlayer(player).isAbilityEquipped(Strings.darkDomination);
	}

	@Override
	public void initDrive(Player player) {
		if (!getRegistryName().equals(NONE)) {
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			playerData.setActiveDriveForm(getName());
			int cost = 1000;
			if(playerData.isAbilityEquipped(Strings.darkDomination)){
				cost = ModDriveForms.registry.get().getValue(new ResourceLocation(getName())).getDriveCost();
			}
			playerData.remDP(cost);
			playerData.setFP(1000);
			playerData.setAntiPoints(playerData.getAntiPoints() + getFormAntiPoints());
			player.heal(player.getMaxHealth());

			if(getDriveSound() != null)
				player.level().playSound(null, player.blockPosition(), getDriveSound(), SoundSource.MASTER, 1.0f, 1.0f);
			pushEntities(player);
			PacketHandler.syncToAllAround(player, playerData);
		}
	}

	@Override
	public SoundEvent getDriveSound() {
		return ModSounds.antidrive.get();
	}
}
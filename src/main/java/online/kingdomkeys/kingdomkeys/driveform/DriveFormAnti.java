package online.kingdomkeys.kingdomkeys.driveform;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

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
		return false;
	}
}
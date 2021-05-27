package online.kingdomkeys.kingdomkeys.driveform;

import net.minecraft.util.ResourceLocation;

public class DriveFormAnti extends DriveForm {

	public DriveFormAnti(String registryName, int order, ResourceLocation skinRL, boolean hasKeychain) {
		super(registryName, order, hasKeychain);
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
}
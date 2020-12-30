package online.kingdomkeys.kingdomkeys.driveform;

import net.minecraft.util.ResourceLocation;
import online.kingdomkeys.kingdomkeys.lib.Strings;

public class DriveFormMaster extends DriveForm {

	public DriveFormMaster(String registryName, int order, ResourceLocation skinRL, boolean hasKeychain) {
		super(registryName, order, hasKeychain);
		this.driveCost = 400;
		this.ap = 1;
		this.levelUpCosts = new int[] {0, 60, 240, 456, 726, 1050, 1500};
		this.color = new float[] { 1F, 0.7F, 0.1F };
		this.skinRL = skinRL;
	}
	
	@Override
	public String getBaseAbilityForLevel(int driveFormLevel) {
		switch (driveFormLevel) {
		case 1:
			return "";
		case 2:
			return Strings.autoMaster;
		case 3:
			return Strings.aerialDodge;
		case 4:
			return "";
		case 5:
			return Strings.aerialDodge;
		case 6:
			return "";
		case 7:
			return Strings.aerialDodge;
		}
		return null;	
	}

	@Override
	public String getDFAbilityForLevel(int driveFormLevel) {
		switch (driveFormLevel) {
		case 1:
			return Strings.aerialDodge;
		case 2:
			return "";
		case 3:
			return Strings.aerialDodge;
		case 4:
			return "";
		case 5:
			return Strings.aerialDodge;
		case 6:
			return "";
		case 7:
			return Strings.aerialDodge;
		}
		return null;
	}
	
	//Hehe you won't find it here, it's in DriveOrbEntity#onPickup

}
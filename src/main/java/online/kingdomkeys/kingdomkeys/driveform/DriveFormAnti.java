package online.kingdomkeys.kingdomkeys.driveform;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

public class DriveFormAnti extends DriveForm {

	public DriveFormAnti(String registryName, int order, ResourceLocation skinRL, boolean hasKeychain) {
		super(registryName, order, hasKeychain);
		this.driveCost = 0;
		this.ap = -4;
		this.levelUpCosts = new int[] { 0, 80, 240, 520, 968, 1528, 2200 };
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
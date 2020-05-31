package online.kingdomkeys.kingdomkeys.driveform;

public class DriveFormFinal extends DriveForm {

	public DriveFormFinal(String registryName) {
		super(registryName);
		this.driveCost = 500;
		this.ap = -10;
		this.levelUpCosts = new int[] {0, 20, 80, 152, 242, 350, 500};
	}

	@Override
	public String getBaseAbilityForLevel(int driveFormLevel) {
		switch (driveFormLevel) {
		case 1:
			return "";
		case 2:
			return "Auto Final";
		case 3:
			return "Glide LV 1";
		case 4:
			return "";
		case 5:
			return "Glide LV 2";
		case 6:
			return "";
		case 7:
			return "Glide LV 3";
		}
		return null;	
	}
	
	@Override
	public String getDFAbilityForLevel(int driveFormLevel) {
		switch (driveFormLevel) {
		case 1:
			return "Glide LV 1";
		case 2:
			return "";
		case 3:
			return "Glide LV 2";
		case 4:
			return "";
		case 5:
			return "Glide LV 3";
		case 6:
			return "";
		case 7:
			return "Glide MAX";
		}
		return null;
	}

}
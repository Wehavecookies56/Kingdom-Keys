package online.kingdomkeys.kingdomkeys.driveform;

public class DriveFormWisdom extends DriveForm {

	public DriveFormWisdom(String registryName) {
		super(registryName);
		this.driveCost = 300;
		this.ap = 1;
		this.levelUpCosts = new int[] { 0, 20, 80, 152, 242, 350, 500 };
	}
	
	@Override
	public String getBaseAbilityForLevel(int driveFormLevel) {
		switch (driveFormLevel) {
		case 1:
			return "";
		case 2:
			return "Auto Wisdom";
		case 3:
			return "Quick Run LV 1";
		case 4:
			return "";
		case 5:
			return "Quick Run LV 2";
		case 6:
			return "";
		case 7:
			return "Quick Run LV 3";
		}
		return null;	
	}

	@Override
	public String getDFAbilityForLevel(int driveFormLevel) {
		switch (driveFormLevel) {
		case 1:
			return "Quick Run LV 1";
		case 2:
			return "";
		case 3:
			return "Quick Run LV 2";
		case 4:
			return "";
		case 5:
			return "Quick Run LV 3";
		case 6:
			return "";
		case 7:
			return "Quick Run MAX";
		}
		return null;
	}

}
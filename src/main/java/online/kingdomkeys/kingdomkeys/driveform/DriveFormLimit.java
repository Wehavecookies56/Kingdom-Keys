package online.kingdomkeys.kingdomkeys.driveform;

public class DriveFormLimit extends DriveForm {

	public DriveFormLimit(String registryName) {
		super(registryName);
		this.driveCost = 400;
		this.ap = 1;
		this.levelUpCosts = new int[] {0, 3, 9, 21, 40, 63, 90};
	}
	
	@Override
	public String getBaseAbilityForLevel(int driveFormLevel) {
		switch (driveFormLevel) {
		case 1:
			return "";
		case 2:
			return "Auto Limit";
		case 3:
			return "Dodge Roll LV 1";
		case 4:
			return "";
		case 5:
			return "Dodge Roll LV 2";
		case 6:
			return "";
		case 7:
			return "Dodge Roll LV 3";
		}
		return null;	
	}

	@Override
	public String getDFAbilityForLevel(int driveFormLevel) {
		switch (driveFormLevel) {
		case 1:
			return "Dodge Roll LV 1";
		case 2:
			return "";
		case 3:
			return "Dodge Roll LV 2";
		case 4:
			return "";
		case 5:
			return "Dodge Roll LV 3";
		case 6:
			return "";
		case 7:
			return "Dodge Roll LV MAX";
		}
		return null;
	}

}
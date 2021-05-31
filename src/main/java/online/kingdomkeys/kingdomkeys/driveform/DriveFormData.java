package online.kingdomkeys.kingdomkeys.driveform;

/**
 * Stores the data loaded from the driveforms datapack
 */
public class DriveFormData {

    float strMult, magMult, speedMult;
    int cost, ap;
    int[] levelUp;
    
    public DriveFormData() {

    }

    public DriveFormData(int cost, int ap, float strMult, float magMult, float speedMult, int[] levelUp) {
    	this.cost = cost;
    	this.ap = ap;
		this.strMult = strMult;
		this.magMult = magMult;
		this.speedMult = speedMult;
		this.levelUp = levelUp;
	}

    public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}
	
	public int getAP() {
		return ap;
	}

	public void setAP(int ap) {
		this.ap = ap;
	}
	
	public float getStrMult() {
		return strMult;
	}

	public void setStrMult(float strMult) {
		this.strMult = strMult;
	}

	public float getMagMult() {
		return magMult;
	}

	public void setMagMult(float magMult) {
		this.magMult = magMult;
	}

	public float getSpeedMult() {
		return speedMult;
	}

	public void setSpeedMult(float speedMult) {
		this.speedMult = speedMult;
	}
	
	public int[] getLevelUp() {
		return levelUp;
	}
	
	public void setLevelUp(int[] levelup) {
		this.levelUp = levelup;
	}

}

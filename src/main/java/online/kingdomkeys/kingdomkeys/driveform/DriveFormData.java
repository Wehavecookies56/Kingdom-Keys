package online.kingdomkeys.kingdomkeys.driveform;

import java.util.List;

/**
 * Stores the data loaded from the driveforms datapack
 */
public class DriveFormData {

    float strMult, magMult, speedMult;
    int cost, ap;
    int[] levelUp;
    List<String> abilities;
    List<String> baseLevelUpAbilities;
    List<String> dfLevelUpAbilities;
    boolean canGoAnti;
    
    public DriveFormData() {

    }

    public DriveFormData(int cost, int ap, float strMult, float magMult, float speedMult, int[] levelUp, boolean canGoAnti) {
    	this.cost = cost;
    	this.ap = ap;
		this.strMult = strMult;
		this.magMult = magMult;
		this.speedMult = speedMult;
		this.levelUp = levelUp;
		this.canGoAnti = canGoAnti;
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
	
	public List<String> getAbilities() {
		return this.abilities;
	}

	public void setAbilities(List<String> array) {
		this.abilities = array;
	}

	public List<String> getBaseLevelUpAbilities() {
		return this.baseLevelUpAbilities;
	}

	public void setBaseLevelUpAbilities(List<String> array) {
		this.baseLevelUpAbilities = array;
	}
	
	public String getBaseAbilityForLevel(int driveFormLevel) {
		return this.baseLevelUpAbilities.get(driveFormLevel);
	}
	
	public List<String> getDFLevelUpAbilities() {
		return this.dfLevelUpAbilities;
	}

	public void setDFLevelUpAbilities(List<String> array) {
		this.dfLevelUpAbilities = array;
	}
	
	public String getDFAbilityForLevel(int driveFormLevel) {
		return this.dfLevelUpAbilities.get(driveFormLevel);
	}
	
	public boolean canGoAnti() {
		return this.canGoAnti;
	}
	
	public void setCanGoAnti(boolean canGoAnti) {
		this.canGoAnti = canGoAnti;
	}

}

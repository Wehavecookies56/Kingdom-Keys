package online.kingdomkeys.kingdomkeys.magic;

/**
 * Stores the data loaded from the driveforms datapack
 */
public class MagicData {

    float dmgMult;
    int cost, cd;
    
    public MagicData() {

    }

    public MagicData(int cost, int cd, float dmgMult, float magMult) {
    	this.cost = cost;
    	this.cd = cd;
		this.dmgMult = dmgMult;
	}

    public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}
	
	public int getCooldown() {
		return cd;
	}

	public void setCooldown(int cd) {
		this.cd = cd;
	}
	
	public float getDmgMult() {
		return dmgMult;
	}

	public void setDmgMult(float dmgMult) {
		this.dmgMult = dmgMult;
	}

}

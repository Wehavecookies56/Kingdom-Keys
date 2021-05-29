package online.kingdomkeys.kingdomkeys.magic;

/**
 * Stores the data loaded from the driveforms datapack
 */
public class MagicData {

    float[] dmgMult = new float[4];
    int[] cost = new int[4];
    int[] cd = new int[4];
    int[] usesToGM = new int[4];
    
    public MagicData() {

    }

    public MagicData(int level, int cost, int cd, float dmgMult, float magMult, int usesToGM) {
    	this.cost[level] = cost;
    	this.cd[level] = cd;
		this.dmgMult[level] = dmgMult;
		this.usesToGM[level] = usesToGM;
	}

    public int getCost(int lvl) {
		return cost[lvl];
	}

	public void setCost(int lvl, int cost) {
		this.cost[lvl] = cost;
	}
	
	public int getCooldown(int lvl) {
		return cd[lvl];
	}

	public void setCooldown(int lvl, int cd) {
		this.cd[lvl] = cd;
	}
	
	public float getDmgMult(int lvl) {
		return dmgMult[lvl];
	}

	public void setDmgMult(int lvl, float dmgMult) {
		this.dmgMult[lvl] = dmgMult;
	}

	public int getUsesToGM(int lvl) {
		return usesToGM[lvl];
	}
	
	public void setUsesToGM(int lvl, int uses) {
		this.usesToGM[lvl] = uses;
	}

}

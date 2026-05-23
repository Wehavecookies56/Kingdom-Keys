package online.kingdomkeys.kingdomkeys.magic;

/**
 * Stores the data loaded from the magics datapack
 */
public class MagicData {

    float[] dmgMult = new float[4];
    int[] cost = new int[4];
    int[] ct = new int[4];
    int[] cd = new int[4];
    boolean[] magicLockOn = new boolean[4];
	int[] maxExp = new int[4];

    public MagicData() {

    }

    public MagicData(int level, int cost, int ct, int cd, float dmgMult, float magMult, boolean magicLockOn, int maxExp) {
    	this.cost[level] = cost;
    	this.ct[level] = ct;
    	this.cd[level] = cd;
		this.dmgMult[level] = dmgMult;
		this.magicLockOn[level] = magicLockOn;
		this.maxExp[level] = maxExp;
	}

    public int getCost(int lvl) {
		return cost[lvl];
	}

	public void setCost(int lvl, int cost) {
		this.cost[lvl] = cost;
	}

	public int getCasttime(int lvl) {
		return ct[lvl];
	}

	public void setCasttime(int lvl, int ct) {
		this.ct[lvl] = ct;
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

	public boolean getMagicLockOn(int lvl) {
		return this.magicLockOn[lvl];
	}

	public void setMagicLockon(int lvl, boolean lockOn) {
		this.magicLockOn[lvl] = lockOn;
	}

	public int getMaxExp(int lvl) {
		return this.maxExp[lvl];
	}

	public void setMaxExp(int lvl, int maxExp) {
		this.maxExp[lvl] = maxExp;
	}
}

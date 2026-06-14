package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.resources.ResourceLocation;

/**
 * Stores the data loaded from the magics datapack
 */
public class MagicData {

	private float dmgMult;
	private float dmgMultMax;
	private int cost;
	private int ct;
	private int cd;
	private boolean magicLockOn;
	private int maxExp;
	private int maxLevel;
	private ResourceLocation nextTier, magicRC;

	public MagicData() {}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public int getCasttime() {
		return ct;
	}

	public void setCasttime(int ct) {
		this.ct = ct;
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

	public float getDmgMultMax() {
		return dmgMultMax > 0 ? dmgMultMax : dmgMult;
	}

	public void setDmgMultMax(float dmgMultMax) {
		this.dmgMultMax = dmgMultMax;
	}

	public boolean getMagicLockOn() {
		return magicLockOn;
	}

	public void setMagicLockon(boolean lockOn) {
		this.magicLockOn = lockOn;
	}

	public int getMaxExp() {
		return maxExp;
	}

	public void setMaxExp(int maxExp) {
		this.maxExp = maxExp;
	}

	public int getMaxLevel() {
		return maxLevel;
	}

	public void setMaxLevel(int level) {
		this.maxLevel = level;
	}

	public ResourceLocation getNextTier() {
		return nextTier;
	}

	public void setNextTier(ResourceLocation nextTier) {
		this.nextTier = nextTier;
	}

	public ResourceLocation getMagicRC() {
		return magicRC;
	}

	public void setMagicRC(ResourceLocation rc) {
		this.magicRC = rc;
	}
}
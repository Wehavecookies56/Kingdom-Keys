package online.kingdomkeys.kingdomkeys.shotlock;

public class ShotlockData {

	int cooldown; // ticks between each lock-on while aiming
	int cooldownMax; // fastest cooldown once the item reaches maxLevel - interpolated between cooldown and this
	int max; // max number of locks
	float dmgMult;
	float dmgMultMax; // damage multiplier once the item reaches maxLevel - interpolated between dmgMult and this
	int maxExp;
	int maxLevel;
	String element = "";
	// Which follow-up minigame a full Shotlock triggers: mash, mash_dash, timing, keys or none
	String minigame = "";

	public ShotlockData() {
	}

	public ShotlockData(int cooldown, int max, float dmgMult, String element) {
		this(cooldown, max, dmgMult, dmgMult, 0, 1, element);
	}

	public ShotlockData(int cooldown, int max, float dmgMult, float dmgMultMax, int maxExp, int maxLevel, String element) {
		this.cooldown = cooldown;
		this.max = max;
		this.dmgMult = dmgMult;
		this.dmgMultMax = dmgMultMax;
		this.maxExp = maxExp;
		this.maxLevel = maxLevel;
		this.element = element == null ? "" : element;
	}

	public int getCooldown() {
		return cooldown;
	}

	public void setCooldown(int cooldown) {
		this.cooldown = cooldown;
	}

	public int getCooldownMax() {
		return cooldownMax == 0 ? cooldown : cooldownMax;
	}

	public void setCooldownMax(int cooldownMax) {
		this.cooldownMax = cooldownMax;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public float getDmgMult() {
		return dmgMult;
	}

	public void setDmgMult(float dmgMult) {
		this.dmgMult = dmgMult;
	}

	public float getDmgMultMax() {
		return dmgMultMax == 0 ? dmgMult : dmgMultMax;
	}

	public void setDmgMultMax(float dmgMultMax) {
		this.dmgMultMax = dmgMultMax;
	}

	public int getMaxExp() {
		return maxExp;
	}

	public void setMaxExp(int maxExp) {
		this.maxExp = maxExp;
	}

	public int getMaxLevel() {
		return maxLevel == 0 ? 1 : maxLevel;
	}

	public void setMaxLevel(int maxLevel) {
		this.maxLevel = maxLevel;
	}

	public String getElement() {
		return element;
	}

	public void setElement(String element) {
		this.element = element == null ? "" : element;
	}

	public String getMinigame() {
		return minigame;
	}

	public void setMinigame(String minigame) {
		this.minigame = minigame == null ? "" : minigame;
	}
}

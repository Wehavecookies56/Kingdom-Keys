package online.kingdomkeys.kingdomkeys.shotlock;

public class ShotlockData {

	int cooldown; // ticks between each lock-on while aiming
	int max; // max number of locks
	float dmgMult;
	String element = ""; // "" = no elemental override (generic damage) - else a KKDamageTypes registry path

	public ShotlockData() {
	}

	public ShotlockData(int cooldown, int max, float dmgMult, String element) {
		this.cooldown = cooldown;
		this.max = max;
		this.dmgMult = dmgMult;
		this.element = element == null ? "" : element;
	}

	public int getCooldown() {
		return cooldown;
	}

	public void setCooldown(int cooldown) {
		this.cooldown = cooldown;
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

	public String getElement() {
		return element;
	}

	public void setElement(String element) {
		this.element = element == null ? "" : element;
	}
}

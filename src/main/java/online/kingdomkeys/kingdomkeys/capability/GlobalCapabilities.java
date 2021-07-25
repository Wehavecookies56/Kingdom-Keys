package online.kingdomkeys.kingdomkeys.capability;

public class GlobalCapabilities implements IGlobalCapabilities {
	
	private int timeStopped, flatTicks;
	float stopDmg;
	private String stopCaster;

	@Override
	public void setStoppedTicks(int time) {
		this.timeStopped = time;
	}

	@Override
	public int getStoppedTicks() {
		return timeStopped;
	}

	@Override
	public void subStoppedTicks(int time) {
		this.timeStopped -= time;
	}

	@Override
	public float getDamage() {
		return stopDmg;
	}

	@Override
	public void setDamage(float dmg) {
		this.stopDmg = dmg;
	}

	@Override
	public void addDamage(float dmg) {
		this.stopDmg+=dmg;
	}

	@Override
	public void setStopCaster(String name) {
		this.stopCaster = name;
	}

	@Override
	public String getStopCaster() {
		return this.stopCaster;
	}

	@Override
	public int getFlatTicks() {
		return flatTicks;
	}

	@Override
	public void setFlatTicks(int time) {
		this.flatTicks = time;
	}

	@Override
	public void subFlatTicks(int time) {
		this.flatTicks -= time;
	}
}

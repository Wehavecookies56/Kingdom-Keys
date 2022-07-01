package online.kingdomkeys.kingdomkeys.capability;

import net.minecraft.nbt.CompoundTag;

public class GlobalCapabilities implements IGlobalCapabilities {

	@Override
	public CompoundTag serializeNBT() {
		CompoundTag storage = new CompoundTag();
		storage.putInt("ticks_stopped", this.getStoppedTicks());
		storage.putFloat("stop_dmg", this.getDamage());
		storage.putInt("ticks_flat", this.getFlatTicks());
		storage.putBoolean("castle_oblivion_marker", this.getCastleOblivionMarker());
		return storage;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
		CompoundTag properties = (CompoundTag) nbt;
		this.setStoppedTicks(properties.getInt("ticks_stopped"));
		this.setDamage(properties.getFloat("stop_dmg"));
		this.setFlatTicks(properties.getInt("ticks_flat"));
		this.setCastleOblivionMarker(properties.getBoolean("castle_oblivion_marker"));
	}

	private int timeStopped, flatTicks;
	float stopDmg;
	private String stopCaster;
	private boolean castleOblivionMarker;

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

	@Override
	public boolean getCastleOblivionMarker() {
		return castleOblivionMarker;
	}

	@Override
	public void setCastleOblivionMarker(boolean marker) {
		this.castleOblivionMarker = marker;
	}
}

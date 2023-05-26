package online.kingdomkeys.kingdomkeys.capability;

import net.minecraft.nbt.CompoundTag;

public class GlobalCapabilities implements IGlobalCapabilities {

	@Override
	public CompoundTag serializeNBT() {
		CompoundTag storage = new CompoundTag();
		storage.putInt("ticks_stopped", this.getStoppedTicks());
		storage.putFloat("stop_dmg", this.getStopDamage());
		storage.putInt("ticks_flat", this.getFlatTicks());
		storage.putInt("aero_ticks", this.getAeroTicks());
		storage.putInt("aero_level", this.getAeroLevel());
		storage.putBoolean("castle_oblivion_marker", this.getCastleOblivionMarker());
		storage.putInt("level", this.getLevel());
		return storage;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
		CompoundTag properties = (CompoundTag) nbt;
		this.setStoppedTicks(properties.getInt("ticks_stopped"));
		this.setStopDamage(properties.getFloat("stop_dmg"));
		this.setFlatTicks(properties.getInt("ticks_flat"));
		this.setAeroTicks(properties.getInt("aero_ticks"), properties.getInt("aero_level"));
		this.setCastleOblivionMarker(properties.getBoolean("castle_oblivion_marker"));
		this.setLevel(properties.getInt("level"));
	}

	private int timeStopped, flatTicks, aeroTicks, aeroLevel, level, stopModelTicks;
	float stopDmg;
	private String stopCaster;
	private boolean castleOblivionMarker;

	@Override
	public void setLevel(int lvl) {
		this.level = lvl;
	}

	@Override
	public int getLevel() {
		return level;
	}
	
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
	public float getStopDamage() {
		return stopDmg;
	}

	@Override
	public void setStopDamage(float dmg) {
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
	public int getAeroLevel() {
		return aeroLevel;
	}

	@Override
	public void setAeroLevel(int level) {
		this.aeroLevel = level;
	}
	
	@Override
	public int getAeroTicks() {
		return aeroTicks;
	}

	@Override
	public void setAeroTicks(int i, int level) {
		aeroTicks = i;
		aeroLevel = level;
	}
	
	@Override
	public void remAeroTicks(int ticks) {
		aeroTicks -= ticks;
	}

	@Override
	public boolean getCastleOblivionMarker() {
		return castleOblivionMarker;
	}

	@Override
	public void setCastleOblivionMarker(boolean marker) {
		this.castleOblivionMarker = marker;
	}
	
	@Override
	public int getStopModelTicks() {
		return stopModelTicks;
	}

	@Override
	public void setStopModelTicks(int ticks) {
		this.stopModelTicks = ticks;		
	}
}

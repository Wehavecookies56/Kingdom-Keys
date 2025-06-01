package online.kingdomkeys.kingdomkeys.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.common.util.INBTSerializable;

import java.util.HashMap;
import java.util.Map;

public class GlobalData implements INBTSerializable<CompoundTag> {

	protected GlobalData() {}

	private static Map<Integer, GlobalData> mobDataClientCache = new HashMap<>();

	public static GlobalData get(LivingEntity entity) {
		if (!entity.hasData(ModData.GLOBAL_DATA)) {
			entity.setData(ModData.GLOBAL_DATA, new GlobalData());
		}
		return entity.getData(ModData.GLOBAL_DATA);
	}

	public static GlobalData getClient(LivingEntity entity) {
		return mobDataClientCache.get(entity.getId());
	}

	public static void setClientCache(LivingEntity entity, GlobalData data) {
		mobDataClientCache.put(entity.getId(), data);
	}

	public static void clearClientCache() {
		mobDataClientCache = new HashMap<>();
	}

	@Override
	public CompoundTag serializeNBT(HolderLookup.Provider provider) {
		CompoundTag storage = new CompoundTag();
		storage.putInt("ticks_stopped", this.getStoppedTicks());
		storage.putFloat("stop_dmg", this.getStopDamage());
		storage.putInt("ticks_flat", this.getFlatTicks());
		storage.putBoolean("castle_oblivion_marker", this.getCastleOblivionMarker());
		storage.putInt("level", this.getLevel());
		storage.putBoolean("is_ko", isKO);
		return storage;
	}

	@Override
	public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        this.setStoppedTicks(nbt.getInt("ticks_stopped"));
		this.setStopDamage(nbt.getFloat("stop_dmg"));
		this.setFlatTicks(nbt.getInt("ticks_flat"));
		this.setCastleOblivionMarker(nbt.getBoolean("castle_oblivion_marker"));
		this.setLevel(nbt.getInt("level"));
		this.setKO(nbt.getBoolean("is_ko"));
	}

	private int timeStopped, flatTicks, aeroTicks, aeroLevel, level, stopModelTicks;
	float stopDmg;
	private String stopCaster;
	private boolean castleOblivionMarker, isKO;

	public void setLevel(int lvl) {
		this.level = lvl;
	}


	public int getLevel() {
		return level;
	}
	

	public void setStoppedTicks(int time) {
		this.timeStopped = time;
	}


	public int getStoppedTicks() {
		return timeStopped;
	}


	public void subStoppedTicks(int time) {
		this.timeStopped -= time;
	}


	public float getStopDamage() {
		return stopDmg;
	}


	public void setStopDamage(float dmg) {
		this.stopDmg = dmg;
	}


	public void addDamage(float dmg) {
		this.stopDmg+=dmg;
	}


	public void setStopCaster(String name) {
		this.stopCaster = name;
	}


	public String getStopCaster() {
		return this.stopCaster;
	}


	public int getFlatTicks() {
		return flatTicks;
	}


	public void setFlatTicks(int time) {
		this.flatTicks = time;
	}


	public void subFlatTicks(int time) {
		this.flatTicks -= time;
	}


	public boolean getCastleOblivionMarker() {
		return castleOblivionMarker;
	}


	public void setCastleOblivionMarker(boolean marker) {
		this.castleOblivionMarker = marker;
	}


	public int getStopModelTicks() {
		return stopModelTicks;
	}


	public void setStopModelTicks(int ticks) {
		this.stopModelTicks = ticks;		
	}


	public boolean isKO() {
		return isKO;
	}


	public void setKO(boolean ko) {
		this.isKO = ko;
	}
}

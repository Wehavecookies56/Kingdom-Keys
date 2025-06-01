package online.kingdomkeys.kingdomkeys.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.common.util.INBTSerializable;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

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
		KingdomKeys.LOGGER.debug(mobDataClientCache.get(5));
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
		storage.putFloat("stop_dmg", this.getStopDamage());
		storage.putBoolean("castle_oblivion_marker", this.getCastleOblivionMarker());
		storage.putInt("level", this.getLevel());
		storage.putBoolean("is_ko", isKO);
		return storage;
	}

	@Override
	public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
		this.setStopDamage(nbt.getFloat("stop_dmg"));
		this.setCastleOblivionMarker(nbt.getBoolean("castle_oblivion_marker"));
		this.setLevel(nbt.getInt("level"));
		this.setKO(nbt.getBoolean("is_ko"));
	}

	private int level, stopModelTicks;
	float stopDmg;
	private String stopCaster;
	private boolean castleOblivionMarker, isKO;

	public void setLevel(int lvl) {
		this.level = lvl;
	}


	public int getLevel() {
		return level;
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

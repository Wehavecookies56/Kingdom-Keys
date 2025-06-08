package online.kingdomkeys.kingdomkeys.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.common.util.INBTSerializable;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.shotlock.ModShotlocks;

import java.util.ArrayList;
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

		ListTag dmgList = new ListTag();
		for (float unit : this.stopDmg) {
			dmgList.add(FloatTag.valueOf(unit));
		}
		storage.put("stop_dmg", dmgList);

		storage.putBoolean("castle_oblivion_marker", this.getCastleOblivionMarker());
		storage.putInt("level", this.getLevel());
		if (this.getStopCaster() != null) {
			storage.putString("stop_caster", this.getStopCaster());
		}
		storage.putInt("stop_model_ticks", this.getStopModelTicks());
		return storage;
	}

	@Override
	public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
		stopDmg.clear();
		ListTag floatListTag = nbt.getList("stop_dmg", Tag.TAG_FLOAT);
		for (int i = 0; i < floatListTag.size(); i++) {
			stopDmg.add(floatListTag.getFloat(i));
		}

		this.setCastleOblivionMarker(nbt.getBoolean("castle_oblivion_marker"));
		this.setLevel(nbt.getInt("level"));
		if (nbt.contains("stop_caster")) {
			this.setStopCaster(nbt.getString("stop_caster"));
		}
		this.setStopModelTicks(nbt.getInt("stop_model_ticks"));
	}

	private int level, stopModelTicks;
	ArrayList<Float> stopDmg = new ArrayList<>();
	private String stopCaster;
	private boolean castleOblivionMarker;

	public void setLevel(int lvl) {
		this.level = lvl;
	}


	public int getLevel() {
		return level;
	}


	public ArrayList<Float> getStopDamage() {
		return stopDmg;
	}


	public void setStopDamage(ArrayList<Float> dmg) {
		this.stopDmg = dmg;
	}


	public void addDamage(float dmg) {
		this.stopDmg.add(dmg);
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
}

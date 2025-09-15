package online.kingdomkeys.kingdomkeys.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

import java.util.ArrayList;

public class GlobalCapabilities implements IGlobalCapabilities {

	@Override
	public CompoundTag serializeNBT() {
		CompoundTag storage = new CompoundTag();
		ListTag dmgList = new ListTag();

		for (float unit : this.stopDmg) {
			dmgList.add(FloatTag.valueOf(unit));
		}

		storage.put("stop_dmg", dmgList);
		storage.putBoolean("castle_oblivion_marker", this.getCastleOblivionMarker());
		storage.putInt("level", this.getLevel());
		return storage;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
		CompoundTag properties = nbt;
		stopDmg.clear();
		ListTag floatListTag = properties.getList("stop_dmg", Tag.TAG_FLOAT);
		for (int i = 0; i < floatListTag.size(); i++) {
			stopDmg.add(floatListTag.getFloat(i));
		}
		this.setCastleOblivionMarker(properties.getBoolean("castle_oblivion_marker"));
		this.setLevel(properties.getInt("level"));
	}

	private int level, stopModelTicks;
	ArrayList<Float> stopDmg = new ArrayList<>();
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
	public ArrayList<Float> getStopDamage() {
		return stopDmg ;//== null ? new ArrayList<>() : stopDmg;
	}

	@Override
	public void setStopDamage(ArrayList<Float> dmg) {
		this.stopDmg = dmg;
	}

	@Override
	public void addDamage(float dmg) {
		this.stopDmg.add(dmg);
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

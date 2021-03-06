package online.kingdomkeys.kingdomkeys.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class GlobalCapabilities implements IGlobalCapabilities {

	public static class Storage implements IStorage<IGlobalCapabilities> {
		@Override
		public INBT writeNBT(Capability<IGlobalCapabilities> capability, IGlobalCapabilities instance, Direction side) {
			CompoundNBT storage = new CompoundNBT();
			storage.putInt("ticks_stopped", instance.getStoppedTicks());
			storage.putFloat("stop_dmg", instance.getDamage());
			storage.putInt("ticks_flat", instance.getFlatTicks());
			return storage;
		}

		@Override
		public void readNBT(Capability<IGlobalCapabilities> capability, IGlobalCapabilities instance, Direction side, INBT nbt) {
			CompoundNBT properties = (CompoundNBT) nbt;
			instance.setStoppedTicks(properties.getInt("ticks_stopped"));
			instance.setDamage(properties.getFloat("stop_dmg"));
			instance.setFlatTicks(properties.getInt("ticks_flat"));
		}
	}

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

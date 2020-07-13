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
			CompoundNBT props = new CompoundNBT();
			props.putInt("ticks_stopped", instance.getStoppedTicks());
			props.putInt("stop_dmg", instance.getDamage());
			props.putInt("ticks_flat", instance.getFlatTicks());
			return props;
		}

		@Override
		public void readNBT(Capability<IGlobalCapabilities> capability, IGlobalCapabilities instance, Direction side, INBT nbt) {
			CompoundNBT properties = (CompoundNBT) nbt;
			instance.setStoppedTicks(properties.getInt("ticks_stopped"));
			instance.setDamage(properties.getInt("stop_dmg"));
			instance.setFlatTicks(properties.getInt("ticks_flat"));
		}
	}

	private int timeStopped, stopDmg, flatTicks;
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
	public int getDamage() {
		return stopDmg;
	}

	@Override
	public void setDamage(int dmg) {
		this.stopDmg = dmg;
	}

	@Override
	public void addDamage(int dmg) {
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

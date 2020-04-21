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
			props.putBoolean("stopped", instance.getStopped());
			return props;
		}

		@Override
		public void readNBT(Capability<IGlobalCapabilities> capability, IGlobalCapabilities instance, Direction side, INBT nbt) {
			CompoundNBT properties = (CompoundNBT) nbt;
			instance.setStoppedTicks(properties.getInt("ticks_stopped"));
			instance.setStopped(properties.getBoolean("stopped"));
		}
	}

	private int timeStopped;

	private boolean stopped;

	@Override
	public void setStopped(boolean b) {
		this.stopped = b;
	}

	@Override
	public boolean getStopped() {
		return this.stopped;
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
}

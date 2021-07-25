package online.kingdomkeys.kingdomkeys.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class GlobalCapabilitiesProvider implements ICapabilitySerializable<CompoundTag> {
	IGlobalCapabilities instance = new GlobalCapabilities();

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		return ModCapabilities.GLOBAL_CAPABILITIES.orEmpty(cap, LazyOptional.of(() -> instance));
	}

	@Override
	public CompoundTag serializeNBT() {
		CompoundTag storage = new CompoundTag();
		storage.putInt("ticks_stopped", instance.getStoppedTicks());
		storage.putFloat("stop_dmg", instance.getDamage());
		storage.putInt("ticks_flat", instance.getFlatTicks());
		return storage;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
		CompoundTag properties = (CompoundTag) nbt;
		instance.setStoppedTicks(properties.getInt("ticks_stopped"));
		instance.setDamage(properties.getFloat("stop_dmg"));
		instance.setFlatTicks(properties.getInt("ticks_flat"));
	}

}

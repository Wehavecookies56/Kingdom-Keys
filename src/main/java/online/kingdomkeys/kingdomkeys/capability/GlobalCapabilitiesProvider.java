package online.kingdomkeys.kingdomkeys.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class GlobalCapabilitiesProvider implements ICapabilitySerializable<CompoundTag> {
	IGlobalCapabilities instance = ModCapabilities.GLOBAL_CAPABILITIES.getDefaultInstance();

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		return ModCapabilities.GLOBAL_CAPABILITIES.orEmpty(cap, LazyOptional.of(() -> instance));
	}

	@Override
	public CompoundTag serializeNBT() {
		return (CompoundTag) ModCapabilities.GLOBAL_CAPABILITIES.getStorage().writeNBT(ModCapabilities.GLOBAL_CAPABILITIES, instance, null);
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
		ModCapabilities.GLOBAL_CAPABILITIES.getStorage().readNBT(ModCapabilities.GLOBAL_CAPABILITIES, instance, null, nbt);
	}

}

package online.kingdomkeys.kingdomkeys.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class GlobalCapabilitiesProvider implements ICapabilitySerializable<CompoundNBT> {
	IGlobalCapabilities instance = ModCapabilities.GLOBAL_CAPABILITIES.getDefaultInstance();

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		return ModCapabilities.GLOBAL_CAPABILITIES.orEmpty(cap, LazyOptional.of(() -> instance));
	}

	@Override
	public CompoundNBT serializeNBT() {
		return (CompoundNBT) ModCapabilities.GLOBAL_CAPABILITIES.getStorage().writeNBT(ModCapabilities.GLOBAL_CAPABILITIES, instance, null);
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		ModCapabilities.GLOBAL_CAPABILITIES.getStorage().readNBT(ModCapabilities.GLOBAL_CAPABILITIES, instance, null, nbt);
	}

}

package online.kingdomkeys.kingdomkeys.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class CapabilitiesProvider implements ICapabilitySerializable<CompoundNBT> {
	ILevelCapabilities instance = ModCapabilities.LEVEL_CAPABILITIES.getDefaultInstance();

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		return ModCapabilities.LEVEL_CAPABILITIES.orEmpty(cap, LazyOptional.of(() -> instance));
	}

	@Override
	public CompoundNBT serializeNBT() {
		return (CompoundNBT) ModCapabilities.LEVEL_CAPABILITIES.getStorage().writeNBT(ModCapabilities.LEVEL_CAPABILITIES, instance, null);
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		ModCapabilities.LEVEL_CAPABILITIES.getStorage().readNBT(ModCapabilities.LEVEL_CAPABILITIES, instance, null, nbt);
	}

}

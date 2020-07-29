package online.kingdomkeys.kingdomkeys.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class WorldCapabilitiesProvider implements ICapabilitySerializable<CompoundNBT> {
	IWorldCapabilities instance = ModCapabilities.WORLD_CAPABILITIES.getDefaultInstance();

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		return ModCapabilities.WORLD_CAPABILITIES.orEmpty(cap, LazyOptional.of(() -> instance));
	}

	@Override
	public CompoundNBT serializeNBT() {
		return (CompoundNBT) ModCapabilities.WORLD_CAPABILITIES.getStorage().writeNBT(ModCapabilities.WORLD_CAPABILITIES, instance, null);
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		ModCapabilities.WORLD_CAPABILITIES.getStorage().readNBT(ModCapabilities.WORLD_CAPABILITIES, instance, null, nbt);
	}

}

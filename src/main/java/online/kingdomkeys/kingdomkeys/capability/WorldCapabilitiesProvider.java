package online.kingdomkeys.kingdomkeys.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class WorldCapabilitiesProvider implements ICapabilitySerializable<CompoundTag> {
	IWorldCapabilities instance = ModCapabilities.WORLD_CAPABILITIES.getDefaultInstance();

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		return ModCapabilities.WORLD_CAPABILITIES.orEmpty(cap, LazyOptional.of(() -> instance));
	}

	@Override
	public CompoundTag serializeNBT() {
		return (CompoundTag) ModCapabilities.WORLD_CAPABILITIES.getStorage().writeNBT(ModCapabilities.WORLD_CAPABILITIES, instance, null);
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
		ModCapabilities.WORLD_CAPABILITIES.getStorage().readNBT(ModCapabilities.WORLD_CAPABILITIES, instance, null, nbt);
	}

}

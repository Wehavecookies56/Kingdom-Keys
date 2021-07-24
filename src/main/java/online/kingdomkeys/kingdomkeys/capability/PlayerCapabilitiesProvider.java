package online.kingdomkeys.kingdomkeys.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class PlayerCapabilitiesProvider implements ICapabilitySerializable<CompoundTag> {
	IPlayerCapabilities instance = ModCapabilities.PLAYER_CAPABILITIES.getDefaultInstance();

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		return ModCapabilities.PLAYER_CAPABILITIES.orEmpty(cap, LazyOptional.of(() -> instance));
	}

	@Override
	public CompoundTag serializeNBT() {
		return (CompoundTag) ModCapabilities.PLAYER_CAPABILITIES.getStorage().writeNBT(ModCapabilities.PLAYER_CAPABILITIES, instance, null);
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
		ModCapabilities.PLAYER_CAPABILITIES.getStorage().readNBT(ModCapabilities.PLAYER_CAPABILITIES, instance, null, nbt);
	}

}

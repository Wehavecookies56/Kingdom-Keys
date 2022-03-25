package online.kingdomkeys.kingdomkeys.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class PlayerCapabilitiesProvider implements ICapabilitySerializable<CompoundTag> {
	private final IPlayerCapabilities instance = new PlayerCapabilities();

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		return ModCapabilities.PLAYER_CAPABILITIES.orEmpty(cap, LazyOptional.of(() -> instance));
	}

	@Override
	public CompoundTag serializeNBT() {
		return instance.serializeNBT();
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
		instance.deserializeNBT(nbt);
	}

}

package online.kingdomkeys.kingdomkeys.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class PlayerCapabilitiesProvider implements ICapabilitySerializable<CompoundNBT> {
	IPlayerCapabilities instance = ModCapabilities.PLAYER_CAPABILITIES.getDefaultInstance();

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		return ModCapabilities.PLAYER_CAPABILITIES.orEmpty(cap, LazyOptional.of(() -> instance));
	}

	@Override
	public CompoundNBT serializeNBT() {
		return (CompoundNBT) ModCapabilities.PLAYER_CAPABILITIES.getStorage().writeNBT(ModCapabilities.PLAYER_CAPABILITIES, instance, null);
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		ModCapabilities.PLAYER_CAPABILITIES.getStorage().readNBT(ModCapabilities.PLAYER_CAPABILITIES, instance, null, nbt);
	}

}

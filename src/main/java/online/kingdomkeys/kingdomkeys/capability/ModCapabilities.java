package online.kingdomkeys.kingdomkeys.capability;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.util.LazyOptional;

public class ModCapabilities {

	@CapabilityInject(ILevelCapabilities.class)
	public static final Capability<ILevelCapabilities> LEVEL_CAPABILITIES = null;

	public static ILevelCapabilities get(PlayerEntity player) {
		LazyOptional<ILevelCapabilities> props = player.getCapability(ModCapabilities.LEVEL_CAPABILITIES, null);
		return props.orElse(null);
	}

	public static void register() {
		CapabilityManager.INSTANCE.register(ILevelCapabilities.class, new LevelCapabilities.Storage(), () -> new LevelCapabilities());
	}

}

package online.kingdomkeys.kingdomkeys.capability;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.util.LazyOptional;

public class ModCapabilities {

	@CapabilityInject(IPlayerCapabilities.class)
	public static final Capability<IPlayerCapabilities> PLAYER_CAPABILITIES = null;
	@CapabilityInject(IGlobalCapabilities.class)
	public static final Capability<IGlobalCapabilities> GLOBAL_CAPABILITIES = null;
	@CapabilityInject(IWorldCapabilities.class)
	public static final Capability<IWorldCapabilities> WORLD_CAPABILITIES = null;

	public static IPlayerCapabilities get(PlayerEntity player) {
		LazyOptional<IPlayerCapabilities> props = player.getCapability(ModCapabilities.PLAYER_CAPABILITIES, null);
		return props.orElse(null);
	}

	public static IGlobalCapabilities getGlobal(LivingEntity e) {
		LazyOptional<IGlobalCapabilities> props = e.getCapability(ModCapabilities.GLOBAL_CAPABILITIES, null);
		return props.orElse(null);
	}
	
	public static IWorldCapabilities getWorld(World w) {
		LazyOptional<IWorldCapabilities> props = w.getCapability(ModCapabilities.WORLD_CAPABILITIES, null);
		return props.orElse(null);
	}

	public static void register() {
		CapabilityManager.INSTANCE.register(IPlayerCapabilities.class, new PlayerCapabilities.Storage(), PlayerCapabilities::new);
		CapabilityManager.INSTANCE.register(IGlobalCapabilities.class, new GlobalCapabilities.Storage(), GlobalCapabilities::new);
		CapabilityManager.INSTANCE.register(IWorldCapabilities.class, new WorldCapabilities.Storage(), WorldCapabilities::new);
	}

}

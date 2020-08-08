package online.kingdomkeys.kingdomkeys.capability;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

public class ModCapabilities {

	@CapabilityInject(IPlayerCapabilities.class)
	public static final Capability<IPlayerCapabilities> PLAYER_CAPABILITIES = null;
	@CapabilityInject(IGlobalCapabilities.class)
	public static final Capability<IGlobalCapabilities> GLOBAL_CAPABILITIES = null;
	@CapabilityInject(IWorldCapabilities.class)
	public static final Capability<IWorldCapabilities> WORLD_CAPABILITIES = null;

	public static IPlayerCapabilities getPlayer(PlayerEntity player) {
		LazyOptional<IPlayerCapabilities> playerData = player.getCapability(ModCapabilities.PLAYER_CAPABILITIES, null);
		return playerData.orElse(null);
	}

	public static IGlobalCapabilities getGlobal(LivingEntity e) {
		LazyOptional<IGlobalCapabilities> globalData = e.getCapability(ModCapabilities.GLOBAL_CAPABILITIES, null);
		return globalData.orElse(null);
	}
	
	public static IWorldCapabilities getWorld(World w) {
		LazyOptional<IWorldCapabilities> worldData = w.getCapability(ModCapabilities.WORLD_CAPABILITIES, null);
		return worldData.orElse(null);
	}

	public static void register() {
		CapabilityManager.INSTANCE.register(IPlayerCapabilities.class, new PlayerCapabilities.Storage(), PlayerCapabilities::new);
		CapabilityManager.INSTANCE.register(IGlobalCapabilities.class, new GlobalCapabilities.Storage(), GlobalCapabilities::new);
		CapabilityManager.INSTANCE.register(IWorldCapabilities.class, new WorldCapabilities.Storage(), WorldCapabilities::new);
	}


	@SubscribeEvent
	public void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
		if (event.getObject() instanceof LivingEntity) {
			event.addCapability(new ResourceLocation(KingdomKeys.MODID, "global_capabilities"), new GlobalCapabilitiesProvider());
			if (event.getObject() instanceof PlayerEntity)
				event.addCapability(new ResourceLocation(KingdomKeys.MODID, "player_capabilities"), new PlayerCapabilitiesProvider());
		}
	}

	@SubscribeEvent
	public void attachWorldCapabilities(AttachCapabilitiesEvent<World> event) {
		event.addCapability(new ResourceLocation(KingdomKeys.MODID, "world_capabilities"), new WorldCapabilitiesProvider());
	}

	/*@SubscribeEvent
	public void onPlayerJoin(EntityJoinWorldEvent event) { //I used this to sync to the client the other players skins in RayCraft idk if it's gonna be useful for drive forms
		if (!event.getEntity().world.isRemote && event.getEntity() instanceof PlayerEntity) {
			ILevelCapabilities playerData = ModCapabilities.getPlayer((PlayerEntity) event.getEntity());
			PacketHandler.sendTo(new PacketSyncCapability(playerData), (ServerPlayerEntity) event.getEntity());
		}
	}*/

}

package online.kingdomkeys.kingdomkeys.capability;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

@Mod.EventBusSubscriber(modid = KingdomKeys.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModCapabilities {

	public static final Capability<IPlayerCapabilities> PLAYER_CAPABILITIES = CapabilityManager.get(new CapabilityToken<IPlayerCapabilities>() {});
	public static final Capability<IGlobalCapabilities> GLOBAL_CAPABILITIES = CapabilityManager.get(new CapabilityToken<IGlobalCapabilities>() {});
	public static final Capability<IWorldCapabilities> WORLD_CAPABILITIES = CapabilityManager.get(new CapabilityToken<IWorldCapabilities>() {});

	public static IPlayerCapabilities getPlayer(Player player) {
		LazyOptional<IPlayerCapabilities> playerData = player.getCapability(ModCapabilities.PLAYER_CAPABILITIES, null);
		return playerData.orElse(null);
	}

	public static IGlobalCapabilities getGlobal(LivingEntity e) {
		LazyOptional<IGlobalCapabilities> globalData = e.getCapability(ModCapabilities.GLOBAL_CAPABILITIES, null);
		return globalData.orElse(null);
	}
	
	public static IWorldCapabilities getWorld(Level w) {
		LazyOptional<IWorldCapabilities> worldData = w.getCapability(ModCapabilities.WORLD_CAPABILITIES, null);
		return worldData.orElse(null);
	}


	@SubscribeEvent
	public static void register(RegisterCapabilitiesEvent event) {
		event.register(IPlayerCapabilities.class);
		event.register(IGlobalCapabilities.class);
		event.register(IWorldCapabilities.class);
	}


	@SubscribeEvent
	public void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
		if (event.getObject() instanceof LivingEntity) {
			event.addCapability(new ResourceLocation(KingdomKeys.MODID, "global_capabilities"), new GlobalCapabilitiesProvider());
			if (event.getObject() instanceof Player)
				event.addCapability(new ResourceLocation(KingdomKeys.MODID, "player_capabilities"), new PlayerCapabilitiesProvider());
		}
	}

	@SubscribeEvent
	public void attachWorldCapabilities(AttachCapabilitiesEvent<Level> event) {
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

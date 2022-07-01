package online.kingdomkeys.kingdomkeys.capability;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
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
import online.kingdomkeys.kingdomkeys.leveling.ModLevels;

@Mod.EventBusSubscriber(modid = KingdomKeys.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModCapabilities {

	public static final Capability<IPlayerCapabilities> PLAYER_CAPABILITIES = CapabilityManager.get(new CapabilityToken<>() {});
	public static final Capability<IGlobalCapabilities> GLOBAL_CAPABILITIES = CapabilityManager.get(new CapabilityToken<>() {});
	public static final Capability<IWorldCapabilities> WORLD_CAPABILITIES = CapabilityManager.get(new CapabilityToken<>() {});
	public static final Capability<CastleOblivionCapabilities.ICastleOblivionInteriorCapability> CASTLE_OBLIVION_INTERIOR_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});
	public static final Capability<CastleOblivionCapabilities.ICastleOblivionExteriorCapability> CASTLE_OBLIVION_EXTERIOR_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});

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

	public static CastleOblivionCapabilities.ICastleOblivionInteriorCapability getCastleOblivionInterior(Level level) {
		LazyOptional<CastleOblivionCapabilities.ICastleOblivionInteriorCapability> castleOblivionData = level.getCapability(ModCapabilities.CASTLE_OBLIVION_INTERIOR_CAPABILITY, null);
		if (!castleOblivionData.isPresent()) {
			KingdomKeys.LOGGER.warn("Castle Oblivion data not present in this dimension");
			return null;
		}
		return castleOblivionData.orElse(null);
	}

	public static CastleOblivionCapabilities.ICastleOblivionExteriorCapability getCastleOblivionExterior(Level level) {
		LazyOptional<CastleOblivionCapabilities.ICastleOblivionExteriorCapability> castleOblivionData = level.getCapability(ModCapabilities.CASTLE_OBLIVION_EXTERIOR_CAPABILITY, null);
		if (!castleOblivionData.isPresent()) {
			KingdomKeys.LOGGER.warn("Castle Oblivion data not present in this dimension");
			return null;
		}
		return castleOblivionData.orElse(null);
	}

	@SubscribeEvent
	public static void register(RegisterCapabilitiesEvent event) {
		event.register(IPlayerCapabilities.class);
		event.register(IGlobalCapabilities.class);
		event.register(IWorldCapabilities.class);
		event.register(CastleOblivionCapabilities.ICastleOblivionInteriorCapability.class);
		event.register(CastleOblivionCapabilities.ICastleOblivionExteriorCapability.class);
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
		if (event.getObject().dimension().location().toString().contains(KingdomKeys.MODID + ":castle_oblivion_interior_")) {
			event.addCapability(new ResourceLocation(KingdomKeys.MODID, "castle_oblivion_interior_capability"), new CastleOblivionCapabilities.InteriorProvider());
		}
		if (event.getObject().dimension().equals(ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(KingdomKeys.MODID, "castle_oblivion")))) {
			event.addCapability(new ResourceLocation(KingdomKeys.MODID, "castle_oblivion_exterior_capability"), new CastleOblivionCapabilities.ExteriorProvider());
		}
	}

}

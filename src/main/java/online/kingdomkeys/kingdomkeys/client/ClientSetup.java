package online.kingdomkeys.kingdomkeys.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.common.NeoForge;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.gui.overlay.*;
import online.kingdomkeys.kingdomkeys.client.render.*;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.data.GlobalData;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.effects.ModMobEffects;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.handler.ClientEvents;
import online.kingdomkeys.kingdomkeys.handler.InputHandler;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.io.IOException;
import java.util.Map.Entry;

@EventBusSubscriber(value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class ClientSetup {

	public static ResourceLocation
			COMMAND_MENU,
			PLAYER_PORTRAIT,
			HP_BAR,
			MP_BAR,
			DRIVE_BAR,
			KK_NOTIFICATIONS,
			LOCK_ON,
			PARTY_INFO,
			SHOTLOCK,
			STATION_OF_AWAKENING_MESSAGES;

    //Register the entity models
    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        ModEntities.registerRenderers(event);
    }

    @SubscribeEvent
	public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
    	ModEntities.registerLayers(event);
	}

	@SubscribeEvent
	public static void addLayers(EntityRenderersEvent.AddLayers event) {
		Minecraft mc = Minecraft.getInstance();
		EntityRendererProvider.Context context = new EntityRendererProvider.Context(mc.getEntityRenderDispatcher(), mc.getItemRenderer(), mc.getBlockRenderer(), mc.gameRenderer.itemInHandRenderer, mc.getResourceManager(), mc.getEntityModels(), mc.font);

		for(Entry<EntityType<?>, EntityRenderer<?>> entry : Minecraft.getInstance().getEntityRenderDispatcher().renderers.entrySet()) {
			if(entry.getValue() instanceof LivingEntityRenderer renderer && !(entry.getValue() instanceof PlayerRenderer)) {
				renderer.addLayer(new AeroLayerRenderer<LivingEntity>(renderer, event.getEntityModels()));
				renderer.addLayer(new KeybladeArmorRenderer<>(renderer, event.getEntityModels()));
			}
		}
		
		LivingEntityRenderer<Player, PlayerModel<Player>> renderer = event.getSkin(PlayerSkin.Model.WIDE);
		renderer.addLayer(new DriveLayerRenderer<>(renderer));
		renderer.addLayer(new StopLayerRenderer<>(renderer, event.getEntityModels()));
		renderer.addLayer(new ShoulderLayerRenderer<>(renderer, event.getEntityModels(),true));
		renderer.addLayer(new KeybladeArmorRenderer<>(renderer, event.getEntityModels()));
		renderer.addLayer(new AeroLayerRenderer<>(renderer, event.getEntityModels()));

		renderer = event.getSkin(PlayerSkin.Model.SLIM);
		renderer.addLayer(new DriveLayerRenderer<>(renderer));
		renderer.addLayer(new StopLayerRenderer<>(renderer, event.getEntityModels()));
		renderer.addLayer(new ShoulderLayerRenderer<>(renderer, event.getEntityModels(),false));
		renderer.addLayer(new KeybladeArmorRenderer<>(renderer, event.getEntityModels()));
		renderer.addLayer(new AeroLayerRenderer<>(renderer, event.getEntityModels()));
	}

	@SubscribeEvent
	public static void registerOverlays(RegisterGuiLayersEvent event) {
		event.registerBelow(VanillaGuiLayers.CHAT, ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "shortcuts"), ShortcutsGui.INSTANCE);
		event.registerBelow(VanillaGuiLayers.CHAT, ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "command_menu"), CommandMenuGui.INSTANCE);
		event.registerBelow(VanillaGuiLayers.CHAT, ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "player_portrait"), PlayerPortraitGui.INSTANCE);
		event.registerBelow(VanillaGuiLayers.CHAT, ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "hp_bar"), HPGui.INSTANCE);
		event.registerBelow(VanillaGuiLayers.CHAT, ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "mp_bar"), MPGui.INSTANCE);
		event.registerBelow(VanillaGuiLayers.CHAT, ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "drive_bar"), DriveGui.INSTANCE);
		event.registerBelow(VanillaGuiLayers.CHAT, ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "kk_notifications"), GuiOverlay.INSTANCE);
		event.registerBelow(VanillaGuiLayers.CROSSHAIR, ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "lock_on"), LockOnGui.INSTANCE);
		event.registerBelow(VanillaGuiLayers.CHAT, ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "party_info"), PartyHUDGui.INSTANCE);
		event.registerBelow(VanillaGuiLayers.CROSSHAIR, ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "shotlock"), ShotlockGUI.INSTANCE);
		event.registerBelow(VanillaGuiLayers.TITLE, ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "station_of_awakening_messages"), SoAMessages.INSTANCE);
		event.registerBelow(VanillaGuiLayers.CHAT, ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "castle_oblivion_minimap"), COMinimap.INSTANCE);
		event.registerBelow(VanillaGuiLayers.CHAT, ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "gummi_hud"), GummiHUD.INSTANCE);
	}

	@SubscribeEvent
	public static void registerKeyBinding(RegisterKeyMappingsEvent event) {
		for (InputHandler.Keybinds key : InputHandler.Keybinds.values())
			event.register(key.getKeybind());
	}

	public static void renderOverlays(RenderGuiLayerEvent.Pre event) {
		LocalPlayer player = Minecraft.getInstance().player;
		ResourceLocation o = event.getName();
		PlayerData playerData = PlayerData.get(player);
		GlobalData globalData = GlobalData.get(player);
		if(playerData == null || globalData == null)
			return;
		
		if(!Utils.shouldRenderOverlay(player)) { //If it shouldn't render cause it's set to HIDE or WEAPON and not holding one
			event.setCanceled(o.equals(COMMAND_MENU) || o.equals(MP_BAR) || o.equals(DRIVE_BAR) || o.equals(SHOTLOCK)); //Remove all these 4 bars
			if(o.equals(HP_BAR) || o.equals(PLAYER_PORTRAIT)) { //Allow HP to be shown if KO'd
				event.setCanceled(!player.hasEffect(ModMobEffects.KO));
			}
		} else { //If mode is set to SHOW or WEAPON while holding one
			if(o.equals(MP_BAR)) { //Remove MP Bar is magics map is empty
				event.setCanceled(playerData.getMagicsMap().isEmpty());
				return;
			}
			if(o.equals(SHOTLOCK)) {
				event.setCanceled(playerData.getEquippedShotlock().isEmpty());
				return;
			}
			if(o.equals(DRIVE_BAR)) {
				event.setCanceled(Utils.getVisibleDriveForms(player).size() <= 1);
				return;
			}
		}

		if (!ModConfigs.hpShowHearts) {
			event.setCanceled(o.equals(VanillaGuiLayers.PLAYER_HEALTH));
		}
	}

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void setupClient(FMLClientSetupEvent event) {
		COMMAND_MENU = ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "command_menu");
		PLAYER_PORTRAIT = ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "player_portrait");
		HP_BAR = ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "hp_bar");
		MP_BAR = ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "mp_bar");
		DRIVE_BAR = ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "drive_bar");
		KK_NOTIFICATIONS = ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "kk_notifications");
		LOCK_ON = ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "lock_on");
		PARTY_INFO = ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "party_info");
		SHOTLOCK = ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "shotlock");
		STATION_OF_AWAKENING_MESSAGES = ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "station_of_awakening_messages");

		NeoForge.EVENT_BUS.addListener(ClientSetup::renderOverlays);
		NeoForge.EVENT_BUS.register(GuiOverlay.INSTANCE);
		NeoForge.EVENT_BUS.register(new ClientEvents());
		NeoForge.EVENT_BUS.register(DriveGui.INSTANCE);
		NeoForge.EVENT_BUS.register(new InputHandler());
		NeoForge.EVENT_BUS.register(SoAMessages.INSTANCE);
		
    }

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void modelRegistry(ModelEvent.RegisterAdditional event) {
		event.register(ModelResourceLocation.standalone(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "entity/portal")));
		event.register(ModelResourceLocation.standalone(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "block/station_of_awakening")));
		event.register(ModelResourceLocation.standalone(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "entity/heart")));
	}

	public static ShaderInstance testShader, focusShader;

	@SubscribeEvent
	public static void registerShaders(RegisterShadersEvent event) {
		try {
			event.registerShader(new ShaderInstance(event.getResourceProvider(), ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID,"test"), DefaultVertexFormat.POSITION_TEX), shaderInstance -> {
				testShader = shaderInstance;
			});
            event.registerShader(new ShaderInstance(event.getResourceProvider(), ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID,"focus"), DefaultVertexFormat.POSITION_TEX), shaderInstance -> {
                focusShader = shaderInstance;
            });

		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Could not load shader: "+e);
		}
	}
}

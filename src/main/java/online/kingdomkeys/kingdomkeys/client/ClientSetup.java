package online.kingdomkeys.kingdomkeys.client;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.GuiOverlayManager;
import net.minecraftforge.client.gui.overlay.NamedGuiOverlay;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.gui.overlay.CommandMenuGui;
import online.kingdomkeys.kingdomkeys.client.gui.overlay.DriveGui;
import online.kingdomkeys.kingdomkeys.client.gui.overlay.GuiOverlay;
import online.kingdomkeys.kingdomkeys.client.gui.overlay.HPGui;
import online.kingdomkeys.kingdomkeys.client.gui.overlay.LockOnGui;
import online.kingdomkeys.kingdomkeys.client.gui.overlay.MPGui;
import online.kingdomkeys.kingdomkeys.client.gui.overlay.PartyHUDGui;
import online.kingdomkeys.kingdomkeys.client.gui.overlay.PlayerPortraitGui;
import online.kingdomkeys.kingdomkeys.client.gui.overlay.ShotlockGUI;
import online.kingdomkeys.kingdomkeys.client.gui.overlay.SoAMessages;
import online.kingdomkeys.kingdomkeys.client.model.armor.AquaModel;
import online.kingdomkeys.kingdomkeys.client.model.armor.ArmorModel;
import online.kingdomkeys.kingdomkeys.client.model.armor.EraqusModel;
import online.kingdomkeys.kingdomkeys.client.model.armor.TerraModel;
import online.kingdomkeys.kingdomkeys.client.model.armor.UXArmorModel;
import online.kingdomkeys.kingdomkeys.client.model.armor.VentusModel;
import online.kingdomkeys.kingdomkeys.client.model.armor.XehanortModel;
import online.kingdomkeys.kingdomkeys.client.render.AeroLayerRenderer;
import online.kingdomkeys.kingdomkeys.client.render.DriveLayerRenderer;
import online.kingdomkeys.kingdomkeys.client.render.ShoulderLayerRenderer;
import online.kingdomkeys.kingdomkeys.client.render.StopLayerRenderer;
import online.kingdomkeys.kingdomkeys.client.render.KeybladeArmorRenderer;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.container.ModContainers;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.handler.ClientEvents;
import online.kingdomkeys.kingdomkeys.handler.InputHandler;
import online.kingdomkeys.kingdomkeys.item.ModItems;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus=Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {

	public static final Map<Item, HumanoidModel<LivingEntity>> armorModels = new HashMap<>();

	public static NamedGuiOverlay
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
	public static void registerLayers(EntityRenderersEvent.AddLayers event) {
		Minecraft mc = Minecraft.getInstance();
    	EntityRendererProvider.Context context = new EntityRendererProvider.Context(mc.getEntityRenderDispatcher(), mc.getItemRenderer(), mc.getBlockRenderer(), mc.gameRenderer.itemInHandRenderer, mc.getResourceManager(), mc.getEntityModels(), mc.font);
		ArmorModel<LivingEntity> top = new ArmorModel<>(context.bakeLayer(ArmorModel.LAYER_LOCATION_TOP));
		ArmorModel<LivingEntity> bot = new ArmorModel<>(context.bakeLayer(ArmorModel.LAYER_LOCATION_BOTTOM));

		VentusModel<LivingEntity> vTop = new VentusModel<>(context.bakeLayer(VentusModel.LAYER_LOCATION_TOP));
		VentusModel<LivingEntity> vBot = new VentusModel<>(context.bakeLayer(VentusModel.LAYER_LOCATION_BOTTOM));
		
		TerraModel<LivingEntity> tTop = new TerraModel<>(context.bakeLayer(TerraModel.LAYER_LOCATION_TOP));
		TerraModel<LivingEntity> tBot = new TerraModel<>(context.bakeLayer(TerraModel.LAYER_LOCATION_BOTTOM));
		
		AquaModel<LivingEntity> aTop = new AquaModel<>(context.bakeLayer(AquaModel.LAYER_LOCATION_TOP));
		AquaModel<LivingEntity> aBot = new AquaModel<>(context.bakeLayer(AquaModel.LAYER_LOCATION_BOTTOM));
		
		EraqusModel<LivingEntity> eTop = new EraqusModel<>(context.bakeLayer(EraqusModel.LAYER_LOCATION_TOP));
		EraqusModel<LivingEntity> eBot = new EraqusModel<>(context.bakeLayer(EraqusModel.LAYER_LOCATION_BOTTOM));
		
		XehanortModel<LivingEntity> xTop = new XehanortModel<>(context.bakeLayer(XehanortModel.LAYER_LOCATION_TOP));
		XehanortModel<LivingEntity> xBot = new XehanortModel<>(context.bakeLayer(XehanortModel.LAYER_LOCATION_BOTTOM));
		
		UXArmorModel<LivingEntity> uxTop = new UXArmorModel<>(context.bakeLayer(UXArmorModel.LAYER_LOCATION_TOP));
		UXArmorModel<LivingEntity> uxBot = new UXArmorModel<>(context.bakeLayer(UXArmorModel.LAYER_LOCATION_BOTTOM));


        armorModels.put(ModItems.terra_Helmet.get(), tTop);
		armorModels.put(ModItems.terra_Chestplate.get(), tTop);
		armorModels.put(ModItems.terra_Leggings.get(), tBot);
		armorModels.put(ModItems.terra_Boots.get(), tTop);

		armorModels.put(ModItems.aqua_Helmet.get(), aTop);
		armorModels.put(ModItems.aqua_Chestplate.get(), aTop);
		armorModels.put(ModItems.aqua_Leggings.get(), aBot);
		armorModels.put(ModItems.aqua_Boots.get(), aTop);

		armorModels.put(ModItems.ventus_Helmet.get(), vTop);
		armorModels.put(ModItems.ventus_Chestplate.get(), vTop);
		armorModels.put(ModItems.ventus_Leggings.get(), vBot);
		armorModels.put(ModItems.ventus_Boots.get(), vTop);

		armorModels.put(ModItems.nightmareVentus_Helmet.get(), vTop);
		armorModels.put(ModItems.nightmareVentus_Chestplate.get(), vTop);
		armorModels.put(ModItems.nightmareVentus_Leggings.get(), vBot);
		armorModels.put(ModItems.nightmareVentus_Boots.get(), vTop);

		armorModels.put(ModItems.eraqus_Helmet.get(), eTop);
		armorModels.put(ModItems.eraqus_Chestplate.get(), eTop);
		armorModels.put(ModItems.eraqus_Leggings.get(), eBot);
		armorModels.put(ModItems.eraqus_Boots.get(), eTop);
		
		armorModels.put(ModItems.xehanort_Helmet.get(), xTop);
		armorModels.put(ModItems.xehanort_Chestplate.get(), xTop);
		armorModels.put(ModItems.xehanort_Leggings.get(), xBot);
		armorModels.put(ModItems.xehanort_Boots.get(), xTop);
		
		armorModels.put(ModItems.ux_Helmet.get(), uxTop);
		armorModels.put(ModItems.ux_Chestplate.get(), uxTop);
		armorModels.put(ModItems.ux_Leggings.get(), uxBot);
		armorModels.put(ModItems.ux_Boots.get(), uxTop);

		armorModels.put(ModItems.organizationRobe_Helmet.get(), top);
		armorModels.put(ModItems.organizationRobe_Chestplate.get(), top);
		armorModels.put(ModItems.organizationRobe_Leggings.get(), bot);
		armorModels.put(ModItems.organizationRobe_Boots.get(), top);

		armorModels.put(ModItems.xemnas_Helmet.get(), top);
		armorModels.put(ModItems.xemnas_Chestplate.get(), top);
		armorModels.put(ModItems.xemnas_Leggings.get(), bot);
		armorModels.put(ModItems.xemnas_Boots.get(), top);

		armorModels.put(ModItems.vanitas_Helmet.get(), top);
		armorModels.put(ModItems.vanitas_Chestplate.get(), top);
		armorModels.put(ModItems.vanitas_Leggings.get(), bot);
		armorModels.put(ModItems.vanitas_Boots.get(), top);
		
		armorModels.put(ModItems.vanitas_Remnant_Helmet.get(), top);
		armorModels.put(ModItems.vanitas_Remnant_Chestplate.get(), top);
		armorModels.put(ModItems.vanitas_Remnant_Leggings.get(), bot);
		armorModels.put(ModItems.vanitas_Remnant_Boots.get(), top);
		
		armorModels.put(ModItems.dark_Riku_Chestplate.get(), top);
		armorModels.put(ModItems.dark_Riku_Leggings.get(), bot);
		armorModels.put(ModItems.dark_Riku_Boots.get(), top);

		armorModels.put(ModItems.antiCoat_Helmet.get(), top);
		armorModels.put(ModItems.antiCoat_Chestplate.get(), top);
		armorModels.put(ModItems.antiCoat_Leggings.get(), bot);
		armorModels.put(ModItems.antiCoat_Boots.get(), top);

		armorModels.put(ModItems.aced_Helmet.get(), top);
		armorModels.put(ModItems.aced_Chestplate.get(), top);
		armorModels.put(ModItems.aced_Leggings.get(), bot);
		armorModels.put(ModItems.aced_Boots.get(), top);

		armorModels.put(ModItems.ava_Helmet.get(), top);
		armorModels.put(ModItems.ava_Chestplate.get(), top);
		armorModels.put(ModItems.ava_Leggings.get(), bot);
		armorModels.put(ModItems.ava_Boots.get(), top);

		armorModels.put(ModItems.gula_Helmet.get(), top);
		armorModels.put(ModItems.gula_Chestplate.get(), top);
		armorModels.put(ModItems.gula_Leggings.get(), bot);
		armorModels.put(ModItems.gula_Boots.get(), top);

		armorModels.put(ModItems.invi_Helmet.get(), top);
		armorModels.put(ModItems.invi_Chestplate.get(), top);
		armorModels.put(ModItems.invi_Leggings.get(), bot);
		armorModels.put(ModItems.invi_Boots.get(), top);

		armorModels.put(ModItems.ira_Helmet.get(), top);
		armorModels.put(ModItems.ira_Chestplate.get(), top);
		armorModels.put(ModItems.ira_Leggings.get(), bot);
		armorModels.put(ModItems.ira_Boots.get(), top);    	
    }

    @SubscribeEvent
	public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
    	ModEntities.registerLayers(event);
	}

	@SubscribeEvent
	public static void addLayers(EntityRenderersEvent.AddLayers event) {
		for(Entry<EntityType<?>, EntityRenderer<?>> entry : Minecraft.getInstance().getEntityRenderDispatcher().renderers.entrySet()) {
			if(entry.getValue() instanceof LivingEntityRenderer renderer && !(entry.getValue() instanceof PlayerRenderer)) {
				renderer.addLayer(new AeroLayerRenderer<LivingEntity>(renderer, event.getEntityModels()));
			}
		}
		
		LivingEntityRenderer<Player, PlayerModel<Player>> renderer = event.getSkin("default");
		renderer.addLayer(new DriveLayerRenderer<>(renderer));
		renderer.addLayer(new StopLayerRenderer<>(renderer, event.getEntityModels()));
		renderer.addLayer(new ShoulderLayerRenderer<>(renderer, event.getEntityModels(),true));
		renderer.addLayer(new KeybladeArmorRenderer<>(renderer, event.getEntityModels(),true));
		renderer.addLayer(new AeroLayerRenderer<>(renderer, event.getEntityModels()));

		renderer = event.getSkin("slim");
		renderer.addLayer(new DriveLayerRenderer<>(renderer));
		renderer.addLayer(new StopLayerRenderer<>(renderer, event.getEntityModels()));
		renderer.addLayer(new ShoulderLayerRenderer<>(renderer, event.getEntityModels(),false));
		renderer.addLayer(new KeybladeArmorRenderer<>(renderer, event.getEntityModels(),false));
		renderer.addLayer(new AeroLayerRenderer<>(renderer, event.getEntityModels()));
	}

	@SubscribeEvent
	public static void registerOverlays(RegisterGuiOverlaysEvent event) {
		event.registerBelow(VanillaGuiOverlay.CHAT_PANEL.id(), "command_menu", CommandMenuGui.INSTANCE);
		event.registerBelow(VanillaGuiOverlay.CHAT_PANEL.id(), "player_portrait", PlayerPortraitGui.INSTANCE);
		event.registerBelow(VanillaGuiOverlay.CHAT_PANEL.id(), "hp_bar", HPGui.INSTANCE);
		event.registerBelow(VanillaGuiOverlay.CHAT_PANEL.id(), "mp_bar", MPGui.INSTANCE);
		event.registerBelow(VanillaGuiOverlay.CHAT_PANEL.id(), "drive_bar", DriveGui.INSTANCE);
		event.registerBelow(VanillaGuiOverlay.CHAT_PANEL.id(), "kk_notifications", GuiOverlay.INSTANCE);
		event.registerBelow(VanillaGuiOverlay.CROSSHAIR.id(), "lock_on", LockOnGui.INSTANCE);
		event.registerBelow(VanillaGuiOverlay.CHAT_PANEL.id(), "party_info", PartyHUDGui.INSTANCE);
		event.registerBelow(VanillaGuiOverlay.CROSSHAIR.id(), "shotlock", ShotlockGUI.INSTANCE);
		event.registerBelow(VanillaGuiOverlay.TITLE_TEXT.id(), "station_of_awakening_messages", SoAMessages.INSTANCE);
	}

	@SubscribeEvent
	public static void registerKeyBinding(RegisterKeyMappingsEvent event) {
		for (InputHandler.Keybinds key : InputHandler.Keybinds.values())
			event.register(key.getKeybind());
	}

	@SubscribeEvent
	public void renderOverlays(RenderGuiOverlayEvent.Pre event) {
		if (ModConfigs.showGuiToggle == ModConfigs.ShowType.HIDE) {
			NamedGuiOverlay o = event.getOverlay();
			event.setCanceled(o == COMMAND_MENU || o == PLAYER_PORTRAIT || o == HP_BAR || o == MP_BAR || o == DRIVE_BAR || o == SHOTLOCK);
		}

		if (!ModConfigs.hpShowHearts) {
			event.setCanceled(event.getOverlay() == VanillaGuiOverlay.PLAYER_HEALTH.type());
		}
	}

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void setupClient(FMLClientSetupEvent event) {
		COMMAND_MENU = GuiOverlayManager.findOverlay(new ResourceLocation(KingdomKeys.MODID, "command_menu"));
		PLAYER_PORTRAIT = GuiOverlayManager.findOverlay(new ResourceLocation(KingdomKeys.MODID, "player_portrait"));
		HP_BAR = GuiOverlayManager.findOverlay(new ResourceLocation(KingdomKeys.MODID, "hp_bar"));
		MP_BAR = GuiOverlayManager.findOverlay(new ResourceLocation(KingdomKeys.MODID, "mp_bar"));
		DRIVE_BAR = GuiOverlayManager.findOverlay(new ResourceLocation(KingdomKeys.MODID, "drive_bar"));
		KK_NOTIFICATIONS = GuiOverlayManager.findOverlay(new ResourceLocation(KingdomKeys.MODID, "kk_notifications"));
		LOCK_ON = GuiOverlayManager.findOverlay(new ResourceLocation(KingdomKeys.MODID, "lock_on"));
		PARTY_INFO = GuiOverlayManager.findOverlay(new ResourceLocation(KingdomKeys.MODID, "party_info"));
		SHOTLOCK = GuiOverlayManager.findOverlay(new ResourceLocation(KingdomKeys.MODID, "shotlock"));
		STATION_OF_AWAKENING_MESSAGES = GuiOverlayManager.findOverlay(new ResourceLocation(KingdomKeys.MODID, "station_of_awakening_messages"));

		MinecraftForge.EVENT_BUS.register(new ClientSetup());
		MinecraftForge.EVENT_BUS.register(GuiOverlay.INSTANCE);
		MinecraftForge.EVENT_BUS.register(new ClientEvents());
		MinecraftForge.EVENT_BUS.register(CommandMenuGui.INSTANCE);
		MinecraftForge.EVENT_BUS.register(PlayerPortraitGui.INSTANCE);
		MinecraftForge.EVENT_BUS.register(HPGui.INSTANCE);
		MinecraftForge.EVENT_BUS.register(MPGui.INSTANCE);
		MinecraftForge.EVENT_BUS.register(ShotlockGUI.INSTANCE);
		MinecraftForge.EVENT_BUS.register(DriveGui.INSTANCE);
		MinecraftForge.EVENT_BUS.register(new InputHandler());
		MinecraftForge.EVENT_BUS.register(LockOnGui.INSTANCE);
		MinecraftForge.EVENT_BUS.register(PartyHUDGui.INSTANCE);
		MinecraftForge.EVENT_BUS.register(SoAMessages.INSTANCE);
		
		ModContainers.registerGUIFactories();
    }

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void modelRegistry(ModelEvent.RegisterAdditional event) {
		event.register(new ResourceLocation(KingdomKeys.MODID, "entity/portal"));
		event.register(new ResourceLocation(KingdomKeys.MODID, "block/station_of_awakening"));
		event.register(new ResourceLocation(KingdomKeys.MODID, "entity/heart"));
	}
}

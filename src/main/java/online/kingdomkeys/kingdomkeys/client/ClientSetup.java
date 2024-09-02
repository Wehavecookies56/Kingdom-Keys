package online.kingdomkeys.kingdomkeys.client;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.common.NeoForge;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.api.event.client.CommandMenuEvent;
import online.kingdomkeys.kingdomkeys.data.GlobalData;
import online.kingdomkeys.kingdomkeys.data.ModData;
import online.kingdomkeys.kingdomkeys.client.gui.overlay.*;
import online.kingdomkeys.kingdomkeys.client.model.armor.*;
import online.kingdomkeys.kingdomkeys.client.render.*;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.menu.ModMenus;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.handler.ClientEvents;
import online.kingdomkeys.kingdomkeys.handler.InputHandler;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.kingdomkeys.kingdomkeys.util.Utils;

@EventBusSubscriber(value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class ClientSetup {

	public static final Map<Item, ArmorBaseModel<LivingEntity>> armorModels = new HashMap<>();

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
				renderer.addLayer(new KeybladeArmorRenderer<>(renderer, event.getEntityModels()));

			}
		}
		
		LivingEntityRenderer<Player, PlayerModel<Player>> renderer = event.getSkin(PlayerSkin.Model.WIDE);
		renderer.addLayer(new DriveLayerRenderer<>(renderer));
		renderer.addLayer(new StopLayerRenderer<>(renderer, event.getEntityModels()));
		renderer.addLayer(new ShoulderLayerRenderer<>(renderer, event.getEntityModels(),true));
		renderer.addLayer(new KeybladeArmorRenderer<>(renderer, event.getEntityModels()));
		renderer.addLayer(new AeroLayerRenderer<>(renderer, event.getEntityModels()));
		renderer.addLayer(new HeartLayerRenderer<>(renderer, event.getEntityModels()));

		renderer = event.getSkin(PlayerSkin.Model.SLIM);
		renderer.addLayer(new DriveLayerRenderer<>(renderer));
		renderer.addLayer(new StopLayerRenderer<>(renderer, event.getEntityModels()));
		renderer.addLayer(new ShoulderLayerRenderer<>(renderer, event.getEntityModels(),false));
		renderer.addLayer(new KeybladeArmorRenderer<>(renderer, event.getEntityModels()));
		renderer.addLayer(new AeroLayerRenderer<>(renderer, event.getEntityModels()));
		renderer.addLayer(new HeartLayerRenderer<>(renderer, event.getEntityModels()));
		

	}

	@SubscribeEvent
	public static void registerOverlays(RegisterGuiLayersEvent event) {

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
	}

	@SubscribeEvent
	public static void registerKeyBinding(RegisterKeyMappingsEvent event) {
		for (InputHandler.Keybinds key : InputHandler.Keybinds.values())
			event.register(key.getKeybind());
	}

	@SubscribeEvent
	public void renderOverlays(RenderGuiLayerEvent.Pre event) {
		LocalPlayer player = Minecraft.getInstance().player;
		ResourceLocation o = event.getName();
		PlayerData playerData = PlayerData.get(player);
		GlobalData globalData = GlobalData.get(player);
		if(playerData == null || globalData == null)
			return;
		
		if(!Utils.shouldRenderOverlay(player)) { //If it shouldn't render cause it's set to HIDE or WEAPON and not holding one
			event.setCanceled(o.equals(COMMAND_MENU) || o.equals(MP_BAR) || o.equals(DRIVE_BAR) || o.equals(SHOTLOCK)); //Remove all these 4 bars
			if(o.equals(HP_BAR) || o.equals(PLAYER_PORTRAIT)) { //Allow HP to be shown if KO'd
				event.setCanceled(!globalData.isKO());
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
				event.setCanceled(playerData.getVisibleDriveForms().size() <= 1);
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
		NeoForge.EVENT_BUS.addListener(ClientEvents::colourTint);
		NeoForge.EVENT_BUS.addListener(ClientEvents::itemColour);
		NeoForge.EVENT_BUS.addListener(ClientSetup::modLoaded);
		NeoForge.EVENT_BUS.addListener(ModMenus::registerGUIFactories);
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

		NeoForge.EVENT_BUS.register(new ClientSetup());
		NeoForge.EVENT_BUS.register(GuiOverlay.INSTANCE);
		NeoForge.EVENT_BUS.register(new ClientEvents());
		NeoForge.EVENT_BUS.register(CommandMenuGui.INSTANCE);
		NeoForge.EVENT_BUS.register(PlayerPortraitGui.INSTANCE);
		NeoForge.EVENT_BUS.register(HPGui.INSTANCE);
		NeoForge.EVENT_BUS.register(MPGui.INSTANCE);
		NeoForge.EVENT_BUS.register(ShotlockGUI.INSTANCE);
		NeoForge.EVENT_BUS.register(DriveGui.INSTANCE);
		NeoForge.EVENT_BUS.register(new InputHandler());
		NeoForge.EVENT_BUS.register(LockOnGui.INSTANCE);
		NeoForge.EVENT_BUS.register(PartyHUDGui.INSTANCE);
		NeoForge.EVENT_BUS.register(SoAMessages.INSTANCE);
		
    }

	private static void modLoaded(final FMLLoadCompleteEvent event) {
		if (FMLEnvironment.dist.isClient()) {
			if (ModList.get().isLoaded("epicfight")) {
				//FMLJavaModLoadingContext.get().getModEventBus().addListener(EpicFightRendering::patchedRenderersEventModify);
			}
			NeoForge.EVENT_BUS.post(new CommandMenuEvent.Construct(CommandMenuGui.INSTANCE));
		}
	}

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void modelRegistry(ModelEvent.RegisterAdditional event) {
		event.register(ModelResourceLocation.standalone(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "entity/portal")));
		event.register(ModelResourceLocation.standalone(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "block/station_of_awakening")));
		event.register(ModelResourceLocation.standalone(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "entity/heart")));
	}

	@SubscribeEvent
	public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
		event.registerItem(new IClientItemExtensions() {
			@Override
			public HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot armorSlot, HumanoidModel<?> original) {
				ArmorBaseModel<LivingEntity> armorModel = ClientSetup.armorModels.get(itemStack.getItem());

				if (armorModel != null) {
					armorModel.head.visible = armorSlot == EquipmentSlot.HEAD;
					armorModel.body.visible = armorSlot == EquipmentSlot.CHEST || armorSlot == EquipmentSlot.LEGS;
					armorModel.rightArm.visible = armorSlot == EquipmentSlot.CHEST;
					armorModel.leftArm.visible = armorSlot == EquipmentSlot.CHEST;
					armorModel.rightLeg.visible = armorSlot == EquipmentSlot.LEGS || armorSlot == EquipmentSlot.FEET;
					armorModel.leftLeg.visible = armorSlot == EquipmentSlot.LEGS || armorSlot == EquipmentSlot.FEET;
					return new HumanoidModel<>(armorModel.root);
				}
				return original;
			}
		}, ModItems.terra_Helmet.get(), ModItems.terra_Chestplate.get(), ModItems.terra_Leggings.get(), ModItems.terra_Boots.get());
	}
}

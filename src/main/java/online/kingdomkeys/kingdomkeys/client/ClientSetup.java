package online.kingdomkeys.kingdomkeys.client;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.IIngameOverlay;
import net.minecraftforge.client.gui.OverlayRegistry;
import net.minecraftforge.client.model.ForgeModelBakery;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;
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
import online.kingdomkeys.kingdomkeys.client.model.armor.ArmorModel;
import online.kingdomkeys.kingdomkeys.client.model.armor.VentusModel;
import online.kingdomkeys.kingdomkeys.client.render.AeroLayerRenderer;
import online.kingdomkeys.kingdomkeys.client.render.DriveLayerRenderer;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.container.ModContainers;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.handler.ClientEvents;
import online.kingdomkeys.kingdomkeys.handler.InputHandler;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.CastleOblivionRenderInfo;
import online.kingdomkeys.kingdomkeys.world.dimension.dive_to_the_heart.DiveToTheHeartRenderInfo;
import online.kingdomkeys.kingdomkeys.world.dimension.station_of_sorrow.StationOfSorrowRenderInfo;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus=Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {

	public static final Map<Item, HumanoidModel<LivingEntity>> armorModels = new HashMap<>();

	public static IIngameOverlay COMMAND_MENU, PLAYER_PORTRAIT, HP_BAR, MP_BAR, DRIVE_BAR, KK_NOTIFICATIONS, LOCK_ON, PARTY_INFO, SHOTLOCK, STATION_OF_AWAKENING_MESSAGES;

    //Register the entity models
    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        ModEntities.registerRenderers(event);
    }
    
    @SubscribeEvent
	public static void registerLayers(EntityRenderersEvent.AddLayers event) {
    	EntityRendererProvider.Context context = new EntityRendererProvider.Context(Minecraft.getInstance().getEntityRenderDispatcher(), Minecraft.getInstance().getItemRenderer(), Minecraft.getInstance().getResourceManager(), Minecraft.getInstance().getEntityModels(), Minecraft.getInstance().font);
		ArmorModel<LivingEntity> top = new ArmorModel<>(context.bakeLayer(ArmorModel.LAYER_LOCATION_TOP));
		ArmorModel<LivingEntity> bot = new ArmorModel<>(context.bakeLayer(ArmorModel.LAYER_LOCATION_BOTTOM));


		VentusModel<LivingEntity> vTop = new VentusModel<>(context.bakeLayer(VentusModel.LAYER_LOCATION_TOP));
		VentusModel<LivingEntity> vBot = new VentusModel<>(context.bakeLayer(VentusModel.LAYER_LOCATION_BOTTOM));

        armorModels.put(ModItems.terra_Helmet.get(), top);
		armorModels.put(ModItems.terra_Chestplate.get(), top);
		armorModels.put(ModItems.terra_Leggings.get(), bot);
		armorModels.put(ModItems.terra_Boots.get(), top);

		armorModels.put(ModItems.aqua_Helmet.get(), top);
		armorModels.put(ModItems.aqua_Chestplate.get(), top);
		armorModels.put(ModItems.aqua_Leggings.get(), bot);
		armorModels.put(ModItems.aqua_Boots.get(), top);

		armorModels.put(ModItems.ventus_Helmet.get(), vTop);
		armorModels.put(ModItems.ventus_Chestplate.get(), vTop);
		armorModels.put(ModItems.ventus_Leggings.get(), vBot);
		armorModels.put(ModItems.ventus_Boots.get(), vTop);

		armorModels.put(ModItems.nightmareVentus_Helmet.get(), vTop);
		armorModels.put(ModItems.nightmareVentus_Chestplate.get(), vTop);
		armorModels.put(ModItems.nightmareVentus_Leggings.get(), vBot);
		armorModels.put(ModItems.nightmareVentus_Boots.get(), vTop);

		armorModels.put(ModItems.eraqus_Helmet.get(), top);
		armorModels.put(ModItems.eraqus_Chestplate.get(), top);
		armorModels.put(ModItems.eraqus_Leggings.get(), bot);
		armorModels.put(ModItems.eraqus_Boots.get(), top);

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
		LivingEntityRenderer<Player, PlayerModel<Player>> renderer = event.getSkin("default");
		renderer.addLayer(new DriveLayerRenderer<>(renderer));
		renderer.addLayer(new AeroLayerRenderer<>(renderer,event.getEntityModels()));

		renderer = event.getSkin("slim");
		renderer.addLayer(new DriveLayerRenderer<>(renderer));
		renderer.addLayer(new AeroLayerRenderer<>(renderer,event.getEntityModels()));


	}

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void setupClient(FMLClientSetupEvent event) {
        for (InputHandler.Keybinds key : InputHandler.Keybinds.values())
            ClientRegistry.registerKeyBinding(key.getKeybind());

		MinecraftForge.EVENT_BUS.register(new GuiOverlay());
		MinecraftForge.EVENT_BUS.register(new ClientEvents());
		MinecraftForge.EVENT_BUS.register(new CommandMenuGui());
		MinecraftForge.EVENT_BUS.register(new PlayerPortraitGui());
		MinecraftForge.EVENT_BUS.register(new HPGui());
		MinecraftForge.EVENT_BUS.register(new MPGui());
		MinecraftForge.EVENT_BUS.register(new ShotlockGUI());
		MinecraftForge.EVENT_BUS.register(new DriveGui());
		MinecraftForge.EVENT_BUS.register(new InputHandler());
		MinecraftForge.EVENT_BUS.register(new LockOnGui());
		MinecraftForge.EVENT_BUS.register(new PartyHUDGui());
		MinecraftForge.EVENT_BUS.register(SoAMessages.INSTANCE);

		//Could probably use one for all void style dimensions
		DimensionSpecialEffects.EFFECTS.put(new ResourceLocation(KingdomKeys.MODID, Strings.diveToTheHeart), new DiveToTheHeartRenderInfo());
		DimensionSpecialEffects.EFFECTS.put(new ResourceLocation(KingdomKeys.MODID, Strings.stationOfSorrow), new StationOfSorrowRenderInfo());
		DimensionSpecialEffects.EFFECTS.put(new ResourceLocation(KingdomKeys.MODID, Strings.castleOblivion), new CastleOblivionRenderInfo());

		//Overlay setup

		COMMAND_MENU = OverlayRegistry.registerOverlayBelow(ForgeIngameGui.HUD_TEXT_ELEMENT, "Command Menu", new CommandMenuGui());
		PLAYER_PORTRAIT = OverlayRegistry.registerOverlayBelow(ForgeIngameGui.HUD_TEXT_ELEMENT, "Player Portrait", new PlayerPortraitGui());
		HP_BAR = OverlayRegistry.registerOverlayBelow(ForgeIngameGui.HUD_TEXT_ELEMENT, "HP Bar", new HPGui());
		MP_BAR = OverlayRegistry.registerOverlayBelow(ForgeIngameGui.HUD_TEXT_ELEMENT, "MP Bar", new MPGui());
		DRIVE_BAR = OverlayRegistry.registerOverlayBelow(ForgeIngameGui.HUD_TEXT_ELEMENT, "Drive Bar", new DriveGui());
		KK_NOTIFICATIONS = OverlayRegistry.registerOverlayBelow(ForgeIngameGui.HUD_TEXT_ELEMENT, "KK Notifications", new GuiOverlay());
		LOCK_ON = OverlayRegistry.registerOverlayBelow(ForgeIngameGui.CROSSHAIR_ELEMENT, "Lock On", new LockOnGui());
		PARTY_INFO = OverlayRegistry.registerOverlayBelow(ForgeIngameGui.HUD_TEXT_ELEMENT, "Party Info", new PartyHUDGui());
		SHOTLOCK = OverlayRegistry.registerOverlayBelow(ForgeIngameGui.CROSSHAIR_ELEMENT, "Shotlock", new ShotlockGUI());
		STATION_OF_AWAKENING_MESSAGES = OverlayRegistry.registerOverlayBelow(ForgeIngameGui.TITLE_TEXT_ELEMENT, "Station of Awakening Messages", SoAMessages.INSTANCE);

		if (ModConfigs.showGuiToggle == ModConfigs.ShowType.HIDE) {
			OverlayRegistry.enableOverlay(ClientSetup.COMMAND_MENU, false);
			OverlayRegistry.enableOverlay(ClientSetup.PLAYER_PORTRAIT, false);
			OverlayRegistry.enableOverlay(ClientSetup.HP_BAR, false);
			OverlayRegistry.enableOverlay(ClientSetup.MP_BAR, false);
			OverlayRegistry.enableOverlay(ClientSetup.DRIVE_BAR, false);
			OverlayRegistry.enableOverlay(ClientSetup.SHOTLOCK, false);
		}

		if (!ModConfigs.hpShowHearts) {
			OverlayRegistry.enableOverlay(ForgeIngameGui.PLAYER_HEALTH_ELEMENT, false);
		}
		
		ModContainers.registerGUIFactories();

        event.enqueueWork(() -> {
			ItemBlockRenderTypes.setRenderLayer(ModBlocks.ghostBlox.get(), RenderType.cutout());
			ItemBlockRenderTypes.setRenderLayer(ModBlocks.magicalChest.get(), RenderType.cutout());
			ItemBlockRenderTypes.setRenderLayer(ModBlocks.mosaic_stained_glass.get(), RenderType.translucent());
			ItemBlockRenderTypes.setRenderLayer(ModBlocks.soADoor.get(), RenderType.cutout());
			ItemBlockRenderTypes.setRenderLayer(ModBlocks.moogleProjector.get(), RenderType.cutout());
			ItemBlockRenderTypes.setRenderLayer(ModBlocks.savepoint.get(), RenderType.cutout());
			ItemBlockRenderTypes.setRenderLayer(ModBlocks.orgPortal.get(), RenderType.cutout());
			ItemBlockRenderTypes.setRenderLayer(ModBlocks.pedestal.get(), RenderType.cutout());
			ItemBlockRenderTypes.setRenderLayer(ModBlocks.station_of_awakening_core.get(), RenderType.translucent());
			ItemBlockRenderTypes.setRenderLayer(ModBlocks.structureWall.get(), RenderType.cutout());
			ItemBlockRenderTypes.setRenderLayer(ModBlocks.castleOblivionWall.get(), RenderType.cutout());
			ItemBlockRenderTypes.setRenderLayer(ModBlocks.castleOblivionPillar.get(), RenderType.cutout());
			ItemBlockRenderTypes.setRenderLayer(ModBlocks.cardDoor.get(), RenderType.cutout());
        });
    }

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void modelRegistry(ModelRegistryEvent event) {
		//Lances
		ForgeModelBakery.addSpecialModel(new ResourceLocation(KingdomKeys.MODID, "item/" + Strings.zephyr));

		ForgeModelBakery.addSpecialModel(new ResourceLocation(KingdomKeys.MODID, "item/" + Strings.aer));


		ForgeModelBakery.addSpecialModel(new ResourceLocation(KingdomKeys.MODID, "item/" + Strings.asura));
		ForgeModelBakery.addSpecialModel(new ResourceLocation(KingdomKeys.MODID, "item/" + Strings.crux));

		ForgeModelBakery.addSpecialModel(new ResourceLocation(KingdomKeys.MODID, "item/" + Strings.fellking));


		ForgeModelBakery.addSpecialModel(new ResourceLocation(KingdomKeys.MODID, "item/" + Strings.scission));
		ForgeModelBakery.addSpecialModel(new ResourceLocation(KingdomKeys.MODID, "item/" + Strings.heavenfall));
		ForgeModelBakery.addSpecialModel(new ResourceLocation(KingdomKeys.MODID, "item/" + Strings.aether));

		ForgeModelBakery.addSpecialModel(new ResourceLocation(KingdomKeys.MODID, "item/" + Strings.hegemon));

		ForgeModelBakery.addSpecialModel(new ResourceLocation(KingdomKeys.MODID, "item/" + Strings.yaksha));
		ForgeModelBakery.addSpecialModel(new ResourceLocation(KingdomKeys.MODID, "item/" + Strings.cynosura));
		ForgeModelBakery.addSpecialModel(new ResourceLocation(KingdomKeys.MODID, "item/" + Strings.dragonreign));
		ForgeModelBakery.addSpecialModel(new ResourceLocation(KingdomKeys.MODID, "item/" + Strings.lindworm));


		//Chakrams
		ForgeModelBakery.addSpecialModel(new ResourceLocation(KingdomKeys.MODID, "item/" + Strings.ashes));
		ForgeModelBakery.addSpecialModel(new ResourceLocation(KingdomKeys.MODID, "item/" + Strings.doldrums));
		ForgeModelBakery.addSpecialModel(new ResourceLocation(KingdomKeys.MODID, "item/" + Strings.delayedAction));
		ForgeModelBakery.addSpecialModel(new ResourceLocation(KingdomKeys.MODID, "item/" + Strings.diveBombers));
		ForgeModelBakery.addSpecialModel(new ResourceLocation(KingdomKeys.MODID, "item/" + Strings.combustion));
		ForgeModelBakery.addSpecialModel(new ResourceLocation(KingdomKeys.MODID, "item/" + Strings.moulinRouge));
		ForgeModelBakery.addSpecialModel(new ResourceLocation(KingdomKeys.MODID, "item/" + Strings.blazeOfGlory));
		ForgeModelBakery.addSpecialModel(new ResourceLocation(KingdomKeys.MODID, "item/" + Strings.prometheus));
		ForgeModelBakery.addSpecialModel(new ResourceLocation(KingdomKeys.MODID, "item/" + Strings.ifrit));
		ForgeModelBakery.addSpecialModel(new ResourceLocation(KingdomKeys.MODID, "item/" + Strings.magmaOcean));
		ForgeModelBakery.addSpecialModel(new ResourceLocation(KingdomKeys.MODID, "item/" + Strings.volcanics));
		ForgeModelBakery.addSpecialModel(new ResourceLocation(KingdomKeys.MODID, "item/" + Strings.inferno));
		ForgeModelBakery.addSpecialModel(new ResourceLocation(KingdomKeys.MODID, "item/" + Strings.sizzlingEdge));
		ForgeModelBakery.addSpecialModel(new ResourceLocation(KingdomKeys.MODID, "item/" + Strings.corona));
		ForgeModelBakery.addSpecialModel(new ResourceLocation(KingdomKeys.MODID, "item/" + Strings.ferrisWheel));
		ForgeModelBakery.addSpecialModel(new ResourceLocation(KingdomKeys.MODID, "item/" + Strings.burnout));
		ForgeModelBakery.addSpecialModel(new ResourceLocation(KingdomKeys.MODID, "item/" + Strings.omegaTrinity));
		ForgeModelBakery.addSpecialModel(new ResourceLocation(KingdomKeys.MODID, "item/" + Strings.outbreak));
		ForgeModelBakery.addSpecialModel(new ResourceLocation(KingdomKeys.MODID, "item/" + Strings.doubleEdge));
		ForgeModelBakery.addSpecialModel(new ResourceLocation(KingdomKeys.MODID, "item/" + Strings.wildfire));
		ForgeModelBakery.addSpecialModel(new ResourceLocation(KingdomKeys.MODID, "item/" + Strings.prominence));
		ForgeModelBakery.addSpecialModel(new ResourceLocation(KingdomKeys.MODID, "item/" + Strings.eternalFlames));

		ForgeModelBakery.addSpecialModel(new ResourceLocation(KingdomKeys.MODID, "item/" + Strings.conformers));

		//Other
		ForgeModelBakery.addSpecialModel(new ResourceLocation(KingdomKeys.MODID, "entity/portal"));
		ForgeModelBakery.addSpecialModel(new ResourceLocation(KingdomKeys.MODID, "block/station_of_awakening"));
		ForgeModelBakery.addSpecialModel(new ResourceLocation(KingdomKeys.MODID, "entity/heart"));
	}
}

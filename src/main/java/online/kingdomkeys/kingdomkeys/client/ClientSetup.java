package online.kingdomkeys.kingdomkeys.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
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
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelEvent.RegisterGeometryLoaders;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.client.gui.overlay.GuiOverlayManager;
import net.minecraftforge.client.model.ForgeModelBakery;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;
import online.kingdomkeys.kingdomkeys.client.gui.overlay.*;
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
import online.kingdomkeys.kingdomkeys.world.dimension.dive_to_the_heart.DiveToTheHeartRenderInfo;
import online.kingdomkeys.kingdomkeys.world.dimension.station_of_sorrow.StationOfSorrowRenderInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus=Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {

	public static final Map<Item, HumanoidModel<LivingEntity>> armorModels = new HashMap<>();

	public static IGuiOverlay COMMAND_MENU, PLAYER_PORTRAIT, HP_BAR, MP_BAR, DRIVE_BAR, KK_NOTIFICATIONS, LOCK_ON, PARTY_INFO, SHOTLOCK, STATION_OF_AWAKENING_MESSAGES;

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
		renderer.addLayer(new AeroLayerRenderer<>(renderer, event.getEntityModels()));

		renderer = event.getSkin("slim");
		renderer.addLayer(new DriveLayerRenderer<>(renderer));
		renderer.addLayer(new AeroLayerRenderer<>(renderer, event.getEntityModels()));
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

		//Overlay setup

		COMMAND_MENU = GuiOverlayManager.registerOverlayBelow(ForgeGui.HUD_TEXT_ELEMENT, "Command Menu", new CommandMenuGui());
		PLAYER_PORTRAIT = GuiOverlayManager.registerOverlayBelow(ForgeGui.HUD_TEXT_ELEMENT, "Player Portrait", new PlayerPortraitGui());
		HP_BAR = GuiOverlayManager.registerOverlayBelow(ForgeGui.HUD_TEXT_ELEMENT, "HP Bar", new HPGui());
		MP_BAR = GuiOverlayManager.registerOverlayBelow(ForgeGui.HUD_TEXT_ELEMENT, "MP Bar", new MPGui());
		DRIVE_BAR = GuiOverlayManager.registerOverlayBelow(ForgeGui.HUD_TEXT_ELEMENT, "Drive Bar", new DriveGui());
		KK_NOTIFICATIONS = GuiOverlayManager.registerOverlayBelow(ForgeGui.HUD_TEXT_ELEMENT, "KK Notifications", new GuiOverlay());
		LOCK_ON = GuiOverlayManager.registerOverlayBelow(ForgeGui.CROSSHAIR_ELEMENT, "Lock On", new LockOnGui());
		PARTY_INFO = GuiOverlayManager.registerOverlayBelow(ForgeGui.HUD_TEXT_ELEMENT, "Party Info", new PartyHUDGui());
		SHOTLOCK = GuiOverlayManager.registerOverlayBelow(ForgeGui.CROSSHAIR_ELEMENT, "Shotlock", new ShotlockGUI());
		STATION_OF_AWAKENING_MESSAGES = GuiOverlayManager.registerOverlayBelow(ForgeGui.TITLE_TEXT_ELEMENT, "Station of Awakening Messages", SoAMessages.INSTANCE);

		if (ModConfigs.showGuiToggle == ModConfigs.ShowType.HIDE) {
			GuiOverlayManager.enableOverlay(ClientSetup.COMMAND_MENU, false);
			GuiOverlayManager.enableOverlay(ClientSetup.PLAYER_PORTRAIT, false);
			GuiOverlayManager.enableOverlay(ClientSetup.HP_BAR, false);
			GuiOverlayManager.enableOverlay(ClientSetup.MP_BAR, false);
			GuiOverlayManager.enableOverlay(ClientSetup.DRIVE_BAR, false);
			GuiOverlayManager.enableOverlay(ClientSetup.SHOTLOCK, false);
		}

		if (!ModConfigs.hpShowHearts) {
			GuiOverlayManager.enableOverlay(ForgeGui.PLAYER_HEALTH_ELEMENT, false);
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
        });
    }

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void modelRegistry(RegisterGeometryLoaders event) {
		ForgeModelBakery.addSpecialModel(new ResourceLocation(KingdomKeys.MODID, "entity/portal"));
		ForgeModelBakery.addSpecialModel(new ResourceLocation(KingdomKeys.MODID, "block/station_of_awakening"));
		ForgeModelBakery.addSpecialModel(new ResourceLocation(KingdomKeys.MODID, "entity/heart"));
	}
}

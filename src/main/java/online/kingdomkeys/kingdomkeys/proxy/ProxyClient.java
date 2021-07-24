package online.kingdomkeys.kingdomkeys.proxy;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fmlclient.registry.ClientRegistry;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;
import online.kingdomkeys.kingdomkeys.client.gui.CommandMenuGui;
import online.kingdomkeys.kingdomkeys.client.gui.DriveGui;
import online.kingdomkeys.kingdomkeys.client.gui.GuiOverlay;
import online.kingdomkeys.kingdomkeys.client.gui.HPGui;
import online.kingdomkeys.kingdomkeys.client.gui.LockOnGui;
import online.kingdomkeys.kingdomkeys.client.gui.MPGui;
import online.kingdomkeys.kingdomkeys.client.gui.PartyHUDGui;
import online.kingdomkeys.kingdomkeys.client.gui.PlayerPortraitGui;
import online.kingdomkeys.kingdomkeys.client.gui.ShotlockGUI;
import online.kingdomkeys.kingdomkeys.client.gui.SoAMessages;
import online.kingdomkeys.kingdomkeys.client.model.armor.ArmorModel;
import online.kingdomkeys.kingdomkeys.client.model.armor.VentusModel;
import online.kingdomkeys.kingdomkeys.client.render.DriveLayerRenderer;
import online.kingdomkeys.kingdomkeys.container.ModContainers;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.handler.ClientEvents;
import online.kingdomkeys.kingdomkeys.handler.InputHandler;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.world.dimension.dive_to_the_heart.DiveToTheHeartRenderInfo;
import online.kingdomkeys.kingdomkeys.world.dimension.station_of_sorrow.StationOfSorrowRenderInfo;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus=Mod.EventBusSubscriber.Bus.MOD)
public class ProxyClient implements IProxy {

	public static final Map<Item, HumanoidModel> armorModels = new HashMap<Item, HumanoidModel>();

    @Override
    public void setup(FMLCommonSetupEvent event) {
        //MinecraftForge.EVENT_BUS.register(new HUDElementHandler());
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
    }

    //Register the entity models
    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        ModEntities.registerModels();
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void setupClient(FMLClientSetupEvent event) {
        for (InputHandler.Keybinds key : InputHandler.Keybinds.values())
            ClientRegistry.registerKeyBinding(key.getKeybind());

		MinecraftForge.EVENT_BUS.register(new GuiOverlay());
		MinecraftForge.EVENT_BUS.register(new ClientEvents());

		DimensionSpecialEffects.EFFECTS.put(new ResourceLocation(KingdomKeys.MODID, Strings.diveToTheHeart), new DiveToTheHeartRenderInfo());
		DimensionSpecialEffects.EFFECTS.put(new ResourceLocation(KingdomKeys.MODID, Strings.stationOfSorrow), new StationOfSorrowRenderInfo());

        PlayerRenderer renderPlayer = Minecraft.getInstance().getEntityRenderDispatcher().getSkinMap().get("default");
		renderPlayer.addLayer(new DriveLayerRenderer(renderPlayer));
		renderPlayer = Minecraft.getInstance().getEntityRenderDispatcher().getSkinMap().get("slim");
		renderPlayer.addLayer(new DriveLayerRenderer(renderPlayer));
        ModContainers.registerGUIFactories();

        event.enqueueWork(() -> {
			ItemBlockRenderTypes.setRenderLayer(ModBlocks.ghostBlox.get(), RenderType.cutout());
			ItemBlockRenderTypes.setRenderLayer(ModBlocks.magicalChest.get(), RenderType.cutout());
			ItemBlockRenderTypes.setRenderLayer(ModBlocks.mosaic_stained_glass.get(), RenderType.translucent());
			ItemBlockRenderTypes.setRenderLayer(ModBlocks.soADoor.get(), RenderType.cutout());
			ItemBlockRenderTypes.setRenderLayer(ModBlocks.moogleProjector.get(), RenderType.cutout());
			ItemBlockRenderTypes.setRenderLayer(ModBlocks.pedestal.get(), RenderType.cutout());
			ItemBlockRenderTypes.setRenderLayer(ModBlocks.station_of_awakening_core.get(), RenderType.translucent());
        });
        
        ArmorModel top = new ArmorModel(0.5F);
        ArmorModel bot = new ArmorModel(0.25F);
        
        VentusModel vTop = new VentusModel(0.5F);
        VentusModel vBot = new VentusModel(0.25F);
        
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

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void modelRegistry(ModelRegistryEvent event) {
		//Lances
    	ModelLoader.addSpecialModel(new ResourceLocation(KingdomKeys.MODID, "item/"+Strings.zephyr));
    	
    	ModelLoader.addSpecialModel(new ResourceLocation(KingdomKeys.MODID, "item/"+Strings.aer));
    	
    	
    	ModelLoader.addSpecialModel(new ResourceLocation(KingdomKeys.MODID, "item/"+Strings.asura));
    	ModelLoader.addSpecialModel(new ResourceLocation(KingdomKeys.MODID, "item/"+Strings.crux));
    	
    	ModelLoader.addSpecialModel(new ResourceLocation(KingdomKeys.MODID, "item/"+Strings.fellking));
    	
    	
    	
    	ModelLoader.addSpecialModel(new ResourceLocation(KingdomKeys.MODID, "item/"+Strings.scission));
    	ModelLoader.addSpecialModel(new ResourceLocation(KingdomKeys.MODID, "item/"+Strings.heavenfall));
    	ModelLoader.addSpecialModel(new ResourceLocation(KingdomKeys.MODID, "item/"+Strings.aether));
    	
    	ModelLoader.addSpecialModel(new ResourceLocation(KingdomKeys.MODID, "item/"+Strings.hegemon));
    	
    	ModelLoader.addSpecialModel(new ResourceLocation(KingdomKeys.MODID, "item/"+Strings.yaksha));
    	ModelLoader.addSpecialModel(new ResourceLocation(KingdomKeys.MODID, "item/"+Strings.cynosura));
    	ModelLoader.addSpecialModel(new ResourceLocation(KingdomKeys.MODID, "item/"+Strings.dragonreign));
    	ModelLoader.addSpecialModel(new ResourceLocation(KingdomKeys.MODID, "item/"+Strings.lindworm));
    	
    	
    	
    	//Chakrams
    	ModelLoader.addSpecialModel(new ResourceLocation(KingdomKeys.MODID, "item/"+Strings.ashes));
    	ModelLoader.addSpecialModel(new ResourceLocation(KingdomKeys.MODID, "item/"+Strings.doldrums));
    	ModelLoader.addSpecialModel(new ResourceLocation(KingdomKeys.MODID, "item/"+Strings.delayedAction));
    	ModelLoader.addSpecialModel(new ResourceLocation(KingdomKeys.MODID, "item/"+Strings.diveBombers));
    	ModelLoader.addSpecialModel(new ResourceLocation(KingdomKeys.MODID, "item/"+Strings.combustion));
    	ModelLoader.addSpecialModel(new ResourceLocation(KingdomKeys.MODID, "item/"+Strings.moulinRouge));
    	ModelLoader.addSpecialModel(new ResourceLocation(KingdomKeys.MODID, "item/"+Strings.blazeOfGlory));
    	ModelLoader.addSpecialModel(new ResourceLocation(KingdomKeys.MODID, "item/"+Strings.prometheus));
    	ModelLoader.addSpecialModel(new ResourceLocation(KingdomKeys.MODID, "item/"+Strings.ifrit));
    	ModelLoader.addSpecialModel(new ResourceLocation(KingdomKeys.MODID, "item/"+Strings.magmaOcean));
    	ModelLoader.addSpecialModel(new ResourceLocation(KingdomKeys.MODID, "item/"+Strings.volcanics));
    	ModelLoader.addSpecialModel(new ResourceLocation(KingdomKeys.MODID, "item/"+Strings.inferno));
    	ModelLoader.addSpecialModel(new ResourceLocation(KingdomKeys.MODID, "item/"+Strings.sizzlingEdge));
    	ModelLoader.addSpecialModel(new ResourceLocation(KingdomKeys.MODID, "item/"+Strings.corona));
    	ModelLoader.addSpecialModel(new ResourceLocation(KingdomKeys.MODID, "item/"+Strings.ferrisWheel));
    	ModelLoader.addSpecialModel(new ResourceLocation(KingdomKeys.MODID, "item/"+Strings.burnout));
    	ModelLoader.addSpecialModel(new ResourceLocation(KingdomKeys.MODID, "item/"+Strings.omegaTrinity));
    	ModelLoader.addSpecialModel(new ResourceLocation(KingdomKeys.MODID, "item/"+Strings.outbreak));
    	ModelLoader.addSpecialModel(new ResourceLocation(KingdomKeys.MODID, "item/"+Strings.doubleEdge));
    	ModelLoader.addSpecialModel(new ResourceLocation(KingdomKeys.MODID, "item/"+Strings.wildfire));
    	ModelLoader.addSpecialModel(new ResourceLocation(KingdomKeys.MODID, "item/"+Strings.prominence));
    	ModelLoader.addSpecialModel(new ResourceLocation(KingdomKeys.MODID, "item/"+Strings.eternalFlames));
    	
    	ModelLoader.addSpecialModel(new ResourceLocation(KingdomKeys.MODID, "item/"+Strings.conformers));
    	
        //Other
    	ModelLoader.addSpecialModel(new ResourceLocation(KingdomKeys.MODID, "entity/portal"));
		ModelLoader.addSpecialModel(new ResourceLocation(KingdomKeys.MODID, "block/station_of_awakening"));
		ModelLoader.addSpecialModel(new ResourceLocation(KingdomKeys.MODID, "entity/heart"));
	}

	@Override
	public Player getClientPlayer() {
		return Minecraft.getInstance().player;
	}

	@Override
	public Level getClientWorld() {
		return Minecraft.getInstance().level;
	}

	@Override
	public Minecraft getClientMCInstance() {
		return Minecraft.getInstance();
	}

}

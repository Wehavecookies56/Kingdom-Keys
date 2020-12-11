package online.kingdomkeys.kingdomkeys.proxy;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
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
import online.kingdomkeys.kingdomkeys.client.render.DriveLayerRenderer;
import online.kingdomkeys.kingdomkeys.client.render.armor.ArmorModel;
import online.kingdomkeys.kingdomkeys.client.render.armor.VentusModel;
import online.kingdomkeys.kingdomkeys.container.ModContainers;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.handler.ClientEvents;
import online.kingdomkeys.kingdomkeys.handler.InputHandler;
import online.kingdomkeys.kingdomkeys.integration.corsair.KeyboardManager;
import online.kingdomkeys.kingdomkeys.item.ModItems;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus=Mod.EventBusSubscriber.Bus.MOD)
public class ProxyClient implements IProxy {

    public static KeyboardManager keyboardManager;
	public static final Map<Item, BipedModel> armorModels = new HashMap<Item, BipedModel>();

    @Override
    public void setup(FMLCommonSetupEvent event) {

        /*if (ClientConfig.CORSAIR_KEYBOARD_LIGHTING) {
            keyboardManager = new KeyboardManager();
            MinecraftForge.EVENT_BUS.register(new CorsairTickHandler(keyboardManager));
            keyboardManager.showLogo();
        }*/

       // new ScrollCallbackWrapper().setup(Minecraft.getInstance());
        //MinecraftForge.EVENT_BUS.register(new HUDElementHandler());
        MinecraftForge.EVENT_BUS.register(new CommandMenuGui());
        MinecraftForge.EVENT_BUS.register(new PlayerPortraitGui());
        MinecraftForge.EVENT_BUS.register(new HPGui());
        MinecraftForge.EVENT_BUS.register(new MPGui());
        MinecraftForge.EVENT_BUS.register(new DriveGui());
        MinecraftForge.EVENT_BUS.register(new InputHandler());
        MinecraftForge.EVENT_BUS.register(new LockOnGui());
        MinecraftForge.EVENT_BUS.register(new PartyHUDGui());
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

        RenderTypeLookup.setRenderLayer(ModBlocks.ghostBlox.get(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.magicalChest.get(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.mosaic_stained_glass.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(ModBlocks.soADoor.get(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.moogleProjector.get(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.pedestal.get(), RenderType.getCutout());
        
        PlayerRenderer renderPlayer = Minecraft.getInstance().getRenderManager().getSkinMap().get("default");
		renderPlayer.addLayer(new DriveLayerRenderer(renderPlayer));
		renderPlayer = Minecraft.getInstance().getRenderManager().getSkinMap().get("slim");
		renderPlayer.addLayer(new DriveLayerRenderer(renderPlayer));
        ModContainers.registerGUIFactories();
        
        DeferredWorkQueue.runLater(() -> {
        	ModelLoader.addSpecialModel(new ResourceLocation(KingdomKeys.MODID, "entity/portal"));
        	ModelLoader.addSpecialModel(new ResourceLocation(KingdomKeys.MODID, "item/zephyr"));
            ModelLoader.addSpecialModel(new ResourceLocation(KingdomKeys.MODID, "item/eternal_flames"));
            ModelLoader.addSpecialModel(new ResourceLocation(KingdomKeys.MODID, "item/burnout"));
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

	@Override
	public PlayerEntity getClientPlayer() {
		return Minecraft.getInstance().player;
	}

	@Override
	public World getClientWorld() {
		return Minecraft.getInstance().world;
	}

	@Override
	public Minecraft getClientMCInstance() {
		return Minecraft.getInstance();
	}

}

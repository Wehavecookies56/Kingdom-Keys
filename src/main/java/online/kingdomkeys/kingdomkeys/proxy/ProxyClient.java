package online.kingdomkeys.kingdomkeys.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;
import online.kingdomkeys.kingdomkeys.client.gui.CommandMenuGui;
import online.kingdomkeys.kingdomkeys.client.gui.DriveGui;
import online.kingdomkeys.kingdomkeys.client.gui.GuiOverlay;
import online.kingdomkeys.kingdomkeys.client.gui.HPGui;
import online.kingdomkeys.kingdomkeys.client.gui.MPGui;
import online.kingdomkeys.kingdomkeys.client.gui.PlayerPortraitGui;
import online.kingdomkeys.kingdomkeys.client.render.LayerRendererDrive;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.handler.ClientEvents;
import online.kingdomkeys.kingdomkeys.handler.InputHandler;
import online.kingdomkeys.kingdomkeys.integration.corsair.KeyboardManager;
import online.kingdomkeys.kingdomkeys.worldgen.OreGen;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class ProxyClient implements IProxy {

    public static KeyboardManager keyboardManager;

    @Override
    public void setup(FMLCommonSetupEvent event) {

       // ((KeybladeItem)ModItems.kingdomKey).getProperties().setTEISR(()->()->new KeybladeRenderer());

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
		//ModModels.register();
        


    }

    //Register the entity models
    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        ModEntities.registerModels();
		//ModelLoader.setCustomModelResourceLocation(ModItems.kingdomKey, 0, new ModelResourceLocation("", "inventory"));
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void setupClient(FMLClientSetupEvent event) {
        for (InputHandler.Keybinds key : InputHandler.Keybinds.values())
            ClientRegistry.registerKeyBinding(key.getKeybind());
        
		MinecraftForge.EVENT_BUS.register(new GuiOverlay());
        RenderTypeLookup.setRenderLayer(ModBlocks.ghostBlox, RenderType.getTranslucent());
        
        PlayerRenderer renderPlayer = Minecraft.getInstance().getRenderManager().getSkinMap().get("default");
		renderPlayer.addLayer(new LayerRendererDrive(renderPlayer));
		renderPlayer = Minecraft.getInstance().getRenderManager().getSkinMap().get("slim");
		renderPlayer.addLayer(new LayerRendererDrive(renderPlayer));
		MinecraftForge.EVENT_BUS.register(new ClientEvents());


    }

    @SubscribeEvent
    public static void LoadCompleteEvent(FMLLoadCompleteEvent event)
    {
        OreGen.generateOre();
    }

    /*@SubscribeEvent
    public static void onModelBakeEvent(ModelBakeEvent event) {
        //TODO make this simpler for doing this for every model
        try {
            IUnbakedModel model = ModelLoaderRegistry.getModelOrMissing(new ResourceLocation(KingdomKeys.MODID + ":item/kingdom_key.obj"));

            if (model instanceof OBJModel) {
                IBakedModel bakedModel = model.bake(event.getModelLoader(), ModelLoader.defaultTextureGetter(), new BasicState(model.getDefaultState(), false), DefaultVertexFormats.ITEM);
                event.getModelRegistry().put(new ModelResourceLocation(KingdomKeys.MODID + ":kingdom_key", "inventory"), bakedModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

}

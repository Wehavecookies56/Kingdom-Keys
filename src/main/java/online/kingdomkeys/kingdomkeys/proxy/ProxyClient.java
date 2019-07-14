package online.kingdomkeys.kingdomkeys.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.IUnbakedModel;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.BasicState;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.b3d.B3DLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.model.KeybladeModel;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.handler.ScrollCallbackWrapper;
import online.kingdomkeys.kingdomkeys.item.ModItems;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class ProxyClient implements IProxy {
    @Override
    public void setup(FMLCommonSetupEvent event) {
        //OBJLoader and B3DLoader currently aren't hooked up however, this is here for when they are
        OBJLoader.INSTANCE.addDomain(KingdomKeys.MODID);
        //TODO convert B3D models to OBJ so we don't need this
        B3DLoader.INSTANCE.addDomain(KingdomKeys.MODID);
        new ScrollCallbackWrapper().setup(Minecraft.getInstance());
    }

    //Register the entity models
    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        ModEntities.registerModels();
		//ModelLoader.setCustomModelResourceLocation(ModItems.kingdomKey, 0, new ModelResourceLocation("", "inventory"));


    }

    @SubscribeEvent
    public static void onModelBakeEvent(ModelBakeEvent event) {
        //TODO make this simpler for doing this for every model
        try {
            ResourceLocation modelLoc = ModelLoaderRegistry.getActualLocation(new ResourceLocation("kingdomkeys:item/kingdom_key.obj"));
            IUnbakedModel model = new OBJModel.Parser(Minecraft.getInstance().getResourceManager().getResource(modelLoc), Minecraft.getInstance().getResourceManager()).parse();
            IBakedModel bakedModel = model.bake(event.getModelLoader(), ModelLoader.defaultTextureGetter(), new BasicState(model.getDefaultState(), false), DefaultVertexFormats.ITEM);
            KeybladeModel keybladeModel = new KeybladeModel(bakedModel);
            event.getModelRegistry().put(new ModelResourceLocation(ModItems.kingdomKey.getRegistryName(), "inventory"), keybladeModel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

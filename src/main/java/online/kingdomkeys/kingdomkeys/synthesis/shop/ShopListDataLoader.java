package online.kingdomkeys.kingdomkeys.synthesis.shop;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.ServerLifecycleHooks;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncShopData;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class ShopListDataLoader extends SimpleJsonResourceReloadListener {

    //GSON builder with custom deserializer for keyblade data
    public static final Gson GSON_BUILDER = new GsonBuilder().registerTypeAdapter(ShopList.class, new ShopListDataDeserializer()).setPrettyPrinting().create();

    public ShopListDataLoader() {
        super(GSON_BUILDER, "shop");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> objectIn, ResourceManager resourceManagerIn, ProfilerFiller profilerIn) {
        KingdomKeys.LOGGER.info("Loading shop data");
        loadData(resourceManagerIn);
        if (ServerLifecycleHooks.getCurrentServer() != null) {
            for (ServerPlayer player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()) {
                PacketHandler.sendTo(new SCSyncShopData(ShopListRegistry.getInstance().getValues()), player);
            }
        }
    }

    /**
     * Method searches the keyblades folder in the datapack for all json files inside it.
     * Loaded data is assigned to the keyblade with the same name as the json file
     * @param manager Resource manager from the server
     */
    public void loadData(ResourceManager manager) {
        String folder = "shop";
        String extension = ".json";

        ShopListRegistry.getInstance().clearRegistry();
        for (ResourceLocation file : manager.listResources(folder, n -> n.toString().endsWith(extension)).keySet()) { //Get all .json files
            ResourceLocation shopList = new ResourceLocation(file.getNamespace(), file.getPath().substring(folder.length() + 1, file.getPath().length() - extension.length()));
            try {
            	ShopList result;
                try {
                    result = GSON_BUILDER.fromJson(manager.getResource(file).get().openAsReader(), ShopList.class);
                    result.setRegistryName(file.getNamespace(), file.getPath().substring(folder.length() + 1, file.getPath().length() - extension.length()));
                } catch (JsonParseException e) {
                	//System.out.println(file+" is having issues "+shopList);
                    KingdomKeys.LOGGER.error("Error parsing json file {}: {}", manager.getResource(file).get().sourcePackId().toString(), e);
                    continue;
                }
                ShopListRegistry.getInstance().register(result);
                
                IOUtils.closeQuietly(manager.getResource(file).get().openAsReader());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        KingdomKeys.LOGGER.info("Loaded shop");
    }
}

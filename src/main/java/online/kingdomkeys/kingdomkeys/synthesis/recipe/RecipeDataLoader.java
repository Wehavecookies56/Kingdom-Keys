package online.kingdomkeys.kingdomkeys.synthesis.recipe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncSynthesisData;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class RecipeDataLoader extends JsonReloadListener {

    //GSON builder with custom deserializer for keyblade data
    public static final Gson GSON_BUILDER = new GsonBuilder().registerTypeAdapter(Recipe.class, new RecipeDataDeserializer()).setPrettyPrinting().create();
    /**
     * Method searches the keyblades folder in the datapack for all json files inside it.
     * Loaded data is assigned to the keyblade with the same name as the json file
     * @param manager Resource manager from the server
     */

    public RecipeDataLoader() {
        super(GSON_BUILDER, "synthesis");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> objectIn, IResourceManager resourceManagerIn, IProfiler profilerIn) {
        KingdomKeys.LOGGER.info("Loading recipe data");
        loadData(resourceManagerIn);
        if (ServerLifecycleHooks.getCurrentServer() != null) {
            for (ServerPlayerEntity player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()) {
                PacketHandler.sendTo(new SCSyncSynthesisData(RecipeRegistry.getInstance().getValues()), player);
            }
        }
    }

    public void loadData(IResourceManager manager) {
        String folder = "synthesis";
        String extension = ".json";

        RecipeRegistry.getInstance().clearRegistry();
        for (ResourceLocation file : manager.getAllResourceLocations(folder, n -> n.endsWith(extension))) { //Get all .json files
            ResourceLocation recipe = new ResourceLocation(file.getNamespace(), file.getPath().substring(folder.length() + 1, file.getPath().length() - extension.length()));
            try {
            	Recipe result;
                try {
                    result = GSON_BUILDER.fromJson(new BufferedReader(new InputStreamReader(manager.getResource(file).getInputStream())), Recipe.class);
                    result.setRegistryName(file.getNamespace(), file.getPath().substring(folder.length() + 1, file.getPath().length() - extension.length()));
                } catch (JsonParseException e) {
                	System.out.println(file+" is having issues "+recipe);
                    KingdomKeys.LOGGER.error("Error parsing json file {}: {}", manager.getResource(file).getLocation().toString(), e);
                    continue;
                }
                RecipeRegistry.getInstance().register(result);
                
                IOUtils.closeQuietly(manager.getResource(file));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}

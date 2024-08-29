package online.kingdomkeys.kingdomkeys.leveling;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

public class LevelingDataLoader extends SimpleJsonResourceReloadListener {

    //GSON builder with custom deserializer for keyblade data
    public static final Gson GSON_BUILDER = new GsonBuilder().registerTypeAdapter(LevelingData.class, new LevelingDataDeserializer()).setPrettyPrinting().create();
    
    public LevelingDataLoader() {
        super(GSON_BUILDER, "levels");
    }
    
    @Override
    protected void apply(Map<ResourceLocation, JsonElement> objectIn, ResourceManager resourceManagerIn, ProfilerFiller profilerIn) {
        KingdomKeys.LOGGER.info("Loading levels data");
        loadData(resourceManagerIn);
    }

    /**
     * Method searches the keyblades folder in the datapack for all json files inside it.
     * Loaded data is assigned to the keyblade with the same name as the json file
     * @param manager Resource manager from the server
     */
    public void loadData(ResourceManager manager) {
        String folder = "leveling";
        String extension = ".json";

        for (ResourceLocation file : manager.listResources(folder, n -> n.toString().endsWith(extension)).keySet()) { //Get all .json files
            ResourceLocation levelName = ResourceLocation.fromNamespaceAndPath(file.getNamespace(), file.getPath().substring(folder.length() + 1, file.getPath().length() - extension.length()));
			Level level = ModLevels.registry.get(levelName);
            try {
            	BufferedReader br2 = manager.getResource(file).get().openAsReader();
            	LevelingData result;
                try {
                    result = GSON_BUILDER.fromJson(br2, LevelingData.class);
                } catch (JsonParseException e) {
                    KingdomKeys.LOGGER.error("Error parsing json file {}: {}", manager.getResource(file).get().sourcePackId().toString(), e);
                    continue;
                }
                level.setLevelingData(result);
                IOUtils.closeQuietly(br2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}


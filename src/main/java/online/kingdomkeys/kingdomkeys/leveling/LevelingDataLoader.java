package online.kingdomkeys.kingdomkeys.leveling;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import online.kingdomkeys.kingdomkeys.limit.ModLimits;
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
        super(GSON_BUILDER, "leveling");
    }
    
    @Override
    protected void apply(Map<ResourceLocation, JsonElement> objectIn, ResourceManager resourceManagerIn, ProfilerFiller profilerIn) {
        AtomicInteger count = new AtomicInteger();
        objectIn.forEach((resourceLocation, element) -> {
            try {
                if (ModLevels.registry.get().containsKey(resourceLocation)) {
                    Level level = ModLevels.registry.get().getValue(resourceLocation);
                    LevelingData result = GSON_BUILDER.fromJson(element, LevelingData.class);
                    level.setLevelingData(result);
                    count.incrementAndGet();
                } else {
                    KingdomKeys.LOGGER.warn("Found level data {} for level that doesn't exist", resourceLocation);
                }
            } catch (JsonParseException e) {
                KingdomKeys.LOGGER.error("Error parsing level json file {}: {}", resourceLocation, e);
            }
        });
        KingdomKeys.LOGGER.info("Loaded {} leveling data", count.get());
    }
}


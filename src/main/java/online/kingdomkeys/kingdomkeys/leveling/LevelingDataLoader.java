package online.kingdomkeys.kingdomkeys.leveling;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncLevelingData;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class LevelingDataLoader extends SimpleJsonResourceReloadListener {

    //GSON builder with custom deserializer for levelling data
    public static final Gson GSON_BUILDER = new GsonBuilder().registerTypeAdapter(LevelingData.class, new LevelingDataDeserializer()).setPrettyPrinting().create();
    public static List<String> names = new LinkedList<>();
    public static List<String> dataList = new LinkedList<>();

    public LevelingDataLoader() {
        super(GSON_BUILDER, "leveling");
    }
    
    @Override
    protected void apply(Map<ResourceLocation, JsonElement> objectIn, ResourceManager resourceManagerIn, ProfilerFiller profilerIn) {
        names.clear();
        dataList.clear();

        AtomicInteger count = new AtomicInteger();
        objectIn.forEach((resourceLocation, element) -> {
            try {
                if (ModLevels.registry.containsKey(resourceLocation)) {
                    Level level = ModLevels.registry.get(resourceLocation);
                    LevelingData result = GSON_BUILDER.fromJson(element, LevelingData.class);
                    dataList.add(element.toString());
                    names.add(resourceLocation.toString());
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
        if (ServerLifecycleHooks.getCurrentServer() != null) {
            for (ServerPlayer player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()) {
                PacketHandler.sendTo(new SCSyncLevelingData(LevelingDataLoader.names, LevelingDataLoader.dataList), player);
            }
        }
    }
}


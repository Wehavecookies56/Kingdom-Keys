package online.kingdomkeys.kingdomkeys.limit;

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
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncLimitData;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class LimitDataLoader extends SimpleJsonResourceReloadListener {

    //GSON builder with custom deserializer for limit data
    public static final Gson GSON_BUILDER = new GsonBuilder().registerTypeAdapter(LimitData.class, new LimitDataDeserializer()).setPrettyPrinting().create();
    
    public LimitDataLoader() {
        super(GSON_BUILDER, "limits");
    }
    
    public static List<String> names = new LinkedList<>();
    public static List<String> dataList = new LinkedList<>();

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> objectIn, ResourceManager resourceManagerIn, ProfilerFiller profilerIn) {
        KingdomKeys.LOGGER.info("Loading limits data");
        AtomicInteger count = new AtomicInteger();
        objectIn.forEach((resourceLocation, element) -> {
            try {
                if (ModLimits.registry.containsKey(resourceLocation)) {
                    Limit limit = ModLimits.registry.get(resourceLocation);
                    dataList.add(element.toString());
                    LimitData result = GSON_BUILDER.fromJson(element, LimitData.class);
                    names.add(resourceLocation.toString());
                    limit.setLimitData(result);
                    count.incrementAndGet();
                } else {
                    KingdomKeys.LOGGER.warn("Found limit data {} for limit that doesn't exist", resourceLocation);
                }
            } catch (JsonParseException e) {
                KingdomKeys.LOGGER.error("Error parsing limit json file {}: {}", resourceLocation, e);
            }
        });
        KingdomKeys.LOGGER.info("Loaded {} limits data", count.get());
        if (ServerLifecycleHooks.getCurrentServer() != null) {
            for (ServerPlayer player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()) {
                PacketHandler.sendTo(new SCSyncLimitData(names,dataList), player);
            }
        }
    }
}


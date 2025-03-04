package online.kingdomkeys.kingdomkeys.magic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.server.ServerLifecycleHooks;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncMagicData;
import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class MagicDataLoader extends SimpleJsonResourceReloadListener {

    //GSON builder with custom deserializer for keyblade data
    public static final Gson GSON_BUILDER = new GsonBuilder().registerTypeAdapter(MagicData.class, new MagicDataDeserializer()).setPrettyPrinting().create();
    
    public MagicDataLoader() {
        super(GSON_BUILDER, "magics");
    }
    
    public static List<String> names = new LinkedList<>();
    public static List<String> dataList = new LinkedList<>();

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> objectIn, ResourceManager resourceManagerIn, ProfilerFiller profilerIn) {
        names.clear();
        dataList.clear();
        AtomicInteger count = new AtomicInteger();
        objectIn.forEach((resourceLocation, element) -> {
            try {
                if (ModMagic.registry.get().containsKey(resourceLocation)) {
                    Magic magic = ModMagic.registry.get().getValue(resourceLocation);
                    dataList.add(element.toString());
                    MagicData result = GSON_BUILDER.fromJson(element, MagicData.class);
                    names.add(resourceLocation.toString());;
                    magic.setMagicData(result);
                    count.incrementAndGet();
                } else {
                    KingdomKeys.LOGGER.warn("Found magic data {} for magic that doesn't exist", resourceLocation);
                }
            } catch (JsonParseException e) {
                KingdomKeys.LOGGER.error("Error parsing magic json file {}: {}", resourceLocation, e);
            }
        });
        KingdomKeys.LOGGER.info("Loaded {} magics data", count.get());
        if (ServerLifecycleHooks.getCurrentServer() != null) {
            for (ServerPlayer player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()) {
                PacketHandler.sendTo(new SCSyncMagicData(names,dataList), player);
            }
        }
    }
}


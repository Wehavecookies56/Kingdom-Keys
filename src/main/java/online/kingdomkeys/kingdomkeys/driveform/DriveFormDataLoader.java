package online.kingdomkeys.kingdomkeys.driveform;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

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
import net.minecraftforge.server.ServerLifecycleHooks;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncDriveFormData;

public class DriveFormDataLoader extends SimpleJsonResourceReloadListener {

    //GSON builder with custom deserializer for driveform data
    public static final Gson GSON_BUILDER = new GsonBuilder().registerTypeAdapter(DriveFormData.class, new DriveFormDataDeserializer()).setPrettyPrinting().create();

    
    public DriveFormDataLoader() {
        super(GSON_BUILDER, "driveforms");
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
                if (ModDriveForms.registry.get().containsKey(resourceLocation)) {
                    DriveForm driveform = ModDriveForms.registry.get().getValue(resourceLocation);
                    dataList.add(element.toString());
                    DriveFormData result = GSON_BUILDER.fromJson(element, DriveFormData.class);
                    driveform.setDriveFormData(result);
                    names.add(resourceLocation.toString());
                    count.incrementAndGet();
                } else {
                    KingdomKeys.LOGGER.warn("Found drive form data {} for drive form that doesn't exist", resourceLocation);
                }
            } catch (JsonParseException e) {
                KingdomKeys.LOGGER.error("Error parsing driveform json file {}: {}", resourceLocation, e);
            }
        });
        KingdomKeys.LOGGER.info("Loaded {} driveforms data", count.get());
        if (ServerLifecycleHooks.getCurrentServer() != null) {
            for (ServerPlayer player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()) {
                PacketHandler.sendTo(new SCSyncDriveFormData(names,dataList), player);
            }
        }
    }
}


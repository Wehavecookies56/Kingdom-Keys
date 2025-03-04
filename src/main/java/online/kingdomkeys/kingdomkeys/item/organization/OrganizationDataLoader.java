package online.kingdomkeys.kingdomkeys.item.organization;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.server.ServerLifecycleHooks;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncOrganizationData;
import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class OrganizationDataLoader extends SimpleJsonResourceReloadListener {

    //GSON builder with custom deserializer for organization data
    public static final Gson GSON_BUILDER = new GsonBuilder().registerTypeAdapter(OrganizationData.class, new OrganizationDataDeserializer()).setPrettyPrinting().create();
    
    public static List<String> names = new LinkedList<>();
    public static List<String> dataList = new LinkedList<>();

    public OrganizationDataLoader() {
        super(GSON_BUILDER, "organization");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> objectIn, ResourceManager resourceManagerIn, ProfilerFiller profilerIn) {
        names.clear();
        dataList.clear();
        AtomicInteger count = new AtomicInteger();
        objectIn.forEach((resourceLocation, element) -> {
            try {
                if (ForgeRegistries.ITEMS.containsKey(resourceLocation)) {
                    try {
                        IOrgWeapon weapon = (IOrgWeapon) ForgeRegistries.ITEMS.getValue(resourceLocation);
                        dataList.add(element.toString());
                        OrganizationData result = GSON_BUILDER.fromJson(element, OrganizationData.class);
                        names.add(resourceLocation.toString());
                        weapon.setOrganizationData(result);
                        count.incrementAndGet();
                    } catch (ClassCastException e) {
                        KingdomKeys.LOGGER.warn("Organization weapon data for non organization weapon found {}", resourceLocation);
                    }
                } else {
                    KingdomKeys.LOGGER.warn("Found organization weapon data {} for organization weapon that doesn't exist", resourceLocation);
                }

            } catch (JsonParseException e) {
                KingdomKeys.LOGGER.error("Error parsing organization json file {}: {}", resourceLocation, e);
            }
        });
        KingdomKeys.LOGGER.info("Loaded {} organization data", count.get());
        if (ServerLifecycleHooks.getCurrentServer() != null) {
            for (ServerPlayer player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()) {
                PacketHandler.sendTo(new SCSyncOrganizationData(OrganizationDataLoader.names, OrganizationDataLoader.dataList), player);
            }
        }
    }
}

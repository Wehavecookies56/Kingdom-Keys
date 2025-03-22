package online.kingdomkeys.kingdomkeys.synthesis.shop;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
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
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncShopData;

public class ShopListDataLoader extends SimpleJsonResourceReloadListener {

    //GSON builder with custom deserializer for shop data
    public static final Gson GSON_BUILDER = new GsonBuilder().registerTypeAdapter(ShopList.class, new ShopListDataDeserializer()).setPrettyPrinting().create();

    public ShopListDataLoader() {
        super(GSON_BUILDER, "shop");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> objectIn, ResourceManager resourceManagerIn, ProfilerFiller profilerIn) {
        ShopListRegistry.getInstance().clearRegistry();
        AtomicInteger count = new AtomicInteger();
        objectIn.forEach((resourceLocation, element) -> {
            if (!resourceLocation.getPath().contains("names/")) {
                try {
                    ShopList result = GSON_BUILDER.fromJson(element, ShopList.class);
                    result.setRegistryName(resourceLocation);
                    ShopListRegistry.getInstance().register(result);
                    count.incrementAndGet();
                } catch (JsonParseException e) {
                    KingdomKeys.LOGGER.error("Error parsing json file {}: {}", resourceLocation, e);
                }
            }
        });
        KingdomKeys.LOGGER.info("Loaded {} shop data", count.get());
        if (ServerLifecycleHooks.getCurrentServer() != null) {
            for (ServerPlayer player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()) {
                PacketHandler.sendTo(new SCSyncShopData(ShopListRegistry.getInstance().getValues()), player);
            }
        }
    }
}

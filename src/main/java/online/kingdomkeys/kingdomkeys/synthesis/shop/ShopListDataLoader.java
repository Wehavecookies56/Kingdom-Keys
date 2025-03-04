package online.kingdomkeys.kingdomkeys.synthesis.shop;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.gson.*;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.ServerLifecycleHooks;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncMoogleNames;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncShopData;
import online.kingdomkeys.kingdomkeys.synthesis.shop.names.NamesListRegistry;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class ShopListDataLoader extends SimpleJsonResourceReloadListener {

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

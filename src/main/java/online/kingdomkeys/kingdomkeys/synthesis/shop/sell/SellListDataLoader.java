package online.kingdomkeys.kingdomkeys.synthesis.shop.sell;

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
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncSellData;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class SellListDataLoader extends SimpleJsonResourceReloadListener {

    //GSON builder with custom deserializer for Sell data
    public static final Gson GSON_BUILDER = new GsonBuilder().registerTypeAdapter(SellList.class, new SellListDataDeserializer()).setPrettyPrinting().create();

    public SellListDataLoader() {
        super(GSON_BUILDER, "sell");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> objectIn, ResourceManager resourceManagerIn, ProfilerFiller profilerIn) {
        SellListRegistry.getInstance().clearRegistry();
        AtomicInteger count = new AtomicInteger();
        ResourceLocation sellId = ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "sell");
        JsonElement element = objectIn.get(sellId);

        if (element != null) {
            try {
                SellList result = GSON_BUILDER.fromJson(element, SellList.class);
                result.setRegistryName(sellId);
                SellListRegistry.getInstance().register(result);
                count.incrementAndGet();
            } catch (JsonParseException e) {
                KingdomKeys.LOGGER.error("Error parsing json file {}: {}", sellId, e);
            }
        }

        KingdomKeys.LOGGER.info("Loaded {} sell data", count.get());
        if (ServerLifecycleHooks.getCurrentServer() != null) {
            for (ServerPlayer player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()) {
                PacketHandler.sendTo(new SCSyncSellData(SellListRegistry.getInstance().getValues()), player);
            }
        }
    }
}

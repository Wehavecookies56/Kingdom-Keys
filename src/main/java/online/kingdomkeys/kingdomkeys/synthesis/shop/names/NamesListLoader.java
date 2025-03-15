package online.kingdomkeys.kingdomkeys.synthesis.shop.names;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.apache.commons.io.IOUtils;

import com.google.common.reflect.TypeToken;
import com.google.gson.*;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncMoogleNames;

public class NamesListLoader {

    public static class Loader extends SimpleJsonResourceReloadListener {

        private static final Type stringList = new TypeToken<List<String>>() {}.getType();

        public static final Gson GSON_BUILDER = new GsonBuilder().registerTypeAdapter(stringList, (JsonDeserializer<List<String>>) (json, typeOfT, context) -> json.getAsJsonArray().asList().stream().map(JsonElement::getAsString).toList()).setPrettyPrinting().create();

        public Loader() {
            super(GSON_BUILDER, "shop/names");
        }

        @Override
        protected void apply(Map<ResourceLocation, JsonElement> pObject, ResourceManager pResourceManager, ProfilerFiller pProfiler) {
            NamesListRegistry.getInstance().clearRegistry();
            AtomicInteger count = new AtomicInteger();
            pObject.forEach((resourceLocation, element) -> {
                try {
                    List<String> result = GSON_BUILDER.fromJson(element, stringList);
                    NamesListRegistry.getInstance().register(resourceLocation, result);
                    count.incrementAndGet();
                } catch (JsonParseException e) {
                    KingdomKeys.LOGGER.error("Error parsing json file {}: {}", resourceLocation, e);
                }
            });
            KingdomKeys.LOGGER.info("Loaded {} shop/names data", count.get());
            if (ServerLifecycleHooks.getCurrentServer() != null) {
                for (ServerPlayer player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()) {
                    PacketHandler.sendTo(new SCSyncMoogleNames(NamesListRegistry.getInstance()), player);
                }
            }
        }
    }
}

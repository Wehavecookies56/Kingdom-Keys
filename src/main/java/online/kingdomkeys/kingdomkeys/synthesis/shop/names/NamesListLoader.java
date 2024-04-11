package online.kingdomkeys.kingdomkeys.synthesis.shop.names;

import com.google.common.reflect.TypeToken;
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
import online.kingdomkeys.kingdomkeys.synthesis.shop.ShopList;
import online.kingdomkeys.kingdomkeys.synthesis.shop.ShopListDataDeserializer;
import online.kingdomkeys.kingdomkeys.synthesis.shop.ShopListRegistry;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NamesListLoader {

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class Loader extends SimpleJsonResourceReloadListener {

        private static final Type stringList = new TypeToken<List<String>>(){}.getType();

        public static final Gson GSON_BUILDER = new GsonBuilder().registerTypeAdapter(stringList, (JsonDeserializer<List<String>>) (json, typeOfT, context) -> json.getAsJsonArray().asList().stream().map(JsonElement::getAsString).toList()).setPrettyPrinting().create();

        public Loader() {
            super(GSON_BUILDER, "shop/names");
        }

        @Override
        protected void apply(Map<ResourceLocation, JsonElement> pObject, ResourceManager pResourceManager, ProfilerFiller pProfiler) {
            KingdomKeys.LOGGER.info("Loading Moogle names");
            loadData(pResourceManager);
            if (ServerLifecycleHooks.getCurrentServer() != null) {
                for (ServerPlayer player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()) {
                    PacketHandler.sendTo(new SCSyncMoogleNames(NamesListRegistry.getInstance()), player);
                }
            }
        }

        public void loadData(ResourceManager manager) {
            String folder = "shop/names";
            String extension = ".json";

            NamesListRegistry.getInstance().clearRegistry();
            for (ResourceLocation file : manager.listResources(folder, n -> n.toString().endsWith(extension)).keySet()) { //Get all .json files
                ResourceLocation namesList = new ResourceLocation(file.getNamespace(), file.getPath().substring(folder.length() + 1, file.getPath().length() - extension.length()));
                try {
                    ResourceLocation registryName;
                    List<String> result;
                    try {
                        result = GSON_BUILDER.fromJson(manager.getResource(file).get().openAsReader(), stringList);
                        registryName = new ResourceLocation(file.getNamespace(), file.getPath().substring(folder.length() + 1, file.getPath().length() - extension.length()));
                    } catch (JsonParseException e) {
                        KingdomKeys.LOGGER.error("Error parsing json file {}: {}", manager.getResource(file).get().sourcePackId().toString(), e);
                        continue;
                    }
                    NamesListRegistry.getInstance().register(registryName, result);

                    IOUtils.closeQuietly(manager.getResource(file).get().openAsReader());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            KingdomKeys.LOGGER.info("Loaded Moogle names");
        }
    }
}

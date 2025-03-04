package online.kingdomkeys.kingdomkeys.synthesis.shop;

import java.util.Map;

import com.google.gson.*;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.fml.common.Mod;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class ShopListDataLoader extends SimpleJsonResourceReloadListener {

    public static final Gson GSON_BUILDER = new GsonBuilder().registerTypeAdapter(ShopList.class, new ShopListDataDeserializer()).setPrettyPrinting().create();

    public ShopListDataLoader() {
        super(GSON_BUILDER, "shop");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> objectIn, ResourceManager resourceManagerIn, ProfilerFiller profilerIn) {
        KingdomKeys.LOGGER.info("Loading shop data");
        ShopListRegistry.getInstance().clearRegistry();
        objectIn.forEach((resourceLocation, element) -> {
            if (!resourceLocation.getPath().contains("names/")) {
                try {
                    ShopList result = GSON_BUILDER.fromJson(element, ShopList.class);
                    result.setRegistryName(resourceLocation);
                    ShopListRegistry.getInstance().register(result);
                } catch (JsonParseException e) {
                    KingdomKeys.LOGGER.error("Error parsing json file {}: {}", resourceLocation, e);
                }
            }
        });
    }
}

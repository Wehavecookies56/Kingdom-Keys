package online.kingdomkeys.kingdomkeys.synthesis.recipe;

import java.io.IOException;
import java.util.Map;

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
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.ServerLifecycleHooks;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncSynthesisData;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class RecipeDataLoader extends SimpleJsonResourceReloadListener {

    //GSON builder with custom deserializer for keyblade data
    public static final Gson GSON_BUILDER = new GsonBuilder().registerTypeAdapter(Recipe.class, new RecipeDataDeserializer()).setPrettyPrinting().create();

    public RecipeDataLoader() {
        super(GSON_BUILDER, "synthesis");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> objectIn, ResourceManager resourceManagerIn, ProfilerFiller profilerIn) {
        KingdomKeys.LOGGER.info("Loading recipe data");
        RecipeRegistry.getInstance().clearRegistry();
        objectIn.forEach((resourceLocation, element) -> {
                    try {
                        Recipe result = GSON_BUILDER.fromJson(element, Recipe.class);
                        result.setRegistryName(resourceLocation);
                        RecipeRegistry.getInstance().register(result);
                    } catch (JsonParseException e) {
                        KingdomKeys.LOGGER.error("Error parsing json file {}: {}", resourceLocation, e);
                    }
        });
        if (ServerLifecycleHooks.getCurrentServer() != null) {
            for (ServerPlayer player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()) {
                PacketHandler.sendTo(new SCSyncSynthesisData(RecipeRegistry.getInstance().getValues()), player);
            }
        }
    }
}

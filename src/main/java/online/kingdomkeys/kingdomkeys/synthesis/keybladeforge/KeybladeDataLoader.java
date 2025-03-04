package online.kingdomkeys.kingdomkeys.synthesis.keybladeforge;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
import online.kingdomkeys.kingdomkeys.item.KeybladeItem;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncKeybladeData;

public class KeybladeDataLoader extends SimpleJsonResourceReloadListener {

    //GSON builder with custom deserializer for keyblade data
    public static final Gson GSON_BUILDER = new GsonBuilder().registerTypeAdapter(KeybladeData.class, new KeybladeDataDeserializer()).create();
    
    public static List<String> names = new LinkedList<>();
    public static List<String> dataList = new LinkedList<>();

    public KeybladeDataLoader() {
        super(GSON_BUILDER, "keyblades");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> objectIn, ResourceManager resourceManagerIn, ProfilerFiller profilerIn) {
        KingdomKeys.LOGGER.info("Loading keyblade data");
        names.clear();
        dataList.clear();
        objectIn.forEach((resourceLocation, element) -> {
                try {
                    if (ForgeRegistries.ITEMS.containsKey(resourceLocation)) {
                        KeybladeItem keyblade = (KeybladeItem) ForgeRegistries.ITEMS.getValue(resourceLocation);
                        KeybladeData result = GSON_BUILDER.fromJson(element, KeybladeData.class);
                        dataList.add(element.toString());
                        names.add(resourceLocation.toString());
                        keyblade.setKeybladeData(result);
                        if (result.keychain != null) {
                            result.keychain.setKeyblade(keyblade);
                        }
                    } else {
                        KingdomKeys.LOGGER.warn("Found keyblade data {} for keyblade that doesn't exist", resourceLocation);
                    }
                } catch (JsonParseException e) {
                    KingdomKeys.LOGGER.error("Error parsing json file {}: {}", resourceLocation, e);
                }
        });
        if (ServerLifecycleHooks.getCurrentServer() != null) {
            for (ServerPlayer player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()) {
                PacketHandler.sendTo(new SCSyncKeybladeData(KeybladeDataLoader.names, KeybladeDataLoader.dataList), player);
            }
        }
    }
}

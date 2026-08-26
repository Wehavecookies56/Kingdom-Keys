package online.kingdomkeys.kingdomkeys.shotlock;

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
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncShotlockData;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class ShotlockDataLoader extends SimpleJsonResourceReloadListener {

	public static final Gson GSON_BUILDER = new GsonBuilder().registerTypeAdapter(ShotlockData.class, new ShotlockDataDeserializer()).setPrettyPrinting().create();

	public ShotlockDataLoader() {
		super(GSON_BUILDER, "shotlocks");
	}

	public static List<String> names = new LinkedList<>();
	public static List<String> dataList = new LinkedList<>();

	@Override
	protected void apply(Map<ResourceLocation, JsonElement> objectIn, ResourceManager resourceManagerIn, ProfilerFiller profilerIn) {
		KingdomKeys.LOGGER.info("Loading shotlocks data");
		names.clear();
		dataList.clear();
		AtomicInteger count = new AtomicInteger();
		objectIn.forEach((resourceLocation, element) -> {
			try {
				if (ModShotlocks.registry.containsKey(resourceLocation)) {
					Shotlock shotlock = ModShotlocks.registry.get(resourceLocation);
					dataList.add(element.toString());
					ShotlockData result = GSON_BUILDER.fromJson(element, ShotlockData.class);
					names.add(resourceLocation.toString());
					shotlock.setShotlockData(result);
					count.incrementAndGet();
				} else {
					KingdomKeys.LOGGER.warn("Found shotlock data {} for shotlock that doesn't exist", resourceLocation);
				}
			} catch (JsonParseException e) {
				KingdomKeys.LOGGER.error("Error parsing shotlock json file {}: {}", resourceLocation, e);
			}
		});
		KingdomKeys.LOGGER.info("Loaded {} shotlocks data", count.get());
		if (ServerLifecycleHooks.getCurrentServer() != null) {
			for (ServerPlayer player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()) {
				PacketHandler.sendTo(new SCSyncShotlockData(names, dataList), player);
			}
		}
	}
}

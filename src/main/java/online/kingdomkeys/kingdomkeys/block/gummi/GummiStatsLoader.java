package online.kingdomkeys.kingdomkeys.block.gummi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncGummiStats;

import java.util.*;

public class GummiStatsLoader extends SimpleJsonResourceReloadListener {
	public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

	public static volatile List<String> names = List.of();
	public static volatile List<String> dataList = List.of();

	private static volatile Map<ResourceLocation, GummiStats> STATS = Map.of();

	public GummiStatsLoader() {
		super(GSON, "gummi_stats");
	}

	@Override
	protected void apply(Map<ResourceLocation, JsonElement> objectIn, ResourceManager resourceManagerIn, ProfilerFiller profilerIn) {
		KingdomKeys.LOGGER.info("Loading gummi stats data");

		Map<ResourceLocation, GummiStats> loaded = new LinkedHashMap<>();
		List<String> loadedNames = new ArrayList<>();
		List<String> loadedData = new ArrayList<>();

		objectIn.forEach((resourceLocation, element) -> GummiStats.CODEC.parse(JsonOps.INSTANCE, element)
				.resultOrPartial(error -> KingdomKeys.LOGGER.error("Error parsing gummi stats json file {}: {}", resourceLocation, error))
				.ifPresent(stats -> {
					loaded.put(resourceLocation, stats);
					loadedNames.add(resourceLocation.toString());
					loadedData.add(element.toString());
				}));

		STATS = Collections.unmodifiableMap(loaded);
		names = List.copyOf(loadedNames);
		dataList = List.copyOf(loadedData);

		KingdomKeys.LOGGER.info("Loaded {} gummi stats", loaded.size());

		if (ServerLifecycleHooks.getCurrentServer() != null) {
			for (ServerPlayer player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()) {
				PacketHandler.sendTo(new SCSyncGummiStats(names, dataList), player);
			}
		}
	}

	public static Map<ResourceLocation, GummiStats> all() {
		return STATS;
	}

	public static GummiStats get(ResourceLocation id) {
		return STATS.get(id);
	}

	public static void replaceAll(Map<ResourceLocation, GummiStats> stats) {
		STATS = Collections.unmodifiableMap(new LinkedHashMap<>(stats));
	}
}

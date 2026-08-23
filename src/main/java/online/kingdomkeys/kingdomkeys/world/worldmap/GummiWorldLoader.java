package online.kingdomkeys.kingdomkeys.world.worldmap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncGummiWorlds;

import java.util.*;

public class GummiWorldLoader extends SimpleJsonResourceReloadListener {

	public static final Gson GSON = new GsonBuilder().registerTypeAdapter(GummiWorld.class, new GummiWorldDeserializer()).setPrettyPrinting().create();

	// Never mutated in place. A reload, or the sync packet arriving on the client thread, swaps whole
	// collections instead, so whoever is halfway through iterating one simply finishes with the old copy.
	// Clearing them live was crashing the server tick with a ConcurrentModificationException.
	public static volatile List<String> names = List.of();
	public static volatile List<String> dataList = List.of();

	private static volatile Map<ResourceLocation, GummiWorld> WORLDS = Map.of();

	public GummiWorldLoader() {
		super(GSON, "gummi_worlds");
	}

	@Override
	protected void apply(Map<ResourceLocation, JsonElement> objectIn, ResourceManager resourceManagerIn, ProfilerFiller profilerIn) {
		KingdomKeys.LOGGER.info("Loading gummi world data");

		Map<ResourceLocation, GummiWorld> loaded = new LinkedHashMap<>();
		List<String> loadedNames = new ArrayList<>();
		List<String> loadedData = new ArrayList<>();

		objectIn.forEach((resourceLocation, element) -> {
			try {
				loaded.put(resourceLocation, GSON.fromJson(element, GummiWorld.class));
				loadedNames.add(resourceLocation.toString());
				loadedData.add(element.toString());
			} catch (JsonParseException e) {
				KingdomKeys.LOGGER.error("Error parsing gummi world json file {}: {}", resourceLocation, e);
			}
		});

		WORLDS = Collections.unmodifiableMap(loaded);
		names = List.copyOf(loadedNames);
		dataList = List.copyOf(loadedData);

		KingdomKeys.LOGGER.info("Loaded {} gummi worlds", loaded.size());

		if (ServerLifecycleHooks.getCurrentServer() != null) {
			for (ServerPlayer player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()) {
				PacketHandler.sendTo(new SCSyncGummiWorlds(names, dataList), player);
			}
		}
	}

	public static Map<ResourceLocation, GummiWorld> all() {
		return WORLDS;
	}

	public static GummiWorld get(ResourceLocation id) {
		return WORLDS.get(id);
	}

	public static GummiWorld get(String id) {
		return id == null || id.isEmpty() ? null : WORLDS.get(ResourceLocation.parse(id));
	}

	public static GummiWorld forDimension(ResourceKey<Level> dimension) {
		for (GummiWorld world : WORLDS.values()) {
			if (world.dimension().equals(dimension)) {
				return world;
			}
		}
		return null;
	}

	public static void replaceAll(Map<ResourceLocation, GummiWorld> worlds) {
		WORLDS = Collections.unmodifiableMap(new LinkedHashMap<>(worlds));
	}
}

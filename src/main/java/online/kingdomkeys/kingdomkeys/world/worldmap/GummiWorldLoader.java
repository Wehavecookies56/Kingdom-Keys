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

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GummiWorldLoader extends SimpleJsonResourceReloadListener {

	public static final Gson GSON = new GsonBuilder().registerTypeAdapter(GummiWorld.class, new GummiWorldDeserializer()).setPrettyPrinting().create();

	public static List<String> names = new LinkedList<>();
	public static List<String> dataList = new LinkedList<>();

	private static final Map<ResourceLocation, GummiWorld> WORLDS = new LinkedHashMap<>();

	public GummiWorldLoader() {
		super(GSON, "gummi_worlds");
	}

	@Override
	protected void apply(Map<ResourceLocation, JsonElement> objectIn, ResourceManager resourceManagerIn, ProfilerFiller profilerIn) {
		KingdomKeys.LOGGER.info("Loading gummi world data");

		names.clear();
		dataList.clear();
		WORLDS.clear();

		objectIn.forEach((resourceLocation, element) -> {
			try {
				WORLDS.put(resourceLocation, GSON.fromJson(element, GummiWorld.class));
				names.add(resourceLocation.toString());
				dataList.add(element.toString());
			} catch (JsonParseException e) {
				KingdomKeys.LOGGER.error("Error parsing gummi world json file {}: {}", resourceLocation, e);
			}
		});

		KingdomKeys.LOGGER.info("Loaded {} gummi worlds", WORLDS.size());

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
		WORLDS.clear();
		WORLDS.putAll(worlds);
	}
}

package online.kingdomkeys.kingdomkeys.savepoint;

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
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncSavePointData;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class SavePointDataLoader extends SimpleJsonResourceReloadListener {
	public static final Gson GSON = new GsonBuilder().registerTypeAdapter(SavePointData.class, new SavePointDataDeserializer()).setPrettyPrinting().create();

	public static List<String> names = new LinkedList<>();
	public static List<String> dataList = new LinkedList<>();

	public SavePointDataLoader() {
		super(GSON, "savepoints");
	}

	@Override
	protected void apply(Map<ResourceLocation, JsonElement> objectIn, ResourceManager resourceManagerIn, ProfilerFiller profilerIn) {
		KingdomKeys.LOGGER.info("Loading savepoint data");

		names.clear();
		dataList.clear();

		AtomicInteger count = new AtomicInteger();

		objectIn.forEach((resourceLocation, element) -> {
			try {
				if (ModSavePoints.registry.containsKey(resourceLocation)) {
					SavePoint point = ModSavePoints.registry.get(resourceLocation);
					dataList.add(element.toString());
					names.add(resourceLocation.toString());

					SavePointData data = GSON.fromJson(element, SavePointData.class);
					data.setName(resourceLocation.toString());
					point.setData(data);
					count.incrementAndGet();
				} else {
					KingdomKeys.LOGGER.warn("Found savepoint data {} for savepoint that doesn't exist", resourceLocation);
				}

			} catch (JsonParseException e) {
				KingdomKeys.LOGGER.error("Error parsing savepoint json file {}: {}", resourceLocation, e);
			}
		});

		KingdomKeys.LOGGER.info("Loaded {} savepoint configs", count.get());
		if (ServerLifecycleHooks.getCurrentServer() != null) {
			for (ServerPlayer player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()) {
				PacketHandler.sendTo(new SCSyncSavePointData(names, dataList), player);
			}
		}
	}
}
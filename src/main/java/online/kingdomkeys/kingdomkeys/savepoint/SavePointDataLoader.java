package online.kingdomkeys.kingdomkeys.savepoint;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class SavePointDataLoader extends SimpleJsonResourceReloadListener {

	public static final Gson GSON = new GsonBuilder().registerTypeAdapter(SavePointData.class, new SavePointDataDeserializer()).setPrettyPrinting().create();

	public SavePointDataLoader() {
		super(GSON, "savepoints");
	}

	@Override
	protected void apply(Map<ResourceLocation, JsonElement> objectIn, ResourceManager resourceManagerIn, ProfilerFiller profilerIn) {
		AtomicInteger count = new AtomicInteger();
		objectIn.forEach((rl, json) -> {
			try {
				if(ModSavePoints.registry.containsKey(rl)) {
					SavePoint point = ModSavePoints.registry.get(rl);
					SavePointData data = GSON.fromJson(json, SavePointData.class);

					point.setData(data);
					count.incrementAndGet();
				} else {
					KingdomKeys.LOGGER.warn("Unknown savepoint {}", rl);
				}

			} catch (Exception e) {
				KingdomKeys.LOGGER.error("Error parsing savepoint {}", rl, e);
			}
		});

		KingdomKeys.LOGGER.info("Loaded {} savepoint configs", count.get());
	}
}
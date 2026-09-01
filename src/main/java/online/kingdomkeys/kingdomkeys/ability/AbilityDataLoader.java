package online.kingdomkeys.kingdomkeys.ability;

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
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncAbilityData;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class AbilityDataLoader extends SimpleJsonResourceReloadListener {

	public static final Gson GSON_BUILDER = new GsonBuilder().registerTypeAdapter(AbilityData.class, new AbilityDataDeserializer()).setPrettyPrinting().create();

	public AbilityDataLoader() {
		super(GSON_BUILDER, "abilities");
	}

	public static List<String> names = new LinkedList<>();
	public static List<String> dataList = new LinkedList<>();

	@Override
	protected void apply(Map<ResourceLocation, JsonElement> objectIn, ResourceManager resourceManagerIn, ProfilerFiller profilerIn) {
		AtomicInteger count = new AtomicInteger();
		names.clear();
		dataList.clear();
		ModAbilities.registry.forEach(ability -> ability.setAbilityData(null));

		objectIn.forEach((resourceLocation, element) -> {
			try {
				if (ModAbilities.registry.containsKey(resourceLocation)) {
					Ability ability = ModAbilities.registry.get(resourceLocation);
					dataList.add(element.toString());
					AbilityData result = GSON_BUILDER.fromJson(element, AbilityData.class);
					names.add(resourceLocation.toString());
					ability.setAbilityData(result);
					count.incrementAndGet();
				} else {
					KingdomKeys.LOGGER.warn("Found ability data {} for an ability that doesn't exist", resourceLocation);
				}
			} catch (JsonParseException e) {
				KingdomKeys.LOGGER.error("Error parsing ability json file {}: {}", resourceLocation, e);
			}
		});

		KingdomKeys.LOGGER.info("Loaded {} abilities data", count.get());

		if (ServerLifecycleHooks.getCurrentServer() != null) {
			for (ServerPlayer player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()) {
				PacketHandler.sendTo(new SCSyncAbilityData(names, dataList), player);
			}
		}
	}
}

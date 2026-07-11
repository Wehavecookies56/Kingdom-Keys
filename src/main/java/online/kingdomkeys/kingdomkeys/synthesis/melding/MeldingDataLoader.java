package online.kingdomkeys.kingdomkeys.synthesis.melding;

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
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncMeldingData;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class MeldingDataLoader extends SimpleJsonResourceReloadListener {
	//GSON builder with custom deserializer for melding data
	public static final Gson GSON_BUILDER = new GsonBuilder().registerTypeAdapter(Melding.class, new MeldingDataDeserializer()).setPrettyPrinting().create();

	public MeldingDataLoader() {
		super(GSON_BUILDER, "melding");
	}

	@Override
	protected void apply(Map<ResourceLocation, JsonElement> objectIn, ResourceManager resourceManagerIn, ProfilerFiller profilerIn) {
		MeldingRegistry.getInstance().clearRegistry();
		AtomicInteger count = new AtomicInteger();
		objectIn.forEach((resourceLocation, element) -> {
			try {
				Melding result = GSON_BUILDER.fromJson(element, Melding.class);
				result.setRegistryName(resourceLocation);
				MeldingRegistry.getInstance().register(result);
				count.incrementAndGet();
			} catch (JsonParseException e) {
				KingdomKeys.LOGGER.error("Error parsing melding json file {}: {}", resourceLocation, e);
			}
		});
		KingdomKeys.LOGGER.info("Loaded {} melding data", count.get());

		if (ServerLifecycleHooks.getCurrentServer() != null) {
			for (ServerPlayer player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()) {
				PacketHandler.sendTo(new SCSyncMeldingData(MeldingRegistry.getInstance().getValues()), player);
			}
		}
	}
}

package online.kingdomkeys.kingdomkeys.lib;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;

public final class StaffCrowns {
	private StaffCrowns() {}

	private static final String REMOTE = "https://raw.githubusercontent.com/Wehavecookies56/Kingdom-Keys/1.21.1/ExtraResources/staff_crowns.json";

	private static final String BUNDLED = "/staff_crowns.json";

	private static final Duration TIMEOUT = Duration.ofSeconds(15);

	/** Empty until something has been read, and replaced */
	private static volatile Map<UUID, List<String>> crowns;

	private static HttpClient client;

	public static List<String> forPlayer(UUID player) {
		return held().getOrDefault(player, List.of());
	}

	public static boolean isStaffCrown(String crown) {
		return held().values().stream().anyMatch(held -> held.contains(crown));
	}

	public static void refresh(UUID player, PlayerData data) {
		List<String> due = forPlayer(player);
		Set<String> kept = new LinkedHashSet<>();

		for (String crown : data.getUnlockedCrowns()) {
			if (CrownTier.byName(crown) != null || due.contains(crown)) {
				kept.add(crown);
			}
		}

		kept.addAll(due);
		data.setUnlockedCrowns(kept);

		// Taking a crown away while it is being worn has to take it off their head as well
		if (!data.getCrown().isEmpty() && !kept.contains(data.getCrown())) {
			data.setCrown("");
		}
	}

	public static void fetch(MinecraftServer server) {
		if (!ModConfigs.SERVER.staffCrownsUpdate.get()) {
			return;
		}

		HttpRequest request = HttpRequest.newBuilder(URI.create(REMOTE))
				.header("Accept", "application/json")
				.header("User-Agent", KingdomKeys.MODID)
				.timeout(TIMEOUT)
				.GET()
				.build();

		http().sendAsync(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8))
				.whenComplete((response, failed) -> {
					if (failed != null) {
						KingdomKeys.LOGGER.warn("Could not fetch the staff crown list, carrying on with the one already read: {}", failed.toString());
						return;
					}

					if (response.statusCode() != 200) {
						KingdomKeys.LOGGER.warn("The staff crown list came back as {}, carrying on with the one already read", response.statusCode());
						return;
					}

					Map<UUID, List<String>> fetched = parse(new StringReader(response.body()), REMOTE);

					// An unreadable answer is worse than no answer, since it would take everybody's crowns
					if (fetched.isEmpty()) {
						return;
					}

					crowns = fetched;
					// Back onto the server thread before touching anybody's data
					server.execute(() -> handOut(server));
				});
	}

	/** In case somebody got in while the request was still in flight, which is most of a second at worst */
	private static void handOut(MinecraftServer server) {
		for (ServerPlayer player : server.getPlayerList().getPlayers()) {
			PlayerData data = PlayerData.get(player);

			if (data != null) {
				refresh(player.getUUID(), data);
				PacketHandler.sendTo(new SCSyncPlayerData(player), player);
			}
		}
	}

	/** Whatever has been read so far, falling back to the jar the first time anybody asks */
	private static Map<UUID, List<String>> held() {
		if (crowns == null) {
			synchronized (StaffCrowns.class) {
				if (crowns == null) {
					crowns = bundled();
				}
			}
		}

		return crowns;
	}

	private static Map<UUID, List<String>> bundled() {
		try (InputStream in = StaffCrowns.class.getResourceAsStream(BUNDLED)) {
			if (in == null) {
				KingdomKeys.LOGGER.warn("No staff crown list at {} inside the mod, nobody will be given one until GitHub answers", BUNDLED);
				return Map.of();
			}

			return parse(new InputStreamReader(in, StandardCharsets.UTF_8), BUNDLED);
		} catch (Exception failed) {
			KingdomKeys.LOGGER.error("Could not read the staff crown list at " + BUNDLED, failed);
			return Map.of();
		}
	}

	private static HttpClient http() {
		if (client == null) {
			client = HttpClient.newBuilder().connectTimeout(TIMEOUT).followRedirects(HttpClient.Redirect.NORMAL).build();
		}

		return client;
	}

	private static Map<UUID, List<String>> parse(Reader source, String where) {
		Map<UUID, List<String>> found = new HashMap<>();

		try {
			JsonObject root = JsonParser.parseReader(source).getAsJsonObject();

			for (Map.Entry<String, JsonElement> person : root.entrySet()) {
				if (person.getKey().startsWith("_comment") || !person.getValue().isJsonObject()) {
					continue;
				}

				readPerson(person.getKey(), person.getValue().getAsJsonObject(), found);
			}
		} catch (Exception failed) {
			// A typo in the file is not worth taking the game down over, so it just means no staff crowns
			KingdomKeys.LOGGER.error("Could not read the staff crown list from " + where, failed);
			return Map.of();
		}

		found.replaceAll((player, held) -> List.copyOf(held));
		return found;
	}

	private static void readPerson(String who, JsonObject entry, Map<UUID, List<String>> found) {
		List<String> held = names(entry.get("crowns"));

		if (held.isEmpty()) {
			return;
		}

		for (String id : names(entry.get("uuids"))) {
			UUID account = parseId(id);

			if (account == null) {
				KingdomKeys.LOGGER.warn("Skipping \"{}\" under {} in the staff crown list, it is not a UUID", id, who);
				continue;
			}

			// Two people sharing an account is a mistake, but adding rather than replacing means the
			// account keeps both crowns instead of silently losing one, and the file can be fixed later
			Set<String> all = new LinkedHashSet<>(found.getOrDefault(account, List.of()));
			all.addAll(held);
			found.put(account, new ArrayList<>(all));
		}
	}

	private static List<String> names(JsonElement element) {
		List<String> read = new ArrayList<>();

		if (element == null || element.isJsonNull()) {
			return read;
		}

		if (element.isJsonArray()) {
			for (JsonElement each : element.getAsJsonArray()) {
				add(read, each);
			}

			return read;
		}

		add(read, element);
		return read;
	}

	private static void add(List<String> read, JsonElement element) {
		if (element.isJsonPrimitive() && !element.getAsString().isBlank()) {
			read.add(element.getAsString().trim());
		}
	}

	/** Accepts a UUID with or without its dashes, which is how most of the tools that report one print it */
	private static UUID parseId(String id) {
		String text = id.trim();

		if (text.length() == 32) {
			text = text.replaceAll("(.{8})(.{4})(.{4})(.{4})(.{12})", "$1-$2-$3-$4-$5");
		}

		try {
			return UUID.fromString(text);
		} catch (IllegalArgumentException notAUuid) {
			return null;
		}
	}
}

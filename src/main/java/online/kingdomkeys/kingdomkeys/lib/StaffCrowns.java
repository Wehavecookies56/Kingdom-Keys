package online.kingdomkeys.kingdomkeys.lib;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.data.PlayerData;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public final class StaffCrowns {
	private StaffCrowns() {}

	private static final String PATH = "/kingdomkeys/staff_crowns.json";

	private static Map<UUID, List<String>> crowns;

	public static List<String> forPlayer(UUID player) {
		return read().getOrDefault(player, List.of());
	}

	public static boolean isStaffCrown(String crown) {
		return read().values().stream().anyMatch(held -> held.contains(crown));
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

	private static Map<UUID, List<String>> read() {
		if (crowns == null) {
			crowns = parse();
		}

		return crowns;
	}

	private static Map<UUID, List<String>> parse() {
		Map<UUID, List<String>> found = new HashMap<>();

		try (InputStream in = StaffCrowns.class.getResourceAsStream(PATH)) {
			if (in == null) {
				KingdomKeys.LOGGER.warn("No staff crown list at {}, nobody will be given one", PATH);
				return found;
			}

			JsonObject root = JsonParser.parseReader(new InputStreamReader(in, StandardCharsets.UTF_8)).getAsJsonObject();

			for (Map.Entry<String, JsonElement> person : root.entrySet()) {
				if (person.getKey().startsWith("_") || !person.getValue().isJsonObject()) {
					continue;
				}

				readPerson(person.getKey(), person.getValue().getAsJsonObject(), found);
			}
		} catch (Exception failed) {
			// A typo in the file is not worth taking the game down over, so it just means no staff crowns
			KingdomKeys.LOGGER.error("Could not read the staff crown list at " + PATH, failed);
		}

		found.replaceAll((player, held) -> List.copyOf(held));
		return found;
	}

	private static void readPerson(String who, JsonObject entry, Map<UUID, List<String>> found) {
		List<String> held = names(entry.get("crowns"));

		if (held.isEmpty()) {
			KingdomKeys.LOGGER.warn("Skipping {} in the staff crown list, no crowns were listed for them", who);
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

package online.kingdomkeys.kingdomkeys.datagen.provider;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.block.gummi.GummiStats;
import online.kingdomkeys.kingdomkeys.datagen.builder.GummiStatsBuilder;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class GummiStatsProvider implements DataProvider {

	private final PackOutput.PathProvider pathProvider;

	public GummiStatsProvider(PackOutput output) {
		this.pathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, "gummi_stats");
	}

	public static Map<String, GummiStats> stats() {
		Map<String, GummiStats> stats = new LinkedHashMap<>();

		String[] shapes = {"cube", "wedge", "pyramid", "inner_corner", "cylinder", "pie", "round_corner", "cone", "dome"};
		String[] tiers = {"gummi_", "shell_gummi_", "dispel_gummi_"};
		int[][] armour = {{5, 3, 3, 4, 4, 4, 3, 3, 3}, {10, 5, 5, 7, 7, 7, 5, 5, 5}, {15, 7, 7, 12, 12, 12, 7, 7, 7}};

		for (int tier = 0; tier < tiers.length; tier++) {
			for (int shape = 0; shape < shapes.length; shape++) {
				stats.put(tiers[tier] + shapes[shape], new GummiStatsBuilder().weight(1).armour(armour[tier][shape]).cost(tier + 1).build());
			}
		}

		stats.put("gummi_fire", weapon(40).firepower(2).fuelPerShot(35).build());
		stats.put("gummi_fira", weapon(50).firepower(3).fuelPerShot(41).build());
		stats.put("gummi_blizzard", weapon(70).firepower(2).fuelPerShot(71).build());
		stats.put("gummi_blizzara", weapon(110).firepower(3).fuelPerShot(108).build());
		stats.put("gummi_gravity", weapon(70).firepower(10).fuelPerShot(145).build());
		stats.put("gummi_gravira", weapon(110).firepower(15).fuelPerShot(155).build());
		stats.put("gummi_water", weapon(50).firepower(2).build());
		stats.put("gummi_watera", weapon(70).firepower(3).build());

		stats.put("gummi_vernier", engine(24).topSpeed(80).lowSpeed(60).horsepower(10).build());
		stats.put("gummi_thruster", engine(32).topSpeed(90).lowSpeed(70).horsepower(20).build());
		stats.put("gummi_booster", engine(32).topSpeed(100).lowSpeed(80).horsepower(30).build());
		stats.put("gummi_flare", engine(40).topSpeed(110).lowSpeed(90).horsepower(30).build());
		stats.put("gummi_holy", engine(45).topSpeed(120).lowSpeed(100).horsepower(80).build());

		stats.put("gummi_aero_square", new GummiStatsBuilder().weight(1).armour(1).cost(2).mobility(10).build());
		stats.put("gummi_aero_triangle", new GummiStatsBuilder().weight(1).armour(1).cost(2).mobility(5).build());

		stats.put("gummi_bubble_helm", new GummiStatsBuilder().weight(2).armour(40).build());
		stats.put("gummi_mini_helm", new GummiStatsBuilder().weight(2).armour(20).build());

		return stats;
	}

	private static GummiStatsBuilder weapon(int cost) {
		return new GummiStatsBuilder().weight(1).armour(1).cost(cost);
	}

	private static GummiStatsBuilder engine(int cost) {
		return new GummiStatsBuilder().weight(1).armour(1).cost(cost);
	}

	@Override
	public CompletableFuture<?> run(CachedOutput cache) {
		CompletableFuture<?>[] futures = stats().entrySet().stream().map(entry -> {
			JsonElement json = GummiStats.CODEC.encodeStart(JsonOps.INSTANCE, entry.getValue()).getOrThrow();
			return DataProvider.saveStable(cache, json, pathProvider.json(KingdomKeys.rl(entry.getKey())));
		}).toArray(CompletableFuture[]::new);

		return CompletableFuture.allOf(futures);
	}

	@Override
	public String getName() {
		return "Kingdom Keys Gummi Stats";
	}
}

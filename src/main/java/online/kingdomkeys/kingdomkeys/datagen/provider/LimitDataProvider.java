package online.kingdomkeys.kingdomkeys.datagen.provider;

import com.google.gson.JsonObject;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.datagen.builder.LimitBuilder;
import online.kingdomkeys.kingdomkeys.lib.Strings;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class LimitDataProvider implements DataProvider {

	private final PackOutput.PathProvider pathProvider;

	public LimitDataProvider(PackOutput output) {
		this.pathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, "limits");
	}

	@Override
	public CompletableFuture<?> run(CachedOutput cache) {
		Map<String, JsonObject> limits = new LinkedHashMap<>();

		// Xemnas
		limits.put(Strings.LaserCircle, new LimitBuilder().cost(100).cooldown(200).damageMultiplier(1.0F).build());
		limits.put(Strings.LaserDome, new LimitBuilder().cost(400).cooldown(600).damageMultiplier(1.0F).build());

		// Xigbar
		limits.put(Strings.ArrowRain, new LimitBuilder().cost(300).cooldown(600).damageMultiplier(1.0F).build());

		// Larxene
		limits.put(Strings.SlowThunderTrail, new LimitBuilder().cost(100).cooldown(100).damageMultiplier(1.0F).build());
		limits.put(Strings.FastThunderTrail, new LimitBuilder().cost(200).cooldown(50).damageMultiplier(1.0F).build());

		// Axel
		limits.put(Strings.FlameRing, new LimitBuilder().cost(100).cooldown(100).damageMultiplier(1.0F).build());
		limits.put(Strings.FlameWall, new LimitBuilder().cost(400).cooldown(300).damageMultiplier(1.0F).build());

		// Xaldin
		limits.put(Strings.LanceStorm, new LimitBuilder().cost(200).cooldown(300).damageMultiplier(1.0F).build());
		limits.put(Strings.FallingSpear, new LimitBuilder().cost(200).cooldown(300).damageMultiplier(1.0F).build());

		// Vexen
		limits.put(Strings.IcePillars, new LimitBuilder().cost(300).cooldown(300).damageMultiplier(1.0F).build());

		// Lexaeus
		limits.put(Strings.Powerup, new LimitBuilder().cost(200).cooldown(250).damageMultiplier(1.0F).build());
		limits.put(Strings.RockyPillars, new LimitBuilder().cost(300).cooldown(300).damageMultiplier(1.0F).build());

		// Demyx
		limits.put(Strings.WaterWall, new LimitBuilder().cost(400).cooldown(300).damageMultiplier(1.0F).build());
		limits.put(Strings.WaterTrail, new LimitBuilder().cost(100).cooldown(100).damageMultiplier(1.0F).build());

		// Luxord
		limits.put(Strings.CardRing, new LimitBuilder().cost(400).cooldown(300).damageMultiplier(1.0F).build());

		// Marluxia
		limits.put(Strings.ScytheDash, new LimitBuilder().cost(200).cooldown(200).damageMultiplier(1.0F).build());
		limits.put(Strings.PetalVoid, new LimitBuilder().cost(400).cooldown(300).damageMultiplier(1.0F).build());

		// Saix
		limits.put(Strings.BerserkClaymore, new LimitBuilder().cost(150).cooldown(250).damageMultiplier(1.0F).build());

		// Roxas
		limits.put(Strings.LightBarrage, new LimitBuilder().cost(300).cooldown(400).damageMultiplier(1.0F).build());

		CompletableFuture<?>[] futures = limits.entrySet().stream().map(entry -> {
			Path path = pathProvider.json(KingdomKeys.rl(entry.getKey()));
			return DataProvider.saveStable(cache, entry.getValue(), path);
		}).toArray(CompletableFuture[]::new);
		return CompletableFuture.allOf(futures);
	}

	@Override
	public String getName() {
		return "Kingdom Keys Limit Data";
	}
}

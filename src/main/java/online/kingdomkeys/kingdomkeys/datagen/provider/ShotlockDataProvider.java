package online.kingdomkeys.kingdomkeys.datagen.provider;

import com.google.gson.JsonObject;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.datagen.builder.ShotlockBuilder;
import online.kingdomkeys.kingdomkeys.lib.Strings;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ShotlockDataProvider implements DataProvider {

	private final PackOutput.PathProvider pathProvider;

	public ShotlockDataProvider(PackOutput output) {
		this.pathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, "shotlocks");
	}

	@Override
	public CompletableFuture<?> run(CachedOutput cache) {
		Map<String, JsonObject> shotlocks = new LinkedHashMap<>();

		shotlocks.put(Strings.DarkVolley, new ShotlockBuilder().cooldown(4).cooldownMax(2).maxLocks(18).damageMultiplier(0.9F).damageMultiplierMax(1.08F).maxExp(8600).maxLevel(5).build());
		shotlocks.put(Strings.ChaosSnake, new ShotlockBuilder().cooldown(3).cooldownMax(2).maxLocks(20).damageMultiplier(0.8F).damageMultiplierMax(0.96F).maxExp(6400).maxLevel(4).build());
		shotlocks.put(Strings.BubbleBlaster, new ShotlockBuilder().cooldown(4).cooldownMax(2).maxLocks(15).damageMultiplier(1.1F).damageMultiplierMax(1.32F).maxExp(8600).maxLevel(5).build());
		shotlocks.put(Strings.PulseBomb, new ShotlockBuilder().cooldown(3).cooldownMax(2).maxLocks(20).damageMultiplier(0.8F).damageMultiplierMax(0.96F).maxExp(8600).maxLevel(5).build());
		shotlocks.put(Strings.MeteorShower, new ShotlockBuilder().cooldown(3).cooldownMax(1).maxLocks(30).damageMultiplier(0.5F).damageMultiplierMax(0.6F).maxExp(6400).maxLevel(4).build());
		shotlocks.put(Strings.FlameSalvo, new ShotlockBuilder().cooldown(4).cooldownMax(2).maxLocks(15).damageMultiplier(1.1F).damageMultiplierMax(1.32F).maxExp(6400).maxLevel(4).element("fire").build());

		shotlocks.put(Strings.Ragnarok, new ShotlockBuilder().cooldown(4).cooldownMax(2).maxLocks(16).damageMultiplier(1.0F).damageMultiplierMax(1.2F).maxExp(6400).maxLevel(4).build());
		shotlocks.put(Strings.Thunderstorm, new ShotlockBuilder().cooldown(13).cooldownMax(8).maxLocks(5).damageMultiplier(3.2F).damageMultiplierMax(3.84F).maxExp(6400).maxLevel(4).element("lightning").build());
		shotlocks.put(Strings.PrismRain, new ShotlockBuilder().cooldown(4).cooldownMax(2).maxLocks(16).damageMultiplier(1.0F).damageMultiplierMax(1.2F).maxExp(8600).maxLevel(5).element("light").build());
		shotlocks.put(Strings.BioBarrage, new ShotlockBuilder().cooldown(8).cooldownMax(5).maxLocks(8).damageMultiplier(2.0F).damageMultiplierMax(2.4F).maxExp(6400).maxLevel(4).build());

		shotlocks.put(Strings.SonicShadow, new ShotlockBuilder().cooldown(6).cooldownMax(4).maxLocks(10).damageMultiplier(1.6F).damageMultiplierMax(1.92F).maxExp(8600).maxLevel(5).element("darkness").build());
		shotlocks.put(Strings.AbsoluteZero, new ShotlockBuilder().cooldown(5).cooldownMax(3).maxLocks(12).damageMultiplier(1.3F).damageMultiplierMax(1.56F).maxExp(6400).maxLevel(4).element("ice").build());
		shotlocks.put(Strings.PhotonCharge, new ShotlockBuilder().cooldown(4).cooldownMax(2).maxLocks(15).damageMultiplier(1.1F).damageMultiplierMax(1.32F).maxExp(6400).maxLevel(4).element("light").build());
		shotlocks.put(Strings.LightningRay, new ShotlockBuilder().cooldown(4).cooldownMax(2).maxLocks(18).damageMultiplier(0.9F).damageMultiplierMax(1.08F).maxExp(6400).maxLevel(4).element("lightning").build());

		shotlocks.put(Strings.Multivortex, new ShotlockBuilder().cooldown(3).cooldownMax(2).maxLocks(30).damageMultiplier(0.5F).damageMultiplierMax(0.6F).maxExp(12000).maxLevel(6).element("air").build());
		shotlocks.put(Strings.Lightbloom, new ShotlockBuilder().cooldown(3).cooldownMax(2).maxLocks(20).damageMultiplier(0.8F).damageMultiplierMax(0.96F).maxExp(12000).maxLevel(6).element("light").build());
		shotlocks.put(Strings.UltimaCannon, new ShotlockBuilder().cooldown(40).cooldownMax(30).maxLocks(1).damageMultiplier(1.0F).damageMultiplierMax(1.2F).maxExp(12000).maxLevel(6).element("fire").build());

		CompletableFuture<?>[] futures = shotlocks.entrySet().stream().map(entry -> {
			Path path = pathProvider.json(KingdomKeys.rl(entry.getKey()));
			return DataProvider.saveStable(cache, entry.getValue(), path);
		}).toArray(CompletableFuture[]::new);
		return CompletableFuture.allOf(futures);
	}

	@Override
	public String getName() {
		return "Kingdom Keys Shotlock Data";
	}
}

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

		// Volley family (Terra/Aqua) - BBS Max Locks: Dark Volley 18, Prism Rain 16
		shotlocks.put(Strings.DarkVolley, new ShotlockBuilder().cooldown(4).max(18).damageMultiplier(0.9F).build());
		shotlocks.put(Strings.PrismRain, new ShotlockBuilder().cooldown(4).max(16).damageMultiplier(1.0F).build());
		// Volley family reskins - BBS Max Locks: Chaos Snake 20, Bubble Blaster 15, Bio Barrage 8, Pulse Bomb 20
		shotlocks.put(Strings.ChaosSnake, new ShotlockBuilder().cooldown(3).max(20).damageMultiplier(0.8F).build());
		shotlocks.put(Strings.BubbleBlaster, new ShotlockBuilder().cooldown(4).max(15).damageMultiplier(1.1F).build());
		shotlocks.put(Strings.BioBarrage, new ShotlockBuilder().cooldown(8).max(8).damageMultiplier(2.0F).build());
		shotlocks.put(Strings.PulseBomb, new ShotlockBuilder().cooldown(3).max(20).damageMultiplier(0.8F).build());

		// Ragnarok/salvo family - BBS Max Locks: Ragnarok 16, Flame Salvo 15, Thunderstorm 5
		shotlocks.put(Strings.Ragnarok, new ShotlockBuilder().cooldown(4).max(16).damageMultiplier(1.0F).build());
		shotlocks.put(Strings.FlameSalvo, new ShotlockBuilder().cooldown(4).max(15).damageMultiplier(1.1F).element("fire").build());
		shotlocks.put(Strings.Thunderstorm, new ShotlockBuilder().cooldown(13).max(5).damageMultiplier(3.2F).element("lightning").build());

		// Sonic Blade/rush family - BBS Max Locks: Sonic Shadow 10, Absolute Zero 12, Photon Charge 15, Lightning Ray 18
		shotlocks.put(Strings.SonicShadow, new ShotlockBuilder().cooldown(6).max(10).damageMultiplier(1.6F).element("darkness").build());
		shotlocks.put(Strings.AbsoluteZero, new ShotlockBuilder().cooldown(5).max(12).damageMultiplier(1.3F).element("ice").build());
		shotlocks.put(Strings.PhotonCharge, new ShotlockBuilder().cooldown(4).max(15).damageMultiplier(1.1F).element("light").build());
		shotlocks.put(Strings.LightningRay, new ShotlockBuilder().cooldown(4).max(18).damageMultiplier(0.9F).element("lightning").build());

		// Meteor Shower (its own Volley-family thing, no direct sibling) - BBS Max Locks: 30, the highest of any Shotlock
		shotlocks.put(Strings.MeteorShower, new ShotlockBuilder().cooldown(2).max(30).damageMultiplier(0.5F).build());

		// Not a family shotlock - untouched behavior-wise, kept at its original values.
		shotlocks.put(Strings.UltimaCannon, new ShotlockBuilder().cooldown(40).max(1).damageMultiplier(1.0F).build());

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

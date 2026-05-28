package online.kingdomkeys.kingdomkeys.datagen.provider;

import com.google.gson.JsonObject;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.datagen.builder.MagicBuilder;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class MagicDataProvider implements DataProvider {

	private final PackOutput.PathProvider pathProvider;

	public MagicDataProvider(PackOutput output) {
		this.pathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, "magics");
	}

	@Override
	public CompletableFuture<?> run(CachedOutput cache) {
		Map<String, JsonObject> magics = new LinkedHashMap<>();

		magics.put("magic_fire", new MagicBuilder()
				.level(0).cost(8).castTime(10).cooldown(5).damageMultiplier(0.2F).lockOn(true).maxExp(1800).maxExpLevel(3).end()
				.level(1).cost(8).castTime(10).cooldown(5).damageMultiplier(0.3F).lockOn(true).maxExp(2400).maxExpLevel(3).end()
				.level(2).cost(8).castTime(10).cooldown(5).damageMultiplier(0.4F).lockOn(true).maxExp(4200).maxExpLevel(4).end()
				.level(3).cost(8).castTime(15).cooldown(10).damageMultiplier(1.0F).lockOn(true).maxExp(800).maxExpLevel(3).end()
				.build());

		magics.put("magic_blizzard", new MagicBuilder()
				.level(0).cost(10).castTime(10).cooldown(20).damageMultiplier(0.3F).lockOn(false).maxExp(1800).maxExpLevel(3).end()
				.level(1).cost(10).castTime(10).cooldown(20).damageMultiplier(0.25F).lockOn(false).maxExp(2400).maxExpLevel(3).end()
				.level(2).cost(10).castTime(10).cooldown(20).damageMultiplier(0.2F).lockOn(false).maxExp(4200).maxExpLevel(4).end()
				.level(3).cost(10).castTime(15).cooldown(20).damageMultiplier(0.8F).lockOn(false).maxExp(200).maxExpLevel(3).end()
				.build());

		magics.put("magic_water", new MagicBuilder()
				.level(0).cost(12).castTime(0).cooldown(55).damageMultiplier(0.15F).lockOn(false).maxExp(1800).maxExpLevel(3).end()
				.level(1).cost(12).castTime(0).cooldown(55).damageMultiplier(0.25F).lockOn(false).maxExp(2400).maxExpLevel(3).end()
				.level(2).cost(12).castTime(0).cooldown(55).damageMultiplier(0.35F).lockOn(false).maxExp(4200).maxExpLevel(4).end()
				.level(3).cost(12).castTime(0).cooldown(55).damageMultiplier(0.8F).lockOn(false).maxExp(200).maxExpLevel(3).end()
				.build());

		magics.put("magic_thunder", new MagicBuilder()
				.level(0).cost(14).castTime(10).cooldown(30).damageMultiplier(0.1F).lockOn(true).maxExp(1800).maxExpLevel(3).end()
				.level(1).cost(14).castTime(10).cooldown(35).damageMultiplier(0.11F).lockOn(true).maxExp(2400).maxExpLevel(3).end()
				.level(2).cost(14).castTime(10).cooldown(40).damageMultiplier(0.12F).lockOn(true).maxExp(4200).maxExpLevel(4).end()
				.level(3).cost(14).castTime(10).cooldown(50).damageMultiplier(0.14F).lockOn(true).maxExp(200).maxExpLevel(3).end()
				.build());

		magics.put("magic_cure", new MagicBuilder()
				.level(0).cost(300).castTime(1).cooldown(20).damageMultiplier(0.25F).lockOn(false).maxExp(1800).maxExpLevel(3).end()
				.level(1).cost(300).castTime(1).cooldown(20).damageMultiplier(0.5F).lockOn(false).maxExp(2400).maxExpLevel(3).end()
				.level(2).cost(300).castTime(1).cooldown(20).damageMultiplier(0.75F).lockOn(false).maxExp(4200).maxExpLevel(4).end()
				.level(3).cost(300).castTime(1).cooldown(20).damageMultiplier(1.0F).lockOn(false).maxExp(200).maxExpLevel(3).end()
				.build());

		magics.put("magic_aero", new MagicBuilder()
				.level(0).cost(12).castTime(10).cooldown(20).damageMultiplier(1.0F).lockOn(false).maxExp(1800).maxExpLevel(3).end()
				.level(1).cost(12).castTime(10).cooldown(20).damageMultiplier(1.5F).lockOn(false).maxExp(2400).maxExpLevel(3).end()
				.level(2).cost(12).castTime(10).cooldown(20).damageMultiplier(2.0F).lockOn(false).maxExp(4200).maxExpLevel(4).end()
				.level(3).cost(12).castTime(10).cooldown(20).damageMultiplier(1.0F).lockOn(false).maxExp(200).maxExpLevel(3).end()
				.build());

		magics.put("magic_gravity", new MagicBuilder()
				.level(0).cost(14).castTime(10).cooldown(30).damageMultiplier(0.25F).lockOn(true).maxExp(1800).maxExpLevel(3).end()
				.level(1).cost(14).castTime(10).cooldown(30).damageMultiplier(0.5F).lockOn(true).maxExp(2400).maxExpLevel(3).end()
				.level(2).cost(14).castTime(10).cooldown(30).damageMultiplier(0.75F).lockOn(true).maxExp(4200).maxExpLevel(4).end()
				.level(3).cost(14).castTime(15).cooldown(40).damageMultiplier(1.0F).lockOn(true).maxExp(200).maxExpLevel(3).end()
				.build());

		magics.put("magic_magnet", new MagicBuilder()
				.level(0).cost(15).castTime(10).cooldown(3).damageMultiplier(0.0F).lockOn(true).maxExp(1800).maxExpLevel(3).end()
				.level(1).cost(15).castTime(10).cooldown(3).damageMultiplier(1.0F).lockOn(true).maxExp(2400).maxExpLevel(3).end()
				.level(2).cost(15).castTime(10).cooldown(3).damageMultiplier(2.0F).lockOn(true).maxExp(4200).maxExpLevel(4).end()
				.level(3).cost(15).castTime(10).cooldown(3).damageMultiplier(3.0F).lockOn(true).maxExp(200).maxExpLevel(3).end()
				.build());

		magics.put("magic_reflect", new MagicBuilder()
				.level(0).cost(10).castTime(1).cooldown(69).damageMultiplier(0.3F).lockOn(false).maxExp(1800).maxExpLevel(3).end()
				.level(1).cost(10).castTime(1).cooldown(69).damageMultiplier(0.5F).lockOn(false).maxExp(2400).maxExpLevel(3).end()
				.level(2).cost(10).castTime(1).cooldown(69).damageMultiplier(0.7F).lockOn(false).maxExp(4200).maxExpLevel(4).end()
				.level(3).cost(10).castTime(1).cooldown(69).damageMultiplier(1.0F).lockOn(false).maxExp(200).maxExpLevel(3).end()
				.build());

		magics.put("magic_stop", new MagicBuilder()
				.level(0).cost(20).castTime(10).cooldown(20).damageMultiplier(0.5F).lockOn(false).maxExp(1800).maxExpLevel(3).end()
				.level(1).cost(20).castTime(10).cooldown(20).damageMultiplier(1.0F).lockOn(false).maxExp(2400).maxExpLevel(3).end()
				.level(2).cost(20).castTime(10).cooldown(20).damageMultiplier(1.5F).lockOn(false).maxExp(4200).maxExpLevel(4).end()
				.level(3).cost(20).castTime(0).cooldown(20).damageMultiplier(1.0F).lockOn(false).maxExp(200).maxExpLevel(3).end()
				.build());

		magics.put("magic_darkfire", new MagicBuilder()
				.level(0).cost(8).castTime(10).cooldown(5).damageMultiplier(0.5F).lockOn(true).maxExp(6600).maxExpLevel(5).end()
				.build());

		CompletableFuture<?>[] futures = magics.entrySet().stream().map(entry -> {
			Path path = pathProvider.json(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, entry.getKey()));
			return DataProvider.saveStable(cache, entry.getValue(), path);
		}).toArray(CompletableFuture[]::new);
		return CompletableFuture.allOf(futures);
	}

	@Override
	public String getName() {
		return "Kingdom Keys Magic Data";
	}
}
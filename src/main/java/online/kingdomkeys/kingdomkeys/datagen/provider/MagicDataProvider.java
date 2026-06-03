package online.kingdomkeys.kingdomkeys.datagen.provider;

import com.google.gson.JsonObject;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.datagen.builder.MagicBuilder;
import online.kingdomkeys.kingdomkeys.lib.Strings;

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
				.level(0).cost(8).castTime(10).cooldown(5).damageMultiplier(0.2F,0.25F).lockOn(true).maxExp(1800).maxExpLevel(3).end()
				.level(1).cost(8).castTime(10).cooldown(5).damageMultiplier(0.3F, 0.35F).lockOn(true).maxExp(2400).maxExpLevel(3).end()
				.level(2).cost(8).castTime(10).cooldown(5).damageMultiplier(0.4F, 0.45F).lockOn(true).maxExp(4200).maxExpLevel(4).end()
				.level(3).cost(8).castTime(15).cooldown(10).damageMultiplier(1.0F).lockOn(true).maxExp(800).maxExpLevel(3).end()
				.build());

		magics.put("magic_blizzard", new MagicBuilder()
				.level(0).cost(10).castTime(10).cooldown(20).damageMultiplier(0.3F, 0.32F).lockOn(false).maxExp(1800).maxExpLevel(3).end()
				.level(1).cost(10).castTime(10).cooldown(20).damageMultiplier(0.25F, 0.27F).lockOn(false).maxExp(2400).maxExpLevel(3).end()
				.level(2).cost(10).castTime(10).cooldown(20).damageMultiplier(0.2F, 0.22F).lockOn(false).maxExp(4200).maxExpLevel(4).end()
				.level(3).cost(10).castTime(15).cooldown(20).damageMultiplier(0.8F, 0.9F).lockOn(false).maxExp(200).maxExpLevel(3).end()
				.build());

		magics.put("magic_water", new MagicBuilder()
				.level(0).cost(12).castTime(0).cooldown(55).damageMultiplier(0.15F, 1.19F).lockOn(false).maxExp(1800).maxExpLevel(3).end()
				.level(1).cost(12).castTime(0).cooldown(55).damageMultiplier(0.25F, 0.3F).lockOn(false).maxExp(2400).maxExpLevel(3).end()
				.level(2).cost(12).castTime(0).cooldown(55).damageMultiplier(0.35F, 0.37F).lockOn(false).maxExp(4200).maxExpLevel(4).end()
				.level(3).cost(12).castTime(0).cooldown(55).damageMultiplier(0.8F,0.85F).lockOn(false).maxExp(200).maxExpLevel(3).end()
				.build());

		magics.put("magic_thunder", new MagicBuilder()
				.level(0).cost(14).castTime(10).cooldown(30).damageMultiplier(0.1F, 0.104F).lockOn(true).maxExp(1800).maxExpLevel(3).end()
				.level(1).cost(14).castTime(10).cooldown(35).damageMultiplier(0.11F, 0.114F).lockOn(true).maxExp(2400).maxExpLevel(3).end()
				.level(2).cost(14).castTime(10).cooldown(40).damageMultiplier(0.12F, 0.124F).lockOn(true).maxExp(4200).maxExpLevel(4).end()
				.level(3).cost(14).castTime(10).cooldown(50).damageMultiplier(0.14F, 0.144F).lockOn(true).maxExp(200).maxExpLevel(3).end()
				.build());

		magics.put("magic_cure", new MagicBuilder()
				.level(0).cost(300).castTime(1).cooldown(20).damageMultiplier(0.25F,0.33F).lockOn(false).maxExp(1800).maxExpLevel(3).end()
				.level(1).cost(300).castTime(1).cooldown(20).damageMultiplier(0.5F,0.58F).lockOn(false).maxExp(2400).maxExpLevel(3).end()
				.level(2).cost(300).castTime(1).cooldown(20).damageMultiplier(0.75F, 0.8F).lockOn(false).maxExp(4200).maxExpLevel(4).end()
				.level(3).cost(300).castTime(1).cooldown(20).damageMultiplier(1.0F,1.1F).lockOn(false).maxExp(200).maxExpLevel(3).end()
				.build());

		magics.put("magic_aero", new MagicBuilder()
				.level(0).cost(12).castTime(10).cooldown(20).damageMultiplier(1.0F,1.3F).lockOn(false).maxExp(1800).maxExpLevel(3).end()
				.level(1).cost(12).castTime(10).cooldown(20).damageMultiplier(1.5F,1.7F).lockOn(false).maxExp(2400).maxExpLevel(3).end()
				.level(2).cost(12).castTime(10).cooldown(20).damageMultiplier(2.0F, 2.2F).lockOn(false).maxExp(4200).maxExpLevel(4).end()
				.level(3).cost(12).castTime(10).cooldown(20).damageMultiplier(1.0F).lockOn(false).maxExp(200).maxExpLevel(3).end()
				.build());

		magics.put("magic_gravity", new MagicBuilder()
				.level(0).cost(14).castTime(10).cooldown(30).damageMultiplier(0.25F,0.33F).lockOn(true).maxExp(1800).maxExpLevel(3).end()
				.level(1).cost(14).castTime(10).cooldown(30).damageMultiplier(0.5F,0.6F).lockOn(true).maxExp(2400).maxExpLevel(3).end()
				.level(2).cost(14).castTime(10).cooldown(30).damageMultiplier(0.75F,0.8F).lockOn(true).maxExp(4200).maxExpLevel(4).end()
				.level(3).cost(14).castTime(15).cooldown(40).damageMultiplier(1.0F,1.1F).lockOn(true).maxExp(200).maxExpLevel(3).end()
				.build());

		magics.put("magic_magnet", new MagicBuilder()
				.level(0).cost(15).castTime(10).cooldown(3).damageMultiplier(0.0F, 0.4F).lockOn(true).maxExp(1800).maxExpLevel(3).end()
				.level(1).cost(15).castTime(10).cooldown(3).damageMultiplier(1.0F, 1.4F).lockOn(true).maxExp(2400).maxExpLevel(3).end()
				.level(2).cost(15).castTime(10).cooldown(3).damageMultiplier(2.0F, 2.4F).lockOn(true).maxExp(4200).maxExpLevel(4).end()
				.level(3).cost(15).castTime(10).cooldown(3).damageMultiplier(3.0F,3.4F).lockOn(true).maxExp(200).maxExpLevel(3).end()
				.build());

		magics.put("magic_reflect", new MagicBuilder()
				.level(0).cost(10).castTime(1).cooldown(69).damageMultiplier(0.3F,0.34F).lockOn(false).maxExp(1800).maxExpLevel(3).end()
				.level(1).cost(10).castTime(1).cooldown(69).damageMultiplier(0.5F, 0.58F).lockOn(false).maxExp(2400).maxExpLevel(3).end()
				.level(2).cost(10).castTime(1).cooldown(69).damageMultiplier(0.7F, 0.8F).lockOn(false).maxExp(4200).maxExpLevel(4).end()
				.level(3).cost(10).castTime(1).cooldown(69).damageMultiplier(1.0F, 1.1F).lockOn(false).maxExp(200).maxExpLevel(3).end()
				.build());

		magics.put("magic_stop", new MagicBuilder()
				.level(0).cost(20).castTime(10).cooldown(20).damageMultiplier(0.5F,0.6F).lockOn(false).maxExp(1800).maxExpLevel(3).end()
				.level(1).cost(20).castTime(10).cooldown(20).damageMultiplier(1.0F,1.2F).lockOn(false).maxExp(2400).maxExpLevel(3).end()
				.level(2).cost(20).castTime(10).cooldown(20).damageMultiplier(1.5F,1.8F).lockOn(false).maxExp(4200).maxExpLevel(4).end()
				.level(3).cost(20).castTime(0).cooldown(20).damageMultiplier(1.0F).lockOn(false).maxExp(200).maxExpLevel(3).end()
				.build());


		magics.put(ResourceLocation.parse(Strings.Magic_DarkFiraga).getPath(), new MagicBuilder()
				.level(0).cost(8).castTime(10).cooldown(5).damageMultiplier(0.5F, 0.55F).lockOn(true).maxExp(6600).maxExpLevel(5).end()
				.build());

		magics.put(ResourceLocation.parse(Strings.Magic_TripleFiraga).getPath(), new MagicBuilder()
				.level(0).cost(22).castTime(15).cooldown(120).damageMultiplier(0.4F, 0.47F).lockOn(true).maxExp(4200).maxExpLevel(4).end()
				.build());

		magics.put(ResourceLocation.parse(Strings.Magic_CrawlingFiraga).getPath(), new MagicBuilder()
				.level(0).cost(12).castTime(10).cooldown(28).damageMultiplier(0.35F, 0.46F).lockOn(false).maxExp(4200).maxExpLevel(4).end()
				.build());

		magics.put(ResourceLocation.parse(Strings.Magic_FissionFiraga).getPath(), new MagicBuilder()
				.level(0).cost(12).castTime(10).cooldown(24).damageMultiplier(0.35F,0.46F).lockOn(false).maxExp(4200).maxExpLevel(4).end()
				.build());

		magics.put(ResourceLocation.parse(Strings.Magic_FiragaBurst).getPath(), new MagicBuilder()
				.level(0).cost(26).castTime(10).cooldown(150).damageMultiplier(0.1F, 0.15F).lockOn(false).maxExp(6800).maxExpLevel(5).end()
				.build());


		magics.put(ResourceLocation.parse(Strings.Magic_TripleBlizzard).getPath(), new MagicBuilder()
				.level(0).cost(22).castTime(15).cooldown(120).damageMultiplier(0.3F,0.4F).lockOn(true).maxExp(4200).maxExpLevel(4).end()
				.build());

		magics.put(ResourceLocation.parse(Strings.Magic_DeepFreeze).getPath(), new MagicBuilder()
				.level(0).cost(22).castTime(12).cooldown(120).damageMultiplier(0.3F, 3.8F).lockOn(true).maxExp(4200).maxExpLevel(5).end()
				.build());

		magics.put(ResourceLocation.parse(Strings.Magic_Glacier).getPath(), new MagicBuilder()
				.level(0).cost(22).castTime(15).cooldown(120).damageMultiplier(0.45F,0.55F).lockOn(true).maxExp(4200).maxExpLevel(5).end()
				.build());

		magics.put(ResourceLocation.parse(Strings.Magic_IceBarrage).getPath(), new MagicBuilder()
				.level(0).cost(22).castTime(15).cooldown(120).damageMultiplier(0.3F).lockOn(true).maxExp(4200).maxExpLevel(5).end()
				.build());


		magics.put(ResourceLocation.parse(Strings.Magic_ThundagaShot).getPath(), new MagicBuilder()
				.level(0).cost(12).castTime(10).cooldown(20).damageMultiplier(0.5F,0.6F).lockOn(false).maxExp(1800).maxExpLevel(3).end()
				.build());


		magics.put(ResourceLocation.parse(Strings.Magic_Blackout).getPath(), new MagicBuilder()
				.level(0).cost(12).castTime(10).cooldown(20).damageMultiplier(0.3F, 0.4F).lockOn(false).maxExp(1800).maxExpLevel(3).end()
				.build());

		magics.put(ResourceLocation.parse(Strings.Magic_Poison).getPath(), new MagicBuilder()
				.level(0).cost(12).castTime(10).cooldown(20).damageMultiplier(0.3F, 0.4F).lockOn(false).maxExp(1800).maxExpLevel(3).end()
				.build());

		magics.put(ResourceLocation.parse(Strings.Magic_ZeroGravity).getPath(), new MagicBuilder()
				.level(0).cost(24).castTime(20).cooldown(40).damageMultiplier(2F, 2.5F).lockOn(true).maxExp(1800).maxExpLevel(3).end()
				.level(1).cost(24).castTime(20).cooldown(40).damageMultiplier(4F, 4.5F).lockOn(true).maxExp(2400).maxExpLevel(3).end()
				.level(2).cost(24).castTime(20).cooldown(40).damageMultiplier(6F, 6.7F).lockOn(true).maxExp(4200).maxExpLevel(4).end()
				.level(3).cost(24).castTime(15).cooldown(40).damageMultiplier(1.0F, 1.2F).lockOn(true).maxExp(200).maxExpLevel(4).end()
				.build());

		magics.put(ResourceLocation.parse(Strings.Magic_Balloon).getPath(), new MagicBuilder()
				.level(0).cost(12).castTime(10).cooldown(20).damageMultiplier(0.3F, 0.4F).lockOn(false).maxExp(1800).maxExpLevel(3).end()
				.level(1).cost(12).castTime(10).cooldown(20).damageMultiplier(0.35F, 0.45F).lockOn(false).maxExp(1800).maxExpLevel(4).end()
				.level(2).cost(12).castTime(10).cooldown(20).damageMultiplier(0.4F, 0.45F).lockOn(false).maxExp(1800).maxExpLevel(4).end()
				.build());

		magics.put(ResourceLocation.parse(Strings.Magic_Spark).getPath(), new MagicBuilder()
				.level(0).cost(12).castTime(10).cooldown(20).damageMultiplier(0.3F, 0.4F).lockOn(false).maxExp(1800).maxExpLevel(3).end()
				.level(1).cost(12).castTime(10).cooldown(20).damageMultiplier(0.35F, 0.45F).lockOn(false).maxExp(1800).maxExpLevel(4).end()
				.level(2).cost(12).castTime(10).cooldown(20).damageMultiplier(0.4F, 0.45F).lockOn(false).maxExp(1800).maxExpLevel(4).end()
				.build());

		magics.put(ResourceLocation.parse(Strings.Magic_MineShield).getPath(), new MagicBuilder()
				.level(0).cost(12).castTime(10).cooldown(20).damageMultiplier(0.3F, 0.4F).lockOn(false).maxExp(1800).maxExpLevel(4).end()
				.level(1).cost(12).castTime(10).cooldown(20).damageMultiplier(0.35F, 0.45F).lockOn(false).maxExp(1800).maxExpLevel(4).end()
				.level(2).cost(12).castTime(10).cooldown(20).damageMultiplier(0.35F, 0.45F).lockOn(false).maxExp(1800).maxExpLevel(4).end()
				.build());

		magics.put(ResourceLocation.parse(Strings.Magic_Warp).getPath(), new MagicBuilder()
				.level(0).cost(12).castTime(10).cooldown(20).damageMultiplier(0.2F, 0.6F).lockOn(false).maxExp(1800).maxExpLevel(5).end()
				.build());

		magics.put(ResourceLocation.parse(Strings.Magic_Esuna).getPath(), new MagicBuilder()
				.level(0).cost(12).castTime(10).cooldown(20).damageMultiplier(0.3F).lockOn(false).maxExp(1800).maxExpLevel(1).end()
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
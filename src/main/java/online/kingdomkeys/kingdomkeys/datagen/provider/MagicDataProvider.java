package online.kingdomkeys.kingdomkeys.datagen.provider;

import com.google.gson.JsonObject;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.datagen.builder.MagicBuilder;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.magic.ModMagic;

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

		magics.put(ResourceLocation.parse(Strings.Magic_Fire).getPath(), new MagicBuilder().cost(8).castTime(10).cooldown(5).damageMultiplier(0.2F, 0.25F).lockOn(true).maxExp(1800).maxExpLevel(3).nextTier(ModMagic.FIRA.get().getRegistryName(), ModMagic.FIRE.get().getRegistryName()).build());
		magics.put(ResourceLocation.parse(Strings.Magic_Fira).getPath(), new MagicBuilder().cost(8).castTime(10).cooldown(5).damageMultiplier(0.3F, 0.35F).lockOn(true).maxExp(2400).maxExpLevel(3).nextTier(ModMagic.FIRAGA.get().getRegistryName(), ModMagic.FIRE.get().getRegistryName()).build());
		magics.put(ResourceLocation.parse(Strings.Magic_Firaga).getPath(), new MagicBuilder().cost(8).castTime(10).cooldown(5).damageMultiplier(0.4F, 0.45F).lockOn(true).maxExp(4200).maxExpLevel(4).nextTier(ModMagic.FIRAZA.get().getRegistryName(), ModMagic.FIRE.get().getRegistryName()).build());
		magics.put(ResourceLocation.parse(Strings.Magic_Firaza).getPath(), new MagicBuilder().cost(8).castTime(15).cooldown(10).damageMultiplier(1.0F).lockOn(true).maxExp(800).maxExpLevel(3).build());

		magics.put(ResourceLocation.parse(Strings.Magic_Blizzard).getPath(), new MagicBuilder().cost(10).castTime(10).cooldown(20).damageMultiplier(0.3F, 0.32F).lockOn(false).maxExp(1800).maxExpLevel(3).nextTier(ModMagic.BLIZZARA.get().getRegistryName(), ModMagic.BLIZZARD.get().getRegistryName()).build());
		magics.put(ResourceLocation.parse(Strings.Magic_Blizzara).getPath(), new MagicBuilder().cost(10).castTime(10).cooldown(20).damageMultiplier(0.25F, 0.27F).lockOn(false).maxExp(2400).maxExpLevel(3).nextTier(ModMagic.BLIZZAGA.get().getRegistryName(), ModMagic.BLIZZARD.get().getRegistryName()).build());
		magics.put(ResourceLocation.parse(Strings.Magic_Blizzaga).getPath(), new MagicBuilder().cost(10).castTime(10).cooldown(20).damageMultiplier(0.2F, 0.22F).lockOn(false).maxExp(4200).maxExpLevel(4).nextTier(ModMagic.BLIZZAZA.get().getRegistryName(), ModMagic.BLIZZARD.get().getRegistryName()).build());
		magics.put(ResourceLocation.parse(Strings.Magic_Blizzaza).getPath(), new MagicBuilder().cost(10).castTime(15).cooldown(20).damageMultiplier(0.8F, 0.9F).lockOn(false).maxExp(200).maxExpLevel(3).build());

		magics.put(ResourceLocation.parse(Strings.Magic_Water).getPath(), new MagicBuilder().cost(12).castTime(0).cooldown(55).damageMultiplier(0.15F, 1.19F).lockOn(false).maxExp(1800).maxExpLevel(3).nextTier(ModMagic.WATERA.get().getRegistryName(), ModMagic.WATER.get().getRegistryName()).build());
		magics.put(ResourceLocation.parse(Strings.Magic_Watera).getPath(), new MagicBuilder().cost(12).castTime(0).cooldown(55).damageMultiplier(0.25F, 0.30F).lockOn(false).maxExp(2400).maxExpLevel(3).nextTier(ModMagic.WATERGA.get().getRegistryName(), ModMagic.WATER.get().getRegistryName()).build());
		magics.put(ResourceLocation.parse(Strings.Magic_Waterga).getPath(), new MagicBuilder().cost(12).castTime(0).cooldown(55).damageMultiplier(0.35F, 0.37F).lockOn(false).maxExp(4200).maxExpLevel(4).nextTier(ModMagic.WATERZA.get().getRegistryName(), ModMagic.WATER.get().getRegistryName()).build());
		magics.put(ResourceLocation.parse(Strings.Magic_Waterza).getPath(), new MagicBuilder().cost(12).castTime(0).cooldown(55).damageMultiplier(0.8F, 0.85F).lockOn(false).maxExp(200).maxExpLevel(3).build());

		magics.put(ResourceLocation.parse(Strings.Magic_Thunder).getPath(), new MagicBuilder().cost(14).castTime(10).cooldown(30).damageMultiplier(0.1F, 0.104F).lockOn(true).maxExp(1800).maxExpLevel(3).nextTier(ModMagic.THUNDARA.get().getRegistryName(), ModMagic.THUNDER.get().getRegistryName()).build());
		magics.put(ResourceLocation.parse(Strings.Magic_Thundara).getPath(), new MagicBuilder().cost(14).castTime(10).cooldown(35).damageMultiplier(0.11F, 0.114F).lockOn(true).maxExp(2400).maxExpLevel(3).nextTier(ModMagic.THUNDAGA.get().getRegistryName(), ModMagic.THUNDER.get().getRegistryName()).build());
		magics.put(ResourceLocation.parse(Strings.Magic_Thundaga).getPath(), new MagicBuilder().cost(14).castTime(10).cooldown(40).damageMultiplier(0.12F, 0.124F).lockOn(true).maxExp(4200).maxExpLevel(4).nextTier(ModMagic.THUNDAZA.get().getRegistryName(), ModMagic.THUNDER.get().getRegistryName()).build());
		magics.put(ResourceLocation.parse(Strings.Magic_Thundaza).getPath(), new MagicBuilder().cost(14).castTime(10).cooldown(50).damageMultiplier(0.14F, 0.144F).lockOn(true).maxExp(200).maxExpLevel(3).build());

		magics.put(ResourceLocation.parse(Strings.Magic_Cure).getPath(), new MagicBuilder().cost(300).castTime(1).cooldown(20).damageMultiplier(0.25F, 0.33F).lockOn(false).maxExp(1800).maxExpLevel(3).nextTier(ModMagic.CURA.get().getRegistryName(), ModMagic.CURE.get().getRegistryName()).build());
		magics.put(ResourceLocation.parse(Strings.Magic_Cura).getPath(), new MagicBuilder().cost(300).castTime(1).cooldown(20).damageMultiplier(0.5F, 0.58F).lockOn(false).maxExp(2400).maxExpLevel(3).nextTier(ModMagic.CURAGA.get().getRegistryName(), ModMagic.CURE.get().getRegistryName()).build());
		magics.put(ResourceLocation.parse(Strings.Magic_Curaga).getPath(), new MagicBuilder().cost(300).castTime(1).cooldown(20).damageMultiplier(0.75F, 0.8F).lockOn(false).maxExp(4200).maxExpLevel(4).nextTier(ModMagic.CURAZA.get().getRegistryName(), ModMagic.CURE.get().getRegistryName()).build());
		magics.put(ResourceLocation.parse(Strings.Magic_Curaza).getPath(), new MagicBuilder().cost(300).castTime(1).cooldown(20).damageMultiplier(1.0F, 1.1F).lockOn(false).maxExp(200).maxExpLevel(3).build());

		magics.put(ResourceLocation.parse(Strings.Magic_Aero).getPath(), new MagicBuilder().cost(12).castTime(10).cooldown(20).damageMultiplier(1.0F, 1.3F).lockOn(false).maxExp(1800).maxExpLevel(3).build());
		magics.put(ResourceLocation.parse(Strings.Magic_Aerora).getPath(), new MagicBuilder().cost(12).castTime(10).cooldown(20).damageMultiplier(1.5F, 1.7F).lockOn(false).maxExp(2400).maxExpLevel(3).build());
		magics.put(ResourceLocation.parse(Strings.Magic_Aeroga).getPath(), new MagicBuilder().cost(12).castTime(10).cooldown(20).damageMultiplier(2.0F, 2.2F).lockOn(false).maxExp(4200).maxExpLevel(4).build());
		//magics.put(ResourceLocation.parse(Strings.Magic_Aeroza).getPath(), new MagicBuilder().cost(12).castTime(10).cooldown(20).damageMultiplier(1.0F).lockOn(false).maxExp(200).maxExpLevel(3).build());

		magics.put(ResourceLocation.parse(Strings.Magic_Gravity).getPath(), new MagicBuilder().cost(14).castTime(10).cooldown(30).damageMultiplier(0.25F, 0.33F).lockOn(true).maxExp(1800).maxExpLevel(3).build());
		magics.put(ResourceLocation.parse(Strings.Magic_Gravira).getPath(), new MagicBuilder().cost(14).castTime(10).cooldown(30).damageMultiplier(0.5F, 0.6F).lockOn(true).maxExp(2400).maxExpLevel(3).build());
		magics.put(ResourceLocation.parse(Strings.Magic_Graviga).getPath(), new MagicBuilder().cost(14).castTime(10).cooldown(30).damageMultiplier(0.75F, 0.8F).lockOn(true).maxExp(4200).maxExpLevel(4).build());
		//magics.put(ResourceLocation.parse(Strings.Magic_Graviza).getPath(), new MagicBuilder().cost(14).castTime(15).cooldown(40).damageMultiplier(1.0F, 1.1F).lockOn(true).maxExp(200).maxExpLevel(3).build());

		magics.put(ResourceLocation.parse(Strings.Magic_Magnet).getPath(), new MagicBuilder().cost(15).castTime(10).cooldown(3).damageMultiplier(0.0F, 0.4F).lockOn(true).maxExp(1800).maxExpLevel(3).build());
		magics.put(ResourceLocation.parse(Strings.Magic_Magnera).getPath(), new MagicBuilder().cost(15).castTime(10).cooldown(3).damageMultiplier(1.0F, 1.4F).lockOn(true).maxExp(2400).maxExpLevel(3).build());
		magics.put(ResourceLocation.parse(Strings.Magic_Magnega).getPath(), new MagicBuilder().cost(15).castTime(10).cooldown(3).damageMultiplier(2.0F, 2.4F).lockOn(true).maxExp(4200).maxExpLevel(4).build());
		//magics.put(ResourceLocation.parse(Strings.Magic_Magneza).getPath(), new MagicBuilder().cost(15).castTime(10).cooldown(3).damageMultiplier(3.0F, 3.4F).lockOn(true).maxExp(200).maxExpLevel(3).build());

		magics.put(ResourceLocation.parse(Strings.Magic_Reflect).getPath(), new MagicBuilder().cost(10).castTime(1).cooldown(69).damageMultiplier(0.3F, 0.34F).lockOn(false).maxExp(1800).maxExpLevel(3).build());
		magics.put(ResourceLocation.parse(Strings.Magic_Reflera).getPath(), new MagicBuilder().cost(10).castTime(1).cooldown(69).damageMultiplier(0.5F, 0.58F).lockOn(false).maxExp(2400).maxExpLevel(3).build());
		magics.put(ResourceLocation.parse(Strings.Magic_Reflega).getPath(), new MagicBuilder().cost(10).castTime(1).cooldown(69).damageMultiplier(0.7F, 0.8F).lockOn(false).maxExp(4200).maxExpLevel(4).build());
		//magics.put(ResourceLocation.parse(Strings.Magic_Refleza).getPath(), new MagicBuilder().cost(10).castTime(1).cooldown(69).damageMultiplier(1.0F, 1.1F).lockOn(false).maxExp(200).maxExpLevel(3).build());

		magics.put(ResourceLocation.parse(Strings.Magic_Stop).getPath(), new MagicBuilder().cost(20).castTime(10).cooldown(20).damageMultiplier(0.5F, 0.6F).lockOn(false).maxExp(1800).maxExpLevel(3).build());
		magics.put(ResourceLocation.parse(Strings.Magic_Stopra).getPath(), new MagicBuilder().cost(20).castTime(10).cooldown(20).damageMultiplier(1.0F, 1.2F).lockOn(false).maxExp(2400).maxExpLevel(3).build());
		magics.put(ResourceLocation.parse(Strings.Magic_Stopga).getPath(), new MagicBuilder().cost(20).castTime(10).cooldown(20).damageMultiplier(1.5F, 1.8F).lockOn(false).maxExp(4200).maxExpLevel(4).build());
		//magics.put(ResourceLocation.parse(Strings.Magic_Stopza).getPath(), new MagicBuilder().cost(20).castTime(0).cooldown(20).damageMultiplier(1.0F).lockOn(false).maxExp(200).maxExpLevel(3).build());


		magics.put(ResourceLocation.parse(Strings.Magic_DarkFiraga).getPath(), new MagicBuilder().cost(8).castTime(10).cooldown(5).damageMultiplier(0.5F, 0.55F).lockOn(true).maxExp(6600).maxExpLevel(5).build());
		magics.put(ResourceLocation.parse(Strings.Magic_TripleFiraga).getPath(), new MagicBuilder().cost(22).castTime(15).cooldown(120).damageMultiplier(0.4F, 0.47F).lockOn(true).maxExp(4600).maxExpLevel(4).build());
		magics.put(ResourceLocation.parse(Strings.Magic_CrawlingFiraga).getPath(), new MagicBuilder().cost(12).castTime(10).cooldown(28).damageMultiplier(0.35F, 0.46F).lockOn(false).maxExp(4400).maxExpLevel(4).build());
		magics.put(ResourceLocation.parse(Strings.Magic_FissionFiraga).getPath(), new MagicBuilder().cost(12).castTime(10).cooldown(24).damageMultiplier(0.35F, 0.46F).lockOn(false).maxExp(4600).maxExpLevel(4).build());
		magics.put(ResourceLocation.parse(Strings.Magic_FiragaBurst).getPath(), new MagicBuilder().cost(26).castTime(10).cooldown(150).damageMultiplier(0.1F, 0.15F).lockOn(false).maxExp(6800).maxExpLevel(5).build());
		magics.put(ResourceLocation.parse(Strings.Magic_Ignite).getPath(), new MagicBuilder().cost(16).castTime(10).cooldown(30).damageMultiplier(30F, 120F).lockOn(true).maxExp(3400).maxExpLevel(4).build());

		magics.put(ResourceLocation.parse(Strings.Magic_TripleBlizzard).getPath(), new MagicBuilder().cost(22).castTime(15).cooldown(120).damageMultiplier(0.3F, 0.4F).lockOn(true).maxExp(4600).maxExpLevel(4).build());
		magics.put(ResourceLocation.parse(Strings.Magic_DeepFreeze).getPath(), new MagicBuilder().cost(22).castTime(12).cooldown(120).damageMultiplier(0.3F, 3.8F).lockOn(true).maxExp(6400).maxExpLevel(5).build());
		magics.put(ResourceLocation.parse(Strings.Magic_Glacier).getPath(), new MagicBuilder().cost(22).castTime(15).cooldown(120).damageMultiplier(0.45F, 0.55F).lockOn(true).maxExp(7200).maxExpLevel(5).build());
		magics.put(ResourceLocation.parse(Strings.Magic_IceBarrage).getPath(), new MagicBuilder().cost(22).castTime(15).cooldown(120).damageMultiplier(0.3F).lockOn(true).maxExp(6800).maxExpLevel(5).build());

		magics.put(ResourceLocation.parse(Strings.Magic_ThundagaShot).getPath(), new MagicBuilder().cost(12).castTime(10).cooldown(20).damageMultiplier(0.5F, 0.6F).lockOn(false).maxExp(4200).maxExpLevel(4).build());
		magics.put(ResourceLocation.parse(Strings.Magic_TriplePlasma).getPath(), new MagicBuilder().cost(12).castTime(10).cooldown(20).damageMultiplier(0.3F, 0.4F).lockOn(false).maxExp(4600).maxExpLevel(4).build());

		magics.put(ResourceLocation.parse(Strings.Magic_Blackout).getPath(), new MagicBuilder().cost(12).castTime(10).cooldown(20).damageMultiplier(0.3F, 0.4F).lockOn(false).maxExp(1800).maxExpLevel(3).build());
		magics.put(ResourceLocation.parse(Strings.Magic_Poison).getPath(), new MagicBuilder().cost(12).castTime(10).cooldown(20).damageMultiplier(0.3F, 0.4F).lockOn(false).maxExp(1800).maxExpLevel(3).build());

		magics.put(ResourceLocation.parse(Strings.Magic_ZeroGravity).getPath(), new MagicBuilder().cost(24).castTime(20).cooldown(40).damageMultiplier(2F, 2.5F).lockOn(true).maxExp(1800).maxExpLevel(3).build());
		magics.put(ResourceLocation.parse(Strings.Magic_ZeroGravira).getPath(), new MagicBuilder().cost(24).castTime(20).cooldown(40).damageMultiplier(4F, 4.5F).lockOn(true).maxExp(2400).maxExpLevel(3).build());
		magics.put(ResourceLocation.parse(Strings.Magic_ZeroGraviga).getPath(), new MagicBuilder().cost(24).castTime(20).cooldown(40).damageMultiplier(6F, 6.7F).lockOn(true).maxExp(4200).maxExpLevel(4).build());
		//magics.put(ResourceLocation.parse(Strings.Magic_ZeroGraviza).getPath(), new MagicBuilder().cost(24).castTime(15).cooldown(40).damageMultiplier(1.0F, 1.2F).lockOn(true).maxExp(200).maxExpLevel(4).build());

		magics.put(ResourceLocation.parse(Strings.Magic_Balloon).getPath(), new MagicBuilder().cost(12).castTime(10).cooldown(20).damageMultiplier(0.3F, 0.4F).lockOn(false).maxExp(1800).maxExpLevel(3).build());
		magics.put(ResourceLocation.parse(Strings.Magic_Balloonra).getPath(), new MagicBuilder().cost(12).castTime(10).cooldown(20).damageMultiplier(0.35F, 0.45F).lockOn(false).maxExp(2400).maxExpLevel(4).build());
		magics.put(ResourceLocation.parse(Strings.Magic_Balloonga).getPath(), new MagicBuilder().cost(12).castTime(10).cooldown(20).damageMultiplier(0.4F, 0.45F).lockOn(false).maxExp(4200).maxExpLevel(4).build());

		magics.put(ResourceLocation.parse(Strings.Magic_Spark).getPath(), new MagicBuilder().cost(12).castTime(10).cooldown(20).damageMultiplier(0.3F, 0.4F).lockOn(false).maxExp(1800).maxExpLevel(3).build());
		magics.put(ResourceLocation.parse(Strings.Magic_Sparkra).getPath(), new MagicBuilder().cost(12).castTime(10).cooldown(20).damageMultiplier(0.35F, 0.45F).lockOn(false).maxExp(2400).maxExpLevel(4).build());
		magics.put(ResourceLocation.parse(Strings.Magic_Sparkga).getPath(), new MagicBuilder().cost(12).castTime(10).cooldown(20).damageMultiplier(0.4F, 0.45F).lockOn(false).maxExp(4200).maxExpLevel(4).build());

		magics.put(ResourceLocation.parse(Strings.Magic_MineShield).getPath(), new MagicBuilder().cost(12).castTime(10).cooldown(20).damageMultiplier(0.3F, 0.4F).lockOn(false).maxExp(1800).maxExpLevel(4).build());
		magics.put(ResourceLocation.parse(Strings.Magic_MineSquare).getPath(), new MagicBuilder().cost(12).castTime(10).cooldown(20).damageMultiplier(0.35F, 0.45F).lockOn(false).maxExp(2500).maxExpLevel(4).build());
		magics.put(ResourceLocation.parse(Strings.Magic_SeekerMine).getPath(), new MagicBuilder().cost(12).castTime(10).cooldown(20).damageMultiplier(0.35F, 0.45F).lockOn(false).maxExp(4600).maxExpLevel(4).build());

		magics.put(ResourceLocation.parse(Strings.Magic_Warp).getPath(), new MagicBuilder().cost(12).castTime(10).cooldown(20).damageMultiplier(0.2F, 0.6F).lockOn(false).maxExp(8600).maxExpLevel(5).build());
		magics.put(ResourceLocation.parse(Strings.Magic_Faith).getPath(), new MagicBuilder().cost(12).castTime(10).cooldown(200).damageMultiplier(0.3F, 0.55F).lockOn(false).maxExp(9600).maxExpLevel(6).build());
		magics.put(ResourceLocation.parse(Strings.Magic_Esuna).getPath(), new MagicBuilder().cost(12).castTime(10).cooldown(20).damageMultiplier(0.3F).lockOn(false).maxExp(1800).maxExpLevel(1).build());
		magics.put(ResourceLocation.parse(Strings.Magic_Bind).getPath(), new MagicBuilder().cost(8).castTime(10).cooldown(40).damageMultiplier(2F, 6F).lockOn(false).maxExp(1800).maxExpLevel(3).build());
		magics.put(ResourceLocation.parse(Strings.Magic_Confuse).getPath(), new MagicBuilder().cost(14).castTime(15).cooldown(40).damageMultiplier(2F, 6F).lockOn(false).maxExp(1800).maxExpLevel(3).build());
		magics.put(ResourceLocation.parse(Strings.Magic_Mini).getPath(), new MagicBuilder().cost(30).castTime(15).cooldown(70).damageMultiplier(2F, 6F).lockOn(false).maxExp(5800).maxExpLevel(4).build());
		magics.put(ResourceLocation.parse(Strings.Magic_Slow).getPath(), new MagicBuilder().cost(30).castTime(15).cooldown(70).damageMultiplier(2F, 6F).lockOn(false).maxExp(1800).maxExpLevel(3).build());

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
package online.kingdomkeys.kingdomkeys.datagen.provider;

import com.google.gson.JsonObject;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.datagen.builder.DriveFormBuilder;

import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class DriveFormDataProvider implements DataProvider {

	private final PackOutput.PathProvider pathProvider;

	public DriveFormDataProvider(PackOutput output) {
		this.pathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, "driveforms");
	}

	@Override
	public CompletableFuture<?> run(CachedOutput cache) {
		Map<String, JsonObject> forms = Map.of(
				"form_valor", buildValor(),
				"form_wisdom", buildWisdom(),
				"form_limit", buildLimit(),
				"form_master", buildMaster(),
				"form_final", buildFinal(),
				"form_anti", buildAnti()
		);

		CompletableFuture<?>[] futures = forms.entrySet().stream().map(entry -> {
			Path path = pathProvider.json(KingdomKeys.rl(entry.getKey()));
			return DataProvider.saveStable(cache, entry.getValue(), path);
		}).toArray(CompletableFuture[]::new);
		return CompletableFuture.allOf(futures);
	}

	@Override
	public String getName() {
		return "Kingdom Keys Drive Form Data";
	}

	private static JsonObject buildValor() {
		return new DriveFormBuilder()
			.cost(300)
			.ap(1)
			.canGoAnti(true)
			.canUseMagic(false)
			.strMult(1.2F)
			.magMult(1.0F)
			.speedMult(1.4F)
			.levelUp(0, 80, 240, 520, 968, 1528, 2200)
			.abilities(ModAbilities.SYNCH_BLADE)
			.baseLevelUpAbilities(null, ModAbilities.AUTO_VALOR, ModAbilities.HIGH_JUMP, ModAbilities.SUPERJUMP, ModAbilities.HIGH_JUMP, ModAbilities.SUPERJUMP, ModAbilities.HIGH_JUMP)
			.driveFormLevelUpAbilities(ModAbilities.HIGH_JUMP, null, ModAbilities.HIGH_JUMP, null, ModAbilities.HIGH_JUMP, null, ModAbilities.HIGH_JUMP).build();
	}

	private static JsonObject buildWisdom() {
		return new DriveFormBuilder()
			.cost(300)
			.ap(1)
			.canGoAnti(true)
			.canUseMagic(true)
			.strMult(1.0F)
			.magMult(1.2F)
			.speedMult(1.2F)
			.levelUp(0, 20, 80, 152, 242, 350, 500)
			.abilities(ModAbilities.MP_HASTEGA, ModAbilities.MP_HASTEGA)
			.baseLevelUpAbilities(null, ModAbilities.AUTO_WISDOM, ModAbilities.QUICK_RUN, ModAbilities.MP_RAGE, ModAbilities.QUICK_RUN, ModAbilities.MP_HASTE, ModAbilities.QUICK_RUN)
			.driveFormLevelUpAbilities(ModAbilities.QUICK_RUN, null, ModAbilities.QUICK_RUN, null, ModAbilities.QUICK_RUN, null, ModAbilities.QUICK_RUN).build();
	}

	private static JsonObject buildLimit() {
		return new DriveFormBuilder()
			.cost(400)
			.ap(1)
			.canGoAnti(true)
			.canUseMagic(true)
			.strMult(1.1F)
			.magMult(1.1F)
			.speedMult(1.2F)
			.levelUp(0, 3, 9, 21, 40, 63, 90)
			.abilities(ModAbilities.TREASURE_MAGNET, ModAbilities.LUCKY_STRIKE, ModAbilities.HP_GAIN, ModAbilities.MP_RAGE, ModAbilities.MP_HASTE)
			.baseLevelUpAbilities(null, ModAbilities.AUTO_LIMIT, ModAbilities.DODGE_ROLL, ModAbilities.TREASURE_MAGNET, ModAbilities.DODGE_ROLL, ModAbilities.LUCKY_STRIKE, ModAbilities.DODGE_ROLL)
			.driveFormLevelUpAbilities(ModAbilities.DODGE_ROLL, null, ModAbilities.DODGE_ROLL, null, ModAbilities.DODGE_ROLL, null, ModAbilities.DODGE_ROLL).build();
	}

	private static JsonObject buildMaster() {
		return new DriveFormBuilder()
			.cost(400)
			.ap(1)
			.canGoAnti(true)
			.canUseMagic(true)
			.strMult(1.2F)
			.magMult(1.2F)
			.speedMult(1.3F)
			.levelUp(0, 60, 240, 456, 726, 1050, 1500)
			.abilities(ModAbilities.SYNCH_BLADE, ModAbilities.TREASURE_MAGNET, ModAbilities.TREASURE_MAGNET, ModAbilities.MP_HASTERA)
			.baseLevelUpAbilities(null, ModAbilities.AUTO_MASTER, ModAbilities.AERIAL_DODGE, ModAbilities.SUPERSLIDE, ModAbilities.AERIAL_DODGE, ModAbilities.SUPERSLIDE, ModAbilities.AERIAL_DODGE)
			.driveFormLevelUpAbilities(ModAbilities.AERIAL_DODGE, null, ModAbilities.AERIAL_DODGE, null, ModAbilities.AERIAL_DODGE, null, ModAbilities.AERIAL_DODGE).build();
	}

	private static JsonObject buildFinal() {
		return new DriveFormBuilder()
			.cost(500)
			.ap(-10)
			.canGoAnti(false)
			.canUseMagic(true)
			.strMult(1.3F)
			.magMult(1.3F)
			.speedMult(1.3F)
			.levelUp(0, 20, 80, 152, 242, 350, 500)
			.abilities(ModAbilities.SYNCH_BLADE, ModAbilities.MP_HASTE)
			.baseLevelUpAbilities(null, ModAbilities.AUTO_FINAL, ModAbilities.GLIDE, ModAbilities.FORM_BOOST, ModAbilities.GLIDE, ModAbilities.FORM_BOOST, ModAbilities.GLIDE)
			.driveFormLevelUpAbilities(ModAbilities.GLIDE, null, ModAbilities.GLIDE, null, ModAbilities.GLIDE, null, ModAbilities.GLIDE).build();
	}

	private static JsonObject buildAnti() {
		return new DriveFormBuilder()
			.cost(100)
			.ap(-4)
			.canGoAnti(true)
			.canUseMagic(false)
			.strMult(1.0F)
			.magMult(1.0F)
			.speedMult(1.3F)
			.levelUp(0, 20, 80, 152, 242, 350, 500).build();
	}
}

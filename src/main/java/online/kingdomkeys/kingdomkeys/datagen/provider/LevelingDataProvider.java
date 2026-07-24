package online.kingdomkeys.kingdomkeys.datagen.provider;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.datagen.builder.LevelingBuilder;
import online.kingdomkeys.kingdomkeys.shotlock.ModShotlocks;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Generates data/kingdomkeys/leveling/{warrior,guardian,mystic}.json entirely from Java, so the mod no
 * longer depends on those 3 hand-authored JSON files existing on disk at all - the whole level-by-level
 * stat/ability/shotlock progression for all 3 paths lives here now.
 *
 * The buildWarrior()/buildGuardian()/buildMystic() methods below were generated directly from the
 * original JSON files (via a one-off script, not retyped by hand) to guarantee an exact, error-free
 * transcription of the existing stat/ability progression - the 12 new Shotlock unlocks are merged into
 * their correct level in that same pass (not tacked on at the end), and every ability/shotlock
 * reference uses the real ModAbilities.XXX / ModShotlocks.XXX constant instead of a hand-typed string,
 * so a rename or removal of one of those would show up as a compile error here instead of silently
 * breaking at runtime.
 */
public class LevelingDataProvider implements DataProvider {

	private final PackOutput.PathProvider pathProvider;

	public LevelingDataProvider(PackOutput output) {
		this.pathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, "leveling");
	}

	@Override
	public CompletableFuture<?> run(CachedOutput cache) {
		Map<String, JsonObject> paths = new LinkedHashMap<>();
		paths.put("warrior", buildWarrior());
		paths.put("mystic", buildMystic());
		paths.put("guardian", buildGuardian());

		CompletableFuture<?>[] futures = paths.entrySet().stream().map(entry -> {
			Path path = pathProvider.json(KingdomKeys.rl(entry.getKey()));
			return save(cache, entry.getValue(), path);
		}).toArray(CompletableFuture[]::new);
		return CompletableFuture.allOf(futures);
	}

	private CompletableFuture<?> save(CachedOutput cache, JsonObject json, Path path) {
		return CompletableFuture.runAsync(() -> {
			try {
				StringWriter stringWriter = new StringWriter();
				JsonWriter jsonWriter = new JsonWriter(stringWriter);
				jsonWriter.setIndent("  ");
				Gson gson = new Gson();
				gson.toJson(json, jsonWriter);
				byte[] bytes = stringWriter.toString().getBytes(StandardCharsets.UTF_8);
				HashCode hash = Hashing.sha1().hashBytes(bytes);
				cache.writeIfNeeded(path, bytes, hash);
			} catch (IOException e) {
				throw new UncheckedIOException(e);
			}
		});
	}

	@Override
	public String getName() {
		return "Kingdom Keys Leveling Data";
	}

	private static JsonObject buildWarrior() {
		return new LevelingBuilder()
			.level(0).str(-1)
			.level(1).str(1).ap(10).abilities(ModAbilities.ZERO_EXP)
			.level(2).def(1).abilities(ModAbilities.SCAN)
			.level(3).str(1).ap(1)
			.level(4).def(1).maxMp(8)
			.level(5).str(1).maxHp(5).maxArmors(1).maxMagics(1)
			.level(6).def(1).mag(1).ap(1)
			.level(7).str(1).abilities(ModAbilities.WALL_KICK)
			.level(8).mag(1).maxMp(8)
			.level(9).str(1).ap(1).abilities(ModAbilities.COMBO_PLUS)
			.level(10).def(1).mag(1).maxHp(5).maxMagics(1).maxAccessories(1)
			.level(11).str(1).shotlocks(ModShotlocks.RAGNAROK)
			.level(12).mag(1).maxMp(8).ap(1).abilities(ModAbilities.MP_HASTE)
			.level(13).str(1).abilities(ModAbilities.STRIKE_RAID)
			.level(14).def(1).mag(1).abilities(ModAbilities.MP_SAFETY)
			.level(15).str(1).maxHp(5).ap(1).maxArmors(1).maxMagics(1).abilities(ModAbilities.DAMAGE_DRIVE).shotlocks(ModShotlocks.METEOR_SHOWER)
			.level(16).mag(1).maxMp(8).abilities(ModAbilities.MP_RAGE)
			.level(17).str(1).abilities(ModAbilities.GRAND_MAGIC_EXTENDER)
			.level(18).def(1).mag(1).ap(1).abilities(ModAbilities.FOCUS_CONVERTER)
			.level(19).str(1).abilities(ModAbilities.AIR_SLIDE)
			.level(20).mag(1).maxHp(5).maxMp(8).maxMagics(1).maxAccessories(1).abilities(ModAbilities.JACKPOT).shotlocks(ModShotlocks.CHAOS_SNAKE)
			.level(21).str(1).ap(1).abilities(ModAbilities.ENCOUNTER_PLUS)
			.level(22).def(1).mag(1).abilities(ModAbilities.FORM_BOOST)
			.level(23).str(1)
			.level(24).mag(1).maxMp(4).ap(1).abilities(ModAbilities.DRIVE_CONVERTER)
			.level(25).str(1).maxHp(5).maxArmors(1).maxMagics(1).abilities(ModAbilities.DRIVE_BOOST).shotlocks(ModShotlocks.PULSE_BOMB)
			.level(26).def(1).mag(1).abilities(ModAbilities.LEAF_BRACER)
			.level(27).str(1).mag(1).ap(1).abilities(ModAbilities.BERSERK_CHARGE)
			.level(28).mag(1).maxMp(4).abilities(ModAbilities.EXPERIENCE_BOOST)
			.level(29).str(1).abilities(ModAbilities.GRAND_MAGIC_EXTENDER).shotlocks(ModShotlocks.DARK_VOLLEY)
			.level(30).def(1).mag(1).maxHp(5).ap(1).maxMagics(1).maxAccessories(1).abilities(ModAbilities.CURAZA)
			.level(31).str(1).abilities(ModAbilities.TREASURE_MAGNET)
			.level(32).str(1).mag(1).maxMp(4)
			.level(33).str(1).ap(1).abilities(ModAbilities.FLOWSTEP).shotlocks(ModShotlocks.LIGHTNING_RAY)
			.level(34).def(1).mag(1).abilities(ModAbilities.MP_HASTERA)
			.level(35).str(1).maxHp(5).maxArmors(1).maxMagics(1).abilities(ModAbilities.GRAND_MAGIC_HASTE)
			.level(36).mag(1).maxMp(4).ap(1).abilities(ModAbilities.FIRAZA)
			.level(37).str(1)
			.level(38).def(1).mag(1).abilities(ModAbilities.EXTRA_CAST)
			.level(39).str(1).ap(1).abilities(ModAbilities.ITEM_BOOST)
			.level(40).mag(1).maxHp(5).maxMp(4).maxMagics(1).maxAccessories(1).abilities(ModAbilities.AIR_SLIDE).shotlocks(ModShotlocks.BUBBLE_BLASTER)
			.level(41).str(1).abilities(ModAbilities.LUCKY_STRIKE)
			.level(42).def(1).mag(1).ap(1).abilities(ModAbilities.SUPERSLIDE)
			.level(43).str(1).mag(1).abilities(ModAbilities.BLIZZAZA)
			.level(44).mag(1).maxMp(4).abilities(ModAbilities.JACKPOT, ModAbilities.NEGATIVE_COMBO)
			.level(45).str(1).maxHp(5).ap(1).maxMagics(1).abilities(ModAbilities.ENDLESS_MAGIC).shotlocks(ModShotlocks.FLAME_SALVO)
			.level(46).def(1).mag(1).abilities(ModAbilities.MAGIC_LOCK_ON)
			.level(47).str(1).abilities(ModAbilities.TREASURE_MAGNET)
			.level(48).str(1).mag(1).maxMp(4).ap(1).abilities(ModAbilities.THUNDAZA)
			.level(49).str(1).abilities(ModAbilities.FINISHING_PLUS)
			.level(50).def(1).mag(1).maxHp(5).maxMagics(1).abilities(ModAbilities.SYNCH_BLADE).shotlocks(ModShotlocks.PHOTON_CHARGE)
			.level(51).str(1).ap(1).abilities(ModAbilities.WALL_KICK)
			.level(52).mag(1).maxMp(4)
			.level(53).str(1).abilities(ModAbilities.MP_HASTEGA)
			.level(54).def(1).mag(1).ap(1)
			.level(55).str(1).maxHp(5).abilities(ModAbilities.WATERZA)
			.level(56).mag(1).maxMp(4).abilities(ModAbilities.SECOND_CHANCE)
			.level(57).str(1).ap(1)
			.level(58).def(1).mag(1).shotlocks(ModShotlocks.ABSOLUTE_ZERO)
			.level(59).str(1)
			.level(60).mag(1).maxHp(5).maxMp(4).ap(1)
			.level(61).str(1)
			.level(62).def(1).mag(1)
			.level(63).str(1).ap(1)
			.level(64).mag(1).maxMp(4)
			.level(65).str(1).maxHp(5).shotlocks(ModShotlocks.SONIC_SHADOW)
			.level(66).def(1).mag(1).ap(1)
			.level(67).str(1)
			.level(68).mag(1).maxMp(4)
			.level(69).str(1).ap(1).shotlocks(ModShotlocks.PRISM_RAIN)
			.level(70).def(1).mag(1).maxHp(5)
			.level(71).str(1)
			.level(72).mag(1).maxMp(4).ap(1).abilities(ModAbilities.JACKPOT, ModAbilities.COMBO_PLUS)
			.level(73).str(1)
			.level(74).def(1).mag(1)
			.level(75).str(1).maxHp(5).ap(1).shotlocks(ModShotlocks.BIO_BARRAGE)
			.level(76).mag(1).maxMp(4)
			.level(77).str(1)
			.level(78).def(1).mag(1).ap(1)
			.level(79).str(1)
			.level(80).mag(1).maxHp(5).maxMp(4)
			.level(81).str(1).ap(1)
			.level(82).def(1).mag(1)
			.level(83).str(1)
			.level(84).mag(1).maxMp(4).ap(1).abilities(ModAbilities.COMBO_PLUS)
			.level(85).str(1).maxHp(5).shotlocks(ModShotlocks.THUNDERSTORM)
			.level(86).def(1).mag(1)
			.level(87).str(1).ap(1)
			.level(88).mag(1).maxMp(4)
			.level(89).str(1)
			.level(90).def(1).mag(1).maxHp(5).ap(1)
			.level(91).str(1)
			.level(92).mag(1).maxMp(4)
			.level(93).str(1).ap(1)
			.level(94).def(1).mag(1)
			.level(95).str(1).maxHp(5)
			.level(96).mag(1).maxMp(4).ap(1).abilities(ModAbilities.COMBO_PLUS)
			.level(97).str(1)
			.level(98).def(1).mag(1).shotlocks(ModShotlocks.ULTIMA_CANNON, ModShotlocks.LIGHTBLOOM, ModShotlocks.MULTIVORTEX)
			.level(99).str(1).ap(1)
			.level(100).str(10).def(10).mag(10).maxHp(5).maxMp(4)
			.build();
	}

	private static JsonObject buildMystic() {
		return new LevelingBuilder()
			.level(0).mag(-1)
			.level(1).mag(1).ap(10).abilities(ModAbilities.ZERO_EXP)
			.level(2).def(1).abilities(ModAbilities.SCAN)
			.level(3).str(1).ap(1)
			.level(4).def(1).maxMp(8).maxMagics(1)
			.level(5).str(1).maxHp(5).maxArmors(1)
			.level(6).def(1).mag(1).ap(1)
			.level(7).str(1)
			.level(8).mag(1).maxMp(8).maxMagics(1)
			.level(9).str(1).ap(1).abilities(ModAbilities.WALL_KICK, ModAbilities.GRAND_MAGIC_EXTENDER)
			.level(10).def(1).mag(1).maxHp(5).maxAccessories(1)
			.level(11).str(1).shotlocks(ModShotlocks.RAGNAROK)
			.level(12).mag(1).maxMp(8).ap(1).maxMagics(1).abilities(ModAbilities.MP_HASTE)
			.level(13).str(1).abilities(ModAbilities.FIRAZA)
			.level(14).def(1).mag(1).abilities(ModAbilities.MP_SAFETY)
			.level(15).str(1).maxHp(5).ap(1).maxArmors(1).abilities(ModAbilities.DAMAGE_DRIVE).shotlocks(ModShotlocks.METEOR_SHOWER)
			.level(16).mag(1).maxMp(8).maxMagics(1).abilities(ModAbilities.MP_RAGE)
			.level(17).str(1).abilities(ModAbilities.BLIZZAZA)
			.level(18).def(1).mag(1).ap(1).abilities(ModAbilities.FOCUS_CONVERTER)
			.level(19).str(1).abilities(ModAbilities.GRAND_MAGIC_HASTE)
			.level(20).mag(1).maxHp(5).maxMp(8).maxMagics(1).maxAccessories(1).abilities(ModAbilities.JACKPOT, ModAbilities.GRAND_MAGIC_EXTENDER).shotlocks(ModShotlocks.CHAOS_SNAKE)
			.level(21).str(1).ap(1).abilities(ModAbilities.CURAZA)
			.level(22).def(1).mag(1).abilities(ModAbilities.FORM_BOOST)
			.level(23).str(1).abilities(ModAbilities.THUNDAZA)
			.level(24).mag(1).maxMp(4).ap(1).maxMagics(1).abilities(ModAbilities.DRIVE_CONVERTER, ModAbilities.ENDLESS_MAGIC)
			.level(25).str(1).maxHp(5).maxArmors(1).abilities(ModAbilities.DRIVE_BOOST).shotlocks(ModShotlocks.PULSE_BOMB)
			.level(26).def(1).mag(1).abilities(ModAbilities.ITEM_BOOST, ModAbilities.WALL_KICK)
			.level(27).str(1).mag(1).ap(1).abilities(ModAbilities.MAGIC_LOCK_ON)
			.level(28).mag(1).maxMp(4).maxMagics(1).abilities(ModAbilities.EXPERIENCE_BOOST)
			.level(29).str(1).abilities(ModAbilities.AIR_SLIDE).shotlocks(ModShotlocks.DARK_VOLLEY)
			.level(30).def(1).mag(1).maxHp(5).ap(1).maxAccessories(1).abilities(ModAbilities.WATERZA)
			.level(31).str(1).abilities(ModAbilities.TREASURE_MAGNET)
			.level(32).str(1).mag(1).maxMp(4).maxMagics(1).abilities(ModAbilities.ENCOUNTER_PLUS)
			.level(33).str(1).ap(1).abilities(ModAbilities.STRIKE_RAID).shotlocks(ModShotlocks.LIGHTNING_RAY)
			.level(34).def(1).mag(1).abilities(ModAbilities.MP_HASTERA)
			.level(35).str(1).maxHp(5).maxArmors(1)
			.level(36).mag(1).maxMp(4).ap(1).maxMagics(1).abilities(ModAbilities.NEGATIVE_COMBO)
			.level(37).str(1).abilities(ModAbilities.FLOWSTEP)
			.level(38).def(1).mag(1).abilities(ModAbilities.EXTRA_CAST)
			.level(39).str(1).ap(1)
			.level(40).mag(1).maxHp(5).maxMp(4).maxMagics(1).maxAccessories(1).abilities(ModAbilities.LEAF_BRACER).shotlocks(ModShotlocks.BUBBLE_BLASTER)
			.level(41).str(1)
			.level(42).def(1).mag(1).ap(1)
			.level(43).str(1).mag(1)
			.level(44).mag(1).maxMp(4).abilities(ModAbilities.JACKPOT)
			.level(45).str(1).maxHp(5).ap(1).abilities(ModAbilities.AIR_SLIDE).shotlocks(ModShotlocks.FLAME_SALVO)
			.level(46).def(1).mag(1).abilities(ModAbilities.SUPERSLIDE)
			.level(47).str(1).abilities(ModAbilities.TREASURE_MAGNET)
			.level(48).str(1).mag(1).maxMp(4).ap(1).abilities(ModAbilities.BERSERK_CHARGE)
			.level(49).str(1)
			.level(50).def(1).mag(1).maxHp(5).abilities(ModAbilities.SYNCH_BLADE).shotlocks(ModShotlocks.PHOTON_CHARGE)
			.level(51).str(1).ap(1)
			.level(52).mag(1).maxMp(4)
			.level(53).str(1).abilities(ModAbilities.MP_HASTEGA)
			.level(54).def(1).mag(1).ap(1)
			.level(55).str(1).maxHp(5)
			.level(56).mag(1).maxMp(4).abilities(ModAbilities.SECOND_CHANCE)
			.level(57).str(1).ap(1)
			.level(58).def(1).mag(1).shotlocks(ModShotlocks.ABSOLUTE_ZERO)
			.level(59).str(1)
			.level(60).mag(1).maxHp(5).maxMp(4).ap(1)
			.level(61).str(1)
			.level(62).def(1).mag(1).abilities(ModAbilities.WALL_KICK)
			.level(63).str(1).ap(1)
			.level(64).mag(1).maxMp(4)
			.level(65).str(1).maxHp(5).shotlocks(ModShotlocks.SONIC_SHADOW)
			.level(66).def(1).mag(1).ap(1).abilities(ModAbilities.COMBO_PLUS)
			.level(67).str(1)
			.level(68).mag(1).maxMp(4)
			.level(69).str(1).ap(1).shotlocks(ModShotlocks.PRISM_RAIN)
			.level(70).def(1).mag(1).maxHp(5)
			.level(71).str(1)
			.level(72).mag(1).maxMp(4).ap(1).abilities(ModAbilities.JACKPOT)
			.level(73).str(1)
			.level(74).def(1).mag(1)
			.level(75).str(1).maxHp(5).ap(1).shotlocks(ModShotlocks.BIO_BARRAGE)
			.level(76).mag(1).maxMp(4)
			.level(77).str(1)
			.level(78).def(1).mag(1).ap(1).abilities(ModAbilities.COMBO_PLUS)
			.level(79).str(1)
			.level(80).mag(1).maxHp(5).maxMp(4)
			.level(81).str(1).ap(1)
			.level(82).def(1).mag(1)
			.level(83).str(1)
			.level(84).mag(1).maxMp(4).ap(1).abilities(ModAbilities.COMBO_PLUS)
			.level(85).str(1).maxHp(5).abilities(ModAbilities.FINISHING_PLUS).shotlocks(ModShotlocks.THUNDERSTORM)
			.level(86).def(1).mag(1)
			.level(87).str(1).ap(1)
			.level(88).mag(1).maxMp(4)
			.level(89).str(1)
			.level(90).def(1).mag(1).maxHp(5).ap(1)
			.level(91).str(1)
			.level(92).mag(1).maxMp(4)
			.level(93).str(1).ap(1)
			.level(94).def(1).mag(1)
			.level(95).str(1).maxHp(5)
			.level(96).mag(1).maxMp(4).ap(1).abilities(ModAbilities.COMBO_PLUS)
			.level(97).str(1)
			.level(98).def(1).mag(1).shotlocks(ModShotlocks.ULTIMA_CANNON, ModShotlocks.LIGHTBLOOM, ModShotlocks.MULTIVORTEX)
			.level(99).str(1).ap(1).abilities(ModAbilities.LUCKY_STRIKE)
			.level(100).str(10).def(10).mag(10).maxHp(5).maxMp(4)
			.build();
	}

	private static JsonObject buildGuardian() {
		return new LevelingBuilder()
			.level(0).def(-1)
			.level(1).def(1).ap(10).abilities(ModAbilities.ZERO_EXP)
			.level(2).def(1).abilities(ModAbilities.SCAN)
			.level(3).str(1).ap(1)
			.level(4).def(1).maxMp(8)
			.level(5).str(1).maxHp(5).maxArmors(1)
			.level(6).def(1).mag(1).ap(1).maxMagics(1)
			.level(7).str(1)
			.level(8).mag(1).maxMp(8)
			.level(9).str(1).ap(1)
			.level(10).def(1).mag(1).maxHp(5).maxAccessories(1).abilities(ModAbilities.GRAND_MAGIC_EXTENDER)
			.level(11).str(1).shotlocks(ModShotlocks.RAGNAROK)
			.level(12).mag(1).maxMp(8).ap(1).maxMagics(1).abilities(ModAbilities.MP_HASTE)
			.level(13).str(1).abilities(ModAbilities.ITEM_BOOST)
			.level(14).def(1).mag(1).abilities(ModAbilities.MP_SAFETY, ModAbilities.WALL_KICK)
			.level(15).str(1).maxHp(5).ap(1).maxArmors(1).abilities(ModAbilities.DAMAGE_DRIVE).shotlocks(ModShotlocks.METEOR_SHOWER)
			.level(16).mag(1).maxMp(8).abilities(ModAbilities.MP_RAGE)
			.level(17).str(1).abilities(ModAbilities.LEAF_BRACER)
			.level(18).def(1).mag(1).ap(1).maxMagics(1).abilities(ModAbilities.FOCUS_CONVERTER)
			.level(19).str(1).abilities(ModAbilities.FIRAZA)
			.level(20).mag(1).maxHp(5).maxMp(8).maxAccessories(1).abilities(ModAbilities.JACKPOT).shotlocks(ModShotlocks.CHAOS_SNAKE)
			.level(21).str(1).ap(1).abilities(ModAbilities.CURAZA)
			.level(22).def(1).mag(1).abilities(ModAbilities.FORM_BOOST)
			.level(23).str(1).abilities(ModAbilities.STRIKE_RAID)
			.level(24).mag(1).maxMp(4).ap(1).maxMagics(1).abilities(ModAbilities.DRIVE_CONVERTER, ModAbilities.AIR_SLIDE)
			.level(25).str(1).maxHp(5).maxArmors(1).abilities(ModAbilities.DRIVE_BOOST).shotlocks(ModShotlocks.PULSE_BOMB)
			.level(26).def(1).mag(1).abilities(ModAbilities.BLIZZAZA)
			.level(27).str(1).mag(1).ap(1).abilities(ModAbilities.GRAND_MAGIC_HASTE, ModAbilities.GRAND_MAGIC_EXTENDER)
			.level(28).mag(1).maxMp(4).abilities(ModAbilities.EXPERIENCE_BOOST)
			.level(29).str(1).abilities(ModAbilities.ENCOUNTER_PLUS).shotlocks(ModShotlocks.DARK_VOLLEY)
			.level(30).def(1).mag(1).maxHp(5).ap(1).maxMagics(1).maxAccessories(1).abilities(ModAbilities.THUNDAZA)
			.level(31).str(1).abilities(ModAbilities.TREASURE_MAGNET)
			.level(32).str(1).mag(1).maxMp(4).abilities(ModAbilities.ENDLESS_MAGIC)
			.level(33).str(1).ap(1).abilities(ModAbilities.WATERZA).shotlocks(ModShotlocks.LIGHTNING_RAY)
			.level(34).def(1).mag(1).abilities(ModAbilities.MP_HASTERA)
			.level(35).str(1).maxHp(5).maxArmors(1).abilities(ModAbilities.MAGIC_LOCK_ON)
			.level(36).mag(1).maxMp(4).ap(1).maxMagics(1).abilities(ModAbilities.BERSERK_CHARGE)
			.level(37).str(1)
			.level(38).def(1).mag(1).abilities(ModAbilities.EXTRA_CAST)
			.level(39).str(1).ap(1).abilities(ModAbilities.SUPERSLIDE)
			.level(40).mag(1).maxHp(5).maxMp(4).maxAccessories(1).abilities(ModAbilities.FLOWSTEP).shotlocks(ModShotlocks.BUBBLE_BLASTER)
			.level(41).str(1)
			.level(42).def(1).mag(1).ap(1).maxMagics(1).abilities(ModAbilities.COMBO_PLUS)
			.level(43).str(1).mag(1).abilities(ModAbilities.WALL_KICK)
			.level(44).mag(1).maxMp(4).abilities(ModAbilities.JACKPOT)
			.level(45).str(1).maxHp(5).ap(1).abilities(ModAbilities.AIR_SLIDE).shotlocks(ModShotlocks.FLAME_SALVO)
			.level(46).def(1).mag(1)
			.level(47).str(1).abilities(ModAbilities.TREASURE_MAGNET)
			.level(48).str(1).mag(1).maxMp(4).ap(1).maxMagics(1)
			.level(49).str(1)
			.level(50).def(1).mag(1).maxHp(5).abilities(ModAbilities.SYNCH_BLADE).shotlocks(ModShotlocks.PHOTON_CHARGE)
			.level(51).str(1).ap(1)
			.level(52).mag(1).maxMp(4)
			.level(53).str(1).abilities(ModAbilities.MP_HASTEGA, ModAbilities.LUCKY_STRIKE)
			.level(54).def(1).mag(1).ap(1).maxMagics(1)
			.level(55).str(1).maxHp(5)
			.level(56).mag(1).maxMp(4).abilities(ModAbilities.SECOND_CHANCE)
			.level(57).str(1).ap(1)
			.level(58).def(1).mag(1).shotlocks(ModShotlocks.ABSOLUTE_ZERO)
			.level(59).str(1)
			.level(60).mag(1).maxHp(5).maxMp(4).ap(1).maxMagics(1)
			.level(61).str(1)
			.level(62).def(1).mag(1)
			.level(63).str(1).ap(1)
			.level(64).mag(1).maxMp(4)
			.level(65).str(1).maxHp(5).abilities(ModAbilities.FINISHING_PLUS).shotlocks(ModShotlocks.SONIC_SHADOW)
			.level(66).def(1).mag(1).ap(1)
			.level(67).str(1)
			.level(68).mag(1).maxMp(4)
			.level(69).str(1).ap(1).shotlocks(ModShotlocks.PRISM_RAIN)
			.level(70).def(1).mag(1).maxHp(5)
			.level(71).str(1)
			.level(72).mag(1).maxMp(4).ap(1).abilities(ModAbilities.JACKPOT)
			.level(73).str(1).abilities(ModAbilities.NEGATIVE_COMBO)
			.level(74).def(1).mag(1)
			.level(75).str(1).maxHp(5).ap(1).abilities(ModAbilities.COMBO_PLUS).shotlocks(ModShotlocks.BIO_BARRAGE)
			.level(76).mag(1).maxMp(4)
			.level(77).str(1)
			.level(78).def(1).mag(1).ap(1)
			.level(79).str(1)
			.level(80).mag(1).maxHp(5).maxMp(4)
			.level(81).str(1).ap(1)
			.level(82).def(1).mag(1)
			.level(83).str(1)
			.level(84).mag(1).maxMp(4).ap(1).abilities(ModAbilities.COMBO_PLUS)
			.level(85).str(1).maxHp(5).shotlocks(ModShotlocks.THUNDERSTORM)
			.level(86).def(1).mag(1)
			.level(87).str(1).ap(1)
			.level(88).mag(1).maxMp(4)
			.level(89).str(1)
			.level(90).def(1).mag(1).maxHp(5).ap(1)
			.level(91).str(1)
			.level(92).mag(1).maxMp(4)
			.level(93).str(1).ap(1)
			.level(94).def(1).mag(1)
			.level(95).str(1).maxHp(5)
			.level(96).mag(1).maxMp(4).ap(1).abilities(ModAbilities.COMBO_PLUS)
			.level(97).str(1)
			.level(98).def(1).mag(1).shotlocks(ModShotlocks.ULTIMA_CANNON, ModShotlocks.LIGHTBLOOM, ModShotlocks.MULTIVORTEX)
			.level(99).str(1).ap(1)
			.level(100).str(10).def(10).mag(10).maxHp(5).maxMp(4)
			.build();
	}

}
package online.kingdomkeys.kingdomkeys.datagen.provider;

import com.google.gson.JsonObject;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.ability.Ability.AbilityType;
import online.kingdomkeys.kingdomkeys.datagen.builder.AbilityBuilder;
import online.kingdomkeys.kingdomkeys.lib.Strings;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Writes data/kingdomkeys/abilities/. These values used to live in the ModAbilities registration
 * calls; the registration still carries them as the fallback for before this pack is read, so the two
 * should be kept in step when adding an ability.
 *
 * <p>The order decides where an ability sits in the menu within its type, and abilities sharing an
 * exclusion group can't be equipped together.</p>
 */
public class AbilityDataProvider implements DataProvider {

	private final PackOutput.PathProvider pathProvider;

	public AbilityDataProvider(PackOutput output) {
		this.pathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, "abilities");
	}

	@Override
	public CompletableFuture<?> run(CachedOutput cache) {
		Map<String, JsonObject> abilities = new LinkedHashMap<>();
		int order = 0;
		abilities.put(KingdomKeys.rl(Strings.autoValor).getPath(), new AbilityBuilder().apCost(1).type(AbilityType.ACTION).order(order++).build());
		abilities.put(KingdomKeys.rl(Strings.autoWisdom).getPath(), new AbilityBuilder().apCost(1).type(AbilityType.ACTION).order(order++).build());
		abilities.put(KingdomKeys.rl(Strings.autoLimit).getPath(), new AbilityBuilder().apCost(1).type(AbilityType.ACTION).order(order++).build());
		abilities.put(KingdomKeys.rl(Strings.autoMaster).getPath(), new AbilityBuilder().apCost(1).type(AbilityType.ACTION).order(order++).build());
		abilities.put(KingdomKeys.rl(Strings.autoFinal).getPath(), new AbilityBuilder().apCost(1).type(AbilityType.ACTION).order(order++).build());
		abilities.put(KingdomKeys.rl(Strings.strikeRaid).getPath(), new AbilityBuilder().apCost(3).type(AbilityType.ACTION).order(order++).build());
		abilities.put(KingdomKeys.rl(Strings.guard).getPath(), new AbilityBuilder().apCost(2).type(AbilityType.ACTION).order(order++).build());
		abilities.put(KingdomKeys.rl(Strings.counterguard).getPath(), new AbilityBuilder().apCost(3).type(AbilityType.ACTION).order(order++).build());
		abilities.put(KingdomKeys.rl(Strings.flowStep).getPath(), new AbilityBuilder().apCost(4).type(AbilityType.ACTION).order(order++).build());

		abilities.put(KingdomKeys.rl(Strings.highJump).getPath(), new AbilityBuilder().apCost(2).type(AbilityType.GROWTH).order(order++).build());
		abilities.put(KingdomKeys.rl(Strings.quickRun).getPath(), new AbilityBuilder().apCost(2).type(AbilityType.GROWTH).order(order++).build());
		abilities.put(KingdomKeys.rl(Strings.dodgeRoll).getPath(), new AbilityBuilder().apCost(3).type(AbilityType.GROWTH).order(order++).build());
		abilities.put(KingdomKeys.rl(Strings.aerialDodge).getPath(), new AbilityBuilder().apCost(3).type(AbilityType.GROWTH).order(order++).build());
		abilities.put(KingdomKeys.rl(Strings.glide).getPath(), new AbilityBuilder().apCost(3).type(AbilityType.GROWTH).order(order++).build());

		abilities.put(KingdomKeys.rl(Strings.airSlide).getPath(), new AbilityBuilder().apCost(3).type(AbilityType.GROWTH_STACKABLE).order(order++).build());
		abilities.put(KingdomKeys.rl(Strings.wallKick).getPath(), new AbilityBuilder().apCost(2).type(AbilityType.GROWTH_STACKABLE).order(order++).build());
		abilities.put(KingdomKeys.rl(Strings.superJump).getPath(), new AbilityBuilder().apCost(3).type(AbilityType.GROWTH_STACKABLE).order(order++).build());
		abilities.put(KingdomKeys.rl(Strings.superSlide).getPath(), new AbilityBuilder().apCost(2).type(AbilityType.GROWTH_STACKABLE).order(order++).build());

		abilities.put(KingdomKeys.rl(Strings.zeroExp).getPath(), new AbilityBuilder().apCost(0).type(AbilityType.SUPPORT).order(order++).build());
		abilities.put(KingdomKeys.rl(Strings.scan).getPath(), new AbilityBuilder().apCost(1).type(AbilityType.SUPPORT).order(order++).build());
		abilities.put(KingdomKeys.rl(Strings.aerialRecovery).getPath(), new AbilityBuilder().apCost(2).type(AbilityType.SUPPORT).order(order++).build());
		abilities.put(KingdomKeys.rl(Strings.magicLockOn).getPath(), new AbilityBuilder().apCost(4).type(AbilityType.SUPPORT).order(order++).build());
		abilities.put(KingdomKeys.rl(Strings.leafBracer).getPath(), new AbilityBuilder().apCost(1).type(AbilityType.SUPPORT).order(order++).build());
		abilities.put(KingdomKeys.rl(Strings.hpGain).getPath(), new AbilityBuilder().apCost(6).type(AbilityType.SUPPORT).order(order++).build());
		abilities.put(KingdomKeys.rl(Strings.mpSafety).getPath(), new AbilityBuilder().apCost(0).type(AbilityType.SUPPORT).order(order++).build());
		abilities.put(KingdomKeys.rl(Strings.mpHaste).getPath(), new AbilityBuilder().apCost(3).type(AbilityType.SUPPORT).order(order++).build());
		abilities.put(KingdomKeys.rl(Strings.mpHastera).getPath(), new AbilityBuilder().apCost(4).type(AbilityType.SUPPORT).order(order++).build());
		abilities.put(KingdomKeys.rl(Strings.mpHastega).getPath(), new AbilityBuilder().apCost(5).type(AbilityType.SUPPORT).order(order++).build());
		abilities.put(KingdomKeys.rl(Strings.mpRage).getPath(), new AbilityBuilder().apCost(3).type(AbilityType.SUPPORT).order(order++).build());
		abilities.put(KingdomKeys.rl(Strings.endlessMagic).getPath(), new AbilityBuilder().apCost(4).type(AbilityType.SUPPORT).order(order++).build());
		abilities.put(KingdomKeys.rl(Strings.damageDrive).getPath(), new AbilityBuilder().apCost(3).type(AbilityType.SUPPORT).order(order++).build());
		abilities.put(KingdomKeys.rl(Strings.driveConverter).getPath(), new AbilityBuilder().apCost(4).type(AbilityType.SUPPORT).order(order++).build());
		abilities.put(KingdomKeys.rl(Strings.focusConverter).getPath(), new AbilityBuilder().apCost(3).type(AbilityType.SUPPORT).order(order++).build());
		abilities.put(KingdomKeys.rl(Strings.driveBoost).getPath(), new AbilityBuilder().apCost(3).type(AbilityType.SUPPORT).order(order++).build());
		abilities.put(KingdomKeys.rl(Strings.formBoost).getPath(), new AbilityBuilder().apCost(3).type(AbilityType.SUPPORT).order(order++).build());
		abilities.put(KingdomKeys.rl(Strings.fullMPBlast).getPath(), new AbilityBuilder().apCost(2).type(AbilityType.SUPPORT).order(order++).build());
		abilities.put(KingdomKeys.rl(Strings.mpThrift).getPath(), new AbilityBuilder().apCost(2).type(AbilityType.SUPPORT).order(order++).build());
		abilities.put(KingdomKeys.rl(Strings.luckyStrike).getPath(), new AbilityBuilder().apCost(5).type(AbilityType.SUPPORT).order(order++).build());
		abilities.put(KingdomKeys.rl(Strings.jackpot).getPath(), new AbilityBuilder().apCost(4).type(AbilityType.SUPPORT).order(order++).build());
		abilities.put(KingdomKeys.rl(Strings.fireBoost).getPath(), new AbilityBuilder().apCost(3).type(AbilityType.SUPPORT).order(order++).build());
		abilities.put(KingdomKeys.rl(Strings.blizzardBoost).getPath(), new AbilityBuilder().apCost(4).type(AbilityType.SUPPORT).order(order++).build());
		abilities.put(KingdomKeys.rl(Strings.waterBoost).getPath(), new AbilityBuilder().apCost(4).type(AbilityType.SUPPORT).order(order++).build());
		abilities.put(KingdomKeys.rl(Strings.thunderBoost).getPath(), new AbilityBuilder().apCost(5).type(AbilityType.SUPPORT).order(order++).build());
		abilities.put(KingdomKeys.rl(Strings.experienceBoost).getPath(), new AbilityBuilder().apCost(4).type(AbilityType.SUPPORT).order(order++).build());
		abilities.put(KingdomKeys.rl(Strings.encounterPlus).getPath(), new AbilityBuilder().apCost(5).type(AbilityType.SUPPORT).order(order++).build());
		abilities.put(KingdomKeys.rl(Strings.criticalBoost).getPath(), new AbilityBuilder().apCost(3).type(AbilityType.SUPPORT).order(order++).build());
		abilities.put(KingdomKeys.rl(Strings.itemBoost).getPath(), new AbilityBuilder().apCost(2).type(AbilityType.SUPPORT).order(order++).build());
		abilities.put(KingdomKeys.rl(Strings.treasureMagnet).getPath(), new AbilityBuilder().apCost(3).type(AbilityType.SUPPORT).order(order++).build());
		abilities.put(KingdomKeys.rl(Strings.secondChance).getPath(), new AbilityBuilder().apCost(4).type(AbilityType.SUPPORT).order(order++).build());
		abilities.put(KingdomKeys.rl(Strings.onceMore).getPath(), new AbilityBuilder().apCost(4).type(AbilityType.SUPPORT).order(order++).build());
		abilities.put(KingdomKeys.rl(Strings.wizardsRuse).getPath(), new AbilityBuilder().apCost(4).type(AbilityType.SUPPORT).order(order++).build());
		abilities.put(KingdomKeys.rl(Strings.extraCast).getPath(), new AbilityBuilder().apCost(3).type(AbilityType.SUPPORT).order(order++).build());
		abilities.put(KingdomKeys.rl(Strings.damageControl).getPath(), new AbilityBuilder().apCost(5).type(AbilityType.SUPPORT).order(order++).build());
		abilities.put(KingdomKeys.rl(Strings.lightAndDarkness).getPath(), new AbilityBuilder().apCost(2).type(AbilityType.SUPPORT).order(order++).build());
		abilities.put(KingdomKeys.rl(Strings.synchBlade).getPath(), new AbilityBuilder().apCost(5).type(AbilityType.SUPPORT).order(order++).build());
		abilities.put(KingdomKeys.rl(Strings.grandMagicHaste).getPath(), new AbilityBuilder().apCost(3).type(AbilityType.SUPPORT).order(order++).build());
		abilities.put(KingdomKeys.rl(Strings.grandMagicExtender).getPath(), new AbilityBuilder().apCost(3).type(AbilityType.SUPPORT).order(order++).build());
		abilities.put(KingdomKeys.rl(Strings.comboPlus).getPath(), new AbilityBuilder().apCost(1).type(AbilityType.SUPPORT).order(order++).build());
		abilities.put(KingdomKeys.rl(Strings.negativeCombo).getPath(), new AbilityBuilder().apCost(1).type(AbilityType.SUPPORT).order(order++).build());
		abilities.put(KingdomKeys.rl(Strings.finishingPlus).getPath(), new AbilityBuilder().apCost(1).type(AbilityType.SUPPORT).order(order++).build());
		abilities.put(KingdomKeys.rl(Strings.berserkCharge).getPath(), new AbilityBuilder().apCost(5).type(AbilityType.SUPPORT).order(order++).build());
		abilities.put(KingdomKeys.rl(Strings.darkDomination).getPath(), new AbilityBuilder().apCost(5).type(AbilityType.SUPPORT).order(order++).build());
		abilities.put(KingdomKeys.rl(Strings.protect).getPath(), new AbilityBuilder().apCost(2).type(AbilityType.SUPPORT).order(order++).build());
		abilities.put(KingdomKeys.rl(Strings.protectra).getPath(), new AbilityBuilder().apCost(4).type(AbilityType.SUPPORT).order(order++).build());
		abilities.put(KingdomKeys.rl(Strings.protectga).getPath(), new AbilityBuilder().apCost(6).type(AbilityType.SUPPORT).order(order++).build());
		abilities.put(KingdomKeys.rl(Strings.firaza).getPath(), new AbilityBuilder().apCost(3).type(AbilityType.SUPPORT).order(order++).build());
		abilities.put(KingdomKeys.rl(Strings.blizzaza).getPath(), new AbilityBuilder().apCost(3).type(AbilityType.SUPPORT).order(order++).build());
		abilities.put(KingdomKeys.rl(Strings.waterza).getPath(), new AbilityBuilder().apCost(3).type(AbilityType.SUPPORT).order(order++).build());
		abilities.put(KingdomKeys.rl(Strings.thundaza).getPath(), new AbilityBuilder().apCost(3).type(AbilityType.SUPPORT).order(order++).build());
		abilities.put(KingdomKeys.rl(Strings.curaza).getPath(), new AbilityBuilder().apCost(3).type(AbilityType.SUPPORT).order(order++).build());

		CompletableFuture<?>[] futures = abilities.entrySet().stream().map(entry -> {
			Path path = pathProvider.json(KingdomKeys.rl(entry.getKey()));
			return DataProvider.saveStable(cache, entry.getValue(), path);
		}).toArray(CompletableFuture[]::new);
		return CompletableFuture.allOf(futures);
	}

	@Override
	public String getName() {
		return "Kingdom Keys Ability Data";
	}
}

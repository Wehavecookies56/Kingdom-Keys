package online.kingdomkeys.kingdomkeys.datagen.init;

import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.datagen.builder.KeybladeBuilder;
import online.kingdomkeys.kingdomkeys.datagen.provider.KeybladeProvider;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static online.kingdomkeys.kingdomkeys.KingdomKeys.MODID;

public class KeybladeStats extends KeybladeProvider {
	public KeybladeStats(DataGenerator generator, ExistingFileHelper existingFileHelper) {
		super(generator, KingdomKeys.MODID, KeybladeBuilder::new, existingFileHelper);
	}

	@Override
	protected void registerKeyblades() {
		addStats(Strings.abaddonPlasma, Strings.abaddonPlasmaChain, 7, 3, null, 1.0F, "item." + MODID + "." + Strings.abaddonPlasma + ".desc");
		addStats(Strings.abyssalTide, Strings.abyssalTideChain, 5, 2, ModAbilities.WATER_BOOST.location(), 1.0F, "item." + MODID + "." + Strings.abyssalTide + ".desc");
		addStats(Strings.acedsKeyblade, Strings.acedsKeybladeChain, 8, 7, null, 1.0F, "item." + MODID + "." + Strings.acedsKeyblade + ".desc");
		addStats(Strings.adventRed, Strings.adventRedChain, 4, 4, null, 1.0F, "item." + MODID + "." + Strings.adventRed + ".desc");
		addStats(Strings.allForOne, Strings.allForOneChain, 6, 7, null, 1.0F, "item." + MODID + "." + Strings.allForOne + ".desc");
		addStats(Strings.astralBlast, Strings.astralBlastChain, 8, 4, null, 1.0F, "item." + MODID + "." + Strings.astralBlast + ".desc");
		addStats(Strings.aubade, Strings.aubadeChain, 8, 4, null, 1.0F, "item." + MODID + "." + Strings.aubade + ".desc");
		addStats(Strings.avasKeyblade, Strings.avasKeybladeChain, 8, 7, null, 1.0F, "item." + MODID + "." + Strings.avasKeyblade + ".desc");
		addStats(Strings.bondOfFlame, Strings.bondOfFlameChain, 6, 6, ModAbilities.FIRE_BOOST.location(), 1.0F, "item." + MODID + "." + Strings.bondOfFlame + ".desc");
		addStats(Strings.bondOfTheBlaze, Strings.bondOfTheBlazeChain, 6, 7, ModAbilities.FIRE_BOOST.location(), 1.0F, "item." + MODID + "." + Strings.bondOfTheBlaze + ".desc");
		addStats(Strings.braveheart, Strings.braveheartChain, 5, 4, null, 1.0F, "item." + MODID + "." + Strings.braveheart + ".desc");
		addStats(Strings.brightcrest, Strings.brightcrestChain, 5, 6, ModAbilities.FULL_MP_BLAST.location(), 2.0F, "item." + MODID + "." + Strings.brightcrest + ".desc");
		addStats(Strings.chaosRipper, Strings.chaosRipperChain, 9, 0, null, 6.5F, "item." + MODID + "." + Strings.chaosRipper + ".desc");
		addStats(Strings.circleOfLife, Strings.circleOfLifeChain, 6, 2, ModAbilities.MP_HASTE.location(), 1.0F, "item." + MODID + "." + Strings.circleOfLife + ".desc");
		addStats(Strings.classicTone, Strings.classicToneChain, 6, 9, ModAbilities.MP_HASTE.location(), 1.0F, "item." + MODID + "." + Strings.classicTone + ".desc");
		addStats(Strings.counterpoint, Strings.counterpointChain, 7, 7, ModAbilities.ENDLESS_MAGIC.location(), 2.0F, "item." + MODID + "." + Strings.counterpoint + ".desc");
		addStats(Strings.crabclaw, Strings.crabclawChain, 5, 3, ModAbilities.WATER_BOOST.location(), 1.0F, "item." + MODID + "." + Strings.crabclaw + ".desc");
		addStats(Strings.crownOfGuilt, Strings.crownOfGuiltChain, 6, 7, null, 1.0F, "item." + MODID + "." + Strings.crownOfGuilt + ".desc");
		addStats(Strings.crystalSnow, Strings.crystalSnowChain, 3, 6, ModAbilities.BLIZZARD_BOOST.location(), 1.0F, "item." + MODID + "." + Strings.crystalSnow + ".desc");
		addStats(Strings.darkerThanDark, Strings.darkerThanDarkChain, 7, 8, null, 1.0F, "item." + MODID + "." + Strings.darkerThanDark + ".desc");
		addStats(Strings.darkgnaw, Strings.darkgnawChain, 6, 4, null, 1.0F, "item." + MODID + "." + Strings.darkgnaw + ".desc");
		addStats(Strings.dawnTillDusk, Strings.dawnTillDuskChain, 4, 4, null, 1.0F, "item." + MODID + "." + Strings.dawnTillDusk + ".desc");
		addStats(Strings.deadOfNight, Strings.deadOfNightChain, 4, 4, ModAbilities.GRAND_MAGIC_HASTE.location(), 1.0F, "item." + MODID + "." + Strings.deadOfNight + ".desc");
		addStats(Strings.decisivePumpkin, Strings.decisivePumpkinChain, 8, 3, null, 1.0F, "item." + MODID + "." + Strings.decisivePumpkin + ".desc");
		addStats(Strings.destinysEmbrace, Strings.destinysEmbraceChain, 4, 4, null, 1.0F, "item." + MODID + "." + Strings.destinysEmbrace + ".desc");
		addStats(Strings.diamondDust, Strings.diamondDustChain, 4, 9, ModAbilities.BLIZZARD_BOOST.location(), 1.0F, "item." + MODID + "." + Strings.diamondDust + ".desc");
		addStats(Strings.divewing, Strings.divewingChain, 7, 8, null, 1.0F, "item." + MODID + "." + Strings.divewing + ".desc");
		addStats(Strings.divineRose, Strings.divineRoseChain, 8, 2, null, 1.0F, "item." + MODID + "." + Strings.divineRose + ".desc");
		addStats(Strings.dualDisc, Strings.dualDiscChain, 6, 4, null, 1.0F, "item." + MODID + "." + Strings.dualDisc + ".desc");
		addStats(Strings.earthshaker, Strings.earthshakerChain, 4, 3, null, 1.0F, "item." + MODID + "." + Strings.earthshaker + ".desc");
		addStats(Strings.elementalEncoder, Strings.elementalEncoderChain, 4, 4, ModAbilities.GRAND_MAGIC_HASTE.location(), 1.0F, "item." + MODID + "." + Strings.elementalEncoder + ".desc");
		addStats(Strings.endOfPain, Strings.endOfPainChain, 8, 8, null, 1.0F, "item." + MODID + "." + Strings.endOfPain + ".desc");
		addStats(Strings.endsOfTheEarth, Strings.endsOfTheEarthChain, 6, 5, null, 1.0F, "item." + MODID + "." + Strings.endsOfTheEarth + ".desc");
		addStats(Strings.everAfter, Strings.everAfterChain, 4, 6, ModAbilities.LEAF_BRACER.location(), 1.0F, "item." + MODID + "." + Strings.everAfter + ".desc");
		addStats(Strings.fairyHarp, Strings.fairyHarpChain, 6, 4, null, 1.0F, "item." + MODID + "." + Strings.fairyHarp + ".desc");
		addStats(Strings.fairyStars, Strings.fairyStarsChain, 4, 5, ModAbilities.ENDLESS_MAGIC.location(), 1.0F, "item." + MODID + "." + Strings.fairyStars + ".desc");
		addStats(Strings.fatalCrest, Strings.fatalCrestChain, 5, 7, ModAbilities.BERSERK_CHARGE.location(), 1.0F, "item." + MODID + "." + Strings.fatalCrest + ".desc");
		addStats(Strings.favoriteDeputy, Strings.favoriteDeputyChain, 5, 3, null, 1.0F, "item." + MODID + "." + Strings.favoriteDeputy + ".desc");
		addStats(Strings.fenrir, Strings.fenrirChain, 9, 2, ModAbilities.NEGATIVE_COMBO.location(), 1.0F, "item." + MODID + "." + Strings.fenrir + ".desc");
		addStats(Strings.ferrisGear, Strings.ferrisGearChain, 5, 5, null, 1.0F, "item." + MODID + "." + Strings.ferrisGear + ".desc");
		addStats(Strings.followTheWind, Strings.followTheWindChain, 4, 2, ModAbilities.TREASURE_MAGNET.location(), 1.0F, "item." + MODID + "." + Strings.followTheWind + ".desc");
		addStats(Strings.frolicFlame, Strings.frolicFlameChain, 5, 5, ModAbilities.FIRE_BOOST.location(), 1.0F, "item." + MODID + "." + Strings.frolicFlame + ".desc");
		addStats(Strings.glimpseOfDarkness, Strings.glimpseOfDarknessChain, 7, 3, null, 1.0F, "item." + MODID + "." + Strings.glimpseOfDarkness + ".desc");
		addStats(Strings.grandChef, Strings.grandChefChain, 4, 6, ModAbilities.FIRE_BOOST.location(), 1.0F, "item." + MODID + "." + Strings.grandChef + ".desc");
		addStats(Strings.guardianBell, Strings.guardianBellChain, 4, 5, null, 1.5F, "item." + MODID + "." + Strings.guardianBell + ".desc");
		addStats(Strings.guardianSoul, Strings.guardianSoulChain, 7, 4, null, 1.0F, "item." + MODID + "." + Strings.guardianSoul + ".desc");
		addStats(Strings.gulasKeyblade, Strings.gulasKeybladeChain, 8, 7, null, 1.0F, "item." + MODID + "." + Strings.gulasKeyblade + ".desc");
		addStats(Strings.gullWing, Strings.gullWingChain, 4, 5, ModAbilities.EXPERIENCE_BOOST.location(), 1.0F, "item." + MODID + "." + Strings.gullWing + ".desc");
		addStats(Strings.happyGear, Strings.happyGearChain, 6, 2, ModAbilities.FULL_MP_BLAST.location(), 2.0F, "item." + MODID + "." + Strings.happyGear + ".desc");
		addStats(Strings.herosCrest, Strings.herosCrestChain, 6, 1, null, 1.0F, "item." + MODID + "." + Strings.herosCrest + ".desc");
		addStats(Strings.herosOrigin, Strings.herosOriginChain, 5, 2, null, 1.0F, "item." + MODID + "." + Strings.herosOrigin + ".desc");
		addStats(Strings.hiddenDragon, Strings.hiddenDragonChain, 4, 4, ModAbilities.MP_RAGE.location(), 1.0F, "item." + MODID + "." + Strings.hiddenDragon + ".desc");
		addStats(Strings.hunnySpout, Strings.hunnySpoutChain, 4, 4, ModAbilities.MP_HASTE.location(), 1.0F, "item." + MODID + "." + Strings.hunnySpout + ".desc");
		addStats(Strings.hyperdrive, Strings.hyperdriveChain, 5, 5, null, 1.0F, "item." + MODID + "." + Strings.hyperdrive + ".desc");
		addStats(Strings.incompleteKiblade, Strings.incompleteKibladeChain, 5, 3, ModAbilities.LIGHT_AND_DARKNESS.location(), 2.0F, "item." + MODID + "." + Strings.incompleteKiblade + ".desc");
		addStats(Strings.invisKeyblade, Strings.invisKeybladeChain, 8, 7, null, 1.0F, "item." + MODID + "." + Strings.invisKeyblade + ".desc");
		addStats(Strings.irasKeyblade, Strings.irasKeybladeChain, 8, 7, null, 1.0F, "item." + MODID + "." + Strings.irasKeyblade + ".desc");
		addStats(Strings.jungleKing, Strings.jungleKingChain, 5, 2, null, 2.0F, "item." + MODID + "." + Strings.jungleKing + ".desc");
		addStats(Strings.keybladeOfPeoplesHearts, Strings.keybladeOfPeoplesHeartsChain, 6, 2, null, 1.0F, "item." + MODID + "." + Strings.keybladeOfPeoplesHearts + ".desc");
		addStats(Strings.kiblade, Strings.kibladeChain, 10, 7, ModAbilities.DARK_DOMINATION.location(), 2.0F, "item." + MODID + "." + Strings.kiblade + ".desc");
		addStats(Strings.kingdomKey, Strings.kingdomKeyChain, 4, 1, ModAbilities.DAMAGE_CONTROL.location(), 1.0F, "item." + MODID + "." + Strings.kingdomKey + ".desc");
		addStats(Strings.kingdomKeyD, Strings.kingdomKeyDChain, 4, 1, null, 1.0F, "item." + MODID + "." + Strings.kingdomKeyD + ".desc");
		addStats(Strings.kingdomKeyN, Strings.kingdomKeyNChain, 4, 1, ModAbilities.BERSERK_CHARGE.location(), 1.0F, "item." + MODID + "." + Strings.kingdomKeyN + ".desc");
		addStats(Strings.knockoutPunch, Strings.knockoutPunchChain, 7, 5, null, 1.0F, "item." + MODID + "." + Strings.knockoutPunch + ".desc");
		addStats(Strings.ladyLuck, Strings.ladyLuckChain, 6, 5, ModAbilities.LUCKY_STRIKE.location(), 1.0F, "item." + MODID + "." + Strings.ladyLuck + ".desc");
		addStats(Strings.leviathan, Strings.leviathanChain, 5, 2, ModAbilities.WATER_BOOST.location(), 1.0F, "item." + MODID + "." + Strings.leviathan + ".desc");
		addStats(Strings.lionheart, Strings.lionheartChain, 7, 3, null, 1.0F, "item." + MODID + "." + Strings.lionheart + ".desc");
		addStats(Strings.lostMemory, Strings.lostMemoryChain, 7, 6, null, 1.5F, "item." + MODID + "." + Strings.lostMemory + ".desc");
		addStats(Strings.lunarEclipse, Strings.lunarEclipseChain, 9, 7, null, 1.0F, "item." + MODID + "." + Strings.lunarEclipse + ".desc");
		addStats(Strings.markOfAHero, Strings.markOfAHeroChain, 6, 3, null, 1.0F, "item." + MODID + "." + Strings.markOfAHero + ".desc");
		addStats(Strings.mastersDefender, Strings.mastersDefenderChain, 8, 7, ModAbilities.PROTECTGA.location(), 1.0F, "item." + MODID + "." + Strings.mastersDefender + ".desc");
		addStats(Strings.maverickFlare, Strings.maverickFlareChain, 8, 4, ModAbilities.FIRE_BOOST.location(), 1.0F, "item." + MODID + "." + Strings.maverickFlare + ".desc");
		addStats(Strings.metalChocobo, Strings.metalChocoboChain, 7, 1, null, 1.0F, "item." + MODID + "." + Strings.metalChocobo + ".desc");
		addStats(Strings.midnightBlue, Strings.midnightBlueChain, 4, 4, null, 1.0F, "item." + MODID + "." + Strings.midnightBlue + ".desc");
		addStats(Strings.midnightRoar, Strings.midnightRoarChain, 7, 4, null, 1.0F, "item." + MODID + "." + Strings.midnightRoar + ".desc");
		addStats(Strings.mirageSplit, Strings.mirageSplitChain, 7, 7, ModAbilities.WIZARDS_RUSE.location(), 1.0F, "item." + MODID + "." + Strings.mirageSplit + ".desc");
		addStats(Strings.missingAche, Strings.missingAcheChain, 4, 1, null, 1.0F, "item." + MODID + "." + Strings.missingAche + ".desc");
		addStats(Strings.monochrome, Strings.monochromeChain, 4, 3, null, 1.0F, "item." + MODID + "." + Strings.monochrome + ".desc");
		addStats(Strings.moogleOGlory, Strings.moogleOGloryChain, 8, 7, null, 1.0F, "item." + MODID + "." + Strings.moogleOGlory + ".desc");
		addStats(Strings.mysteriousAbyss, Strings.mysteriousAbyssChain, 5, 5, ModAbilities.BLIZZARD_BOOST.location(), 1.0F, "item." + MODID + "." + Strings.mysteriousAbyss + ".desc");
		addStats(Strings.nanoGear, Strings.nanoGearChain, 5, 4, null, 1.0F, "item." + MODID + "." + Strings.nanoGear + ".desc");
		addStats(Strings.nightmaresEnd, Strings.nightmaresEndChain, 7, 7, ModAbilities.WIZARDS_RUSE.location(), 1.0F, "item." + MODID + "." + Strings.nightmaresEnd + ".desc");
		addStats(Strings.nightmaresEndAndMirageSplit, Strings.nightmaresEndAndMirageSplitChain, 9, 8, ModAbilities.WIZARDS_RUSE.location(), 1.0F, "item." + MODID + "." + Strings.nightmaresEndAndMirageSplit + ".desc");
		addStats(Strings.noName, Strings.noNameChain, 9, 7, null, 1.0F, "item." + MODID + "." + Strings.noName + ".desc");
		addStats(Strings.noNameBBS, Strings.noNameBBSChain, 7, 7, null, 2.0F, "item." + MODID + "." + Strings.noNameBBS + ".desc");
		addStats(Strings.oathkeeper, Strings.oathkeeperChain, 7, 7, ModAbilities.FORM_BOOST.location(), 1.0F, "item." + MODID + "." + Strings.oathkeeper + ".desc");
		addStats(Strings.oblivion, Strings.oblivionChain, 8, 5, ModAbilities.DRIVE_BOOST.location(), 1.0F, "item." + MODID + "." + Strings.oblivion + ".desc");
		addStats(Strings.oceansRage, Strings.oceansRageChain, 5, 5, ModAbilities.BLIZZARD_BOOST.location(), 5.0F, "item." + MODID + "." + Strings.oceansRage + ".desc");
		addStats(Strings.olympia, Strings.olympiaChain, 7, 1, null, 1.0F, "item." + MODID + "." + Strings.olympia + ".desc");
		addStats(Strings.omegaWeapon, Strings.omegaWeaponChain, 8, 7, null, 1.0F, "item." + MODID + "." + Strings.omegaWeapon + ".desc");
		addStats(Strings.ominousBlight, Strings.ominousBlightChain, 4, 2, null, 1.0F, "item." + MODID + "." + Strings.ominousBlight + ".desc");
		addStats(Strings.oneWingedAngel, Strings.oneWingedAngelChain, 6, 0, null, 1.5F, "item." + MODID + "." + Strings.oneWingedAngel + ".desc");
		addStats(Strings.painOfSolitude, Strings.painOfSolitudeChain, 4, 5, null, 1.0F, "item." + MODID + "." + Strings.painOfSolitude + ".desc");
		addStats(Strings.phantomGreen, Strings.phantomGreenChain, 4, 4, null, 1.0F, "item." + MODID + "." + Strings.phantomGreen + ".desc");
		addStats(Strings.photonDebugger, Strings.photonDebuggerChain, 5, 3, ModAbilities.THUNDER_BOOST.location(), 1.0F, "item." + MODID + "." + Strings.photonDebugger + ".desc");
		addStats(Strings.pixiePetal, Strings.pixiePetalChain, 4, 8, ModAbilities.LEAF_BRACER.location(), 1.0F, "item." + MODID + "." + Strings.pixiePetal + ".desc");
		addStats(Strings.pumpkinhead, Strings.pumpkinheadChain, 6, 2, ModAbilities.CRITICAL_BOOST.location(), 2.0F, "item." + MODID + "." + Strings.pumpkinhead + ".desc");
		addStats(Strings.rainfell, Strings.rainfellChain, 4, 4, null, 1.0F, "item." + MODID + "." + Strings.rainfell + ".desc");
		addStats(Strings.rejectionOfFate, Strings.rejectionOfFateChain, 5, 2, null, 1.0F, "item." + MODID + "." + Strings.rejectionOfFate + ".desc");
		addStats(Strings.royalRadiance, Strings.royalRadianceChain, 9, 8, ModAbilities.ENDLESS_MAGIC.location(), 1.0F, "item." + MODID + "." + Strings.royalRadiance + ".desc");
		addStats(Strings.rumblingRose, Strings.rumblingRoseChain, 7, 3, null, 1.0F, "item." + MODID + "." + Strings.rumblingRose + ".desc");
		addStats(Strings.shootingStar, Strings.shootingStarChain, 4, 6, null, 1.0F, "item." + MODID + "." + Strings.shootingStar + ".desc");
		addStats(Strings.signOfInnocence, Strings.signOfInnocenceChain, 6, 5, null, 1.0F, "item." + MODID + "." + Strings.signOfInnocence + ".desc");
		addStats(Strings.silentDirge, Strings.silentDirgeChain, 6, 6, null, 1.0F, "item." + MODID + "." + Strings.silentDirge + ".desc");
		addStats(Strings.skullNoise, Strings.skullNoiseChain, 4, 4, null, 1.0F, "item." + MODID + "." + Strings.skullNoise + ".desc");
		addStats(Strings.sleepingLion, Strings.sleepingLionChain, 7, 5, null, 1.0F, "item." + MODID + "." + Strings.sleepingLion + ".desc");
		addStats(Strings.soulEater, Strings.soulEaterChain, 4, 1, null, 1.0F, "item." + MODID + "." + Strings.soulEater + ".desc");
		addStats(Strings.spellbinder, Strings.spellbinderChain, 4, 8, ModAbilities.FULL_MP_BLAST.location(), 1.0F, "item." + MODID + "." + Strings.spellbinder + ".desc");
		addStats(Strings.starCluster, Strings.starClusterChain, 5, 6, null, 1.0F, "item." + MODID + "." + Strings.starCluster + ".desc");
		addStats(Strings.starSeeker, Strings.starSeekerChain, 4, 1, null, 1.0F, "item." + MODID + "." + Strings.starSeeker + ".desc");
		addStats(Strings.starlight, Strings.starlightChain, 5, 5, ModAbilities.MP_HASTE.location(), 1.0F, "item." + MODID + "." + Strings.starlight + ".desc");
		addStats(Strings.stormfall, Strings.stormfallChain, 6, 5, null, 1.0F, "item." + MODID + "." + Strings.stormfall + ".desc");
		addStats(Strings.strokeOfMidnight, Strings.strokeOfMidnightChain, 4, 4, null, 1.0F, "item." + MODID + "." + Strings.strokeOfMidnight + ".desc");
		addStats(Strings.sweetDreams, Strings.sweetDreamsChain, 8, 6, null, 2.0F, "item." + MODID + "." + Strings.sweetDreams + ".desc");
		addStats(Strings.sweetMemories, Strings.sweetMemoriesChain, 4, 8, ModAbilities.LUCKY_STRIKE.location(), 1.0F, "item." + MODID + "." + Strings.sweetMemories + ".desc");
		addStats(Strings.sweetstack, Strings.sweetstackChain, 7, 5, null, 1.0F, "item." + MODID + "." + Strings.sweetstack + ".desc");
		addStats(Strings.threeWishes, Strings.threeWishesChain, 5, 1, null, 1.0F, "item." + MODID + "." + Strings.threeWishes + ".desc");
		addStats(Strings.totalEclipse, Strings.totalEclipseChain, 6, 3, null, 1.0F, "item." + MODID + "." + Strings.totalEclipse + ".desc");
		addStats(Strings.treasureTrove, Strings.treasureTroveChain, 4, 2, ModAbilities.JACKPOT.location(), 1.0F, "item." + MODID + "." + Strings.treasureTrove + ".desc");
		addStats(Strings.trueLightsFlight, Strings.trueLightsFlightChain, 5, 2, null, 1.0F, "item." + MODID + "." + Strings.trueLightsFlight + ".desc");
		addStats(Strings.twilightBlaze, Strings.twilightBlazeChain, 9, 5, ModAbilities.FIRAZA.location(), 1.0F, "item." + MODID + "." + Strings.twilightBlaze + ".desc");
		addStats(Strings.twoBecomeOne, Strings.twoBecomeOneChain, 7, 6, ModAbilities.LIGHT_AND_DARKNESS.location(), 1.0F, "item." + MODID + "." + Strings.twoBecomeOne + ".desc");
		addStats(Strings.ultimaWeaponBBS, Strings.ultimaWeaponBBSChain, 9, 7, ModAbilities.MP_HASTEGA.location(), 1.0F, "item." + MODID + "." + Strings.ultimaWeaponBBS + ".desc");
		addStats(Strings.ultimaWeaponDDD, Strings.ultimaWeaponDDDChain, 9, 7, ModAbilities.MP_HASTEGA.location(), 1.0F, "item." + MODID + "." + Strings.ultimaWeaponDDD + ".desc");
		addStats(Strings.ultimaWeaponKH1, Strings.ultimaWeaponKH1Chain, 9, 6, ModAbilities.MP_HASTEGA.location(), 1.0F, "item." + MODID + "." + Strings.ultimaWeaponKH1 + ".desc");
		addStats(Strings.ultimaWeaponKH2, Strings.ultimaWeaponKH2Chain, 9, 7, ModAbilities.MP_HASTEGA.location(), 1.0F, "item." + MODID + "." + Strings.ultimaWeaponKH2 + ".desc");
		addStats(Strings.ultimaWeaponKH3, Strings.ultimaWeaponKH3Chain, 9, 7, ModAbilities.MP_HASTEGA.location(), 1.0F, "item." + MODID + "." + Strings.ultimaWeaponKH3 + ".desc");
		addStats(Strings.umbrella, Strings.umbrellaChain, 4, 0, null, 0.67F, "item." + MODID + "." + Strings.umbrella + ".desc");
		addStats(Strings.unbound, Strings.unboundChain, 9, 6, null, 1.0F, "item." + MODID + "." + Strings.unbound + ".desc");
		addStats(Strings.victoryLine, Strings.victoryLineChain, 5, 3, null, 1.0F, "item." + MODID + "." + Strings.victoryLine + ".desc");
		addStats(Strings.voidGear, Strings.voidGearChain, 9, 6, null, 1.5F, "item." + MODID + "." + Strings.voidGear + ".desc");
		addStats(Strings.voidGearRemnant, Strings.voidGearRemnantChain, 9, 6, null, 1.5F, "item." + MODID + "." + Strings.voidGearRemnant + ".desc");
		addStats(Strings.wayToTheDawn, Strings.wayToTheDawnChain, 4, 1, null, 1.0F, "item." + MODID + "." + Strings.wayToTheDawn + ".desc");
		addStats(Strings.waywardWind, Strings.waywardWindChain, 4, 1, null, 1.0F, "item." + MODID + "." + Strings.waywardWind + ".desc");
		addStats(Strings.wheelOfFate, Strings.wheelOfFateChain, 6, 4, ModAbilities.WATERZA.location(), 1.0F, "item." + MODID + "." + Strings.wheelOfFate + ".desc");
		addStats(Strings.winnersProof, Strings.winnersProofChain, 8, 9, ModAbilities.ZERO_EXP.location(), 1.0F, "item." + MODID + "." + Strings.winnersProof + ".desc");
		addStats(Strings.wishingLamp, Strings.wishingLampChain, 6, 5, ModAbilities.JACKPOT.location(), 1.0F, "item." + MODID + "." + Strings.wishingLamp + ".desc");
		addStats(Strings.wishingStar, Strings.wishingStarChain, 5, 1, null, 0F, "item." + MODID + "." + Strings.wishingStar + ".desc");
		addStats(Strings.youngXehanortsKeyblade, Strings.youngXehanortsKeybladeChain, 9, 7, null, 1.0F, "item." + MODID + "." + Strings.youngXehanortsKeyblade + ".desc");
		addStats(Strings.zeroOne, Strings.zeroOneChain, 6, 4, null, 1.0F, "item." + MODID + "." + Strings.zeroOne + ".desc");
		addStats(Strings.k111, Strings.k111c, 10, 8, null, 0.69F, "item." + MODID + "." + Strings.k111 + ".desc");
		addStats(Strings.retribution, Strings.retributionChain, 5, 5, ModAbilities.FIRE_BOOST.location(), 0.5F, "item." + MODID + "." + Strings.retribution + ".desc");
	}

	private static final Map<String, List<Pair<Item, Integer>>> BASE_MATERIALS = new HashMap<>();

	/**
	 * Fallback used only for keyblades that either have no synthesis recipe in SynthesisRecipe.java
	 * (e.g. k111, retribution) or whose recipe has no eligible shard/stone/gem/crystal materials at all
	 * (e.g. nightmaresEndAndMirageSplit, which is only made of two other keyblades' keychains).
	 */
	private static final List<Pair<Item, Integer>> DEFAULT_MATERIALS = List.of(
			m(ModItems.wellspring_shard.get(), 2),
			m(ModItems.wellspring_stone.get(), 1),
			m(ModItems.wellspring_gem.get(), 1)
	);

	static {
		BASE_MATERIALS.put(Strings.abaddonPlasma, List.of(m(ModItems.soothing_gem.get(), 2), m(ModItems.writhing_stone.get(), 4), m(ModItems.soothing_crystal.get(), 3)));
		BASE_MATERIALS.put(Strings.abyssalTide, List.of(m(ModItems.pulsing_stone.get(), 3), m(ModItems.frost_shard.get(), 3)));
		BASE_MATERIALS.put(Strings.acedsKeyblade, List.of(m(ModItems.tranquility_crystal.get(), 6), m(ModItems.tranquility_stone.get(), 3), m(ModItems.tranquility_gem.get(), 4)));
		BASE_MATERIALS.put(Strings.adventRed, List.of(m(ModItems.blazing_crystal.get(), 1), m(ModItems.pulsing_crystal.get(), 1)));
		BASE_MATERIALS.put(Strings.allForOne, List.of(m(ModItems.wellspring_crystal.get(), 1), m(ModItems.soothing_stone.get(), 3), m(ModItems.soothing_shard.get(), 2)));
		BASE_MATERIALS.put(Strings.astralBlast, List.of(m(ModItems.soothing_gem.get(), 2), m(ModItems.lucid_stone.get(), 2), m(ModItems.blazing_shard.get(), 3)));
		BASE_MATERIALS.put(Strings.aubade, List.of(m(ModItems.blazing_stone.get(), 1), m(ModItems.blazing_shard.get(), 2), m(ModItems.blazing_crystal.get(), 1)));
		BASE_MATERIALS.put(Strings.avasKeyblade, List.of(m(ModItems.betwixt_crystal.get(), 6), m(ModItems.betwixt_stone.get(), 3), m(ModItems.betwixt_gem.get(), 4)));
		BASE_MATERIALS.put(Strings.bondOfFlame, List.of(m(ModItems.blazing_stone.get(), 3), m(ModItems.blazing_shard.get(), 5), m(ModItems.blazing_gem.get(), 2)));
		BASE_MATERIALS.put(Strings.bondOfTheBlaze, List.of(m(ModItems.blazing_gem.get(), 4), m(ModItems.blazing_stone.get(), 2), m(ModItems.blazing_shard.get(), 5)));
		BASE_MATERIALS.put(Strings.braveheart, List.of(m(ModItems.betwixt_crystal.get(), 1), m(ModItems.pulsing_gem.get(), 2)));
		BASE_MATERIALS.put(Strings.brightcrest, List.of(m(ModItems.soothing_gem.get(), 2), m(ModItems.soothing_stone.get(), 3), m(ModItems.soothing_crystal.get(), 1)));
		BASE_MATERIALS.put(Strings.chaosRipper, List.of(m(ModItems.lucid_crystal.get(), 2), m(ModItems.writhing_gem.get(), 4), m(ModItems.writhing_crystal.get(), 3)));
		BASE_MATERIALS.put(Strings.circleOfLife, List.of(m(ModItems.soothing_gem.get(), 3), m(ModItems.pulsing_stone.get(), 3), m(ModItems.wellspring_shard.get(), 3)));
		BASE_MATERIALS.put(Strings.classicTone, List.of(m(ModItems.soothing_gem.get(), 3), m(ModItems.sinister_stone.get(), 3)));
		BASE_MATERIALS.put(Strings.counterpoint, List.of(m(ModItems.hungry_stone.get(), 1), m(ModItems.wellspring_crystal.get(), 1), m(ModItems.soothing_shard.get(), 2)));
		BASE_MATERIALS.put(Strings.crabclaw, List.of(m(ModItems.soothing_gem.get(), 2), m(ModItems.hungry_stone.get(), 2), m(ModItems.lucid_shard.get(), 2)));
		BASE_MATERIALS.put(Strings.crownOfGuilt, List.of(m(ModItems.tranquility_gem.get(), 1), m(ModItems.writhing_stone.get(), 2), m(ModItems.pulsing_crystal.get(), 2)));
		BASE_MATERIALS.put(Strings.crystalSnow, List.of(m(ModItems.tranquility_gem.get(), 1), m(ModItems.frost_stone.get(), 2), m(ModItems.frost_crystal.get(), 2)));
		BASE_MATERIALS.put(Strings.darkerThanDark, List.of(m(ModItems.lucid_crystal.get(), 3), m(ModItems.writhing_crystal.get(), 2), m(ModItems.writhing_gem.get(), 2)));
		BASE_MATERIALS.put(Strings.darkgnaw, List.of(m(ModItems.writhing_crystal.get(), 5), m(ModItems.lucid_shard.get(), 5)));
		BASE_MATERIALS.put(Strings.dawnTillDusk, List.of(m(ModItems.blazing_crystal.get(), 1), m(ModItems.blazing_gem.get(), 3), m(ModItems.blazing_stone.get(), 2)));
		BASE_MATERIALS.put(Strings.deadOfNight, List.of(m(ModItems.betwixt_gem.get(), 1), m(ModItems.betwixt_crystal.get(), 1), m(ModItems.lucid_crystal.get(), 1)));
		BASE_MATERIALS.put(Strings.decisivePumpkin, List.of(m(ModItems.frost_crystal.get(), 3), m(ModItems.writhing_crystal.get(), 3), m(ModItems.writhing_gem.get(), 2)));
		BASE_MATERIALS.put(Strings.destinysEmbrace, List.of(m(ModItems.pulsing_stone.get(), 3), m(ModItems.lightning_stone.get(), 5), m(ModItems.soothing_crystal.get(), 5)));
		BASE_MATERIALS.put(Strings.diamondDust, List.of(m(ModItems.pulsing_stone.get(), 1), m(ModItems.frost_stone.get(), 5), m(ModItems.frost_gem.get(), 3)));
		BASE_MATERIALS.put(Strings.divewing, List.of(m(ModItems.wellspring_crystal.get(), 3), m(ModItems.twilight_crystal.get(), 3), m(ModItems.blazing_gem.get(), 2)));
		BASE_MATERIALS.put(Strings.divineRose, List.of(m(ModItems.soothing_gem.get(), 4), m(ModItems.pulsing_stone.get(), 4), m(ModItems.wellspring_crystal.get(), 3)));
		BASE_MATERIALS.put(Strings.dualDisc, List.of(m(ModItems.soothing_crystal.get(), 2), m(ModItems.pulsing_gem.get(), 5), m(ModItems.lightning_gem.get(), 4)));
		BASE_MATERIALS.put(Strings.earthshaker, List.of(m(ModItems.writhing_shard.get(), 5), m(ModItems.betwixt_stone.get(), 3), m(ModItems.wellspring_shard.get(), 3)));
		BASE_MATERIALS.put(Strings.elementalEncoder, List.of(m(ModItems.blazing_gem.get(), 1), m(ModItems.frost_crystal.get(), 1), m(ModItems.lightning_crystal.get(), 1)));
		BASE_MATERIALS.put(Strings.endOfPain, List.of(m(ModItems.pulsing_stone.get(), 3), m(ModItems.wellspring_crystal.get(), 2), m(ModItems.writhing_shard.get(), 3)));
		BASE_MATERIALS.put(Strings.endsOfTheEarth, List.of(m(ModItems.pulsing_stone.get(), 3), m(ModItems.writhing_shard.get(), 3), m(ModItems.writhing_gem.get(), 1)));
		BASE_MATERIALS.put(Strings.everAfter, List.of(m(ModItems.stormy_stone.get(), 3), m(ModItems.lightning_shard.get(), 1), m(ModItems.writhing_stone.get(), 1)));
		BASE_MATERIALS.put(Strings.fairyHarp, List.of(m(ModItems.soothing_shard.get(), 3), m(ModItems.pulsing_gem.get(), 2), m(ModItems.lucid_gem.get(), 2)));
		BASE_MATERIALS.put(Strings.fairyStars, List.of(m(ModItems.soothing_shard.get(), 4), m(ModItems.pulsing_gem.get(), 3), m(ModItems.wellspring_shard.get(), 3)));
		BASE_MATERIALS.put(Strings.fatalCrest, List.of(m(ModItems.writhing_stone.get(), 3), m(ModItems.writhing_gem.get(), 3), m(ModItems.betwixt_stone.get(), 3)));
		BASE_MATERIALS.put(Strings.favoriteDeputy, List.of(m(ModItems.lucid_crystal.get(), 3), m(ModItems.remembrance_gem.get(), 3), m(ModItems.betwixt_stone.get(), 1)));
		BASE_MATERIALS.put(Strings.fenrir, List.of(m(ModItems.twilight_gem.get(), 3), m(ModItems.pulsing_stone.get(), 6), m(ModItems.wellspring_crystal.get(), 2)));
		BASE_MATERIALS.put(Strings.ferrisGear, List.of(m(ModItems.soothing_gem.get(), 2), m(ModItems.lightning_stone.get(), 2), m(ModItems.lucid_gem.get(), 2)));
		BASE_MATERIALS.put(Strings.followTheWind, List.of(m(ModItems.writhing_stone.get(), 3), m(ModItems.pulsing_stone.get(), 2), m(ModItems.blazing_shard.get(), 2)));
		BASE_MATERIALS.put(Strings.frolicFlame, List.of(m(ModItems.pulsing_stone.get(), 1), m(ModItems.blazing_shard.get(), 3), m(ModItems.blazing_gem.get(), 2)));
		BASE_MATERIALS.put(Strings.glimpseOfDarkness, List.of(m(ModItems.writhing_stone.get(), 3), m(ModItems.betwixt_crystal.get(), 3), m(ModItems.writhing_gem.get(), 3)));
		BASE_MATERIALS.put(Strings.grandChef, List.of(m(ModItems.blazing_stone.get(), 3), m(ModItems.blazing_crystal.get(), 1), m(ModItems.blazing_shard.get(), 2)));
		BASE_MATERIALS.put(Strings.guardianBell, List.of(m(ModItems.writhing_stone.get(), 3), m(ModItems.lightning_shard.get(), 3), m(ModItems.wellspring_crystal.get(), 2)));
		BASE_MATERIALS.put(Strings.guardianSoul, List.of(m(ModItems.pulsing_stone.get(), 4), m(ModItems.writhing_shard.get(), 3), m(ModItems.lightning_gem.get(), 3)));
		BASE_MATERIALS.put(Strings.gulasKeyblade, List.of(m(ModItems.hungry_crystal.get(), 6), m(ModItems.hungry_stone.get(), 3), m(ModItems.hungry_gem.get(), 4)));
		BASE_MATERIALS.put(Strings.gullWing, List.of(m(ModItems.wellspring_crystal.get(), 2), m(ModItems.blazing_gem.get(), 2), m(ModItems.pulsing_shard.get(), 5)));
		BASE_MATERIALS.put(Strings.happyGear, List.of(m(ModItems.wellspring_crystal.get(), 2), m(ModItems.sinister_gem.get(), 2), m(ModItems.sinister_shard.get(), 4)));
		BASE_MATERIALS.put(Strings.herosCrest, List.of(m(ModItems.soothing_stone.get(), 2), m(ModItems.lightning_crystal.get(), 2), m(ModItems.pulsing_shard.get(), 3)));
		BASE_MATERIALS.put(Strings.herosOrigin, List.of(m(ModItems.soothing_stone.get(), 2), m(ModItems.lightning_shard.get(), 2), m(ModItems.pulsing_shard.get(), 1)));
		BASE_MATERIALS.put(Strings.hiddenDragon, List.of(m(ModItems.blazing_shard.get(), 4), m(ModItems.pulsing_stone.get(), 3), m(ModItems.mythril_crystal.get(), 4)));
		BASE_MATERIALS.put(Strings.hunnySpout, List.of(m(ModItems.blazing_shard.get(), 1), m(ModItems.pulsing_stone.get(), 3), m(ModItems.betwixt_crystal.get(), 1)));
		BASE_MATERIALS.put(Strings.hyperdrive, List.of(m(ModItems.wellspring_crystal.get(), 4), m(ModItems.frost_stone.get(), 2), m(ModItems.lucid_gem.get(), 2)));
		BASE_MATERIALS.put(Strings.incompleteKiblade, List.of(m(ModItems.writhing_crystal.get(), 1), m(ModItems.mythril_crystal.get(), 1), m(ModItems.twilight_crystal.get(), 1)));
		BASE_MATERIALS.put(Strings.invisKeyblade, List.of(m(ModItems.lucid_crystal.get(), 6), m(ModItems.lucid_stone.get(), 3), m(ModItems.lucid_gem.get(), 4)));
		BASE_MATERIALS.put(Strings.irasKeyblade, List.of(m(ModItems.pulsing_crystal.get(), 6), m(ModItems.pulsing_stone.get(), 3), m(ModItems.pulsing_gem.get(), 4)));
		BASE_MATERIALS.put(Strings.jungleKing, List.of(m(ModItems.wellspring_gem.get(), 3), m(ModItems.pulsing_stone.get(), 4), m(ModItems.betwixt_shard.get(), 3)));
		BASE_MATERIALS.put(Strings.keybladeOfPeoplesHearts, List.of(m(ModItems.writhing_crystal.get(), 3), m(ModItems.pulsing_stone.get(), 2)));
		BASE_MATERIALS.put(Strings.kiblade, List.of(m(ModItems.blazing_crystal.get(), 2), m(ModItems.soothing_crystal.get(), 2), m(ModItems.writhing_crystal.get(), 2)));
		BASE_MATERIALS.put(Strings.kingdomKey, List.of(m(ModItems.pulsing_stone.get(), 1), m(ModItems.pulsing_shard.get(), 1)));
		BASE_MATERIALS.put(Strings.kingdomKeyD, List.of(m(ModItems.pulsing_gem.get(), 1), m(ModItems.pulsing_shard.get(), 1)));
		BASE_MATERIALS.put(Strings.kingdomKeyN, List.of(m(ModItems.writhing_gem.get(), 1), m(ModItems.sinister_shard.get(), 1)));
		BASE_MATERIALS.put(Strings.knockoutPunch, List.of(m(ModItems.pulsing_stone.get(), 2), m(ModItems.wellspring_crystal.get(), 2), m(ModItems.soothing_stone.get(), 2)));
		BASE_MATERIALS.put(Strings.ladyLuck, List.of(m(ModItems.pulsing_stone.get(), 4), m(ModItems.wellspring_crystal.get(), 2), m(ModItems.hungry_shard.get(), 1)));
		BASE_MATERIALS.put(Strings.leviathan, List.of(m(ModItems.lucid_crystal.get(), 2), m(ModItems.frost_stone.get(), 2), m(ModItems.writhing_shard.get(), 3)));
		BASE_MATERIALS.put(Strings.lionheart, List.of(m(ModItems.betwixt_crystal.get(), 3), m(ModItems.twilight_crystal.get(), 3), m(ModItems.blazing_gem.get(), 5)));
		BASE_MATERIALS.put(Strings.lostMemory, List.of(m(ModItems.twilight_gem.get(), 3), m(ModItems.pulsing_stone.get(), 2), m(ModItems.hungry_shard.get(), 1)));
		BASE_MATERIALS.put(Strings.lunarEclipse, List.of(m(ModItems.soothing_gem.get(), 5), m(ModItems.frost_gem.get(), 2), m(ModItems.writhing_gem.get(), 5)));
		BASE_MATERIALS.put(Strings.markOfAHero, List.of(m(ModItems.lightning_shard.get(), 3), m(ModItems.pulsing_stone.get(), 2), m(ModItems.soothing_crystal.get(), 3)));
		BASE_MATERIALS.put(Strings.mastersDefender, List.of(m(ModItems.twilight_gem.get(), 10), m(ModItems.mythril_crystal.get(), 4), m(ModItems.twilight_crystal.get(), 7)));
		BASE_MATERIALS.put(Strings.maverickFlare, List.of(m(ModItems.blazing_shard.get(), 3), m(ModItems.blazing_gem.get(), 3), m(ModItems.wellspring_stone.get(), 3)));
		BASE_MATERIALS.put(Strings.metalChocobo, List.of(m(ModItems.lucid_crystal.get(), 2), m(ModItems.pulsing_stone.get(), 5), m(ModItems.betwixt_crystal.get(), 3)));
		BASE_MATERIALS.put(Strings.midnightBlue, List.of(m(ModItems.frost_crystal.get(), 1), m(ModItems.pulsing_crystal.get(), 1)));
		BASE_MATERIALS.put(Strings.midnightRoar, List.of(m(ModItems.soothing_gem.get(), 2), m(ModItems.writhing_stone.get(), 3), m(ModItems.writhing_crystal.get(), 2)));
		BASE_MATERIALS.put(Strings.mirageSplit, List.of(m(ModItems.writhing_stone.get(), 2), m(ModItems.writhing_crystal.get(), 4), m(ModItems.writhing_shard.get(), 3)));
		BASE_MATERIALS.put(Strings.missingAche, List.of(m(ModItems.writhing_stone.get(), 3), m(ModItems.soothing_stone.get(), 3), m(ModItems.wellspring_shard.get(), 2)));
		BASE_MATERIALS.put(Strings.monochrome, List.of(m(ModItems.lucid_stone.get(), 3), m(ModItems.writhing_stone.get(), 2), m(ModItems.pulsing_shard.get(), 2)));
		BASE_MATERIALS.put(Strings.moogleOGlory, List.of(m(ModItems.lucid_gem.get(), 3), m(ModItems.wellspring_stone.get(), 3), m(ModItems.pulsing_crystal.get(), 5)));
		BASE_MATERIALS.put(Strings.mysteriousAbyss, List.of(m(ModItems.frost_crystal.get(), 1), m(ModItems.frost_shard.get(), 2), m(ModItems.frost_stone.get(), 5)));
		BASE_MATERIALS.put(Strings.nanoGear, List.of(m(ModItems.lightning_crystal.get(), 1), m(ModItems.lightning_shard.get(), 2), m(ModItems.remembrance_stone.get(), 3)));
		BASE_MATERIALS.put(Strings.nightmaresEnd, List.of(m(ModItems.soothing_gem.get(), 6), m(ModItems.soothing_stone.get(), 2), m(ModItems.soothing_shard.get(), 3)));
		BASE_MATERIALS.put(Strings.noName, List.of(m(ModItems.frost_shard.get(), 3), m(ModItems.frost_gem.get(), 2), m(ModItems.writhing_gem.get(), 3)));
		BASE_MATERIALS.put(Strings.noNameBBS, List.of(m(ModItems.frost_shard.get(), 3), m(ModItems.frost_gem.get(), 2), m(ModItems.writhing_gem.get(), 3)));
		BASE_MATERIALS.put(Strings.oathkeeper, List.of(m(ModItems.mythril_crystal.get(), 3), m(ModItems.twilight_stone.get(), 4), m(ModItems.pulsing_gem.get(), 3)));
		BASE_MATERIALS.put(Strings.oblivion, List.of(m(ModItems.writhing_crystal.get(), 2), m(ModItems.pulsing_stone.get(), 5), m(ModItems.writhing_gem.get(), 4)));
		BASE_MATERIALS.put(Strings.oceansRage, List.of(m(ModItems.lightning_stone.get(), 5), m(ModItems.frost_shard.get(), 5)));
		BASE_MATERIALS.put(Strings.olympia, List.of(m(ModItems.pulsing_stone.get(), 2), m(ModItems.soothing_shard.get(), 2), m(ModItems.lightning_crystal.get(), 1)));
		BASE_MATERIALS.put(Strings.omegaWeapon, List.of(m(ModItems.twilight_crystal.get(), 1), m(ModItems.mythril_gem.get(), 2), m(ModItems.writhing_gem.get(), 2)));
		BASE_MATERIALS.put(Strings.ominousBlight, List.of(m(ModItems.writhing_stone.get(), 2), m(ModItems.soothing_stone.get(), 2), m(ModItems.pulsing_gem.get(), 2)));
		BASE_MATERIALS.put(Strings.oneWingedAngel, List.of(m(ModItems.blazing_stone.get(), 1), m(ModItems.blazing_gem.get(), 3), m(ModItems.blazing_crystal.get(), 5)));
		BASE_MATERIALS.put(Strings.painOfSolitude, List.of(m(ModItems.writhing_crystal.get(), 2), m(ModItems.betwixt_shard.get(), 2), m(ModItems.twilight_stone.get(), 1)));
		BASE_MATERIALS.put(Strings.phantomGreen, List.of(m(ModItems.lightning_crystal.get(), 1), m(ModItems.pulsing_crystal.get(), 1)));
		BASE_MATERIALS.put(Strings.photonDebugger, List.of(m(ModItems.lightning_shard.get(), 4), m(ModItems.lightning_crystal.get(), 2), m(ModItems.lightning_gem.get(), 3)));
		BASE_MATERIALS.put(Strings.pixiePetal, List.of(m(ModItems.lucid_stone.get(), 2), m(ModItems.soothing_shard.get(), 2), m(ModItems.pulsing_gem.get(), 2)));
		BASE_MATERIALS.put(Strings.pumpkinhead, List.of(m(ModItems.writhing_crystal.get(), 2), m(ModItems.pulsing_gem.get(), 3), m(ModItems.lucid_shard.get(), 5)));
		BASE_MATERIALS.put(Strings.rainfell, List.of(m(ModItems.writhing_stone.get(), 2), m(ModItems.frost_stone.get(), 1), m(ModItems.lucid_shard.get(), 5)));
		BASE_MATERIALS.put(Strings.rejectionOfFate, List.of(m(ModItems.twilight_gem.get(), 2), m(ModItems.writhing_stone.get(), 2), m(ModItems.twilight_crystal.get(), 3)));
		BASE_MATERIALS.put(Strings.royalRadiance, List.of(m(ModItems.pulsing_stone.get(), 5), m(ModItems.frost_shard.get(), 2), m(ModItems.soothing_stone.get(), 3)));
		BASE_MATERIALS.put(Strings.rumblingRose, List.of(m(ModItems.pulsing_stone.get(), 4), m(ModItems.soothing_shard.get(), 2), m(ModItems.lucid_gem.get(), 3)));
		BASE_MATERIALS.put(Strings.shootingStar, List.of(m(ModItems.hungry_stone.get(), 5), m(ModItems.hungry_shard.get(), 5), m(ModItems.lucid_shard.get(), 2)));
		BASE_MATERIALS.put(Strings.signOfInnocence, List.of(m(ModItems.twilight_gem.get(), 2), m(ModItems.twilight_crystal.get(), 1), m(ModItems.writhing_shard.get(), 3)));
		BASE_MATERIALS.put(Strings.silentDirge, List.of(m(ModItems.soothing_gem.get(), 2), m(ModItems.twilight_crystal.get(), 2), m(ModItems.writhing_shard.get(), 1)));
		BASE_MATERIALS.put(Strings.skullNoise, List.of(m(ModItems.writhing_crystal.get(), 3), m(ModItems.writhing_gem.get(), 2), m(ModItems.wellspring_shard.get(), 3)));
		BASE_MATERIALS.put(Strings.sleepingLion, List.of(m(ModItems.twilight_gem.get(), 1), m(ModItems.pulsing_stone.get(), 2), m(ModItems.tranquility_crystal.get(), 2)));
		BASE_MATERIALS.put(Strings.soulEater, List.of(m(ModItems.writhing_crystal.get(), 3), m(ModItems.pulsing_stone.get(), 5), m(ModItems.writhing_gem.get(), 5)));
		BASE_MATERIALS.put(Strings.spellbinder, List.of(m(ModItems.lucid_crystal.get(), 2), m(ModItems.frost_stone.get(), 2), m(ModItems.pulsing_gem.get(), 2)));
		BASE_MATERIALS.put(Strings.starCluster, List.of(m(ModItems.twilight_crystal.get(), 2), m(ModItems.betwixt_stone.get(), 2)));
		BASE_MATERIALS.put(Strings.starSeeker, List.of(m(ModItems.twilight_stone.get(), 5), m(ModItems.betwixt_shard.get(), 3), m(ModItems.pulsing_shard.get(), 2)));
		BASE_MATERIALS.put(Strings.starlight, List.of(m(ModItems.mythril_stone.get(), 3), m(ModItems.mythril_crystal.get(), 3), m(ModItems.mythril_shard.get(), 3)));
		BASE_MATERIALS.put(Strings.stormfall, List.of(m(ModItems.stormy_stone.get(), 2), m(ModItems.stormy_crystal.get(), 1), m(ModItems.soothing_stone.get(), 3)));
		BASE_MATERIALS.put(Strings.strokeOfMidnight, List.of(m(ModItems.pulsing_stone.get(), 2), m(ModItems.frost_shard.get(), 2), m(ModItems.betwixt_shard.get(), 1)));
		BASE_MATERIALS.put(Strings.sweetDreams, List.of(m(ModItems.pulsing_stone.get(), 2), m(ModItems.wellspring_crystal.get(), 2), m(ModItems.twilight_shard.get(), 4)));
		BASE_MATERIALS.put(Strings.sweetMemories, List.of(m(ModItems.wellspring_crystal.get(), 3), m(ModItems.pulsing_stone.get(), 2), m(ModItems.lucid_shard.get(), 4)));
		BASE_MATERIALS.put(Strings.sweetstack, List.of(m(ModItems.pulsing_stone.get(), 4), m(ModItems.blazing_crystal.get(), 2), m(ModItems.lucid_gem.get(), 1)));
		BASE_MATERIALS.put(Strings.threeWishes, List.of(m(ModItems.lucid_gem.get(), 3), m(ModItems.wellspring_stone.get(), 3), m(ModItems.pulsing_crystal.get(), 5)));
		BASE_MATERIALS.put(Strings.totalEclipse, List.of(m(ModItems.writhing_stone.get(), 3), m(ModItems.blazing_shard.get(), 2), m(ModItems.blazing_gem.get(), 2)));
		BASE_MATERIALS.put(Strings.treasureTrove, List.of(m(ModItems.lucid_crystal.get(), 2), m(ModItems.frost_crystal.get(), 2), m(ModItems.writhing_crystal.get(), 1)));
		BASE_MATERIALS.put(Strings.trueLightsFlight, List.of(m(ModItems.twilight_gem.get(), 3), m(ModItems.twilight_shard.get(), 3)));
		BASE_MATERIALS.put(Strings.twilightBlaze, List.of(m(ModItems.twilight_crystal.get(), 3), m(ModItems.blazing_crystal.get(), 3)));
		BASE_MATERIALS.put(Strings.twoBecomeOne, List.of(m(ModItems.twilight_gem.get(), 2), m(ModItems.twilight_crystal.get(), 3)));
		BASE_MATERIALS.put(Strings.ultimaWeaponBBS, List.of(m(ModItems.mythril_crystal.get(), 1), m(ModItems.hungry_crystal.get(), 5), m(ModItems.lightning_gem.get(), 1)));
		BASE_MATERIALS.put(Strings.ultimaWeaponDDD, List.of(m(ModItems.twilight_gem.get(), 4), m(ModItems.twilight_crystal.get(), 3), m(ModItems.betwixt_crystal.get(), 2)));
		BASE_MATERIALS.put(Strings.ultimaWeaponKH1, List.of(m(ModItems.stormy_stone.get(), 3), m(ModItems.hungry_stone.get(), 5), m(ModItems.lightning_gem.get(), 5)));
		BASE_MATERIALS.put(Strings.ultimaWeaponKH2, List.of(m(ModItems.mythril_crystal.get(), 1), m(ModItems.betwixt_crystal.get(), 1), m(ModItems.twilight_crystal.get(), 1)));
		BASE_MATERIALS.put(Strings.ultimaWeaponKH3, List.of(m(ModItems.wellspring_crystal.get(), 2), m(ModItems.lucid_crystal.get(), 2), m(ModItems.pulsing_crystal.get(), 2)));
		BASE_MATERIALS.put(Strings.umbrella, List.of(m(ModItems.twilight_shard.get(), 1), m(ModItems.mythril_shard.get(), 10)));
		BASE_MATERIALS.put(Strings.unbound, List.of(m(ModItems.twilight_gem.get(), 5), m(ModItems.mythril_crystal.get(), 3), m(ModItems.betwixt_gem.get(), 5)));
		BASE_MATERIALS.put(Strings.victoryLine, List.of(m(ModItems.soothing_stone.get(), 3), m(ModItems.lucid_gem.get(), 2), m(ModItems.pulsing_crystal.get(), 3)));
		BASE_MATERIALS.put(Strings.voidGear, List.of(m(ModItems.writhing_shard.get(), 1), m(ModItems.sinister_crystal.get(), 4), m(ModItems.sinister_shard.get(), 5)));
		BASE_MATERIALS.put(Strings.voidGearRemnant, List.of(m(ModItems.writhing_shard.get(), 1), m(ModItems.sinister_crystal.get(), 4), m(ModItems.sinister_shard.get(), 5)));
		BASE_MATERIALS.put(Strings.wayToTheDawn, List.of(m(ModItems.writhing_crystal.get(), 1), m(ModItems.hungry_gem.get(), 4), m(ModItems.twilight_crystal.get(), 1)));
		BASE_MATERIALS.put(Strings.waywardWind, List.of(m(ModItems.writhing_shard.get(), 2), m(ModItems.pulsing_shard.get(), 2), m(ModItems.stormy_shard.get(), 1)));
		BASE_MATERIALS.put(Strings.wheelOfFate, List.of(m(ModItems.writhing_shard.get(), 2), m(ModItems.pulsing_gem.get(), 2), m(ModItems.stormy_crystal.get(), 3)));
		BASE_MATERIALS.put(Strings.wishingLamp, List.of(m(ModItems.wellspring_stone.get(), 3), m(ModItems.lucid_crystal.get(), 2), m(ModItems.pulsing_crystal.get(), 3)));
		BASE_MATERIALS.put(Strings.winnersProof, List.of(m(ModItems.writhing_stone.get(), 3), m(ModItems.tranquility_shard.get(), 4), m(ModItems.writhing_shard.get(), 5)));
		BASE_MATERIALS.put(Strings.wishingStar, List.of(m(ModItems.soothing_gem.get(), 2), m(ModItems.pulsing_stone.get(), 2), m(ModItems.mythril_crystal.get(), 2)));
		BASE_MATERIALS.put(Strings.youngXehanortsKeyblade, List.of(m(ModItems.lucid_crystal.get(), 3), m(ModItems.writhing_crystal.get(), 10), m(ModItems.frost_shard.get(), 5)));
		BASE_MATERIALS.put(Strings.zeroOne, List.of(m(ModItems.lightning_crystal.get(), 3), m(ModItems.pulsing_gem.get(), 4), m(ModItems.lightning_stone.get(), 2)));
	}

	private static Pair<Item, Integer> m(Item item, int qty) {
		return Pair.of(item, qty);
	}

	public void addStats(String keyblade, String keychain, int baseStr, int baseMag, ResourceLocation ability, float extraReach, String description) {
		Recipe[] levels = buildLevels(BASE_MATERIALS.getOrDefault(keyblade, DEFAULT_MATERIALS));
		getBuilder(keyblade).keychain(keychain).baseStats(baseStr, baseMag).ability(ability).reach(extraReach).levels(levels).desc(description);
	}

	/**
	 * Builds the 10 upgrade-level recipes for a keyblade from its (up to 3) base synthesis materials.
	 * <ul>
	 *     <li>Levels 1-3: 1 Fluorite + material #1 (quantity scales proportionally to its synthesis amount).</li>
	 *     <li>Levels 4-6: 1 Damascus + material #1 + material #2 (both scaling proportionally).</li>
	 *     <li>Levels 7-9: 1 Adamantite + materials #1, #2 and #3 (all scaling proportionally).</li>
	 *     <li>Level 10: 1 Electrum + materials #1, #2 and #3 at their peak (3x synthesis) quantities.</li>
	 * </ul>
	 * If a keyblade's base recipe has fewer than 3 eligible materials, the missing material slots are
	 * simply skipped (the recipe keeps fewer materials, as requested), and only the existing ones scale up.
	 * <p>
	 * Each material's quantity grows linearly, proportional to its synthesis-recipe amount, from 1x that
	 * amount on the level it's introduced up to 3x that amount by level 10 - so it's never stuck at a flat
	 * "x1" and materials asked for in bulk during synthesis stay proportionally bulkier during upgrades.
	 */
	private static Recipe[] buildLevels(List<Pair<Item, Integer>> baseMats) {
		Recipe[] levels = new Recipe[10];
		int count = Math.min(baseMats.size(), 3);

		for (int idx = 0; idx < 10; idx++) {
			Recipe recipe = new Recipe().addMaterial(tierItemFor(idx), 1);
			int tier = Math.min(idx / 3, 2);
			int available = Math.min(count, tier + 1);
			for (int mIdx = 0; mIdx < available; mIdx++) {
				int baseQty = baseMats.get(mIdx).getRight();
				int qty = proportionalQty(baseQty, mIdx, idx);
				recipe.addMaterial(baseMats.get(mIdx).getLeft(), qty);
			}
			levels[idx] = recipe;
		}

		return levels;
	}

	/** The "filler" tier material required at each of the 10 levels (indices 0-9 = levels 1-10). */
	private static Item tierItemFor(int levelIndex) {
		if (levelIndex < 3) return ModItems.fluorite.get();
		if (levelIndex < 6) return ModItems.damascus.get();
		if (levelIndex < 9) return ModItems.adamantite.get();
		return ModItems.electrum.get();
	}

	/**
	 * Scales a material's quantity proportionally to its synthesis-recipe amount ({@code baseQty}),
	 * growing linearly from 1x on the level the material (index {@code matIdx}, 0/1/2) is introduced
	 * up to 3x by level 10 (level index 9).
	 */
	private static int proportionalQty(int baseQty, int matIdx, int levelIndex) {
		int introLevel = matIdx * 3;
		int rampLen = 10 - introLevel;
		int position = levelIndex - introLevel;
		double factor = (rampLen > 1) ? 1.0 + position * (2.0 / (rampLen - 1)) : 1.0;
		return Math.max(1, (int) Math.ceil(baseQty * factor));
	}

	public static class Recipe {
		private final List<Map.Entry<Item, Integer>> recipe = new ArrayList<>();

		public Recipe() { }

		public Recipe addMaterial(Item mat, int quantity) {
			recipe.add(Pair.of(mat, quantity));
			return this;
		}

		public Map<Item, Integer> asMap() {
			Map<Item, Integer> matMap = new LinkedHashMap<>();
			recipe.forEach(p -> matMap.put(p.getKey(), p.getValue()));
			return matMap;
		}
	}

	@Override
	public String getName() {
		return "Keyblade json";
	}


}
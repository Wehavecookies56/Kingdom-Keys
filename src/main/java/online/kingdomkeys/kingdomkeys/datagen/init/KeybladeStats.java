package online.kingdomkeys.kingdomkeys.datagen.init;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import online.kingdomkeys.kingdomkeys.util.Utils;
import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.datagen.builder.KeybladeBuilder;
import online.kingdomkeys.kingdomkeys.datagen.provider.KeybladeProvider;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.synthesis.material.Material;
import online.kingdomkeys.kingdomkeys.synthesis.material.ModMaterials;

import static online.kingdomkeys.kingdomkeys.KingdomKeys.MODID;

public class KeybladeStats extends KeybladeProvider {
    public KeybladeStats(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, KingdomKeys.MODID, KeybladeBuilder::new, existingFileHelper);
    }

    @Override
    protected void registerKeyblades() {
		addStats(Strings.abaddonPlasma, Strings.abaddonPlasmaChain, 7, 3, "", 5.0F, "item." + MODID + "." + Strings.abaddonPlasma + ".desc");
		addStats(Strings.abyssalTide, Strings.abyssalTideChain, 5, 2, "", 5.0F, "item." + MODID + "." + Strings.abyssalTide + ".desc");
		addStats(Strings.acedsKeyblade, Strings.acedsKeybladeChain, 8, 7, "", 5.0F, "item." + MODID + "." + Strings.acedsKeyblade + ".desc");
		addStats(Strings.adventRed, Strings.adventRedChain, 4, 4, "", 5.0F, "item." + MODID + "." + Strings.adventRed + ".desc");
		addStats(Strings.allForOne, Strings.allForOneChain, 6, 7, "", 5.0F, "item." + MODID + "." + Strings.allForOne + ".desc");
		addStats(Strings.astralBlast, Strings.astralBlastChain, 8, 4, "", 5.0F, "item." + MODID + "." + Strings.astralBlast + ".desc");
		addStats(Strings.aubade, Strings.aubadeChain, 8, 4, "", 5.0F, "item." + MODID + "." + Strings.aubade + ".desc");
		addStats(Strings.avasKeyblade, Strings.avasKeybladeChain, 8, 7, "", 5.0F, "item." + MODID + "." + Strings.avasKeyblade + ".desc");
		addStats(Strings.bondOfFlame, Strings.bondOfFlameChain, 6, 6, ModAbilities.FIRE_BOOST.get().toString(), 5.0F, "item." + MODID + "." + Strings.bondOfFlame + ".desc");
		addStats(Strings.bondOfTheBlaze, Strings.bondOfTheBlazeChain, 6, 7, ModAbilities.FIRE_BOOST.get().toString(), 5.0F, "item." + MODID + "." + Strings.bondOfTheBlaze + ".desc");
		addStats(Strings.braveheart, Strings.braveheartChain, 5, 4, "", 5.0F, "item." + MODID + "." + Strings.braveheart + ".desc");
		addStats(Strings.brightcrest, Strings.brightcrestChain, 5, 6, ModAbilities.FULL_MP_BLAST.get().toString(), 6.0F, "item." + MODID + "." + Strings.brightcrest + ".desc");
		addStats(Strings.chaosRipper, Strings.chaosRipperChain, 9, 0, "", 6.5F, "item." + MODID + "." + Strings.chaosRipper + ".desc");
		addStats(Strings.circleOfLife, Strings.circleOfLifeChain, 6, 2, ModAbilities.MP_HASTE.get().toString(), 5.0F, "item." + MODID + "." + Strings.circleOfLife + ".desc");
		addStats(Strings.classicTone, Strings.classicToneChain, 6, 9, ModAbilities.MP_HASTE.get().toString(), 5.0F, "item." + MODID + "." + Strings.classicTone + ".desc");
		addStats(Strings.counterpoint, Strings.counterpointChain, 7, 7, ModAbilities.ENDLESS_MAGIC.get().toString(), 6.0F, "item." + MODID + "." + Strings.counterpoint + ".desc");
		addStats(Strings.crabclaw, Strings.crabclawChain, 5, 3, ModAbilities.WATER_BOOST.get().toString(), 5.0F, "item." + MODID + "." + Strings.crabclaw + ".desc");
		addStats(Strings.crownOfGuilt, Strings.crownOfGuiltChain, 6, 7, "", 5.0F, "item." + MODID + "." + Strings.crownOfGuilt + ".desc");
		addStats(Strings.crystalSnow, Strings.crystalSnowChain, 3, 6, "", 5.0F, "item." + MODID + "." + Strings.crystalSnow + ".desc");
		addStats(Strings.darkerThanDark, Strings.darkerThanDarkChain, 7, 8, "", 5.0F, "item." + MODID + "." + Strings.darkerThanDark + ".desc");
		addStats(Strings.darkgnaw, Strings.darkgnawChain, 6, 4, "", 5.0F, "item." + MODID + "." + Strings.darkgnaw + ".desc");
		addStats(Strings.dawnTillDusk, Strings.dawnTillDuskChain, 4, 4, "", 5.0F, "item." + MODID + "." + Strings.dawnTillDusk + ".desc");
		addStats(Strings.deadOfNight, Strings.deadOfNightChain, 4, 4, ModAbilities.GRAND_MAGIC_HASTE.get().toString(), 5.0F, "item." + MODID + "." + Strings.deadOfNight + ".desc");
		addStats(Strings.decisivePumpkin, Strings.decisivePumpkinChain, 8, 3, "", 5.0F, "item." + MODID + "." + Strings.decisivePumpkin + ".desc");
		addStats(Strings.destinysEmbrace, Strings.destinysEmbraceChain, 4, 4, "", 5.0F, "item." + MODID + "." + Strings.destinysEmbrace + ".desc");
		addStats(Strings.diamondDust, Strings.diamondDustChain, 4, 9, ModAbilities.BLIZZARD_BOOST.get().toString(), 5.0F, "item." + MODID + "." + Strings.diamondDust + ".desc");
		addStats(Strings.divewing, Strings.divewingChain, 7, 8, "", 5.0F, "item." + MODID + "." + Strings.divewing + ".desc");
		addStats(Strings.divineRose, Strings.divineRoseChain, 8, 2, "", 5.0F, "item." + MODID + "." + Strings.divineRose + ".desc");
		addStats(Strings.dualDisc, Strings.dualDiscChain, 6, 4, "", 5.0F, "item." + MODID + "." + Strings.dualDisc + ".desc");
		addStats(Strings.earthshaker, Strings.earthshakerChain, 4, 3, "", 5.0F, "item." + MODID + "." + Strings.earthshaker + ".desc");
		addStats(Strings.elementalEncoder, Strings.elementalEncoderChain, 4, 4, ModAbilities.GRAND_MAGIC_HASTE.get().toString(), 5.0F, "item." + MODID + "." + Strings.elementalEncoder + ".desc");
		addStats(Strings.endOfPain, Strings.endOfPainChain, 8, 8, "", 5.0F, "item." + MODID + "." + Strings.endOfPain + ".desc");
		addStats(Strings.endsOfTheEarth, Strings.endsOfTheEarthChain, 6, 5, "", 5.0F, "item." + MODID + "." + Strings.endsOfTheEarth + ".desc");
		addStats(Strings.everAfter, Strings.everAfterChain, 4, 6, ModAbilities.LEAF_BRACER.get().toString(), 5.0F, "item." + MODID + "." + Strings.everAfter + ".desc");
		addStats(Strings.fairyHarp, Strings.fairyHarpChain, 6, 4, "", 5.0F, "item." + MODID + "." + Strings.fairyHarp + ".desc");
		addStats(Strings.fairyStars, Strings.fairyStarsChain, 4, 5, ModAbilities.ENDLESS_MAGIC.get().toString(), 5.0F, "item." + MODID + "." + Strings.fairyStars + ".desc");
		addStats(Strings.fatalCrest, Strings.fatalCrestChain, 5, 7, ModAbilities.BERSERK_CHARGE.get().toString(), 5.0F, "item." + MODID + "." + Strings.fatalCrest + ".desc");
		addStats(Strings.favoriteDeputy, Strings.favoriteDeputyChain, 5, 3, "", 5.0F, "item." + MODID + "." + Strings.favoriteDeputy + ".desc");
		addStats(Strings.fenrir, Strings.fenrirChain, 9, 2, ModAbilities.NEGATIVE_COMBO.get().toString(), 5.0F, "item." + MODID + "." + Strings.fenrir + ".desc");
		addStats(Strings.ferrisGear, Strings.ferrisGearChain, 5, 5, "", 5.0F, "item." + MODID + "." + Strings.ferrisGear + ".desc");
		addStats(Strings.followTheWind, Strings.followTheWindChain, 4, 2, ModAbilities.TREASURE_MAGNET.get().toString(), 5.0F, "item." + MODID + "." + Strings.followTheWind + ".desc");
		addStats(Strings.frolicFlame, Strings.frolicFlameChain, 5, 5, "", 5.0F, "item." + MODID + "." + Strings.frolicFlame + ".desc");
		addStats(Strings.glimpseOfDarkness, Strings.glimpseOfDarknessChain, 7, 3, "", 5.0F, "item." + MODID + "." + Strings.glimpseOfDarkness + ".desc");
		addStats(Strings.grandChef, Strings.grandChefChain, 4, 6, ModAbilities.FIRE_BOOST.get().toString(), 5.0F, "item." + MODID + "." + Strings.grandChef + ".desc");
		addStats(Strings.guardianBell, Strings.guardianBellChain, 4, 5, "", 5.5F, "item." + MODID + "." + Strings.guardianBell + ".desc");
		addStats(Strings.guardianSoul, Strings.guardianSoulChain, 7, 4, "", 5.0F, "item." + MODID + "." + Strings.guardianSoul + ".desc");
		addStats(Strings.gulasKeyblade, Strings.gulasKeybladeChain, 8, 7, "", 5.0F, "item." + MODID + "." + Strings.gulasKeyblade + ".desc");
		addStats(Strings.gullWing, Strings.gullWingChain, 4, 5, ModAbilities.EXPERIENCE_BOOST.get().toString(), 5.0F, "item." + MODID + "." + Strings.gullWing + ".desc");
		addStats(Strings.happyGear, Strings.happyGearChain, 6, 2, ModAbilities.FULL_MP_BLAST.get().toString(), 6.0F, "item." + MODID + "." + Strings.happyGear + ".desc");
		addStats(Strings.herosCrest, Strings.herosCrestChain, 6, 1, "", 5.0F, "item." + MODID + "." + Strings.herosCrest + ".desc");
		addStats(Strings.herosOrigin, Strings.herosOriginChain, 5, 2, "", 5.0F, "item." + MODID + "." + Strings.herosOrigin + ".desc");
		addStats(Strings.hiddenDragon, Strings.hiddenDragonChain, 4, 4, ModAbilities.MP_RAGE.get().toString(), 5.0F, "item." + MODID + "." + Strings.hiddenDragon + ".desc");
		addStats(Strings.hunnySpout, Strings.hunnySpoutChain, 4, 4, ModAbilities.MP_HASTE.get().toString(), 5.0F, "item." + MODID + "." + Strings.hunnySpout + ".desc");
		addStats(Strings.hyperdrive, Strings.hyperdriveChain, 5, 5, "", 5.0F, "item." + MODID + "." + Strings.hyperdrive + ".desc");
		addStats(Strings.incompleteKiblade, Strings.incompleteKibladeChain, 5, 3, "", 8.0F, "item." + MODID + "." + Strings.incompleteKiblade + ".desc");
		addStats(Strings.invisKeyblade, Strings.invisKeybladeChain, 8, 7, "", 5.0F, "item." + MODID + "." + Strings.invisKeyblade + ".desc");
		addStats(Strings.irasKeyblade, Strings.irasKeybladeChain, 8, 7, "", 5.0F, "item." + MODID + "." + Strings.irasKeyblade + ".desc");
		addStats(Strings.jungleKing, Strings.jungleKingChain, 5, 2, "", 6.0F, "item." + MODID + "." + Strings.jungleKing + ".desc");
		addStats(Strings.keybladeOfPeoplesHearts, Strings.keybladeOfPeoplesHeartsChain, 6, 2, "", 5.0F, "item." + MODID + "." + Strings.keybladeOfPeoplesHearts + ".desc");
		addStats(Strings.kiblade, Strings.kibladeChain, 10, 7, ModAbilities.DARK_DOMINATION.get().toString(), 5.0F, "item." + MODID + "." + Strings.kiblade + ".desc");
		addStats(Strings.kingdomKey, Strings.kingdomKeyChain, 4, 1, ModAbilities.DAMAGE_CONTROL.get().toString(), 5.0F, "item." + MODID + "." + Strings.kingdomKey + ".desc");
		addStats(Strings.kingdomKeyD, Strings.kingdomKeyDChain, 4, 1, "", 5.0F, "item." + MODID + "." + Strings.kingdomKeyD + ".desc");
		addStats(Strings.knockoutPunch, Strings.knockoutPunchChain, 7, 5, "", 5.0F, "item." + MODID + "." + Strings.knockoutPunch + ".desc");
		addStats(Strings.ladyLuck, Strings.ladyLuckChain, 6, 5, "", 5.0F, "item." + MODID + "." + Strings.ladyLuck + ".desc");
		addStats(Strings.leviathan, Strings.leviathanChain, 5, 2, "", 5.0F, "item." + MODID + "." + Strings.leviathan + ".desc");
		addStats(Strings.lionheart, Strings.lionheartChain, 7, 3, "", 5.0F, "item." + MODID + "." + Strings.lionheart + ".desc");
		addStats(Strings.lostMemory, Strings.lostMemoryChain, 7, 6, "", 5.5F, "item." + MODID + "." + Strings.lostMemory + ".desc");
		addStats(Strings.lunarEclipse, Strings.lunarEclipseChain, 9, 7, "", 5.0F, "item." + MODID + "." + Strings.lunarEclipse + ".desc");
		addStats(Strings.markOfAHero, Strings.markOfAHeroChain, 6, 3, "", 5.0F, "item." + MODID + "." + Strings.markOfAHero + ".desc");
		addStats(Strings.mastersDefender, Strings.mastersDefenderChain, 8, 7, "", 5.0F, "item." + MODID + "." + Strings.mastersDefender + ".desc");
		addStats(Strings.maverickFlare, Strings.maverickFlareChain, 8, 4, ModAbilities.FIRE_BOOST.get().toString(), 5.0F, "item." + MODID + "." + Strings.maverickFlare + ".desc");
		addStats(Strings.metalChocobo, Strings.metalChocoboChain, 7, 1, "", 5.0F, "item." + MODID + "." + Strings.metalChocobo + ".desc");
		addStats(Strings.midnightBlue, Strings.midnightBlueChain, 4, 4, "", 5.0F, "item." + MODID + "." + Strings.midnightBlue + ".desc");
		addStats(Strings.midnightRoar, Strings.midnightRoarChain, 7, 4, "", 5.0F, "item." + MODID + "." + Strings.midnightRoar + ".desc");
		addStats(Strings.mirageSplit, Strings.mirageSplitChain, 7, 7, "", 5.0F, "item." + MODID + "." + Strings.mirageSplit + ".desc");
		addStats(Strings.missingAche, Strings.missingAcheChain, 4, 1, "", 5.0F, "item." + MODID + "." + Strings.missingAche + ".desc");
		addStats(Strings.monochrome, Strings.monochromeChain, 4, 3, "", 5.0F, "item." + MODID + "." + Strings.monochrome + ".desc");
		addStats(Strings.moogleOGlory, Strings.moogleOGloryChain, 8, 7, "", 5.0F, "item." + MODID + "." + Strings.moogleOGlory + ".desc");
		addStats(Strings.mysteriousAbyss, Strings.mysteriousAbyssChain, 5, 5, ModAbilities.BLIZZARD_BOOST.get().toString(), 5.0F, "item." + MODID + "." + Strings.mysteriousAbyss + ".desc");
		addStats(Strings.nanoGear, Strings.nanoGearChain, 5, 4, "", 5.0F, "item." + MODID + "." + Strings.nanoGear + ".desc");
		addStats(Strings.nightmaresEnd, Strings.nightmaresEndChain, 7, 7, "", 5.0F, "item." + MODID + "." + Strings.nightmaresEnd + ".desc");
		addStats(Strings.nightmaresEndAndMirageSplit, Strings.nightmaresEndAndMirageSplitChain, 9, 8, "", 5.0F, "item." + MODID + "." + Strings.nightmaresEndAndMirageSplit + ".desc");
		addStats(Strings.noName, Strings.noNameChain, 9, 7, "", 5.0F, "item." + MODID + "." + Strings.noName + ".desc");
		addStats(Strings.noNameBBS, Strings.noNameBBSChain, 7, 7, "", 6.0F, "item." + MODID + "." + Strings.noNameBBS + ".desc");
		addStats(Strings.oathkeeper, Strings.oathkeeperChain, 7, 7, ModAbilities.FORM_BOOST.get().toString(), 5.0F, "item." + MODID + "." + Strings.oathkeeper + ".desc");
		addStats(Strings.oblivion, Strings.oblivionChain, 8, 5, ModAbilities.DRIVE_BOOST.get().toString(), 5.0F, "item." + MODID + "." + Strings.oblivion + ".desc");
		addStats(Strings.oceansRage, Strings.oceansRageChain, 5, 5, ModAbilities.BLIZZARD_BOOST.get().toString(), 5.0F, "item." + MODID + "." + Strings.oceansRage + ".desc");
		addStats(Strings.olympia, Strings.olympiaChain, 7, 1, "", 5.0F, "item." + MODID + "." + Strings.olympia + ".desc");
		addStats(Strings.omegaWeapon, Strings.omegaWeaponChain, 8, 7, "", 5.0F, "item." + MODID + "." + Strings.omegaWeapon + ".desc");
		addStats(Strings.ominousBlight, Strings.ominousBlightChain, 4, 2, "", 5.0F, "item." + MODID + "." + Strings.ominousBlight + ".desc");
		addStats(Strings.oneWingedAngel, Strings.oneWingedAngelChain, 6, 0, "", 5.0F, "item." + MODID + "." + Strings.oneWingedAngel + ".desc");
		addStats(Strings.painOfSolitude, Strings.painOfSolitudeChain, 4, 5, "", 5.0F, "item." + MODID + "." + Strings.painOfSolitude + ".desc");
		addStats(Strings.phantomGreen, Strings.phantomGreenChain, 4, 4, "", 5.0F, "item." + MODID + "." + Strings.phantomGreen + ".desc");
		addStats(Strings.photonDebugger, Strings.photonDebuggerChain, 5, 3, ModAbilities.THUNDER_BOOST.get().toString(), 5.0F, "item." + MODID + "." + Strings.photonDebugger + ".desc");
		addStats(Strings.pixiePetal, Strings.pixiePetalChain, 4, 8, ModAbilities.LEAF_BRACER.get().toString(), 5.0F, "item." + MODID + "." + Strings.pixiePetal + ".desc");
		addStats(Strings.pumpkinhead, Strings.pumpkinheadChain, 6, 2, ModAbilities.CRITICAL_BOOST.get().toString(), 6.0F, "item." + MODID + "." + Strings.pumpkinhead + ".desc");
		addStats(Strings.rainfell, Strings.rainfellChain, 4, 4, "", 5.0F, "item." + MODID + "." + Strings.rainfell + ".desc");
		addStats(Strings.rejectionOfFate, Strings.rejectionOfFateChain, 5, 2, "", 5.0F, "item." + MODID + "." + Strings.rejectionOfFate + ".desc");
		addStats(Strings.royalRadiance, Strings.royalRadianceChain, 9, 8, ModAbilities.ENDLESS_MAGIC.get().toString(), 5.0F, "item." + MODID + "." + Strings.royalRadiance + ".desc");
		addStats(Strings.rumblingRose, Strings.rumblingRoseChain, 7, 3, "", 5.0F, "item." + MODID + "." + Strings.rumblingRose + ".desc");
		addStats(Strings.shootingStar, Strings.shootingStarChain, 4, 6, "", 5.0F, "item." + MODID + "." + Strings.shootingStar + ".desc");
		addStats(Strings.signOfInnocence, Strings.signOfInnocenceChain, 6, 5, "", 5.0F, "item." + MODID + "." + Strings.signOfInnocence + ".desc");
		addStats(Strings.silentDirge, Strings.silentDirgeChain, 6, 6, "", 5.0F, "item." + MODID + "." + Strings.silentDirge + ".desc");
		addStats(Strings.skullNoise, Strings.skullNoiseChain, 4, 4, "", 5.0F, "item." + MODID + "." + Strings.skullNoise + ".desc");
		addStats(Strings.sleepingLion, Strings.sleepingLionChain, 7, 5, "", 5.0F, "item." + MODID + "." + Strings.sleepingLion + ".desc");
		addStats(Strings.soulEater, Strings.soulEaterChain, 4, 1, "", 5.0F, "item." + MODID + "." + Strings.soulEater + ".desc");
		addStats(Strings.spellbinder, Strings.spellbinderChain, 4, 8, ModAbilities.FULL_MP_BLAST.get().toString(), 5.0F, "item." + MODID + "." + Strings.spellbinder + ".desc");
		addStats(Strings.starCluster, Strings.starClusterChain, 5, 6, "", 5.0F, "item." + MODID + "." + Strings.starCluster + ".desc");
		addStats(Strings.starSeeker, Strings.starSeekerChain, 4, 1, "", 5.0F, "item." + MODID + "." + Strings.starSeeker + ".desc");
		addStats(Strings.starlight, Strings.starlightChain, 5, 5, ModAbilities.MP_HASTE.get().toString(), 5.0F, "item." + MODID + "." + Strings.starlight + ".desc");
		addStats(Strings.stormfall, Strings.stormfallChain, 6, 5, "", 5.0F, "item." + MODID + "." + Strings.stormfall + ".desc");
		addStats(Strings.strokeOfMidnight, Strings.strokeOfMidnightChain, 4, 4, "", 5.0F, "item." + MODID + "." + Strings.strokeOfMidnight + ".desc");
		addStats(Strings.sweetDreams, Strings.sweetDreamsChain, 8, 6, "", 6.0F, "item." + MODID + "." + Strings.sweetDreams + ".desc");
		addStats(Strings.sweetMemories, Strings.sweetMemoriesChain, 4, 8, ModAbilities.LUCKY_LUCKY.get().toString(), 5.0F, "item." + MODID + "." + Strings.sweetMemories + ".desc");
		addStats(Strings.sweetstack, Strings.sweetstackChain, 7, 5, "", 5.0F, "item." + MODID + "." + Strings.sweetstack + ".desc");
		addStats(Strings.threeWishes, Strings.threeWishesChain, 5, 1, "", 5.0F, "item." + MODID + "." + Strings.threeWishes + ".desc");
		addStats(Strings.totalEclipse, Strings.totalEclipseChain, 6, 3, "", 5.0F, "item." + MODID + "." + Strings.totalEclipse + ".desc");
		addStats(Strings.treasureTrove, Strings.treasureTroveChain, 4, 2, "", 5.0F, "item." + MODID + "." + Strings.treasureTrove + ".desc");
		addStats(Strings.trueLightsFlight, Strings.trueLightsFlightChain, 5, 2, "", 5.0F, "item." + MODID + "." + Strings.trueLightsFlight + ".desc");
		addStats(Strings.twilightBlaze, Strings.twilightBlazeChain, 9, 5, "", 5.0F, "item." + MODID + "." + Strings.twilightBlaze + ".desc");
		addStats(Strings.twoBecomeOne, Strings.twoBecomeOneChain, 7, 6, ModAbilities.LIGHT_AND_DARKNESS.get().toString(), 5.0F, "item." + MODID + "." + Strings.twoBecomeOne + ".desc");
		addStats(Strings.ultimaWeaponBBS, Strings.ultimaWeaponBBSChain, 9, 7, ModAbilities.MP_HASTEGA.get().toString(), 5.0F, "item." + MODID + "." + Strings.ultimaWeaponBBS + ".desc");
		addStats(Strings.ultimaWeaponDDD, Strings.ultimaWeaponDDDChain, 9, 7, ModAbilities.MP_HASTEGA.get().toString(), 5.0F, "item." + MODID + "." + Strings.ultimaWeaponDDD + ".desc");
		addStats(Strings.ultimaWeaponKH1, Strings.ultimaWeaponKH1Chain, 9, 6, ModAbilities.MP_HASTEGA.get().toString(), 5.0F, "item." + MODID + "." + Strings.ultimaWeaponKH1 + ".desc");
		addStats(Strings.ultimaWeaponKH2, Strings.ultimaWeaponKH2Chain, 9, 7, ModAbilities.MP_HASTEGA.get().toString(), 5.0F, "item." + MODID + "." + Strings.ultimaWeaponKH2 + ".desc");
		addStats(Strings.ultimaWeaponKH3, Strings.ultimaWeaponKH3Chain, 9, 7, ModAbilities.MP_HASTEGA.get().toString(), 5.0F, "item." + MODID + "." + Strings.ultimaWeaponKH3 + ".desc");
		addStats(Strings.umbrella, Strings.umbrellaChain, 4, 0, "", 5.0F, "item." + MODID + "." + Strings.umbrella + ".desc");
		addStats(Strings.unbound, Strings.unboundChain, 9, 6, "", 5.0F, "item." + MODID + "." + Strings.unbound + ".desc");
		addStats(Strings.victoryLine, Strings.victoryLineChain, 5, 3, "", 5.0F, "item." + MODID + "." + Strings.victoryLine + ".desc");
		addStats(Strings.voidGear, Strings.voidGearChain, 9, 6, "", 5.5F, "item." + MODID + "." + Strings.voidGear + ".desc");
		addStats(Strings.voidGearRemnant, Strings.voidGearRemnantChain, 9, 6, "", 5.5F, "item." + MODID + "." + Strings.voidGearRemnant + ".desc");
		addStats(Strings.wayToTheDawn, Strings.wayToTheDawnChain, 4, 1, "", 5.0F, "item." + MODID + "." + Strings.wayToTheDawn + ".desc");
		addStats(Strings.waywardWind, Strings.waywardWindChain, 4, 1, "", 5.0F, "item." + MODID + "." + Strings.waywardWind + ".desc");
		addStats(Strings.wheelOfFate, Strings.wheelOfFateChain, 6, 4, "", 5.0F, "item." + MODID + "." + Strings.wheelOfFate + ".desc");
		addStats(Strings.winnersProof, Strings.winnersProofChain, 8, 9, ModAbilities.ZERO_EXP.get().toString(), 5.0F, "item." + MODID + "." + Strings.winnersProof + ".desc");
		addStats(Strings.wishingLamp, Strings.wishingLampChain, 6, 5, ModAbilities.JACKPOT.get().toString(), 5.0F, "item." + MODID + "." + Strings.wishingLamp + ".desc");
		addStats(Strings.wishingStar, Strings.wishingStarChain, 5, 1, "", 5.0F, "item." + MODID + "." + Strings.wishingStar + ".desc");
		addStats(Strings.youngXehanortsKeyblade, Strings.youngXehanortsKeybladeChain, 9, 7, "", 5.0F, "item." + MODID + "." + Strings.youngXehanortsKeyblade + ".desc");
		addStats(Strings.zeroOne, Strings.zeroOneChain, 6, 4, "", 5.0F, "item." + MODID + "." + Strings.zeroOne + ".desc");
		addStats(Strings.k111, Strings.k111c, 10, 8, "", 5, "item." + MODID + "." + Strings.k111 + ".desc");
	}

	static Recipe[] recipes = {
			new Recipe().addMaterial(Strings.SM_Fluorite, 1).addMaterial(Strings.SM_WellspringShard, 2),
			new Recipe().addMaterial(Strings.SM_Fluorite, 1).addMaterial(Strings.SM_WellspringShard, 3),
			new Recipe().addMaterial(Strings.SM_Fluorite, 1).addMaterial(Strings.SM_WellspringShard, 4),
			new Recipe().addMaterial(Strings.SM_Damascus, 1).addMaterial(Strings.SM_WellspringStone, 1),
			new Recipe().addMaterial(Strings.SM_Damascus, 1).addMaterial(Strings.SM_WellspringStone, 2).addMaterial(Strings.SM_WrithingStone, 2),
			new Recipe().addMaterial(Strings.SM_Damascus, 1).addMaterial(Strings.SM_WellspringStone, 3).addMaterial(Strings.SM_WrithingStone, 2),
			new Recipe().addMaterial(Strings.SM_Adamantite, 1).addMaterial(Strings.SM_WellspringGem, 1).addMaterial(Strings.SM_WrithingGem, 1),
			new Recipe().addMaterial(Strings.SM_Adamantite, 1).addMaterial(Strings.SM_WellspringGem, 2).addMaterial(Strings.SM_WrithingGem, 2),
			new Recipe().addMaterial(Strings.SM_Adamantite, 1).addMaterial(Strings.SM_WellspringGem, 3).addMaterial(Strings.SM_WrithingGem, 2),
			new Recipe().addMaterial(Strings.SM_Electrum, 1).addMaterial(Strings.SM_WellspringCrystal, 1).addMaterial(Strings.SM_WrithingCrystal, 1),
	};

	public void addStats(String keyblade, String keychain, int baseStr, int baseMag, String ability, float reach, String description) {
			getBuilder(keyblade).keychain(keychain).baseStats(baseStr, baseMag).ability(ability).reach(reach).levels(recipes).desc(description);
	}

	public static class Recipe {
        private List<Map.Entry<String, Integer>> recipe = new ArrayList<>();

        public Recipe() { }

        public Recipe addMaterial(String mat, int quantity) {
            recipe.add(Pair.of(mat, quantity));
            return this;
        }

        public Map<Material, Integer> asMap() {
            Map<Material, Integer> matMap = new LinkedHashMap<>();
            recipe.forEach(p -> matMap.put(ModMaterials.registry.get().getValue(new ResourceLocation(KingdomKeys.MODID + ":" + Strings.SM_Prefix + p.getKey())), p.getValue()));
            return matMap;
        }
    }

    @Override
    public String getName() {
        return "Keyblade json";
    }

	
}

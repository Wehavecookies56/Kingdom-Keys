package online.kingdomkeys.kingdomkeys.datagen.init;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

public class KeybladeStats extends KeybladeProvider {
    public KeybladeStats(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, KingdomKeys.MODID, KeybladeBuilder::new, existingFileHelper);
    }

    @Override
    protected void registerKeyblades() {
		addStats(Strings.abaddonPlasma, Strings.abaddonPlasmaChain, 7, 3, "", 5.0F, "A weapon that lets you string together faster, incredibly long ground combos.");
		addStats(Strings.abyssalTide, Strings.abyssalTideChain, 5, 2, "", 5.0F, "A weapon that performs very well in midair. Excellent for taking on fliers.");
		addStats(Strings.acedsKeyblade, Strings.acedsKeybladeChain, 8, 7, "", 5.0F, "The Keyblade owned by Ursus' Foreteller.");
		addStats(Strings.adventRed, Strings.adventRedChain, 4, 4, "", 5.0F, "A Keyblade that courses with mystic power.");
		addStats(Strings.allForOne, Strings.allForOneChain, 6, 7, "", 5.0F, "A Keyblade that triggers fewer Reality Shifts, but compensates with a boost in Magic and more frequent critical hits.");
		addStats(Strings.astralBlast, Strings.astralBlastChain, 8, 4, "", 5.0F, "A weapon that lets you string together longer ground and aerial combos.");
		addStats(Strings.aubade, Strings.aubadeChain, 8, 4, "", 5.0F, "A weapon that draws forth its wielder's personality.");
		addStats(Strings.avasKeyblade, Strings.avasKeybladeChain, 8, 7, "", 5.0F, "The Keyblade owned by Vulpeus' Foreteller.");
		addStats(Strings.bondOfFlame, Strings.bondOfFlameChain, 6, 6, ModAbilities.FIRE_BOOST.get().toString(), 5.0F, "Enhances magic to increase damage dealt by fire-based attacks.");
		addStats(Strings.bondOfTheBlaze, Strings.bondOfTheBlazeChain, 6, 7, ModAbilities.FIRE_BOOST.get().toString(), 5.0F, "The Keyblade wielded by Lea.");
		addStats(Strings.braveheart, Strings.braveheartChain, 5, 4, "", 5.0F, "Riku's Keyblade after the Way to the Dawn was broken.");
		addStats(Strings.brightcrest, Strings.brightcrestChain, 5, 6, ModAbilities.FULL_MP_BLAST.get().toString(), 6.0F, "A Keyblade with long reach that provides an outstanding boost in Magic. It also makes it easier to land critical hits, and deals higher damage when you do.");
		addStats(Strings.chaosRipper, Strings.chaosRipperChain, 9, 0, "", 6.5F, "A Keyblade with long reach that does little for your Magic, but provides an outstanding boost in Strength.");
		addStats(Strings.circleOfLife, Strings.circleOfLifeChain, 6, 2, ModAbilities.MP_HASTE.get().toString(), 5.0F, "Has great strength, increasing MP restoration speed after MP is consumed.");
		addStats(Strings.classicTone, Strings.classicToneChain, 6, 9, ModAbilities.MP_HASTE.get().toString(), 5.0F, "A Keyblade with an emphasis on Magic.");
		addStats(Strings.counterpoint, Strings.counterpointChain, 7, 7, "", 6.0F, "A Keyblade with long reach that provides an extra boost in Magic and makes it easier to trigger Reality Shifts.");
		addStats(Strings.crabclaw, Strings.crabclawChain, 5, 3, ModAbilities.WATER_BOOST.get().toString(), 5.0F, "Raises max MP by 1, and enhances magic and summon power. Also deals good physical damage.");
		addStats(Strings.crownOfGuilt, Strings.crownOfGuiltChain, 6, 7, "", 5.0F, "A weapon that boosts your Magic to give it incredible power.");
		addStats(Strings.crystalSnow, Strings.crystalSnowChain, 3, 6, "", 5.0F, "A Keyblade with an emphasis on Magic.");
		addStats(Strings.darkerThanDark, Strings.darkerThanDarkChain, 7, 8, "", 5.0F, "A weapon that offers high Magic and combo reach.");
		addStats(Strings.darkgnaw, Strings.darkgnawChain, 6, 4, "", 5.0F, "A Keyblade that makes up for its poor reach and low critical hit ratio by providing an extra boost in Strength.");
		addStats(Strings.dawnTillDusk, Strings.dawnTillDuskChain, 4, 4, "", 5.0F, "A Keyblade that courses with mystic power.");
		addStats(Strings.decisivePumpkin, Strings.decisivePumpkinChain, 8, 3, "", 5.0F, "The greater number of combos landed, the more damage is dealt, leading to a strong finishing move!");
		addStats(Strings.destinysEmbrace, Strings.destinysEmbraceChain, 4, 4, "", 5.0F, "A Keyblade that makes it easier to land critical hits.");
		addStats(Strings.diamondDust, Strings.diamondDustChain, 4, 9, ModAbilities.BLIZZARD_BOOST.get().toString(), 5.0F, "Greatly enhances magic and summon power. Raises max MP by 3.");
		addStats(Strings.divewing, Strings.divewingChain, 7, 8, "", 5.0F, "A Keyblade with long reach that provides an extra boost in Magic and makes it easier to trigger Reality Shifts.");
		addStats(Strings.divineRose, Strings.divineRoseChain, 8, 2, "", 5.0F, "A powerful weapon that is difficult to deflect. Capable of dealing a string of critical blows.");
		addStats(Strings.dualDisc, Strings.dualDiscChain, 6, 4, "", 5.0F, "A Keyblade that provides an extra boost in Strength and makes it easier to land critical hits.");
		addStats(Strings.earthshaker, Strings.earthshakerChain, 4, 3, "", 5.0F, "The Keyblade Terra started out with. What it lacks in reach it makes up for with a slight boost in Strength.");
		addStats(Strings.elementalEncoder, Strings.elementalEncoderChain, 4, 4, ModAbilities.GRAND_MAGIC_HASTE.get().toString(), 5.0F, "A Keyblade that courses with mystic power.");
		addStats(Strings.endOfPain, Strings.endOfPainChain, 8, 8, "", 5.0F, "A Keyblade with high magical power and critical hit rate, but reduces the occurrence of Reality Shift.");
		addStats(Strings.endsOfTheEarth, Strings.endsOfTheEarthChain, 6, 5, "", 5.0F, "A well-balanced Keyblade that provides an extra boost to all your stats.");
		addStats(Strings.everAfter, Strings.everAfterChain, 4, 6, "", 5.0F, "A Keyblade with an emphasis on Magic.");
		addStats(Strings.fairyHarp, Strings.fairyHarpChain, 6, 4, "", 5.0F, "Raises max MP by 1, and enhances magic and summon power. Sometimes deals powerful critical blows.");
		addStats(Strings.fairyStars, Strings.fairyStarsChain, 4, 5, "", 5.0F, "A Keyblade that provides a balanced boost in Strength and Magic.");
		addStats(Strings.fatalCrest, Strings.fatalCrestChain, 5, 7, ModAbilities.BERSERK_CHARGE.get().toString(), 5.0F, "Increases strength during MP Charge and allows unlimited chaining of combos.");
		addStats(Strings.favoriteDeputy, Strings.favoriteDeputyChain, 5, 3, "", 5.0F, "A Keyblade with an emphasis on Strength.");
		addStats(Strings.fenrir, Strings.fenrirChain, 9, 2, "", 5.0F, "Has great range and strength, but maximum ground and midair combos are decreased by 1.");
		addStats(Strings.ferrisGear, Strings.ferrisGearChain, 5, 5, "", 5.0F, "A Keyblade that provides an extra boost in Strength and makes it easier to land critical hits.");
		addStats(Strings.followTheWind, Strings.followTheWindChain, 4, 2, ModAbilities.TREASURE_MAGNET.get().toString(), 5.0F, "Draws in nearby orbs.");
		addStats(Strings.frolicFlame, Strings.frolicFlameChain, 5, 5, "", 5.0F, "A well-balanced Keyblade that provides an extra boost to all of your stats.");
		addStats(Strings.glimpseOfDarkness, Strings.glimpseOfDarknessChain, 7, 3, "", 5.0F, "A weapon that possesses very high Strength. Effective against tough enemies.");
		addStats(Strings.grandChef, Strings.grandChefChain, 4, 6, ModAbilities.FIRE_BOOST.get().toString(), 5.0F, "A Keyblade with an emphasis on Magic.");
		addStats(Strings.guardianBell, Strings.guardianBellChain, 4, 5, "", 5.5F, "A Keyblade with long reach that provides an extra boost in Magic.");
		addStats(Strings.guardianSoul, Strings.guardianSoulChain, 7, 4, "", 5.0F, "Has great strength, increasing the amount of damage dealt from Reaction Commands.");
		addStats(Strings.gulasKeyblade, Strings.gulasKeybladeChain, 8, 7, "", 5.0F, "The Keyblade owned by Leopardos' Foreteller.");
		addStats(Strings.gullWing, Strings.gullWingChain, 4, 5, ModAbilities.EXPERIENCE_BOOST.get().toString(), 5.0F, "Greatly increases the amount of experience gained when defeating an enemy at a critical moment.");
		addStats(Strings.happyGear, Strings.happyGearChain, 6, 2, ModAbilities.FULL_MP_BLAST.get().toString(), 6.0F, "A Keyblade with an emphasis on Strength.");
		addStats(Strings.herosCrest, Strings.herosCrestChain, 6, 1, "", 5.0F, "Increases the damage of the finishing move in the air relative to the number of hits in the combo.");
		addStats(Strings.herosOrigin, Strings.herosOriginChain, 5, 2, "", 5.0F, "A Keyblade with an emphasis on Strength.");
		addStats(Strings.hiddenDragon, Strings.hiddenDragonChain, 4, 4, ModAbilities.MP_RAGE.get().toString(), 5.0F, "Restores MP relative to the amount of damage taken.");
		addStats(Strings.hunnySpout, Strings.hunnySpoutChain, 4, 4, ModAbilities.MP_HASTE.get().toString(), 5.0F, "A well-balanced Keyblade.");
		addStats(Strings.hyperdrive, Strings.hyperdriveChain, 5, 5, "", 5.0F, "A Keyblade with above-average reach that provides a balanced boost in Strength and Magic.");
		addStats(Strings.incompleteKiblade, Strings.incompleteKibladeChain, 5, 3, "", 8.0F, "An incomplete form of the legendary Keyblade, the \u03c7-blade.");
		addStats(Strings.invisKeyblade, Strings.invisKeybladeChain, 8, 7, "", 5.0F, "The Keyblade owned by Anguis' Foreteller.");
		addStats(Strings.irasKeyblade, Strings.irasKeybladeChain, 8, 7, "", 5.0F, "The Keyblade owned by Unicornis' Foreteller.");
		addStats(Strings.jungleKing, Strings.jungleKingChain, 5, 2, "", 6.0F, "Has a long reach, but seldom deals critical blows.");
		addStats(Strings.keybladeOfPeoplesHearts, Strings.keybladeOfPeoplesHeartsChain, 6, 2, "", 5.0F, "A keyblade with the ability to unlock a person's heart, releasing the darkness within.");
		addStats(Strings.kiblade, Strings.kibladeChain, 10, 7, "", 5.0F, "A legendary weapon, the original Keyblade which all other are imperfectly modeled after.");
		addStats(Strings.kingdomKey, Strings.kingdomKeyChain, 4, 1, ModAbilities.DAMAGE_CONTROL.get().toString(), 5.0F, "The key chain attached draws out the Keyblade's true form and power.");
		addStats(Strings.kingdomKeyD, Strings.kingdomKeyDChain, 4, 1, "", 5.0F, "A Keyblade which mirrors the Kingdom Key from the Realm of Darkness.");
		addStats(Strings.knockoutPunch, Strings.knockoutPunchChain, 7, 5, "", 5.0F, "A Keyblade that lands fewer critical hits, but compensates with a Strength boost and more frequent Reality Shifts.");
		addStats(Strings.ladyLuck, Strings.ladyLuckChain, 6, 5, "", 5.0F, "Raises max MP by 2, and significantly enhances magic and summon power. Also inflicts good physical damage.");
		addStats(Strings.leviathan, Strings.leviathanChain, 5, 2, "", 5.0F, "A weapon that performs extremely well in midair. Outstanding for taking on fliers.");
		addStats(Strings.lionheart, Strings.lionheartChain, 7, 3, "", 5.0F, "Raises max MP by 1, and enhances magic and summon power. Also deals great physical damage.");
		addStats(Strings.lostMemory, Strings.lostMemoryChain, 7, 6, "", 5.5F, "A Keyblade with long reach that makes it easier to land critical hits, and deals higher damage when you do.");
		addStats(Strings.lunarEclipse, Strings.lunarEclipseChain, 9, 7, "", 5.0F, "A weapon that boosts versatility by greatly boosting both Strength and Magic.");
		addStats(Strings.markOfAHero, Strings.markOfAHeroChain, 6, 3, "", 5.0F, "A Keyblade that provides an extra boost in Strength and deals higher damage when you land a critical hit.");
		addStats(Strings.mastersDefender, Strings.mastersDefenderChain, 8, 7, "", 5.0F, "Master Eraqus's Keyblade. All of its stats are high.");
		addStats(Strings.maverickFlare, Strings.maverickFlareChain, 8, 4, ModAbilities.FIRE_BOOST.get().toString(), 5.0F, "A weapon that offers high Strength and ground combo speed.");
		addStats(Strings.metalChocobo, Strings.metalChocoboChain, 7, 1, "", 5.0F, "Possesses incredible power and reach, but reduces max MP by 1. Rarely deals critical blows.");
		addStats(Strings.midnightBlue, Strings.midnightBlueChain, 4, 4, "", 5.0F, "A Keyblade imbued with wondrous power.");
		addStats(Strings.midnightRoar, Strings.midnightRoarChain, 7, 4, "", 5.0F, "A weapon that possesses high Strength. Useful against tough enemies.");
		addStats(Strings.mirageSplit, Strings.mirageSplitChain, 7, 7, "", 5.0F, "A Keyblade formed from a Reality Shift in The World That Never Was.");
		addStats(Strings.missingAche, Strings.missingAcheChain, 4, 1, "", 5.0F, "A weapon that lets you string together faster, longer ground combos.");
		addStats(Strings.monochrome, Strings.monochromeChain, 4, 3, "", 5.0F, "Increases the effect of restoration items used on the field.");
		addStats(Strings.moogleOGlory, Strings.moogleOGloryChain, 8, 7, "", 5.0F, "Kupo.");
		addStats(Strings.mysteriousAbyss, Strings.mysteriousAbyssChain, 5, 5, ModAbilities.BLIZZARD_BOOST.get().toString(), 5.0F, "Enhances magic to increase damage dealt by blizzard-based attacks.");
		addStats(Strings.nanoGear, Strings.nanoGearChain, 5, 4, "", 5.0F, "A well-balanced Keyblade.");
		addStats(Strings.nightmaresEnd, Strings.nightmaresEndChain, 7, 7, "", 5.0F, "A Keyblade formed from a Reality Shift in The World That Never Was.");
		addStats(Strings.nightmaresEndAndMirageSplit, Strings.nightmaresEndAndMirageSplitChain, 9, 8, "", 5.0F, "A Keyblade formed by combining both the Mirage Split and Nightmare's End.");
		addStats(Strings.noName, Strings.noNameChain, 9, 7, "", 5.0F, "The Keyblade weilded by Master Xehanort.");
		addStats(Strings.noNameBBS, Strings.noNameBBSChain, 7, 7, "", 6.0F, "A Keyblade with long reach that provides an outstanding boost in Magic and makes it easier to land critical hits.");
		addStats(Strings.oathkeeper, Strings.oathkeeperChain, 7, 7, ModAbilities.FORM_BOOST.get().toString(), 5.0F, "Enhances magic and increases the duration of a Drive Form.");
		addStats(Strings.oblivion, Strings.oblivionChain, 8, 5, ModAbilities.DRIVE_BOOST.get().toString(), 5.0F, "Has great strength, and allows the Drive Gauge to restore quickly during MP Charge.");
		addStats(Strings.oceansRage, Strings.oceansRageChain, 5, 5, ModAbilities.BLIZZARD_BOOST.get().toString(), 5.0F, "A Keyblade that lands fewer critical hits, but compensates with a boost in Magic and more frequent Reality Shifts.");
		addStats(Strings.olympia, Strings.olympiaChain, 7, 1, "", 5.0F, "A powerful weapon that is difficult to deflect. Capable of inflicting mighty critical blows.");
		addStats(Strings.omegaWeapon, Strings.omegaWeaponChain, 8, 7, "", 5.0F, "A formidable weapon with exceptional capabilities.");
		addStats(Strings.ominousBlight, Strings.ominousBlightChain, 4, 2, "", 5.0F, "A weapon that lets you string together faster, much longer ground combos.");
		addStats(Strings.oneWingedAngel, Strings.oneWingedAngelChain, 6, 0, "", 5.0F, "Raises max MP by 1, and enhances magic and summon power. Also deals great physical damage.");
		addStats(Strings.painOfSolitude, Strings.painOfSolitudeChain, 4, 5, "", 5.0F, "A weapon that boosts your Magic to give it more power.");
		addStats(Strings.phantomGreen, Strings.phantomGreenChain, 4, 4, "", 5.0F, "A Keyblade imbued with wondrous power.");
		addStats(Strings.photonDebugger, Strings.photonDebuggerChain, 5, 3, ModAbilities.THUNDER_BOOST.get().toString(), 5.0F, "Increases damage done by thunder-based attacks.");
		addStats(Strings.pixiePetal, Strings.pixiePetalChain, 4, 8, "", 5.0F, "A Keyblade that makes up for its poor reach with an extra boost in Magic. It also makes it easier to land critical hits, and deals higher damage when you do.");
		addStats(Strings.pumpkinhead, Strings.pumpkinheadChain, 6, 2, ModAbilities.CRITICAL_BOOST.get().toString(), 6.0F, "Has a long reach and the ability to deal a string of critical blows.");
		addStats(Strings.rainfell, Strings.rainfellChain, 4, 4, "", 5.0F, "The Keyblade Aqua started out with. What it lacks in reach it makes up for with a balanced boost to Strength and Magic.");
		addStats(Strings.rejectionOfFate, Strings.rejectionOfFateChain, 5, 2, "", 5.0F, "A weapon that enables your attacks to reach a wide area and deal immense damage.");
		addStats(Strings.royalRadiance, Strings.royalRadianceChain, 9, 8, "", 5.0F, "A Keyblade with long reach that makes it easier to land critical hits, and deals higher damage when you do.");
		addStats(Strings.rumblingRose, Strings.rumblingRoseChain, 7, 3, "", 5.0F, "Has great strength, allowing finishing combo moves to be unleashed successively.");
		addStats(Strings.shootingStar, Strings.shootingStarChain, 4, 6, "", 5.0F, "A Keyblade with an emphasis on Magic.");
		addStats(Strings.signOfInnocence, Strings.signOfInnocenceChain, 6, 5, "", 5.0F, "A weapon that boosts your Magic to give it a lot more power.");
		addStats(Strings.silentDirge, Strings.silentDirgeChain, 6, 6, "", 5.0F, "A weapon that provides versatility by boosting both Strength and Magic. ");
		addStats(Strings.skullNoise, Strings.skullNoiseChain, 4, 4, "", 5.0F, "A Keyblade that provides a balanced boost in Strength and Magic.");
		addStats(Strings.sleepingLion, Strings.sleepingLionChain, 7, 5, "", 5.0F, "Well-balanced with strength and magic, increasing maximum ground-based combos by 1.");
		addStats(Strings.soulEater, Strings.soulEaterChain, 4, 1, "", 5.0F, "A sword that swims with darkness. Possesses high Strength.");
		addStats(Strings.spellbinder, Strings.spellbinderChain, 4, 8, ModAbilities.FULL_MP_BLAST.get().toString(), 5.0F, "Raises max MP by 2, and significantly enhances magic and summon power.");
		addStats(Strings.starCluster, Strings.starClusterChain, 5, 6, "", 5.0F, "Mickey's Keyblade, also known as Kingdom Key W.");
		addStats(Strings.starSeeker, Strings.starSeekerChain, 4, 1, "", 5.0F, "Increases maximum combo by 1 when in midair.");
		addStats(Strings.starlight, Strings.starlightChain, 5, 5, ModAbilities.MP_HASTE.get().toString(), 5.0F, "A basic Keyblade which is associated with the force of Light.");
		addStats(Strings.stormfall, Strings.stormfallChain, 6, 5, "", 5.0F, "A well-balanced Keyblade that provides an extra boost to all your stats.");
		addStats(Strings.strokeOfMidnight, Strings.strokeOfMidnightChain, 4, 4, "", 5.0F, "A Keyblade that makes it easier to land critical hits.");
		addStats(Strings.sweetDreams, Strings.sweetDreamsChain, 8, 6, "", 6.0F, "A Keyblade with long reach that provides an extra boost in Strength and makes it easier to land critical hits.");
		addStats(Strings.sweetMemories, Strings.sweetMemoriesChain, 4, 8, ModAbilities.LUCKY_LUCKY.get().toString(), 5.0F, "Although it does not enhance attack strength, it increases the drop rate of items.");
		addStats(Strings.sweetstack, Strings.sweetstackChain, 7, 5, "", 5.0F, "A Keyblade that provides an extra boost in Strength and ensures every strike is a critical hit.");
		addStats(Strings.threeWishes, Strings.threeWishesChain, 5, 1, "", 5.0F, "A powerful weapon that is difficult to deflect.");
		addStats(Strings.totalEclipse, Strings.totalEclipseChain, 6, 3, "", 5.0F, "A weapon that possesses extreme Strength. Devastates tough enemies.");
		addStats(Strings.treasureTrove, Strings.treasureTroveChain, 4, 2, "", 5.0F, "A Keyblade that makes up for its poor reach with a balanced boost in Strength and Magic.");
		addStats(Strings.trueLightsFlight, Strings.trueLightsFlightChain, 5, 2, "", 5.0F, "A weapon that enables your attacks to reach a wide area and deal heavy damage.");
		addStats(Strings.twilightBlaze, Strings.twilightBlazeChain, 9, 5, "", 5.0F, "A weapon that boasts superior Strength and ground combo speed.");
		addStats(Strings.twoBecomeOne, Strings.twoBecomeOneChain, 7, 6, ModAbilities.LIGHT_AND_DARKNESS.get().toString(), 5.0F, "A weapon of great strength and magic that has a special effect.");
		addStats(Strings.ultimaWeaponBBS, Strings.ultimaWeaponBBSChain, 9, 7, ModAbilities.MP_HASTEGA.get().toString(), 5.0F, "The most powerful of Keyblades.");
		addStats(Strings.ultimaWeaponDDD, Strings.ultimaWeaponDDDChain, 9, 7, ModAbilities.MP_HASTEGA.get().toString(), 5.0F, "An outstanding Keyblade that boosts all stats, and makes it easy to both land critical hits and trigger Reality Shifts.");
		addStats(Strings.ultimaWeaponKH1, Strings.ultimaWeaponKH1Chain, 9, 6, ModAbilities.MP_HASTEGA.get().toString(), 5.0F, "The ultimate Keyblade. Raises max MP by 2, and possesses maximum power and attributes.");
		addStats(Strings.ultimaWeaponKH2, Strings.ultimaWeaponKH2Chain, 9, 7, ModAbilities.MP_HASTEGA.get().toString(), 5.0F, "The Keyblade above all others, holding all power and will increase MP restoration rate, once all MP has been consumed.");
		addStats(Strings.ultimaWeaponKH3, Strings.ultimaWeaponKH3Chain, 9, 7, ModAbilities.MP_HASTEGA.get().toString(), 5.0F, "The supreme Keyblade.");
		addStats(Strings.umbrella, Strings.umbrellaChain, 4, 0, "", 5.0F, "This looks awfully familiar...");
		addStats(Strings.unbound, Strings.unboundChain, 9, 6, "", 5.0F, "Keyblade perfection. It boosts all stats, while making it easy to land critical hits and even easier to trigger Reality Shifts.");
		addStats(Strings.victoryLine, Strings.victoryLineChain, 5, 3, "", 5.0F, "A Keyblade with above-average reach that makes it easier to land critical hits.");
		addStats(Strings.voidGear, Strings.voidGearChain, 9, 6, "", 5.5F, "A Keyblade with long reach that provides an outstanding boost in Strength and deals higher damage when you land a critical hit.");
		addStats(Strings.voidGearRemnant, Strings.voidGearRemnantChain, 9, 6, "", 5.5F, "A Keyblade with long reach that provides an outstanding boost in Strength and deals higher damage when you land a critical hit.");
		addStats(Strings.wayToTheDawn, Strings.wayToTheDawnChain, 4, 1, "", 5.0F, "Deals various attacks.");
		addStats(Strings.waywardWind, Strings.waywardWindChain, 4, 1, "", 5.0F, "The Keyblade Ventus started out with. What it lacks in reach it makes up for with a slight boost in Strength.");
		addStats(Strings.wheelOfFate, Strings.wheelOfFateChain, 6, 4, "", 5.0F, "A Keyblade with an emphasis on Strength.");
		addStats(Strings.winnersProof, Strings.winnersProofChain, 8, 9, ModAbilities.ZERO_EXP.get().toString(), 5.0F, "Has high strength and hold's an excellent magic power. When the enemies are defeated, experience points are not gained.");
		addStats(Strings.wishingLamp, Strings.wishingLampChain, 6, 5, ModAbilities.JACKPOT.get().toString(), 5.0F, "Wishes come true by increasing the drop rate of munny, HP and MP orbs.");
		addStats(Strings.wishingStar, Strings.wishingStarChain, 5, 1, "", 5.0F, "Has a short reach, but always finishes up a combo attack with a powerful critical blow.");
		addStats(Strings.youngXehanortsKeyblade, Strings.youngXehanortsKeybladeChain, 9, 7, "", 5.0F, "The Keyblade weilded by Young Xehanort.");
		addStats(Strings.zeroOne, Strings.zeroOneChain, 6, 4, "", 5.0F, "A Keyblade newly wrought within the datascape. Its powers render all opponents helpless.");
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
			new Recipe().addMaterial(Strings.SM_Electrum, 1).addMaterial(Strings.SM_WellspringCrystal, 1).addMaterial(Strings.SM_WrithingCrystal, 1)
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

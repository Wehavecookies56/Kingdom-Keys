package online.kingdomkeys.kingdomkeys.datagen.init;

import net.minecraft.data.DataGenerator;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.datagen.builder.SynthesisRecipeBuilder;
import online.kingdomkeys.kingdomkeys.datagen.provider.SynthesisRecipeProvider;
import online.kingdomkeys.kingdomkeys.lib.Strings;

import static online.kingdomkeys.kingdomkeys.item.ModItems.*;

public class SynthesisRecipe extends SynthesisRecipeProvider {
    public SynthesisRecipe(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, KingdomKeys.MODID, SynthesisRecipeBuilder::new, existingFileHelper);
    }

    int D = 1, C = 2, B = 3, A = 4, S = 5, SS = 6, SSS = 7;
    
    @Override
    protected void registerRecipe() {
        // Keyblades
        getBuilder(Strings.abaddonPlasma).output(Strings.abaddonPlasmaChain).tier(A).type("keyblade").addMaterial(soothing_gem, 2).addMaterial(writhing_stone, 4).addMaterial(soothing_crystal, 3);
        getBuilder(Strings.abyssalTide).output(Strings.abyssalTideChain).tier(C).type("keyblade").addMaterial(pulsing_stone, 3).addMaterial(frost_shard, 3);
        getBuilder(Strings.adventRed).output(Strings.adventRedChain).tier(D).type("keyblade").addMaterial(blazing_crystal,1).addMaterial(pulsing_crystal,1);
        getBuilder(Strings.allForOne).output(Strings.allForOneChain).tier(B).type("keyblade").addMaterial(wellspring_crystal, 1).addMaterial(soothing_stone, 3).addMaterial(soothing_shard, 2);
        getBuilder(Strings.astralBlast).output(Strings.astralBlastChain).tier(S).type("keyblade").addMaterial(soothing_gem, 2).addMaterial(lucid_stone, 2).addMaterial(blazing_shard, 3);
        getBuilder(Strings.aubade).output(Strings.aubadeChain).tier(S).type("keyblade").addMaterial(blazing_stone, 1).addMaterial(blazing_shard, 2).addMaterial(blazing_crystal, 1);
        getBuilder(Strings.bondOfFlame).output(Strings.bondOfFlameChain).tier(B).type("keyblade").addMaterial(blazing_stone, 3).addMaterial(blazing_shard, 5).addMaterial(blazing_gem, 2);
        getBuilder(Strings.bondOfTheBlaze).output(Strings.bondOfTheBlazeChain).tier(B).type("keyblade").addMaterial(blazing_gem, 4).addMaterial(blazing_stone, 2).addMaterial(blazing_shard, 5);
        getBuilder(Strings.braveheart).output(Strings.braveheartChain).tier(D).type("keyblade").addMaterial(betwixt_crystal,1).addMaterial(pulsing_gem,2);
        getBuilder(Strings.brightcrest).output(Strings.brightcrestChain).tier(C).type("keyblade").addMaterial(soothing_gem, 2).addMaterial(soothing_stone, 3).addMaterial(soothing_crystal, 1);
        getBuilder(Strings.chaosRipper).output(Strings.chaosRipperChain).tier(SS).type("keyblade").addMaterial(lucid_crystal, 2).addMaterial(writhing_gem, 4).addMaterial(writhing_crystal, 3).addMaterial(betwixt_crystal, 3).addMaterial(hungry_gem, 4).addMaterial(orichalcum, 1);
        getBuilder(Strings.circleOfLife).output(Strings.circleOfLifeChain).tier(B).type("keyblade").addMaterial(soothing_gem, 3).addMaterial(pulsing_stone, 3).addMaterial(wellspring_shard, 3);
        getBuilder(Strings.classicTone).output(Strings.classicToneChain).tier(A).type("keyblade").addMaterial(soothing_gem, 3).addMaterial(sinister_stone, 3).addMaterial(wellspring_gem , 3);
        getBuilder(Strings.counterpoint).output(Strings.counterpointChain).tier(S).type("keyblade").addMaterial(hungry_stone, 1).addMaterial(wellspring_crystal, 1).addMaterial(soothing_shard, 2);
        getBuilder(Strings.crabclaw).output(Strings.crabclawChain).tier(C).type("keyblade").addMaterial(soothing_gem, 2).addMaterial(hungry_stone, 2).addMaterial(lucid_shard, 2).addMaterial(wellspring_stone, 1);
        getBuilder(Strings.crownOfGuilt).output(Strings.crownOfGuiltChain).tier(B).type("keyblade").addMaterial(tranquility_gem, 1).addMaterial(writhing_stone, 2).addMaterial(pulsing_crystal, 2);
        getBuilder(Strings.crystalSnow).output(Strings.crystalSnowChain).tier(D).type("keyblade").addMaterial(tranquility_gem, 1).addMaterial(frost_stone, 2).addMaterial(frost_crystal, 2);
        getBuilder(Strings.darkerThanDark).output(Strings.darkerThanDarkChain).tier(A).type("keyblade").addMaterial(lucid_crystal, 3).addMaterial(writhing_crystal, 2).addMaterial(writhing_gem, 2);
        getBuilder(Strings.darkgnaw).output(Strings.darkgnawChain).tier(B).type("keyblade").addMaterial(writhing_crystal, 5).addMaterial(lucid_shard, 5);
        getBuilder(Strings.dawnTillDusk).output(Strings.dawnTillDuskChain).tier(D).type("keyblade").addMaterial(blazing_crystal,1).addMaterial(blazing_gem,3).addMaterial(blazing_stone,2);
        getBuilder(Strings.deadOfNight).output(Strings.deadOfNightChain).tier(D).type("keyblade").addMaterial(betwixt_gem,1).addMaterial(betwixt_crystal,1).addMaterial(lucid_crystal,1);
        getBuilder(Strings.decisivePumpkin).output(Strings.decisivePumpkinChain).tier(S).type("keyblade").addMaterial(frost_crystal, 3).addMaterial(writhing_crystal, 3).addMaterial(orichalcum, 1).addMaterial(writhing_gem, 2);
        getBuilder(Strings.destinysEmbrace).output(Strings.destinysEmbraceChain).tier(D).type("keyblade").addMaterial(pulsing_stone, 3).addMaterial(lightning_stone, 5).addMaterial(soothing_crystal, 5).addMaterial(lightning_gem, 5);
        getBuilder(Strings.diamondDust).output(Strings.diamondDustChain).tier(C).type("keyblade").addMaterial(pulsing_stone, 1).addMaterial(frost_stone, 5).addMaterial(frost_gem, 3);
        getBuilder(Strings.divewing).output(Strings.divewingChain).tier(A).type("keyblade").addMaterial(wellspring_crystal, 3).addMaterial(twilight_crystal, 3).addMaterial(blazing_gem, 2).addMaterial(pulsing_crystal, 3);
        getBuilder(Strings.divineRose).output(Strings.divineRoseChain).tier(S).type("keyblade").addMaterial(soothing_gem, 4).addMaterial(pulsing_stone, 4).addMaterial(wellspring_crystal, 3).addMaterial(lucid_shard, 2);
        getBuilder(Strings.dualDisc).output(Strings.dualDiscChain).tier(B).type("keyblade").addMaterial(soothing_crystal, 2).addMaterial(pulsing_gem, 5).addMaterial(lightning_gem, 4);
        getBuilder(Strings.earthshaker).output(Strings.earthshakerChain).tier(D).type("keyblade").addMaterial(writhing_shard, 5).addMaterial(betwixt_stone, 3).addMaterial(wellspring_shard, 3);
        getBuilder(Strings.elementalEncoder).output(Strings.elementalEncoderChain).tier(D).type("keyblade").addMaterial(blazing_gem,1).addMaterial(frost_crystal,1).addMaterial(lightning_crystal,1);
        getBuilder(Strings.endOfPain).output(Strings.endOfPainChain).tier(S).type("keyblade").addMaterial(pulsing_stone, 3).addMaterial(wellspring_crystal, 2).addMaterial(writhing_shard, 3).addMaterial(writhing_gem, 3);
        getBuilder(Strings.endsOfTheEarth).output(Strings.endsOfTheEarthChain).tier(B).type("keyblade").addMaterial(pulsing_stone, 3).addMaterial(writhing_shard, 3).addMaterial(writhing_gem, 1).addMaterial(lucid_gem, 2);
        getBuilder(Strings.everAfter).output(Strings.everAfterChain).tier(D).type("keyblade").addMaterial(stormy_stone, 3).addMaterial(lightning_shard, 1).addMaterial(writhing_stone, 1).addMaterial(lucid_gem, 2);
        getBuilder(Strings.fairyHarp).output(Strings.fairyHarpChain).tier(B).type("keyblade").addMaterial(soothing_shard, 3).addMaterial(pulsing_gem, 2).addMaterial(lucid_gem, 2);
        getBuilder(Strings.fairyStars).output(Strings.fairyStarsChain).tier(D).type("keyblade").addMaterial(soothing_shard, 4).addMaterial(pulsing_gem, 3).addMaterial(wellspring_shard, 3);
        getBuilder(Strings.fatalCrest).output(Strings.fatalCrestChain).tier(C).type("keyblade").addMaterial(writhing_stone, 3).addMaterial(writhing_gem, 3).addMaterial(betwixt_stone, 3).addMaterial(lightning_shard, 1);
        getBuilder(Strings.favoriteDeputy).output(Strings.favoriteDeputyChain).tier(D).type("keyblade").addMaterial(lucid_crystal, 3).addMaterial(remembrance_gem, 3).addMaterial(betwixt_stone, 1);
        getBuilder(Strings.fenrir).output(Strings.fenrirChain).tier(SS).type("keyblade").addMaterial(twilight_gem, 3).addMaterial(pulsing_stone, 6).addMaterial(wellspring_crystal, 2).addMaterial(betwixt_shard, 2);
        getBuilder(Strings.ferrisGear).output(Strings.ferrisGearChain).tier(C).type("keyblade").addMaterial(soothing_gem, 2).addMaterial(lightning_stone, 2).addMaterial(lucid_gem, 2);
        getBuilder(Strings.followTheWind).output(Strings.followTheWindChain).tier(D).type("keyblade").addMaterial(writhing_stone, 3).addMaterial(pulsing_stone, 2).addMaterial(blazing_shard, 2);
        getBuilder(Strings.frolicFlame).output(Strings.frolicFlameChain).tier(C).type("keyblade").addMaterial(pulsing_stone, 1).addMaterial(blazing_shard, 3).addMaterial(blazing_gem, 2).addMaterial(blazing_crystal, 1);
        getBuilder(Strings.glimpseOfDarkness).output(Strings.glimpseOfDarknessChain).tier(D).type("keyblade").addMaterial(writhing_stone, 3).addMaterial(betwixt_crystal, 3).addMaterial(writhing_gem, 3).addMaterial(pulsing_crystal, 2);
        getBuilder(Strings.grandChef).output(Strings.grandChefChain).tier(D).type("keyblade").addMaterial(blazing_stone, 3).addMaterial(blazing_crystal, 1).addMaterial(blazing_shard, 2);
        getBuilder(Strings.guardianBell).output(Strings.guardianBellChain).tier(D).type("keyblade").addMaterial(writhing_stone, 3).addMaterial(lightning_shard, 3).addMaterial(wellspring_crystal, 2);
        getBuilder(Strings.guardianSoul).output(Strings.guardianSoulChain).tier(A).type("keyblade").addMaterial(pulsing_stone, 4).addMaterial(writhing_shard, 3).addMaterial(lightning_gem, 3).addMaterial(wellspring_stone, 1);
        getBuilder(Strings.gullWing).output(Strings.gullWingChain).tier(D).type("keyblade").addMaterial(wellspring_crystal, 2).addMaterial(blazing_gem, 2).addMaterial(pulsing_shard, 5);
        getBuilder(Strings.happyGear).output(Strings.happyGearChain).tier(C).type("keyblade").addMaterial(wellspring_crystal, 2).addMaterial(sinister_gem, 2).addMaterial(sinister_shard, 4);
        getBuilder(Strings.herosCrest).output(Strings.herosCrestChain).tier(B).type("keyblade").addMaterial(soothing_stone, 2).addMaterial(lightning_crystal, 2).addMaterial(pulsing_shard, 3).addMaterial(lightning_gem, 2);
        getBuilder(Strings.herosOrigin).output(Strings.herosOriginChain).tier(D).type("keyblade").addMaterial(soothing_stone, 2).addMaterial(lightning_shard, 2).addMaterial(pulsing_shard, 1);
        getBuilder(Strings.hiddenDragon).output(Strings.hiddenDragonChain).tier(D).type("keyblade").addMaterial(blazing_shard, 1).addMaterial(pulsing_stone, 3).addMaterial(mythril_crystal, 4).addMaterial(blazing_shard, 4);
        getBuilder(Strings.hunnySpout).output(Strings.hunnySpoutChain).tier(D).type("keyblade").addMaterial(blazing_shard, 1).addMaterial(pulsing_stone, 3).addMaterial(betwixt_crystal, 1);
        getBuilder(Strings.hyperdrive).output(Strings.hyperdriveChain).tier(C).type("keyblade").addMaterial(wellspring_crystal, 4).addMaterial(frost_stone, 2).addMaterial(lucid_gem, 2);
        getBuilder(Strings.incompleteKiblade).output(Strings.incompleteKibladeChain).tier(D).type("keyblade").addMaterial(writhing_crystal, 1).addMaterial(mythril_crystal, 1).addMaterial(twilight_crystal, 1).addMaterial(betwixt_crystal, 1).addMaterial(blazing_crystal, 1).addMaterial(pulsing_crystal, 1);
        getBuilder(Strings.jungleKing).output(Strings.jungleKingChain).tier(C).type("keyblade").addMaterial(wellspring_gem, 3).addMaterial(pulsing_stone, 4).addMaterial(betwixt_shard, 3);
        getBuilder(Strings.keybladeOfPeoplesHearts).output(Strings.keybladeOfPeoplesHeartsChain).tier(B).type("keyblade").addMaterial(writhing_crystal, 3).addMaterial(pulsing_stone, 2).addMaterial(lost_illusion, 1);
        // TODO finish the kiblade recipe
        getBuilder(Strings.kiblade).output(Strings.kibladeChain).tier(SSS).type("keyblade").addMaterial(incompleteKibladeChain, 1).addMaterial(orichalcum, 8).addMaterial(orichalcumplus, 5).addMaterial(blazing_crystal, 2).addMaterial(soothing_crystal, 2).addMaterial(writhing_crystal, 2).addMaterial(betwixt_crystal, 2).addMaterial(wellspring_crystal, 2).addMaterial(frost_crystal, 2).addMaterial(lightning_crystal, 2).addMaterial(lucid_crystal, 2).addMaterial(mythril_crystal, 2).addMaterial(pulsing_crystal, 2).addMaterial(remembrance_crystal,2).addMaterial(hungry_crystal,2).addMaterial(stormy_crystal,2).addMaterial(tranquility_crystal,2).addMaterial(twilight_crystal,2);
        getBuilder(Strings.kingdomKey).output(Strings.kingdomKeyChain).tier(D).type("keyblade").addMaterial(pulsing_stone, 1).addMaterial(pulsing_shard, 1);
        getBuilder(Strings.kingdomKeyD).output(Strings.kingdomKeyDChain).tier(D).type("keyblade").addMaterial(pulsing_gem, 1).addMaterial(pulsing_shard, 1);
        getBuilder(Strings.kingdomKeyN).output(Strings.kingdomKeyNChain).tier(D).type("keyblade").addMaterial(writhing_gem, 1).addMaterial(sinister_shard, 1);
        getBuilder(Strings.knockoutPunch).output(Strings.knockoutPunchChain).tier(A).type("keyblade").addMaterial(pulsing_stone, 2).addMaterial(wellspring_crystal, 2).addMaterial(soothing_stone, 2).addMaterial(mythril_gem, 1);
        getBuilder(Strings.ladyLuck).output(Strings.ladyLuckChain).tier(B).type("keyblade").addMaterial(pulsing_stone, 4).addMaterial(wellspring_crystal, 2).addMaterial(hungry_shard, 1).addMaterial(blazing_gem, 2);
        getBuilder(Strings.leviathan).output(Strings.leviathanChain).tier(C).type("keyblade").addMaterial(lucid_crystal, 2).addMaterial(frost_stone, 2).addMaterial(writhing_shard, 3);
        getBuilder(Strings.lionheart).output(Strings.lionheartChain).tier(A).type("keyblade").addMaterial(betwixt_crystal, 3).addMaterial(twilight_crystal, 3).addMaterial(blazing_gem, 5).addMaterial(pulsing_crystal, 2);
        getBuilder(Strings.longNight).output(Strings.longNightChain).tier(D).type("keyblade").addMaterial(lucid_shard, 3).addMaterial(writhing_shard, 2).addMaterial(sinister_shard, 2);
        getBuilder(Strings.lostMemory).output(Strings.lostMemoryChain).tier(A).type("keyblade").addMaterial(twilight_gem, 3).addMaterial(pulsing_stone, 2).addMaterial(hungry_shard, 1).addMaterial(mythril_gem, 3);
        getBuilder(Strings.lunarEclipse).output(Strings.lunarEclipseChain).tier(SS).type("keyblade").addMaterial(soothing_gem, 5).addMaterial(frost_gem, 2).addMaterial(writhing_gem, 5).addMaterial(pulsing_crystal, 2);
        getBuilder(Strings.markOfAHero).output(Strings.markOfAHeroChain).tier(B).type("keyblade").addMaterial(lightning_shard, 3).addMaterial(pulsing_stone, 2).addMaterial(soothing_crystal, 3).addMaterial(lightning_gem, 3);
        getBuilder(Strings.mastersDefender).output(Strings.mastersDefenderChain).tier(S).type("keyblade").addMaterial(twilight_gem, 10).addMaterial(mythril_crystal, 4).addMaterial(twilight_crystal, 7).addMaterial(pulsing_gem, 5);
        getBuilder(Strings.maverickFlare).output(Strings.maverickFlareChain).tier(S).type("keyblade").addMaterial(blazing_shard, 3).addMaterial(blazing_gem, 3).addMaterial(wellspring_stone, 3);
        getBuilder(Strings.metalChocobo).output(Strings.metalChocoboChain).tier(A).type("keyblade").addMaterial(lucid_crystal, 2).addMaterial(pulsing_stone, 5).addMaterial(betwixt_crystal, 3).addMaterial(wellspring_shard, 1);
        getBuilder(Strings.midnightBlue).output(Strings.midnightBlueChain).tier(D).type("keyblade").addMaterial(frost_crystal,1).addMaterial(pulsing_crystal,1);
        getBuilder(Strings.midnightRoar).output(Strings.midnightRoarChain).tier(A).type("keyblade").addMaterial(soothing_gem, 2).addMaterial(writhing_stone, 3).addMaterial(writhing_crystal, 2);
        getBuilder(Strings.mirageSplit).output(Strings.mirageSplitChain).tier(A).type("keyblade").addMaterial(writhing_stone, 2).addMaterial(writhing_crystal, 4).addMaterial(writhing_shard, 3).addMaterial(orichalcum, 1).addMaterial(writhing_gem, 6);
        getBuilder(Strings.missingAche).output(Strings.missingAcheChain).tier(D).type("keyblade").addMaterial(writhing_stone, 3).addMaterial(soothing_stone, 3).addMaterial(wellspring_shard, 2);
        getBuilder(Strings.monochrome).output(Strings.monochromeChain).tier(D).type("keyblade").addMaterial(lucid_stone, 3).addMaterial(writhing_stone, 2).addMaterial(pulsing_shard, 2);
        getBuilder(Strings.moogleOGlory).output(Strings.moogleOGloryChain).tier(S).type("keyblade").addMaterial(lucid_gem, 3).addMaterial(wellspring_stone, 3).addMaterial(pulsing_crystal, 5);
        getBuilder(Strings.mysteriousAbyss).output(Strings.mysteriousAbyssChain).tier(C).type("keyblade").addMaterial(frost_crystal, 1).addMaterial(frost_shard, 2).addMaterial(frost_stone, 5).addMaterial(frost_gem, 2);
        getBuilder(Strings.nanoGear).output(Strings.nanoGearChain).tier(D).type("keyblade").addMaterial(lightning_crystal, 1).addMaterial(lightning_shard, 2).addMaterial(remembrance_stone, 3).addMaterial(writhing_gem, 1);
        getBuilder(Strings.nightmaresEnd).output(Strings.nightmaresEndChain).tier(A).type("keyblade").addMaterial(soothing_gem, 6).addMaterial(soothing_stone, 2).addMaterial(orichalcum, 1).addMaterial(soothing_shard, 3).addMaterial(soothing_crystal, 4);
        getBuilder(Strings.nightmaresEndAndMirageSplit).output(Strings.nightmaresEndAndMirageSplitChain).tier(SS).type("keyblade").addMaterial(mirageSplitChain, 1).addMaterial(nightmaresEndChain, 1);
        getBuilder(Strings.noName).output(Strings.noNameChain).tier(SS).type("keyblade").addMaterial(frost_shard, 3).addMaterial(frost_gem, 2).addMaterial(writhing_gem, 3);
        getBuilder(Strings.noNameBBS).output(Strings.noNameBBSChain).tier(A).type("keyblade").addMaterial(frost_shard, 3).addMaterial(frost_gem, 2).addMaterial(writhing_gem, 3);
        getBuilder(Strings.oathkeeper).output(Strings.oathkeeperChain).tier(A).type("keyblade").addMaterial(mythril_crystal, 3).addMaterial(twilight_stone, 4).addMaterial(pulsing_gem, 3);
        getBuilder(Strings.oblivion).output(Strings.oblivionChain).tier(S).type("keyblade").addMaterial(writhing_crystal, 2).addMaterial(pulsing_stone, 5).addMaterial(writhing_gem, 4).addMaterial(betwixt_gem, 3);
        getBuilder(Strings.oceansRage).output(Strings.oceansRageChain).tier(C).type("keyblade").addMaterial(lightning_stone, 5).addMaterial(frost_shard, 5);
        getBuilder(Strings.olympia).output(Strings.olympiaChain).tier(A).type("keyblade").addMaterial(pulsing_stone, 2).addMaterial(soothing_shard, 2).addMaterial(lightning_crystal, 1).addMaterial(lightning_gem, 2);
        getBuilder(Strings.omegaWeapon).output(Strings.omegaWeaponChain).tier(S).type("keyblade").addMaterial(twilight_crystal, 1).addMaterial(mythril_gem, 2).addMaterial(writhing_gem, 2).addMaterial(pulsing_crystal, 3);
        getBuilder(Strings.ominousBlight).output(Strings.ominousBlightChain).tier(D).type("keyblade").addMaterial(writhing_stone, 2).addMaterial(soothing_stone, 2).addMaterial(pulsing_gem, 2);
        getBuilder(Strings.oneWingedAngel).output(Strings.oneWingedAngelChain).tier(B).type("keyblade").addMaterial(blazing_stone, 1).addMaterial(orichalcum, 1).addMaterial(blazing_gem, 3).addMaterial(blazing_crystal, 5);
        getBuilder(Strings.painOfSolitude).output(Strings.painOfSolitudeChain).tier(D).type("keyblade").addMaterial(writhing_crystal, 2).addMaterial(betwixt_shard, 2).addMaterial(twilight_stone, 1).addMaterial(pulsing_shard, 2);
        getBuilder(Strings.phantomGreen).output(Strings.phantomGreenChain).tier(D).type("keyblade").addMaterial(lightning_crystal,1).addMaterial(pulsing_crystal,1);
        getBuilder(Strings.photonDebugger).output(Strings.photonDebuggerChain).tier(C).type("keyblade").addMaterial(lightning_shard, 4).addMaterial(lightning_crystal, 2).addMaterial(lightning_gem, 3);
        getBuilder(Strings.pixiePetal).output(Strings.pixiePetalChain).tier(C).type("keyblade").addMaterial(lucid_stone, 2).addMaterial(soothing_shard, 2).addMaterial(pulsing_gem, 2);
        getBuilder(Strings.pumpkinhead).output(Strings.pumpkinheadChain).tier(B).type("keyblade").addMaterial(writhing_crystal, 2).addMaterial(pulsing_gem, 3).addMaterial(lucid_shard, 5);
        getBuilder(Strings.rainfell).output(Strings.rainfellChain).tier(D).type("keyblade").addMaterial(writhing_stone, 2).addMaterial(frost_stone, 1).addMaterial(lucid_shard, 5).addMaterial(stormy_gem, 1);
        getBuilder(Strings.rejectionOfFate).output(Strings.rejectionOfFateChain).tier(C).type("keyblade").addMaterial(twilight_gem, 2).addMaterial(writhing_stone, 2).addMaterial(twilight_crystal, 3);
        getBuilder(Strings.royalRadiance).output(Strings.royalRadianceChain).tier(SS).type("keyblade").addMaterial(pulsing_stone, 5).addMaterial(frost_shard, 2).addMaterial(soothing_stone, 3);
        getBuilder(Strings.rumblingRose).output(Strings.rumblingRoseChain).tier(A).type("keyblade").addMaterial(pulsing_stone, 4).addMaterial(soothing_shard, 2).addMaterial(lucid_gem, 3).addMaterial(wellspring_stone, 2);
        getBuilder(Strings.shootingStar).output(Strings.shootingStarChain).tier(D).type("keyblade").addMaterial(hungry_stone, 5).addMaterial(hungry_shard, 5).addMaterial(lucid_shard, 2).addMaterial(wellspring_stone, 2);
        getBuilder(Strings.signOfInnocence).output(Strings.signOfInnocenceChain).tier(B).type("keyblade").addMaterial(twilight_gem, 2).addMaterial(twilight_crystal, 1).addMaterial(writhing_shard, 3);
        getBuilder(Strings.silentDirge).output(Strings.silentDirgeChain).tier(B).type("keyblade").addMaterial(soothing_gem, 2).addMaterial(twilight_crystal, 2).addMaterial(writhing_shard, 1);
        getBuilder(Strings.skullNoise).output(Strings.skullNoiseChain).tier(D).type("keyblade").addMaterial(writhing_crystal, 3).addMaterial(lost_illusion, 1).addMaterial(writhing_gem, 2).addMaterial(wellspring_shard, 3);
        getBuilder(Strings.sleepingLion).output(Strings.sleepingLionChain).tier(A).type("keyblade").addMaterial(twilight_gem, 1).addMaterial(pulsing_stone, 2).addMaterial(tranquility_crystal, 2).addMaterial(blazing_shard, 4);
        getBuilder(Strings.soulEater).output(Strings.soulEaterChain).tier(D).type("keyblade").addMaterial(writhing_crystal, 3).addMaterial(pulsing_stone, 5).addMaterial(writhing_gem, 5).addMaterial(pulsing_crystal, 3);
        getBuilder(Strings.spellbinder).output(Strings.spellbinderChain).tier(D).type("keyblade").addMaterial(lucid_crystal, 2).addMaterial(frost_stone, 2).addMaterial(pulsing_gem, 2);
        getBuilder(Strings.starCluster).output(Strings.starClusterChain).tier(D).type("keyblade").addMaterial(twilight_crystal, 2).addMaterial(betwixt_stone, 2);
        getBuilder(Strings.starSeeker).output(Strings.starSeekerChain).tier(D).type("keyblade").addMaterial(twilight_stone, 5).addMaterial(betwixt_shard, 3).addMaterial(pulsing_shard, 2);
        getBuilder(Strings.starlight).output(Strings.starlightChain).tier(D).type("keyblade").addMaterial(mythril_stone, 3).addMaterial(mythril_crystal, 3).addMaterial(mythril_shard, 3).addMaterial(mythril_gem, 3);
        getBuilder(Strings.stormfall).output(Strings.stormfallChain).tier(B).type("keyblade").addMaterial(stormy_stone, 2).addMaterial(stormy_crystal, 1).addMaterial(soothing_stone, 3).addMaterial(writhing_gem, 2).addMaterial(pulsing_crystal, 3);
        getBuilder(Strings.strokeOfMidnight).output(Strings.strokeOfMidnightChain).tier(D).type("keyblade").addMaterial(pulsing_stone, 2).addMaterial(frost_shard, 2).addMaterial(betwixt_shard, 1).addMaterial(soothing_crystal, 3);
        getBuilder(Strings.sweetDreams).output(Strings.sweetDreamsChain).tier(S).type("keyblade").addMaterial(pulsing_stone, 2).addMaterial(wellspring_crystal, 2).addMaterial(twilight_shard, 4).addMaterial(betwixt_shard, 2);
        getBuilder(Strings.sweetMemories).output(Strings.sweetMemoriesChain).tier(C).type("keyblade").addMaterial(wellspring_crystal, 3).addMaterial(pulsing_stone, 2).addMaterial(lucid_shard, 4).addMaterial(remembrance_gem, 1);
        getBuilder(Strings.sweetstack).output(Strings.sweetstackChain).tier(A).type("keyblade").addMaterial(pulsing_stone, 4).addMaterial(blazing_crystal, 2).addMaterial(lucid_gem, 1).addMaterial(wellspring_shard, 3);
        getBuilder(Strings.threeWishes).output(Strings.threeWishesChain).tier(C).type("keyblade").addMaterial(lucid_gem, 3).addMaterial(wellspring_stone, 3).addMaterial(pulsing_crystal, 5);
        getBuilder(Strings.totalEclipse).output(Strings.totalEclipseChain).tier(B).type("keyblade").addMaterial(writhing_stone, 3).addMaterial(blazing_shard, 2).addMaterial(blazing_gem, 2);
        getBuilder(Strings.treasureTrove).output(Strings.treasureTroveChain).tier(D).type("keyblade").addMaterial(lucid_crystal, 2).addMaterial(frost_crystal, 2).addMaterial(writhing_crystal, 1).addMaterial(soothing_crystal, 2).addMaterial(blazing_crystal, 2);
        getBuilder(Strings.trueLightsFlight).output(Strings.trueLightsFlightChain).tier(C).type("keyblade").addMaterial(twilight_gem, 3).addMaterial(twilight_shard, 3).addMaterial(lost_illusion, 1);
        getBuilder(Strings.twilightBlaze).output(Strings.twilightBlazeChain).tier(SS).type("keyblade").addMaterial(twilight_crystal, 3).addMaterial(blazing_crystal, 3);
        getBuilder(Strings.twoBecomeOne).output(Strings.twoBecomeOneChain).tier(A).type("keyblade").addMaterial(twilight_gem, 2).addMaterial(twilight_crystal, 3).addMaterial(lost_illusion, 1);
        getBuilder(Strings.ultimaWeaponBBS).output(Strings.ultimaWeaponBBSChain).tier(SS).type("keyblade").addMaterial(mythril_crystal, 1).addMaterial(orichalcum, 1).addMaterial(orichalcumplus, 9).addMaterial(hungry_crystal, 5).addMaterial(lightning_gem, 1);
        getBuilder(Strings.ultimaWeaponDDD).output(Strings.ultimaWeaponDDDChain).tier(SS).type("keyblade").addMaterial(twilight_gem, 4).addMaterial(twilight_crystal, 3).addMaterial(betwixt_crystal, 2).addMaterial(orichalcum, 1).addMaterial(twilight_stone, 5);
        getBuilder(Strings.ultimaWeaponKH1).output(Strings.ultimaWeaponKH1Chain).tier(SS).type("keyblade").addMaterial(stormy_stone, 3).addMaterial(hungry_stone, 5).addMaterial(lost_illusion, 1).addMaterial(lightning_gem, 5).addMaterial(hungry_crystal, 5);
        getBuilder(Strings.ultimaWeaponKH2).output(Strings.ultimaWeaponKH2Chain).tier(SS).type("keyblade").addMaterial(mythril_crystal, 1).addMaterial(betwixt_crystal, 1).addMaterial(twilight_crystal, 1).addMaterial(orichalcum, 1).addMaterial(orichalcumplus, 13).addMaterial(hungry_crystal, 7);
        getBuilder(Strings.ultimaWeaponKH3).output(Strings.ultimaWeaponKH3Chain).tier(SS).type("keyblade").addMaterial(orichalcumplus, 7).addMaterial(wellspring_crystal, 2).addMaterial(lucid_crystal, 2).addMaterial(pulsing_crystal, 2);
        getBuilder(Strings.umbrella).output(Strings.umbrellaChain).tier(D).type("keyblade").addMaterial(twilight_shard, 1).addMaterial(mythril_shard, 10);
        getBuilder(Strings.unbound).output(Strings.unboundChain).tier(SS).type("keyblade").addMaterial(twilight_gem, 5).addMaterial(mythril_crystal, 3).addMaterial(betwixt_gem, 5).addMaterial(pulsing_crystal, 3);
        getBuilder(Strings.victoryLine).output(Strings.victoryLineChain).tier(C).type("keyblade").addMaterial(soothing_stone, 3).addMaterial(lucid_gem, 2).addMaterial(pulsing_crystal, 3);
        getBuilder(Strings.voidGear).output(Strings.voidGearChain).tier(SS).type("keyblade").addMaterial(writhing_shard, 1).addMaterial(sinister_crystal, 4).addMaterial(sinister_shard, 5).addMaterial(sinister_gem, 3).addMaterial(sinister_stone, 4);
        getBuilder(Strings.voidGearRemnant).output(Strings.voidGearRemnantChain).tier(SS).type("keyblade").addMaterial(writhing_shard, 1).addMaterial(sinister_crystal, 4).addMaterial(sinister_shard, 5).addMaterial(sinister_gem, 3).addMaterial(sinister_stone, 4);
        getBuilder(Strings.wayToTheDawn).output(Strings.wayToTheDawnChain).tier(D).type("keyblade").addMaterial(writhing_crystal, 1).addMaterial(hungry_gem, 4).addMaterial(twilight_crystal, 1).addMaterial(pulsing_crystal, 2);
        getBuilder(Strings.waywardWind).output(Strings.waywardWindChain).tier(D).type("keyblade").addMaterial(writhing_shard, 2).addMaterial(pulsing_shard, 2).addMaterial(stormy_shard, 1);
        getBuilder(Strings.wheelOfFate).output(Strings.wheelOfFateChain).tier(A).type("keyblade").addMaterial(writhing_shard, 2).addMaterial(pulsing_gem, 2).addMaterial(stormy_crystal, 3);
        getBuilder(Strings.wishingLamp).output(Strings.wishingLampChain).tier(B).type("keyblade").addMaterial(wellspring_stone, 3).addMaterial(lucid_crystal, 2).addMaterial(pulsing_crystal, 3).addMaterial(betwixt_gem, 2);
        getBuilder(Strings.winnersProof).output(Strings.winnersProofChain).tier(S).type("keyblade").addMaterial(writhing_stone, 3).addMaterial(tranquility_shard, 4).addMaterial(writhing_shard, 5);
        getBuilder(Strings.wishingStar).output(Strings.wishingStarChain).tier(C).type("keyblade").addMaterial(soothing_gem, 2).addMaterial(pulsing_stone, 2).addMaterial(mythril_crystal, 2);
        getBuilder(Strings.youngXehanortsKeyblade).output(Strings.youngXehanortsKeybladeChain).tier(SS).type("keyblade").addMaterial(lucid_crystal, 3).addMaterial(writhing_crystal, 10).addMaterial(frost_shard, 5).addMaterial(mythril_crystal, 3).addMaterial(writhing_gem, 10);
        getBuilder(Strings.zeroOne).output(Strings.zeroOneChain).tier(B).type("keyblade").addMaterial(lightning_crystal, 3).addMaterial(pulsing_gem, 4).addMaterial(lightning_stone, 2);

        // Items
        getBuilder(Strings.SM_MythrilCrystal).output(Strings.SM_MythrilCrystal).tier(C).type("item").addMaterial(betwixt_crystal, 1).addMaterial(betwixt_gem, 3).addMaterial(twilight_crystal, 1).addMaterial(twilight_gem, 3).addMaterial(hungry_stone, 1);
        getBuilder(Strings.SM_MythrilGem).output(Strings.SM_MythrilGem).tier(C).type("item").addMaterial(betwixt_crystal, 1).addMaterial(betwixt_gem, 3).addMaterial(twilight_crystal, 1).addMaterial(twilight_gem, 3);
        getBuilder(Strings.SM_MythrilStone).output(Strings.SM_MythrilStone).tier(D).type("item").addMaterial(betwixt_stone, 1).addMaterial(betwixt_shard, 3).addMaterial(twilight_stone, 1).addMaterial(twilight_shard, 3).addMaterial(hungry_shard, 1);
        getBuilder(Strings.SM_MythrilShard).output(Strings.SM_MythrilShard).tier(D).type("item").addMaterial(betwixt_stone, 1).addMaterial(betwixt_shard, 3).addMaterial(twilight_stone, 1).addMaterial(twilight_shard, 3);

        getBuilder(Strings.potion).output(Strings.potion, 1).cost(100).tier(D).type("item").addMaterial(lucid_shard, 2).addMaterial(soothing_shard, 2).addMaterial(pulsing_shard, 2);
        getBuilder(Strings.hiPotion).output(Strings.hiPotion, 1).cost(200).tier(D).type("item").addMaterial(lucid_stone, 3).addMaterial(soothing_stone, 3).addMaterial(pulsing_stone, 3).addMaterial(hungry_shard, 1);
        getBuilder(Strings.megaPotion).output(Strings.megaPotion, 1).cost(400).tier(C).type("item").addMaterial(lucid_gem, 3).addMaterial(soothing_gem, 3).addMaterial(pulsing_gem, 3).addMaterial(hungry_gem, 1);

        getBuilder(Strings.ether).output(Strings.ether, 1).cost(80).tier(D).type("item").addMaterial(wellspring_shard, 2).addMaterial(lightning_shard, 2).addMaterial(writhing_shard, 2);
        getBuilder(Strings.hiEther).output(Strings.hiEther, 1).cost(160).tier(D).type("item").addMaterial(pulsing_stone, 3).addMaterial(lightning_stone, 3).addMaterial(writhing_stone, 3).addMaterial(hungry_shard, 1);
        getBuilder(Strings.megaEther).output(Strings.megaEther, 1).cost(320).tier(C).type("item").addMaterial(pulsing_stone, 3).addMaterial(lightning_stone, 3).addMaterial(writhing_stone, 3).addMaterial(hungry_gem, 1);

        getBuilder(Strings.elixir).output(Strings.elixir, 1).cost(3000).tier(C).type("item").addMaterial(pulsing_crystal, 3).addMaterial(frost_gem, 2).addMaterial(sinister_stone, 2).addMaterial(sinister_shard, 2).addMaterial(hungry_gem, 2);
        getBuilder(Strings.megaLixir).output(Strings.megaLixir, 1).cost(6000).tier(B).type("item").addMaterial(pulsing_crystal, 3).addMaterial(frost_crystal, 2).addMaterial(sinister_crystal, 2).addMaterial(sinister_gem, 2).addMaterial(hungry_crystal, 2);

        getBuilder(Strings.driveRecovery).output(Strings.driveRecovery, 1).cost(400).tier(D).type("item").addMaterial(mythril_shard, 3).addMaterial(writhing_shard, 3).addMaterial(frost_shard, 1).addMaterial(lightning_shard, 1);
        getBuilder(Strings.hiDriveRecovery).output(Strings.hiDriveRecovery, 1).cost(800).tier(C).type("item").addMaterial(mythril_gem, 3).addMaterial(writhing_gem, 3).addMaterial(frost_gem, 1).addMaterial(lightning_gem, 1).addMaterial(hungry_crystal, 1);

        getBuilder(Strings.refocuser).output(Strings.refocuser, 1).cost(250).tier(D).type("item").addMaterial(blazing_shard, 3).addMaterial(lightning_shard, 3).addMaterial(lucid_shard, 3).addMaterial(hungry_shard, 1);
        getBuilder(Strings.hiRefocuser).output(Strings.hiRefocuser, 1).cost(500).tier(C).type("item").addMaterial(blazing_stone, 3).addMaterial(lightning_stone, 3).addMaterial(lucid_stone, 3).addMaterial(hungry_gem, 1);

        getBuilder(Strings.powerBoost).output(Strings.powerBoost, 1).cost(6000).tier(B).type("item").addMaterial(mythril_crystal, 1).addMaterial(blazing_crystal, 3).addMaterial(lightning_crystal, 3).addMaterial(lucid_crystal, 3).addMaterial(hungry_crystal, 1);
        getBuilder(Strings.magicBoost).output(Strings.magicBoost, 1).cost(6000).tier(B).type("item").addMaterial(mythril_gem, 1).addMaterial(pulsing_crystal, 3).addMaterial(writhing_crystal, 3).addMaterial(frost_crystal, 3).addMaterial(hungry_crystal, 1);
        getBuilder(Strings.defenseBoost).output(Strings.defenseBoost, 1).cost(4000).tier(B).type("item").addMaterial(mythril_crystal, 1).addMaterial(blazing_crystal, 3).addMaterial(lightning_crystal, 3).addMaterial(lucid_crystal, 3);
        getBuilder(Strings.apBoost).output(Strings.apBoost, 1).cost(4000).tier(C).type("item").addMaterial(mythril_gem, 1).addMaterial(pulsing_crystal, 3).addMaterial(writhing_crystal, 3).addMaterial(frost_crystal, 3);

        getBuilder(Strings.abilityRing).output(Strings.abilityRing, 1).cost(200).tier(D).type("item").addMaterial(betwixt_gem, 1).addMaterial(hungry_shard, 3);
        getBuilder(Strings.aquamarineRing).output(Strings.aquamarineRing, 1).cost(700).tier(D).type("item").addMaterial(writhing_stone, 1).addMaterial(soothing_shard, 5).addMaterial(blazing_shard, 2);
        getBuilder(Strings.executiveRing).output(Strings.executiveRing, 1).cost(1200).tier(B).type("item").addMaterial(mythril_crystal, 2).addMaterial(lightning_gem, 3).addMaterial(lightning_stone, 2);
        getBuilder(Strings.fullBloom).output(Strings.fullBloom, 1).cost(2200).tier(C).type("item").addMaterial(hungry_stone, 3).addMaterial(lost_illusion, 1).addMaterial(manifest_illusion, 1).addMaterial(tranquility_crystal, 1);
        getBuilder(Strings.fullBloomPlus).output(Strings.fullBloomPlus, 1).cost(2600).tier(B).type("item").addMaterial(hungry_stone, 3).addMaterial(lost_illusion, 1).addMaterial(manifest_illusion, 1).addMaterial(tranquility_crystal, 1).addMaterial(electrum, 1);
        getBuilder(Strings.shadowArchive).output(Strings.shadowArchive, 1).cost(2200).tier(C).type("item").addMaterial(hungry_stone, 3).addMaterial(lost_illusion, 1).addMaterial(manifest_illusion, 1).addMaterial(remembrance_crystal, 1);
        getBuilder(Strings.shadowArchivePlus).output(Strings.shadowArchivePlus, 1).cost(2600).tier(B).type("item").addMaterial(hungry_stone, 3).addMaterial(lost_illusion, 1).addMaterial(manifest_illusion, 1).addMaterial(remembrance_crystal, 1).addMaterial(electrum, 1);
        getBuilder(Strings.drawRing).output(Strings.drawRing, 1).cost(1200).tier(D).type("item").addMaterial(wellspring_crystal, 1).addMaterial(twilight_gem, 3).addMaterial(betwixt_stone, 3).addMaterial(remembrance_shard, 5);
        getBuilder(Strings.luckyRing).output(Strings.luckyRing, 1).cost(2600).tier(C).type("item").addMaterial(manifest_illusion, 1).addMaterial(remembrance_shard, 3).addMaterial(soothing_gem, 3).addMaterial(soothing_stone, 5).addMaterial(soothing_shard, 9).addMaterial(hungry_crystal, 1);
        getBuilder(Strings.starCharm).output(Strings.starCharm, 1).cost(2000).tier(A).type("item").addMaterial(mythril_crystal, 1).addMaterial(adamantite, 2).addMaterial(wellspring_crystal, 3).addMaterial(sinister_crystal, 3);
        getBuilder(Strings.cosmicArts).output(Strings.cosmicArts, 1).cost(1600).tier(S).type("item").addMaterial(mythril_stone, 2).addMaterial(pulsing_gem, 2).addMaterial(remembrance_shard, 2).addMaterial(twilight_stone, 4);
        getBuilder(Strings.mastersRing).output(Strings.mastersRing, 1).cost(2000).tier(A).type("item").addMaterial(mythril_crystal, 2).addMaterial(writhing_crystal, 1).addMaterial(hungry_gem, 1);
        getBuilder(Strings.cosmicRing).output(Strings.cosmicRing, 1).cost(2000).tier(S).type("item").addMaterial(mythril_crystal, 2).addMaterial(writhing_crystal, 1).addMaterial(hungry_crystal, 1).addMaterial(evanescent_crystal, 1);
        getBuilder(Strings.slayerEarring).output(Strings.slayerEarring, 1).cost(2000).tier(A).type("item").addMaterial(mythril_gem, 3).addMaterial(pulsing_gem, 2).addMaterial(hungry_stone, 1).addMaterial(evanescent_crystal, 1);
        getBuilder(Strings.fencerEarring).output(Strings.fencerEarring, 1).cost(2000).tier(A).type("item").addMaterial(mythril_gem, 3).addMaterial(writhing_gem, 2).addMaterial(hungry_stone, 1).addMaterial(evanescent_crystal, 1);

        getBuilder(Strings.busterBand).output(Strings.busterBand, 1).cost(5000).tier(S).type("item").addMaterial(pulsing_crystal, 2).addMaterial(pulsing_stone, 3).addMaterial(mythril_crystal, 2);
        getBuilder(Strings.cosmicBelt).output(Strings.cosmicBelt, 1).cost(6000).tier(SS).type("item").addMaterial(pulsing_shard, 6).addMaterial(pulsing_gem, 3).addMaterial(mythril_crystal, 3).addMaterial(illusory_crystal, 1);
        getBuilder(Strings.acrisiusPlus).output(Strings.acrisiusPlus, 1).cost(3500).tier(A).type("item").addMaterial(mythril_gem, 2).addMaterial(remembrance_shard, 6).addMaterial(wellspring_crystal, 3);
        getBuilder(Strings.cosmicChain).output(Strings.cosmicChain, 1).cost(5000).tier(S).type("item").addMaterial(mythril_stone, 5).addMaterial(hungry_gem, 4).addMaterial(soothing_crystal, 3).addMaterial(illusory_crystal, 1);
        getBuilder(Strings.firagunBangle).output(Strings.firagunBangle, 1).cost(6000).tier(A).type("item").addMaterial(blazing_crystal, 3).addMaterial(blazing_gem, 4).addMaterial(blazing_shard, 2).addMaterial(blazing_stone, 4).addMaterial(illusory_crystal, 1);
        getBuilder(Strings.blizzagunArmlet).output(Strings.blizzagunArmlet, 1).cost(6000).tier(A).type("item").addMaterial(frost_crystal, 3).addMaterial(frost_gem, 4).addMaterial(frost_shard, 2).addMaterial(frost_stone, 4).addMaterial(illusory_crystal, 1);
        getBuilder(Strings.thundagunTrinket).output(Strings.thundagunTrinket, 1).cost(6000).tier(A).type("item").addMaterial(lightning_crystal, 3).addMaterial(lightning_gem, 4).addMaterial(lightning_shard, 2).addMaterial(lightning_stone, 4).addMaterial(illusory_crystal, 1);
        getBuilder(Strings.shockCharm).output(Strings.shockCharm, 1).cost(4600).tier(B).type("item").addMaterial(lightning_crystal, 1).addMaterial(lightning_gem, 2).addMaterial(pulsing_crystal, 1).addMaterial(lightning_stone, 3);
        getBuilder(Strings.shockCharmPlus).output(Strings.shockCharmPlus, 1).cost(6400).tier(A).type("item").addMaterial(lightning_crystal, 2).addMaterial(lightning_gem, 3).addMaterial(pulsing_crystal, 2).addMaterial(lightning_stone, 4).addMaterial(illusory_crystal, 1);
        getBuilder(Strings.chaosAnklet).output(Strings.chaosAnklet, 1).cost(6000).tier(A).type("item").addMaterial(writhing_crystal, 3).addMaterial(writhing_gem, 4).addMaterial(writhing_shard, 2).addMaterial(writhing_stone, 4).addMaterial(illusory_crystal, 1);
        getBuilder(Strings.gaiaBelt).output(Strings.gaiaBelt, 1).cost(6000).tier(A).type("item").addMaterial(writhing_gem, 3).addMaterial(lightning_gem, 3).addMaterial(lightning_stone, 5).addMaterial(writhing_stone, 4).addMaterial(illusory_crystal, 1);


    }
}
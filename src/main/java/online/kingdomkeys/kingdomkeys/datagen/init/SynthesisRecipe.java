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

    @Override
    protected void registerRecipe() {
        // Keyblades
        getBuilder(Strings.abaddonPlasma).output(Strings.abaddonPlasmaChain, 1).addTier(4).addType("keyblade").addMaterial(soothing_gem, 2).addMaterial(writhing_stone, 4).addMaterial(soothing_crystal, 3);
        getBuilder(Strings.abyssalTide).output(Strings.abyssalTideChain, 1).addTier(2).addType("keyblade").addMaterial(pulsing_stone, 3).addMaterial(frost_shard, 3);
        getBuilder(Strings.acedsKeyblade).output(Strings.acedsKeybladeChain, 1).addTier(5).addType("keyblade").addMaterial(manifest_illusion, 1).addMaterial(lost_illusion, 1).addMaterial(tranquility_crystal, 6).addMaterial(tranquility_stone, 3).addMaterial(tranquility_gem, 4).addMaterial(tranquility_shard, 2);
        getBuilder(Strings.adventRed).output(Strings.adventRedChain, 1).addTier(1).addType("keyblade").addMaterial(blazing_crystal,1).addMaterial(pulsing_crystal,1);
        getBuilder(Strings.allForOne).output(Strings.allForOneChain, 1).addTier(3).addType("keyblade").addMaterial(wellspring_crystal, 1).addMaterial(soothing_stone, 3).addMaterial(soothing_shard, 2);
        getBuilder(Strings.astralBlast).output(Strings.astralBlastChain, 1).addTier(5).addType("keyblade").addMaterial(soothing_gem, 2).addMaterial(lucid_stone, 2).addMaterial(blazing_shard, 3);
        getBuilder(Strings.aubade).output(Strings.aubadeChain, 1).addTier(5).addType("keyblade").addMaterial(blazing_stone, 1).addMaterial(blazing_shard, 2).addMaterial(blazing_crystal, 1);
        getBuilder(Strings.avasKeyblade).output(Strings.avasKeybladeChain, 1).addTier(5).addType("keyblade").addMaterial(lost_illusion, 1).addMaterial(betwixt_crystal, 6).addMaterial(betwixt_stone, 3).addMaterial(betwixt_gem, 4).addMaterial(betwixt_shard, 2);
        getBuilder(Strings.bondOfFlame).output(Strings.bondOfFlameChain, 1).addTier(3).addType("keyblade").addMaterial(blazing_stone, 3).addMaterial(blazing_shard, 5).addMaterial(blazing_gem, 2);
        getBuilder(Strings.bondOfTheBlaze).output(Strings.bondOfTheBlazeChain, 1).addTier(3).addType("keyblade").addMaterial(blazing_gem, 4).addMaterial(blazing_stone, 2).addMaterial(blazing_shard, 5);
        getBuilder(Strings.braveheart).output(Strings.braveheartChain, 1).addTier(1).addType("keyblade").addMaterial(betwixt_crystal,1).addMaterial(pulsing_gem,2);
        getBuilder(Strings.brightcrest).output(Strings.brightcrestChain, 1).addTier(2).addType("keyblade").addMaterial(soothing_gem, 2).addMaterial(soothing_stone, 3).addMaterial(soothing_crystal, 1);
        getBuilder(Strings.chaosRipper).output(Strings.chaosRipperChain, 1).addTier(6).addType("keyblade").addMaterial(lucid_crystal, 2).addMaterial(writhing_gem, 4).addMaterial(writhing_crystal, 3).addMaterial(betwixt_crystal, 3).addMaterial(hungry_gem, 4).addMaterial(orichalcum, 1);
        getBuilder(Strings.circleOfLife).output(Strings.circleOfLifeChain, 1).addTier(3).addType("keyblade").addMaterial(soothing_gem, 3).addMaterial(pulsing_stone, 3).addMaterial(wellspring_shard, 3);
        getBuilder(Strings.classicTone).output(Strings.classicToneChain, 1).addTier(4).addType("keyblade").addMaterial(soothing_gem, 3).addMaterial(sinister_stone, 3).addMaterial(wellspring_gem , 3);
        getBuilder(Strings.counterpoint).output(Strings.counterpointChain, 1).addTier(5).addType("keyblade").addMaterial(hungry_stone, 1).addMaterial(wellspring_crystal, 1).addMaterial(soothing_shard, 2);
        getBuilder(Strings.crabclaw).output(Strings.crabclawChain, 1).addTier(2).addType("keyblade").addMaterial(soothing_gem, 2).addMaterial(hungry_stone, 2).addMaterial(lucid_shard, 2).addMaterial(wellspring_stone, 1);
        getBuilder(Strings.crownOfGuilt).output(Strings.crownOfGuiltChain, 1).addTier(3).addType("keyblade").addMaterial(tranquility_gem, 1).addMaterial(writhing_stone, 2).addMaterial(pulsing_crystal, 2);
        getBuilder(Strings.crystalSnow).output(Strings.crystalSnowChain, 1).addTier(1).addType("keyblade").addMaterial(tranquility_gem, 1).addMaterial(frost_stone, 2).addMaterial(frost_crystal, 2);
        getBuilder(Strings.darkerThanDark).output(Strings.darkerThanDarkChain, 1).addTier(4).addType("keyblade").addMaterial(lucid_crystal, 3).addMaterial(writhing_crystal, 2).addMaterial(writhing_gem, 2);
        getBuilder(Strings.darkgnaw).output(Strings.darkgnawChain, 1).addTier(3).addType("keyblade").addMaterial(writhing_crystal, 5).addMaterial(lucid_shard, 5);
        getBuilder(Strings.dawnTillDusk).output(Strings.dawnTillDuskChain, 1).addTier(1).addType("keyblade").addMaterial(blazing_crystal,1).addMaterial(blazing_gem,3).addMaterial(blazing_stone,2);
        getBuilder(Strings.deadOfNight).output(Strings.deadOfNightChain, 1).addTier(1).addType("keyblade").addMaterial(betwixt_gem,1).addMaterial(betwixt_crystal,1).addMaterial(lucid_crystal,1);
        getBuilder(Strings.decisivePumpkin).output(Strings.decisivePumpkinChain, 1).addTier(5).addType("keyblade").addMaterial(frost_crystal, 3).addMaterial(writhing_crystal, 3).addMaterial(orichalcum, 1).addMaterial(writhing_gem, 2);
        getBuilder(Strings.destinysEmbrace).output(Strings.destinysEmbraceChain, 1).addTier(1).addType("keyblade").addMaterial(pulsing_stone, 3).addMaterial(lightning_stone, 5).addMaterial(soothing_crystal, 5).addMaterial(lightning_gem, 5);
        getBuilder(Strings.diamondDust).output(Strings.diamondDustChain, 1).addTier(2).addType("keyblade").addMaterial(pulsing_stone, 1).addMaterial(frost_stone, 5).addMaterial(frost_gem, 3);
        getBuilder(Strings.divewing).output(Strings.divewingChain, 1).addTier(4).addType("keyblade").addMaterial(wellspring_crystal, 3).addMaterial(twilight_crystal, 3).addMaterial(blazing_gem, 2).addMaterial(pulsing_crystal, 3);
        getBuilder(Strings.divineRose).output(Strings.divineRoseChain, 1).addTier(5).addType("keyblade").addMaterial(soothing_gem, 4).addMaterial(pulsing_stone, 4).addMaterial(wellspring_crystal, 3).addMaterial(lucid_shard, 2);
        getBuilder(Strings.dualDisc).output(Strings.dualDiscChain, 1).addTier(3).addType("keyblade").addMaterial(soothing_crystal, 2).addMaterial(pulsing_gem, 5).addMaterial(lightning_gem, 4);
        getBuilder(Strings.earthshaker).output(Strings.earthshakerChain, 1).addTier(1).addType("keyblade").addMaterial(writhing_shard, 5).addMaterial(betwixt_stone, 3).addMaterial(wellspring_shard, 3);
        getBuilder(Strings.elementalEncoder).output(Strings.elementalEncoderChain, 1).addTier(1).addType("keyblade").addMaterial(blazing_gem,1).addMaterial(frost_crystal,1).addMaterial(lightning_crystal,1);
        getBuilder(Strings.endOfPain).output(Strings.endOfPainChain, 1).addTier(5).addType("keyblade").addMaterial(pulsing_stone, 3).addMaterial(wellspring_crystal, 2).addMaterial(writhing_shard, 3).addMaterial(writhing_gem, 3);
        getBuilder(Strings.endsOfTheEarth).output(Strings.endsOfTheEarthChain, 1).addTier(3).addType("keyblade").addMaterial(pulsing_stone, 3).addMaterial(writhing_shard, 3).addMaterial(writhing_gem, 1).addMaterial(lucid_gem, 2);
        getBuilder(Strings.everAfter).output(Strings.everAfterChain, 1).addTier(1).addType("keyblade").addMaterial(stormy_stone, 3).addMaterial(lightning_shard, 1).addMaterial(writhing_stone, 1).addMaterial(lucid_gem, 2);
        getBuilder(Strings.fairyHarp).output(Strings.fairyHarpChain, 1).addTier(3).addType("keyblade").addMaterial(soothing_shard, 3).addMaterial(pulsing_gem, 2).addMaterial(lucid_gem, 2);
        getBuilder(Strings.fairyStars).output(Strings.fairyStarsChain, 1).addTier(1).addType("keyblade").addMaterial(soothing_shard, 4).addMaterial(pulsing_gem, 3).addMaterial(wellspring_shard, 3);
        getBuilder(Strings.fatalCrest).output(Strings.fatalCrestChain, 1).addTier(2).addType("keyblade").addMaterial(writhing_stone, 3).addMaterial(writhing_gem, 3).addMaterial(betwixt_stone, 3).addMaterial(lightning_shard, 1);
        getBuilder(Strings.favoriteDeputy).output(Strings.favoriteDeputyChain, 1).addTier(1).addType("keyblade").addMaterial(lucid_crystal, 3).addMaterial(remembrance_gem, 3).addMaterial(betwixt_stone, 1);
        getBuilder(Strings.fenrir).output(Strings.fenrirChain, 1).addTier(6).addType("keyblade").addMaterial(twilight_gem, 3).addMaterial(pulsing_stone, 6).addMaterial(wellspring_crystal, 2).addMaterial(betwixt_shard, 2);
        getBuilder(Strings.ferrisGear).output(Strings.ferrisGearChain, 1).addTier(2).addType("keyblade").addMaterial(soothing_gem, 2).addMaterial(lightning_stone, 2).addMaterial(lucid_gem, 2);
        getBuilder(Strings.followTheWind).output(Strings.followTheWindChain, 1).addTier(1).addType("keyblade").addMaterial(writhing_stone, 3).addMaterial(pulsing_stone, 2).addMaterial(blazing_shard, 2);
        getBuilder(Strings.frolicFlame).output(Strings.frolicFlameChain, 1).addTier(2).addType("keyblade").addMaterial(pulsing_stone, 1).addMaterial(blazing_shard, 3).addMaterial(blazing_gem, 2).addMaterial(blazing_crystal, 1);
        getBuilder(Strings.glimpseOfDarkness).output(Strings.glimpseOfDarknessChain, 1).addTier(1).addType("keyblade").addMaterial(writhing_stone, 3).addMaterial(betwixt_crystal, 3).addMaterial(writhing_gem, 3).addMaterial(pulsing_crystal, 2);
        getBuilder(Strings.grandChef).output(Strings.grandChefChain, 1).addTier(1).addType("keyblade").addMaterial(blazing_stone, 3).addMaterial(blazing_crystal, 1).addMaterial(blazing_shard, 2);
        getBuilder(Strings.guardianBell).output(Strings.guardianBellChain, 1).addTier(1).addType("keyblade").addMaterial(writhing_stone, 3).addMaterial(lightning_shard, 3).addMaterial(wellspring_crystal, 2);
        getBuilder(Strings.guardianSoul).output(Strings.guardianSoulChain, 1).addTier(4).addType("keyblade").addMaterial(pulsing_stone, 4).addMaterial(writhing_shard, 3).addMaterial(lightning_gem, 3).addMaterial(wellspring_stone, 1);
        getBuilder(Strings.gulasKeyblade).output(Strings.gulasKeybladeChain, 1).addTier(5).addType("keyblade").addMaterial(manifest_illusion, 1).addMaterial(lost_illusion, 1).addMaterial(hungry_crystal, 6).addMaterial(hungry_stone, 3).addMaterial(hungry_gem, 4).addMaterial(hungry_shard, 2);
        getBuilder(Strings.gullWing).output(Strings.gullWingChain, 1).addTier(1).addType("keyblade").addMaterial(wellspring_crystal, 2).addMaterial(blazing_gem, 2).addMaterial(pulsing_shard, 5);
        getBuilder(Strings.happyGear).output(Strings.happyGearChain, 1).addTier(2).addType("keyblade").addMaterial(wellspring_crystal, 2).addMaterial(sinister_gem, 2).addMaterial(sinister_shard, 4);
        getBuilder(Strings.herosCrest).output(Strings.herosCrestChain, 1).addTier(3).addType("keyblade").addMaterial(soothing_stone, 2).addMaterial(lightning_crystal, 2).addMaterial(pulsing_shard, 3).addMaterial(lightning_gem, 2);
        getBuilder(Strings.herosOrigin).output(Strings.herosOriginChain, 1).addTier(1).addType("keyblade").addMaterial(soothing_stone, 2).addMaterial(lightning_shard, 2).addMaterial(pulsing_shard, 1);
        getBuilder(Strings.hiddenDragon).output(Strings.hiddenDragonChain, 1).addTier(1).addType("keyblade").addMaterial(blazing_shard, 1).addMaterial(pulsing_stone, 3).addMaterial(mythril_crystal, 4).addMaterial(blazing_shard, 4);
        getBuilder(Strings.hunnySpout).output(Strings.hunnySpoutChain, 1).addTier(1).addType("keyblade").addMaterial(blazing_shard, 1).addMaterial(pulsing_stone, 3).addMaterial(betwixt_crystal, 1);
        getBuilder(Strings.hyperdrive).output(Strings.hyperdriveChain, 1).addTier(2).addType("keyblade").addMaterial(wellspring_crystal, 4).addMaterial(frost_stone, 2).addMaterial(lucid_gem, 2);
        getBuilder(Strings.incompleteKiblade).output(Strings.incompleteKibladeChain, 1).addTier(1).addType("keyblade").addMaterial(writhing_crystal, 1).addMaterial(mythril_crystal, 1).addMaterial(twilight_crystal, 1).addMaterial(betwixt_crystal, 1).addMaterial(blazing_crystal, 1).addMaterial(pulsing_crystal, 1);
        getBuilder(Strings.invisKeyblade).output(Strings.invisKeybladeChain, 1).addTier(5).addType("keyblade").addMaterial(lost_illusion, 1).addMaterial(lucid_crystal, 6).addMaterial(lucid_stone, 3).addMaterial(lucid_gem, 4).addMaterial(lucid_shard, 2);
        getBuilder(Strings.irasKeyblade).output(Strings.irasKeybladeChain, 1).addTier(5).addType("keyblade").addMaterial(lost_illusion, 1).addMaterial(pulsing_crystal, 6).addMaterial(pulsing_stone, 3).addMaterial(pulsing_gem, 4).addMaterial(pulsing_shard, 2);
        getBuilder(Strings.jungleKing).output(Strings.jungleKingChain, 1).addTier(2).addType("keyblade").addMaterial(wellspring_gem, 3).addMaterial(pulsing_stone, 4).addMaterial(betwixt_shard, 3);
        getBuilder(Strings.keybladeOfPeoplesHearts).output(Strings.keybladeOfPeoplesHeartsChain, 1).addTier(3).addType("keyblade").addMaterial(writhing_crystal, 3).addMaterial(pulsing_stone, 2).addMaterial(lost_illusion, 1);
        // TODO finish the kiblade recipe
        getBuilder(Strings.kiblade).output(Strings.kibladeChain, 1).addTier(7).addType("keyblade").addMaterial(incompleteKibladeChain, 1).addMaterial(orichalcum, 8).addMaterial(orichalcumplus, 5).addMaterial(blazing_crystal, 2).addMaterial(soothing_crystal, 2).addMaterial(writhing_crystal, 2).addMaterial(betwixt_crystal, 2).addMaterial(wellspring_crystal, 2).addMaterial(frost_crystal, 2).addMaterial(lightning_crystal, 2).addMaterial(lucid_crystal, 2).addMaterial(mythril_crystal, 2).addMaterial(pulsing_crystal, 2).addMaterial(remembrance_crystal,2).addMaterial(hungry_crystal,2).addMaterial(stormy_crystal,2).addMaterial(tranquility_crystal,2).addMaterial(twilight_crystal,2);
        getBuilder(Strings.kingdomKey).output(Strings.kingdomKeyChain, 1).addTier(1).addType("keyblade").addMaterial(pulsing_stone, 1).addMaterial(pulsing_shard, 1);
        getBuilder(Strings.kingdomKeyD).output(Strings.kingdomKeyDChain, 1).addTier(1).addType("keyblade").addMaterial(pulsing_gem, 1).addMaterial(pulsing_shard, 1);
        getBuilder(Strings.kingdomKeyN).output(Strings.kingdomKeyNChain, 1).addTier(1).addType("keyblade").addMaterial(writhing_gem, 1).addMaterial(sinister_shard, 1);
        getBuilder(Strings.knockoutPunch).output(Strings.knockoutPunchChain, 1).addTier(4).addType("keyblade").addMaterial(pulsing_stone, 2).addMaterial(wellspring_crystal, 2).addMaterial(soothing_stone, 2).addMaterial(mythril_gem, 1);
        getBuilder(Strings.ladyLuck).output(Strings.ladyLuckChain, 1).addTier(3).addType("keyblade").addMaterial(pulsing_stone, 4).addMaterial(wellspring_crystal, 2).addMaterial(hungry_shard, 1).addMaterial(blazing_gem, 2);
        getBuilder(Strings.leviathan).output(Strings.leviathanChain, 1).addTier(2).addType("keyblade").addMaterial(lucid_crystal, 2).addMaterial(frost_stone, 2).addMaterial(writhing_shard, 3);
        getBuilder(Strings.lionheart).output(Strings.lionheartChain, 1).addTier(4).addType("keyblade").addMaterial(betwixt_crystal, 3).addMaterial(twilight_crystal, 3).addMaterial(blazing_gem, 5).addMaterial(pulsing_crystal, 2);
        getBuilder(Strings.lostMemory).output(Strings.lostMemoryChain, 1).addTier(4).addType("keyblade").addMaterial(twilight_gem, 3).addMaterial(pulsing_stone, 2).addMaterial(hungry_shard, 1).addMaterial(mythril_gem, 3);
        getBuilder(Strings.lunarEclipse).output(Strings.lunarEclipseChain, 1).addTier(6).addType("keyblade").addMaterial(soothing_gem, 5).addMaterial(frost_gem, 2).addMaterial(writhing_gem, 5).addMaterial(pulsing_crystal, 2);
        getBuilder(Strings.markOfAHero).output(Strings.markOfAHeroChain, 1).addTier(3).addType("keyblade").addMaterial(lightning_shard, 3).addMaterial(pulsing_stone, 2).addMaterial(soothing_crystal, 3).addMaterial(lightning_gem, 3);
        getBuilder(Strings.mastersDefender).output(Strings.mastersDefenderChain, 1).addTier(5).addType("keyblade").addMaterial(twilight_gem, 10).addMaterial(mythril_crystal, 4).addMaterial(twilight_crystal, 7).addMaterial(pulsing_gem, 5);
        getBuilder(Strings.maverickFlare).output(Strings.maverickFlareChain, 1).addTier(5).addType("keyblade").addMaterial(blazing_shard, 3).addMaterial(blazing_gem, 3).addMaterial(wellspring_stone, 3);
        getBuilder(Strings.metalChocobo).output(Strings.metalChocoboChain, 1).addTier(4).addType("keyblade").addMaterial(lucid_crystal, 2).addMaterial(pulsing_stone, 5).addMaterial(betwixt_crystal, 3).addMaterial(wellspring_shard, 1);
        getBuilder(Strings.midnightBlue).output(Strings.midnightBlueChain, 1).addTier(1).addType("keyblade").addMaterial(frost_crystal,1).addMaterial(pulsing_crystal,1);
        getBuilder(Strings.midnightRoar).output(Strings.midnightRoarChain, 1).addTier(4).addType("keyblade").addMaterial(soothing_gem, 2).addMaterial(writhing_stone, 3).addMaterial(writhing_crystal, 2);
        getBuilder(Strings.mirageSplit).output(Strings.mirageSplitChain, 1).addTier(4).addType("keyblade").addMaterial(writhing_stone, 2).addMaterial(writhing_crystal, 4).addMaterial(writhing_shard, 3).addMaterial(orichalcum, 1).addMaterial(writhing_gem, 6);
        getBuilder(Strings.missingAche).output(Strings.missingAcheChain, 1).addTier(1).addType("keyblade").addMaterial(writhing_stone, 3).addMaterial(soothing_stone, 3).addMaterial(wellspring_shard, 2);
        getBuilder(Strings.monochrome).output(Strings.monochromeChain, 1).addTier(1).addType("keyblade").addMaterial(lucid_stone, 3).addMaterial(writhing_stone, 2).addMaterial(pulsing_shard, 2);
        getBuilder(Strings.moogleOGlory).output(Strings.moogleOGloryChain, 1).addTier(5).addType("keyblade").addMaterial(lucid_gem, 3).addMaterial(wellspring_stone, 3).addMaterial(pulsing_crystal, 5);
        getBuilder(Strings.mysteriousAbyss).output(Strings.mysteriousAbyssChain, 1).addTier(2).addType("keyblade").addMaterial(frost_crystal, 1).addMaterial(frost_shard, 2).addMaterial(frost_stone, 5).addMaterial(frost_gem, 2);
        getBuilder(Strings.nanoGear).output(Strings.nanoGearChain, 1).addTier(1).addType("keyblade").addMaterial(lightning_crystal, 1).addMaterial(lightning_shard, 2).addMaterial(remembrance_stone, 3).addMaterial(writhing_gem, 1);
        getBuilder(Strings.nightmaresEnd).output(Strings.nightmaresEndChain, 1).addTier(4).addType("keyblade").addMaterial(soothing_gem, 6).addMaterial(soothing_stone, 2).addMaterial(orichalcum, 1).addMaterial(soothing_shard, 3).addMaterial(soothing_crystal, 4);
        getBuilder(Strings.nightmaresEndAndMirageSplit).output(Strings.nightmaresEndAndMirageSplitChain, 1).addTier(6).addType("keyblade").addMaterial(mirageSplitChain, 1).addMaterial(nightmaresEndChain, 1);
        getBuilder(Strings.noName).output(Strings.noNameChain, 1).addTier(6).addType("keyblade").addMaterial(frost_shard, 3).addMaterial(frost_gem, 2).addMaterial(writhing_gem, 3);
        getBuilder(Strings.noNameBBS).output(Strings.noNameBBSChain, 1).addTier(4).addType("keyblade").addMaterial(frost_shard, 3).addMaterial(frost_gem, 2).addMaterial(writhing_gem, 3);
        getBuilder(Strings.oathkeeper).output(Strings.oathkeeperChain, 1).addTier(4).addType("keyblade").addMaterial(mythril_crystal, 3).addMaterial(twilight_stone, 4).addMaterial(pulsing_gem, 3);
        getBuilder(Strings.oblivion).output(Strings.oblivionChain, 1).addTier(5).addType("keyblade").addMaterial(writhing_crystal, 2).addMaterial(pulsing_stone, 5).addMaterial(writhing_gem, 4).addMaterial(betwixt_gem, 3);
        getBuilder(Strings.oceansRage).output(Strings.oceansRageChain, 1).addTier(2).addType("keyblade").addMaterial(lightning_stone, 5).addMaterial(frost_shard, 5);
        getBuilder(Strings.olympia).output(Strings.olympiaChain, 1).addTier(4).addType("keyblade").addMaterial(pulsing_stone, 2).addMaterial(soothing_shard, 2).addMaterial(lightning_crystal, 1).addMaterial(lightning_gem, 2);
        getBuilder(Strings.omegaWeapon).output(Strings.omegaWeaponChain, 1).addTier(5).addType("keyblade").addMaterial(twilight_crystal, 1).addMaterial(mythril_gem, 2).addMaterial(writhing_gem, 2).addMaterial(pulsing_crystal, 3);
        getBuilder(Strings.ominousBlight).output(Strings.ominousBlightChain, 1).addTier(1).addType("keyblade").addMaterial(writhing_stone, 2).addMaterial(soothing_stone, 2).addMaterial(pulsing_gem, 2);
        getBuilder(Strings.oneWingedAngel).output(Strings.oneWingedAngelChain, 1).addTier(3).addType("keyblade").addMaterial(blazing_stone, 1).addMaterial(orichalcum, 1).addMaterial(blazing_gem, 3).addMaterial(blazing_crystal, 5);
        getBuilder(Strings.painOfSolitude).output(Strings.painOfSolitudeChain, 1).addTier(1).addType("keyblade").addMaterial(writhing_crystal, 2).addMaterial(betwixt_shard, 2).addMaterial(twilight_stone, 1).addMaterial(pulsing_shard, 2);
        getBuilder(Strings.phantomGreen).output(Strings.phantomGreenChain, 1).addTier(1).addType("keyblade").addMaterial(lightning_crystal,1).addMaterial(pulsing_crystal,1);
        getBuilder(Strings.photonDebugger).output(Strings.photonDebuggerChain, 1).addTier(2).addType("keyblade").addMaterial(lightning_shard, 4).addMaterial(lightning_crystal, 2).addMaterial(lightning_gem, 3);
        getBuilder(Strings.pixiePetal).output(Strings.pixiePetalChain, 1).addTier(2).addType("keyblade").addMaterial(lucid_stone, 2).addMaterial(soothing_shard, 2).addMaterial(pulsing_gem, 2);
        getBuilder(Strings.pumpkinhead).output(Strings.pumpkinheadChain, 1).addTier(3).addType("keyblade").addMaterial(writhing_crystal, 2).addMaterial(pulsing_gem, 3).addMaterial(lucid_shard, 5);
        getBuilder(Strings.rainfell).output(Strings.rainfellChain, 1).addTier(1).addType("keyblade").addMaterial(writhing_stone, 2).addMaterial(frost_stone, 1).addMaterial(lucid_shard, 5).addMaterial(stormy_gem, 1);
        getBuilder(Strings.rejectionOfFate).output(Strings.rejectionOfFateChain, 1).addTier(2).addType("keyblade").addMaterial(twilight_gem, 2).addMaterial(writhing_stone, 2).addMaterial(twilight_crystal, 3);
        getBuilder(Strings.royalRadiance).output(Strings.royalRadianceChain, 1).addTier(6).addType("keyblade").addMaterial(pulsing_stone, 5).addMaterial(frost_shard, 2).addMaterial(soothing_stone, 3);
        getBuilder(Strings.rumblingRose).output(Strings.rumblingRoseChain, 1).addTier(4).addType("keyblade").addMaterial(pulsing_stone, 4).addMaterial(soothing_shard, 2).addMaterial(lucid_gem, 3).addMaterial(wellspring_stone, 2);
        getBuilder(Strings.shootingStar).output(Strings.shootingStarChain, 1).addTier(1).addType("keyblade").addMaterial(hungry_stone, 5).addMaterial(hungry_shard, 5).addMaterial(lucid_shard, 2).addMaterial(wellspring_stone, 2);
        getBuilder(Strings.signOfInnocence).output(Strings.signOfInnocenceChain, 1).addTier(3).addType("keyblade").addMaterial(twilight_gem, 2).addMaterial(twilight_crystal, 1).addMaterial(writhing_shard, 3);
        getBuilder(Strings.silentDirge).output(Strings.silentDirgeChain, 1).addTier(3).addType("keyblade").addMaterial(soothing_gem, 2).addMaterial(twilight_crystal, 2).addMaterial(writhing_shard, 1);
        getBuilder(Strings.skullNoise).output(Strings.skullNoiseChain, 1).addTier(1).addType("keyblade").addMaterial(writhing_crystal, 3).addMaterial(lost_illusion, 1).addMaterial(writhing_gem, 2).addMaterial(wellspring_shard, 3);
        getBuilder(Strings.sleepingLion).output(Strings.sleepingLionChain, 1).addTier(4).addType("keyblade").addMaterial(twilight_gem, 1).addMaterial(pulsing_stone, 2).addMaterial(tranquility_crystal, 2).addMaterial(blazing_shard, 4);
        getBuilder(Strings.soulEater).output(Strings.soulEaterChain, 1).addTier(1).addType("keyblade").addMaterial(writhing_crystal, 3).addMaterial(pulsing_stone, 5).addMaterial(writhing_gem, 5).addMaterial(pulsing_crystal, 3);
        getBuilder(Strings.spellbinder).output(Strings.spellbinderChain, 1).addTier(1).addType("keyblade").addMaterial(lucid_crystal, 2).addMaterial(frost_stone, 2).addMaterial(pulsing_gem, 2);
        getBuilder(Strings.starCluster).output(Strings.starClusterChain, 1).addTier(1).addType("keyblade").addMaterial(twilight_crystal, 2).addMaterial(betwixt_stone, 2);
        getBuilder(Strings.starSeeker).output(Strings.starSeekerChain, 1).addTier(1).addType("keyblade").addMaterial(twilight_stone, 5).addMaterial(betwixt_shard, 3).addMaterial(pulsing_shard, 2);
        getBuilder(Strings.starlight).output(Strings.starlightChain, 1).addTier(2).addType("keyblade").addMaterial(mythril_stone, 3).addMaterial(mythril_crystal, 3).addMaterial(mythril_shard, 3).addMaterial(mythril_gem, 3);
        getBuilder(Strings.stormfall).output(Strings.stormfallChain, 1).addTier(3).addType("keyblade").addMaterial(stormy_stone, 2).addMaterial(stormy_crystal, 1).addMaterial(soothing_stone, 3).addMaterial(writhing_gem, 2).addMaterial(pulsing_crystal, 3);
        getBuilder(Strings.strokeOfMidnight).output(Strings.strokeOfMidnightChain, 1).addTier(1).addType("keyblade").addMaterial(pulsing_stone, 2).addMaterial(frost_shard, 2).addMaterial(betwixt_shard, 1).addMaterial(soothing_crystal, 3);
        getBuilder(Strings.sweetDreams).output(Strings.sweetDreamsChain, 1).addTier(5).addType("keyblade").addMaterial(pulsing_stone, 2).addMaterial(wellspring_crystal, 2).addMaterial(twilight_shard, 4).addMaterial(betwixt_shard, 2);
        getBuilder(Strings.sweetMemories).output(Strings.sweetMemoriesChain, 1).addTier(2).addType("keyblade").addMaterial(wellspring_crystal, 3).addMaterial(pulsing_stone, 2).addMaterial(lucid_shard, 4).addMaterial(remembrance_gem, 1);
        getBuilder(Strings.sweetstack).output(Strings.sweetstackChain, 1).addTier(4).addType("keyblade").addMaterial(pulsing_stone, 4).addMaterial(blazing_crystal, 2).addMaterial(lucid_gem, 1).addMaterial(wellspring_shard, 3);
        getBuilder(Strings.threeWishes).output(Strings.threeWishesChain, 1).addTier(2).addType("keyblade").addMaterial(lucid_gem, 3).addMaterial(wellspring_stone, 3).addMaterial(pulsing_crystal, 5);
        getBuilder(Strings.totalEclipse).output(Strings.totalEclipseChain, 1).addTier(3).addType("keyblade").addMaterial(writhing_stone, 3).addMaterial(blazing_shard, 2).addMaterial(blazing_gem, 2);
        getBuilder(Strings.treasureTrove).output(Strings.treasureTroveChain, 1).addTier(1).addType("keyblade").addMaterial(lucid_crystal, 2).addMaterial(frost_crystal, 2).addMaterial(writhing_crystal, 1).addMaterial(soothing_crystal, 2).addMaterial(blazing_crystal, 2);
        getBuilder(Strings.trueLightsFlight).output(Strings.trueLightsFlightChain, 1).addTier(2).addType("keyblade").addMaterial(twilight_gem, 3).addMaterial(twilight_shard, 3).addMaterial(lost_illusion, 1);
        getBuilder(Strings.twilightBlaze).output(Strings.twilightBlazeChain, 1).addTier(6).addType("keyblade").addMaterial(twilight_crystal, 3).addMaterial(blazing_crystal, 3);
        getBuilder(Strings.twoBecomeOne).output(Strings.twoBecomeOneChain, 1).addTier(4).addType("keyblade").addMaterial(twilight_gem, 2).addMaterial(twilight_crystal, 3).addMaterial(lost_illusion, 1);
        getBuilder(Strings.ultimaWeaponBBS).output(Strings.ultimaWeaponBBSChain, 1).addTier(6).addType("keyblade").addMaterial(mythril_crystal, 1).addMaterial(orichalcum, 1).addMaterial(orichalcumplus, 9).addMaterial(hungry_crystal, 5).addMaterial(lightning_gem, 1);
        getBuilder(Strings.ultimaWeaponDDD).output(Strings.ultimaWeaponDDDChain, 1).addTier(6).addType("keyblade").addMaterial(twilight_gem, 4).addMaterial(twilight_crystal, 3).addMaterial(betwixt_crystal, 2).addMaterial(orichalcum, 1).addMaterial(twilight_stone, 5);
        getBuilder(Strings.ultimaWeaponKH1).output(Strings.ultimaWeaponKH1Chain, 1).addTier(6).addType("keyblade").addMaterial(stormy_stone, 3).addMaterial(hungry_stone, 5).addMaterial(lost_illusion, 1).addMaterial(lightning_gem, 5).addMaterial(hungry_crystal, 5);
        getBuilder(Strings.ultimaWeaponKH2).output(Strings.ultimaWeaponKH2Chain, 1).addTier(6).addType("keyblade").addMaterial(mythril_crystal, 1).addMaterial(betwixt_crystal, 1).addMaterial(twilight_crystal, 1).addMaterial(orichalcum, 1).addMaterial(orichalcumplus, 13).addMaterial(hungry_crystal, 7);
        getBuilder(Strings.ultimaWeaponKH3).output(Strings.ultimaWeaponKH3Chain, 1).addTier(6).addType("keyblade").addMaterial(orichalcumplus, 7).addMaterial(wellspring_crystal, 2).addMaterial(lucid_crystal, 2).addMaterial(pulsing_crystal, 2);
        getBuilder(Strings.umbrella).output(Strings.umbrellaChain, 1).addTier(1).addType("keyblade").addMaterial(twilight_shard, 1).addMaterial(mythril_shard, 10);
        getBuilder(Strings.unbound).output(Strings.unboundChain, 1).addTier(6).addType("keyblade").addMaterial(twilight_gem, 5).addMaterial(mythril_crystal, 3).addMaterial(betwixt_gem, 5).addMaterial(pulsing_crystal, 3);
        getBuilder(Strings.victoryLine).output(Strings.victoryLineChain, 1).addTier(2).addType("keyblade").addMaterial(soothing_stone, 3).addMaterial(lucid_gem, 2).addMaterial(pulsing_crystal, 3);
        getBuilder(Strings.voidGear).output(Strings.voidGearChain, 1).addTier(6).addType("keyblade").addMaterial(writhing_shard, 1).addMaterial(sinister_crystal, 4).addMaterial(sinister_shard, 5).addMaterial(sinister_gem, 3).addMaterial(sinister_stone, 4);
        getBuilder(Strings.voidGearRemnant).output(Strings.voidGearRemnantChain, 1).addTier(6).addType("keyblade").addMaterial(writhing_shard, 1).addMaterial(sinister_crystal, 4).addMaterial(sinister_shard, 5).addMaterial(sinister_gem, 3).addMaterial(sinister_stone, 4);
        getBuilder(Strings.wayToTheDawn).output(Strings.wayToTheDawnChain, 1).addTier(1).addType("keyblade").addMaterial(writhing_crystal, 1).addMaterial(hungry_gem, 4).addMaterial(twilight_crystal, 1).addMaterial(pulsing_crystal, 2);
        getBuilder(Strings.waywardWind).output(Strings.waywardWindChain, 1).addTier(1).addType("keyblade").addMaterial(writhing_shard, 2).addMaterial(pulsing_shard, 2).addMaterial(stormy_shard, 1);
        getBuilder(Strings.wheelOfFate).output(Strings.wheelOfFateChain, 1).addTier(4).addType("keyblade").addMaterial(writhing_shard, 2).addMaterial(pulsing_gem, 2).addMaterial(stormy_crystal, 3);
        getBuilder(Strings.wishingLamp).output(Strings.wishingLampChain, 1).addTier(3).addType("keyblade").addMaterial(wellspring_stone, 3).addMaterial(lucid_crystal, 2).addMaterial(pulsing_crystal, 3).addMaterial(betwixt_gem, 2);
        getBuilder(Strings.winnersProof).output(Strings.winnersProofChain, 1).addTier(5).addType("keyblade").addMaterial(writhing_stone, 3).addMaterial(tranquility_shard, 4).addMaterial(writhing_shard, 5);
        getBuilder(Strings.wishingStar).output(Strings.wishingStarChain, 1).addTier(2).addType("keyblade").addMaterial(soothing_gem, 2).addMaterial(pulsing_stone, 2).addMaterial(mythril_crystal, 2);
        getBuilder(Strings.youngXehanortsKeyblade).output(Strings.youngXehanortsKeybladeChain, 1).addTier(6).addType("keyblade").addMaterial(lucid_crystal, 3).addMaterial(writhing_crystal, 10).addMaterial(frost_shard, 5).addMaterial(mythril_crystal, 3).addMaterial(writhing_gem, 10);
        getBuilder(Strings.zeroOne).output(Strings.zeroOneChain, 1).addTier(3).addType("keyblade").addMaterial(lightning_crystal, 3).addMaterial(pulsing_gem, 4).addMaterial(lightning_stone, 2);

        // Items
        getBuilder(Strings.SM_MythrilCrystal).output(Strings.SM_MythrilCrystal, 1).addTier(2).addType("item").addMaterial(betwixt_crystal, 1).addMaterial(betwixt_gem, 3).addMaterial(twilight_crystal, 1).addMaterial(twilight_gem, 3);
        getBuilder(Strings.SM_MythrilGem).output(Strings.SM_MythrilGem, 1).addTier(2).addType("item").addMaterial(betwixt_crystal, 1).addMaterial(betwixt_gem, 3).addMaterial(twilight_crystal, 1).addMaterial(twilight_gem, 3).addMaterial(hungry_stone, 1);
        getBuilder(Strings.SM_MythrilStone).output(Strings.SM_MythrilStone, 1).addTier(1).addType("item").addMaterial(betwixt_stone, 1).addMaterial(betwixt_shard, 3).addMaterial(twilight_stone, 1).addMaterial(twilight_shard, 3).addMaterial(hungry_shard, 1);
        getBuilder(Strings.SM_MythrilShard).output(Strings.SM_MythrilShard, 1).addTier(1).addType("item").addMaterial(betwixt_stone, 1).addMaterial(betwixt_shard, 3).addMaterial(twilight_stone, 1).addMaterial(twilight_shard, 3);

        getBuilder(Strings.potion).output(Strings.potion, 1).addCost(100).addTier(1).addType("item").addMaterial(lucid_shard, 2).addMaterial(soothing_shard, 2).addMaterial(pulsing_shard, 2);
        getBuilder(Strings.hiPotion).output(Strings.hiPotion, 1).addCost(200).addTier(1).addType("item").addMaterial(lucid_stone, 3).addMaterial(soothing_stone, 3).addMaterial(pulsing_stone, 3).addMaterial(hungry_shard, 1);
        getBuilder(Strings.megaPotion).output(Strings.megaPotion, 1).addCost(400).addTier(2).addType("item").addMaterial(lucid_gem, 3).addMaterial(soothing_gem, 3).addMaterial(pulsing_gem, 3).addMaterial(hungry_gem, 1);

        getBuilder(Strings.ether).output(Strings.ether, 1).addCost(80).addTier(1).addType("item").addMaterial(wellspring_shard, 2).addMaterial(lightning_shard, 2).addMaterial(writhing_shard, 2);
        getBuilder(Strings.hiEther).output(Strings.hiEther, 1).addCost(160).addTier(1).addType("item").addMaterial(pulsing_stone, 3).addMaterial(lightning_stone, 3).addMaterial(writhing_stone, 3).addMaterial(hungry_shard, 1);
        getBuilder(Strings.megaEther).output(Strings.megaEther, 1).addCost(320).addTier(2).addType("item").addMaterial(pulsing_stone, 3).addMaterial(lightning_stone, 3).addMaterial(writhing_stone, 3).addMaterial(hungry_gem, 1);

        getBuilder(Strings.elixir).output(Strings.elixir, 1).addCost(3000).addTier(2).addType("item").addMaterial(pulsing_crystal, 3).addMaterial(frost_gem, 2).addMaterial(sinister_stone, 2).addMaterial(sinister_shard, 2).addMaterial(hungry_gem, 2);
        getBuilder(Strings.megaLixir).output(Strings.megaLixir, 1).addCost(6000).addTier(3).addType("item").addMaterial(pulsing_crystal, 3).addMaterial(frost_crystal, 2).addMaterial(sinister_crystal, 2).addMaterial(sinister_gem, 2).addMaterial(hungry_crystal, 2);

        getBuilder(Strings.driveRecovery).output(Strings.driveRecovery, 1).addCost(400).addTier(1).addType("item").addMaterial(mythril_shard, 3).addMaterial(writhing_shard, 3).addMaterial(frost_shard, 1).addMaterial(lightning_shard, 1);
        getBuilder(Strings.hiDriveRecovery).output(Strings.hiDriveRecovery, 1).addCost(800).addTier(2).addType("item").addMaterial(mythril_gem, 3).addMaterial(writhing_gem, 3).addMaterial(frost_gem, 1).addMaterial(lightning_gem, 1).addMaterial(hungry_crystal, 1);

        getBuilder(Strings.refocuser).output(Strings.refocuser, 1).addCost(250).addTier(1).addType("item").addMaterial(blazing_shard, 3).addMaterial(lightning_shard, 3).addMaterial(lucid_shard, 3).addMaterial(hungry_shard, 1);
        getBuilder(Strings.hiRefocuser).output(Strings.hiRefocuser, 1).addCost(500).addTier(2).addType("item").addMaterial(blazing_stone, 3).addMaterial(lightning_stone, 3).addMaterial(lucid_stone, 3).addMaterial(hungry_gem, 1);

        getBuilder(Strings.powerBoost).output(Strings.powerBoost, 1).addCost(6000).addTier(3).addType("item").addMaterial(mythril_crystal, 1).addMaterial(blazing_crystal, 3).addMaterial(lightning_crystal, 3).addMaterial(lucid_crystal, 3).addMaterial(hungry_crystal, 1);
        getBuilder(Strings.magicBoost).output(Strings.magicBoost, 1).addCost(6000).addTier(3).addType("item").addMaterial(mythril_gem, 1).addMaterial(pulsing_crystal, 3).addMaterial(writhing_crystal, 3).addMaterial(frost_crystal, 3).addMaterial(hungry_crystal, 1);
        getBuilder(Strings.defenseBoost).output(Strings.defenseBoost, 1).addCost(4000).addTier(3).addType("item").addMaterial(mythril_crystal, 1).addMaterial(blazing_crystal, 3).addMaterial(lightning_crystal, 3).addMaterial(lucid_crystal, 3);
        getBuilder(Strings.apBoost).output(Strings.apBoost, 1).addCost(4000).addTier(2).addType("item").addMaterial(mythril_gem, 1).addMaterial(pulsing_crystal, 3).addMaterial(writhing_crystal, 3).addMaterial(frost_crystal, 3);

        getBuilder(Strings.abilityRing).output(Strings.abilityRing, 1).addCost(200).addTier(1).addType("item").addMaterial(betwixt_gem, 1).addMaterial(hungry_shard, 3);
        getBuilder(Strings.aquamarineRing).output(Strings.aquamarineRing, 1).addCost(700).addTier(1).addType("item").addMaterial(writhing_stone, 1).addMaterial(soothing_shard, 5).addMaterial(blazing_shard, 2);
        getBuilder(Strings.executiveRing).output(Strings.executiveRing, 1).addCost(1200).addTier(3).addType("item").addMaterial(mythril_crystal, 2).addMaterial(lightning_gem, 3).addMaterial(lightning_stone, 2);
        getBuilder(Strings.fullBloom).output(Strings.fullBloom, 1).addCost(2200).addTier(2).addType("item").addMaterial(hungry_stone, 3).addMaterial(lost_illusion, 1).addMaterial(manifest_illusion, 1).addMaterial(tranquility_crystal, 1);
        getBuilder(Strings.fullBloomPlus).output(Strings.fullBloomPlus, 1).addCost(2600).addTier(3).addType("item").addMaterial(hungry_stone, 3).addMaterial(lost_illusion, 1).addMaterial(manifest_illusion, 1).addMaterial(tranquility_crystal, 1).addMaterial(electrum, 1);
        getBuilder(Strings.shadowArchive).output(Strings.shadowArchive, 1).addCost(2200).addTier(2).addType("item").addMaterial(hungry_stone, 3).addMaterial(lost_illusion, 1).addMaterial(manifest_illusion, 1).addMaterial(remembrance_crystal, 1);
        getBuilder(Strings.shadowArchivePlus).output(Strings.shadowArchivePlus, 1).addCost(2600).addTier(3).addType("item").addMaterial(hungry_stone, 3).addMaterial(lost_illusion, 1).addMaterial(manifest_illusion, 1).addMaterial(remembrance_crystal, 1).addMaterial(electrum, 1);
        getBuilder(Strings.drawRing).output(Strings.drawRing, 1).addCost(1200).addTier(1).addType("item").addMaterial(wellspring_crystal, 1).addMaterial(twilight_gem, 3).addMaterial(betwixt_stone, 3).addMaterial(remembrance_shard, 5);
        getBuilder(Strings.luckyRing).output(Strings.luckyRing, 1).addCost(2600).addTier(2).addType("item").addMaterial(manifest_illusion, 1).addMaterial(remembrance_shard, 3).addMaterial(soothing_gem, 3).addMaterial(soothing_stone, 5).addMaterial(soothing_shard, 9).addMaterial(hungry_crystal, 1);
        getBuilder(Strings.starCharm).output(Strings.starCharm, 1).addCost(2000).addTier(4).addType("item").addMaterial(mythril_crystal, 1).addMaterial(adamantite, 2).addMaterial(wellspring_crystal, 3).addMaterial(sinister_crystal, 3);
        getBuilder(Strings.cosmicArts).output(Strings.cosmicArts, 1).addCost(1600).addTier(5).addType("item").addMaterial(mythril_stone, 2).addMaterial(pulsing_gem, 2).addMaterial(remembrance_shard, 2).addMaterial(twilight_stone, 4);
        getBuilder(Strings.mastersRing).output(Strings.mastersRing, 1).addCost(2000).addTier(4).addType("item").addMaterial(mythril_crystal, 2).addMaterial(writhing_crystal, 1).addMaterial(hungry_gem, 1);
        getBuilder(Strings.cosmicRing).output(Strings.cosmicRing, 1).addCost(2000).addTier(5).addType("item").addMaterial(mythril_crystal, 2).addMaterial(writhing_crystal, 1).addMaterial(hungry_crystal, 1).addMaterial(evanescent_crystal, 1);
        getBuilder(Strings.slayerEarring).output(Strings.slayerEarring, 1).addCost(2000).addTier(4).addType("item").addMaterial(mythril_gem, 3).addMaterial(pulsing_gem, 2).addMaterial(hungry_stone, 1).addMaterial(evanescent_crystal, 1);
        getBuilder(Strings.fencerEarring).output(Strings.fencerEarring, 1).addCost(2000).addTier(4).addType("item").addMaterial(mythril_gem, 3).addMaterial(writhing_gem, 2).addMaterial(hungry_stone, 1).addMaterial(evanescent_crystal, 1);

        getBuilder(Strings.busterBand).output(Strings.busterBand, 1).addCost(5000).addTier(5).addType("item").addMaterial(pulsing_crystal, 2).addMaterial(pulsing_stone, 3).addMaterial(mythril_crystal, 2);
        getBuilder(Strings.cosmicBelt).output(Strings.cosmicBelt, 1).addCost(6000).addTier(6).addType("item").addMaterial(pulsing_shard, 6).addMaterial(pulsing_gem, 3).addMaterial(mythril_crystal, 3).addMaterial(illusory_crystal, 1);
        getBuilder(Strings.acrisiusPlus).output(Strings.acrisiusPlus, 1).addCost(3500).addTier(4).addType("item").addMaterial(mythril_gem, 2).addMaterial(remembrance_shard, 6).addMaterial(wellspring_crystal, 3);
        getBuilder(Strings.cosmicChain).output(Strings.cosmicChain, 1).addCost(5000).addTier(5).addType("item").addMaterial(mythril_stone, 5).addMaterial(hungry_gem, 4).addMaterial(soothing_crystal, 3).addMaterial(illusory_crystal, 1);
        getBuilder(Strings.firagunBangle).output(Strings.firagunBangle, 1).addCost(6000).addTier(4).addType("item").addMaterial(blazing_crystal, 3).addMaterial(blazing_gem, 4).addMaterial(blazing_shard, 2).addMaterial(blazing_stone, 4).addMaterial(illusory_crystal, 1);
        getBuilder(Strings.blizzagunArmlet).output(Strings.blizzagunArmlet, 1).addCost(6000).addTier(4).addType("item").addMaterial(frost_crystal, 3).addMaterial(frost_gem, 4).addMaterial(frost_shard, 2).addMaterial(frost_stone, 4).addMaterial(illusory_crystal, 1);
        getBuilder(Strings.thundagunTrinket).output(Strings.thundagunTrinket, 1).addCost(6000).addTier(4).addType("item").addMaterial(lightning_crystal, 3).addMaterial(lightning_gem, 4).addMaterial(lightning_shard, 2).addMaterial(lightning_stone, 4).addMaterial(illusory_crystal, 1);
        getBuilder(Strings.shockCharm).output(Strings.shockCharm, 1).addCost(4600).addTier(3).addType("item").addMaterial(lightning_crystal, 1).addMaterial(lightning_gem, 2).addMaterial(pulsing_crystal, 1).addMaterial(lightning_stone, 3);
        getBuilder(Strings.shockCharmPlus).output(Strings.shockCharmPlus, 1).addCost(6400).addTier(4).addType("item").addMaterial(lightning_crystal, 2).addMaterial(lightning_gem, 3).addMaterial(pulsing_crystal, 2).addMaterial(lightning_stone, 4).addMaterial(illusory_crystal, 1);
        getBuilder(Strings.chaosAnklet).output(Strings.chaosAnklet, 1).addCost(6000).addTier(4).addType("item").addMaterial(writhing_crystal, 3).addMaterial(writhing_gem, 4).addMaterial(writhing_shard, 2).addMaterial(writhing_stone, 4).addMaterial(illusory_crystal, 1);
        getBuilder(Strings.gaiaBelt).output(Strings.gaiaBelt, 1).addCost(6000).addTier(4).addType("item").addMaterial(writhing_gem, 3).addMaterial(lightning_gem, 3).addMaterial(lightning_stone, 5).addMaterial(writhing_stone, 4).addMaterial(illusory_crystal, 1);


    }
}
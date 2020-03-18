package online.kingdomkeys.kingdomkeys.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.lib.Strings;

public class ItemModels extends ItemModelProvider {

    public ItemModels(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, KingdomKeys.MODID, existingFileHelper);
    }


    @Override
    protected void registerModels() {
        items();
        keychains();
    }
    private void items()
    {
        getBuilder(Strings.iceCream).parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0","item/icecream");
        getBuilder(Strings.heart).parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0","item/heart");
        getBuilder(Strings.kingdom_hearts).parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0","item/kingdomhearts");
        getBuilder(Strings.pureHeart).parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0","item/pureheart");
        getBuilder(Strings.darkHeart).parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0","item/darkheart");
        getBuilder(Strings.emptyBottle).parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0","item/emptybottle");
    }

    private void keychains() {
        getBuilder(Strings.abaddonPlasmaChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.abaddonPlasmaChain);
        getBuilder(Strings.abyssalTideChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.abyssalTideChain);
        getBuilder(Strings.acedsKeybladeChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.acedsKeybladeChain);
        getBuilder(Strings.allForOneChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.allForOneChain);
        getBuilder(Strings.astralBlastChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.astralBlastChain);
        getBuilder(Strings.aubadeChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.aubadeChain);
        getBuilder(Strings.avasKeybladeChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.avasKeybladeChain);
        getBuilder(Strings.bondOfFlameChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.bondOfFlameChain);
        getBuilder(Strings.brightcrestChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.brightcrestChain);
        getBuilder(Strings.chaosRipperChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.chaosRipperChain);
        getBuilder(Strings.circleOfLifeChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.circleOfLifeChain);
        getBuilder(Strings.counterpointChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.counterpointChain);
        getBuilder(Strings.crabclawChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.crabclawChain);
        getBuilder(Strings.crownOfGuiltChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.crownOfGuiltChain);
        getBuilder(Strings.darkerThanDarkChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.darkerThanDarkChain);
        getBuilder(Strings.darkgnawChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.darkgnawChain);
        getBuilder(Strings.decisivePumpkinChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.decisivePumpkinChain);
        getBuilder(Strings.destinysEmbraceChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.destinysEmbraceChain);
        getBuilder(Strings.diamondDustChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.diamondDustChain);
        getBuilder(Strings.divewingChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.divewingChain);
        getBuilder(Strings.divineRoseChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.divineRoseChain);
        getBuilder(Strings.dualDiscChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.dualDiscChain);
        getBuilder(Strings.earthshakerChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.earthshakerChain);
        getBuilder(Strings.endOfPainChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.endOfPainChain);
        getBuilder(Strings.endsOfTheEarthChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.endsOfTheEarthChain);
        getBuilder(Strings.fairyHarpChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.fairyHarpChain);
        getBuilder(Strings.fairyStarsChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.fairyStarsChain);
        getBuilder(Strings.fatalCrestChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.fatalCrestChain);
        getBuilder(Strings.fenrirChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.fenrirChain);
        getBuilder(Strings.ferrisGearChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.ferrisGearChain);
        getBuilder(Strings.flameLiberatorChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.flameLiberatorChain);
        getBuilder(Strings.followtheWindChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.followtheWindChain);
        getBuilder(Strings.frolicFlameChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.frolicFlameChain);
        getBuilder(Strings.glimpseOfDarknessChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.glimpseOfDarknessChain);
        getBuilder(Strings.guardianBellChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.guardianBellChain);
        getBuilder(Strings.guardianSoulChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.guardianSoulChain);
        getBuilder(Strings.gulasKeybladeChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.gulasKeybladeChain);
        getBuilder(Strings.gullWingChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.gullWingChain);
        getBuilder(Strings.herosCrestChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.herosCrestChain);
        getBuilder(Strings.hiddenDragonChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.hiddenDragonChain);
        getBuilder(Strings.hyperdriveChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.hyperdriveChain);
        getBuilder(Strings.incompleteKibladeChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.incompleteKibladeChain);
        getBuilder(Strings.invisKeybladeChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.invisKeybladeChain);
        getBuilder(Strings.mysteriousAbyssChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.mysteriousAbyssChain);
        getBuilder(Strings.irasKeybladeChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.irasKeybladeChain);
        getBuilder(Strings.jungleKingChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.jungleKingChain);
        getBuilder(Strings.keybladeOfPeoplesHeartsChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.keybladeOfPeoplesHeartsChain);
        getBuilder(Strings.kibladeChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.kibladeChain);
        getBuilder(Strings.kingdomKeyChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.kingdomKeyChain);
        getBuilder(Strings.kingdomKeyDChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.kingdomKeyDChain);
        getBuilder(Strings.knockoutPunchChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.knockoutPunchChain);
        getBuilder(Strings.ladyLuckChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.ladyLuckChain);
        getBuilder(Strings.leviathanChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.leviathanChain);
        getBuilder(Strings.lionheartChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.lionheartChain);
        getBuilder(Strings.lostMemoryChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.lostMemoryChain);
        getBuilder(Strings.lunarEclipseChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.lunarEclipseChain);
        getBuilder(Strings.markOfAHeroChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.markOfAHeroChain);
        getBuilder(Strings.mastersDefenderChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.mastersDefenderChain);
        getBuilder(Strings.maverickFlareChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.maverickFlareChain);
        getBuilder(Strings.metalChocoboChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.metalChocoboChain);
        getBuilder(Strings.midnightRoarChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.midnightRoarChain);
        getBuilder(Strings.mirageSplitChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.mirageSplitChain);
        getBuilder(Strings.missingAcheChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.missingAcheChain);
        getBuilder(Strings.monochromeChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.monochromeChain);
        getBuilder(Strings.moogleOGloryChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.moogleOGloryChain);
        getBuilder(Strings.mysteriousAbyssChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.mysteriousAbyssChain);
        getBuilder(Strings.nightmaresEndAndMirageSplitChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.nightmaresEndAndMirageSplitChain);
        getBuilder(Strings.nightmaresEndChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.nightmaresEndChain);
        getBuilder(Strings.noNameBBSChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.noNameBBSChain);
        getBuilder(Strings.noNameChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.noNameChain);
        getBuilder(Strings.oathkeeperChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.oathkeeperChain);
        getBuilder(Strings.oblivionChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.oblivionChain);
        getBuilder(Strings.oceansRageChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.oceansRageChain);
        getBuilder(Strings.olympiaChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.olympiaChain);
        getBuilder(Strings.omegaWeaponChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.omegaWeaponChain);
        getBuilder(Strings.ominousBlightChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.ominousBlightChain);
        getBuilder(Strings.oneWingedAngelChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.oneWingedAngelChain);
        getBuilder(Strings.painOfSolitudeChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.painOfSolitudeChain);
        getBuilder(Strings.photonDebuggerChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.photonDebuggerChain);
        getBuilder(Strings.pixiePetalChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.pixiePetalChain);
        getBuilder(Strings.pumpkinheadChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.pumpkinheadChain);
        getBuilder(Strings.rainfellChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.rainfellChain);
        getBuilder(Strings.rejectionOfFateChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.rejectionOfFateChain);
        getBuilder(Strings.royalRadianceChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.royalRadianceChain);
        getBuilder(Strings.rumblingRoseChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.rumblingRoseChain);
        getBuilder(Strings.signOfInnocenceChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.signOfInnocenceChain);
        getBuilder(Strings.silentDirgeChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.silentDirgeChain);
        getBuilder(Strings.skullNoiseChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.skullNoiseChain);
        getBuilder(Strings.sleepingLionChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.sleepingLionChain);
        getBuilder(Strings.soulEaterChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.soulEaterChain);
        getBuilder(Strings.spellbinderChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.spellbinderChain);
        getBuilder(Strings.starSeekerChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.starSeekerChain);
        getBuilder(Strings.starlightChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.starlightChain);
        getBuilder(Strings.stormfallChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.stormfallChain);
        getBuilder(Strings.strokeOfMidnightChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.strokeOfMidnightChain);
        getBuilder(Strings.sweetDreamsChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.sweetDreamsChain);
        getBuilder(Strings.sweetMemoriesChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.sweetMemoriesChain);
        getBuilder(Strings.sweetstackChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.sweetstackChain);
        getBuilder(Strings.threeWishesChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.threeWishesChain);
        getBuilder(Strings.totalEclipseChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.totalEclipseChain);
        getBuilder(Strings.treasureTroveChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.treasureTroveChain);
        getBuilder(Strings.trueLightsFlightChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.trueLightsFlightChain);
        getBuilder(Strings.twilightBlazeChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.twilightBlazeChain);
        getBuilder(Strings.twoBecomeOneChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.twoBecomeOneChain);
        getBuilder(Strings.ultimaWeaponBBSChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.ultimaWeaponBBSChain);
        getBuilder(Strings.ultimaWeaponDDDChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.ultimaWeaponDDDChain);
        getBuilder(Strings.ultimaWeaponKH1Chain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.ultimaWeaponKH1Chain);
        getBuilder(Strings.ultimaWeaponKH2Chain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.ultimaWeaponKH2Chain);
        getBuilder(Strings.ultimaWeaponKH3Chain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.ultimaWeaponKH3Chain);
        getBuilder(Strings.umbrellaChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.umbrellaChain);
        getBuilder(Strings.unboundChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.unboundChain);
        getBuilder(Strings.victoryLineChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.victoryLineChain);
        getBuilder(Strings.voidGearChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.voidGearChain);
        getBuilder(Strings.waytotheDawnChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.waytotheDawnChain);
        getBuilder(Strings.waywardWindChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.waywardWindChain);
        getBuilder(Strings.winnersProofChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.winnersProofChain);
        getBuilder(Strings.wishingLampChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.wishingLampChain);
        getBuilder(Strings.wishingStarChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.wishingStarChain);
        getBuilder(Strings.youngXehanortsKeybladeChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.youngXehanortsKeybladeChain);
        getBuilder(Strings.zeroOneChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/keychains/" + Strings.zeroOneChain);
    }

    @Override
    public String getName() {
        return "Item Models";
    }
}

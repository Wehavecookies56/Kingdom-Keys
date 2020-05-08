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
        armor();
        synthesis();
    }

    private void armor() {
        getBuilder(Strings.organizationHood).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/armor/organizationrobe_helmet");
        getBuilder(Strings.organizationChestplate).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/armor/organizationrobe_chestplate");
        getBuilder(Strings.organizationLeggings).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/armor/organizationrobe_leggings");
        getBuilder(Strings.organizationBoots).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/armor/organizationrobe_boots");
        getBuilder(Strings.terraHelmet).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/armor/"+ Strings.terraHelmet);
        getBuilder(Strings.terraChestplate).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/armor/"+ Strings.terraChestplate);
        getBuilder(Strings.terraLeggings).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/armor/"+ Strings.terraLeggings);
        getBuilder(Strings.terraBoots).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/armor/"+ Strings.terraBoots);
        getBuilder(Strings.aquaHelmet).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/armor/"+ Strings.aquaHelmet);
        getBuilder(Strings.aquaChestplate).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/armor/"+ Strings.aquaChestplate);
        getBuilder(Strings.aquaLeggings).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/armor/"+ Strings.aquaLeggings);
        getBuilder(Strings.aquaBoots).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/armor/"+ Strings.aquaBoots);
        getBuilder(Strings.ventusHelmet).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/armor/"+ Strings.ventusHelmet);
        getBuilder(Strings.ventusChestplate).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/armor/"+ Strings.ventusChestplate);
        getBuilder(Strings.ventusLeggings).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/armor/"+ Strings.ventusLeggings);
        getBuilder(Strings.ventusBoots).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/armor/"+ Strings.ventusBoots);
        getBuilder(Strings.eraqusHelmet).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/armor/"+ Strings.eraqusHelmet);
        getBuilder(Strings.eraqusChestplate).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/armor/"+ Strings.eraqusChestplate);
        getBuilder(Strings.eraqusLeggings).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/armor/"+ Strings.eraqusLeggings);
        getBuilder(Strings.eraqusBoots).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/armor/"+ Strings.eraqusBoots);
        getBuilder(Strings.xemnasHelmet).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/armor/"+ Strings.xemnasHelmet);
        getBuilder(Strings.xemnasChestplate).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/armor/"+ Strings.xemnasChestplate);
        getBuilder(Strings.xemnasLeggings).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/armor/"+ Strings.xemnasLeggings);
        getBuilder(Strings.xemnasBoots).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/armor/"+ Strings.xemnasBoots);
        getBuilder(Strings.nightmareVentusHelmet).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/armor/"+ Strings.nightmareVentusHelmet);
        getBuilder(Strings.nightmareVentusChestplate).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/armor/"+ Strings.nightmareVentusChestplate);
        getBuilder(Strings.nightmareVentusLeggings).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/armor/"+ Strings.nightmareVentusLeggings);
        getBuilder(Strings.nightmareVentusBoots).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/armor/"+ Strings.nightmareVentusBoots);
        getBuilder(Strings.vanitasHelmet).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/armor/"+ Strings.vanitasHelmet);
        getBuilder(Strings.vanitasChestplate).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/armor/"+ Strings.vanitasChestplate);
        getBuilder(Strings.vanitasLeggings).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/armor/"+ Strings.vanitasLeggings);
        getBuilder(Strings.vanitasBoots).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/armor/"+ Strings.vanitasBoots);
        getBuilder(Strings.antiCoatHelmet).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/armor/"+ Strings.antiCoatHelmet);
        getBuilder(Strings.antiCoatChestplate).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/armor/"+ Strings.antiCoatChestplate);
        getBuilder(Strings.antiCoatLeggings).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/armor/"+ Strings.antiCoatLeggings);
        getBuilder(Strings.antiCoatBoots).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/armor/"+ Strings.antiCoatBoots);
    }

    private void items() {
		getBuilder(Strings.iceCream).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/icecream");
		getBuilder(Strings.heart).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/heart");
		getBuilder(Strings.kingdom_hearts).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/kingdomhearts");
		getBuilder(Strings.pureHeart).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/pureheart");
		getBuilder(Strings.darkHeart).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/darkheart");
		getBuilder(Strings.emptyBottle).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/emptybottle");

		getBuilder(Strings.Disc_Birth_by_Sleep_A_Link_to_the_Future).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/discs/birthbysleep-alinktothefuture-");
		getBuilder(Strings.Disc_Darkness_of_the_Unknown).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/discs/darknessoftheunknown");
		getBuilder(Strings.Disc_Dearly_Beloved_Symphony_Version).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/discs/dearlybeloved-symphonyversion-");
		getBuilder(Strings.Disc_Dream_Drop_Distance_The_Next_Awakening).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/discs/dreamdropdistance-thenextawakening-");
		getBuilder(Strings.Disc_Hikari_KINGDOM_Instrumental_Version).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/discs/hikari-kingdominstrumentalversion-");
		getBuilder(Strings.Disc_L_Oscurita_Dell_Ignoto).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/discs/loscuritadellignoto");
		getBuilder(Strings.Disc_Musique_pour_la_tristesse_de_Xion).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/discs/musiquepourlatristessedexion");
		getBuilder(Strings.Disc_No_More_Bugs_Bug_Version).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/discs/nomorebugs-bugversion-");
		getBuilder(Strings.Disc_Organization_XIII).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/discs/organizationxiii");
		getBuilder(Strings.Disc_Sanctuary).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/discs/sanctuary");
		getBuilder(Strings.Disc_Simple_And_Clean_PLANITb_Remix).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/discs/simpleandcleanplanitbremix");
		getBuilder(Strings.Disc_Sinister_Sundown).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/discs/sinistersundown");
		getBuilder(Strings.Disc_The_13th_Anthology).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/discs/the13thanthology");

    }

	private void keychains() {
		getBuilder(Strings.abaddonPlasmaChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.abaddonPlasmaChain);
		getBuilder(Strings.abyssalTideChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.abyssalTideChain);
		getBuilder(Strings.acedsKeybladeChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.acedsKeybladeChain);
		getBuilder(Strings.allForOneChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.allForOneChain);
		getBuilder(Strings.astralBlastChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.astralBlastChain);
		getBuilder(Strings.aubadeChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.aubadeChain);
		getBuilder(Strings.avasKeybladeChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.avasKeybladeChain);
		getBuilder(Strings.bondOfFlameChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.bondOfFlameChain);
		getBuilder(Strings.brightcrestChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.brightcrestChain);
		getBuilder(Strings.chaosRipperChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.chaosRipperChain);
		getBuilder(Strings.circleOfLifeChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.circleOfLifeChain);
		getBuilder(Strings.counterpointChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.counterpointChain);
		getBuilder(Strings.crabclawChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.crabclawChain);
		getBuilder(Strings.crownOfGuiltChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.crownOfGuiltChain);
		getBuilder(Strings.darkerThanDarkChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.darkerThanDarkChain);
		getBuilder(Strings.darkgnawChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.darkgnawChain);
		getBuilder(Strings.decisivePumpkinChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.decisivePumpkinChain);
		getBuilder(Strings.destinysEmbraceChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.destinysEmbraceChain);
		getBuilder(Strings.diamondDustChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.diamondDustChain);
		getBuilder(Strings.divewingChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.divewingChain);
		getBuilder(Strings.divineRoseChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.divineRoseChain);
		getBuilder(Strings.dualDiscChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.dualDiscChain);
		getBuilder(Strings.earthshakerChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.earthshakerChain);
		getBuilder(Strings.endOfPainChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.endOfPainChain);
		getBuilder(Strings.endsOfTheEarthChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.endsOfTheEarthChain);
		getBuilder(Strings.fairyHarpChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.fairyHarpChain);
		getBuilder(Strings.fairyStarsChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.fairyStarsChain);
		getBuilder(Strings.fatalCrestChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.fatalCrestChain);
		getBuilder(Strings.fenrirChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.fenrirChain);
		getBuilder(Strings.ferrisGearChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.ferrisGearChain);
		getBuilder(Strings.flameLiberatorChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.flameLiberatorChain);
		getBuilder(Strings.followtheWindChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.followtheWindChain);
		getBuilder(Strings.frolicFlameChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.frolicFlameChain);
		getBuilder(Strings.glimpseOfDarknessChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.glimpseOfDarknessChain);
		getBuilder(Strings.guardianBellChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.guardianBellChain);
		getBuilder(Strings.guardianSoulChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.guardianSoulChain);
		getBuilder(Strings.gulasKeybladeChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.gulasKeybladeChain);
		getBuilder(Strings.gullWingChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.gullWingChain);
		getBuilder(Strings.herosCrestChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.herosCrestChain);
		getBuilder(Strings.hiddenDragonChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.hiddenDragonChain);
		getBuilder(Strings.hyperdriveChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.hyperdriveChain);
		getBuilder(Strings.incompleteKibladeChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.incompleteKibladeChain);
		getBuilder(Strings.invisKeybladeChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.invisKeybladeChain);
		getBuilder(Strings.mysteriousAbyssChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.mysteriousAbyssChain);
		getBuilder(Strings.irasKeybladeChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.irasKeybladeChain);
		getBuilder(Strings.jungleKingChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.jungleKingChain);
		getBuilder(Strings.keybladeOfPeoplesHeartsChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.keybladeOfPeoplesHeartsChain);
		getBuilder(Strings.kibladeChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.kibladeChain);
		getBuilder(Strings.kingdomKeyChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.kingdomKeyChain);
		getBuilder(Strings.kingdomKeyDChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.kingdomKeyDChain);
		getBuilder(Strings.knockoutPunchChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.knockoutPunchChain);
		getBuilder(Strings.ladyLuckChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.ladyLuckChain);
		getBuilder(Strings.leviathanChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.leviathanChain);
		getBuilder(Strings.lionheartChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.lionheartChain);
		getBuilder(Strings.lostMemoryChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.lostMemoryChain);
		getBuilder(Strings.lunarEclipseChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.lunarEclipseChain);
		getBuilder(Strings.markOfAHeroChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.markOfAHeroChain);
		getBuilder(Strings.mastersDefenderChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.mastersDefenderChain);
		getBuilder(Strings.maverickFlareChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.maverickFlareChain);
		getBuilder(Strings.metalChocoboChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.metalChocoboChain);
		getBuilder(Strings.midnightRoarChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.midnightRoarChain);
		getBuilder(Strings.mirageSplitChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.mirageSplitChain);
		getBuilder(Strings.missingAcheChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.missingAcheChain);
		getBuilder(Strings.monochromeChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.monochromeChain);
		getBuilder(Strings.moogleOGloryChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.moogleOGloryChain);
		getBuilder(Strings.mysteriousAbyssChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.mysteriousAbyssChain);
		getBuilder(Strings.nightmaresEndAndMirageSplitChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.nightmaresEndAndMirageSplitChain);
		getBuilder(Strings.nightmaresEndChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.nightmaresEndChain);
		getBuilder(Strings.noNameBBSChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.noNameBBSChain);
		getBuilder(Strings.noNameChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.noNameChain);
		getBuilder(Strings.oathkeeperChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.oathkeeperChain);
		getBuilder(Strings.oblivionChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.oblivionChain);
		getBuilder(Strings.oceansRageChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.oceansRageChain);
		getBuilder(Strings.olympiaChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.olympiaChain);
		getBuilder(Strings.omegaWeaponChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.omegaWeaponChain);
		getBuilder(Strings.ominousBlightChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.ominousBlightChain);
		getBuilder(Strings.oneWingedAngelChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.oneWingedAngelChain);
		getBuilder(Strings.painOfSolitudeChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.painOfSolitudeChain);
		getBuilder(Strings.photonDebuggerChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.photonDebuggerChain);
		getBuilder(Strings.pixiePetalChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.pixiePetalChain);
		getBuilder(Strings.pumpkinheadChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.pumpkinheadChain);
		getBuilder(Strings.rainfellChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.rainfellChain);
		getBuilder(Strings.rejectionOfFateChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.rejectionOfFateChain);
		getBuilder(Strings.royalRadianceChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.royalRadianceChain);
		getBuilder(Strings.rumblingRoseChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.rumblingRoseChain);
		getBuilder(Strings.signOfInnocenceChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.signOfInnocenceChain);
		getBuilder(Strings.silentDirgeChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.silentDirgeChain);
		getBuilder(Strings.skullNoiseChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.skullNoiseChain);
		getBuilder(Strings.sleepingLionChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.sleepingLionChain);
		getBuilder(Strings.soulEaterChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.soulEaterChain);
		getBuilder(Strings.spellbinderChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.spellbinderChain);
		getBuilder(Strings.starSeekerChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.starSeekerChain);
		getBuilder(Strings.starlightChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.starlightChain);
		getBuilder(Strings.stormfallChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.stormfallChain);
		getBuilder(Strings.strokeOfMidnightChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.strokeOfMidnightChain);
		getBuilder(Strings.sweetDreamsChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.sweetDreamsChain);
		getBuilder(Strings.sweetMemoriesChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.sweetMemoriesChain);
		getBuilder(Strings.sweetstackChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.sweetstackChain);
		getBuilder(Strings.threeWishesChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.threeWishesChain);
		getBuilder(Strings.totalEclipseChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.totalEclipseChain);
		getBuilder(Strings.treasureTroveChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.treasureTroveChain);
		getBuilder(Strings.trueLightsFlightChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.trueLightsFlightChain);
		getBuilder(Strings.twilightBlazeChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.twilightBlazeChain);
		getBuilder(Strings.twoBecomeOneChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.twoBecomeOneChain);
		getBuilder(Strings.ultimaWeaponBBSChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.ultimaWeaponBBSChain);
		getBuilder(Strings.ultimaWeaponDDDChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.ultimaWeaponDDDChain);
		getBuilder(Strings.ultimaWeaponKH1Chain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.ultimaWeaponKH1Chain);
		getBuilder(Strings.ultimaWeaponKH2Chain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.ultimaWeaponKH2Chain);
		getBuilder(Strings.ultimaWeaponKH3Chain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.ultimaWeaponKH3Chain);
		getBuilder(Strings.umbrellaChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.umbrellaChain);
		getBuilder(Strings.unboundChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.unboundChain);
		getBuilder(Strings.victoryLineChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.victoryLineChain);
		getBuilder(Strings.voidGearChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.voidGearChain);
		getBuilder(Strings.waytotheDawnChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.waytotheDawnChain);
		getBuilder(Strings.waywardWindChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.waywardWindChain);
		getBuilder(Strings.winnersProofChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.winnersProofChain);
		getBuilder(Strings.wishingLampChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.wishingLampChain);
		getBuilder(Strings.wishingStarChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.wishingStarChain);
		getBuilder(Strings.youngXehanortsKeybladeChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.youngXehanortsKeybladeChain);
		getBuilder(Strings.zeroOneChain).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", "item/keychains/" + Strings.zeroOneChain);
	}

    private void synthesis(){
        getBuilder(Strings.SM_BlazingCrystal).parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0","item/synthesis/" + Strings.SM_BlazingCrystal);
        getBuilder(Strings.SM_BlazingGem).parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0","item/synthesis/" + Strings.SM_BlazingGem);
        getBuilder(Strings.SM_BlazingShard).parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0","item/synthesis/" + Strings.SM_BlazingShard);
        getBuilder(Strings.SM_BlazingStone).parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0","item/synthesis/" + Strings.SM_BlazingStone);
		getBuilder(Strings.SM_PowerCrystal).parent(new ModelFile.UncheckedModelFile("item/generated"))
				.texture("layer0","item/synthesis/" + Strings.SM_PowerCrystal);
		getBuilder(Strings.SM_PowerGem).parent(new ModelFile.UncheckedModelFile("item/generated"))
				.texture("layer0","item/synthesis/" + Strings.SM_PowerGem);
		getBuilder(Strings.SM_PowerShard).parent(new ModelFile.UncheckedModelFile("item/generated"))
				.texture("layer0","item/synthesis/" + Strings.SM_PowerShard);
		getBuilder(Strings.SM_PowerStone).parent(new ModelFile.UncheckedModelFile("item/generated"))
				.texture("layer0","item/synthesis/" + Strings.SM_PowerStone);
		getBuilder(Strings.SM_FrostCrystal).parent(new ModelFile.UncheckedModelFile("item/generated"))
				.texture("layer0","item/synthesis/" + Strings.SM_FrostCrystal);
		getBuilder(Strings.SM_FrostGem).parent(new ModelFile.UncheckedModelFile("item/generated"))
				.texture("layer0","item/synthesis/" + Strings.SM_FrostGem);
		getBuilder(Strings.SM_FrostShard).parent(new ModelFile.UncheckedModelFile("item/generated"))
				.texture("layer0","item/synthesis/" + Strings.SM_FrostShard);
		getBuilder(Strings.SM_FrostStone).parent(new ModelFile.UncheckedModelFile("item/generated"))
				.texture("layer0","item/synthesis/" + Strings.SM_FrostStone);
		getBuilder(Strings.SM_DarkCrystal).parent(new ModelFile.UncheckedModelFile("item/generated"))
				.texture("layer0","item/synthesis/" + Strings.SM_DarkCrystal);
		getBuilder(Strings.SM_DarkGem).parent(new ModelFile.UncheckedModelFile("item/generated"))
				.texture("layer0","item/synthesis/" + Strings.SM_DarkGem);
		getBuilder(Strings.SM_DarkShard).parent(new ModelFile.UncheckedModelFile("item/generated"))
				.texture("layer0","item/synthesis/" + Strings.SM_DarkShard);
		getBuilder(Strings.SM_DarkStone).parent(new ModelFile.UncheckedModelFile("item/generated"))
				.texture("layer0","item/synthesis/" + Strings.SM_DarkStone);
		getBuilder(Strings.SM_DenseCrystal).parent(new ModelFile.UncheckedModelFile("item/generated"))
				.texture("layer0","item/synthesis/" + Strings.SM_DenseCrystal);
		getBuilder(Strings.SM_DenseGem).parent(new ModelFile.UncheckedModelFile("item/generated"))
				.texture("layer0","item/synthesis/" + Strings.SM_DenseGem);
		getBuilder(Strings.SM_DenseShard).parent(new ModelFile.UncheckedModelFile("item/generated"))
				.texture("layer0","item/synthesis/" + Strings.SM_DenseShard);
		getBuilder(Strings.SM_DenseStone).parent(new ModelFile.UncheckedModelFile("item/generated"))
				.texture("layer0","item/synthesis/" + Strings.SM_DenseStone);
		getBuilder(Strings.SM_LightningCrystal).parent(new ModelFile.UncheckedModelFile("item/generated"))
				.texture("layer0","item/synthesis/" + Strings.SM_LightningCrystal);
		getBuilder(Strings.SM_LightningGem).parent(new ModelFile.UncheckedModelFile("item/generated"))
				.texture("layer0","item/synthesis/" + Strings.SM_LightningGem);
		getBuilder(Strings.SM_LightningShard).parent(new ModelFile.UncheckedModelFile("item/generated"))
				.texture("layer0","item/synthesis/" + Strings.SM_LightningShard);
		getBuilder(Strings.SM_LightningStone).parent(new ModelFile.UncheckedModelFile("item/generated"))
				.texture("layer0","item/synthesis/" + Strings.SM_LightningStone);
		getBuilder(Strings.SM_LucidCrystal).parent(new ModelFile.UncheckedModelFile("item/generated"))
				.texture("layer0","item/synthesis/" + Strings.SM_LucidCrystal);
		getBuilder(Strings.SM_LucidGem).parent(new ModelFile.UncheckedModelFile("item/generated"))
				.texture("layer0","item/synthesis/" + Strings.SM_LucidGem);
		getBuilder(Strings.SM_LucidShard).parent(new ModelFile.UncheckedModelFile("item/generated"))
				.texture("layer0","item/synthesis/" + Strings.SM_LucidShard);
		getBuilder(Strings.SM_LucidStone).parent(new ModelFile.UncheckedModelFile("item/generated"))
				.texture("layer0","item/synthesis/" + Strings.SM_LucidStone);
		getBuilder(Strings.SM_MythrilCrystal).parent(new ModelFile.UncheckedModelFile("item/generated"))
				.texture("layer0","item/synthesis/" + Strings.SM_MythrilCrystal);
		getBuilder(Strings.SM_MythrilGem).parent(new ModelFile.UncheckedModelFile("item/generated"))
				.texture("layer0","item/synthesis/" + Strings.SM_MythrilGem);
		getBuilder(Strings.SM_MythrilShard).parent(new ModelFile.UncheckedModelFile("item/generated"))
				.texture("layer0","item/synthesis/" + Strings.SM_MythrilShard);
		getBuilder(Strings.SM_MythrilStone).parent(new ModelFile.UncheckedModelFile("item/generated"))
				.texture("layer0","item/synthesis/" + Strings.SM_MythrilStone);
		getBuilder(Strings.SM_TwilightCrystal).parent(new ModelFile.UncheckedModelFile("item/generated"))
				.texture("layer0","item/synthesis/" + Strings.SM_TwilightCrystal);
		getBuilder(Strings.SM_TwilightGem).parent(new ModelFile.UncheckedModelFile("item/generated"))
				.texture("layer0","item/synthesis/" + Strings.SM_TwilightGem);
		getBuilder(Strings.SM_TwilightShard).parent(new ModelFile.UncheckedModelFile("item/generated"))
				.texture("layer0","item/synthesis/" + Strings.SM_TwilightShard);
		getBuilder(Strings.SM_TwilightStone).parent(new ModelFile.UncheckedModelFile("item/generated"))
				.texture("layer0","item/synthesis/" + Strings.SM_TwilightStone);
		getBuilder(Strings.SM_SerenityCrystal).parent(new ModelFile.UncheckedModelFile("item/generated"))
				.texture("layer0","item/synthesis/" + Strings.SM_SerenityCrystal);
		getBuilder(Strings.SM_SerenityGem).parent(new ModelFile.UncheckedModelFile("item/generated"))
				.texture("layer0","item/synthesis/" + Strings.SM_SerenityGem);
		getBuilder(Strings.SM_SerenityShard).parent(new ModelFile.UncheckedModelFile("item/generated"))
				.texture("layer0","item/synthesis/" + Strings.SM_SerenityShard);
		getBuilder(Strings.SM_SerenityStone).parent(new ModelFile.UncheckedModelFile("item/generated"))
				.texture("layer0","item/synthesis/" + Strings.SM_SerenityStone);
		getBuilder(Strings.SM_BrightCrystal).parent(new ModelFile.UncheckedModelFile("item/generated"))
				.texture("layer0","item/synthesis/" + Strings.SM_BrightCrystal);
		getBuilder(Strings.SM_BrightGem).parent(new ModelFile.UncheckedModelFile("item/generated"))
				.texture("layer0","item/synthesis/" + Strings.SM_BrightGem);
		getBuilder(Strings.SM_BrightShard).parent(new ModelFile.UncheckedModelFile("item/generated"))
				.texture("layer0","item/synthesis/" + Strings.SM_BrightShard);
		getBuilder(Strings.SM_BrightStone).parent(new ModelFile.UncheckedModelFile("item/generated"))
				.texture("layer0","item/synthesis/" + Strings.SM_BrightStone);
		getBuilder(Strings.SM_EnergyCrystal).parent(new ModelFile.UncheckedModelFile("item/generated"))
				.texture("layer0","item/synthesis/" + Strings.SM_EnergyCrystal);
		getBuilder(Strings.SM_EnergyGem).parent(new ModelFile.UncheckedModelFile("item/generated"))
				.texture("layer0","item/synthesis/" + Strings.SM_EnergyGem);
		getBuilder(Strings.SM_EnergyShard).parent(new ModelFile.UncheckedModelFile("item/generated"))
				.texture("layer0","item/synthesis/" + Strings.SM_EnergyShard);
		getBuilder(Strings.SM_EnergyStone).parent(new ModelFile.UncheckedModelFile("item/generated"))
				.texture("layer0","item/synthesis/" + Strings.SM_EnergyStone);
		getBuilder(Strings.SM_StormyCrystal).parent(new ModelFile.UncheckedModelFile("item/generated"))
				.texture("layer0","item/synthesis/" + Strings.SM_StormyCrystal);
		getBuilder(Strings.SM_StormyGem).parent(new ModelFile.UncheckedModelFile("item/generated"))
				.texture("layer0","item/synthesis/" + Strings.SM_StormyGem);
		getBuilder(Strings.SM_StormyShard).parent(new ModelFile.UncheckedModelFile("item/generated"))
				.texture("layer0","item/synthesis/" + Strings.SM_StormyShard);
		getBuilder(Strings.SM_StormyStone).parent(new ModelFile.UncheckedModelFile("item/generated"))
				.texture("layer0","item/synthesis/" + Strings.SM_StormyStone);
		getBuilder(Strings.SM_TranquilCrystal).parent(new ModelFile.UncheckedModelFile("item/generated"))
				.texture("layer0","item/synthesis/" + Strings.SM_TranquilCrystal);
		getBuilder(Strings.SM_TranquilGem).parent(new ModelFile.UncheckedModelFile("item/generated"))
				.texture("layer0","item/synthesis/" + Strings.SM_TranquilGem);
		getBuilder(Strings.SM_TranquilShard).parent(new ModelFile.UncheckedModelFile("item/generated"))
				.texture("layer0","item/synthesis/" + Strings.SM_TranquilShard);
		getBuilder(Strings.SM_TranquilStone).parent(new ModelFile.UncheckedModelFile("item/generated"))
				.texture("layer0","item/synthesis/" + Strings.SM_TranquilStone);
		getBuilder(Strings.SM_RemembranceCrystal).parent(new ModelFile.UncheckedModelFile("item/generated"))
				.texture("layer0","item/synthesis/" + Strings.SM_RemembranceCrystal);
		getBuilder(Strings.SM_RemembranceGem).parent(new ModelFile.UncheckedModelFile("item/generated"))
				.texture("layer0","item/synthesis/" + Strings.SM_RemembranceGem);
		getBuilder(Strings.SM_RemembranceShard).parent(new ModelFile.UncheckedModelFile("item/generated"))
				.texture("layer0","item/synthesis/" + Strings.SM_RemembranceShard);
		getBuilder(Strings.SM_RemembranceStone).parent(new ModelFile.UncheckedModelFile("item/generated"))
				.texture("layer0","item/synthesis/" + Strings.SM_RemembranceStone);
		getBuilder(Strings.SM_Orichalcum).parent(new ModelFile.UncheckedModelFile("item/generated"))
				.texture("layer0","item/synthesis/" + Strings.SM_Orichalcum);
		getBuilder(Strings.SM_OrichalcumPlus).parent(new ModelFile.UncheckedModelFile("item/generated"))
				.texture("layer0","item/synthesis/" + Strings.SM_OrichalcumPlus);
		getBuilder(Strings.SM_ManifestIllusion).parent(new ModelFile.UncheckedModelFile("item/generated"))
				.texture("layer0","item/synthesis/" + Strings.SM_ManifestIllusion);
		getBuilder(Strings.SM_LostIllusion).parent(new ModelFile.UncheckedModelFile("item/generated"))
				.texture("layer0","item/synthesis/" + Strings.SM_LostIllusion);
    }

    @Override
    public String getName() {
        return "Item Models";
    }
}

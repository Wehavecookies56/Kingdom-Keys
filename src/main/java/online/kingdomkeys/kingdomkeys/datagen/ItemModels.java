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
		standardArmor(Strings.organizationHood);
		standardArmor(Strings.organizationChestplate);
		standardArmor(Strings.organizationLeggings);
		standardArmor(Strings.organizationBoots);
		standardArmor(Strings.terraHelmet);
		standardArmor(Strings.terraChestplate);
		standardArmor(Strings.terraLeggings);
		standardArmor(Strings.terraBoots);
		standardArmor(Strings.aquaHelmet);
		standardArmor(Strings.aquaChestplate);
		standardArmor(Strings.aquaLeggings);
		standardArmor(Strings.aquaBoots);
		standardArmor(Strings.ventusHelmet);
		standardArmor(Strings.ventusChestplate);
		standardArmor(Strings.ventusLeggings);
		standardArmor(Strings.ventusBoots);
		standardArmor(Strings.eraqusHelmet);
		standardArmor(Strings.eraqusChestplate);
		standardArmor(Strings.eraqusLeggings);
		standardArmor(Strings.eraqusBoots);
		standardArmor(Strings.xemnasHelmet);
		standardArmor(Strings.xemnasChestplate);
		standardArmor(Strings.xemnasLeggings);
		standardArmor(Strings.xemnasBoots);
		standardArmor(Strings.nightmareVentusHelmet);
		standardArmor(Strings.nightmareVentusChestplate);
		standardArmor(Strings.nightmareVentusLeggings);
		standardArmor(Strings.nightmareVentusBoots);
		standardArmor(Strings.vanitasHelmet);
		standardArmor(Strings.vanitasChestplate);
		standardArmor(Strings.vanitasLeggings);
		standardArmor(Strings.vanitasBoots);
		standardArmor(Strings.antiCoatHelmet);
		standardArmor(Strings.antiCoatChestplate);
		standardArmor(Strings.antiCoatLeggings);
		standardArmor(Strings.antiCoatBoots);
    }

    private void items() {
		standardItem(Strings.iceCream);
		standardItem(Strings.heart);
		standardItem(Strings.kingdom_hearts);
		standardItem(Strings.pureHeart);
		standardItem(Strings.darkHeart);
		standardItem(Strings.emptyBottle);

		standardDisc(Strings.Disc_Birth_by_Sleep_A_Link_to_the_Future);
		standardDisc(Strings.Disc_Darkness_of_the_Unknown);
		standardDisc(Strings.Disc_Dearly_Beloved_Symphony_Version);
		standardDisc(Strings.Disc_Dream_Drop_Distance_The_Next_Awakening);
		standardDisc(Strings.Disc_Hikari_KINGDOM_Instrumental_Version);
		standardDisc(Strings.Disc_L_Oscurita_Dell_Ignoto);
		standardDisc(Strings.Disc_Musique_pour_la_tristesse_de_Xion);
		standardDisc(Strings.Disc_No_More_Bugs_Bug_Version);
		standardDisc(Strings.Disc_Organization_XIII);
		standardDisc(Strings.Disc_Sanctuary);
		standardDisc(Strings.Disc_Simple_And_Clean_PLANITb_Remix);
		standardDisc(Strings.Disc_Sinister_Sundown);
		standardDisc(Strings.Disc_The_13th_Anthology);

		standardItem(Strings.LevelUpFinal);
		standardItem(Strings.LevelUpLimit);
		standardItem(Strings.LevelUpMaster);
		standardItem(Strings.LevelUpWisdom);
		standardItem(Strings.LevelUpValor);

    }

	private void keychains() {
		standardKeychain(Strings.abaddonPlasmaChain);
		standardKeychain(Strings.abyssalTideChain);
		standardKeychain(Strings.acedsKeybladeChain);
		standardKeychain(Strings.allForOneChain);
		standardKeychain(Strings.astralBlastChain);
		standardKeychain(Strings.aubadeChain);
		standardKeychain(Strings.avasKeybladeChain);
		standardKeychain(Strings.bondOfFlameChain);
		standardKeychain(Strings.brightcrestChain);
		standardKeychain(Strings.chaosRipperChain);
		standardKeychain(Strings.circleOfLifeChain);
		standardKeychain(Strings.counterpointChain);
		standardKeychain(Strings.crabclawChain);
		standardKeychain(Strings.crownOfGuiltChain);
		standardKeychain(Strings.darkerThanDarkChain);
		standardKeychain(Strings.darkgnawChain);
		standardKeychain(Strings.decisivePumpkinChain);
		standardKeychain(Strings.destinysEmbraceChain);
		standardKeychain(Strings.diamondDustChain);
		standardKeychain(Strings.divewingChain);
		standardKeychain(Strings.divineRoseChain);
		standardKeychain(Strings.dualDiscChain);
		standardKeychain(Strings.earthshakerChain);
		standardKeychain(Strings.endOfPainChain);
		standardKeychain(Strings.endsOfTheEarthChain);
		standardKeychain(Strings.fairyHarpChain);
		standardKeychain(Strings.fairyStarsChain);
		standardKeychain(Strings.fatalCrestChain);
		standardKeychain(Strings.fenrirChain);
		standardKeychain(Strings.ferrisGearChain);
		standardKeychain(Strings.flameLiberatorChain);
		standardKeychain(Strings.followtheWindChain);
		standardKeychain(Strings.frolicFlameChain);
		standardKeychain(Strings.glimpseOfDarknessChain);
		standardKeychain(Strings.guardianBellChain);
		standardKeychain(Strings.guardianSoulChain);
		standardKeychain(Strings.gulasKeybladeChain);
		standardKeychain(Strings.gullWingChain);
		standardKeychain(Strings.herosCrestChain);
		standardKeychain(Strings.hiddenDragonChain);
		standardKeychain(Strings.hyperdriveChain);
		standardKeychain(Strings.incompleteKibladeChain);
		standardKeychain(Strings.invisKeybladeChain);
		standardKeychain(Strings.mysteriousAbyssChain);
		standardKeychain(Strings.irasKeybladeChain);
		standardKeychain(Strings.jungleKingChain);
		standardKeychain(Strings.keybladeOfPeoplesHeartsChain);
		standardKeychain(Strings.kibladeChain);
		standardKeychain(Strings.kingdomKeyChain);
		standardKeychain(Strings.kingdomKeyDChain);
		standardKeychain(Strings.knockoutPunchChain);
		standardKeychain(Strings.ladyLuckChain);
		standardKeychain(Strings.leviathanChain);
		standardKeychain(Strings.lionheartChain);
		standardKeychain(Strings.lostMemoryChain);
		standardKeychain(Strings.lunarEclipseChain);
		standardKeychain(Strings.markOfAHeroChain);
		standardKeychain(Strings.mastersDefenderChain);
		standardKeychain(Strings.maverickFlareChain);
		standardKeychain(Strings.metalChocoboChain);
		standardKeychain(Strings.midnightRoarChain);
		standardKeychain(Strings.mirageSplitChain);
		standardKeychain(Strings.missingAcheChain);
		standardKeychain(Strings.monochromeChain);
		standardKeychain(Strings.moogleOGloryChain);
		standardKeychain(Strings.mysteriousAbyssChain);
		standardKeychain(Strings.nightmaresEndAndMirageSplitChain);
		standardKeychain(Strings.nightmaresEndChain);
		standardKeychain(Strings.noNameBBSChain);
		standardKeychain(Strings.noNameChain);
		standardKeychain(Strings.oathkeeperChain);
		standardKeychain(Strings.oblivionChain);
		standardKeychain(Strings.oceansRageChain);
		standardKeychain(Strings.olympiaChain);
		standardKeychain(Strings.omegaWeaponChain);
		standardKeychain(Strings.ominousBlightChain);
		standardKeychain(Strings.oneWingedAngelChain);
		standardKeychain(Strings.painOfSolitudeChain);
		standardKeychain(Strings.photonDebuggerChain);
		standardKeychain(Strings.pixiePetalChain);
		standardKeychain(Strings.pumpkinheadChain);
		standardKeychain(Strings.rainfellChain);
		standardKeychain(Strings.rejectionOfFateChain);
		standardKeychain(Strings.royalRadianceChain);
		standardKeychain(Strings.rumblingRoseChain);
		standardKeychain(Strings.signOfInnocenceChain);
		standardKeychain(Strings.silentDirgeChain);
		standardKeychain(Strings.skullNoiseChain);
		standardKeychain(Strings.sleepingLionChain);
		standardKeychain(Strings.soulEaterChain);
		standardKeychain(Strings.spellbinderChain);
		standardKeychain(Strings.starSeekerChain);
		standardKeychain(Strings.starlightChain);
		standardKeychain(Strings.stormfallChain);
		standardKeychain(Strings.strokeOfMidnightChain);
		standardKeychain(Strings.sweetDreamsChain);
		standardKeychain(Strings.sweetMemoriesChain);
		standardKeychain(Strings.sweetstackChain);
		standardKeychain(Strings.threeWishesChain);
		standardKeychain(Strings.totalEclipseChain);
		standardKeychain(Strings.treasureTroveChain);
		standardKeychain(Strings.trueLightsFlightChain);
		standardKeychain(Strings.twilightBlazeChain);
		standardKeychain(Strings.twoBecomeOneChain);
		standardKeychain(Strings.ultimaWeaponBBSChain);
		standardKeychain(Strings.ultimaWeaponDDDChain);
		standardKeychain(Strings.ultimaWeaponKH1Chain);
		standardKeychain(Strings.ultimaWeaponKH2Chain);
		standardKeychain(Strings.ultimaWeaponKH3Chain);
		standardKeychain(Strings.umbrellaChain);
		standardKeychain(Strings.unboundChain);
		standardKeychain(Strings.victoryLineChain);
		standardKeychain(Strings.voidGearChain);
		standardKeychain(Strings.waytotheDawnChain);
		standardKeychain(Strings.waywardWindChain);
		standardKeychain(Strings.winnersProofChain);
		standardKeychain(Strings.wishingLampChain);
		standardKeychain(Strings.wishingStarChain);
		standardKeychain(Strings.youngXehanortsKeybladeChain);
		standardKeychain(Strings.zeroOneChain);
	}

    private void synthesis(){
        standardMaterial(Strings.SM_BlazingCrystal);
        standardMaterial(Strings.SM_BlazingGem);
        standardMaterial(Strings.SM_BlazingShard);
        standardMaterial(Strings.SM_BlazingStone);
		standardMaterial(Strings.SM_PowerCrystal);
		standardMaterial(Strings.SM_PowerGem);
		standardMaterial(Strings.SM_PowerShard);
		standardMaterial(Strings.SM_PowerStone);
		standardMaterial(Strings.SM_FrostCrystal);
		standardMaterial(Strings.SM_FrostGem);
		standardMaterial(Strings.SM_FrostShard);
		standardMaterial(Strings.SM_FrostStone);
		standardMaterial(Strings.SM_DarkCrystal);
		standardMaterial(Strings.SM_DarkGem);
		standardMaterial(Strings.SM_DarkShard);
		standardMaterial(Strings.SM_DarkStone);
		standardMaterial(Strings.SM_DenseCrystal);
		standardMaterial(Strings.SM_DenseGem);
		standardMaterial(Strings.SM_DenseShard);
		standardMaterial(Strings.SM_DenseStone);
		standardMaterial(Strings.SM_LightningCrystal);
		standardMaterial(Strings.SM_LightningGem);
		standardMaterial(Strings.SM_LightningShard);
		standardMaterial(Strings.SM_LightningStone);
		standardMaterial(Strings.SM_LucidCrystal);
		standardMaterial(Strings.SM_LucidGem);
		standardMaterial(Strings.SM_LucidShard);
		standardMaterial(Strings.SM_LucidStone);
		standardMaterial(Strings.SM_MythrilCrystal);
		standardMaterial(Strings.SM_MythrilGem);
		standardMaterial(Strings.SM_MythrilShard);
		standardMaterial(Strings.SM_MythrilStone);
		standardMaterial(Strings.SM_TwilightCrystal);
		standardMaterial(Strings.SM_TwilightGem);
		standardMaterial(Strings.SM_TwilightShard);
		standardMaterial(Strings.SM_TwilightStone);
		standardMaterial(Strings.SM_SerenityCrystal);
		standardMaterial(Strings.SM_SerenityGem);
		standardMaterial(Strings.SM_SerenityShard);
		standardMaterial(Strings.SM_SerenityStone);
		standardMaterial(Strings.SM_BrightCrystal);
		standardMaterial(Strings.SM_BrightGem);
		standardMaterial(Strings.SM_BrightShard);
		standardMaterial(Strings.SM_BrightStone);
		standardMaterial(Strings.SM_EnergyCrystal);
		standardMaterial(Strings.SM_EnergyGem);
		standardMaterial(Strings.SM_EnergyShard);
		standardMaterial(Strings.SM_EnergyStone);
		standardMaterial(Strings.SM_StormyCrystal);
		standardMaterial(Strings.SM_StormyGem);
		standardMaterial(Strings.SM_StormyShard);
		standardMaterial(Strings.SM_StormyStone);
		standardMaterial(Strings.SM_TranquilCrystal);
		standardMaterial(Strings.SM_TranquilGem);
		standardMaterial(Strings.SM_TranquilShard);
		standardMaterial(Strings.SM_TranquilStone);
		standardMaterial(Strings.SM_RemembranceCrystal);
		standardMaterial(Strings.SM_RemembranceGem);
		standardMaterial(Strings.SM_RemembranceShard);
		standardMaterial(Strings.SM_RemembranceStone);
		standardMaterial(Strings.SM_Orichalcum);
		standardMaterial(Strings.SM_OrichalcumPlus);
		standardMaterial(Strings.SM_ManifestIllusion);
		standardMaterial(Strings.SM_LostIllusion);
    }

    void standardMaterial(String name) {
		standardItem(name, "synthesis/");
	}

    void standardDisc(String name) {
		standardItem(name, "discs/");
	}

    void standardArmor(String name) {
		standardItem(name, "armor/");
	}

    void standardKeychain(String name) {
		standardItem(name, "keychains/");
	}

    void standardItem(String name) {
		standardItem(name, "");
	}

    void standardItem(String name, String path) {
		getBuilder(name).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0","item/"+ path + name);
	}

    @Override
    public String getName() {
        return "Item Models";
    }
}

package online.kingdomkeys.kingdomkeys.datagen.init;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
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
        blocks();
    }

    private void armor() {
		standardArmor(Strings.organization+"_"+Strings.helmet);
		standardArmor(Strings.organization+"_"+Strings.chestplate);
		standardArmor(Strings.organization+"_"+Strings.leggings);
		standardArmor(Strings.organization+"_"+Strings.boots);
		
		standardArmor(Strings.xemnas+"_"+Strings.helmet);
		standardArmor(Strings.xemnas+"_"+Strings.chestplate);
		standardArmor(Strings.xemnas+"_"+Strings.leggings);
		standardArmor(Strings.xemnas+"_"+Strings.boots);
		
		standardArmor(Strings.antiCoat+"_"+Strings.helmet);
		standardArmor(Strings.antiCoat+"_"+Strings.chestplate);
		standardArmor(Strings.antiCoat+"_"+Strings.leggings);
		standardArmor(Strings.antiCoat+"_"+Strings.boots);
		
		standardArmor(Strings.terra+"_"+Strings.helmet);
		standardArmor(Strings.terra+"_"+Strings.chestplate);
		standardArmor(Strings.terra+"_"+Strings.leggings);
		standardArmor(Strings.terra+"_"+Strings.boots);
		
		standardArmor(Strings.aqua+"_"+Strings.helmet);
		standardArmor(Strings.aqua+"_"+Strings.chestplate);
		standardArmor(Strings.aqua+"_"+Strings.leggings);
		standardArmor(Strings.aqua+"_"+Strings.boots);
		
		standardArmor(Strings.ventus+"_"+Strings.helmet);
		standardArmor(Strings.ventus+"_"+Strings.chestplate);
		standardArmor(Strings.ventus+"_"+Strings.leggings);
		standardArmor(Strings.ventus+"_"+Strings.boots);
		
		standardArmor(Strings.eraqus+"_"+Strings.helmet);
		standardArmor(Strings.eraqus+"_"+Strings.chestplate);
		standardArmor(Strings.eraqus+"_"+Strings.leggings);
		standardArmor(Strings.eraqus+"_"+Strings.boots);
		
		standardArmor(Strings.nightmareVentus+"_"+Strings.helmet);
		standardArmor(Strings.nightmareVentus+"_"+Strings.chestplate);
		standardArmor(Strings.nightmareVentus+"_"+Strings.leggings);
		standardArmor(Strings.nightmareVentus+"_"+Strings.boots);
		
		standardArmor(Strings.vanitas+"_"+Strings.helmet);
		standardArmor(Strings.vanitas+"_"+Strings.chestplate);
		standardArmor(Strings.vanitas+"_"+Strings.leggings);
		standardArmor(Strings.vanitas+"_"+Strings.boots);
		
		standardArmor(Strings.aced+"_"+Strings.helmet);
		standardArmor(Strings.aced+"_"+Strings.chestplate);
		standardArmor(Strings.aced+"_"+Strings.leggings);
		standardArmor(Strings.aced+"_"+Strings.boots);
		
		standardArmor(Strings.ava+"_"+Strings.helmet);
		standardArmor(Strings.ava+"_"+Strings.chestplate);
		standardArmor(Strings.ava+"_"+Strings.leggings);
		standardArmor(Strings.ava+"_"+Strings.boots);
		
		standardArmor(Strings.gula+"_"+Strings.helmet);
		standardArmor(Strings.gula+"_"+Strings.chestplate);
		standardArmor(Strings.gula+"_"+Strings.leggings);
		standardArmor(Strings.gula+"_"+Strings.boots);
		
		standardArmor(Strings.invi+"_"+Strings.helmet);
		standardArmor(Strings.invi+"_"+Strings.chestplate);
		standardArmor(Strings.invi+"_"+Strings.leggings);
		standardArmor(Strings.invi+"_"+Strings.boots);
		
		standardArmor(Strings.ira+"_"+Strings.helmet);
		standardArmor(Strings.ira+"_"+Strings.chestplate);
		standardArmor(Strings.ira+"_"+Strings.leggings);
		standardArmor(Strings.ira+"_"+Strings.boots);
		
    }

    private void items() {
		standardItem(Strings.iceCream);
		standardItem(Strings.emptyBottle);
		standardItem(Strings.proofOfHeart);

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

		standardItem(Strings.SpellFire);
		standardItem(Strings.SpellBlizzard);
		standardItem(Strings.SpellWater);
		standardItem(Strings.SpellThunder);
		standardItem(Strings.SpellCure);
		standardItem(Strings.SpellAero);
		standardItem(Strings.SpellMagnet);
		standardItem(Strings.SpellReflect);
		standardItem(Strings.SpellGravity);
		standardItem(Strings.SpellStop);
		
		standardItem(Strings.LevelUpFinal);
		standardItem(Strings.LevelUpLimit);
		standardItem(Strings.LevelUpMaster);
		standardItem(Strings.LevelUpWisdom);
		standardItem(Strings.LevelUpValor);

		standardItem(Strings.synthesisBag);
		standardItem("recipe");
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
		standardKeychain(Strings.bondOfTheBlazeChain);
		standardKeychain(Strings.followTheWindChain);
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
		standardKeychain(Strings.shootingStarChain);
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
		standardKeychain(Strings.voidGearRemnantChain);
		standardKeychain(Strings.wayToTheDawnChain);
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
		standardMaterial(Strings.SM_PulsingCrystal);
		standardMaterial(Strings.SM_PulsingGem);
		standardMaterial(Strings.SM_PulsingShard);
		standardMaterial(Strings.SM_PulsingStone);
		standardMaterial(Strings.SM_FrostCrystal);
		standardMaterial(Strings.SM_FrostGem);
		standardMaterial(Strings.SM_FrostShard);
		standardMaterial(Strings.SM_FrostStone);
		standardMaterial(Strings.SM_WrithingCrystal);
		standardMaterial(Strings.SM_WrithingGem);
		standardMaterial(Strings.SM_WrithingShard);
		standardMaterial(Strings.SM_WrithingStone);
		standardMaterial(Strings.SM_BetwixtCrystal);
		standardMaterial(Strings.SM_BetwixtGem);
		standardMaterial(Strings.SM_BetwixtShard);
		standardMaterial(Strings.SM_BetwixtStone);
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
		standardMaterial(Strings.SM_HungryCrystal);
		standardMaterial(Strings.SM_HungryGem);
		standardMaterial(Strings.SM_HungryShard);
		standardMaterial(Strings.SM_HungryStone);
		standardMaterial(Strings.SM_SoothingCrystal);
		standardMaterial(Strings.SM_SoothingGem);
		standardMaterial(Strings.SM_SoothingShard);
		standardMaterial(Strings.SM_SoothingStone);
		standardMaterial(Strings.SM_WellspringCrystal);
		standardMaterial(Strings.SM_WellspringGem);
		standardMaterial(Strings.SM_WellspringShard);
		standardMaterial(Strings.SM_WellspringStone);
		standardMaterial(Strings.SM_StormyCrystal);
		standardMaterial(Strings.SM_StormyGem);
		standardMaterial(Strings.SM_StormyShard);
		standardMaterial(Strings.SM_StormyStone);
		standardMaterial(Strings.SM_TranquilityCrystal);
		standardMaterial(Strings.SM_TranquilityGem);
		standardMaterial(Strings.SM_TranquilityShard);
		standardMaterial(Strings.SM_TranquilityStone);
		standardMaterial(Strings.SM_RemembranceCrystal);
		standardMaterial(Strings.SM_RemembranceGem);
		standardMaterial(Strings.SM_RemembranceShard);
		standardMaterial(Strings.SM_RemembranceStone);
		standardMaterial(Strings.SM_Orichalcum);
		standardMaterial(Strings.SM_OrichalcumPlus);
		standardMaterial(Strings.SM_ManifestIllusion);
		standardMaterial(Strings.SM_LostIllusion);
		standardMaterial(Strings.SM_SinisterCrystal);
		standardMaterial(Strings.SM_SinisterGem);
		standardMaterial(Strings.SM_SinisterStone);
		standardMaterial(Strings.SM_SinisterShard);
		standardMaterial(Strings.SM_Fluorite);
		standardMaterial(Strings.SM_Damascus);
		standardMaterial(Strings.SM_Adamantite);
		standardMaterial(Strings.SM_Electrum);
		standardMaterial(Strings.SM_EvanescentCrystal);
		standardMaterial(Strings.SM_IllusoryCrystal);
    }
    
    private void blocks() {
		standardBlockItem("bounce_blox");
		standardBlockItem("hard_blox");
		standardBlockItem("metal_blox");
		standardBlockItem("ghost_blox_invisible");
		standardBlockItem("ghost_blox_visible");
		standardBlockItem("danger_blox");
		standardBlockItem("normal_blox");
		standardBlockItem("prize_blox");
		standardBlockItem("rare_prize_blox");
		standardBlockItem("blast_blox");
		standardBlockItem("magnet_blox_on");
		standardBlockItem("magnet_blox_off");
		standardBlockItem("pair_blox");
		standardBlockItem("org_portal");
		standardBlockItem("magical_chest");
		standardBlockItem("pedestal");
		//standardBlockItem("moogle_projector");

		//ore
		standardBlockItem("blazing_ore");
		standardBlockItem("blazing_ore_n");
		standardBlockItem("soothing_ore");
		standardBlockItem("writhing_ore");
		standardBlockItem("writhing_ore_e");
		standardBlockItem("writhing_ore_n");
		standardBlockItem("betwixt_ore");
		standardBlockItem("wellspring_ore");
		standardBlockItem("wellspring_ore_n");
		standardBlockItem("frost_ore");
		standardBlockItem("lightning_ore");
		standardBlockItem("lucid_ore");
		standardBlockItem("pulsing_ore");
		standardBlockItem("pulsing_ore_e");
		standardBlockItem("remembrance_ore");
		standardBlockItem("hungry_ore");
		standardBlockItem("sinister_ore");
		standardBlockItem("stormy_ore");
		standardBlockItem("tranquility_ore");
		standardBlockItem("twilight_ore");
		standardBlockItem("twilight_ore_n");

		standardBlockItem("mosaic_stained_glass");
		standardBlockItem("synthesis_table");
		standardBlockItem("station_of_awakening_core");
		standardBlockItem("moogle_projector");

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

	void standardBlockItem(String name) {
		getBuilder(name).parent(new ModelFile.UncheckedModelFile(KingdomKeys.MODID + ":block/" + name));
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

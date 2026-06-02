package online.kingdomkeys.kingdomkeys.datagen.init;

import net.minecraft.data.DataGenerator;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.datagen.builder.MeldingRecipeBuilder;
import online.kingdomkeys.kingdomkeys.datagen.provider.MeldingRecipeProvider;

import static online.kingdomkeys.kingdomkeys.item.ModItems.*;

public class MeldingRecipe extends MeldingRecipeProvider<MeldingRecipeBuilder> {

	public MeldingRecipe(DataGenerator generator, ExistingFileHelper existingFileHelper) {
		super(generator, KingdomKeys.MODID, MeldingRecipeBuilder::new, existingFileHelper);
	}

	@Override
	protected void registerRecipe() {
// Tier 2
		getBuilder("fira").ingredient1(fireSpell).ingredient2(fireSpell).output(firaSpell, 1).addCost(400).addTier(2);
		getBuilder("blizzara").ingredient1(blizzardSpell).ingredient2(blizzardSpell).output(blizzaraSpell, 1).addCost(400).addTier(2);
		getBuilder("thundara").ingredient1(thunderSpell).ingredient2(thunderSpell).output(thundaraSpell, 1).addCost(400).addTier(2);
		getBuilder("cura").ingredient1(cureSpell).ingredient2(cureSpell).output(curaSpell, 1).addCost(400).addTier(2);
		getBuilder("aerora").ingredient1(aeroSpell).ingredient2(aeroSpell).output(aeroraSpell, 1).addCost(400).addTier(2);
		getBuilder("gravira").ingredient1(gravitySpell).ingredient2(gravitySpell).output(graviraSpell, 1).addCost(400).addTier(2);
		getBuilder("stopra").ingredient1(stopSpell).ingredient2(stopSpell).output(stopraSpell, 1).addCost(400).addTier(2);
		getBuilder("watera").ingredient1(waterSpell).ingredient2(waterSpell).output(wateraSpell, 1).addCost(400).addTier(2);
		getBuilder("magnera").ingredient1(magnetSpell).ingredient2(magnetSpell).output(magneraSpell, 1).addCost(400).addTier(2);
		getBuilder("reflera").ingredient1(reflectSpell).ingredient2(reflectSpell).output(refleraSpell, 1).addCost(400).addTier(2);

// Tier 3
		getBuilder("firaga").ingredient1(firaSpell).ingredient2(firaSpell).output(firagaSpell, 1).addCost(800).addTier(3);
		getBuilder("blizzaga").ingredient1(blizzaraSpell).ingredient2(blizzaraSpell).output(blizzagaSpell, 1).addCost(800).addTier(3);
		getBuilder("thundaga").ingredient1(thundaraSpell).ingredient2(thundaraSpell).output(thundagaSpell, 1).addCost(800).addTier(3);
		getBuilder("curaga").ingredient1(curaSpell).ingredient2(curaSpell).output(curagaSpell, 1).addCost(800).addTier(3);
		getBuilder("aeroga").ingredient1(aeroraSpell).ingredient2(aeroraSpell).output(aerogaSpell, 1).addCost(800).addTier(3);
		getBuilder("graviga").ingredient1(graviraSpell).ingredient2(graviraSpell).output(gravigaSpell, 1).addCost(800).addTier(3);
		getBuilder("stopga").ingredient1(stopraSpell).ingredient2(stopraSpell).output(stopgaSpell, 1).addCost(800).addTier(3);
		getBuilder("waterga").ingredient1(wateraSpell).ingredient2(wateraSpell).output(watergaSpell, 1).addCost(800).addTier(3);
		getBuilder("magnega").ingredient1(magneraSpell).ingredient2(magneraSpell).output(magnegaSpell, 1).addCost(800).addTier(3);
		getBuilder("reflega").ingredient1(refleraSpell).ingredient2(refleraSpell).output(reflegaSpell, 1).addCost(800).addTier(3);

//BBS
		getBuilder("dark_firaga").ingredient1(firagaSpell).ingredient2(blackoutSpell).output(darkFiragaSpell, 1).addCost(1200).addTier(4);

		getBuilder("triple_firaga").ingredient1(firagaSpell).ingredient2(firagaSpell).output(tripleFiragaSpell, 1).addCost(1000).addTier(3);
		getBuilder("triple_firaga2").ingredient1(firagaSpell).ingredient2(firaSpell).output(tripleFiragaSpell, 1).addCost(1200).addTier(4);

		getBuilder("crawling_firaga").ingredient1(firagaSpell).ingredient2(stopraSpell).output(crawlingFiragaSpell, 1).bonusOutput(firagaBurstSpell,1,20).addCost(1800).addTier(5);
		getBuilder("crawling_firaga2").ingredient1(firagaSpell).ingredient2(stopgaSpell).output(crawlingFiragaSpell, 1).bonusOutput(firagaBurstSpell,1,20).addCost(1200).addTier(4);

		getBuilder("fission_firaga").ingredient1(firaSpell).ingredient2(aerogaSpell).output(fissionFiragaSpell, 1).bonusOutput(firagaBurstSpell,1,20).addCost(1000).addTier(4);
		getBuilder("fission_firaga2").ingredient1(firagaSpell).ingredient2(aeroraSpell).output(fissionFiragaSpell, 1).bonusOutput(firagaBurstSpell,1,20).addCost(1000).addTier(4);
		getBuilder("fission_firaga3").ingredient1(firagaSpell).ingredient2(aerogaSpell).output(fissionFiragaSpell, 1).bonusOutput(firagaBurstSpell,1,20).addCost(1200).addTier(3);


		getBuilder("triple_blizzaga").ingredient1(blizzagaSpell).ingredient2(blizzagaSpell).output(tripleBlizzagaSpell, 1).addCost(1000).addTier(3);
		getBuilder("triple_blizzaga2").ingredient1(blizzagaSpell).ingredient2(blizzaraSpell).output(tripleBlizzagaSpell, 1).addCost(1200).addTier(4);

		getBuilder("deep_freeze").ingredient1(blizzagaSpell).ingredient2(tripleBlizzagaSpell).output(deepFreezeSpell, 1).addCost(1800).addTier(5);

		getBuilder("glacier").ingredient1(blizzagaSpell).ingredient2(deepFreezeSpell).output(glacier, 1).addCost(1600).addTier(5);
		getBuilder("glacier2").ingredient1(tripleBlizzagaSpell).ingredient2(deepFreezeSpell).output(glacier, 1).addCost(1200).addTier(4);

		//getBuilder("ice_barrage").ingredient1(blizzagaSpell).ingredient2(deepFreezeSpell).output(glacier, 1).addCost(1600).addTier(5);
		//getBuilder("ice_barrage2").ingredient1(blizzagaSpell).ingredient2(deepFreezeSpell).output(glacier, 1).addCost(1600).addTier(5);


		getBuilder("thundaga_shot").ingredient1(thundagaSpell).ingredient2(thundagaSpell).output(thundagaShotSpell, 1).addCost(100).addTier(3);

		getBuilder("spark").ingredient1(thunderSpell).ingredient2(magnetSpell).output(sparkSpell, 1).addCost(800).addTier(3);
		getBuilder("sparkra").ingredient1(thundaraSpell).ingredient2(magneraSpell).output(sparkraSpell, 1).addCost(1200).addTier(3);
		getBuilder("sparkga").ingredient1(thundagaSpell).ingredient2(magnegaSpell).output(sparkgaSpell, 1).addCost(1200).addTier(4);

		getBuilder("baloon").ingredient1(waterSpell).ingredient2(gravitySpell).output(balloonSpell, 1).addCost(800).addTier(3);
		getBuilder("balloonra").ingredient1(wateraSpell).ingredient2(graviraSpell).output(balloonraSpell, 1).addCost(1200).addTier(3);
		getBuilder("balloonga").ingredient1(watergaSpell).ingredient2(gravigaSpell).output(balloongaSpell, 1).addCost(1200).addTier(4);

		getBuilder("zero_gravity").ingredient1(firagaSpell).ingredient2(blackoutSpell).output(darkFiragaSpell, 1).addCost(1200).addTier(4);

		//Warps are here too
		getBuilder("zero_gravira").ingredient1(zeroGravitySpell).ingredient2(zeroGravitySpell).output(zeroGraviraSpell, 1).bonusOutput(warpSpell,1,10).addCost(1200).addTier(4);
		getBuilder("zero_gravira2").ingredient1(magnetSpell).ingredient2(aeroSpell).output(zeroGraviraSpell, 1).addCost(1600).addTier(4);
		getBuilder("zero_gravira3").ingredient1(thunderSpell).ingredient2(zeroGravitySpell).output(zeroGraviraSpell, 1).bonusOutput(warpSpell,1,10).addCost(1600).addTier(4);

		getBuilder("zero_graviga").ingredient1(zeroGraviraSpell).ingredient2(zeroGraviraSpell).output(zeroGravigaSpell, 1).bonusOutput(warpSpell,1,20).addCost(1200).addTier(4);
		getBuilder("zero_graviga2").ingredient1(zeroGraviraSpell).ingredient2(thundaraSpell).output(zeroGravigaSpell, 1).bonusOutput(warpSpell,1,20).addCost(1400).addTier(4);
		getBuilder("zero_graviga3").ingredient1(zeroGravitySpell).ingredient2(zeroGraviraSpell).output(zeroGravigaSpell, 1).bonusOutput(warpSpell,1,20).addCost(1400).addTier(4);

		getBuilder("blackout").ingredient1(zeroGraviraSpell).ingredient2(poisonSpell).output(blackoutSpell, 1).addCost(1600).addTier(4);


		getBuilder("mine_shield").ingredient1(firaSpell).ingredient2(zeroGravitySpell).output(mineShieldSpell, 1).addCost(1200).addTier(3);

		getBuilder("mine_square").ingredient1(firaSpell).ingredient2(stopSpell).output(mineSquareSpell, 1).addCost(1600).addTier(4);

		getBuilder("seeker_mine").ingredient1(mineShieldSpell).ingredient2(magnegaSpell).output(mineSeekerSpell, 1).addCost(1800).addTier(5);
		getBuilder("seeker_mine2").ingredient1(mineSquareSpell).ingredient2(magnegaSpell).output(mineSeekerSpell, 1).addCost(1800).addTier(5);
		getBuilder("seeker_mine3").ingredient1(mineShieldSpell).ingredient2(mineSquareSpell).output(mineSeekerSpell, 1).addCost(1600).addTier(5);

	}
}
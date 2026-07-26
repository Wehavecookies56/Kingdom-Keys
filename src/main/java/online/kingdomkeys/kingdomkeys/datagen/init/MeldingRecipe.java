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
		getBuilder("fira").ingredient1(fireSpell).ingredient2(fireSpell).output(firaSpell).addCost(400).addTier(2);
		getBuilder("blizzara").ingredient1(blizzardSpell).ingredient2(blizzardSpell).output(blizzaraSpell).addCost(400).addTier(2);
		getBuilder("thundara").ingredient1(thunderSpell).ingredient2(thunderSpell).output(thundaraSpell).addCost(400).addTier(2);
		getBuilder("cura").ingredient1(cureSpell).ingredient2(cureSpell).output(curaSpell).addCost(400).addTier(2);
		getBuilder("aerora").ingredient1(aeroSpell).ingredient2(aeroSpell).output(aeroraSpell).addCost(400).addTier(2);
		getBuilder("gravira").ingredient1(gravitySpell).ingredient2(gravitySpell).output(graviraSpell).addCost(400).addTier(2);
		getBuilder("stopra").ingredient1(stopSpell).ingredient2(stopSpell).output(stopraSpell).addCost(400).addTier(2);
		getBuilder("watera").ingredient1(waterSpell).ingredient2(waterSpell).output(wateraSpell).addCost(400).addTier(2);
		getBuilder("magnera").ingredient1(magnetSpell).ingredient2(magnetSpell).output(magneraSpell).addCost(400).addTier(2);
		getBuilder("reflera").ingredient1(reflectSpell).ingredient2(reflectSpell).output(refleraSpell).addCost(400).addTier(2);

// Tier 3
		getBuilder("firaga").ingredient1(firaSpell).ingredient2(firaSpell).output(firagaSpell).addCost(800).addTier(3);
		getBuilder("blizzaga").ingredient1(blizzaraSpell).ingredient2(blizzaraSpell).output(blizzagaSpell).addCost(800).addTier(3);
		getBuilder("thundaga").ingredient1(thundaraSpell).ingredient2(thundaraSpell).output(thundagaSpell).addCost(800).addTier(3);
		getBuilder("curaga").ingredient1(curaSpell).ingredient2(curaSpell).output(curagaSpell).addCost(800).addTier(3);
		getBuilder("aeroga").ingredient1(aeroraSpell).ingredient2(aeroraSpell).output(aerogaSpell).addCost(800).addTier(3);
		getBuilder("graviga").ingredient1(graviraSpell).ingredient2(graviraSpell).output(gravigaSpell).addCost(800).addTier(3);
		getBuilder("stopga").ingredient1(stopraSpell).ingredient2(stopraSpell).output(stopgaSpell).addCost(800).addTier(3);
		getBuilder("waterga").ingredient1(wateraSpell).ingredient2(wateraSpell).output(watergaSpell).addCost(800).addTier(3);
		getBuilder("magnega").ingredient1(magneraSpell).ingredient2(magneraSpell).output(magnegaSpell).addCost(800).addTier(3);
		getBuilder("reflega").ingredient1(refleraSpell).ingredient2(refleraSpell).output(reflegaSpell).addCost(800).addTier(3);

//BBS
		getBuilder("dark_firaga").ingredient1(firagaSpell).ingredient2(blackoutSpell).output(darkFiragaSpell).addCost(1200).addTier(4);

		getBuilder("triple_firaga").ingredient1(firagaSpell).ingredient2(firagaSpell).output(tripleFiragaSpell).addCost(1000).addTier(3);
		getBuilder("triple_firaga2").ingredient1(firagaSpell).ingredient2(firaSpell).output(tripleFiragaSpell).addCost(1200).addTier(4);

		//Firaga bursts are here too
		getBuilder("crawling_firaga").ingredient1(firagaSpell).ingredient2(stopraSpell).output(crawlingFiragaSpell).bonusOutput(firagaBurstSpell,1,20).addCost(1800).addTier(5);
		getBuilder("crawling_firaga2").ingredient1(firagaSpell).ingredient2(stopgaSpell).output(crawlingFiragaSpell).bonusOutput(firagaBurstSpell,1,20).addCost(1200).addTier(4);
		getBuilder("crawling_firaga3").ingredient1(firagaSpell).ingredient2(slowSpell).output(crawlingFiragaSpell).bonusOutput(firagaBurstSpell,1,20).addCost(2000).addTier(4);

		getBuilder("fission_firaga").ingredient1(firaSpell).ingredient2(aerogaSpell).output(fissionFiragaSpell).bonusOutput(firagaBurstSpell,1,20).addCost(1000).addTier(4);
		getBuilder("fission_firaga2").ingredient1(firagaSpell).ingredient2(aeroraSpell).output(fissionFiragaSpell).bonusOutput(firagaBurstSpell,1,20).addCost(1000).addTier(4);
		getBuilder("fission_firaga3").ingredient1(firagaSpell).ingredient2(aerogaSpell).output(fissionFiragaSpell).bonusOutput(firagaBurstSpell,1,20).addCost(1200).addTier(3);

		getBuilder("ignite").ingredient1(fireSpell).ingredient2(bindSpell).output(igniteSpell).addCost(1200).addTier(4);


		getBuilder("triple_blizzaga").ingredient1(blizzagaSpell).ingredient2(blizzagaSpell).output(tripleBlizzagaSpell).addCost(1000).addTier(3);
		getBuilder("triple_blizzaga2").ingredient1(blizzagaSpell).ingredient2(blizzaraSpell).output(tripleBlizzagaSpell).addCost(1200).addTier(4);

		getBuilder("deep_freeze").ingredient1(blizzagaSpell).ingredient2(tripleBlizzagaSpell).output(deepFreezeSpell).addCost(1400).addTier(4);

		getBuilder("glacier").ingredient1(blizzagaSpell).ingredient2(deepFreezeSpell).output(glacierSpell).addCost(1600).addTier(5);
		getBuilder("glacier2").ingredient1(tripleBlizzagaSpell).ingredient2(deepFreezeSpell).output(glacierSpell).addCost(1200).addTier(4);

		getBuilder("ice_barrage").ingredient1(blizzagaSpell).ingredient2(mineShieldSpell).output(iceBarrageSpell).addCost(1600).addTier(5);
		getBuilder("ice_barrage2").ingredient1(blizzagaSpell).ingredient2(mineSquareSpell).output(iceBarrageSpell).addCost(1600).addTier(5);


		getBuilder("thundaga_shot").ingredient1(thundagaSpell).ingredient2(thundagaSpell).output(thundagaShotSpell).addCost(1000).addTier(4);
		getBuilder("triple_plasma").ingredient1(thundagaShotSpell).ingredient2(thundagaShotSpell).output(triplePlasmaSpell).addCost(1600).addTier(5);

		getBuilder("spark").ingredient1(thunderSpell).ingredient2(magnetSpell).output(sparkSpell).addCost(800).addTier(3);
		getBuilder("sparkra").ingredient1(thundaraSpell).ingredient2(magneraSpell).output(sparkraSpell).addCost(1200).addTier(3);
		getBuilder("sparkra2").ingredient1(sparkSpell).ingredient2(sparkSpell).output(sparkraSpell).addCost(1200).addTier(3);
		getBuilder("sparkga").ingredient1(thundagaSpell).ingredient2(magnegaSpell).output(sparkgaSpell).addCost(1600).addTier(4);
		getBuilder("sparkga2").ingredient1(sparkraSpell).ingredient2(sparkraSpell).output(sparkgaSpell).addCost(1600).addTier(4);

		getBuilder("balloon").ingredient1(waterSpell).ingredient2(gravitySpell).output(balloonSpell).addCost(800).addTier(3);
		getBuilder("balloonra").ingredient1(wateraSpell).ingredient2(graviraSpell).output(balloonraSpell).addCost(1200).addTier(3);
		getBuilder("balloonra2").ingredient1(balloonSpell).ingredient2(balloonSpell).output(balloonraSpell).addCost(1200).addTier(3);
		getBuilder("balloonga").ingredient1(watergaSpell).ingredient2(gravigaSpell).output(balloongaSpell).addCost(1600).addTier(4);
		getBuilder("balloonga2").ingredient1(balloonraSpell).ingredient2(balloonraSpell).output(balloongaSpell).addCost(1600).addTier(4);


		getBuilder("dark_firaga").ingredient1(firagaSpell).ingredient2(blackoutSpell).output(darkFiragaSpell).addCost(1200).addTier(4);

		//Warps are here too
		getBuilder("zero_gravira").ingredient1(zeroGravitySpell).ingredient2(zeroGravitySpell).output(zeroGraviraSpell).bonusOutput(warpSpell,1,10).addCost(1200).addTier(4);
		getBuilder("zero_gravira2").ingredient1(magnetSpell).ingredient2(aeroSpell).output(zeroGraviraSpell).addCost(1600).addTier(4);
		getBuilder("zero_gravira3").ingredient1(thunderSpell).ingredient2(zeroGravitySpell).output(zeroGraviraSpell).bonusOutput(warpSpell,1,10).addCost(1600).addTier(4);

		getBuilder("zero_graviga").ingredient1(zeroGraviraSpell).ingredient2(zeroGraviraSpell).output(zeroGravigaSpell).bonusOutput(warpSpell,1,20).addCost(1200).addTier(4);
		getBuilder("zero_graviga2").ingredient1(zeroGraviraSpell).ingredient2(thundaraSpell).output(zeroGravigaSpell).bonusOutput(warpSpell,1,20).addCost(1400).addTier(4);
		getBuilder("zero_graviga3").ingredient1(zeroGravitySpell).ingredient2(zeroGraviraSpell).output(zeroGravigaSpell).bonusOutput(warpSpell,1,20).addCost(1400).addTier(4);

		getBuilder("blackout").ingredient1(zeroGraviraSpell).ingredient2(poisonSpell).output(blackoutSpell).addCost(1600).addTier(4);

		getBuilder("faith").ingredient1(aerogaSpell).ingredient2(curagaSpell).output(faithSpell).addCost(3600).addTier(6);

		getBuilder("mine_shield").ingredient1(firaSpell).ingredient2(zeroGravitySpell).output(mineShieldSpell).addCost(1200).addTier(3);

		getBuilder("mine_square").ingredient1(firaSpell).ingredient2(stopSpell).output(mineSquareSpell).addCost(1600).addTier(4);

		getBuilder("seeker_mine").ingredient1(mineShieldSpell).ingredient2(magnegaSpell).output(mineSeekerSpell).addCost(1800).addTier(5);
		getBuilder("seeker_mine2").ingredient1(mineSquareSpell).ingredient2(magnegaSpell).output(mineSeekerSpell).addCost(1800).addTier(5);
		getBuilder("seeker_mine3").ingredient1(mineShieldSpell).ingredient2(mineSquareSpell).output(mineSeekerSpell).addCost(1600).addTier(5);

		getBuilder("mini").ingredient1(magneraSpell).ingredient2(warpSpell).output(miniSpell).addCost(2600).addTier(5);
		getBuilder("mini2").ingredient1(magnegaSpell).ingredient2(magnegaSpell).output(miniSpell).addCost(3800).addTier(5);
		getBuilder("mini3").ingredient1(magnegaSpell).ingredient2(bindSpell).output(miniSpell).addCost(4600).addTier(5);

	}
}
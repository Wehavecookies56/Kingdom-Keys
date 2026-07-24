package online.kingdomkeys.kingdomkeys.datagen.provider;

import com.google.gson.JsonArray;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.datagen.builder.SellBuilder;
import online.kingdomkeys.kingdomkeys.item.ModItems;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

public class SellDataProvider implements DataProvider {

	private final PackOutput.PathProvider pathProvider;

	public SellDataProvider(PackOutput output) {
		this.pathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, "sell");
	}

	@Override
	public CompletableFuture<?> run(CachedOutput cache) {
		Path path = pathProvider.json(KingdomKeys.rl("sell"));
		return DataProvider.saveStable(cache, buildSell(), path);
	}

	@Override
	public String getName() {
		return "Kingdom Keys Sell Data";
	}

	private static JsonArray buildSell() {
		return new SellBuilder()
			.item(ModItems.winnerStick, 500)
			.item(Items.COAL, 10)
			.item(Items.COPPER_INGOT, 10)
			.item(Items.IRON_INGOT, 20)
			.item(Items.GOLD_INGOT, 160)
			.item(Items.DIAMOND, 640)
			.item(Items.EMERALD, 960)
			.item(Items.LAPIS_LAZULI, 30)
			.item(Items.REDSTONE, 10)
			.item(Items.NETHERITE_INGOT, 1024)
			.item(ModItems.potion, 50)
			.item(ModItems.hiPotion, 100)
			.item(ModItems.ether, 40)
			.item(ModItems.hiEther, 62)
			.item(ModItems.panacea, 90)
			.item(ModItems.abilityRing, 70)
			.item(ModItems.engineersRing, 97)
			.item(ModItems.techniciansRing, 110)
			.item(ModItems.skillRing, 130)
			.item(ModItems.skillfulRing, 165)
			.item(ModItems.expertsRing, 180)
			.item(ModItems.sardonyxRing, 95)
			.item(ModItems.goldRing, 150)
			.item(ModItems.aquamarineRing, 220)
			.item(ModItems.garnetRing, 240)
			.item(ModItems.diamondRing, 250)
			.item(ModItems.silverRing, 95)
			.item(ModItems.tourmalineRing, 115)
			.item(ModItems.platinumRing, 162)
			.item(ModItems.mythrilRing, 207)
			.item(ModItems.orichalcumRing, 245)
			.item(ModItems.medal, 20)
			.item(ModItems.soldierEarring, 210)
			.item(ModItems.mageEarring, 210)
			.item(ModItems.moonAmulet, 305)
			.item(ModItems.fireBangle, 125)
			.item(ModItems.firaBangle, 200)
			.item(ModItems.firagaBangle, 300)
			.item(ModItems.blizzardArmlet, 125)
			.item(ModItems.blizzaraArmlet, 200)
			.item(ModItems.blizzagaArmlet, 300)
			.item(ModItems.thunderTrinket, 125)
			.item(ModItems.thundaraTrinket, 200)
			.item(ModItems.thundagaTrinket, 300)
			.item(ModItems.shadowAnklet, 125)
			.item(ModItems.darkAnklet, 200)
			.item(ModItems.midnightAnklet, 300)
			.item(ModItems.abasChain, 175)
			.item(ModItems.aegisChain, 237)
			.item(ModItems.acrisius, 350)
			.item(ModItems.elvenBandanna, 62)
			.item(ModItems.divineBandanna, 112)
			.item(ModItems.protectBelt, 175)
			.item(ModItems.powerBand, 237)
			.item(ModItems.petiteRibbon, 1250)
			.item(ModItems.ribbon, 1875)
			.item(ModItems.grandRibbon, 2250)
			.item(ModItems.fluorite, 20)
			.item(ModItems.damascus, 80)
			.item(ModItems.adamantite, 120)
			.item(ModItems.electrum, 280)
			.item(ModItems.orichalcumplus, 1200)
			.item(ModItems.orichalcum, 800)
			.item(ModItems.manifest_illusion, 800)
			.item(ModItems.lost_illusion, 1200)
			.item(ModItems.evanescent_crystal, 600)
			.item(ModItems.illusory_crystal, 600)
			.item(ModItems.soothing_crystal, 100)
			.item(ModItems.soothing_gem, 60)
			.item(ModItems.soothing_stone, 25)
			.item(ModItems.soothing_shard, 10)
			.item(ModItems.wellspring_crystal, 100)
			.item(ModItems.wellspring_gem, 60)
			.item(ModItems.wellspring_shard, 10)
			.item(ModItems.wellspring_stone, 25)
			.item(ModItems.hungry_crystal, 100)
			.item(ModItems.hungry_gem, 60)
			.item(ModItems.hungry_shard, 10)
			.item(ModItems.hungry_stone, 25)
			.item(ModItems.blazing_crystal, 100)
			.item(ModItems.blazing_gem, 60)
			.item(ModItems.blazing_shard, 10)
			.item(ModItems.blazing_stone, 25)
			.item(ModItems.lightning_crystal, 100)
			.item(ModItems.lightning_gem, 60)
			.item(ModItems.lightning_shard, 10)
			.item(ModItems.lightning_stone, 25)
			.item(ModItems.lucid_crystal, 100)
			.item(ModItems.lucid_gem, 60)
			.item(ModItems.lucid_shard, 10)
			.item(ModItems.lucid_stone, 25)
			.item(ModItems.tranquility_crystal, 100)
			.item(ModItems.tranquility_gem, 60)
			.item(ModItems.tranquility_shard, 10)
			.item(ModItems.tranquility_stone, 25)
			.item(ModItems.twilight_crystal, 100)
			.item(ModItems.twilight_gem, 60)
			.item(ModItems.twilight_shard, 10)
			.item(ModItems.twilight_stone, 25)
			.item(ModItems.remembrance_crystal, 100)
			.item(ModItems.remembrance_gem, 60)
			.item(ModItems.remembrance_shard, 10)
			.item(ModItems.remembrance_stone, 25)
			.item(ModItems.writhing_crystal, 100)
			.item(ModItems.writhing_gem, 60)
			.item(ModItems.writhing_shard, 10)
			.item(ModItems.writhing_stone, 25)
			.item(ModItems.betwixt_crystal, 100)
			.item(ModItems.betwixt_gem, 60)
			.item(ModItems.betwixt_shard, 10)
			.item(ModItems.betwixt_stone, 25)
			.item(ModItems.frost_crystal, 100)
			.item(ModItems.frost_gem, 60)
			.item(ModItems.frost_shard, 10)
			.item(ModItems.frost_stone, 25)
			.item(ModItems.mythril_crystal, 100)
			.item(ModItems.mythril_gem, 60)
			.item(ModItems.mythril_shard, 10)
			.item(ModItems.mythril_stone, 25)
			.item(ModItems.pulsing_crystal, 100)
			.item(ModItems.pulsing_gem, 60)
			.item(ModItems.pulsing_shard, 10)
			.item(ModItems.pulsing_stone, 25)
			.item(ModItems.stormy_crystal, 100)
			.item(ModItems.stormy_gem, 60)
			.item(ModItems.stormy_stone, 25)
			.item(ModItems.stormy_shard, 10)
			.item(ModItems.sinister_crystal, 100)
			.item(ModItems.sinister_gem, 60)
			.item(ModItems.sinister_stone, 25)
			.item(ModItems.sinister_shard, 10)
			.build();
	}
}

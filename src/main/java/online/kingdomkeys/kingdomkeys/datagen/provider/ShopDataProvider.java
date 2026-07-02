package online.kingdomkeys.kingdomkeys.datagen.provider;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.lib.Strings;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ShopDataProvider implements DataProvider {

	private static final int REQ_SHARD = 30;
	private static final int REQ_STONE = 25;
	private static final int REQ_GEM = 20;
	private static final int REQ_CRYSTAL = 15;

	private static final int TIER_SHARD = 1;
	private static final int TIER_STONE = 2;
	private static final int TIER_GEM = 3;
	private static final int TIER_CRYSTAL = 4;

	private static final int PRICE_SHARD = 80;
	private static final int PRICE_STONE = 120;
	private static final int PRICE_GEM = 180;
	private static final int PRICE_CRYSTAL = 240;

	private static final int
			TIER_1_MAGIC = 800,
			TIER_2_MAGIC = 1200,
			TIER_3_MAGIC = 1800,
			TIER_4_MAGIC = 2400,
			TIER_5_MAGIC = 2800;

	private final PackOutput.PathProvider pathProvider;

	public ShopDataProvider(PackOutput output) {
		this.pathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, "shop");
	}

	@Override
	public CompletableFuture<?> run(CachedOutput cache) {
		Map<String, JsonArray> shops = new LinkedHashMap<>();

		JsonArray defaultShop = new JsonArray();

		JsonObject names = new JsonObject();
		names.addProperty("names", KingdomKeys.MODID + ":default");
		defaultShop.add(names);

		addMaterialSet(defaultShop, Strings.SM_Blazing);
		addMaterialSet(defaultShop, Strings.SM_Soothing);
		addMaterialSet(defaultShop, Strings.SM_Writhing);
		addMaterialSet(defaultShop, Strings.SM_Betwixt);
		addMaterialSet(defaultShop, Strings.SM_Wellspring);
		addMaterialSet(defaultShop, Strings.SM_Frost);
		addMaterialSet(defaultShop, Strings.SM_Lightning);
		addMaterialSet(defaultShop, Strings.SM_Lucid);
		addMaterialSet(defaultShop, Strings.SM_Hungry);
		addMaterialSet(defaultShop, Strings.SM_Twilight);
		// Mythril only synthesis
		addMaterialSet(defaultShop, Strings.SM_Tranquility);
		addMaterialSet(defaultShop, Strings.SM_Sinister);
		addMaterialSet(defaultShop, Strings.SM_Stormy);
		addMaterialSet(defaultShop, Strings.SM_Remembrance);
		addMaterialSet(defaultShop, Strings.SM_Pulsing);
		
		defaultShop.add(shop(Strings.SM_Orichalcum, 1, 5, 500, 30, "all"));
		defaultShop.add(shop(Strings.SM_OrichalcumPlus, 1, 5, 2000, 30, "all"));
		defaultShop.add(shop(Strings.SM_ManifestIllusion, 1, 5, 1500, 30, "all"));
		defaultShop.add(shop(Strings.SM_LostIllusion, 1, 5, 2500, 30, "all"));
		defaultShop.add(shop(Strings.SM_Fluorite, 1, 2, 300, 30, "all"));
		defaultShop.add(shop(Strings.SM_Damascus, 1, 3, 600, 30, "all"));
		defaultShop.add(shop(Strings.SM_Adamantite, 1, 4, 900, 30, "all"));
		defaultShop.add(shop(Strings.SM_Electrum, 1, 5, 1200, 30, "all"));
		defaultShop.add(shop(Strings.SM_EvanescentCrystal, 1, 5, 1800, 30, "all"));
		defaultShop.add(shop(Strings.SM_IllusoryCrystal, 1, 5, 1800, 30, "all"));
		
		// Consumables
		defaultShop.add(shop(Strings.potion, 1, 1, 200));
		defaultShop.add(shop(Strings.hiPotion, 1, 2, 400));
		defaultShop.add(shop(Strings.ether, 1, 1, 160));
		defaultShop.add(shop(Strings.hiEther, 1, 2, 250));
		defaultShop.add(shop(Strings.panacea, 1, 2, 360));

		// Accessories
		defaultShop.add(shop(Strings.abilityRing, 1, 1, 280));
		defaultShop.add(shop(Strings.engineersRing, 1, 1, 390));
		defaultShop.add(shop(Strings.techniciansRing, 1, 2, 440));
		defaultShop.add(shop(Strings.skillRing, 1, 2, 520));
		defaultShop.add(shop(Strings.skillfulRing, 1, 3, 660));
		defaultShop.add(shop(Strings.expertsRing, 1, 3, 720));
		defaultShop.add(shop(Strings.sardonyxRing, 1, 1, 380));
		defaultShop.add(shop(Strings.goldRing, 1, 1, 600));
		defaultShop.add(shop(Strings.aquamarineRing, 1, 2, 880));
		defaultShop.add(shop(Strings.garnetRing, 1, 2, 960));
		defaultShop.add(shop(Strings.diamondRing, 1, 3, 1000));
		defaultShop.add(shop(Strings.silverRing, 1, 1, 380));
		defaultShop.add(shop(Strings.tourmalineRing, 1, 1, 460));
		defaultShop.add(shop(Strings.platinumRing, 1, 2, 650));
		defaultShop.add(shop(Strings.mythrilRing, 1, 2, 830));
		defaultShop.add(shop(Strings.orichalcumRing, 1, 3, 980));
		defaultShop.add(shop(Strings.medal, 1, 1, 80));
		defaultShop.add(shop(Strings.soldierEarring, 1, 3, 840));
		defaultShop.add(shop(Strings.mageEarring, 1, 3, 840));
		defaultShop.add(shop(Strings.moonAmulet, 1, 3, 1220));
		defaultShop.add(shop(Strings.fireBangle, 1, 1, 500));
		defaultShop.add(shop(Strings.firaBangle, 1, 2, 800));
		defaultShop.add(shop(Strings.firagaBangle, 1, 3, 1200));

		defaultShop.add(shop(Strings.blizzardArmlet, 1, 1, 500));
		defaultShop.add(shop(Strings.blizzaraArmlet, 1, 2, 800));
		defaultShop.add(shop(Strings.blizzagaArmlet, 1, 3, 1200));

		defaultShop.add(shop(Strings.thunderTrinket, 1, 1, 500));
		defaultShop.add(shop(Strings.thundaraTrinket, 1, 2, 800));
		defaultShop.add(shop(Strings.thundagaTrinket, 1, 3, 1200));

		defaultShop.add(shop(Strings.shadowAnklet, 1, 1, 500));
		defaultShop.add(shop(Strings.darkAnklet, 1, 2, 800));
		defaultShop.add(shop(Strings.midnightAnklet, 1, 3, 1200));

		defaultShop.add(shop(Strings.abasChain, 1, 1, 700));
		defaultShop.add(shop(Strings.aegisChain, 1, 2, 950));
		defaultShop.add(shop(Strings.acrisius, 1, 3, 1400));

		defaultShop.add(shop(Strings.elvenBandanna, 1, 1, 250));
		defaultShop.add(shop(Strings.divineBandanna, 1, 2, 450));
		defaultShop.add(shop(Strings.protectBelt, 1, 3, 700));
		defaultShop.add(shop(Strings.powerBand, 1, 4, 950));

		defaultShop.add(shop(Strings.petiteRibbon, 1, 2, 5000));
		defaultShop.add(shop(Strings.ribbon, 1, 3, 7500));
		defaultShop.add(shop(Strings.grandRibbon, 1, 5, 9001));

		// Magics
		defaultShop.add(shop(Strings.SpellFire, 1, 2, TIER_1_MAGIC, 1, "all"));
		defaultShop.add(shop(Strings.SpellFira, 1, 3, TIER_2_MAGIC, 1, "all"));
		defaultShop.add(shop(Strings.SpellFiraga, 1, 4, TIER_3_MAGIC, 1, "all"));
		defaultShop.add(shop(Strings.SpellDarkFiraga, 1, 5, TIER_4_MAGIC, 1, "all"));
		defaultShop.add(shop(Strings.SpellTripleFiraga, 1, 5, TIER_4_MAGIC, 1, "all"));
		defaultShop.add(shop(Strings.SpellCrawlingFiraga, 1, 5, TIER_4_MAGIC, 1, "all"));
		defaultShop.add(shop(Strings.SpellFissionFiraga, 1, 5, TIER_4_MAGIC, 1, "all"));
		defaultShop.add(shop(Strings.SpellFiragaBurst, 1, 6, TIER_5_MAGIC, 1, "all"));

		defaultShop.add(shop(Strings.SpellBlizzard, 1, 2, TIER_1_MAGIC, 1, "all"));
		defaultShop.add(shop(Strings.SpellBlizzara, 1, 3, TIER_2_MAGIC, 1, "all"));
		defaultShop.add(shop(Strings.SpellBlizzaga, 1, 4, TIER_3_MAGIC, 1, "all"));
		defaultShop.add(shop(Strings.SpellTripleBlizzaga, 1, 5, TIER_4_MAGIC, 1, "all"));
		defaultShop.add(shop(Strings.SpellDeepFreeze, 1, 5, TIER_4_MAGIC, 1, "all"));
		defaultShop.add(shop(Strings.SpellIceBarrage, 1, 5, TIER_4_MAGIC, 1, "all"));
		defaultShop.add(shop(Strings.SpellGlacier, 1, 6, TIER_5_MAGIC, 1, "all"));

		defaultShop.add(shop(Strings.SpellWater, 1, 2, TIER_1_MAGIC, 1, "all"));
		defaultShop.add(shop(Strings.SpellWatera, 1, 3, TIER_2_MAGIC, 1, "all"));
		defaultShop.add(shop(Strings.SpellWaterga, 1, 4, TIER_3_MAGIC, 1, "all"));

		defaultShop.add(shop(Strings.SpellThunder, 1, 2, TIER_1_MAGIC, 1, "all"));
		defaultShop.add(shop(Strings.SpellThundara, 1, 3, TIER_2_MAGIC, 1, "all"));
		defaultShop.add(shop(Strings.SpellThundaga, 1, 4, TIER_3_MAGIC, 1, "all"));
		defaultShop.add(shop(Strings.SpellThundagaShot, 1, 5, TIER_4_MAGIC, 1, "all"));
		defaultShop.add(shop(Strings.SpellTriplePlasma, 1, 6, TIER_5_MAGIC, 1, "all"));

		defaultShop.add(shop(Strings.SpellCure, 1, 2, TIER_1_MAGIC, 1, "all"));
		defaultShop.add(shop(Strings.SpellCura, 1, 3, TIER_2_MAGIC, 1, "all"));
		defaultShop.add(shop(Strings.SpellCuraga, 1, 4, TIER_3_MAGIC, 1, "all"));

		defaultShop.add(shop(Strings.SpellAero, 1, 2, TIER_1_MAGIC, 1, "all"));
		defaultShop.add(shop(Strings.SpellAerora, 1, 3, TIER_2_MAGIC, 1, "all"));
		defaultShop.add(shop(Strings.SpellAeroga, 1, 4, TIER_3_MAGIC, 1, "all"));

		defaultShop.add(shop(Strings.SpellMagnet, 1, 2, TIER_1_MAGIC, 1, "all"));
		defaultShop.add(shop(Strings.SpellMagnera, 1, 3, TIER_2_MAGIC, 1, "all"));
		defaultShop.add(shop(Strings.SpellMagnega, 1, 4, TIER_3_MAGIC, 1, "all"));

		defaultShop.add(shop(Strings.SpellReflect, 1, 2, TIER_1_MAGIC, 1, "all"));
		defaultShop.add(shop(Strings.SpellReflera, 1, 3, TIER_2_MAGIC, 1, "all"));
		defaultShop.add(shop(Strings.SpellReflega, 1, 4, TIER_3_MAGIC, 1, "all"));

		defaultShop.add(shop(Strings.SpellGravity, 1, 2, TIER_1_MAGIC, 1, "all"));
		defaultShop.add(shop(Strings.SpellGravira, 1, 3, TIER_2_MAGIC, 1, "all"));
		defaultShop.add(shop(Strings.SpellGraviga, 1, 4, TIER_3_MAGIC, 1, "all"));

		defaultShop.add(shop(Strings.SpellStop, 1, 2, TIER_1_MAGIC, 1, "all"));
		defaultShop.add(shop(Strings.SpellStopra, 1, 3, TIER_2_MAGIC, 1, "all"));
		defaultShop.add(shop(Strings.SpellStopga, 1, 4, TIER_3_MAGIC, 1, "all"));

		defaultShop.add(shop(Strings.SpellBalloon, 1, 2, TIER_1_MAGIC, 1, "all"));
		defaultShop.add(shop(Strings.SpellBalloonra, 1, 3, TIER_2_MAGIC, 1, "all"));
		defaultShop.add(shop(Strings.SpellBalloonga, 1, 4, TIER_3_MAGIC, 1, "all"));

		defaultShop.add(shop(Strings.SpellMineShield, 1, 2, TIER_1_MAGIC, 1, "all"));
		defaultShop.add(shop(Strings.SpellMineSquare, 1, 3, TIER_2_MAGIC, 1, "all"));
		defaultShop.add(shop(Strings.SpellMineSeeker, 1, 4, TIER_3_MAGIC, 1, "all"));

		defaultShop.add(shop(Strings.SpellSpark, 1, 2, TIER_1_MAGIC, 1, "all"));
		defaultShop.add(shop(Strings.SpellSparkra, 1, 3, TIER_2_MAGIC, 1, "all"));
		defaultShop.add(shop(Strings.SpellSparkga, 1, 4, TIER_3_MAGIC, 1, "all"));

		defaultShop.add(shop(Strings.SpellWarp, 1, 6, TIER_5_MAGIC, 1, "all"));
		defaultShop.add(shop(Strings.SpellFaith, 1, 6, TIER_5_MAGIC, 1, "all"));

		defaultShop.add(shop(Strings.SpellZeroGravity, 1, 3, 800));
		defaultShop.add(shop(Strings.SpellPoison, 1, 2, 500));
		defaultShop.add(shop(Strings.SpellEsuna, 1, 2, 700));
		defaultShop.add(shop(Strings.SpellBind, 1, 2, 750));
		defaultShop.add(shop(Strings.SpellConfuse, 1, 3, 850));
		defaultShop.add(shop(Strings.SpellSlow, 1, 2, 650));

		

		shops.put("default", defaultShop);

		CompletableFuture<?>[] futures = shops.entrySet().stream().map(entry -> {
			Path path = pathProvider.json(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, entry.getKey()));
			return DataProvider.saveStable(cache, entry.getValue(), path);
		}).toArray(CompletableFuture[]::new);

		return CompletableFuture.allOf(futures);
	}

	private static void addMaterialSet(JsonArray shop, String baseName) {
		shop.add(shop(baseName + Strings.SM_Shard, 1, TIER_SHARD, PRICE_SHARD, REQ_SHARD, "all"));
		shop.add(shop(baseName + Strings.SM_Stone, 1, TIER_STONE, PRICE_STONE, REQ_STONE, "all"));
		shop.add(shop(baseName + Strings.SM_Gem, 1, TIER_GEM, PRICE_GEM, REQ_GEM, "all"));
		shop.add(shop(baseName + Strings.SM_Crystal, 1, TIER_CRYSTAL, PRICE_CRYSTAL, REQ_CRYSTAL, "all"));
	}

	@Override
	public String getName() {
		return "Kingdom Keys Shop Data";
	}

	private static JsonObject shop(String item, int amount, int tier, int cost) {
		JsonObject obj = new JsonObject();
		obj.addProperty("item", KingdomKeys.MODID + ":" + item);
		obj.addProperty("amount", amount);
		obj.addProperty("tier", tier);
		obj.addProperty("cost", cost);
		return obj;
	}

	private static JsonObject shop(String item, int amount, int tier, int cost, int matReq, String condition) {
		JsonObject obj = shop(item, amount, tier, cost);
		obj.addProperty("mat_req", matReq);
		obj.addProperty("condition", condition);
		return obj;
	}
}

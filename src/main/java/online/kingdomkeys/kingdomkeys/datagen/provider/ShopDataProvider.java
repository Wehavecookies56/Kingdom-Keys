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

		// Req
		int req = 25;
		
		// Materials
		int shard = 80;
		int stone = 120;
		int gem = 180;
		int crystal = 240;

		defaultShop.add(shop(Strings.SM_BlazingShard, 1, 1, shard, req, "all"));
		defaultShop.add(shop(Strings.SM_BlazingStone, 1, 2, stone, req, "all"));
		defaultShop.add(shop(Strings.SM_BlazingGem, 1, 3, gem, req, "all"));
		defaultShop.add(shop(Strings.SM_BlazingCrystal, 1, 4, crystal, req, "all"));

		defaultShop.add(shop(Strings.SM_SoothingShard, 1, 1, shard, req, "all"));
		defaultShop.add(shop(Strings.SM_SoothingStone, 1, 2, stone, req, "all"));
		defaultShop.add(shop(Strings.SM_SoothingGem, 1, 3, gem, req, "all"));
		defaultShop.add(shop(Strings.SM_SoothingCrystal, 1, 4, crystal, req, "all"));

		defaultShop.add(shop(Strings.SM_WrithingShard, 1, 1, shard, req, "all"));
		defaultShop.add(shop(Strings.SM_WrithingStone, 1, 2, stone, req, "all"));
		defaultShop.add(shop(Strings.SM_WrithingGem, 1, 3, gem, req, "all"));
		defaultShop.add(shop(Strings.SM_WrithingCrystal, 1, 4, crystal, req, "all"));

		defaultShop.add(shop(Strings.SM_BetwixtShard, 1, 1, shard, req, "all"));
		defaultShop.add(shop(Strings.SM_BetwixtStone, 1, 2, stone, req, "all"));
		defaultShop.add(shop(Strings.SM_BetwixtGem, 1, 3, gem, req, "all"));
		defaultShop.add(shop(Strings.SM_BetwixtCrystal, 1, 4, crystal, req, "all"));

		defaultShop.add(shop(Strings.SM_WellspringShard, 1, 1, shard, req, "all"));
		defaultShop.add(shop(Strings.SM_WellspringStone, 1, 2, stone, req, "all"));
		defaultShop.add(shop(Strings.SM_WellspringGem, 1, 3, gem, req, "all"));
		defaultShop.add(shop(Strings.SM_WellspringCrystal, 1, 4, crystal, req, "all"));

		defaultShop.add(shop(Strings.SM_FrostShard, 1, 1, shard, req, "all"));
		defaultShop.add(shop(Strings.SM_FrostStone, 1, 2, stone, req, "all"));
		defaultShop.add(shop(Strings.SM_FrostGem, 1, 3, gem, req, "all"));
		defaultShop.add(shop(Strings.SM_FrostCrystal, 1, 4, crystal, req, "all"));

		defaultShop.add(shop(Strings.SM_LightningShard, 1, 1, shard, req, "all"));
		defaultShop.add(shop(Strings.SM_LightningStone, 1, 2, stone, req, "all"));
		defaultShop.add(shop(Strings.SM_LightningGem, 1, 3, gem, req, "all"));
		defaultShop.add(shop(Strings.SM_LightningCrystal, 1, 4, crystal, req, "all"));

		defaultShop.add(shop(Strings.SM_LucidShard, 1, 1, shard, req, "all"));
		defaultShop.add(shop(Strings.SM_LucidStone, 1, 2, stone, req, "all"));
		defaultShop.add(shop(Strings.SM_LucidGem, 1, 3, gem, req, "all"));
		defaultShop.add(shop(Strings.SM_LucidCrystal, 1, 4, crystal, req, "all"));

		defaultShop.add(shop(Strings.SM_HungryShard, 1, 1, shard, req, "all"));
		defaultShop.add(shop(Strings.SM_HungryStone, 1, 2, stone, req, "all"));
		defaultShop.add(shop(Strings.SM_HungryGem, 1, 3, gem, req, "all"));
		defaultShop.add(shop(Strings.SM_HungryCrystal, 1, 4, crystal, req, "all"));

		defaultShop.add(shop(Strings.SM_TwilightShard, 1, 1, shard, req, "all"));
		defaultShop.add(shop(Strings.SM_TwilightStone, 1, 2, stone, req, "all"));
		defaultShop.add(shop(Strings.SM_TwilightGem, 1, 3, gem, req, "all"));
		defaultShop.add(shop(Strings.SM_TwilightCrystal, 1, 4, crystal, req, "all"));

// Mythril is only synthesised
		defaultShop.add(shop(Strings.SM_TranquilityShard, 1, 1, shard, req, "all"));
		defaultShop.add(shop(Strings.SM_TranquilityStone, 1, 2, stone, req, "all"));
		defaultShop.add(shop(Strings.SM_TranquilityGem, 1, 3, gem, req, "all"));
		defaultShop.add(shop(Strings.SM_TranquilityCrystal, 1, 4, crystal, req, "all"));

		defaultShop.add(shop(Strings.SM_SinisterShard, 1, 1, shard, req, "all"));
		defaultShop.add(shop(Strings.SM_SinisterStone, 1, 2, stone, req, "all"));
		defaultShop.add(shop(Strings.SM_SinisterGem, 1, 3, gem, req, "all"));
		defaultShop.add(shop(Strings.SM_SinisterCrystal, 1, 4, crystal, req, "all"));

		defaultShop.add(shop(Strings.SM_StormyShard, 1, 1, shard, req, "all"));
		defaultShop.add(shop(Strings.SM_StormyStone, 1, 2, stone, req, "all"));
		defaultShop.add(shop(Strings.SM_StormyGem, 1, 3, gem, req, "all"));
		defaultShop.add(shop(Strings.SM_StormyCrystal, 1, 4, crystal, req, "all"));

		defaultShop.add(shop(Strings.SM_RemembranceShard, 1, 1, shard, req, "all"));
		defaultShop.add(shop(Strings.SM_RemembranceStone, 1, 2, stone, req, "all"));
		defaultShop.add(shop(Strings.SM_RemembranceGem, 1, 3, gem, req, "all"));
		defaultShop.add(shop(Strings.SM_RemembranceCrystal, 1, 4, crystal, req, "all"));

		defaultShop.add(shop(Strings.SM_PulsingShard, 1, 1, shard, req, "all"));
		defaultShop.add(shop(Strings.SM_PulsingStone, 1, 2, stone, req, "all"));
		defaultShop.add(shop(Strings.SM_PulsingGem, 1, 3, gem, req, "all"));
		defaultShop.add(shop(Strings.SM_PulsingCrystal, 1, 4, crystal, req, "all"));

		//Other
		defaultShop.add(shop(Strings.SM_Orichalcum, 1, 5, 500, req, "all"));
		defaultShop.add(shop(Strings.SM_OrichalcumPlus, 1, 5, 2000, req, "all"));
		defaultShop.add(shop(Strings.SM_ManifestIllusion, 1, 5, 1500, req, "all"));
		defaultShop.add(shop(Strings.SM_LostIllusion, 1, 5, 2500, req, "all"));
		defaultShop.add(shop(Strings.SM_Fluorite, 1, 2, 300, req, "all"));
		defaultShop.add(shop(Strings.SM_Damascus, 1, 3, 600, req, "all"));
		defaultShop.add(shop(Strings.SM_Adamantite, 1, 4, 900, req, "all"));
		defaultShop.add(shop(Strings.SM_Electrum, 1, 5, 1200, req, "all"));
		defaultShop.add(shop(Strings.SM_EvanescentCrystal, 1, 5, 1800, req, "all"));
		defaultShop.add(shop(Strings.SM_IllusoryCrystal, 1, 5, 1800, req, "all"));

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

		// Magic
		defaultShop.add(shop(Strings.SpellFire, 1, 4, 800));
		defaultShop.add(shop(Strings.SpellBlizzard, 1, 4, 800));
		defaultShop.add(shop(Strings.SpellWater, 1, 4, 800));
		defaultShop.add(shop(Strings.SpellThunder, 1, 4, 800));

		defaultShop.add(shop(Strings.SpellCure, 1, 5, 1200));
		defaultShop.add(shop(Strings.SpellAero, 1, 5, 1200));
		defaultShop.add(shop(Strings.SpellMagnet, 1, 5, 1200));
		defaultShop.add(shop(Strings.SpellReflect, 1, 5, 1200));
		defaultShop.add(shop(Strings.SpellGravity, 1, 5, 1200));
		defaultShop.add(shop(Strings.SpellStop, 1, 5, 1200));

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

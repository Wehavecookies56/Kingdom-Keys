package online.kingdomkeys.kingdomkeys.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.synthesis.keybladeforge.KeybladeLevel;
import online.kingdomkeys.kingdomkeys.synthesis.material.Material;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KeybladeStats extends KeybladeProvider {
    public KeybladeStats(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, KingdomKeys.MODID, KEYBLADE_FOLDER, KeybladeBuilder::new, existingFileHelper);
    }

    @Override
    protected void registerKeyblades() {
    	uxKeyblades();
    	bbsKeyblades();
        kh1Keyblades();
        kh2Keyblades();
        kh358Keyblades();
        khReCodedKeyblade();
        dddKeyblades();
        kh3Keyblades();
    }

    public static class Recipe {
        private List<Map.Entry<String, Integer>> recipe = new ArrayList<>();

        public Recipe() { }

        public Recipe addMaterial(String mat, int quantity) {
            recipe.add(Pair.of(mat, quantity));
            return this;
        }

        public Map<Material, Integer> asMap() {
            Map<Material, Integer> matMap = new HashMap<>();
            recipe.forEach(p -> matMap.put(GameRegistry.findRegistry(Material.class).getValue(new ResourceLocation(KingdomKeys.MODID + ":" + Strings.SM_Prefix + p.getKey())), p.getValue()));
            return matMap;
        }
    }

    public void kh3Keyblades() {
        getBuilder(Strings.ultimaWeaponKH3)
                .keychain(Strings.ultimaWeaponKH3Chain)
                .baseStats(8,3)
                .abilities("Combo Boost", "Air Combo Boost", "Situation Boost")
                .level( new KeybladeLevel.KeybladeLevelBuilder()
                        .withStats(9, 4)
                        .withMaterials(new Recipe()
                                .addMaterial(Strings.SM_Fluorite, 1)
                                .addMaterial(Strings.SM_WellspringShard, 3)
                        )
                        .build()
                )
                .level( new KeybladeLevel.KeybladeLevelBuilder()
                        .withStats(9, 5)
                        .withMaterials(new Recipe()
                                .addMaterial(Strings.SM_Fluorite, 1)
                                .addMaterial(Strings.SM_WellspringShard, 4)
                        )
                        .build()
                )
                .level( new KeybladeLevel.KeybladeLevelBuilder()
                        .withStats(10, 6)
                        .withMaterials(new Recipe()
                                .addMaterial(Strings.SM_Fluorite, 1)
                                .addMaterial(Strings.SM_WellspringStone, 1)
                        )
                        .build()
                )
                .level( new KeybladeLevel.KeybladeLevelBuilder()
                        .withStats(10, 7)
                        .withMaterials(new Recipe()
                                .addMaterial(Strings.SM_Damascus, 1)
                                .addMaterial(Strings.SM_WellspringStone, 2)
                                .addMaterial(Strings.SM_HungryStone, 1)
                        )
                        .build()
                )
                .level( new KeybladeLevel.KeybladeLevelBuilder()
                        .withStats(11, 8)
                        .withMaterials(new Recipe()
                                .addMaterial(Strings.SM_Damascus, 1)
                                .addMaterial(Strings.SM_WellspringStone, 3)
                                .addMaterial(Strings.SM_HungryStone, 2)
                        )
                        .build()
                )
                .level( new KeybladeLevel.KeybladeLevelBuilder()
                        .withStats(11, 9)
                        .withMaterials(new Recipe()
                                .addMaterial(Strings.SM_Damascus, 1)
                                .addMaterial(Strings.SM_WellspringGem, 1)
                        )
                        .build()
                )
                .level( new KeybladeLevel.KeybladeLevelBuilder()
                        .withStats(12, 10)
                        .withMaterials(new Recipe()
                                .addMaterial(Strings.SM_Adamantite, 1)
                                .addMaterial(Strings.SM_WellspringGem, 2)
                                .addMaterial(Strings.SM_HungryGem, 1)
                        )
                        .build()
                )
                .level( new KeybladeLevel.KeybladeLevelBuilder()
                        .withStats(12, 11)
                        .withMaterials(new Recipe()
                                .addMaterial(Strings.SM_Adamantite, 1)
                                .addMaterial(Strings.SM_WellspringGem, 3)
                                .addMaterial(Strings.SM_HungryGem, 2)
                        )
                        .build()
                )
                .level( new KeybladeLevel.KeybladeLevelBuilder()
                        .withStats(13, 12)
                        .withMaterials(new Recipe()
                                .addMaterial(Strings.SM_Adamantite, 1)
                                .addMaterial(Strings.SM_WellspringCrystal, 1)
                        )
                        .build()
                )
                .level( new KeybladeLevel.KeybladeLevelBuilder()
                        .withStats(13, 13)
                        .withMaterials(new Recipe()
                                .addMaterial(Strings.SM_Electrum, 1)
                                .addMaterial(Strings.SM_WellspringCrystal, 2)
                                .addMaterial(Strings.SM_HungryCrystal, 1)
                        )
                        .build()
                )
                .desc("test");
    }

    //TODO the rest of the keyblades

    public void kh1Keyblades() {
        /*
        getBuilder(Strings.kingdomKey).keychain(Strings.kingdomKeyChain).baseStats(3,0)
                .level( new KeybladeLevel.KeybladeLevelBuilder().withStr(3).withMag(3).withAbilty("Test_Abiltity")
                        .withMaterials(kslm.getMap(Strings.kingdomKey + "_Lvl1")).build())
                .level( new KeybladeLevel.KeybladeLevelBuilder().withStr(4).withMag(4)
                        .withMaterials(kslm.getMap(Strings.kingdomKey + "_Lvl2")).build())
                .desc("test");

        getBuilder(Strings.kingdomKeyD).keychain(Strings.kingdomKeyDChain).baseStats(3,0).level( new KeybladeLevel.KeybladeLevelBuilder()
                .withStr(3).withMag(3).withAbilty("Test_Abiltity")
                .withMaterials(kslm.getMap(Strings.kingdomKeyD + "_Lvl1")).build()).level( new KeybladeLevel.KeybladeLevelBuilder()
                .withStr(4).withMag(4).withMaterials(kslm.getMap(Strings.kingdomKeyD + "_Lvl2")).build()).desc("test");

        getBuilder(Strings.keybladeOfPeoplesHearts).keychain(Strings.keybladeOfPeoplesHeartsChain).baseStats(11,5).level( new KeybladeLevel.KeybladeLevelBuilder()
                .withStr(3).withMag(3).withAbilty("Test_Abiltity")
                .withMaterials(kslm.getMap(Strings.keybladeOfPeoplesHearts + "_Lvl1")).build()).level( new KeybladeLevel.KeybladeLevelBuilder()
                .withStr(4).withMag(4).withMaterials(kslm.getMap(Strings.keybladeOfPeoplesHearts + "_Lvl2")).build()).desc("test");

        getBuilder(Strings.jungleKing).keychain(Strings.jungleKingChain).baseStats(5,1).level( new KeybladeLevel.KeybladeLevelBuilder()
                .withStr(3).withMag(3).withAbilty("Test_Abiltity")
                .withMaterials(kslm.getMap(Strings.jungleKing + "_Lvl1")).build()).level( new KeybladeLevel.KeybladeLevelBuilder()
                .withStr(4).withMag(4).withMaterials(kslm.getMap(Strings.jungleKing + "_Lvl2")).build()).desc("test");

        getBuilder(Strings.oathkeeper).keychain(Strings.oathkeeperChain).baseStats(8,4).level( new KeybladeLevel.KeybladeLevelBuilder()
                .withStr(3).withMag(3).withAbilty("Test_Abiltity")
                .withMaterials(kslm.getMap(Strings.oathkeeper + "_Lvl1")).build()).level( new KeybladeLevel.KeybladeLevelBuilder()
                .withStr(4).withMag(4).withMaterials(kslm.getMap(Strings.oathkeeper + "_Lvl2")).build()).desc("test");

        getBuilder(Strings.oblivion).keychain(Strings.oblivionChain).baseStats(10,-1).level( new KeybladeLevel.KeybladeLevelBuilder()
                .withStr(3).withMag(3).withAbilty("Test_Abiltity")
                .withMaterials(kslm.getMap(Strings.oblivion + "_Lvl1")).build()).level( new KeybladeLevel.KeybladeLevelBuilder()
                .withStr(4).withMag(4).withMaterials(kslm.getMap(Strings.oblivion + "_Lvl2")).build()).desc("test");

        getBuilder(Strings.ultimaWeaponKH1).keychain(Strings.ultimaWeaponKH1Chain).baseStats(12,8).level( new KeybladeLevel.KeybladeLevelBuilder()
                .withStr(13).withMag(10).withAbilty("Test_Abiltity")
                .withMaterials(kslm.getMap(Strings.ultimaWeaponKH1 + "Lvl1")).build()).level( new KeybladeLevel.KeybladeLevelBuilder()
                .withStr(16).withMag(12).withMaterials(kslm.getMap(Strings.ultimaWeaponKH1 + "Lvl2")).build()).desc("test");

        getBuilder(Strings.wishingStar).keychain(Strings.wishingStarChain).baseStats(5,2).level( new KeybladeLevel.KeybladeLevelBuilder()
                .withStr(3).withMag(3).withAbilty("Test_Abiltity")
                .withMaterials(kslm.getMap(Strings.wishingStar + "Lvl1")).build()).level( new KeybladeLevel.KeybladeLevelBuilder()
                .withStr(4).withMag(4).withMaterials(kslm.getMap(Strings.wishingStar + "Lvl2")).build()).desc("test");

        getBuilder(Strings.ladyLuck).keychain(Strings.ladyLuckChain).baseStats(6,6).level( new KeybladeLevel.KeybladeLevelBuilder()
                .withStr(3).withMag(3).withAbilty("Test_Abiltity")
                .withMaterials(kslm.getMap(Strings.wishingStar + "Lvl1")).build()).level( new KeybladeLevel.KeybladeLevelBuilder()
                .withStr(4).withMag(4).withMaterials(kslm.getMap(Strings.wishingStar + "Lvl2")).build()).desc("test");

        getBuilder(Strings.olympia).keychain(Strings.olympiaChain).baseStats(9,0).level( new KeybladeLevel.KeybladeLevelBuilder()
                .withStr(3).withMag(3).withAbilty("Test_Abiltity")
                .withMaterials(kslm.getMap(Strings.olympia + "Lvl1")).build()).level( new KeybladeLevel.KeybladeLevelBuilder()
                .withStr(4).withMag(4).withMaterials(kslm.getMap(Strings.olympia + "Lvl2")).build()).desc("test");

        getBuilder(Strings.threeWishes).keychain(Strings.threeWishesChain).baseStats(6,2).level( new KeybladeLevel.KeybladeLevelBuilder()
                .withStr(3).withMag(3).withAbilty("Test_Abiltity")
                .withMaterials(kslm.getMap(Strings.threeWishes + "Lvl1")).build()).level( new KeybladeLevel.KeybladeLevelBuilder()
                .withStr(4).withMag(4).withMaterials(kslm.getMap(Strings.threeWishes + "Lvl2")).build()).desc("test");

        */

    }

    public void kh2Keyblades() {


    }

    public void bbsKeyblades() {

    }

    public void dddKeyblades() {

    }

    public void uxKeyblades() {

    }

    public void kh358Keyblades() {

    }
    
    public void khReCodedKeyblade() {
    	
    }

    @Override
    public String getName() {
        return "Keyblade json";
    }
}

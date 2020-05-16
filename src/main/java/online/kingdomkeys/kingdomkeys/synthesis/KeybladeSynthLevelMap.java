package online.kingdomkeys.kingdomkeys.synthesis;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.synthesis.material.Material;

import java.util.HashMap;
import java.util.Map;

public class KeybladeSynthLevelMap {
    private Map<String, Map> masterMap = new HashMap<>();
    public KeybladeSynthLevelMap()
    {
        init();

    }
    public void init()
    {
        kh1Keyblades();
        kh2Keyblades();
        kh3Keyblades();
        bbsKeyblades();
        dddKeyblades();
        khdKeyblades();
        uxKeyblades();
    }

    public void kh1Keyblades()
    {
        Map<Material, Integer> kingdomkeyLvl1 = new HashMap<>();
        kingdomkeyLvl1.put(GameRegistry.findRegistry(Material.class).getValue(new ResourceLocation("kingdomkeys:mat_" + Strings.SM_BlazingCrystal)), 4);
        kingdomkeyLvl1.put(GameRegistry.findRegistry(Material.class).getValue(new ResourceLocation("kingdomkeys:mat_" + Strings.SM_Orichalcum)), 6);
        masterMap.put(Strings.kingdomKey +"_Lvl1",kingdomkeyLvl1);
        Map<Material, Integer> kingdomkeyLvl2 = new HashMap<>();
        kingdomkeyLvl2.put(GameRegistry.findRegistry(Material.class).getValue(new ResourceLocation("kingdomkeys:mat_" + Strings.SM_BrightCrystal)), 4);
        kingdomkeyLvl2.put(GameRegistry.findRegistry(Material.class).getValue(new ResourceLocation("kingdomkeys:mat_" + Strings.SM_OrichalcumPlus)), 6);
        masterMap.put(Strings.kingdomKey +"_Lvl2",kingdomkeyLvl2);

        Map<Material, Integer> kingdomkeyDLvl1 = new HashMap<>();
        kingdomkeyDLvl1.put(GameRegistry.findRegistry(Material.class).getValue(new ResourceLocation("kingdomkeys:mat_" + Strings.SM_BlazingCrystal)), 4);
        kingdomkeyDLvl1.put(GameRegistry.findRegistry(Material.class).getValue(new ResourceLocation("kingdomkeys:mat_" + Strings.SM_Orichalcum)), 6);
        masterMap.put(Strings.kingdomKeyD +"_Lvl1",kingdomkeyDLvl1);
        Map<Material, Integer> kingdomkeyDLvl2 = new HashMap<>();
        kingdomkeyDLvl2.put(GameRegistry.findRegistry(Material.class).getValue(new ResourceLocation("kingdomkeys:mat_" + Strings.SM_BrightCrystal)), 4);
        kingdomkeyDLvl2.put(GameRegistry.findRegistry(Material.class).getValue(new ResourceLocation("kingdomkeys:mat_" + Strings.SM_OrichalcumPlus)), 6);
        masterMap.put(Strings.kingdomKeyD +"_Lvl2",kingdomkeyDLvl2);

        Map<Material, Integer> KeybladeOfPeoplesHeartsLvl1 = new HashMap<>();
        KeybladeOfPeoplesHeartsLvl1.put(GameRegistry.findRegistry(Material.class).getValue(new ResourceLocation("kingdomkeys:mat_" + Strings.SM_BlazingCrystal)), 4);
        KeybladeOfPeoplesHeartsLvl1.put(GameRegistry.findRegistry(Material.class).getValue(new ResourceLocation("kingdomkeys:mat_" + Strings.SM_Orichalcum)), 6);
        masterMap.put(Strings.keybladeOfPeoplesHearts +"_Lvl1",KeybladeOfPeoplesHeartsLvl1);
        Map<Material, Integer> KeybladeOfPeoplesHeartsLvl2 = new HashMap<>();
        KeybladeOfPeoplesHeartsLvl2.put(GameRegistry.findRegistry(Material.class).getValue(new ResourceLocation("kingdomkeys:mat_" + Strings.SM_BrightCrystal)), 4);
        KeybladeOfPeoplesHeartsLvl2.put(GameRegistry.findRegistry(Material.class).getValue(new ResourceLocation("kingdomkeys:mat_" + Strings.SM_OrichalcumPlus)), 6);
        masterMap.put(Strings.keybladeOfPeoplesHearts+"_Lvl2",KeybladeOfPeoplesHeartsLvl2);

        Map<Material, Integer> JungleKingLvl1 = new HashMap<>();
        JungleKingLvl1.put(GameRegistry.findRegistry(Material.class).getValue(new ResourceLocation("kingdomkeys:mat_" + Strings.SM_BlazingCrystal)), 4);
        JungleKingLvl1.put(GameRegistry.findRegistry(Material.class).getValue(new ResourceLocation("kingdomkeys:mat_" + Strings.SM_Orichalcum)), 6);
        masterMap.put(Strings.jungleKing +"_Lvl1", JungleKingLvl1);
        Map<Material, Integer> JungleKingLvl2 = new HashMap<>();
        JungleKingLvl2.put(GameRegistry.findRegistry(Material.class).getValue(new ResourceLocation("kingdomkeys:mat_" + Strings.SM_BrightCrystal)), 4);
        JungleKingLvl2.put(GameRegistry.findRegistry(Material.class).getValue(new ResourceLocation("kingdomkeys:mat_" + Strings.SM_OrichalcumPlus)), 6);
        masterMap.put(Strings.jungleKing + "_Lvl2", JungleKingLvl2);

        Map<Material, Integer> OathkeeperLvl1 = new HashMap<>();
        OathkeeperLvl1.put(GameRegistry.findRegistry(Material.class).getValue(new ResourceLocation("kingdomkeys:mat_" + Strings.SM_BlazingCrystal)), 4);
        OathkeeperLvl1.put(GameRegistry.findRegistry(Material.class).getValue(new ResourceLocation("kingdomkeys:mat_" + Strings.SM_Orichalcum)), 6);
        masterMap.put(Strings.oathkeeper +"_Lvl1", OathkeeperLvl1);
        Map<Material, Integer> OathkeeperLvl2 = new HashMap<>();
        OathkeeperLvl2.put(GameRegistry.findRegistry(Material.class).getValue(new ResourceLocation("kingdomkeys:mat_" + Strings.SM_BrightCrystal)), 4);
        OathkeeperLvl2.put(GameRegistry.findRegistry(Material.class).getValue(new ResourceLocation("kingdomkeys:mat_" + Strings.SM_OrichalcumPlus)), 6);
        masterMap.put(Strings.oathkeeper + "_Lvl2", OathkeeperLvl2);

        Map<Material, Integer> OblivionLvl1 = new HashMap<>();
        OblivionLvl1.put(GameRegistry.findRegistry(Material.class).getValue(new ResourceLocation("kingdomkeys:mat_" + Strings.SM_BlazingCrystal)), 4);
        OblivionLvl1.put(GameRegistry.findRegistry(Material.class).getValue(new ResourceLocation("kingdomkeys:mat_" + Strings.SM_Orichalcum)), 6);
        masterMap.put(Strings.oblivion +"_Lvl1", OblivionLvl1);
        Map<Material, Integer> OblivionLvl2 = new HashMap<>();
        OblivionLvl2.put(GameRegistry.findRegistry(Material.class).getValue(new ResourceLocation("kingdomkeys:mat_" + Strings.SM_BrightCrystal)), 4);
        OblivionLvl2.put(GameRegistry.findRegistry(Material.class).getValue(new ResourceLocation("kingdomkeys:mat_" + Strings.SM_OrichalcumPlus)), 6);
        masterMap.put(Strings.oblivion + "_Lvl2", OblivionLvl2);

        Map<Material, Integer> ultKH1Lvl1 = new HashMap<>();
        ultKH1Lvl1.put(GameRegistry.findRegistry(Material.class).getValue(new ResourceLocation("kingdomkeys:mat_" + Strings.SM_BlazingCrystal)), 4);
        ultKH1Lvl1.put(GameRegistry.findRegistry(Material.class).getValue(new ResourceLocation("kingdomkeys:mat_" + Strings.SM_Orichalcum)), 6);
        masterMap.put(Strings.ultimaWeaponKH1 +"_Lvl1", ultKH1Lvl1);
        Map<Material, Integer> ultKH1Lvl2 = new HashMap<>();
        ultKH1Lvl2.put(GameRegistry.findRegistry(Material.class).getValue(new ResourceLocation("kingdomkeys:mat_" + Strings.SM_BrightCrystal)), 4);
        ultKH1Lvl2.put(GameRegistry.findRegistry(Material.class).getValue(new ResourceLocation("kingdomkeys:mat_" + Strings.SM_OrichalcumPlus)), 6);
        masterMap.put(Strings.ultimaWeaponKH1 + "_Lvl2", ultKH1Lvl2);

        Map<Material, Integer> wishingstarLvl1 = new HashMap<>();
        wishingstarLvl1.put(GameRegistry.findRegistry(Material.class).getValue(new ResourceLocation("kingdomkeys:mat_" + Strings.SM_BlazingCrystal)), 4);
        wishingstarLvl1.put(GameRegistry.findRegistry(Material.class).getValue(new ResourceLocation("kingdomkeys:mat_" + Strings.SM_Orichalcum)), 6);
        masterMap.put(Strings.wishingStar +"_Lvl1", wishingstarLvl1);
        Map<Material, Integer> wishingstarLvl2 = new HashMap<>();
        wishingstarLvl2.put(GameRegistry.findRegistry(Material.class).getValue(new ResourceLocation("kingdomkeys:mat_" + Strings.SM_BrightCrystal)), 4);
        wishingstarLvl2.put(GameRegistry.findRegistry(Material.class).getValue(new ResourceLocation("kingdomkeys:mat_" + Strings.SM_OrichalcumPlus)), 6);
        masterMap.put(Strings.wishingStar + "_Lvl2", wishingstarLvl2);

        Map<Material, Integer> ladyLuckLvl1 = new HashMap<>();
        ladyLuckLvl1.put(GameRegistry.findRegistry(Material.class).getValue(new ResourceLocation("kingdomkeys:mat_" + Strings.SM_BlazingCrystal)), 4);
        ladyLuckLvl1.put(GameRegistry.findRegistry(Material.class).getValue(new ResourceLocation("kingdomkeys:mat_" + Strings.SM_Orichalcum)), 6);
        masterMap.put(Strings.ladyLuck +"_Lvl1", ladyLuckLvl1);
        Map<Material, Integer> ladyLuckLvl2 = new HashMap<>();
        ladyLuckLvl2.put(GameRegistry.findRegistry(Material.class).getValue(new ResourceLocation("kingdomkeys:mat_" + Strings.SM_BrightCrystal)), 4);
        ladyLuckLvl2.put(GameRegistry.findRegistry(Material.class).getValue(new ResourceLocation("kingdomkeys:mat_" + Strings.SM_OrichalcumPlus)), 6);
        masterMap.put(Strings.ladyLuck + "_Lvl2", ladyLuckLvl2);

        Map<Material, Integer> olympiaLvl1 = new HashMap<>();
        olympiaLvl1.put(GameRegistry.findRegistry(Material.class).getValue(new ResourceLocation("kingdomkeys:mat_" + Strings.SM_BlazingCrystal)), 4);
        olympiaLvl1.put(GameRegistry.findRegistry(Material.class).getValue(new ResourceLocation("kingdomkeys:mat_" + Strings.SM_Orichalcum)), 6);
        masterMap.put(Strings.olympia +"_Lvl1", olympiaLvl1);
        Map<Material, Integer> olympiaLvl2 = new HashMap<>();
        olympiaLvl2.put(GameRegistry.findRegistry(Material.class).getValue(new ResourceLocation("kingdomkeys:mat_" + Strings.SM_BrightCrystal)), 4);
        olympiaLvl2.put(GameRegistry.findRegistry(Material.class).getValue(new ResourceLocation("kingdomkeys:mat_" + Strings.SM_OrichalcumPlus)), 6);
        masterMap.put(Strings.olympia + "_Lvl2", olympiaLvl2);

        Map<Material, Integer> threeWishesLvl1 = new HashMap<>();
        threeWishesLvl1.put(GameRegistry.findRegistry(Material.class).getValue(new ResourceLocation("kingdomkeys:mat_" + Strings.SM_BlazingCrystal)), 4);
        threeWishesLvl1.put(GameRegistry.findRegistry(Material.class).getValue(new ResourceLocation("kingdomkeys:mat_" + Strings.SM_Orichalcum)), 6);
        masterMap.put(Strings.olympia +"_Lvl1", olympiaLvl1);
        Map<Material, Integer> threeWishesLvl2 = new HashMap<>();
        threeWishesLvl2.put(GameRegistry.findRegistry(Material.class).getValue(new ResourceLocation("kingdomkeys:mat_" + Strings.SM_BrightCrystal)), 4);
        threeWishesLvl2.put(GameRegistry.findRegistry(Material.class).getValue(new ResourceLocation("kingdomkeys:mat_" + Strings.SM_OrichalcumPlus)), 6);
        masterMap.put(Strings.threeWishes + "_Lvl2", threeWishesLvl2);

    }

    public void kh2Keyblades()
    {

    }

    public void kh3Keyblades()
    {
        Map<Material, Integer> ultimaKH3Lvl1 = new HashMap<>();
        ultimaKH3Lvl1.put(GameRegistry.findRegistry(Material.class).getValue(new ResourceLocation("kingdomkeys:mat_" + Strings.SM_BlazingCrystal)), 4);
        ultimaKH3Lvl1.put(GameRegistry.findRegistry(Material.class).getValue(new ResourceLocation("kingdomkeys:mat_" + Strings.SM_Orichalcum)), 6);
        masterMap.put("kh3ultLvl1",ultimaKH3Lvl1);
        Map<Material, Integer> ultimaKH3Lvl2 = new HashMap<>();
        ultimaKH3Lvl2.put(GameRegistry.findRegistry(Material.class).getValue(new ResourceLocation("kingdomkeys:mat_" + Strings.SM_BrightCrystal)), 4);
        ultimaKH3Lvl2.put(GameRegistry.findRegistry(Material.class).getValue(new ResourceLocation("kingdomkeys:mat_" + Strings.SM_OrichalcumPlus)), 6);
        masterMap.put("kh3ultLvl2",ultimaKH3Lvl2);
    }

    public void bbsKeyblades()
    {

    }

    public void dddKeyblades()
    {

    }

    public void uxKeyblades()
    {

    }

    public void khdKeyblades()
    {

    }

    public Map getMap(String string){
        return masterMap.get(string);
    }
}

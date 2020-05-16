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



    }

    public void kh2Keyblades()
    {
        Map<Material, Integer> kingdomkeyDLvl1 = new HashMap<>();
        kingdomkeyDLvl1.put(GameRegistry.findRegistry(Material.class).getValue(new ResourceLocation("kingdomkeys:mat_" + Strings.SM_BlazingCrystal)), 4);
        kingdomkeyDLvl1.put(GameRegistry.findRegistry(Material.class).getValue(new ResourceLocation("kingdomkeys:mat_" + Strings.SM_Orichalcum)), 6);
        masterMap.put(Strings.kingdomKeyD +"_Lvl1",kingdomkeyDLvl1);
        Map<Material, Integer> kingdomkeyDLvl2 = new HashMap<>();
        kingdomkeyDLvl2.put(GameRegistry.findRegistry(Material.class).getValue(new ResourceLocation("kingdomkeys:mat_" + Strings.SM_BrightCrystal)), 4);
        kingdomkeyDLvl2.put(GameRegistry.findRegistry(Material.class).getValue(new ResourceLocation("kingdomkeys:mat_" + Strings.SM_OrichalcumPlus)), 6);
        masterMap.put(Strings.kingdomKeyD +"_Lvl2",kingdomkeyDLvl2);
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

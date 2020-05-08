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
        Map<Material, Integer> ultimaKH3Lvl1 = new HashMap<>();
        masterMap.put("kh3ultLvl1",ultimaKH3Lvl1);

        Map<Material, Integer> ultimaKH3Lvl2 = new HashMap<>();
        masterMap.put("kh3ultLvl2",ultimaKH3Lvl2);
    }


    public Map getMap(String string){
        return masterMap.get(string);
    }
}

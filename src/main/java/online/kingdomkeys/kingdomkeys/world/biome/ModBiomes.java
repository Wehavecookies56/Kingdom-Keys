package online.kingdomkeys.kingdomkeys.world.biome;


import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

public class ModBiomes {

    public static final RegistryKey<Biome> diveToTheHeart = createNewBiome("dive_to_the_heart");

    private static RegistryKey<Biome> createNewBiome(String bName) {
        return RegistryKey.getOrCreateKey(Registry.BIOME_KEY, new ResourceLocation(KingdomKeys.MODID, bName));
    }

    public static void init() {
        BiomeDictionary.addTypes(diveToTheHeart, BiomeDictionary.Type.VOID);
    }


}


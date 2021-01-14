package online.kingdomkeys.kingdomkeys.world.biome;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeMaker;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

public class ModBiomes {

    public static final DeferredRegister<Biome> BIOMES = DeferredRegister.create(ForgeRegistries.BIOMES, KingdomKeys.MODID);

    static {
        BIOMES.register("dive_to_the_heart", BiomeMaker::makeVoidBiome);
    }
}


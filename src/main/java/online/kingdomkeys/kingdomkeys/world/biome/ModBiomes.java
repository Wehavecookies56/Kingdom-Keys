package online.kingdomkeys.kingdomkeys.world.biome;

import java.awt.Color;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.RainType;
import net.minecraft.world.biome.BiomeAmbience;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import online.kingdomkeys.kingdomkeys.KingdomKeys;


public class ModBiomes {

    public static final DeferredRegister<Biome> BIOMES = DeferredRegister.create(ForgeRegistries.BIOMES, KingdomKeys.MODID);

    public static final RegistryObject<Biome> diveToTheHeart = createNewBiome("dive_to_the_heart_biome", "Dive to the Heart");
    //traverseTown = createNewBiome("traverse_town_biome", "Traverse Town")

    private static RegistryObject<Biome> createNewBiome(String bName, String dName) {
        RegistryObject<Biome> newBiome = BIOMES.register(bName, () -> new ModBiomeBase(new Biome.Builder()
                .precipitation(RainType.NONE)
                .setEffects(new BiomeAmbience()4159204)
                .temperature(0.5F)
                .parent(dName)
                .surfaceBuilder(SurfaceBuilder.NOPE, SurfaceBuilder.STONE_STONE_GRAVEL_CONFIG)
                .category(Biome.Category.NONE)
                .depth(0.1F)
                .scale(0.2F)
                .downfall(0.5F)
                .waterFogColor(329011)
        ));
        return newBiome;
    }

    public static void init() {
        BiomeDictionary.addTypes(diveToTheHeart.get(), BiomeDictionary.Type.VOID);
        //BiomeDictionary.addTypes(traverseTown.get(), BiomeDictionary.Type.DRY, BiomeDictionary.Type.MAGICAL, BiomeDictionary.Type.VOID);
    }


}

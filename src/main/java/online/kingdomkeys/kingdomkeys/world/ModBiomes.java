package online.kingdomkeys.kingdomkeys.world;

import java.awt.Color;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.RainType;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import online.kingdomkeys.kingdomkeys.KingdomKeys;


public class ModBiomes {

    public static final DeferredRegister<Biome> BIOMES = new DeferredRegister<>(ForgeRegistries.BIOMES, KingdomKeys.MODID);

    public static final RegistryObject<Biome>
    traverseTownBiome = createNewBiome("traverse_town_biome", "Traverse Town");
    
    private static RegistryObject<Biome> createNewBiome(String bName, String dName) {
        RegistryObject<Biome> newBiome = BIOMES.register(bName, () -> new ModBiomeBase(new Biome.Builder()
                .precipitation(RainType.NONE)
                .waterColor(new Color(76, 225, 255).hashCode())
                .temperature(0.8F)
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
       // BiomeDictionary.addTypes(traverseTownBiome.get(), BiomeDictionary.Type.WET, BiomeDictionary.Type.BEACH);
       // BiomeDictionary.addTypes(DiveToTheHeartBiome, BiomeDictionary.Type.VOID);
        BiomeDictionary.addTypes(traverseTownBiome.get(), BiomeDictionary.Type.DRY, BiomeDictionary.Type.MAGICAL, BiomeDictionary.Type.VOID);
    }

    /*@SubscribeEvent
    public void registerBiomes(RegistryEvent.Register<Biome> event) {
    	
        event.getRegistry().register(new BiomeBase(new Biome.Builder()
                .precipitation(RainType.NONE)
                .waterColor(new Color(76, 225, 255).hashCode())
                .temperature(0.8F)
                .parent("Destiny Islands")
        ).setRegistryName("biome_destinyislands"));
        
        event.getRegistry().register(new BiomeBase(new Biome.Builder()
                .precipitation(RainType.NONE)
                .parent("Station of Awakening")
        ).setRegistryName("biome_divetotheheart"));
        
        event.getRegistry().register(new BiomeBase(new Biome.Builder()
                .precipitation(RainType.NONE)
                .parent("Traverse Town")
        ).setRegistryName("biome_traversetown"));
    }*/


}

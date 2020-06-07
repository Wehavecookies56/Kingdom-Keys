package online.kingdomkeys.kingdomkeys.world;

import net.minecraftforge.common.ModDimension;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.lib.Strings;

public class ModDimensions {

	public static final DeferredRegister<ModDimension> DIMENSIONS = new DeferredRegister<>(ForgeRegistries.MOD_DIMENSIONS, KingdomKeys.MODID);

	public static final RegistryObject<ModDimension> traverseTownDimension = createNewDimension(Strings.twilightTown);

    private static RegistryObject<ModDimension> createNewDimension(String name) {
        RegistryObject<ModDimension> newDimension = DIMENSIONS.register(name, () -> new KKDimension(name)//traverseTownDimension.get().getFactory())
               
        );
        return newDimension;
    }


  /*  public static void init() {
       // BiomeDictionary.addTypes(traverseTownBiome.get(), BiomeDictionary.Type.WET, BiomeDictionary.Type.BEACH);
       // BiomeDictionary.addTypes(DiveToTheHeartBiome, BiomeDictionary.Type.VOID);
      //  BiomeDictionary.addTypes(traverseTownBiome.get(), BiomeDictionary.Type.DRY, BiomeDictionary.Type.MAGICAL, BiomeDictionary.Type.VOID);
    	
    	DimensionManager.registerDimension(new ResourceLocation(KingdomKeys.MODID, "traverse_town"), traverseTownDimension.get(), null, false);
    }*/
    
    /*public static DimensionType diveToTheHeart, traverseTown, destinyIslands;
    public static int diveToTheHeartID, traverseTownID, destinyIslandsID;

    public static void init() {
        diveToTheHeartID = getDimIDFromString(MainConfig.worldgen.StationOfAwakeningID);
        diveToTheHeart = DimensionType.register("Dive to the Heart", "", diveToTheHeartID, WorldProviderDiveToTheHeart.class, false);
        DimensionManager.registerDimension(diveToTheHeartID, diveToTheHeart);
        traverseTownID = getDimIDFromString(MainConfig.worldgen.TraverseTownID);
        traverseTown = DimensionType.register("Traverse Town", "", traverseTownID, WorldProviderTraverseTown.class, false);
        DimensionManager.registerDimension(traverseTownID, traverseTown);
        destinyIslandsID = getDimIDFromString(MainConfig.worldgen.DestinyIslandsID);
        destinyIslands = DimensionType.register("Destiny Islands", "", destinyIslandsID, WorldProviderDestinyIslands.class, false);
        DimensionManager.registerDimension(destinyIslandsID, destinyIslands);
    }

    static int getDimIDFromString(String string) {
        return string.equals("auto") ? DimensionManager.getNextFreeDimId() : Integer.parseInt(string);
    }*/
	
	
}

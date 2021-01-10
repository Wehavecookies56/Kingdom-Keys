package online.kingdomkeys.kingdomkeys.world.dimension;

import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

@Mod.EventBusSubscriber
public class ModDimensions {

    public static RegistryKey<DimensionType> DIVE_TO_THE_HEART_TYPE = RegistryKey.getOrCreateKey(Registry.DIMENSION_TYPE_KEY, new ResourceLocation(KingdomKeys.MODID, "dive_to_the_heart"));
    public static RegistryKey<World> DIVE_TO_THE_HEART = RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation(KingdomKeys.MODID, "dive_to_the_heart"));


	/*
	public static final DeferredRegister<ModDimension> DIMENSIONS = DeferredRegister.create(ForgeRegistries.MOD_DIMENSIONS, KingdomKeys.MODID);

	public static final RegistryObject<ModDimension>
            diveToTheHeartDimension = createNewDimension(Strings.diveToTheHeart);
    traverseTownDimension = createNewDimension(Strings.twilightTown),

    private static RegistryObject<ModDimension> createNewDimension(String name) {
        RegistryObject<ModDimension> newDimension = DIMENSIONS.register(name, () -> new KingdomKeysModDimension(name));
        return newDimension;
    }

    @SubscribeEvent
    public static void dimensionRegistry(RegisterDimensionsEvent event) {
        DIVE_TO_THE_HEART_TYPE = DimensionManager.registerOrGetDimension(new ResourceLocation(KingdomKeys.MODID, Strings.diveToTheHeart), diveToTheHeartDimension.get(), null, true);
    }
    */
}

package online.kingdomkeys.kingdomkeys.world.dimension;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ModDimension;
import net.minecraftforge.event.world.RegisterDimensionsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.lib.Strings;

@Mod.EventBusSubscriber
public class ModDimensions {

    public static DimensionType DIVE_TO_THE_HEART_TYPE;

	public static final DeferredRegister<ModDimension> DIMENSIONS = DeferredRegister.create(ForgeRegistries.MOD_DIMENSIONS, KingdomKeys.MODID);

	public static final RegistryObject<ModDimension>
            diveToTheHeartDimension = createNewDimension(Strings.diveToTheHeart);
    //traverseTownDimension = createNewDimension(Strings.twilightTown),

    private static RegistryObject<ModDimension> createNewDimension(String name) {
        RegistryObject<ModDimension> newDimension = DIMENSIONS.register(name, () -> new KingdomKeysModDimension(name));
        return newDimension;
    }

    @SubscribeEvent
    public static void dimensionRegistry(RegisterDimensionsEvent event) {
        DIVE_TO_THE_HEART_TYPE = DimensionManager.registerOrGetDimension(new ResourceLocation(KingdomKeys.MODID, Strings.diveToTheHeart), diveToTheHeartDimension.get(), null, true);
    }

}

package online.kingdomkeys.kingdomkeys.world.features;

import net.minecraft.world.gen.feature.Feature;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

public class ModFeatures {

    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, KingdomKeys.MODID);

    public static final RegistryObject<Feature<BloxOreFeatureConfig>> BLOX = FEATURES.register("blox", () -> new BloxOreFeature(BloxOreFeatureConfig.CODEC));

}

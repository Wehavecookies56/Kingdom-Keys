package online.kingdomkeys.kingdomkeys.world.biome;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.TheVoidBiome;

public final class ModBiomeBase extends Biome {
   public ModBiomeBase(Biome.Builder builder) {
      super(builder);
      //this.addFeature(GenerationStage.Decoration.TOP_LAYER_MODIFICATION, Feature.VOID_START_PLATFORM.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.NOPE.configure(IPlacementConfig.NO_PLACEMENT_CONFIG)));
   }
}

package online.kingdomkeys.kingdomkeys.world.biome;

//Potentially completely replaced by json

import net.minecraft.entity.EntityClassification;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeGenerationSettings;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.gen.GenerationStage;

public class DiveToTheHeartBiome {

    public Biome build() {
        Biome.Builder builder = new Biome.Builder();
        BiomeGenerationSettings.Builder settingsBuilder = new BiomeGenerationSettings.Builder();
        //settingsBuilder.withFeature(GenerationStage.Decoration.RAW_GENERATION, ) platform
        MobSpawnInfo.Builder spawnBuilder = new MobSpawnInfo.Builder();
        builder.withGenerationSettings(settingsBuilder.build());
        builder.withMobSpawnSettings(spawnBuilder.copy());
        return builder.build();
    }

}

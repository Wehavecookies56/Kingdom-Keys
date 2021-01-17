package online.kingdomkeys.kingdomkeys.entity;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.registries.ForgeRegistries;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.lib.Strings;

public class MobSpawnings {

    public static void addSpawns() {
        String[] moogle = ModConfigs.mobSpawnRate.get(0).split(",");
        String[] pureblood = ModConfigs.mobSpawnRate.get(1).split(",");
        String[] emblem = ModConfigs.mobSpawnRate.get(2).split(",");
        String[] nobody = ModConfigs.mobSpawnRate.get(3).split(",");

        for(Biome b : ForgeRegistries.BIOMES){
            if(b.getDefaultTemperature() >= 0.3 && b.getDefaultTemperature() <= 1.0) {
                b.getSpawns(ModEntities.TYPE_MOOGLE.get().getClassification()).add(new Biome.SpawnListEntry(ModEntities.TYPE_MOOGLE.get(), Integer.parseInt(moogle[1]), Integer.parseInt(moogle[2]), Integer.parseInt(moogle[3])));
            }

            for(EntityType<?> entityType : ModEntities.pureblood) {
                b.getSpawns(entityType.getClassification()).add(new Biome.SpawnListEntry(entityType, Integer.parseInt(pureblood[1]), Integer.parseInt(pureblood[2]), Integer.parseInt(pureblood[3])));
            }
            for(EntityType<?> entityType : ModEntities.emblem) {
                b.getSpawns(entityType.getClassification()).add(new Biome.SpawnListEntry(entityType, Integer.parseInt(emblem[1]), Integer.parseInt(emblem[2]), Integer.parseInt(emblem[3])));
            }
            for(EntityType<?> entityType : ModEntities.nobody) {
                b.getSpawns(entityType.getClassification()).add(new Biome.SpawnListEntry(entityType, Integer.parseInt(nobody[1]), Integer.parseInt(nobody[2]), Integer.parseInt(nobody[3])));
            }
        }
        //Remove all entity spawns added to the Dive to the Heart biome
        Biome dtth = ForgeRegistries.BIOMES.getValue(new ResourceLocation(KingdomKeys.MODID, Strings.diveToTheHeart + "_biome"));
        for (EntityClassification entityClassification : EntityClassification.values()) {
            dtth.getSpawns(entityClassification).clear();
        }
    }

}

package online.kingdomkeys.kingdomkeys.entity;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.biome.Biome.Category;
import net.minecraft.world.gen.GenerationStage;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.config.CommonConfig;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.lib.Strings;

public class MobSpawnings {

	public static void registerSpawns(BiomeLoadingEvent event) {
		String[] moogle = CommonConfig.mobSpawnRate.get().get(0).split(",");
		String[] pureblood = CommonConfig.mobSpawnRate.get().get(1).split(",");
		String[] emblem = CommonConfig.mobSpawnRate.get().get(2).split(",");
		String[] nobody = CommonConfig.mobSpawnRate.get().get(3).split(",");
		
		if(event.getClimate().temperature >= 0.3 && event.getClimate().temperature <= 1.0) {
			event.getSpawns().getSpawner(ModEntities.TYPE_MOOGLE.get().getClassification()).add(new MobSpawnInfo.Spawners(ModEntities.TYPE_MOOGLE.get(), Integer.parseInt(moogle[1]),Integer.parseInt(moogle[2]), Integer.parseInt(moogle[3])));
		}
		for(EntityType<?> entityType : ModEntities.pureblood) {
			event.getSpawns().getSpawner(entityType.getClassification()).add(new MobSpawnInfo.Spawners(entityType, Integer.parseInt(pureblood[1]),Integer.parseInt(pureblood[2]), Integer.parseInt(pureblood[3])));
		}
		for(EntityType<?> entityType : ModEntities.emblem) {
			event.getSpawns().getSpawner(entityType.getClassification()).add(new MobSpawnInfo.Spawners(entityType, Integer.parseInt(emblem[1]),Integer.parseInt(emblem[2]), Integer.parseInt(emblem[3])));
		}
		for(EntityType<?> entityType : ModEntities.nobody) {
			event.getSpawns().getSpawner(entityType.getClassification()).add(new MobSpawnInfo.Spawners(entityType, Integer.parseInt(nobody[1]),Integer.parseInt(nobody[2]), Integer.parseInt(nobody[3])));
		}
		
		if (event.getName().equals(new ResourceLocation(KingdomKeys.MODID, Strings.diveToTheHeart))) {
			//Remove all entity spawns added to the Dive to the Heart biome
			for (EntityClassification entityClassification : EntityClassification.values()) {
				event.getSpawns().getSpawner(entityClassification).clear();
			}
			for (GenerationStage.Decoration i : GenerationStage.Decoration.values()) {
				event.getGeneration().getFeatures(i).clear();
			}
		}
	}

}

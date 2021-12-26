package online.kingdomkeys.kingdomkeys.entity;

import java.util.List;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.gen.GenerationStage;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.lib.Strings;

public class MobSpawnings {

	public static void registerSpawns(BiomeLoadingEvent event) {
		String[] moogle = ModConfigs.moogleSpawnRate.get(0).split(",");
		String[] enemies = ModConfigs.moogleSpawnRate.get(1).split(",");
		
		if (event.getClimate().temperature >= 0.3 && event.getClimate().temperature <= 1.0) {
			event.getSpawns().getSpawner(ModEntities.TYPE_MOOGLE.get().getClassification()).add(new MobSpawnInfo.Spawners(ModEntities.TYPE_MOOGLE.get(), Integer.parseInt(moogle[1]), Integer.parseInt(moogle[2]), Integer.parseInt(moogle[3])));
		}
		
		event.getSpawns().getSpawner(ModEntities.TYPE_SPAWNING_ORB.get().getClassification()).add(new MobSpawnInfo.Spawners(ModEntities.TYPE_SPAWNING_ORB.get(), Integer.parseInt(enemies[1]), Integer.parseInt(enemies[2]), Integer.parseInt(enemies[3])));

		/*for (EntityType<?> entityType : ModEntities.pureblood) {
			event.getSpawns().getSpawner(entityType.getClassification()).add(new MobSpawnInfo.Spawners(entityType, Integer.parseInt(pureblood[1]), Integer.parseInt(pureblood[2]), Integer.parseInt(pureblood[3])));
		}
		for (EntityType<?> entityType : ModEntities.emblem) {
			event.getSpawns().getSpawner(entityType.getClassification()).add(new MobSpawnInfo.Spawners(entityType, Integer.parseInt(emblem[1]), Integer.parseInt(emblem[2]), Integer.parseInt(emblem[3])));
		}
		for (EntityType<?> entityType : ModEntities.nobody) {
			event.getSpawns().getSpawner(entityType.getClassification()).add(new MobSpawnInfo.Spawners(entityType, Integer.parseInt(nobody[1]), Integer.parseInt(nobody[2]), Integer.parseInt(nobody[3])));
		}*/
	}

	public static void removeSpawns(BiomeLoadingEvent event) {
		if (event.getName().equals(new ResourceLocation(KingdomKeys.MODID, Strings.diveToTheHeart)) || event.getName().equals(new ResourceLocation(KingdomKeys.MODID, Strings.stationOfSorrow))) {
			//Remove all entity spawns added to the Dive to the Heart and Station of Sorrow biome
			for (EntityClassification entityClassification : EntityClassification.values()) {
				event.getSpawns().getSpawner(entityClassification).clear();
			}
			for (GenerationStage.Decoration i : GenerationStage.Decoration.values()) {
				event.getGeneration().getFeatures(i).clear();
			}
		}
	}

}

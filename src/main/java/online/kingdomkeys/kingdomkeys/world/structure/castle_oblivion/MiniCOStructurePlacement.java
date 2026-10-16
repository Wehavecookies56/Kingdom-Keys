package online.kingdomkeys.kingdomkeys.world.structure.castle_oblivion;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.world.structure.ModStructures;

public class MiniCOStructurePlacement extends RandomSpreadStructurePlacement {
	public static final MapCodec<MiniCOStructurePlacement> CODEC = MapCodec.unit(MiniCOStructurePlacement::new);

	public MiniCOStructurePlacement() {
		super(4096, 0, RandomSpreadType.LINEAR, 0);
	}

	private static ChunkPos chunk() {
		return new ChunkPos(ModConfigs.coEntranceChunkX, ModConfigs.coEntranceChunkZ);
	}

	@Override
	public ChunkPos getPotentialStructureChunk(long seed, int regionX, int regionZ) {
		return chunk();
	}

	@Override
	protected boolean isPlacementChunk(ChunkGeneratorStructureState structureState, int x, int z) {
		ChunkPos chunk = chunk();
		return ModConfigs.generateCOEntrance && chunk.x == x && chunk.z == z;
	}

	@Override
	public StructurePlacementType<?> type() {
		return ModStructures.MINI_CO_PLACEMENT.get();
	}
}

package online.kingdomkeys.kingdomkeys.world.structure.castle_oblivion;

import java.util.Optional;

import org.jetbrains.annotations.NotNull;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.PostPlacementProcessor;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.CastleOblivionInteriorChunkGenerator;
import online.kingdomkeys.kingdomkeys.world.structure.JigsawPlacementRotation;

public class CastleOblivionLobbyStructure implements StructureType<FeatureConfiguration> {

    public static final Codec<FeatureConfiguration> CODEC = RecordCodecBuilder.create((codec) -> codec.group(
            StructureTemplatePool.CODEC.fieldOf("start_pool").forGetter(FeatureConfiguration::startPool),
            Codec.intRange(0, 30).fieldOf("size").forGetter(FeatureConfiguration::maxDepth)
    ).apply(codec, FeatureConfiguration::new));

    public CastleOblivionLobbyStructure() {
        super(CODEC, CastleOblivionLobbyStructure::createPiecesGenerator, PostPlacementProcessor.NONE);
    }

    @Override
    public GenerationStep.Decoration step() {
        return GenerationStep.Decoration.SURFACE_STRUCTURES;
    }

    //Only generate at 1 chunk so 1 structure generates
    private static boolean isFeatureChunk(PieceGeneratorSupplier.Context<FeatureConfiguration> context) {
        return context.chunkPos().equals(new ChunkPos(0, 0));
    }

    public static @NotNull Optional<PieceGenerator<FeatureConfiguration>> createPiecesGenerator(PieceGeneratorSupplier.Context<FeatureConfiguration> context) {
        if (!isFeatureChunk(context) || !(context.chunkGenerator() instanceof CastleOblivionInteriorChunkGenerator)) {
            return Optional.empty();
        }
        BlockPos pos = new BlockPos(context.chunkPos().getBlockX(0), 0, context.chunkPos().getBlockZ(0));
        pos = pos.above(60);
        //it's best that this does not change
        Rotation rotation = Rotation.NONE;

        Optional<PieceGenerator<FeatureConfiguration>> structurePiecesGenerator = JigsawPlacementRotation.addPieces(context, PoolElementStructurePiece::new, pos, rotation, false, false);
        if (structurePiecesGenerator.isPresent()) {
            KingdomKeys.LOGGER.debug("Castle Oblivion Lobby generated at {}", pos);
        }
        return structurePiecesGenerator;
    }

	@Override
	public Codec<FeatureConfiguration> codec() {
		// TODO Auto-generated method stub
		return CODEC;
	}

}

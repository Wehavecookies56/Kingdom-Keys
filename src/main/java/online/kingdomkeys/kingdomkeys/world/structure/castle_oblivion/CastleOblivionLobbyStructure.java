package online.kingdomkeys.kingdomkeys.world.structure.castle_oblivion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.PostPlacementProcessor;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.CastleOblivionChunkGenerator;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.CastleOblivionInteriorChunkGenerator;
import online.kingdomkeys.kingdomkeys.world.structure.JigsawPlacementRotation;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class CastleOblivionLobbyStructure extends StructureFeature<JigsawConfiguration> {

    public static final Codec<JigsawConfiguration> CODEC = RecordCodecBuilder.create((codec) -> codec.group(
            StructureTemplatePool.CODEC.fieldOf("start_pool").forGetter(JigsawConfiguration::startPool),
            Codec.intRange(0, 30).fieldOf("size").forGetter(JigsawConfiguration::maxDepth)
    ).apply(codec, JigsawConfiguration::new));

    public CastleOblivionLobbyStructure() {
        super(CODEC, CastleOblivionLobbyStructure::createPiecesGenerator, PostPlacementProcessor.NONE);
    }

    @Override
    public GenerationStep.Decoration step() {
        return GenerationStep.Decoration.SURFACE_STRUCTURES;
    }

    //Only generate at 1 chunk so 1 structure generates
    private static boolean isFeatureChunk(PieceGeneratorSupplier.Context<JigsawConfiguration> context) {
        return context.chunkPos().equals(new ChunkPos(0, 0));
    }

    public static @NotNull Optional<PieceGenerator<JigsawConfiguration>> createPiecesGenerator(PieceGeneratorSupplier.Context<JigsawConfiguration> context) {
        if (!isFeatureChunk(context) || !(context.chunkGenerator() instanceof CastleOblivionInteriorChunkGenerator)) {
            return Optional.empty();
        }
        BlockPos pos = new BlockPos(context.chunkPos().getBlockX(0), 0, context.chunkPos().getBlockZ(0));
        pos = pos.above(60);
        //it's best that this does not change
        Rotation rotation = Rotation.NONE;

        Optional<PieceGenerator<JigsawConfiguration>> structurePiecesGenerator = JigsawPlacementRotation.addPieces(context, PoolElementStructurePiece::new, pos, rotation, false, false);
        if (structurePiecesGenerator.isPresent()) {
            KingdomKeys.LOGGER.debug("Castle Oblivion Lobby generated at {}", pos);
        }
        return structurePiecesGenerator;
    }

}

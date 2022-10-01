package online.kingdomkeys.kingdomkeys.world.structure.castle_oblivion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.*;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.PostPlacementProcessor;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraft.world.level.levelgen.structure.pools.*;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.CastleOblivionChunkGenerator;
import online.kingdomkeys.kingdomkeys.world.structure.JigsawPlacementRotation;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CastleOblivionStructure extends StructureFeature<JigsawConfiguration> {

    public static final Codec<JigsawConfiguration> CODEC = RecordCodecBuilder.create((codec) -> codec.group(
            StructureTemplatePool.CODEC.fieldOf("start_pool").forGetter(JigsawConfiguration::startPool),
            Codec.intRange(0, 30).fieldOf("size").forGetter(JigsawConfiguration::maxDepth)
    ).apply(codec, JigsawConfiguration::new));

    public CastleOblivionStructure() {
        super(CODEC, CastleOblivionStructure::createPiecesGenerator, PostPlacementProcessor.NONE);
    }

    @Override
    public GenerationStep.Decoration step() {
        return GenerationStep.Decoration.SURFACE_STRUCTURES;
    }

    //Only generate at 1 chunk so 1 structure generates
    private static boolean isFeatureChunk(PieceGeneratorSupplier.Context<JigsawConfiguration> context) {
        //presenting the dumbest way to generate the structure in 7 parts
        if (context.config().startPool().value().getName().getPath().contains("_0")) {
            return context.chunkPos().equals(new ChunkPos(0, 0));
        }
        if (context.config().startPool().value().getName().getPath().contains("_1")) {
            return context.chunkPos().equals(new ChunkPos(0, -9));
        }
        if (context.config().startPool().value().getName().getPath().contains("_2")) {
            return context.chunkPos().equals(new ChunkPos(0, -18));
        }
        if (context.config().startPool().value().getName().getPath().contains("_3")) {
            return context.chunkPos().equals(new ChunkPos(-9, 0));
        }
        if (context.config().startPool().value().getName().getPath().contains("_4")) {
            return context.chunkPos().equals(new ChunkPos(-9, -9));
        }
        if (context.config().startPool().value().getName().getPath().contains("_5")) {
            return context.chunkPos().equals(new ChunkPos(-9, -18));
        }
        if (context.config().startPool().value().getName().getPath().contains("_6")) {
            return context.chunkPos().equals(new ChunkPos(-18, 0));
        }
        return false;
    }

    public static @NotNull Optional<PieceGenerator<JigsawConfiguration>> createPiecesGenerator(PieceGeneratorSupplier.Context<JigsawConfiguration> context) {
        if (!isFeatureChunk(context) || !(context.chunkGenerator() instanceof CastleOblivionChunkGenerator)) {
            return Optional.empty();
        }
        BlockPos pos = new BlockPos(context.chunkPos().getBlockX(0), 0, context.chunkPos().getBlockZ(0));
        pos = pos.above(0);
        //it's best that this does not change
        Rotation rotation = Rotation.NONE;

        Optional<PieceGenerator<JigsawConfiguration>> structurePiecesGenerator = JigsawPlacementRotation.addPieces(context, PoolElementStructurePiece::new, pos, rotation, false, false);
        if (structurePiecesGenerator.isPresent()) {
            KingdomKeys.LOGGER.debug("Castle Oblivion generated at {}", pos);
        }
        return structurePiecesGenerator;
    }

}

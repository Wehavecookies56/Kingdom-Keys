package online.kingdomkeys.kingdomkeys.world.structure.castle_oblivion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.CastleOblivionChunkGenerator;
import online.kingdomkeys.kingdomkeys.world.structure.JigsawPlacementRotation;
import online.kingdomkeys.kingdomkeys.world.structure.ModStructures;

import java.util.Optional;

public class CastleOblivionStructure extends Structure {

    public static final Codec<CastleOblivionStructure> CODEC = RecordCodecBuilder.<CastleOblivionStructure>create((instance) -> instance.group(
            CastleOblivionLobbyStructure.settingsCodec(instance),
            StructureTemplatePool.CODEC.fieldOf("start_pool").forGetter(structure -> structure.startPool),
            Codec.intRange(0, 30).fieldOf("size").forGetter(structure -> structure.size),
            Codec.intRange(0, 6).fieldOf("piece_index").forGetter(structure -> structure.pieceIndex)
    ).apply(instance, CastleOblivionStructure::new));

    private final Holder<StructureTemplatePool> startPool;
    private final int size;
    private final int pieceIndex;

    public CastleOblivionStructure(Structure.StructureSettings config, Holder<StructureTemplatePool> startPool, int size, int pieceIndex) {
        super(config);
        this.startPool = startPool;
        this.size = size;
        this.pieceIndex = pieceIndex;
    }

    @Override
    protected Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
        if (!isFeatureChunk(context, pieceIndex) || !(context.chunkGenerator() instanceof CastleOblivionChunkGenerator)) {
            return Optional.empty();
        }
        BlockPos pos = new BlockPos(context.chunkPos().getBlockX(0), 0, context.chunkPos().getBlockZ(0));
        pos = pos.above(0);
        //it's best that this does not change
        Rotation rotation = Rotation.NONE;

        Optional<Structure.GenerationStub> structurePiecesGenerator = JigsawPlacementRotation.addPieces(context, startPool, Optional.empty(), this.size, pos, rotation, false, Optional.empty(), 128);
        if (structurePiecesGenerator.isPresent()) {
            KingdomKeys.LOGGER.debug("Castle Oblivion generated at {}", pos);
        }
        return structurePiecesGenerator;
    }

    @Override
    public StructureType<?> type() {
        return ModStructures.CASTLE_OBLIVION.get();
    }

    //Only generate at 1 chunk so 1 structure generates
    private static boolean isFeatureChunk(GenerationContext context, int pieceIndex) {
        //presenting the dumbest way to generate the structure in 7 parts
        return switch (pieceIndex) {
            case 1 -> context.chunkPos().equals(new ChunkPos(0, -9));
            case 2 -> context.chunkPos().equals(new ChunkPos(0, -18));
            case 3 -> context.chunkPos().equals(new ChunkPos(-9, 0));
            case 4 -> context.chunkPos().equals(new ChunkPos(-9, -9));
            case 5 -> context.chunkPos().equals(new ChunkPos(-9, -18));
            case 6 -> context.chunkPos().equals(new ChunkPos(-18, 0));
            default -> context.chunkPos().equals(new ChunkPos(0, 0));
        };
    }
}


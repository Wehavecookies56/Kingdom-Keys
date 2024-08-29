package online.kingdomkeys.kingdomkeys.world.structure.castle_oblivion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pools.DimensionPadding;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.pools.alias.PoolAliasLookup;
import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.CastleOblivionInteriorChunkGenerator;
import online.kingdomkeys.kingdomkeys.world.structure.JigsawPlacementRotation;
import online.kingdomkeys.kingdomkeys.world.structure.ModStructures;

import java.util.Optional;

public class CastleOblivionLobbyStructure extends Structure {

    public static final MapCodec<CastleOblivionLobbyStructure> CODEC = RecordCodecBuilder.<CastleOblivionLobbyStructure>mapCodec(instance ->
            instance.group(CastleOblivionLobbyStructure.settingsCodec(instance),
                    StructureTemplatePool.CODEC.fieldOf("start_pool").forGetter(structure -> structure.startPool),
                    Codec.intRange(0, 30).fieldOf("size").forGetter(structure -> structure.size)
            ).apply(instance, CastleOblivionLobbyStructure::new));

    private final Holder<StructureTemplatePool> startPool;
    private final int size;

    public CastleOblivionLobbyStructure(StructureSettings config, Holder<StructureTemplatePool> startPool, int size) {
        super(config);
        this.startPool = startPool;
        this.size = size;
    }
    @Override
    protected Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
        if (!isFeatureChunk(context) || !(context.chunkGenerator() instanceof CastleOblivionInteriorChunkGenerator)) {
            return Optional.empty();
        }
        BlockPos pos = new BlockPos(context.chunkPos().getBlockX(0), 0, context.chunkPos().getBlockZ(0));
        pos = pos.above(60);
        //it's best that this does not change
        Rotation rotation = Rotation.NONE;

        Optional<GenerationStub> structurePiecesGenerator = JigsawPlacementRotation.addPieces(context, this.startPool, Optional.empty(), this.size, pos, false, Optional.empty(), 128, PoolAliasLookup.EMPTY, new DimensionPadding(0), LiquidSettings.IGNORE_WATERLOGGING, rotation);
        if (structurePiecesGenerator.isPresent()) {
            KingdomKeys.LOGGER.debug("Castle Oblivion Lobby generated at {}", pos);
        }
        return structurePiecesGenerator;
    }

    @Override
    public StructureType<?> type() {
        return ModStructures.CASTLE_OBLIVION_LOBBY.get();
    }

    //Only generate at 1 chunk so 1 structure generates
    private static boolean isFeatureChunk(GenerationContext context) {
        return context.chunkPos().equals(new ChunkPos(0, 0));
    }
}

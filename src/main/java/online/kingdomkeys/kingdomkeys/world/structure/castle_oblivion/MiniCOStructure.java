package online.kingdomkeys.kingdomkeys.world.structure.castle_oblivion;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pools.DimensionPadding;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.pools.alias.PoolAliasLookup;
import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;
import online.kingdomkeys.kingdomkeys.world.structure.JigsawPlacementRotation;
import online.kingdomkeys.kingdomkeys.world.structure.ModStructures;

import java.util.Optional;

public class MiniCOStructure extends Structure {
	public static final MapCodec<MiniCOStructure> CODEC = RecordCodecBuilder.mapCodec(instance ->
			instance.group(settingsCodec(instance),
					StructureTemplatePool.CODEC.fieldOf("start_pool").forGetter(structure -> structure.startPool)
			).apply(instance, MiniCOStructure::new));

	private static final int SUNK = 2;

	private final Holder<StructureTemplatePool> startPool;

	public MiniCOStructure(StructureSettings settings, Holder<StructureTemplatePool> startPool) {
		super(settings);
		this.startPool = startPool;
	}

	@Override
	protected Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
		StructurePoolElement element = startPool.value().getRandomTemplate(context.random());
		Vec3i size = element.getSize(context.structureTemplateManager(), Rotation.NONE);
		int x = context.chunkPos().getMinBlockX();
		int z = context.chunkPos().getMinBlockZ();
		// The jigsaw puts the piece's base that much under the height it is given
		int y = context.chunkGenerator().getFirstFreeHeight(x + size.getX() / 2, z + size.getZ() / 2, Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor(), context.randomState()) - SUNK + element.getGroundLevelDelta();

		return JigsawPlacementRotation.addPieces(context, startPool, Optional.empty(), 1, new BlockPos(x, y, z), false, Optional.empty(), 128, PoolAliasLookup.EMPTY, new DimensionPadding(0), LiquidSettings.IGNORE_WATERLOGGING, Rotation.NONE);
	}

	@Override
	public StructureType<?> type() {
		return ModStructures.MINI_CO.get();
	}
}

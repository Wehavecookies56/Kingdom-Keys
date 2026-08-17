package online.kingdomkeys.kingdomkeys.world.dimension.prebuilt;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

/**
 * The sea a {@link PrebuiltWorldChunkGenerator} pours around a hand built world, so an island world sits in
 * an ocean instead of hanging over void.
 *
 * It is a sea bed rather than a slab: bedrock at the very bottom, stone above it and a few blocks of sand on
 * top, with the surface rolling gently so it doesn't read as a floor tile. The relief comes from a hash, not
 * from the world seed, which keeps a prebuilt world identical in every save.
 *
 * @param water        what the sea itself is made of
 * @param level        surface of the water, exclusive, the same sense as vanilla's sea level
 * @param floor        average Y of the topmost bed block. Match it to the export's own sea bed and the seam disappears
 * @param variation    how far below {@code floor} the bed is allowed to dip
 * @param surfaceDepth how many blocks of {@code surface} sit on top of the {@code body}
 * @param body         the bulk of the bed, under the surface
 * @param surface      what the bed is dressed with, sand for a warm ocean
 * @param patches      scattered through the surface for variety, gravel or clay
 * @param bedrock      floor of the world. Absent leaves the bottom open
 */
public record SeaSettings(BlockState water, int level, int floor, int variation, int surfaceDepth, BlockState body, BlockState surface, Optional<BlockState> patches, Optional<BlockState> bedrock) {

	// A plain block name rather than BlockState's {"Name", "Properties"} pair: these are all bulk blocks and the dimension json is written by hand
	private static final Codec<BlockState> BLOCK = BuiltInRegistries.BLOCK.byNameCodec().xmap(Block::defaultBlockState, BlockState::getBlock);

	public static final Codec<SeaSettings> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			BLOCK.optionalFieldOf("water", Blocks.WATER.defaultBlockState()).forGetter(SeaSettings::water),
			Codec.INT.fieldOf("level").forGetter(SeaSettings::level),
			Codec.INT.fieldOf("floor").forGetter(SeaSettings::floor),
			Codec.INT.optionalFieldOf("variation", 3).forGetter(SeaSettings::variation),
			Codec.INT.optionalFieldOf("surface_depth", 4).forGetter(SeaSettings::surfaceDepth),
			BLOCK.optionalFieldOf("body", Blocks.STONE.defaultBlockState()).forGetter(SeaSettings::body),
			BLOCK.optionalFieldOf("surface", Blocks.SAND.defaultBlockState()).forGetter(SeaSettings::surface),
			BLOCK.optionalFieldOf("patches").forGetter(SeaSettings::patches),
			BLOCK.optionalFieldOf("bedrock").forGetter(SeaSettings::bedrock)
	).apply(instance, SeaSettings::new));

	// How many layers the bedrock appears over, bottom one always solid
	private static final int BEDROCK_DEPTH = 4;
	// Block spacing of the lattice the bed's relief is interpolated over
	private static final int RELIEF = 24;
	// And of the patches, which want to be small enough to read as patches
	private static final int PATCH = 7;
	private static final float PATCH_THRESHOLD = 0.62F;

	/** Topmost solid block of the bed under this column */
	public int floorAt(int x, int z) {
		if (variation <= 0) {
			return floor;
		}

		// Squared so the bed mostly sits at its stated height and only dips here and there, instead of spending its time halfway down. It also keeps the step where it meets a build down to a block.
		float dip = noise(x, z, RELIEF, 0);
		return floor - Math.round(dip * dip * variation);
	}

	// solidTop the highest block of this column, which is not always floorAt
	public BlockState bed(int x, int y, int z, int bottom, int solidTop) {
		BlockState rock = bedrock.orElse(null);

		// Solid at the very bottom and increasingly patchy above it, the way vanilla frays its own
		if (rock != null && y <= bottom + BEDROCK_DEPTH && (y == bottom || Math.floorMod(hash(x, y, z), BEDROCK_DEPTH + 1) >= y - bottom)) {
			return rock;
		}

		if (y > solidTop - surfaceDepth) {
			return patches.isPresent() && noise(x, z, PATCH, 1) > PATCH_THRESHOLD ? patches.get() : surface;
		}

		return body;
	}

	// Value noise: a hashed height every <spacing> blocks, smoothed between them. Cheap, seamless across chunk borders because it only ever looks at world coordinates, and identical on every run.
	private static float noise(int x, int z, int spacing, int salt) {
		int cellX = Math.floorDiv(x, spacing);
		int cellZ = Math.floorDiv(z, spacing);

		float fx = smooth((x - cellX * spacing) / (float) spacing);
		float fz = smooth((z - cellZ * spacing) / (float) spacing);

		float north = lattice(cellX, cellZ, salt) + (lattice(cellX + 1, cellZ, salt) - lattice(cellX, cellZ, salt)) * fx;
		float south = lattice(cellX, cellZ + 1, salt) + (lattice(cellX + 1, cellZ + 1, salt) - lattice(cellX, cellZ + 1, salt)) * fx;

		return north + (south - north) * fz;
	}

	private static float smooth(float t) {
		return t * t * (3 - 2 * t);
	}

	private static float lattice(int x, int z, int salt) {
		return (hash(x, z, salt) >>> 40) / (float) (1L << 24);
	}

	private static long hash(int x, int y, int z) {
		long h = x * 0x9E3779B97F4A7C15L ^ y * 0xC2B2AE3D27D4EB4FL ^ z * 0x165667B19E3779F9L;
		h ^= h >>> 29;
		h *= 0xBF58476D1CE4E5B9L;
		h ^= h >>> 32;
		return h;
	}
}

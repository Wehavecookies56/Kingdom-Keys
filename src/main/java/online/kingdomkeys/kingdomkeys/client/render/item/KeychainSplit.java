package online.kingdomkeys.kingdomkeys.client.render.item;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.model.BakedModelWrapper;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A keyblade taken apart into the weapon and the chain that hangs off it, worked out from the model that was
 * actually baked rather than from a second set of model files.
 * <p>
 * Doing it here rather than in the assets is what makes it survive resource packs. A pack that replaces a
 * keyblade replaces its obj, and an obj carries no promise about how its groups are named, so a split that
 * lives in the files stops matching the moment somebody ships their own model: the halves come out wrong, or
 * the material names stop lining up and the model fails to load at all. Whatever geometry wins the resource
 * lookup is what arrives here, so the split is made against that.
 */
@OnlyIn(Dist.CLIENT)
public final class KeychainSplit {
	private static final RandomSource RANDOM = RandomSource.create();

	/** How close two vertices have to be, as a share of the model's height, to count as the same point */
	private static final float WELD = 1.0E-4F;

	/** How close in height a vertex has to be to the highest one to count as part of the same top face */
	private static final float TOP_FACE = 1.0E-4F;

	/** A chain bigger than this share of the weapon means the cut landed somewhere silly, so nothing is split */
	private static final float MAX_SHARE = 0.6F;

	public final BakedModel blade;
	public final BakedModel keychain;

	/** The point the chain hangs from, in model space */
	public final Vector3f hinge;

	private KeychainSplit(BakedModel blade, BakedModel keychain, Vector3f hinge) {
		this.blade = blade;
		this.keychain = keychain;
		this.hinge = hinge;
	}

	/**
	 * Splits a baked keyblade at the given height, or returns null if that leaves nothing sensible to swing.
	 *
	 * @param cut where to cut, as a share of the model's height measured from its lowest point. A share rather
	 *            than a coordinate because a resource pack is free to model the same weapon at another scale,
	 *            and the chain still starts about a quarter of the way up whatever units it used.
	 */
	public static KeychainSplit of(BakedModel model, float cut) {
		List<Bucketed> quads = collect(model);

		if (quads.isEmpty()) {
			return null;
		}

		float low = Float.POSITIVE_INFINITY, high = Float.NEGATIVE_INFINITY;

		for (Bucketed quad : quads) {
			low = Math.min(low, quad.low);
			high = Math.max(high, quad.high);
		}

		float span = high - low;

		if (span <= 0.0F) {
			return null;
		}

		// Whole connected pieces, never a flat slice. A guard often reaches further down than the chain starts,
		// so cutting on height alone would saw a wing in half and hang it off the hilt. Grouping first means a
		// piece goes wherever its topmost point goes and stays in one piece either way.
		int[] owner = connect(quads, span);
		float[] tops = new float[quads.size()];

		Arrays.fill(tops, Float.NEGATIVE_INFINITY);

		for (int i = 0; i < quads.size(); i++) {
			int root = find(owner, i);
			tops[root] = Math.max(tops[root], quads.get(i).high);
		}

		float height = low + cut * span;
		List<Bucketed> chain = new ArrayList<>();
		List<Bucketed> rest = new ArrayList<>();

		for (int i = 0; i < quads.size(); i++) {
			(tops[find(owner, i)] <= height ? chain : rest).add(quads.get(i));
		}

		if (chain.isEmpty() || rest.isEmpty() || chain.size() > quads.size() * MAX_SHARE) {
			return null;
		}

		return new KeychainSplit(new Half(model, rest), new Half(model, chain), hinge(chain));
	}

	/**
	 * The middle of the chain's top face, which is where it meets the weapon.
	 * <p>
	 * Averaged across the whole face rather than taken from whichever corner happened to be highest: a single
	 * corner sits half a link off the axis, so the chain would turn about a point beside itself and slide
	 * sideways instead of pivoting.
	 */
	private static Vector3f hinge(List<Bucketed> chain) {
		float best = Float.NEGATIVE_INFINITY;

		for (Bucketed quad : chain) {
			best = Math.max(best, quad.high);
		}

		float x = 0.0F, z = 0.0F;
		int counted = 0;

		for (Bucketed quad : chain) {
			int[] vertices = quad.quad.getVertices();
			int stride = vertices.length / 4;

			for (int i = 0; i < 4; i++) {
				int at = i * stride;

				if (Math.abs(Float.intBitsToFloat(vertices[at + 1]) - best) < TOP_FACE) {
					x += Float.intBitsToFloat(vertices[at]);
					z += Float.intBitsToFloat(vertices[at + 2]);
					counted++;
				}
			}
		}

		return counted == 0 ? new Vector3f(0.0F, best, 0.0F) : new Vector3f(x / counted, best, z / counted);
	}

	/**
	 * Every quad the model holds, with the face it was filed under kept alongside it.
	 * <p>
	 * All seven buckets, not just the unculled one. A baked model files a quad under the face that would hide
	 * it, and a chain modelled straight down the middle has every link sitting on one plane exactly, so reading
	 * only the null bucket can come back with nothing at all.
	 */
	private static List<Bucketed> collect(BakedModel model) {
		List<Bucketed> out = new ArrayList<>();

		for (BakedQuad quad : model.getQuads(null, null, RANDOM)) {
			out.add(new Bucketed(quad, null));
		}

		for (Direction direction : Direction.values()) {
			for (BakedQuad quad : model.getQuads(null, direction, RANDOM)) {
				out.add(new Bucketed(quad, direction));
			}
		}

		return out;
	}

	/** Union find over quads that share a corner, so each quad ends up pointing at the piece it belongs to */
	private static int[] connect(List<Bucketed> quads, float span) {
		int[] owner = new int[quads.size()];

		for (int i = 0; i < owner.length; i++) {
			owner[i] = i;
		}

		// Keyed on the three rounded coordinates themselves rather than on a number mixed out of them: a hash
		// collision here would weld two corners that are nowhere near each other and fuse the chain to the
		// blade, which is a silent and very confusing way to fail.
		Map<Corner, Integer> corners = new HashMap<>();
		float grid = span * WELD;

		for (int i = 0; i < quads.size(); i++) {
			int[] vertices = quads.get(i).quad.getVertices();
			int stride = vertices.length / 4;

			for (int v = 0; v < 4; v++) {
				int at = v * stride;
				Corner corner = new Corner(
						Math.round(Float.intBitsToFloat(vertices[at]) / grid),
						Math.round(Float.intBitsToFloat(vertices[at + 1]) / grid),
						Math.round(Float.intBitsToFloat(vertices[at + 2]) / grid));
				Integer seen = corners.putIfAbsent(corner, i);

				if (seen != null) {
					union(owner, seen, i);
				}
			}
		}

		return owner;
	}

	/** A vertex snapped to a grid, so two corners that all but touch count as the same point */
	private record Corner(long x, long y, long z) {}

	private static int find(int[] owner, int at) {
		while (owner[at] != at) {
			owner[at] = owner[owner[at]];
			at = owner[at];
		}

		return at;
	}

	private static void union(int[] owner, int a, int b) {
		int ra = find(owner, a), rb = find(owner, b);

		if (ra != rb) {
			owner[ra] = rb;
		}
	}

	/** A quad together with the face bucket it came out of, so it can be put back in the same one */
	private record Bucketed(BakedQuad quad, Direction side, float low, float high) {

		Bucketed(BakedQuad quad, Direction side) {
			this(quad, side, bound(quad, true), bound(quad, false));
		}

		private static float bound(BakedQuad quad, boolean lowest) {
			int[] vertices = quad.getVertices();
			int stride = vertices.length / 4;
			float out = lowest ? Float.POSITIVE_INFINITY : Float.NEGATIVE_INFINITY;

			for (int i = 0; i < 4; i++) {
				float y = Float.intBitsToFloat(vertices[i * stride + 1]);
				out = lowest ? Math.min(out, y) : Math.max(out, y);
			}

			return out;
		}
	}

	/** One side of the split. Everything except the geometry is answered by the model it came from. */
	private static final class Half extends BakedModelWrapper<BakedModel> {

		private final List<BakedQuad> loose = new ArrayList<>();
		private final Map<Direction, List<BakedQuad>> sided = new EnumMap<>(Direction.class);

		Half(BakedModel original, List<Bucketed> quads) {
			super(original);

			for (Direction direction : Direction.values()) {
				sided.put(direction, new ArrayList<>());
			}

			for (Bucketed quad : quads) {
				(quad.side() == null ? loose : sided.get(quad.side())).add(quad.quad());
			}
		}

		@Override
		public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource random) {
			return side == null ? loose : sided.get(side);
		}

		// Both overloads, or anything reaching for the extended one gets the whole weapon back
		@Override
		public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource random, ModelData data, @Nullable RenderType renderType) {
			return getQuads(state, side, random);
		}
	}
}

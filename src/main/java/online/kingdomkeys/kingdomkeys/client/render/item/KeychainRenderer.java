package online.kingdomkeys.kingdomkeys.client.render.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.model.CompositeModel;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import org.joml.Matrix3f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Draws a keyblade as two pieces so the keychain can hang and swing instead of being welded to the hilt.
 * <p>
 * The obj carries both halves as named groups, and the two model files bake it twice with opposite
 * {@code visibility}, which is what lets each half be drawn on its own transform. By the time this runs,
 * {@code ItemRenderer} has already applied the display transform of the model the item points at, so both
 * halves are drawn raw, in the same space, and only the keychain gets an extra rotation.
 */
@OnlyIn(Dist.CLIENT)
public class KeychainRenderer extends BlockEntityWithoutLevelRenderer {

	/**
	 * Keyblades whose obj has been split into a base and a keychain group. Only these get the two piece
	 * treatment; every other keyblade keeps rendering the way it always has, so nothing breaks while the rest
	 * of the models are still in one piece.
	 */
	public static final List<ResourceLocation> SPLIT_KEYBLADES = List.of(
			KingdomKeys.rl(Strings.kingdomKey),
			KingdomKeys.rl(Strings.oblivion),
			KingdomKeys.rl(Strings.oathkeeper),
			KingdomKeys.rl(Strings.bondOfFlame),
			KingdomKeys.rl(Strings.bondOfTheBlaze),
			KingdomKeys.rl(Strings.starCluster),
			KingdomKeys.rl(Strings.twoBecomeOne),
			KingdomKeys.rl(Strings.ultimaWeaponBBS),
			KingdomKeys.rl(Strings.ultimaWeaponDDD),
			KingdomKeys.rl(Strings.ultimaWeaponKH1),
			KingdomKeys.rl(Strings.ultimaWeaponKH2),
			KingdomKeys.rl(Strings.ultimaWeaponKH3),
			KingdomKeys.rl(Strings.mastersDefender),
			KingdomKeys.rl(Strings.youngXehanortsKeyblade),
			KingdomKeys.rl(Strings.noName),
			KingdomKeys.rl(Strings.noNameBBS),
			KingdomKeys.rl(Strings.wayToTheDawn),
			KingdomKeys.rl(Strings.mirageSplit),
			KingdomKeys.rl(Strings.nightmaresEndAndMirageSplit),
			KingdomKeys.rl(Strings.voidGear),
			KingdomKeys.rl(Strings.kiblade),
			KingdomKeys.rl(Strings.incompleteKiblade),
			KingdomKeys.rl(Strings.kingdomKeyD),
			KingdomKeys.rl(Strings.kingdomKeyN),
			KingdomKeys.rl(Strings.fenrir),
			KingdomKeys.rl(Strings.deadOfNight));

	/**
	 * The model holding both halves. A composite, whose children are the same obj baked twice with opposite
	 * visibility, because visibility is a bake time switch and a BakedModel has no memory of which group a
	 * quad came from: ObjModel.addQuads pours every visible group into one builder. Two bakes are unavoidable,
	 * but the composite loader at least keeps them in one file and hands them back by name.
	 */
	public static ModelResourceLocation partsModel(ResourceLocation keyblade) {
		return ModelResourceLocation.standalone(KingdomKeys.rl("item/keyblade/" + keyblade.getPath() + "_parts"));
	}

	public static final String BLADE_PART = "blade";
	public static final String KEYCHAIN_PART = "keychain";

	/** Where the chain points in the model when nothing is pulling on it: straight down the hilt */
	private static final Vector3f REST = new Vector3f(0.0F, -1.0F, 0.0F);

	/** How hard the holder's movement drags the chain off vertical. One block per tick would lay it flat at 3 */
	private static final float TRAIL = 3.0F;
	/** Idle wobble, as a sideways fraction of straight down, so a still chain is not perfectly rigid */
	private static final float SWAY = 0.05F;
	private static final float PERIOD_TICKS = 27.0F;

	private static final RandomSource RANDOM = RandomSource.create();

	/** Worked out from the baked quads the first time each keyblade is drawn, one hinge per model */
	private static final Map<ResourceLocation, Vector3f> PIVOTS = new HashMap<>();

	public KeychainRenderer() {
		super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
	}

	@Override
	public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
		Minecraft minecraft = Minecraft.getInstance();
		ResourceLocation keyblade = BuiltInRegistries.ITEM.getKey(stack.getItem());

		if (!(minecraft.getModelManager().getModel(partsModel(keyblade)) instanceof CompositeModel.Baked parts)) {
			KingdomKeys.LOGGER.warn("{} is listed as split but its parts model is missing or is not a composite", keyblade);
			return;
		}

		BakedModel blade = parts.getPart(BLADE_PART);
		BakedModel keychain = parts.getPart(KEYCHAIN_PART);

		if (blade == null || keychain == null) {
			KingdomKeys.LOGGER.warn("The parts model for {} is missing a {} or {} child", keyblade, BLADE_PART, KEYCHAIN_PART);
			return;
		}

		ItemRenderer itemRenderer = minecraft.getItemRenderer();

		Vector3f hinge = pivot(keyblade, keychain);

		// The blade keeps the render type the item would have had anyway
		VertexConsumer bladeConsumer = ItemRenderer.getFoilBufferDirect(buffer, ItemBlockRenderTypes.getRenderType(stack, true), true, stack.hasFoil());
		itemRenderer.renderModelLists(blade, stack, packedLight, packedOverlay, poseStack, bladeConsumer);

		// The chain links and the token are flat cards with no thickness, so the culling render type the item
		// would normally get makes them vanish from behind. They need the two sided sheet.
		VertexConsumer keychainConsumer = ItemRenderer.getFoilBufferDirect(buffer, Sheets.translucentItemSheet(), true, stack.hasFoil());

		poseStack.pushPose();
		{
			// Turn about the point the chain hangs from rather than the model origin, which is out at the blade
			poseStack.translate(hinge.x(), hinge.y(), hinge.z());
			poseStack.mulPose(hangRotation(displayContext, poseStack, minecraft));
			poseStack.translate(-hinge.x(), -hinge.y(), -hinge.z());

			itemRenderer.renderModelLists(keychain, stack, packedLight, packedOverlay, poseStack, keychainConsumer);
		}
		poseStack.popPose();
	}

	/**
	 * The turn that takes the chain from where the model puts it to where it ought to hang.
	 * <p>
	 * Worked out in world space, where down is always down no matter how the keyblade is being held, and then
	 * carried back into the model. That is the difference from a plain rotation about a model axis: the chain
	 * does not care which way the weapon is pointing, so slinging it across your back leaves the chain still
	 * pointing at the floor rather than sticking out sideways.
	 */
	private static Quaternionf hangRotation(ItemDisplayContext displayContext, PoseStack poseStack, Minecraft minecraft) {
		// Menus, item frames and dropped items read better left exactly as modelled
		if (displayContext == ItemDisplayContext.GUI || displayContext == ItemDisplayContext.FIXED || displayContext == ItemDisplayContext.GROUND) {
			return new Quaternionf();
		}

		Vector3f target = new Vector3f(0.0F, -1.0F, 0.0F);
		Player player = minecraft.player;
		double vx = 0.0D, vy = 0.0D, vz = 0.0D;

		if (player != null) {
			// Trailing behind the holder is what sells the weight: set off and the chain is left behind, stop
			// and it falls back under the hilt. Taken from the last tick of movement rather than from any
			// stored velocity, so it needs nothing kept between frames
			vx = player.getX() - player.xo;
			vy = player.getY() - player.yo;
			vz = player.getZ() - player.zo;

			target.add((float) (-vx * TRAIL), (float) (-vy * TRAIL), (float) (-vz * TRAIL));
		}

		// A touch of wander so a chain at rest is not dead. Wrapped to one period before it becomes a float:
		// Mth.sin is the lookup SIN[(int)(v * 10430.378F) & 65535], and a float to int cast in Java saturates
		// at Integer.MAX_VALUE instead of wrapping, so a large enough argument always lands on index 65535 and
		// the sine freezes. Handing it the raw game time did exactly that past about 884736 ticks
		long period = (long) PERIOD_TICKS;
		long ticks = minecraft.level == null ? 0L : Math.floorMod(minecraft.level.getGameTime(), period);
		float phase = (ticks + minecraft.getTimer().getGameTimeDeltaPartialTick(false)) / PERIOD_TICKS * Mth.TWO_PI;
		target.add(Mth.sin(phase) * SWAY, 0.0F, Mth.cos(phase * 0.7F) * SWAY);

		target.normalize();

		Vector3f inWorld = new Vector3f(target);

		new Matrix3f(poseStack.last().pose()).invert().transform(target);
		target.normalize();

		trace(minecraft, displayContext, vx, vy, vz, inWorld, target);

		return new Quaternionf().rotateTo(REST.x(), REST.y(), REST.z(), target.x(), target.y(), target.z());
	}

	private static long lastTrace = Long.MIN_VALUE;

	/**
	 * Temporary. Prints the direction the chain is being aimed at, at each step of the way from world space to
	 * model space, so it is possible to tell which of those steps is losing the sideways part of the movement.
	 * Once a second, and only while actually moving, so it stays readable.
	 */
	private static void trace(Minecraft minecraft, ItemDisplayContext displayContext, double vx, double vy, double vz, Vector3f inWorld, Vector3f inModel) {
		if (minecraft.level == null) {
			return;
		}

		long now = minecraft.level.getGameTime();

		if (now - lastTrace < 20L || Math.abs(vx) + Math.abs(vy) + Math.abs(vz) < 0.05D) {
			return;
		}

		lastTrace = now;
		KingdomKeys.LOGGER.info(String.format("keychain %s | vel %+.3f %+.3f %+.3f | world %+.2f %+.2f %+.2f | model %+.2f %+.2f %+.2f", displayContext, vx, vy, vz, inWorld.x(), inWorld.y(), inWorld.z(), inModel.x(), inModel.y(), inModel.z()));
	}

	/**
	 * The topmost point of the keychain, read straight off the baked geometry. Taking it from the quads rather
	 * than from the obj file means it lands in whatever units the loader ended up in, so the hinge cannot drift
	 * out of place if that ever changes.
	 */
	private static Vector3f pivot(ResourceLocation keyblade, BakedModel keychain) {
		Vector3f cached = PIVOTS.get(keyblade);

		if (cached != null) {
			return cached;
		}

		float bestY = Float.NEGATIVE_INFINITY;
		float x = 0.0F, z = 0.0F;

		List<BakedQuad> quads = keychain.getQuads(null, null, RANDOM);

		for (BakedQuad quad : quads) {
			int[] vertices = quad.getVertices();
			int stride = vertices.length / 4;

			for (int i = 0; i < 4; i++) {
				int at = i * stride;
				float vy = Float.intBitsToFloat(vertices[at + 1]);

				if (vy > bestY) {
					bestY = vy;
					x = Float.intBitsToFloat(vertices[at]);
					z = Float.intBitsToFloat(vertices[at + 2]);
				}
			}
		}

		if (bestY == Float.NEGATIVE_INFINITY) {
			// No keychain geometry baked. Hinging on the origin is wrong but harmless, and beats crashing
			KingdomKeys.LOGGER.warn("The keychain model for {} baked with no quads, so its hinge could not be found", keyblade);
			bestY = 0.0F;
		}

		Vector3f hinge = new Vector3f(x, bestY, z);
		PIVOTS.put(keyblade, hinge);

		return hinge;
	}

	/** Clears the cached hinges so a resource reload can pick up an edited model */
	public static void clearCache() {
		PIVOTS.clear();
	}
}

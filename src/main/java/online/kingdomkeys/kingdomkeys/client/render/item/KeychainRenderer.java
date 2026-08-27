package online.kingdomkeys.kingdomkeys.client.render.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import org.joml.Matrix3f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Draws a keyblade as two pieces so the keychain can hang and swing instead of being welded to the hilt.
 * <p>
 * The two halves are worked out by {@link KeychainSplit} from the model that was actually baked, not from a
 * second set of model files. Splitting in the assets meant the result depended on which obj won the resource
 * lookup, and any pack that shipped its own keyblade broke it: its groups are named differently, so nothing
 * got hidden and the whole weapon was drawn twice, and its material names differ, so the model failed to load
 * outright. Reading the baked quads sidesteps both, and a repainted keyblade comes apart like any other.
 * <p>
 * By the time this runs, {@code ItemRenderer} has already applied the display transform of the model the item
 * points at, so both halves are drawn raw, in the same space, and only the keychain gets an extra rotation.
 */
@OnlyIn(Dist.CLIENT)
public class KeychainRenderer extends BlockEntityWithoutLevelRenderer {

	/** Every keyblade this renderer knows how to take apart */
	public static Set<ResourceLocation> splitKeyblades() {
		return CUTS.keySet();
	}

	/**
	 * Where each keyblade is cut, as a share of its height measured up from its lowest point.
	 * <p>
	 * A share and not a coordinate on purpose. A resource pack is free to remodel a keyblade at whatever scale
	 * it likes, and the split is made against whichever model won the resource lookup, so anything expressed in
	 * model units would stop meaning the same thing the moment somebody shipped their own obj. The chain still
	 * begins about a quarter of the way up, whatever units that turns out to be.
	 * <p>
	 * These were measured off the hand checked splits rather than guessed.
	 */
	private static final Map<ResourceLocation, Float> CUTS = new HashMap<>();

	/** Filled in at bake time, one entry per keyblade whose model could actually be taken apart */
	private static final Map<ResourceLocation, KeychainSplit> SPLITS = new HashMap<>();

	/**
	 * Takes a baked keyblade apart and remembers the halves, or leaves it alone if the cut found nothing to
	 * swing. Returns whether the keyblade should be drawn by this renderer at all.
	 */
	public static boolean install(ResourceLocation keyblade, BakedModel model) {
		Float cut = CUTS.get(keyblade);

		if (cut == null) {
			return false;
		}

		KeychainSplit split = KeychainSplit.of(model, cut);

		if (split == null) {
			KingdomKeys.LOGGER.warn("The model for {} could not be split at {}, so its keychain will not swing", keyblade, cut);
			return false;
		}

		SPLITS.put(keyblade, split);

		return true;
	}

	/** Where the chain points in the model when nothing is pulling on it: straight down the hilt */
	private static final Vector3f REST = new Vector3f(0.0F, -1.0F, 0.0F);

	/** How hard the holder's movement drags the chain off vertical. One block per tick would lay it flat at 3 */
	private static final float TRAIL = 3.0F;
	/** Idle wobble, as a sideways fraction of straight down, so a still chain is not perfectly rigid */
	private static final float SWAY = 0.05F;
	private static final float PERIOD_TICKS = 27.0F;

	static {
		CUTS.put(KingdomKeys.rl(Strings.bondOfFlame), 0.3149F);
		CUTS.put(KingdomKeys.rl(Strings.bondOfTheBlaze), 0.2244F);
		CUTS.put(KingdomKeys.rl(Strings.braveheart), 0.2268F);
		CUTS.put(KingdomKeys.rl(Strings.dawnTillDusk), 0.2941F);
		CUTS.put(KingdomKeys.rl(Strings.deadOfNight), 0.2787F);
		CUTS.put(KingdomKeys.rl(Strings.destinysEmbrace), 0.3639F);
		CUTS.put(KingdomKeys.rl(Strings.earthshaker), 0.2578F);
		CUTS.put(KingdomKeys.rl(Strings.endsOfTheEarth), 0.2437F);
		CUTS.put(KingdomKeys.rl(Strings.fenrir), 0.2349F);
		CUTS.put(KingdomKeys.rl(Strings.frolicFlame), 0.2955F);
		CUTS.put(KingdomKeys.rl(Strings.grandChef), 0.2107F);
		CUTS.put(KingdomKeys.rl(Strings.hiddenDragon), 0.3141F);
		CUTS.put(KingdomKeys.rl(Strings.incompleteKiblade), 0.1575F);
		CUTS.put(KingdomKeys.rl(Strings.kiblade), 0.1575F);
		CUTS.put(KingdomKeys.rl(Strings.kingdomKey), 0.2870F);
		CUTS.put(KingdomKeys.rl(Strings.kingdomKeyD), 0.2870F);
		CUTS.put(KingdomKeys.rl(Strings.kingdomKeyN), 0.2870F);
		CUTS.put(KingdomKeys.rl(Strings.longNight), 0.2450F);//
		CUTS.put(KingdomKeys.rl(Strings.lostMemory), 0.2490F);
		CUTS.put(KingdomKeys.rl(Strings.mastersDefender), 0.2429F);
		CUTS.put(KingdomKeys.rl(Strings.midnightBlue), 0.2420F);
		CUTS.put(KingdomKeys.rl(Strings.mirageSplit), 0.2600F);
		CUTS.put(KingdomKeys.rl(Strings.nightmaresEndAndMirageSplit), 0.1526F);
		CUTS.put(KingdomKeys.rl(Strings.noName), 0.1945F);
		CUTS.put(KingdomKeys.rl(Strings.noNameBBS), 0.2198F);
		CUTS.put(KingdomKeys.rl(Strings.oathkeeper), 0.3052F);
		CUTS.put(KingdomKeys.rl(Strings.oblivion), 0.2846F);
		CUTS.put(KingdomKeys.rl(Strings.phantomGreen), 0.2421F);
		CUTS.put(KingdomKeys.rl(Strings.rainfell), 0.2946F);
		CUTS.put(KingdomKeys.rl(Strings.retribution), 0.2927F);
		CUTS.put(KingdomKeys.rl(Strings.starCluster), 0.2787F);
		CUTS.put(KingdomKeys.rl(Strings.starSeeker), 0.2450F);
		CUTS.put(KingdomKeys.rl(Strings.stormfall), 0.2765F);
		CUTS.put(KingdomKeys.rl(Strings.twoBecomeOne), 0.2603F);
		CUTS.put(KingdomKeys.rl(Strings.ultimaWeaponBBS), 0.1781F);
		CUTS.put(KingdomKeys.rl(Strings.ultimaWeaponDDD), 0.2381F);
		CUTS.put(KingdomKeys.rl(Strings.ultimaWeaponKH1), 0.2839F);
		CUTS.put(KingdomKeys.rl(Strings.ultimaWeaponKH2), 0.2533F);
		CUTS.put(KingdomKeys.rl(Strings.ultimaWeaponKH3), 0.1614F);
		CUTS.put(KingdomKeys.rl(Strings.voidGear), 0.2172F);
		CUTS.put(KingdomKeys.rl(Strings.voidGearRemnant), 0.2172F);
		CUTS.put(KingdomKeys.rl(Strings.wayToTheDawn), 0.2543F);
		CUTS.put(KingdomKeys.rl(Strings.waywardWind), 0.3229F);
		CUTS.put(KingdomKeys.rl(Strings.youngXehanortsKeyblade), 0.1858F);
	}

	public KeychainRenderer() {
		super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
	}

	@Override
	public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
		Minecraft minecraft = Minecraft.getInstance();
		ResourceLocation keyblade = BuiltInRegistries.ITEM.getKey(stack.getItem());

		KeychainSplit split = SPLITS.get(keyblade);

		if (split == null) {
			KingdomKeys.LOGGER.warn("{} reached the keychain renderer without a split, so it cannot be drawn", keyblade);
			return;
		}

		BakedModel blade = split.blade;
		BakedModel keychain = split.keychain;

		ItemRenderer itemRenderer = minecraft.getItemRenderer();
		Vector3f hinge = split.hinge;

		// The blade keeps the render type the item would have had anyway
		VertexConsumer bladeConsumer = ItemRenderer.getFoilBufferDirect(buffer, ItemBlockRenderTypes.getRenderType(stack, true), true, stack.hasFoil());
		itemRenderer.renderModelLists(blade, stack, packedLight, packedOverlay, poseStack, bladeConsumer);

		VertexConsumer keychainConsumer = ItemRenderer.getFoilBufferDirect(buffer, ItemBlockRenderTypes.getRenderType(stack, true), true, stack.hasFoil());

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

		// Straight down in world space is not straight down in the model. The inverse of the pose, not of its
		// normal matrix: normal() is the inverse transpose, which only agrees with the inverse when the matrix
		// is orthonormal, and these display transforms scale unevenly.
		new Matrix3f(poseStack.last().pose()).invert().transform(target);
		target.normalize();

		return new Quaternionf().rotateTo(REST.x(), REST.y(), REST.z(), target.x(), target.y(), target.z());
	}

	/** Drops the split halves so a resource reload takes them apart again from the new models */
	public static void clearCache() {
		SPLITS.clear();
	}
}

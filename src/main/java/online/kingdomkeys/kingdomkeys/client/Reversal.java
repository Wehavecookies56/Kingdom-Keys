package online.kingdomkeys.kingdomkeys.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class Reversal {

	private Reversal() {}

	//Anim length
	public static final int TICKS = 9;

	// Distance from the dusk's back at the end
	private static final double BEHIND = 1.6;

	private static final double MIN_RADIUS = 1.4;
	private static final double MAX_RADIUS = 3.2;

	private static Entity dusk;
	private static float start;
	private static float sweep;
	private static double radius;
	private static double height;
	private static int at;

	public static void begin(Entity target) {
		LocalPlayer player = Minecraft.getInstance().player;

		if (player == null || target == null) {
			return;
		}

		Vec3 from = player.position();
		Vec3 centre = target.position();

		dusk = target;
		radius = Mth.clamp(from.subtract(centre).horizontalDistance(), MIN_RADIUS, MAX_RADIUS);
		start = (float) Mth.atan2(from.z - centre.z, from.x - centre.x);

		// A mob at yaw 0 faces +Z, and this angle is measured atan2(z, x), so its back sits 90 degrees round
		float end = (target.getYRot() - 90F) * Mth.DEG_TO_RAD;
		sweep = Mth.wrapDegrees((end - start) * Mth.RAD_TO_DEG) * Mth.DEG_TO_RAD;

		height = from.y;
		at = 0;
	}

	public static boolean running() {
		return dusk != null;
	}

	public static void tick() {
		if (dusk == null) {
			return;
		}

		LocalPlayer player = Minecraft.getInstance().player;

		if (player == null || !dusk.isAlive() || at >= TICKS) {
			dusk = null;
			return;
		}

		at++;

		float progress = (float) at / TICKS;
		// Easing in and out, so it pushes off and settles rather than sliding at one speed
		float eased = progress * progress * (3 - 2 * progress);

		Vec3 centre = dusk.position();
		float angle = start + sweep * eased;

		// The radius closes as it goes, so the arc tightens onto its back instead of stopping wide of it
		double r = Mth.lerp(eased, radius, BEHIND);
		double x = centre.x + Math.cos(angle) * r;
		double z = centre.z + Math.sin(angle) * r;

		if (!player.level().noCollision(player, player.getBoundingBox().move(x - player.getX(), height - player.getY(), z - player.getZ()))) {
			dusk = null; // ran into something, better to stop short than to end up inside a wall
			return;
		}

		Vec3 eye = dusk.getEyePosition();
		double dx = eye.x - x;
		double dz = eye.z - z;

		double wasX = player.getX();
		double wasY = player.getY();
		double wasZ = player.getZ();

		player.setPos(x, height, z);
		player.xo = wasX;
		player.yo = wasY;
		player.zo = wasZ;
		player.xOld = wasX;
		player.yOld = wasY;
		player.zOld = wasZ;

		player.setDeltaMovement(Vec3.ZERO);
		player.fallDistance = 0;

		// Facing it the whole way round, which is what makes this read as circling rather than as being dragged
		player.setYRot((float) (Mth.atan2(dz, dx) * Mth.RAD_TO_DEG) - 90F);
		player.setXRot((float) -(Mth.atan2(eye.y - (height + player.getEyeHeight()), Math.sqrt(dx * dx + dz * dz)) * Mth.RAD_TO_DEG));

		player.level().addParticle(ParticleTypes.CLOUD, x, height + 0.1, z, 0, 0, 0);
	}
}

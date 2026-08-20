package online.kingdomkeys.kingdomkeys.client.render.magic;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.TrailRenderer;
import online.kingdomkeys.kingdomkeys.entity.magic.AeroTornadoEntity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class AeroTornadoRenderer extends EntityRenderer<AeroTornadoEntity> {
	private static final int LENGTH = 40;
	private static final float WIDTH = 0.09F;

	private static final float[][] COLORS = {
			{0.85F, 1.00F, 0.98F},
			{0.45F, 1.00F, 0.90F},
			{0.20F, 0.90F, 0.80F},
			{0.10F, 0.70F, 0.70F}
	};

	private final Map<Integer, Ribbon> ribbons = new HashMap<>();
	private int forEntity = -1;
	private long lastTick = Long.MIN_VALUE;

	public AeroTornadoRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public void render(AeroTornadoEntity tornado, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
		Minecraft mc = Minecraft.getInstance();

		if (mc.level == null) {
			return;
		}

		if (forEntity != tornado.getId()) {
			forEntity = tornado.getId();
			lastTick = Long.MIN_VALUE;
			ribbons.clear();
		}

		long now = mc.level.getGameTime();

		if (now != lastTick) {
			lastTick = now;
			follow(tornado);
		}

		if (ribbons.isEmpty()) {
			return;
		}

		VertexConsumer consumer = bufferSource.getBuffer(RenderType.debugQuads());

		Vec3 origin = tornado.position();

		for (Ribbon ribbon : ribbons.values()) {
			TrailRenderer.render(ribbon.trail.interpolated(partialTicks), origin, poseStack.last().pose(), consumer, COLORS, WIDTH);
		}
	}

	private void follow(AeroTornadoEntity tornado) {
		List<Entity> caught = caught(tornado);

		for (Entity entity : caught) {
			Ribbon ribbon = ribbons.computeIfAbsent(entity.getId(), id -> new Ribbon());

			ribbon.last = entity.position().add(0, entity.getBbHeight() * 0.5, 0);
			ribbon.settling = -1;
			ribbon.trail.pushHead(ribbon.last);
		}

		Iterator<Map.Entry<Integer, Ribbon>> each = ribbons.entrySet().iterator();

		while (each.hasNext()) {
			Map.Entry<Integer, Ribbon> entry = each.next();

			if (entry.getValue().settling < 0 && held(caught, entry.getKey())) {
				continue;
			}

			Ribbon ribbon = entry.getValue();
			ribbon.settling++;

			// The same point over and over: the head stays put while the tail is pulled into it
			ribbon.trail.pushHead(ribbon.last);

			if (ribbon.settling >= LENGTH) {
				each.remove();
			}
		}
	}

	private static boolean held(List<Entity> caught, int id) {
		for (Entity entity : caught) {
			if (entity.getId() == id) {
				return true;
			}
		}

		return false;
	}

	private static List<Entity> caught(AeroTornadoEntity tornado) {
		float reach = tornado.getReach();
		float height = tornado.getHeight();

		return tornado.level().getEntitiesOfClass(LivingEntity.class, tornado.getBoundingBox().inflate(reach, height, reach), living -> {
			if (living instanceof Player || !living.isAlive()) {
				return false;
			}

			double dx = living.getX() - tornado.getX();
			double dz = living.getZ() - tornado.getZ();

			// Round like the column, not square like the box it was found in
			return dx * dx + dz * dz <= reach * reach && living.getY() >= tornado.getY() - 1 && living.getY() <= tornado.getY() + height;
		}).stream().map(living -> (Entity) living).toList();
	}

	@Override
	public boolean shouldRender(AeroTornadoEntity entity, Frustum frustum, double x, double y, double z) {
		return true;
	}

	@Override
	public ResourceLocation getTextureLocation(AeroTornadoEntity entity) {
		return KingdomKeys.rl("textures/entity/aero_tornado.png");
	}

	// Per enemy ribbon
	private static final class Ribbon {

		private final TrailRenderer.Trail trail = new TrailRenderer.Trail(LENGTH);

		//Where its head was last put, which is where it stays once nobody is carrying it
		private Vec3 last = Vec3.ZERO;

		//Ticks since it was let go, or -1 while its owner is still in the wind
		private int settling = -1;
	}
}

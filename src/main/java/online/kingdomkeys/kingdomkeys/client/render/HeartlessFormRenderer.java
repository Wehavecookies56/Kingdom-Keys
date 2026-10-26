package online.kingdomkeys.kingdomkeys.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderHandEvent;
import net.neoforged.neoforge.client.event.RenderPlayerEvent;
import net.neoforged.neoforge.client.model.data.ModelData;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.entity.mob.ShadowEntity;
import online.kingdomkeys.kingdomkeys.world.HeartlessForm;
import org.joml.Vector3f;

import java.util.Map;
import java.util.WeakHashMap;

// A player turned Heartless is drawn as a Shadow: a stand-in Shadow per player, posed and animated to match them
@EventBusSubscriber(value = Dist.CLIENT)
public class HeartlessFormRenderer {
	private static final Map<Player, ShadowEntity> STAND_INS = new WeakHashMap<>();

	private static final Map<Player, Integer> FALLING = new WeakHashMap<>();
	private static final int HEART_LANDS = 14, HEART_GONE = 26, SHADOW_UP = 50;
	private static final float HEART_FROM = 1.1F, HEART_REST = 0.15F, HEART_SCALE = 0.004F;
	private static final DustParticleOptions DARKNESS = new DustParticleOptions(new Vector3f(0.12F, 0.02F, 0.18F), 1.3F);
	private static final ModelResourceLocation HEART = ModelResourceLocation.standalone(KingdomKeys.rl("entity/heart"));
	private static final RandomSource RANDOM = RandomSource.create();

	public static void fall(Player player) {
		FALLING.put(player, 0);
	}

	private static ShadowEntity standIn(Player player) {
		return STAND_INS.computeIfAbsent(player, p -> {
			ShadowEntity shadow = new ShadowEntity(ModEntities.TYPE_SHADOW.get(), p.level());
			shadow.setNoAi(true);
			return shadow;
		});
	}

	@SubscribeEvent
	public static void renderPlayer(RenderPlayerEvent.Pre event) {
		Player player = event.getEntity();
		if (!HeartlessForm.isHeartless(player)) {
			return;
		}

		event.setCanceled(true);

		float partial = event.getPartialTick();
		Integer falling = FALLING.get(player);
		if (falling != null) {
			float ticks = falling + partial;
			renderHeart(event.getPoseStack(), event.getMultiBufferSource(), ticks);
			if (ticks < HEART_GONE - 4) {
				return;
			}
		}

		ShadowEntity shadow = standIn(player);

		// Placed where the player is: the name tag and lighting go by the entity's own position
		shadow.setPos(player.getX(), player.getY(), player.getZ());
		shadow.xo = player.xo;
		shadow.yo = player.yo;
		shadow.zo = player.zo;
		shadow.yBodyRot = player.yBodyRot;
		shadow.yBodyRotO = player.yBodyRotO;
		shadow.yHeadRot = player.yHeadRot;
		shadow.yHeadRotO = player.yHeadRotO;
		shadow.setYRot(player.getYRot());
		shadow.yRotO = player.yRotO;
		shadow.setXRot(player.getXRot());
		shadow.xRotO = player.xRotO;
		shadow.hurtTime = player.hurtTime;
		// The player's swing drives the Shadow's claw swipe
		shadow.attackAnim = player.attackAnim;
		shadow.oAttackAnim = player.oAttackAnim;
		shadow.swinging = player.swinging;
		shadow.hurtDuration = player.hurtDuration;
		shadow.tickCount = player.tickCount;

		shadow.setCustomName(null);
		shadow.setCustomNameVisible(false);

		PoseStack poseStack = event.getPoseStack();
		poseStack.pushPose();
		{
			// The player renderer has already dropped a crouching player a little; a Shadow sinks on its own, so undo it
			if (player instanceof AbstractClientPlayer clientPlayer) {
				Vec3 crouch = event.getRenderer().getRenderOffset(clientPlayer, partial);
				poseStack.translate(-crouch.x, -crouch.y, -crouch.z);
			}
			Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(shadow).render(shadow, player.getViewYRot(partial), partial, poseStack, event.getMultiBufferSource(), event.getPackedLight());
		}
		poseStack.popPose();
	}

	// Walk cycle and the sink into the ground, advanced once a tick like a real Shadow's
	@SubscribeEvent
	public static void tick(ClientTickEvent.Post event) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.level == null) {
			STAND_INS.clear();
			return;
		}

		guide(mc);

		for (Player player : mc.level.players()) {
			if (!HeartlessForm.isHeartless(player)) {
				FALLING.remove(player);
				STAND_INS.remove(player);
				continue;
			}

			ShadowEntity shadow = standIn(player);
			double dx = player.getX() - player.xo, dz = player.getZ() - player.zo;
			shadow.walkAnimation.update(Math.min((float) Math.sqrt(dx * dx + dz * dz) * 4F, 1F), 0.4F);

			shadow.prevShadowAnim = shadow.shadowAnim;
			Integer falling = FALLING.get(player);
			if (falling != null) {
				fallingDarkness(mc, player, falling);
				if (falling + 1 >= SHADOW_UP) {
					FALLING.remove(player);
				} else {
					FALLING.put(player, falling + 1);
				}
				float out = Mth.clamp((falling + 1 - (HEART_GONE - 4)) / (float) (SHADOW_UP - HEART_GONE + 4), 0F, 1F);
				shadow.shadowAnim = 1F - out * out * (3F - 2F * out);
				continue;
			}
			float target = HeartlessForm.isSunk(player) ? 1F : 0F;
			shadow.shadowAnim += (target - shadow.shadowAnim) * 0.5F;
		}
	}

	private static float heartHeight(float ticks) {
		if (ticks < HEART_LANDS) {
			float fall = ticks / HEART_LANDS;
			return HEART_FROM + (HEART_REST - HEART_FROM) * fall * fall;
		}
		return HEART_REST * (1F - swallowed(ticks));
	}

	private static float swallowed(float ticks) {
		return Mth.clamp((ticks - HEART_LANDS) / (HEART_GONE - HEART_LANDS), 0F, 1F);
	}

	private static void renderHeart(PoseStack pose, MultiBufferSource buffers, float ticks) {
		float swallowed = swallowed(ticks);
		if (swallowed >= 1F) {
			return;
		}

		BakedModel model = Minecraft.getInstance().getModelManager().getModel(HEART);
		VertexConsumer buffer = buffers.getBuffer(Sheets.translucentCullBlockSheet());
		float scale = HEART_SCALE * (1F - swallowed);
		float light = 1F - swallowed * 0.7F;

		pose.pushPose();
		{
			pose.translate(0D, heartHeight(ticks), 0D);
			pose.scale(scale, scale, scale);
			pose.mulPose(Axis.YP.rotationDegrees(ticks * 20F));
			for (BakedQuad quad : model.getQuads(null, null, RANDOM, ModelData.EMPTY, RenderType.cutout())) {
				buffer.putBulkData(pose.last(), quad, light, light, light, 1F, 0x00F000F0, OverlayTexture.NO_OVERLAY, true);
			}
		}
		pose.popPose();
	}

	private static void fallingDarkness(Minecraft mc, Player player, int ticks) {
		RandomSource random = player.getRandom();
		double x = player.getX(), y = player.getY(), z = player.getZ();
		if (ticks < HEART_LANDS) {
			double heart = y + heartHeight(ticks);
			for (int i = 0; i < 3; i++) {
				mc.level.addParticle(DARKNESS, x + (random.nextDouble() - 0.5D) * 0.25D, heart + random.nextDouble() * 0.2D, z + (random.nextDouble() - 0.5D) * 0.25D, 0D, 0D, 0D);
			}
			mc.level.addParticle(ParticleTypes.SMOKE, x, heart + 0.1D, z, 0D, 0.02D, 0D);
		} else if (ticks == HEART_LANDS) {
			for (int i = 0; i < 16; i++) {
				double angle = i * Math.PI * 2D / 16D;
				mc.level.addParticle(ParticleTypes.SQUID_INK, x, y + 0.1D, z, Math.cos(angle) * 0.12D, 0D, Math.sin(angle) * 0.12D);
			}
		} else if (ticks < HEART_GONE) {
			double angle = random.nextDouble() * Math.PI * 2D, reach = 0.6D + random.nextDouble() * 0.4D;
			double px = x + Math.cos(angle) * reach, pz = z + Math.sin(angle) * reach;
			mc.level.addParticle(DARKNESS, px, y + 0.05D, pz, 0D, 0D, 0D);
			mc.level.addParticle(ParticleTypes.SMOKE, px, y + 0.05D, pz, -Math.cos(angle) * 0.08D, 0.01D, -Math.sin(angle) * 0.08D);
		} else {
			mc.level.addParticle(ParticleTypes.LARGE_SMOKE, x + (random.nextDouble() - 0.5D) * 0.6D, y + 0.1D, z + (random.nextDouble() - 0.5D) * 0.6D, 0D, 0.03D, 0D);
		}
	}

	private static final int GUIDE_EVERY = 20;
	private static final double GUIDE_FROM = 1.5D, GUIDE_REACH = 32D, GUIDE_QUIET = 3D, GUIDE_DRAG = 0.04D;

	// A faint run of motes drifting off from you towards your light, so you always know which way it lies. Only you see yours
	private static void guide(Minecraft mc) {
		Player player = mc.player;
		if (player == null || !HeartlessForm.isHeartless(player) || player.tickCount % GUIDE_EVERY != 0) {
			return;
		}

		PlayerData data = PlayerData.get(player);
		if (data.getLightPos() == null || !player.level().dimension().equals(data.getLightDimension())) {
			return;
		}

		Vec3 from = player.position().add(0D, player.getBbHeight() * 0.6D, 0D);
		Vec3 toLight = Vec3.atBottomCenterOf(data.getLightPos()).add(0D, 1D, 0D).subtract(from);
		double distance = toLight.length();
		if (distance < GUIDE_QUIET) {
			return;
		}

		Vec3 way = toLight.scale(1D / distance);
		Vec3 at = from.add(way.scale(GUIDE_FROM));
		Vec3 speed = way.scale(Math.min(GUIDE_REACH, distance - GUIDE_FROM) * GUIDE_DRAG);
		mc.level.addParticle(ParticleTypes.END_ROD, at.x, at.y, at.z, speed.x, speed.y, speed.z);
	}

	// No hands to see in first person: a Shadow has claws, and nothing in them
	@SubscribeEvent
	public static void renderHand(RenderHandEvent event) {
		if (HeartlessForm.isHeartless(Minecraft.getInstance().player)) {
			event.setCanceled(true);
		}
	}
}

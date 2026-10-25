package online.kingdomkeys.kingdomkeys.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderHandEvent;
import net.neoforged.neoforge.client.event.RenderPlayerEvent;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.entity.mob.ShadowEntity;
import online.kingdomkeys.kingdomkeys.world.HeartlessForm;

import java.util.Map;
import java.util.WeakHashMap;

// A player turned Heartless is drawn as a Shadow: a stand-in Shadow per player, posed and animated to match them
@EventBusSubscriber(value = Dist.CLIENT)
public class HeartlessFormRenderer {
	private static final Map<Player, ShadowEntity> STAND_INS = new WeakHashMap<>();

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

		ShadowEntity shadow = standIn(player);
		float partial = event.getPartialTick();

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
				STAND_INS.remove(player);
				continue;
			}

			ShadowEntity shadow = standIn(player);
			double dx = player.getX() - player.xo, dz = player.getZ() - player.zo;
			shadow.walkAnimation.update(Math.min((float) Math.sqrt(dx * dx + dz * dz) * 4F, 1F), 0.4F);

			shadow.prevShadowAnim = shadow.shadowAnim;
			float target = HeartlessForm.isSunk(player) ? 1F : 0F;
			shadow.shadowAnim += (target - shadow.shadowAnim) * 0.5F;
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

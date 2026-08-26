package online.kingdomkeys.kingdomkeys.client.render.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.phys.Vec3;
import online.kingdomkeys.kingdomkeys.client.TrailRenderer;
import online.kingdomkeys.kingdomkeys.entity.block.OrgPortalTileEntity;
import org.joml.Matrix4f;

// The same orbiting ribbons the save point uses, in the portal's dark purple. Replaces the pair of
// dust particles the tile entity's tick used to spit out, which read as two dots rather than a swirl.
public class OrgPortalBlockEntityRenderer implements BlockEntityRenderer<OrgPortalTileEntity> {

	// Darkest at the tail so the ribbon fades into the block rather than ending abruptly.
	private static final float[][] COLORS = {{0.55F, 0.20F, 0.80F}, {0.45F, 0.10F, 0.70F}, {0.35F, 0.05F, 0.60F}, {0.20F, 0.00F, 0.40F}};
	private static final float WIDTH = 0.05F;

	public OrgPortalBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
	}

	@Override
	public void render(OrgPortalTileEntity be, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
		Minecraft mc = Minecraft.getInstance();

		if (mc.level == null)
			return;

		Vec3 center = Vec3.atCenterOf(be.getBlockPos()).add(0.5, 0.15, 0.5);
		Vec3 origin = Vec3.atCenterOf(be.getBlockPos());

		//Init
		if (be.particles[0] == null) {
			for (int i = 0; i < be.particles.length; i++) {
				SavePointBlockEntityRenderer.SavePointParticle p = new SavePointBlockEntityRenderer.SavePointParticle();

				p.angle = i * Math.PI;
				p.progress = i * Math.PI;

				p.radius = 0.8;
				// Wound the other way from the save point's, so a portal reads as the darker twin.
				p.rotationSpeed = -0.1;
				p.verticalSpeed = 0.05;

				be.particles[i] = p;
			}
		}

		long gameTime = mc.level.getGameTime();

		//Update
		if (gameTime != be.lastUpdateTick) {
			be.lastUpdateTick = gameTime;
			for (SavePointBlockEntityRenderer.SavePointParticle p : be.particles) {
				p.angle += p.rotationSpeed;
				p.progress += p.verticalSpeed;

				Vec3 head = center.add(Math.cos(p.angle) * p.radius, (Math.sin(p.progress) + 1.0) * 0.35, Math.sin(p.angle) * p.radius);

				p.trail.pushHead(head);
			}
		}

		VertexConsumer consumer = bufferSource.getBuffer(RenderType.debugQuads());
		Matrix4f pose = poseStack.last().pose();

		for (SavePointBlockEntityRenderer.SavePointParticle p : be.particles) {
			Vec3[] trail = p.trail.points.clone();

			if (trail[1] != null) {
				double renderAngle = p.angle + partialTicks * p.rotationSpeed;
				double renderProgress = p.progress + partialTicks * p.verticalSpeed;

				trail[0] = center.add(Math.cos(renderAngle) * p.radius, (Math.sin(renderProgress) + 1.0) * 0.5, Math.sin(renderAngle) * p.radius);
			}

			TrailRenderer.render(trail, origin, pose, consumer, COLORS, WIDTH);
		}
	}
}

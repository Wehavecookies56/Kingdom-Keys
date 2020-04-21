package online.kingdomkeys.kingdomkeys.client.render;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.model.ModelValor;

public class RenderValor extends EntityRenderer<LivingEntity> implements IKKRender {

	float scale;
	private ModelValor model;

	public RenderValor(EntityRendererManager renderManager, ModelValor model, float scale) {
		super(renderManager);
		this.model = model;
		this.scale = scale;
	}

	@Override
	public void doRender(LivingEntity entityLiving, float v, MatrixStack matrixStackIn, IRenderTypeBuffer iRenderTypeBuffer, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
		RenderSystem.pushMatrix();
		{
			matrixStackIn.push();

			float ageInTicks = entityLiving.ticksExisted + v;

			float headYawOffset = this.interpolateRotation(entityLiving.prevRenderYawOffset, entityLiving.renderYawOffset, v);
			float headYaw = this.interpolateRotation(entityLiving.prevRotationYawHead, entityLiving.rotationYawHead, v);
			float headPitch = entityLiving.prevRotationPitch + (entityLiving.rotationPitch - entityLiving.prevRotationPitch) * v;

			this.rotateCorpse(entityLiving, ageInTicks, headYawOffset, v);

			float limbSwingAmount = entityLiving.prevLimbSwingAmount + (entityLiving.limbSwingAmount - entityLiving.prevLimbSwingAmount) * v;
			float limbSwing = entityLiving.limbSwing - entityLiving.limbSwingAmount * (1.0F - v);

			this.model.setRotationAngles(entityLiving, limbSwing, limbSwingAmount, ageInTicks, headYaw - headYawOffset, headPitch);
			this.model.render(matrixStackIn, iRenderTypeBuffer.getBuffer(model.getRenderType(getEntityTexture(entityLiving))), packedLightIn, packedOverlayIn, red, green, blue, alpha);
			matrixStackIn.pop();

		}
		RenderSystem.popMatrix();

	}

	private float interpolateRotation(float lowerLimit, float upperLimit, float range) {
		float f3;

		for (f3 = upperLimit - lowerLimit; f3 < -180.0F; f3 += 360.0F) {
			;
		}

		while (f3 >= 180.0F) {
			f3 -= 360.0F;
		}

		return lowerLimit + range * f3;
	}

	protected void rotateCorpse(LivingEntity entityLiving, float ageInTicks, float headYawOffset, float v) {
		GL11.glRotatef(180.0F + headYawOffset, 0.0F, 1.0F, 0.0F);

		if (entityLiving.deathTime > 0) {
			float f3 = ((float) entityLiving.deathTime + v - 1.0F) / 20.0F * 1.6F;
			f3 = MathHelper.sqrt(f3);

			if (f3 > 1.0F) {
				f3 = 1.0F;
			}
		}
	}

	@Override
	public void renderFirstPersonArm(PlayerEntity player, MatrixStack matrixStackIn, IRenderTypeBuffer buffer, int packedLightIn) {
		Minecraft mc = Minecraft.getInstance();
		//RenderHelper.enableStandardItemLighting();
		matrixStackIn.push();
		{
			//matrixStackIn.translate(1, -1, -0.6);
			//matrixStackIn.rotate(Vector3f.XN.rotationDegrees(20));
			//matrixStackIn.rotate(Vector3f.YP.rotationDegrees(150));
			//matrixStackIn.rotate(Vector3f.ZN.rotationDegrees(50));

			if (mc.gameSettings.thirdPersonView == 0 && !mc.gameSettings.hideGUI && !player.isSleeping()) {
				model.swingProgress = 0.0F;
				model.isSneak = false;
				model.swimAnimation = 0.0F;
				model.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, player);
				model.bipedRightArm.rotateAngleX = 0.0F;				
			}
		}
		matrixStackIn.pop();
	}

	@Override
	public ResourceLocation getEntityTexture(LivingEntity entity) {
		return new ResourceLocation(KingdomKeys.MODID, "textures/entity/forms/" + ModCapabilities.get((PlayerEntity) entity).getDriveForm() + ".png");
	}

}

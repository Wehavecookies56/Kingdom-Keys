package online.kingdomkeys.kingdomkeys.client.model.armor;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;

public class ArmorModel extends HumanoidModel {

	public ArmorModel(float size) {
		super(size, 0, 64, 64);
	}

	@Override
	public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
		super.renderToBuffer(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
	}
	
	@Override
	public void setupAnim(LivingEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		if(entityIn instanceof ArmorStand) {
			super.setupAnim(entityIn, 0, 0, 0, 0, 0);
		} else {
			super.setupAnim(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		}
	}

	public void setRotateAngle(ModelPart modelRenderer, float x, float y, float z) {
		modelRenderer.getXRot() = x;
		modelRenderer.getYRot() = y;
		modelRenderer.zRot = z;
	}

}

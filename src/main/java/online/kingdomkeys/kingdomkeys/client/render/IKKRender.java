package online.kingdomkeys.kingdomkeys.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

public interface IKKRender {
	void doRender(LivingEntity entityLiving, float v, MatrixStack matrixStackIn, IRenderTypeBuffer iRenderTypeBuffer, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha);
	void renderFirstPersonArm(PlayerEntity player, MatrixStack matrixStackIn, IRenderTypeBuffer iRenderTypeBuffer, int packedLightIn);
}

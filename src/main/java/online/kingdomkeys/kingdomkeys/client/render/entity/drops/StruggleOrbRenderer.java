package online.kingdomkeys.kingdomkeys.client.render.entity.drops;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.entity.drops.ItemDropEntity;
import online.kingdomkeys.kingdomkeys.entity.drops.StruggleOrbEntity;
import online.kingdomkeys.kingdomkeys.util.Utils;
import org.joml.Matrix4f;

public class StruggleOrbRenderer extends EntityItemDropRenderer {

	private static final ResourceLocation TEXTURE = KingdomKeys.rl("textures/entity/gummi_fire.png");

	public StruggleOrbRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public ResourceLocation getTextureLocation(ItemDropEntity entity) {
		return TEXTURE;
	}

	@Override
	public void render(ItemDropEntity entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
		int[] rgb = entityIn instanceof StruggleOrbEntity orb ? Utils.getRGBFromDec(orb.getColor()) : new int[]{255, 255, 255};

		matrixStackIn.pushPose();
		{
			float f = 0;
			float f1 = 1F;
			float f2 = 0;
			float f3 = 1F;

			matrixStackIn.translate(0.0D, 0.1F, 0.0D);
			matrixStackIn.mulPose(this.entityRenderDispatcher.cameraOrientation());
			matrixStackIn.mulPose(Axis.YP.rotationDegrees(180.0F));
			matrixStackIn.scale(0.5F, 0.5F, 0.5F);
			VertexConsumer ivertexbuilder = bufferIn.getBuffer(RenderType.entityTranslucent(getTextureLocation(entityIn)));
			PoseStack.Pose matrixstack$entry = matrixStackIn.last();
			Matrix4f matrix4f = matrixstack$entry.pose();
			vertex(ivertexbuilder, matrix4f, -0.5F, -0.25F, rgb[0], rgb[1], rgb[2], f, f3, packedLightIn);
			vertex(ivertexbuilder, matrix4f, 0.5F, -0.25F, rgb[0], rgb[1], rgb[2], f1, f3, packedLightIn);
			vertex(ivertexbuilder, matrix4f, 0.5F, 0.75F, rgb[0], rgb[1], rgb[2], f1, f2, packedLightIn);
			vertex(ivertexbuilder, matrix4f, -0.5F, 0.75F, rgb[0], rgb[1], rgb[2], f, f2, packedLightIn);
		}
		matrixStackIn.popPose();
	}

	private static void vertex(VertexConsumer bufferIn, Matrix4f matrixIn, float x, float y, int red, int green, int blue, float texU, float texV, int packedLight) {
		bufferIn.addVertex(matrixIn, x, y, 0.0F).setColor(red, green, blue, 255).setUv(texU, texV).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(0.0F, 1.0F, 0.0F);
	}
}

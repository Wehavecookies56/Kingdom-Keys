package online.kingdomkeys.kingdomkeys.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.entity.GummiShipEntity;

import java.awt.*;

public class GummiShipEntityRenderer extends EntityRenderer<GummiShipEntity> {

	int red = 96, green = 140, blue = 109, alpha = 255;
	//private GummiShipModel model;

	public GummiShipEntityRenderer(EntityRendererProvider.Context context) {
		super(context);
		//model = new GummiShipModel();
	}

	@Override
	public void render(GummiShipEntity entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
		matrixStackIn.pushPose();
		{
			matrixStackIn.translate(0, 2.5, 0);
			matrixStackIn.scale(0.5F, 0.5F, 0.5F);
			String dataS = entityIn.getData();
			if (dataS.contains(",")) {
				String[] data = dataS.split(","); // "16711680,255,16711680,255,16711680".split(",");//dataS.split(";")
				for (int i = 0; i < data.length; i++) {
					if (!data[i].equals("-1")) {
						Color color = new Color(Integer.parseInt(data[i]));
						//model.parts[i].render(matrixStackIn, bufferIn.getBuffer(model.renderType(getTextureLocation(entityIn))), packedLightIn, OverlayTexture.NO_OVERLAY, color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, 1F);
					}
				}
			}
		}
		matrixStackIn.popPose();
	}

	@Override
	public ResourceLocation getTextureLocation(GummiShipEntity entity) {
		return new ResourceLocation(KingdomKeys.MODID, "textures/entity/models/gummi.png");
	}

}

package online.kingdomkeys.kingdomkeys.client.render.entity;

import java.awt.Color;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.model.GummiShipModel;
import online.kingdomkeys.kingdomkeys.client.model.entity.CubeModel;
import online.kingdomkeys.kingdomkeys.entity.GummiShipEntity;
import online.kingdomkeys.kingdomkeys.entity.SeedBulletEntity;

public class GummiShipEntityRenderer extends EntityRenderer<GummiShipEntity> implements IRenderFactory<GummiShipEntity> {

	int red = 96, green = 140, blue = 109, alpha = 255;
	private GummiShipModel model;

	public GummiShipEntityRenderer(EntityRendererManager renderManager) {
		super(renderManager);
		model = new GummiShipModel();
	}

	@Override
	public void render(GummiShipEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
		matrixStackIn.push();
		{
			matrixStackIn.translate(0, 2.5, 0);
			String dataS = entityIn.getData();
			if (dataS.contains(",")) {
				String[] data = dataS.split(","); // "16711680,255,16711680,255,16711680".split(",");//dataS.split(";")
				for (int i = 0; i < data.length; i++) {
					if (!data[i].equals("-1")) {
						Color color = new Color(Integer.parseInt(data[i]));
						model.parts[i].render(matrixStackIn, bufferIn.getBuffer(model.getRenderType(getEntityTexture(entityIn))), packedLightIn, OverlayTexture.NO_OVERLAY, color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, 1F);
					}
				}
			}
		}
		matrixStackIn.pop();
	}

	@Override
	public ResourceLocation getEntityTexture(GummiShipEntity entity) {
		return new ResourceLocation(KingdomKeys.MODID, "textures/entity/models/gummi.png");
	}

	@Override
	public EntityRenderer<? super GummiShipEntity> createRenderFor(EntityRendererManager manager) {
		return new GummiShipEntityRenderer(manager);
	}
}

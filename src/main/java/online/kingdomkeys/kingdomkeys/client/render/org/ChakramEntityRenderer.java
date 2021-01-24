package online.kingdomkeys.kingdomkeys.client.render.org;

import java.util.Random;

import javax.annotation.Nullable;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.entity.organization.ChakramEntity;

@OnlyIn(Dist.CLIENT)
public class ChakramEntityRenderer extends EntityRenderer<ChakramEntity> {

	public static final Factory FACTORY = new ChakramEntityRenderer.Factory();
	Random rand = new Random();
	float rotation = 0;
	
	public ChakramEntityRenderer(EntityRendererManager renderManager) {
		super(renderManager);
		this.shadowSize = 0.25F;
	}

	@Override
	public void render(ChakramEntity entity, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
		matrixStackIn.push();
		{
			String name = entity.getModel();
			
			IVertexBuilder buffer = bufferIn.getBuffer(Atlases.getTranslucentBlockType());
			IBakedModel model = Minecraft.getInstance().getModelManager().getModel(new ResourceLocation(KingdomKeys.MODID, "item/"+name));

			float scale = 0.03F;

			matrixStackIn.push();
			{
				matrixStackIn.scale(scale, scale, scale);
				matrixStackIn.translate(0, 10, 0);

				float a = 1;// MathHelper.clamp(1 - progress1, 0, 1);
				float rgb = 1;// MathHelper.clamp(progress1, 0, 1);
				
				if(entity.getRotationPoint() == 0) {
					matrixStackIn.rotate(Vector3f.YP.rotationDegrees(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw)));
					matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(90));
					matrixStackIn.rotate(Vector3f.YP.rotationDegrees(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch)));
					matrixStackIn.rotate(Vector3f.XP.rotationDegrees(rotation));
				}
				
				if(entity.getRotationPoint() == 1) {
					
				}
				
				if(entity.getRotationPoint() == 2) {
					matrixStackIn.rotate(Vector3f.YP.rotationDegrees(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw)));
					matrixStackIn.rotate(Vector3f.XP.rotationDegrees(90));
					matrixStackIn.rotate(Vector3f.XN.rotationDegrees(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch)));
					matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(rotation));
				}

				
				rotation+=10;

				if(entity.ticksExisted > 1) {
					for (BakedQuad quad : model.getQuads(null, null, rand, EmptyModelData.INSTANCE)) {
						buffer.addVertexData(matrixStackIn.getLast(), quad, rgb, rgb, rgb, a, 0x00F000F0, OverlayTexture.NO_OVERLAY, true);
					}
				}

			}
			matrixStackIn.pop();


		}
		matrixStackIn.pop();
		super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}

	@Nullable
	@Override
	public ResourceLocation getEntityTexture(ChakramEntity entity) {
		String name = entity.getModel().substring(entity.getModel().indexOf(KingdomKeys.MODID+".")+ KingdomKeys.MODID.length()+1);
		//System.out.println(name);
		return new ResourceLocation(KingdomKeys.MODID, "textures/entity/models/"+name+".png");
	}

	public static class Factory implements IRenderFactory<ChakramEntity> {
		@Override
		public EntityRenderer<? super ChakramEntity> createRenderFor(EntityRendererManager manager) {
			return new ChakramEntityRenderer(manager);
		}
	}
}

package online.kingdomkeys.kingdomkeys.client.render.org;

import java.util.Random;

import javax.annotation.Nullable;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.entity.organization.LanceEntity;

@OnlyIn(Dist.CLIENT)
public class LanceEntityRenderer extends EntityRenderer<LanceEntity> {

	public static final Factory FACTORY = new LanceEntityRenderer.Factory();
	Random rand = new Random();
	
	public LanceEntityRenderer(EntityRendererManager renderManager) {
		super(renderManager);
		this.shadowSize = 0.25F;
	}

	@Override
	public void render(LanceEntity entity, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
		matrixStackIn.push();
		{
			String name = entity.getModel();
			
			IVertexBuilder buffer = bufferIn.getBuffer(Atlases.getTranslucentCullBlockType());
			IBakedModel model = Minecraft.getInstance().getModelManager().getModel(new ResourceLocation(KingdomKeys.MODID, "item/"+name));

			float scale = 0.02F;

			matrixStackIn.push();
			{
				if(entity.ticksExisted > 1) {
					matrixStackIn.scale(scale, scale, scale);
	
					float a = 1;
					float rgb = 1;
					
					matrixStackIn.rotate(Vector3f.YP.rotationDegrees(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw)));
					if(entity.getRotationPoint() == 2)
						matrixStackIn.rotate(Vector3f.XN.rotationDegrees(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) + 270));
					else if(entity.getRotationPoint() == 0) {
						matrixStackIn.rotate(Vector3f.XN.rotationDegrees(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) + 90));
					}
					for (BakedQuad quad : model.getQuads(null, null, rand, EmptyModelData.INSTANCE)) {
						buffer.addVertexData(matrixStackIn.getLast(), quad, rgb, rgb, rgb, a, 0x00F000F0, OverlayTexture.NO_OVERLAY, true);
					}
				}

				matrixStackIn.pop();
			}

		}
		matrixStackIn.pop();
		super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}

	@Nullable
	@Override
	public ResourceLocation getEntityTexture(LanceEntity entity) {
		String name = entity.getModel().substring(entity.getModel().indexOf(KingdomKeys.MODID+".")+ KingdomKeys.MODID.length()+1);
		//System.out.println(name);
		return new ResourceLocation(KingdomKeys.MODID, "textures/entity/models/"+name+".png");
	}

	public static class Factory implements IRenderFactory<LanceEntity> {
		@Override
		public EntityRenderer<? super LanceEntity> createRenderFor(EntityRendererManager manager) {
			return new LanceEntityRenderer(manager);
		}
	}
}

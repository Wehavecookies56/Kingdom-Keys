package online.kingdomkeys.kingdomkeys.client.render.magic;

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
import online.kingdomkeys.kingdomkeys.entity.HeartEntity;

@OnlyIn(Dist.CLIENT)
public class HeartEntityRenderer extends EntityRenderer<HeartEntity> {
	public static final Factory FACTORY = new HeartEntityRenderer.Factory();
	Random rand = new Random();
	float rotation = 0;
	
	public HeartEntityRenderer(EntityRendererManager renderManager) {
		super(renderManager);
		this.shadowSize = 0.25F;
	}

	@Override
	public void render(HeartEntity entity, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
		matrixStackIn.push();
		{
			IVertexBuilder buffer = bufferIn.getBuffer(Atlases.getTranslucentCullBlockType());
			IBakedModel model = Minecraft.getInstance().getModelManager().getModel(new ResourceLocation(KingdomKeys.MODID, "entity/heart"));

			matrixStackIn.push();
			{

				float a = 1;
				float rgb = 1;

				float ticks = entity.ticksExisted;
				if (ticks < 10) // Growing
					matrixStackIn.scale(ticks * 0.0005f, ticks * 0.0005f, ticks * 0.0005f);
				else if (ticks > HeartEntity.MAX_TICKS - 10) // Disappearing
					matrixStackIn.scale((HeartEntity.MAX_TICKS - ticks) * 0.0005f, (HeartEntity.MAX_TICKS - ticks) * 0.0005f, (HeartEntity.MAX_TICKS - ticks) * 0.0005f);
				else // Static size
					matrixStackIn.scale(0.005f, 0.005f, 0.005f);
						        
				matrixStackIn.rotate(Vector3f.YP.rotationDegrees(ticks*20));
				
				for (BakedQuad quad : model.getQuads(null, null, rand, EmptyModelData.INSTANCE)) {
					buffer.addVertexData(matrixStackIn.getLast(), quad, rgb, rgb, rgb, a, 0x00F000F0, OverlayTexture.NO_OVERLAY, true);
				}
				

			}
			matrixStackIn.pop();


		}
		matrixStackIn.pop();
		super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}

	@Nullable
	@Override
	public ResourceLocation getEntityTexture(HeartEntity entity) {
		return new ResourceLocation(KingdomKeys.MODID, "textures/entity/models/heart.png");
	}


	public static class Factory implements IRenderFactory<HeartEntity> {
		@Override
		public EntityRenderer<? super HeartEntity> createRenderFor(EntityRendererManager manager) {
			return new HeartEntityRenderer(manager);
		}
	}
}

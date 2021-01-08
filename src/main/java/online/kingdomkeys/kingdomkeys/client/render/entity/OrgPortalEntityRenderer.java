package online.kingdomkeys.kingdomkeys.client.render.entity;

import java.util.Random;

import javax.annotation.Nullable;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.matrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.IRenderTypeBuffer;
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
import online.kingdomkeys.kingdomkeys.entity.OrgPortalEntity;

@OnlyIn(Dist.CLIENT)
public class OrgPortalEntityRenderer extends EntityRenderer<OrgPortalEntity> {

	public static final Factory FACTORY = new OrgPortalEntityRenderer.Factory();
	Random rand = new Random();
	
	public OrgPortalEntityRenderer(EntityRendererManager renderManager) {
		super(renderManager);
		this.shadowSize = 0.25F;
	}

	@Override
	public void render(OrgPortalEntity entity, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
		matrixStackIn.push();
		{
			IVertexBuilder buffer = bufferIn.getBuffer(Atlases.getTranslucentBlockType());
			IBakedModel model = Minecraft.getInstance().getModelManager().getModel(new ResourceLocation(KingdomKeys.MODID, "entity/portal"));

			matrixStackIn.push();
			{

				float a = 1;
				float rgb = 1;

				float ticks = entity.ticksExisted;
		        if(ticks < 10) //Growing
		        	matrixStackIn.scale(ticks*0.2f, ticks*0.2f, ticks*0.2f);
		        else if(ticks > 90) //Disappearing
		        	matrixStackIn.scale((100-ticks)*0.2f, (100-ticks)*0.2f, (100-ticks)*0.2f);
		        else //Static size
		        	matrixStackIn.scale(2.0f, 2.0f, 2.0f);
		        
				matrixStackIn.rotate(Vector3f.YN.rotationDegrees(Minecraft.getInstance().player.getPitchYaw().y));
				
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
	public ResourceLocation getEntityTexture(OrgPortalEntity entity) {
		return new ResourceLocation(KingdomKeys.MODID, "textures/entity/models/fire.png");
	}

	public static class Factory implements IRenderFactory<OrgPortalEntity> {
		@Override
		public EntityRenderer<? super OrgPortalEntity> createRenderFor(EntityRendererManager manager) {
			return new OrgPortalEntityRenderer(manager);
		}
	}
}

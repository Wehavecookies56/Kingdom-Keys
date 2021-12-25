package online.kingdomkeys.kingdomkeys.client.render.entity;

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
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper.MobType;
import online.kingdomkeys.kingdomkeys.entity.mob.SpawningOrbEntity;

public class SpawningOrbRenderer extends EntityRenderer<SpawningOrbEntity> {

    public static final SpawningOrbRenderer.Factory FACTORY = new SpawningOrbRenderer.Factory();

    public SpawningOrbRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn);
    }

    @Override
	public void render(SpawningOrbEntity entity, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
		matrixStackIn.push();
		{
			IVertexBuilder buffer = bufferIn.getBuffer(Atlases.getTranslucentCullBlockType());
			IBakedModel model = Minecraft.getInstance().getModelManager().getModel(new ResourceLocation(KingdomKeys.MODID, "entity/portal"));

			matrixStackIn.push();
			{

				float[] rgb = entity.getEntityType().equals(MobType.NOBODY.name()) ? new float[] { 0.6F, 0.6F, 0.6F, 0.9F } : new float[] { 0.2F, 0.1F, 0.3F, 0.9F };

				float ticks = entity.ticksExisted;
		        if(ticks < 10) //Growing
		        	matrixStackIn.scale(ticks*0.2f, ticks*0.2f, ticks*0.2f);
		        else if(ticks > 90) //Disappearing
		        	matrixStackIn.scale((100-ticks)*0.2f, (100-ticks)*0.2f, (100-ticks)*0.2f);
		        else //Static size
		        	matrixStackIn.scale(2.0f, 2.0f, 2.0f);
		        
	        	matrixStackIn.scale(1.0f, 0.5f, 1.0f);

				matrixStackIn.rotate(Vector3f.YN.rotationDegrees(Minecraft.getInstance().player.getPitchYaw().y));
				
				for (BakedQuad quad : model.getQuads(null, null, entity.world.rand, EmptyModelData.INSTANCE)) {
					buffer.addVertexData(matrixStackIn.getLast(), quad, rgb[0], rgb[1], rgb[2], rgb[3], 0x00F000F0, OverlayTexture.NO_OVERLAY, true);
				}
				
			}
			matrixStackIn.pop();


		}
		matrixStackIn.pop();
		super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}


    @Override
    public ResourceLocation getEntityTexture(SpawningOrbEntity entity) {
        return new ResourceLocation(KingdomKeys.MODID, "textures/entity/models/portal.png");
    }

    public static class Factory implements IRenderFactory<SpawningOrbEntity> {
        @Override
        public EntityRenderer<? super SpawningOrbEntity> createRenderFor(EntityRendererManager entityRendererManager) {
            return new SpawningOrbRenderer(entityRendererManager);
        }
    }
}

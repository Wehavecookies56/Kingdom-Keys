package online.kingdomkeys.kingdomkeys.client.render.magics;

import javax.annotation.Nullable;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.model.ModelBlizzard;
import online.kingdomkeys.kingdomkeys.entity.magic.EntityGravity;

@OnlyIn(Dist.CLIENT)
public class EntityGravityRenderer extends EntityRenderer<EntityGravity> {

	public static final Factory FACTORY = new EntityGravityRenderer.Factory();
	ModelBlizzard shot;

	public EntityGravityRenderer(EntityRendererManager renderManager, ModelBlizzard fist) {
		super(renderManager);
		this.shot = fist;
		this.shadowSize = 0.25F;
	}

	@Override
	public void render(EntityGravity entity, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
		/*matrixStackIn.push();
		{
			float r = 1, g = 0, b = 0;
				
			matrixStackIn.rotate(Vector3f.YP.rotationDegrees(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw)));
			matrixStackIn.rotate(Vector3f.XN.rotationDegrees(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch)));

			if (entity.ticksExisted > 1) //Prevent entity rendering in your face
				shot.render(matrixStackIn, bufferIn.getBuffer(shot.getRenderType(getEntityTexture(entity))), packedLightIn, OverlayTexture.NO_OVERLAY, r, g, b, 1F);

		}
		matrixStackIn.pop();*/
		super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}

	@Nullable
	@Override
	public ResourceLocation getEntityTexture(EntityGravity entity) {
		return new ResourceLocation(KingdomKeys.MODID, "textures/entity/models/fire.png");
	}

	public static class Factory implements IRenderFactory<EntityGravity> {
		@Override
		public EntityRenderer<? super EntityGravity> createRenderFor(EntityRendererManager manager) {
			return new EntityGravityRenderer(manager, new ModelBlizzard());
		}
	}
}

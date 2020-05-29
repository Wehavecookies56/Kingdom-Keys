package online.kingdomkeys.kingdomkeys.client.render.entity;

import javax.annotation.Nullable;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.model.ModelBlizzard;
import online.kingdomkeys.kingdomkeys.entity.OrgPortalEntity;

@OnlyIn(Dist.CLIENT)
public class OrgPortalEntityRenderer extends EntityRenderer<OrgPortalEntity> {

	public static final Factory FACTORY = new OrgPortalEntityRenderer.Factory();
	ModelBlizzard portal;

	public OrgPortalEntityRenderer(EntityRendererManager renderManager, ModelBlizzard fist) {
		super(renderManager);
		this.portal = fist;
		this.shadowSize = 0.25F;
	}

	@Override
	public void render(OrgPortalEntity entity, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
		matrixStackIn.push();
		{
			float r = 1, g = 0, b = 0;
				
			matrixStackIn.rotate(Vector3f.YP.rotationDegrees(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw)));
			matrixStackIn.rotate(Vector3f.XN.rotationDegrees(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch)));

			if (entity.ticksExisted > 1) //Prevent entity rendering in your face
				portal.render(matrixStackIn, bufferIn.getBuffer(portal.getRenderType(getEntityTexture(entity))), packedLightIn, OverlayTexture.NO_OVERLAY, r, g, b, 1F);

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
			return new OrgPortalEntityRenderer(manager, new ModelBlizzard());
		}
	}
}

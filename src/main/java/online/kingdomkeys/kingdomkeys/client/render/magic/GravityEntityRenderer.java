package online.kingdomkeys.kingdomkeys.client.render.magic;

import javax.annotation.Nullable;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.IGlobalCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.model.BlizzardModel;
import online.kingdomkeys.kingdomkeys.entity.magic.GravityEntity;

@OnlyIn(Dist.CLIENT)
public class GravityEntityRenderer extends EntityRenderer<GravityEntity> {

	BlizzardModel shot;

	public GravityEntityRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.shot = new BlizzardModel(context.bakeLayer(BlizzardModel.LAYER_LOCATION));
		this.shadowRadius = 0.25F;
	}

	@Override
	public void render(GravityEntity entity, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
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
	public ResourceLocation getTextureLocation(GravityEntity entity) {
		return new ResourceLocation(KingdomKeys.MODID, "textures/entity/models/fire.png");
	}

	@Mod.EventBusSubscriber(value = Dist.CLIENT)
	public static class Events {
		@SubscribeEvent
		public static void RenderEntity(RenderLivingEvent.Pre event) {
			IGlobalCapabilities globalData = ModCapabilities.getGlobal(event.getEntity());
			if (globalData != null) {
				if (globalData.getFlatTicks() > 0) {// || event.getEntity().getScoreboardName().equals(new String(Base64.getDecoder().decode("c3RlbDEwMzQ=")))) {
					PoseStack mat = event.getPoseStack();
					mat.scale(1.5F, 0.01F, 1.5F);
				}
			}
		}
	}
}

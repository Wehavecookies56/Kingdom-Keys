package online.kingdomkeys.kingdomkeys.client.render.magic;

import javax.annotation.Nullable;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLivingEvent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.data.ModData;
import online.kingdomkeys.kingdomkeys.entity.magic.GravityEntity;

@OnlyIn(Dist.CLIENT)
public class GravityEntityRenderer extends EntityRenderer<GravityEntity> {

	public GravityEntityRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.shadowRadius = 0.25F;
	}

	@Override
	public void render(GravityEntity entity, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
		super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}

	@Nullable
	@Override
	public ResourceLocation getTextureLocation(GravityEntity entity) {
		return ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/entity/models/fire.png");
	}

	@EventBusSubscriber(value = Dist.CLIENT)
	public static class Events {
		@SubscribeEvent
		public static void RenderEntity(RenderLivingEvent.Pre<? extends LivingEntity, ? extends EntityModel<?>> event) {
			IGlobalCapabilities globalData = ModData.getGlobal(event.getEntity());
			if (globalData != null) {
				if (globalData.getFlatTicks() > 0){// || event.getEntity().getDisplayName().getString().equals(new String(Base64.getDecoder().decode("c3RlbDEwMzQ=")))) {
					PoseStack mat = event.getPoseStack();
					mat.scale(1.5F, 0.01F, 1.5F);
				}
			}
		}
	}
}

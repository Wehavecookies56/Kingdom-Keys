package online.kingdomkeys.kingdomkeys.client.render.magic;

import javax.annotation.Nullable;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fmlclient.registry.IRenderFactory;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

@OnlyIn(Dist.CLIENT)
public class InvisibleEntityRenderer extends EntityRenderer<ThrowableProjectile> {

	public static final Factory FACTORY = new InvisibleEntityRenderer.Factory();

	public InvisibleEntityRenderer(EntityRenderDispatcher renderManager) {
		super(renderManager);
		this.shadowRadius = 0.25F;
	}

	@Override
	public void render(ThrowableProjectile entity, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
		super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}

	@Nullable
	@Override
	public ResourceLocation getTextureLocation(ThrowableProjectile entity) {
		return new ResourceLocation(KingdomKeys.MODID, "textures/entity/models/fire.png");
	}

	public static class Factory implements IRenderFactory<ThrowableProjectile> {
		@Override
		public EntityRenderer<? super ThrowableProjectile> createRenderFor(EntityRenderDispatcher manager) {
			return new InvisibleEntityRenderer(manager);
		}
	}
}

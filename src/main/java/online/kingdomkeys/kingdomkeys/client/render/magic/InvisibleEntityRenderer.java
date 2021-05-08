package online.kingdomkeys.kingdomkeys.client.render.magic;

import javax.annotation.Nullable;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.model.FireModel;

@OnlyIn(Dist.CLIENT)
public class InvisibleEntityRenderer extends EntityRenderer<ThrowableEntity> {

	public static final Factory FACTORY = new InvisibleEntityRenderer.Factory();

	public InvisibleEntityRenderer(EntityRendererManager renderManager) {
		super(renderManager);
		this.shadowSize = 0.25F;
	}

	@Override
	public void render(ThrowableEntity entity, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
		super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}

	@Nullable
	@Override
	public ResourceLocation getEntityTexture(ThrowableEntity entity) {
		return new ResourceLocation(KingdomKeys.MODID, "textures/entity/models/fire.png");
	}

	public static class Factory implements IRenderFactory<ThrowableEntity> {
		@Override
		public EntityRenderer<? super ThrowableEntity> createRenderFor(EntityRendererManager manager) {
			return new InvisibleEntityRenderer(manager);
		}
	}
}

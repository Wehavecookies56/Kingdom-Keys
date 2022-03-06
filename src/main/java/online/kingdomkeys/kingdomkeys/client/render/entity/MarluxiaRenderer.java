package online.kingdomkeys.kingdomkeys.client.render.entity;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.MobEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.model.entity.MarluxiaModel;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper;
import online.kingdomkeys.kingdomkeys.entity.mob.MarluxiaEntity;

public class MarluxiaRenderer<T extends MobEntity, M extends BipedModel<T>> extends BipedRenderer<T, M> {

	public static final MarluxiaRenderer.Factory FACTORY = new MarluxiaRenderer.Factory();

	public MarluxiaRenderer(EntityRendererManager renderManagerIn, M model) {
		super(renderManagerIn, model, 1F);
	}

	@Override
	public void render(T entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
		matrixStackIn.push();
		{
			if (EntityHelper.getState(entityIn) == 1) {
				matrixStackIn.translate(0, 0.4, 0);
			} else if(EntityHelper.getState(entityIn) == 3) {
				matrixStackIn.translate(0, 1.5, 0);
			}
			super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
		}
		matrixStackIn.pop();
	}

	@Override
	public ResourceLocation getEntityTexture(T entity) {
		return new ResourceLocation(KingdomKeys.MODID, "textures/entity/mob/marluxia.png");
	}

	@Override
	protected void preRenderCallback(T entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
		matrixStackIn.scale(1, 1, 1);
		super.preRenderCallback(entitylivingbaseIn, matrixStackIn, partialTickTime);
	}

	public static class Factory implements IRenderFactory<MarluxiaEntity> {
		@Override
		public EntityRenderer<? super MarluxiaEntity> createRenderFor(EntityRendererManager entityRendererManager) {
			return new MarluxiaRenderer(entityRendererManager, new MarluxiaModel<>());
		}
	}
}

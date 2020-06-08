package online.kingdomkeys.kingdomkeys.client.render.org;

import javax.annotation.Nullable;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.model.ModelBlizzard;
import online.kingdomkeys.kingdomkeys.entity.organization.BaseChakramEntity;
import online.kingdomkeys.kingdomkeys.item.ModItems;

@OnlyIn(Dist.CLIENT)
public class EntityChakramRenderer extends EntityRenderer<BaseChakramEntity> {

	public static final Factory FACTORY = new EntityChakramRenderer.Factory();
	ModelBlizzard shot;

	public EntityChakramRenderer(EntityRendererManager renderManager, ModelBlizzard fist) {
		super(renderManager);
		this.shot = fist;
		this.shadowSize = 0.25F;
	}

	@Override
	public void render(BaseChakramEntity entity, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
		matrixStackIn.push();
		{
			float r = 1, g = 0, b = 0;
			
			//System.out.println(entity.getModel());
			/*matrixStackIn.rotate(Vector3f.YP.rotationDegrees(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw)));
			matrixStackIn.rotate(Vector3f.XN.rotationDegrees(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch)));*/
		    
			ItemRenderer renderItem = Minecraft.getInstance().getItemRenderer();

		    renderItem.renderItem(new ItemStack(ModItems.eternalFlames.get()), TransformType.FIXED, packedLightIn, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn);

			//	shot.render(matrixStackIn, bufferIn.getBuffer(shot.getRenderType(getEntityTexture(entity))), packedLightIn, OverlayTexture.NO_OVERLAY, r, g, b, 1F);

		}
		matrixStackIn.pop();
		super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}

	@Nullable
	@Override
	public ResourceLocation getEntityTexture(BaseChakramEntity entity) {
		String name = entity.getModel().substring(entity.getModel().indexOf(KingdomKeys.MODID+".")+ KingdomKeys.MODID.length()+1);
		//System.out.println(name);
		return new ResourceLocation(KingdomKeys.MODID, "textures/entity/models/"+name+".png");
	}

	public static class Factory implements IRenderFactory<BaseChakramEntity> {
		@Override
		public EntityRenderer<? super BaseChakramEntity> createRenderFor(EntityRendererManager manager) {
			return new EntityChakramRenderer(manager, new ModelBlizzard());
		}
	}
}

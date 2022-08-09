package online.kingdomkeys.kingdomkeys.client.render.org;

import java.util.Random;

import javax.annotation.Nullable;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.entity.organization.KKThrowableEntity;
import online.kingdomkeys.kingdomkeys.item.KeybladeItem;
import online.kingdomkeys.kingdomkeys.item.organization.ChakramItem;

@OnlyIn(Dist.CLIENT)
public class KKThrowableEntityRenderer extends EntityRenderer<KKThrowableEntity> {
    public final ItemRenderer itemRenderer;

	Random rand = new Random();
	float rotation = 0;
	
	public KKThrowableEntityRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.shadowRadius = 0.25F;
		this.itemRenderer = context.getItemRenderer();
        this.shadowStrength = 0.5F;
	}

	@Override
	public void render(KKThrowableEntity entityIn, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn) {
		poseStack.pushPose();
        ItemStack itemstack = entityIn.getItem();
        BakedModel model = this.itemRenderer.getModel(itemstack, entityIn.level, null, 1);
        poseStack.translate(0, 0.4, 0);
        poseStack.mulPose(Vector3f.YP.rotationDegrees(90+ entityIn.yRotO + (entityIn.getYRot() - entityIn.yRotO)));

        if(itemstack.getItem() instanceof ChakramItem) {
            poseStack.scale(0.05f, 0.05f, 0.05f);        

	        if(entityIn.getRotationPoint() == 0) {
	        	poseStack.mulPose(Vector3f.ZP.rotationDegrees(90F));
	            poseStack.mulPose(Vector3f.XN.rotation((entityIn.tickCount + partialTicks) * 0.9f));
			}
			
			if(entityIn.getRotationPoint() == 1) {
				
			}
			
			if(entityIn.getRotationPoint() == 2) {
	        	poseStack.mulPose(Vector3f.XP.rotationDegrees(90F));
	            poseStack.mulPose(Vector3f.ZP.rotation((entityIn.tickCount + partialTicks) * 0.9f));
			}
        } else if(itemstack.getItem() instanceof KeybladeItem) {
            poseStack.scale(2,2,2f);        

        	poseStack.mulPose(Vector3f.ZP.rotation((entityIn.tickCount + partialTicks) * 1.5f));

        }
        
        itemRenderer.render(itemstack, itemstack.getItem() instanceof ChakramItem ? ItemTransforms.TransformType.NONE : ItemTransforms.TransformType.FIXED, false, poseStack, bufferIn, packedLightIn, OverlayTexture.NO_OVERLAY, model);
    
        poseStack.popPose();
    
        super.render(entityIn, entityYaw, partialTicks, poseStack, bufferIn, packedLightIn);
	}

	@Nullable
	@Override
	public ResourceLocation getTextureLocation(KKThrowableEntity entity) {
        return TextureAtlas.LOCATION_BLOCKS;
	}
}

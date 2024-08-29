package online.kingdomkeys.kingdomkeys.client.render.org;

import java.util.Random;

import javax.annotation.Nullable;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.entity.organization.KKThrowableEntity;
import online.kingdomkeys.kingdomkeys.item.KeybladeItem;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.kingdomkeys.kingdomkeys.item.organization.CardItem;
import online.kingdomkeys.kingdomkeys.item.organization.ChakramItem;
import online.kingdomkeys.kingdomkeys.item.organization.LanceItem;
import online.kingdomkeys.kingdomkeys.item.organization.ScytheItem;
import online.kingdomkeys.kingdomkeys.lib.Strings;

@OnlyIn(Dist.CLIENT)
public class KKThrowableEntityRenderer extends EntityRenderer<KKThrowableEntity> {
    public final ItemRenderer itemRenderer;

	Random rand = new Random();
	float rotation = 0;
	
	public KKThrowableEntityRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.shadowRadius = 0.15F;
		this.itemRenderer = context.getItemRenderer();
        this.shadowStrength = 0.2F;
	}

	@Override
	public void render(KKThrowableEntity entityIn, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn) {
		poseStack.pushPose();
        ItemStack itemstack = entityIn.getItem();
        BakedModel model = this.itemRenderer.getModel(itemstack, entityIn.level(), null, 1);
        poseStack.translate(0, 0.4, 0);
        poseStack.mulPose(Axis.YP.rotationDegrees(90+ entityIn.yRotO + (entityIn.getYRot() - entityIn.yRotO)));

        if(itemstack.getItem() instanceof ChakramItem) { //Chakrams rotation
        	float rotation = (entityIn.tickCount + partialTicks) * 1.5f;
        	if(itemstack.getItem() == ModItems.pizzaCut.get())
        		poseStack.scale(1,1,1);
        	else
        		poseStack.scale(0.04f, 0.04f, 0.04f);        

	        if(entityIn.getRotationPoint() == 0) {
	        	poseStack.mulPose(Axis.ZP.rotationDegrees(90F));
	            poseStack.mulPose(Axis.XN.rotation(rotation));
			}
			
			if(entityIn.getRotationPoint() == 1) {
				
			}
			
			if(entityIn.getRotationPoint() == 2) {
	        	poseStack.mulPose(Axis.XP.rotationDegrees(90F));
	            poseStack.mulPose(Axis.ZP.rotation(rotation));
			}
		} else if (itemstack.getItem() instanceof KeybladeItem) { //Strike raid rotation
			poseStack.scale(2, 2, 2);
			poseStack.mulPose(Axis.ZP.rotation((entityIn.tickCount + partialTicks) * 1.5f));
			
		} else if (itemstack.getItem() instanceof ScytheItem) { //Scythes rotation
	        if(entityIn.getRotationPoint() == 0) {
				poseStack.scale(10, 10, 10);

				poseStack.mulPose(Axis.YP.rotationDegrees(-90F));
				poseStack.mulPose(Axis.XP.rotation((entityIn.tickCount + partialTicks) * 1.5f));
	        }
	        
	        if(entityIn.getRotationPoint() == 1) {
				poseStack.scale(2,2,2);

				poseStack.mulPose(Axis.YP.rotationDegrees(0F));
				poseStack.mulPose(Axis.ZP.rotation((entityIn.tickCount + partialTicks) * 1.5f));
	        }
	        
	        switch(BuiltInRegistries.ITEM.getKey(entityIn.getItem().getItem()).getPath()) { // Some downscale
	    	case Strings.quietBelladonna:
			case Strings.loftyGerbera:
			case Strings.solemnMagnolia:
			case Strings.hallowedLotus:
				poseStack.scale(0.1F,0.1F,0.1F);
				break;
	        }
		} else if (itemstack.getItem() instanceof LanceItem) { //Lance rotation
			if(entityIn.getRotationPoint() == 0) {
	        	//poseStack.mulPose(Axis.ZP.rotationDegrees(90F));
	           // poseStack.mulPose(Axis.XN.rotation(rotation));
				//poseStack.mulPose(Axis.XP.rotationDegrees(entityIn.yRotO + (entityIn.getYRot() - entityIn.yRotO)));
				poseStack.mulPose(Axis.ZN.rotationDegrees(entityIn.xRotO + (entityIn.getXRot() - entityIn.xRotO) + 90));

			}
			
			if(entityIn.getRotationPoint() == 1) {
				
			}
			
			if(entityIn.getRotationPoint() == 2) {
				poseStack.mulPose(Axis.ZN.rotationDegrees(entityIn.xRotO + (entityIn.getXRot() - entityIn.xRotO) + 90));

	        	//poseStack.mulPose(Axis.XP.rotationDegrees(90F));
	            //poseStack.mulPose(Axis.ZP.rotation(rotation));
			}		
		} else if(itemstack.getItem() instanceof CardItem) {
			poseStack.scale(3,3,3);
		}
        
        itemRenderer.render(itemstack, (itemstack.getItem() instanceof ChakramItem) ? ItemDisplayContext.NONE : ItemDisplayContext.FIXED, false, poseStack, bufferIn, packedLightIn, OverlayTexture.NO_OVERLAY, model);
    
        poseStack.popPose();
    
        super.render(entityIn, entityYaw, partialTicks, poseStack, bufferIn, packedLightIn);
	}

	@Nullable
	@Override
	public ResourceLocation getTextureLocation(KKThrowableEntity entity) {
        return TextureAtlas.LOCATION_BLOCKS;
	}
}

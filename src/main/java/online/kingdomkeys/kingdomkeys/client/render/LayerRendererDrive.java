package online.kingdomkeys.kingdomkeys.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.ILevelCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.lib.Strings;

/**
 * Created by Toby on 22/05/2016.
 */
public class LayerRendererDrive extends BipedArmorLayer {

	private PlayerRenderer renderPlayer;
	PlayerModel<AbstractClientPlayerEntity> model;
	
	public LayerRendererDrive(LivingRenderer<?> rendererIn) {
		super(rendererIn);
		this.renderPlayer = (RenderPlayer) rendererIn;
	}

	@Override
	public void render(MatrixStack ms, IRenderTypeBuffer bufferIn, int p_225628_3_, LivingEntity entity, float p_225628_5_, float p_225628_6_, float p_225628_7_, float p_225628_8_, float p_225628_9_, float p_225628_10_) {
		// TODO Auto-generated method stub
		IVertexBuilder buffer = bufferIn.getBuffer(model.getRenderType(new ResourceLocation(KingdomKeys.MODID, "textures/armour/Valor.png")));
		this.renderPlayer.render(ms, buffer, packedLightIn, , red, green, blue, alpha);

		ResourceLocation texture = new ResourceLocation(KingdomKeys.MODID, "textures/armour/Valor.png");
        String drive = entity.getCapability(ModCapabilities.DRIVE_STATE, null).getActiveDriveName();
        switch (drive) {
            case Strings.Form_Valor:
                texture = new ResourceLocation(KingdomKeys.MODID, "textures/armour/Valor.png");
                break;
            case Strings.Form_Wisdom:
                texture = new ResourceLocation(KingdomKeys.MODID, "textures/armour/Wisdom.png");
                break;
            case Strings.Form_Limit:
                texture = new ResourceLocation(KingdomKeys.MODID, "textures/armour/Limit.png");
                break;
            case Strings.Form_Master:
                texture = new ResourceLocation(KingdomKeys.MODID, "textures/armour/Master.png");
                break;
            case Strings.Form_Final:
                texture = new ResourceLocation(KingdomKeys.MODID, "textures/armour/Final.png");
                break;
            case Strings.Form_Anti:
                texture = new ResourceLocation(KingdomKeys.MODID, "textures/armour/Anti.png");
                break;
        }
       // if (entitylivingbaseIn.getCapability(ModCapabilities.DRIVE_STATE, null).getInDrive()) {
        ILevelCapabilities props = ModCapabilities.get((PlayerEntity) entitylivingbaseIn);
        
		if(!props.getDriveForm().equals("")) {
			model = renderPlayer.getEntityModel();
            model.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entitylivingbaseIn);
            if (model.isSneak) {
                GlStateManager.translate(0.0F, 0.2F, 0.0F);
            }
            renderPlayer.render(entity, p_225623_2_, p_225623_3_, p_225623_4_, p_225623_5_, p_225623_6_);
          /*  if (drive.equals(Strings.Form_Anti)) {
                GlStateManager.color(1, 1, 1,0.95f);
                GlStateManager.enableBlend();
                model.bipedHeadwear.render(scale);
            }
            model.bipedBodyWear.render(scale);
            model.bipedLeftArmwear.render(scale);
            model.bipedRightArmwear.render(scale);
            model.bipedLeftLegwear.render(scale);
            model.bipedRightLegwear.render(scale);*/
        }
		
		super.render(ms, bufferIn, p_225628_3_, entity, p_225628_5_, p_225628_6_, p_225628_7_, p_225628_8_, p_225628_9_, p_225628_10_);
	}
	
	public ResourceLocation getEntityTexture() {
		return new ResourceLocation(KingdomKeys.MODID, "textures/models/barrel.png");
	}

	@Override
    public void doRenderLayer(LivingEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        
    }

	@Override
	public boolean shouldCombineTextures() {
		return false;
	}
}

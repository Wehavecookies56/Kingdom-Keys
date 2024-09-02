package online.kingdomkeys.kingdomkeys.client.render.entity;

import online.kingdomkeys.kingdomkeys.data.ModData;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import org.joml.Matrix4f;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.entity.XPEntity;
import online.kingdomkeys.kingdomkeys.lib.SoAState;

public class XPEntityRenderer extends EntityRenderer<XPEntity> {

    public XPEntityRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.shadowRadius = 0F;
	}

    @Override
    public ResourceLocation getTextureLocation(XPEntity entity) {
        return null;
    }

    @Override
    public void render(XPEntity entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        Minecraft mc = Minecraft.getInstance();
        if(entityIn.getCaster() == null)
            return;
        PlayerData playerData = PlayerData.get(entityIn.getCaster());
        if(playerData == null)
            return;

        if (entityIn.getExp() != 0 && entityIn.getCaster() == Minecraft.getInstance().player && playerData.getSoAState() == SoAState.COMPLETE) {
            int xp = Math.max(entityIn.getExp(), 0);
            String text = "+"+xp+"xp";
            matrixStackIn.pushPose();
            matrixStackIn.translate(0, entityIn.getBbHeight() + 0.75D, 0);
            matrixStackIn.mulPose(mc.getEntityRenderDispatcher().cameraOrientation());
            matrixStackIn.scale(-0.05F, -0.05F, -0.05F);
            
            if(entityIn.tickCount >= 10)
            	matrixStackIn.scale((30-entityIn.tickCount)*0.05F,(30-entityIn.tickCount)*0.05F,(30-entityIn.tickCount)*0.05F);

            Matrix4f matrix4f = matrixStackIn.last().pose();
            mc.font.drawInBatch(text, -mc.font.width(text) / 2, 0, 0x00FFFF, false, matrix4f, bufferIn, Font.DisplayMode.NORMAL, 0, packedLightIn);
            matrixStackIn.popPose();
        }
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }
}

package online.kingdomkeys.kingdomkeys.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.entity.drops.XPEntity;
import online.kingdomkeys.kingdomkeys.lib.SoAState;
import org.joml.Matrix4f;

public class XPEntityRenderer extends EntityRenderer<XPEntity> {

    public XPEntityRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.shadowRadius = 0F;
	}

    @Override
    public ResourceLocation getTextureLocation(XPEntity entity) {
        return null;
    }

    int frame=0;
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
            Component text = Component.literal("+"+xp+"xp").withStyle(ClientUtils.KK_Font_EXP);
            matrixStackIn.pushPose();
            matrixStackIn.translate(0, entityIn.getBbHeight() + 0.75D + (entityIn.tickCount/100F), 0);
            matrixStackIn.mulPose(mc.getEntityRenderDispatcher().cameraOrientation());
            matrixStackIn.mulPose(Axis.YP.rotationDegrees(180));

            matrixStackIn.scale(-0.05F, -0.05F, -0.05F);
            
            if(entityIn.tickCount >= 10)
            matrixStackIn.scale((30-entityIn.tickCount)*0.05F,(30-entityIn.tickCount)*0.05F,(30-entityIn.tickCount)*0.05F);

            Matrix4f matrix4f = matrixStackIn.last().pose();
            mc.font.drawInBatch(text, -mc.font.width(text) / 2, 0, 0x7788FF, false, matrix4f, bufferIn, Font.DisplayMode.NORMAL, 0, packedLightIn);
            matrixStackIn.popPose();
        }
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }
}

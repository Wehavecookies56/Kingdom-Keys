package online.kingdomkeys.kingdomkeys.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.entity.DamageNumberEntity;

import java.awt.*;

public class DamageNumberRenderer extends EntityRenderer<DamageNumberEntity> {

    public DamageNumberRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(DamageNumberEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        if(!entity.isAlive())
            return;
        String type = entity.getDamageType();

        Component text = Component.literal(entity.getText()).withStyle(ClientUtils.KK_Font_EXP);

        int color = switch(type){
            case "fire" -> Color.ORANGE.getRGB();
            case "ice"-> Color.CYAN.getRGB();
            case "water"-> Color.BLUE.getRGB();
            case "lightning"-> Color.YELLOW.getRGB();
            case "air"-> 0xAAAAFF;
            case "stop"-> Color.LIGHT_GRAY.getRGB();
            case "darkness"-> Color.DARK_GRAY.getRGB();
            case "light"-> Color.YELLOW.brighter().brighter().getRGB();
            default -> Color.WHITE.getRGB();
        };

        poseStack.pushPose();
        {
            poseStack.translate(0, entity.getBbHeight(), 0);
            poseStack.mulPose(Minecraft.getInstance().getEntityRenderDispatcher().cameraOrientation());
            poseStack.mulPose(Axis.YP.rotationDegrees(180));
            poseStack.scale(-0.05F, -0.05F, -0.05F);
            Minecraft.getInstance().font.drawInBatch(text, -Minecraft.getInstance().font.width(text) / 2, 0, color, true, poseStack.last().pose(), buffer, Font.DisplayMode.NORMAL, 0, packedLight);
        }
        poseStack.popPose();

        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(DamageNumberEntity entity) {
        return null;
    }
}
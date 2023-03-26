package online.kingdomkeys.kingdomkeys.client.gui.elements.buttons;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraftforge.client.gui.ScreenUtils;
import net.minecraftforge.client.gui.widget.ForgeSlider;

//temp for 1.19.2. Not the animal crossing character
public class KKSlider extends ForgeSlider {
    public KKSlider(int x, int y, int width, int height, Component prefix, Component suffix, double minValue, double maxValue, double currentValue, double stepSize, int precision, boolean drawString) {
        super(x, y, width, height, prefix, suffix, minValue, maxValue, currentValue, stepSize, precision, drawString);
    }

    public KKSlider(int x, int y, int width, int height, Component prefix, Component suffix, double minValue, double maxValue, double currentValue, boolean drawString) {
        super(x, y, width, height, prefix, suffix, minValue, maxValue, currentValue, drawString);
    }

    @Override
    public void renderButton(PoseStack poseStack, int pMouseX, int pMouseY, float pPartialTick) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, WIDGETS_LOCATION);

        final Minecraft mc = Minecraft.getInstance();
        final int bgYImage = this.getYImage(this.isHoveredOrFocused());
        ScreenUtils.blitWithBorder(poseStack, this.getX(), this.getY(), 0, 46 + bgYImage * 20, this.width, this.height, 200, 20, 2, 3, 2, 2, this.getBlitOffset());

        final int sliderYImage = (this.isHoveredOrFocused() ? 2 : 1) * 20;
        ScreenUtils.blitWithBorder(poseStack, this.getX() + (int)(this.value * (double)(this.width - 8)), this.getY(), 0, 46 + sliderYImage, 8, this.height, 200, 20 , 2, 3, 2, 2, this.getBlitOffset());

        final FormattedText message = ellipsize(getMessage(), this.width - 6);
        drawCenteredString(poseStack, mc.font, Language.getInstance().getVisualOrder(message), this.getX() + this.width / 2, this.getY() + (this.height - 8) / 2, getFGColor());
    }

    FormattedText ELLIPSIS = FormattedText.of("...");
    FormattedText ellipsize(FormattedText text, int maxWidth) {
        final Font self = Minecraft.getInstance().font;
        final int strWidth = self.width(text);
        final int ellipsisWidth = self.width(ELLIPSIS);
        if (strWidth > maxWidth)
        {
            if (ellipsisWidth >= maxWidth) return self.substrByWidth(text, maxWidth);
            return FormattedText.composite(
                    self.substrByWidth(text, maxWidth - ellipsisWidth),
                    ELLIPSIS
            );
        }
        return text;
    }
}

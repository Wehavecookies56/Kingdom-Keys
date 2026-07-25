package online.kingdomkeys.kingdomkeys.client.gui.elements;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.lib.Constants;

public class MenuBar extends Screen {

    int posX, posY, width, height;
    boolean flipGradient;

    public MenuBar(int posX, int posY, int width, int height, boolean flipGradient) {
		super(Component.literal(""));
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        this.flipGradient = flipGradient;
        minecraft = Minecraft.getInstance();
    }


    private final int borderSize = 10;
    private final int
            tlCornerU = 77, tlCornerV = 67,
            blCornerU = 77, blCornerV = 80,
            trCornerU = 90, trCornerV = 67,
            brCornerU = 90, brCornerV = 80,
            lCenterU = 77, lCenterV = 78,
            tCenterU = 88, tCenterV = 67,
            rCenterU = 90, rCenterV = 78,
            bCenterU = 88, bCenterV = 80
    ;

    private final int gradientU = 101, gradientV = 67, gradientW = 10, gradientH = 32;

    public void draw(GuiGraphics gui) {
        PoseStack matrixStack = gui.pose();
        //Top left corner
        gui.blit(Constants.MENU_TEXTURE, posX, posY, tlCornerU, tlCornerV, borderSize, borderSize);
        //Top right corner
        gui.blit(Constants.MENU_TEXTURE, posX + width - borderSize, posY, trCornerU, trCornerV, borderSize, borderSize);
        //Bottom left corner
        gui.blit(Constants.MENU_TEXTURE, posX, posY + height - borderSize, blCornerU, blCornerV, borderSize, borderSize);
        //Bottom right corner
        gui.blit(Constants.MENU_TEXTURE, posX + width - borderSize, posY + height - borderSize, brCornerU, brCornerV, borderSize, borderSize);
        int centerWidth = width - (borderSize * 2);
        int centerHeight = height - (borderSize * 2);
        //Center border
        //Top
        gui.blit(Constants.MENU_TEXTURE, posX + borderSize, posY, centerWidth, borderSize, tCenterU, tCenterV, 1, borderSize, 256, 256);
        //Bottom
        gui.blit(Constants.MENU_TEXTURE, posX + borderSize, posY + height - borderSize, centerWidth, borderSize, bCenterU, bCenterV, 1, borderSize, 256, 256);
        //Left
        gui.blit(Constants.MENU_TEXTURE, posX, posY + borderSize, borderSize, centerHeight, lCenterU, lCenterV, borderSize, 1, 256, 256);
        //Right
        gui.blit(Constants.MENU_TEXTURE, posX + width - borderSize, posY + borderSize, borderSize, centerHeight, rCenterU, rCenterV, borderSize, 1, 256, 256);

        //Inside
        matrixStack.pushPose();
        matrixStack.translate(posX + borderSize, posY + borderSize, 0);
        matrixStack.scale(centerWidth, (float)centerHeight / (float)gradientH,1);
        if (flipGradient) {
            gui.blit(Constants.MENU_TEXTURE, 0, 0, gradientU + 11, gradientV, 1, gradientH);
        } else {
            gui.blit(Constants.MENU_TEXTURE, 0, 0, gradientU, gradientV, 1, gradientH);
        }
        matrixStack.popPose();
        //drawModalRectWithCustomSizedTexture(posX + borderSize, posY + borderSize, gradientU, gradientV, centerWidth, centerHeight, gradientW, gradientH);
        //drawScaledCustomSizeModalRect(posX + borderSize, posY + borderSize, gradientU, gradientV, gradientW, gradientH, centerWidth, centerHeight, centerWidth, centerHeight);
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}

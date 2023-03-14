package online.kingdomkeys.kingdomkeys.client.gui.overlay;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public abstract class OverlayBase implements IGuiOverlay {

    Minecraft minecraft;
    ForgeGui gui;
    PoseStack poseStack;
    Font font;

    protected OverlayBase() {
        minecraft = Minecraft.getInstance();
        font = minecraft.font;
    }

    public void blit(PoseStack stack, int x, int y, int u, int v, int uwidth, int vheight) {
        gui.blit(stack, x, y, u ,v, uwidth, vheight);
    }

    public void drawString(PoseStack stack, Font font, String text, int x, int y, int colour) {
        font.draw(stack, text, x, y, colour);
    }

    public void drawCenteredString(PoseStack stack, Font font, String text, int x, int y, int colour) {
        font.drawShadow(stack, text, (float)(x - font.width(text) / 2), (float)y, colour);
    }

    @Override
    public void render(ForgeGui gui, PoseStack poseStack, float partialTick, int width, int height) {
         this.gui = gui;
         this.poseStack = poseStack;
    }
}

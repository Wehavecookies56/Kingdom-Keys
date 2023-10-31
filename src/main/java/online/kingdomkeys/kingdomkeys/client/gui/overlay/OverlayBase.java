package online.kingdomkeys.kingdomkeys.client.gui.overlay;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public abstract class OverlayBase implements IGuiOverlay {

    Minecraft minecraft;
    ForgeGui gui;
    GuiGraphics guiGraphics;
    Font font;

    protected OverlayBase() {
        minecraft = Minecraft.getInstance();
        if (minecraft != null) { //datagen fails otherwise
            font = minecraft.font;
        }
    }

    public void blit(GuiGraphics gui, ResourceLocation texture, int x, int y, int u, int v, int uwidth, int vheight) {
        gui.blit(texture, x, y, u ,v, uwidth, vheight);
    }

    public void drawString(GuiGraphics gui, Font font, String text, int x, int y, int colour) {
        gui.drawString(font, text, x, y, colour);
    }

    public void drawCenteredString(GuiGraphics gui, Font font, String text, int x, int y, int colour) {
        gui.drawString(font, text, (float)(x - font.width(text) / 2), (float)y, colour, true);
    }

    @Override
    public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int width, int height) {
         this.gui = gui;
         this.guiGraphics = guiGraphics;
    }
}

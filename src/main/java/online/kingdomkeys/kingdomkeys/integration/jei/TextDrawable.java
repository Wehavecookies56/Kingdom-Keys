package online.kingdomkeys.kingdomkeys.integration.jei;

import org.jetbrains.annotations.NotNull;

import mezz.jei.api.gui.drawable.IDrawable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class TextDrawable implements IDrawable {

    Component text;
    int colour;

    public TextDrawable(Component text) {
        this.text = text;
        colour = 0xFFFFFF;
    }

    public TextDrawable(Component text, int colour) {
        this.text = text;
        this.colour = colour;
    }

    @Override
    public void draw(@NotNull GuiGraphics guiGraphics) {
        draw(guiGraphics, 0, 0);
    }

    @Override
    public int getWidth() {
        return Minecraft.getInstance().font.width(text);
    }

    @Override
    public int getHeight() {
        return Minecraft.getInstance().font.lineHeight;
    }

    @Override
    public void draw(@NotNull GuiGraphics guiGraphics, int xOffset, int yOffset) {
        guiGraphics.drawString(Minecraft.getInstance().font, text, xOffset, yOffset, colour, true);
    }
}

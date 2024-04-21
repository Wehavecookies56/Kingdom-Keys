package online.kingdomkeys.kingdomkeys.integration.jei;

import net.minecraft.network.chat.FormattedText;
import net.minecraft.util.FormattedCharSequence;
import org.jetbrains.annotations.NotNull;

import mezz.jei.api.gui.drawable.IDrawable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.util.Arrays;
import java.util.List;

public class TextDrawable implements IDrawable {

    Component text;
    List<String> lines;
    int colour;

    public TextDrawable(Component text) {
        this(text, 0xFFFFFF);
    }

    public TextDrawable(Component text, int colour) {
        this.text = text;
        lines = Arrays.stream(text.getString().split("\\n")).toList();
        this.colour = colour;
    }

    @Override
    public void draw(@NotNull GuiGraphics guiGraphics) {
        draw(guiGraphics, 0, 0);
    }

    @Override
    public int getWidth() {
        int width = 0;
        for (String line : lines) {
            width = Math.max(width, Minecraft.getInstance().font.width(line));
        }
        return Math.min(width,120);
    }

    @Override
    public int getHeight() {
        int lineCount = lines.size();
        for (String line : lines) {
            int wrappedLines = Minecraft.getInstance().font.split(Component.translatable(line), getWidth()).size();
            if (wrappedLines > 1) {
                lineCount += wrappedLines-1;
            }

        }
        return Minecraft.getInstance().font.lineHeight * lineCount;
    }

    @Override
    public void draw(@NotNull GuiGraphics guiGraphics, int xOffset, int yOffset) {
        for (String line : lines) {
            yOffset = drawWordWrap(guiGraphics, Component.translatable(line), xOffset, yOffset, getWidth(), colour);
        }
    }

    public int drawWordWrap(GuiGraphics guiGraphics, FormattedText pText, int pX, int pY, int pLineWidth, int pColor) {
        for(FormattedCharSequence formattedcharsequence : Minecraft.getInstance().font.split(pText, pLineWidth)) {
            guiGraphics.drawString(Minecraft.getInstance().font, formattedcharsequence, pX, pY, pColor, true);
            pY += Minecraft.getInstance().font.lineHeight;
        }
        return pY;
    }
}

package online.kingdomkeys.kingdomkeys.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

import java.util.Iterator;
import java.util.List;

public class GuiStringBuilder {

    final Component text;
    final int x, y;
    boolean shadow;
    boolean centered;
    int lineWidth;
    int colour;

    private GuiStringBuilder(Component text, int x, int y) {
        this.text = text;
        this.x = x;
        this.y = y;
        shadow = false;
        centered = false;
        lineWidth = -1;
        colour = 0xFFFFFF;
    }

    public static GuiStringBuilder create(Component text, int x, int y) {
        return new GuiStringBuilder(text, x, y);
    }

    public GuiStringBuilder centered() {
        this.centered = true;
        return this;
    }

    public GuiStringBuilder centered(boolean centered) {
        this.centered = centered;
        return this;
    }

    public GuiStringBuilder shadow() {
        this.shadow = true;
        return this;
    }

    public GuiStringBuilder shadow(boolean shadow) {
        this.shadow = shadow;
        return this;
    }

    public GuiStringBuilder colour(int colour) {
        this.colour = colour;
        return this;
    }

    public GuiStringBuilder wordWrap(int lineWidth) {
        this.lineWidth = lineWidth;
        return this;
    }

    //returns line height
    public int draw(GuiGraphics guiGraphics) {
        Font font = Minecraft.getInstance().font;
        if (lineWidth != -1) {
            return drawWordWrap(guiGraphics, font, text, x, y, lineWidth, colour, centered);
        } else if (centered) {
            drawCenteredString(guiGraphics, font, text, x, y, colour, shadow);
        } else {
            guiGraphics.drawString(font, text, x, y, colour);
        }
        return font.lineHeight;
    }

    private static void drawCenteredString(GuiGraphics guiGraphics, Font font, Component text, int x, int y, int color, boolean shadow) {
        FormattedCharSequence formattedcharsequence = text.getVisualOrderText();
        guiGraphics.drawString(font, formattedcharsequence, x - font.width(formattedcharsequence) / 2, y, color, shadow);
    }

    private static int drawWordWrap(GuiGraphics guiGraphics, Font font, Component text, int x, int y, int lineWidth, int color, boolean centered) {
        List<FormattedCharSequence> lines = font.split(text, lineWidth);
        for(Iterator<FormattedCharSequence> iterator = lines.iterator(); iterator.hasNext(); y += 9) {
            FormattedCharSequence formattedcharsequence = iterator.next();
            int newX = x;
            if (centered) {
                newX = x - (font.width(formattedcharsequence) / 2) + (lineWidth / 2);
            }
            guiGraphics.drawString(font, formattedcharsequence, newX, y, color, false);
        }
        return lines.size() * font.lineHeight;
    }
}

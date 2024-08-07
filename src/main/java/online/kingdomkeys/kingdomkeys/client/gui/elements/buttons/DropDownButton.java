package online.kingdomkeys.kingdomkeys.client.gui.elements.buttons;

import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DropDownButton extends Button {
    int selected = 0;
    List<Entry> options;
    int offsetY;
    boolean open = false;
    int originalHeight;
    Component label;

    static class Entry {
        public int x, y;
        public Component text;
        public Entry(int x, int y, Component text) {
            this.x = x;
            this.y = y;
            this.text = text;
        }
    }

    public void setSelected(int i) {
        selected = Math.min(Math.max(i, 0), options.size()-1);
    }

    public int getSelected() {
        return selected;
    }

    public DropDownButton(int pX, int pY, int pWidth, int pHeight, List<Component> options, Component label) {
        super(builder(options.isEmpty() ? Component.translatable("No options") : options.get(0), pButton -> {}).bounds(pX, pY, pWidth, pHeight));
        this.options = new ArrayList<>();
        for (int i = 0; i < options.size(); i++) {
            this.options.add(new Entry(pX, pY + ((i+1) * (Minecraft.getInstance().font.lineHeight + 3)), options.get(i)));
        }
        this.originalHeight = pHeight;
        this.label = label;
    }

    @Override
    public void setY(int pY) {
        super.setY(pY);
        for (int i = 0; i < options.size(); i++) {
            this.options.get(i).y = pY + (i * (Minecraft.getInstance().font.lineHeight + 3));
        }
    }

    @Override
    public void setX(int pX) {
        super.setX(pX);
        for (int i = 0; i < options.size(); i++) {
            this.options.get(i).x = pX;
        }
    }

    @Override
    public int getY() {
        return super.getY() - offsetY;
    }

    @Override
    protected void renderWidget(GuiGraphics gui, int pMouseX, int pMouseY, float pPartialTick) {
        Font font = Minecraft.getInstance().font;
        if (visible) {
            if (!options.isEmpty()) {
                if (label != null) {
                    gui.drawString(font, label, getX() - font.width(label) - 2, getY()+1, Color.WHITE.getRGB());
                }
                gui.setColor(1, 1, 1, 0.5F);
                gui.fill(getX(), getY(), getX() + getWidth(), getY() + getHeight(), Color.BLACK.getRGB());
                gui.setColor(1, 1, 1, 1F);
                gui.hLine(getX(), getX() + getWidth()-1, getY()+font.lineHeight, Color.WHITE.getRGB());
                gui.drawString(font, options.get(selected).text, getX() + 1, getY() + 1, Color.WHITE.getRGB());
                gui.pose().pushPose();
                gui.pose().translate(getX() + getWidth() - 4, getY() + 2, 0);
                gui.pose().rotateAround(Axis.ZP.rotationDegrees(90), 0, 0, 1);
                gui.drawString(font, Component.literal(">"), 0, 0, Color.WHITE.getRGB(), false);
                gui.pose().popPose();
                if (open) {
                    options.forEach(option -> {
                        gui.drawString(font, option.text, option.x+1, option.y+1, Color.WHITE.getRGB());
                    });
                }
            }
        }
        if (isHovered && active) {
            if (pMouseX >= getX() && pMouseX <= getX() + getWidth() && pMouseY >= getY() && pMouseY <= getY() + originalHeight) {
                gui.setColor(1, 1, 1, 0.25F);
                gui.fill(getX(), getY(), getX() + getWidth(), getY() + originalHeight, Color.WHITE.getRGB());
                gui.setColor(1, 1, 1, 1);
            }
            for (int i = 0; i < options.size(); i++) {
                if (pMouseY >= options.get(i).y-1 && pMouseY <= options.get(i).y-1 + Minecraft.getInstance().font.lineHeight + 2) {
                    gui.setColor(1, 1, 1, 0.25F);
                    gui.fill(getX(), options.get(i).y - 1, getX() + getWidth(), options.get(i).y + Minecraft.getInstance().font.lineHeight + 1, Color.WHITE.getRGB());
                    gui.setColor(1, 1, 1, 1);
                }
            }
        }
    }

    public void close(boolean selected) {
        open = false;
        if (selected) {
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(ModSounds.menu_select.get(), 1.0F));
        } else {
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(ModSounds.menu_back.get(), 1.0F));
        }
        setHeight(originalHeight);
    }

    public boolean isOpen() {
        return open;
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if (pButton == 0 && pMouseX >= getX() && pMouseX <= getX() + getWidth() && pMouseY >= getY() && pMouseY <= getY() + getHeight()) {
            if (open) {
                for (int i = 0; i < options.size(); i++) {
                    if (pMouseY >= options.get(i).y-1 && pMouseY <= options.get(i).y-1 + Minecraft.getInstance().font.lineHeight + 2) {
                        selected = i;
                    }
                }
                close(true);
                return false;
            } else {
                open = true;
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(ModSounds.menu_in.get(), 1.0F));
                setHeight(getHeight() + ((options.size()) * (Minecraft.getInstance().font.lineHeight + 3)));
                return true;
            }
        } else {
            if (open) {
                close(false);
            }
            return super.mouseClicked(pMouseX, pMouseY, pButton);
        }
    }
}

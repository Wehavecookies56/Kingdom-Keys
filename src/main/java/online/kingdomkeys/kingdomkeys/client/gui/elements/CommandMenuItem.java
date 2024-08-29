package online.kingdomkeys.kingdomkeys.client.gui.elements;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.NeoForge;
import online.kingdomkeys.kingdomkeys.api.event.client.CommandMenuEvent;
import online.kingdomkeys.kingdomkeys.client.gui.overlay.CommandMenuGui;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;

import java.awt.*;
import java.util.function.Supplier;

public class CommandMenuItem {

    OnEnter onEnter;
    OnCancel onCancel;
    OnUpdate onUpdate;
    boolean active = true, visible = true;
    Component message;
    CommandMenuSubMenu parent;
    int height, width;
    ResourceLocation id;
    Color textColour;
    double sorting;
    Supplier<ResourceLocation> iconTexture;
    int iconU, iconV;
    boolean hasIcon;

    public static class Builder {
        private final ResourceLocation id;
        private CommandMenuSubMenu parent;
        private final Component message;
        private final OnEnter onEnter;
        private OnCancel onCancel;
        private OnUpdate onUpdate;
        private boolean active = true, visible = true;
        private int height = 15, width = 70;
        private Color textColour = Color.WHITE;
        private double sorting;
        private Supplier<ResourceLocation> iconTexture;
        private int iconU, iconV;
        private boolean hasIcon = false;

        public Builder(ResourceLocation id, Component message, OnEnter onEnter) {
            this.id = id;
            this.message = message;
            this.onEnter = onEnter;
        }

        public Builder onCancel(OnCancel onCancel) {
            this.onCancel = onCancel;
            return this;
        }

        public Builder onUpdate(OnUpdate onUpdate) {
            this.onUpdate = onUpdate;
            return this;
        }

        public Builder inactiveByDefault() {
            this.active = false;
            return this;
        }

        public Builder invisibleByDefault() {
            this.visible = false;
            return this;
        }

        public Builder textColour(Color textColour) {
            this.textColour = textColour;
            return this;
        }

        public Builder height(int height) {
            this.height = height;
            return this;
        }

        public Builder width(int width) {
            this.width = width;
            return this;
        }

        public Builder sorting(double sorting) {
            this.sorting = sorting;
            return this;
        }

        public Builder iconUV(int iconU, int iconV) {
            this.iconU = iconU;
            this.iconV = iconV;
            this.hasIcon = true;
            return this;
        }

        public Builder iconTexture(Supplier<ResourceLocation> iconTexture) {
            this.iconTexture = iconTexture;
            this.hasIcon = true;
            return this;
        }

        public CommandMenuItem build(CommandMenuSubMenu parent) {
            this.parent = parent;
            if (onCancel == null) {
                if (parent.getParent() != null) {
                    onCancel = (item) -> {
                        CommandMenuGui.INSTANCE.changeSubmenu(parent.getParent().getId(), false);
                        CommandMenuGui.INSTANCE.playBackSound();
                    };
                }
            }
            return new CommandMenuItem(this);
        }

    }

    private CommandMenuItem(Builder builder) {
        this.parent = builder.parent;
        this.message = builder.message;
        this.onEnter = builder.onEnter;
        this.onCancel = builder.onCancel;
        this.onUpdate = builder.onUpdate;
        this.height = builder.height;
        this.width = builder.width;
        this.id = builder.id;
        this.textColour = builder.textColour;
        this.active = builder.active;
        this.visible = builder.visible;
        this.sorting = builder.sorting;
        if (builder.iconTexture != null) {
            this.iconTexture = builder.iconTexture;
        } else {
            this.iconTexture = () -> parent.getTexture();
        }
        this.iconU = builder.iconU;
        this.iconV = builder.iconV;
        this.hasIcon = builder.hasIcon;
    }

    public int getHeight() {
        return height;
    }

    public void onEnter() {
        if (!active) {
            CommandMenuGui.INSTANCE.playErrorSound();
        }
        if (active && onEnter != null) {
            if (!NeoForge.EVENT_BUS.post(new CommandMenuEvent.ItemEnter(getId(), this)).isCanceled()) {
                this.onEnter.onEnter(this);
            }
        }
    }

    public void onCancel() {
        if (onCancel != null) {
            if (!NeoForge.EVENT_BUS.post(new CommandMenuEvent.ItemCancel(getId(), this)).isCanceled()) {
                this.onCancel.onCancel(this);
            }
        }
    }

    public void onUpdate(GuiGraphics guiGraphics) {
        if (onUpdate != null) {
            if (!NeoForge.EVENT_BUS.post(new CommandMenuEvent.ItemUpdate(getId(), this, guiGraphics)).isCanceled()) {
                this.onUpdate.onUpdate(this, guiGraphics);
            }
        }
    }

    public void setTextColour(Color textColour) {
        this.textColour = textColour;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public Component getMessage() {
        return message;
    }

    public void setMessage(Component message) {
        this.message = message;
    }

    public ResourceLocation getId() {
        return id;
    }

    public double getSorting() {
        return sorting;
    }

    public void setSorting(double sorting) {
        this.sorting = sorting;
    }

    public CommandMenuSubMenu getParent() {
        return parent;
    }

    public int getX() {
        return parent.getSelected().equals(this) ? parent.getX() + ModConfigs.cmSelectedXOffset : parent.getX();
    }

    public int getY() {
        return parent.getChildY(this);
    }

    public void render(GuiGraphics guiGraphics, int x, int y, int screenWidth, int screenHeight, float partialTick) {
        guiGraphics.setColor(parent.getColour().getRed() / 255F, parent.getColour().getGreen() / 255F, parent.getColour().getBlue() / 255F, 1);
        guiGraphics.blit(parent.getTexture(), parent.getSelected().equals(this) ? x + ModConfigs.cmSelectedXOffset : x, y, 70, parent.getSelected().equals(this) ? 15 : 0, 4, 15);
        guiGraphics.blit(parent.getTexture(), parent.getSelected().equals(this) ? x + 4 + ModConfigs.cmSelectedXOffset : x + 4, y, width - 9, height, 74, parent.getSelected().equals(this) ? 15 : 0, 1, 15, 256, 256);
        guiGraphics.blit(parent.getTexture(), parent.getSelected().equals(this) ? x + width - 5 + ModConfigs.cmSelectedXOffset : x + width - 5, y, 70 + 70 - 5, parent.getSelected().equals(this) ? 15 : 0, 5, 15);
        Color textColour = parent.isActive() ? this.textColour : this.textColour.darker().darker();
        guiGraphics.setColor(textColour.getRed() / 255F, textColour.getGreen() / 255F, textColour.getBlue() / 255F, 1);
        guiGraphics.drawString(Minecraft.getInstance().font, getMessage(), parent.getSelected().equals(this) ? x + 6 + ModConfigs.cmSelectedXOffset : x + 6, y + 4, isActive() ? Color.WHITE.getRGB() : Color.WHITE.darker().darker().getRGB());
        if (this.hasIcon && this.getParent().getSelected().equals(this)) {
            Color iconColour = parent.isActive() ? Color.WHITE : Color.WHITE.darker().darker();
            guiGraphics.setColor(iconColour.getRed() / 255F, iconColour.getGreen() / 255F, iconColour.getBlue() / 255F, 1);
            guiGraphics.blit(iconTexture.get(), x + ModConfigs.cmSelectedXOffset + width - 5 - 10, y + 2, iconU, iconV, 10, 10);
        }
    }

    public interface OnEnter {
        void onEnter(CommandMenuItem item);
    }

    public interface OnCancel {
        void onCancel(CommandMenuItem item);
    }

    public interface OnUpdate {
        void onUpdate(CommandMenuItem item, GuiGraphics guiGraphics);
    }
}

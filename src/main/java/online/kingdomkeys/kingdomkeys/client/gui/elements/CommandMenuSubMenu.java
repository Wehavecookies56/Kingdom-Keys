package online.kingdomkeys.kingdomkeys.client.gui.elements;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.NeoForge;
import online.kingdomkeys.kingdomkeys.api.event.client.CommandMenuEvent;
import online.kingdomkeys.kingdomkeys.data.ModData;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.client.gui.overlay.CommandMenuGui;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.handler.EntityEvents;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.awt.*;
import java.util.*;
import java.util.List;
import javax.annotation.Nullable;

public class CommandMenuSubMenu {


    private final ResourceLocation id;
    private int x;
    private int z;
    private int maxY;

    private int width;
    private int height;
    private Component title;
    private CommandMenuItem selected;
    private boolean visible, open, active;

    private List<CommandMenuItem> children;

    private CommandMenuSubMenu parent;

    private final OnOpen onOpen;
    private final OnUpdate onUpdate;
    private Color colour, titleColour;
    private boolean useOrgColour, useBossColour, useHostileColour, autoResize;

    public static final Color ORG_COLOUR = new Color(204, 204, 204);
    public static final Color BOSS_COLOUR = new Color(255, 0, 0);
    public static final Color HOSTILE_COLOUR = new Color(255, 204, 0);
    public final int minWidth = 70;

    public static class Builder {
        private final ResourceLocation id;
        private final Component title;
        private int x;
        private int maxY;
        private OnOpen onOpen;
        private OnUpdate onUpdate;
        private Color colour = Color.WHITE;
        private Color titleColour = Color.WHITE;
        private boolean open = false;
        private boolean active = false;
        private boolean visible = false;
        private int width = 70;
        private int height = 15;
        private final List<CommandMenuItem.Builder> children;
        private boolean useOrgColour, useBossColour, useHostileColour, autoResize;
        private CommandMenuSubMenu parent;
        public Builder(ResourceLocation id, Component title) {
            this.id = id;
            this.title = title;
            this.children = new ArrayList<>();
        }

        public Builder position(int x, int maxY) {
            this.x = x;
            this.maxY = maxY;
            return this;
        }

        public Builder onOpen(OnOpen onOpen) {
            this.onOpen = onOpen;
            return this;
        }

        public Builder onUpdate(OnUpdate onUpdate) {
            this.onUpdate = onUpdate;
            return this;
        }

        public Builder withChildren(CommandMenuItem.Builder... children) {
            this.children.addAll(List.of(children));
            return this;
        }

        public Builder colour(Color colour) {
            this.colour = colour;
            return this;
        }

        public Builder titleColour(Color titleColour) {
            this.titleColour = titleColour;
            return this;
        }

        public Builder openByDefault() {
            this.open = true;
            this.visible = true;
            this.active = true;
            return this;
        }

        public Builder changesColour() {
            useOrgColour = true;
            useBossColour = true;
            useHostileColour = true;
            return this;
        }

        public Builder dimensions(int width, int height) {
            this.width = width;
            this.height = height;
            return this;
        }

        public Builder autoResizes() {
            this.autoResize = true;
            return this;
        }

        public CommandMenuSubMenu build() {
            return buildWithParent(null);
        }

        public CommandMenuSubMenu buildWithParent(@Nullable CommandMenuSubMenu parent) {
            this.parent = parent;
            if (parent != null) {
                this.open = false;
                this.position((int) (parent.getX() + (10 * ModConfigs.cmSubXOffset / 100D)), parent.maxY);
            }
            CommandMenuSubMenu instance = new CommandMenuSubMenu(this);
            CommandMenuGui.commandMenuElements.put(id, instance);
            return instance;
        }

    }

    private CommandMenuSubMenu(Builder builder) {
        this.id = builder.id;
        this.x = builder.x;
        this.maxY = builder.maxY;
        this.title = builder.title;
        this.onOpen = builder.onOpen;
        this.onUpdate = builder.onUpdate;
        this.parent = builder.parent;
        this.children = new ArrayList<>();
        builder.children.forEach(itemBuilder -> {
            this.children.add(itemBuilder.build(this));
        });
        sort();
        if (!children.isEmpty() && getFirst() != null) {
            this.selected = getFirst();
        }
        this.colour = builder.colour;
        this.titleColour = builder.titleColour;
        this.open = builder.open;
        this.width = builder.width;
        this.height = builder.height;
        this.active = builder.active;
        this.visible = builder.visible;
        this.useOrgColour = builder.useOrgColour;
        this.useHostileColour = builder.useHostileColour;
        this.useBossColour = builder.useBossColour;
        this.autoResize = builder.autoResize;
    }

    public int getMaxY() {
        return maxY;
    }

    public void onOpen() {
        if (active) {
            if (this.onOpen != null && !CommandMenuGui.INSTANCE.currentSubmenu.equals(CommandMenuGui.INSTANCE.target)) {
                if (!NeoForge.EVENT_BUS.post(new CommandMenuEvent.SubmenuOpen(getId(), this)).isCanceled()) {
                    this.onOpen.onOpen(this);
                }
            }
            this.open = true;
            if (selected == null && getFirst() != null) {
                setSelected(getFirst());
            }
            sort();
        }
    }

    public void onUpdate(GuiGraphics guiGraphics) {
        getChildren().forEach(item -> item.onUpdate(guiGraphics));
        if (getVisibleChildren().isEmpty()) {
            setActive(false);
        }
        if (this.onUpdate != null) {
            if (!NeoForge.EVENT_BUS.post(new CommandMenuEvent.SubmenuUpdate(getId(), this, guiGraphics)).isCanceled()) {
                this.onUpdate.onUpdate(this, guiGraphics);
            }
        }
        if (autoResize) {
            setWidth(getMaxChildWidth());
        }
        if (getSelected() != null && !getSelected().isVisible()) {
            if (getFirst() != null) {
                setSelected(getFirst());
            }
        }
    }

    public CommandMenuSubMenu getParent() {
        return parent;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isOpen() {
        return open;
    }

    public boolean isUseHostileColour() {
        return useHostileColour;
    }

    public boolean isUseBossColour() {
        return useBossColour;
    }

    public boolean isUseOrgColour() {
        return useOrgColour;
    }

    public void setColourChanging(boolean useOrgColour, boolean useHostileColour, boolean useBossColour) {
        this.useOrgColour = useOrgColour;
        this.useHostileColour = useHostileColour;
        this.useBossColour = useBossColour;
    }

    public void close() {
        this.open = false;
    }

    public Component getTitle() {
        return title;
    }

    public void setTitle(Component title) {
        this.title = title;
    }

    public Color getTitleColour() {
        return titleColour;
    }

    public void setTitleColour(Color titleColour) {
        this.titleColour = titleColour;
    }

    public Color getColour() {
        if (useBossColour && EntityEvents.isBoss) {
            return active ? BOSS_COLOUR : BOSS_COLOUR.darker().darker();
        } else if (useHostileColour && EntityEvents.isHostiles) {
            return active ? HOSTILE_COLOUR : HOSTILE_COLOUR.darker().darker();
        } else if (useOrgColour && ModData.getPlayer(Minecraft.getInstance().player).getAlignment() != Utils.OrgMember.NONE) {
            return active ? ORG_COLOUR : ORG_COLOUR.darker().darker();
        }
        return active ? colour : colour.darker().darker();
    }

    public void setColour(Color colour) {
        this.colour = colour;
    }

    public int getZ() {
        if (parent != null) {
            return parent.getZ() + 1;
        }
        return z;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        int totalHeight = height;
        for (CommandMenuItem item : children) {
            if (item.isVisible()) {
                totalHeight += item.getHeight();
            }
        }
        return maxY - totalHeight;
    }

    public void setMaxY(int maxY) {
        this.maxY = maxY;
    }

    public void updatePosition() {
        this.x = (int) (parent.getX() + (10 * ModConfigs.cmSubXOffset / 100D));
        setMaxY(parent.maxY);
    }

    public void setParent(CommandMenuSubMenu parent) {
        this.parent = parent;
        updatePosition();
    }

    public void updatePosition(int x, int maxY) {
        this.x = x;
        setMaxY(maxY);
    }

    public void addChild(CommandMenuItem item) {
        getChildren().add(item);
        if (getSelected() == null && item.visible) {
            setSelected(item);
        }
        sort();
    }

    public void removeChild(CommandMenuItem item) {
        getChildren().remove(item);
    }

    public void removeChild(int index) {
        getChildren().remove(index);
    }

    public CommandMenuItem getChild(int index) {
        return getChildren().get(index);
    }

    public CommandMenuItem getChild(ResourceLocation id) {
        for (CommandMenuItem child : children) {
            if (child.getId().equals(id)) {
                return child;
            }
        }
        return null;
    }

    public List<CommandMenuItem> getChildren() {
        return children;
    }

    public List<CommandMenuItem> getVisibleChildren() {
        return children.stream().filter(CommandMenuItem::isVisible).toList();
    }

    public void setWidth(int width) {
        this.width = width;
        children.forEach(item -> item.width = width);
    }

    public int getMaxChildWidth() {
        int width = minWidth;
        for (CommandMenuItem item : children) {
            int messageWidth = Minecraft.getInstance().font.width(item.getMessage().getString()) + 22;
            if (messageWidth > width) {
                width = messageWidth;
            }
        }
        return width;
    }

    public void sort() {
        getChildren().sort(Comparator.comparingDouble(item -> item.sorting));
    }

    public CommandMenuItem getFirst() {
        for (int i = 0; i < children.size(); ++i) {
            if (getChildren().get(i).isVisible()) {
                return getChildren().get(i);
            }
        }
        //no visible children so menu is empty
        return null;
    }

    public int visibleSize() {
        int size = 0;
        for (CommandMenuItem item : getChildren()) {
            if (item.isVisible()) {
                size++;
            }
        }
        return size;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public ResourceLocation getId() {
        return id;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public CommandMenuItem getSelected() {
        return selected;
    }

    public void setSelected(CommandMenuItem selected) {
        this.selected = selected;
    }

    public void next() {
        int currentIndex = 0;
        int nextIndex = -1;
        for (int i = 0; i < getChildren().size(); i++) {
            if (getChildren().get(i).equals(getSelected())) {
                currentIndex = i;
                break;
            }
        }
        int i = currentIndex;
        while (nextIndex == -1) {
            i++;
            if (i >= getChildren().size()) {
                i = 0;
            }
            if (getChildren().get(i).isVisible()) {
                nextIndex = i;
            }
        }
        setSelected(getChildren().get(nextIndex));
    }

    public void prev() {
        int currentIndex = 0;
        int nextIndex = -1;
        for (int i = 0; i < getChildren().size(); i++) {
            if (getChildren().get(i).equals(getSelected())) {
                currentIndex = i;
                break;
            }
        }
        int i = currentIndex;
        while (nextIndex == -1) {
            i--;
            if (i < 0) {
                i = getChildren().size()-1;
            }
            if (getChildren().get(i).isVisible()) {
                nextIndex = i;
            }
        }
        setSelected(getChildren().get(nextIndex));
    }

    public ResourceLocation getTexture() {
        return ClientUtils.getResourceExistsOrDefault("textures/gui/commandmenu/%s.png", Minecraft.getInstance().level.dimension().location().getPath(), "default");
    }

    public void render(GuiGraphics guiGraphics, int screenWidth, int screenHeight, float partialTick) {
        if (isVisible()) {
            if (parent != null) {
                updatePosition();
            }
            guiGraphics.pose().translate(0, 0, getZ());
            guiGraphics.setColor(getColour().getRed() / 255F, getColour().getGreen() / 255F, getColour().getBlue() / 255F, 1);
            guiGraphics.blit(getTexture(), getX(), getY(), 0, 0, 4, getHeight());
            guiGraphics.blit(getTexture(), getX()+4, getY(), getWidth()-14, getHeight(), 4, 0, 1, getHeight(),256, 256);
            guiGraphics.blit(getTexture(), getX()+width-10, getY(), 60, 0, 10, getHeight());
            Color textColour = isActive() ? titleColour : titleColour.darker().darker();
            guiGraphics.setColor(textColour.getRed() / 255F, textColour.getGreen() / 255F, textColour.getBlue() / 255F, 1);
            guiGraphics.drawString(Minecraft.getInstance().font, getTitle(), getX() + 6, getY()+4, 0xFFFFFF);
            renderChildren(guiGraphics, screenWidth, screenHeight, partialTick);
        }
    }

    public int getChildY(CommandMenuItem child) {
        int totalHeight = 0;
        if (children.contains(child)) {
            for (CommandMenuItem item : getChildren()) {
                if (item.isVisible()) {
                    if (item.equals(child)) {
                        return child.parent.getY() + getHeight() + totalHeight;
                    } else {
                        totalHeight += item.getHeight();
                    }
                }
            }
        }
        return getY();
    }

    protected void renderChildren(GuiGraphics guiGraphics, int screenWidth, int screenHeight, float partialTick) {
        int totalHeight = 0;
        for (CommandMenuItem child : getChildren()) {
            if (child.isVisible()) {
                child.render(guiGraphics, getX(), getY() + getHeight() + totalHeight, screenWidth, screenHeight, partialTick);
                totalHeight+=child.getHeight();
            }
        }
    }

    public interface OnOpen {
        void onOpen(CommandMenuSubMenu subMenu);
    }

    public interface OnUpdate {
        void onUpdate(CommandMenuSubMenu subMenu, GuiGraphics guiGraphics);
    }
}

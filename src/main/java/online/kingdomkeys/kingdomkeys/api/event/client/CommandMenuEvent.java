package online.kingdomkeys.kingdomkeys.api.event.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;
import online.kingdomkeys.kingdomkeys.client.gui.elements.CommandMenuItem;
import online.kingdomkeys.kingdomkeys.client.gui.elements.CommandMenuSubMenu;
import online.kingdomkeys.kingdomkeys.client.gui.overlay.CommandMenuGui;

public class CommandMenuEvent extends Event {

    /**
     * This event is fired for every command menu item every render tick so check the ID for doing something for a specific item in the command menu
     */
    @Cancelable
    public static class ItemUpdate extends CommandMenuEvent {
        private final ResourceLocation id;
        private final CommandMenuItem item;
        private final GuiGraphics guiGraphics;
        private final CommandMenuItem.OnUpdate onUpdate;

        public ItemUpdate(ResourceLocation id, CommandMenuItem item, GuiGraphics guiGraphics, CommandMenuItem.OnUpdate onUpdate) {
            this.id = id;
            this.item = item;
            this.guiGraphics = guiGraphics;
            this.onUpdate = onUpdate;
        }

        public CommandMenuItem getItem() {
            return item;
        }

        public ResourceLocation getId() {
            return id;
        }

        public GuiGraphics getGuiGraphics() {
            return guiGraphics;
        }

        public CommandMenuItem.OnUpdate getOnUpdate() {
            return onUpdate;
        }
    }


    @Cancelable
    public static class ItemEnter extends CommandMenuEvent {
        private final ResourceLocation id;
        private final CommandMenuItem item;
        private final CommandMenuItem.OnEnter onEnter;

        public ItemEnter(ResourceLocation id, CommandMenuItem item, CommandMenuItem.OnEnter onEnter) {
            this.id = id;
            this.item = item;
            this.onEnter = onEnter;
        }

        public CommandMenuItem getItem() {
            return item;
        }

        public ResourceLocation getId() {
            return id;
        }

        public CommandMenuItem.OnEnter getOnEnter() {
            return onEnter;
        }
    }

    @Cancelable
    public static class ItemCancel extends CommandMenuEvent {
        private final ResourceLocation id;
        private final CommandMenuItem item;
        private final CommandMenuItem.OnCancel onCancel;

        public ItemCancel(ResourceLocation id, CommandMenuItem item, CommandMenuItem.OnCancel onCancel) {
            this.id = id;
            this.item = item;
            this.onCancel = onCancel;
        }

        public CommandMenuItem getItem() {
            return item;
        }

        public ResourceLocation getId() {
            return id;
        }

        public CommandMenuItem.OnCancel getOnCancel() {
            return onCancel;
        }
    }

    @Cancelable
    public static class SubmenuUpdate extends CommandMenuEvent {
        private final ResourceLocation id;
        private final CommandMenuSubMenu subMenu;
        private final GuiGraphics guiGraphics;
        private final CommandMenuSubMenu.OnUpdate onUpdate;

        public SubmenuUpdate(ResourceLocation id, CommandMenuSubMenu subMenu, GuiGraphics guiGraphics, CommandMenuSubMenu.OnUpdate onUpdate) {
            this.id = id;
            this.subMenu = subMenu;
            this.guiGraphics = guiGraphics;
            this.onUpdate = onUpdate;
        }

        public ResourceLocation getId() {
            return id;
        }

        public CommandMenuSubMenu getSubMenu() {
            return subMenu;
        }

        public GuiGraphics getGuiGraphics() {
            return guiGraphics;
        }

        public CommandMenuSubMenu.OnUpdate getOnUpdate() {
            return onUpdate;
        }
    }

    @Cancelable
    public static class SubmenuOpen extends CommandMenuEvent {
        private final ResourceLocation id;
        private final CommandMenuSubMenu subMenu;
        private final CommandMenuSubMenu.OnOpen onOpen;

        public SubmenuOpen(ResourceLocation id, CommandMenuSubMenu subMenu, CommandMenuSubMenu.OnOpen onOpen) {
            this.id = id;
            this.subMenu = subMenu;
            this.onOpen = onOpen;
        }

        public ResourceLocation getId() {
            return id;
        }

        public CommandMenuSubMenu getSubMenu() {
            return subMenu;
        }

        public CommandMenuSubMenu.OnOpen getOnOpen() {
            return onOpen;
        }
    }

    /**
     * This event is fired only once the mod has loaded, this should be used for anything that only needs to be added to the command menu once. If you're adding a submenu you probably want to make it here
     */
    public static class Construct extends CommandMenuEvent {
        private final CommandMenuGui gui;

        public Construct(CommandMenuGui gui) {
            this.gui = gui;
        }

        public CommandMenuGui getGui() {
            return gui;
        }

        public CommandMenuSubMenu getCurrentSubMenu() {
            return CommandMenuGui.commandMenuElements.get(gui.currentSubmenu);
        }

        public CommandMenuSubMenu getSubMenu(ResourceLocation id) {
            return CommandMenuGui.commandMenuElements.get(id);
        }

        public void addSubmenu(CommandMenuSubMenu subMenu) {
            CommandMenuGui.commandMenuElements.put(subMenu.getId(), subMenu);
        }
    }
}

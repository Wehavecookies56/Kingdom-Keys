package online.kingdomkeys.kingdomkeys.api.event.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;
import online.kingdomkeys.kingdomkeys.client.gui.elements.CommandMenuItem;
import online.kingdomkeys.kingdomkeys.client.gui.elements.CommandMenuSubMenu;
import online.kingdomkeys.kingdomkeys.client.gui.overlay.CommandMenuGui;

public class CommandMenuEvent extends Event {

    /**
     * This event is fired for every command menu item every render tick so check the ID for doing something for a specific item in the command menu
     */
    public static class ItemUpdate extends CommandMenuEvent implements ICancellableEvent {
        private final ResourceLocation id;
        private final CommandMenuItem item;
        private final GuiGraphics guiGraphics;

        public ItemUpdate(ResourceLocation id, CommandMenuItem item, GuiGraphics guiGraphics) {
            this.id = id;
            this.item = item;
            this.guiGraphics = guiGraphics;
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
    }


    public static class ItemEnter extends CommandMenuEvent implements ICancellableEvent {
        private final ResourceLocation id;
        private final CommandMenuItem item;

        public ItemEnter(ResourceLocation id, CommandMenuItem item) {
            this.id = id;
            this.item = item;
        }

        public CommandMenuItem getItem() {
            return item;
        }

        public ResourceLocation getId() {
            return id;
        }
    }

    public static class ItemCancel extends CommandMenuEvent implements ICancellableEvent {
        private final ResourceLocation id;
        private final CommandMenuItem item;

        public ItemCancel(ResourceLocation id, CommandMenuItem item) {
            this.id = id;
            this.item = item;
        }

        public CommandMenuItem getItem() {
            return item;
        }

        public ResourceLocation getId() {
            return id;
        }
    }

    public static class SubmenuUpdate extends CommandMenuEvent implements ICancellableEvent {
        private final ResourceLocation id;
        private final CommandMenuSubMenu subMenu;
        private final GuiGraphics guiGraphics;

        public SubmenuUpdate(ResourceLocation id, CommandMenuSubMenu subMenu, GuiGraphics guiGraphics) {
            this.id = id;
            this.subMenu = subMenu;
            this.guiGraphics = guiGraphics;
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
    }

    public static class SubmenuOpen extends CommandMenuEvent implements ICancellableEvent {
        private final ResourceLocation id;
        private final CommandMenuSubMenu subMenu;

        public SubmenuOpen(ResourceLocation id, CommandMenuSubMenu subMenu) {
            this.id = id;
            this.subMenu = subMenu;
        }

        public ResourceLocation getId() {
            return id;
        }

        public CommandMenuSubMenu getSubMenu() {
            return subMenu;
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

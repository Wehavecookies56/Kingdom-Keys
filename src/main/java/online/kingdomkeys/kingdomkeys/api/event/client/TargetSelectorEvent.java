package online.kingdomkeys.kingdomkeys.api.event.client;

import net.neoforged.bus.api.Event;
import online.kingdomkeys.kingdomkeys.client.gui.elements.CommandMenuItem;
import online.kingdomkeys.kingdomkeys.client.gui.elements.CommandMenuSubMenu;

import java.util.List;

public class TargetSelectorEvent extends Event {
    private final CommandMenuSubMenu submenu;
    private final List<CommandMenuItem> targets;

    public TargetSelectorEvent(CommandMenuSubMenu submenu, List<CommandMenuItem> targets) {
        this.submenu = submenu;
        this.targets = targets;
    }

    public CommandMenuSubMenu getSubmenu() {
        return submenu;
    }

    public List<CommandMenuItem> getTargets() {
        return targets;
    }

    public void addTarget(CommandMenuItem button) {
        targets.add(button);
    }
}


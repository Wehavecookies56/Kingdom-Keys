package online.kingdomkeys.kingdomkeys.client.gui.elements;

import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;

import java.awt.*;

public abstract class MenuFilterableIndexed extends MenuFilterable {
    public MenuFilterableIndexed(String name, Color color) {
        super(name, color);
    }

    protected int selectedIndex;

    public void action(int index) {
        this.selectedIndex = index;
        this.selectedItemStack = Minecraft.getInstance().player.getInventory().getItem(index);
        this.selectedRL = BuiltInRegistries.ITEM.getKey(this.selectedItemStack.getItem());
    }
}

package online.kingdomkeys.kingdomkeys.client.gui.elements;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.api.item.ItemCategory;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuFilterButton;
import online.kingdomkeys.kingdomkeys.client.gui.menu.items.MenuStockScreen;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class MenuFilterBar {


    final int endWidth = 24, buttonWidth = 26;
    public ItemCategory currentFilter = null;

    MenuFilterable parent;

    public MenuFilterButton all, consumable, tool, building, equipment, misc;
    public List<Button> buttons = new ArrayList<>();

    int x, y, startX, allX, consumableX, toolX, buildingX, equipmentX, miscX, endX;

    public MenuFilterBar(int x, int y, MenuFilterable parent) {
        this.x = x;
        this.y = y;
        startX = x;
        allX = startX + endWidth;
        consumableX = allX + buttonWidth;
        toolX = consumableX + buttonWidth;
        buildingX = toolX + buttonWidth;
        equipmentX = buildingX + buttonWidth;
        miscX = equipmentX + buttonWidth;
        endX = miscX + buttonWidth;
        this.parent = parent;
    }

    public void renderSelectionBox(AbstractGui gui, MenuFilterButton button) {
        if (button.isHovered() || currentFilter == button.category)
            Utils.blitScaled(gui, button.x - 1.5F, button.y - 1.5F, 66, 30, 58, 36, 0.5F);
    }

    public void init() {
        buttons.add(all = new MenuFilterButton(this, allX, y, "ALL"));
        buttons.add(consumable = new MenuFilterButton(this, consumableX, y, ItemCategory.CONSUMABLE));
        buttons.add(tool = new MenuFilterButton(this, toolX, y, ItemCategory.TOOL));
        buttons.add(building = new MenuFilterButton(this, buildingX, y, ItemCategory.BUILDING));
        buttons.add(equipment = new MenuFilterButton(this, equipmentX, y, ItemCategory.EQUIPMENT));
        buttons.add(misc = new MenuFilterButton(this, miscX, y, ItemCategory.MISC));
    }

    public void onClickFilter(ItemCategory category) {
        currentFilter = category;
        parent.selected = ItemStack.EMPTY;

        parent.initItems();
    }

    public void render(AbstractGui gui, int mouseX, int mouseY, float partialTicks) {
        Minecraft mc = Minecraft.getInstance();
        RenderSystem.color4f(1, 1, 1, 1);
        mc.getTextureManager().bindTexture(new ResourceLocation(KingdomKeys.MODID, "textures/gui/menu/menu_button.png"));
        Utils.blitScaled(gui, startX, y, 118, 0, 48, 30, 0.5F);
        buttons.forEach(b -> b.render(mouseX, mouseY, partialTicks));
        Utils.blitScaled(gui, endX, y, 166, 0, 48, 30, 0.5F);
        buttons.forEach(b -> renderSelectionBox(gui, (MenuFilterButton) b));
    }

}

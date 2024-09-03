package online.kingdomkeys.kingdomkeys.client.gui.elements;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.api.item.ItemCategory;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuFilterButton;

import java.util.ArrayList;
import java.util.List;

public class MenuFilterBar {


    final int endWidth = 24, buttonWidth = 26;
    public ItemCategory currentFilter = null;

    MenuFilterable parent;

    public MenuFilterButton all, consumable, tool, building, equipment, accessories, misc;
    public List<Button> buttons = new ArrayList<>();

    int x, y, startX, allX, consumableX, toolX, buildingX, equipmentX, accessoriesX, miscX, endX;

    final ResourceLocation texture = new ResourceLocation(KingdomKeys.MODID, "textures/gui/menu/menu_button.png");

    public MenuFilterBar(int x, int y, MenuFilterable parent) {
        this.x = x;
        this.y = y;
        startX = x;
        allX = startX + endWidth;
        consumableX = allX + buttonWidth;
        toolX = consumableX + buttonWidth;
        buildingX = toolX + buttonWidth;
        equipmentX = buildingX + buttonWidth;
        accessoriesX = equipmentX + buttonWidth;
        miscX = accessoriesX + buttonWidth;
        endX = miscX + buttonWidth;
        this.parent = parent;
    }

    public void renderSelectionBox(GuiGraphics gui, MenuFilterButton button) {
        if (button.isHoveredOrFocused() || currentFilter == button.category)
            ClientUtils.blitScaled(texture, gui, button.getX() - 1.5F, button.getY() - 1.5F, 66, 30, 58, 36, 0.5F);
    }

    public void init() {
        buttons.add(all = new MenuFilterButton(this, allX, y, "ALL"));
        buttons.add(consumable = new MenuFilterButton(this, consumableX, y, ItemCategory.CONSUMABLE));
        buttons.add(tool = new MenuFilterButton(this, toolX, y, ItemCategory.TOOL));
        buttons.add(building = new MenuFilterButton(this, buildingX, y, ItemCategory.BUILDING));
        buttons.add(equipment = new MenuFilterButton(this, equipmentX, y, ItemCategory.EQUIPMENT));
        buttons.add(accessories = new MenuFilterButton(this, accessoriesX, y, ItemCategory.ACCESSORIES));
        buttons.add(misc = new MenuFilterButton(this, miscX, y, ItemCategory.MISC));
    }

    public void onClickFilter(ItemCategory category) {
        currentFilter = category;
        parent.selectedRL = new ResourceLocation("");
        parent.initItems();
    }

    public void render(GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
        Minecraft mc = Minecraft.getInstance();
        RenderSystem.setShaderColor(1, 1, 1, 1);
        ClientUtils.blitScaled(texture, gui, startX, y, 118, 0, 48, 30, 0.5F);
        buttons.forEach(b -> b.render(gui, mouseX, mouseY, partialTicks));
        ClientUtils.blitScaled(texture, gui, endX, y, 166, 0, 48, 30, 0.5F);
        buttons.forEach(b -> renderSelectionBox(gui, (MenuFilterButton) b));
    }

}

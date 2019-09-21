package uk.co.wehavecookies56.kk.client.gui.redesign;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import uk.co.wehavecookies56.kk.api.menu.ItemCategory;
import uk.co.wehavecookies56.kk.client.gui.GuiStock;
import uk.co.wehavecookies56.kk.common.lib.Reference;
import uk.co.wehavecookies56.kk.common.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class GuiFilterBar {

    public GuiButtonFilter all, consumable, tool, building, equipment, misc;
    final int ALL = 0, CONSUMABLE = 1, TOOL = 2, BUILDING = 3, EQUIPMENT = 4, MISC = 5;

    final int endWidth = 24, buttonWidth = 26;

    public ItemCategory currentFilter = null;

    GuiStock parent;

    public List<GuiButton> buttons = new ArrayList<>();

    int x, y;
    int startX;
    int allX;
    int consumableX;
    int toolX;
    int buildingX;
    int equipmentX;
    int miscX;
    int endX;

    public GuiFilterBar(int x, int y, GuiStock parent) {
        this.x = x;
        this.y = y;
        startX = x;
        allX = startX+endWidth;
        consumableX = allX+buttonWidth;
        toolX = consumableX+buttonWidth;
        buildingX = toolX+buttonWidth;
        equipmentX = buildingX+buttonWidth;
        miscX = equipmentX+buttonWidth;
        endX = miscX+buttonWidth;
        this.parent = parent;
    }

    public void drawScreen(Gui gui, int mouseX, int mouseY, float partialTicks) {
        Minecraft mc = Minecraft.getMinecraft();
        mc.renderEngine.bindTexture(new ResourceLocation(Reference.MODID, "textures/gui/menu/menu_button.png"));
        Utils.drawScaledModalRect(gui, startX, y, 118, 0, 48, 30, 0.5F, 0.5F);
        all.drawButton(mc, mouseX, mouseY, partialTicks);
        consumable.drawButton(mc, mouseX, mouseY, partialTicks);
        tool.drawButton(mc, mouseX, mouseY, partialTicks);
        building.drawButton(mc, mouseX, mouseY, partialTicks);
        equipment.drawButton(mc, mouseX, mouseY, partialTicks);
        misc.drawButton(mc, mouseX, mouseY, partialTicks);
        Utils.drawScaledModalRect(gui, endX, y, 166, 0, 48, 30, 0.5F, 0.5F);
        if (all.isMouseOver() || currentFilter == all.category) Utils.drawScaledModalRect(gui,all.x-(1.5F), all.y-(1.5F), 66, 30, 58, 36, 0.5F, 0.5F);
        if (consumable.isMouseOver() || currentFilter == consumable.category) Utils.drawScaledModalRect(gui, consumable.x-(1.5F), consumable.y-(1.5F), 66, 30, 58, 36, 0.5F, 0.5F);
        if (tool.isMouseOver() || currentFilter == tool.category) Utils.drawScaledModalRect(gui, tool.x-(1.5F), tool.y-(1.5F), 66, 30, 58, 36, 0.5F, 0.5F);
        if (building.isMouseOver() || currentFilter == building.category) Utils.drawScaledModalRect(gui, building.x-(1.5F), building.y-(1.5F), 66, 30, 58, 36, 0.5F, 0.5F);
        if (equipment.isMouseOver() || currentFilter == equipment.category) Utils.drawScaledModalRect(gui, equipment.x-(1.5F), equipment.y-(1.5F), 66, 30, 58, 36, 0.5F, 0.5F);
        if (misc.isMouseOver() || currentFilter == misc.category) Utils.drawScaledModalRect(gui, misc.x-(1.5F), misc.y-(1.5F), 66, 30, 58, 36, 0.5F, 0.5F);

    }

    public void actionPerformed(GuiButton button) {
        switch (button.id) {
            case ALL:
                currentFilter = null;
                break;
            case CONSUMABLE:
                currentFilter = ItemCategory.CONSUMABLE;
                break;
            case TOOL:
                currentFilter = ItemCategory.TOOL;
                break;
            case BUILDING:
                currentFilter = ItemCategory.BUILDING;
                break;
            case EQUIPMENT:
                currentFilter = ItemCategory.EQUIPMENT;
                break;
            case MISC:
                currentFilter = ItemCategory.MISC;
            default:
                break;
        }
        parent.initItems();
    }

    public void initGui() {
        buttons.add(all = new GuiButtonFilter(this, ALL, allX, y, "ALL"));
        buttons.add(consumable = new GuiButtonFilter(this, CONSUMABLE, consumableX, y, ItemCategory.CONSUMABLE));
        buttons.add(tool = new GuiButtonFilter(this, TOOL, toolX, y, ItemCategory.TOOL));
        buttons.add(building = new GuiButtonFilter(this, BUILDING, buildingX, y, ItemCategory.BUILDING));
        buttons.add(equipment = new GuiButtonFilter(this, EQUIPMENT, equipmentX, y, ItemCategory.EQUIPMENT));
        buttons.add(misc = new GuiButtonFilter(this, MISC, miscX, y, ItemCategory.MISC));
    }
}

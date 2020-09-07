package online.kingdomkeys.kingdomkeys.client.gui.menu.items;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import online.kingdomkeys.kingdomkeys.api.item.IItemCategory;
import online.kingdomkeys.kingdomkeys.api.item.ItemCategory;
import online.kingdomkeys.kingdomkeys.api.item.ItemCategoryRegistry;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBox;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuFilterBar;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuFilterable;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuScrollBar;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuStockItem;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MenuStockScreen extends MenuFilterable {


    MenuScrollBar scrollBar;
    MenuBox box;
    int itemsX = 100, itemsY = 100, itemWidth = 140, itemHeight = 10;

    public MenuStockScreen() {
        super("Stock", new Color(0,0,255));
        drawSeparately = true;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        drawMenuBackground(mouseX, mouseY, partialTicks);
        box.draw();
        super.render(mouseX, mouseY, partialTicks);
    }
    
    @Override
	protected void renderSelectedData() {
		float tooltipPosX = width * 0.3333F;
        float tooltipPosY = height * 0.8F;

        float iconPosX = width * 0.163F;
        float iconPosY = height * 0.8083F;
        float iconWidth = width * 0.1015F;
        float iconHeight = height * 0.1537F;
        
		Minecraft mc = Minecraft.getInstance();
        RenderHelper.disableStandardItemLighting();
        RenderSystem.pushMatrix();
        {
            RenderSystem.translatef(iconPosX, tooltipPosY, 0);
            RenderSystem.scalef((float) (0.0625F * iconHeight), (float) (0.0625F * iconHeight), 1);
            mc.getItemRenderer().renderItemAndEffectIntoGUI(selected, 0, 0);
        }
        RenderSystem.popMatrix();
        List<ITextComponent> tooltip = selected.getTooltip(mc.player, ITooltipFlag.TooltipFlags.NORMAL);
        for (int i = 0; i < tooltip.size(); i++) {
            drawString(mc.fontRenderer, tooltip.get(i).getUnformattedComponentText(), (int) tooltipPosX + 5, (int) tooltipPosY + (mc.fontRenderer.FONT_HEIGHT * i), 0xFFFFFF);
        }
	}

    @Override
    public void init() {
        float boxPosX = (float) width * 0.1437F;
        float topBarHeight = (float) height * 0.17F;
        float boxWidth = (float) width * 0.7135F;
        float middleHeight = (float) height * 0.6F;
        box = new MenuBox((int) boxPosX, (int) topBarHeight, (int) boxWidth, (int) middleHeight, new Color(4, 4, 68));
        float filterPosX = width * 0.3525F;
        float filterPosY = height * 0.023F;
        filterBar = new MenuFilterBar((int) filterPosX, (int) filterPosY, this);
        filterBar.init();
        initItems();
        filterBar.buttons.forEach(this::addButton);
        //addButton(scrollBar = new MenuScrollBar());
        super.init();
    }

    @Override
    public void initItems() {
        PlayerEntity player = Minecraft.getInstance().player;
        float invPosX = (float) width * 0.1494F;
        float invPosY = (float) height * 0.1851F;
        inventory.clear();
        List<ItemStack> items = new ArrayList<>();
        for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
            if (filterItem(player.inventory.getStackInSlot(i))) {
                items.add(player.inventory.getStackInSlot(i));
            }
        }
        items.sort(Comparator.comparing(Utils::getCategoryForStack).thenComparing(stack -> stack.getDisplayName().getUnformattedComponentText()));
        for (int i = 0; i < items.size(); i += 2) {
        	//Left col
            inventory.add(new MenuStockItem(this,items.get(i), (int) invPosX, (int) invPosY + (i * 7),true));
            if (i + 1 < items.size()) {
            	//Right col
                inventory.add(new MenuStockItem(this, items.get(i + 1), (int) invPosX + inventory.get(i).getWidth(), (int) invPosY + (i * 7),true));
            }
        }
    }

    /**
     * Returns wether the given item should be visible based on the selected filter
     * @param item
     * @return
     */
    public boolean filterItem(ItemStack item) {
        if (ItemStack.areItemStacksEqual(item, ItemStack.EMPTY)) { //If no item
            return false;
        } else {//If there's item
            if (filterBar.currentFilter == null) { //If the filter is null (ALL)
                return true;
            } else {//If there is a filter selected
                if (item.getItem() instanceof IItemCategory) { //If the item has IItemCategory interface (mod items)
                    if (filterBar.currentFilter == ((IItemCategory) (item.getItem())).getCategory()) {
                        return true;
                    } else {
                        return false;
                    }
                } else if (ItemCategoryRegistry.hasCategory(item.getItem())) { //If it's not a mod item but still has category (like blocks, food)
                    if (filterBar.currentFilter == ItemCategoryRegistry.getCategory(item.getItem())) {
                        return true;
                    } else {
                        return false;
                    }
                } else if (filterBar.currentFilter == ItemCategory.MISC) { //If doesn't have anything it's probably because it's a misc (default value for unassigned categories)
                    return true;
                } else {
                    return false;
                }
            }
        }
    }
}

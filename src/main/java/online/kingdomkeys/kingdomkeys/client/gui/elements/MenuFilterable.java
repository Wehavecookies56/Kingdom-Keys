package online.kingdomkeys.kingdomkeys.client.gui.elements;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.item.ItemStack;
import online.kingdomkeys.kingdomkeys.api.item.IItemCategory;
import online.kingdomkeys.kingdomkeys.api.item.ItemCategory;
import online.kingdomkeys.kingdomkeys.api.item.ItemCategoryRegistry;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuScrollBar;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuStockItem;

public abstract class MenuFilterable extends MenuBackground {

    protected List<MenuStockItem> inventory = new ArrayList<>();

    protected MenuFilterBar filterBar;
    MenuScrollBar scrollBar;
    public ItemStack selected = ItemStack.EMPTY;
    int itemsX = 100, itemsY = 100, itemWidth = 140, itemHeight = 10;
    
	protected int page = 0;

    public MenuFilterable(String name, Color color) {
        super(name, color);
        drawSeparately = true;
    }
    
    public void action(ItemStack stack) {
    	selected = stack;
	}
    
    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        if(filterBar != null)
        	filterBar.render(matrixStack, this, mouseX, mouseY, partialTicks);
        //scrollBar.render(mouseX, mouseY, partialTicks);

        if (!ItemStack.areItemStacksEqual(selected, ItemStack.EMPTY)) {
            renderSelectedData(matrixStack, mouseX, mouseY, partialTicks);
        }
        
       // inventory.forEach(i -> i.render(mouseX, mouseY, partialTicks));
        
     //  super.render(mouseX, mouseY, partialTicks);
    }
    
    @Override
    public void init() {
    	super.init();
    }
    
    protected abstract void renderSelectedData(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks);

	public abstract void initItems();
    
    /**
     * Returns whether the given item should be visible based on the selected filter
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
                	return filterBar.currentFilter == ((IItemCategory) (item.getItem())).getCategory();
                } else if (ItemCategoryRegistry.hasCategory(item.getItem())) { //If it's not a mod item but still has category (like blocks, food)
                   	return filterBar.currentFilter == ItemCategoryRegistry.getCategory(item.getItem());
                }
                return filterBar.currentFilter == ItemCategory.MISC; //If doesn't have anything it's probably because it's a misc (default value for unassigned categories)
            }
        }
    }

}

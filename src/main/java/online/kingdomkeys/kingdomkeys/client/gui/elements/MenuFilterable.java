package online.kingdomkeys.kingdomkeys.client.gui.elements;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import online.kingdomkeys.kingdomkeys.api.item.IItemCategory;
import online.kingdomkeys.kingdomkeys.api.item.ItemCategory;
import online.kingdomkeys.kingdomkeys.api.item.ItemCategoryRegistry;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuScrollBar;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuStockItem;

public abstract class MenuFilterable extends MenuBackground {

    protected List<MenuStockItem> inventory = new ArrayList<>();

    protected MenuFilterBar filterBar;
    protected MenuScrollBar scrollBar;
    public ResourceLocation selectedRL = null;
    public ItemStack selectedItemStack;
    int itemsX = 100, itemsY = 100, itemWidth = 140, itemHeight = 10;

    public MenuFilterable(String name, Color color) {
        super(name, color);
        drawSeparately = true;
    }
    
    public void action(ResourceLocation loc, ItemStack stack) {
    	selectedRL = loc;
        selectedItemStack = stack;
	}
    
    @Override
    public void render(@NotNull GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
        if(filterBar != null)
        	filterBar.render(gui, mouseX, mouseY, partialTicks);
        if(scrollBar != null)
            scrollBar.render(gui, mouseX, mouseY, partialTicks);
        if (selectedItemStack == null)
            selectedItemStack = new ItemStack(ForgeRegistries.ITEMS.getValue(selectedRL));
	    if (!ItemStack.matches(selectedItemStack, ItemStack.EMPTY)) {
	        renderSelectedData(gui, mouseX, mouseY, partialTicks);
	    }
        
        
       // inventory.forEach(i -> i.render(mouseX, mouseY, partialTicks));
        
     //  super.render(mouseX, mouseY, partialTicks);
    }
    
    @Override
    public void init() {
    	super.init();
    }
    
    protected abstract void renderSelectedData(GuiGraphics gui, int mouseX, int mouseY, float partialTicks);

	public abstract void initItems();
    
    /**
     * Returns whether the given item should be visible based on the selected filter
     * @param item
     * @return
     */
    public boolean filterItem(ItemStack item) {
        if (ItemStack.matches(item, ItemStack.EMPTY)) { //If no item
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

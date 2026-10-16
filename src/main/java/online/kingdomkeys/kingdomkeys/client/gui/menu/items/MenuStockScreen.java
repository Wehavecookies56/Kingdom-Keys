package online.kingdomkeys.kingdomkeys.client.gui.menu.items;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBox;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuFilterBar;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuFilterable;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuScrollBar;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuStockItem;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSTakeOverflowItem;
import online.kingdomkeys.kingdomkeys.util.Utils;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MenuStockScreen extends MenuFilterable {
    MenuBox box;
	MenuButton back, takeItem;

	private int listedOverflowSize = -1;

    public MenuStockScreen() {
        super(Strings.Gui_Menu_Items_Stock, new Color(0,0,255));
        drawSeparately = true;
        minecraft = Minecraft.getInstance();
    }

    @Override
    public void render(@NotNull GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
        drawMenuBackground(gui, mouseX, mouseY, partialTicks);
		box.renderWidget(gui, mouseX, mouseY, partialTicks);

        if (!inventory.isEmpty()) {
            int listHeight = (inventory.get(inventory.size() - 1).getY() + 20) - inventory.get(0).getY() + 3;
            scrollBar.setContentHeight(listHeight);
        }
        gui.managed = true;
        gui.enableScissor(box.getX()+2,scrollBar.getY()+2,box.getX()+box.getWidth(),scrollBar.getBottom()-5); //Arbitrary number to hide the cut one
        for(Renderable renderable : this.inventory){
            if(renderable instanceof MenuStockItem menuStockItem){
                menuStockItem.active = true;
                renderable.render(gui,mouseX,mouseY,partialTicks);
            } else {
                renderable.render(gui,mouseX,mouseY,partialTicks);
            }
        }
        gui.disableScissor();
        gui.managed = false;
        takeItem.render(gui, mouseX, mouseY, partialTicks);
        back.render(gui, mouseX, mouseY, partialTicks);
        super.render(gui, mouseX, mouseY, partialTicks);

    }

    @Override
    public void tick() {
        super.tick();

        if (takeItem == null) {
            return; // ticked before init built the widgets
        }

        //Checks here in case it changes (or when taking it out)
        int overflowSize = playerData.getOverflow().size();
        if (overflowSize != listedOverflowSize) {
            listedOverflowSize = overflowSize;
            initItems();
        }

        takeItem.active = canTakeItem();
    }

    private boolean canTakeItem() {
        return playerData.checkNextOverflow() != null && Utils.tryToAddItem(player, playerData.checkNextOverflow(), true);
    }
    
    // Stock items are only listed, never selected
    @Override
    public void action(ResourceLocation loc, ItemStack stack) {
    }

    @Override
	protected void renderSelectedData(GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
	}

    @Override
    public void init() {
        float boxPosX = (float) width * 0.1537F;
        float topBarHeight = (float) height * 0.17F;
        float boxWidth = (float) width * 0.7135F;
        float middleHeight = (float) height * 0.6F;
        box = new MenuBox((int) boxPosX, (int) topBarHeight, (int) boxWidth, (int) middleHeight, 1F,  new Color(40, 4, 255));
        float filterPosX = width * 0.3F;
        float filterPosY = height * 0.023F;

        buttonPosX = (float) width * 0.03F;
        buttonPosY = (int) (topBarHeight + 5);

        filterBar = new MenuFilterBar((int) filterPosX, (int) filterPosY, this);
        filterBar.init();

        scrollBar = new MenuScrollBar(box.getX()+box.getWidth()-17,box.getY(),box.getY()+box.getHeight(), box.getHeight(), 0, true);
        addRenderableWidget(scrollBar);

        initItems();
        super.init();
    }

    @Override
    public void initItems() {
        buttonWidth = ((float)width * 0.07F);

        float invPosX = (float) width * 0.1594F;
        float invPosY = (float) height * 0.1851F;
        inventory.clear();
        renderables.clear();
        children().clear();

        filterBar.buttons.forEach(this::addWidget);
        
        addRenderableWidget(takeItem = new MenuButton((int)buttonPosX, buttonPosY, (int)buttonWidth, Component.translatable(Strings.Gui_Menu_Items_Stock_Take).getString(), MenuButton.ButtonType.BUTTON, b -> PacketHandler.sendToServer(new CSTakeOverflowItem())));
        takeItem.active = canTakeItem();

        addRenderableWidget(back = new MenuButton((int)buttonPosX, buttonPosY + 18, (int)buttonWidth, Component.translatable(Strings.Gui_Menu_Back).getString(), MenuButton.ButtonType.BUTTON, b -> minecraft.setScreen(new MenuItemsScreen())));

        List<ItemStack> items = new ArrayList<>();
        List<ItemStack> overflow = playerData.getOverflowForDisplay();
	    for (ItemStack itemStack : overflow) {
		    if (filterItem(itemStack)) {
			    items.add(itemStack);
		    }
	    }

        items.sort(Comparator.comparing(Utils::getCategoryForStack).thenComparing(stack -> stack.getHoverName().getContents().toString()));
        int itemWidth = box.getWidth() / 2 - 10;
        for (int i = 0; i < items.size(); i += 2) {
        	//Left col
            MenuStockItem item = new MenuStockItem(this,items.get(i), (int) invPosX, (int) invPosY + (i * 7), itemWidth, true);
            //item.setBackgroundColor(new Color(30,30,100));
            inventory.add(item);
            if (i + 1 < items.size()) {
            	//Right col
                MenuStockItem item2 = new MenuStockItem(this, items.get(i+1), (int) invPosX + inventory.get(i).getWidth(), (int) invPosY + (i * 7),itemWidth, true);
                //item2.setBackgroundColor(new Color(30,30,100));
                inventory.add(item2);
            }
        }
        inventory.forEach(this::addWidget);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        scrollBar.mouseClicked(mouseX, mouseY, mouseButton);

        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public boolean mouseReleased(double pMouseX, double pMouseY, int pButton) {
        scrollBar.mouseReleased(pMouseX, pMouseY, pButton);

        return super.mouseReleased(pMouseX, pMouseY, pButton);
    }

    @Override
    public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
        scrollBar.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);

        updateScroll();
        return super.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
    }

    public void updateScroll() {
        inventory.forEach(button -> {
            button.offsetY = (int) scrollBar.scrollOffset;
        });
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double deltaX, double deltaY) {
        if(mouseX >= box.getX() && mouseX <= scrollBar.getX()+ scrollBar.getWidth())
            scrollBar.mouseScrolled(mouseX, mouseY, deltaX, deltaY);

        updateScroll();
        return false;
    }
}

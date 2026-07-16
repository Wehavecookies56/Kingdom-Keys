package online.kingdomkeys.kingdomkeys.client.gui.menu.items;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBox;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuFilterBar;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuFilterable;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuScrollBar;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuStockItem;
import online.kingdomkeys.kingdomkeys.item.KeybladeItem;
import online.kingdomkeys.kingdomkeys.item.KeychainItem;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.util.Utils;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MenuStockScreen extends MenuFilterable {
    MenuBox box;
	MenuButton back;

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

        for(Renderable renderable : this.inventory){
            if(renderable instanceof MenuStockItem menuStockItem){
                menuStockItem.active = true;
                gui.enableScissor(box.getX()+2,scrollBar.getY()+2,box.getX()+box.getWidth(),scrollBar.getBottom()-5); //Arbitrary number to hide the cut one
                renderable.render(gui,mouseX,mouseY,partialTicks);
                gui.disableScissor();
            } else {
                renderable.render(gui,mouseX,mouseY,partialTicks);
            }
        }
        back.render(gui, mouseX, mouseY, partialTicks);
        super.render(gui, mouseX, mouseY, partialTicks);

    }
    
    @Override
	protected void renderSelectedData(GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
        PoseStack matrixStack = gui.pose();
        float iconPosX = bottomRightBar.getPosX() + 8;
        float iconPosY = height * 0.8283F;
        float iconWidth = width * 0.1015F;
        float iconHeight = height * 0.1537F;
        
        matrixStack.pushPose();
        {
            matrixStack.translate(iconPosX, iconPosY, 0);
            matrixStack.scale(0.0625F * iconHeight, 0.0625F * iconHeight, 1);
            ClientUtils.drawItemAsIcon(selectedItemStack, matrixStack, 1, -1, 16);
        }
        matrixStack.popPose();

        gui.drawString(minecraft.font, selectedItemStack.getHoverName().getString(), (int) tooltipPosX + 45, (int) tooltipPosY + (0), 0xFFFFFF);

        if (selectedItemStack.getItem() instanceof KeybladeItem || selectedItemStack.getItem() instanceof KeychainItem) {
            KeybladeItem kb = selectedItemStack.getItem() instanceof KeychainItem ? ((KeychainItem) selectedItemStack.getItem()).getKeyblade() : (KeybladeItem) selectedItemStack.getItem();
            if (kb != null && kb.data != null) {
                ClientUtils.drawSplitString(gui, kb.getDesc(), (int) tooltipPosX + 55, (int) tooltipPosY + 10, (int) (width * 0.38F), 0xAAAAAA);
                gui.drawString(minecraft.font, Utils.translateToLocal(Strings.Gui_Menu_Status_Strength) + ": " + kb.getStrength(0), (int) (width * 0.85F), (int) (tooltipPosY), 0xFF0000);
                gui.drawString(minecraft.font, Utils.translateToLocal(Strings.Gui_Menu_Status_Magic) + ": " + kb.getMagic(0), (int) (width * 0.85F), (int) tooltipPosY + 10, 0x4444FF);
            } else {
                drawTooltipText(gui, selectedItemStack);
            }
        } else {
            drawTooltipText(gui, selectedItemStack);
        }
        
	}

    public void drawTooltipText(GuiGraphics gui, ItemStack selectedItemstack) {
        List<Component> tooltip = selectedItemstack.getTooltipLines(Item.TooltipContext.of(minecraft.level), minecraft.player, TooltipFlag.Default.NORMAL);
        for (int i = 1; i < Math.min(tooltip.size(), 3); i++) {
            gui.drawString(minecraft.font, tooltip.get(i).getString(), (int) tooltipPosX + 60, (int) tooltipPosY + (minecraft.font.lineHeight * i) + 5, 0xFFFFFF);
        }
        if (tooltip.size() > 3) {
            gui.drawString(minecraft.font, "...", (int) tooltipPosX + 60, (int) tooltipPosY + (minecraft.font.lineHeight * 3) + 5, 0xFFFFFF);
        }
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

        Player player = minecraft.player;
        float invPosX = (float) width * 0.1594F;
        float invPosY = (float) height * 0.1851F;
        inventory.clear();
        renderables.clear();
        children().clear();

        filterBar.buttons.forEach(this::addWidget);
        
        addRenderableWidget(back = new MenuButton((int)buttonPosX, buttonPosY, (int)buttonWidth, Component.translatable(Strings.Gui_Menu_Back).getString(), MenuButton.ButtonType.BUTTON, b -> minecraft.setScreen(new MenuItemsScreen())));

        List<ItemStack> items = new ArrayList<>();
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            if (filterItem(player.getInventory().getItem(i))) {
                items.add(player.getInventory().getItem(i));
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
        List<ItemStack> overflow = playerData.getOverflowForDisplay();
        for (int i = items.size(); i < overflow.size(); i += 2) {
            int j = i + items.size();
            MenuStockItem item = new MenuStockItem(this, overflow.get(i), (int) invPosX, (int) invPosY + (j * 7), itemWidth, true);
            inventory.add(item);
            if (i + 1 < overflow.size()) {
                MenuStockItem item2 = new MenuStockItem(this, overflow.get(i+1), (int) invPosX + inventory.get(i).getWidth(), (int) invPosY + (j * 7),itemWidth, true);
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

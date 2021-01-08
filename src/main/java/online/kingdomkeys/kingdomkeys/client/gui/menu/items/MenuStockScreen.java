package online.kingdomkeys.kingdomkeys.client.gui.menu.items;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
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

public class MenuStockScreen extends MenuFilterable {


    MenuScrollBar scrollBar;
    MenuBox box;
    int itemsX = 100, itemsY = 100, itemWidth = 140, itemHeight = 10;
	MenuButton back;

    public MenuStockScreen() {
        super(Strings.Gui_Menu_Items_Stock, new Color(0,0,255));
        drawSeparately = true;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        drawMenuBackground(matrixStack, mouseX, mouseY, partialTicks);
        box.draw(matrixStack);

        super.render(matrixStack, mouseX, mouseY, partialTicks);
        inventory.forEach(i -> i.render(matrixStack, mouseX, mouseY, partialTicks));
        back.render(matrixStack, mouseX, mouseY, partialTicks);
    }
    
    @Override
	protected void renderSelectedData(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		float tooltipPosX = width * 0.3333F;
        float tooltipPosY = height * 0.8F;

        float iconPosX = width * 0.335F;
        float iconPosY = height * 0.8283F;
        float iconWidth = width * 0.1015F;
        float iconHeight = height * 0.1537F;
        
		Minecraft mc = Minecraft.getInstance();
        RenderHelper.disableStandardItemLighting();
        matrixStack.push();
        {
            matrixStack.translate(iconPosX, iconPosY, 0);
            matrixStack.scale((float) (0.0625F * iconHeight), (float) (0.0625F * iconHeight), 1);
            mc.getItemRenderer().renderItemAndEffectIntoGUI(selected, 0, 0);
        }
        matrixStack.pop();
        
        drawString(matrixStack, mc.fontRenderer, selected.getDisplayName().getString(), (int) tooltipPosX + 50, (int) tooltipPosY + (mc.fontRenderer.FONT_HEIGHT * 0) + 5, 0xFFFFFF);

        if(selected.getItem() instanceof KeybladeItem || selected.getItem() instanceof KeychainItem) {
        	KeybladeItem kb = selected.getItem() instanceof KeychainItem ? ((KeychainItem) selected.getItem()).getKeyblade() : (KeybladeItem) selected.getItem();
        	
        	Utils.drawSplitString(font, kb.getDescription(), (int) tooltipPosX + 60, (int) tooltipPosY + 15, (int) (width * 0.38F), 0xAAAAAA);
			drawString(matrixStack, minecraft.fontRenderer, Utils.translateToLocal(Strings.Gui_Menu_Status_Strength)+": "+kb.getStrength(0), (int) (width * 0.85F), (int) (tooltipPosY + 5), 0xFF0000);
			drawString(matrixStack, minecraft.fontRenderer, Utils.translateToLocal(Strings.Gui_Menu_Status_Magic)+": "+kb.getMagic(0),  (int) (width * 0.85F), (int) tooltipPosY + 15, 0x4444FF);
        } else {
        	List<ITextComponent> tooltip = selected.getTooltip(mc.player, ITooltipFlag.TooltipFlags.NORMAL);
            for (int i = 0; i < tooltip.size(); i++) {
                drawString(matrixStack, mc.fontRenderer, tooltip.get(i).getUnformattedComponentText(), (int) tooltipPosX + 60, (int) tooltipPosY + (mc.fontRenderer.FONT_HEIGHT * i) + 5, 0xFFFFFF);
            }
        }
        
	}

    @Override
    public void init() {

        buttonWidth = ((float)width * 0.07F);

        float boxPosX = (float) width * 0.1537F;
        float topBarHeight = (float) height * 0.17F;
        float boxWidth = (float) width * 0.7135F;
        float middleHeight = (float) height * 0.6F;
        box = new MenuBox((int) boxPosX, (int) topBarHeight, (int) boxWidth, (int) middleHeight, new Color(4, 4, 68));
        float filterPosX = width * 0.3F;
        float filterPosY = height * 0.023F;

        buttonPosX = (float) width * 0.03F;
        buttonPosY = (int) (topBarHeight + 5);

        filterBar = new MenuFilterBar((int) filterPosX, (int) filterPosY, this);
        filterBar.init();
        initItems();
        //addButton(scrollBar = new MenuScrollBar());

        super.init();
    }

    @Override
    public void initItems() {
        PlayerEntity player = Minecraft.getInstance().player;
        float invPosX = (float) width * 0.1594F;
        float invPosY = (float) height * 0.1851F;
        inventory.clear();
        buttons.clear();
        children.clear();

        filterBar.buttons.forEach(this::addButton);
        
        addButton(back = new MenuButton((int)buttonPosX, buttonPosY, (int)buttonWidth, new TranslationTextComponent(Strings.Gui_Menu_Back).getString(), MenuButton.ButtonType.BUTTON, b -> minecraft.displayGuiScreen(new MenuItemsScreen())));

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
        inventory.forEach(this::addButton);

    }
}

package online.kingdomkeys.kingdomkeys.client.gui.menu.items;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraftforge.registries.ForgeRegistries;
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

public class MenuStockScreen extends MenuFilterable {


    MenuScrollBar scrollBar;
    MenuBox box;
    int itemsX = 100, itemsY = 100, itemWidth = 140, itemHeight = 10;
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

        super.render(gui, mouseX, mouseY, partialTicks);
        inventory.forEach(i -> i.render(gui, mouseX, mouseY, partialTicks));
        back.render(gui, mouseX, mouseY, partialTicks);
    }
    
    @Override
	protected void renderSelectedData(GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
        PoseStack matrixStack = gui.pose();
        float iconPosX = width * 0.335F;
        float iconPosY = height * 0.8283F;
        float iconWidth = width * 0.1015F;
        float iconHeight = height * 0.1537F;
        
        ItemStack selectedItemstack = new ItemStack(ForgeRegistries.ITEMS.getValue(selectedRL));
        matrixStack.pushPose();
        {
        	matrixStack.translate(iconPosX, iconPosY, 0);
        	matrixStack.scale((float) (0.0625F * iconHeight), (float) (0.0625F * iconHeight), 1);
			ClientUtils.drawItemAsIcon(selectedItemstack, matrixStack, 1, -1, 16);
        }
        matrixStack.popPose();
        
        gui.drawString(minecraft.font, selectedItemstack.getHoverName().getString(), (int) tooltipPosX + 45, (int) tooltipPosY + (minecraft.font.lineHeight * 0), 0xFFFFFF);

        if(selectedItemstack.getItem() instanceof KeybladeItem || selectedItemstack.getItem() instanceof KeychainItem) {
        	KeybladeItem kb = selectedItemstack.getItem() instanceof KeychainItem ? ((KeychainItem) selectedItemstack.getItem()).getKeyblade() : (KeybladeItem) selectedItemstack.getItem();

            ClientUtils.drawSplitString(gui, kb.getDesc(), (int) tooltipPosX + 55, (int) tooltipPosY + 10, (int) (width * 0.38F), 0xAAAAAA);
			gui.drawString(minecraft.font, Utils.translateToLocal(Strings.Gui_Menu_Status_Strength)+": "+kb.getStrength(0), (int) (width * 0.85F), (int) (tooltipPosY), 0xFF0000);
			gui.drawString(minecraft.font, Utils.translateToLocal(Strings.Gui_Menu_Status_Magic)+": "+kb.getMagic(0),  (int) (width * 0.85F), (int) tooltipPosY + 10, 0x4444FF);
        } else {
        	List<Component> tooltip = selectedItemstack.getTooltipLines(minecraft.player, TooltipFlag.Default.NORMAL);
            for (int i = 0; i < tooltip.size(); i++) {
                gui.drawString(minecraft.font, tooltip.get(i).getContents().toString(), (int) tooltipPosX + 60, (int) tooltipPosY + (minecraft.font.lineHeight * i) + 5, 0xFFFFFF);
            }
        }
        
	}

    @Override
    public void init() {
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
        for (int i = 0; i < items.size(); i += 2) {
        	//Left col
            inventory.add(new MenuStockItem(this,items.get(i), (int) invPosX, (int) invPosY + (i * 7), (int)(width * 0.3255F), true));
            if (i + 1 < items.size()) {
            	//Right col
                inventory.add(new MenuStockItem(this, items.get(i+1), (int) invPosX + inventory.get(i).getWidth(), (int) invPosY + (i * 7),(int)(width * 0.3255F), true));
            }
        }
        inventory.forEach(this::addWidget);

    }
}

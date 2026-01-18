package online.kingdomkeys.kingdomkeys.client.gui.synthesis;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.ability.Ability;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBox;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuFilterBar;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuFilterable;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuFilterableIndexed;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuScrollBar;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuStockItem;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuStockItemIndexed;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.item.KKAccessoryItem;
import online.kingdomkeys.kingdomkeys.item.KKArmorItem;
import online.kingdomkeys.kingdomkeys.item.KeybladeItem;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSCloseMoogleGUI;
import online.kingdomkeys.kingdomkeys.network.cts.CSShopSell;
import online.kingdomkeys.kingdomkeys.synthesis.shop.sell.SellItem;
import online.kingdomkeys.kingdomkeys.synthesis.shop.sell.SellList;
import online.kingdomkeys.kingdomkeys.synthesis.shop.sell.SellListRegistry;
import online.kingdomkeys.kingdomkeys.util.Utils;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.*;
import java.util.List;

public class SellScreen extends MenuFilterableIndexed {
	MenuBox boxL, boxM;

	MenuButton sell;
	private MenuButton buy, back;
    EditBox amountBox;

	SynthesisScreen parent;

	public SellScreen(PlayerData playerData, SynthesisScreen parent) {
		super(Strings.Gui_Shop_Main_Title, new Color(0, 0, 255));
		drawSeparately = true;
		this.parent = parent;
		parent.playerData = playerData;
	}

	public SellScreen(PlayerData playerData, String nbt, SynthesisScreen parent) {
		this(playerData, parent);
	}

    public SellScreen(PlayerData playerData, String inv, String name, int moogle) {
        this(playerData, new SynthesisScreen(playerData, inv, name, moogle));
    }

	public SellList getSellList(){
		return SellListRegistry.getInstance().getRegistry().get(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "sell"));
	}

    @Override
    public void action(int index) {
        super.action(index);
        int amount = minecraft.player.getInventory().getItem(index).getCount();
        amountBox.setValue(""+amount);
    }

	protected void action(String string) {
		switch (string) {
		case "sell":
            if(getTextBoxAmount() > 0){
                ItemStack item = minecraft.player.getInventory().getItem(selectedIndex);
                if(item != null && item.getCount() >= getTextBoxAmount()) {
                    minecraft.player.getInventory().getItem(selectedIndex).setCount(minecraft.player.getInventory().getItem(selectedIndex).getCount() - getTextBoxAmount());
                    PacketHandler.sendToServer(new CSShopSell(selectedIndex, Integer.parseInt(amountBox.getValue()), parent.invFile, parent.name == null ? "" : parent.name, parent.moogle));
                }
                minecraft.level.playSound(minecraft.player, minecraft.player.blockPosition(), ModSounds.itemget.get(), SoundSource.MASTER, 1.0f, 1.0f);
            }
            break;
		}
	}
	
	@Override
	public void init() {
		float boxPosX = (float) width * 0.2F;
		float topBarHeight = (float) height * 0.17F;
		float boxWidth = (float) width * 0.3F;
		float middleHeight = (float) height * 0.6F;
		boxL = new MenuBox((int) boxPosX, (int) topBarHeight, (int) boxWidth, (int) middleHeight,1F, new Color(4, 4, 150));
		boxM = new MenuBox((int) boxPosX + (int) boxWidth, (int) topBarHeight, (int) (boxWidth*0.7F), (int) middleHeight, 1F,new Color(4, 4, 150));		int scrollTop = (int) topBarHeight;
		int scrollBot = (int) (scrollTop + middleHeight);
		float filterPosX = width * 0.3F;
		float filterPosY = height * 0.02F;
		filterBar = new MenuFilterBar((int) filterPosX, (int) filterPosY, this);
		filterBar.init();
		scrollBar = new MenuScrollBar((int) (boxPosX + boxWidth - 17), scrollTop, scrollBot, (int) middleHeight, 0);
		addRenderableWidget(scrollBar);
		initItems();
		buttonPosX -= 10;
		buttonWidth = ((float)width * 0.07F);
		super.init();
	}

	@Override
	public void initItems() {
		float invPosX = (float) boxL.getX()+4;
		float invPosY = (float) height * 0.1851F;
		inventory.clear();
		children().clear();
		renderables.clear();
		filterBar.buttons.forEach(this::addWidget);
		
		SellList sellList = getSellList();

		int c = 0;
		for (int i = 0; i < minecraft.player.getInventory().getContainerSize(); i++) {
            ItemStack stack = minecraft.player.getInventory().getItem(i);
            if(stack != null){
                for(int j=0;j<getSellList().getList().size();j++) {
                    SellItem sellItem = sellList.getList().get(j);
                    if (stack.getItem() == sellItem.getResult() && filterItem(stack)) {
						MenuStockItemIndexed item = new MenuStockItemIndexed(this, i, stack, (int) invPosX, (int) invPosY + (c++ * 14), boxL.getWidth() - scrollBar.getWidth() - 6, true);
						item.setBackgroundColor(new Color(10, 10, 80));
						inventory.add(item);
					}
                }
            }
		}
		
		inventory.forEach(this::addWidget);

		super.init();


		addRenderableWidget(buy = new MenuButton((int)this.buttonPosX, this.buttonPosY, (int)(buttonWidth+15)/2, Component.translatable(Strings.Gui_Shop_Buy).getString(), MenuButton.ButtonType.BUTTON, b -> minecraft.setScreen(new ShopScreen(parent.playerData, parent))));
		addRenderableWidget(back = new MenuButton((int)this.buttonPosX, this.buttonPosY+18, (int)(buttonWidth+15)/2, Component.translatable(Strings.Gui_Menu_Back).getString(), MenuButton.ButtonType.BUTTON, b -> minecraft.setScreen(new SynthesisScreen(parent.playerData, parent.invFile, parent.name, parent.moogle))));
		addRenderableWidget(amountBox = new EditBox(minecraft.font, boxM.getX()+5, (int) (topBarHeight + middleHeight - 22), minecraft.font.width("#####"), 16, Component.translatable("test")) {
			@Override
			public boolean charTyped(char c, int i) {
				if (Utils.isNumber(c)) {
					String text = new StringBuilder(this.getValue()).insert(this.getCursorPosition(), c).toString();
					if (Integer.parseInt(text) > selectedItemStack.getCount()) {
						return false;
					}
				} else {
					return false;
				}
				return super.charTyped(c, i);
			}
		});

        if(selectedItemStack != null) {
            amountBox.setValue(""+selectedItemStack.getCount());
        }
        sell = new MenuButton(amountBox.getX() + amountBox.getWidth(), amountBox.getY()-2,boxM.getWidth() - amountBox.getWidth()*2, Strings.Gui_Shop_Sell, MenuButton.ButtonType.ROUNDBUTTON,(e) -> {
            action("sell");
        });
        sell.setCenterText(true);
        addRenderableWidget(sell);
    }

    public int getTextBoxAmount(){
        try {
            Integer.parseInt(amountBox.getValue());
            return Integer.parseInt(amountBox.getValue());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
	@Override
	public void render(@NotNull GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
		drawMenuBackground(gui, mouseX, mouseY, partialTicks);
		boxL.renderWidget(gui, mouseX, mouseY, partialTicks);
		boxM.renderWidget(gui, mouseX, mouseY, partialTicks);
		super.render(gui, mouseX, mouseY, partialTicks);

		if (!inventory.isEmpty()) {
			int listHeight = (inventory.get(inventory.size() - 1).getY() + 20) - inventory.get(0).getY() + 3;
			scrollBar.setContentHeight(listHeight);

		}
		if (selectedItemStack != ItemStack.EMPTY) {
            List<SellItem> list = getSellList().getList();
            SellItem item = null;
			for(SellItem sellItem : list) {
				Item it = sellItem.getResult();

                if(ItemStack.isSameItem(new ItemStack(it), selectedItemStack)) {
					item = sellItem;
					break;
				}
				
			}			
			if(item != null) {
				sell.visible = true;
			}
			sell.visible = item != null;
		} else {
			sell.visible = false;
		}
        amountBox.visible = sell.visible;

        for(Renderable renderable : this.inventory){
			if(renderable instanceof MenuStockItem menuStockItem){
				menuStockItem.active = true;
				gui.enableScissor(boxL.getX()+2,scrollBar.getY()+2,boxL.getX()+boxL.getWidth(),scrollBar.getBottom()-5); //Arbitrary number to hide the cut one
				renderable.render(gui,mouseX,mouseY,partialTicks);
				gui.disableScissor();
			} else {
				renderable.render(gui,mouseX,mouseY,partialTicks);
			}
		}
		sell.render(gui, mouseX,  mouseY,  partialTicks);
        buy.render(gui, mouseX,  mouseY,  partialTicks);
		back.render(gui, mouseX, mouseY, partialTicks);
        amountBox.render(gui, mouseX, mouseY, partialTicks);
	}

	@Override
	protected void renderSelectedData(GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
		PoseStack matrixStack = gui.pose();
		float tooltipPosX = width * 0.3333F;
		float tooltipPosY = height * 0.8F;

		float iconPosY = boxM.getPosY() + 25;

		matrixStack.pushPose();
		{
			double offset = boxM.getWidth()*0.1F;
			matrixStack.translate(boxM.getX() + offset/2, iconPosY, 1);
			
			List<SellItem> list = getSellList().getList();
            SellItem item = null;
            //iterate through the list made from the file
			for(SellItem sellItem : list) {
				Item it = sellItem.getResult();
				if(ItemStack.isSameItem(new ItemStack(it,1), selectedItemStack)) {
					item = sellItem;
					break;
				}
				
			}
			if(item != null) {
				gui.drawString(minecraft.font, Utils.translateToLocal(Strings.Gui_Shop_Buy_Price)+" ", 2, -20, Color.yellow.getRGB());
				String line = Utils.getFormattedNumber(item.getPrice() * getTextBoxAmount())+" "+Utils.translateToLocal(Strings.Gui_Menu_Main_Munny);
				gui.drawString(minecraft.font, line, boxM.getWidth() - minecraft.font.width(line) - 10, -20, Color.GREEN.getRGB());
				
				matrixStack.pushPose();
				{
					float size = 80;
					matrixStack.translate(boxM.getWidth()*0.7F / 2,boxM.getHeight() * 0.6F - size / 2,0);
					ClientUtils.drawItemAsIcon(selectedItemStack, matrixStack, 0, -30, (int) size);
				}
				matrixStack.popPose();
			}
		}
		matrixStack.popPose();

		if (selectedItemStack != null && selectedItemStack.getItem() instanceof KeybladeItem || selectedItemStack.getItem() instanceof KKAccessoryItem || selectedItemStack.getItem() instanceof KKArmorItem) {
			String desc = "";
			String ability = "";
			if(selectedItemStack.getItem() instanceof KeybladeItem kb) {
				desc = kb.getDesc();
				ability = kb.data.getLevelAbility(0);
			} else if(selectedItemStack.getItem() instanceof KKAccessoryItem accessory) {
				ability = !accessory.getAbilities().isEmpty() ? accessory.getAbilities().getFirst() : null;
			}

			matrixStack.pushPose();
			{
				matrixStack.translate(boxM.getX()+20, height*0.58, 1);
				List<Component> stats = Utils.getResistancesStats(selectedItemStack);

				float scale = stats.size() > 4 ? 1F-(stats.size()-4)*0.25F: 1F;
				matrixStack.scale(scale, scale, scale);

				int offset = -15;
				for(int i=0;i<stats.size();i++){
					Component c = stats.get(i);
					gui.drawString(minecraft.font, c, 0, offset+(10*i), 0x4444FF);
				}

				if(ability != null) {
					Ability a = ModAbilities.registry.get(ResourceLocation.parse(ability));
					if(a != null) {
						String abilityName = Utils.translateToLocal(a.getTranslationKey());
						gui.drawString(minecraft.font, abilityName, -20 + (boxM.getWidth()/2) - (minecraft.font.width(abilityName)/2), (stats.size()-1)*10, 0xFFAA44);
					}
				}
			}
			matrixStack.popPose();
			
			if(!desc.equals("")) {
				matrixStack.pushPose();
				{
					String text = Utils.translateToLocal(selectedItemStack.getDescriptionId());
					gui.drawString(minecraft.font, text, (int)(tooltipPosX + 5), (int) (tooltipPosY)+5, 0xFF9900);
					ClientUtils.drawSplitString(gui, desc, (int) tooltipPosX + 5, (int) tooltipPosY + 5 + minecraft.font.lineHeight, (int) (width * 0.6F), 0xFFFFFF);
				}
				matrixStack.popPose();
			}
		}
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	@Override
	public void onClose() {
		if (parent.moogle != -1) {
			PacketHandler.sendToServer(new CSCloseMoogleGUI(parent.moogle));
		}
		super.onClose();
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
		scrollBar.mouseScrolled(mouseX, mouseY, deltaX, deltaY);
		updateScroll();
		return false;
	}

}

package online.kingdomkeys.kingdomkeys.client.gui.synthesis;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
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
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuScrollBar;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuStockItem;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.item.KKAccessoryItem;
import online.kingdomkeys.kingdomkeys.item.KKArmorItem;
import online.kingdomkeys.kingdomkeys.item.KeybladeItem;
import online.kingdomkeys.kingdomkeys.item.KeychainItem;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSCloseMoogleGUI;
import online.kingdomkeys.kingdomkeys.network.cts.CSShopBuy;
import online.kingdomkeys.kingdomkeys.synthesis.shop.ShopItem;
import online.kingdomkeys.kingdomkeys.synthesis.shop.ShopList;
import online.kingdomkeys.kingdomkeys.synthesis.shop.ShopListRegistry;
import online.kingdomkeys.kingdomkeys.util.Utils;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class ShopScreen extends MenuFilterable {
	MenuBox boxL, boxM;

	MenuButton create;
	private MenuButton sell, back;
	
	SynthesisScreen parent;

	public ShopScreen(PlayerData playerData, SynthesisScreen parent) {
		super(Strings.Gui_Shop_Main_Title, new Color(255, 0, 0));
		drawSeparately = true;
		this.parent = parent;
		this.playerData = playerData;
	}
	
	public ShopScreen(PlayerData playerData, String nbt, SynthesisScreen parent) {
		this(playerData, parent);
	}

	public ShopList getShopList(){
		return ShopListRegistry.getInstance().getRegistry().get(ResourceLocation.parse(parent.invFile));
	}

	protected void action(String string) {
		switch (string) {
		case "create":
			PacketHandler.sendToServer(new CSShopBuy(ResourceLocation.parse(parent.invFile), selectedItemStack));
			minecraft.level.playSound(minecraft.player, minecraft.player.blockPosition(), ModSounds.buy.get(), SoundSource.MASTER, 1.0f, 1.0f);
			break;
		}
	}
	
	@Override
	public void init() {
		float boxPosX = (float) width * 0.2F;
		float topBarHeight = (float) height * 0.17F;
		float boxWidth = (float) width * 0.3F;
		float middleHeight = (float) height * 0.6F;
		boxL = new MenuBox((int) boxPosX, (int) topBarHeight, (int) boxWidth, (int) middleHeight,1F, new Color(100, 4, 4));
		boxM = new MenuBox((int) boxPosX + (int) boxWidth, (int) topBarHeight, (int) (boxWidth*0.7F), (int) middleHeight, 1F,new Color(100, 4, 4));		int scrollTop = (int) topBarHeight;
		int scrollBot = (int) (scrollTop + middleHeight);
		float filterPosX = width * 0.3F;
		float filterPosY = height * 0.02F;
		filterBar = new MenuFilterBar((int) filterPosX, (int) filterPosY, this);
		filterBar.init();
		scrollBar = new MenuScrollBar((int) (boxPosX + boxWidth - 17), scrollTop, scrollBot, (int) middleHeight, 0, true);
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

		boolean requireTier = ModConfigs.SERVER.requireSynthTierShop.get();
		int synthLevel = playerData.getSynthLevel();
		List<ShopItem> list = getShopList().getList().stream()
				.filter(shopItem -> {
					boolean tierOk = !requireTier || shopItem.getTier() <= synthLevel;
					boolean matsOk = shopItem.getMatReq() <= playerData.getTotalMaterialAmount(shopItem.getResult());

					return shopItem.requireAll() ? (tierOk && matsOk) : (tierOk || matsOk);
				}).toList();

		List<ResourceLocation> items = new ArrayList<>();
		for (ShopItem shopItem : list) {
			ResourceLocation itemName = null;
			if (shopItem != null) {
				ResourceLocation recipeRL = Utils.getItemRegistryName(shopItem.getResult());
				ItemStack stack = new ItemStack(shopItem.getResult());

				if (shopItem.getResult() instanceof KeychainItem)
					stack = new ItemStack(((KeychainItem) shopItem.getResult()).getKeyblade());

				if (filterItem(stack)) {
					items.add(recipeRL);
				}
			} else {
				KingdomKeys.LOGGER.error(itemName + " is not a valid recipe, check it");
			}
		}


		items.sort(Comparator.comparing(Utils::getCategoryForShop).thenComparing(stackRL -> new ItemStack(BuiltInRegistries.ITEM.get(stackRL)).getHoverName().getContents().toString()));

		for (int i = 0; i < items.size(); i++) {
			ItemStack itemStack = new ItemStack(BuiltInRegistries.ITEM.get(items.get(i)));
			if(itemStack != null && itemStack.getItem() instanceof KeychainItem) {
				itemStack = new ItemStack(((KeychainItem) itemStack.getItem()).getKeyblade());
			}
			MenuStockItem item = new MenuStockItem(this, items.get(i), itemStack, (int) invPosX, (int) invPosY + (i * 14), boxL.getWidth()-scrollBar.getWidth()-6, false);
			item.setBackgroundColor(new Color(80,10,10));
			inventory.add(item);
		}
		
		inventory.forEach(this::addWidget);
		
		super.init();

		create = new MenuButton(boxM.getX()+boxM.getWidth()/2 - (int)(buttonWidth+22)/2, (int) (height * 0.67),(int)buttonWidth, Strings.Gui_Shop_Buy, MenuButton.ButtonType.ROUNDBUTTON,(e) -> {
			action("create");
		});
		create.setCenterText(true);
		addRenderableWidget(create);

        addRenderableWidget(sell = new MenuButton((int)this.buttonPosX, this.buttonPosY, (int)(buttonWidth+15)/2, Component.translatable(Strings.Gui_Shop_Sell).getString(), MenuButton.ButtonType.BUTTON, b -> minecraft.setScreen(new SellScreen(playerData, parent))));
		addRenderableWidget(back = new MenuButton((int)this.buttonPosX, this.buttonPosY+18, (int)(buttonWidth+15)/2, Component.translatable(Strings.Gui_Menu_Back).getString(), MenuButton.ButtonType.BUTTON, b -> minecraft.setScreen(new SynthesisScreen(playerData, parent.invFile, parent.name, parent.moogle))));
	}

	@Override
	public void render(@NotNull GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
		drawMenuBackground(gui, mouseX, mouseY, partialTicks);
		boxL.renderWidget(gui, mouseX, mouseY, partialTicks);
		boxM.renderWidget(gui, mouseX, mouseY, partialTicks);
		super.render(gui, mouseX, mouseY, partialTicks);

		if(inventory.isEmpty())
			return;

		int listHeight = (inventory.get(inventory.size()-1).getY()+20) - inventory.get(0).getY() + 3;
		scrollBar.setContentHeight(listHeight);

		if (selectedItemStack != ItemStack.EMPTY) {
			boolean enoughMunny = false;
			boolean enoughTier = false;
			List<ShopItem> list = ShopListRegistry.getInstance().getRegistry().get(ResourceLocation.parse(parent.invFile)).getList();
			ShopItem item = null;
			for(ShopItem shopItem : list) {
				Item it = shopItem.getResult();

				if (it instanceof KeychainItem) {
					it = ((KeychainItem) it).getKeyblade();
				}

				if (ItemStack.isSameItem(new ItemStack(it, shopItem.getAmount()), selectedItemStack)) {
					item = shopItem;
					break;
				}
			}
			if (item != null) {
				enoughMunny = playerData.getMunny() >= item.getCost();
				enoughTier = !ModConfigs.SERVER.requireSynthTierShop.get() || playerData.getSynthLevel() >= item.getTier();
				boolean matsOk = item.getMatReq() <= playerData.getTotalMaterialAmount(item.getResult());

				enoughTier = item.requireAll() ? (enoughTier && matsOk) : (enoughTier || matsOk);
				create.visible = true;
				create.active = enoughMunny && enoughTier && matsOk;

				if (minecraft.player.getInventory().getFreeSlot() == -1) {
					create.setMessage(Component.translatable(Strings.Gui_Shop_NoSpace));
				}
			}
			create.visible = item != null;
		} else {
			create.visible = false;
		}

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
		create.render(gui, mouseX,  mouseY,  partialTicks);
        sell.render(gui, mouseX, mouseY, partialTicks);
		back.render(gui, mouseX, mouseY, partialTicks);
	}

	@Override
	protected void renderSelectedData(GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
		PoseStack matrixStack = gui.pose();
		float tooltipPosX = width * 0.3333F;
		float tooltipPosY = height * 0.8F;

		float iconPosY = boxM.getPosY() + 25;

		PlayerData playerData = PlayerData.get(minecraft.player);

		matrixStack.pushPose();
		{
			double offset = boxM.getWidth() * 0.1F;
			matrixStack.translate(boxM.getX() + offset / 2, iconPosY, 1);
			
			List<ShopItem> list = ShopListRegistry.getInstance().getRegistry().get(ResourceLocation.parse(parent.invFile)).getList();
			ShopItem item = null;
			for(ShopItem shopItem : list) {
				Item it = shopItem.getResult();

				if(it instanceof KeychainItem) {
					it = ((KeychainItem)it).getKeyblade();
				}

				if(ItemStack.isSameItem(new ItemStack(it, shopItem.getAmount()), selectedItemStack)) {
					item = shopItem;
					break;
				}

			}
			if(item != null) {
				gui.drawString(minecraft.font, Utils.translateToLocal(Strings.Gui_Shop_Buy_Cost)+" ", 2, -20, Color.yellow.getRGB());
				String line = Utils.getFormattedNumber(item.getCost())+" "+Utils.translateToLocal(Strings.Gui_Menu_Main_Munny);
				gui.drawString(minecraft.font, line, boxM.getWidth() - minecraft.font.width(line) - 10, -20, item.getCost() > playerData.getMunny() ? Color.RED.getRGB() : Color.GREEN.getRGB());

				/*if(ModConfigs.SERVER.requireSynthTierShop.get()) {
					gui.drawString(minecraft.font, Utils.translateToLocal(Strings.Gui_Shop_Tier) + " ", 2, -10, Color.yellow.getRGB());
					line = Utils.getTierFromInt(item.getTier()) + " - " + (10 + item.getTier() * 2) + " " + Utils.translateToLocal(Strings.Gui_Synthesis_Exp);
					gui.drawString(minecraft.font, line, boxM.getWidth() - minecraft.font.width(line) - 10, -10, item.getTier() > playerData.getSynthLevel() ? Color.RED.getRGB() : Color.GREEN.getRGB());
				}*/
				
				matrixStack.pushPose();
				{
					float size = 80;
					matrixStack.translate(boxM.getWidth()*0.7F / 2,boxM.getHeight()*0.6F - size / 2,0);
					ClientUtils.drawItemAsIcon(selectedItemStack, matrixStack, 0, -30, (int)size);
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

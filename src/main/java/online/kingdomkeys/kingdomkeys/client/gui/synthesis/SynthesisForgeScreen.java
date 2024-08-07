package online.kingdomkeys.kingdomkeys.client.gui.synthesis;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import online.kingdomkeys.kingdomkeys.ability.Ability;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.client.gui.GuiHelper;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBox;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuFilterable;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuScrollBar;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuStockItem;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.item.KeybladeItem;
import online.kingdomkeys.kingdomkeys.item.KeychainItem;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSCloseMoogleGUI;
import online.kingdomkeys.kingdomkeys.network.cts.CSLevelUpKeybladePacket;
import online.kingdomkeys.kingdomkeys.synthesis.material.Material;
import online.kingdomkeys.kingdomkeys.util.Utils;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.Map.Entry;

public class SynthesisForgeScreen extends MenuFilterable {
 
	int ticks=0;
	// MenuFilterBar filterBar;
	MenuBox boxL, boxM, boxR;
	int itemsX = 100, itemsY = 100, itemWidth = 140, itemHeight = 10;

	Button upgrade;
	int itemsPerPage = 10;
	private MenuButton back;
	SynthesisScreen parent;

	public SynthesisForgeScreen(SynthesisScreen parent) {
		super(Strings.Gui_Synthesis_Forge_Title, new Color(0, 255, 0));
		drawSeparately = true;
		this.parent = parent;
	}

	protected void action(String string) {
		switch (string) {
		case "upgrade":
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);
			minecraft.level.playSound(minecraft.player, minecraft.player.blockPosition(), ModSounds.itemget.get(), SoundSource.MASTER, 1.0f, 1.0f);

			ItemStack stack = selectedItemStack.copy();
			KeychainItem kcItem = (KeychainItem) stack.getItem();
			KeybladeItem item = kcItem.getKeyblade();

			Iterator<Entry<Material, Integer>> itMats = item.data.getLevelData(item.getKeybladeLevel(stack)).getMaterialList().entrySet().iterator();
			boolean hasMaterials = true;
			while(itMats.hasNext()) { //Check if the player has the materials
				Entry<Material, Integer> m = itMats.next();
				
				if(playerData.getMaterialAmount(m.getKey()) < m.getValue()) {
					hasMaterials = false;
				}
			}
			
			if(hasMaterials) { //If the player has the materials substract them and give the item
                for (Entry<Material, Integer> m : item.data.getLevelData(item.getKeybladeLevel(stack)).getMaterialList().entrySet()) {
                    playerData.removeMaterial(m.getKey(), m.getValue());
                }
				kcItem.setKeybladeLevel(stack, kcItem.getKeybladeLevel(stack)+1);
				UUID keybladeID = Utils.getKeybladeID(stack);
				if (keybladeID != null) {
					ResourceLocation slot = null;
					for (Entry<ResourceLocation, ItemStack> entry : playerData.getEquippedKeychains().entrySet()) {
						if (keybladeID.equals(Utils.getKeybladeID(entry.getValue()))) {
							slot = entry.getKey();
						}
					}
					if (slot != null) {
						playerData.equipKeychain(slot, stack);
					} else {
						minecraft.player.getInventory().setItem(minecraft.player.getInventory().findSlotMatchingItem(selectedItemStack), stack);
					}
				}
			}
			PacketHandler.sendToServer(new CSLevelUpKeybladePacket(selectedItemStack));
			init();
			selectedItemStack = stack;
			break;
		}

	}
	
	@Override
	public void tick() {
		super.tick();
		ticks++;
	}
	
	@Override
	public void init() {	
		ticks = 0;
		float boxPosX = (float) width * 0.1437F;
		float topBarHeight = (float) height * 0.17F;
		float boxWidth = (float) width * 0.3F;
		float middleHeight = (float) height * 0.6F;
		boxL = new MenuBox((int) boxPosX, (int) topBarHeight, (int) boxWidth, (int) middleHeight, new Color(4, 4, 68));
		boxM = new MenuBox((int) boxPosX + (int) boxWidth, (int) topBarHeight, (int) (boxWidth*0.7F), (int) middleHeight, new Color(4, 4, 68));
		boxR = new MenuBox((int) boxM.getX() + (int) (boxWidth*0.7F), (int) topBarHeight, (int) (boxWidth*1.17F), (int) middleHeight, new Color(4, 4, 68));
		int scrollTop = (int) topBarHeight;
		int scrollBot = (int) (scrollTop + middleHeight);

		//float filterPosX = width * 0.3F;
		//float filterPosY = height * 0.02F;
		//filterBar = new MenuFilterBar((int) filterPosX, (int) filterPosY, this);
		//filterBar.init();
		scrollBar = new MenuScrollBar((int) (boxPosX + boxWidth - 17), scrollTop, scrollBot, (int) middleHeight, 0);
		addRenderableWidget(scrollBar);

		initItems();

		buttonPosX -= 10;
		buttonWidth = ((float)width * 0.07F);
		addRenderableWidget(back = new MenuButton((int)this.buttonPosX, this.buttonPosY, (int)buttonWidth, Component.translatable(Strings.Gui_Menu_Back).getString(), MenuButton.ButtonType.BUTTON, b -> minecraft.setScreen(new SynthesisScreen(parent.invFile, parent.name, parent.moogle))));

		super.init();
		itemsPerPage = (int) (middleHeight / 14);
	}

	@Override
	public void initItems() {
		Player player = minecraft.player;
		float invPosX = (float) width * 0.1494F;
		float invPosY = (float) height * 0.1851F;
		inventory.clear();
		children().clear();
		renderables.clear();
		//filterBar.buttons.forEach(this::addButton);

		List<ItemStack> items = new ArrayList<>();
		
		for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
			if (player.getInventory().getItem(i).getItem() instanceof KeychainItem) {
				items.add(player.getInventory().getItem(i));
			}
		}
		items.addAll(ModCapabilities.getPlayer(player).getEquippedKeychains().values().stream().filter(itemStack -> !itemStack.isEmpty()).toList());
		items.sort(Comparator.comparing(Utils::getCategoryForStack).thenComparing(stack -> stack.getHoverName().getContents().toString()));

		for (int i = 0; i < items.size(); i++) {
			if(items.get(i).getItem() instanceof KeychainItem) {
				KeybladeItem kb = ((KeychainItem)items.get(i).getItem()).toSummon();
				inventory.add(new MenuStockItem(this, items.get(i), (int) invPosX, (int) invPosY + (i * 14),boxL.getWidth()-scrollBar.getWidth()-6, false, new ItemStack(kb).getHoverName().getString()));
			} else {
				inventory.add(new MenuStockItem(this, items.get(i), (int) invPosX, (int) invPosY + (i * 14),boxL.getWidth()-scrollBar.getWidth()-6, false));
			}
		}
		
		inventory.forEach(this::addWidget);
		
		super.init();
		
		float buttonPosX = (float) width * 0.03F;

        addRenderableWidget(upgrade = Button.builder(Component.translatable(Utils.translateToLocal(Strings.Gui_Synthesis_Forge_Upgrade)), (e) -> {
			action("upgrade");
		}).bounds((int) (boxM.getX()+3), (int) (height * 0.67), 70, 20).build());
	}

	@Override
	public void render(@NotNull GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
		PoseStack matrixStack = gui.pose();
		drawMenuBackground(gui, mouseX, mouseY, partialTicks);
		boxL.renderWidget(gui, mouseX, mouseY, partialTicks);
		boxM.renderWidget(gui, mouseX, mouseY, partialTicks);
		boxR.renderWidget(gui, mouseX, mouseY, partialTicks);
		super.render(gui, mouseX, mouseY, partialTicks);

		if(inventory.isEmpty())
			return;

		int listHeight = (inventory.get(inventory.size()-1).getY()+20) - inventory.get(0).getY() + 3;
		scrollBar.setContentHeight(listHeight);

		if (selectedItemStack != null && !selectedItemStack.isEmpty() && selectedItemStack.getItem() instanceof KeychainItem keychain && keychain.getKeybladeLevel(selectedItemStack) < keychain.getKeyblade().getMaxLevel()) {
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);
			boolean enoughMats = true;
			KeychainItem kcItem = (KeychainItem)selectedItemStack.getItem();

			//Set create button state
			if(kcItem.getKeybladeLevel(selectedItemStack) < kcItem.getKeyblade().getMaxLevel()) {
				KeychainItem kChain = (KeychainItem) selectedItemStack.getItem();
				KeybladeItem kBlade = kChain.getKeyblade();
				upgrade.visible = true;
                for (Entry<Material, Integer> m : kBlade.data.getLevelData(kBlade.getKeybladeLevel(selectedItemStack)).getMaterialList().entrySet()) {
                    if (playerData.getMaterialAmount(m.getKey()) < m.getValue()) {
                        enoughMats = false;
                    }
                }
			}
			
			upgrade.active = enoughMats && ticks > 10;
			upgrade.visible = true;
		} else {
			upgrade.visible = false;
		}

		matrixStack.popPose();

		for(Renderable renderable : this.inventory){
			if(renderable instanceof MenuStockItem menuStockItem){
				menuStockItem.active = true;
				gui.enableScissor(boxL.getX()+2,scrollBar.getY()+2,boxL.getX()+boxL.getWidth(),scrollBar.getHeight()-5); //Arbitrary number to hide the cut one
				renderable.render(gui,mouseX,mouseY,partialTicks);
				gui.disableScissor();
			} else {
				renderable.render(gui,mouseX,mouseY,partialTicks);
			}
		}
		
		upgrade.render(gui, mouseX,  mouseY,  partialTicks);
		back.render(gui, mouseX, mouseY, partialTicks);
	}

	@Override
	protected void renderSelectedData(GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
		PoseStack matrixStack = gui.pose();
		float tooltipPosX = width * 0.3333F;
		float tooltipPosY = height * 0.8F;

		float iconPosX = boxR.getX();
		float iconPosY = boxR.getY() + 25;

		if (selectedItemStack != null && selectedItemStack.getItem() instanceof KeychainItem kc) {
			KeybladeItem kb = kc.getKeyblade();

			IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);
	
			//Icon
			matrixStack.pushPose();
			{
				double offset = (boxM.getWidth()*0.1F);
				matrixStack.translate(boxM.getX() + offset/2, iconPosY, 1);
				float size = 80;
				matrixStack.translate(boxM.getWidth()*0.7F / 2,boxM.getHeight()/2 - size / 2,0);
				ClientUtils.drawItemAsIcon(new ItemStack(kb), matrixStack, 0, -30, (int)size);
			}
			matrixStack.popPose();
			
			//Description
			matrixStack.pushPose();
			{
				String text = Utils.translateToLocal(kb.getDescriptionId());
				gui.drawString(minecraft.font, text, (int)(tooltipPosX + 5), (int) (tooltipPosY)+5, 0xFF9900);
				ClientUtils.drawSplitString(gui, kb.getDesc(), (int) tooltipPosX + 5, (int) tooltipPosY + 5 + minecraft.font.lineHeight, (int) (width * 0.6F), 0xFFFFFF);
			}
			matrixStack.popPose();
			
			matrixStack.pushPose();
			{
				matrixStack.translate(boxM.getX()+10, height*0.58, 1);
				int level = kb.getKeybladeLevel(selectedItemStack);
				if(level < kb.getMaxLevel()) {
					gui.drawString(minecraft.font, Utils.translateToLocal(Strings.Gui_Menu_Status_Level)+": "+level+" -> "+(level+1), 0, -20, 0xFFFF00);
					int actualStr = kb.getStrength(level);
					int nextStr = kb.getStrength(level+1);
					int actualMag = kb.getMagic(level);
					int nextMag = kb.getMagic(level+1);
					String nextAbility = kb.data.getLevelAbility(level+1);
					gui.drawString(minecraft.font, Utils.translateToLocal(Strings.Gui_Menu_Status_Strength)+": "+actualStr+" -> "+nextStr, 0, -10, 0xFF0000);
					gui.drawString(minecraft.font, Utils.translateToLocal(Strings.Gui_Menu_Status_Magic)+": "+actualMag+" -> "+nextMag, 0, 0, 0x4444FF);
					if(nextAbility != null) {
						Ability a = ModAbilities.registry.get().getValue(new ResourceLocation(nextAbility));
						if(a != null)
							gui.drawString(minecraft.font, Utils.translateToLocal(a.getTranslationKey()), 0, 10, 0x44FF44);
					}
				} else {
					gui.drawString(minecraft.font, Utils.translateToLocal(Strings.Gui_Menu_Status_Level)+": "+level, 0, -20, 0xFFFF00);
					int actualStr = kb.getStrength(kb.getKeybladeLevel(selectedItemStack));
					int actualMag = kb.getMagic(kb.getKeybladeLevel(selectedItemStack));
					gui.drawString(minecraft.font, Utils.translateToLocal(Strings.Gui_Menu_Status_Strength)+": "+actualStr, 0, -10, 0xFF0000);
					gui.drawString(minecraft.font, Utils.translateToLocal(Strings.Gui_Menu_Status_Magic)+": "+actualMag, 0, 0, 0x4444FF);
				}
			}
			matrixStack.popPose();
		
			//Materials display
			matrixStack.pushPose();
			{
				matrixStack.translate(iconPosX + 20, height*0.2, 1);
				if(kb.getKeybladeLevel(selectedItemStack) < kb.getMaxLevel()) {
					Iterator<Entry<Material, Integer>> itMats = kb.data.getLevelData(kb.getKeybladeLevel(selectedItemStack)).getMaterialList().entrySet().iterator();
					int i = 0;
					while(itMats.hasNext()) {
						Entry<Material, Integer> m = itMats.next();
						ItemStack stack = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(m.getKey().getMaterialName())),m.getValue());
						String n = Utils.translateToLocal(stack.getDescriptionId());
						//playerData.setMaterial(m.getKey(), 1);
						int color = playerData.getMaterialAmount(m.getKey()) >= m.getValue() ?  0x00FF00 : 0xFF0000;
						gui.drawString(minecraft.font, n+" x"+m.getValue()+" ("+playerData.getMaterialAmount(m.getKey())+")", 0, (i*16), color);
						ClientUtils.drawItemAsIcon(stack, matrixStack, -17, (i*16)-4, 16);
						i++;
					}
				}
			}
			matrixStack.popPose();
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
		if (mouseButton == 1) {
			GuiHelper.openMenu();
		}
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
	public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
		scrollBar.mouseScrolled(mouseX, mouseY, delta);
		updateScroll();
		return false;
	}
}

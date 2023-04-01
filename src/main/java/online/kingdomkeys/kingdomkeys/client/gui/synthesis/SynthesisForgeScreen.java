package online.kingdomkeys.kingdomkeys.client.gui.synthesis;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.components.Button;
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
import online.kingdomkeys.kingdomkeys.network.cts.CSLevelUpKeybladePacket;
import online.kingdomkeys.kingdomkeys.synthesis.material.Material;
import online.kingdomkeys.kingdomkeys.synthesis.recipe.Recipe;
import online.kingdomkeys.kingdomkeys.synthesis.recipe.RecipeRegistry;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class SynthesisForgeScreen extends MenuFilterable {
 
	int ticks=0;
	// MenuFilterBar filterBar;
	MenuScrollBar scrollBar;
	MenuBox boxL, boxM, boxR;
	int itemsX = 100, itemsY = 100, itemWidth = 140, itemHeight = 10;

	Button prev, next, upgrade;
	int itemsPerPage = 10;
	private MenuButton back;
	SynthesisScreen parent;

	public SynthesisForgeScreen(SynthesisScreen parent) {
		super("Forge", new Color(0, 255, 0));
		drawSeparately = true;
		this.parent = parent;
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double delta)
	{
		if (delta > 0 && prev.visible)
		{
			action("prev");
			return true;
		}
		else if  (delta < 0 && next.visible)
		{
			action("next");
			return true;
		}

		return false;
	}

	protected void action(String string) {
		switch (string) {
		case "prev":
			page--;
			minecraft.level.playSound(minecraft.player, minecraft.player.blockPosition(), ModSounds.menu_in.get(), SoundSource.MASTER, 1.0f, 1.0f);
			break;
		case "next":
			page++;
			minecraft.level.playSound(minecraft.player, minecraft.player.blockPosition(), ModSounds.menu_in.get(), SoundSource.MASTER, 1.0f, 1.0f);
			break;
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
				Iterator<Entry<Material, Integer>> ite = item.data.getLevelData(item.getKeybladeLevel(stack)).getMaterialList().entrySet().iterator();
				while(ite.hasNext()) {
					Entry<Material, Integer> m = ite.next();
					playerData.removeMaterial(m.getKey(), m.getValue());
				}
				kcItem.setKeybladeLevel(stack, kcItem.getKeybladeLevel(stack)+1);
				minecraft.player.getInventory().setItem(minecraft.player.getInventory().findSlotMatchingItem(selectedItemStack), stack);
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
		
		//float filterPosX = width * 0.3F;
		//float filterPosY = height * 0.02F;
		//filterBar = new MenuFilterBar((int) filterPosX, (int) filterPosY, this);
		//filterBar.init();
		initItems();
		// addButton(scrollBar = new MenuScrollBar());
		buttonPosX -= 10;
		buttonWidth = ((float)width * 0.07F);
		addRenderableWidget(back = new MenuButton((int)this.buttonPosX, this.buttonPosY, (int)buttonWidth, Component.translatable(Strings.Gui_Menu_Back).getString(), MenuButton.ButtonType.BUTTON, b -> minecraft.setScreen(new SynthesisScreen(parent.invFile))));

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
		items.sort(Comparator.comparing(Utils::getCategoryForStack).thenComparing(stack -> stack.getHoverName().getContents().toString()));

		for (int i = 0; i < items.size(); i++) {
			if(items.get(i).getItem() instanceof KeychainItem) {
				KeybladeItem kb = ((KeychainItem)items.get(i).getItem()).toSummon();
				inventory.add(new MenuStockItem(this, items.get(i), (int) invPosX, (int) invPosY + (i * 14),  (int)(width * 0.28F), false, new ItemStack(kb).getHoverName().getString()));
			} else {
				inventory.add(new MenuStockItem(this, items.get(i), (int) invPosX, (int) invPosY + (i * 14),  (int)(width * 0.28F), false));
			}
		}
		
		inventory.forEach(this::addWidget);
		
		super.init();
		
		float buttonPosX = (float) width * 0.03F;
		
		addRenderableWidget(prev = Button.builder(Component.translatable("<--"), (e) -> {
			action("prev");
		}).bounds((int) buttonPosX + 10, (int)(height * 0.1F), 30, 20).build());

		addRenderableWidget(next = Button.builder(Component.translatable("-->"), (e) -> {
			action("next");
		}).bounds((int) buttonPosX + 10 + 76, (int)(height * 0.1F), 30, 20).build());
        
        addRenderableWidget(upgrade = Button.builder(Component.translatable(Utils.translateToLocal(Strings.Gui_Synthesis_Forge_Upgrade)), (e) -> {
			action("upgrade");
		}).bounds((int) (boxM.getX()+3), (int) (height * 0.67), 70, 20).build());
	}

	@Override
	public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		drawMenuBackground(matrixStack, mouseX, mouseY, partialTicks);
		boxL.renderWidget(matrixStack, mouseX, mouseY, partialTicks);
		boxM.renderWidget(matrixStack, mouseX, mouseY, partialTicks);
		boxR.renderWidget(matrixStack, mouseX, mouseY, partialTicks);
		super.render(matrixStack, mouseX, mouseY, partialTicks);

		prev.visible = page > 0;
		next.visible = page < inventory.size() / itemsPerPage;
		if (selectedItemStack != null && (selectedItemStack.getItem() != ItemStack.EMPTY.getItem()) && ((KeychainItem)selectedItemStack.getItem()).getKeybladeLevel(selectedItemStack) < 10) {
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);
			boolean enoughMats = true;
			KeychainItem kcItem = (KeychainItem)selectedItemStack.getItem();
			KeybladeItem kb = ((KeychainItem)selectedItemStack.getItem()).getKeyblade();
			if(!RecipeRegistry.getInstance().containsKey(Utils.getItemRegistryName(kb))){
				return;
			}
			Recipe recipe = RecipeRegistry.getInstance().getValue(Utils.getItemRegistryName(kb));
			
			//Set create button state
			if(kcItem.getKeybladeLevel(selectedItemStack) < 10) {
				KeychainItem kChain = (KeychainItem) selectedItemStack.getItem();
				KeybladeItem kBlade = kChain.getKeyblade();
				if(recipe != null) {
					upgrade.visible = true;
					Iterator<Entry<Material, Integer>> materials = kBlade.data.getLevelData(kBlade.getKeybladeLevel(selectedItemStack)).getMaterialList().entrySet().iterator();
					while(materials.hasNext()) {
						Entry<Material, Integer> m = materials.next();
						if(playerData.getMaterialAmount(m.getKey()) < m.getValue()) {
							enoughMats = false;
						}
					}
					
				}
			}
			
			upgrade.active = enoughMats && ticks > 10;
			upgrade.visible = recipe != null;
		} else {
			upgrade.visible = false;
		}
		
		//Page renderer
		matrixStack.pushPose();
		{
			matrixStack.translate(width * 0.03F + 45, (height * 0.15) - 18, 1);
			drawString(matrixStack, minecraft.font, Utils.translateToLocal("Page: " + (page + 1)), 0, 10, 0xFF9900);
			
		}
		matrixStack.popPose();

		for (int i = 0; i < inventory.size(); i++) {
			inventory.get(i).active = false;
		}

		for (int i = page * itemsPerPage; i < page * itemsPerPage + itemsPerPage; i++) {
			if (i < inventory.size()) {
				if (inventory.get(i) != null) {
					inventory.get(i).visible = true;
					inventory.get(i).setY((int) (topBarHeight) + (i % itemsPerPage) * 14 + 5); // 6 = offset
					inventory.get(i).render(matrixStack, mouseX, mouseY, partialTicks);
					inventory.get(i).active = true;
				}
			}
		}
		
		prev.render(matrixStack, mouseX,  mouseY,  partialTicks);
		next.render(matrixStack, mouseX,  mouseY,  partialTicks);
		upgrade.render(matrixStack, mouseX,  mouseY,  partialTicks);
		back.render(matrixStack, mouseX, mouseY, partialTicks);
	}

	@Override
	protected void renderSelectedData(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
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
				matrixStack.scale((float)(boxM.getWidth() / 16F - offset / 16F), (float)(boxM.getWidth() / 16F - offset / 16F), 1);
				matrixStack.scale(0.8F, 0.8F, 0.8F);
				ClientUtils.drawItemAsIcon(new ItemStack(kb), matrixStack, 2, -4, 16);

			}
			matrixStack.popPose();
			
			//Description
			matrixStack.pushPose();
			{
				String text = Utils.translateToLocal(kb.getDescriptionId());
				drawString(matrixStack, minecraft.font, text, (int)(tooltipPosX + 5), (int) (tooltipPosY)+5, 0xFF9900);
				ClientUtils.drawSplitString(matrixStack, font, kb.getDesc(), (int) tooltipPosX + 5, (int) tooltipPosY + 5 + minecraft.font.lineHeight, (int) (width * 0.6F), 0xFFFFFF);
			}
			matrixStack.popPose();
			
			matrixStack.pushPose();
			{
				matrixStack.translate(boxM.getX()+10, height*0.58, 1);
				int level = kb.getKeybladeLevel(selectedItemStack);
				if(level < 10) {
					drawString(matrixStack, minecraft.font, Utils.translateToLocal(Strings.Gui_Menu_Status_Level)+": "+level+" -> "+(level+1), 0, -20, 0xFFFF00);				
					int actualStr = kb.getStrength(level);
					int nextStr = kb.getStrength(level+1);
					int actualMag = kb.getMagic(level);
					int nextMag = kb.getMagic(level+1);
					String nextAbility = kb.data.getLevelAbility(level+1);
					drawString(matrixStack, minecraft.font, Utils.translateToLocal(Strings.Gui_Menu_Status_Strength)+": "+actualStr+" -> "+nextStr, 0, -10, 0xFF0000);
					drawString(matrixStack, minecraft.font, Utils.translateToLocal(Strings.Gui_Menu_Status_Magic)+": "+actualMag+" -> "+nextMag, 0, 0, 0x4444FF);
					if(nextAbility != null) {
						Ability a = ModAbilities.registry.get().getValue(new ResourceLocation(nextAbility));
						if(a != null)
							drawString(matrixStack, minecraft.font, Utils.translateToLocal(a.getTranslationKey()), 0, 10, 0x44FF44);
					}
				} else {
					drawString(matrixStack, minecraft.font, Utils.translateToLocal(Strings.Gui_Menu_Status_Level)+": "+level, 0, -20, 0xFFFF00);				
					int actualStr = kb.getStrength(kb.getKeybladeLevel(selectedItemStack));
					int actualMag = kb.getMagic(kb.getKeybladeLevel(selectedItemStack));
					drawString(matrixStack, minecraft.font, Utils.translateToLocal(Strings.Gui_Menu_Status_Strength)+": "+actualStr, 0, -10, 0xFF0000);
					drawString(matrixStack, minecraft.font, Utils.translateToLocal(Strings.Gui_Menu_Status_Magic)+": "+actualMag, 0, 0, 0x4444FF);
				}
			}
			matrixStack.popPose();
		
			//Materials display
			matrixStack.pushPose();
			{
				matrixStack.translate(iconPosX + 20, height*0.2, 1);
				if(kb.getKeybladeLevel(selectedItemStack) < 10) {
					Iterator<Entry<Material, Integer>> itMats = kb.data.getLevelData(kb.getKeybladeLevel(selectedItemStack)).getMaterialList().entrySet().iterator();
					int i = 0;
					while(itMats.hasNext()) {
						Entry<Material, Integer> m = itMats.next();
						ItemStack stack = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(m.getKey().getMaterialName())),m.getValue());
						String n = Utils.translateToLocal(stack.getDescriptionId());
						//playerData.setMaterial(m.getKey(), 1);
						int color = playerData.getMaterialAmount(m.getKey()) >= m.getValue() ?  0x00FF00 : 0xFF0000;
						drawString(matrixStack, minecraft.font, n+" x"+m.getValue()+" ("+playerData.getMaterialAmount(m.getKey())+")", 0, (i*16), color);
						itemRenderer.renderGuiItem(matrixStack, stack, -17, (i*16)-4);
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
}

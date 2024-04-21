package online.kingdomkeys.kingdomkeys.client.gui.synthesis;

import java.awt.Color;
import java.util.*;
import java.util.Map.Entry;

import org.jetbrains.annotations.NotNull;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.ability.Ability;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBox;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuFilterBar;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuFilterable;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuScrollBar;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuStockItem;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.item.*;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSCloseMoogleGUI;
import online.kingdomkeys.kingdomkeys.network.cts.CSSynthesiseRecipe;
import online.kingdomkeys.kingdomkeys.synthesis.material.Material;
import online.kingdomkeys.kingdomkeys.synthesis.recipe.Recipe;
import online.kingdomkeys.kingdomkeys.synthesis.recipe.RecipeRegistry;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class SynthesisCreateScreen extends MenuFilterable {

	// MenuFilterBar filterBar;
	MenuScrollBar scrollBar;
	MenuBox boxL, boxM, boxR;
	int itemsX = 100, itemsY = 100, itemWidth = 140, itemHeight = 10;

	Button prev, next, create;
	int itemsPerPage;
	private MenuButton back;
	SynthesisScreen parent;

	public SynthesisCreateScreen(SynthesisScreen parent) {
		super(Gui_Synthesis_Synthesise_Title, new Color(0, 255, 0));
		drawSeparately = true;
		this.parent = parent;
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
		if (delta > 0 && prev.visible) {
			action("prev");
			return true;
		} else if (delta < 0 && next.visible) {
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
		case "create":
			PacketHandler.sendToServer(new CSSynthesiseRecipe(selectedRL));
			minecraft.level.playSound(minecraft.player, minecraft.player.blockPosition(), ModSounds.itemget.get(), SoundSource.MASTER, 1.0f, 1.0f);
			break;
		}
	}
	
	@Override
	public void init() {
		float boxPosX = (float) width * 0.1437F;
		float topBarHeight = (float) height * 0.17F;
		float boxWidth = (float) width * 0.3F;
		float middleHeight = (float) height * 0.6F;
		boxL = new MenuBox((int) boxPosX, (int) topBarHeight, (int) boxWidth, (int) middleHeight, new Color(4, 4, 68));
		boxM = new MenuBox((int) boxPosX + (int) boxWidth, (int) topBarHeight, (int) (boxWidth*0.7F), (int) middleHeight, new Color(4, 4, 68));
		boxR = new MenuBox((int) boxM.getX() + (int) (boxWidth*0.7F), (int) topBarHeight, (int) (boxWidth*1.17F), (int) middleHeight, new Color(4, 4, 68));
		
		float filterPosX = width * 0.3F;
		float filterPosY = height * 0.02F;
		filterBar = new MenuFilterBar((int) filterPosX, (int) filterPosY, this);
		filterBar.init();
		initItems();
		buttonPosX -= 10;
		buttonWidth = ((float)width * 0.07F);
		// addButton(scrollBar = new MenuScrollBar());
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
		filterBar.buttons.forEach(this::addWidget);

		List<ResourceLocation> items = new ArrayList<>();
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
		for (int i = 0; i < playerData.getKnownRecipeList().size(); i++) {
			ResourceLocation itemName = playerData.getKnownRecipeList().get(i);
			Recipe recipe = RecipeRegistry.getInstance().getValue(itemName);
			if(recipe != null) {
				ResourceLocation recipeRL = recipe.getRegistryName();
				ItemStack stack = new ItemStack(recipe.getResult());
	
				if (recipe.getResult() instanceof KeychainItem)
					stack = new ItemStack(((KeychainItem) recipe.getResult()).getKeyblade());
	
				if (filterItem(stack)) {
					items.add(recipeRL);
				}
			} else {
				KingdomKeys.LOGGER.error(itemName +" is not a valid recipe, check it");
			}
		}
		items.sort(Comparator.comparing(Utils::getCategoryForRecipe).thenComparing(stack -> new ItemStack(RecipeRegistry.getInstance().getValue(stack).getResult()).getHoverName().getContents().toString()));

		for (int i = 0; i < items.size(); i++) {
			ItemStack itemStack = new ItemStack(RecipeRegistry.getInstance().getValue(items.get(i)).getResult());
			if(itemStack != null && itemStack.getItem() instanceof KeychainItem) {
				itemStack = new ItemStack(((KeychainItem) RecipeRegistry.getInstance().getValue(items.get(i)).getResult()).getKeyblade());
			}
			inventory.add(new MenuStockItem(this, items.get(i), itemStack, (int) invPosX, (int) invPosY + (i * 14), (int)(width * 0.28F), false));
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
        
        addRenderableWidget(create = Button.builder(Component.translatable(Utils.translateToLocal(Strings.Gui_Synthesis_Synthesise_Create)), (e) -> {
			action("create");
		}).bounds((int) (boxM.getX()+3), (int) (height * 0.67), boxM.getWidth()-5, 20).build());
        
		addRenderableWidget(back = new MenuButton((int)this.buttonPosX, this.buttonPosY, (int)buttonWidth/2, Component.translatable(Strings.Gui_Menu_Back).getString(), MenuButton.ButtonType.BUTTON, b -> minecraft.setScreen(new SynthesisScreen(parent.invFile, parent.name, parent.moogle))));

	}

	@Override
	public void render(@NotNull GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
		PoseStack matrixStack = gui.pose();
		drawMenuBackground(gui, mouseX, mouseY, partialTicks);
		boxL.renderWidget(gui, mouseX, mouseY, partialTicks);
		boxM.renderWidget(gui, mouseX, mouseY, partialTicks);
		boxR.renderWidget(gui, mouseX, mouseY, partialTicks);
		super.render(gui, mouseX, mouseY, partialTicks);

		prev.visible = page > 0;
		next.visible = page < inventory.size() / itemsPerPage;
		if (selectedItemStack != ItemStack.EMPTY) {
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);
			boolean enoughMats = true;
			boolean enoughMunny = false;
			boolean enoughTier = false;
			boolean enoughSpace = false;
			if (RecipeRegistry.getInstance().containsKey(selectedRL)) {
				Recipe recipe = RecipeRegistry.getInstance().getValue(selectedRL);
				enoughSpace = Utils.getFreeSlotsForPlayer(minecraft.player) >= Utils.stacksForItemAmount(new ItemStack(recipe.getResult()), recipe.getAmount());
				enoughMunny = playerData.getMunny() >= recipe.getCost();
				enoughTier = !ModConfigs.requireSynthTier || playerData.getSynthLevel() >= recipe.getTier();
				create.visible = true;
				Iterator<Entry<Material, Integer>> materials = recipe.getMaterials().entrySet().iterator();// item.getRecipe().getMaterials().entrySet().iterator();//item.data.getLevelData(item.getKeybladeLevel()).getMaterialList().entrySet().iterator();
				while (materials.hasNext()) {
					Entry<Material, Integer> m = materials.next();
					if (playerData.getMaterialAmount(m.getKey()) < m.getValue()) {
						enoughMats = false;
					}
				}

			}

			create.active = enoughMats && enoughMunny && enoughTier && enoughSpace;
			if(!enoughSpace) { //TODO somehow make this detect in singleplayer the inventory changes
				create.setMessage(Component.translatable(Utils.translateToLocal(Strings.Gui_Synthesis_Synthesise_Create));
			} else {
				create.setMessage(Component.translatable(Utils.translateToLocal(Gui_Shop_NoSpace)));
			}
			create.visible = RecipeRegistry.getInstance().containsKey(selectedRL);
		} else {
			create.visible = false;
		}
		
		//Page renderer
		matrixStack.pushPose();
		{
			matrixStack.translate(width * 0.03F + 45, (height * 0.15) - 18, 1);
			gui.drawString(minecraft.font, Gui_Shop_Page + ": " + (page + 1)), 0, 10, 0xFF9900);
		}
		matrixStack.popPose();

		for (int i = 0; i < inventory.size(); i++) {
			inventory.get(i).active = false;
		}

		for (int i = page * itemsPerPage; i < page * itemsPerPage + itemsPerPage; i++) {
			if (i < inventory.size() && i >= 0) {
				if (inventory.get(i) != null) {
					inventory.get(i).visible = true;
					inventory.get(i).setY((int) (topBarHeight) + (i % itemsPerPage) * 14 + 5); // 6 = offset
					inventory.get(i).render(gui, mouseX, mouseY, partialTicks);
					inventory.get(i).active = true;
				}
			}
		}
		
		prev.render(gui, mouseX,  mouseY,  partialTicks);
		next.render(gui, mouseX,  mouseY,  partialTicks);
		create.render(gui, mouseX,  mouseY,  partialTicks);
		back.render(gui, mouseX, mouseY, partialTicks);
	}

	@Override
	protected void renderSelectedData(GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
		PoseStack matrixStack = gui.pose();
		float tooltipPosX = width * 0.3333F;
		float tooltipPosY = height * 0.8F;

		float iconPosX = boxR.getX();
		float iconPosY = boxR.getY() + 25;

		IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);

		matrixStack.pushPose();
		{
			double offset = (boxM.getWidth()*0.1F);
			matrixStack.translate(boxM.getX() + offset/2, iconPosY, 1);
			
			if(RecipeRegistry.getInstance().containsKey(selectedRL)) {
				Recipe recipe = RecipeRegistry.getInstance().getValue(selectedRL);
				gui.drawString(minecraft.font, Utils.translateToLocal(Gui_Shop_Buy_Cost)+":", 2, -20, Color.yellow.getRGB());
				String line = recipe.getCost()+" "+Utils.translateToLocal(Gui_Menu_Main_Munny);
				gui.drawString(minecraft.font, line, boxM.getWidth() - minecraft.font.width(line) - 10, -20, recipe.getCost() > playerData.getMunny() ? Color.RED.getRGB() : Color.GREEN.getRGB());
				gui.drawString(minecraft.font, Utils.translateToLocal(Gui_Shop_Tier)+":", 2, -10, Color.yellow.getRGB());
				line = Utils.getTierFromInt(recipe.getTier())+" "+(10 + recipe.getTier()*2)+"exp";
				gui.drawString(minecraft.font, line, boxM.getWidth() - minecraft.font.width(line) - 10, -10, recipe.getTier() > playerData.getSynthLevel() ? Color.RED.getRGB() : Color.GREEN.getRGB());
			}
			//matrixStack.scale((float)(boxM.getHeight() / 20F - offset / 20F), (float)(boxM.getHeight() / 20F - offset / 20F), 1);
			float size = 80;
			matrixStack.translate(boxM.getWidth()*0.7F / 2,boxM.getHeight()/2 - size / 2,0);
			ClientUtils.drawItemAsIcon(selectedItemStack, matrixStack, 0, -10, (int)size);
		}
		matrixStack.popPose();

		if (selectedItemStack != null && selectedItemStack.getItem() instanceof KeybladeItem || selectedItemStack.getItem() instanceof KKAccessoryItem || selectedItemStack.getItem() instanceof KKArmorItem) {
			String desc = "";
			String ability = "";
			int str=0, mag=0, ap = 0, def = 0, fireRes = 0, iceRes = 0, thunderRes = 0, darkRes = 0;
			if(selectedItemStack.getItem() instanceof KeybladeItem) {
				KeybladeItem kb = (KeybladeItem) selectedItemStack.getItem();
				desc = kb.getDesc();
				ability = kb.data.getLevelAbility(0);
				str= kb.getStrength(0);
				mag = kb.getMagic(0);
				
			} else if(selectedItemStack.getItem() instanceof KKAccessoryItem) {
				KKAccessoryItem accessory = (KKAccessoryItem) selectedItemStack.getItem();
				ability = accessory.getAbilities().size() > 0 ? accessory.getAbilities().get(0) : null;
				str = accessory.getStr();
				mag = accessory.getMag();
				ap = accessory.getAp();
			} else if(selectedItemStack.getItem() instanceof KKArmorItem) {
				KKArmorItem armor = (KKArmorItem) selectedItemStack.getItem();
				def = armor.getDefense();
				for (Map.Entry<KKResistanceType, Integer> resistanceType : armor.getResList().entrySet()) {
					switch (resistanceType.getKey()) {
					case fire -> fireRes = resistanceType.getValue();
					case ice -> iceRes = resistanceType.getValue();
					case lightning -> thunderRes = resistanceType.getValue();
					case darkness -> darkRes = resistanceType.getValue();
					}
				}
			}
			
				
			matrixStack.pushPose();
			{
				matrixStack.translate(boxM.getX()+20, height*0.58, 1);
				
				int offset = -15;
				
				if(ap != 0)
					gui.drawString(minecraft.font, Utils.translateToLocal(Strings.Gui_Menu_Status_AP)+": "+ap, 0, offset+=10, 0xFFFF44);
				if(str != 0 || selectedItemStack.getItem() instanceof KeybladeItem)
					gui.drawString(minecraft.font, Utils.translateToLocal(Strings.Gui_Menu_Status_Strength)+": +"+str, 0, offset+=10, 0xFF0000);
				if(mag != 0 || selectedItemStack.getItem() instanceof KeybladeItem)
					gui.drawString(minecraft.font, Utils.translateToLocal(Strings.Gui_Menu_Status_Magic)+": +"+mag, 0, offset+=10, 0x4444FF);
				if(def != 0)
					gui.drawString(minecraft.font, Utils.translateToLocal(Strings.Gui_Menu_Status_Defense)+": "+def, 0, offset+=10, 0xFFFFFF);
				if(fireRes != 0)
					gui.drawString(minecraft.font, Utils.translateToLocal(Strings.Gui_Menu_Status_FireResShort)+": "+fireRes+"%", 0, offset+=10, 0xFF4444);
				if(iceRes != 0)
					gui.drawString(minecraft.font, Utils.translateToLocal(Strings.Gui_Menu_Status_BlizzardResShort)+": "+iceRes+"%", 0, offset+=10, 0x55FFFF);
				if(thunderRes != 0)
					gui.drawString(minecraft.font, Utils.translateToLocal(Strings.Gui_Menu_Status_ThunderResShort)+": "+thunderRes+"%", 0, offset+=10, 0xFFFF44);
				if(darkRes != 0)
					gui.drawString(minecraft.font, Utils.translateToLocal(Strings.Gui_Menu_Status_DarkResShort)+": "+darkRes+"%", 0, offset+=10, 0xAAAAAA);
				if(ability != null) {
					Ability a = ModAbilities.registry.get().getValue(new ResourceLocation(ability));
					if(a != null) {
						String abilityName = Utils.translateToLocal(a.getTranslationKey());
						gui.drawString(minecraft.font, abilityName, -20 + (boxM.getWidth()/2) - (minecraft.font.width(abilityName)/2), offset+=10, 0xFFAA44);
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

		//Materials
		matrixStack.pushPose();
		{
			matrixStack.translate(iconPosX + 20, height*0.2, 1);
			
			if(RecipeRegistry.getInstance().containsKey(selectedRL)) {
				Recipe recipe = RecipeRegistry.getInstance().getValue(selectedRL);
				Iterator<Entry<Material, Integer>> materials = Utils.getSortedMaterials(recipe.getMaterials()).entrySet().iterator();//item.data.getLevelData(item.getKeybladeLevel()).getMaterialList().entrySet().iterator();
				int i = 0;
				while(materials.hasNext()) {
					Entry<Material, Integer> m = materials.next();
					ItemStack stack = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(m.getKey().getMaterialName())),m.getValue());
					String n = Utils.translateToLocal(stack.getDescriptionId());
					int color = playerData.getMaterialAmount(m.getKey()) >= m.getValue() ?  0x00FF00 : 0xFF0000;
					gui.drawString(minecraft.font, n+" x"+m.getValue()+" ("+playerData.getMaterialAmount(m.getKey())+")", 0, (i*16), color);
					ClientUtils.drawItemAsIcon(stack, matrixStack, -17, (i*16)-4, 16);
					i++;
				}				
			}
		}
		matrixStack.popPose();
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

}

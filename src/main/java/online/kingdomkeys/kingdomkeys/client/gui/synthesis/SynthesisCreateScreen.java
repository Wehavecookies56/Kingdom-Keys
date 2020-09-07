package online.kingdomkeys.kingdomkeys.client.gui.synthesis;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.registries.ForgeRegistries;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBox;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuFilterBar;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuFilterable;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuScrollBar;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuStockItem;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.item.KeybladeItem;
import online.kingdomkeys.kingdomkeys.item.KeychainItem;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSSynthesiseKeyblade;
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

	int page = 0;
	int itemsPerPage = 10;

	public SynthesisCreateScreen() {
		super("Synthesis", new Color(0, 255, 0));
		drawSeparately = true;
	}

	protected void action(String string) {
		switch (string) {
		case "prev":
			page--;
			minecraft.world.playSound(minecraft.player, minecraft.player.getPosition(), ModSounds.menu_in.get(), SoundCategory.MASTER, 1.0f, 1.0f);
			break;
		case "next":
			page++;
			minecraft.world.playSound(minecraft.player, minecraft.player.getPosition(), ModSounds.menu_in.get(), SoundCategory.MASTER, 1.0f, 1.0f);
			break;
		case "create":
			PacketHandler.sendToServer(new CSSynthesiseKeyblade(selected.getItem().getRegistryName()));
			minecraft.world.playSound(minecraft.player, minecraft.player.getPosition(), ModSounds.itemget.get(), SoundCategory.MASTER, 1.0f, 1.0f);
			break;
		}

	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		drawMenuBackground(mouseX, mouseY, partialTicks);
		boxL.draw();
		boxM.draw();
		boxR.draw();
		super.render(mouseX, mouseY, partialTicks);

		prev.visible = page > 0;
		next.visible = page < inventory.size() / itemsPerPage;

		if (selected != ItemStack.EMPTY) {
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);
			boolean enoughMats = true;
			Recipe recipe = RecipeRegistry.getInstance().getValue(selected.getItem().getRegistryName());
			if (recipe != null) {
				create.visible = true;
				Iterator<Entry<Material, Integer>> materials = recipe.getMaterials().entrySet().iterator();// item.getRecipe().getMaterials().entrySet().iterator();//item.data.getLevelData(item.getKeybladeLevel()).getMaterialList().entrySet().iterator();
				while (materials.hasNext()) {
					Entry<Material, Integer> m = materials.next();
					if (playerData.getMaterialAmount(m.getKey()) < m.getValue()) {
						enoughMats = false;
					}
				}
			}

			create.active = enoughMats;
			create.visible = recipe != null;
		} else {
			create.visible = false;
		}
		RenderSystem.pushMatrix();
		{
			RenderSystem.translated(width * 0.03F + 45, (height * 0.17) - 18, 1);
			drawString(minecraft.fontRenderer, Utils.translateToLocal("Page: " + (page + 1)), 0, 10, 0xFF9900);
			//System.out.println(next.visible);
		}
		RenderSystem.popMatrix();

		for (int i = 0; i < inventory.size(); i++) {
			inventory.get(i).active = false;
		}

		for (int i = page * itemsPerPage; i < page * itemsPerPage + itemsPerPage; i++) {
			if (i < inventory.size()) {
				if (inventory.get(i) != null) {
					inventory.get(i).visible = true;
					inventory.get(i).y = ((i / itemsPerPage) - (i / itemsPerPage) + 3 + (i % itemsPerPage)) * 14 + 6; // 6 = offset
					inventory.get(i).render(mouseX, mouseY, partialTicks);
					inventory.get(i).active = true;
				}
			}
		}
		
		prev.render(mouseX,  mouseY,  partialTicks);
		next.render(mouseX,  mouseY,  partialTicks);
		create.render(mouseX,  mouseY,  partialTicks);
	}

	@Override
	protected void renderSelectedData() {
		float tooltipPosX = width * 0.3333F;
		float tooltipPosY = height * 0.8F;

		float iconPosX = boxM.x;
		float iconPosY = boxM.y + 25;

		IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);

		RenderHelper.disableStandardItemLighting();
		RenderSystem.pushMatrix();
		{
			RenderSystem.translated(boxR.x, iconPosY, 1);
			RenderSystem.scaled(4, 4, 4);
			itemRenderer.renderItemIntoGUI(selected, 0, 0);
		}
		RenderSystem.popMatrix();

		if (selected.getItem() != null && selected.getItem() instanceof KeybladeItem) {
			KeybladeItem kb = (KeybladeItem) selected.getItem();

			RenderSystem.pushMatrix();
			{
				minecraft.fontRenderer.drawSplitString(kb.getDescription(), (int) tooltipPosX + 5, (int) tooltipPosY + 5, (int) (width * 0.6F), 0xFFFFFF);
			}
			RenderSystem.popMatrix();
			
			RenderSystem.pushMatrix();
			{
				RenderSystem.translated(width*0.87F, height*0.58, 1);
				drawString(minecraft.fontRenderer, Utils.translateToLocal(Strings.Gui_Menu_Status_Strength)+": "+kb.getStrength(0), 0, 0, 0xFF0000);
				drawString(minecraft.fontRenderer, Utils.translateToLocal(Strings.Gui_Menu_Status_Magic)+": "+kb.getMagic(0), 0, 10, 0x0000FF);
			}
			RenderSystem.popMatrix();
		}
		
		RenderSystem.pushMatrix();
		{
			RenderSystem.scaled(1,1,1);
			String text = Utils.translateToLocal(selected.getTranslationKey());
			//System.out.println(width);
			drawString(minecraft.fontRenderer, text, (int)(width-minecraft.fontRenderer.getStringWidth(text))-2, (int) (height*0.17)+5, 0xFF9900);
		}
		RenderSystem.popMatrix();

		//Materials
		RenderSystem.pushMatrix();
		{
			RenderSystem.translated(iconPosX + 20, height*0.2, 1);
			Recipe recipe = RecipeRegistry.getInstance().getValue(selected.getItem().getRegistryName());
			if(recipe != null) {
				Iterator<Entry<Material, Integer>> materials = Utils.getSortedMaterials(recipe.getMaterials()).entrySet().iterator();//item.data.getLevelData(item.getKeybladeLevel()).getMaterialList().entrySet().iterator();
				int i = 0;
				while(materials.hasNext()) {
					Entry<Material, Integer> m = materials.next();
					ItemStack stack = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(m.getKey().getMaterialName())),m.getValue());
					String n = Utils.translateToLocal(stack.getTranslationKey());
					int color = playerData.getMaterialAmount(m.getKey()) >= m.getValue() ?  0x00FF00 : 0xFF0000;
					drawString(minecraft.fontRenderer, n+" x"+m.getValue()+" ("+playerData.getMaterialAmount(m.getKey())+")", 0, (i*16), color);
					itemRenderer.renderItemIntoGUI(stack, -17, (i*16)-5);
					i++;
				}
			}
		}
		RenderSystem.popMatrix();
	}

	@Override
	public void init() {
		float boxPosX = (float) width * 0.1437F;
		float topBarHeight = (float) height * 0.17F;
		float boxWidth = (float) width * 0.35F;
		float middleHeight = (float) height * 0.6F;
		boxL = new MenuBox((int) boxPosX, (int) topBarHeight, (int) boxWidth, (int) middleHeight, new Color(4, 4, 68));
		boxM = new MenuBox((int) boxPosX + (int) boxWidth, (int) topBarHeight, (int) boxWidth, (int) middleHeight, new Color(4, 4, 68));
		boxR = new MenuBox((int) boxM.x + (int) boxWidth, (int) topBarHeight, (int) (boxWidth*0.46F), (int) middleHeight, new Color(4, 4, 68));
		
		float filterPosX = width * 0.3F;
		float filterPosY = height * 0.02F;
		filterBar = new MenuFilterBar((int) filterPosX, (int) filterPosY, this);
		filterBar.init();
		initItems();
		// addButton(scrollBar = new MenuScrollBar());
		super.init();
	}

	@Override
	public void initItems() {
		PlayerEntity player = minecraft.player;
		float invPosX = (float) width * 0.1494F;
		float invPosY = (float) height * 0.1851F;
		inventory.clear();
		children.clear();
		buttons.clear();
		filterBar.buttons.forEach(this::addButton);

		List<ItemStack> items = new ArrayList<>();
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
		for (int i = 0; i < playerData.getKnownRecipeList().size(); i++) {
			ResourceLocation itemName = playerData.getKnownRecipeList().get(i);
			Recipe recipe = RecipeRegistry.getInstance().getValue(itemName);
			ItemStack stack = new ItemStack(recipe.getResult());

			if (recipe.getResult() instanceof KeychainItem)
				stack = new ItemStack(((KeychainItem) recipe.getResult()).getKeyblade());

			if (filterItem(stack)) {
				items.add(stack);
			}
		}
		items.sort(Comparator.comparing(Utils::getCategoryForStack).thenComparing(stack -> stack.getDisplayName().getUnformattedComponentText()));

		for (int i = 0; i < items.size(); i++) {
			inventory.add(new MenuStockItem(this, items.get(i), (int) invPosX, (int) invPosY + (i * 14), false));
		}
		
		inventory.forEach(this::addButton);
		
		super.init();
		
		int button_statsY = (int) topBarHeight + 5;
		float buttonPosX = (float) width * 0.03F;
		addButton(prev = new Button((int) buttonPosX + 10, button_statsY + (-1 * 18), 30, 20, Utils.translateToLocal("<--"), (e) -> {
			action("prev");
		}));
		addButton(next = new Button((int) buttonPosX + 10 + 80, button_statsY + (-1 * 18), 30, 20, Utils.translateToLocal("-->"), (e) -> { //MenuButton((int) buttonPosX, button_statsY + (0 * 18), (int) 100, Utils.translateToLocal(Strings.Gui_Synthesis_Materials_Deposit), ButtonType.BUTTON, (e) -> { //
			action("next");
		}));
		addButton(create = new Button((int) (width * 0.85F), (int) (height * 0.67), 70, 20, Utils.translateToLocal(Strings.Gui_Synthesis_Synthesise_Create), (e) -> {
			action("create");
		}));
	}

}

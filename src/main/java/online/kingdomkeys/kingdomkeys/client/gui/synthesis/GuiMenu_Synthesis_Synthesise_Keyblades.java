package online.kingdomkeys.kingdomkeys.client.gui.synthesis;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.registries.ForgeRegistries;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton.ButtonType;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.item.KeybladeItem;
import online.kingdomkeys.kingdomkeys.lib.Lists;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSSynthesiseKeyblade;
import online.kingdomkeys.kingdomkeys.synthesis.material.Material;
import online.kingdomkeys.kingdomkeys.synthesis.recipe.Recipe;
import online.kingdomkeys.kingdomkeys.synthesis.recipe.RecipeRegistry;

public class GuiMenu_Synthesis_Synthesise_Keyblades extends MenuBackground {
	
	MenuButton[] keyblades = new MenuButton[Lists.keybladeRecipes.size()+Lists.itemRecipes.size()];
	Button prev,next, create;
	int selectedKB = -1;
	int page = 0;
	
	List<ResourceLocation> keybladeRecipes = new ArrayList<ResourceLocation>();

		
	public GuiMenu_Synthesis_Synthesise_Keyblades() {
		super(Strings.Gui_Synthesis_Synthesise, new Color(0, 255, 0));
		drawPlayerInfo = true;
	}

	protected void action(String string) {
		if(string.startsWith("s:")) {
			String[] data = string.split(":");
			selectedKB = getIndexFromName(data[1]);
		} else {
			switch(string) {
			case "prev":
				page--;
				minecraft.world.playSound(minecraft.player, minecraft.player.getPosition(), ModSounds.menu_in.get(), SoundCategory.MASTER, 1.0f, 1.0f);
				break;
			case "next":
				page++;
				minecraft.world.playSound(minecraft.player, minecraft.player.getPosition(), ModSounds.menu_in.get(), SoundCategory.MASTER, 1.0f, 1.0f);
				break;
			case "create":
				IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);
				PacketHandler.sendToServer(new CSSynthesiseKeyblade(keybladeRecipes.get(selectedKB).toString()));
				minecraft.world.playSound(minecraft.player, minecraft.player.getPosition(), ModSounds.itemget.get(), SoundCategory.MASTER, 1.0f, 1.0f);
				break;
			}
		}
	}

	private int getIndexFromName(String name) {
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);
		for(int i = 0;i < keybladeRecipes.size(); i++) {
			if(keybladeRecipes.get(i).equals(name)) {
				return i;
			}
		}
		return -1;
	}
	
	//boolean enoughMats = false;
	private void updateButtons() {
		
		if(selectedKB > -1) {
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);
			List<ResourceLocation> recipeList = keybladeRecipes;
			ResourceLocation kb = recipeList.get(selectedKB);
			KeybladeItem item = (KeybladeItem) ForgeRegistries.ITEMS.getValue(kb);
			
			boolean enoughMats = true;
			Recipe recipe = RecipeRegistry.getInstance().getValue(item.getRegistryName());
			if(recipe != null) {
				create.visible = true;
				Iterator<Entry<Material, Integer>> materials = recipe.getMaterials().entrySet().iterator();//item.getRecipe().getMaterials().entrySet().iterator();//item.data.getLevelData(item.getKeybladeLevel()).getMaterialList().entrySet().iterator();
				while(materials.hasNext()) {
					Entry<Material, Integer> m = materials.next();
					if(playerData.getMaterialAmount(m.getKey()) < m.getValue()) {
						enoughMats = false;
					}
				}
			}

			create.active = enoughMats;
			create.visible = recipe != null;
		} else {
			create.visible = false;
		}
	}

	@Override
	public void init() {
		//TODO request packet to sync other players data
		super.width = width;
		super.height = height;
		super.init();
		this.buttons.clear();
				
		float topBarHeight = (float) height * 0.17F;
		int button_statsY = (int) topBarHeight + 5;
		float buttonPosX = (float) width * 0.03F;
		float buttonWidth = ((float) width * 0.1744F) - 20;

		IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);
		
		addButton(prev = new Button((int) buttonPosX+10, button_statsY + (-1 * 18), 30,20, Utils.translateToLocal("<--"), (e) -> { action("prev"); }));
		addButton(next = new Button((int) buttonPosX+10+80, button_statsY + (-1 * 18), 30,20, Utils.translateToLocal("-->"), (e) -> { action("next"); }));
		addButton(create = new Button((int) (width*0.75F), (int) (height*0.6)+20, 50,20, Utils.translateToLocal(Strings.Gui_Synthesis_Synthesise_Create), (e) -> { action("create"); }));
		
		prev.visible = false;
		next.visible = false;
		keybladeRecipes.clear();

		for(int i = 0;i<playerData.getKnownRecipeList().size();i++) {
			ResourceLocation name = playerData.getKnownRecipeList().get(i);
			Recipe recipe = RecipeRegistry.getInstance().getValue(name);

			if(recipe != null && recipe.getType().equals("keyblade")) {
				keybladeRecipes.add(name);
			} else {
				System.out.println("F: "+name);
			}
		}
		
		
		for(int i = 0;i<keybladeRecipes.size();i++) {
			ResourceLocation name = keybladeRecipes.get(i);
			Recipe recipe = RecipeRegistry.getInstance().getValue(name);

			if(recipe != null) {
				addButton(keyblades[i] = new MenuButton((int) buttonPosX, button_statsY + (i * 18), (int) buttonWidth, Utils.translateToLocal(keybladeRecipes.get(i).getPath()), ButtonType.BUTTON, (e) -> { action("s:"+name); }));
				keyblades[i].active = RecipeRegistry.getInstance().getValue(name) != null;
			} 
		}
		
		updateButtons();
	}

	
	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		updateButtons();

		//System.out.println(phase);
		//fill(125, ((-140 / 16) + 75) + 10, 200, ((-140 / 16) + 75) + 20, 0xFFFFFF);
		super.render(mouseX, mouseY, partialTicks);
		
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);
		prev.visible = page > 0;
		next.visible = page < keybladeRecipes.size()/8;
		
		RenderSystem.pushMatrix();
		{
			RenderSystem.translated(width * 0.03F+45, (height*0.17) - 18, 1);
			RenderSystem.scaled(1,1,1);
			drawString(minecraft.fontRenderer, Utils.translateToLocal("Page: "+(page+1)), 0, 10, 0xFF9900);
		}
		RenderSystem.popMatrix();
		
		
		for(int i=0;i< keyblades.length;i++) {
			if(keyblades[i] != null) {
				keyblades[i].visible = false;
			}
		}
		
		for(int i=page*8;i< page*8+8;i++) {
			if(i < Lists.keybladeRecipes.size() && keyblades[i] != null) {
				keyblades[i].visible = true;
				keyblades[i].y = ((i/8)-(i/8)+3+(i%8))*18;
				keyblades[i].x = (int) (width * 0.04F);
			}
		}
		
		if(selectedKB > -1) {
			RenderSystem.pushMatrix();
			{
				ResourceLocation kb = keybladeRecipes.get(selectedKB);
				KeybladeItem item = (KeybladeItem) ForgeRegistries.ITEMS.getValue(kb);
				
				RenderSystem.pushMatrix();
				{
					RenderSystem.scaled(1,1,1);
					String text = Utils.translateToLocal(keybladeRecipes.get(selectedKB).toString());
					//System.out.println(width);
					drawString(minecraft.fontRenderer, text, width-minecraft.fontRenderer.getStringWidth(text)-10, (int) (height*0.17)+5, 0xFF9900);
				}
				RenderSystem.popMatrix();
				
				RenderSystem.pushMatrix();
				{
					RenderSystem.translated(width*0.75F, height*0.27, 1);
					RenderSystem.scaled(5, 5, 5);
					itemRenderer.renderItemIntoGUI(new ItemStack(item), 0, 0);
				}
				RenderSystem.popMatrix();
				
				RenderSystem.pushMatrix();
				{
					RenderSystem.translated(width*0.75F, height*0.6, 1);
					drawString(minecraft.fontRenderer, Utils.translateToLocal(Strings.Gui_Menu_Status_Strength)+": "+item.getStrength(0), 0, 0, 0xFF0000);
					drawString(minecraft.fontRenderer, Utils.translateToLocal(Strings.Gui_Menu_Status_Magic)+": "+item.getMagic(0), 0, 10, 0x0000FF);
					
					//drawString(minecraft.fontRenderer, "Ability: "+item.getAbility(), 0, 20, 0x0000FF);
				}
				RenderSystem.popMatrix();
				
				RenderSystem.pushMatrix();
				{
					RenderSystem.translated(width*0.4F, height*0.2, 1);
					Recipe recipe = RecipeRegistry.getInstance().getValue(item.getRegistryName());
					if(recipe != null) {
						Iterator<Entry<Material, Integer>> materials = Utils.getSortedMaterials(recipe.getMaterials()).entrySet().iterator();//item.data.getLevelData(item.getKeybladeLevel()).getMaterialList().entrySet().iterator();
						int i = 0;
						while(materials.hasNext()) {
							Entry<Material, Integer> m = materials.next();
							ItemStack stack = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(m.getKey().getMaterialName())),m.getValue());
							String n = Utils.translateToLocal(stack.getTranslationKey());
							//playerData.setMaterial(m.getKey(), 1);
							int color = playerData.getMaterialAmount(m.getKey()) >= m.getValue() ?  0x00FF00 : 0xFF0000;
							drawString(minecraft.fontRenderer, n+" x"+m.getValue()+" ("+playerData.getMaterialAmount(m.getKey())+")", 0, (i*16), color);
							itemRenderer.renderItemIntoGUI(stack, -17, (i*16)-5);
							i++;
						}
					}
				}
				RenderSystem.popMatrix();
				

			}
			RenderSystem.popMatrix();
		}
	}
	
}

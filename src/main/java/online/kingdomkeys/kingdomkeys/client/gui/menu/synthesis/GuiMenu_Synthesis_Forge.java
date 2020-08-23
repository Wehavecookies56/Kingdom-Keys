package online.kingdomkeys.kingdomkeys.client.gui.menu.synthesis;

import java.awt.Color;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.registries.ForgeRegistries;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.gui.menu.GuiMenuButton;
import online.kingdomkeys.kingdomkeys.client.gui.menu.GuiMenuButton.ButtonType;
import online.kingdomkeys.kingdomkeys.client.gui.menu.GuiMenu_Background;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.item.KeybladeItem;
import online.kingdomkeys.kingdomkeys.item.KeychainItem;
import online.kingdomkeys.kingdomkeys.lib.Lists;
import online.kingdomkeys.kingdomkeys.lib.Utils;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSLevelUpKeybladePacket;
import online.kingdomkeys.kingdomkeys.synthesis.material.Material;

public class GuiMenu_Synthesis_Forge extends GuiMenu_Background {
	
	GuiMenuButton[] keychains = new GuiMenuButton[36];
	Button prev,next, create;
	int selectedKC = -1;
	int page = 0;
																// Name, inv_slot
	LinkedHashMap<String,Integer> keychainsMap = new LinkedHashMap<String,Integer>();
		
	public GuiMenu_Synthesis_Forge() {
		super("Synthesis Forge", new Color(0, 255, 0));
		drawPlayerInfo = true;
	}

	protected void action(String string) {
		if(string.startsWith("s:")) {
			String[] data = string.split(":");
			selectedKC = Integer.parseInt(data[2]);
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
			case "upgrade":
				IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);
				PacketHandler.sendToServer(new CSLevelUpKeybladePacket(getIndexFromMapIndex(selectedKC)));
				minecraft.world.playSound(minecraft.player, minecraft.player.getPosition(), ModSounds.itemget.get(), SoundCategory.MASTER, 1.0f, 1.0f);
				
				ItemStack stack = minecraft.player.inventory.getStackInSlot(getIndexFromMapIndex(selectedKC));
				KeychainItem kcItem = (KeychainItem) stack.getItem();
				KeybladeItem item = (KeybladeItem) kcItem.getKeyblade();

				Iterator<Entry<Material, Integer>> itMats = item.data.getLevelData(item.getKeybladeLevel(stack)).getMaterialList().entrySet().iterator();
				boolean hasMaterials = true;
				while(itMats.hasNext()) { //Check if the player has the materials (checked serverside just in case)
					Entry<Material, Integer> m = itMats.next();
					//System.out.println(m.getKey().getMaterialName()+" x"+m.getValue());
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
				}
				break;
			}
		}
	}

	private int getIndexFromName(String name) {
		if(keychainsMap.containsKey(name)) {
			return keychainsMap.get(name);
		}
		return -1;
	}
	
	private int getIndexFromMapIndex(int index) {
		return (int) keychainsMap.values().toArray()[index];		
	}
	
	private void updateButtons() {
		if(selectedKC > -1) {
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);
			//List<String> recipeList = playerData.getKnownRecipeList();
			//System.out.println(selectedKC);
			ItemStack stack = minecraft.player.inventory.getStackInSlot(getIndexFromMapIndex(selectedKC));
			boolean enoughMats = true;

			if(stack != null && stack.getItem() != null && stack.getItem() instanceof KeychainItem) {
				KeychainItem kcItem = (KeychainItem)stack.getItem();
				if(kcItem.getKeybladeLevel(stack) < 10) {
					KeychainItem kChain = (KeychainItem) stack.getItem();
					KeybladeItem kBlade = kChain.getKeyblade();
					create.visible = true;
					Iterator<Entry<Material, Integer>> materials = kBlade.data.getLevelData(kBlade.getKeybladeLevel(stack)).getMaterialList().entrySet().iterator();
					while(materials.hasNext()) {
						Entry<Material, Integer> m = materials.next();
						if(playerData.getMaterialAmount(m.getKey()) < m.getValue()) {
							enoughMats = false;
						}
					}
				}
			}
		//	System.out.println(stack);

			create.active = enoughMats;
			create.visible = stack != null && stack.getItem() instanceof KeychainItem && ((KeychainItem)stack.getItem()).getKeyblade().getKeybladeLevel(stack) < 10;
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

		//IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);
		
		addButton(prev = new Button((int) buttonPosX+10, button_statsY + (-1 * 18), 30,20, Utils.translateToLocal("<--"), (e) -> { action("prev"); }));
		addButton(next = new Button((int) buttonPosX+10+80, button_statsY + (-1 * 18), 30,20, Utils.translateToLocal("-->"), (e) -> { action("next"); }));
		addButton(create = new Button((int) (width*0.75F), (int) (height*0.6)+20, 50,20, Utils.translateToLocal("gui.synthesis.synthesise.upgrade"), (e) -> { action("upgrade"); }));
		
		for(int i = 0;i<minecraft.player.inventory.getSizeInventory();i++) {
			ItemStack stack = minecraft.player.inventory.getStackInSlot(i);
			String name = stack.getItem().getName().getFormattedText();
			if(stack.getItem() != null && stack.getItem() instanceof KeychainItem) {
				keychainsMap.put(name, i);
			}
		}
		
		Iterator<Entry<String, Integer>> itKeychains = keychainsMap.entrySet().iterator();//item.data.getLevelData(item.getKeybladeLevel()).getMaterialList().entrySet().iterator();
		int i = -1;
		while(itKeychains.hasNext()) {
			Entry<String, Integer> k = itKeychains.next();
			String name = k.getKey();
			int index = ++i;
			System.out.println(index);
			addButton(keychains[index] = new GuiMenuButton((int) buttonPosX, button_statsY + (index * 18), (int) buttonWidth, Utils.translateToLocal(name.substring(0, name.length()-"_chain".length())), ButtonType.BUTTON, (e) -> { action("s:"+name+":"+index); }));
			//keychains[i].active = RecipeRegistry.getInstance().getValue(new ResourceLocation(name.substring(5).replace(".", ":"))) != null;
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
		next.visible = page < playerData.getKnownRecipeList().size()/8;
		
		RenderSystem.pushMatrix();
		{
			RenderSystem.translated(width * 0.03F+45, (height*0.17) - 18, 1);
			RenderSystem.scaled(1,1,1);
			drawString(minecraft.fontRenderer, Utils.translateToLocal("Page: "+(page+1)), 0, 10, 0xFF9900);
		}
		RenderSystem.popMatrix();
		
		
		for(int i=0;i< keychains.length;i++) {
			if(keychains[i] != null) {
				keychains[i].visible = false;
			}
		}
		
		for(int i=page*8;i< page*8+8;i++) {
			if(i < Lists.recipes.size() && keychains[i] != null) {
				keychains[i].visible = true;
				keychains[i].y = ((i/8)-(i/8)+3+(i%8))*18;
				keychains[i].x = (int) (width * 0.04F);
			}
		}
		
		
		if(selectedKC > -1) {
			RenderSystem.pushMatrix();
			{
				ItemStack kcStack = minecraft.player.inventory.getStackInSlot(getIndexFromMapIndex(selectedKC));
					
				KeychainItem kc = (KeychainItem) kcStack.getItem();
				KeybladeItem item = kc.getKeyblade();
				
				RenderSystem.pushMatrix();
				{
					String text = Utils.translateToLocal(item.getName().getFormattedText()+" ["+item.getKeybladeLevel(kcStack)+"]");
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
					if(item.getKeybladeLevel(kcStack) < 10) {
						RenderSystem.translated(width*0.75F, height*0.6, 1);
						int actualStr = item.getStrength(item.getKeybladeLevel(kcStack));
						int nextStr = item.getStrength(item.getKeybladeLevel(kcStack)+1);
						int actualMag = item.getMagic(item.getKeybladeLevel(kcStack));
						int nextMag = item.getMagic(item.getKeybladeLevel(kcStack)+1);
						drawString(minecraft.fontRenderer, "Strength: "+actualStr+" --> "+nextStr, 0, 0, 0xFF0000);
						drawString(minecraft.fontRenderer, "Magic: "+actualMag+" --> "+nextMag, 0, 10, 0x4444FF);
						//drawString(minecraft.fontRenderer, "Ability: "+item.getAbility(), 0, 20, 0x0000FF);
					}
				}
				RenderSystem.popMatrix();
				
				//Materials display
				RenderSystem.pushMatrix();
				{
					
					RenderSystem.translated(width*0.4F, height*0.3, 1);
					
					if(item.getKeybladeLevel(kcStack) < 10) {
						Iterator<Entry<Material, Integer>> itMats = item.data.getLevelData(item.getKeybladeLevel(kcStack)).getMaterialList().entrySet().iterator();
						int i = 0;
						while(itMats.hasNext()) {
							Entry<Material, Integer> m = itMats.next();
							ItemStack stack = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(m.getKey().getMaterialName())),m.getValue());
							String n = Utils.translateToLocal(stack.getTranslationKey());
							//playerData.setMaterial(m.getKey(), 1);
							int color = playerData.getMaterialAmount(m.getKey()) >= m.getValue() ?  0x00FF00 : 0xFF0000;
							drawString(minecraft.fontRenderer, n+" x"+m.getValue()+" ("+playerData.getMaterialAmount(m.getKey())+")", 0, (i*16), color);
							itemRenderer.renderItemIntoGUI(stack, -17, (i*16)-4);
							i++;
						}
					}
				}
				RenderSystem.popMatrix();
				

			}
			RenderSystem.popMatrix();
		}
		
		int buttonX = (int)(width*0.25);
		
		RenderSystem.pushMatrix();
		{
			RenderSystem.scaled(1.5,1.5, 1);
			drawString(minecraft.fontRenderer, "SYNTHESISE", 2, 10, 0xFF9900);
		}
		RenderSystem.popMatrix();
	}
	
}

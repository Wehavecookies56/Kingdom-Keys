package online.kingdomkeys.kingdomkeys.client.gui.synthesis;

import java.awt.Color;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.gui.menu.GuiMenuButton;
import online.kingdomkeys.kingdomkeys.client.gui.menu.GuiMenuButton.ButtonType;
import online.kingdomkeys.kingdomkeys.client.gui.menu.GuiMenu_Background;
import online.kingdomkeys.kingdomkeys.item.KeybladeItem;
import online.kingdomkeys.kingdomkeys.lib.Lists;
import online.kingdomkeys.kingdomkeys.lib.Utils;
import online.kingdomkeys.kingdomkeys.synthesis.material.Material;

public class GuiMenu_Synthesis_Synthesise extends GuiMenu_Background {
	
	GuiMenuButton[] keyblades = new GuiMenuButton[Lists.recipes.size()];
	int selectedKB = -1;
		
	public GuiMenu_Synthesis_Synthesise() {
		super("Synthesis Synthesise", new Color(0, 255, 0));
		drawPlayerInfo = true;
	}

	protected void action(String string) {
		if(string.startsWith("s:")) {
			String[] data = string.split(":");
			selectedKB = getIndexFromName(data[1]);
		} else {
			switch(string) {
			/*case "synthesise":
				minecraft.world.playSound(minecraft.player, minecraft.player.getPosition(), ModSounds.menu_in.get(), SoundCategory.MASTER, 1.0f, 1.0f);
				minecraft.displayGuiScreen(new GuiMenu_Synthesis_Synthesise());
				break;
			case "forge":
				minecraft.world.playSound(minecraft.player, minecraft.player.getPosition(), ModSounds.menu_in.get(), SoundCategory.MASTER, 1.0f, 1.0f);
				minecraft.displayGuiScreen(new GuiMenu_Synthesis_Forge("Forge"));
				break;
			case "materials":
				minecraft.world.playSound(minecraft.player, minecraft.player.getPosition(), ModSounds.menu_in.get(), SoundCategory.MASTER, 1.0f, 1.0f);
				minecraft.displayGuiScreen(new GuiMenu_Synthesis_Materials("Materials"));
				break;*/
			}
		}
		updateButtons();
	}

	private int getIndexFromName(String name) {
		IPlayerCapabilities props = ModCapabilities.get(minecraft.player);
		for(int i = 0;i < props.getKnownRecipesList().size(); i++) {
			if(props.getKnownRecipesList().get(i).equals(name)) {
				return i;
			}
		}
		return -1;
	}

	private void updateButtons() {

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

		IPlayerCapabilities props = ModCapabilities.get(minecraft.player);
		
		for(int i = 0;i<props.getKnownRecipesList().size();i++) {
			String name = props.getKnownRecipesList().get(i);
			addButton(keyblades[i] = new GuiMenuButton((int) buttonPosX, button_statsY + (i * 18), (int) buttonWidth, Utils.translateToLocal(props.getKnownRecipesList().get(i)), ButtonType.BUTTON, (e) -> { action("s:"+name); }));
		}
		
		updateButtons();
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		//System.out.println(phase);
		//fill(125, ((-140 / 16) + 75) + 10, 200, ((-140 / 16) + 75) + 20, 0xFFFFFF);
		super.render(mouseX, mouseY, partialTicks);
		//System.out.println(selectedKB);
		if(selectedKB > -1) {
			IPlayerCapabilities props = ModCapabilities.get(minecraft.player);
			RenderSystem.pushMatrix();
			{
				String kb = props.getKnownRecipesList().get(selectedKB);
				String name = kb.substring("item.kingdomkeys.".length());
				ResourceLocation loc = new ResourceLocation(KingdomKeys.MODID, name);
				KeybladeItem item = (KeybladeItem) ForgeRegistries.ITEMS.getValue(loc);
				
				RenderSystem.pushMatrix();
				{
					RenderSystem.translated(width*0.75F, height*0.17, 1);
					RenderSystem.scaled(1,1,1);
					drawString(minecraft.fontRenderer, Utils.translateToLocal(props.getKnownRecipesList().get(selectedKB)), 2, 10, 0xFF9900);
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
					drawString(minecraft.fontRenderer, "Strength: "+item.getStrength()+" ["+(props.getStrength()+item.getStrength())+"]", 2, 0, 0xFF0000);
					drawString(minecraft.fontRenderer, "Magic: "+item.getMagic()+" ["+(props.getMagic()+item.getMagic())+"]", 2, 10, 0x0000FF);
					
					//drawString(minecraft.fontRenderer, "Ability: "+item.getAbility(), 2, 20, 0x0000FF);
				}
				RenderSystem.popMatrix();
				
				RenderSystem.pushMatrix();
				{
					RenderSystem.translated(width*0.4F, height*0.3, 1);
					Iterator<Entry<Material, Integer>> materials = item.data.getLevelData(item.getKeybladeLevel()).getMaterialList().entrySet().iterator();
					//for(int i = 0;i<materials.size();i++) {
					int i = 0;
						while(materials.hasNext()) {
							Entry<Material, Integer> m = materials.next();
							if(m.getKey() != null) {
								String n = Utils.translateToLocal(m.getKey().getMaterialName());
								drawString(minecraft.fontRenderer, n+" "+m.getValue(), 2, (i*10), 0xFFFF00);
								i++;
							}
						}
					//}
					
					//drawString(minecraft.fontRenderer, "Materials: ", 2, 20, 0xFF0000);
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

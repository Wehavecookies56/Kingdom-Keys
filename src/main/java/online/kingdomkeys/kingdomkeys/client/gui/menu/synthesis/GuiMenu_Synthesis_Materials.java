package online.kingdomkeys.kingdomkeys.client.gui.menu.synthesis;

import java.awt.Color;
import java.util.Iterator;
import java.util.Map.Entry;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.registries.ForgeRegistries;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.gui.menu.GuiMenuButton;
import online.kingdomkeys.kingdomkeys.client.gui.menu.GuiMenuButton.ButtonType;
import online.kingdomkeys.kingdomkeys.client.gui.menu.GuiMenu_Background;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.lib.Lists;
import online.kingdomkeys.kingdomkeys.lib.Utils;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSDepositMaterials;
import online.kingdomkeys.kingdomkeys.synthesis.material.Material;
import online.kingdomkeys.kingdomkeys.synthesis.material.ModMaterials;

public class GuiMenu_Synthesis_Materials extends GuiMenu_Background {
		
	GuiMenuButton deposit,back;
	Button prev,next;
	int page = 0;
		
	public GuiMenu_Synthesis_Materials() {
		super("Synthesis",new Color(0,255,0));
		drawPlayerInfo = true;
	}

	protected void action(String string) {
		switch(string) {
		case "prev":
			page--;
			minecraft.world.playSound(minecraft.player, minecraft.player.getPosition(), ModSounds.menu_in.get(), SoundCategory.MASTER, 1.0f, 1.0f);
			break;
		case "next":
			page++;
			minecraft.world.playSound(minecraft.player, minecraft.player.getPosition(), ModSounds.menu_in.get(), SoundCategory.MASTER, 1.0f, 1.0f);
			break;
		case "deposit":
			minecraft.world.playSound(minecraft.player, minecraft.player.getPosition(), ModSounds.menu_in.get(), SoundCategory.MASTER, 1.0f, 1.0f);
			
			ClientPlayerEntity player = minecraft.player;
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			for(int i = 0; i < player.inventory.getSizeInventory();i++) {
				ItemStack stack = player.inventory.getStackInSlot(i);
				
				if(!ItemStack.areItemStacksEqual(stack, ItemStack.EMPTY)) {
					//System.out.println(stack.getItem().getRegistryName().getPath());
					if(ModMaterials.registry.getValue(new ResourceLocation(KingdomKeys.MODID,"mat_"+stack.getItem().getRegistryName().getPath())) != null) {
						Material mat = ModMaterials.registry.getValue(new ResourceLocation(KingdomKeys.MODID,"mat_"+stack.getItem().getRegistryName().getPath()));
						playerData.addMaterial(mat, stack.getCount());
						player.inventory.setInventorySlotContents(i, ItemStack.EMPTY);
						//System.out.println("mat");
					}
				}
			}
			PacketHandler.sendToServer(new CSDepositMaterials());
			break;
		case "back":
			minecraft.displayGuiScreen(null);
			break;
		}
		
		updateButtons();
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
		addButton(prev = new Button((int) buttonPosX+10, button_statsY + (-1 * 18), 30,20, Utils.translateToLocal("<--"), (e) -> { action("prev"); }));
		addButton(next = new Button((int) buttonPosX+10+80, button_statsY + (-1 * 18), 30,20, Utils.translateToLocal("-->"), (e) -> { action("next"); }));

		addButton(deposit = new GuiMenuButton((int) buttonPosX, button_statsY + (0 * 18), (int) buttonWidth, Utils.translateToLocal("Deposit Materials"), ButtonType.BUTTON, (e) -> { action("deposit"); }));
		addButton(back = new GuiMenuButton((int) buttonPosX, button_statsY + (1 * 18), (int) buttonWidth, Utils.translateToLocal("Back"), ButtonType.BUTTON, (e) -> { action("back"); }));
		
		updateButtons();
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		//System.out.println(phase);
		//fill(125, ((-140 / 16) + 75) + 10, 200, ((-140 / 16) + 75) + 20, 0xFFFFFF);
		super.render(mouseX, mouseY, partialTicks);
		
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);
		int matsPerPage = 9;

		prev.visible = page > 0;
		next.visible = page < playerData.getMaterialMap().size()/matsPerPage;
		
		RenderSystem.pushMatrix();
		{
			RenderSystem.translated(width * 0.03F+45, (height*0.17) - 18, 1);
			RenderSystem.scaled(1,1,1);
			drawString(minecraft.fontRenderer, Utils.translateToLocal("Page: "+(page+1)), 0, 10, 0xFF9900);
		}
		RenderSystem.popMatrix();
		
		Iterator<Entry<String, Integer>> itMats = playerData.getMaterialMap().entrySet().iterator();
		int i = 0;
		RenderSystem.pushMatrix();
		{
			RenderSystem.translated(width*0.4F, height*0.2, 1);

			while(itMats.hasNext()) {
				Entry<String, Integer> m = itMats.next();
				if(i>page*matsPerPage-1 && i < page*matsPerPage+matsPerPage) {
					ItemStack stack = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(m.getKey())),m.getValue());
					String n = Utils.translateToLocal(stack.getTranslationKey());
					drawString(minecraft.fontRenderer, Utils.translateToLocal(n)+ " x"+stack.getCount(), 2, (int) ((i-matsPerPage*page)*16), 0xFFFF00);
					itemRenderer.renderItemIntoGUI(stack, -17, ((i-matsPerPage*page)*16)-5);
				}
				i++;
			}
		
		}
		RenderSystem.popMatrix();
		
		RenderSystem.pushMatrix();
		{
			RenderSystem.scaled(1.5,1.5, 1);
			drawString(minecraft.fontRenderer, "Materials", 2, 10, 0xFF9900);
		}
		RenderSystem.popMatrix();
	}
	
}

package online.kingdomkeys.kingdomkeys.client.gui.synthesis;

import java.awt.Color;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.gui.menu.GuiMenuButton;
import online.kingdomkeys.kingdomkeys.client.gui.menu.GuiMenuButton.ButtonType;
import online.kingdomkeys.kingdomkeys.client.gui.menu.GuiMenu_Background;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.lib.Utils;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSDepositMaterials;
import online.kingdomkeys.kingdomkeys.synthesis.material.Material;
import online.kingdomkeys.kingdomkeys.synthesis.material.ModMaterials;

public class GuiMenu_Synthesis_Materials extends GuiMenu_Background {
		
	GuiMenuButton deposit;
	
		
	public GuiMenu_Synthesis_Materials() {
		super("Synthesis",new Color(0,255,0));
		drawPlayerInfo = true;
	}

	protected void action(String string) {
		switch(string) {
		case "deposit":
			minecraft.world.playSound(minecraft.player, minecraft.player.getPosition(), ModSounds.menu_in.get(), SoundCategory.MASTER, 1.0f, 1.0f);
			
			ClientPlayerEntity player = minecraft.player;
			IPlayerCapabilities props = ModCapabilities.get(player);
			for(int i = 0; i < player.inventory.getSizeInventory();i++) {
				ItemStack stack = player.inventory.getStackInSlot(i);
				
				if(stack != null) {
					//System.out.println(stack.getItem().getRegistryName().getPath());
					if(ModMaterials.registry.getValue(new ResourceLocation(KingdomKeys.MODID,"mat_"+stack.getItem().getRegistryName().getPath())) != null) {
						Material mat = ModMaterials.registry.getValue(new ResourceLocation(KingdomKeys.MODID,"mat_"+stack.getItem().getRegistryName().getPath()));
						props.addMaterial(mat, stack.getCount());
						player.inventory.setInventorySlotContents(i, ItemStack.EMPTY);
						System.out.println("mat");
					}
				}
			}
			PacketHandler.sendToServer(new CSDepositMaterials());
			//minecraft.displayGuiScreen(new GuiMenu_Synthesis_Synthesise());
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

		addButton(deposit = new GuiMenuButton((int) buttonPosX, button_statsY + (0 * 18), (int) buttonWidth, Utils.translateToLocal("Deposit Materials"), ButtonType.BUTTON, (e) -> { action("deposit"); }));
		
		updateButtons();
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		//System.out.println(phase);
		//fill(125, ((-140 / 16) + 75) + 10, 200, ((-140 / 16) + 75) + 20, 0xFFFFFF);
		super.render(mouseX, mouseY, partialTicks);
		
		int buttonX = (int)(width*0.25);
		
		RenderSystem.pushMatrix();
		{
			RenderSystem.scaled(1.5,1.5, 1);
			drawString(minecraft.fontRenderer, "Materials", 2, 10, 0xFF9900);
		}
		RenderSystem.popMatrix();
	}
	
}

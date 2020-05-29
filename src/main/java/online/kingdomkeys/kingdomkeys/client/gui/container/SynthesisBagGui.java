package online.kingdomkeys.kingdomkeys.client.gui.container;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.container.SynthesisBagContainer;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.packet.PacketUpgradeSynthesisBag;

public class SynthesisBagGui extends ContainerScreen<SynthesisBagContainer> {

	private static final String textureBase = KingdomKeys.MODID+":textures/gui/synthesis_bag_";
	int[] texHeight = {140, 176, 212};
	int bagLevel = 0;
	Button upgrade;

	public SynthesisBagGui(SynthesisBagContainer container, PlayerInventory playerInv, ITextComponent title) {
		super(container, playerInv, title);
	}

	@Override
	protected void init() {
		CompoundNBT nbt = playerInventory.getCurrentItem().getOrCreateTag();
		bagLevel = nbt.getInt("level");
		Minecraft.getInstance().getMainWindow().getHeight();
		this.ySize = texHeight[bagLevel];
		addButton(upgrade = new Button(20, 20, 60, 20, "Upgrade", (e) -> { upgrade(); }));
		super.init();
	}

	private void upgrade() {
		if(bagLevel < 2) {
			PacketHandler.sendToServer(new PacketUpgradeSynthesisBag());
		    this.minecraft.displayGuiScreen((Screen)null);
		}
	}
	
	@Override
	public void onClose() {
		// TODO Auto-generated method stub
		super.onClose();
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		this.renderBackground();
		super.render(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
		
		upgrade.active = bagLevel < 2;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		String s = I18n.format("Synthesis Bag LV."+(bagLevel+1));
		font.drawString(s, xSize / 2 - font.getStringWidth(s) / 2, 5, 4210752);
		//font.drawString(I18n.format("container.inventory"), 8, ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		Minecraft mc = Minecraft.getInstance();
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(new ResourceLocation(textureBase+bagLevel+".png"));

		int xPos = (width - xSize) / 2;
		int yPos = (height / 2) - (ySize / 2);
		blit(xPos, yPos, 0, 0, xSize, ySize);

		/*for (Slot slot : container.inventorySlots) {
			if (slot instanceof SlotItemHandler && !slot.getHasStack()) {
				ItemStack stack = new ItemStack(ModBlocks.blazingOre);
				int x = guiLeft + slot.xPos;
				int y = guiTop + slot.yPos;
				//mc.getItemRenderer().renderItemIntoGUI(stack, x, y);
				mc.fontRenderer.drawStringWithShadow("0", x + 11, y + 9, 0xFF6666);
			}
		}*/
	}

}

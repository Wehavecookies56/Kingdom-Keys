package online.kingdomkeys.kingdomkeys.client.gui.castle_oblivion;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.CardSelectButton;
import online.kingdomkeys.kingdomkeys.entity.block.CardDoorTileEntity;
import online.kingdomkeys.kingdomkeys.item.card.MapCardItem;

public class CardSelectionScreen extends MenuBackground {

	public CardDoorTileEntity te;
	private List<CardSelectButton> cards = new ArrayList<>();
	
	public CardSelectionScreen(CardDoorTileEntity te) {
		super("Card Selection", new Color(100,100,100));
		drawBiome = false;
		this.te = te;
		this.minecraft = Minecraft.getInstance();
	}
	
	@Override
	public void init() {
		int x = 0;
		int y = 2;
		for(int i = 0; i< minecraft.player.getInventory().getContainerSize();i++) {
			ItemStack stack = minecraft.player.getInventory().getItem(i);
				if(!ItemStack.isSame(stack, ItemStack.EMPTY) && stack.getItem() instanceof MapCardItem card) {
					CardSelectButton c = new CardSelectButton((3+x++) * 42, y * 50, 42, 42, stack, this, (e) -> {
						System.out.println(((MapCardItem)stack.getItem()).getCardValue(stack));
					});
					c.visible = true;
					cards.add(c);
				}
				if(x == 6) {
					x = 0;
					y++;
				}

		}
		cards.forEach(this::addWidget);

		super.init();
	}
	
	@Override
	public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		drawString(matrixStack, minecraft.font,"Is opened? "+te.isOpen(), 20, 50, 0xFF9900);
		drawString(matrixStack, minecraft.font,"Cost: "+te.getNumber(), 20, 60, 0xFF9900);
		
		for (int i = 0; i < cards.size(); i++) {
			cards.get(i).visible = true;
			cards.get(i).render(matrixStack, mouseX, mouseY, partialTicks);
		}
	}
}
package online.kingdomkeys.kingdomkeys.client.gui.castle_oblivion;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import online.kingdomkeys.kingdomkeys.capability.CastleOblivionCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.client.gui.GuiHelper;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.CardSelectButton;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton.ButtonType;
import online.kingdomkeys.kingdomkeys.entity.block.CardDoorTileEntity;
import online.kingdomkeys.kingdomkeys.item.card.MapCardItem;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSGenerateRoom;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.Room;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.RoomData;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.RoomGenerator;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.RoomType;

public class CardSelectionScreen extends MenuBackground {

	public CardDoorTileEntity te;
	private List<CardSelectButton> cards = new ArrayList<>();
	private ItemStack selectedCardStack;
	private MenuButton createBtn;
	
	
	public CardSelectionScreen(CardDoorTileEntity te) {
		super("Card Selection", new Color(100,100,100));
		this.te = te;
		this.minecraft = Minecraft.getInstance();
	}

	@Override
	public void init() {
		int x = 0;
		int y = 0;
		cards.clear();
		
		for (int i = 0; i < minecraft.player.getInventory().getContainerSize(); i++) {
			ItemStack stack = minecraft.player.getInventory().getItem(i);
			if (!ItemStack.isSame(stack, ItemStack.EMPTY) && stack.getItem() instanceof MapCardItem card) {
				CardSelectButton c = new CardSelectButton((int)(width * 0.25F+(x++ * 42)), (int)(height * 0.5F + y * 50), 42, 50, stack, this, (e) -> {
					System.out.println(((MapCardItem) stack.getItem()).getCardValue(stack));
					selectedCardStack = stack;
				});
				cards.add(c);
			}
			if (x == 6) {
				x = 0;
				y++;
			}
		}
		cards.forEach(this::addWidget);
		
		super.init();
		
		addRenderableWidget(createBtn = new MenuButton((int) (width*0.79), (int)(height * 0.6), (int) buttonWidth, Utils.translateToLocal("create"), ButtonType.BUTTON, (e) -> {
			int slot = -1;
			for(int i=0; i< minecraft.player.getInventory().getContainerSize();i++) {
				ItemStack stack = minecraft.player.getInventory().getItem(i);
				if(stack == selectedCardStack) {
					slot = i;
					break;
				}
			}
			PacketHandler.sendToServer(new CSGenerateRoom(selectedCardStack, slot, te.getBlockPos()));
			selectedCardStack.shrink(1);
			
			Level level = minecraft.level;
            CastleOblivionCapabilities.ICastleOblivionInteriorCapability cap = ModCapabilities.getCastleOblivionInterior(level);
			Room currentRoom = cap.getRoomAtPos(te.getBlockPos());
            te.openDoor(null, currentRoom, null);
            minecraft.setScreen(null);
		}));
		createBtn.visible = false;

	}

	@Override
	public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		drawString(matrixStack, minecraft.font,"Is opened? "+te.isOpen(), 20, 50, 0xFF9900);
		drawString(matrixStack, minecraft.font,"Cost: "+te.getNumber(), 20, 60, 0xFF9900);
		
		for (int i = 0; i < cards.size(); i++) {
			cards.get(i).render(matrixStack, mouseX, mouseY, partialTicks);
		}
		
		if(selectedCardStack != null) {
			matrixStack.pushPose();
			{
	        	matrixStack.translate(width*0.83,height * 0.3, 0);

				drawCenteredString(matrixStack, minecraft.font,Utils.translateToLocal(selectedCardStack.getItem().getName(selectedCardStack).getString()), 26, -20, 0xFFFFFF);

        		matrixStack.scale(5,5, 1);
            	matrixStack.translate(-2.5,-2.5, 20);

				ClientUtils.drawItemAsIcon(selectedCardStack, matrixStack, 0,0, 16);
	    	}
			matrixStack.popPose();
			createBtn.active = true;
			createBtn.visible = true;
			//createBtn.render(matrixStack, mouseX, mouseY, partialTicks);
		} else {
			createBtn.active = false;
			createBtn.visible = false;
		}
		
	}
}
package online.kingdomkeys.kingdomkeys.client.gui.castle_oblivion;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.CardSelectButton;
import online.kingdomkeys.kingdomkeys.data.CastleOblivionData;
import online.kingdomkeys.kingdomkeys.entity.block.CardDoorTileEntity;
import online.kingdomkeys.kingdomkeys.item.card.CardCategory;
import online.kingdomkeys.kingdomkeys.item.card.KeycardType;
import online.kingdomkeys.kingdomkeys.lib.ModTags;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSConsumeCard;
import online.kingdomkeys.kingdomkeys.network.cts.CSGenerateRoom;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.DoorData;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.Room;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class RoomSynthesisScreen extends MenuBackground {

	public final CardDoorTileEntity te;
	private final List<CardSelectButton> cards = new ArrayList<>();
	private final DoorData.Type doorType;
	
	
	public RoomSynthesisScreen(CardDoorTileEntity te) {
		super("Room Synthesis", new Color(100,100,100));
		this.te = te;
		this.minecraft = Minecraft.getInstance();
		this.doorType = te.getData().getType();
		if (te.getCurrentCriteria() == null || te.getCurrentCriteria().isEmpty()) {
			te.setCurrentCriteria(te.getData().getCardCriteria());
		}
	}

	@Override
	public void init() {
		int x = 0;
		int y = 0;
		cards.clear();
		
		for (int i = 0; i < minecraft.player.getInventory().getContainerSize(); i++) {
			ItemStack stack = minecraft.player.getInventory().getItem(i);
			if (!stack.isEmpty() && (stack.is(ModTags.MAP_CARD) || stack.is(ModTags.KEY_CARD))) {
				CardSelectButton c = new CardSelectButton((int)(width * 0.25F+(x++ * 42)), (int)(height * 0.5F + y * 50), 42, 50, stack, this, (e) -> {
					ItemStack copyStack = stack.copy();
					PacketHandler.sendToServer(new CSConsumeCard(te.getBlockPos(), copyStack));
					te.consumeCard(stack);
					updateCards();
					if (te.getCurrentCriteria().isEmpty()) {
						if (doorType == DoorData.Type.NORMAL) {
							PacketHandler.sendToServer(new CSGenerateRoom(copyStack, te.getBlockPos()));
						} else if (doorType == DoorData.Type.KEY) {
							DoorData.CardCriteria criteria = te.getData().getCardCriteria().get(CardCategory.YELLOW);
							ItemStack keycard = ItemStack.EMPTY;
							if (criteria != null) {
								keycard = new ItemStack(KeycardType.values()[criteria.value()].getCardForType());
							}
							PacketHandler.sendToServer(new CSGenerateRoom(keycard, te.getBlockPos()));
						}
						Level level = minecraft.level;
						CastleOblivionData.InteriorData cap = CastleOblivionData.InteriorData.getClient((ClientLevel) level);
						Room currentRoom = cap.getRoomAtPos(te.getBlockPos());
						te.openDoor(true);
						minecraft.setScreen(null);
					}
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

	}

	void updateCards() {
		CardSelectButton toRemove = null;
		for (CardSelectButton button : cards) {
			if (button.stack.isEmpty()) {
				toRemove = button;
				break;
			}
		}
		if (toRemove != null) {
			cards.remove(toRemove);
			removeWidget(toRemove);
			int x = 0;
			int y = 0;
			for (CardSelectButton cardSelectButton : cards) {
				cardSelectButton.setX((int)(width * 0.25F+(x++ * 42)));
				cardSelectButton.setY((int)(height * 0.5F + y * 50));
				if (x == 6) {
					x = 0;
					y++;
				}
			}
		}

	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
		//guiGraphics.drawString(minecraft.font,"Is opened? "+te.isOpen(), 20, 50, 0xFF9900);

			int criteriaX = width/4;
			int criteriaY = 120;
			for (var critera : te.getCurrentCriteria().entrySet()) {
				guiGraphics.pose().pushPose();
				{
					guiGraphics.pose().translate(criteriaX, criteriaY, 1);
					guiGraphics.pose().scale(3, 3, 1);
					switch (critera.getKey()) {
						case YELLOW -> {
							ClientUtils.drawItemAsIcon(new ItemStack(KeycardType.values()[te.getData().getCardCriteria().get(CardCategory.YELLOW).value()].getCardForType()), guiGraphics.pose(), 8, 8, 32);
							criteriaX += 72;
						}
						case RGB, RED, GREEN, BLUE -> {
							Color colour = switch (critera.getKey()) {
								case RED -> Color.RED;
								case GREEN -> Color.GREEN;
								case BLUE -> Color.BLUE;
								case RGB -> Color.WHITE;
								default -> throw new IllegalStateException("Unexpected value: " + critera.getKey());
							};
							RenderSystem.setShaderColor(colour.getRed(), colour.getGreen(), colour.getBlue(), 1);
							guiGraphics.blit(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/gui/co/card_outline.png"), 0, 0, 0, 0, 32, 32, 32, 32);
							RenderSystem.setShaderColor(1, 1, 1, 1);
							guiGraphics.drawString(minecraft.font, Component.literal(critera.getValue().toString()).withStyle(ClientUtils.KK_Font_EXP), 9, 14, 0xFFFFFF, false);
							criteriaX += 72;
						}
					}
				}
				guiGraphics.pose().popPose();
			}

		for (int i = 0; i < cards.size(); i++) {
			cards.get(i).render(guiGraphics, mouseX, mouseY, partialTicks);
		}
		
	}
}
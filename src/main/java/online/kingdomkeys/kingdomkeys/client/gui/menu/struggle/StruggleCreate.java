package online.kingdomkeys.kingdomkeys.client.gui.menu.struggle;

import java.awt.Color;

import org.jetbrains.annotations.NotNull;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.IWorldCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton.ButtonType;
import online.kingdomkeys.kingdomkeys.client.gui.menu.party.GuiMenu_Party_Leader;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSPartyCreate;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class StruggleCreate extends MenuBackground {
	BlockPos boardPos;

	boolean priv = false;
	byte pSize = Party.PARTY_LIMIT;
	
	EditBox tfName;
	Button togglePriv, accept, size;
	MenuButton back;
		
	final IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);
	IWorldCapabilities worldData;
	
	Party party;
		
	public StruggleCreate(BlockPos pos) {
		super("Start Struggle", new Color(252, 173, 3));
		drawPlayerInfo = true;
		worldData = ModCapabilities.getWorld(minecraft.level);
		this.boardPos = pos;
	}

	protected void action(String string) {
		switch(string) {
		case "back":
			minecraft.level.playSound(minecraft.player, minecraft.player.blockPosition(), ModSounds.menu_in.get(), SoundSource.MASTER, 1.0f, 1.0f);
			minecraft.setScreen(new MenuStruggle(boardPos));
			break;
		case "togglePriv":
			priv = !priv;
			break;
		case "accept":
			if(!tfName.getValue().equals("") && checkAvailable()) { //Accept Party creation
				Party localParty = new Party(tfName.getValue(), minecraft.player.getUUID(), minecraft.player.getName().getString(), priv, Byte.parseByte(size.getMessage().getString()));
				PacketHandler.sendToServer(new CSPartyCreate(localParty));
				
				minecraft.level.playSound(minecraft.player, minecraft.player.blockPosition(), ModSounds.menu_in.get(), SoundSource.MASTER, 1.0f, 1.0f);
				minecraft.setScreen(new GuiMenu_Party_Leader());
			}
			break;
		case "size":
			if(pSize == Party.PARTY_LIMIT) {
				pSize = 2;
			} else {
				pSize++;
			}
			size.setMessage(Component.translatable(pSize+""));
			break;
		}
		
		updateButtons();
	}

	private void updateButtons() {
		//IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);
		togglePriv.setMessage(priv ? Component.translatable(Utils.translateToLocal(Strings.Gui_Menu_Party_Create_Accessibility_Private)) : Component.translatable(Utils.translateToLocal(Strings.Gui_Menu_Party_Create_Accessibility_Public)));

		
		//TBName
		togglePriv.visible = true;
		accept.visible = true;
		tfName.visible = true;
		size.visible = true;
	}

	@Override
	public void init() {
		//TODO request packet to sync other players data
		super.init();
		this.renderables.clear();
		
		party = worldData.getPartyFromMember(minecraft.player.getUUID());
		
		float topBarHeight = (float) height * 0.17F;
		int button_statsY = (int) topBarHeight + 5;
		float buttonPosX = (float) width * 0.03F;
		float buttonWidth = ((float) width * 0.1744F) - 20;


		addRenderableWidget(togglePriv = Button.builder(Component.translatable(""), (e) -> {
			action("togglePriv");
		}).bounds((int) (width*0.25)-2, button_statsY + (3 * 18), 100, 20).build());
		
		addRenderableWidget(accept = Button.builder(Component.translatable(Utils.translateToLocal(Strings.Gui_Menu_Accept)), (e) -> {
			action("accept");
		}).bounds((int) (width*0.25)-2, button_statsY + (5 * 18), (int) 100, 20).build());
		
		addRenderableWidget(size = Button.builder(Component.translatable(Party.PARTY_LIMIT+""), (e) -> {
			action("size");
		}).bounds((int) (width * 0.25 - 2 + 100 + 4), button_statsY + (3 * 18), (int) 20, 20).build());
		
		addRenderableWidget(back = new MenuButton((int) buttonPosX, button_statsY + (0 * 18), (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Menu_Back), ButtonType.BUTTON, (e) -> { action("back"); }));
		
		addRenderableWidget(tfName = new EditBox(minecraft.font, (int)(width*0.25), (int)(height*0.25), 100, 15, Component.translatable("")) {
			@Override
			public boolean charTyped(char c, int i) {
				super.charTyped(c, i);
				checkAvailable();
				return true;
			}
			
			@Override
			public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
				super.keyPressed(keyCode, scanCode, modifiers);
				checkAvailable();
				return true;
			}
			
		});
		
		updateButtons();
	}
	
	private boolean checkAvailable() {
		if(tfName.getValue() != null && !tfName.getValue().equals("")) {
			Party p = worldData.getPartyFromName(tfName.getValue());
			accept.active = p == null;	
			return p == null;
		}
		return false;
	}

	@Override
	public void render(@NotNull GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
		
		//fill(125, ((-140 / 16) + 75) + 10, 200, ((-140 / 16) + 75) + 20, 0xFFFFFF);
		super.render(gui, mouseX, mouseY, partialTicks);
		worldData = ModCapabilities.getWorld(minecraft.level);
		party = worldData.getPartyFromMember(minecraft.player.getUUID());
		
		int buttonX = (int)(width*0.25);
		
		gui.drawString(minecraft.font, Utils.translateToLocal(Strings.Gui_Menu_Party_Create_Name), buttonX, (int)(height * 0.2), 0xFFFFFF);
		gui.drawString(minecraft.font, Utils.translateToLocal(Strings.Gui_Menu_Party_Create_Accessibility), buttonX, (int)(height * 0.35), 0xFFFFFF);
	}
	
}

package online.kingdomkeys.kingdomkeys.client.gui.menu.party;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBox;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton.ButtonType;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSPartyCreate;
import online.kingdomkeys.kingdomkeys.util.Utils;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class GuiMenu_Party_Create extends MenuBackground {

	boolean priv = false;
	int pSize = ModConfigs.SERVER.partyMembersLimit.get();
	MenuBox box;
	EditBox tfName;
	MenuButton togglePriv, accept, size;
	MenuButton back;
		
	final PlayerData playerData = PlayerData.get(minecraft.player);
	WorldData worldData;
	
	Party party;
		
	public GuiMenu_Party_Create() {
		super(Strings.Gui_Menu_Party_Create, new Color(0,0,255));
		drawPlayerInfo = true;
		worldData = WorldData.getClient();
	}

	protected void action(String string) {
		switch(string) {
		case "back":
			minecraft.level.playSound(minecraft.player, minecraft.player.blockPosition(), ModSounds.menu_in.get(), SoundSource.MASTER, 1.0f, 1.0f);
			minecraft.setScreen(new GuiMenu_Party_None());
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
			//System.out.println(ModConfigs.partyMembersLimit);
			if(pSize == ModConfigs.SERVER.partyMembersLimit.get()) {
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

		box = new MenuBox((int)(width*0.25F), (int)topBarHeight, (int)(width*0.5F), (int) middleHeight,0.8F, new Color(255,128,255));

		addRenderableWidget(togglePriv = new MenuButton(box.getX() + 10, button_statsY + (3 * 18), 80, "", ButtonType.ROUNDBUTTON, (e) -> { action("togglePriv"); }).setCenterText());
		addRenderableWidget(size = new MenuButton(togglePriv.getX() + togglePriv.getWidth(), button_statsY + (3 * 18), 0, ModConfigs.SERVER.partyMembersLimit.get()+"", ButtonType.ROUNDBUTTON,(e) -> { action("size"); }).setCenterText());
		addRenderableWidget(accept = new MenuButton(togglePriv.getX(), button_statsY + (5 * 18), 110, Strings.Gui_Menu_Accept, ButtonType.ROUNDBUTTON,(e) -> { action("accept"); }).setCenterText());

		addRenderableWidget(back = new MenuButton((int) buttonPosX, button_statsY, (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Menu_Back), ButtonType.BUTTON, (e) -> { action("back"); }));
		
		addRenderableWidget(tfName = new EditBox(minecraft.font, togglePriv.getX(), (int)(height*0.25), 100, 15, Component.literal("")) {
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
		box.render(gui, mouseX, mouseY, partialTicks);
		super.render(gui, mouseX, mouseY, partialTicks);

		worldData = WorldData.getClient();
		party = worldData.getPartyFromMember(minecraft.player.getUUID());
		
		int buttonX = togglePriv.getX();
		
		gui.drawString(minecraft.font, Utils.translateToLocal(Strings.Gui_Menu_Party_Create_Name), buttonX, (int)(height * 0.2), 0xFFFFFF);
		gui.drawString(minecraft.font, Utils.translateToLocal(Strings.Gui_Menu_Party_Create_Accessibility), buttonX, (int)(height * 0.35), 0xFFFFFF);
	}
	
}

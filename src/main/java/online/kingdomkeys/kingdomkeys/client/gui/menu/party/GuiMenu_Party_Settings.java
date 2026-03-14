package online.kingdomkeys.kingdomkeys.client.gui.menu.party;

import net.minecraft.client.gui.GuiGraphics;
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
import online.kingdomkeys.kingdomkeys.network.cts.CSOpenMenu;
import online.kingdomkeys.kingdomkeys.network.cts.CSPartySettings;
import online.kingdomkeys.kingdomkeys.util.Utils;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class GuiMenu_Party_Settings extends MenuBackground {

	boolean priv = false, friendlyFire = false;
	int pSize = ModConfigs.SERVER.partyMembersLimit.get();
	MenuBox box;
	MenuButton togglePriv, toggleFF, accept, size;
	MenuButton back;
		
	final PlayerData playerData = PlayerData.get(minecraft.player);
	WorldData worldData;
	
	Party party;
		
	public GuiMenu_Party_Settings() {
		super(Strings.Gui_Menu_Party_Leader_Settings, new Color(0,0,255));
		drawPlayerInfo = true;
		worldData = WorldData.getClient();
	}

	protected void action(String string) {
		switch(string) {
		case "back":
			minecraft.level.playSound(minecraft.player, minecraft.player.blockPosition(), ModSounds.menu_in.get(), SoundSource.MASTER, 1.0f, 1.0f);
			minecraft.setScreen(new GuiMenu_Party_Leader());
			break;
		case "togglePriv":
			priv = !priv;
			break;
		case "ff":
			friendlyFire = !friendlyFire;
			break;
		case "accept":
			party.setPriv(priv);
			party.setSize((byte)pSize);
			party.setFriendlyFire(friendlyFire);
			PacketHandler.sendToServer(new CSPartySettings(party));
			
			minecraft.level.playSound(minecraft.player, minecraft.player.blockPosition(), ModSounds.menu_in.get(), SoundSource.MASTER, 1.0f, 1.0f);
			minecraft.setScreen(new GuiMenu_Party_Leader());
			
			break;
		case "size":
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
		toggleFF.setMessage(Component.translatable(friendlyFire+""));// ? new TranslationTextComponent(Utils.translateToLocal("FF")) : new TranslationTextComponent(Utils.translateToLocal(Strings.Gui_Menu_Party_Create_Accessibility_Public)));

		size.setMessage(Component.translatable(pSize+""));

		//TBName
		togglePriv.visible = true;
		toggleFF.visible = true;
		accept.visible = true;
		size.visible = true;
	}

	@Override
	public void init() {
		super.init();
		this.renderables.clear();
		
		party = worldData.getPartyFromMember(minecraft.player.getUUID());
		if(party != null) {
			priv = party.getPriv();
			pSize = party.getSize();
			friendlyFire = party.getFriendlyFire();

			topBarHeight = (float) height * 0.17F;
			int button_statsY = (int) topBarHeight + 5;
			float buttonPosX = (float) width * 0.03F;
			float buttonWidth = ((float) width * 0.1744F) - 20;

			box = new MenuBox((int)(width*0.25F), (int)topBarHeight, (int)(width*0.5F), (int) middleHeight,0.8F, new Color(255,128,255));

			addRenderableWidget(togglePriv = new MenuButton(box.getX() + 10, button_statsY + (18), 80, "", ButtonType.ROUNDBUTTON, (e) -> { action("togglePriv"); }).setCenterText());
			addRenderableWidget(size = new MenuButton(togglePriv.getX() + togglePriv.getWidth(), button_statsY + (18), 0, "", ButtonType.ROUNDBUTTON,(e) -> { action("size"); }).setCenterText());
			addRenderableWidget(toggleFF = new MenuButton(togglePriv.getX(), button_statsY + (3 * 18), 80, "", ButtonType.ROUNDBUTTON,(e) -> { action("ff"); }).setCenterText());
			addRenderableWidget(accept = new MenuButton(togglePriv.getX(), button_statsY + (5 * 18), 110, Strings.Gui_Menu_Accept, ButtonType.ROUNDBUTTON,(e) -> { action("accept"); }).setCenterText());

			addRenderableWidget(back = new MenuButton((int) buttonPosX, button_statsY, (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Menu_Back), ButtonType.BUTTON, (e) -> { action("back"); }));
		}
		
		updateButtons();
	}

	@Override
	public void render(@NotNull GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
		box.render(gui, mouseX, mouseY, partialTicks);
		super.render(gui, mouseX, mouseY, partialTicks);
		worldData = WorldData.getClient();

		party = worldData.getPartyFromMember(minecraft.player.getUUID());
		if(party == null) {
			PacketHandler.sendToServer(new CSOpenMenu());
        } else {
			if(!party.getMember(minecraft.player.getUUID()).isLeader()) {
				minecraft.setScreen(new GuiMenu_Party_Member());
				return;
			}
			
			int buttonX = togglePriv.getX();
			gui.drawString(minecraft.font, Utils.translateToLocal(Strings.Gui_Menu_Party_Create_Accessibility), buttonX, (int)(height * 0.21), 0xFFFFFF);
			gui.drawString(minecraft.font, Utils.translateToLocal("Friendly Fire"), buttonX, (int)(height * 0.21) + 38, 0xFFFFFF);
		}
	}
}

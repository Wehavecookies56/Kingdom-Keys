package online.kingdomkeys.kingdomkeys.client.gui.menu.party;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.sounds.SoundSource;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBox;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton.ButtonType;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSPartyAddMember;
import online.kingdomkeys.kingdomkeys.util.Utils;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GuiMenu_Party_Join extends MenuBackground {
	MenuButton back;
	MenuBox box;
	PlayerData playerData = PlayerData.get(minecraft.player);
	WorldData worldData;

	private final List<MenuButton> partyButtons = new ArrayList<>();
	private final List<String> shown = new ArrayList<>();

	public GuiMenu_Party_Join() {
		super(Strings.Gui_Menu_Party_Join, new Color(0,0,255));
		drawPlayerInfo = true;
		worldData = WorldData.getClient();
	}

	protected void action(String string) {
		switch(string) {
		case "back":
			minecraft.level.playSound(minecraft.player, minecraft.player.blockPosition(), ModSounds.menu_in.get(), SoundSource.MASTER, 1.0f, 1.0f);
			minecraft.setScreen(new GuiMenu_Party_None());
			break;
		}
	}

	private void join(String partyName) {
		Party p = WorldData.getClient().getPartyFromName(partyName);

		if(p == null || p.getMembers().size() >= p.getSize()) {
			return;
		}

		PacketHandler.sendToServer(new CSPartyAddMember(p, minecraft.player));
		p.addMember(minecraft.player.getUUID(), minecraft.player.getGameProfile().getName());

		minecraft.level.playSound(minecraft.player, minecraft.player.blockPosition(), ModSounds.menu_in.get(), SoundSource.MASTER, 1.0f, 1.0f);
		minecraft.setScreen(new GuiMenu_Party_Member());
	}

	private void refreshParties() {
		playerData = PlayerData.get(minecraft.player);
		worldData = WorldData.getClient();

		List<String> privateParties = playerData.getPartiesInvited();

		List<Party> listed = new ArrayList<>();
		List<String> labels = new ArrayList<>();

		for(String privateParty : privateParties) {
			Party p = worldData.getPartyFromName(privateParty);

			if(p != null) {
				listed.add(p);
				labels.add("(P) [" + p.getMembers().size() + "/" + p.getSize() + "] " + p.getName());
			}
		}

		for(Party p : worldData.getParties()) {
			if(p != null && !p.getPriv() && !privateParties.contains(p.getName())) {
				listed.add(p);
				labels.add("[" + p.getMembers().size() + "/" + p.getSize() + "] " + p.getName());
			}
		}

		if(labels.equals(shown)) {
			return;
		}

		for(MenuButton button : partyButtons) {
			removeWidget(button);
		}

		partyButtons.clear();
		shown.clear();
		shown.addAll(labels);

		float topBarHeight = (float) height * 0.17F;
		int button_statsY = (int) topBarHeight + 5;
		int buttonWidth = box.getWidth() - 40;

		for(int i = 0; i < listed.size(); i++) {
			String name = listed.get(i).getName();

			MenuButton button = new MenuButton(box.getX() + 10, button_statsY + (i * 18), buttonWidth, labels.get(i), ButtonType.ROUNDBUTTON, (e) -> join(name));

			partyButtons.add(button);
			addRenderableWidget(button);
		}
	}

	@Override
	public void init() {
		super.init();
		clearWidgets();

		partyButtons.clear();
		shown.clear();

		float topBarHeight = (float) height * 0.17F;
		int button_statsY = (int) topBarHeight + 5;
		float buttonPosX = (float) width * 0.03F;
		float buttonWidth = ((float) width * 0.1744F) - 20;
		box = new MenuBox((int)(width*0.25F), (int)topBarHeight, (int)(width*0.3F), (int) middleHeight,0.8F, new Color(255,128,255));
		addRenderableWidget(back = new MenuButton((int) buttonPosX, button_statsY, (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Menu_Back), ButtonType.BUTTON, (e) -> { action("back"); }));

		refreshParties();
	}

	@Override
	public void render(@NotNull GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
		box.render(gui, mouseX, mouseY, partialTicks);
		super.render(gui, mouseX, mouseY, partialTicks);
		refreshParties();
	}
}

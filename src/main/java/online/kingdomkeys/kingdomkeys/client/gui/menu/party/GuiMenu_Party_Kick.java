package online.kingdomkeys.kingdomkeys.client.gui.menu.party;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.sounds.SoundSource;
import net.minecraft.network.chat.Component;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBox;
import online.kingdomkeys.kingdomkeys.client.gui.elements.PopupWarningScreen;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton.ButtonType;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.lib.Party.Member;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSOpenMenu;
import online.kingdomkeys.kingdomkeys.network.cts.CSPartyLeave;
import online.kingdomkeys.kingdomkeys.util.Utils;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GuiMenu_Party_Kick extends MenuBackground {
	MenuBox box;
	MenuButton back;

	WorldData worldData;
	Party party;

	private final List<MenuButton> memberButtons = new ArrayList<>();
	private final List<UUID> shown = new ArrayList<>();

	public GuiMenu_Party_Kick() {
		super(Strings.Gui_Menu_Party_Leader_Kick, new Color(0,0,255));
		drawPlayerInfo = true;
		worldData = WorldData.getClient();
	}

	protected void action(String string) {
		switch(string) {
		case "back":
			minecraft.level.playSound(minecraft.player, minecraft.player.blockPosition(), ModSounds.menu_in.get(), SoundSource.MASTER, 1.0f, 1.0f);
			minecraft.setScreen(new GuiMenu_Party_Leader());
			break;
		case "refresh":
			refreshMembers();
			break;
		}
	}

	private void kick(UUID id) {
		if (party == null || party.getMember(id) == null) {
			return;
		}

		Party target = party;
		String name = party.getMember(id).getUsername();

		minecraft.setScreen(new PopupWarningScreen(this, Component.translatable(Strings.WarningInformation), Component.translatable(Strings.WarningPartyKick, name), new Color(112, 31, 35), () -> {
					PacketHandler.sendToServer(new CSPartyLeave(target, id));
					target.removeMember(id);

					minecraft.level.playSound(minecraft.player, minecraft.player.blockPosition(), ModSounds.menu_in.get(), SoundSource.MASTER, 1.0f, 1.0f);
					refreshMembers();
				}));
	}

	private void refreshMembers() {
		worldData = WorldData.getClient();
		party = worldData.getPartyFromMember(minecraft.player.getUUID());

		if(party == null) {
			PacketHandler.sendToServer(new CSOpenMenu());
			return;
		}

		Member self = party.getMember(minecraft.player.getUUID());

		if(self == null || !self.isLeader()) {
			minecraft.setScreen(new GuiMenu_Party_Member());
			return;
		}

		List<Member> kickable = party.getMembers().stream().filter(member -> !member.isLeader()).toList();
		List<UUID> ids = kickable.stream().map(Member::getUUID).toList();

		if(ids.equals(shown)) {
			return;
		}

		for(MenuButton button : memberButtons) {
			removeWidget(button);
		}

		memberButtons.clear();
		shown.clear();
		shown.addAll(ids);

		float topBarHeight = (float) height * 0.17F;
		int button_statsY = (int) topBarHeight + 5;
		int buttonWidth = box.getWidth() - 40;

		for(int i = 0; i < kickable.size(); i++) {
			Member member = kickable.get(i);
			UUID id = member.getUUID();
			MenuButton button = new MenuButton(box.getX() + 10, button_statsY + (i * 18), buttonWidth, member.getUsername(), ButtonType.ROUNDBUTTON, (e) -> kick(id));

			memberButtons.add(button);
			addRenderableWidget(button);
		}
	}

	@Override
	public void init() {
		super.init();
		clearWidgets();

		memberButtons.clear();
		shown.clear();

		float topBarHeight = (float) height * 0.17F;
		int button_statsY = (int) topBarHeight + 5;
		float buttonPosX = (float) width * 0.03F;
		float buttonWidth = ((float) width * 0.1744F) - 20;
		box = new MenuBox((int)(width*0.25F), (int)topBarHeight, (int)(width*0.3F), (int) middleHeight,0.8F, new Color(255,128,255));
		addRenderableWidget(back = new MenuButton((int) buttonPosX, button_statsY, (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Menu_Back), ButtonType.BUTTON, (e) -> { action("back"); }));

		refreshMembers();
	}

	@Override
	public void render(@NotNull GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
		box.render(gui, mouseX, mouseY, partialTicks);
		super.render(gui, mouseX, mouseY, partialTicks);
		refreshMembers();
	}
}

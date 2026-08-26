package online.kingdomkeys.kingdomkeys.client.gui.menu.party;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBox;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton.ButtonType;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.lib.Party.Member;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSOpenMenu;
import online.kingdomkeys.kingdomkeys.network.cts.CSPartyPromote;
import online.kingdomkeys.kingdomkeys.util.Utils;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GuiMenu_Party_Promote extends MenuBackground {
	MenuBox box;
	MenuButton back;

	WorldData worldData;
	Party party;

	private final List<MenuButton> memberButtons = new ArrayList<>();
	private final List<UUID> shown = new ArrayList<>();
	private final List<Boolean> shownLeaders = new ArrayList<>();

	public GuiMenu_Party_Promote() {
		super(Strings.Gui_Menu_Party_Leader_Promote, new Color(0,0,255));
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

	private void promote(UUID id) {
		if(id.equals(minecraft.player.getUUID())) {
			minecraft.player.playSound(ModSounds.error.get());
			return;
		}

		if(party == null) {
			return;
		}

		Member member = party.getMember(id);

		if(member == null) {
			return;
		}

		PacketHandler.sendToServer(new CSPartyPromote(party, id));
		member.setIsLeader(!member.isLeader());

		minecraft.level.playSound(minecraft.player, minecraft.player.blockPosition(), ModSounds.menu_in.get(), SoundSource.MASTER, 1.0f, 1.0f);

		refreshMembers();
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

		List<Member> promotable = party.getMembers().stream().filter(Member::isPlayer).toList();
		List<UUID> ids = promotable.stream().map(Member::getUUID).toList();
		List<Boolean> leaders = promotable.stream().map(Member::isLeader).toList();

		if(ids.equals(shown) && leaders.equals(shownLeaders)) {
			return;
		}

		for(MenuButton button : memberButtons) {
			removeWidget(button);
		}

		memberButtons.clear();
		shown.clear();
		shown.addAll(ids);
		shownLeaders.clear();
		shownLeaders.addAll(leaders);

		float topBarHeight = (float) height * 0.17F;
		int button_statsY = (int) topBarHeight + 5;
		int buttonWidth = box.getWidth() - 40;

		for(int i = 0; i < promotable.size(); i++) {
			UUID id = promotable.get(i).getUUID();

			MenuButton button = new MenuButton(box.getX() + 10, button_statsY + (i * 18), buttonWidth, promotable.get(i).getUsername(), ButtonType.ROUNDBUTTON, (e) -> promote(id));

			memberButtons.add(button);
			addRenderableWidget(button);
		}
	}

	private void drawLeaderTags(GuiGraphics gui) {
		if(party == null) {
			return;
		}

		float topBarHeight = (float) height * 0.17F;
		int button_statsY = (int) topBarHeight + 5;

		for(int i = 0; i < shownLeaders.size(); i++) {
			if(shownLeaders.get(i)) {
				gui.drawString(minecraft.font, "Leader", box.getX() + box.getWidth() - minecraft.font.width("Leader") - 15, button_statsY + (i * 18) + 6, 0xFF9900);
			}
		}
	}

	@Override
	public void init() {
		super.init();
		clearWidgets();

		memberButtons.clear();
		shown.clear();
		shownLeaders.clear();

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

		if(party != null) {
			gui.pose().pushPose();
			{
				float scale = 1.5F;
				gui.pose().scale(scale, scale, 1);
				gui.drawString(minecraft.font, Component.literal("["+party.getMembers().size()+"/"+party.getSize()+"] "+party.getName()).withStyle(ClientUtils.KK_Font_EXP), (int) (topLeftBar.getWidth() / scale + topGap) + 5, 10, 0xFF9900);
			}
			gui.pose().popPose();
		}

		drawLeaderTags(gui);
	}
}

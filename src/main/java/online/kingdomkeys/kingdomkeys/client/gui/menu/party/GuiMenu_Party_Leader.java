package online.kingdomkeys.kingdomkeys.client.gui.menu.party;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton.ButtonType;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSOpenMenu;
import online.kingdomkeys.kingdomkeys.network.cts.CSPartyDisband;
import online.kingdomkeys.kingdomkeys.network.cts.CSPartyLeave;
import online.kingdomkeys.kingdomkeys.util.Utils;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class GuiMenu_Party_Leader extends MenuBackground {
	
	MenuButton back, invite, settings, promote, kick, disband;
		
	WorldData worldData;
	Party party;
	
	public GuiMenu_Party_Leader() {
		super(Strings.Gui_Menu_Party, new Color(0,0,255));
		drawPlayerInfo = true;
		worldData = WorldData.getClient();
	}

	protected void action(String string) {
		switch(string) {
		case "back":
			PacketHandler.sendToServer(new CSOpenMenu());
			break;
		case "disband":
			PacketHandler.sendToServer(new CSPartyDisband(party));
			PacketHandler.sendToServer(new CSOpenMenu());
			break;
		case "leave":
			PacketHandler.sendToServer(new CSPartyLeave(party, minecraft.player.getUUID()));
			party = null;
			break;
		case "settings":
			minecraft.level.playSound(minecraft.player, minecraft.player.blockPosition(), ModSounds.menu_in.get(), SoundSource.MASTER, 1.0f, 1.0f);
			minecraft.setScreen(new GuiMenu_Party_Settings());
			break;
		case "promote":
			minecraft.level.playSound(minecraft.player, minecraft.player.blockPosition(), ModSounds.menu_in.get(), SoundSource.MASTER, 1.0f, 1.0f);
			minecraft.setScreen(new GuiMenu_Party_Promote());
			break;
		case "kick":
			minecraft.level.playSound(minecraft.player, minecraft.player.blockPosition(), ModSounds.menu_in.get(), SoundSource.MASTER, 1.0f, 1.0f);
			minecraft.setScreen(new GuiMenu_Party_Kick());
			break;
		case "invite":
			minecraft.level.playSound(minecraft.player, minecraft.player.blockPosition(), ModSounds.menu_in.get(), SoundSource.MASTER, 1.0f, 1.0f);
			minecraft.setScreen(new GuiMenu_Party_Invite());
			break;
		}
		
		updateButtons();
	}

	private void updateButtons() {
		invite.visible = true;
		promote.visible = true;
		kick.visible = true;
		disband.visible = true;
	}

	@Override
	public void init() {
		super.init();
		this.renderables.clear();
		
		party = worldData.getPartyFromMember(minecraft.player.getUUID());
		if(party == null) {
			PacketHandler.sendToServer(new CSOpenMenu());
			return;
		}

		float topBarHeight = (float) height * 0.17F;
		int button_statsY = (int) topBarHeight + 5;
		float buttonPosX = (float) width * 0.03F;
		float buttonWidth = ((float) width * 0.1744F) - 20;

		int i = 0;
		addRenderableWidget(invite = new MenuButton((int) buttonPosX, button_statsY + (i++ * 18), (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Menu_Party_Leader_Invite), ButtonType.BUTTON, (e) -> { action("invite"); }));
		addRenderableWidget(settings = new MenuButton((int) buttonPosX, button_statsY + (i++ * 18), (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Menu_Party_Leader_Settings), ButtonType.BUTTON, (e) -> { action("settings"); }));
		addRenderableWidget(promote = new MenuButton((int) buttonPosX, button_statsY + (i++ * 18), (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Menu_Party_Leader_Promote), ButtonType.BUTTON, (e) -> { action("promote"); }));
		addRenderableWidget(kick = new MenuButton((int) buttonPosX, button_statsY + (i++ * 18), (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Menu_Party_Leader_Kick), ButtonType.BUTTON, (e) -> { action("kick"); }));
		addRenderableWidget(disband = new MenuButton((int) buttonPosX, button_statsY + (i++ * 18), (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Menu_Party_Leader_Disband), ButtonType.BUTTON, (e) -> { action("disband"); }));
		addRenderableWidget(back = new MenuButton((int) buttonPosX, button_statsY + (i++ * 18), (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Menu_Back), ButtonType.BUTTON, (e) -> { action("back"); }));
		
		updateButtons();
	}

	@Override
	public void render(@NotNull GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
		PoseStack matrixStack = gui.pose();
		super.render(gui, mouseX, mouseY, partialTicks);
		worldData = WorldData.getClient();
		if(worldData == null) {
			return;
		}

		party = worldData.getPartyFromMember(minecraft.player.getUUID());
		if(party == null) {
			PacketHandler.sendToServer(new CSOpenMenu());
        } else {
			if(!party.getMember(minecraft.player.getUUID()).isLeader()) {
				minecraft.setScreen(new GuiMenu_Party_Member());
				return;
			}

			if(invite != null) {
				invite.active = party.getMembers().size() < party.getSize();
				kick.active = party.getMembers().size() > 1;
				promote.active = party.getMembers().size() > 1;
			}
			matrixStack.pushPose();
			{
				matrixStack.scale(1.5F,1.5F, 1);
				gui.drawString(minecraft.font, Component.literal("["+party.getMembers().size()+"/"+party.getSize()+"] "+party.getName()).withStyle(ClientUtils.KK_Font_EXP), (int) (topLeftBarWidth + topGap) + 5, 10, 0xFF9900);
			}
			matrixStack.popPose();
		
			drawParty(worldData, gui);
		}
	}
}

package online.kingdomkeys.kingdomkeys.client.gui.menu.party;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.PopupWarningScreen;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton.ButtonType;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSOpenMenu;
import online.kingdomkeys.kingdomkeys.network.cts.CSPartyLeave;
import online.kingdomkeys.kingdomkeys.util.Utils;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class GuiMenu_Party_Member extends MenuBackground {

	MenuButton back, leave;
		
	WorldData worldData;
	Party party;

	public GuiMenu_Party_Member() {
		super(Strings.Gui_Menu_Party, new Color(0,0,255));
		drawPlayerInfo = true;
		worldData = WorldData.getClient();
		party = worldData.getPartyFromMember(minecraft.player.getUUID());
	}

	protected void action(String string) {
		switch(string) {
		case "back":
			PacketHandler.sendToServer(new CSOpenMenu());
			break;		
		case "leave":
			minecraft.setScreen(new PopupWarningScreen(this, Component.translatable(Strings.WarningInformation), Component.translatable(Strings.WarningPartyLeave, party.getName()), new Color(112, 31, 35), () -> {
						PacketHandler.sendToServer(new CSPartyLeave(party, minecraft.player.getUUID()));
						minecraft.level.playSound(minecraft.player, minecraft.player.blockPosition(), ModSounds.menu_in.get(), SoundSource.MASTER, 1.0f, 1.0f);
						minecraft.setScreen(new GuiMenu_Party_None());
					}));
			break;
		}
		
		updateButtons();
	}

	private void updateButtons() {
		leave.visible = true;
	}

	@Override
	public void init() {
		super.init();
		this.renderables.clear();
		
		party = worldData.getPartyFromMember(minecraft.player.getUUID());
		if(party == null) {
			PacketHandler.sendToServer(new CSOpenMenu());
		} else {			
			if(party.getMember(minecraft.player.getUUID()).isLeader()) {
				minecraft.setScreen(new GuiMenu_Party_Leader());
				return;
			}
		}
		
		float topBarHeight = (float) height * 0.17F;
		int button_statsY = (int) topBarHeight + 5;
		float buttonPosX = (float) width * 0.03F;
		float buttonWidth = ((float) width * 0.1744F) - 20;

		addRenderableWidget(leave = new MenuButton((int) buttonPosX, button_statsY, (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Menu_Party_Member_Leave), ButtonType.BUTTON, (e) -> { action("leave"); }));
		addRenderableWidget(back = new MenuButton((int) buttonPosX, button_statsY + (18), (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Menu_Back), ButtonType.BUTTON, (e) -> { action("back"); }));
		
		updateButtons();
	}

	@Override
	public void render(@NotNull GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
		PoseStack matrixStack = gui.pose();
		super.render(gui, mouseX, mouseY, partialTicks);
		worldData = WorldData.getClient();
		if(worldData == null){
			return;
		}
		party = worldData.getPartyFromMember(minecraft.player.getUUID());
		
		if(party == null) {
			PacketHandler.sendToServer(new CSOpenMenu());
		} else {
			if(party.getMember(minecraft.player.getUUID()).isLeader()) {
				minecraft.setScreen(new GuiMenu_Party_Leader());
				return;
			}
			
			matrixStack.pushPose();
			{
				float scale = 1.5F;
				gui.pose().scale(scale, scale, 1);
				gui.drawString(minecraft.font, Component.literal("["+party.getMembers().size()+"/"+party.getSize()+"] "+party.getName()).withStyle(ClientUtils.KK_Font_EXP), (int) (topLeftBar.getWidth() / scale + topGap) + 5, 10, 0xFF9900);
			}
			matrixStack.popPose();
			drawParty(worldData, gui);
		}
	}
}

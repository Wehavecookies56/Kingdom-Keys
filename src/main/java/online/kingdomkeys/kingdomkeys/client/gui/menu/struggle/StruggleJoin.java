package online.kingdomkeys.kingdomkeys.client.gui.menu.struggle;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton.ButtonType;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.lib.Struggle;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSStruggleJoin;
import online.kingdomkeys.kingdomkeys.util.Utils;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;

public class StruggleJoin extends MenuBackground {
	BlockPos boardPos;

	MenuButton back;

	PlayerData playerData = PlayerData.get(minecraft.player);
	WorldData worldData;

	MenuButton[] matches = new MenuButton[100];

	public StruggleJoin(BlockPos pos) {
		super(Utils.translateToLocal(Strings.Gui_Menu_Struggle_Join_Title), new Color(252, 173, 3));
		drawPlayerInfo = true;
		worldData = WorldData.getClient();
		boardPos = pos;
	}

	protected void action(String string) {
		//Clear list, it gets rebuilt every call
		for(int i=0;i<matches.length;i++) {
			if(matches[i] != null) {
				matches[i].visible = false;
			}
		}

		switch(string) {
		case "back":
			minecraft.level.playSound(minecraft.player, minecraft.player.blockPosition(), ModSounds.menu_in.get(), SoundSource.MASTER, 1.0f, 1.0f);
			minecraft.setScreen(new MenuStruggle(boardPos));
			break;
		}

		if(string.startsWith("struggle:")) {
			String[] data = string.split(":");
			String struggleName = data[1].substring(data[1].indexOf("]")+2);
			Struggle s = worldData.getStruggleFromName(struggleName);
			if(s != null && s.getParticipants().size() < s.getSize() && !s.hasParticipant(minecraft.player.getUUID())) {
				PacketHandler.sendToServer(new CSStruggleJoin(s.getName()));

				minecraft.level.playSound(minecraft.player, minecraft.player.blockPosition(), ModSounds.menu_in.get(), SoundSource.MASTER, 1.0f, 1.0f);
				minecraft.setScreen(null);
			}
		}
		updateButtons();
	}

	private void updateButtons() {
		refreshMatches();
	}

	private void refreshMatches() {
		worldData = WorldData.getClient();

		float topBarHeight = (float) height * 0.17F;
		int button_statsY = (int) topBarHeight + 5;
		float buttonWidth = ((float) width * 0.1744F) - 20;

		for(int i = 0;i<renderables.size();i++) {
			if(((AbstractWidget)renderables.get(i)).getMessage().getString().startsWith("[")) {
				renderables.remove(i);
			}
		}

		List<Struggle> matchList = worldData.getStruggles();
		int c = 0;
		for(int i=0;i<matchList.size();i++) {
			Struggle s = matchList.get(i);
			if(s != null) {
				addRenderableWidget(matches[c] = new MenuButton((int)(width * 0.3F), button_statsY + (c * 18), (int)(buttonWidth * 2), "["+s.getParticipants().size()+"/"+s.getSize()+"] "+s.getName(), ButtonType.BUTTON, (e) -> { action("struggle:"+e.getMessage().getString()); }));
				c++;
			}
		}
	}

	@Override
	public void init() {
		super.width = width;
		super.height = height;
		super.init();
		this.renderables.clear();

		float topBarHeight = (float) height * 0.17F;
		int button_statsY = (int) topBarHeight + 5;
		float buttonPosX = (float) width * 0.03F;
		float buttonWidth = ((float) width * 0.1744F) - 20;

		addRenderableWidget(back = new MenuButton((int) buttonPosX, button_statsY, (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Menu_Back), ButtonType.BUTTON, (e) -> { action("back"); }));

		updateButtons();
	}

	@Override
	public void render(@NotNull GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
		super.render(gui, mouseX, mouseY, partialTicks);
		worldData = WorldData.getClient();
		refreshMatches();
	}


}

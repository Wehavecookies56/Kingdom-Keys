package online.kingdomkeys.kingdomkeys.client.gui.menu.struggle;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
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
import online.kingdomkeys.kingdomkeys.network.cts.CSStruggleCreate;
import online.kingdomkeys.kingdomkeys.util.Utils;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class StruggleCreate extends MenuBackground {
	BlockPos boardPos;

	int size = 2;
	Struggle.Mode mode = Struggle.Mode.DUEL;

	EditBox tfName;
	Button accept, sizeButton, modeButton;
	MenuButton back;

	final PlayerData playerData = PlayerData.get(minecraft.player);
	WorldData worldData;

	public StruggleCreate(BlockPos pos) {
		super(Utils.translateToLocal(Strings.Gui_Menu_Struggle_Create_Title), new Color(252, 173, 3));
		drawPlayerInfo = true;
		worldData = WorldData.getClient();
		this.boardPos = pos;
	}

	protected void action(String string) {
		switch(string) {
		case "back":
			minecraft.level.playSound(minecraft.player, minecraft.player.blockPosition(), ModSounds.menu_in.get(), SoundSource.MASTER, 1.0f, 1.0f);
			minecraft.setScreen(new MenuStruggle(boardPos));
			break;
		case "accept":
			if(!tfName.getValue().equals("") && checkAvailable()) {
				Struggle struggle = new Struggle(boardPos, tfName.getValue(), minecraft.player.getUUID(), minecraft.player.getName().getString(), false, (byte) size);
				struggle.setMode(mode);
				PacketHandler.sendToServer(new CSStruggleCreate(struggle));

				minecraft.level.playSound(minecraft.player, minecraft.player.blockPosition(), ModSounds.menu_in.get(), SoundSource.MASTER, 1.0f, 1.0f);
				// Straight to Settings so the owner can define the arena corners right away.
				minecraft.setScreen(new StruggleSettings(boardPos));
			}
			break;
		case "size":
			if(size >= Struggle.PARTICIPANTS_LIMIT) {
				size = 2;
			} else {
				size++;
			}
			sizeButton.setMessage(Component.literal(size+""));
			break;
		case "mode":
			Struggle.Mode[] modes = Struggle.Mode.values();
			mode = modes[(mode.ordinal() + 1) % modes.length];
			modeButton.setMessage(Component.literal(modeLabel(mode)));
			break;
		}

		updateButtons();
	}

	private String modeLabel(Struggle.Mode mode) {
		return switch (mode) {
			case DUEL -> Utils.translateToLocal(Strings.Gui_Menu_Struggle_Mode_Duel);
			case TOURNAMENT -> Utils.translateToLocal(Strings.Gui_Menu_Struggle_Mode_Tournament);
			case FFA -> Utils.translateToLocal(Strings.Gui_Menu_Struggle_Mode_Ffa);
		};
	}

	private void updateButtons() {
		accept.visible = true;
		tfName.visible = true;
		sizeButton.visible = true;
		modeButton.visible = true;
	}

	@Override
	public void init() {
		super.init();
		this.renderables.clear();

		float topBarHeight = (float) height * 0.17F;
		int button_statsY = (int) topBarHeight + 5;
		float buttonPosX = (float) width * 0.03F;
		float buttonWidth = ((float) width * 0.1744F) - 20;

		addRenderableWidget(accept = Button.builder(Component.translatable(Utils.translateToLocal(Strings.Gui_Menu_Accept)), (e) -> {
			action("accept");
		}).bounds((int) (width*0.25)-2, button_statsY + (5 * 18), 100, 20).build());

		addRenderableWidget(sizeButton = Button.builder(Component.literal(size+""), (e) -> {
			action("size");
		}).bounds((int) (width * 0.25 - 2 + 100 + 4), button_statsY + (3 * 18), 20, 20).build());

		addRenderableWidget(modeButton = Button.builder(Component.literal(modeLabel(mode)), (e) -> {
			action("mode");
		}).bounds((int) (width * 0.25) - 2, button_statsY + (4 * 18), 100, 20).build());

		addRenderableWidget(back = new MenuButton((int) buttonPosX, button_statsY, (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Menu_Back), ButtonType.BUTTON, (e) -> { action("back"); }));

		addRenderableWidget(tfName = new EditBox(minecraft.font, (int)(width*0.25), (int)(height*0.25), 100, 15, Component.literal("")) {
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
			Struggle s = worldData.getStruggleFromName(tfName.getValue());
			accept.active = s == null;
			return s == null;
		}
		return false;
	}

	@Override
	public void render(@NotNull GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
		super.render(gui, mouseX, mouseY, partialTicks);
		worldData = WorldData.getClient();

		int buttonX = (int)(width*0.25);

		gui.drawString(minecraft.font, Utils.translateToLocal(Strings.Gui_Menu_Struggle_Name_And_Size), buttonX, (int)(height * 0.2), 0xFFFFFF);
	}

}

package online.kingdomkeys.kingdomkeys.client.gui.menu.struggle;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBox;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton.ButtonType;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.lib.Struggle;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSStruggleSettings;
import online.kingdomkeys.kingdomkeys.util.Utils;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class StruggleSettings extends MenuBackground {

	EditBox nameBox, pos1Box, pos2Box, spectatorPosBox;
	EditBox dmgMultBox, roundTimeBox, startingScoreBox;

	byte pSize = Struggle.PARTICIPANTS_LIMIT;
	int dmgMult = 100;
	int roundTimeSeconds = 60;
	int startingScore = 100;
	BlockPos pos1, pos2;
	Struggle.Mode selectedMode = Struggle.Mode.DUEL;

	BlockPos boardPos;

	MenuBox box;
	MenuButton accept, size, modeButton;
	MenuButton back;

	WorldData worldData;

	Struggle struggle;

	public StruggleSettings(BlockPos pos) {
		super(Utils.translateToLocal(Strings.Gui_Menu_Struggle_Settings_Title), new Color(252, 173, 3));
		drawPlayerInfo = true;
		worldData = WorldData.getClient();
		boardPos = pos;
	}

	protected void action(String string) {
		switch(string) {
			case "back":
				minecraft.level.playSound(minecraft.player, minecraft.player.blockPosition(), ModSounds.menu_in.get(), SoundSource.MASTER, 1.0f, 1.0f);
				minecraft.setScreen(new MenuStruggle(boardPos));
				break;
			case "accept":
				struggle.setSize(pSize);
				struggle.setDamageMult(dmgMult);
				struggle.setRoundTimeSeconds(roundTimeSeconds);
				struggle.setStartingScore(startingScore);
				struggle.setMode(selectedMode);
				struggle.setName(nameBox.getValue());

				pos1 = Utils.stringArrayToBlockPos(pos1Box.getValue().split(","));
				pos2 = Utils.stringArrayToBlockPos(pos2Box.getValue().split(","));

				struggle.setC1(pos1);
				struggle.setC2(pos2);

				if (spectatorPosBox.getValue() == null || spectatorPosBox.getValue().isBlank()) {
					struggle.setSpectatorPos(null);
				} else {
					struggle.setSpectatorPos(Utils.stringArrayToBlockPos(spectatorPosBox.getValue().split(",")));
				}

				PacketHandler.sendToServer(new CSStruggleSettings(struggle));

				minecraft.level.playSound(minecraft.player, minecraft.player.blockPosition(), ModSounds.menu_in.get(), SoundSource.MASTER, 1.0f, 1.0f);
				minecraft.setScreen(new MenuStruggle(boardPos));

				break;
			case "size":
				if (selectedMode == Struggle.Mode.DUEL)
					break;
				if(pSize == Struggle.PARTICIPANTS_LIMIT) {
					pSize = 2;
				} else {
					pSize++;
				}
				size.setMessage(Component.literal(pSize+""));
				break;
			case "mode":
				Struggle.Mode[] modes = Struggle.Mode.values();
				selectedMode = modes[(selectedMode.ordinal() + 1) % modes.length];
				if (selectedMode == Struggle.Mode.DUEL) {
					pSize = 2;
					size.active = false;
				}
				modeButton.setMessage(Component.literal(modeLabel(selectedMode)));
				break;
		}

		updateButtons();
	}

	private String modeLabel(Struggle.Mode mode) {
		return Utils.translateToLocal(Strings.Gui_Menu_Struggle + "." + mode.name().toLowerCase());
	}

	private void updateButtons() {
		if(struggle == null)
			return;

		boolean isDuel = selectedMode == Struggle.Mode.DUEL;
		if (isDuel)
			pSize = 2;

		size.setMessage(Component.literal(pSize+""));
		size.active = !isDuel;
		nameBox.setValue(struggle.getName());
		dmgMultBox.setValue(dmgMult+"");
		roundTimeBox.setValue(roundTimeSeconds+"");
		startingScoreBox.setValue(startingScore+"");
		if(struggle.c1 != null && struggle.c2 != null) {
			pos1Box.setValue(struggle.c1.getX()+","+struggle.c1.getY()+","+struggle.c1.getZ());
			pos2Box.setValue(struggle.c2.getX()+","+struggle.c2.getY()+","+struggle.c2.getZ());
		}
		if (struggle.getSpectatorPos() != null) {
			BlockPos spec = struggle.getSpectatorPos();
			spectatorPosBox.setValue(spec.getX()+","+spec.getY()+","+spec.getZ());
		}
		accept.setMessage(Component.literal(Utils.translateToLocal(Strings.Gui_Menu_Accept)));
		accept.visible = true;
		size.visible = true;
		modeButton.setMessage(Component.literal(modeLabel(selectedMode)));
		modeButton.visible = true;
	}

	/** A small numeric-only EditBox, used for damage mult/round time/starting score alike. */
	private EditBox numberBox(int x, int y, int width, java.util.function.IntConsumer onChange) {
		EditBox box = new EditBox(minecraft.font, x, y, width, 15, Component.literal("")) {
			@Override
			public boolean charTyped(char c, int i) {
				if (Utils.isNumber(c) || c == '-') {
					String text = new StringBuilder(this.getValue()).insert(this.getCursorPosition(), c).toString();
					if (Utils.getInt(text) < 100000 && Utils.getInt(text) > -100000) {
						super.charTyped(c, i);
						onChange.accept(Utils.getInt(getValue()));
						return true;
					} else {
						return false;
					}
				} else {
					return false;
				}
			}

			@Override
			public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
				super.keyPressed(keyCode, scanCode, modifiers);
				onChange.accept(Utils.getInt(getValue()));
				return true;
			}
		};
		addRenderableWidget(box);
		return box;
	}

	/** A coordinate ("x,y,z") EditBox, used for both arena corners and the spectator spot. */
	private EditBox coordBox(int x, int y) {
		EditBox box = new EditBox(minecraft.font, x, y, 60, 15, Component.literal("")) {
			@Override
			public boolean charTyped(char c, int i) {
				if (Utils.isNumber(c) || c == '-' || c == ',') {
					String text = new StringBuilder(this.getValue()).insert(this.getCursorPosition(), c).toString();
					if (Utils.getInt(text) < 1000 && Utils.getInt(text) > -1000) {
						super.charTyped(c, i);
						return true;
					} else {
						return false;
					}
				} else {
					return false;
				}
			}

			@Override
			public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
				super.keyPressed(keyCode, scanCode, modifiers);
				return true;
			}
		};
		addRenderableWidget(box);
		return box;
	}

	@Override
	public void init() {
		super.init();
		this.renderables.clear();

		//Get struggle from name stored in the block
		struggle = worldData.getStruggleFromBlockPos(boardPos);
		if(struggle != null) {
			pSize = struggle.getSize();
			dmgMult = struggle.getDamageMult();
			roundTimeSeconds = struggle.getRoundTimeSeconds();
			startingScore = struggle.getStartingScore();
			selectedMode = struggle.getMode();

			int button_statsY = (int) topBarHeight + 5;
			float buttonPosX = (float) width * 0.03F;
			float buttonWidth = ((float) width * 0.1744F) - 20;

			box = new MenuBox((int) (width * 0.25F), (int) topBarHeight, 250, (int) middleHeight, 0.8F, new Color(252, 173, 3));
			int boxX = box.getX() + 10;

			addRenderableWidget(nameBox = new EditBox(minecraft.font, boxX, button_statsY + 18, 100, 16, Component.literal("")) {
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

			addRenderableWidget(modeButton = new MenuButton(boxX + 100, button_statsY + 18 - 2, 70, "", ButtonType.ROUNDBUTTON, (e) -> { action("mode"); }).setCenterText());
			addRenderableWidget(size = new MenuButton(modeButton.getX() + modeButton.getWidth(), button_statsY + 18 - 2, 0, "", ButtonType.ROUNDBUTTON, (e) -> { action("size"); }).setCenterText());

			// Each of these labels+boxes sits on its own row, box positioned right after its own label (label width measured so the box never overlaps the text, whatever language it's in).
			int dmgMultLabelWidth = minecraft.font.width(Utils.translateToLocal(Strings.Gui_Menu_Struggle_Damage_Mult));
			dmgMultBox = numberBox(boxX + dmgMultLabelWidth + 10, button_statsY + (2 * 18), 40, v -> dmgMult = v);

			int roundTimeLabelWidth = minecraft.font.width(Utils.translateToLocal(Strings.Gui_Menu_Struggle_Round_Time));
			roundTimeBox = numberBox(boxX + roundTimeLabelWidth + 10, button_statsY + (3 * 18), 40, v -> roundTimeSeconds = v);

			int startingScoreLabelWidth = minecraft.font.width(Utils.translateToLocal(Strings.Gui_Menu_Struggle_Starting_Score));
			startingScoreBox = numberBox(boxX + startingScoreLabelWidth + 10, button_statsY + (4 * 18), 40, v -> startingScore = v);

			pos1Box = coordBox(boxX + 100, button_statsY + (5 * 18));
			pos2Box = coordBox(boxX + 165, button_statsY + (5 * 18));

			spectatorPosBox = coordBox(boxX + 100, button_statsY + (6 * 18));

			addRenderableWidget(accept = new MenuButton(boxX, button_statsY + (7 * 18), 130, "", ButtonType.ROUNDBUTTON, (e) -> { action("accept"); }).setCenterText());
			addRenderableWidget(back = new MenuButton((int) buttonPosX, button_statsY, (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Menu_Back), ButtonType.BUTTON, (e) -> { action("back"); }));
		}
		updateButtons();
	}

	private boolean checkAvailable() {
		if(nameBox.getValue() != null && !nameBox.getValue().equals("")) {
			Struggle s = worldData.getStruggleFromName(nameBox.getValue());
			boolean available = s == null || nameBox.getValue().equals(struggle.getName());
			accept.active = available;
			return available;
		}
		return false;
	}

	@Override
	public void render(@NotNull GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
		if (box != null)
			box.render(gui, mouseX, mouseY, partialTicks);
		super.render(gui, mouseX, mouseY, partialTicks);
		worldData = WorldData.getClient();
		Struggle latest = worldData.getStruggleFromBlockPos(boardPos);
		if (latest != null && nameBox == null) {
			this.init();
		}
		struggle = latest;
		if (struggle == null || box == null)
			return;

		int button_statsY = (int) topBarHeight + 5;
		int boxX = box.getX() + 10;

		gui.drawString(minecraft.font, Utils.translateToLocal(Strings.Gui_Menu_Struggle_Name_And_Size), boxX, button_statsY + 4, 0xFFFFFF);
		gui.drawString(minecraft.font, Utils.translateToLocal(Strings.Gui_Menu_Struggle_Damage_Mult), boxX, button_statsY + (2 * 18) + 4, 0xFFFFFF);
		gui.drawString(minecraft.font, Utils.translateToLocal(Strings.Gui_Menu_Struggle_Round_Time), boxX, button_statsY + (3 * 18) + 4, 0xFFFFFF);
		gui.drawString(minecraft.font, Utils.translateToLocal(Strings.Gui_Menu_Struggle_Starting_Score), boxX, button_statsY + (4 * 18) + 4, 0xFFFFFF);
		gui.drawString(minecraft.font, Utils.translateToLocal(Strings.Gui_Menu_Struggle_Corners_Pos), boxX, button_statsY + (5 * 18) + 4, 0xFFFFFF);
		gui.drawString(minecraft.font, Utils.translateToLocal(Strings.Gui_Menu_Struggle_Spectator_Pos), boxX, button_statsY + (6 * 18) + 4, 0xFFFFFF);
	}

}
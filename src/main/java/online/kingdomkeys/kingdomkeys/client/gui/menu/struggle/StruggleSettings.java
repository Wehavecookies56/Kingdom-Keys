package online.kingdomkeys.kingdomkeys.client.gui.menu.struggle;

import com.mojang.blaze3d.systems.RenderSystem;
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
import online.kingdomkeys.kingdomkeys.network.cts.CSStruggleSettings;
import online.kingdomkeys.kingdomkeys.util.Utils;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class StruggleSettings extends MenuBackground {

	EditBox nameBox, pos1Box, pos2Box;
	EditBox dmgMultBox, roundTimeBox, startingScoreBox;

	boolean priv = false;
	byte pSize = Struggle.PARTICIPANTS_LIMIT;
	int dmgMult = 100;
	int roundTimeSeconds = 60;
	int startingScore = 100;
	BlockPos pos1, pos2;

	BlockPos boardPos;

	Button togglePriv, accept, size, modeButton;
	MenuButton back;

	final PlayerData playerData = PlayerData.get(minecraft.player);
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
			case "togglePriv":
				priv = !priv;
				break;
			case "accept":
				//struggle.setPriv(priv);
				struggle.setSize(pSize);
				struggle.setDamageMult(dmgMult);
				struggle.setRoundTimeSeconds(roundTimeSeconds);
				struggle.setStartingScore(startingScore);
				struggle.setName(nameBox.getValue());

				pos1 = Utils.stringArrayToBlockPos(pos1Box.getValue().split(","));
				pos2 = Utils.stringArrayToBlockPos(pos2Box.getValue().split(","));

				struggle.setC1(pos1);
				struggle.setC2(pos2);

				PacketHandler.sendToServer(new CSStruggleSettings(struggle));

				minecraft.level.playSound(minecraft.player, minecraft.player.blockPosition(), ModSounds.menu_in.get(), SoundSource.MASTER, 1.0f, 1.0f);
				minecraft.setScreen(new MenuStruggle(boardPos));

				break;
			case "size":
				if(pSize == Struggle.PARTICIPANTS_LIMIT) {
					pSize = 2;
				} else {
					pSize++;
				}
				size.setMessage(Component.translatable(pSize+""));
				break;
			case "mode":
				Struggle.Mode[] modes = Struggle.Mode.values();
				struggle.setMode(modes[(struggle.getMode().ordinal() + 1) % modes.length]);
				modeButton.setMessage(Component.literal(modeLabel(struggle.getMode())));
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

		size.setMessage(Component.translatable(pSize+""));
		nameBox.setValue(struggle.getName());
		dmgMultBox.setValue(dmgMult+"");
		roundTimeBox.setValue(roundTimeSeconds+"");
		startingScoreBox.setValue(startingScore+"");
		if(struggle.c1 != null && struggle.c2 != null) {
			pos1Box.setValue(struggle.c1.getX()+","+struggle.c1.getY()+","+struggle.c1.getZ());
			pos2Box.setValue(struggle.c2.getX()+","+struggle.c2.getY()+","+struggle.c2.getZ());
		}
		accept.setMessage(Component.translatable(Strings.Gui_Menu_Accept));
		accept.visible = true;
		size.visible = true;
		modeButton.setMessage(Component.literal(modeLabel(struggle.getMode())));
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

			@Override
			public void renderWidget(@NotNull GuiGraphics gui, int pMouseX, int pMouseY, float pPartialTick) {
				RenderSystem.setShaderColor(1, 1, 1, 1);
				super.renderWidget(gui, pMouseX, pMouseY, pPartialTick);
			}
		};
		addRenderableWidget(box);
		return box;
	}

	@Override
	public void init() {
		super.width = width;
		super.height = height;
		super.init();
		this.renderables.clear();

		//Get struggle from name stored in the block
		struggle = worldData.getStruggleFromBlockPos(boardPos);
		if(struggle != null) {
			//priv = struggle.getPriv();
			pSize = struggle.getSize();
			dmgMult = struggle.getDamageMult();
			roundTimeSeconds = struggle.getRoundTimeSeconds();
			startingScore = struggle.getStartingScore();

			float topBarHeight = (float) height * 0.17F;
			int button_statsY = (int) topBarHeight + 5;
			float buttonPosX = (float) width * 0.03F;
			float buttonWidth = ((float) width * 0.1744F) - 20;
			int buttonX = (int) (width * 0.25);

			addRenderableWidget(nameBox = new EditBox(minecraft.font, buttonX, button_statsY + 18, 100, 16, Component.literal("")) {
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

			addRenderableWidget(size = Button.builder(Component.literal(""), (e) -> {
				action("size");
			}).bounds(buttonX - 2 + 100 + 4, button_statsY + 18 - 2, 20, 20).build());

			addRenderableWidget(modeButton = Button.builder(Component.literal(""), (e) -> {
				action("mode");
			}).bounds(buttonX + 130, button_statsY + 18 - 2, 100, 20).build());

			// Each of these labels+boxes sits on its own row, box positioned right after its own label
			// (label width measured so the box never overlaps the text, whatever language it's in).
			int dmgMultLabelWidth = minecraft.font.width(Utils.translateToLocal(Strings.Gui_Menu_Struggle_Damage_Mult));
			dmgMultBox = numberBox(buttonX + dmgMultLabelWidth + 10, button_statsY + (2 * 18), 40, v -> dmgMult = v);

			int roundTimeLabelWidth = minecraft.font.width(Utils.translateToLocal(Strings.Gui_Menu_Struggle_Round_Time));
			roundTimeBox = numberBox(buttonX + roundTimeLabelWidth + 10, button_statsY + (3 * 18), 40, v -> roundTimeSeconds = v);

			int startingScoreLabelWidth = minecraft.font.width(Utils.translateToLocal(Strings.Gui_Menu_Struggle_Starting_Score));
			startingScoreBox = numberBox(buttonX + startingScoreLabelWidth + 10, button_statsY + (4 * 18), 40, v -> startingScore = v);

			addRenderableWidget(pos1Box = new EditBox(minecraft.font, buttonX, button_statsY + (5 * 18), 100, 15, Component.literal("")) {
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

				@Override
				public void renderWidget(@NotNull GuiGraphics gui, int pMouseX, int pMouseY, float pPartialTick) {
					RenderSystem.setShaderColor(1, 1, 1, 1);
					super.renderWidget(gui, pMouseX, pMouseY, pPartialTick);
				}

			});

			addRenderableWidget(pos2Box = new EditBox(minecraft.font, buttonX + 110, button_statsY + (5 * 18), 100, 15, Component.literal("")) {
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

				@Override
				public void renderWidget(@NotNull GuiGraphics gui, int pMouseX, int pMouseY, float pPartialTick) {
					RenderSystem.setShaderColor(1, 1, 1, 1);
					super.renderWidget(gui, pMouseX, pMouseY, pPartialTick);
				}

			});


			addRenderableWidget(accept = Button.builder(Component.literal(""), (e) -> {
				action("accept");
			}).bounds(buttonX - 2, button_statsY + (6 * 18), 130, 20).build());

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
		super.render(gui, mouseX, mouseY, partialTicks);
		worldData = WorldData.getClient();
		Struggle latest = worldData.getStruggleFromBlockPos(boardPos);
		if (latest != null && nameBox == null) {
			// The struggle wasn't synced from the server yet when this screen first opened
			// (e.g. right after creating it). Now that it is, build the widgets.
			this.init();
		}
		struggle = latest;

		float topBarHeight = (float) height * 0.17F;
		int button_statsY = (int) topBarHeight + 5;
		int buttonX = (int) (width * 0.25);

		gui.drawString(minecraft.font, Utils.translateToLocal(Strings.Gui_Menu_Struggle_Name_And_Size), buttonX, button_statsY + 4, 0xFFFFFF);
		gui.drawString(minecraft.font, Utils.translateToLocal(Strings.Gui_Menu_Struggle_Damage_Mult), buttonX, button_statsY + (2 * 18) + 4, 0xFFFFFF);
		gui.drawString(minecraft.font, Utils.translateToLocal(Strings.Gui_Menu_Struggle_Round_Time), buttonX, button_statsY + (3 * 18) + 4, 0xFFFFFF);
		gui.drawString(minecraft.font, Utils.translateToLocal(Strings.Gui_Menu_Struggle_Starting_Score), buttonX, button_statsY + (4 * 18) + 4, 0xFFFFFF);
	}

}

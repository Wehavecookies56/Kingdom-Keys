package online.kingdomkeys.kingdomkeys.client.gui.menu.struggle;

import java.awt.Color;

import org.jetbrains.annotations.NotNull;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.IWorldCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton.ButtonType;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.lib.Struggle;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSStruggleSettings;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class StruggleSettings extends MenuBackground {

	EditBox nameBox, pos1Box, pos2Box;
	EditBox dmgMultBox;

	boolean priv = false;
	byte pSize = Struggle.PARTICIPANTS_LIMIT;
	int dmgMult = 100;
	BlockPos pos1, pos2;
	
	BlockPos boardPos;
	
	Button togglePriv, accept, size;
	MenuButton back;
		
	final IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);
	IWorldCapabilities worldData;
	
	Struggle struggle;
		
	public StruggleSettings(BlockPos pos) {
		super("Struggle Settings", new Color(252, 173, 3));
		drawPlayerInfo = true;
		worldData = ModCapabilities.getWorld(minecraft.level);
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
		case "dmg_mult":
			dmgMult = 1;//TODO
			break;
		case "accept":
			//struggle.setPriv(priv);
			struggle.setSize(pSize);
			struggle.setDamageMult(dmgMult);
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
		}
		
		updateButtons();
	}

	private void updateButtons() {
		if(struggle == null)
			return;
		
		size.setMessage(Component.translatable(pSize+""));
		nameBox.setValue(struggle.getName());
		dmgMultBox.setValue(dmgMult+"");
		if(struggle.c1 != null && struggle.c2 != null) {
			pos1Box.setValue(struggle.c1.getX()+","+struggle.c1.getY()+","+struggle.c1.getZ());
			pos2Box.setValue(struggle.c2.getX()+","+struggle.c2.getY()+","+struggle.c2.getZ());
		}
		accept.setMessage(Component.translatable("Accept"));
		accept.visible = true;
		size.visible = true;
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
			
			float topBarHeight = (float) height * 0.17F;
			int button_statsY = (int) topBarHeight + 5;
			float buttonPosX = (float) width * 0.03F;
			float buttonWidth = ((float) width * 0.1744F) - 20;
	
			addRenderableWidget(nameBox = new EditBox(minecraft.font, (int)(width*0.25), button_statsY + (1 * 18), 100, 16, Component.translatable("")) {
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
			
			addRenderableWidget(size = Button.builder(Component.translatable(""), (e) -> {
				action("size");
			}).bounds((int) (width * 0.25 - 2 + 100 + 4), button_statsY + (1 * 18)-2, (int) 20, 20).build());

			addRenderableWidget(dmgMultBox = new EditBox(minecraft.font, (int) (width * 0.25), button_statsY + (3 * 18), 30, 15, Component.translatable("")) {
				@Override
				public boolean charTyped(char c, int i) {
					if (Utils.isNumber(c) || c == '-') {
						String text = new StringBuilder(this.getValue()).insert(this.getCursorPosition(), c).toString();
						if (Utils.getInt(text) < 1000 && Utils.getInt(text) > -1000) {
							super.charTyped(c, i);
							dmgMult = Utils.getInt(getValue());
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
					dmgMult = Utils.getInt(getValue());
					return true;
				}

				@Override
				public void render(@NotNull GuiGraphics gui, int pMouseX, int pMouseY, float pPartialTick) {
					RenderSystem.setShaderColor(1, 1, 1, 1);
					super.render(gui, pMouseX, pMouseY, pPartialTick);
				}

			});
			
			addRenderableWidget(pos1Box = new EditBox(minecraft.font, (int) (width * 0.25), button_statsY + (5 * 18), 100, 15, Component.translatable("")) {
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
				public void render(@NotNull GuiGraphics gui, int pMouseX, int pMouseY, float pPartialTick) {
					RenderSystem.setShaderColor(1, 1, 1, 1);
					super.render(gui, pMouseX, pMouseY, pPartialTick);
				}

			});
			
			addRenderableWidget(pos2Box = new EditBox(minecraft.font, (int) (width * 0.25) + 110, button_statsY + (5 * 18), 100, 15, Component.translatable("")) {
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
				public void render(@NotNull GuiGraphics gui, int pMouseX, int pMouseY, float pPartialTick) {
					RenderSystem.setShaderColor(1, 1, 1, 1);
					super.render(gui, pMouseX, pMouseY, pPartialTick);
				}

			});
			
			
			addRenderableWidget(accept = Button.builder(Component.translatable(""), (e) -> {
				action("accept");
			}).bounds((int) (width*0.25)-2, button_statsY + (6 * 18), (int) 130, 20).build());
			
			addRenderableWidget(back = new MenuButton((int) buttonPosX, button_statsY + (0 * 18), (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Menu_Back), ButtonType.BUTTON, (e) -> { action("back"); }));
		}
		
		updateButtons();
	}
	
	private boolean checkAvailable() {
		if(nameBox.getValue() != null && !nameBox.getValue().equals("")) {
			Party p = worldData.getPartyFromName(nameBox.getValue());
			accept.active = p == null;	
			return p == null;
		}
		return false;
	}

	@Override
	public void render(@NotNull GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
		super.render(gui, mouseX, mouseY, partialTicks);
		worldData = ModCapabilities.getWorld(minecraft.level);
		struggle = worldData.getStruggleFromBlockPos(boardPos);
		
		int buttonX = (int)(width*0.25);
		gui.drawString(minecraft.font, Utils.translateToLocal("Struggle name and size"), buttonX, (int)(height * 0.21), 0xFFFFFF);
		gui.drawString(minecraft.font, Utils.translateToLocal("Damage multiplier"), buttonX, (int)(height * 0.202) + 38, 0xFFFFFF);
	}
	
}

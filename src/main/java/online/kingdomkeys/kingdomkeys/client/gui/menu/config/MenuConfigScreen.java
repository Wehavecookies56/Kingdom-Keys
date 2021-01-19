package online.kingdomkeys.kingdomkeys.client.gui.menu.config;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.TranslationTextComponent;
import online.kingdomkeys.kingdomkeys.client.gui.GuiHelper;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBox;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton.ButtonType;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class MenuConfigScreen extends MenuBackground {
		
	enum ActualWindow {
		COMMAND_MENU, HP, MP, DRIVE, PLAYER, LOCK_ON_HP
	}

	ActualWindow window = ActualWindow.COMMAND_MENU;
	
	MenuButton back, commandMenuButton, hpButton, mpButton;
	Button backgroundButton;
	MenuBox box;
	
	//Command Menu
	TextFieldWidget cmTextXOffsetBox, cmXScaleBox, cmXPosBox, cmSubXOffsetBox;
	Button cmHeaderTextVisibleButton;
	boolean cmHeaderTextVisible;
	
	//HP
	TextFieldWidget hpXPosBox, hpYPosBox;
	Button hpShowHeartsButton;
	boolean hpShowHearts;

	//MP
	TextFieldWidget mpXPosBox, mpYPosBox;
	List<Widget> commandMenuList = new ArrayList<Widget>();
	List<Widget> hpList = new ArrayList<Widget>();
	List<Widget> mpList = new ArrayList<Widget>();
	
	int buttonsX = 0;
	public MenuConfigScreen() {
		super(Strings.Gui_Menu_Config, new Color(0,0,255));
		drawPlayerInfo = false;
	}

	
	protected void action(String string) {
		switch(string) {
		case "back":
			GuiHelper.openMenu();
			break;
		case "textHeaderVisibility":
			cmHeaderTextVisible = !cmHeaderTextVisible;
			cmHeaderTextVisibleButton.setMessage(new TranslationTextComponent(cmHeaderTextVisible+""));
			ModConfigs.setCmHeaderTextVisible(cmHeaderTextVisible);
			break;
		case "hpShowHearts":
			hpShowHearts = !hpShowHearts;
			hpShowHeartsButton.setMessage(new TranslationTextComponent(hpShowHearts+""));
			ModConfigs.setShowHearts(hpShowHearts);
			break;
		}
		
	}


	@Override
	public void init() {
		float boxPosX = (float) width * 0.25F;
		float topBarHeight = (float) height * 0.17F;
		float boxWidth = (float) width * 0.67F;
		float middleHeight = (float) height * 0.6F;
		
		box = new MenuBox((int) boxPosX, (int) topBarHeight, (int) boxWidth, (int) middleHeight, new Color(4, 4, 68));
		buttonsX = box.x + 10;
		
		super.init();
		this.buttons.clear();
		
		initCommandMenu();
		initHP();
		initMP();
		
		addButton(commandMenuButton = new MenuButton((int) buttonPosX, (int) topBarHeight + 5 + (0 * 18), (int) buttonWidth, Utils.translateToLocal("gui.menu.config.command_menu"), ButtonType.BUTTON, (e) -> { window = ActualWindow.COMMAND_MENU; }));
		addButton(hpButton = new MenuButton((int) buttonPosX, (int) topBarHeight + 5 + (1 * 18), (int) buttonWidth, Utils.translateToLocal("gui.menu.config.hp"), ButtonType.BUTTON, (e) -> { window = ActualWindow.HP; }));
		addButton(mpButton = new MenuButton((int) buttonPosX, (int) topBarHeight + 5 + (2 * 18), (int) buttonWidth, Utils.translateToLocal("gui.menu.config.mp"), ButtonType.BUTTON, (e) -> { window = ActualWindow.MP; }));

		addButton(back = new MenuButton((int) buttonPosX, (int) topBarHeight + 5 + (5 * 18), (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Menu_Back), ButtonType.BUTTON, (e) -> { action("back"); }));
		addButton(backgroundButton = new MenuButton((int) width / 2 - (int)buttonWidth / 2, (int) topBarHeight + 5 + (7-2 * 18), (int) buttonWidth, Utils.translateToLocal("gui.menu.config.bg"), ButtonType.BUTTON, (e) -> { drawSeparately = !drawSeparately; }));
	}

	private void initCommandMenu() {
		cmHeaderTextVisible = ModConfigs.cmHeaderTextVisible;
		int pos = 0;

		addButton(cmXScaleBox = new TextFieldWidget(minecraft.fontRenderer, buttonsX, (int) (topBarHeight + 20 * ++pos), minecraft.fontRenderer.getStringWidth("#####"), 16, new TranslationTextComponent("test")){
			@Override
			public boolean charTyped(char c, int i) {
				if (Utils.isNumber(c) || c == '-') {
					String text = new StringBuilder(this.getText()).insert(this.getCursorPosition(), c).toString();
					if (Utils.getInt(text) < 1000 && Utils.getInt(text) > -1000) {
						super.charTyped(c, i);
						ModConfigs.setCmXScale(Utils.getInt(getText()));
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
				ModConfigs.setCmXScale(Utils.getInt(getText()));
				return true;
			}
			
		});
		
		addButton(cmXPosBox = new TextFieldWidget(minecraft.fontRenderer, buttonsX, (int) (topBarHeight + 20 * ++pos), minecraft.fontRenderer.getStringWidth("#####"), 16, new TranslationTextComponent("test")){
			@Override
			public boolean charTyped(char c, int i) {
				if (Utils.isNumber(c) || c == '-') {
					String text = new StringBuilder(this.getText()).insert(this.getCursorPosition(), c).toString();
					if (Utils.getInt(text) < 1000 && Utils.getInt(text) > -1000) {
						super.charTyped(c, i);
						ModConfigs.setCmXPos(Utils.getInt(getText()));
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
				ModConfigs.setCmXPos(Utils.getInt(getText()));
				return true;
			}
			
		});
		
		addButton(cmSubXOffsetBox = new TextFieldWidget(minecraft.fontRenderer, buttonsX, (int) (topBarHeight + 20 * ++pos), minecraft.fontRenderer.getStringWidth("#####"), 16, new TranslationTextComponent("test")){
			@Override
			public boolean charTyped(char c, int i) {
				if (Utils.isNumber(c) || c == '-') {
					String text = new StringBuilder(this.getText()).insert(this.getCursorPosition(), c).toString();
					if (Utils.getInt(text) < 1000 && Utils.getInt(text) > -1000) {
						super.charTyped(c, i);
						ModConfigs.setCmSubXOffset(Utils.getInt(getText()));
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
				ModConfigs.setCmSubXOffset(Utils.getInt(getText()));
				return true;
			}
			
		});
		
		addButton(cmHeaderTextVisibleButton = new Button(buttonsX - 1, (int) topBarHeight + 20 * ++pos - 2, minecraft.fontRenderer.getStringWidth("#####")+2, 20, new TranslationTextComponent(cmHeaderTextVisible+""), (e) -> { action("textHeaderVisibility"); }));
		addButton(cmTextXOffsetBox = new TextFieldWidget(minecraft.fontRenderer, buttonsX, (int) (topBarHeight + 20 * ++pos), minecraft.fontRenderer.getStringWidth("#####"), 16, new TranslationTextComponent("test")){
			@Override
			public boolean charTyped(char c, int i) {
				if (Utils.isNumber(c) || c == '-') {
					String text = new StringBuilder(this.getText()).insert(this.getCursorPosition(), c).toString();
					if (Utils.getInt(text) < 1000 && Utils.getInt(text) > -1000) {
						super.charTyped(c, i);
						ModConfigs.setCmTextXOffset(Utils.getInt(getText()));
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
				ModConfigs.setCmTextXOffset(Utils.getInt(getText()));
				return true;
			}
			
		});
		
		cmTextXOffsetBox.setText(""+ModConfigs.cmTextXOffset);
		cmHeaderTextVisibleButton.setMessage(new TranslationTextComponent(cmHeaderTextVisible+""));
		cmXScaleBox.setText(""+ModConfigs.cmXScale);
		cmXPosBox.setText(""+ModConfigs.cmXPos);
		cmSubXOffsetBox.setText(""+ModConfigs.cmSubXOffset);
		
		commandMenuList.add(cmHeaderTextVisibleButton);
		commandMenuList.add(cmTextXOffsetBox);
		commandMenuList.add(cmTextXOffsetBox);
		commandMenuList.add(cmHeaderTextVisibleButton);
		commandMenuList.add(cmXScaleBox);
		commandMenuList.add(cmXPosBox);
		commandMenuList.add(cmSubXOffsetBox);
	}

	private void initHP() {
		hpShowHearts = ModConfigs.hpShowHearts;

		int pos = 0;
		
		addButton(hpXPosBox = new TextFieldWidget(minecraft.fontRenderer, buttonsX, (int) (topBarHeight + 20 * ++pos), minecraft.fontRenderer.getStringWidth("#####"), 16, new TranslationTextComponent("test")){
			@Override
			public boolean charTyped(char c, int i) {
				if (Utils.isNumber(c) || c == '-') {
					String text = new StringBuilder(this.getText()).insert(this.getCursorPosition(), c).toString();
					if (Utils.getInt(text) < 1000 && Utils.getInt(text) > -1000) {
						super.charTyped(c, i);
						ModConfigs.setHpXPos(Utils.getInt(getText()));
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
				ModConfigs.setHpXPos(Utils.getInt(getText()));
				return true;
			}
			
		});
		
		addButton(hpYPosBox = new TextFieldWidget(minecraft.fontRenderer, buttonsX, (int) (topBarHeight + 20 * ++pos), minecraft.fontRenderer.getStringWidth("#####"), 16, new TranslationTextComponent("test")){
			@Override
			public boolean charTyped(char c, int i) {
				if (Utils.isNumber(c) || c == '-') {
					String text = new StringBuilder(this.getText()).insert(this.getCursorPosition(), c).toString();
					if (Utils.getInt(text) < 1000 && Utils.getInt(text) > -1000) {
						super.charTyped(c, i);
						ModConfigs.setHpYPos(Utils.getInt(getText()));
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
				ModConfigs.setHpYPos(Utils.getInt(getText()));
				return true;
			}
			
		});
		
		addButton(hpShowHeartsButton = new Button(buttonsX - 1, (int) topBarHeight + 20 * ++pos - 2, minecraft.fontRenderer.getStringWidth("#####")+2, 20, new TranslationTextComponent(hpShowHearts+""), (e) -> { action("hpShowHearts"); }));

		
		hpXPosBox.setText(""+ModConfigs.hpXPos);
		hpYPosBox.setText(""+ModConfigs.hpYPos);
		hpShowHeartsButton.setMessage(new TranslationTextComponent(hpShowHearts+""));
		
		hpList.add(hpXPosBox);
		hpList.add(hpYPosBox);
		hpList.add(hpShowHeartsButton);

	}
	
	private void initMP() {
		int pos = 0;
		
		addButton(mpXPosBox = new TextFieldWidget(minecraft.fontRenderer, buttonsX, (int) (topBarHeight + 20 * ++pos), minecraft.fontRenderer.getStringWidth("#####"), 16, new TranslationTextComponent("test")){
			@Override
			public boolean charTyped(char c, int i) {
				if (Utils.isNumber(c) || c == '-') {
					String text = new StringBuilder(this.getText()).insert(this.getCursorPosition(), c).toString();
					if (Utils.getInt(text) < 1000 && Utils.getInt(text) > -1000) {
						super.charTyped(c, i);
						ModConfigs.setMpXPos(Utils.getInt(getText()));
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
				ModConfigs.setMpXPos(Utils.getInt(getText()));
				return true;
			}
			
		});
		
		addButton(mpYPosBox = new TextFieldWidget(minecraft.fontRenderer, buttonsX, (int) (topBarHeight + 20 * ++pos), minecraft.fontRenderer.getStringWidth("#####"), 16, new TranslationTextComponent("test")){
			@Override
			public boolean charTyped(char c, int i) {
				if (Utils.isNumber(c) || c == '-') {
					String text = new StringBuilder(this.getText()).insert(this.getCursorPosition(), c).toString();
					if (Utils.getInt(text) < 1000 && Utils.getInt(text) > -1000) {
						super.charTyped(c, i);
						ModConfigs.setMpYPos(Utils.getInt(getText()));
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
				ModConfigs.setMpYPos(Utils.getInt(getText()));
				return true;
			}
			
		});
		

		
		mpXPosBox.setText(""+ModConfigs.mpXPos);
		mpYPosBox.setText(""+ModConfigs.mpYPos);
		
		mpList.add(mpXPosBox);
		mpList.add(mpYPosBox);

	}
	
	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		commandMenuButton.active = window != ActualWindow.COMMAND_MENU;
		hpButton.active = window != ActualWindow.HP;
		mpButton.active = window != ActualWindow.MP;
		
		box.draw(matrixStack);
		super.render(matrixStack, mouseX, mouseY, partialTicks);

		for(Widget b : commandMenuList) {
			b.active = false;
			b.visible = false;
		}
		
		for(Widget b : hpList) {
			b.active = false;
			b.visible = false;
		}
		
		for(Widget b : mpList) {
			b.active = false;
			b.visible = false;
		}

		switch(window) {
		case COMMAND_MENU:
			for(Widget b : commandMenuList) {
				b.active = true;
				b.visible = true;
			}
			
			matrixStack.push();
			{
				int pos = 0;
				matrixStack.translate(buttonsX, box.y+4, 1);
				drawString(matrixStack, minecraft.fontRenderer, Utils.translateToLocal("gui.menu.config.command_menu"), 20, 0, 0xFF9900);
				drawString(matrixStack, minecraft.fontRenderer, Utils.translateToLocal("gui.menu.config.x_scale"), 40, 20 * ++pos, 0xFF9900);
				drawString(matrixStack, minecraft.fontRenderer, Utils.translateToLocal("gui.menu.config.x_pos"), 40, 20 * ++pos, 0xFF9900);
				drawString(matrixStack, minecraft.fontRenderer, Utils.translateToLocal("gui.menu.config.sub_x_offset"), 40, 20 * ++pos, 0xFF9900);
				drawString(matrixStack, minecraft.fontRenderer, Utils.translateToLocal("gui.menu.config.header_title"), 40, 20 * ++pos, 0xFF9900);
				drawString(matrixStack, minecraft.fontRenderer, Utils.translateToLocal("gui.menu.config.text_x_offset"), 40, 20 * ++pos, 0xFF9900);
			}
			matrixStack.pop();
			
			break;
			
		case HP:
			for(Widget b : hpList) {
				b.active = true;
				b.visible = true;
			}
			
			matrixStack.push();
			{
				int pos = 0;
				matrixStack.translate(buttonsX, box.y+4, 1);
				drawString(matrixStack, minecraft.fontRenderer, Utils.translateToLocal("gui.menu.config.hp"), 20, 0, 0xFF9900);
				drawString(matrixStack, minecraft.fontRenderer, Utils.translateToLocal("gui.menu.config.x_pos"), 40, 20 * ++pos, 0xFF9900);
				drawString(matrixStack, minecraft.fontRenderer, Utils.translateToLocal("gui.menu.config.y_pos"), 40, 20 * ++pos, 0xFF9900);
				drawString(matrixStack, minecraft.fontRenderer, Utils.translateToLocal("gui.menu.config.show_hearts"), 40, 20 * ++pos, 0xFF9900);
			}
			matrixStack.pop();
			
			break;
			
		case MP:
			for(Widget b : mpList) {
				b.active = true;
				b.visible = true;
			}
			
			matrixStack.push();
			{
				int pos = 0;
				matrixStack.translate(buttonsX, box.y+4, 1);
				drawString(matrixStack, minecraft.fontRenderer, Utils.translateToLocal("gui.menu.config.mp"), 20, 0, 0xFF9900);
				drawString(matrixStack, minecraft.fontRenderer, Utils.translateToLocal("gui.menu.config.x_pos"), 40, 20 * ++pos, 0xFF9900);
				drawString(matrixStack, minecraft.fontRenderer, Utils.translateToLocal("gui.menu.config.y_pos"), 40, 20 * ++pos, 0xFF9900);
			}
			matrixStack.pop();
			
			break;
		
		}
	}	
}

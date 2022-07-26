package online.kingdomkeys.kingdomkeys.client.gui.menu.config;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.TranslatableComponent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.gui.GuiHelper;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBox;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.EditBoxLength;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton.ButtonType;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class MenuConfigScreen extends MenuBackground {
		
	enum ActualWindow {
		COMMAND_MENU, HP, MP, DRIVE, PLAYER, LOCK_ON_HP, PARTY, FOCUS, IMPORT_EXPORT
	}

	ActualWindow window = ActualWindow.COMMAND_MENU;
	
	MenuButton back, commandMenuButton, hpButton, mpButton, dpButton, playerSkinButton, lockOnButton, partyButton, focusButton, impExButton;
	Button backgroundButton;
	MenuBox box;
	
	//Command Menu
	EditBox cmTextXOffsetBox, cmXScaleBox, cmXPosBox, cmSelectedXOffsetBox, cmSubXOffsetBox;
	Button cmHeaderTextVisibleButton;
	boolean cmHeaderTextVisible;
	
	//HP
	EditBox hpXPosBox, hpYPosBox, hpAlarmBox;
	Button hpShowHeartsButton;
	boolean hpShowHearts;

	//MP
	EditBox mpXPosBox, mpYPosBox;

	//DP
	EditBox dpXPosBox, dpYPosBox;
	
	//PlayerSkin
	EditBox playerSkinXPosBox, playerSkinYPosBox;

	//Lock On
	EditBox lockOnXPosBox, lockOnYPosBox, lockOnHPScaleBox, lockOnIconScaleBox, lockOnIconRotationBox, lockOnHpPerBarBox;

	//Party
	EditBox partyXPosBox, partyYPosBox, partyYDistanceBox;

	//Focus
	EditBox focusXPosBox, focusYPosBox;

	//Import Export
	Button export, Import;
	EditBoxLength importCode;
	
	List<AbstractWidget> commandMenuList = new ArrayList<AbstractWidget>();
	List<AbstractWidget> hpList = new ArrayList<AbstractWidget>();
	List<AbstractWidget> mpList = new ArrayList<AbstractWidget>();
	List<AbstractWidget> dpList = new ArrayList<AbstractWidget>();
	List<AbstractWidget> playerSkinList = new ArrayList<AbstractWidget>();
	List<AbstractWidget> lockOnList = new ArrayList<AbstractWidget>();
	List<AbstractWidget> partyList = new ArrayList<AbstractWidget>();
	List<AbstractWidget> focusList = new ArrayList<AbstractWidget>();
	List<AbstractWidget> impExpList = new ArrayList<>();

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
			cmHeaderTextVisibleButton.setMessage(new TranslatableComponent(cmHeaderTextVisible+""));
			ModConfigs.setCmHeaderTextVisible(cmHeaderTextVisible);
			break;
		case "hpShowHearts":
			hpShowHearts = !hpShowHearts;
			hpShowHeartsButton.setMessage(new TranslatableComponent(hpShowHearts+""));
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
		this.renderables.clear();
		
		initCommandMenu();
		initHP();
		initMP();
		initDP();
		initPlayerSkin();
		initLockOn();
		initParty();
		initFocus();
		initImpExp();
		
		addRenderableWidget(commandMenuButton = new MenuButton((int) buttonPosX, (int) topBarHeight + 5 + (0 * 18), (int) buttonWidth, Utils.translateToLocal("gui.menu.config.command_menu"), ButtonType.BUTTON, (e) -> { window = ActualWindow.COMMAND_MENU; }));
		addRenderableWidget(hpButton = new MenuButton((int) buttonPosX, (int) topBarHeight + 5 + (1 * 18), (int) buttonWidth, Utils.translateToLocal("gui.menu.config.hp"), ButtonType.BUTTON, (e) -> { window = ActualWindow.HP; }));
		addRenderableWidget(mpButton = new MenuButton((int) buttonPosX, (int) topBarHeight + 5 + (2 * 18), (int) buttonWidth, Utils.translateToLocal("gui.menu.config.mp"), ButtonType.BUTTON, (e) -> { window = ActualWindow.MP; }));
		addRenderableWidget(dpButton = new MenuButton((int) buttonPosX, (int) topBarHeight + 5 + (3 * 18), (int) buttonWidth, Utils.translateToLocal("gui.menu.config.dp"), ButtonType.BUTTON, (e) -> { window = ActualWindow.DRIVE; }));
		addRenderableWidget(playerSkinButton = new MenuButton((int) buttonPosX, (int) topBarHeight + 5 + (4 * 18), (int) buttonWidth, Utils.translateToLocal("gui.menu.config.player_skin"), ButtonType.BUTTON, (e) -> { window = ActualWindow.PLAYER; }));
		addRenderableWidget(lockOnButton = new MenuButton((int) buttonPosX, (int) topBarHeight + 5 + (5 * 18), (int) buttonWidth, Utils.translateToLocal("gui.menu.config.lock_on_hp"), ButtonType.BUTTON, (e) -> { window = ActualWindow.LOCK_ON_HP; }));
		addRenderableWidget(partyButton = new MenuButton((int) buttonPosX, (int) topBarHeight + 5 + (6 * 18), (int) buttonWidth, Utils.translateToLocal("gui.menu.config.party"), ButtonType.BUTTON, (e) -> { window = ActualWindow.PARTY; }));
		addRenderableWidget(focusButton = new MenuButton((int) buttonPosX, (int) topBarHeight + 5 + (7 * 18), (int) buttonWidth, Utils.translateToLocal("gui.menu.config.focus"), ButtonType.BUTTON, (e) -> { window = ActualWindow.FOCUS; }));
		addRenderableWidget(impExButton = new MenuButton((int) buttonPosX, (int) topBarHeight + 5 + (8 * 18), (int) buttonWidth, Utils.translateToLocal("gui.menu.config.impexp"), ButtonType.BUTTON, (e) -> window = ActualWindow.IMPORT_EXPORT));

		addRenderableWidget(back = new MenuButton((int) buttonPosX, (int) topBarHeight + 5 + (9 * 18), (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Menu_Back), ButtonType.BUTTON, (e) -> { action("back"); }));
		addRenderableWidget(backgroundButton = new MenuButton((int) width / 2 - (int)buttonWidth / 2, (int) topBarHeight + 5 + (7-2 * 18), (int) buttonWidth, Utils.translateToLocal("gui.menu.config.bg"), ButtonType.BUTTON, (e) -> { drawSeparately = !drawSeparately; }));
	}

	private void initCommandMenu() {
		cmHeaderTextVisible = ModConfigs.cmHeaderTextVisible;
		int pos = 0;

		addRenderableWidget(cmXScaleBox = new EditBox(minecraft.font, buttonsX, (int) (topBarHeight + 20 * ++pos), minecraft.font.width("#####"), 16, new TranslatableComponent("test")){
			@Override
			public boolean charTyped(char c, int i) {
				if (Utils.isNumber(c) || c == '-') {
					String text = new StringBuilder(this.getValue()).insert(this.getCursorPosition(), c).toString();
					if (Utils.getInt(text) < 1000 && Utils.getInt(text) > -1000) {
						super.charTyped(c, i);
						ModConfigs.setCmXScale(Utils.getInt(getValue()));
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
				ModConfigs.setCmXScale(Utils.getInt(getValue()));
				return true;
			}
			
		});
		
		addRenderableWidget(cmXPosBox = new EditBox(minecraft.font, buttonsX, (int) (topBarHeight + 20 * ++pos), minecraft.font.width("#####"), 16, new TranslatableComponent("test")){
			@Override
			public boolean charTyped(char c, int i) {
				if (Utils.isNumber(c) || c == '-') {
					String text = new StringBuilder(this.getValue()).insert(this.getCursorPosition(), c).toString();
					if (Utils.getInt(text) < 1000 && Utils.getInt(text) > -1000) {
						super.charTyped(c, i);
						ModConfigs.setCmXPos(Utils.getInt(getValue()));
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
				ModConfigs.setCmXPos(Utils.getInt(getValue()));
				return true;
			}
			
		});
		
		addRenderableWidget(cmSelectedXOffsetBox = new EditBox(minecraft.font, buttonsX, (int) (topBarHeight + 20 * ++pos), minecraft.font.width("#####"), 16, new TranslatableComponent("test")){
			@Override
			public boolean charTyped(char c, int i) {
				if (Utils.isNumber(c) || c == '-') {
					String text = new StringBuilder(this.getValue()).insert(this.getCursorPosition(), c).toString();
					if (Utils.getInt(text) < 1000 && Utils.getInt(text) > -1000) {
						super.charTyped(c, i);
						ModConfigs.setCmSelectedXOffset(Utils.getInt(getValue()));
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
				ModConfigs.setCmSelectedXOffset(Utils.getInt(getValue()));
				return true;
			}
			
		});
		
		addRenderableWidget(cmSubXOffsetBox = new EditBox(minecraft.font, buttonsX, (int) (topBarHeight + 20 * ++pos), minecraft.font.width("#####"), 16, new TranslatableComponent("test")){
			@Override
			public boolean charTyped(char c, int i) {
				if (Utils.isNumber(c) || c == '-') {
					String text = new StringBuilder(this.getValue()).insert(this.getCursorPosition(), c).toString();
					if (Utils.getInt(text) < 1000 && Utils.getInt(text) > -1000) {
						super.charTyped(c, i);
						ModConfigs.setCmSubXOffset(Utils.getInt(getValue()));
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
				ModConfigs.setCmSubXOffset(Utils.getInt(getValue()));
				return true;
			}
			
		});
		
		addRenderableWidget(cmHeaderTextVisibleButton = new Button(buttonsX - 1, (int) topBarHeight + 20 * ++pos - 2, minecraft.font.width("#####")+2, 20, new TranslatableComponent(cmHeaderTextVisible+""), (e) -> { action("textHeaderVisibility"); }));
		addRenderableWidget(cmTextXOffsetBox = new EditBox(minecraft.font, buttonsX, (int) (topBarHeight + 20 * ++pos), minecraft.font.width("#####"), 16, new TranslatableComponent("test")){
			@Override
			public boolean charTyped(char c, int i) {
				if (Utils.isNumber(c) || c == '-') {
					String text = new StringBuilder(this.getValue()).insert(this.getCursorPosition(), c).toString();
					if (Utils.getInt(text) < 1000 && Utils.getInt(text) > -1000) {
						super.charTyped(c, i);
						ModConfigs.setCmTextXOffset(Utils.getInt(getValue()));
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
				ModConfigs.setCmTextXOffset(Utils.getInt(getValue()));
				return true;
			}
			
		});
		
		cmTextXOffsetBox.setValue(""+ModConfigs.cmTextXOffset);
		cmHeaderTextVisibleButton.setMessage(new TranslatableComponent(cmHeaderTextVisible+""));
		cmXScaleBox.setValue(""+ModConfigs.cmXScale);
		cmXPosBox.setValue(""+ModConfigs.cmXPos);
		cmSelectedXOffsetBox.setValue(""+ModConfigs.cmSelectedXOffset);
		cmSubXOffsetBox.setValue(""+ModConfigs.cmSubXOffset);
		
		commandMenuList.add(cmHeaderTextVisibleButton);
		commandMenuList.add(cmTextXOffsetBox);
		commandMenuList.add(cmTextXOffsetBox);
		commandMenuList.add(cmHeaderTextVisibleButton);
		commandMenuList.add(cmXScaleBox);
		commandMenuList.add(cmXPosBox);
		commandMenuList.add(cmSelectedXOffsetBox);
		commandMenuList.add(cmSubXOffsetBox);
	}

	private void initHP() {
		hpShowHearts = ModConfigs.hpShowHearts;
		int pos = 0;
		
		addRenderableWidget(hpXPosBox = new EditBox(minecraft.font, buttonsX, (int) (topBarHeight + 20 * ++pos), minecraft.font.width("#####"), 16, new TranslatableComponent("test")){
			@Override
			public boolean charTyped(char c, int i) {
				if (Utils.isNumber(c) || c == '-') {
					String text = new StringBuilder(this.getValue()).insert(this.getCursorPosition(), c).toString();
					if (Utils.getInt(text) < 1000 && Utils.getInt(text) > -1000) {
						super.charTyped(c, i);
						ModConfigs.setHpXPos(Utils.getInt(getValue()));
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
				ModConfigs.setHpXPos(Utils.getInt(getValue()));
				return true;
			}
			
		});
		
		addRenderableWidget(hpYPosBox = new EditBox(minecraft.font, buttonsX, (int) (topBarHeight + 20 * ++pos), minecraft.font.width("#####"), 16, new TranslatableComponent("test")){
			@Override
			public boolean charTyped(char c, int i) {
				if (Utils.isNumber(c) || c == '-') {
					String text = new StringBuilder(this.getValue()).insert(this.getCursorPosition(), c).toString();
					if (Utils.getInt(text) < 1000 && Utils.getInt(text) > -1000) {
						super.charTyped(c, i);
						ModConfigs.setHpYPos(Utils.getInt(getValue()));
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
				ModConfigs.setHpYPos(Utils.getInt(getValue()));
				return true;
			}
			
		});
		
		addRenderableWidget(hpShowHeartsButton = new Button(buttonsX - 1, (int) topBarHeight + 20 * ++pos - 2, minecraft.font.width("#####")+2, 20, new TranslatableComponent(hpShowHearts+""), (e) -> { action("hpShowHearts"); }));
		addRenderableWidget(hpAlarmBox = new EditBox(minecraft.font, buttonsX, (int) (topBarHeight + 20 * ++pos), minecraft.font.width("#####"), 16, new TranslatableComponent("")){
			@Override
			public boolean charTyped(char c, int i) {
				if (Utils.isNumber(c) || c == '-') {
					String text = new StringBuilder(this.getValue()).insert(this.getCursorPosition(), c).toString();
					if (Utils.getInt(text) <= 10 && Utils.getInt(text) >= 0) {
						super.charTyped(c, i);
						ModConfigs.setHPAlarm(Utils.getInt(getValue()));
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
				ModConfigs.setHPAlarm(Utils.getInt(getValue()));
				return true;
			}
			
		});

		
		hpXPosBox.setValue(""+ModConfigs.hpXPos);
		hpYPosBox.setValue(""+ModConfigs.hpYPos);
		hpShowHeartsButton.setMessage(new TranslatableComponent(hpShowHearts+""));
		hpAlarmBox.setValue(""+ModConfigs.hpAlarm);

		hpList.add(hpXPosBox);
		hpList.add(hpYPosBox);
		hpList.add(hpShowHeartsButton);
		hpList.add(hpAlarmBox);

	}
	
	private void initMP() {
		int pos = 0;
		
		addRenderableWidget(mpXPosBox = new EditBox(minecraft.font, buttonsX, (int) (topBarHeight + 20 * ++pos), minecraft.font.width("#####"), 16, new TranslatableComponent("test")){
			@Override
			public boolean charTyped(char c, int i) {
				if (Utils.isNumber(c) || c == '-') {
					String text = new StringBuilder(this.getValue()).insert(this.getCursorPosition(), c).toString();
					if (Utils.getInt(text) < 1000 && Utils.getInt(text) > -1000) {
						super.charTyped(c, i);
						ModConfigs.setMpXPos(Utils.getInt(getValue()));
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
				ModConfigs.setMpXPos(Utils.getInt(getValue()));
				return true;
			}
			
		});
		
		addRenderableWidget(mpYPosBox = new EditBox(minecraft.font, buttonsX, (int) (topBarHeight + 20 * ++pos), minecraft.font.width("#####"), 16, new TranslatableComponent("test")){
			@Override
			public boolean charTyped(char c, int i) {
				if (Utils.isNumber(c) || c == '-') {
					String text = new StringBuilder(this.getValue()).insert(this.getCursorPosition(), c).toString();
					if (Utils.getInt(text) < 1000 && Utils.getInt(text) > -1000) {
						super.charTyped(c, i);
						ModConfigs.setMpYPos(Utils.getInt(getValue()));
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
				ModConfigs.setMpYPos(Utils.getInt(getValue()));
				return true;
			}
			
		});
		

		
		mpXPosBox.setValue(""+ModConfigs.mpXPos);
		mpYPosBox.setValue(""+ModConfigs.mpYPos);
		
		mpList.add(mpXPosBox);
		mpList.add(mpYPosBox);

	}
	
	private void initDP() {
		int pos = 0;
		
		addRenderableWidget(dpXPosBox = new EditBox(minecraft.font, buttonsX, (int) (topBarHeight + 20 * ++pos), minecraft.font.width("#####"), 16, new TranslatableComponent("test")){
			@Override
			public boolean charTyped(char c, int i) {
				if (Utils.isNumber(c) || c == '-') {
					String text = new StringBuilder(this.getValue()).insert(this.getCursorPosition(), c).toString();
					if (Utils.getInt(text) < 1000 && Utils.getInt(text) > -1000) {
						super.charTyped(c, i);
						ModConfigs.setDpXPos(Utils.getInt(getValue()));
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
				ModConfigs.setDpXPos(Utils.getInt(getValue()));
				return true;
			}
			
		});
		
		addRenderableWidget(dpYPosBox = new EditBox(minecraft.font, buttonsX, (int) (topBarHeight + 20 * ++pos), minecraft.font.width("#####"), 16, new TranslatableComponent("test")){
			@Override
			public boolean charTyped(char c, int i) {
				if (Utils.isNumber(c) || c == '-') {
					String text = new StringBuilder(this.getValue()).insert(this.getCursorPosition(), c).toString();
					if (Utils.getInt(text) < 1000 && Utils.getInt(text) > -1000) {
						super.charTyped(c, i);
						ModConfigs.setDpYPos(Utils.getInt(getValue()));
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
				ModConfigs.setDpYPos(Utils.getInt(getValue()));
				return true;
			}
			
		});
		

		
		dpXPosBox.setValue(""+ModConfigs.dpXPos);
		dpYPosBox.setValue(""+ModConfigs.dpYPos);
		
		dpList.add(dpXPosBox);
		dpList.add(dpYPosBox);

	}
	
	private void initPlayerSkin() {
		int pos = 0;
		
		addRenderableWidget(playerSkinXPosBox = new EditBox(minecraft.font, buttonsX, (int) (topBarHeight + 20 * ++pos), minecraft.font.width("#####"), 16, new TranslatableComponent("test")){
			@Override
			public boolean charTyped(char c, int i) {
				if (Utils.isNumber(c) || c == '-') {
					String text = new StringBuilder(this.getValue()).insert(this.getCursorPosition(), c).toString();
					if (Utils.getInt(text) < 1000 && Utils.getInt(text) > -1000) {
						super.charTyped(c, i);
						ModConfigs.setPlayerSkinXPos(Utils.getInt(getValue()));
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
				ModConfigs.setPlayerSkinXPos(Utils.getInt(getValue()));
				return true;
			}
			
		});
		
		addRenderableWidget(playerSkinYPosBox = new EditBox(minecraft.font, buttonsX, (int) (topBarHeight + 20 * ++pos), minecraft.font.width("#####"), 16, new TranslatableComponent("test")){
			@Override
			public boolean charTyped(char c, int i) {
				if (Utils.isNumber(c) || c == '-') {
					String text = new StringBuilder(this.getValue()).insert(this.getCursorPosition(), c).toString();
					if (Utils.getInt(text) < 1000 && Utils.getInt(text) > -1000) {
						super.charTyped(c, i);
						ModConfigs.setPlayerSkinYPos(Utils.getInt(getValue()));
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
				ModConfigs.setPlayerSkinYPos(Utils.getInt(getValue()));
				return true;
			}
			
		});

		playerSkinXPosBox.setValue(""+ModConfigs.playerSkinXPos);
		playerSkinYPosBox.setValue(""+ModConfigs.playerSkinYPos);
		
		playerSkinList.add(playerSkinXPosBox);
		playerSkinList.add(playerSkinYPosBox);

	}
	
	private void initLockOn() {
		int pos = 0;
		
		addRenderableWidget(lockOnXPosBox = new EditBox(minecraft.font, buttonsX, (int) (topBarHeight + 20 * ++pos), minecraft.font.width("#####"), 16, new TranslatableComponent("test")){
			@Override
			public boolean charTyped(char c, int i) {
				if (Utils.isNumber(c) || c == '-') {
					String text = new StringBuilder(this.getValue()).insert(this.getCursorPosition(), c).toString();
					if (Utils.getInt(text) < 1000 && Utils.getInt(text) > -1000) {
						super.charTyped(c, i);
						ModConfigs.setLockOnXPos(Utils.getInt(getValue()));
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
				ModConfigs.setLockOnXPos(Utils.getInt(getValue()));
				return true;
			}
			
		});
		
		addRenderableWidget(lockOnYPosBox = new EditBox(minecraft.font, buttonsX, (int) (topBarHeight + 20 * ++pos), minecraft.font.width("#####"), 16, new TranslatableComponent("test")){
			@Override
			public boolean charTyped(char c, int i) {
				if (Utils.isNumber(c) || c == '-') {
					String text = new StringBuilder(this.getValue()).insert(this.getCursorPosition(), c).toString();
					if (Utils.getInt(text) < 1000 && Utils.getInt(text) > -1000) {
						super.charTyped(c, i);
						ModConfigs.setLockOnYPos(Utils.getInt(getValue()));
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
				ModConfigs.setLockOnYPos(Utils.getInt(getValue()));
				return true;
			}
			
		});
		
		addRenderableWidget(lockOnHPScaleBox = new EditBox(minecraft.font, buttonsX, (int) (topBarHeight + 20 * ++pos), minecraft.font.width("#####"), 16, new TranslatableComponent("test")){
			@Override
			public boolean charTyped(char c, int i) {
				if (Utils.isNumber(c) || c == '-') {
					String text = new StringBuilder(this.getValue()).insert(this.getCursorPosition(), c).toString();
					if (Utils.getInt(text) < 1000 && Utils.getInt(text) > -1000) {
						super.charTyped(c, i);
						ModConfigs.setLockOnHPScale(Utils.getInt(getValue()));
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
				ModConfigs.setLockOnHPScale(Utils.getInt(getValue()));
				return true;
			}
			
		});
		
		addRenderableWidget(lockOnIconScaleBox = new EditBox(minecraft.font, buttonsX, (int) (topBarHeight + 20 * ++pos), minecraft.font.width("#####"), 16, new TranslatableComponent("test")){
			@Override
			public boolean charTyped(char c, int i) {
				if (Utils.isNumber(c) || c == '-') {
					String text = new StringBuilder(this.getValue()).insert(this.getCursorPosition(), c).toString();
					if (Utils.getInt(text) < 1000 && Utils.getInt(text) > -1000) {
						super.charTyped(c, i);
						ModConfigs.setLockOnIconScale(Utils.getInt(getValue()));
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
				ModConfigs.setLockOnIconScale(Utils.getInt(getValue()));
				return true;
			}
			
		});
		

		addRenderableWidget(lockOnIconRotationBox = new EditBox(minecraft.font, buttonsX, (int) (topBarHeight + 20 * ++pos), minecraft.font.width("#####"), 16, new TranslatableComponent("test")){
			@Override
			public boolean charTyped(char c, int i) {
				if (Utils.isNumber(c) || c == '-') {
					String text = new StringBuilder(this.getValue()).insert(this.getCursorPosition(), c).toString();
					if (Utils.getInt(text) <= 100 && Utils.getInt(text) >= -100) {
						super.charTyped(c, i);
						ModConfigs.setLockOnIconRotation(Utils.getInt(getValue()));
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
				ModConfigs.setLockOnIconRotation(Utils.getInt(getValue()));
				return true;
			}
			
		});
		
		addRenderableWidget(lockOnHpPerBarBox = new EditBox(minecraft.font, buttonsX, (int) (topBarHeight + 20 * ++pos), minecraft.font.width("#####"), 16, new TranslatableComponent("test")){
			@Override
			public boolean charTyped(char c, int i) {
				if (Utils.isNumber(c) || c == '-') {
					String text = new StringBuilder(this.getValue()).insert(this.getCursorPosition(), c).toString();
					if (Utils.getInt(text) <= 100 && Utils.getInt(text) >= 0) {
						super.charTyped(c, i);
						ModConfigs.setLockOnHpPerBar(Utils.getInt(getValue()));
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
				ModConfigs.setLockOnHpPerBar(Utils.getInt(getValue()));
				return true;
			}
			
		});
		
		
		lockOnXPosBox.setValue(""+ModConfigs.lockOnXPos);
		lockOnYPosBox.setValue(""+ModConfigs.lockOnYPos);
		lockOnHPScaleBox.setValue(""+ModConfigs.lockOnHPScale);
		lockOnIconScaleBox.setValue(""+ModConfigs.lockOnIconScale);
		lockOnIconRotationBox.setValue(""+ModConfigs.lockOnIconRotation);
		lockOnHpPerBarBox.setValue(""+ModConfigs.lockOnHpPerBar);
		
		lockOnList.add(lockOnXPosBox);
		lockOnList.add(lockOnYPosBox);
		lockOnList.add(lockOnHPScaleBox);
		lockOnList.add(lockOnIconScaleBox);
		lockOnList.add(lockOnIconRotationBox);
		lockOnList.add(lockOnHpPerBarBox);
	}
	
	private void initParty() {
		int pos = 0;
		
		addRenderableWidget(partyXPosBox = new EditBox(minecraft.font, buttonsX, (int) (topBarHeight + 20 * ++pos), minecraft.font.width("#####"), 16, new TranslatableComponent("test")){
			@Override
			public boolean charTyped(char c, int i) {
				if (Utils.isNumber(c) || c == '-') {
					String text = new StringBuilder(this.getValue()).insert(this.getCursorPosition(), c).toString();
					if (Utils.getInt(text) < 1000 && Utils.getInt(text) > -1000) {
						super.charTyped(c, i);
						ModConfigs.setPartyXPos(Utils.getInt(getValue()));
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
				ModConfigs.setPartyXPos(Utils.getInt(getValue()));
				return true;
			}
			
		});
		
		addRenderableWidget(partyYPosBox = new EditBox(minecraft.font, buttonsX, (int) (topBarHeight + 20 * ++pos), minecraft.font.width("#####"), 16, new TranslatableComponent("test")){
			@Override
			public boolean charTyped(char c, int i) {
				if (Utils.isNumber(c) || c == '-') {
					String text = new StringBuilder(this.getValue()).insert(this.getCursorPosition(), c).toString();
					if (Utils.getInt(text) < 1000 && Utils.getInt(text) > -1000) {
						super.charTyped(c, i);
						ModConfigs.setPartyYPos(Utils.getInt(getValue()));
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
				ModConfigs.setPartyYPos(Utils.getInt(getValue()));
				return true;
			}
			
		});
		
		addRenderableWidget(partyYDistanceBox = new EditBox(minecraft.font, buttonsX, (int) (topBarHeight + 20 * ++pos), minecraft.font.width("#####"), 16, new TranslatableComponent("test")){
			@Override
			public boolean charTyped(char c, int i) {
				if (Utils.isNumber(c) || c == '-') {
					String text = new StringBuilder(this.getValue()).insert(this.getCursorPosition(), c).toString();
					if (Utils.getInt(text) < 1000 && Utils.getInt(text) > -1000) {
						super.charTyped(c, i);
						ModConfigs.setPartyYDistance(Utils.getInt(getValue()));
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
				ModConfigs.setPartyYDistance(Utils.getInt(getValue()));
				return true;
			}
			
		});
		
		partyXPosBox.setValue(""+ModConfigs.partyXPos);
		partyYPosBox.setValue(""+ModConfigs.partyYPos);
		partyYDistanceBox.setValue(""+ModConfigs.partyYDistance);
		
		partyList.add(partyXPosBox);
		partyList.add(partyYPosBox);
		partyList.add(partyYDistanceBox);

	}
	
	private void initFocus() {
		int pos = 0;
		
		addRenderableWidget(focusXPosBox = new EditBox(minecraft.font, buttonsX, (int) (topBarHeight + 20 * ++pos), minecraft.font.width("#####"), 16, new TranslatableComponent("test")){
			@Override
			public boolean charTyped(char c, int i) {
				if (Utils.isNumber(c) || c == '-') {
					String text = new StringBuilder(this.getValue()).insert(this.getCursorPosition(), c).toString();
					if (Utils.getInt(text) < 1000 && Utils.getInt(text) > -1000) {
						super.charTyped(c, i);
						ModConfigs.setFocusXPos(Utils.getInt(getValue()));
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
				ModConfigs.setFocusXPos(Utils.getInt(getValue()));
				return true;
			}
			
		});
		
		addRenderableWidget(focusYPosBox = new EditBox(minecraft.font, buttonsX, (int) (topBarHeight + 20 * ++pos), minecraft.font.width("#####"), 16, new TranslatableComponent("test")){
			@Override
			public boolean charTyped(char c, int i) {
				if (Utils.isNumber(c) || c == '-') {
					String text = new StringBuilder(this.getValue()).insert(this.getCursorPosition(), c).toString();
					if (Utils.getInt(text) < 1000 && Utils.getInt(text) > -1000) {
						super.charTyped(c, i);
						ModConfigs.setFocusYPos(Utils.getInt(getValue()));
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
				ModConfigs.setFocusYPos(Utils.getInt(getValue()));
				return true;
			}
			
		});
		
		focusXPosBox.setValue(""+ModConfigs.focusXPos);
		focusYPosBox.setValue(""+ModConfigs.focusYPos);
		
		focusList.add(focusXPosBox);
		focusList.add(focusYPosBox);
	}
	
	private void initImpExp() {
		int pos = 0;

		addRenderableWidget(importCode = new EditBoxLength(minecraft.font, buttonsX, (int) (topBarHeight + 20 * ++pos), minecraft.font.width("##############################"), 16, 100, new TranslatableComponent("test")){
			@Override
			public boolean charTyped(char c, int i) {
				super.charTyped(c, i);
				return true;
			}

			@Override
			public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
				super.keyPressed(keyCode, scanCode, modifiers);
				return true;
			}

		});

		addRenderableWidget(Import = new Button(buttonsX - 1, (int) topBarHeight + 20 * ++pos - 2, minecraft.font.width("#######")+2, 20, new TranslatableComponent("gui.menu.config.impexp.import"), (e) -> {
			readImportCode(importCode.getValue());
		}));

		addRenderableWidget(export = new Button(buttonsX - 1, (int) topBarHeight + 20 * ++pos - 2, minecraft.font.width("###################")+2, 20, new TranslatableComponent("gui.menu.config.impexp.export"), (e) -> {
			Minecraft.getInstance().keyboardHandler.setClipboard(exportCode());
		}));

		impExpList.add(importCode);
		impExpList.add(Import);
		impExpList.add(export);
	}

	@Override
	public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		commandMenuButton.active = window != ActualWindow.COMMAND_MENU;
		hpButton.active = window != ActualWindow.HP;
		mpButton.active = window != ActualWindow.MP;
		dpButton.active = window != ActualWindow.DRIVE;
		playerSkinButton.active = window != ActualWindow.PLAYER;
		lockOnButton.active = window != ActualWindow.LOCK_ON_HP;
		partyButton.active = window != ActualWindow.PARTY;
		focusButton.active = window != ActualWindow.FOCUS;
		impExButton.active = window != ActualWindow.IMPORT_EXPORT;
		
		box.draw(matrixStack);
		super.render(matrixStack, mouseX, mouseY, partialTicks);

		for(AbstractWidget b : commandMenuList) {
			b.active = false;
			b.visible = false;
		}
		
		for(AbstractWidget b : hpList) {
			b.active = false;
			b.visible = false;
		}
		
		for(AbstractWidget b : mpList) {
			b.active = false;
			b.visible = false;
		}
		
		for(AbstractWidget b : dpList) {
			b.active = false;
			b.visible = false;
		}
		
		for(AbstractWidget b : playerSkinList) {
			b.active = false;
			b.visible = false;
		}

		for(AbstractWidget b : lockOnList) {
			b.active = false;
			b.visible = false;
		}
		
		for(AbstractWidget b : partyList) {
			b.active = false;
			b.visible = false;
		}
		
		for(AbstractWidget b : focusList) {
			b.active = false;
			b.visible = false;
		}

		for (AbstractWidget b : impExpList) {
			b.active = false;
			b.visible = false;
		}

		matrixStack.pushPose();
		{
			int pos = 0;
			matrixStack.translate(buttonsX, box.y + 4, 1);
			
			switch (window) {
			case COMMAND_MENU:
				for (AbstractWidget b : commandMenuList) {
					b.active = true;
					b.visible = true;
				}

				drawString(matrixStack, minecraft.font, Utils.translateToLocal("gui.menu.config.command_menu"), 20, 0, 0xFF9900);
				drawString(matrixStack, minecraft.font, Utils.translateToLocal("gui.menu.config.x_scale"), 40, 20 * ++pos, 0xFF9900);
				drawString(matrixStack, minecraft.font, Utils.translateToLocal("gui.menu.config.x_pos"), 40, 20 * ++pos, 0xFF9900);
				drawString(matrixStack, minecraft.font, Utils.translateToLocal("gui.menu.config.selected_x_pos"), 40, 20 * ++pos, 0xFF9900);
				drawString(matrixStack, minecraft.font, Utils.translateToLocal("gui.menu.config.sub_x_offset"), 40, 20 * ++pos, 0xFF9900);
				drawString(matrixStack, minecraft.font, Utils.translateToLocal("gui.menu.config.header_title"), 40, 20 * ++pos, 0xFF9900);
				drawString(matrixStack, minecraft.font, Utils.translateToLocal("gui.menu.config.text_x_offset"), 40, 20 * ++pos, 0xFF9900);

				break;

			case HP:
				for (AbstractWidget b : hpList) {
					b.active = true;
					b.visible = true;
				}

				drawString(matrixStack, minecraft.font, Utils.translateToLocal("gui.menu.config.hp"), 20, 0, 0xFF9900);
				drawString(matrixStack, minecraft.font, Utils.translateToLocal("gui.menu.config.x_pos"), 40, 20 * ++pos, 0xFF9900);
				drawString(matrixStack, minecraft.font, Utils.translateToLocal("gui.menu.config.y_pos"), 40, 20 * ++pos, 0xFF9900);
				drawString(matrixStack, minecraft.font, Utils.translateToLocal("gui.menu.config.show_hearts"), 40, 20 * ++pos, 0xFF9900);
				drawString(matrixStack, minecraft.font, Utils.translateToLocal("gui.menu.config.hp_alarm"), 40, 20 * ++pos, 0xFF9900);

				break;

			case MP:
				for (AbstractWidget b : mpList) {
					b.active = true;
					b.visible = true;
				}

				drawString(matrixStack, minecraft.font, Utils.translateToLocal("gui.menu.config.mp"), 20, 0, 0xFF9900);
				drawString(matrixStack, minecraft.font, Utils.translateToLocal("gui.menu.config.x_pos"), 40, 20 * ++pos, 0xFF9900);
				drawString(matrixStack, minecraft.font, Utils.translateToLocal("gui.menu.config.y_pos"), 40, 20 * ++pos, 0xFF9900);

				break;

			case DRIVE:
				for (AbstractWidget b : dpList) {
					b.active = true;
					b.visible = true;
				}

				drawString(matrixStack, minecraft.font, Utils.translateToLocal("gui.menu.config.dp"), 20, 0, 0xFF9900);
				drawString(matrixStack, minecraft.font, Utils.translateToLocal("gui.menu.config.x_pos"), 40, 20 * ++pos, 0xFF9900);
				drawString(matrixStack, minecraft.font, Utils.translateToLocal("gui.menu.config.y_pos"), 40, 20 * ++pos, 0xFF9900);

				break;

			case PLAYER:
				for (AbstractWidget b : playerSkinList) {
					b.active = true;
					b.visible = true;
				}

				drawString(matrixStack, minecraft.font, Utils.translateToLocal("gui.menu.config.player_skin"), 20, 0, 0xFF9900);
				drawString(matrixStack, minecraft.font, Utils.translateToLocal("gui.menu.config.x_pos"), 40, 20 * ++pos, 0xFF9900);
				drawString(matrixStack, minecraft.font, Utils.translateToLocal("gui.menu.config.y_pos"), 40, 20 * ++pos, 0xFF9900);

				break;

			case LOCK_ON_HP:
				for (AbstractWidget b : lockOnList) {
					b.active = true;
					b.visible = true;
				}

				drawString(matrixStack, minecraft.font, Utils.translateToLocal("gui.menu.config.lock_on_hp"), 20, 0, 0xFF9900);
				drawString(matrixStack, minecraft.font, Utils.translateToLocal("gui.menu.config.x_pos"), 40, 20 * ++pos, 0xFF9900);
				drawString(matrixStack, minecraft.font, Utils.translateToLocal("gui.menu.config.y_pos"), 40, 20 * ++pos, 0xFF9900);
				drawString(matrixStack, minecraft.font, Utils.translateToLocal("gui.menu.config.hp_scale"), 40, 20 * ++pos, 0xFF9900);
				drawString(matrixStack, minecraft.font, Utils.translateToLocal("gui.menu.config.icon_scale"), 40, 20 * ++pos, 0xFF9900);
				drawString(matrixStack, minecraft.font, Utils.translateToLocal("gui.menu.config.icon_rotation"), 40, 20 * ++pos, 0xFF9900);
				drawString(matrixStack, minecraft.font, Utils.translateToLocal("gui.menu.config.hp_per_bar"), 40, 20 * ++pos, 0xFF9900);

				break;

			case PARTY:
				for (AbstractWidget b : partyList) {
					b.active = true;
					b.visible = true;
				}

				drawString(matrixStack, minecraft.font, Utils.translateToLocal("gui.menu.config.party"), 20, 0, 0xFF9900);
				drawString(matrixStack, minecraft.font, Utils.translateToLocal("gui.menu.config.x_pos"), 40, 20 * ++pos, 0xFF9900);
				drawString(matrixStack, minecraft.font, Utils.translateToLocal("gui.menu.config.y_pos"), 40, 20 * ++pos, 0xFF9900);
				drawString(matrixStack, minecraft.font, Utils.translateToLocal("gui.menu.config.y_dist"), 40, 20 * ++pos, 0xFF9900);

				break;

			case FOCUS:
				for (AbstractWidget b : focusList) {
					b.active = true;
					b.visible = true;
				}

				drawString(matrixStack, minecraft.font, Utils.translateToLocal("gui.menu.config.focus"), 20, 0, 0xFF9900);
				drawString(matrixStack, minecraft.font, Utils.translateToLocal("gui.menu.config.x_pos"), 40, 20 * ++pos, 0xFF9900);
				drawString(matrixStack, minecraft.font, Utils.translateToLocal("gui.menu.config.y_pos"), 40, 20 * ++pos, 0xFF9900);

				break;

			case IMPORT_EXPORT:
				for (AbstractWidget b : impExpList) {
					b.active = true;
					b.visible = true;
				}


				break;
			}

		}
		matrixStack.popPose();

	}

	public Map<Character, Integer> getOptionsMap() {
		Map<Character, Integer> options = new HashMap<>();
		options.put('A', Integer.valueOf(cmXScaleBox.getValue()));
		options.put('B', Integer.valueOf(cmXPosBox.getValue()));
		options.put('C', Integer.valueOf(cmSelectedXOffsetBox.getValue()));
		options.put('D', Integer.valueOf(cmSubXOffsetBox.getValue()));
		options.put('E', cmHeaderTextVisible ? 1 : 0);
		options.put('F', Integer.valueOf(cmTextXOffsetBox.getValue()));
		options.put('G', Integer.valueOf(hpXPosBox.getValue()));
		options.put('H', Integer.valueOf(hpYPosBox.getValue()));
		options.put('I', hpShowHearts ? 1 : 0);
		options.put('J', Integer.valueOf(mpXPosBox.getValue()));
		options.put('K', Integer.valueOf(mpYPosBox.getValue()));
		options.put('L', Integer.valueOf(dpXPosBox.getValue()));
		options.put('M', Integer.valueOf(dpYPosBox.getValue()));
		options.put('N', Integer.valueOf(playerSkinXPosBox.getValue()));
		options.put('O', Integer.valueOf(playerSkinYPosBox.getValue()));
		options.put('P', Integer.valueOf(lockOnXPosBox.getValue()));
		options.put('Q', Integer.valueOf(lockOnYPosBox.getValue()));
		options.put('R', Integer.valueOf(lockOnHPScaleBox.getValue()));
		options.put('S', Integer.valueOf(lockOnIconScaleBox.getValue()));
		options.put('T', Integer.valueOf(lockOnIconRotationBox.getValue()));
		options.put('U', Integer.valueOf(lockOnHpPerBarBox.getValue()));
		options.put('V', Integer.valueOf(partyXPosBox.getValue()));
		options.put('W', Integer.valueOf(partyYPosBox.getValue()));
		options.put('X', Integer.valueOf(partyYDistanceBox.getValue()));
		options.put('Y', Integer.valueOf(focusXPosBox.getValue()));
		options.put('Z', Integer.valueOf(focusYPosBox.getValue()));
		options.put('+', hpShowHearts ? 1 : 0);
		return options;
	}

	public String exportCode() {
		Map<Character, Integer> options = getOptionsMap();
		Map<String, String> pValues = new HashMap<>();
		Map<String, String> nValues = new HashMap<>();
		options.forEach((c, i) -> {
			boolean minus = i < 0;
			if (minus) i = Math.abs(i);
			String b36 = toBase36(i);
			Map<String, String> values = minus ? nValues : pValues;
			if (i != 0) {
				if (values.containsKey(b36)) {
					values.replace(b36, values.get(b36).concat(c.toString()));
				} else {
					values.put(b36, c.toString());
				}
			}
		});
		StringBuilder builder = new StringBuilder();
		pValues.forEach((b36, c) -> {
			builder.append(b36);
			builder.append(c);
		});
		if (nValues.size() > 0) {
			builder.append("-");
			nValues.forEach((b36, c) -> {
				builder.append(b36);
				builder.append(c);
			});
		}
		return builder.toString();
	}

	public void readImportCode(String code) {
		if (!isBase36Char(code.charAt(0)) || isBase36Char(code.charAt(code.length()-1))) {
			KingdomKeys.LOGGER.info("invalid import code");
			return;
		}
		String remaningCode = code;
		List<Integer> b10vals = new ArrayList<>();
		List<String> configs = new ArrayList<>();
		boolean minusToggle = false;
		StringBuilder currentVal = new StringBuilder();
		boolean base36 = true;
		while (remaningCode.length() > 0) {
			char currChar = remaningCode.charAt(0);
			if (currChar == '-') {
				remaningCode = remaningCode.substring(1);
				minusToggle = true;
				currChar = remaningCode.charAt(0);
			}
			if (base36) {
				if (!isBase36Char(currChar)) {
					int b10val = toBase10(currentVal.toString());
					if (minusToggle) b10val = -b10val;
					b10vals.add(b10val);
					base36 = false;
					currentVal = new StringBuilder();
				}
			} else {
				if (isBase36Char(currChar)) {
					configs.add(currentVal.toString());
					base36 = true;
					currentVal = new StringBuilder();
				}
			}
			currentVal.append(currChar);
			remaningCode = remaningCode.substring(1);
			if (remaningCode.length() == 0) {
				configs.add(currentVal.toString());
			}
		}

		if (b10vals.size() != configs.size()) {
			KingdomKeys.LOGGER.info("invalid import code");
			return;
		}

		setAllZero();

		for (int i = 0; i < b10vals.size(); i++) {
			for (int j = 0; j < configs.get(i).length(); j++) {
				importSetting(configs.get(i).charAt(j), b10vals.get(i));
			}
		}
	}

	public void setAllZero() {
		ModConfigs.setCmXScale(0);
		ModConfigs.setCmXPos(0);
		ModConfigs.setCmSelectedXOffset(0);
		ModConfigs.setCmSubXOffset(0);
		ModConfigs.setCmHeaderTextVisible(false);
		ModConfigs.setCmTextXOffset(0);
		ModConfigs.setHpXPos(0);
		ModConfigs.setHpYPos(0);
		ModConfigs.setShowHearts(false);
		ModConfigs.setMpXPos(0);
		ModConfigs.setMpYPos(0);
		ModConfigs.setDpXPos(0);
		ModConfigs.setDpYPos(0);
		ModConfigs.setPlayerSkinXPos(0);
		ModConfigs.setPlayerSkinYPos(0);
		ModConfigs.setLockOnXPos(0);
		ModConfigs.setLockOnYPos(0);
		ModConfigs.setLockOnHPScale(0);
		ModConfigs.setLockOnIconScale(0);
		ModConfigs.setLockOnIconRotation(0);
		ModConfigs.setLockOnHpPerBar(0);
		ModConfigs.setPartyXPos(0);
		ModConfigs.setPartyYPos(0);
		ModConfigs.setPartyYDistance(0);
		ModConfigs.setFocusXPos(0);
		ModConfigs.setFocusYPos(0);
		ModConfigs.setHPAlarm(0);
	}

	public boolean isBase36Char(char c) {
		return Utils.isNumber(c) || Character.isLowerCase(c);
	}

	public String toBase36(int value) {
		return Integer.toString(value, 36);
	}

	public int toBase10(String value) {
		try {
			return Integer.valueOf(value, 36);
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	//for new options use special characters, (. , + etc.) excluding '-'
	public void importSetting(char c, int value) {
		switch (c) {
			case 'A' -> {
				ModConfigs.setCmXScale(value);
				cmXScaleBox.setValue(""+value);
			}
			case 'B' -> {
				ModConfigs.setCmXPos(value);
				cmXPosBox.setValue(""+value);
			}
			case 'C' -> {
				ModConfigs.setCmSelectedXOffset(value);
				cmSelectedXOffsetBox.setValue(""+value);
			}
			case 'D' -> {
				ModConfigs.setCmSubXOffset(value);
				cmSubXOffsetBox.setValue(""+value);
			}
			case 'E' -> {
				ModConfigs.setCmHeaderTextVisible(value == 1);
				cmHeaderTextVisible = value == 1;
				cmHeaderTextVisibleButton.setMessage(new TranslatableComponent(cmHeaderTextVisible+""));
			}
			case 'F' -> {
				ModConfigs.setCmTextXOffset(value);
				cmTextXOffsetBox.setValue(""+value);
			}
			case 'G' -> {
				ModConfigs.setHpXPos(value);
				hpXPosBox.setValue(""+value);
			}
			case 'H' -> {
				ModConfigs.setHpYPos(value);
				hpYPosBox.setValue(""+value);
			}
			case 'I' -> {
				ModConfigs.setShowHearts(value == 1);
				hpShowHearts = value == 1;
				hpShowHeartsButton.setMessage(new TranslatableComponent(hpShowHearts+""));
			}
			case 'J' -> {
				ModConfigs.setMpXPos(value);
				mpXPosBox.setValue(""+value);
			}
			case 'K' -> {
				ModConfigs.setMpYPos(value);
				mpYPosBox.setValue(""+value);
			}
			case 'L' -> {
				ModConfigs.setDpXPos(value);
				dpXPosBox.setValue(""+value);
			}
			case 'M' -> {
				ModConfigs.setDpYPos(value);
				dpYPosBox.setValue(""+value);
			}
			case 'N' -> {
				ModConfigs.setPlayerSkinXPos(value);
				playerSkinXPosBox.setValue(""+value);
			}
			case 'O' -> {
				ModConfigs.setPlayerSkinYPos(value);
				playerSkinYPosBox.setValue(""+value);
			}
			case 'P' -> {
				ModConfigs.setLockOnXPos(value);
				lockOnXPosBox.setValue(""+value);
			}
			case 'Q' -> {
				ModConfigs.setLockOnYPos(value);
				lockOnYPosBox.setValue(""+value);
			}
			case 'R' -> {
				ModConfigs.setLockOnHPScale(value);
				lockOnHPScaleBox.setValue(""+value);
			}
			case 'S' -> {
				ModConfigs.setLockOnIconScale(value);
				lockOnIconScaleBox.setValue(""+value);
			}
			case 'T' -> {
				ModConfigs.setLockOnIconRotation(value);
				lockOnIconRotationBox.setValue(""+value);
			}
			case 'U' -> {
				ModConfigs.setLockOnHpPerBar(value);
				lockOnHpPerBarBox.setValue(""+value);
			}
			case 'V' -> {
				ModConfigs.setPartyXPos(value);
				partyXPosBox.setValue(""+value);
			}
			case 'W' -> {
				ModConfigs.setPartyYPos(value);
				partyYPosBox.setValue(""+value);
			}
			case 'X' -> {
				ModConfigs.setPartyYDistance(value);
				partyYDistanceBox.setValue(""+value);
			}
			case 'Y' -> {
				ModConfigs.setFocusXPos(value);
				focusXPosBox.setValue(""+value);
			}
			case 'Z' -> {
				ModConfigs.setFocusYPos(value);
				focusYPosBox.setValue(""+value);
			}
			case '+' -> {
				ModConfigs.setHPAlarm(value);
				hpAlarmBox.setValue(""+value);
			}
		}
	}
}

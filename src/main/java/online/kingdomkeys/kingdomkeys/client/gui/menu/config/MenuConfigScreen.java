package online.kingdomkeys.kingdomkeys.client.gui.menu.config;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.TranslatableComponent;
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
		COMMAND_MENU, HP, MP, DRIVE, PLAYER, LOCK_ON_HP, PARTY, FOCUS
	}

	ActualWindow window = ActualWindow.COMMAND_MENU;
	
	MenuButton back, commandMenuButton, hpButton, mpButton, dpButton, playerSkinButton, lockOnButton, partyButton, focusButton;
	Button backgroundButton;
	MenuBox box;
	
	//Command Menu
	EditBox cmTextXOffsetBox, cmXScaleBox, cmXPosBox, cmSubXOffsetBox;
	Button cmHeaderTextVisibleButton;
	boolean cmHeaderTextVisible;
	
	//HP
	EditBox hpXPosBox, hpYPosBox;
	Button hpShowHeartsButton;
	boolean hpShowHearts;

	//MP
	EditBox mpXPosBox, mpYPosBox;

	//DP
	EditBox dpXPosBox, dpYPosBox;
	
	//PlayerSkin
	EditBox playerSkinXPosBox, playerSkinYPosBox;

	//Lock On
	EditBox lockOnXPosBox, lockOnYPosBox, lockOnHPScaleBox, lockOnIconScaleBox, lockOnHpPerBarBox;

	//Party
	EditBox partyXPosBox, partyYPosBox, partyYDistanceBox;

	//Focus
	EditBox focusXPosBox, focusYPosBox;
	
	List<AbstractWidget> commandMenuList = new ArrayList<AbstractWidget>();
	List<AbstractWidget> hpList = new ArrayList<AbstractWidget>();
	List<AbstractWidget> mpList = new ArrayList<AbstractWidget>();
	List<AbstractWidget> dpList = new ArrayList<AbstractWidget>();
	List<AbstractWidget> playerSkinList = new ArrayList<AbstractWidget>();
	List<AbstractWidget> lockOnList = new ArrayList<AbstractWidget>();
	List<AbstractWidget> partyList = new ArrayList<AbstractWidget>();
	List<AbstractWidget> focusList = new ArrayList<AbstractWidget>();

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
		
		addWidget(commandMenuButton = new MenuButton((int) buttonPosX, (int) topBarHeight + 5 + (0 * 18), (int) buttonWidth, Utils.translateToLocal("gui.menu.config.command_menu"), ButtonType.BUTTON, (e) -> { window = ActualWindow.COMMAND_MENU; }));
		addWidget(hpButton = new MenuButton((int) buttonPosX, (int) topBarHeight + 5 + (1 * 18), (int) buttonWidth, Utils.translateToLocal("gui.menu.config.hp"), ButtonType.BUTTON, (e) -> { window = ActualWindow.HP; }));
		addWidget(mpButton = new MenuButton((int) buttonPosX, (int) topBarHeight + 5 + (2 * 18), (int) buttonWidth, Utils.translateToLocal("gui.menu.config.mp"), ButtonType.BUTTON, (e) -> { window = ActualWindow.MP; }));
		addWidget(dpButton = new MenuButton((int) buttonPosX, (int) topBarHeight + 5 + (3 * 18), (int) buttonWidth, Utils.translateToLocal("gui.menu.config.dp"), ButtonType.BUTTON, (e) -> { window = ActualWindow.DRIVE; }));
		addWidget(playerSkinButton = new MenuButton((int) buttonPosX, (int) topBarHeight + 5 + (4 * 18), (int) buttonWidth, Utils.translateToLocal("gui.menu.config.player_skin"), ButtonType.BUTTON, (e) -> { window = ActualWindow.PLAYER; }));
		addWidget(lockOnButton = new MenuButton((int) buttonPosX, (int) topBarHeight + 5 + (5 * 18), (int) buttonWidth, Utils.translateToLocal("gui.menu.config.lock_on_hp"), ButtonType.BUTTON, (e) -> { window = ActualWindow.LOCK_ON_HP; }));
		addWidget(partyButton = new MenuButton((int) buttonPosX, (int) topBarHeight + 5 + (6 * 18), (int) buttonWidth, Utils.translateToLocal("gui.menu.config.party"), ButtonType.BUTTON, (e) -> { window = ActualWindow.PARTY; }));
		addWidget(focusButton = new MenuButton((int) buttonPosX, (int) topBarHeight + 5 + (7 * 18), (int) buttonWidth, Utils.translateToLocal("gui.menu.config.focus"), ButtonType.BUTTON, (e) -> { window = ActualWindow.FOCUS; }));

		addWidget(back = new MenuButton((int) buttonPosX, (int) topBarHeight + 5 + (8 * 18), (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Menu_Back), ButtonType.BUTTON, (e) -> { action("back"); }));
		addWidget(backgroundButton = new MenuButton((int) width / 2 - (int)buttonWidth / 2, (int) topBarHeight + 5 + (7-2 * 18), (int) buttonWidth, Utils.translateToLocal("gui.menu.config.bg"), ButtonType.BUTTON, (e) -> { drawSeparately = !drawSeparately; }));
	}

	private void initCommandMenu() {
		cmHeaderTextVisible = ModConfigs.cmHeaderTextVisible;
		int pos = 0;

		addWidget(cmXScaleBox = new EditBox(minecraft.font, buttonsX, (int) (topBarHeight + 20 * ++pos), minecraft.font.width("#####"), 16, new TranslatableComponent("test")){
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
		
		addWidget(cmXPosBox = new EditBox(minecraft.font, buttonsX, (int) (topBarHeight + 20 * ++pos), minecraft.font.width("#####"), 16, new TranslatableComponent("test")){
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
		
		addWidget(cmSubXOffsetBox = new EditBox(minecraft.font, buttonsX, (int) (topBarHeight + 20 * ++pos), minecraft.font.width("#####"), 16, new TranslatableComponent("test")){
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
		
		addWidget(cmHeaderTextVisibleButton = new Button(buttonsX - 1, (int) topBarHeight + 20 * ++pos - 2, minecraft.font.width("#####")+2, 20, new TranslatableComponent(cmHeaderTextVisible+""), (e) -> { action("textHeaderVisibility"); }));
		addWidget(cmTextXOffsetBox = new EditBox(minecraft.font, buttonsX, (int) (topBarHeight + 20 * ++pos), minecraft.font.width("#####"), 16, new TranslatableComponent("test")){
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
		cmSubXOffsetBox.setValue(""+ModConfigs.cmSubXOffset);
		
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
		
		addWidget(hpXPosBox = new EditBox(minecraft.font, buttonsX, (int) (topBarHeight + 20 * ++pos), minecraft.font.width("#####"), 16, new TranslatableComponent("test")){
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
		
		addWidget(hpYPosBox = new EditBox(minecraft.font, buttonsX, (int) (topBarHeight + 20 * ++pos), minecraft.font.width("#####"), 16, new TranslatableComponent("test")){
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
		
		addWidget(hpShowHeartsButton = new Button(buttonsX - 1, (int) topBarHeight + 20 * ++pos - 2, minecraft.font.width("#####")+2, 20, new TranslatableComponent(hpShowHearts+""), (e) -> { action("hpShowHearts"); }));

		
		hpXPosBox.setValue(""+ModConfigs.hpXPos);
		hpYPosBox.setValue(""+ModConfigs.hpYPos);
		hpShowHeartsButton.setMessage(new TranslatableComponent(hpShowHearts+""));
		
		hpList.add(hpXPosBox);
		hpList.add(hpYPosBox);
		hpList.add(hpShowHeartsButton);

	}
	
	private void initMP() {
		int pos = 0;
		
		addWidget(mpXPosBox = new EditBox(minecraft.font, buttonsX, (int) (topBarHeight + 20 * ++pos), minecraft.font.width("#####"), 16, new TranslatableComponent("test")){
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
		
		addWidget(mpYPosBox = new EditBox(minecraft.font, buttonsX, (int) (topBarHeight + 20 * ++pos), minecraft.font.width("#####"), 16, new TranslatableComponent("test")){
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
		
		addWidget(dpXPosBox = new EditBox(minecraft.font, buttonsX, (int) (topBarHeight + 20 * ++pos), minecraft.font.width("#####"), 16, new TranslatableComponent("test")){
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
		
		addWidget(dpYPosBox = new EditBox(minecraft.font, buttonsX, (int) (topBarHeight + 20 * ++pos), minecraft.font.width("#####"), 16, new TranslatableComponent("test")){
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
		
		addWidget(playerSkinXPosBox = new EditBox(minecraft.font, buttonsX, (int) (topBarHeight + 20 * ++pos), minecraft.font.width("#####"), 16, new TranslatableComponent("test")){
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
		
		addWidget(playerSkinYPosBox = new EditBox(minecraft.font, buttonsX, (int) (topBarHeight + 20 * ++pos), minecraft.font.width("#####"), 16, new TranslatableComponent("test")){
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
		
		addWidget(lockOnXPosBox = new EditBox(minecraft.font, buttonsX, (int) (topBarHeight + 20 * ++pos), minecraft.font.width("#####"), 16, new TranslatableComponent("test")){
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
		
		addWidget(lockOnYPosBox = new EditBox(minecraft.font, buttonsX, (int) (topBarHeight + 20 * ++pos), minecraft.font.width("#####"), 16, new TranslatableComponent("test")){
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
		
		addWidget(lockOnHPScaleBox = new EditBox(minecraft.font, buttonsX, (int) (topBarHeight + 20 * ++pos), minecraft.font.width("#####"), 16, new TranslatableComponent("test")){
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
		
		addWidget(lockOnIconScaleBox = new EditBox(minecraft.font, buttonsX, (int) (topBarHeight + 20 * ++pos), minecraft.font.width("#####"), 16, new TranslatableComponent("test")){
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
		
		addWidget(lockOnHpPerBarBox = new EditBox(minecraft.font, buttonsX, (int) (topBarHeight + 20 * ++pos), minecraft.font.width("#####"), 16, new TranslatableComponent("test")){
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
		lockOnHpPerBarBox.setValue(""+ModConfigs.lockOnHpPerBar);
		
		lockOnList.add(lockOnXPosBox);
		lockOnList.add(lockOnYPosBox);
		lockOnList.add(lockOnHPScaleBox);
		lockOnList.add(lockOnIconScaleBox);
		lockOnList.add(lockOnHpPerBarBox);
	}
	
	private void initParty() {
		int pos = 0;
		
		addWidget(partyXPosBox = new EditBox(minecraft.font, buttonsX, (int) (topBarHeight + 20 * ++pos), minecraft.font.width("#####"), 16, new TranslatableComponent("test")){
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
		
		addWidget(partyYPosBox = new EditBox(minecraft.font, buttonsX, (int) (topBarHeight + 20 * ++pos), minecraft.font.width("#####"), 16, new TranslatableComponent("test")){
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
		
		addWidget(partyYDistanceBox = new EditBox(minecraft.font, buttonsX, (int) (topBarHeight + 20 * ++pos), minecraft.font.width("#####"), 16, new TranslatableComponent("test")){
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
		
		addWidget(focusXPosBox = new EditBox(minecraft.font, buttonsX, (int) (topBarHeight + 20 * ++pos), minecraft.font.width("#####"), 16, new TranslatableComponent("test")){
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
		
		addWidget(focusYPosBox = new EditBox(minecraft.font, buttonsX, (int) (topBarHeight + 20 * ++pos), minecraft.font.width("#####"), 16, new TranslatableComponent("test")){
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

		switch(window) {
		case COMMAND_MENU:
			for(AbstractWidget b : commandMenuList) {
				b.active = true;
				b.visible = true;
			}
			
			matrixStack.pushPose();
			{
				int pos = 0;
				matrixStack.translate(buttonsX, box.y+4, 1);
				drawString(matrixStack, minecraft.font, Utils.translateToLocal("gui.menu.config.command_menu"), 20, 0, 0xFF9900);
				drawString(matrixStack, minecraft.font, Utils.translateToLocal("gui.menu.config.x_scale"), 40, 20 * ++pos, 0xFF9900);
				drawString(matrixStack, minecraft.font, Utils.translateToLocal("gui.menu.config.x_pos"), 40, 20 * ++pos, 0xFF9900);
				drawString(matrixStack, minecraft.font, Utils.translateToLocal("gui.menu.config.sub_x_offset"), 40, 20 * ++pos, 0xFF9900);
				drawString(matrixStack, minecraft.font, Utils.translateToLocal("gui.menu.config.header_title"), 40, 20 * ++pos, 0xFF9900);
				drawString(matrixStack, minecraft.font, Utils.translateToLocal("gui.menu.config.text_x_offset"), 40, 20 * ++pos, 0xFF9900);
			}
			matrixStack.popPose();
			
			break;
			
		case HP:
			for(AbstractWidget b : hpList) {
				b.active = true;
				b.visible = true;
			}
			
			matrixStack.pushPose();
			{
				int pos = 0;
				matrixStack.translate(buttonsX, box.y+4, 1);
				drawString(matrixStack, minecraft.font, Utils.translateToLocal("gui.menu.config.hp"), 20, 0, 0xFF9900);
				drawString(matrixStack, minecraft.font, Utils.translateToLocal("gui.menu.config.x_pos"), 40, 20 * ++pos, 0xFF9900);
				drawString(matrixStack, minecraft.font, Utils.translateToLocal("gui.menu.config.y_pos"), 40, 20 * ++pos, 0xFF9900);
				drawString(matrixStack, minecraft.font, Utils.translateToLocal("gui.menu.config.show_hearts"), 40, 20 * ++pos, 0xFF9900);
			}
			matrixStack.popPose();
			
			break;
			
		case MP:
			for(AbstractWidget b : mpList) {
				b.active = true;
				b.visible = true;
			}
			
			matrixStack.pushPose();
			{
				int pos = 0;
				matrixStack.translate(buttonsX, box.y+4, 1);
				drawString(matrixStack, minecraft.font, Utils.translateToLocal("gui.menu.config.mp"), 20, 0, 0xFF9900);
				drawString(matrixStack, minecraft.font, Utils.translateToLocal("gui.menu.config.x_pos"), 40, 20 * ++pos, 0xFF9900);
				drawString(matrixStack, minecraft.font, Utils.translateToLocal("gui.menu.config.y_pos"), 40, 20 * ++pos, 0xFF9900);
			}
			matrixStack.popPose();
			
			break;
			
		case DRIVE:
			for(AbstractWidget b : dpList) {
				b.active = true;
				b.visible = true;
			}
			
			matrixStack.pushPose();
			{
				int pos = 0;
				matrixStack.translate(buttonsX, box.y+4, 1);
				drawString(matrixStack, minecraft.font, Utils.translateToLocal("gui.menu.config.dp"), 20, 0, 0xFF9900);
				drawString(matrixStack, minecraft.font, Utils.translateToLocal("gui.menu.config.x_pos"), 40, 20 * ++pos, 0xFF9900);
				drawString(matrixStack, minecraft.font, Utils.translateToLocal("gui.menu.config.y_pos"), 40, 20 * ++pos, 0xFF9900);
			}
			matrixStack.popPose();
			
			break;
			
		case PLAYER:
			for(AbstractWidget b : playerSkinList) {
				b.active = true;
				b.visible = true;
			}
			
			matrixStack.pushPose();
			{
				int pos = 0;
				matrixStack.translate(buttonsX, box.y+4, 1);
				drawString(matrixStack, minecraft.font, Utils.translateToLocal("gui.menu.config.player_skin"), 20, 0, 0xFF9900);
				drawString(matrixStack, minecraft.font, Utils.translateToLocal("gui.menu.config.x_pos"), 40, 20 * ++pos, 0xFF9900);
				drawString(matrixStack, minecraft.font, Utils.translateToLocal("gui.menu.config.y_pos"), 40, 20 * ++pos, 0xFF9900);
			}
			matrixStack.popPose();
			
			break;
			
		case LOCK_ON_HP:
			for(AbstractWidget b : lockOnList) {
				b.active = true;
				b.visible = true;
			}
			
			matrixStack.pushPose();
			{
				int pos = 0;
				matrixStack.translate(buttonsX, box.y+4, 1);
				drawString(matrixStack, minecraft.font, Utils.translateToLocal("gui.menu.config.lock_on_hp"), 20, 0, 0xFF9900);
				drawString(matrixStack, minecraft.font, Utils.translateToLocal("gui.menu.config.x_pos"), 40, 20 * ++pos, 0xFF9900);
				drawString(matrixStack, minecraft.font, Utils.translateToLocal("gui.menu.config.y_pos"), 40, 20 * ++pos, 0xFF9900);
				drawString(matrixStack, minecraft.font, Utils.translateToLocal("gui.menu.config.hp_scale"), 40, 20 * ++pos, 0xFF9900);
				drawString(matrixStack, minecraft.font, Utils.translateToLocal("gui.menu.config.icon_scale"), 40, 20 * ++pos, 0xFF9900);
				drawString(matrixStack, minecraft.font, Utils.translateToLocal("gui.menu.config.hp_per_bar"), 40, 20 * ++pos, 0xFF9900);
			}
			matrixStack.popPose();
			
			break;
		
		case PARTY:
			for(AbstractWidget b : partyList) {
				b.active = true;
				b.visible = true;
			}
			
			matrixStack.pushPose();
			{
				int pos = 0;
				matrixStack.translate(buttonsX, box.y+4, 1);
				drawString(matrixStack, minecraft.font, Utils.translateToLocal("gui.menu.config.party"), 20, 0, 0xFF9900);
				drawString(matrixStack, minecraft.font, Utils.translateToLocal("gui.menu.config.x_pos"), 40, 20 * ++pos, 0xFF9900);
				drawString(matrixStack, minecraft.font, Utils.translateToLocal("gui.menu.config.y_pos"), 40, 20 * ++pos, 0xFF9900);
				drawString(matrixStack, minecraft.font, Utils.translateToLocal("gui.menu.config.y_dist"), 40, 20 * ++pos, 0xFF9900);
			}
			matrixStack.popPose();
			
			break;
			
		case FOCUS:
			for(AbstractWidget b : focusList) {
				b.active = true;
				b.visible = true;
			}
			
			matrixStack.pushPose();
			{
				int pos = 0;
				matrixStack.translate(buttonsX, box.y+4, 1);
				drawString(matrixStack, minecraft.font, Utils.translateToLocal("gui.menu.config.focus"), 20, 0, 0xFF9900);
				drawString(matrixStack, minecraft.font, Utils.translateToLocal("gui.menu.config.x_pos"), 40, 20 * ++pos, 0xFF9900);
				drawString(matrixStack, minecraft.font, Utils.translateToLocal("gui.menu.config.y_pos"), 40, 20 * ++pos, 0xFF9900);
			}
			matrixStack.popPose();
			
			break;
		}
	}	
}

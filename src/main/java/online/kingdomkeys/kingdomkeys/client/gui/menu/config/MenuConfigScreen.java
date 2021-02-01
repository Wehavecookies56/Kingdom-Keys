package online.kingdomkeys.kingdomkeys.client.gui.menu.config;

import com.mojang.blaze3d.systems.RenderSystem;
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

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MenuConfigScreen extends MenuBackground {

	enum ActualWindow {
		COMMAND_MENU, HP, MP, DRIVE, PLAYER, LOCK_ON_HP, PARTY
	}

	ActualWindow window = ActualWindow.COMMAND_MENU;
	
	MenuButton back, commandMenuButton, hpButton, mpButton, dpButton, playerSkinButton, lockOnButton, partyButton;
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

	//DP
	TextFieldWidget dpXPosBox, dpYPosBox;
	
	//PlayerSkin
	TextFieldWidget playerSkinXPosBox, playerSkinYPosBox;

	//Lock On
	TextFieldWidget lockOnXPosBox, lockOnYPosBox, lockOnHPScaleBox, lockOnIconScaleBox;

	//Party
	TextFieldWidget partyXPosBox, partyYPosBox, partyYOffsetBox;
		
		
	List<Widget> commandMenuList = new ArrayList<Widget>();
	List<Widget> hpList = new ArrayList<Widget>();
	List<Widget> mpList = new ArrayList<Widget>();
	List<Widget> dpList = new ArrayList<Widget>();
	List<Widget> playerSkinList = new ArrayList<Widget>();
	List<Widget> lockOnList = new ArrayList<Widget>();
	List<Widget> partyList = new ArrayList<Widget>();
	
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
			cmHeaderTextVisibleButton.setMessage(cmHeaderTextVisible+"");
			ModConfigs.setCmHeaderTextVisible(cmHeaderTextVisible);
			break;
		case "hpShowHearts":
			hpShowHearts = !hpShowHearts;
			hpShowHeartsButton.setMessage(hpShowHearts+"");
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
		initDP();
		initPlayerSkin();
		initLockOn();
		initParty();
		
		addButton(commandMenuButton = new MenuButton((int) buttonPosX, (int) topBarHeight + 5 + (0 * 18), (int) buttonWidth, Utils.translateToLocal("gui.menu.config.command_menu"), ButtonType.BUTTON, (e) -> { window = ActualWindow.COMMAND_MENU; }));
		addButton(hpButton = new MenuButton((int) buttonPosX, (int) topBarHeight + 5 + (1 * 18), (int) buttonWidth, Utils.translateToLocal("gui.menu.config.hp"), ButtonType.BUTTON, (e) -> { window = ActualWindow.HP; }));
		addButton(mpButton = new MenuButton((int) buttonPosX, (int) topBarHeight + 5 + (2 * 18), (int) buttonWidth, Utils.translateToLocal("gui.menu.config.mp"), ButtonType.BUTTON, (e) -> { window = ActualWindow.MP; }));
		addButton(dpButton = new MenuButton((int) buttonPosX, (int) topBarHeight + 5 + (3 * 18), (int) buttonWidth, Utils.translateToLocal("gui.menu.config.dp"), ButtonType.BUTTON, (e) -> { window = ActualWindow.DRIVE; }));
		addButton(playerSkinButton = new MenuButton((int) buttonPosX, (int) topBarHeight + 5 + (4 * 18), (int) buttonWidth, Utils.translateToLocal("gui.menu.config.player_skin"), ButtonType.BUTTON, (e) -> { window = ActualWindow.PLAYER; }));
		addButton(lockOnButton = new MenuButton((int) buttonPosX, (int) topBarHeight + 5 + (5 * 18), (int) buttonWidth, Utils.translateToLocal("gui.menu.config.lock_on_hp"), ButtonType.BUTTON, (e) -> { window = ActualWindow.LOCK_ON_HP; }));
		addButton(partyButton = new MenuButton((int) buttonPosX, (int) topBarHeight + 5 + (6 * 18), (int) buttonWidth, Utils.translateToLocal("gui.menu.config.party"), ButtonType.BUTTON, (e) -> { window = ActualWindow.PARTY; }));

		addButton(back = new MenuButton((int) buttonPosX, (int) topBarHeight + 5 + (7 * 18), (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Menu_Back), ButtonType.BUTTON, (e) -> { action("back"); }));
		addButton(backgroundButton = new MenuButton((int) width / 2 - (int)buttonWidth / 2, (int) topBarHeight + 5 + (7-2 * 18), (int) buttonWidth, Utils.translateToLocal("gui.menu.config.bg"), ButtonType.BUTTON, (e) -> { drawSeparately = !drawSeparately; }));
	}

	private void initCommandMenu() {
		cmHeaderTextVisible = ModConfigs.cmHeaderTextVisible;
		int pos = 0;

		addButton(cmXScaleBox = new TextFieldWidget(minecraft.fontRenderer, buttonsX, (int) (topBarHeight + 20 * ++pos), minecraft.fontRenderer.getStringWidth("#####"), 16, ("test")){
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
		
		addButton(cmXPosBox = new TextFieldWidget(minecraft.fontRenderer, buttonsX, (int) (topBarHeight + 20 * ++pos), minecraft.fontRenderer.getStringWidth("#####"), 16, ("test")){
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
		
		addButton(cmSubXOffsetBox = new TextFieldWidget(minecraft.fontRenderer, buttonsX, (int) (topBarHeight + 20 * ++pos), minecraft.fontRenderer.getStringWidth("#####"), 16, ("test")){
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
		
		addButton(cmHeaderTextVisibleButton = new Button(buttonsX - 1, (int) topBarHeight + 20 * ++pos - 2, minecraft.fontRenderer.getStringWidth("#####")+2, 20, (cmHeaderTextVisible+""), (e) -> { action("textHeaderVisibility"); }));
		addButton(cmTextXOffsetBox = new TextFieldWidget(minecraft.fontRenderer, buttonsX, (int) (topBarHeight + 20 * ++pos), minecraft.fontRenderer.getStringWidth("#####"), 16, ("test")){
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
		cmHeaderTextVisibleButton.setMessage((cmHeaderTextVisible+""));
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
		
		addButton(hpXPosBox = new TextFieldWidget(minecraft.fontRenderer, buttonsX, (int) (topBarHeight + 20 * ++pos), minecraft.fontRenderer.getStringWidth("#####"), 16, ("test")){
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
		
		addButton(hpYPosBox = new TextFieldWidget(minecraft.fontRenderer, buttonsX, (int) (topBarHeight + 20 * ++pos), minecraft.fontRenderer.getStringWidth("#####"), 16, ("test")){
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
		
		addButton(hpShowHeartsButton = new Button(buttonsX - 1, (int) topBarHeight + 20 * ++pos - 2, minecraft.fontRenderer.getStringWidth("#####")+2, 20, (hpShowHearts+""), (e) -> { action("hpShowHearts"); }));

		
		hpXPosBox.setText(""+ModConfigs.hpXPos);
		hpYPosBox.setText(""+ModConfigs.hpYPos);
		hpShowHeartsButton.setMessage((hpShowHearts+""));
		
		hpList.add(hpXPosBox);
		hpList.add(hpYPosBox);
		hpList.add(hpShowHeartsButton);

	}
	
	private void initMP() {
		int pos = 0;
		
		addButton(mpXPosBox = new TextFieldWidget(minecraft.fontRenderer, buttonsX, (int) (topBarHeight + 20 * ++pos), minecraft.fontRenderer.getStringWidth("#####"), 16, ("test")){
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
		
		addButton(mpYPosBox = new TextFieldWidget(minecraft.fontRenderer, buttonsX, (int) (topBarHeight + 20 * ++pos), minecraft.fontRenderer.getStringWidth("#####"), 16, ("test")){
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
	
	private void initDP() {
		int pos = 0;
		
		addButton(dpXPosBox = new TextFieldWidget(minecraft.fontRenderer, buttonsX, (int) (topBarHeight + 20 * ++pos), minecraft.fontRenderer.getStringWidth("#####"), 16, ("test")){
			@Override
			public boolean charTyped(char c, int i) {
				if (Utils.isNumber(c) || c == '-') {
					String text = new StringBuilder(this.getText()).insert(this.getCursorPosition(), c).toString();
					if (Utils.getInt(text) < 1000 && Utils.getInt(text) > -1000) {
						super.charTyped(c, i);
						ModConfigs.setDpXPos(Utils.getInt(getText()));
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
				ModConfigs.setDpXPos(Utils.getInt(getText()));
				return true;
			}
			
		});
		
		addButton(dpYPosBox = new TextFieldWidget(minecraft.fontRenderer, buttonsX, (int) (topBarHeight + 20 * ++pos), minecraft.fontRenderer.getStringWidth("#####"), 16, ("test")){
			@Override
			public boolean charTyped(char c, int i) {
				if (Utils.isNumber(c) || c == '-') {
					String text = new StringBuilder(this.getText()).insert(this.getCursorPosition(), c).toString();
					if (Utils.getInt(text) < 1000 && Utils.getInt(text) > -1000) {
						super.charTyped(c, i);
						ModConfigs.setDpYPos(Utils.getInt(getText()));
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
				ModConfigs.setDpYPos(Utils.getInt(getText()));
				return true;
			}
			
		});
		

		
		dpXPosBox.setText(""+ModConfigs.dpXPos);
		dpYPosBox.setText(""+ModConfigs.dpYPos);
		
		dpList.add(dpXPosBox);
		dpList.add(dpYPosBox);

	}
	
	private void initPlayerSkin() {
		int pos = 0;
		
		addButton(playerSkinXPosBox = new TextFieldWidget(minecraft.fontRenderer, buttonsX, (int) (topBarHeight + 20 * ++pos), minecraft.fontRenderer.getStringWidth("#####"), 16, ("test")){
			@Override
			public boolean charTyped(char c, int i) {
				if (Utils.isNumber(c) || c == '-') {
					String text = new StringBuilder(this.getText()).insert(this.getCursorPosition(), c).toString();
					if (Utils.getInt(text) < 1000 && Utils.getInt(text) > -1000) {
						super.charTyped(c, i);
						ModConfigs.setPlayerSkinXPos(Utils.getInt(getText()));
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
				ModConfigs.setPlayerSkinXPos(Utils.getInt(getText()));
				return true;
			}
			
		});
		
		addButton(playerSkinYPosBox = new TextFieldWidget(minecraft.fontRenderer, buttonsX, (int) (topBarHeight + 20 * ++pos), minecraft.fontRenderer.getStringWidth("#####"), 16, ("test")){
			@Override
			public boolean charTyped(char c, int i) {
				if (Utils.isNumber(c) || c == '-') {
					String text = new StringBuilder(this.getText()).insert(this.getCursorPosition(), c).toString();
					if (Utils.getInt(text) < 1000 && Utils.getInt(text) > -1000) {
						super.charTyped(c, i);
						ModConfigs.setPlayerSkinYPos(Utils.getInt(getText()));
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
				ModConfigs.setPlayerSkinYPos(Utils.getInt(getText()));
				return true;
			}
			
		});
		

		
		playerSkinXPosBox.setText(""+ModConfigs.playerSkinXPos);
		playerSkinYPosBox.setText(""+ModConfigs.playerSkinYPos);
		
		playerSkinList.add(playerSkinXPosBox);
		playerSkinList.add(playerSkinYPosBox);

	}
	
	private void initLockOn() {
		int pos = 0;
		
		addButton(lockOnXPosBox = new TextFieldWidget(minecraft.fontRenderer, buttonsX, (int) (topBarHeight + 20 * ++pos), minecraft.fontRenderer.getStringWidth("#####"), 16, ("test")){
			@Override
			public boolean charTyped(char c, int i) {
				if (Utils.isNumber(c) || c == '-') {
					String text = new StringBuilder(this.getText()).insert(this.getCursorPosition(), c).toString();
					if (Utils.getInt(text) < 1000 && Utils.getInt(text) > -1000) {
						super.charTyped(c, i);
						ModConfigs.setLockOnXPos(Utils.getInt(getText()));
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
				ModConfigs.setLockOnXPos(Utils.getInt(getText()));
				return true;
			}
			
		});
		
		addButton(lockOnYPosBox = new TextFieldWidget(minecraft.fontRenderer, buttonsX, (int) (topBarHeight + 20 * ++pos), minecraft.fontRenderer.getStringWidth("#####"), 16, ("test")){
			@Override
			public boolean charTyped(char c, int i) {
				if (Utils.isNumber(c) || c == '-') {
					String text = new StringBuilder(this.getText()).insert(this.getCursorPosition(), c).toString();
					if (Utils.getInt(text) < 1000 && Utils.getInt(text) > -1000) {
						super.charTyped(c, i);
						ModConfigs.setLockOnYPos(Utils.getInt(getText()));
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
				ModConfigs.setLockOnYPos(Utils.getInt(getText()));
				return true;
			}
			
		});
		
		addButton(lockOnHPScaleBox = new TextFieldWidget(minecraft.fontRenderer, buttonsX, (int) (topBarHeight + 20 * ++pos), minecraft.fontRenderer.getStringWidth("#####"), 16, ("test")){
			@Override
			public boolean charTyped(char c, int i) {
				if (Utils.isNumber(c) || c == '-') {
					String text = new StringBuilder(this.getText()).insert(this.getCursorPosition(), c).toString();
					if (Utils.getInt(text) < 1000 && Utils.getInt(text) > -1000) {
						super.charTyped(c, i);
						ModConfigs.setLockOnHPScale(Utils.getInt(getText()));
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
				ModConfigs.setLockOnHPScale(Utils.getInt(getText()));
				return true;
			}
			
		});
		
		addButton(lockOnIconScaleBox = new TextFieldWidget(minecraft.fontRenderer, buttonsX, (int) (topBarHeight + 20 * ++pos), minecraft.fontRenderer.getStringWidth("#####"), 16, ("test")){
			@Override
			public boolean charTyped(char c, int i) {
				if (Utils.isNumber(c) || c == '-') {
					String text = new StringBuilder(this.getText()).insert(this.getCursorPosition(), c).toString();
					if (Utils.getInt(text) < 1000 && Utils.getInt(text) > -1000) {
						super.charTyped(c, i);
						ModConfigs.setLockOnIconScale(Utils.getInt(getText()));
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
				ModConfigs.setLockOnIconScale(Utils.getInt(getText()));
				return true;
			}
			
		});
				
		lockOnXPosBox.setText(""+ModConfigs.lockOnXPos);
		lockOnYPosBox.setText(""+ModConfigs.lockOnYPos);
		lockOnHPScaleBox.setText(""+ModConfigs.lockOnHPScale);
		lockOnIconScaleBox.setText(""+ModConfigs.lockOnIconScale);

		lockOnList.add(lockOnXPosBox);
		lockOnList.add(lockOnYPosBox);
		lockOnList.add(lockOnHPScaleBox);
		lockOnList.add(lockOnIconScaleBox);

	}
	
	private void initParty() {
		int pos = 0;
		
		addButton(partyXPosBox = new TextFieldWidget(minecraft.fontRenderer, buttonsX, (int) (topBarHeight + 20 * ++pos), minecraft.fontRenderer.getStringWidth("#####"), 16, ("test")){
			@Override
			public boolean charTyped(char c, int i) {
				if (Utils.isNumber(c) || c == '-') {
					String text = new StringBuilder(this.getText()).insert(this.getCursorPosition(), c).toString();
					if (Utils.getInt(text) < 1000 && Utils.getInt(text) > -1000) {
						super.charTyped(c, i);
						ModConfigs.setPartyXPos(Utils.getInt(getText()));
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
				ModConfigs.setPartyXPos(Utils.getInt(getText()));
				return true;
			}
			
		});
		
		addButton(partyYPosBox = new TextFieldWidget(minecraft.fontRenderer, buttonsX, (int) (topBarHeight + 20 * ++pos), minecraft.fontRenderer.getStringWidth("#####"), 16, ("test")){
			@Override
			public boolean charTyped(char c, int i) {
				if (Utils.isNumber(c) || c == '-') {
					String text = new StringBuilder(this.getText()).insert(this.getCursorPosition(), c).toString();
					if (Utils.getInt(text) < 1000 && Utils.getInt(text) > -1000) {
						super.charTyped(c, i);
						ModConfigs.setPartyYPos(Utils.getInt(getText()));
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
				ModConfigs.setPartyYPos(Utils.getInt(getText()));
				return true;
			}
			
		});
		
		addButton(partyYOffsetBox = new TextFieldWidget(minecraft.fontRenderer, buttonsX, (int) (topBarHeight + 20 * ++pos), minecraft.fontRenderer.getStringWidth("#####"), 16, ("test")){
			@Override
			public boolean charTyped(char c, int i) {
				if (Utils.isNumber(c) || c == '-') {
					String text = new StringBuilder(this.getText()).insert(this.getCursorPosition(), c).toString();
					if (Utils.getInt(text) < 1000 && Utils.getInt(text) > -1000) {
						super.charTyped(c, i);
						ModConfigs.setPartyYOffset(Utils.getInt(getText()));
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
				ModConfigs.setPartyYOffset(Utils.getInt(getText()));
				return true;
			}
			
		});
		
		partyXPosBox.setText(""+ModConfigs.partyXPos);
		partyYPosBox.setText(""+ModConfigs.partyYPos);
		partyYOffsetBox.setText(""+ModConfigs.partyYOffset);
		
		partyList.add(partyXPosBox);
		partyList.add(partyYPosBox);
		partyList.add(partyYOffsetBox);

	}
	
	
	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		commandMenuButton.active = window != ActualWindow.COMMAND_MENU;
		hpButton.active = window != ActualWindow.HP;
		mpButton.active = window != ActualWindow.MP;
		dpButton.active = window != ActualWindow.DRIVE;
		playerSkinButton.active = window != ActualWindow.PLAYER;
		lockOnButton.active = window != ActualWindow.LOCK_ON_HP;
		partyButton.active = window != ActualWindow.PARTY;
		
		box.draw();
		super.render(mouseX, mouseY, partialTicks);

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
		
		for(Widget b : dpList) {
			b.active = false;
			b.visible = false;
		}
		
		for(Widget b : playerSkinList) {
			b.active = false;
			b.visible = false;
		}

		for(Widget b : lockOnList) {
			b.active = false;
			b.visible = false;
		}
		
		for(Widget b : partyList) {
			b.active = false;
			b.visible = false;
		}

		switch(window) {
		case COMMAND_MENU:
			for(Widget b : commandMenuList) {
				b.active = true;
				b.visible = true;
			}
			
			RenderSystem.pushMatrix();
			{
				int pos = 0;
				RenderSystem.translatef(buttonsX, box.y+4, 1);
				drawString( minecraft.fontRenderer, Utils.translateToLocal("gui.menu.config.command_menu"), 20, 0, 0xFF9900);
				drawString( minecraft.fontRenderer, Utils.translateToLocal("gui.menu.config.x_scale"), 40, 20 * ++pos, 0xFF9900);
				drawString( minecraft.fontRenderer, Utils.translateToLocal("gui.menu.config.x_pos"), 40, 20 * ++pos, 0xFF9900);
				drawString( minecraft.fontRenderer, Utils.translateToLocal("gui.menu.config.sub_x_offset"), 40, 20 * ++pos, 0xFF9900);
				drawString( minecraft.fontRenderer, Utils.translateToLocal("gui.menu.config.header_title"), 40, 20 * ++pos, 0xFF9900);
				drawString( minecraft.fontRenderer, Utils.translateToLocal("gui.menu.config.text_x_offset"), 40, 20 * ++pos, 0xFF9900);
			}
			RenderSystem.popMatrix();
			
			break;
			
		case HP:
			for(Widget b : hpList) {
				b.active = true;
				b.visible = true;
			}
			
			RenderSystem.pushMatrix();
			{
				int pos = 0;
				RenderSystem.translatef(buttonsX, box.y+4, 1);
				drawString( minecraft.fontRenderer, Utils.translateToLocal("gui.menu.config.hp"), 20, 0, 0xFF9900);
				drawString( minecraft.fontRenderer, Utils.translateToLocal("gui.menu.config.x_pos"), 40, 20 * ++pos, 0xFF9900);
				drawString( minecraft.fontRenderer, Utils.translateToLocal("gui.menu.config.y_pos"), 40, 20 * ++pos, 0xFF9900);
				drawString( minecraft.fontRenderer, Utils.translateToLocal("gui.menu.config.show_hearts"), 40, 20 * ++pos, 0xFF9900);
			}
			RenderSystem.popMatrix();
			
			break;
			
		case MP:
			for(Widget b : mpList) {
				b.active = true;
				b.visible = true;
			}
			
			RenderSystem.pushMatrix();
			{
				int pos = 0;
				RenderSystem.translatef(buttonsX, box.y+4, 1);
				drawString( minecraft.fontRenderer, Utils.translateToLocal("gui.menu.config.mp"), 20, 0, 0xFF9900);
				drawString( minecraft.fontRenderer, Utils.translateToLocal("gui.menu.config.x_pos"), 40, 20 * ++pos, 0xFF9900);
				drawString( minecraft.fontRenderer, Utils.translateToLocal("gui.menu.config.y_pos"), 40, 20 * ++pos, 0xFF9900);
			}
			RenderSystem.popMatrix();
			
			break;
			
		case DRIVE:
			for(Widget b : dpList) {
				b.active = true;
				b.visible = true;
			}
			
			RenderSystem.pushMatrix();
			{
				int pos = 0;
				RenderSystem.translatef(buttonsX, box.y+4, 1);
				drawString( minecraft.fontRenderer, Utils.translateToLocal("gui.menu.config.dp"), 20, 0, 0xFF9900);
				drawString( minecraft.fontRenderer, Utils.translateToLocal("gui.menu.config.x_pos"), 40, 20 * ++pos, 0xFF9900);
				drawString( minecraft.fontRenderer, Utils.translateToLocal("gui.menu.config.y_pos"), 40, 20 * ++pos, 0xFF9900);
			}
			RenderSystem.popMatrix();
			
			break;
			
		case PLAYER:
			for(Widget b : playerSkinList) {
				b.active = true;
				b.visible = true;
			}
			
			RenderSystem.pushMatrix();
			{
				int pos = 0;
				RenderSystem.translatef(buttonsX, box.y+4, 1);
				drawString( minecraft.fontRenderer, Utils.translateToLocal("gui.menu.config.player_skin"), 20, 0, 0xFF9900);
				drawString( minecraft.fontRenderer, Utils.translateToLocal("gui.menu.config.x_pos"), 40, 20 * ++pos, 0xFF9900);
				drawString( minecraft.fontRenderer, Utils.translateToLocal("gui.menu.config.y_pos"), 40, 20 * ++pos, 0xFF9900);
			}
			RenderSystem.popMatrix();
			
			break;
			
		case LOCK_ON_HP:
			for(Widget b : lockOnList) {
				b.active = true;
				b.visible = true;
			}
			
			RenderSystem.pushMatrix();
			{
				int pos = 0;
				RenderSystem.translatef(buttonsX, box.y+4, 1);
				drawString(minecraft.fontRenderer, Utils.translateToLocal("gui.menu.config.lock_on_hp"), 20, 0, 0xFF9900);
				drawString(minecraft.fontRenderer, Utils.translateToLocal("gui.menu.config.x_pos"), 40, 20 * ++pos, 0xFF9900);
				drawString(minecraft.fontRenderer, Utils.translateToLocal("gui.menu.config.y_pos"), 40, 20 * ++pos, 0xFF9900);
			}
			RenderSystem.popMatrix();
			
			break;
			
		case PARTY:
			for(Widget b : partyList) {
				b.active = true;
				b.visible = true;
			}
			
			RenderSystem.pushMatrix();
			{
				int pos = 0;
				RenderSystem.translatef(buttonsX, box.y+4, 1);
				drawString(minecraft.fontRenderer, Utils.translateToLocal("gui.menu.config.party"), 20, 0, 0xFF9900);
				drawString(minecraft.fontRenderer, Utils.translateToLocal("gui.menu.config.x_pos"), 40, 20 * ++pos, 0xFF9900);
				drawString(minecraft.fontRenderer, Utils.translateToLocal("gui.menu.config.y_pos"), 40, 20 * ++pos, 0xFF9900);
				drawString(minecraft.fontRenderer, Utils.translateToLocal("gui.menu.config.y_offset"), 40, 20 * ++pos, 0xFF9900);
			}
			RenderSystem.popMatrix();
			
			break;
		
		}
	}	

}
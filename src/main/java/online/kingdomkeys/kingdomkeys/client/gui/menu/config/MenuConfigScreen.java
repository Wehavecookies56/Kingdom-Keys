package online.kingdomkeys.kingdomkeys.client.gui.menu.config;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.client.gui.widget.ExtendedSlider;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.client.gui.elements.ColorPickerWidget;
import online.kingdomkeys.kingdomkeys.client.gui.elements.CrownPositionWidget;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBox;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.EditBoxLength;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton.ButtonType;
import online.kingdomkeys.kingdomkeys.client.gui.overlay.HUDEditorScreen;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSOpenMenu;
import online.kingdomkeys.kingdomkeys.network.cts.CSSetCrownOffset;
import online.kingdomkeys.kingdomkeys.network.cts.CSSetNotifColor;
import online.kingdomkeys.kingdomkeys.network.cts.CSSyncArmorColor;
import online.kingdomkeys.kingdomkeys.util.Utils;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuConfigScreen extends MenuBackground {
			
	enum ActualWindow {
		PLAYER, FONT, COMMAND_MENU, HP, LOCK_ON_HP, PARTY, IMPORT_EXPORT
	}

	ActualWindow window = ActualWindow.PLAYER;
	
	MenuButton back, fontButton, commandMenuButton, hpButton, playerSkinButton, lockOnButton, partyButton, impExButton;
	Button backgroundButton, adjustHUDButton;
	MenuBox box;

	//PlayerSkin
	Button glintButton;
	boolean glint;

	//Font
	Button customFontButton;
	boolean customFont = true;

	//Command Menu
	EditBox cmTextXOffsetBox, cmSelectedXOffsetBox, cmSubXOffsetBox;
	Button cmHeaderTextVisibleButton, cmClassicColorsButton;
	boolean cmHeaderTextVisible, cmClassicColors;
	
	//HP
	EditBox hpAlarmBox;
	Button hpShowHeartsButton;
	boolean hpShowHearts;

	//Lock On
	EditBox lockOnIconScaleBox, lockOnIconRotationBox, lockOnHpPerBarBox;

	//Party
	EditBox partyYDistanceBox;

	//Import Export
	Button export, Import;
	EditBoxLength importCode;

	List<AbstractWidget> fontList = new ArrayList<>();
	List<AbstractWidget> commandMenuList = new ArrayList<>();
	List<AbstractWidget> hpList = new ArrayList<>();
	List<AbstractWidget> playerSkinList = new ArrayList<>();
	CrownPositionWidget crownPosition;
	ExtendedSlider crownRotX, crownRotY, crownRotZ;
	ColorPickerWidget notifColorPicker, armorColorPicker;
	List<AbstractWidget> lockOnList = new ArrayList<>();
	List<AbstractWidget> partyList = new ArrayList<>();
	List<AbstractWidget> impExpList = new ArrayList<>();

	int buttonsX = 0;
	public MenuConfigScreen() {
		super(Strings.Gui_Menu_Config, new Color(0,0,255));
		drawPlayerInfo = false;
	}
	
	protected void action(String string) {
		PlayerData playerData = PlayerData.get(minecraft.player);
		switch(string) {
		case "back":
			PacketHandler.sendToServer(new CSOpenMenu());
			break;
		case "customFont":
			customFont = !customFont;
			customFontButton.setMessage(Component.translatable(customFont+""));
			ModConfigs.setCustomFont(customFont);
			break;
		case "textHeaderVisibility":
			cmHeaderTextVisible = !cmHeaderTextVisible;
			cmHeaderTextVisibleButton.setMessage(Component.translatable(cmHeaderTextVisible+""));
			ModConfigs.setCmHeaderTextVisible(cmHeaderTextVisible);
			break;
		case "classicColors":
			cmClassicColors = !cmClassicColors;
			cmClassicColorsButton.setMessage(Component.translatable(cmClassicColors+""));
			ModConfigs.setCmClassicColors(cmClassicColors);
			break;
		case "hpShowHearts":
			hpShowHearts = !hpShowHearts;
			hpShowHeartsButton.setMessage(Component.translatable(hpShowHearts+""));
			ModConfigs.setShowHearts(hpShowHearts);
			break;
		case "glint":
			glint = !glint;
			glintButton.setMessage(Component.translatable(glint+""));
			PacketHandler.sendToServer(new CSSyncArmorColor(playerData.getArmorColor(), glint));
			break;
		}
		
	}

	@Override
	public void init() {
		float boxPosX = (float) width * 0.25F;
		float topBarHeight = (float) height * 0.17F;
		float boxWidth = (float) width * 0.67F;
		float middleHeight = (float) height * 0.6F;

		int scaledWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
		
		box = new MenuBox((int) boxPosX, (int) topBarHeight, (int) boxWidth, (int) middleHeight, 0.6F,new Color(235, 168, 52));
		buttonsX = box.getX() + 10;
		
		super.init();
		this.renderables.clear();

		initFont();
		initCommandMenu();
		initHP();
		initPlayerSkin();
		initLockOn();
		initParty();
		initImpExp();
		int y = 0;
		addRenderableWidget(playerSkinButton = new MenuButton((int) buttonPosX, (int) topBarHeight + 5 + y++ * 18, (int) buttonWidth, Utils.translateToLocal("gui.menu.config.player_skin"), ButtonType.BUTTON, (e) -> { window = ActualWindow.PLAYER; }));
		addRenderableWidget(fontButton = new MenuButton((int) buttonPosX, (int) topBarHeight + 5 + y++ * 18, (int) buttonWidth, Utils.translateToLocal("gui.menu.config.font"), ButtonType.BUTTON, (e) -> { window = ActualWindow.FONT; }));
		addRenderableWidget(commandMenuButton = new MenuButton((int) buttonPosX, (int) topBarHeight + 5 + y++ * 18, (int) buttonWidth, Utils.translateToLocal("gui.menu.config.command_menu"), ButtonType.BUTTON, (e) -> { window = ActualWindow.COMMAND_MENU; }));
		addRenderableWidget(hpButton = new MenuButton((int) buttonPosX, (int) topBarHeight + 5 + + y++ * 18, (int) buttonWidth, Utils.translateToLocal("gui.menu.config.hp"), ButtonType.BUTTON, (e) -> { window = ActualWindow.HP; }));
		addRenderableWidget(lockOnButton = new MenuButton((int) buttonPosX, (int) topBarHeight + 5 + y++ * 18, (int) buttonWidth, Utils.translateToLocal("gui.menu.config.lock_on_hp"), ButtonType.BUTTON, (e) -> { window = ActualWindow.LOCK_ON_HP; }));
		addRenderableWidget(partyButton = new MenuButton((int) buttonPosX, (int) topBarHeight + 5 + y++ * 18, (int) buttonWidth, Utils.translateToLocal("gui.menu.config.party"), ButtonType.BUTTON, (e) -> { window = ActualWindow.PARTY; }));
		addRenderableWidget(impExButton = new MenuButton((int) buttonPosX, (int) topBarHeight + 5 + y++ * 18, (int) buttonWidth, Utils.translateToLocal("gui.menu.config.impexp"), ButtonType.BUTTON, (e) -> window = ActualWindow.IMPORT_EXPORT));

		addRenderableWidget(back = new MenuButton((int) buttonPosX, (int) topBarHeight + 5 + y++ * 18, (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Menu_Back), ButtonType.BUTTON, (e) -> { PacketHandler.sendToServer(new CSSyncArmorColor(PlayerData.get(minecraft.player).getArmorColor(),glint)); action("back"); }));
		addRenderableWidget(backgroundButton = new MenuButton((int)(scaledWidth/2F - buttonWidth - 20), (int) topBarHeight - 30, (int)buttonWidth, Utils.translateToLocal("gui.menu.config.bg"), ButtonType.ROUNDBUTTON, (e) -> { drawSeparately = !drawSeparately; }));
		addRenderableWidget(adjustHUDButton = new MenuButton(scaledWidth/2 + 10, (int) topBarHeight - 30, (int)buttonWidth, Utils.translateToLocal("gui.menu.config.hud"), ButtonType.ROUNDBUTTON, (e) -> { minecraft.setScreen(new HUDEditorScreen()); }));
	}

	private void initFont(){
		customFont = ModConfigs.customFont;

		int pos = 0;
		addRenderableWidget(customFontButton = Button.builder(Component.translatable(customFont+""), (e) -> {
			action("customFont");
		}).bounds(buttonsX - 1, (int) topBarHeight + 20 * ++pos - 2, minecraft.font.width("#####")+2, 20).build());

		customFontButton.setMessage(Component.translatable(customFont+""));
		fontList.add(customFontButton);
	}

	private void initCommandMenu() {
		cmHeaderTextVisible = ModConfigs.cmHeaderTextVisible;
		cmClassicColors = ModConfigs.cmClassicColors;
		int pos = 0;

		addRenderableWidget(cmClassicColorsButton = Button.builder(Component.translatable(cmClassicColors+""), (e) -> {
			 action("classicColors");
		}).bounds(buttonsX - 1, (int) topBarHeight + 20 * ++pos - 2, minecraft.font.width("#####")+2, 20).build());
		
		addRenderableWidget(cmSelectedXOffsetBox = new EditBox(minecraft.font, buttonsX, (int) (topBarHeight + 20 * ++pos), minecraft.font.width("#####"), 16, Component.translatable("test")){
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
		
		addRenderableWidget(cmSubXOffsetBox = new EditBox(minecraft.font, buttonsX, (int) (topBarHeight + 20 * ++pos), minecraft.font.width("#####"), 16, Component.translatable("test")){
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
		
		addRenderableWidget(cmHeaderTextVisibleButton = Button.builder(Component.translatable(cmHeaderTextVisible+""), (e) -> {
			 action("textHeaderVisibility");
		}).bounds(buttonsX - 1, (int) topBarHeight + 20 * ++pos - 2, minecraft.font.width("#####")+2, 20).build());
		

		addRenderableWidget(cmTextXOffsetBox = new EditBox(minecraft.font, buttonsX, (int) (topBarHeight + 20 * ++pos), minecraft.font.width("#####"), 16, Component.translatable("test")){
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
		cmHeaderTextVisibleButton.setMessage(Component.translatable(cmHeaderTextVisible+""));
		cmClassicColorsButton.setMessage(Component.translatable(cmClassicColors+""));

		cmSelectedXOffsetBox.setValue(""+ModConfigs.cmSelectedXOffset);
		cmSubXOffsetBox.setValue(""+ModConfigs.cmSubXOffset);
		
		commandMenuList.add(cmHeaderTextVisibleButton);
		commandMenuList.add(cmClassicColorsButton);
		commandMenuList.add(cmTextXOffsetBox);
		commandMenuList.add(cmTextXOffsetBox);
		commandMenuList.add(cmHeaderTextVisibleButton);
		commandMenuList.add(cmSelectedXOffsetBox);
		commandMenuList.add(cmSubXOffsetBox);
	}

	private void initHP() {
		hpShowHearts = ModConfigs.hpShowHearts;
		int pos = 0;
		
		addRenderableWidget(hpShowHeartsButton = Button.builder(Component.translatable(hpShowHearts+""), (e) -> {
			 action("hpShowHearts");
		}).bounds(buttonsX - 1, (int) topBarHeight + 20 * ++pos - 2, minecraft.font.width("#####")+2, 20).build());
		
		addRenderableWidget(hpAlarmBox = new EditBox(minecraft.font, buttonsX, (int) (topBarHeight + 20 * ++pos), minecraft.font.width("#####"), 16, Component.translatable("")){
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

		hpShowHeartsButton.setMessage(Component.translatable(hpShowHearts+""));
		hpAlarmBox.setValue(""+ModConfigs.hpAlarm);


		hpList.add(hpShowHeartsButton);
		hpList.add(hpAlarmBox);
	}
	
	private void initPlayerSkin() {
		glint = PlayerData.get(minecraft.player).getArmorGlint();

		int pos = 0;
		PlayerData playerData = PlayerData.get(minecraft.player);

		// Notification colour: SB square + hue strip. Applies live, syncs on release.
		addRenderableWidget(notifColorPicker = new ColorPickerWidget(buttonsX, (int) (topBarHeight + 40 * ++pos), 80, 44, playerData::getNotifColor, playerData::setNotifColor, () -> PacketHandler.sendToServer(new CSSetNotifColor(playerData.getNotifColor()))));
		pos += 3; // the picker is taller than one row

		addRenderableWidget(crownPosition = new CrownPositionWidget(box.getX() + box.getWidth() - 155, notifColorPicker.getY() - 30, 48));
		; // the square is taller than one row

		// Three axes: X pitches it forward/back, Y spins it, Z rolls it.
		addRenderableWidget(crownRotX = new ExtendedSlider(crownPosition.getX() + crownPosition.getWidth() + 2, crownPosition.getY(), 100, 16,
				Component.literal("X: "), Component.literal("\u00B0"), -180, 180, playerData.getCrownRotationX(), 1, 0, true) {
			@Override
			protected void onDrag(double mouseX, double mouseY, double dragX, double dragY) {
				super.onDrag(mouseX, mouseY, dragX, dragY);
				applyCrownRotation();
			}

			@Override
			public void onRelease(double pMouseX, double pMouseY) {
				super.onRelease(pMouseX, pMouseY);
				sendCrownPacket();
			}
		});

		addRenderableWidget(crownRotY = new ExtendedSlider(crownPosition.getX() + crownPosition.getWidth() + 2, crownPosition.getY()+16, 100, 16,
				Component.literal("Y: "), Component.literal("\u00B0"), -180, 180, playerData.getCrownRotationY(), 1, 0, true) {
			@Override
			protected void onDrag(double mouseX, double mouseY, double dragX, double dragY) {
				super.onDrag(mouseX, mouseY, dragX, dragY);
				applyCrownRotation();
			}

			@Override
			public void onRelease(double pMouseX, double pMouseY) {
				super.onRelease(pMouseX, pMouseY);
				sendCrownPacket();
			}
		});

		addRenderableWidget(crownRotZ = new ExtendedSlider(crownPosition.getX() + crownPosition.getWidth() + 2, crownPosition.getY()+32, 100, 16,
				Component.literal("Z: "), Component.literal("\u00B0"), -180, 180, playerData.getCrownRotationZ(), 1, 0, true) {
			@Override
			protected void onDrag(double mouseX, double mouseY, double dragX, double dragY) {
				super.onDrag(mouseX, mouseY, dragX, dragY);
				applyCrownRotation();
			}

			@Override
			public void onRelease(double pMouseX, double pMouseY) {
				super.onRelease(pMouseX, pMouseY);
				sendCrownPacket();
			}
		});

		// Armour colour.
		addRenderableWidget(armorColorPicker = new ColorPickerWidget(buttonsX, (int) (topBarHeight + 20 * ++pos), 80, 44, playerData::getArmorColor, playerData::setArmorColor, () -> PacketHandler.sendToServer(new CSSyncArmorColor(playerData.getArmorColor(), glint))));
		pos += 3; // the picker is taller than one row

		addRenderableWidget(glintButton = Button.builder(Component.translatable(glint+""), (e) -> {
			 action("glint");
		}).bounds(buttonsX + 85, (int) topBarHeight + 20 * 7 - 2, minecraft.font.width("#####")+2, 20).build());

		playerSkinList.add(armorColorPicker);
		playerSkinList.add(glintButton);
		playerSkinList.add(notifColorPicker);
		playerSkinList.add(crownPosition);
		playerSkinList.add(crownRotX);
		playerSkinList.add(crownRotY);
		playerSkinList.add(crownRotZ);
	}
		
	@Override
    public boolean keyPressed(int p_keyPressed_1_, int p_keyPressed_2_, int p_keyPressed_3_) {
        if (p_keyPressed_1_ == 256 || p_keyPressed_1_ == Minecraft.getInstance().options.keyInventory.getKey().getValue()) { //256 = Esc
    		PlayerData playerData = PlayerData.get(minecraft.player);
			// The colour pickers write straight into PlayerData, so there is nothing to read back
			// off widgets here - just push whatever is currently set.
			PacketHandler.sendToServer(new CSSyncArmorColor(playerData.getArmorColor(),glint));
			PacketHandler.sendToServer(new CSSetNotifColor(playerData.getNotifColor()));
        }
        return super.keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_);
    }

	
	/** Applies the three rotation sliders locally, so the crown updates while dragging. */
	private void applyCrownRotation() {
		PlayerData.get(minecraft.player).setCrownRotation((float) crownRotX.getValue(), (float) crownRotY.getValue(), (float) crownRotZ.getValue());
	}

	/** Sends position and all three rotations in one packet, on release. */
	private void sendCrownPacket() {
		PlayerData pd = PlayerData.get(minecraft.player);
		PacketHandler.sendToServer(new CSSetCrownOffset(pd.getCrownOffsetX(), pd.getCrownOffsetZ(), pd.getCrownRotationX(), pd.getCrownRotationY(), pd.getCrownRotationZ()));
	}

	private void initLockOn() {
		int pos = 0;

		addRenderableWidget(lockOnIconScaleBox = new EditBox(minecraft.font, buttonsX, (int) (topBarHeight + 20 * ++pos), minecraft.font.width("#####"), 16, Component.translatable("test")){
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
		

		addRenderableWidget(lockOnIconRotationBox = new EditBox(minecraft.font, buttonsX, (int) (topBarHeight + 20 * ++pos), minecraft.font.width("#####"), 16, Component.translatable("test")){
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
		
		addRenderableWidget(lockOnHpPerBarBox = new EditBox(minecraft.font, buttonsX, (int) (topBarHeight + 20 * ++pos), minecraft.font.width("#####"), 16, Component.translatable("test")){
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
		

		lockOnIconScaleBox.setValue(""+ModConfigs.lockOnIconScale);
		lockOnIconRotationBox.setValue(""+ModConfigs.lockOnIconRotation);
		lockOnHpPerBarBox.setValue(""+ModConfigs.lockOnHpPerBar);
		
		lockOnList.add(lockOnIconScaleBox);
		lockOnList.add(lockOnIconRotationBox);
		lockOnList.add(lockOnHpPerBarBox);
	}
	
	private void initParty() {
		int pos = 0;

		addRenderableWidget(partyYDistanceBox = new EditBox(minecraft.font, buttonsX, (int) (topBarHeight + 20 * ++pos), minecraft.font.width("#####"), 16, Component.translatable("test")){
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

		partyYDistanceBox.setValue(""+ModConfigs.partyYDistance);
		partyList.add(partyYDistanceBox);
	}

	private void initImpExp() {
		int pos = 0;

		addRenderableWidget(importCode = new EditBoxLength(minecraft.font, buttonsX, (int) (topBarHeight + 20 * ++pos), minecraft.font.width("##############################"), 16, 100, Component.translatable("test")){
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

		addRenderableWidget(Import = Button.builder(Component.translatable("gui.menu.config.impexp.import"), (e) -> {
			readImportCode(importCode.getValue());
		}).bounds(buttonsX - 1, (int) topBarHeight + 20 * ++pos - 2, minecraft.font.width("#######")+2, 20).build());

		addRenderableWidget(export = Button.builder(Component.translatable("gui.menu.config.impexp.export"), (e) -> {
			Minecraft.getInstance().keyboardHandler.setClipboard(exportCode());
		}).bounds(buttonsX - 1, (int) topBarHeight + 20 * ++pos - 2, minecraft.font.width("###################")+2, 20).build());
		
		impExpList.add(importCode);
		impExpList.add(Import);
		impExpList.add(export);
	}

	@Override
	public void render(@NotNull GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
		PoseStack matrixStack = gui.pose();
		fontButton.active = window != ActualWindow.FONT;
		commandMenuButton.active = window != ActualWindow.COMMAND_MENU;
		hpButton.active = window != ActualWindow.HP;
		playerSkinButton.active = window != ActualWindow.PLAYER;
		lockOnButton.active = window != ActualWindow.LOCK_ON_HP;
		partyButton.active = window != ActualWindow.PARTY;
		impExButton.active = window != ActualWindow.IMPORT_EXPORT;
		
		box.renderWidget(gui, mouseX, mouseY, partialTicks);
		super.render(gui, mouseX, mouseY, partialTicks);

		for(AbstractWidget b : fontList) {
			b.active = false;
			b.visible = false;
		}

		for(AbstractWidget b : commandMenuList) {
			b.active = false;
			b.visible = false;
		}
		
		for(AbstractWidget b : hpList) {
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

		for (AbstractWidget b : impExpList) {			
			b.active = false;
			b.visible = false;
		}

		matrixStack.pushPose();
		{
			int pos = 0;
			matrixStack.translate(buttonsX, box.getY() + 4, 1);
			
			switch (window) {
				case FONT -> {
					for (AbstractWidget b : fontList) {
						b.active = true;
						b.visible = true;
					}

					gui.drawString(minecraft.font, Utils.translateToLocal("gui.menu.config.font"), 20, 0, 0xFF9900);
					gui.drawString(minecraft.font, Utils.translateToLocal("gui.menu.config.custom_font"), 40, 20 * ++pos, 0xFF9900);
				}
				case COMMAND_MENU -> {
					for (AbstractWidget b : commandMenuList) {
						b.active = true;
						b.visible = true;
					}

					gui.drawString(minecraft.font, Utils.translateToLocal("gui.menu.config.command_menu"), 20, 0, 0xFF9900);
					gui.drawString(minecraft.font, Utils.translateToLocal("gui.menu.config.classic_colors"), 40, 20 * ++pos, 0xFF9900);
					gui.drawString(minecraft.font, Utils.translateToLocal("gui.menu.config.selected_x_pos"), 40, 20 * ++pos, 0xFF9900);
					gui.drawString(minecraft.font, Utils.translateToLocal("gui.menu.config.sub_x_offset"), 40, 20 * ++pos, 0xFF9900);
					gui.drawString(minecraft.font, Utils.translateToLocal("gui.menu.config.header_title"), 40, 20 * ++pos, 0xFF9900);
					gui.drawString(minecraft.font, Utils.translateToLocal("gui.menu.config.text_x_offset"), 40, 20 * ++pos, 0xFF9900);
				}
				case HP -> {
					for (AbstractWidget b : hpList) {
						b.active = true;
						b.visible = true;
					}

					gui.drawString(minecraft.font, Utils.translateToLocal("gui.menu.config.hp"), 20, 0, 0xFF9900);
					gui.drawString(minecraft.font, Utils.translateToLocal("gui.menu.config.show_hearts"), 40, 20 * ++pos, 0xFF9900);
					gui.drawString(minecraft.font, Utils.translateToLocal("gui.menu.config.hp_alarm"), 40, 20 * ++pos, 0xFF9900);
				}
				case PLAYER -> {
					for (AbstractWidget b : playerSkinList) {
						b.active = true;
						b.visible = true;
					}

					Player player = Minecraft.getInstance().player;

					matrixStack.pushPose();
						{
						matrixStack.translate(-(width) + box.getX() + 20, 0, 0);
						RenderSystem.enableBlend();
						int notif = PlayerData.get(minecraft.player).getNotifColor();
						RenderSystem.setShaderColor(((notif >> 16) & 0xFF) / 255F, ((notif >> 8) & 0xFF) / 255F, (notif & 0xFF) / 255F, 1F);
						ResourceLocation levelUpTexture = KingdomKeys.rl("textures/gui/levelup.png");

						// Top
						matrixStack.pushPose();
						{
							matrixStack.translate((width - 153.6f - 2), 0, 0);
							matrixStack.scale(0.6f, 0.6f, 1);
							gui.blit(levelUpTexture, 0, 0, 0, 0, 256, 36);
						}
						matrixStack.popPose();

						// Half
						matrixStack.pushPose();
						{
							matrixStack.translate((width - 256.0f * 0.6f - 2), 36.0f * 0.6f, 0);
							matrixStack.scale(0.6f, 0, 1);
							gui.blit(levelUpTexture, 0, 0, 0, 36, 256, 1);
						}
						matrixStack.popPose();

						// Bottom
						matrixStack.pushPose();
						{
							matrixStack.translate((width - 256.0f * 0.6f - 2), 0 + (36.0f * 0.6f), 0);
							matrixStack.scale(0.6f, 0.6f, 1);
							gui.blit(levelUpTexture, 0, 0, 0, 37, 256, 14);
						}
						matrixStack.popPose();
						RenderSystem.disableBlend();
					}
					matrixStack.popPose();
					RenderSystem.setShaderColor(1,1,1,1F);

					ClientUtils.renderEntity(matrixStack, (int) (width*0.5F), (int) (height*0.55F), 40, 0, 0, player);
					ClientUtils.renderPlayerNoAnimsRaw(matrixStack, crownPosition.getX() - crownPosition.getWidth() - 65, (int) (height*0.25F), 55, 0, (float)Math.toRadians(-270), player);


					//gui.drawString(minecraft.font, Utils.translateToLocal("gui.menu.config.player_skin"), 20, 0, 0xFF9900);
					gui.drawString(minecraft.font, Utils.translateToLocal("gui.menu.config.notif_color"), 10, 18 * ++pos, 0xFF9900);
					gui.drawString(minecraft.font, Utils.translateToLocal("gui.menu.config.armor_color"), armorColorPicker.getX() - armorColorPicker.getWidth() + 30, armorColorPicker.getY() - armorColorPicker.getHeight(), 0xFF9900);
					gui.drawString(minecraft.font, Utils.translateToLocal("gui.menu.config.armor.glint"), armorColorPicker.getX() - armorColorPicker.getWidth() + 30, armorColorPicker.getY() - armorColorPicker.getHeight() + 20, 0xFF9900);
				}
				case LOCK_ON_HP -> {
					for (AbstractWidget b : lockOnList) {
						b.active = true;
						b.visible = true;
					}

					gui.drawString(minecraft.font, Utils.translateToLocal("gui.menu.config.lock_on_hp"), 20, 0, 0xFF9900);
					gui.drawString(minecraft.font, Utils.translateToLocal("gui.menu.config.icon_scale"), 40, 20 * ++pos, 0xFF9900);
					gui.drawString(minecraft.font, Utils.translateToLocal("gui.menu.config.icon_rotation"), 40, 20 * ++pos, 0xFF9900);
					gui.drawString(minecraft.font, Utils.translateToLocal("gui.menu.config.hp_per_bar"), 40, 20 * ++pos, 0xFF9900);

				}
				case PARTY -> {
					for (AbstractWidget b : partyList) {
						b.active = true;
						b.visible = true;
					}

					gui.drawString(minecraft.font, Utils.translateToLocal("gui.menu.config.party"), 20, 0, 0xFF9900);
					gui.drawString(minecraft.font, Utils.translateToLocal("gui.menu.config.y_dist"), 40, 20 * ++pos, 0xFF9900);
				}
				case IMPORT_EXPORT -> {
					for (AbstractWidget b : impExpList) {
						if (b == Import) {
							b.active = !importCode.getValue().equals("");
							b.visible = true;
						} else {
							b.active = true;
							b.visible = true;
						}
					}
				}
			}

		}
		matrixStack.popPose();

	}

	public Map<Character, Integer> getOptionsMap() {
		Map<Character, Integer> options = new HashMap<>();
		//A B
		options.put('C', Integer.valueOf(cmSelectedXOffsetBox.getValue()));
		options.put('D', Integer.valueOf(cmSubXOffsetBox.getValue()));
		options.put('E', cmHeaderTextVisible ? 1 : 0);
		options.put('F', Integer.valueOf(cmTextXOffsetBox.getValue()));
		//G H
		options.put('I', hpShowHearts ? 1 : 0);
		//J K L M N O P Q R
		options.put('S', Integer.valueOf(lockOnIconScaleBox.getValue()));
		options.put('T', Integer.valueOf(lockOnIconRotationBox.getValue()));
		options.put('U', Integer.valueOf(lockOnHpPerBarBox.getValue()));
		// V W
		options.put('X', Integer.valueOf(partyYDistanceBox.getValue()));
		//Y Z
		options.put('+', hpShowHearts ? 1 : 0);
		//: _ < > ( )
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
		if (!nValues.isEmpty()) {
			builder.append("-");
			nValues.forEach((b36, c) -> {
				builder.append(b36);
				builder.append(c);
			});
		}
		return builder.toString();
	}

	public void readImportCode(String code) {
		if (!code.equals("") && !isBase36Char(code.charAt(0)) || isBase36Char(code.charAt(code.length()-1))) {
			KingdomKeys.LOGGER.info("invalid import code");
			return;
		}
		String remaningCode = code;
		List<Integer> b10vals = new ArrayList<>();
		List<String> configs = new ArrayList<>();
		boolean minusToggle = false;
		StringBuilder currentVal = new StringBuilder();
		boolean base36 = true;
		while (!remaningCode.isEmpty()) {
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
			if (remaningCode.isEmpty()) {
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
		ModConfigs.setCmSelectedXOffset(0);
		ModConfigs.setCmSubXOffset(0);
		ModConfigs.setCmHeaderTextVisible(false);
		ModConfigs.setCmTextXOffset(0);
		ModConfigs.setShowHearts(false);
		ModConfigs.setLockOnIconScale(0);
		ModConfigs.setLockOnIconRotation(0);
		ModConfigs.setLockOnHpPerBar(0);
		ModConfigs.setPartyYDistance(0);
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
			}
			case 'B' -> {
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
				cmHeaderTextVisibleButton.setMessage(Component.translatable(cmHeaderTextVisible+""));
			}
			case 'F' -> {
				ModConfigs.setCmTextXOffset(value);
				cmTextXOffsetBox.setValue(""+value);
			}
			case 'G' -> {
			}
			case 'H' -> {
			}
			case 'I' -> {
				ModConfigs.setShowHearts(value == 1);
				hpShowHearts = value == 1;
				hpShowHeartsButton.setMessage(Component.translatable(hpShowHearts+""));
			}
			case 'J' -> {
			}
			case 'K' -> {
			}
			case 'L' -> {
			}
			case 'M' -> {
			}
			case 'N' -> {
			}
			case 'O' -> {
			}
			case 'P' -> {
			}
			case 'Q' -> {
			}
			case 'R' -> {
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
			}
			case 'W' -> {
			}
			case 'X' -> {
				ModConfigs.setPartyYDistance(value);
				partyYDistanceBox.setValue(""+value);
			}
			case 'Y' -> {
			}
			case 'Z' -> {
			}
			case '+' -> {
				ModConfigs.setHPAlarm(value);
				hpAlarmBox.setValue(""+value);
			}
			case ':' -> {
			}
			case '_' -> {
			}
			case '<' -> {
			}
			case '>' -> {
			}
			case '(' -> {
			}
			case ')' -> {
			}
		}
	}
}

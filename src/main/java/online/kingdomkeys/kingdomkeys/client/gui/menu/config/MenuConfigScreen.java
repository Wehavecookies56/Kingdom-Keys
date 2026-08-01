package online.kingdomkeys.kingdomkeys.client.gui.menu.config;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.client.gui.widget.ExtendedSlider;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.client.gui.elements.*;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.EditBoxLength;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton.ButtonType;
import online.kingdomkeys.kingdomkeys.client.gui.overlay.HUDEditorScreen;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.lib.CrownTier;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.*;
import online.kingdomkeys.kingdomkeys.util.Utils;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuConfigScreen extends MenuBackground {
			
	enum ActualWindow {
		PLAYER_SKIN, FONT, COMMAND_MENU, HP, LOCK_ON_HP, PARTY, IMPORT_EXPORT
	}

	ActualWindow window = ActualWindow.PLAYER_SKIN;
	
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
	Button cmHeaderTextVisibleButton, cmClassicColorsButton, snapChatButton;
	boolean cmHeaderTextVisible, cmClassicColors, snapChatToCommandMenu;
	
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
	CrownHeightSlider crownHeight;
	private float previewYaw = 0F, previewPitch = 0F;
	private boolean draggingPreview;
	Button crownRotResetX, crownRotResetY, crownRotResetZ;
	Button crownVariant;
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
		case "snapChat":
			snapChatToCommandMenu = !snapChatToCommandMenu;
			snapChatButton.setMessage(Component.translatable(snapChatToCommandMenu+""));
			ModConfigs.setSnapChatToCommandMenu(snapChatToCommandMenu);
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
			glintButton.setMessage(glintLabel());
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
		addRenderableWidget(playerSkinButton = new MenuButton((int) buttonPosX, (int) topBarHeight + 5 + y++ * 18, (int) buttonWidth, Utils.translateToLocal("gui.menu.config.player_skin"), ButtonType.BUTTON, (e) -> { window = ActualWindow.PLAYER_SKIN; }));
		addRenderableWidget(fontButton = new MenuButton((int) buttonPosX, (int) topBarHeight + 5 + y++ * 18, (int) buttonWidth, Utils.translateToLocal("gui.menu.config.font"), ButtonType.BUTTON, (e) -> { window = ActualWindow.FONT; }));
		addRenderableWidget(commandMenuButton = new MenuButton((int) buttonPosX, (int) topBarHeight + 5 + y++ * 18, (int) buttonWidth, Utils.translateToLocal("gui.menu.config.command_menu"), ButtonType.BUTTON, (e) -> { window = ActualWindow.COMMAND_MENU; }));
		addRenderableWidget(hpButton = new MenuButton((int) buttonPosX, (int) topBarHeight + 5 + + y++ * 18, (int) buttonWidth, Utils.translateToLocal("gui.menu.config.hp"), ButtonType.BUTTON, (e) -> { window = ActualWindow.HP; }));
		addRenderableWidget(lockOnButton = new MenuButton((int) buttonPosX, (int) topBarHeight + 5 + y++ * 18, (int) buttonWidth, Utils.translateToLocal("gui.menu.config.lock_on_hp"), ButtonType.BUTTON, (e) -> { window = ActualWindow.LOCK_ON_HP; }));
		addRenderableWidget(partyButton = new MenuButton((int) buttonPosX, (int) topBarHeight + 5 + y++ * 18, (int) buttonWidth, Utils.translateToLocal("gui.menu.config.party"), ButtonType.BUTTON, (e) -> { window = ActualWindow.PARTY; }));
		addRenderableWidget(impExButton = new MenuButton((int) buttonPosX, (int) topBarHeight + 5 + y++ * 18, (int) buttonWidth, Utils.translateToLocal("gui.menu.config.import_export"), ButtonType.BUTTON, (e) -> window = ActualWindow.IMPORT_EXPORT));

		addRenderableWidget(back = new MenuButton((int) buttonPosX, (int) topBarHeight + 5 + y++ * 18, (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Menu_Back), ButtonType.BUTTON, (e) -> { PacketHandler.sendToServer(new CSSyncArmorColor(PlayerData.get(minecraft.player).getArmorColor(),glint)); action("back"); }));
		addRenderableWidget(backgroundButton = new MenuButton((int)(scaledWidth/2F * 1.3F), (int) topBarHeight - 44, (int)buttonWidth, Utils.translateToLocal("gui.menu.config.bg"), ButtonType.ROUNDBUTTON, (e) -> { drawSeparately = !drawSeparately; }));
		addRenderableWidget(adjustHUDButton = new MenuButton((int)(scaledWidth/2F * 1.3F), (int) topBarHeight - 24, (int)buttonWidth, Utils.translateToLocal("gui.menu.config.hud"), ButtonType.ROUNDBUTTON, (e) -> { minecraft.setScreen(new HUDEditorScreen()); }));
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
		snapChatToCommandMenu = ModConfigs.snapChatToCommandMenu;
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
		
		addRenderableWidget(snapChatButton = Button.builder(Component.translatable(snapChatToCommandMenu+""), (e) -> {
			 action("snapChat");
		}).bounds(buttonsX - 1, (int) topBarHeight + 20 * ++pos - 2, minecraft.font.width("#####")+2, 20).build());

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
		commandMenuList.add(snapChatButton);
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

		// Notification colour: SB square + hue strip. Applies live, syncs on release.
		addRenderableWidget(notifColorPicker = new ColorPickerWidget(buttonsX, (int) (topBarHeight + 30 * ++pos), 80, 44, () -> data().getNotifColor(), c -> data().setNotifColor(c), () -> PacketHandler.sendToServer(new CSSetNotifColor(data().getNotifColor()))));
		pos += 3; // the picker is taller than one row
		addRenderableWidget(armorColorPicker = new ColorPickerWidget(buttonsX, (int) (topBarHeight + 20 * ++pos), 80, 44, () -> data().getArmorColor(), c -> data().setArmorColor(c), () -> PacketHandler.sendToServer(new CSSyncArmorColor(data().getArmorColor(), glint))));


		addRenderableWidget(crownPosition = new CrownPositionWidget(box.getX() + box.getWidth() - 155, notifColorPicker.getY() - 15, 48));
		addRenderableWidget(crownHeight = new CrownHeightSlider(crownPosition.getX() - 14, crownPosition.getY(), 10, crownPosition.getHeight(), () -> data().getCrownOffsetY(), y -> data().setCrownOffset(data().getCrownOffsetX(), y, data().getCrownOffsetZ()), this::sendCrownPacket));


		// Three axes: X pitches it forward/back, Y spins it, Z rolls it.
		addRenderableWidget(crownRotX = new ExtendedSlider(crownPosition.getX() + crownPosition.getWidth() + 2, crownPosition.getY(), 84, 16, Component.literal("X: "), Component.literal("\u00B0"), -180, 180, data().getCrownRotationX(), 1, 0, true) {
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

		addRenderableWidget(crownRotY = new ExtendedSlider(crownPosition.getX() + crownPosition.getWidth() + 2, crownPosition.getY()+16, 84, 16, Component.literal("Y: "), Component.literal("\u00B0"), -180, 180, data().getCrownRotationY(), 1, 0, true) {
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

		addRenderableWidget(crownRotZ = new ExtendedSlider(crownPosition.getX() + crownPosition.getWidth() + 2, crownPosition.getY()+32, 84, 16, Component.literal("Z: "), Component.literal("\u00B0"), -180, 180, data().getCrownRotationZ(), 1, 0, true) {
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

		addRenderableWidget(crownRotResetX = Button.builder(Component.literal("\u21BA"), (e) -> {
			crownRotX.setValue(0);
			applyCrownRotation();
			sendCrownPacket();
		}).bounds(crownRotX.getX() + crownRotX.getWidth() + 2, crownRotX.getY(), 16, 16).build());
		addRenderableWidget(crownRotResetY = Button.builder(Component.literal("\u21BA"), (e) -> {
			crownRotY.setValue(0);
			applyCrownRotation();
			sendCrownPacket();
		}).bounds(crownRotY.getX() + crownRotY.getWidth() + 2, crownRotY.getY(), 16, 16).build());
		addRenderableWidget(crownRotResetZ = Button.builder(Component.literal("\u21BA"), (e) -> {
			crownRotZ.setValue(0);
			applyCrownRotation();
			sendCrownPacket();
		}).bounds(crownRotZ.getX() + crownRotZ.getWidth() + 2, crownRotZ.getY(), 16, 16).build());

		addRenderableWidget(crownVariant = Button.builder(crownVariantLabel(), (e) -> cycleCrown()).bounds(crownPosition.getX(), crownPosition.getY() + crownPosition.getHeight() + 2, crownPosition.getWidth() + 86, 16).build());

		// Beside the armour colour swatch, filling the empty strip under the hue bar. The label says what
		// the state is instead of "true"/"false" next to a caption, so it reads on its own.
		addRenderableWidget(glintButton = Button.builder(glintLabel(), (e) -> action("glint"))
				.bounds(armorColorPicker.getSwatchRight() + 4, armorColorPicker.getSwatchTop() - 3, glintButtonWidth(), 15)
				.build());

		playerSkinList.add(armorColorPicker);
		playerSkinList.add(glintButton);
		playerSkinList.add(notifColorPicker);
		playerSkinList.add(crownPosition);
		playerSkinList.add(crownHeight);
		playerSkinList.add(crownRotResetX);
		playerSkinList.add(crownRotResetY);
		playerSkinList.add(crownRotResetZ);
		playerSkinList.add(crownRotX);
		playerSkinList.add(crownRotY);
		playerSkinList.add(crownRotZ);
		playerSkinList.add(crownVariant);
	}

	private void cycleCrown() {
		List<String> options = new ArrayList<>();
		options.add(""); // none

		for (CrownTier tier : CrownTier.values()) {
			if (data().hasUnlockedCrown(tier.getName())) {
				options.add(tier.getName());
			}
		}

		int index = options.indexOf(data().getCrown());
		String next = options.get((index + 1) % options.size()); // -1 wraps to 0, which is "none"

		data().setCrown(next);
		crownVariant.setMessage(crownVariantLabel());
		PacketHandler.sendToServer(new CSSetCrown(next));
	}

	private Component glintLabel() {
		return Component.translatable("gui.menu.config.armor.glint." + (glint ? "enabled" : "disabled"));
	}


	private int glintButtonWidth() {
		int enabled = minecraft.font.width(Utils.translateToLocal("gui.menu.config.armor.glint.enabled"));
		int disabled = minecraft.font.width(Utils.translateToLocal("gui.menu.config.armor.glint.disabled"));
		return Math.max(enabled, disabled) + 10;
	}

	private Component crownVariantLabel() {
		String crown = data().getCrown();
		CrownTier tier = CrownTier.byName(crown);

		Component name = tier != null ? Component.translatable(tier.getTranslationKey()) : crown.isEmpty() ? Component.translatable("kingdomkeys.crown.none") : Component.literal(crown); // a crown from a pack, shown as-is

		return Component.translatable("kingdomkeys.gui.config.crown_variant", name);
	}
		
	@Override
    public boolean keyPressed(int p_keyPressed_1_, int p_keyPressed_2_, int p_keyPressed_3_) {
        if (p_keyPressed_1_ == 256 || p_keyPressed_1_ == Minecraft.getInstance().options.keyInventory.getKey().getValue()) { //256 = Esc
    		PlayerData playerData = PlayerData.get(minecraft.player);
			PacketHandler.sendToServer(new CSSyncArmorColor(playerData.getArmorColor(),glint));
			PacketHandler.sendToServer(new CSSetNotifColor(playerData.getNotifColor()));
        }
        return super.keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_);
    }

	
	private boolean overPreview(double mouseX, double mouseY) {
		return window == ActualWindow.PLAYER_SKIN;
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (super.mouseClicked(mouseX, mouseY, button))
			return true;
		if (button == 0 && overPreview(mouseX, mouseY)) {
			draggingPreview = true;
			return true;
		}
		return false;
	}

	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
		if (draggingPreview) {
			previewYaw += (float) dragX;
			previewPitch = net.minecraft.util.Mth.clamp(previewPitch + (float) dragY, -80F, 80F);
			return true;
		}
		return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		draggingPreview = false;
		return super.mouseReleased(mouseX, mouseY, button);
	}

	private void drawCentredOverSwatch(GuiGraphics gui, String key, ColorPickerWidget picker) {
		String text = Utils.translateToLocal(key);
		int swatchWidth = picker.getWidth() - 13;   // minus hue strip (10) and gap (3)
		int x = picker.getX() - buttonsX + (swatchWidth - minecraft.font.width(text)) / 2;
		int y = picker.getY() - (box.getY() + 4) + picker.getHeight() + 4;
		gui.drawString(minecraft.font, text, x, y, 0xFF9900);
	}

	private PlayerData data() {
		return PlayerData.get(minecraft.player);
	}

	/** Applies the three rotation sliders locally, so the crown updates while dragging. */
	private void applyCrownRotation() {
		data().setCrownRotation((float) crownRotX.getValue(), (float) crownRotY.getValue(), (float) crownRotZ.getValue());
	}

	/** Sends position and all three rotations in one packet, on release. */
	private void sendCrownPacket() {
		PlayerData pd = data();
		PacketHandler.sendToServer(new CSSetCrownOffset(pd.getCrownOffsetX(), pd.getCrownOffsetY(), pd.getCrownOffsetZ(), pd.getCrownRotationX(), pd.getCrownRotationY(), pd.getCrownRotationZ()));
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
		playerSkinButton.active = window != ActualWindow.PLAYER_SKIN;
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
			float scale = 1.5F;
			gui.pose().scale(scale, scale, 1);
			gui.drawString(minecraft.font, Component.literal(Utils.translateToLocal("gui.menu.config."+window.name().toLowerCase())).withStyle(ClientUtils.KK_Font_EXP), (int) (topLeftBar.getWidth() / scale + topGap) + 5, 10, 0xFF9900);
		}
		matrixStack.popPose();

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

					gui.drawString(minecraft.font, Utils.translateToLocal("gui.menu.config.custom_font"), 40, 20 * ++pos, 0xFF9900);
				}
				case COMMAND_MENU -> {
					for (AbstractWidget b : commandMenuList) {
						b.active = true;
						b.visible = true;
					}

					gui.drawString(minecraft.font, Utils.translateToLocal("gui.menu.config.classic_colors"), 40, 20 * ++pos, 0xFF9900);
					gui.drawString(minecraft.font, Utils.translateToLocal("gui.menu.config.selected_x_pos"), 40, 20 * ++pos, 0xFF9900);
					gui.drawString(minecraft.font, Utils.translateToLocal("gui.menu.config.sub_x_offset"), 40, 20 * ++pos, 0xFF9900);
					gui.drawString(minecraft.font, Utils.translateToLocal("gui.menu.config.header_title"), 40, 20 * ++pos, 0xFF9900);
					gui.drawString(minecraft.font, Utils.translateToLocal("gui.menu.config.text_x_offset"), 40, 20 * ++pos, 0xFF9900);
					gui.drawString(minecraft.font, Utils.translateToLocal("gui.menu.config.snap_chat"), 40, 20 * ++pos, 0xFF9900);
				}
				case HP -> {
					for (AbstractWidget b : hpList) {
						b.active = true;
						b.visible = true;
					}

					gui.drawString(minecraft.font, Utils.translateToLocal("gui.menu.config.show_hearts"), 40, 20 * ++pos, 0xFF9900);
					gui.drawString(minecraft.font, Utils.translateToLocal("gui.menu.config.hp_alarm"), 40, 20 * ++pos, 0xFF9900);
				}
				case PLAYER_SKIN -> {
					for (AbstractWidget b : playerSkinList) {
						b.active = true;
						b.visible = true;
					}

					// After the loop above, which switches everything back on: with no crown earned there
					// is nothing to cycle through, so the button stays greyed out.
					crownVariant.active = !data().getUnlockedCrowns().isEmpty();

					Player player = Minecraft.getInstance().player;

					matrixStack.pushPose();
						{
						matrixStack.translate(box.getX() - buttonsX + 10, 4, 0);
						RenderSystem.enableBlend();
						int notif = PlayerData.get(minecraft.player).getNotifColor();
						RenderSystem.setShaderColor(((notif >> 16) & 0xFF) / 255F, ((notif >> 8) & 0xFF) / 255F, (notif & 0xFF) / 255F, 1F);
						ResourceLocation levelUpTexture = KingdomKeys.rl("textures/gui/levelup.png");

						float notifScale = 0.33F;
						// Top
						matrixStack.pushPose();
						{
							matrixStack.scale(notifScale, notifScale, 1);
							gui.blit(levelUpTexture, 0, 0, 0, 0, 256, 36);
						}
						matrixStack.popPose();

						// Half
						matrixStack.pushPose();
						{
							matrixStack.translate(0, 36.0f * notifScale, 0);
							matrixStack.scale(notifScale, 0, 1);
							gui.blit(levelUpTexture, 0, 0, 0, 36, 256, 1);
						}
						matrixStack.popPose();

						// Bottom
						matrixStack.pushPose();
						{
							matrixStack.translate(0, 36.0f * notifScale, 0);
							matrixStack.scale(notifScale, notifScale, 1);
							gui.blit(levelUpTexture, 0, 0, 0, 37, 256, 14);
						}
						matrixStack.popPose();
						RenderSystem.disableBlend();
					}
					matrixStack.popPose();
					RenderSystem.setShaderColor(1,1,1,1);

					int hx = crownPosition.getX() - buttonsX;
					int hy = crownPosition.getY() - (box.getY() + 4) + crownPosition.getHeight() + 4;   // 4 = the old HEAD_PREVIEW_GAP, inlined now that the constant is gone
					int renderSize = 16;
					matrixStack.pushPose();
					{
						matrixStack.translate(hx+50, hy+160, 0);
						matrixStack.scale(renderSize, renderSize, 1);
						if (player instanceof AbstractClientPlayer acpPreview) {
							ClientUtils.renderPlayerNoAnimsRaw(matrixStack, 0, 0, 4, 0F, -previewPitch / 20F, acpPreview, previewYaw);
						}
					}
					matrixStack.popPose();

					drawCentredOverSwatch(gui, "gui.menu.config.notif", notifColorPicker);
					drawCentredOverSwatch(gui, "gui.menu.config.armor", armorColorPicker);
				}
				case LOCK_ON_HP -> {
					for (AbstractWidget b : lockOnList) {
						b.active = true;
						b.visible = true;
					}

					gui.drawString(minecraft.font, Utils.translateToLocal("gui.menu.config.icon_scale"), 40, 20 * ++pos, 0xFF9900);
					gui.drawString(minecraft.font, Utils.translateToLocal("gui.menu.config.icon_rotation"), 40, 20 * ++pos, 0xFF9900);
					gui.drawString(minecraft.font, Utils.translateToLocal("gui.menu.config.hp_per_bar"), 40, 20 * ++pos, 0xFF9900);

				}
				case PARTY -> {
					for (AbstractWidget b : partyList) {
						b.active = true;
						b.visible = true;
					}

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

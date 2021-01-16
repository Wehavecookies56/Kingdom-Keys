package online.kingdomkeys.kingdomkeys.client.gui.menu.config;

import java.awt.Color;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.TranslationTextComponent;
import online.kingdomkeys.kingdomkeys.client.gui.GuiHelper;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBox;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton.ButtonType;
import online.kingdomkeys.kingdomkeys.config.ClientConfig;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class MenuConfigScreen extends MenuBackground {

	Button stats_back, cmHeaderTextVisibleButton;
	MenuBox box;
	TextFieldWidget cmTextXOffsetBox;
	
	boolean cmHeaderTextVisible;

	
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
		}
		
		updateButtons();
	}

	private void updateButtons() {
		
	}

	@Override
	public void init() {
		cmHeaderTextVisible = ModConfigs.cmHeaderTextVisible;

		float boxPosX = (float) width * 0.2F;
		float topBarHeight = (float) height * 0.17F;
		float boxWidth = (float) width * 0.67F;
		float middleHeight = (float) height * 0.6F;
		
		
		box = new MenuBox((int) boxPosX, (int) topBarHeight, (int) boxWidth, (int) middleHeight, new Color(4, 4, 68));
	
		super.init();
		this.buttons.clear();

		addButton(cmHeaderTextVisibleButton = new Button(box.x+99, (int) topBarHeight + 18, (int) (buttonWidth * 0.55F), 20, new TranslationTextComponent(cmHeaderTextVisible+""), (e) -> { action("textHeaderVisibility"); }));
		addButton(cmTextXOffsetBox = new TextFieldWidget(minecraft.fontRenderer, box.x+100, (int) (topBarHeight + 20 * 2), minecraft.fontRenderer.getStringWidth("#####"), 16, new TranslationTextComponent("test")){
			@Override
			public boolean charTyped(char c, int i) {
				if (Utils.isNumber(c) || c == '-') {
					String text = new StringBuilder(this.getText()).insert(this.getCursorPosition(), c).toString();
					if (Utils.getInt(text) < 1000 && Utils.getInt(text) > -1000) {
						super.charTyped(c, i);
						ModConfigs.setCmTextXOffset(Utils.getInt(cmTextXOffsetBox.getText()));
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
				ModConfigs.setCmTextXOffset(Utils.getInt(cmTextXOffsetBox.getText()));
				return true;
			}
			
		});
		
		
		addButton(stats_back = new MenuButton((int) buttonPosX, (int) topBarHeight + (0 * 18), (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Menu_Back), ButtonType.BUTTON, (e) -> { action("back"); }));
		
		cmTextXOffsetBox.setText(""+ModConfigs.cmTextXOffset);
		cmHeaderTextVisibleButton.setMessage(new TranslationTextComponent(cmHeaderTextVisible+""));
		
		updateButtons();
	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		box.draw(matrixStack);
		super.render(matrixStack, mouseX, mouseY, partialTicks);

		matrixStack.push();
		{
			matrixStack.translate(box.x+8, box.y+4, 1);
			drawString(matrixStack, minecraft.fontRenderer, Utils.translateToLocal("Command Menu"), 0, 0, 0xFF9900);
			drawString(matrixStack, minecraft.fontRenderer, Utils.translateToLocal("Header Title: "), 12, 20, 0xFF9900);
			drawString(matrixStack, minecraft.fontRenderer, Utils.translateToLocal("Text X Offset: "), 12, 20*2, 0xFF9900);
		}
		matrixStack.pop();

	
	}
	
}

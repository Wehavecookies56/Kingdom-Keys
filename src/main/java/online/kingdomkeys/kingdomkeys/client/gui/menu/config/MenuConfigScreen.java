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
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class MenuConfigScreen extends MenuBackground {

	Button save, stats_back;
	MenuBox box;
	TextFieldWidget amountBox;

	
	public MenuConfigScreen() {
		super(Strings.Gui_Menu_Config, new Color(0,0,255));
		drawPlayerInfo = false;
	}

	
	protected void action(String string) {
		switch(string) {
		case "back":
			GuiHelper.openMenu();
			break;
		case "save":
			ClientConfig.cmTextXOffset.set(Utils.getInt(amountBox.getText()));
			break;
		}
		
		updateButtons();
	}

	private void updateButtons() {
		
	}

	@Override
	public void init() {
		float boxPosX = (float) width * 0.2F;
		float topBarHeight = (float) height * 0.17F;
		float boxWidth = (float) width * 0.67F;
		float middleHeight = (float) height * 0.6F;
		box = new MenuBox((int) boxPosX, (int) topBarHeight, (int) boxWidth, (int) middleHeight, new Color(4, 4, 68));
	
		super.init();
		this.buttons.clear();

		addButton(amountBox = new TextFieldWidget(minecraft.fontRenderer, box.x+100, (int) (topBarHeight + 16), minecraft.fontRenderer.getStringWidth("#####"), 16, new TranslationTextComponent("test")){
			@Override
			public boolean charTyped(char c, int i) {
				if (Utils.isNumber(c) || c == '-') {
					String text = new StringBuilder(this.getText()).insert(this.getCursorPosition(), c).toString();
					if (Utils.getInt(text) > 1000 || Utils.getInt(text) < -1000) {
						return false;
					}
				} else {
					return false;
				}
				return super.charTyped(c, i);
			}
		});
		
		addButton(stats_back = new MenuButton((int) buttonPosX, (int) topBarHeight + (0 * 18), (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Menu_Back), ButtonType.BUTTON, (e) -> { action("back"); }));
		addButton(save = new MenuButton((int) buttonPosX, (int) topBarHeight + (1 * 18), (int) buttonWidth, Utils.translateToLocal("Save"), ButtonType.BUTTON, (e) -> { action("save"); }));
		
		amountBox.setText(""+ClientConfig.cmTextXOffset.get());

		
		updateButtons();
	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		//fill(matrixStack, 125, ((-140 / 16) + 75) + 10, 200, ((-140 / 16) + 75) + 20, 0xFFFFFF);

		box.draw(matrixStack);
		super.render(matrixStack, mouseX, mouseY, partialTicks);

		matrixStack.push();
		{
			matrixStack.translate(box.x+8, box.y+8, 1);
			drawString(matrixStack, minecraft.fontRenderer, Utils.translateToLocal("Command Menu"), 0, 0, 0xFF9900);
			drawString(matrixStack, minecraft.fontRenderer, Utils.translateToLocal("Text X Offset: "), 12, 12, 0xFF9900);
		}
		matrixStack.pop();

	
	}
	
}

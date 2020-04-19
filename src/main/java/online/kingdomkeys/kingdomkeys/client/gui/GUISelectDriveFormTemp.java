package online.kingdomkeys.kingdomkeys.client.gui;


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.TranslationTextComponent;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.PacketSetDriveForm;

public class GUISelectDriveFormTemp extends Screen{

	Button ok;
	
	public GUISelectDriveFormTemp() {
        super(new TranslationTextComponent(""));
    }
	
	@Override
	protected void init() {
		buttons.clear();
		int yPos = 120;
		addButton(new Button(this.width / 2 - 100, this.height - 120, 200, 20, Minecraft.getInstance().player.getDisplayName().getFormattedText()+"", (e) -> { select(""); }));
		addButton(new Button(this.width / 2 - 100, this.height - 100, 200, 20, "Valor", (e) -> { select("valor"); }));
		addButton(new Button(this.width / 2 - 100, this.height - 80, 200, 20, "Wisdom", (e) -> { select("wisdom"); }));
		addButton(new Button(this.width / 2 - 100, this.height - 60, 200, 20, "Limit", (e) -> { select("limit"); }));
		addButton(new Button(this.width / 2 - 100, this.height - 40, 200, 20, "Master", (e) -> { select("master"); }));
		addButton(new Button(this.width / 2 - 100, this.height - 20, 200, 20, "Final", (e) -> { select("final"); }));
        //buttons.add(ok = new Button(0, 0, 0, 50, 20, "OK", ));

		super.init();
	}
	
	private void select(String model) {
		PacketHandler.sendToServer(new PacketSetDriveForm(model));
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
        renderBackground();
		super.render(mouseX, mouseY, partialTicks);
	}
}

package online.kingdomkeys.kingdomkeys.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.TranslationTextComponent;
import online.kingdomkeys.kingdomkeys.capability.ILevelCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;

public class GUIMenu extends Screen{
	
	public GUIMenu() {
        super(new TranslationTextComponent(""));
    }
	
	@Override
	protected void init() {
		buttons.clear();
		addButton(new Button(this.width / 2 - 100, this.height - 80, 200, 20, "Rayman", (e) -> { select("rayman"); }));
		addButton(new Button(this.width / 2 - 100, this.height - 60, 200, 20, "Red Robopirate", (e) -> { select("robopirate"); }));
		addButton(new Button(this.width / 2 - 100, this.height - 40, 200, 20, "Green Robopirate", (e) -> { select("robopirate2"); }));
        //buttons.add(ok = new Button(0, 0, 0, 50, 20, "OK", ));

		super.init();
	}
	
	private void select(String model) {
		//PacketHandler.sendToServer(new  PacketSetModel(model));
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
        renderBackground();
        Minecraft mc = Minecraft.getInstance();
        ILevelCapabilities props = ModCapabilities.get(mc.player);
        if(props != null) {
        String text = "Level: "+props.getLevel();
		drawCenteredString(Minecraft.getInstance().fontRenderer, text, this.width / 2 - text.length()/2, 120, 0xFFFFFF);
        }
		super.render(mouseX, mouseY, partialTicks);
	}
}

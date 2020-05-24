package online.kingdomkeys.kingdomkeys.client.gui;


import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.TranslationTextComponent;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.PacketSetDriveForm;

public class GUISelectDriveFormTemp extends Screen{

	Button pla,val,wis,lim,mas,fin;
	
	public GUISelectDriveFormTemp() {
        super(new TranslationTextComponent(""));
    }
	
	@Override
	protected void init() {
		buttons.clear();
		int yPos = 120;
		addButton(pla = new Button(this.width / 2 - 100, this.height - 120, 200, 20, Minecraft.getInstance().player.getDisplayName().getFormattedText()+"", (e) -> { select(""); }));
		addButton(val = new Button(this.width / 2 - 100, this.height - 100, 200, 20, "Valor", (e) -> { select(Strings.Form_Valor); }));
		addButton(wis = new Button(this.width / 2 - 100, this.height - 80, 200, 20, "Wisdom", (e) -> { select(Strings.Form_Wisdom); }));
		addButton(lim = new Button(this.width / 2 - 100, this.height - 60, 200, 20, "Limit", (e) -> { select(Strings.Form_Limit); }));
		addButton(mas = new Button(this.width / 2 - 100, this.height - 40, 200, 20, "Master", (e) -> { select(Strings.Form_Master); }));
		addButton(fin = new Button(this.width / 2 - 100, this.height - 20, 200, 20, "Final", (e) -> { select(Strings.Form_Final); }));

		super.init();
	}
	
	private void select(String model) {
		PacketHandler.sendToServer(new PacketSetDriveForm(model));
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
        renderBackground();
        ClientPlayerEntity player = Minecraft.getInstance().player;
        String form = ModCapabilities.get(player).getActiveDriveForm();
        
        for(int i = 0;i<buttons.size();i++) {
        	Button b = (Button) buttons.get(i);
        	//System.out.println(b.getMessage());
        	b.active = !form.contains(b.getMessage().toLowerCase());
        	
        }
		super.render(mouseX, mouseY, partialTicks);
	}
}

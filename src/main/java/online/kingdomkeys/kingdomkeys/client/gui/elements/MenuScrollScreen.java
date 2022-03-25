package online.kingdomkeys.kingdomkeys.client.gui.elements;

import java.awt.Color;
import java.util.ArrayList;

import net.minecraft.client.gui.components.Button;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;

public class MenuScrollScreen extends MenuBackground {

    public MenuScrollScreen(String name, Color color) {
        super(name, color);
        drawSeparately = true;
    }

    protected int scrollOffset = 0;
    protected ArrayList<Button> totalButtons = new ArrayList<Button>();
    protected int maxItems;
    protected int transformedScroll = scrollOffset * 15;


    @Override
    public void init() {
        super.init();
        
     
        for(Button b : totalButtons) {
        	b.visible = false;
        }
        
		for (int i = scrollOffset; i < scrollOffset + maxItems && i < totalButtons.size(); i++) {
			Button b = totalButtons.get(i);
			b.visible = true;
		}

    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
    	if(delta < 0 && scrollOffset < totalButtons.size() - maxItems) {
    		scrollOffset++;
    		minecraft.player.playSound(ModSounds.menu_move.get(), 1, 1);
    	} else if (delta > 0 && scrollOffset > 0) {
    		scrollOffset--;
    		minecraft.player.playSound(ModSounds.menu_move.get(), 1, 1);
    	}
    
    	init();
    	return super.mouseScrolled(mouseX, mouseY, delta);
    }
}
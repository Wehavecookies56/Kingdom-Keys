package online.kingdomkeys.kingdomkeys.client.gui.hud;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.AbstractGui;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * Class to initialise and render all the HUD elements
 */
public class HUDElementHandler extends AbstractGui {

    private List<HUDElement> elements = new ArrayList<>();

    private CommandMenuHUDElement CommandMenu;
    private HPBarHUDElement HPBar;

    public HUDElementHandler() {
        init();
    }

    protected void init() {
        elements.clear();
        elements.add(CommandMenu = new CommandMenuHUDElement(0, 0, 0, 0, HUDAnchorPosition.BOTTOM_LEFT, "Command Menu"));
        elements.add(HPBar = new HPBarHUDElement(0, 0, 256, 256, HUDAnchorPosition.CENTER, "HP Bar"));
        elements.forEach(HUDElement::initElement);
        elements.forEach(HUDElement::anchorElement);
    }

    @SubscribeEvent
    public void onRenderOverlayPost(RenderGameOverlayEvent.Post event) {
        elements.forEach(HUDElement::anchorElement);
        if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {
            elements.forEach(element -> {
                element.render(event.getPartialTicks());
            });
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        elements.forEach(HUDElement::tick);
    }

}

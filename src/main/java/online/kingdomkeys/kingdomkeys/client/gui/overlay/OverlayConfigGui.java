package online.kingdomkeys.kingdomkeys.client.gui.overlay;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import online.kingdomkeys.kingdomkeys.client.gui.elements.DraggableElement;

public class OverlayConfigGui extends Screen {

    public OverlayConfigGui() {
        super(Component.empty());
    }

    DraggableElement test;

    @Override
    protected void init() {
        super.init();
        addRenderableWidget(test = new DraggableElement(0, 0, 100, 100));
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }
}

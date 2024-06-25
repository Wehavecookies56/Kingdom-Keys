package online.kingdomkeys.kingdomkeys.client.gui.elements.buttons;

import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class ScrollableButtonBase extends Button {

    public int offsetY;
    public ScrollableButtonBase(int pX, int pY, int pWidth, int pHeight, Component pMessage, OnPress pOnPress, CreateNarration pCreateNarration) {
        super(pX, pY, pWidth, pHeight, pMessage, pOnPress, pCreateNarration);
    }

    public ScrollableButtonBase(Builder builder) {
        super(builder);
    }

    @Override
    public int getY() {
        return super.getY() - offsetY;
    }
}

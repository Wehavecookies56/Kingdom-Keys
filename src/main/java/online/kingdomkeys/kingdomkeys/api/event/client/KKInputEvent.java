package online.kingdomkeys.kingdomkeys.api.event.client;

import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;
import online.kingdomkeys.kingdomkeys.handler.InputHandler;

public abstract class KKInputEvent extends Event {

    private final InputHandler.Keybinds keybind;
    private final InputHandler handler;

    private KKInputEvent(InputHandler.Keybinds keybind, InputHandler handler) {
        this.handler = handler;
        this.keybind = keybind;
    }

    public InputHandler.Keybinds getKeybind() {
        return keybind;
    }

    public InputHandler getHandler() {
        return handler;
    }

    /**
     * Event is posted before input is handled by KK, cancel to stop KK's default behaviour with the keybind
     */
    public static class Pre extends KKInputEvent implements ICancellableEvent {
        public Pre(InputHandler.Keybinds keybind, InputHandler handler) {
            super(keybind, handler);
        }
    }

    /**
     * Event is posted after input is handled by KK
     */
    public static class Post extends KKInputEvent {
        public Post(InputHandler.Keybinds keybind, InputHandler handler) {
            super(keybind, handler);
        }
    }
}

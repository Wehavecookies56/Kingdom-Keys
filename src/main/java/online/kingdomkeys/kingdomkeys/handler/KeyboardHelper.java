package online.kingdomkeys.kingdomkeys.handler;

import org.lwjgl.glfw.GLFW;

import net.minecraft.client.Minecraft;
import net.minecraft.client.util.InputMappings;

public class KeyboardHelper {

    // Call these to detect if a key is pressed. To add more keys, follow the
    // same format.

    public static boolean isShiftDown () {
        return isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT) || isKeyDown(GLFW.GLFW_KEY_RIGHT_SHIFT);
    }

    public static boolean isControlDown () {
    	return isKeyDown(GLFW.GLFW_KEY_LEFT_CONTROL) || isKeyDown(GLFW.GLFW_KEY_RIGHT_CONTROL);
    }

    public static boolean isScrollActivatorDown () {
        return isKeyDown(InputHandler.Keybinds.SCROLL_ACTIVATOR.getKeybind().getKey().getKeyCode());
    }

    /**
     * Wrapper function so the the MC window handle doesn't need to passed every time
     * @param key the key to check if being pressed
     * @return whether the key is being pressed
     */
    public static boolean isKeyDown(int key) {
        return InputMappings.isKeyDown(Minecraft.getInstance().getMainWindow().getHandle(), key);
    }

}
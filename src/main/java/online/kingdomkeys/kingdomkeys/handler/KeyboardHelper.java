package online.kingdomkeys.kingdomkeys.handler;

import org.lwjgl.glfw.GLFW;

import net.minecraft.client.Minecraft;
import net.minecraft.client.util.InputMappings;

public class KeyboardHelper {

    // Call these to detect if a key is pressed. To add more keys, follow the
    // same format.

    public static boolean isShiftDown () {
        return isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT) || isKeyDown(GLFW.GLFW_KEY_RIGHT_SHIFT);
        /*    return Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
        else return false;*/
    }

   /*public static boolean isControlDown () {
        if (Keyboard.isCreated())
            return Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL);
        else return false;
    }*/

    public static boolean isScrollActivatorDown () {

        /*if(InputMappings.getInputByName("key.kingdomkeys.scrollactivator") != null)
            return InputMappings.isKeyDown(InputMappings.getInputByName("key.kingdomkeys.scrollactivator").getKeyCode());
        else
            return false;*/
        return isKeyDown(GLFW.GLFW_KEY_LEFT_ALT); //TODO change to detect the mapped key
    }

    /**
     * Wrapper function so the the MC window handle doesn't need to passed every time
     * @param key the key to check if being pressed
     * @return whether the key is being pressed
     */
    public static boolean isKeyDown(int key) {
        return InputMappings.isKeyDown(Minecraft.getInstance().func_228018_at_().getHandle(), key);
    }

}
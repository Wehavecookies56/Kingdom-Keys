package online.kingdomkeys.kingdomkeys.handler;

import org.lwjgl.glfw.GLFW;

import net.minecraft.client.util.InputMappings;

public class KeyboardHelper {

    // Call these to detect if a key is pressed. To add more keys, follow the
    // same format.

    public static boolean isShiftDown () {
        return InputMappings.isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT) || InputMappings.isKeyDown(GLFW.GLFW_KEY_RIGHT_SHIFT);
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
        return InputMappings.isKeyDown(GLFW.GLFW_KEY_LEFT_ALT);
    }

}
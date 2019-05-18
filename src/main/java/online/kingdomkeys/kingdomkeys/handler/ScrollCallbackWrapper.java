package online.kingdomkeys.kingdomkeys.handler;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWScrollCallback;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

//Thanks to gigaherz and Tschipp for the help!
public class ScrollCallbackWrapper {
    GLFWScrollCallback oldCallback;

    public void setup(Minecraft mc) {
        oldCallback = GLFW.glfwSetScrollCallback(mc.mainWindow.getHandle(), this::scrollCallback);
    }

    private void scrollCallback(long window, double xoffset, double yoffset) {
        MouseScrolledEvent event = new MouseScrolledEvent(xoffset, yoffset);
        MinecraftForge.EVENT_BUS.post(event);

        if (event.isCanceled())
            return;

        if (oldCallback != null)
            oldCallback.invoke(window, xoffset, yoffset);
    }

    @Cancelable
    public static class MouseScrolledEvent extends Event {
        double xOffset, yOffset;

        public MouseScrolledEvent(double x, double y){
            this.xOffset = x;
            this.yOffset = y;
        }

        public double getYOffset() {
            return yOffset;
        }
    }
}
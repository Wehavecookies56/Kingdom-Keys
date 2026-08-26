package online.kingdomkeys.kingdomkeys.mixin.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ChatComponent;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Lifts the chat log clear of the command menu, which shares the bottom-left corner with it.
 *
 * <p>Done here rather than around the chat GUI layer because the offset has to apply to the hit
 * testing as well. {@code screenToChatY} is what turns a mouse position into a chat line, and if only
 * the drawing moved, clicking a link would hit whatever message used to be under the cursor.</p>
 */
@Mixin(ChatComponent.class)
public class ChatComponentMixin {

    @Inject(method = "render", at = @At("HEAD"))
    private void kingdom_Keys$liftChat(GuiGraphics guiGraphics, int tickCount, int mouseX, int mouseY, boolean focused, CallbackInfo ci) {
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0, -ClientUtils.getChatLift(), 0);
    }

    @Inject(method = "render", at = @At("RETURN"))
    private void kingdom_Keys$dropChat(GuiGraphics guiGraphics, int tickCount, int mouseX, int mouseY, boolean focused, CallbackInfo ci) {
        guiGraphics.pose().popPose();
    }

    /**
     * The log is drawn that many pixels higher, so a cursor at screen Y is over whatever would have
     * been that much further down in the unshifted layout.
     */
    @ModifyVariable(method = "screenToChatY", at = @At("HEAD"), argsOnly = true)
    private double kingdom_Keys$shiftMouseY(double y) {
        return y + ClientUtils.getChatLift();
    }
}

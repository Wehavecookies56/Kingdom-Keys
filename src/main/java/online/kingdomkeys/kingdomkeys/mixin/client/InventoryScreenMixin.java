package online.kingdomkeys.kingdomkeys.mixin.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.world.entity.LivingEntity;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Flags the vanilla "draw a player inside a GUI" path.
 *
 * <p>The inventory preview goes through the normal entity renderer while the camera is still in first
 * person, so anything that hides parts of the player because the camera is inside them would trim the
 * preview too - you would be looking at a floating pair of armoured arms. Every vanilla screen that
 * shows a player funnels through this one method, including our own pauldron screen.</p>
 */
@Mixin(InventoryScreen.class)
public class InventoryScreenMixin {

    @Inject(method = "renderEntityInInventory", at = @At("HEAD"))
    private static void kingdom_Keys$guiRenderStart(GuiGraphics guiGraphics, float x, float y, float scale, Vector3f translate, Quaternionf pose, Quaternionf cameraOrientation, LivingEntity entity, CallbackInfo ci) {
        ClientUtils.renderingEntityInGui = true;
    }

    @Inject(method = "renderEntityInInventory", at = @At("RETURN"))
    private static void kingdom_Keys$guiRenderEnd(GuiGraphics guiGraphics, float x, float y, float scale, Vector3f translate, Quaternionf pose, Quaternionf cameraOrientation, LivingEntity entity, CallbackInfo ci) {
        ClientUtils.renderingEntityInGui = false;
    }
}

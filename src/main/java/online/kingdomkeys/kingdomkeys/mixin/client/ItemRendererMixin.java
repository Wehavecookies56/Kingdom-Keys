package online.kingdomkeys.kingdomkeys.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import online.kingdomkeys.kingdomkeys.client.render.LargeItemModels;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Hands every item render to {@link LargeItemModels} first, which draws the heavy ones from a
 * buffer already on the GPU and turns everything else straight back to vanilla.
 *
 * <p>At the head of the one method every path goes through — pedestals, item frames, hands, the
 * ground, and Epic Fight's own weapon rendering — so a single hook covers all of them.</p>
 */
@Mixin(ItemRenderer.class)
public class ItemRendererMixin {

    @Inject(
            method = "render(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IILnet/minecraft/client/resources/model/BakedModel;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void kingdomKeys$drawBaked(ItemStack stack, ItemDisplayContext context, boolean leftHand, PoseStack pose, MultiBufferSource buffer, int light, int overlay, BakedModel model, CallbackInfo ci) {
        if (LargeItemModels.render(stack, context, leftHand, pose, light, overlay, model)) {
            ci.cancel();
        }
    }
}

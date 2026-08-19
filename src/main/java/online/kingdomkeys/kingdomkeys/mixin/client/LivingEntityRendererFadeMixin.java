package online.kingdomkeys.kingdomkeys.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.LivingEntity;
import online.kingdomkeys.kingdomkeys.entity.mob.BaseKHEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Fades a boss out instead of letting it stand there solid until it is taken away.
 *
 * Alpha on its own does nothing to a normal entity, because the render type it draws with has no blending, so
 * this does two things at the one call site: it tells the renderer the body is translucent, which is a road
 * vanilla already has for entities you can half see, and then it hands the model the alpha to draw at. Doing
 * it here rather than from an event is the only way in: neither the render type nor the colour is exposed.
 *
 * The value comes from the entity itself so the fade and the light that replaces it always agree.
 */
@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererFadeMixin {

	@Unique
	private float kingdomKeys$alpha = 1F;

	@Inject(method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At("HEAD"))
	private void kingdomKeys$readAlpha(LivingEntity entity, float yaw, float partialTick, PoseStack pose, MultiBufferSource buffer, int light, CallbackInfo ci) {
		this.kingdomKeys$alpha = entity instanceof BaseKHEntity boss && boss.isDyingWithRays() ? boss.deathAlpha(partialTick) : 1F;
	}

	// Sends it down vanilla's own translucent branch, which picks the blending render type for the right texture
	@ModifyArg(
			method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/LivingEntityRenderer;getRenderType(Lnet/minecraft/world/entity/LivingEntity;ZZZ)Lnet/minecraft/client/renderer/RenderType;"),
			index = 2
	)
	private boolean kingdomKeys$forceTranslucent(boolean translucent) {
		return translucent || this.kingdomKeys$alpha < 1F;
	}

	// And the color the model is drawn with, whose top byte is the alpha. White below it, so only the fade shows.
	@ModifyArg(
			method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/EntityModel;renderToBuffer(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;III)V"),
			index = 4
	)
	private int kingdomKeys$fade(int colour) {
		return this.kingdomKeys$alpha < 1F ? FastColor.ARGB32.colorFromFloat(this.kingdomKeys$alpha, 1F, 1F, 1F) : colour;
	}
}

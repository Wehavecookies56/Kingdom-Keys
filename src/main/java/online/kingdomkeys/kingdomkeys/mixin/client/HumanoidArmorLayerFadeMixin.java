package online.kingdomkeys.kingdomkeys.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.entity.mob.BaseKHEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidArmorLayer.class)
public class HumanoidArmorLayerFadeMixin {

	@Unique
	private float kingdomKeys$alpha = 1F;

	@Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/LivingEntity;FFFFFF)V", at = @At("HEAD"))
	private void kingdomKeys$readAlpha(PoseStack pose, MultiBufferSource buffer, int light, LivingEntity entity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
		this.kingdomKeys$alpha = entity instanceof BaseKHEntity boss && boss.isDyingWithRays() ? boss.deathAlpha(partialTick) : 1F;
	}

	@Redirect(
			method = "renderModel(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/model/Model;ILnet/minecraft/resources/ResourceLocation;)V",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderType;armorCutoutNoCull(Lnet/minecraft/resources/ResourceLocation;)Lnet/minecraft/client/renderer/RenderType;")
	)
	private RenderType kingdomKeys$blendableArmor(ResourceLocation texture) {
		return this.kingdomKeys$alpha < 1F ? ClientUtils.armorTranslucentNoCull(texture) : RenderType.armorCutoutNoCull(texture);
	}

	// The colour's top byte is the alpha. Everything below it is left alone so dyed armour keeps its dye.
	@ModifyArg(
			method = "renderModel(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/model/Model;ILnet/minecraft/resources/ResourceLocation;)V",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/Model;renderToBuffer(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;III)V"),
			index = 4
	)
	private int kingdomKeys$fade(int colour) {
		if (this.kingdomKeys$alpha >= 1F) {
			return colour;
		}

		int alpha = Math.round(this.kingdomKeys$alpha * 255F);
		return (colour & 0x00FFFFFF) | (alpha << 24);
	}
}

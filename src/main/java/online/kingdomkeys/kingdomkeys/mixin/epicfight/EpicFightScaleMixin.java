package online.kingdomkeys.kingdomkeys.mixin.epicfight;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.api.model.Armature;
import yesman.epicfight.client.renderer.patched.entity.PatchedLivingEntityRenderer;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

@Mixin(value = PatchedLivingEntityRenderer.class, remap = false)
public abstract class EpicFightScaleMixin {

	@Inject(method = "mulPoseStack", at = @At("TAIL"))
	private void kingdomkeys$applyScale(PoseStack poseStack, Armature armature, LivingEntity entity, LivingEntityPatch<?> entityPatch, float partialTicks, CallbackInfo ci) {
		float scale = (float) entity.getAttributeValue(Attributes.SCALE);

		if (scale != 1.0F) {
			poseStack.scale(scale, scale, scale);
		}
	}
}
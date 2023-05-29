package online.kingdomkeys.kingdomkeys.mixin;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.util.IDisabledAnimations;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends Entity> implements IDisabledAnimations {

    @Unique private boolean disabled = false;

    @Unique
    @Override
    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    @Unique
    @Override
    public boolean isDisabled() {
        return disabled;
    }

    @Redirect(method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/EntityModel;setupAnim(Lnet/minecraft/world/entity/Entity;FFFFF)V"))
    public void redirectSetupAnim(EntityModel<T> instance, T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        if (isDisabled()) {
            if (instance instanceof PlayerModel) {
                PlayerModel<AbstractClientPlayer> model = (PlayerModel<AbstractClientPlayer>) instance;
                model.leftArm.resetPose();
                model.rightArm.resetPose();
                model.body.resetPose();
                model.head.resetPose();
                model.leftLeg.resetPose();
                model.rightLeg.resetPose();
            }
        } else {
            instance.setupAnim(pEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);
        }
    }

}

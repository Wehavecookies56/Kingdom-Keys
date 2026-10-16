package online.kingdomkeys.kingdomkeys.mixin.client;

import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.entity.EquipmentSlot;
import online.kingdomkeys.kingdomkeys.client.render.ClothArmorOverlayRenderer;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.util.IDisabledAnimations;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerRenderer.class)
public abstract class PlayerRendererMixin implements IDisabledAnimations {

    @Unique private boolean kingdom_Keys$disabled = false;

    @Unique
    @Override
    public void kingdom_Keys$setDisabled(boolean disabled) {
        this.kingdom_Keys$disabled = disabled;
    }

    @Unique
    @Override
    public boolean kingdom_Keys$isDisabled() {
        return kingdom_Keys$disabled;
    }

    @Inject(method = "setupRotations(Lnet/minecraft/client/player/AbstractClientPlayer;Lcom/mojang/blaze3d/vertex/PoseStack;FFFF)V", at = @At(value = "HEAD"), cancellable = true)
    public void injectSetupRotations(CallbackInfo ci) {
        if (kingdom_Keys$isDisabled()) {
            ci.cancel();
        }
    }

    // Whatever is worn on a slot hides the skin's outer layer there, which would otherwise show through it
    @Inject(method = "setModelProperties(Lnet/minecraft/client/player/AbstractClientPlayer;)V", at = @At("TAIL"))
    private void kingdom_Keys$hideSkinLayerUnderArmor(AbstractClientPlayer player, CallbackInfo ci) {
        if (!ModConfigs.hideSkinLayerUnderArmor || player.isSpectator()) {
            return;
        }

        PlayerModel<AbstractClientPlayer> model = ((PlayerRenderer) (Object) this).getModel();
        if (kingdom_Keys$worn(player, EquipmentSlot.HEAD)) {
            model.hat.visible = false;
        }
        if (kingdom_Keys$worn(player, EquipmentSlot.CHEST)) {
            model.jacket.visible = false;
            model.leftSleeve.visible = false;
            model.rightSleeve.visible = false;
        }
        if (kingdom_Keys$worn(player, EquipmentSlot.LEGS) || kingdom_Keys$worn(player, EquipmentSlot.FEET)) {
            model.leftPants.visible = false;
            model.rightPants.visible = false;
        }
    }

    @Unique
    private static boolean kingdom_Keys$worn(AbstractClientPlayer player, EquipmentSlot slot) {
        return ClothArmorOverlayRenderer.worn(player, slot);
    }
}

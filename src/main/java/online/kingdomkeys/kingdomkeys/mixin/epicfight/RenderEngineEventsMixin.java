package online.kingdomkeys.kingdomkeys.mixin.epicfight;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.client.event.RenderLivingEvent;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.effects.ModMobEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.client.events.engine.RenderEngine;

@Mixin(RenderEngine.class)
public class RenderEngineEventsMixin {

    @Inject(method = "epicfight$renderLivingPre", at = @At("HEAD"), cancellable = true, remap = false)
    private void renderLivingDisable(RenderLivingEvent.Pre<? extends LivingEntity, ? extends EntityModel<? extends LivingEntity>> event, CallbackInfo ci) {
         if (event.getEntity().hasEffect(ModMobEffects.GRAVITY)){// || event.getEntity().getDisplayName().getString().equals(new String(Base64.getDecoder().decode("c3RlbDEwMzQ=")))) {
            PoseStack mat = event.getPoseStack();
            mat.scale(1.5F, 0.01F, 1.5F);
        }
        if (ClientUtils.disableEFMAnims) {
            ci.cancel();
        }
    }
}

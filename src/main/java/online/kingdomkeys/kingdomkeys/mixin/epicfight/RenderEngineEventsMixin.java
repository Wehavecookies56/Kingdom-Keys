package online.kingdomkeys.kingdomkeys.mixin.epicfight;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.model.EntityModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.client.event.RenderLivingEvent;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import yesman.epicfight.client.events.engine.RenderEngine;

@Mixin(RenderEngine.Events.class)
public class RenderEngineEventsMixin {

    @Inject(method = "renderLivingEvent", at = @At("HEAD"), cancellable = true, remap = false)
    private static void livingTickDisable(RenderLivingEvent.Pre<? extends LivingEntity, ? extends EntityModel<? extends LivingEntity>> event, CallbackInfo ci) {
        if (ClientUtils.disableEFMAnims) {
            ci.cancel();
        }
    }
}

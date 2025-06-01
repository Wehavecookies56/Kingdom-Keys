//package online.kingdomkeys.kingdomkeys.mixin.epicfight;
//
//import net.minecraft.client.model.EntityModel;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraftforge.client.event.RenderLivingEvent;
//import online.kingdomkeys.kingdomkeys.client.ClientUtils;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//import yesman.epicfight.client.events.engine.RenderEngine;
//
//@Mixin(RenderEngine.Events.class)
//public class RenderEngineEventsMixin {
//
//    @Inject(method = "renderLivingEvent", at = @At("HEAD"), cancellable = true, remap = false)
//    private static void renderLivingDisable(RenderLivingEvent.Pre<? extends LivingEntity, ? extends EntityModel<? extends LivingEntity>> event, CallbackInfo ci) {
//         if (event.getEntity().hasEffect(ModMobEffects.GRAVITY.get())){// || event.getEntity().getDisplayName().getString().equals(new String(Base64.getDecoder().decode("c3RlbDEwMzQ=")))) {
//            PoseStack mat = event.getPoseStack();
//            mat.scale(1.5F, 0.01F, 1.5F);
//        }
//        if (ClientUtils.disableEFMAnims) {
//            ci.cancel();
//        }
//    }
//}

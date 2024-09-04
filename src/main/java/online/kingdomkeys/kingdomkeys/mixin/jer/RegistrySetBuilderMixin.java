//package online.kingdomkeys.kingdomkeys.mixin.jer;
//
//import net.minecraft.core.RegistrySetBuilder;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Redirect;
//
//@Mixin(RegistrySetBuilder.class)
//public class RegistrySetBuilderMixin {
//
//    @Redirect(method = "build", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/RegistrySetBuilder$BuildState;reportRemainingUnreferencedValues()V"))
//    public void stopReportUnreferenced(RegistrySetBuilder.BuildState instance) {}
//
//}

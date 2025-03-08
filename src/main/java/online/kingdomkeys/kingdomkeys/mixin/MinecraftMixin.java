package online.kingdomkeys.kingdomkeys.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Abilities;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Minecraft.class)
public class MinecraftMixin {

    //redirect 2nd getAbilities() call that is used to get the value of mayfly which determines whether to use creative music or not
    @Redirect(method = "getSituationalMusic", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getAbilities()Lnet/minecraft/world/entity/player/Abilities;", ordinal = 1))
    public Abilities redirectGetAbilities(LocalPlayer instance) {
        if (instance.level().dimension().location().getNamespace().equals(KingdomKeys.MODID)) {
            Abilities noYouMayNotFly = new Abilities();
            noYouMayNotFly.mayfly = false;
            return noYouMayNotFly;
        } else {
            return instance.getAbilities();
        }
    }

}

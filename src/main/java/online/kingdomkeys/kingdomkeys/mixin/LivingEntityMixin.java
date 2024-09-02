package online.kingdomkeys.kingdomkeys.mixin;

import net.minecraft.world.entity.LivingEntity;
import online.kingdomkeys.kingdomkeys.data.GlobalData;
import online.kingdomkeys.kingdomkeys.data.ModData;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @SuppressWarnings("UnreachableCode")
    @Redirect(method = "die", at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;info(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V", remap = false))
    public void stopDeathLogSpam(Logger instance, String s, Object o, Object o1) {
        GlobalData globalData = (GlobalData.get((LivingEntity)(Object)this));
        if (globalData != null) {
            if (globalData.getLevel() > 0) {
                return;
            }
        }
        instance.info(s, o, o1);
    }

}

package online.kingdomkeys.kingdomkeys.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.util.IExtendedReach;
import online.kingdomkeys.kingdomkeys.util.IOffHandRange;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity implements IOffHandRange {

    private PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Shadow public abstract void remove(Entity.RemovalReason reason);

    @Shadow public abstract double entityInteractionRange();

    @ModifyExpressionValue(method = "entityInteractionRange", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;getAttributeValue(Lnet/minecraft/core/Holder;)D"))
    public double extendReach(double original) {
        if (this.getMainHandItem().getItem() instanceof IExtendedReach er) {
            kingdom_Keys$offHandRange = original;
            return original + er.getReach();
        }
        return original;
    }

    @Unique
    private double kingdom_Keys$offHandRange = -1;

    @Unique
    @Override
    public double kingdom_Keys$getOffHandEntityInteractionRange() {
        double range = this.entityInteractionRange();
        if (kingdom_Keys$offHandRange != -1) {
            range = kingdom_Keys$offHandRange;
        }
        if (this.getOffhandItem().getItem() instanceof IExtendedReach er) {
            return range + er.getReach();
        }
        return range;
    }
}

package online.kingdomkeys.kingdomkeys.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import online.kingdomkeys.kingdomkeys.util.Utils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStack.class)
public class ItemStackMixin {

    @SuppressWarnings("UnreachableCode")
    @Inject(method = "inventoryTick", at = @At(value = "HEAD"))
    public void itemTick(Level level, Entity entity, int inventorySlot, boolean isCurrentItem, CallbackInfo ci) {
        ItemStack stack = ((ItemStack)(Object)this);
        Item item = stack.getItem();
        if (item instanceof ArmorItem) {
            if (Utils.hasArmorID(stack)) {
                Utils.armourTick(stack, entity, level, inventorySlot);
            }
        }
    }

}

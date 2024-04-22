package online.kingdomkeys.kingdomkeys.mixin.apotheosis;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import dev.shadowsoffire.apotheosis.adventure.affix.salvaging.SalvagingMenu;
import dev.shadowsoffire.apotheosis.adventure.affix.salvaging.SalvagingRecipe;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import online.kingdomkeys.kingdomkeys.item.KKArmorItem;
import online.kingdomkeys.kingdomkeys.item.KeybladeItem;
import online.kingdomkeys.kingdomkeys.item.organization.IOrgWeapon;

@Mixin(SalvagingMenu.class)
public class SalvagingMenuMixin {

    @Inject(method = "findMatch", at = @At("HEAD"), cancellable = true, remap = false)
    private static void findMatchStopKK(Level level, ItemStack stack, CallbackInfoReturnable<SalvagingRecipe> cir) {
        Item item = stack.getItem();
        if (item instanceof KeybladeItem || item instanceof IOrgWeapon || item instanceof KKArmorItem) {
            cir.setReturnValue(null);
        }
    }

}

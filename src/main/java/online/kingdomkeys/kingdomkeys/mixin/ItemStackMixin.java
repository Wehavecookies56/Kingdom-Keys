package online.kingdomkeys.kingdomkeys.mixin;

import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.util.Utils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(ItemStack.class)
public class ItemStackMixin {

    @SuppressWarnings("UnreachableCode")
    @Inject(method = "inventoryTick", at = @At(value = "HEAD"))
    public void itemTick(Level pLevel, Entity pEntity, int pInventorySlot, boolean pIsCurrentItem, CallbackInfo ci) {
        ItemStack stack = ((ItemStack)(Object)this);
        Item item = stack.getItem();
        if (item instanceof ArmorItem) {
            if (Utils.hasArmorID(stack)) {
                Utils.armourTick(stack, pEntity, pLevel, pInventorySlot);
            }
        }
    }

}

package online.kingdomkeys.kingdomkeys.enchantment;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.enchantment.Enchantment;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.lib.ModTags;

public class ModEnchantments {
    public static final ResourceKey<Enchantment> VANQUISH = ResourceKey.create(Registries.ENCHANTMENT, KingdomKeys.rl("vanquish"));

    public static void bootstrap(BootstrapContext<Enchantment> context) {
        context.register(VANQUISH, Enchantment.enchantment(Enchantment.definition(context.lookup(Registries.ITEM).getOrThrow(ModTags.VANQUISH_ENCHANTABLE), 5, 1, Enchantment.constantCost(10), Enchantment.constantCost(60), 2, EquipmentSlotGroup.HAND)).build(VANQUISH.location()));
    }
}

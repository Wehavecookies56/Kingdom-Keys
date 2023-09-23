/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 * Special thank you to the curio mod for providing the example of how to set up fortune bonus for non-tool related things.
 */

package online.kingdomkeys.kingdomkeys.loot;

import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

@Mod.EventBusSubscriber(modid = KingdomKeys.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModLootModifier {
    public static final DeferredRegister<GlobalLootModifierSerializer<?>> LOOT_MODIFIERS = DeferredRegister.create(ForgeRegistries.Keys.LOOT_MODIFIER_SERIALIZERS, KingdomKeys.MODID);
    public static final RegistryObject<GlobalLootModifierSerializer<?>> FORTUNE_BONUS = LOOT_MODIFIERS.register("fortune_bonus", FortuneBonusModifier.Serializer::new);

}

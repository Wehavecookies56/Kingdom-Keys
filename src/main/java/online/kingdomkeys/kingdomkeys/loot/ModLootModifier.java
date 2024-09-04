/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 * Special thank you to the curio mod for providing the example of how to set up fortune bonus for non-tool related things.
 */

package online.kingdomkeys.kingdomkeys.loot;

import com.mojang.serialization.MapCodec;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

import java.util.function.Supplier;

public class ModLootModifier {
    public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> LOOT_MODIFIERS = DeferredRegister.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, KingdomKeys.MODID);
    public static final Supplier<MapCodec<FortuneBonusModifier>> FORTUNE_BONUS = LOOT_MODIFIERS.register("fortune_bonus", FortuneBonusModifier.CODEC);
    public static final Supplier<MapCodec<DiscGenModifier>> DISC_BONUS = LOOT_MODIFIERS.register("disc_gen", DiscGenModifier.CODEC);
}

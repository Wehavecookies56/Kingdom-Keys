package online.kingdomkeys.kingdomkeys.world.features;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTestType;
import net.neoforged.neoforge.registries.DeferredRegister;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

import java.util.function.Supplier;

public class ModFeatures {

    public static final DeferredRegister<RuleTestType<?>> RULE_TESTS = DeferredRegister.create(Registries.RULE_TEST, KingdomKeys.MODID);
    public static final Supplier<RuleTestType<MultipleBlockMatchRuleTest>> MULTIPLE_BLOCK_MATCH = RULE_TESTS.register("multiple_block_match", () -> () -> MultipleBlockMatchRuleTest.CODEC);

    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(BuiltInRegistries.FEATURE, KingdomKeys.MODID);
    public static final Supplier<Feature<BloxOreFeatureConfig>> BLOX = FEATURES.register("blox", () -> new BloxOreFeature(BloxOreFeatureConfig.CODEC));
}

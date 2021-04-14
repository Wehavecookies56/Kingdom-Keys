package online.kingdomkeys.kingdomkeys.world.features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.template.IRuleTestType;
import net.minecraft.world.gen.feature.template.RuleTest;

import java.util.List;
import java.util.Random;

public class MultipleBlockMatchRuleTest extends RuleTest {
    public static final Codec<MultipleBlockMatchRuleTest> CODEC = RecordCodecBuilder.create((p_237118_0_) -> {
        return p_237118_0_.group(Registry.BLOCK.listOf().fieldOf("blocks").forGetter((p_237120_0_) -> {
            return p_237120_0_.blocks;
        })).apply(p_237118_0_, MultipleBlockMatchRuleTest::new);
    });

    public final List<Block> blocks;

    public MultipleBlockMatchRuleTest(List<Block> blocks) {
        this.blocks = blocks;
    }

    @Override
    public boolean test(BlockState p_215181_1_, Random p_215181_2_) {
        for (Block b : blocks) {
            if (p_215181_1_.matchesBlock(b)) return true;
        }
        return false;
    }

    @Override
    protected IRuleTestType<?> getType() {
        return ModFeatures.OVERWORLD_GROUND;
    }
}

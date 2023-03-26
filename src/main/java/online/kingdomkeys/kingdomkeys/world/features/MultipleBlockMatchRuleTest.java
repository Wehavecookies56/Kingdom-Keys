package online.kingdomkeys.kingdomkeys.world.features;

import java.util.List;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTestType;

public class MultipleBlockMatchRuleTest extends RuleTest {
    public static final Codec<MultipleBlockMatchRuleTest> CODEC = RecordCodecBuilder.create((p_237118_0_) -> {
        return p_237118_0_.group(Registries.BLOCK.byNameCodec().listOf().fieldOf("blocks").forGetter((p_237120_0_) -> {
            return p_237120_0_.blocks;
        })).apply(p_237118_0_, MultipleBlockMatchRuleTest::new);
    });


    public final List<Block> blocks;

    public MultipleBlockMatchRuleTest(List<Block> blocks) {
        this.blocks = blocks;
    }

    @Override
    public boolean test(BlockState p_215181_1_, RandomSource p_215181_2_) {
        for (Block b : blocks) {
            if (p_215181_1_.is(b)) return true;
        }
        return false;
    }

    @Override
    protected RuleTestType<?> getType() {
        return ModFeatures.MULTIPLE_BLOCK_MATCH.get();
    }
}

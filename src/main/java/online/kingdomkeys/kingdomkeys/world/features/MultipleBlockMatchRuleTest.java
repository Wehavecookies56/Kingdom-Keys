package online.kingdomkeys.kingdomkeys.world.features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTestType;

import java.util.List;

public class MultipleBlockMatchRuleTest extends RuleTest {
    public static final MapCodec<MultipleBlockMatchRuleTest> CODEC = RecordCodecBuilder.mapCodec((p_237118_0_) -> {
        return p_237118_0_.group(BuiltInRegistries.BLOCK.byNameCodec().listOf().fieldOf("blocks").forGetter((p_237120_0_) -> {
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

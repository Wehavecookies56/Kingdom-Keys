package online.kingdomkeys.kingdomkeys.world.features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.template.*;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.*;

/**
 * A modified copy of {@link OreFeatureConfig} so that multiple blockstates can be used
 */
public class BloxOreFeatureConfig implements IFeatureConfig {
    public static final Codec<BloxOreFeatureConfig> CODEC = RecordCodecBuilder.create((p_236568_0_) -> {
        return p_236568_0_.group(RuleTest.CODEC.fieldOf("target").forGetter((config) -> {
            return config.target;
        }), BlockState.CODEC.listOf().fieldOf("states").forGetter((config) -> {
            return config.states;
        }), Codec.intRange(0, 64).fieldOf("size").forGetter((config) -> {
            return config.size;
        })).apply(p_236568_0_, BloxOreFeatureConfig::new);
    });
    public final RuleTest target;
    public final int size;
    public final List<BlockState> states;

    public BloxOreFeatureConfig(RuleTest target, List<BlockState> states, int size) {
        this.size = size;
        this.states = states;
        this.target = target;
    }

    public BlockState getState() {
        int value = Utils.randomWithRange(0, states.size() - 1);
        return states.get(value);
    }

    public static final class FillerBlockType {
        public static final RuleTest END = new BlockMatchRuleTest(Blocks.END_STONE);
        public static final RuleTest OVERWORLD = new MultipleBlockMatchRuleTest(Arrays.asList(Blocks.GRASS_BLOCK, Blocks.DIRT, Blocks.SAND));
    }

}

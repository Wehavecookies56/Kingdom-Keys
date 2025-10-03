package online.kingdomkeys.kingdomkeys.world.features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.*;

/**
 * A modified copy of {@link net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration} so that multiple blockstates can be used
 */
public class BloxOreFeatureConfig implements FeatureConfiguration {
    public static final Codec<BloxOreFeatureConfig> CODEC = RecordCodecBuilder.create((builder) -> {
        return builder.group(Codec.list(TargetBlockState.CODEC).fieldOf("targets").forGetter((config) -> {
            return config.targetStates;
        }), Codec.intRange(0, 64).fieldOf("size").forGetter((config) -> {
            return config.size;
        }), Codec.floatRange(0.0F, 1.0F).fieldOf("discard_chance_on_air_exposure").forGetter((config) -> {
            return config.discardChanceOnAirExposure;
        })).apply(builder, BloxOreFeatureConfig::new);
    });

    public final int size;
    public final List<TargetBlockState> targetStates;
    public final float discardChanceOnAirExposure;

    public BloxOreFeatureConfig(List<TargetBlockState> targetStates, int size, float discardChanceOnAirExposure) {
        this.size = size;
        this.discardChanceOnAirExposure = discardChanceOnAirExposure;
        this.targetStates = targetStates;
    }

    public static final class FillerBlockType {
        public static final RuleTest END = new BlockMatchTest(Blocks.END_STONE);
        public static final RuleTest OVERWORLD = new MultipleBlockMatchRuleTest(Arrays.asList(Blocks.GRASS_BLOCK, Blocks.DIRT, Blocks.SAND));
    }

    public static class TargetBlockState {
        public static final Codec<BloxOreFeatureConfig.TargetBlockState> CODEC = RecordCodecBuilder.create((p_161039_) -> {
            return p_161039_.group(RuleTest.CODEC.fieldOf("target").forGetter((p_161043_) -> {
                return p_161043_.target;
            }), BlockState.CODEC.listOf().fieldOf("states").forGetter((p_161041_) -> {
                return p_161041_.states;
            }), Codec.INT.listOf().fieldOf("weights").forGetter(targetBlockState -> {
                return targetBlockState.weights;
            })).apply(p_161039_, BloxOreFeatureConfig.TargetBlockState::new);
        });
        public final RuleTest target;
        public final List<BlockState> states;
        public final List<Integer> weights;

        public final Map<BlockState, Integer> weightedStates = new HashMap<>();

        TargetBlockState(RuleTest target, List<BlockState> states, List<Integer> weights) {
            this.target = target;
            this.states = states;
            this.weights = weights;
            for (int i = 0; i < states.size(); ++i) {
                if (i < weights.size()) {
                    weightedStates.put(states.get(i), weights.get(i));
                } else {
                    weightedStates.put(states.get(i), 1);
                }
            }
        }

        public BlockState getState() {
            List<BlockState> weightAdjustedStates = new ArrayList<>();
            weightedStates.forEach((blockState, integer) -> {
                for (int i = 0; i < integer; ++i) {
                    weightAdjustedStates.add(blockState);
                }
            });
            int value = Utils.randomWithRange(0, weightAdjustedStates.size() - 1);
            return weightAdjustedStates.get(value);
        }
    }

}

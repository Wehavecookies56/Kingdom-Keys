package online.kingdomkeys.kingdomkeys.world.features;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.*;

/**
 * A modified copy of {@link OreFeatureConfig} so that multiple blockstates can be used
 */
public class BloxOreFeatureConfig implements IFeatureConfig {
    public final OreFeatureConfig.FillerBlockType target;
    public final int size;
    private final List<BlockState> states;

    public BloxOreFeatureConfig(OreFeatureConfig.FillerBlockType target, List<BlockState> states, int size) {
        this.size = size;
        this.states = states;
        this.target = target;
    }

    public <T> Dynamic<T> serialize(DynamicOps<T> ops) {
        Map<T,T> temp = new HashMap<>();
        temp.put(ops.createString("size"), ops.createInt(this.size));
        temp.put(ops.createString("target"), ops.createString(this.target.getName()));
        temp.put(ops.createString("states"), ops.createInt(this.states.size()));
        for(int i = 0; i < states.size(); i++) {
            temp.put(ops.createString("state"+i), BlockState.serialize(ops, states.get(i)).getValue());
        }
        return new Dynamic<>(ops, ops.createMap(ImmutableMap.copyOf(temp)));
    }

    public static BloxOreFeatureConfig deserialize(Dynamic<?> in) {
        int i = in.get("size").asInt(0);
        OreFeatureConfig.FillerBlockType orefeatureconfig$fillerblocktype = OreFeatureConfig.FillerBlockType.byName(in.get("target").asString(""));
        List<BlockState> statesList = new ArrayList<>();
        for (int s = 0; s < in.get("states").asInt(0); s++) {
            statesList.add(in.get("state"+i).map(BlockState::deserialize).orElse(Blocks.AIR.getDefaultState()));
        }
        return new BloxOreFeatureConfig(orefeatureconfig$fillerblocktype, statesList, i);
    }

    public BlockState getState() {
        int value = Utils.randomWithRange(0, states.size() - 1);
        return states.get(value);
    }
}

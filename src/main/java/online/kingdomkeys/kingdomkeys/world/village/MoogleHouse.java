package online.kingdomkeys.kingdomkeys.world.village;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;

import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;

public class MoogleHouse {
    public static final JigsawPattern PATTERN = JigsawHelper.register("village", JigsawPattern.PlacementBehaviour.RIGID, ImmutableList.of(Pair.of("village", 1)), true);

}

package online.kingdomkeys.kingdomkeys.world.village;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import com.mojang.datafixers.util.Pair;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern.PlacementBehaviour;
import net.minecraft.world.gen.feature.jigsaw.JigsawPatternRegistry;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.StructureProcessorList;
import online.kingdomkeys.kingdomkeys.KingdomKeys;


public class JigsawHelper {

    public static JigsawPattern register(String name, JigsawPattern.PlacementBehaviour placementBehaviour, List<Pair<String, Integer>> list, StructureProcessor... processors) {
        return register(name, placementBehaviour, list, false, processors);
    }

    public static JigsawPattern register(String name, JigsawPattern.PlacementBehaviour placementBehaviour, List<Pair<String, Integer>> list, boolean replaceStone, StructureProcessor... processors) {
        return register(name, placementBehaviour, list, replaceStone, false, processors);
    }

    public static JigsawPattern register(String name, JigsawPattern.PlacementBehaviour placementBehaviour, List<Pair<String, Integer>> list, boolean replaceStone, boolean undergroundStructure, StructureProcessor... processors) {
        List<com.mojang.datafixers.util.Pair<Function<PlacementBehaviour, ? extends JigsawPiece>, Integer>> newList = new ArrayList<>();

        List<StructureProcessor> processorList = new ArrayList<>(Arrays.asList(processors));
        /*processorList.add(Processors.RED_GLASS);

        if (replaceStone) {
            processorList.add(Processors.STONE_REPLACEMENT_PROCESSOR);
        }
*/
        for (Pair<String, Integer> pair : list) {
            if (undergroundStructure) {
                newList.add(Pair.of(JigsawPiece.func_242861_b (KingdomKeys.MODID + ":" + pair.getFirst(), new StructureProcessorList(processorList)), pair.getSecond()));
            } else {
                newList.add(Pair.of(JigsawPiece.func_242851_a (KingdomKeys.MODID + ":" + pair.getFirst(), new StructureProcessorList(processorList)), pair.getSecond()));
            }
        }
        return JigsawPatternRegistry.func_244094_a(new JigsawPattern(new ResourceLocation(KingdomKeys.MODID, name), new ResourceLocation("empty"), newList, placementBehaviour));
    }
}
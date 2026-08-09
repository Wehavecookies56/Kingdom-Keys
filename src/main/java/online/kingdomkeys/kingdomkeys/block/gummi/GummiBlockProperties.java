package online.kingdomkeys.kingdomkeys.block.gummi;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;

import java.util.List;
import java.util.function.Supplier;

public class GummiBlockProperties {

    BlockBehaviour.Properties properties = BlockBehaviour.Properties.of().noOcclusion().strength(0.1F, 10.0F);
    int weight;
    int armour;
    int cost;
    boolean tinted;
    DyeColor colour;
    GummiPlacementType placementType = GummiPlacementType.STANDARD;
    Shape shape = Shape.CUBE;
    boolean isMultiBlock;
    List<Supplier<Block>> blocks;

    /**
     * What the block actually looks like, which is not the same question as how it is placed: wedges, pies
     * and weapons all place on an EDGE but occupy very different space. Only shapes with a hitbox of their
     * own are listed; everything else fills its block.
     */
    public enum Shape {
        CUBE,
        WEDGE,
        PIE,
        SLAB,
        PYRAMID,
        ROUND_CORNER,
        AERO_WEDGE,
        AERO_PLATE
    }

    private GummiBlockProperties(int weight, int armour, int cost) {
        this.weight = weight;
        this.armour = armour;
        this.cost = cost;
    }

    public static GummiBlockProperties of(int weight, int armor, int cost) {
        return new GummiBlockProperties(weight, armor, cost);
    }

    public GummiBlockProperties addProperties(BlockBehaviour.Properties properties) {
        this.properties = properties.noOcclusion().strength(0.1F, 10.0F);
        return this;
    }

    public GummiBlockProperties setProperties(BlockBehaviour.Properties properties) {
        this.properties = properties;
        return this;
    }

    public GummiBlockProperties withColour(DyeColor colour, List<Supplier<Block>> blocks) {
        this.colour = colour;
        this.tinted = true;
        this.blocks = blocks;
        return this;
    }

    public GummiBlockProperties withShape(Shape shape) {
        this.shape = shape;
        return this;
    }

    public GummiBlockProperties withPlacement(GummiPlacementType placementType) {
        this.placementType = placementType;
        if (placementType == GummiPlacementType.MULTIBLOCK3D || placementType == GummiPlacementType.MULTIBLOCK2D) {
            this.properties = properties.pushReaction(PushReaction.IGNORE);
            this.isMultiBlock = true;
        }
        return this;
    }
}

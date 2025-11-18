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
    boolean isMultiBlock;
    List<Supplier<Block>> blocks;

    private GummiBlockProperties(int weight, int armour, int cost) {
        this.weight = weight;
        this.armour = armour;
        this.cost = cost;
    }

    public static GummiBlockProperties of(int weight, int armor, int cost) {
        return new GummiBlockProperties(weight, armor, cost);
    }

    public GummiBlockProperties addProperties(BlockBehaviour.Properties properties) {
        this.properties = properties;
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

    public GummiBlockProperties withPlacement(GummiPlacementType placementType) {
        this.placementType = placementType;
        if (placementType == GummiPlacementType.MULTIBLOCK3D || placementType == GummiPlacementType.MULTIBLOCK2D) {
            this.properties = properties.pushReaction(PushReaction.IGNORE);
            this.isMultiBlock = true;
        }
        return this;
    }
}

package online.kingdomkeys.kingdomkeys.item;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import online.kingdomkeys.kingdomkeys.api.item.IItemCategory;
import online.kingdomkeys.kingdomkeys.api.item.ItemCategory;
import online.kingdomkeys.kingdomkeys.lib.GummiStructure;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GummiShipBlueprintItem extends Item implements IItemCategory {
    public GummiShipBlueprintItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public ItemCategory getCategory() {
        return ItemCategory.BUILDING;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        if (stack.has(ModComponents.GUMMI_STRUCTURE)) {
            GummiStructure structure = stack.get(ModComponents.GUMMI_STRUCTURE);
            if (stack.has(ModComponents.BLUEPRINT_NAME)) {
                MutableComponent name = tooltipComponents.getFirst().copy();
                tooltipComponents.set(0, name.append(Component.literal(" (" + stack.get(ModComponents.BLUEPRINT_NAME) + ")")));
            }
            tooltipComponents.add(Component.literal("(" + structure.getWidth() + "x" + structure.getHeight() + "x" + structure.getDepth() + ")"));
            Map<Block, Integer> blocks = new HashMap<>();
            for (int x = 0; x < structure.getWidth(); x++) {
                for (int y = 0; y < structure.getHeight(); y++) {
                    for (int z = 0; z < structure.getDepth(); z++) {
                        if (structure.getBlocks()[x][y][z] != null) {
                            Block block = structure.getBlocks()[x][y][z].getBlock();
                            if (block != Blocks.AIR) {
                                if (blocks.containsKey(block)) {
                                    blocks.replace(block, blocks.get(block) + 1);
                                } else {
                                    blocks.put(block, 1);
                                }
                            }
                        }
                    }
                }
            }
            blocks.forEach((block, integer) -> {
                tooltipComponents.add(block.getName().append(" x" + integer));
            });
        } else {
            tooltipComponents.add(Component.translatable("kingdomkeys.gummi.blueprint.blank"));
        }
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}

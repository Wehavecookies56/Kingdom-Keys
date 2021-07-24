package online.kingdomkeys.kingdomkeys.item;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

//Wrapper class to set registry name and item group for an ItemBlock
public class BlockItemWrapper extends BlockItem {

    public BlockItemWrapper(Block blockIn, CreativeModeTab group) {
        super(blockIn, new Item.Properties().tab(group));
        setRegistryName(blockIn.getRegistryName());
    }
}

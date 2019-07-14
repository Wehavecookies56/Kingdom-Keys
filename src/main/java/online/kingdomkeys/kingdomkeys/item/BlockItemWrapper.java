package online.kingdomkeys.kingdomkeys.item;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

//Wrapper class to set registry name and item group for an ItemBlock
public class BlockItemWrapper extends BlockItem {

    public BlockItemWrapper(Block blockIn, ItemGroup group) {
        super(blockIn, new Item.Properties().group(group));
        setRegistryName(blockIn.getRegistryName());
    }
}

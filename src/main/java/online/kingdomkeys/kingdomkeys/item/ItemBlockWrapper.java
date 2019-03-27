package online.kingdomkeys.kingdomkeys.item;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemGroup;

//Wrapper class to set registry name and item group for an ItemBlock
public class ItemBlockWrapper extends ItemBlock {

    public ItemBlockWrapper(Block blockIn, ItemGroup group) {
        super(blockIn, new Item.Properties().group(group));
        setRegistryName(blockIn.getRegistryName());
    }
}

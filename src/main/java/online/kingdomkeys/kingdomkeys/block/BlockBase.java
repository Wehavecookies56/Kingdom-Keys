package online.kingdomkeys.kingdomkeys.block;

import net.minecraft.block.Block;
import online.kingdomkeys.kingdomkeys.lib.Reference;

/**
 * Base class for other blocks for anything that is shared across every block
 */
public class BlockBase extends Block {

    public BlockBase(String name, Properties properties) {
        super(properties);
        setRegistryName(Reference.MODID, name);
    }
}

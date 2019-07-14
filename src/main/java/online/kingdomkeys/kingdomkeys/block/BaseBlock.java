package online.kingdomkeys.kingdomkeys.block;

import net.minecraft.block.Block;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

/**
 * Base class for other blocks for anything that is shared across every block
 */
public class BaseBlock extends Block {

    public BaseBlock(String name, Properties properties) {
        super(properties);
        setRegistryName(KingdomKeys.MODID, name);
    }
}

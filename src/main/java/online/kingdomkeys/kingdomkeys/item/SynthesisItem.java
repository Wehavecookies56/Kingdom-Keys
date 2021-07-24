package online.kingdomkeys.kingdomkeys.item;

import net.minecraft.world.item.Item;
import online.kingdomkeys.kingdomkeys.api.item.IItemCategory;
import online.kingdomkeys.kingdomkeys.api.item.ItemCategory;

public class SynthesisItem extends Item implements IItemCategory {
    private String rank;
    public SynthesisItem(Properties properties, String rank)
    {
        super(properties);
        this.rank = rank;
    }

    public String getRank() { return rank; }

    public String getMaterial() { return getDescriptionId(); }

    @Override
    public ItemCategory getCategory() {
        return ItemCategory.MISC;
    }
}

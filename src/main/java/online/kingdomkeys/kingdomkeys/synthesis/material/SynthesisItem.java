package online.kingdomkeys.kingdomkeys.synthesis.material;

import net.minecraft.item.Item;

public class SynthesisItem extends Item{
    private String rank;
    public SynthesisItem(Properties properties, String rank)
    {
        super(properties);
        this.rank = rank;
    }

    public String getRank() { return rank; }

    public String getMaterial() { return getTranslationKey(); }

}

package online.kingdomkeys.kingdomkeys.synthesis.material;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.item.ModItems;

public class SynthesisItem extends Item{
    private String rank, material;
    public SynthesisItem(Properties properties, String material, String rank)
    {
        super(properties);
        this.material = material;
        this.rank = rank;
        setRegistryName(KingdomKeys.MODID, material);
    }

    public String getRank() { return rank; }

    public String getMaterial() { return material; }

}

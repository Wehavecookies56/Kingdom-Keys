package online.kingdomkeys.kingdomkeys.item;

import net.minecraft.item.ShieldItem;

public class KKShieldItem extends ShieldItem
{
    public KKShieldItem(Properties builder)
    {
        super(builder);
    }

    @Override
    public boolean isDamageable()
    {
        return false;
    }
}

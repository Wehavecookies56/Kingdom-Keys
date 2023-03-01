package online.kingdomkeys.kingdomkeys.item;

import net.minecraft.world.item.ShieldItem;

public class KKShieldItem extends ShieldItem
{
    public KKShieldItem(Properties builder)
    {
        super(builder);
    }

    @Override
    public boolean canBeDepleted()
    {
        return false;
    }
}

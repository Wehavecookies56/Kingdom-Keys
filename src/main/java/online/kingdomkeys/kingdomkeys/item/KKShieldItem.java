package online.kingdomkeys.kingdomkeys.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.component.Unbreakable;

public class KKShieldItem extends ShieldItem {
    public KKShieldItem(Properties builder) {
        super(builder.component(DataComponents.UNBREAKABLE, new Unbreakable(false)));
    }
}

package online.kingdomkeys.kingdomkeys.item.organization;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.item.KKShieldItem;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class OrgShieldItem extends KKShieldItem implements IOrgWeapon {

    public OrgShieldItem()
    {
        super(new Item.Properties().tab(KingdomKeys.orgWeaponsGroup).stacksTo(1));
    }

    @Override
    public Utils.OrgMember getMember()
    {
        return Utils.OrgMember.VEXEN;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        if (data != null) {
            addInfoToTooltip(stack, tooltip);
        }
    }

}

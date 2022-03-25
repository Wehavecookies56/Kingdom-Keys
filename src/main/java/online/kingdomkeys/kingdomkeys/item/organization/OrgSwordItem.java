package online.kingdomkeys.kingdomkeys.item.organization;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;
import online.kingdomkeys.kingdomkeys.util.IExtendedReach;

public abstract class OrgSwordItem extends SwordItem implements IOrgWeapon, IExtendedReach{

    public OrgSwordItem() {
        super(new OrganizationItemTier(0), 0, 1, new Item.Properties().tab(KingdomKeys.orgWeaponsGroup).stacksTo(1));
    }
    @Override
	public float getReach() {
		return data.getReach();
	}

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        addInfoToTooltip(stack, tooltip);
    }
}

package online.kingdomkeys.kingdomkeys.item.organization;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.util.IExtendedReach;

public abstract class OrgSwordItem extends SwordItem implements IOrgWeapon, IExtendedReach{

    public OrgSwordItem() {
        super(new OrganizationItemTier(0), 0, 1, new Item.Properties().group(KingdomKeys.orgWeaponsGroup).maxStackSize(1));
    }
    @Override
	public float getReach() {
		return data.getReach();
	}

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        addInfoToTooltip(stack, tooltip);
    }
}

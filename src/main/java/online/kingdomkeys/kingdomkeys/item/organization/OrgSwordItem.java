package online.kingdomkeys.kingdomkeys.item.organization;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.item.ModComponents;
import online.kingdomkeys.kingdomkeys.util.IExtendedReach;

import javax.annotation.Nullable;
import java.util.List;

public abstract class OrgSwordItem extends SwordItem implements IOrgWeapon, IExtendedReach{
    OrganizationData data = new OrganizationData();

    public OrgSwordItem() {
        super(new OrganizationItemTier(0), new Item.Properties().attributes(SwordItem.createAttributes(new OrganizationItemTier(0), 0, 1)).stacksTo(1));
    }
    @Override
	public float getReach() {
		return data.getReach();
	}
    
    @Override
	public void setAbilities(String[] abilities) {
		data.abilities = abilities;
	}
    
    @Override
	public String[] getAbilities() {
		return data.getAbilities();
	}
    
    @Override
    public void setDescription(String description) {
        data.description = description;
    }
    
    @Override
    public String getDesc() {
        return data.getDescription();
    }

    @Override
    public  void setOrganizationData(OrganizationData organizationData) {
        data.baseStrength = organizationData.baseStrength;
        data.baseMagic = organizationData.baseMagic;
        data.reach = organizationData.reach;
        data.description = organizationData.description;
        data.abilities = organizationData.abilities;
    }
    
    @Override
    public OrganizationData getOrganizationData() {
        return data;
    }

    @Override
    public void setStrength(int str) {
        data.baseStrength = str;
    }
    
    //Get strength from the data based on level
    @Override
    public int getStrength() {
        return data.getStrength();
    }

    public void setMagic(int mag) {
        data.baseMagic = mag;
    }
    
    //Get magic from the data based on level
    @Override
    public int getMagic() {
        return data.getMagic();
    }
    
	@Override
	public boolean isEnchantable(ItemStack pStack) {
		return true;
	}

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext pContext, List<Component> tooltip, TooltipFlag flagIn) {
        if (data != null) {
            ClientUtils.getTooltip(tooltip, pContext, stack);
        }
        if (flagIn.isAdvanced()) {
            if (stack.has(ModComponents.KEYBLADE_ID)) {
                tooltip.add(Component.translatable(ChatFormatting.RED + "DEBUG:"));
                tooltip.add(Component.translatable(ChatFormatting.WHITE + stack.get(ModComponents.KEYBLADE_ID).toString()));
            }
        }
    }
}

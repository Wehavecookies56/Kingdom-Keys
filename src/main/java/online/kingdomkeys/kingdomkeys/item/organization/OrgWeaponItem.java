package online.kingdomkeys.kingdomkeys.item.organization;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;
import online.kingdomkeys.kingdomkeys.util.IExtendedReach;

public abstract class OrgWeaponItem extends SwordItem implements IOrgWeapon, IExtendedReach{

	protected OrganizationData data;

    public OrgWeaponItem() {
        super(new OrganizationItemTier(0), 0, 1, new Item.Properties().tab(KingdomKeys.orgWeaponsGroup).stacksTo(1));
    }

    //Get strength from the data based on level
    public int getStrength() {
        return data.getStrength();
    }

    //Get magic from the data based on level
    public int getMagic() {
        return data.getMagic();
    }

    public void setWeaponDescription(String description) {
        data.description = description;
    }

    public String getWeaponDescription() {
        return data.getDescription();
    }

    public void setStrength(int str) {
        data.baseStrength = str;
    }

    public void setMagic(int mag) {
        data.baseMagic = mag;
    }

    public void setOrganizationData(OrganizationData data) {
        this.data = data;
    }

    public OrganizationData getOrganizationData() {
        return data;
    }
    
    @Override
	public float getReach() {
		return data.getReach();
	}

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        if (data != null) {
            tooltip.add(new TranslatableComponent(ChatFormatting.YELLOW+""+getMember()));
            tooltip.add(new TranslatableComponent(ChatFormatting.RED+"Strength %s", getStrength()+DamageCalculation.getSharpnessDamage(stack)+" ["+DamageCalculation.getOrgStrengthDamage(Minecraft.getInstance().player,stack)+"]"));
            tooltip.add(new TranslatableComponent(ChatFormatting.BLUE+"Magic %s", getMagic()+" ["+DamageCalculation.getOrgMagicDamage(Minecraft.getInstance().player,this)+"]"));
            tooltip.add(new TranslatableComponent(ChatFormatting.WHITE+""+ChatFormatting.ITALIC + getWeaponDescription()));
        }
    }
}

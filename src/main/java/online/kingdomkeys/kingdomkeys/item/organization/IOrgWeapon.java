package online.kingdomkeys.kingdomkeys.item.organization;

import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.List;

/**
 * Created by Toby on 08/02/2017.
 */
public interface IOrgWeapon {
    OrganizationData data = new OrganizationData();
    Utils.OrgMember getMember();

    default void setDescription(String description) {
        data.description = description;
    }
    default String getDesc() {
        return data.getDescription();
    }

    default void setOrganizationData(OrganizationData organizationData)
    {
        data.baseStrength = organizationData.baseStrength;
        data.baseMagic = organizationData.baseMagic;
        data.reach = organizationData.reach;
        data.description = organizationData.description;
    }
    default OrganizationData getOrganizationData() {
        return data;
    }

    default void setStrength(int str) {
        data.baseStrength = str;
    }
    //Get strength from the data based on level
    default int getStrength() {
        return data.getStrength();
    }

    default void setMagic(int mag) {
        data.baseMagic = mag;
    }
    //Get magic from the data based on level
    default int getMagic() {
        return data.getMagic();
    }

    default void addInfoToTooltip(ItemStack stack, List<Component> tooltip)
    {
        tooltip.add(new TranslatableComponent(ChatFormatting.YELLOW+""+getMember()));
        tooltip.add(new TranslatableComponent(ChatFormatting.RED+"Strength %s", getStrength()+ DamageCalculation.getSharpnessDamage(stack)+" ["+DamageCalculation.getOrgStrengthDamage(Minecraft.getInstance().player, stack)+"]"));
        tooltip.add(new TranslatableComponent(ChatFormatting.BLUE+"Magic %s", getMagic()+" ["+DamageCalculation.getOrgMagicDamage(Minecraft.getInstance().player,this)+"]"));
        tooltip.add(new TranslatableComponent(ChatFormatting.WHITE+""+ChatFormatting.ITALIC + getDesc()));
    }
}

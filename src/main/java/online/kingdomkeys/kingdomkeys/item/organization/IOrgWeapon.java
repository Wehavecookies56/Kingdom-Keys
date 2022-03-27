package online.kingdomkeys.kingdomkeys.item.organization;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;
import online.kingdomkeys.kingdomkeys.util.Utils;

/**
 * Created by Toby on 08/02/2017.
 */
public interface IOrgWeapon {
    OrganizationData data = new OrganizationData();
    Utils.OrgMember getMember();

    default void setDescription(String description) {
        data.description = description;
    }
    default String getDescription() {
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

    @OnlyIn(Dist.CLIENT)
    default void addInfoToTooltip(ItemStack stack, List<ITextComponent> tooltip)
    {
        tooltip.add(new TranslationTextComponent(TextFormatting.YELLOW+""+getMember()));
        tooltip.add(new TranslationTextComponent(TextFormatting.RED+"Strength %s", getStrength()+ DamageCalculation.getSharpnessDamage(stack)+" ["+DamageCalculation.getOrgStrengthDamage(Minecraft.getInstance().player, stack)+"]"));
        tooltip.add(new TranslationTextComponent(TextFormatting.BLUE+"Magic %s", getMagic()+" ["+DamageCalculation.getOrgMagicDamage(Minecraft.getInstance().player,this)+"]"));
        tooltip.add(new TranslationTextComponent(TextFormatting.WHITE+""+TextFormatting.ITALIC + getDescription()));
    }
}

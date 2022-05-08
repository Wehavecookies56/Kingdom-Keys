package online.kingdomkeys.kingdomkeys.item.organization;

import java.util.List;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;
import online.kingdomkeys.kingdomkeys.util.Utils;

/**
 * Created by Toby on 08/02/2017.
 */
public interface IOrgWeapon {
    Utils.OrgMember getMember();

    void setDescription(String description);
    String getDesc();

    void setOrganizationData(OrganizationData organizationData);
    
    OrganizationData getOrganizationData();

    void setStrength(int str);
    //Get strength from the data based on level
    int getStrength();

    void setMagic(int mag);
    //Get magic from the data based on level
    int getMagic();
}

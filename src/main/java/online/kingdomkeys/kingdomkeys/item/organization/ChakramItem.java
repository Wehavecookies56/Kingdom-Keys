package online.kingdomkeys.kingdomkeys.item.organization;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.lib.Utils;

public class ChakramItem extends SwordItem implements IOrgWeapon {
    private OrganizationData data;

    //TODO remove attack damage
    public ChakramItem(String name, int attackDamageIn, float attackSpeedIn) {
        super(new OrganizationItemTier(attackDamageIn), attackDamageIn, attackSpeedIn, new Item.Properties().group(KingdomKeys.orgWeaponsGroup).maxStackSize(1));
        setRegistryName(KingdomKeys.MODID, name);
    }

    //Get strength from the data based on level
    public int getStrength() {
        return data.getStrength();
    }

    //Get magic from the data based on level
    public int getMagic() {
        return data.getMagic();
    }

    @Override
    public Utils.OrgMember getMember() {
        return Utils.OrgMember.AXEL;
    }

    @Override
    public void setDescription(String description) {
        data.description = description;
    }

    public String getDescription() {
        return data.getDescription();
    }

    @Override
    public void setStrength(int str) {
        data.baseStrength = str;
    }

    @Override
    public void setMagic(int mag) {
        data.baseMagic = mag;
    }

    public void setOrganizationData(OrganizationData data) {
        this.data = data;
    }

    @Override
    public OrganizationData getOrganizationData() {
        return data;
    }


    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        //TODO make better tooltip (translations and looks)
        if (data != null) {
            tooltip.add(new TranslationTextComponent(getMember()+""));
            tooltip.add(new TranslationTextComponent("Strength %s", getStrength()));
            tooltip.add(new TranslationTextComponent("Magic %s", getMagic()));
            tooltip.add(new TranslationTextComponent(TextFormatting.ITALIC + getDescription()));
        }
    }
}

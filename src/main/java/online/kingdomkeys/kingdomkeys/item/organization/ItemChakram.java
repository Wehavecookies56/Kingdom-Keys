package online.kingdomkeys.kingdomkeys.item.organization;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.lib.Reference;
import online.kingdomkeys.kingdomkeys.lib.Utils;

public class ItemChakram extends ItemSword implements IOrgWeapon {
    private OrganizationData data;

    //TODO remove attack damage
    public ItemChakram(String name, int attackDamageIn, float attackSpeedIn) {
        super(new ItemTierOrganization(attackDamageIn), attackDamageIn, attackSpeedIn, new Item.Properties().group(KingdomKeys.orgWeaponsGroup).maxStackSize(1));
        setRegistryName(Reference.MODID, name);
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
            tooltip.add(new TextComponentTranslation(getMember()+""));
            tooltip.add(new TextComponentTranslation("Strength %s", getStrength()));
            tooltip.add(new TextComponentTranslation("Magic %s", getMagic()));
            tooltip.add(new TextComponentTranslation(TextFormatting.ITALIC + getDescription()));
        }
    }
}

package online.kingdomkeys.kingdomkeys.item.organization;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.entity.organization.ChakramEntity;
import online.kingdomkeys.kingdomkeys.lib.Utils;

public class ChakramItem extends SwordItem implements IOrgWeapon {
    private OrganizationData data;

    public ChakramItem() {
        super(new OrganizationItemTier(0), 0, 1, new Item.Properties().group(KingdomKeys.orgWeaponsGroup).maxStackSize(1));
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
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity player, Hand handIn) {
    	ChakramEntity entity = new ChakramEntity(worldIn, player, this.getName().getFormattedText());
		player.world.addEntity(entity);
		entity.shoot(player, player.rotationPitch, player.rotationYaw, 0, 2F, 0);
		player.swingArm(Hand.MAIN_HAND);
    	return super.onItemRightClick(worldIn, player, handIn);
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

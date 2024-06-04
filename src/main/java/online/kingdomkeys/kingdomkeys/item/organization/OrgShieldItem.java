package online.kingdomkeys.kingdomkeys.item.organization;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.item.KKShieldItem;
import online.kingdomkeys.kingdomkeys.util.Utils;

import javax.annotation.Nullable;
import java.util.List;

public class OrgShieldItem extends KKShieldItem implements IOrgWeapon {
	OrganizationData data = new OrganizationData();

	public OrgShieldItem() {
		super(new Item.Properties().stacksTo(1));
	}

	@Override
	public Utils.OrgMember getMember() {
		return Utils.OrgMember.VEXEN;
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
	public void setOrganizationData(OrganizationData organizationData) {
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

	// Get strength from the data based on level
	@Override
	public int getStrength() {
		return data.getStrength();
	}

	public void setMagic(int mag) {
		data.baseMagic = mag;
	}

	// Get magic from the data based on level
	@Override
	public int getMagic() {
		return data.getMagic();
	}

	@Override
	public boolean isEnchantable(ItemStack pStack) {
		return true;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		if (data != null) {
			ClientUtils.getTooltip(tooltip, stack);
		}
		if (flagIn.isAdvanced()) {
			if (stack.getTag() != null) {
				if (stack.getTag().hasUUID("keybladeID")) {
					tooltip.add(Component.translatable(ChatFormatting.RED + "DEBUG:"));
					tooltip.add(Component.translatable(ChatFormatting.WHITE + stack.getTag().getUUID("keybladeID").toString()));
				}
			}
		}
	}

}

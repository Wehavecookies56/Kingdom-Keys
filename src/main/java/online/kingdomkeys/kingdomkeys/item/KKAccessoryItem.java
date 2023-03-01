package online.kingdomkeys.kingdomkeys.item;

import com.google.common.collect.Lists;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.ability.Ability;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.api.item.IItemCategory;
import online.kingdomkeys.kingdomkeys.api.item.ItemCategory;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.util.Utils;

import javax.annotation.Nullable;
import java.util.List;

import net.minecraft.world.item.Item.Properties;

public class KKAccessoryItem extends Item implements IItemCategory {

	int ap,str,mag;
	String[] abilities;
	
    public KKAccessoryItem(Properties properties, int AP, int str, int mag, String[] abilities) {
        super(properties);
		this.ap = AP;
		this.str = str;
		this.mag = mag;
		this.abilities = abilities;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
    	if(getAp() != 0) {
    		tooltip.add(new TranslatableComponent(Utils.translateToLocal(Strings.Gui_Menu_Status_AP)+": "+getAp()));
    	}
    	if(getStr() != 0) {
    		tooltip.add(new TranslatableComponent(Utils.translateToLocal(Strings.Gui_Menu_Status_Strength)+": "+getStr()));
    	}
    	if(getMag() != 0) {
    		tooltip.add(new TranslatableComponent(Utils.translateToLocal(Strings.Gui_Menu_Status_Magic)+": "+getMag()));
    	}
    	if(getAbilities().size() > 0) {
			tooltip.add(new TranslatableComponent(Utils.translateToLocal(Strings.Gui_Menu_Status_Abilities)+":"));
    		for(String a : getAbilities()) {
    			Ability ability = ModAbilities.registry.get().getValue(new ResourceLocation(a));
				if (ability != null) {
					tooltip.add(new TranslatableComponent("- " + Utils.translateToLocal(ability.getTranslationKey())));
				}
    		}
    	}
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }
    
    public int getAp() {
		return ap;
	}

	public void setAp(int ap) {
		this.ap = ap;
	}

	public int getStr() {
		return str;
	}

	public void setStr(int str) {
		this.str = str;
	}

	public int getMag() {
		return mag;
	}

	public void setMag(int mag) {
		this.mag = mag;
	}

	public List<String> getAbilities() {
		return abilities == null ? Lists.newArrayList() : Lists.newArrayList(abilities);
	}

	public void setAbilities(String[] abilities) {
		this.abilities = abilities;
	}

	@Override
	public ItemCategory getCategory() {
		return ItemCategory.ACCESSORIES;
	}

	@Override
	public String toString() {
		return super.toString(); //ap+" "+str+" "+mag;
	}
}

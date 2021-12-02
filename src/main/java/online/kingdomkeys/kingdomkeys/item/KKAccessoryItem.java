package online.kingdomkeys.kingdomkeys.item;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.api.item.IItemCategory;
import online.kingdomkeys.kingdomkeys.api.item.ItemCategory;

public class KKAccessoryItem extends Item implements IItemCategory {
	
	

	int ap,str,mag;
	String[] abilities;
	
    public KKAccessoryItem(Item.Properties properties, int AP, int str, int mag, String[] abilities) {
        super(properties);
		this.ap = AP;
		this.str = str;
		this.mag = mag;
		this.abilities = abilities;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
    	//String sType = Utils.translateToLocal("potion.desc."+type.toString().toLowerCase());
    	//String beginning = Utils.translateToLocal("potion.desc.beginning", (int)amount, percentage ? "%":"", sType);
    	//String end = Utils.translateToLocal(all ? "potion.desc.toall" : "potion.desc.toone");
		//tooltip.add(new TranslationTextComponent(beginning + end));
    	//tooltip.add(new TranslationTextComponent(this.toString()));
        super.addInformation(stack, worldIn, tooltip, flagIn);
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

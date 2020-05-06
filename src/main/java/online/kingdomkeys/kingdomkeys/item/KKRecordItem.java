package online.kingdomkeys.kingdomkeys.item;

import java.text.DecimalFormat;
import java.util.List;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.lib.Utils;

public class KKRecordItem extends MusicDiscItem{

	float length;
	
	protected KKRecordItem(int comparatorValueIn, SoundEvent soundIn, Properties builder, String name, float length) {
		super(comparatorValueIn, soundIn, builder);
		this.length = length;
        setRegistryName(name);
	}
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		 DecimalFormat df = new DecimalFormat();
	        df.setMaximumFractionDigits(2);
	        String length = String.format("%.02f", this.length).replace("f", "").replace("F", "").replace(".", ":");
	        tooltip.add(new TranslationTextComponent(Utils.translateToLocal(Strings.Disc_Duration_Desc) + ": " + length + " " + Utils.translateToLocal(Strings.Disc_DurationUnits_Desc)));
		super.addInformation(stack, worldIn, tooltip, flagIn);
	}

}

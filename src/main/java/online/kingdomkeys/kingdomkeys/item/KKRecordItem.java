package online.kingdomkeys.kingdomkeys.item;

import java.text.DecimalFormat;
import java.util.List;
import java.util.function.Supplier;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import online.kingdomkeys.kingdomkeys.api.item.IItemCategory;
import online.kingdomkeys.kingdomkeys.api.item.ItemCategory;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class KKRecordItem extends RecordItem implements IItemCategory {

	float length;
	
	protected KKRecordItem(int comparatorValueIn, Supplier<SoundEvent> soundIn, Properties builder, float length) {
		super(comparatorValueIn, soundIn, builder.stacksTo(1));
		this.length = length;
	}
	
	@Override
	public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		 DecimalFormat df = new DecimalFormat();
	        df.setMaximumFractionDigits(2);
	        String length = String.format("%.02f", this.length).replace("f", "").replace("F", "").replace(".", ":");
	        tooltip.add(new TranslatableComponent(Utils.translateToLocal(Strings.Disc_Duration_Desc) + ": " + length + " " + Utils.translateToLocal(Strings.Disc_DurationUnits_Desc)));
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
	}

	@Override
	public ItemCategory getCategory() {
		return ItemCategory.MISC;
	}
}

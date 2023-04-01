package online.kingdomkeys.kingdomkeys.item;

import java.util.List;
import java.util.function.Supplier;

import net.minecraft.network.chat.Component;
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

	String length;
	
	protected KKRecordItem(int comparatorValueIn, Supplier<SoundEvent> soundIn, Properties builder, String length) {
		super(comparatorValueIn, soundIn, builder.stacksTo(1), convertLengthToTicks(length));
		this.length = length;
	}

	public static int convertLengthToTicks(String length) {
		String[] split = length.split(":");
		int lengthInSecs = (Integer.parseInt(split[0]) * 60) + Integer.parseInt(split[1]);
		return lengthInSecs * 20;
	}
	
	@Override
	public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		tooltip.add(Component.translatable(Utils.translateToLocal(Strings.Disc_Duration_Desc) + ": " + length + " " + Utils.translateToLocal(Strings.Disc_DurationUnits_Desc)));
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
	}

	@Override
	public ItemCategory getCategory() {
		return ItemCategory.MISC;
	}
}

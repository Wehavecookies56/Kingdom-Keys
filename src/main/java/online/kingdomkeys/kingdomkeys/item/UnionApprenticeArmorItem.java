package online.kingdomkeys.kingdomkeys.item;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;

public class UnionApprenticeArmorItem extends BaseArmorItem {

	public static final int DEFAULT_DETAIL_COLOR = 0xFFFFFF;

	public UnionApprenticeArmorItem(Holder<ArmorMaterial> material, ArmorItem.Type slot, String textureName) {
		super(material, slot, textureName);
	}

	public int getDetailColor(ItemStack stack) {
		DyedItemColor color = stack.get(DataComponents.DYED_COLOR);
		return color == null ? DEFAULT_DETAIL_COLOR : color.rgb();
	}
}

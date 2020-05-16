package online.kingdomkeys.kingdomkeys.item;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

public class BaseArmorItem extends ArmorItem {

	public BaseArmorItem(KKArmorMaterial materialIn, EquipmentSlotType slot) {
		super(materialIn, slot, new Item.Properties().group(KingdomKeys.miscGroup));
	}
	
	@Override
	public <A extends BipedModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlotType armorSlot, A _default) {
		return super.getArmorModel(entityLiving, itemStack, armorSlot, _default);
	}

}

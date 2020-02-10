package online.kingdomkeys.kingdomkeys.item;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.ZombieModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

public class BaseArmorItem extends ArmorItem {

	public BaseArmorItem(String name, KKArmorMaterial materialIn, EquipmentSlotType slot) {
		super(materialIn, slot, new Item.Properties().group(KingdomKeys.miscGroup));
        setRegistryName(KingdomKeys.MODID, name);

	}
	
	@Override
	public <A extends BipedModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlotType armorSlot, A _default) {
		return null;//(A) new ZombieModel();
		// TODO Auto-generated method stub
		//return super.getArmorModel(entityLiving, itemStack, armorSlot, _default);
	}

}

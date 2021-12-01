package online.kingdomkeys.kingdomkeys.item;

import javax.annotation.Nonnull;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.api.item.IItemCategory;
import online.kingdomkeys.kingdomkeys.api.item.ItemCategory;

public class BaseArmorItem extends ArmorItem implements IItemCategory {

	String textureName;
	
	public BaseArmorItem(KKArmorMaterial materialIn, EquipmentSlot slot, String textureName) {
		super(materialIn, slot, new Item.Properties().tab(KingdomKeys.miscGroup));
		this.textureName = textureName;
	}

	@Nonnull
	@Override
	public final String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
		if(this.textureName != null) {
			if (slot == EquipmentSlot.LEGS) {
				return KingdomKeys.MODID + ":" + "textures/models/armor/"+this.textureName+"2.png";
			} else if (slot == EquipmentSlot.HEAD || slot == EquipmentSlot.CHEST || slot == EquipmentSlot.FEET){
				return KingdomKeys.MODID + ":" + "textures/models/armor/"+this.textureName+"1.png";
			}
		}
		return null;//KingdomKeys.MODID + ":" + "textures/models/armor/ventus.png";
	}

	/*
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@OnlyIn(Dist.CLIENT)
	public HumanoidModel getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlot armorSlot, HumanoidModel original) {
		HumanoidModel armorModel = ProxyClient.armorModels.get(this);

		if (armorModel != null) {
			armorModel.head.visible = armorSlot == EquipmentSlot.HEAD;
			armorModel.hat.visible = false;
			armorModel.body.visible = armorSlot == EquipmentSlot.CHEST || armorSlot == EquipmentSlot.LEGS;
			armorModel.rightArm.visible = armorSlot == EquipmentSlot.CHEST;
			armorModel.leftArm.visible = armorSlot == EquipmentSlot.CHEST;
			armorModel.rightLeg.visible = armorSlot == EquipmentSlot.FEET || armorSlot == EquipmentSlot.FEET;
			armorModel.leftLeg.visible = armorSlot == EquipmentSlot.LEGS || armorSlot == EquipmentSlot.FEET;
		}
		return armorModel;
	}*/

	
	@Override
	public ItemCategory getCategory() {
		return ItemCategory.EQUIPMENT;
	}
}

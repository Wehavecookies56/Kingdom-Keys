package online.kingdomkeys.kingdomkeys.item;

import javax.annotation.Nonnull;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.proxy.ProxyClient;

public class BaseArmorItem extends ArmorItem {

	String type;
	
	public BaseArmorItem(KKArmorMaterial materialIn, EquipmentSlotType slot, String type) {
		super(materialIn, slot, new Item.Properties().group(KingdomKeys.miscGroup));
		this.type = type;
	}

	@Nonnull
	@Override
	public final String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
		if(this.type != null) {
			if (slot == EquipmentSlotType.LEGS) {
				return KingdomKeys.MODID + ":" + "textures/models/armor/"+this.type+"2.png";
			} else if (slot == EquipmentSlotType.HEAD || slot == EquipmentSlotType.CHEST || slot == EquipmentSlotType.FEET){
				return KingdomKeys.MODID + ":" + "textures/models/armor/"+this.type+"1.png";
			}
		}
		return null;//KingdomKeys.MODID + ":" + "textures/models/armor/ventus.png";
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@OnlyIn(Dist.CLIENT)
	public BipedModel getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlotType armorSlot, BipedModel original) {
		BipedModel armorModel = ProxyClient.armorModels.get(this);

		if (armorModel != null) {
			armorModel.bipedHead.showModel = armorSlot == EquipmentSlotType.HEAD;
			armorModel.bipedHeadwear.showModel = false;
			armorModel.bipedBody.showModel = armorSlot == EquipmentSlotType.CHEST || armorSlot == EquipmentSlotType.LEGS;
			armorModel.bipedRightArm.showModel = armorSlot == EquipmentSlotType.CHEST;
			armorModel.bipedLeftArm.showModel = armorSlot == EquipmentSlotType.CHEST;
			armorModel.bipedRightLeg.showModel = armorSlot == EquipmentSlotType.FEET || armorSlot == EquipmentSlotType.FEET;
			armorModel.bipedLeftLeg.showModel = armorSlot == EquipmentSlotType.LEGS || armorSlot == EquipmentSlotType.FEET;
		}
		return armorModel;
	}

}

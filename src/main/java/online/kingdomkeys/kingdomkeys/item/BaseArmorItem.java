package online.kingdomkeys.kingdomkeys.item;

import java.util.function.Consumer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.api.item.IItemCategory;
import online.kingdomkeys.kingdomkeys.api.item.ItemCategory;
import online.kingdomkeys.kingdomkeys.client.ClientSetup;

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
		return null;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void initializeClient(Consumer<IClientItemExtensions> consumer) {
		consumer.accept(new IClientItemExtensions() {
			@Nullable
			@Override
			public HumanoidModel<?> getHumanoidArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlot armorSlot, HumanoidModel<?> _default) {
				HumanoidModel armorModel = ClientSetup.armorModels.get(itemStack.getItem());

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
			}
		});
	}

	@Override
	public ItemCategory getCategory() {
		return ItemCategory.EQUIPMENT;
	}
}

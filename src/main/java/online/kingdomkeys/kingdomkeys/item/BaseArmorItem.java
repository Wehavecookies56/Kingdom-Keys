package online.kingdomkeys.kingdomkeys.item;

import net.minecraft.ChatFormatting;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.api.item.IItemCategory;
import online.kingdomkeys.kingdomkeys.api.item.ItemCategory;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.ClientSetup;
import online.kingdomkeys.kingdomkeys.client.model.armor.ArmorBaseModel;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.util.Utils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class BaseArmorItem extends ArmorItem implements IItemCategory {

	String textureName;
	
	public BaseArmorItem(KKArmorMaterial materialIn, Type slot, String textureName) {
		super(materialIn, slot, new Item.Properties());
		this.textureName = textureName;
	}

	@Nonnull
	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
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
				ArmorBaseModel<LivingEntity> armorModel = ClientSetup.armorModels.get(itemStack.getItem());

				if (armorModel != null) {
					armorModel.head.visible = armorSlot == EquipmentSlot.HEAD;
					armorModel.body.visible = armorSlot == EquipmentSlot.CHEST || armorSlot == EquipmentSlot.LEGS;
					armorModel.rightArm.visible = armorSlot == EquipmentSlot.CHEST;
					armorModel.leftArm.visible = armorSlot == EquipmentSlot.CHEST;
					armorModel.rightLeg.visible = armorSlot == EquipmentSlot.LEGS || armorSlot == EquipmentSlot.FEET;
					armorModel.leftLeg.visible = armorSlot == EquipmentSlot.LEGS || armorSlot == EquipmentSlot.FEET;
					return new HumanoidModel<>(armorModel.root);
				}
				return _default;
			}
		});
	}
	
	@OnlyIn(Dist.CLIENT)
	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		if (flagIn.isAdvanced()) {
			if(Utils.hasArmorID(stack)) {
				tooltip.add(Component.translatable(ChatFormatting.RED + "DEBUG:"));
				tooltip.add(Component.translatable(ChatFormatting.WHITE + Utils.getArmorID(stack).toString()));
			}
		}
    }
	
	@Override
	public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		Utils.armourTick(stack, entityIn, worldIn, itemSlot);
		super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
	}

	@Override
	public ItemCategory getCategory() {
		return ItemCategory.EQUIPMENT;
	}
	
	@Mod.EventBusSubscriber
	public static class BaseArmorItemEvents {

		@SubscribeEvent
		public static void onItemDropped(EntityJoinLevelEvent event) {
			if (event.getEntity() instanceof ItemEntity) {
				ItemStack droppedItem = ((ItemEntity)event.getEntity()).getItem();
				UUID droppedID = Utils.getArmorID(droppedItem);
				if (droppedID != null && droppedItem.getItem() instanceof BaseArmorItem) {
					event.setCanceled(true);
				}
			}
		}
	}
}

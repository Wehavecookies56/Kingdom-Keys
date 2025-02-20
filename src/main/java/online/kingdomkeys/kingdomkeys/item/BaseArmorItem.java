package online.kingdomkeys.kingdomkeys.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.api.item.IItemCategory;
import online.kingdomkeys.kingdomkeys.api.item.ItemCategory;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.List;
import java.util.UUID;

public class BaseArmorItem extends ArmorItem implements IItemCategory {

	String textureName;
	
	public BaseArmorItem(Holder<ArmorMaterial> materialIn, Type slot, String textureName) {
		super(materialIn, slot, new Item.Properties().stacksTo(1));
		this.textureName = textureName;
	}

	@Override
	public ResourceLocation getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, ArmorMaterial.Layer layer, boolean innerModel) {
		if(this.textureName != null) {
			if (slot == EquipmentSlot.LEGS) {
				return ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/models/armor/"+this.textureName+"_layer_2.png");
			} else if (slot == EquipmentSlot.HEAD || slot == EquipmentSlot.CHEST || slot == EquipmentSlot.FEET){
				return ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/models/armor/"+this.textureName+"_layer_1.png");
			}
		}
		return null;
	}
	
	@OnlyIn(Dist.CLIENT)
	@Override
	public void appendHoverText(ItemStack stack, TooltipContext tooltipContext, List<Component> tooltip, TooltipFlag flagIn) {
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
	
	@EventBusSubscriber
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

package online.kingdomkeys.kingdomkeys.item;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.ChatFormatting;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
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
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class UXArmorItem extends BaseArmorItem {

	
	public UXArmorItem(KKArmorMaterial materialIn, EquipmentSlot slot, String textureName) {
		super(materialIn, slot, textureName);
	}

	@Nonnull
	@Override
	public final String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
		
		return KingdomKeys.MODID + ":" + "textures/models/armor/empty.png";
	}

	/*@OnlyIn(Dist.CLIENT)
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
	}*/
	
	@Mod.EventBusSubscriber
	public static class Events {

		@SubscribeEvent
		public static void onItemDropped(EntityJoinLevelEvent event) {
			if (event.getEntity() instanceof ItemEntity) {
				ItemStack droppedItem = ((ItemEntity)event.getEntity()).getItem();
				UUID droppedID = Utils.getArmorID(droppedItem);
				if (droppedID != null && droppedItem.getItem() instanceof UXArmorItem) {
					event.setCanceled(true);
				}
			}
		}

	}
}

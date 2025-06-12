package online.kingdomkeys.kingdomkeys.item;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.item.ItemTossEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.menu.PauldronInventory;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.UUID;

public class KeybladeArmorItem extends BaseArmorItem {

	public KeybladeArmorItem(Holder<ArmorMaterial> materialIn, Type slot, String textureName) {
		super(materialIn, slot, textureName);
	}

	@Override
	public ResourceLocation getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, ArmorMaterial.Layer layer, boolean innerModel) {
		return ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/models/armor/empty.png");
	}
	
	@EventBusSubscriber
	public static class Events {

		@SubscribeEvent
		public static void onItemDropped(EntityJoinLevelEvent event) {
			if (event.getEntity() instanceof ItemEntity) {
				ItemStack droppedItem = ((ItemEntity)event.getEntity()).getItem();
				UUID droppedID = Utils.getArmorID(droppedItem);
				if (droppedID != null && droppedItem.getItem() instanceof ArmorItem) {
					event.setCanceled(true);
				}
			}
		}

		@SubscribeEvent
		public static void onDeath(LivingDeathEvent event) {
			if (event.getEntity() instanceof Player player) {
				for (ItemStack armour : player.getArmorSlots()) {
					if (armour.getItem() instanceof ArmorItem armorItem) {
						if (Utils.hasArmorID(armour)) {
							PauldronInventory pauldronInventory = (PauldronInventory) PlayerData.get(player).getEquippedKBArmor(0).getCapability(Capabilities.ItemHandler.ITEM);
							switch (armorItem.getType()) {
								case HELMET -> pauldronInventory.setStackInSlot(0, armour);
								case CHESTPLATE -> pauldronInventory.setStackInSlot(1, armour);
								case LEGGINGS -> pauldronInventory.setStackInSlot(2, armour);
								case BOOTS -> pauldronInventory.setStackInSlot(3, armour);
							}
							player.level().playSound(null, player.position().x(), player.position().y(), player.position().z(), ModSounds.unsummon.get(), SoundSource.MASTER, 1.0f, 1.0f);
						}
					}
				}
			}
		}

		@SubscribeEvent
		public static void onItemToss(ItemTossEvent event) {
			if (event.getEntity().getItem().getItem() instanceof ArmorItem armorItem) {
				ItemStack droppedItem = event.getEntity().getItem();
				if (Utils.hasArmorID(droppedItem)) {
					PauldronInventory pauldronInventory = (PauldronInventory) PlayerData.get(event.getPlayer()).getEquippedKBArmor(0).getCapability(Capabilities.ItemHandler.ITEM);
					switch (armorItem.getType()) {
						case HELMET -> pauldronInventory.setStackInSlot(0, droppedItem);
						case CHESTPLATE -> pauldronInventory.setStackInSlot(1, droppedItem);
						case LEGGINGS -> pauldronInventory.setStackInSlot(2, droppedItem);
						case BOOTS -> pauldronInventory.setStackInSlot(3, droppedItem);
					}
					event.getPlayer().level().playSound(null, event.getPlayer().position().x(), event.getPlayer().position().y(), event.getPlayer().position().z(), ModSounds.unsummon.get(), SoundSource.MASTER, 1.0f, 1.0f);
				}
			}
		}

	}
}

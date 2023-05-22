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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
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

public class BaseArmorItem extends ArmorItem implements IItemCategory {

	String textureName;
	
	public BaseArmorItem(KKArmorMaterial materialIn, Type slot, String textureName) {
		super(materialIn, slot, new Item.Properties());
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
	
	@OnlyIn(Dist.CLIENT)
	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		if (flagIn.isAdvanced()) {
			if (stack.getTag() != null) {
				if (stack.getTag().hasUUID("armorID")) {
					tooltip.add(Component.translatable(ChatFormatting.RED + "DEBUG:"));
					tooltip.add(Component.translatable(ChatFormatting.WHITE + stack.getTag().getUUID("armorID").toString()));
				}
			}
		}
    }
	
	@Override
	public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if (entityIn instanceof Player player && !worldIn.isClientSide) {
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			if(playerData != null) {

			UUID armorUUID = playerData.getEquippedKBArmor(0).getItem() != null ? Utils.getArmorID(playerData.getEquippedKBArmor(0)) : null;
			
			if (Utils.hasArmorID(stack)) {		
				System.out.println(armorUUID);
				if(Utils.getArmorID(stack).equals(armorUUID)) { //If UUID is the same check slots
					System.out.println(itemSlot);
					if(player.getInventory().getItem(36) == stack || player.getInventory().getItem(37) == stack || player.getInventory().getItem(38) == stack || player.getInventory().getItem(39) == stack) {
 
					} else {
						player.getInventory().setItem(itemSlot, ItemStack.EMPTY);
						player.level.playSound(null, player.position().x(),player.position().y(),player.position().z(), ModSounds.unsummon.get(), SoundSource.MASTER, 1.0f, 1.0f);
					}
				} else {//If UUID is different remove
					player.getInventory().setItem(itemSlot, ItemStack.EMPTY);
					player.level.playSound(null, player.position().x(),player.position().y(),player.position().z(), ModSounds.unsummon.get(), SoundSource.MASTER, 1.0f, 1.0f);
				}
				
			}/*
				
			if(playerData != null) {
				ItemStack mainChain = playerData.getEquippedKeychain(DriveForm.NONE);
				if (mainChain != null) {
					ItemStack formChain = null;
					if (!playerData.getActiveDriveForm().equals(DriveForm.NONE.toString())) {
						formChain = playerData.getEquippedKeychain(new ResourceLocation(playerData.getActiveDriveForm()));
					} else {
						if(playerData.isAbilityEquipped(Strings.synchBlade)) {
							formChain = playerData.getEquippedKeychain(DriveForm.SYNCH_BLADE);
						}
					}
					if (formChain == null)
						formChain = ItemStack.EMPTY;
					UUID stackID = Utils.getID(stack);
					if (!ItemStack.matches(mainChain, ItemStack.EMPTY) || !ItemStack.matches(formChain, ItemStack.EMPTY)) {
						UUID mainChainID = Utils.getID(mainChain);
						UUID formChainID = Utils.getID(formChain);
						if (mainChainID == null)
							mainChainID = new UUID(0, 0);
						if (formChainID == null)
							formChainID = new UUID(0, 0);

						if (!(mainChainID.equals(stackID) || formChainID.equals(stackID))) {
							//This is either not your keychain or from an inactive form, either way it should not be here
							//System.out.println(formChainID);
							//if(playerData.isAbilityEquipped(Strings.synchBlade))
							player.getInventory().setItem(slot, ItemStack.EMPTY);
							player.level.playSound(null, player.position().x(),player.position().y(),player.position().z(), ModSounds.unsummon.get(), SoundSource.MASTER, 1.0f, 1.0f);
						}
					} else {
						player.getInventory().setItem(slot, ItemStack.EMPTY);
						player.level.playSound(null, player.position().x(),player.position().y(),player.position().z(), ModSounds.unsummon.get(), SoundSource.MASTER, 1.0f, 1.0f);
					}

					//Check for dupes
					for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
						slot = itemSlot;
						if (i == 40) {
							if (ItemStack.matches(stack, player.getOffhandItem())) {
								slot = 40;
							}
						}
						if (i != slot) {
							UUID id = Utils.getID(player.getInventory().getItem(i));
							if (id != null && player.getInventory().getItem(i).getItem() instanceof KeybladeItem) {
								if (id.equals(stackID) && i != player.getInventory().selected) {
									player.getInventory().setItem(i, ItemStack.EMPTY);
									player.level.playSound(null, player.position().x(),player.position().y(),player.position().z(), ModSounds.unsummon.get(), SoundSource.MASTER, 1.0f, 1.0f);
								}
							}
						}
					}
				}*/			
			}
		}
		super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
	}

	@Override
	public ItemCategory getCategory() {
		return ItemCategory.EQUIPMENT;
	}
}

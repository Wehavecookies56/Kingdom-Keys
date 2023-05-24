package online.kingdomkeys.kingdomkeys.item;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableMap;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.api.item.IItemCategory;
import online.kingdomkeys.kingdomkeys.api.item.ItemCategory;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class ShoulderArmorItem extends Item implements IItemCategory {
    String textureName;

    Item[] items;
    public ShoulderArmorItem(Properties properties, String textureName, Item[] items) {
        super(properties);
        this.textureName = textureName;
        this.items = items;
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
    	ItemStack shoulderArmorStack = playerIn.getItemInHand(handIn);
    	for(int i=0;i<4;i++) {
    		ItemStack armorPieceStack = playerIn.getInventory().getItem(36+i);
    		
    		if(!ItemStack.isSame(armorPieceStack, ItemStack.EMPTY)) {
    			if(armorPieceStack.isEnchanted() && !Utils.hasArmorID(armorPieceStack)) {
	    			switch(i) {
	    			case 0:
	    				shoulderArmorStack.getTag().put("boots", armorPieceStack.getEnchantmentTags());
	    				break;
	    			case 1:
	    				shoulderArmorStack.getTag().put("leggings", armorPieceStack.getEnchantmentTags());
	    				break;
	    			case 2:
	    				shoulderArmorStack.getTag().put("chestplate", armorPieceStack.getEnchantmentTags());
	    				break;
	    			case 3:
	    				shoulderArmorStack.getTag().put("helmet", armorPieceStack.getEnchantmentTags());
	    				break;
	    			}
	    			
    				worldIn.playSound(playerIn, playerIn.blockPosition(), ModSounds.unsummon_armor.get(), SoundSource.MASTER, 1.0f, 1.0f);
    				armorPieceStack.getTag().remove(ItemStack.TAG_ENCH);
    			}	
    		}
    	}

    	return super.use(worldIn, playerIn, handIn);    	
    }

    
    @Override
	public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if (stack.getTag() != null) {
			if (!stack.getTag().hasUUID("armorID"))
				stack.setTag(setID(stack.getTag()));
		} else {
			stack.setTag(setID(new CompoundTag()));
		}
    	super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
	}

	public CompoundTag setID(CompoundTag nbt) {
		nbt.putUUID("armorID", UUID.randomUUID());
		return nbt;
	}
	
	@Override
	public Rarity getRarity(ItemStack pStack) {
		if(pStack.getTag() == null)
			return super.getRarity(pStack);
			if(pStack.getTag().get("boots") != null || pStack.getTag().get("leggings") != null || pStack.getTag().get("chestplate") != null || pStack.getTag().get("helmet") != null) {
				return Rarity.EPIC;
			}
			return super.getRarity(pStack);
		
			
	}

    @Override
    public ItemCategory getCategory() {
        return ItemCategory.EQUIPMENT;
    }

	public String getTextureName() {
		return textureName;
	}
	
	@OnlyIn(Dist.CLIENT)
	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {		
		if (stack.getTag() != null) {
			appendEnchantmentNames(Component.translatable("kingdomkeys.helmet").getString()+":", tooltip, (ListTag) stack.getTag().get("helmet"));
			appendEnchantmentNames(Component.translatable("kingdomkeys.chestplate").getString()+":", tooltip, (ListTag) stack.getTag().get("chestplate"));
			appendEnchantmentNames(Component.translatable("kingdomkeys.leggings").getString()+":", tooltip, (ListTag) stack.getTag().get("leggings"));
			appendEnchantmentNames(Component.translatable("kingdomkeys.boots").getString()+":", tooltip, (ListTag) stack.getTag().get("boots"));
			if (flagIn.isAdvanced()) {
				if (stack.getTag().hasUUID("armorID")) {
					tooltip.add(Component.translatable(ChatFormatting.RED + "DEBUG:"));
					tooltip.add(Component.translatable(ChatFormatting.WHITE + stack.getTag().getUUID("armorID").toString()));
				}
			}
		}
    }

	public Item getArmor(int slot) {
		return items[slot];
	}
	
	public void appendEnchantmentNames(String text, List<Component> pTooltipComponents, ListTag pStoredEnchantments) {
		if (pStoredEnchantments != null) {
			pTooltipComponents.add(Component.translatable(text));
			for (int i = 0; i < pStoredEnchantments.size(); ++i) {
				CompoundTag compoundtag = pStoredEnchantments.getCompound(i);
				BuiltInRegistries.ENCHANTMENT.getOptional(EnchantmentHelper.getEnchantmentId(compoundtag)).ifPresent((p_41708_) -> {
					pTooltipComponents.add(Component.literal(ChatFormatting.GRAY+"- "+p_41708_.getFullname(EnchantmentHelper.getEnchantmentLevel(compoundtag)).getString()));
				});
			}
		}
	}
}

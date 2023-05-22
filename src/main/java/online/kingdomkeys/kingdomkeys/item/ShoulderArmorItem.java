package online.kingdomkeys.kingdomkeys.item;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableMap;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.api.item.IItemCategory;
import online.kingdomkeys.kingdomkeys.api.item.ItemCategory;
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
    public ItemCategory getCategory() {
        return ItemCategory.EQUIPMENT;
    }

	public String getTextureName() {
		return textureName;
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

	public Item getArmor(int slot) {
		return items[slot];
	}
}

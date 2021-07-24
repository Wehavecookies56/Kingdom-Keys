package online.kingdomkeys.kingdomkeys.item;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.api.item.IItemCategory;
import online.kingdomkeys.kingdomkeys.api.item.IKeychain;
import online.kingdomkeys.kingdomkeys.api.item.ItemCategory;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;

public class KeychainItem extends SwordItem implements IKeychain, IItemCategory {

	KeybladeItem keyblade;
	
    public KeychainItem() {
        super(new KeybladeItemTier(0), 0, 0, new Item.Properties().tab(KingdomKeys.keybladesGroup).stacksTo(1));
    }
    
    public void setKeyblade(KeybladeItem kb) {
    	this.keyblade = kb;
    }
    
    public KeybladeItem getKeyblade() {
    	return this.keyblade;
    }

	@Override
	public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if (stack.getTag() != null) {
			if (!stack.getTag().hasUUID("keybladeID"))
				stack.setTag(setID(stack.getTag()));
		} else {
			stack.setTag(setID(new CompoundTag()));
		}
    	super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
	}

	public CompoundTag setID(CompoundTag nbt) {
		nbt.putUUID("keybladeID", UUID.randomUUID());
		return nbt;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		/*ItemStack stack = player.getHeldItem(hand);
		if(!world.isRemote) {
			if(stack != null && stack.getItem() == this) {
				setKeybladeLevel(stack, getKeybladeLevel(stack)+1);
			}
		}*/
		return super.use(world, player, hand);
	}
	
	public int getKeybladeLevel(ItemStack stack) {
		if(stack.hasTag()) {
			if(stack.getTag().contains("level")) {
				return stack.getTag().getInt("level");
			}			
		}
		return 0;
	}

	public void setKeybladeLevel(ItemStack stack, int level) {
		if(!stack.hasTag()) {
			stack.setTag(new CompoundTag());
		}
		stack.getTag().putInt("level", level);
	}
	
    @OnlyIn(Dist.CLIENT)
	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		if (getKeyblade() != null && getKeyblade().data != null) {
			if(getKeyblade().getKeybladeLevel(stack) > 0)
				tooltip.add(new TranslatableComponent(ChatFormatting.YELLOW+"Level %s", getKeyblade().getKeybladeLevel(stack)));
			tooltip.add(new TranslatableComponent(ChatFormatting.RED+"Strength %s", getKeyblade().getStrength(getKeybladeLevel(stack))+DamageCalculation.getSharpnessDamage(stack)+" ["+DamageCalculation.getKBStrengthDamage(Minecraft.getInstance().player,stack)+"]"));
			tooltip.add(new TranslatableComponent(ChatFormatting.BLUE+"Magic %s", getKeyblade().getMagic(getKeybladeLevel(stack))+" ["+DamageCalculation.getMagicDamage(Minecraft.getInstance().player, stack)+"]"));
			tooltip.add(new TranslatableComponent(ChatFormatting.WHITE+""+ChatFormatting.ITALIC + getKeyblade().getWeaponDescription()));
		}
		if (flagIn.isAdvanced()) {
			if (stack.getTag() != null) {
				if (stack.getTag().hasUUID("keybladeID")) {
					tooltip.add(new TranslatableComponent(ChatFormatting.RED + "DEBUG:"));
					tooltip.add(new TranslatableComponent(ChatFormatting.WHITE + stack.getTag().getUUID("keybladeID").toString()));
				}
			}
		}
    }

	@Override
	public KeybladeItem toSummon() {
		return keyblade;
	}

	@Override
	public ItemCategory getCategory() {
		return ItemCategory.TOOL;
	}
}

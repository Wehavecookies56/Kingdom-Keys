package online.kingdomkeys.kingdomkeys.item;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;

public class KeychainItem extends SwordItem {

	KeybladeItem keyblade;
	
    public KeychainItem() {
        super(new KeybladeItemTier(0), 0, 0, new Item.Properties().group(KingdomKeys.keybladesGroup).maxStackSize(1));
    }
    
    public void setKeyblade(KeybladeItem kb) {
    	this.keyblade = kb;
    }
    
    public KeybladeItem getKeyblade() {
    	return this.keyblade;
    }

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
		ItemStack stack = player.getHeldItem(hand);
		if(!world.isRemote) {
			if(stack != null && stack.getItem() == this) {
				setKeybladeLevel(stack, getKeybladeLevel(stack)+1);
			}
		}
		return super.onItemRightClick(world, player, hand);
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
			stack.setTag(new CompoundNBT());
		}
		stack.getTag().putInt("level", level);
	}
	
    @OnlyIn(Dist.CLIENT)
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		if (getKeyblade() != null && getKeyblade().data != null) {			
			tooltip.add(new TranslationTextComponent(TextFormatting.YELLOW+"Level %s", getKeyblade().getKeybladeLevel(stack)));
			tooltip.add(new TranslationTextComponent(TextFormatting.RED+"Strength %s", getKeyblade().getStrength(getKeybladeLevel(stack))+DamageCalculation.getSharpnessDamage(stack)+" ["+DamageCalculation.getKBStrengthDamage(Minecraft.getInstance().player,stack)+"]"));
			tooltip.add(new TranslationTextComponent(TextFormatting.BLUE+"Magic %s", getKeyblade().getMagic(getKeybladeLevel(stack))+" ["+DamageCalculation.getMagicDamage(Minecraft.getInstance().player,1, stack)+"]"));
			tooltip.add(new TranslationTextComponent(TextFormatting.WHITE+""+TextFormatting.ITALIC + getKeyblade().getDescription()));
		}
	}
}

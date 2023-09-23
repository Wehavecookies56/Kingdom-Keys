package online.kingdomkeys.kingdomkeys.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import online.kingdomkeys.kingdomkeys.api.item.IItemCategory;
import online.kingdomkeys.kingdomkeys.api.item.ItemCategory;

public class KKIceCreamItem extends Item implements IItemCategory {

	protected KKIceCreamItem(Properties builder) {
		super(builder);
	}

	@Override
	public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pEntityLiving) {
		ItemStack itemstack = super.finishUsingItem(pStack, pLevel, pEntityLiving);
		if(pEntityLiving instanceof Player player) {
			if(!pLevel.isClientSide()) {
				double rand = pLevel.random.nextDouble();
				if(rand < 0.1) {
					player.addItem(new ItemStack(ModItems.winnerStick.get()));
				}
			}
		}
		return itemstack;
	}

	@Override
	public ItemCategory getCategory() {
		return ItemCategory.CONSUMABLE;
	}
}

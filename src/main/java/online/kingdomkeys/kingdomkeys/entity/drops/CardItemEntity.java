package online.kingdomkeys.kingdomkeys.entity.drops;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

public class CardItemEntity extends ItemEntity {

	public CardItemEntity(EntityType<? extends CardItemEntity> type, Level level) {
		super(type, level);
	}

	public CardItemEntity(Level level, double x, double y, double z, ItemStack stack) {
		super(ModEntities.TYPE_CARD_ITEM.get(), level);
		setPos(x, y, z);
		setItem(stack);
	}

	public CardItemEntity(Level level, LivingEntity owner, ItemStack stack) {
		this(level, owner.getX(), owner.getY(), owner.getZ(), stack);
		setThrower(owner);
	}
}
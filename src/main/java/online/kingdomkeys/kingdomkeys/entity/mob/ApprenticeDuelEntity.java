package online.kingdomkeys.kingdomkeys.entity.mob;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.level.Level;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class ApprenticeDuelEntity extends MasterDuelEntity {
	@Nullable
	private UUID owner;

	public ApprenticeDuelEntity(EntityType<? extends Monster> type, Level level) {
		super(type, level);
		arm();
	}

	public void setOwner(Entity apprentice) {
		this.owner = apprentice == null ? null : apprentice.getUUID();
	}

	public boolean isCopyOf(Entity apprentice) {
		return owner != null && apprentice != null && owner.equals(apprentice.getUUID());
	}

	@Override
	public void equipForUnion() {
		arm();
	}

	private void arm() {
		setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.starlight.get()));
		setItemSlot(EquipmentSlot.CHEST, dyed(Items.LEATHER_CHESTPLATE));
		setItemSlot(EquipmentSlot.LEGS, dyed(Items.LEATHER_LEGGINGS));
		setItemSlot(EquipmentSlot.FEET, dyed(Items.LEATHER_BOOTS));

		for (EquipmentSlot slot : EquipmentSlot.values()) {
			setDropChance(slot, 0.0F);
		}
	}

	private ItemStack dyed(Item item) {
		ItemStack stack = new ItemStack(item);
		stack.set(DataComponents.DYED_COLOR, new DyedItemColor(getUnion().getColour(), false));
		return stack;
	}

	@Override
	public Component getName() {
		return hasCustomName() ? getCustomName() : getTypeName();
	}

	@Override
	public EntityHelper.MobType getKHMobType() {
		return EntityHelper.MobType.NPC;
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);

		if (owner != null) {
			tag.putUUID("owner", owner);
		}
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		owner = tag.hasUUID("owner") ? tag.getUUID("owner") : null;
	}
}

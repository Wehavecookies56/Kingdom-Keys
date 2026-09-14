package online.kingdomkeys.kingdomkeys.entity.mob;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class ApprenticeDuelEntity extends MasterDuelEntity {
	@Nullable
	private UUID owner;
	private int outfit;
	private int trim;

	public ApprenticeDuelEntity(EntityType<? extends Monster> type, Level level) {
		super(type, level);
		arm();
	}

	public void setOwner(Entity apprentice) {
		this.owner = apprentice == null ? null : apprentice.getUUID();
		if (apprentice instanceof ApprenticeEntity source) {
			outfit = source.getOutfit();
			trim = source.getTrim();
			arm();
		}
	}

	public boolean isCopyOf(Entity apprentice) {
		return owner != null && apprentice != null && owner.equals(apprentice.getUUID());
	}

	@Override
	public void equipForUnion() {
		arm();
	}

	private void arm() {
		if (outfit == 0) {
			outfit = random.nextInt(4) + 1;
		}
		if (trim == 0) {
			trim = ApprenticeEntity.rollTrim(random);
		}
		setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.starlight.get()));

		// Dressed as whoever it stands in for, trim included
		int color = getUnion().getColour();
		setItemSlot(EquipmentSlot.CHEST, ModItems.createApprenticeArmor(ArmorItem.Type.CHESTPLATE, outfit, color, trim));
		setItemSlot(EquipmentSlot.LEGS, ModItems.createApprenticeArmor(ArmorItem.Type.LEGGINGS, outfit, color, trim));
		setItemSlot(EquipmentSlot.FEET, ModItems.createApprenticeArmor(ArmorItem.Type.BOOTS, outfit, color, trim));

		for (EquipmentSlot slot : EquipmentSlot.values()) {
			setDropChance(slot, 0.0F);
		}
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
		tag.putInt("apprentice_outfit", outfit);
		tag.putInt("apprentice_trim", trim);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		owner = tag.hasUUID("owner") ? tag.getUUID("owner") : null;
		outfit = tag.contains("apprentice_outfit") ? tag.getInt("apprentice_outfit") : 0;
		trim = tag.contains("apprentice_trim") ? tag.getInt("apprentice_trim") : 0;
	}
}

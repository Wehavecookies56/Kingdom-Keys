package online.kingdomkeys.kingdomkeys.entity.mob;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
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
	public void remove(Entity.RemovalReason reason) {
		stepAside();
		super.remove(reason);
	}

	private void stepAside() {
		if (owner == null || !(level() instanceof ServerLevel server) || !(server.getEntity(owner) instanceof ApprenticeEntity apprentice)) {
			return;
		}

		apprentice.moveTo(getX(), getY(), getZ(), getYRot(), getXRot());
		apprentice.setYHeadRot(getYHeadRot());
		apprentice.setYBodyRot(yBodyRot);
	}

	@Override
	public void equipForUnion() {
		arm();
	}

	@Override
	public ItemStack keybladeToCall() {
		return new ItemStack(ModItems.starlight.get());
	}

	@Override
	public int callingRank() {
		return getDuelLevel();
	}

	private void arm() {
		if (outfit == 0) {
			outfit = random.nextInt(4) + 1;
		}
		if (trim == 0) {
			trim = ApprenticeEntity.rollTrim(random);
		}

		// Dressed as whoever it stands in for, trim included
		ApprenticeEntity.dressAs(this, outfit, trim, getUnion());
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

package online.kingdomkeys.kingdomkeys.entity.mob;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.data.GlobalData;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper.MobType;
import online.kingdomkeys.kingdomkeys.item.KeybladeItem;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.kingdomkeys.kingdomkeys.item.organization.IOrgWeapon;

public class BaseKHEntity extends Monster implements IKHMob {

	public static final String FINAL_MIX_VARIANT = "fm";
	public double animFrame; //Here so it's not shared between multiple entities

	public BaseKHEntity(EntityType<? extends Monster> type, Level worldIn) {
		super(type, worldIn);
	}

	public static final EntityDataAccessor<Integer> ANIMATION = SynchedEntityData.defineId(BaseKHEntity.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Integer> STATE = SynchedEntityData.defineId(BaseKHEntity.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<String> VARIANT = SynchedEntityData.defineId(BaseKHEntity.class, EntityDataSerializers.STRING);

	public void setState(int i) {
		getEntityData().set(STATE, i);
	}

	public int getState() {
		return getEntityData().get(STATE);
	}

	public void setAnimation(int i) {
		getEntityData().set(ANIMATION, i);
	}

	public int getAnimation() {
		return getEntityData().get(ANIMATION);
	}

	@Override
	public int getDefense() {
		return 0;
	}

	@Override
	public MobType getKHMobType() {
		return MobType.NPC;
	}

	@Override
	protected int getBaseExperienceReward() {
		if (getKHMobType() != MobType.NPC) {
			GlobalData mobData = GlobalData.get(this);
			if (mobData != null && mobData.getLevel() > 0) {
				return (int) (super.getBaseExperienceReward() * (mobData.getLevel() / 10F));
			}
		}
		return super.getBaseExperienceReward();
	}

	@Override //True = can't get hit
	public boolean skipAttackInteraction(Entity pEntity) {
		if (getKHMobType() != MobType.NPC) {
			if(pEntity instanceof Player player) {
				if(ItemStack.isSameItem(player.getMainHandItem(),ItemStack.EMPTY)) {
					return true;
				}
				if(player.getMainHandItem().getItem() instanceof KeybladeItem || player.getMainHandItem().getItem() instanceof IOrgWeapon) {
					return false;
				}
				if(player.getMainHandItem().getItem() == ModItems.dreamSword.get() || player.getMainHandItem().getItem() == ModItems.dreamSword.get() || player.getMainHandItem().getItem() == ModItems.dreamShield.get()) {
					return false;
				}
				if(player.getMainHandItem().getItem() == ModItems.struggleSword.get() || player.getMainHandItem().getItem() == ModItems.struggleWand.get() || player.getMainHandItem().getItem() == ModItems.struggleHammer.get() || player.getMainHandItem().getItem() == ModItems.woodenStick.get()) {
					return true;
				}
				return ModConfigs.needKeybladeForHeartless;
			}
		}
		return super.skipAttackInteraction(pEntity);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
		super.defineSynchedData(pBuilder);
		pBuilder.define(STATE, 0);
		pBuilder.define(ANIMATION,0);
		pBuilder.define(VARIANT, "");
	}

	public String getVariant() {
		return this.entityData.get(VARIANT);
	}

	public void setVariant(String variant) {
		this.entityData.set(VARIANT, variant == null ? "" : variant);
	}

	public boolean canHaveFinalMixVariant() {
		MobType type = getKHMobType();
		return type == MobType.HEARTLESS_PUREBLOOD || type == MobType.HEARTLESS_EMBLEM;
	}

	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData data) {
		SpawnGroupData result = super.finalizeSpawn(level, difficulty, reason, data);
		if (getVariant().isEmpty() && canHaveFinalMixVariant()) {
			int chance = ModConfigs.SERVER.finalMixVariantChance.get();
			if (chance > 0 && this.random.nextInt(100) < chance) {
				setVariant(FINAL_MIX_VARIANT);
			}
		}
		return result;
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		if (!getVariant().isEmpty()) {
			tag.putString("Variant", getVariant());
		}
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		setVariant(tag.getString("Variant"));
	}

}

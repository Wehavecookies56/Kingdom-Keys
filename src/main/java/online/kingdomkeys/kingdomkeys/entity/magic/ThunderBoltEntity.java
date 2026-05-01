package online.kingdomkeys.kingdomkeys.entity.magic;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.damagesource.KKDamageTypes;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.List;

public class ThunderBoltEntity extends ThrowableProjectile {
	private int lightningState;
	public long boltVertex;
	private int boltLivingTime;
	private boolean effectOnly;
	float dmgMult = 1;
	public ThunderBoltEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
		super(type, world);
		this.blocksBuilding = true;
	}

	public ThunderBoltEntity(Level world, LivingEntity player, double x, double y, double z, float dmgMult) {
		super(ModEntities.TYPE_THUNDERBOLT.get(), player, world);
		this.noCulling = true;
		this.moveTo(x, y, z, 0.0F, 0.0F);
		this.lightningState = 2;
		this.boltVertex = this.random.nextLong();
		this.boltLivingTime = this.random.nextInt(3) + 1;
		this.effectOnly = false;
		this.dmgMult = dmgMult;
	}

	public SoundSource getSoundSource() {
		return SoundSource.WEATHER;
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {

	}

	/**
	 * Called to update the entity's position/logic.
	 */
	public void tick() {
		super.tick();
		--this.lightningState;
		if (this.lightningState < 0) {
			if (this.boltLivingTime == 0) {
				this.remove(RemovalReason.KILLED);
			} else if (this.lightningState < -this.random.nextInt(10)) {
				--this.boltLivingTime;
				this.lightningState = 1;
				this.boltVertex = this.random.nextLong();
				// this.igniteBlocks(0);
			}
		}

		if (this.lightningState >= 0 && getOwner() != null) {
			if (this.level().isClientSide) {
				this.level().setSkyFlashTime(2);
			} else if (!this.effectOnly) {
				float radius = 1.0F;
				List<LivingEntity> list;
				if(getOwner() instanceof Player player) {
					list = Utils.getLivingEntitiesInRadiusExcludingParty(player,this, radius, 10F,radius);
				} else {
					list = Utils.getLivingEntitiesInRadius(this, radius);
				}

				for (LivingEntity entity : list) {
					float dmg = this.getOwner() instanceof Player ? DamageCalculation.getMagicDamage((Player) this.getOwner()) * 0.02F : 2;
					entity.invulnerableTime = 0;
					entity.hurt(KKDamageTypes.getElementalDamage(KKDamageTypes.LIGHTNING,this, this.getOwner()), dmg * dmgMult);

					if (entity instanceof Pig) {
						if (level().getDifficulty() != Difficulty.PEACEFUL) {
							Pig pig = (Pig) entity;
							ZombifiedPiglin zombifiedpiglinentity = EntityType.ZOMBIFIED_PIGLIN.create(level());
							zombifiedpiglinentity.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.GOLDEN_SWORD));
							zombifiedpiglinentity.moveTo(pig.getX(), pig.getY(), pig.getZ(), pig.getYRot(), pig.getXRot());
							zombifiedpiglinentity.setNoAi(pig.isNoAi());
							zombifiedpiglinentity.setBaby(pig.isBaby());
							if (pig.hasCustomName()) {
								zombifiedpiglinentity.setCustomName(pig.getCustomName());
								zombifiedpiglinentity.setCustomNameVisible(pig.isCustomNameVisible());
							}

							zombifiedpiglinentity.setPersistenceRequired();
							level().addFreshEntity(zombifiedpiglinentity);
							pig.remove(RemovalReason.KILLED);
						}
					}

					if (entity instanceof Villager) {
						if (level().getDifficulty() != Difficulty.PEACEFUL) {
							Villager villager = (Villager) entity;

							Witch witchentity = EntityType.WITCH.create(level());
							witchentity.moveTo(villager.getX(), villager.getY(), villager.getZ(), villager.getYRot(), villager.getXRot());
							witchentity.finalizeSpawn((ServerLevel) level(), level().getCurrentDifficultyAt(witchentity.blockPosition()), MobSpawnType.CONVERSION, null);
							witchentity.setNoAi(villager.isNoAi());
							if (villager.hasCustomName()) {
								witchentity.setCustomName(villager.getCustomName());
								witchentity.setCustomNameVisible(villager.isCustomNameVisible());
							}

							witchentity.setPersistenceRequired();
							level().addFreshEntity(witchentity);
							villager.remove(RemovalReason.KILLED);
						}
					}

					if (entity instanceof Creeper) {
						LightningBolt lightningBoltEntity = EntityType.LIGHTNING_BOLT.create(this.level());
						lightningBoltEntity.moveTo(Vec3.atBottomCenterOf(entity.blockPosition()));
						lightningBoltEntity.setCause(getOwner() instanceof ServerPlayer ? (ServerPlayer) getOwner() : null);
						this.level().addFreshEntity(lightningBoltEntity);
					}
				}

				if (getOwner() != null) {
					CriteriaTriggers.CHANNELED_LIGHTNING.trigger((ServerPlayer) getOwner(), list);
				}
			}
		}

	}

	/**
	 * Checks if the entity is in range to render.
	 */
	@OnlyIn(Dist.CLIENT)
	public boolean shouldRenderAtSqrDistance(double distance) {
		double d0 = 64.0D * getViewScale();
		return distance < d0 * d0;
	}
}
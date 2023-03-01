package online.kingdomkeys.kingdomkeys.entity.magic;

import net.minecraft.Util;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
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
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import online.kingdomkeys.kingdomkeys.damagesource.LightningDamageSource;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import net.minecraft.world.entity.Entity.RemovalReason;

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

	public ThunderBoltEntity(PlayMessages.SpawnEntity spawnEntity, Level world) {
		super(ModEntities.TYPE_THUNDERBOLT.get(), world);
	}

	public ThunderBoltEntity(Level world) {
		super(ModEntities.TYPE_THUNDERBOLT.get(), world);
		this.blocksBuilding = true;
	}

	public ThunderBoltEntity(Level world, Player player, double x, double y, double z, float dmgMult) {
		super(ModEntities.TYPE_THUNDERBOLT.get(), player, world);
		setCaster(player.getUUID());
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
			if (this.level.isClientSide) {
				this.level.setSkyFlashTime(2);
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
					entity.hurt(LightningDamageSource.getLightningDamage(this, this.getOwner()), dmg * dmgMult);

					if (entity instanceof Pig) {
						if (level.getDifficulty() != Difficulty.PEACEFUL) {
							Pig pig = (Pig) entity;
							ZombifiedPiglin zombifiedpiglinentity = EntityType.ZOMBIFIED_PIGLIN.create(level);
							zombifiedpiglinentity.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.GOLDEN_SWORD));
							zombifiedpiglinentity.moveTo(pig.getX(), pig.getY(), pig.getZ(), pig.getYRot(), pig.getXRot());
							zombifiedpiglinentity.setNoAi(pig.isNoAi());
							zombifiedpiglinentity.setBaby(pig.isBaby());
							if (pig.hasCustomName()) {
								zombifiedpiglinentity.setCustomName(pig.getCustomName());
								zombifiedpiglinentity.setCustomNameVisible(pig.isCustomNameVisible());
							}

							zombifiedpiglinentity.setPersistenceRequired();
							level.addFreshEntity(zombifiedpiglinentity);
							pig.remove(RemovalReason.KILLED);
						}
					}

					if (entity instanceof Villager) {
						if (level.getDifficulty() != Difficulty.PEACEFUL) {
							Villager villager = (Villager) entity;

							Witch witchentity = EntityType.WITCH.create(level);
							witchentity.moveTo(villager.getX(), villager.getY(), villager.getZ(), villager.getYRot(), villager.getXRot());
							witchentity.finalizeSpawn((ServerLevel) level, level.getCurrentDifficultyAt(witchentity.blockPosition()), MobSpawnType.CONVERSION, (SpawnGroupData) null, (CompoundTag) null);
							witchentity.setNoAi(villager.isNoAi());
							if (villager.hasCustomName()) {
								witchentity.setCustomName(villager.getCustomName());
								witchentity.setCustomNameVisible(villager.isCustomNameVisible());
							}

							witchentity.setPersistenceRequired();
							level.addFreshEntity(witchentity);
							villager.remove(RemovalReason.KILLED);
						}
					}

					if (entity instanceof Creeper) {
						LightningBolt lightningBoltEntity = EntityType.LIGHTNING_BOLT.create(this.level);
						lightningBoltEntity.moveTo(Vec3.atBottomCenterOf(entity.blockPosition()));
						lightningBoltEntity.setCause(getCaster() instanceof ServerPlayer ? (ServerPlayer) getCaster() : null);
						this.level.addFreshEntity(lightningBoltEntity);
					}
				}

				if (getCaster() != null) {
					CriteriaTriggers.CHANNELED_LIGHTNING.trigger((ServerPlayer) getCaster(), list);
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

	/*
	 * @Override public void writeAdditional(CompoundNBT compound) {
	 * compound.putUniqueId("caster", this.getCaster()); }
	 * 
	 * @Override public void readAdditional(CompoundNBT compound) {
	 * this.setCaster(compound.getUniqueId("caster")); }
	 */

	private static final EntityDataAccessor<Optional<UUID>> OWNER = SynchedEntityData.defineId(ThunderBoltEntity.class, EntityDataSerializers.OPTIONAL_UUID);

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		if (this.entityData.get(OWNER) != null) {
			compound.putString("OwnerUUID", this.entityData.get(OWNER).get().toString());
		}
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		this.entityData.set(OWNER, Optional.of(UUID.fromString(compound.getString("OwnerUUID"))));
	}

	public Player getCaster() {
		return this.getEntityData().get(OWNER).isPresent() ? this.level.getPlayerByUUID(this.getEntityData().get(OWNER).get()) : null;
	}

	public void setCaster(UUID uuid) {
		this.entityData.set(OWNER, Optional.of(uuid));
	}

	@Override
	protected void defineSynchedData() {
		this.entityData.define(OWNER, Optional.of(Util.NIL_UUID));
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	protected void onHit(HitResult result) {
		// TODO Auto-generated method stub
	}
}
package online.kingdomkeys.kingdomkeys.entity.magic;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.WitchEntity;
import net.minecraft.entity.monster.ZombifiedPiglinEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.Util;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.lib.Party.Member;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class ThunderBoltEntity extends ThrowableEntity {
	private int lightningState;
	public long boltVertex;
	private int boltLivingTime;
	private boolean effectOnly;

	public ThunderBoltEntity(EntityType<? extends ThrowableEntity> type, World world) {
		super(type, world);
		this.preventEntitySpawning = true;
	}

	public ThunderBoltEntity(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
		super(ModEntities.TYPE_THUNDERBOLT.get(), world);
	}

	public ThunderBoltEntity(World world) {
		super(ModEntities.TYPE_THUNDERBOLT.get(), world);
		this.preventEntitySpawning = true;
	}

	public ThunderBoltEntity(World world, PlayerEntity player, double x, double y, double z) {
		super(ModEntities.TYPE_THUNDERBOLT.get(), player, world);
		setCaster(player.getUniqueID());
		this.ignoreFrustumCheck = true;
	      this.setLocationAndAngles(x, y, z, 0.0F, 0.0F);
	      this.lightningState = 2;
	      this.boltVertex = this.rand.nextLong();
	      this.boltLivingTime = this.rand.nextInt(3) + 1;
	      this.effectOnly = false;
	}
	public SoundCategory getSoundCategory() {
		return SoundCategory.WEATHER;
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	public void tick() {
		super.tick();
		--this.lightningState;
		if (this.lightningState < 0) {
			if (this.boltLivingTime == 0) {
				this.remove();
			} else if (this.lightningState < -this.rand.nextInt(10)) {
				--this.boltLivingTime;
				this.lightningState = 1;
				this.boltVertex = this.rand.nextLong();
				//this.igniteBlocks(0);
			}
		}

		if (this.lightningState >= 0 && getShooter() != null) {
			if (this.world.isRemote) {
				this.world.setTimeLightningFlash(2);
			} else if (!this.effectOnly) {
				float radius = 2.0F;
				List<LivingEntity> list = Utils.getLivingEntitiesInRadius(this, radius);
				Party casterParty = ModCapabilities.getWorld(world).getPartyFromMember(getShooter().getUniqueID());

				if(casterParty != null && !casterParty.getFriendlyFire()) {
					for(Member m : casterParty.getMembers()) {
						list.remove(world.getPlayerByUuid(m.getUUID()));
					}
				} else {
					list.remove(getShooter());
				}
				
				for (LivingEntity entity : list) {
					float dmg = this.getShooter() instanceof PlayerEntity ? DamageCalculation.getMagicDamage((PlayerEntity) this.getShooter()) * 0.1F : 2;
					entity.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getShooter()), dmg);

					if (entity instanceof PigEntity) {
						if (world.getDifficulty() != Difficulty.PEACEFUL) {
							PigEntity pig = (PigEntity) entity;
							ZombifiedPiglinEntity zombifiedpiglinentity = EntityType.ZOMBIFIED_PIGLIN.create(world);
							zombifiedpiglinentity.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.GOLDEN_SWORD));
							zombifiedpiglinentity.setLocationAndAngles(pig.getPosX(), pig.getPosY(), pig.getPosZ(), pig.rotationYaw, pig.rotationPitch);
							zombifiedpiglinentity.setNoAI(pig.isAIDisabled());
							zombifiedpiglinentity.setChild(pig.isChild());
							if (pig.hasCustomName()) {
								zombifiedpiglinentity.setCustomName(pig.getCustomName());
								zombifiedpiglinentity.setCustomNameVisible(pig.isCustomNameVisible());
							}

							zombifiedpiglinentity.enablePersistence();
							world.addEntity(zombifiedpiglinentity);
							pig.remove();
						}
					}

					if (entity instanceof VillagerEntity) {
						if (world.getDifficulty() != Difficulty.PEACEFUL) {
							VillagerEntity villager = (VillagerEntity) entity;

							WitchEntity witchentity = EntityType.WITCH.create(world);
							witchentity.setLocationAndAngles(villager.getPosX(), villager.getPosY(), villager.getPosZ(), villager.rotationYaw, villager.rotationPitch);
							witchentity.onInitialSpawn((ServerWorld)world, world.getDifficultyForLocation(witchentity.getPosition()), SpawnReason.CONVERSION, (ILivingEntityData) null, (CompoundNBT) null);
							witchentity.setNoAI(villager.isAIDisabled());
							if (villager.hasCustomName()) {
								witchentity.setCustomName(villager.getCustomName());
								witchentity.setCustomNameVisible(villager.isCustomNameVisible());
							}

							witchentity.enablePersistence();
							world.addEntity(witchentity);
							villager.remove();
						}
					}

					if(entity instanceof CreeperEntity) {
						 LightningBoltEntity lightningBoltEntity = EntityType.LIGHTNING_BOLT.create(this.world);
						 lightningBoltEntity.moveForced(Vector3d.copyCenteredHorizontally(entity.getPosition()));
						 lightningBoltEntity.setCaster(getCaster() instanceof ServerPlayerEntity ? (ServerPlayerEntity)getCaster() : null);
				         this.world.addEntity(lightningBoltEntity);
					}
				}
				
				//System.out.println(list);

				if (getCaster() != null) {
					CriteriaTriggers.CHANNELED_LIGHTNING.trigger((ServerPlayerEntity)getCaster(), list);
				}
			}
		}

	}

	/**
	 * Checks if the entity is in range to render.
	 */
	@OnlyIn(Dist.CLIENT)
	public boolean isInRangeToRenderDist(double distance) {
		double d0 = 64.0D * getRenderDistanceWeight();
		return distance < d0 * d0;
	}

	/*@Override
	public void writeAdditional(CompoundNBT compound) {
		compound.putUniqueId("caster", this.getCaster());
	}

	@Override
	public void readAdditional(CompoundNBT compound) {
		this.setCaster(compound.getUniqueId("caster"));
	}*/
	
	private static final DataParameter<Optional<UUID>> OWNER = EntityDataManager.createKey(ThunderBoltEntity.class, DataSerializers.OPTIONAL_UNIQUE_ID);

	@Override
	public void writeAdditional(CompoundNBT compound) {
		super.writeAdditional(compound);
		if (this.dataManager.get(OWNER) != null) {
			compound.putString("OwnerUUID", this.dataManager.get(OWNER).get().toString());
		}
	}

	@Override
	public void readAdditional(CompoundNBT compound) {
		super.readAdditional(compound);
		this.dataManager.set(OWNER, Optional.of(UUID.fromString(compound.getString("OwnerUUID"))));
	}

	public PlayerEntity getCaster() {
		return this.getDataManager().get(OWNER).isPresent() ? this.world.getPlayerByUuid(this.getDataManager().get(OWNER).get()) : null;
	}

	public void setCaster(UUID uuid) {
		this.dataManager.set(OWNER, Optional.of(uuid));
	}

	@Override
	protected void registerData() {
		this.dataManager.register(OWNER, Optional.of(Util.DUMMY_UUID));
	}

	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	protected void onImpact(RayTraceResult result) {
		// TODO Auto-generated method stub	
	}
}
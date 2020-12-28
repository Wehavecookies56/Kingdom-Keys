package online.kingdomkeys.kingdomkeys.entity.magic;

import java.util.List;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.WitchEntity;
import net.minecraft.entity.monster.ZombiePigmanEntity;
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
import net.minecraft.network.play.server.SSpawnGlobalEntityPacket;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.FMLPlayMessages;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;

public class ThunderBoltEntity extends ThrowableEntity {
	private int lightningState;
	public long boltVertex;
	private int boltLivingTime;
	private boolean effectOnly;
	private PlayerEntity player;
	String caster;

	/*public ThunderBoltEntity(World worldIn, double x, double y, double z, boolean effectOnlyIn) {
		      super(EntityType.LIGHTNING_BOLT, worldIn);
		      this.ignoreFrustumCheck = true;
		      this.setLocationAndAngles(x, y, z, 0.0F, 0.0F);
		      this.lightningState = 2;
		      this.boltVertex = this.rand.nextLong();
		      this.boltLivingTime = this.rand.nextInt(3) + 1;
		      this.effectOnly = effectOnlyIn;
		   }*/

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
		this.player = player;
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
		/*if (this.lightningState == 2) {
			this.world.playSound((PlayerEntity) null, this.getPosX(), this.getPosY(), this.getPosZ(), SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER, SoundCategory.WEATHER, 10000.0F, 0.8F + this.rand.nextFloat() * 0.2F);
			this.world.playSound((PlayerEntity) null, this.getPosX(), this.getPosY(), this.getPosZ(), SoundEvents.ENTITY_LIGHTNING_BOLT_IMPACT, SoundCategory.WEATHER, 2.0F, 0.5F + this.rand.nextFloat() * 0.2F);
			Difficulty difficulty = this.world.getDifficulty();
			if (difficulty == Difficulty.NORMAL || difficulty == Difficulty.HARD) {
				//this.igniteBlocks(4);
			}
		}*/

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

		if (this.lightningState >= 0) {
			if (this.world.isRemote) {
				this.world.setTimeLightningFlash(2);
			} else if (!this.effectOnly) {
				double d0 = 3.0D;
				List<Entity> list = this.world.getEntitiesInAABBexcluding(this, new AxisAlignedBB(this.getPosX() - 2.0D, this.getPosY() - 2.0D, this.getPosZ() - 2.0D, this.getPosX() + 2.0D, this.getPosY() + 6.0D + 2.0D, this.getPosZ() + 2.0D), Entity::isAlive);

				for (Entity entity : list) {
					
					if (entity != getThrower()) {
						float dmg = this.getThrower() instanceof PlayerEntity ? DamageCalculation.getMagicDamage((PlayerEntity) this.getThrower(), 1) : 2;
						entity.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), dmg);
						//System.out.println(dmg);
					}
					
					if (entity instanceof PigEntity) {
						ZombiePigmanEntity zombiepigmanentity = EntityType.ZOMBIE_PIGMAN.create(entity.world);
						zombiepigmanentity.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.GOLDEN_SWORD));
						zombiepigmanentity.setLocationAndAngles(entity.getPosX(), entity.getPosY(), entity.getPosZ(), entity.rotationYaw, entity.rotationPitch);
						zombiepigmanentity.setNoAI(((PigEntity)entity).isAIDisabled());
						if (this.hasCustomName()) {
							zombiepigmanentity.setCustomName(entity.getCustomName());
							zombiepigmanentity.setCustomNameVisible(entity.isCustomNameVisible());
						}

						entity.world.addEntity(zombiepigmanentity);
						entity.remove();
					}

					if (entity instanceof VillagerEntity) {
						WitchEntity witchentity = EntityType.WITCH.create(entity.world);
						witchentity.setLocationAndAngles(entity.getPosX(), entity.getPosY(), entity.getPosZ(), entity.rotationYaw, entity.rotationPitch);
						witchentity.onInitialSpawn(entity.world, entity.world.getDifficultyForLocation(new BlockPos(witchentity)), SpawnReason.CONVERSION, (ILivingEntityData) null, (CompoundNBT) null);
						witchentity.setNoAI(((VillagerEntity)entity).isAIDisabled());
						if (this.hasCustomName()) {
							witchentity.setCustomName(entity.getCustomName());
							witchentity.setCustomNameVisible(entity.isCustomNameVisible());
						}

						entity.world.addEntity(witchentity);
						entity.remove();
					}
					
					if(entity instanceof CreeperEntity) {
						LightningBoltEntity lightningBoltEntity = new LightningBoltEntity(world, entity.getPosX(), entity.getPosY(), entity.getPosZ(), true);
						//((ServerWorld) event.getWorld()).addLightningBolt(lightningBoltEntity);
						entity.onStruckByLightning(lightningBoltEntity);
					}
				}

				if (this.caster != null) {
					CriteriaTriggers.CHANNELED_LIGHTNING.trigger((ServerPlayerEntity)this.player, list);
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

	@Override
	public void writeAdditional(CompoundNBT compound) {
		compound.putString("caster", this.getCaster());
	}

	@Override
	public void readAdditional(CompoundNBT compound) {
		this.setCaster(compound.getString("caster"));
	}

	private static final DataParameter<String> CASTER = EntityDataManager.createKey(MagnetEntity.class, DataSerializers.STRING);

	public String getCaster() {
		return caster;
	}

	public void setCaster(String name) {
		this.dataManager.set(CASTER, name);
		this.caster = name;
	}

	@Override
	public void notifyDataManagerChange(DataParameter<?> key) {
		if (key.equals(CASTER)) {
			this.caster = this.getCasterDataManager();
		}
	}

	@Override
	protected void registerData() {
		this.dataManager.register(CASTER, "");
	}

	public String getCasterDataManager() {
		return this.dataManager.get(CASTER);
	}
	public IPacket<?> createSpawnPacket() {
		return new SSpawnGlobalEntityPacket(this);
	}

	@Override
	protected void onImpact(RayTraceResult result) {
		// TODO Auto-generated method stub	
	}
}
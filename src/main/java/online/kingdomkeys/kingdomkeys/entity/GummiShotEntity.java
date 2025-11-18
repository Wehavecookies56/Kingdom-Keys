package online.kingdomkeys.kingdomkeys.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import online.kingdomkeys.kingdomkeys.block.gummi.GummiWeaponBlock;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.Optional;
import java.util.UUID;

public class GummiShotEntity extends ThrowableProjectile{

	int maxTicks = 100;
	public float dmg;

	public GummiShotEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
		super(type, world);
		this.blocksBuilding = true;
	}

	public GummiShotEntity(EntityType<? extends ThrowableProjectile> type, Level world, LivingEntity player, String gummiType, float dmg) {
		super(type, player, world);
        this.dmg = dmg;
        setShotType(gummiType);
	}

    public GummiShotEntity(Level world, String gummiType, float dmg) {
        this(ModEntities.TYPE_GUMMI_SHOT.get(), world);
        this.dmg = dmg;
        setShotType(gummiType);
    }

    public GummiShotEntity(Level world, LivingEntity player, String gummiType, float dmg) {
        this(ModEntities.TYPE_GUMMI_SHOT.get(), world, player, gummiType, dmg);
    }

    @Override
    public void tick() {
        super.tick();

        int ticks = getTicks();
        if(getShotType().isEmpty())
            return;

        GummiWeaponBlock.ShotType projectileType = GummiWeaponBlock.ShotType.valueOf(getShotType().toUpperCase());
        if(projectileType.getRootType() == GummiWeaponBlock.ShotType.GRAVITY){
            if (ticks == 80) {
                gravityExplosion();
            } else if(ticks == 97){
                float radius = projectileType == GummiWeaponBlock.ShotType.GRAVITY ? 2F : 4F;
                Utils.getEntitiesInRadius(this, radius).forEach(this::damage);
                level().playSound(null, getX(),getY(),getZ(), ModSounds.laser.get(), SoundSource.PLAYERS, 2.5F, 0.4F);
            }
        }

        if(ticks > 100) {
            super.remove(RemovalReason.KILLED);
        }

        setTicks(getTicks()+1);
    }

    public void damage(Entity e){
        float damage = dmg * 0.7F;
        if(this.getOwner() != null)
            e.hurt(e.damageSources().thrown(this, this.getOwner()), damage);
        else
            e.hurt(e.damageSources().magic(), damage);

    }

    @Override
    protected void onHit(HitResult rtRes) {
        super.onHit(rtRes);
        if (!level().isClientSide) {
            if (rtRes instanceof EntityHitResult ertResult) {
                if (ertResult.getEntity() instanceof LivingEntity target) {
                    if (target != getOwner()) {
                        target.invulnerableTime = 0;
                        if(this.getOwner() != null)
                            target.hurt(target.damageSources().thrown(this, this.getOwner()), dmg);
                        else
                            target.hurt(target.damageSources().magic(), dmg);

                        GummiWeaponBlock.ShotType projectileType = GummiWeaponBlock.ShotType.valueOf(getShotType().toUpperCase());
                        if(projectileType.getRootType() == GummiWeaponBlock.ShotType.GRAVITY){
                            gravityExplosion();
                        } else {
                            super.remove(RemovalReason.KILLED);
                        }
                    }
                } else if(ertResult.getEntity() instanceof GummiShipEntity ship) {
                    GummiWeaponBlock.ShotType projectileType = GummiWeaponBlock.ShotType.valueOf(getShotType().toUpperCase());
                    if(projectileType.getRootType() == GummiWeaponBlock.ShotType.GRAVITY){
                        gravityExplosion();
                    } else {
                        if(this.getOwner() != null)
                            ship.hurt(ship.damageSources().thrown(this, this.getOwner()), dmg);
                        else
                            ship.hurt(ship.damageSources().magic(), dmg);

                    }
                }
            }
            if (rtRes instanceof BlockHitResult hitResult) {
                BlockPos blockpos = hitResult.getBlockPos();
                if(!(level().getBlockState(blockpos).getBlock() instanceof GummiWeaponBlock)) {
                    GummiWeaponBlock.ShotType projectileType = GummiWeaponBlock.ShotType.valueOf(getShotType().toUpperCase());
                    if(projectileType.getRootType() == GummiWeaponBlock.ShotType.GRAVITY){
                        gravityExplosion();
                    } else {
                        super.remove(RemovalReason.KILLED);
                    }
                }

            }
            remove(RemovalReason.KILLED);
        }
    }

    private void gravityExplosion() {
        //Stop it
        this.setTicks(81);
        shoot(0,0,0, 0, 0);
    }

    @Override
	protected double getDefaultGravity() {
		return 0D;
	}

	@Override
	public void remove(RemovalReason reason) {
		if(tickCount > 100) {
			super.remove(RemovalReason.KILLED);
		}
	}

	public int getMaxTicks() {
		return maxTicks;
	}

	public void setMaxTicks(int maxTicks) {
		this.maxTicks = maxTicks;
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		if (this.entityData.get(OWNER).isPresent()) {
			compound.putString("OwnerUUID", this.entityData.get(OWNER).get().toString());
			compound.putString("ShotType", this.entityData.get(SHOT_TYPE));
            compound.putInt("ticks", getTicks());
		}
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		this.entityData.set(OWNER, Optional.of(UUID.fromString(compound.getString("OwnerUUID"))));
		this.entityData.set(SHOT_TYPE, compound.getString("ShotType"));
        setTicks(compound.getInt("ticks"));
	}

	private static final EntityDataAccessor<Optional<UUID>> OWNER = SynchedEntityData.defineId(GummiShotEntity.class, EntityDataSerializers.OPTIONAL_UUID);
	private static final EntityDataAccessor<String> SHOT_TYPE = SynchedEntityData.defineId(GummiShotEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Integer> TICKS = SynchedEntityData.defineId(GummiShotEntity.class, EntityDataSerializers.INT);

	public Player getCaster() {
		return this.getEntityData().get(OWNER).isPresent() ? this.level().getPlayerByUUID(this.getEntityData().get(OWNER).get()) : null;
	}

	public void setCaster(UUID uuid) {
		this.entityData.set(OWNER, Optional.of(uuid));
	}

	public String getShotType() {
		return this.getEntityData().get(SHOT_TYPE);
	}
	
	public void setShotType(String color) {
		this.entityData.set(SHOT_TYPE, color);
	}

    public void setTicks(int ticks) {
        this.entityData.set(TICKS, ticks);
    }

    public int getTicks() {
        return this.entityData.get(TICKS);
    }


    @Override
	protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
		pBuilder.define(OWNER, Optional.of(new UUID(0L, 0L)));
		pBuilder.define(SHOT_TYPE, "");
        pBuilder.define(TICKS, 0);
	}
}

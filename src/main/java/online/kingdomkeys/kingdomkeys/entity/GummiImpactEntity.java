package online.kingdomkeys.kingdomkeys.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import online.kingdomkeys.kingdomkeys.block.GummiWeaponBlock;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.sql.SQLOutput;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class GummiImpactEntity extends ThrowableProjectile{

	int maxTicks = 100;
	public float dmg;
    private GummiImpactEntity linked;

	public GummiImpactEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
		super(type, world);
		this.blocksBuilding = true;
	}

	public GummiImpactEntity(EntityType<? extends ThrowableProjectile> type, Level world, LivingEntity player, float dmg) {
		super(type, player, world);
        this.dmg = dmg;
	}

    public GummiImpactEntity(Level world, float dmg) {
        this(ModEntities.TYPE_GUMMI_IMPACT.get(), world);
        this.dmg = dmg;
    }

    public GummiImpactEntity(Level world, LivingEntity player, float dmg) {
        this(ModEntities.TYPE_GUMMI_IMPACT.get(), world, player, dmg);
    }

    public void setLinked(GummiImpactEntity other) {
        this.linked = other;
    }

    @Override
    public void tick() {
        super.tick();

        int ticks = getTicks();

        if (linked != null && !linked.isRemoved() && this.getId() < linked.getId()) {
            spawnLinkParticles(this.position(), linked.position());
            damageEntitiesBetween(linked, dmg ,getCaster());
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

    private void spawnLinkParticles(Vec3 a, Vec3 b) {
        if (level().isClientSide)
            return;

        int steps = 8;
        Vec3 diff = b.subtract(a);

        for (int i = 0; i <= steps; i++) {
            double t = i / (double) steps;
            Vec3 p = a.add(diff.scale(t));

            float offset = level().random.nextFloat()-0.5F;
            ((ServerLevel)level()).sendParticles(ParticleTypes.BUBBLE, p.x, p.y, p.z, 100,offset,offset,offset,0);
        }
    }

    public void damageEntitiesBetween(GummiImpactEntity other, float damage, Entity shooter) {
        if (!(level() instanceof ServerLevel server))
            return;

        Vec3 start = this.position();
        Vec3 end = other.position();

        int steps = 30;
        double radius = 0.5;

        for (int i = 0; i <= steps; i++) {
            double t = i / (double) steps;
            Vec3 point = start.add(end.subtract(start).scale(t));

            AABB box = new AABB(point.x - radius, point.y - radius, point.z - radius, point.x + radius, point.y + radius, point.z + radius);

            List<Entity> entities = level().getEntities(shooter, box, e -> e != shooter && e != this && e != other && e.isAlive());

            for (Entity e : entities) {
                if(e instanceof GummiShipEntity ship){
                    if(!ship.getPassengers().contains(getOwner())) {
                        e.hurt(e.damageSources().indirectMagic(shooter, shooter), damage);
                    }
                } else {
                    e.hurt(e.damageSources().indirectMagic(shooter, shooter), damage);
                }
            }
        }
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

                        //super.remove(RemovalReason.KILLED);
                    }
                } else if(ertResult.getEntity() instanceof GummiShipEntity ship) {
                    if(this.getOwner() != null)
                        ship.hurt(ship.damageSources().thrown(this, this.getOwner()), dmg);
                    else
                        ship.hurt(ship.damageSources().magic(), dmg);
                }
            }
            if (rtRes instanceof BlockHitResult hitResult) {
                BlockPos blockpos = hitResult.getBlockPos();
                if(!(level().getBlockState(blockpos).getBlock() instanceof GummiWeaponBlock)) {
                    super.remove(RemovalReason.KILLED);
                }
            }
            //remove(RemovalReason.KILLED);
        }
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
            compound.putInt("ticks", getTicks());
		}
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		this.entityData.set(OWNER, Optional.of(UUID.fromString(compound.getString("OwnerUUID"))));
        setTicks(compound.getInt("ticks"));
	}

	private static final EntityDataAccessor<Optional<UUID>> OWNER = SynchedEntityData.defineId(GummiImpactEntity.class, EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<Integer> TICKS = SynchedEntityData.defineId(GummiImpactEntity.class, EntityDataSerializers.INT);

	public Player getCaster() {
		return this.getEntityData().get(OWNER).isPresent() ? this.level().getPlayerByUUID(this.getEntityData().get(OWNER).get()) : null;
	}

	public void setCaster(UUID uuid) {
		this.entityData.set(OWNER, Optional.of(uuid));
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
        pBuilder.define(TICKS, 0);
	}
}

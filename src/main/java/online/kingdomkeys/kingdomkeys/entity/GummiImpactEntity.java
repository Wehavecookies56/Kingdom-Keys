package online.kingdomkeys.kingdomkeys.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import org.joml.Vector3f;
import online.kingdomkeys.kingdomkeys.block.gummi.GummiWeaponBlock;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class GummiImpactEntity extends ThrowableProjectile{

	int maxTicks = 20;
	public float dmg;

    private static final int ARC_POINTS = 24;

    // Arc middle peak
    private static final double BOW = 0.5;

    // Entity detection range
    private static final double REACH = 0.9;

    private GummiImpactEntity linked;

    // Just hit the entity once
    private final Set<Integer> caught = new HashSet<>();

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

    public void link(GummiImpactEntity other, Vec3 origin) {
        this.linked = other;

        this.entityData.set(LINKED, other.getId());
        this.entityData.set(ORIGIN, new Vector3f((float) origin.x, (float) origin.y, (float) origin.z));
    }

    @Override
    public void tick() {
        super.tick();

        int ticks = getTicks();

        // Only one of the pair hits, or everything would happen twice
        if (linked != null && !linked.isRemoved() && this.getId() < linked.getId()) {
            damageAlong(arc(this.position(), linked.position(), origin()));
        }

        if (ticks > maxTicks) {
            super.remove(RemovalReason.KILLED);
        }

        setTicks(ticks + 1);
    }

    public static Vec3[] arc(Vec3 from, Vec3 to, Vec3 origin) {
        Vec3 middle = from.add(to).scale(0.5);
        Vec3 outwards = origin == null ? Vec3.ZERO : middle.subtract(origin);
        Vec3 control = middle.add(outwards.scale(BOW));

        Vec3[] points = new Vec3[ARC_POINTS + 3];
        double step = 1D / (ARC_POINTS - 1);

        for (int i = 0; i < points.length; i++) {
            double t = (i - 1) * step;
            double u = 1 - t;
            points[i] = from.scale(u * u).add(control.scale(2 * u * t)).add(to.scale(t * t));
        }

        return points;
    }

    // Middle point, where the arc bows
    public Vec3 origin() {
        Vector3f from = this.entityData.get(ORIGIN);
        return new Vec3(from.x, from.y, from.z);
    }

    @Nullable
    public GummiImpactEntity otherPart() {
        int id = this.entityData.get(LINKED);
        return id >= 0 && level().getEntity(id) instanceof GummiImpactEntity other ? other : null;
    }

    public void damage(Entity e){
        float damage = dmg * 0.7F;
        if(this.getOwner() != null)
            e.hurt(e.damageSources().thrown(this, this.getOwner()), damage);
        else
            e.hurt(e.damageSources().magic(), damage);

    }

    private void damageAlong(Vec3[] arc) {
        if (!(level() instanceof ServerLevel server)) {
            return;
        }

        Entity shooter = getCaster() != null ? getCaster() : getOwner();

        for (Vec3 point : arc) {
            AABB box = new AABB(point.x - REACH, point.y - REACH, point.z - REACH, point.x + REACH, point.y + REACH, point.z + REACH);

            for (Entity hit : server.getEntities(shooter, box, entity -> entity != this && entity != linked && entity.isAlive())) {
                if (!caught.add(hit.getId())) {
                    continue;
                }

                // The ship that fired it, and whoever is riding in it, are behind the wave rather than in it
                if (hit instanceof GummiShipEntity ship && ship.getPassengers().contains(getOwner())) {
                    continue;
                }

                hit.invulnerableTime = 0;
                hit.hurt(shooter == null ? hit.damageSources().magic() : hit.damageSources().indirectMagic(shooter, shooter), dmg);
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
		if(tickCount > maxTicks) {
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
    private static final EntityDataAccessor<Integer> LINKED = SynchedEntityData.defineId(GummiImpactEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Vector3f> ORIGIN = SynchedEntityData.defineId(GummiImpactEntity.class, EntityDataSerializers.VECTOR3);

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
        pBuilder.define(LINKED, -1);
        pBuilder.define(ORIGIN, new Vector3f());
	}
}

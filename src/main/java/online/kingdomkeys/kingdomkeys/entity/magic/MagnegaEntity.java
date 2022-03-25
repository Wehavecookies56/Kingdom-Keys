package online.kingdomkeys.kingdomkeys.entity.magic;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.Util;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.lib.Party.Member;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class MagnegaEntity extends ThrowableProjectile {

	int maxTicks = 200;
	float dmgMult = 1;
	
	public MagnegaEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
		super(type, world);
		this.blocksBuilding = true;
	}

	public MagnegaEntity(PlayMessages.SpawnEntity spawnEntity, Level world) {
		super(ModEntities.TYPE_MAGNEGA.get(), world);
	}

	public MagnegaEntity(Level world) {
		super(ModEntities.TYPE_MAGNEGA.get(), world);
		this.blocksBuilding = true;
	}

	public MagnegaEntity(Level world, Player player, float dmgMult) {
		super(ModEntities.TYPE_MAGNEGA.get(), player, world);
		setCaster(player.getUUID());
		this.dmgMult = dmgMult;
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	protected float getGravity() {
		return 0F;
	}

	@Override
	public void tick() {
		if (this.tickCount > maxTicks || getCaster() == null) {
			this.remove(RemovalReason.KILLED);
		}

		if(level == null || ModCapabilities.getWorld(level) == null || getCaster() == null)
			return;
		
		
		level.addParticle(ParticleTypes.BUBBLE, getX(), getY(), getZ(), 0, 0, 0);

		if (tickCount >= 5) {
			float radius = 3F;
			double X = getX();
			double Y = getY();
			double Z = getZ();

			for (int t = 1; t < 360; t += 30) {
				for (int s = 1; s < 360 ; s += 30) {
					double x = X + (radius * Math.cos(Math.toRadians(s+tickCount)) * Math.sin(Math.toRadians(t+tickCount)));
					double z = Z + (radius * Math.sin(Math.toRadians(s+tickCount)) * Math.sin(Math.toRadians(t+tickCount)));
					double y = Y + (radius * Math.cos(Math.toRadians(t+tickCount)));
					level.addParticle(ParticleTypes.BUBBLE_POP, x, y + 1, z, 0, 0, 0);
				}
			}

			this.setDeltaMovement(0, 0, 0);
			this.hurtMarked = true;
			
			
			List<Entity> list = level.getEntities(getCaster(), getBoundingBox().inflate(radius,radius*2,radius));
			Party casterParty = ModCapabilities.getWorld(level).getPartyFromMember(getCaster().getUUID());

			if(casterParty != null && !casterParty.getFriendlyFire()) { //Exclude members from AOE
				for(Member m : casterParty.getMembers()) {
					list.remove(level.getPlayerByUUID(m.getUUID()));
				}
			} else {
				list.remove(getOwner());
			}
			
			if (!list.isEmpty()) {
				for (int i = 0; i < list.size(); i++) {
					Entity e = (Entity) list.get(i);
					if (e instanceof LivingEntity) {
						double d = e.getX() - getX();
						double d1 = e.getZ() - getZ();
						
						((LivingEntity) e).knockback(1, d, d1);
						if (e.getY() < this.getY() - 0.5) {
							e.setDeltaMovement(e.getDeltaMovement().x, 0.5F, e.getDeltaMovement().z);
						}
						
						if(tickCount + 1 > maxTicks) {
							if(Utils.isHostile(e)) {
								float dmg = this.getOwner() instanceof Player ? DamageCalculation.getMagicDamage((Player) this.getOwner()) * 0.5F : 2;
								e.hurt(DamageSource.thrown(this, this.getOwner()), dmg * dmgMult);
							}
							remove(RemovalReason.KILLED);
						}
					}
				}
			}
		}

		super.tick();
	}

	@Override
	protected void onHit(HitResult rtRes) {

	}

	public int getMaxTicks() {
		return maxTicks;
	}

	public void setMaxTicks(int maxTicks) {
		this.maxTicks = maxTicks;
	}

	private static final EntityDataAccessor<Optional<UUID>> OWNER = SynchedEntityData.defineId(MagnegaEntity.class, EntityDataSerializers.OPTIONAL_UUID);

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
}

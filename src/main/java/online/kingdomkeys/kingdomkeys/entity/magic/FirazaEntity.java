package online.kingdomkeys.kingdomkeys.entity.magic;

import java.util.List;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class FirazaEntity extends ThrowableProjectile {

	int maxTicks = 100;
	float dmgMult = 1;

	public FirazaEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
		super(type, world);
		this.blocksBuilding = true;
	}

	public FirazaEntity(PlayMessages.SpawnEntity spawnEntity, Level world) {
		super(ModEntities.TYPE_FIRAZA.get(), world);
	}

	public FirazaEntity(Level world) {
		super(ModEntities.TYPE_FIRAZA.get(), world);
		this.blocksBuilding = true;
	}

	public FirazaEntity(Level world, LivingEntity player, float dmgMult) {
		super(ModEntities.TYPE_FIRAZA.get(), player, world);
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
		if (this.tickCount > maxTicks) {
			this.remove(RemovalReason.KILLED);
		}

		//world.addParticle(ParticleTypes.ENTITY_EFFECT, getPosX(), getPosY(), getPosZ(), 1, 1, 0);
		if(tickCount > 2) {
			float radius = 1F;
			for (int t = 1; t < 360; t += 30) {
				for (int s = 1; s < 360 ; s += 30) {
					double x = getX() + (radius * Math.cos(Math.toRadians(s)) * Math.sin(Math.toRadians(t)));
					double z = getZ() + (radius * Math.sin(Math.toRadians(s)) * Math.sin(Math.toRadians(t)));
					double y = getY() + (radius * Math.cos(Math.toRadians(t)));
					level.addParticle(ParticleTypes.FLAME, x, y, z, 0, 0, 0);
				}
			}
		}
		super.tick();
	}

	@Override
	protected void onHit(HitResult rtRes) {
		if (!level.isClientSide && getOwner() != null) {
			EntityHitResult ertResult = null;
			BlockHitResult brtResult = null;

			if (rtRes instanceof EntityHitResult) {
				ertResult = (EntityHitResult) rtRes;
			}

			if (rtRes instanceof BlockHitResult) {
				brtResult = (BlockHitResult) rtRes;
			}

			if (ertResult != null && ertResult.getEntity() instanceof LivingEntity) {
				LivingEntity target = (LivingEntity) ertResult.getEntity();

				if (target != getOwner()) {
					Party p = null;
					if (getOwner() != null) {
						p = ModCapabilities.getWorld(getOwner().level).getPartyFromMember(getOwner().getUUID());
					}
					if(p == null || (p.getMember(target.getUUID()) == null || p.getFriendlyFire())) { //If caster is not in a party || the party doesn't have the target in it || the party has FF on
						target.setSecondsOnFire(30);
						float dmg = this.getOwner() instanceof Player ? DamageCalculation.getMagicDamage((Player) this.getOwner()) : 2;
						target.hurt(DamageSource.thrown(this, this.getOwner()), dmg * dmgMult);
					}
				}
			}
			
			float radius = 6F;
			if(getOwner() instanceof Player) {
				List<LivingEntity> list = Utils.getLivingEntitiesInRadiusExcludingParty((Player) getOwner(), radius);
	
				((ServerLevel)level).sendParticles(ParticleTypes.FLAME, getX(), getY(), getZ(), 1000, Math.random() - 0.5D, Math.random() - 0.5D, Math.random() - 0.5D, 0.3);
				
				if (!list.isEmpty()) {
					for (int i = 0; i < list.size(); i++) {
						Entity e = (Entity) list.get(i);
						if (e instanceof LivingEntity) {
							e.setSecondsOnFire(25);
							float dmg = this.getOwner() instanceof Player ? DamageCalculation.getMagicDamage((Player) this.getOwner()) * 0.8F : 2;
							e.hurt(DamageSource.thrown(this, this.getOwner()), dmg * dmgMult);
						}
					}
				}
			}
			remove(RemovalReason.KILLED);
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
		// compound.putInt("lvl", this.getLvl());
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		// this.setLvl(compound.getInt("lvl"));
	}

	@Override
	protected void defineSynchedData() {
		// TODO Auto-generated method stub

	}
}

package online.kingdomkeys.kingdomkeys.entity.magic;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.lib.Party.Member;

public class FiragaEntity extends ThrowableEntity {

	int maxTicks = 100;
	float dmgMult = 1;

	public FiragaEntity(EntityType<? extends ThrowableEntity> type, World world) {
		super(type, world);
		this.preventEntitySpawning = true;
	}

	public FiragaEntity(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
		super(ModEntities.TYPE_FIRAGA.get(), world);
	}

	public FiragaEntity(World world) {
		super(ModEntities.TYPE_FIRAGA.get(), world);
		this.preventEntitySpawning = true;
	}

	public FiragaEntity(World world, LivingEntity player, float dmgMult) {
		super(ModEntities.TYPE_FIRAGA.get(), player, world);
		this.dmgMult = dmgMult;
	}

	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	protected float getGravityVelocity() {
		return 0F;
	}

	@Override
	public void tick() {
		if (this.ticksExisted > maxTicks) {
			this.remove();
		}

		//world.addParticle(ParticleTypes.ENTITY_EFFECT, getPosX(), getPosY(), getPosZ(), 1, 1, 0);
		if(ticksExisted > 2) {
			float radius = 0.8F;
			for (int t = 1; t < 360; t += 30) {
				for (int s = 1; s < 360 ; s += 30) {
					double x = getPosX() + (radius * Math.cos(Math.toRadians(s)) * Math.sin(Math.toRadians(t)));
					double z = getPosZ() + (radius * Math.sin(Math.toRadians(s)) * Math.sin(Math.toRadians(t)));
					double y = getPosY() + (radius * Math.cos(Math.toRadians(t)));
					world.addParticle(ParticleTypes.FLAME, x, y, z, 0, 0, 0);
				}
			}
		}
		super.tick();
	}

	@Override
	protected void onImpact(RayTraceResult rtRes) {
		if (!world.isRemote && getShooter() != null) {
			EntityRayTraceResult ertResult = null;
			BlockRayTraceResult brtResult = null;

			if (rtRes instanceof EntityRayTraceResult) {
				ertResult = (EntityRayTraceResult) rtRes;
			}

			if (rtRes instanceof BlockRayTraceResult) {
				brtResult = (BlockRayTraceResult) rtRes;
			}

			if (ertResult != null && ertResult.getEntity() instanceof LivingEntity) {
				LivingEntity target = (LivingEntity) ertResult.getEntity();

				if (target != getShooter()) {
					Party p = null;
					if (getShooter() != null) {
						p = ModCapabilities.getWorld(getShooter().world).getPartyFromMember(getShooter().getUniqueID());
					}
					if(p == null || (p.getMember(target.getUniqueID()) == null || p.getFriendlyFire())) { //If caster is not in a party || the party doesn't have the target in it || the party has FF on
						target.setFire(15);
						float dmg = this.getShooter() instanceof PlayerEntity ? DamageCalculation.getMagicDamage((PlayerEntity) this.getShooter()) * 0.4F : 2;
						target.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getShooter()), dmg * dmgMult);
					}
				}
			}
			
			float radius = 2F;
			List<Entity> list = world.getEntitiesWithinAABBExcludingEntity(getShooter(), getBoundingBox().grow(radius));
			Party casterParty = ModCapabilities.getWorld(getShooter().world).getPartyFromMember(getShooter().getUniqueID());

			if(casterParty != null && !casterParty.getFriendlyFire()) {
				for(Member m : casterParty.getMembers()) {
					list.remove(world.getPlayerByUuid(m.getUUID()));
				}
			} else {
				list.remove(getShooter());
			}

			((ServerWorld)world).spawnParticle(ParticleTypes.FLAME, getPosX(), getPosY(), getPosZ(), 500, Math.random() - 0.5D, Math.random() - 0.5D, Math.random() - 0.5D,0.1);
			
			if (!list.isEmpty()) {
				for (int i = 0; i < list.size(); i++) {
					Entity e = (Entity) list.get(i);
					if (e instanceof LivingEntity) {
						e.setFire(15);
						float baseDmg = DamageCalculation.getMagicDamage((PlayerEntity) this.getShooter()) * 0.3F;
						float dmg = this.getShooter() instanceof PlayerEntity ? baseDmg : 2;
						e.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getShooter()), dmg);
					}
				}
			}

			remove();
		}
	}

	public int getMaxTicks() {
		return maxTicks;
	}

	public void setMaxTicks(int maxTicks) {
		this.maxTicks = maxTicks;
	}

	@Override
	public void writeAdditional(CompoundNBT compound) {
		// compound.putInt("lvl", this.getLvl());
	}

	@Override
	public void readAdditional(CompoundNBT compound) {
		// this.setLvl(compound.getInt("lvl"));
	}

	@Override
	protected void registerData() {
		// TODO Auto-generated method stub

	}
}

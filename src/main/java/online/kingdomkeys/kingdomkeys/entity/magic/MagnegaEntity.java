package online.kingdomkeys.kingdomkeys.entity.magic;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Util;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.lib.Party.Member;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class MagnegaEntity extends ThrowableEntity {

	int maxTicks = 200;
	float dmgMult = 1;
	
	public MagnegaEntity(EntityType<? extends ThrowableEntity> type, World world) {
		super(type, world);
		this.preventEntitySpawning = true;
	}

	public MagnegaEntity(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
		super(ModEntities.TYPE_MAGNEGA.get(), world);
	}

	public MagnegaEntity(World world) {
		super(ModEntities.TYPE_MAGNEGA.get(), world);
		this.preventEntitySpawning = true;
	}

	public MagnegaEntity(World world, PlayerEntity player, float dmgMult) {
		super(ModEntities.TYPE_MAGNEGA.get(), player, world);
		setCaster(player.getUniqueID());
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
		if (this.ticksExisted > maxTicks || getCaster() == null) {
			this.remove();
		}

		if(world == null || ModCapabilities.getWorld(world) == null || getCaster() == null)
			return;
		
		
		world.addParticle(ParticleTypes.BUBBLE, getPosX(), getPosY(), getPosZ(), 0, 0, 0);

		if (ticksExisted >= 5) {
			float radius = 3F;
			double X = getPosX();
			double Y = getPosY();
			double Z = getPosZ();

			for (int t = 1; t < 360; t += 30) {
				for (int s = 1; s < 360 ; s += 30) {
					double x = X + (radius * Math.cos(Math.toRadians(s+ticksExisted)) * Math.sin(Math.toRadians(t+ticksExisted)));
					double z = Z + (radius * Math.sin(Math.toRadians(s+ticksExisted)) * Math.sin(Math.toRadians(t+ticksExisted)));
					double y = Y + (radius * Math.cos(Math.toRadians(t+ticksExisted)));
					world.addParticle(ParticleTypes.BUBBLE_POP, x, y + 1, z, 0, 0, 0);
				}
			}

			this.setMotion(0, 0, 0);
			this.velocityChanged = true;
			
			
			List<Entity> list = world.getEntitiesWithinAABBExcludingEntity(getCaster(), getBoundingBox().grow(radius,radius*2,radius));
			Party casterParty = ModCapabilities.getWorld(world).getPartyFromMember(getCaster().getUniqueID());

			if(casterParty != null && !casterParty.getFriendlyFire()) { //Exclude members from AOE
				for(Member m : casterParty.getMembers()) {
					list.remove(world.getPlayerByUuid(m.getUUID()));
				}
			} else {
				list.remove(getShooter());
			}
			
			if (!list.isEmpty()) {
				for (int i = 0; i < list.size(); i++) {
					Entity e = (Entity) list.get(i);
					if (e instanceof LivingEntity) {
						double d = e.getPosX() - getPosX();
						double d1 = e.getPosZ() - getPosZ();
						
						((LivingEntity) e).applyKnockback(1, d, d1);
						if (e.getPosY() < this.getPosY() - 0.5) {
							e.setMotion(e.getMotion().x, 0.5F, e.getMotion().z);
						}
						
						if(ticksExisted + 1 > maxTicks) {
							if(Utils.isHostile(e)) {
								float dmg = this.getShooter() instanceof PlayerEntity ? DamageCalculation.getMagicDamage((PlayerEntity) this.getShooter()) * 0.5F : 2;
								e.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getShooter()), dmg * dmgMult);
							}
							remove();
						}
					}
				}
			}
		}

		super.tick();
	}

	@Override
	protected void onImpact(RayTraceResult rtRes) {

	}

	public int getMaxTicks() {
		return maxTicks;
	}

	public void setMaxTicks(int maxTicks) {
		this.maxTicks = maxTicks;
	}

	private static final DataParameter<Optional<UUID>> OWNER = EntityDataManager.createKey(MagnegaEntity.class, DataSerializers.OPTIONAL_UNIQUE_ID);

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
}

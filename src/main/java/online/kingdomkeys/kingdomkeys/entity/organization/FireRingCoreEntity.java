package online.kingdomkeys.kingdomkeys.entity.organization;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.entity.ItemDropEntity;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.lib.Party;

public class FireRingCoreEntity extends ThrowableEntity {

	int maxTicks = 240;
	List<LaserDomeShotEntity> list = new ArrayList<LaserDomeShotEntity>();
	List<Entity> targetList = new ArrayList<Entity>();
	Set<Integer> usedIndexes = new HashSet<Integer>();
	float dmg;
	
	float radius;
	int space;
	int shotsPerTick;
	
	public FireRingCoreEntity(EntityType<? extends ThrowableEntity> type, World world) {
		super(type, world);
		this.preventEntitySpawning = true;
	}

	public FireRingCoreEntity(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
		super(ModEntities.TYPE_FIRE_RING.get(), world);
	}

	public FireRingCoreEntity(World world) {
		super(ModEntities.TYPE_FIRE_RING.get(), world);
		this.preventEntitySpawning = true;
	}

	public FireRingCoreEntity(World world, PlayerEntity player, LivingEntity target, float dmg, int tier) {
		super(ModEntities.TYPE_FIRE_RING.get(), player, world);
		setCaster(player.getUniqueID());
		setTarget(target.getUniqueID());
		setTier(tier);
		this.dmg = dmg;
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
		
		switch(getTier()){
		case 0:
			this.radius = 10;
			this.space = 1;
			this.shotsPerTick = 1;
			this.maxTicks = 240;
			break;
		case 1:
			this.radius = 10;
			this.space = 18;
			this.shotsPerTick = 2;
			this.maxTicks = 180;
			break;
		case 2:
			this.radius = 15;
			this.space = 12;
			this.shotsPerTick = 3;
			this.maxTicks = 240;
			break;
		}

		// world.addParticle(ParticleTypes.ENTITY_EFFECT, getPosX(), getPosY(), getPosZ(), 1, 1, 0);
		world.addParticle(ParticleTypes.BUBBLE, getPosX(), getPosY(), getPosZ(), 0, 0, 0);
		
		double X = getPosX();
		double Y = getPosY();
		double Z = getPosZ();

		for(double i = -1; i < 4; i+=0.1D) {
			for (int a = 1; a <= 360; a += space) {
				double x = X + (radius * Math.cos(Math.toRadians(a)));
				double z = Z + (radius * Math.sin(Math.toRadians(a)));
	
				world.addParticle(ParticleTypes.FLAME, x, Y + i, z, 0.0D, 0.0D, 0.0D);
			}
		}
		updateList();
		if(ticksExisted % 10 == 0) {
			if(!targetList.isEmpty()) {
				for(Entity target : targetList) {
					if(target instanceof LivingEntity) {
						LivingEntity entity = ((LivingEntity) target);
						entity.setHealth(entity.getHealth()-1);
					}
				}
			}
		}
		
		super.tick();
	}

	private void updateList() {
		List<Entity> tempList = world.getEntitiesWithinAABBExcludingEntity(getCaster(), getBoundingBox().grow(radius,radius,radius));
		Party casterParty = ModCapabilities.getWorld(world).getPartyFromMember(getCaster().getUniqueID());

		if(casterParty != null) {
			for(Party.Member m : casterParty.getMembers()) {
				tempList.remove(world.getPlayerByUuid(m.getUUID()));
			}
		} else {
			tempList.remove(getThrower());
		}
		
		targetList.clear();
		for(Entity t : tempList) {
			if(!(t instanceof LaserDomeShotEntity || t instanceof ItemDropEntity || t instanceof ItemEntity || t instanceof ExperienceOrbEntity)) {
				targetList.add(t);
			}
		}
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

	@Override
	public void writeAdditional(CompoundNBT compound) {
		super.writeAdditional(compound);
		if (this.dataManager.get(OWNER) != null) {
			compound.putString("OwnerUUID", this.dataManager.get(OWNER).get().toString());
			compound.putString("TargetUUID", this.dataManager.get(TARGET).get().toString());
			compound.putInt("Tier", this.dataManager.get(TIER));
		}
	}

	@Override
	public void readAdditional(CompoundNBT compound) {
		super.readAdditional(compound);
		this.dataManager.set(OWNER, Optional.of(UUID.fromString(compound.getString("OwnerUUID"))));
		this.dataManager.set(TARGET, Optional.of(UUID.fromString(compound.getString("TargetUUID"))));
		this.dataManager.set(TIER, compound.getInt("Tier"));
	}
	
	private static final DataParameter<Optional<UUID>> OWNER = EntityDataManager.createKey(FireRingCoreEntity.class, DataSerializers.OPTIONAL_UNIQUE_ID);
	private static final DataParameter<Optional<UUID>> TARGET = EntityDataManager.createKey(FireRingCoreEntity.class, DataSerializers.OPTIONAL_UNIQUE_ID);
	private static final DataParameter<Integer> TIER = EntityDataManager.createKey(FireRingCoreEntity.class, DataSerializers.VARINT);
	
	public PlayerEntity getCaster() {
		return this.getDataManager().get(OWNER).isPresent() ? this.world.getPlayerByUuid(this.getDataManager().get(OWNER).get()) : null;
	}

	public void setCaster(UUID uuid) {
		this.dataManager.set(OWNER, Optional.of(uuid));
	}
	
	public PlayerEntity getTarget() {
		return this.getDataManager().get(TARGET).isPresent() ? this.world.getPlayerByUuid(this.getDataManager().get(TARGET).get()) : null;
	}

	public void setTarget(UUID uuid) {
		this.dataManager.set(TARGET, Optional.of(uuid));
	}
	
	public int getTier() {
		return this.dataManager.get(TIER);
	}

	public void setTier(int tier) {
		this.dataManager.set(TIER, tier);
	}
	
	@Override
	protected void registerData() {
		this.dataManager.register(OWNER, Optional.of(new UUID(0L, 0L)));
		this.dataManager.register(TARGET, Optional.of(new UUID(0L, 0L)));
		this.dataManager.register(TIER, 0);
	}
}

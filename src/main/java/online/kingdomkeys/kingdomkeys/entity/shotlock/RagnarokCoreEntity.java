package online.kingdomkeys.kingdomkeys.entity.shotlock;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
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
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.entity.ItemDropEntity;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.entity.organization.LaserDomeShotEntity;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class RagnarokCoreEntity extends ThrowableEntity {

	int maxTicks = 100;
	List<RagnarokShotEntity> list = new ArrayList<RagnarokShotEntity>();
	List<Entity> targetList = new ArrayList<Entity>();
	float dmg;

	public RagnarokCoreEntity(EntityType<? extends ThrowableEntity> type, World world) {
		super(type, world);
		this.preventEntitySpawning = true;
	}

	public RagnarokCoreEntity(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
		super(ModEntities.TYPE_SHOTLOCK_CIRCULAR.get(), world);
	}

	public RagnarokCoreEntity(World world) {
		super(ModEntities.TYPE_SHOTLOCK_CIRCULAR.get(), world);
		this.preventEntitySpawning = true;
	}

	public RagnarokCoreEntity(World world, PlayerEntity player, List<Entity> targets, float dmg) {
		super(ModEntities.TYPE_SHOTLOCK_CIRCULAR.get(), player, world);
		setCaster(player.getUniqueID());
		String targetIDS = "";
		for(Entity t : targets) {
			targetIDS+=","+t.getEntityId();
		}
		setTarget(targetIDS.substring(1));
		this.targetList = targets;
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

		world.addParticle(ParticleTypes.BUBBLE, getPosX(), getPosY(), getPosZ(), 0, 0, 0);

		double X = getPosX();
		double Y = getPosY()+1;
		double Z = getPosZ();
		
		if (getCaster() != null && getTargets() != null) {
			if (ticksExisted == 1) {
				world.playSound(null, this.getPosition(), ModSounds.laser.get(), SoundCategory.PLAYERS, 1, 1);
				for(int i = 0; i< getTargets().size();i++) {
					Entity target = getTargets().get(i);
					if(target != null) {
						RagnarokShotEntity bullet = new RagnarokShotEntity(world, getCaster(), target, dmg);
						float r = 0.3F;
						double offset_amount = -1.5;
						double alpha = Math.toRadians(getCaster().rotationYaw);                        
						double theta = 2 * Math.PI / getTargets().size();
						double x = X + offset_amount * Math.sin(alpha) + r * ((Math.cos(i * theta) + Math.sin(alpha) * Math.sin(alpha) * (1 - Math.cos(i * theta))) * Math.cos(alpha) + (-Math.cos(alpha) * Math.sin(alpha) * (1 - Math.cos(i * theta))) * Math.sin(alpha));
						double y = Y + r * ((Math.cos(alpha) * Math.sin(i * theta)) * Math.cos(alpha) + Math.sin(alpha) * Math.sin(i * theta) * Math.sin(alpha));
						double z = Z - offset_amount * Math.cos(alpha) + r * (-Math.cos(alpha) * Math.sin(alpha) * (1 - Math.cos(i * theta)) * Math.cos(alpha) + (Math.cos(i * theta) + Math.cos(alpha) * Math.cos(alpha) * (1 - Math.cos(i * theta))) * Math.sin(alpha));

						bullet.setPosition(x,y,z);
						bullet.setMaxTicks(maxTicks + 20);
						//bullet.shoot(this.getPosX() - bullet.getPosX(), this.getPosY() - bullet.getPosY(), this.getPosZ() - bullet.getPosZ(), 0.001f, 0);
						list.add(bullet);
						world.addEntity(bullet);
					}
				}
			} else if(ticksExisted > 4 && ticksExisted < 10) {
				for(int i = 0; i< list.size();i++) {
					RagnarokShotEntity bullet = list.get(i);
					float posI = i + ticksExisted*2;
					float r = 0.3F*ticksExisted;
					double offset_amount = -2;
					double alpha = Math.toRadians(getCaster().rotationYaw);                        
					double theta = 2 * Math.PI / getTargets().size();
					double x = X + offset_amount * Math.sin(alpha) + r * ((Math.cos(posI * theta) + Math.sin(alpha) * Math.sin(alpha) * (1 - Math.cos(posI * theta))) * Math.cos(alpha) + (-Math.cos(alpha) * Math.sin(alpha) * (1 - Math.cos(posI * theta))) * Math.sin(alpha));
					double y = Y + r * ((Math.cos(alpha) * Math.sin(posI * theta)) * Math.cos(alpha) + Math.sin(alpha) * Math.sin(posI * theta) * Math.sin(alpha));
					double z = Z - offset_amount * Math.cos(alpha) + r * (-Math.cos(alpha) * Math.sin(alpha) * (1 - Math.cos(posI * theta)) * Math.cos(alpha) + (Math.cos(posI * theta) + Math.cos(alpha) * Math.cos(alpha) * (1 - Math.cos(posI * theta))) * Math.sin(alpha));

					bullet.setPosition(x,y,z);		
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

	@Override
	public void writeAdditional(CompoundNBT compound) {
		super.writeAdditional(compound);
		if (this.dataManager.get(OWNER) != null) {
			compound.putString("OwnerUUID", this.dataManager.get(OWNER).get().toString());
			compound.putString("TargetsUUID", this.dataManager.get(TARGETS));
		}
	}

	@Override
	public void readAdditional(CompoundNBT compound) {
		super.readAdditional(compound);
		this.dataManager.set(OWNER, Optional.of(UUID.fromString(compound.getString("OwnerUUID"))));
		this.dataManager.set(TARGETS, compound.getString("TargetUUID"));
	}

	private static final DataParameter<Optional<UUID>> OWNER = EntityDataManager.createKey(RagnarokCoreEntity.class, DataSerializers.OPTIONAL_UNIQUE_ID);
	private static final DataParameter<String> TARGETS = EntityDataManager.createKey(RagnarokCoreEntity.class, DataSerializers.STRING);

	public PlayerEntity getCaster() {
		return this.getDataManager().get(OWNER).isPresent() ? this.world.getPlayerByUuid(this.getDataManager().get(OWNER).get()) : null;
	}

	public void setCaster(UUID uuid) {
		this.dataManager.set(OWNER, Optional.of(uuid));
	}

	public List<Entity> getTargets() {
		List<Entity> list = new ArrayList<Entity>();
		String[] ids = this.getDataManager().get(TARGETS).split(",");
		//System.out.println(this.getDataManager().get(TARGETS) + " : "+ ids.length);
		for(String id : ids) {
		//	System.out.println(id);
			if(!id.equals(""))
				list.add(world.getEntityByID(Integer.parseInt(id)));
		}
		return list;
	}

	public void setTarget(String lists) {
		this.dataManager.set(TARGETS, lists);
	}

	@Override
	protected void registerData() {
		this.dataManager.register(OWNER, Optional.of(new UUID(0L, 0L)));
		this.dataManager.register(TARGETS, "");
	}
}

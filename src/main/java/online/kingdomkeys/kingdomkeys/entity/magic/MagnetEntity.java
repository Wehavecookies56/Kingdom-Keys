package online.kingdomkeys.kingdomkeys.entity.magic;

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
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

import java.util.List;

public class MagnetEntity extends ThrowableEntity {

	int maxTicks = 100;
	PlayerEntity player;
	String caster;

	public MagnetEntity(EntityType<? extends ThrowableEntity> type, World world) {
		super(type, world);
		this.preventEntitySpawning = true;
	}

	public MagnetEntity(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
		super(ModEntities.TYPE_MAGNET.get(), world);
	}

	public MagnetEntity(World world) {
		super(ModEntities.TYPE_MAGNET.get(), world);
		this.preventEntitySpawning = true;
	}

	public MagnetEntity(World world, PlayerEntity player) {
		super(ModEntities.TYPE_MAGNET.get(), player, world);
		this.player = player;
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

		// world.addParticle(ParticleTypes.ENTITY_EFFECT, getPosX(), getPosY(),
		// getPosZ(), 1, 1, 0);
		world.addParticle(ParticleTypes.BUBBLE, getPosX(), getPosY(), getPosZ(), 0, 0, 0);

		if (ticksExisted >= 3 && ticksExisted % 4 == 0) {

			int radius = 2;
			double freq = 0.6;
			double X = getPosX();
			double Y = getPosY();
			double Z = getPosZ();

			for (double x = X - radius; x <= X + radius; x += freq) {
				for (double y = Y - radius; y <= Y + radius; y += freq) {
					for (double z = Z - radius; z <= Z + radius; z += freq) {
						if ((X - x) * (X - x) + (Y - y) * (Y - y) + (Z - z) * (Z - z) <= radius * radius) {
							world.addParticle(ParticleTypes.BUBBLE_POP, x, y + 1, z, 0, 0, 0);
						}
					}
				}
			}

			this.setMotion(0, 0, 0);
			this.velocityChanged = true;
			
			for (PlayerEntity playerFromList : world.getPlayers()) {
				if(playerFromList.getDisplayName().getFormattedText().equals(getCaster())) {
					player = playerFromList;
					break;
				}
			}			

			List<Entity> list = world.getEntitiesWithinAABBExcludingEntity(player, getBoundingBox().grow(6.0D, 4.0D, 6.0D).offset(-3.0D, -2.0D, -3.0D));
			if (!list.isEmpty()) {
				for (int i = 0; i < list.size(); i++) {
					Entity e = (Entity) list.get(i);
					if (e instanceof LivingEntity) {
						double d = e.getPosX() - getPosX();
						double d1 = e.getPosZ() - getPosZ();
						((LivingEntity) e).knockBack(e, 1, d, d1);
						if (e.getPosY() < this.getPosY() - 0.5) {
							e.setMotion(e.getMotion().x, 0.5F, e.getMotion().z);
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
}

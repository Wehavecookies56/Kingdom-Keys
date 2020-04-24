package online.kingdomkeys.kingdomkeys.entity.magic;

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
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

public class EntityReflect extends Entity {

	int maxTicks = 100;
	PlayerEntity player;

	public EntityReflect(EntityType<? extends Entity> type, World world) {
		super(type, world);
		this.preventEntitySpawning = true;
	}

	public EntityReflect(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
		super(ModEntities.TYPE_REFLECT, world);
	}

	public EntityReflect(World world) {
		super(ModEntities.TYPE_REFLECT, world);
		this.preventEntitySpawning = true;
	}

	public EntityReflect(World world, PlayerEntity player, double x, double y, double z) {
		super(ModEntities.TYPE_REFLECT, world);
		setPosition(x, y, z);
		this.player = player;
	}

	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
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

	float r = 1F;

	@Override
	public void tick() {
		if (this.ticksExisted > maxTicks) {
			this.remove();
		}

		r = 0.5F;
		double h = 3;

		for (double y = 0; y < h; y += 0.1) {
			for (int a = 1; a <= 360; a += 15) {
				double ra = (r + Math.abs(y - 1.5));
				double x = getPosX() + ra * Math.cos(Math.toRadians(a));
				double z = getPosZ() + ra * Math.sin(Math.toRadians(a));
				world.addParticle(ParticleTypes.HAPPY_VILLAGER, x, getPosY() + y, z, 0, 0, 0);
			}
		}

		super.tick();
	}

}

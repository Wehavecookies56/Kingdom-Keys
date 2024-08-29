package online.kingdomkeys.kingdomkeys.entity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class HeartEntity extends Entity {

	public final static int MAX_TICKS = 30;

	public HeartEntity(EntityType<? extends Entity> type, Level world) {
		super(type, world);
		this.blocksBuilding = true;
	}

	public HeartEntity(Level world) {
		this(ModEntities.TYPE_HEART.get(), world);
	}

	@Override
	public void tick(){
		if(this.tickCount < MAX_TICKS - 10) {
			this.setPos(getX(), getY() + 0.15, getZ());
			//this.posY += 0.15;
		} else {
			//KingdomKeys.proxy.spawnDarkSmokeParticle(world, getPosX(), getPosY(), getPosZ(), 0, 0, 0, 0.1F);
			level().addParticle(ParticleTypes.DRAGON_BREATH, getX(), getY(), getZ(), 0, 0, 0);
		}
		
		if(this.tickCount >= MAX_TICKS) {
			this.remove(RemovalReason.KILLED);
		}
		
		super.tick();
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {

	}

	@Override
	protected void readAdditionalSaveData(CompoundTag compound) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag compound) {
		// TODO Auto-generated method stub
		
	}

}

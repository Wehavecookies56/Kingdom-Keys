package online.kingdomkeys.kingdomkeys.entity;

import java.util.Optional;
import java.util.UUID;

import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;

public class XPEntity extends Entity {

	public final static int MAX_TICKS = 30;
	public double xp;
	
	public XPEntity(EntityType<? extends Entity> type, Level world) {
		super(type, world);
		this.blocksBuilding = true;
	}

	public XPEntity(PlayMessages.SpawnEntity spawnEntity, Level world) {
		super(ModEntities.TYPE_XP.get(), world);
	}

	public XPEntity(Level world) {
		super(ModEntities.TYPE_HEART.get(), world);
		this.blocksBuilding = true;
	}

	public XPEntity(Level level, Player player, LivingEntity mob, double exp) {
		super(ModEntities.TYPE_XP.get(), level);
		this.setPos(mob.getX()+0.5,mob.getY(), mob.getZ()+0.5);
		setExp((int) exp);
		setCaster(player.getUUID());		
	}

	@Override
	public void tick(){
		if(this.tickCount < MAX_TICKS - 10) {
			//this.setPos(getX(), getY() + 0.15, getZ());
			//this.posY += 0.15;
		} else {
			//KingdomKeys.proxy.spawnDarkSmokeParticle(world, getPosX(), getPosY(), getPosZ(), 0, 0, 0, 0.1F);
			//level.addParticle(ParticleTypes.DRAGON_BREATH, getX(), getY(), getZ(), 0, 0, 0);
		}
		
		if(this.tickCount >= MAX_TICKS) {
			this.remove(RemovalReason.KILLED);
		}
		
		super.tick();
	}
	
	private static final EntityDataAccessor<Optional<UUID>> OWNER = SynchedEntityData.defineId(XPEntity.class, EntityDataSerializers.OPTIONAL_UUID);
	private static final EntityDataAccessor<Integer> EXP = SynchedEntityData.defineId(XPEntity.class, EntityDataSerializers.INT);

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		if (this.entityData.get(OWNER) != null) {
			compound.putString("OwnerUUID", this.entityData.get(OWNER).get().toString());
		}
		if (this.entityData.get(EXP) != null) {
			compound.putInt("exp", this.entityData.get(EXP));
		}
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		this.entityData.set(OWNER, Optional.of(UUID.fromString(compound.getString("OwnerUUID"))));
		this.entityData.set(EXP, compound.getInt("exp"));
	}

	public Player getCaster() {
		return this.getEntityData().get(OWNER).isPresent() ? this.level().getPlayerByUUID(this.getEntityData().get(OWNER).get()) : null;
	}

	public void setCaster(UUID uuid) {
		this.entityData.set(OWNER, Optional.of(uuid));
	}
	
	public int getExp() {
		return this.getEntityData().get(EXP);
	}

	public void setExp(int exp) {
		this.entityData.set(EXP, exp);
	}

	@Override
	protected void defineSynchedData() {
		this.entityData.define(OWNER, Optional.of(Util.NIL_UUID));
		this.entityData.define(EXP, 0);
	}


	@Override
	public Packet<ClientGamePacketListener> getAddEntityPacket() {
		return (Packet<ClientGamePacketListener>) NetworkHooks.getEntitySpawningPacket(this);
	}


}

package online.kingdomkeys.kingdomkeys.entity.block;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.Pose;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.BooleanProperty;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;
import online.kingdomkeys.kingdomkeys.block.PairBloxBlock;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

/**
 * Mostly a copy of {@link net.minecraft.entity.item.TNTEntity} with some small
 * changes
 */
public class PairBloxEntity extends Entity {

	private static final DataParameter<Integer> PAIR = EntityDataManager.createKey(PairBloxEntity.class, DataSerializers.VARINT);

	private int pair = 0;
	
	@Nullable
	private LivingEntity placedBy;

	public PairBloxEntity(EntityType<? extends Entity> type, World world) {
		super(type, world);
		this.preventEntitySpawning = true;
	}

	public PairBloxEntity(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
		super(ModEntities.TYPE_PAIR_BLOX.get(), world);
	}

	public PairBloxEntity(World world, double x, double y, double z, int pair) {
		this(ModEntities.TYPE_PAIR_BLOX.get(), world);
		this.setPosition(x+0.5, y, z+0.5);
		this.setPair(pair);
		this.prevPosX = x+0.5;
		this.prevPosY = y;
		this.prevPosZ = z+0.5;
	//	System.out.println("Pair: "+getPair());
		//this.placedBy = igniter;
	}

	@Override
	protected void registerData() {
		this.dataManager.register(PAIR, 0);
	}

	@Override
	protected boolean canTriggerWalking() {
		return true;
	}

	@Override
	public boolean canBeCollidedWith() {
		return this.isAlive();
	}

	@Override
	public void tick() {
		this.prevPosX = this.getPosition().getX();
		this.prevPosY = this.getPosition().getY();
		this.prevPosZ = this.getPosition().getZ();
		
		this.move(MoverType.SELF, this.getMotion());
		
		this.handleWaterMovement();
		this.world.addParticle(ParticleTypes.FLAME, this.getPosition().getX(), this.getPosition().getY() + 0.5D, this.getPosition().getZ(), 0.0D, 0.0D, 0.0D);
		
		if(ticksExisted >= 5) {
			this.setMotion(0, -10, 0);
			this.world.setBlockState(this.getPosition(), ModBlocks.pairBlox.get().getDefaultState().with(PairBloxBlock.PAIR, getPair()));
			this.remove();
		}
	}

	@Override
	protected void writeAdditional(CompoundNBT compound) {
		compound.putInt("Pair", this.getPair());
	}

	@Override
	protected void readAdditional(CompoundNBT compound) {
		this.setPair(compound.getInt("Pair"));
	}

	/*@Nullable
	public LivingEntity getPlacedBy() {
		return this.placedBy;
	}*/

	@Override
	protected float getEyeHeight(Pose pose, EntitySize entitySize) {
		return 0.0F;
	}

	public void setPair(int pair) {
		this.dataManager.set(PAIR, pair);
		this.pair = pair;
	}

	@Override
	public void notifyDataManagerChange(DataParameter<?> key) {
		if (PAIR.equals(key)) {
			this.pair = this.getPairDataManager();
		}
	}

	public int getPairDataManager() {
		return this.dataManager.get(PAIR);
	}

	public int getPair() {
		return this.pair;
	}

	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}

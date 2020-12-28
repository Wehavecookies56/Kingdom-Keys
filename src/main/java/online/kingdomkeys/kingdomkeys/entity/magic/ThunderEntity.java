package online.kingdomkeys.kingdomkeys.entity.magic;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap.Type;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

public class ThunderEntity extends ThrowableEntity {

	int maxTicks = 60;
	PlayerEntity player;
	String caster;

	public ThunderEntity(EntityType<? extends ThrowableEntity> type, World world) {
		super(type, world);
		this.preventEntitySpawning = true;
	}

	public ThunderEntity(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
		super(ModEntities.TYPE_THUNDER.get(), world);
	}

	public ThunderEntity(World world) {
		super(ModEntities.TYPE_THUNDER.get(), world);
		this.preventEntitySpawning = true;
	}

	public ThunderEntity(World world, PlayerEntity player) {
		super(ModEntities.TYPE_THUNDER.get(), player, world);
		//setPosition(x, y + 10, z);
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

	@Override
	public void tick() {
		if (this.ticksExisted > maxTicks) {
			this.remove();
		}
		
		for (PlayerEntity playerFromList : world.getPlayers()) {
			if(playerFromList.getDisplayName().getFormattedText().equals(getCaster())) {
				player = playerFromList;
				break;
			}
		}	
		
		if(!world.isRemote) { //Only calculate and spawn lightning bolts server side
        	if(ticksExisted % 5 == 0) {
        		double radius = 2.0D;
				//List<Entity> list = world.getEntitiesWithinAABBExcludingEntity(player, player.getBoundingBox().grow(4.0D, 4.0D, 4.0D).offset(-2.0D, -1.0D, -2.0D));
				List<Entity> list = this.world.getEntitiesInAABBexcluding(player, new AxisAlignedBB(this.getPosX() - radius, this.getPosY() - radius, this.getPosZ() - radius, this.getPosX() + radius, this.getPosY() + 6.0D + radius, this.getPosZ() + radius), Entity::isAlive);

		        if (!list.isEmpty() && list.get(0) != this) {
		            for (int i = 0; i < list.size(); i++) {
		                Entity e = (Entity) list.get(i);
		                if (e instanceof LivingEntity) {
		                	ThunderBoltEntity shot = new ThunderBoltEntity(player.world, player, e.getPosX(), e.getPosY(), e.getPosZ());
		                	shot.setCaster(getCaster());
		            		world.addEntity(shot);
		            		
			    			LightningBoltEntity lightning = new LightningBoltEntity(player.world, e.getPosX(), e.getPosY(), e.getPosZ(), true);
			    			((ServerWorld)player.world).addLightningBolt(lightning);
		                }
		            }
		        } else {
		        	int x = (int) player.getPosX();
		        	int z = (int) player.getPosZ();
		        	
	        		int posX = x + player.world.rand.nextInt(6) - 3;
		        	int posZ = z + player.world.rand.nextInt(6) - 3;
		        	
		        	ThunderBoltEntity shot = new ThunderBoltEntity(player.world, player, posX, player.world.getHeight(Type.WORLD_SURFACE, posX, posZ), posZ);
            		shot.setCaster(getCaster());
            		world.addEntity(shot);
            		
	    			LightningBoltEntity lightning = new LightningBoltEntity(player.world, posX, player.world.getHeight(Type.WORLD_SURFACE, posX, posZ), posZ, true);
	    			((ServerWorld)player.world).addLightningBolt(lightning);
	    		}
        	}
        }

		super.tick();
	}

	@Override
	protected void onImpact(RayTraceResult result) {
		// TODO Auto-generated method stub
		
	}

}

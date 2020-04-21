package online.kingdomkeys.kingdomkeys.entity.magic;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

public class EntityStop extends Entity {

	int maxTicks = 100;
	PlayerEntity player;

	public EntityStop(EntityType<? extends Entity> type, World world) {
		super(type, world);
		this.preventEntitySpawning = true;
	}

	public EntityStop(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
		super(ModEntities.TYPE_STOP, world);
	}

	public EntityStop(World world) {
		super(ModEntities.TYPE_STOP, world);
		this.preventEntitySpawning = true;
	}

	public EntityStop(World world, PlayerEntity player) {
		super(ModEntities.TYPE_STOP, world);
		this.player = player;
		this.prevPosX = player.getPosX();
        this.prevPosY = player.getPosY();
        this.prevPosZ = player.getPosZ();
	}

	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	public void tick() {
		System.out.println("a");
		if (this.ticksExisted > maxTicks) {
			this.remove();
		}

		world.addParticle(ParticleTypes.ENTITY_EFFECT, getPosX(), getPosY(), getPosZ(), 1, 1, 0);
		world.addParticle(ParticleTypes.SMOKE, getPosX(), getPosY(), getPosZ(), 0, 0, 0);
		
		if (player == null)
            return;
		
        //int rotation = 0;
        //if (!world.isRemote)
        //	PacketDispatcher.sendToAllAround(new SpawnStopParticles(this, 1), player, 64.0D);

        //this.rotationYaw = (rotation + 1) % 360;
       
        /*if (ticksExisted < 10)
            player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0D);
        else
            player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.10000000149011612D);*/

        List<Entity> list = player.world.getEntitiesWithinAABBExcludingEntity(player, player.getBoundingBox().grow(16.0D, 10.0D, 16.0D).offset(-8.0D, -5.0D, -8.0D));
        if (!list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                Entity e = (Entity) list.get(i);

                if (e instanceof LivingEntity) {
                    if (ticksExisted < maxTicks) {
                        ((LivingEntity) e).setMotion(new Vec3d(0, 0, 0));
                    }
                }
               /* if(e instanceof EntityPlayerMP){
                    ((EntityPlayerMP)e).getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0D);

                }*/
                /*if (!world.isRemote) {
                    if (ticksExisted < 50) {
                        ((EntityPlayerMP) player).connection.sendPacket(new SPacketEntityVelocity(e.getEntityId(), 0, 0, 0));
                    }
                }*/
            }
        }

		super.tick();
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

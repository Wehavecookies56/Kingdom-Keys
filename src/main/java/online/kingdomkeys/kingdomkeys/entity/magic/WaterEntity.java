package online.kingdomkeys.kingdomkeys.entity.magic;

import java.util.List;

import net.minecraft.block.Blocks;
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
import net.minecraft.util.math.*;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;
import online.kingdomkeys.kingdomkeys.lib.Party;

public class WaterEntity extends ThrowableEntity {

	int maxTicks = 100;
	PlayerEntity player;
	String caster;

	public WaterEntity(EntityType<? extends ThrowableEntity> type, World world) {
		super(type, world);
		this.preventEntitySpawning = true;
	}

	public WaterEntity(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
		super(ModEntities.TYPE_WATER.get(), world);
	}

	public WaterEntity(World world) {
		super(ModEntities.TYPE_WATER.get(), world);
		this.preventEntitySpawning = true;
	}

	public WaterEntity(World world, PlayerEntity player) {
		super(ModEntities.TYPE_WATER.get(), player, world);
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
	
	double a = 0;

	@Override
	public void tick() {
		for (PlayerEntity playerFromList : world.getPlayers()) {
			if(playerFromList.getDisplayName().getString().equals(getCaster())) {
				player = playerFromList;
				break;
			}
		}	
		
		if (this.ticksExisted > maxTicks || player == null) {
			this.remove();
		}
		
		if(ticksExisted <= 1) {
			this.setMotion(0, 0, 0);
			
		} else if (ticksExisted < 50) { //Shield
			setPosition(player.getPosX(), getPosY(), player.getPosZ());
			double r = 1D;
			double cx = getPosX();
			double cy = getPosY();
			double cz = getPosZ();

			a+=30; //Speed and distance between particles
			double x = cx + (r * Math.cos(Math.toRadians(a)));
			double z = cz + (r * Math.sin(Math.toRadians(a)));

			double x2 = cx + (r * Math.cos(Math.toRadians(-a)));
			double z2 = cz + (r * Math.sin(Math.toRadians(-a)));

		//	System.out.println(a / 180 / 2);
			world.addParticle(ParticleTypes.DRIPPING_WATER, x, (cy+0.5) - a / 1080D, z, 0.0D, 0.0D, 0.0D);
			world.addParticle(ParticleTypes.DOLPHIN, x2, (cy+0.5) - a / 1080D, z2, 0.0D, 0.0D, 0.0D);
			
    		double radius = 2.0D;
			List<Entity> list = this.world.getEntitiesInAABBexcluding(player, new AxisAlignedBB(this.getPosX() - radius, this.getPosY() - radius, this.getPosZ() - radius, this.getPosX() + radius, this.getPosY() + 6.0D + radius, this.getPosZ() + radius), Entity::isAlive);

	        if (!list.isEmpty() && list.get(0) != this) {
				float dmg = DamageCalculation.getMagicDamage((PlayerEntity) this.func_234616_v_(), 1);
	            for (int i = 0; i < list.size(); i++) {
	                Entity e = (Entity) list.get(i);
	                if (e instanceof LivingEntity) {
						e.attackEntityFrom(DamageSource.causeThrownDamage(this, (PlayerEntity) this.func_234616_v_()), dmg);
	                }
	            }
	        }

		} else { //Projectile
			func_234612_a_(player, player.rotationPitch, player.rotationYaw, 0, 2F, 0);

			velocityChanged = true;
			for(double px = -0.3;px < 0.3;px+=0.1) {
				for(double pz = -0.3;pz < 0.3;pz+=0.1) {
					world.addParticle(ParticleTypes.DOLPHIN, getPosX()+px, getPosY(), getPosZ()+pz, 0, 0, 0);
				}
			}

		}

		super.tick();
	}

	@Override
	protected void onImpact(RayTraceResult rtRes) {
		if (!world.isRemote) {

			EntityRayTraceResult ertResult = null;
			BlockRayTraceResult brtResult = null;

			if (rtRes instanceof EntityRayTraceResult) {
				ertResult = (EntityRayTraceResult) rtRes;
			}

			if (rtRes instanceof BlockRayTraceResult) {
				brtResult = (BlockRayTraceResult) rtRes;
			}

			if (ertResult != null && ertResult.getEntity() != null && ertResult.getEntity() instanceof LivingEntity) {

				LivingEntity target = (LivingEntity) ertResult.getEntity();
				
				if (target != func_234616_v_()) {
					Party p = null;
					if (func_234616_v_() != null) {
						p = ModCapabilities.getWorld(func_234616_v_().world).getPartyFromMember(func_234616_v_().getUniqueID());
					}
					if(p == null || (p.getMember(target.getUniqueID()) == null || p.getFriendlyFire())) { //If caster is not in a party || the party doesn't have the target in it || the party has FF on
						float dmg = this.func_234616_v_() instanceof PlayerEntity ? DamageCalculation.getMagicDamage((PlayerEntity) this.func_234616_v_(), 1) : 2;
						target.attackEntityFrom(DamageSource.causeThrownDamage(this, this.func_234616_v_()), dmg);
						remove();
					}
				}
			} else { // Block (not ERTR)

				/*if (brtResult != null && rtRes.getType() == Type.BLOCK) {
					BlockPos hitPos = brtResult.getPos();
					System.out.println(world.getBlockState(hitPos).getBlockState());
					if (world.getBlockState(hitPos).getBlockState() == Blocks.WATER.getDefaultState()) {
						System.out.println("water");
					}
				} else {
					// world.playSound(null, getPosition(), ModSounds.fistBounce,
					// SoundCategory.MASTER, 1F, 1F);
				}*/

				remove();
			}
		}

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

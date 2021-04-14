package online.kingdomkeys.kingdomkeys.entity.organization;

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
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;

public class ArrowgunShotEntity extends ThrowableEntity {

	int maxTicks = 120;

	public ArrowgunShotEntity(EntityType<? extends ThrowableEntity> type, World world) {
		super(type, world);
		this.preventEntitySpawning = true;
	}

	public ArrowgunShotEntity(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
		super(ModEntities.TYPE_ARROWGUN_SHOT.get(), world);
	}

	public ArrowgunShotEntity(World world) {
		super(ModEntities.TYPE_ARROWGUN_SHOT.get(), world);
		this.preventEntitySpawning = true;
	}

	public ArrowgunShotEntity(World world, LivingEntity player) {
		super(ModEntities.TYPE_ARROWGUN_SHOT.get(), player, world);
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

		//if(ticksExisted > 1)
			//world.addParticle(ParticleTypes.ENTITY_EFFECT, getPosX(), getPosY(), getPosZ(), 1, 0, 0);
		
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

			if (ertResult != null && ertResult.getEntity() instanceof LivingEntity) {

				LivingEntity target = (LivingEntity) ertResult.getEntity();

				if (target != getShooter()) {
					float dmg = 0;
					if(this.getShooter() instanceof PlayerEntity) {
						PlayerEntity player = (PlayerEntity) this.getShooter();
						IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
						if(player.getHeldItemMainhand() != null) {
							dmg = DamageCalculation.getOrgStrengthDamage(player, player.getHeldItemMainhand()) / 2;
						}
					}
					target.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getShooter()), dmg);
					remove();
				}
			} else { // Block (not ERTR)
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

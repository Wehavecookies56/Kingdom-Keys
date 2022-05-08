package online.kingdomkeys.kingdomkeys.entity.organization;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;

public class ArrowgunShotEntity extends ThrowableProjectile {

	int maxTicks = 120;

	public ArrowgunShotEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
		super(type, world);
		this.blocksBuilding = true;
	}

	public ArrowgunShotEntity(PlayMessages.SpawnEntity spawnEntity, Level world) {
		super(ModEntities.TYPE_ARROWGUN_SHOT.get(), world);
	}

	public ArrowgunShotEntity(Level world) {
		super(ModEntities.TYPE_ARROWGUN_SHOT.get(), world);
		this.blocksBuilding = true;
	}

	public ArrowgunShotEntity(Level world, LivingEntity player) {
		super(ModEntities.TYPE_ARROWGUN_SHOT.get(), player, world);
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	protected float getGravity() {
		return 0F;
	}

	@Override
	public void tick() {
		if (this.tickCount > maxTicks) {
			this.remove(RemovalReason.KILLED);
		}

		//if(ticksExisted > 1)
			//world.addParticle(ParticleTypes.ENTITY_EFFECT, getPosX(), getPosY(), getPosZ(), 1, 0, 0);
		
		super.tick();
	}

	@Override
	protected void onHit(HitResult rtRes) {
		if (!level.isClientSide) {

			EntityHitResult ertResult = null;
			BlockHitResult brtResult = null;

			if (rtRes instanceof EntityHitResult) {
				ertResult = (EntityHitResult) rtRes;
			}

			if (rtRes instanceof BlockHitResult) {
				brtResult = (BlockHitResult) rtRes;
			}

			if (ertResult != null && ertResult.getEntity() instanceof LivingEntity) {

				LivingEntity target = (LivingEntity) ertResult.getEntity();

				if (target != getOwner()) {
					float dmg = 0;
					if(this.getOwner() instanceof Player) {
						Player player = (Player) this.getOwner();
						if(player.getMainHandItem() != null) {
							dmg = DamageCalculation.getOrgStrengthDamage(player, player.getMainHandItem()) / 3;
						}
					}
					target.hurt(DamageSource.thrown(this, this.getOwner()), dmg);
					remove(RemovalReason.KILLED);
				}
			} else { // Block (not ERTR)
				remove(RemovalReason.KILLED);
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
	public void addAdditionalSaveData(CompoundTag compound) {
		// compound.putInt("lvl", this.getLvl());
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		// this.setLvl(compound.getInt("lvl"));
	}

	@Override
	protected void defineSynchedData() {
		// TODO Auto-generated method stub

	}
}

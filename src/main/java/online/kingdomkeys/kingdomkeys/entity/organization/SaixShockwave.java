package online.kingdomkeys.kingdomkeys.entity.organization;

import org.joml.Vector3f;

import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

public class SaixShockwave extends ThrowableProjectile {

	int maxTicks = 30;
	float dmg;

	public SaixShockwave(EntityType<? extends ThrowableProjectile> type, Level world) {
		super(type, world);
		this.blocksBuilding = true;
	}

	public SaixShockwave(PlayMessages.SpawnEntity spawnEntity, Level world) {
		super(ModEntities.TYPE_SAIX_SHOCKWAVE.get(), world);
	}

	public SaixShockwave(Level world, LivingEntity player, float damage, double x, double y, double z) {
		this(world, player, damage);
		this.setPos(x, y, z);
	}

	public SaixShockwave(Level world) {
		super(ModEntities.TYPE_SAIX_SHOCKWAVE.get(), world);
		this.blocksBuilding = true;
	}

	public SaixShockwave(Level world, LivingEntity player, float damage) {
		super(ModEntities.TYPE_SAIX_SHOCKWAVE.get(), player, world);
		this.dmg = damage;
	}

	@Override
	public Packet<ClientGamePacketListener> getAddEntityPacket() {
		return (Packet<ClientGamePacketListener>) NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	protected float getGravity() {
		return 0.25F;
	}

	@Override
	public void tick() {
		if (this.tickCount > maxTicks) {
			this.remove(RemovalReason.KILLED);
		}

		if (tickCount > 2) {
			level().addParticle(ParticleTypes.SQUID_INK, getX(), getY() + 0.15, getZ(), 0, 0, 0);
			level().addParticle(new DustParticleOptions(new Vector3f(1F, 1F, 1F), 8F), getX(), getY() + 0.25, getZ(), 1, 0, 0);
			level().addParticle(new DustParticleOptions(new Vector3f(0.8F, 0F, 0.8F), 6F), getX(), getY() + 0.60, getZ(), 1, 0, 0);
			level().addParticle(new DustParticleOptions(new Vector3f(0.6F, 0F, 0.8F), 6F), getX(), getY() + 0.90, getZ(), 1, 0, 0);
			level().addParticle(new DustParticleOptions(new Vector3f(0.8F, 0F, 0.8F), 6F), getX(), getY() + 1.15, getZ(), 1, 0, 0);
			level().addParticle(new DustParticleOptions(new Vector3f(0.6F, 0F, 0.8F), 6F), getX(), getY() + 1.60, getZ(), 1, 0, 0);
		}
		super.tick();
	}

	@Override
	protected void onHit(HitResult rtRes) {
		if (!level().isClientSide) {
			if (rtRes instanceof EntityHitResult ertResult) {
				if (ertResult != null && ertResult.getEntity() instanceof LivingEntity) {

					LivingEntity target = (LivingEntity) ertResult.getEntity();

					if (target != getOwner()) {
						target.hurt(target.damageSources().thrown(this, this.getOwner()), dmg);
					}
				}
			}

			if (rtRes instanceof BlockHitResult brtResult) {
				// Glide on Ground
				if (brtResult.getType() == Type.BLOCK) {
					System.out.println(brtResult.getDirection());
					if (!(brtResult.getDirection() == Direction.UP || brtResult.getDirection() == Direction.DOWN)) {
						// this.setPos(getX(), getY() + 0.1, getZ());

						Vec3 mot = getDeltaMovement();

						double x = mot.x();
						double y = mot.y();
						double z = mot.z();

						this.setDeltaMovement(x, y * 1, z);
					}
				}
			}

		}
	}

	@Override
	protected void defineSynchedData() {

	}
}

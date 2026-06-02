package online.kingdomkeys.kingdomkeys.entity.magic;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import online.kingdomkeys.kingdomkeys.damagesource.KKDamageTypes;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.util.Utils;
import org.joml.Vector3f;

import java.util.List;

public class MineEntity extends ThrowableProjectile {
	private static final int TRIGGER_RADIUS = 1;
	private static final int DAMAGE_RADIUS = 2;
	public float visualRotation = 0f;
	float dmg;
	float dmgMult;
	LivingEntity closest = null;
	private int maxTicks = 200;
	private String caster;
	private boolean ignoreExplosion = true;
	private boolean settled = false;
	private boolean seeker = false;
	private boolean armed = false;


	public MineEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
		super(type, world);
		this.blocksBuilding = true;
	}


	public MineEntity(Level world, LivingEntity player, float damage, float dmgMult) {
		super(ModEntities.TYPE_MINE.get(), player, world);
		this.dmg = damage;
		this.blocksBuilding = true;
	}

	@Override
	protected double getDefaultGravity() {
		return armed ? 0 : 0.25;
	}


	public boolean getSeeker() {
		return seeker;
	}

	public void setSeeker(boolean seekerMode) {
		this.seeker = seekerMode;
	}

	@Override
	public void push(double x, double y, double z) {
		// Prevent all physical pushes
	}

	@Override
	public void addDeltaMovement(Vec3 vec) {
		// Prevent all velocity changes
	}

	protected boolean doPush(Entity entity) {
		return false;
	}

	public boolean isSettled() {
		return this.seeker;
	}

	public void setMaxTicks(int setMax) {
		this.maxTicks = setMax;
	}


	@Override
	public void tick() {
		if (!level().isClientSide && !settled) {
			if (level().getBlockState(blockPosition()).isSolidRender(level(), blockPosition())) {
				setPos(getX(), getY() + 0.5D, getZ());
				return;
			}
			settleToGround();
		}
		this.setDeltaMovement(Vec3.ZERO);
		this.hasImpulse = false;

		// Seeker Mines
		if (seeker && settled) {
			int searchRadius = 6;
			List<LivingEntity> targets = level().getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(searchRadius), e -> e != getOwner() && e.isAlive());

			Party casterParty = WorldData.get(level().getServer()).getPartyFromMember(getOwner().getUUID());
			if (casterParty != null && !casterParty.getFriendlyFire()) {
				targets.removeIf(e -> casterParty.getMember(e.getUUID()) != null);
			}

			LivingEntity closest = null;
			double minDist = Double.MAX_VALUE;
			for (LivingEntity t : targets) {
				double dist = t.distanceToSqr(this);
				if (dist < minDist) {
					minDist = dist;
					closest = t;
				}
			}

			// Apply controlled movement toward target
			if (closest != null) {
				Vec3 direction = new Vec3(closest.getX() - this.getX(), closest.getY() - this.getY(), closest.getZ() - this.getZ()).normalize();

				double speed = 0.15; // tweak for KH feel
				setDeltaMovement(direction.scale(speed));
			}
		}

		super.tick();

		if (this.tickCount > maxTicks) {
			level().explode(this.getOwner(), this.blockPosition().getX(), this.blockPosition().getY() + (double) (this.getBbHeight() / 16.0F), this.blockPosition().getZ(), 3, false, Level.ExplosionInteraction.NONE);
			this.remove(RemovalReason.KILLED);
		}


		//world.addParticle(ParticleTypes.ENTITY_EFFECT, getPosX(), getPosY(), getPosZ(), 1, 1, 0);
		if (tickCount > 0) {

			level().addAlwaysVisibleParticle(new DustParticleOptions(new Vector3f(0.0F, 1F, 0.0F), 1F), getX() + level().random.nextDouble() - 0.5D, getY() + level().random.nextDouble() * 2D, getZ() + level().random.nextDouble() - 0.5D, 0, 0, 0);
			if (!seeker) {
				level().addAlwaysVisibleParticle(new DustParticleOptions(new Vector3f(1F, 1F, 0F), 1F), getX() + level().random.nextDouble() - 0.5D, getY() + level().random.nextDouble() * 2D, getZ() + level().random.nextDouble() - 0.5D, 0, 0, 0);
			} else {

				level().addAlwaysVisibleParticle(new DustParticleOptions(new Vector3f(1F, 0F, 0F), 1F), getX() + level().random.nextDouble() - 0.5D, getY() + level().random.nextDouble() * 2D, getZ() + level().random.nextDouble() - 0.5D, 0, 0, 0);
			}
		}


		if (tickCount > 5) {
			if (!armed) {
				setPos(getX(), Math.floor(getY()) + 0.01D, getZ());
				armed = true;
				return;
			}

			// Step-on trigger
			AABB triggerBox = new AABB(getX() - TRIGGER_RADIUS, getY(), getZ() - TRIGGER_RADIUS, getX() + TRIGGER_RADIUS, getY() + 0.5, getZ() + TRIGGER_RADIUS);

			List<LivingEntity> entities = level().getEntitiesOfClass(LivingEntity.class, triggerBox, e -> e != getOwner() && e.isAlive());

			for (LivingEntity target : entities) {
				if (canDamage(target)) {
					explode();
					break;
				}
			}

			//level().playSound(null, this.getX(),this.getY(),this.getZ(), ModSoundsRM.DARK_MINE_ALIVE.get(), SoundSource.NEUTRAL,1F,1F);
		}
	}

	private void settleToGround() {
		BlockHitResult hit = level().clip(new ClipContext(position(), position().add(0, -6, 0), // search downward
				ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));

		if (hit.getType() == HitResult.Type.BLOCK) {
			Vec3 pos = hit.getLocation();

			setPos(pos.x, hit.getBlockPos().getY() + 1.01D, pos.z);

			setDeltaMovement(Vec3.ZERO);
			setNoGravity(true);
			settled = true;
		}
	}

	private boolean ignoreExplosion() {
		return true;
	}

	private boolean canDamage(LivingEntity target) {
		if (getOwner() == null) return true;

		Party party = WorldData.get(level().getServer()).getPartyFromMember(getOwner().getUUID());

		return party == null || party.getMember(target.getUUID()) == null || party.getFriendlyFire();
	}

	private void explode() {
		if (!(getOwner() instanceof Player player)) {
			discard();
			return;
		}

		float damage = DamageCalculation.getMagicDamage(player);

		List<LivingEntity> targets = Utils.getLivingEntitiesInRadiusExcludingParty(player, this, DAMAGE_RADIUS, DAMAGE_RADIUS, DAMAGE_RADIUS);

		for (LivingEntity target : targets) {
			target.hurt(KKDamageTypes.getElementalDamage(KKDamageTypes.FIRE, this, getOwner()), damage);
			target.invulnerableTime = 0;
		}

		level().explode(getOwner(), getX(), getY(), getZ(), DAMAGE_RADIUS, false, Level.ExplosionInteraction.NONE);
		discard();
	}


	@Override
	protected void onHit(HitResult rtRes) {
		if (!level().isClientSide && getOwner() != null) {
			EntityHitResult ertResult = null;
			BlockHitResult brtResult = null;

			int radius = 3;

			if (rtRes instanceof EntityHitResult) {
				ertResult = (EntityHitResult) rtRes;
			}

			if (rtRes instanceof BlockHitResult) {
				brtResult = (BlockHitResult) rtRes;
			}

			if (ertResult != null && ertResult.getEntity() instanceof LivingEntity) {
				LivingEntity target = (LivingEntity) ertResult.getEntity();

				if (target != getOwner()) {
					Party p = null;
					if (getOwner() != null) {
						p = WorldData.get(getOwner().getServer()).getPartyFromMember(getOwner().getUUID());
					}
					if (p == null || (p.getMember(target.getUUID()) == null || p.getFriendlyFire())) { //If caster is not in a party || the party doesn't have the target in it || the party has FF on
						float dmg = this.getOwner() instanceof Player ? DamageCalculation.getMagicDamage((Player) this.getOwner()) : 2;
						Player player = (Player) this.getOwner();
						//target.hurt(DarknessDamageSource.getDarknessDamage(this, this.getOwner()), dmg * dmgMult);
						if (this.getOwner() instanceof Player) {
							List<LivingEntity> targetList = Utils.getLivingEntitiesInRadiusExcludingParty((Player) this.getOwner(), this, radius, radius, radius);
							for (LivingEntity e : targetList) {
								e.hurt(KKDamageTypes.getElementalDamage(KKDamageTypes.FIRE, this, this.getOwner()), dmg);
								e.invulnerableTime = 0;
							}
						}
						level().explode(this.getOwner(), this.blockPosition().getX(), this.blockPosition().getY() + (double) (this.getBbHeight() / 16.0F), this.blockPosition().getZ(), radius, false, Level.ExplosionInteraction.NONE);
						PlayerData playerData = PlayerData.get(player);
						remove(RemovalReason.KILLED);

					}
				}
			}

			if (brtResult != null) {
				setDeltaMovement(0, 0, 0);
			}
		}

	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {

	}

	public void setCaster(String caster) {
		this.caster = caster;
	}

	public boolean isIgnoreExplosion() {
		return ignoreExplosion;
	}

	public void setIgnoreExplosion(boolean ignoreExplosion) {
		this.ignoreExplosion = ignoreExplosion;
	}
}


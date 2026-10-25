package online.kingdomkeys.kingdomkeys.entity.magic;

import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.particles.ModParticles;
import online.kingdomkeys.kingdomkeys.damagesource.KKDamageTypes;
import online.kingdomkeys.kingdomkeys.effects.ModMobEffects;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.entity.ProjectileHits;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

// Fire, Fira and Firaga Barrier and Raging Storm: flames spun around the caster that burn whoever they touch
@EventBusSubscriber
public class FireBarrierEntity extends BaseMagicProjectile {

	public enum Tier {
		FIRE_BARRIER(ModEntities.TYPE_FIRE_BARRIER::get, 20, 24, 0.6F, 0, 8, 40, true, false, new Orbit(1.4F, 1F, 0, 0, 0)),
		FIRA_BARRIER(ModEntities.TYPE_FIRA_BARRIER::get, 20, 24, 0.6F, 0, 8, 60, true, false, new Orbit(1.4F, 1F, 30, 0, 0), new Orbit(1.4F, 1F, 30, 90, 180)),
		FIRAGA_BARRIER(ModEntities.TYPE_FIRAGA_BARRIER::get, 20, 24, 0.7F, 0, 10, 100, true, false, new Orbit(2.2F, 1F, 0, 0, 0), new Orbit(1.8F, 1.8F, 0, 0, 180)),
		RAGING_STORM(ModEntities.TYPE_RAGING_STORM::get, 200, 4, 0.7F, 3F, 0, 100, false, true, new Orbit(2.5F, 0, 0, 0, 0), new Orbit(2.5F, 0, 0, 0, 120), new Orbit(2.5F, 0, 0, 0, 240));

		final Supplier<EntityType<FireBarrierEntity>> type;
		final int duration;
		final float spin;
		final float hitRadius, column;
		final int particles, fireTicks;
		final boolean roots, keepsBusy;
		final Orbit[] orbits;

		Tier(Supplier<EntityType<FireBarrierEntity>> type, int duration, float spin, float hitRadius, float column, int particles, int fireTicks, boolean roots, boolean keepsBusy, Orbit... orbits) {
			this.type = type;
			this.duration = duration;
			this.spin = spin;
			this.hitRadius = hitRadius;
			this.column = column;
			this.particles = particles;
			this.fireTicks = fireTicks;
			this.roots = roots;
			this.keepsBusy = keepsBusy;
			this.orbits = orbits;
		}

		static Tier of(EntityType<?> type) {
			for (Tier tier : values()) {
				if (tier.type.get() == type) {
					return tier;
				}
			}
			return FIRE_BARRIER;
		}
	}

	// A circle at this height over the caster's feet, tilted about a horizontal axis turned yaw degrees. The phase is where the flame starts
	record Orbit(float radius, float height, float tilt, float yaw, float phase) {
		Vec3 at(double turned) {
			double angle = Math.toRadians(turned + phase);
			double x = Math.cos(angle) * radius;
			double z = Math.sin(angle) * radius;

			double tiltRad = Math.toRadians(tilt);
			double y = -z * Math.sin(tiltRad);
			z *= Math.cos(tiltRad);

			double yawRad = Math.toRadians(yaw);
			return new Vec3(x * Math.cos(yawRad) + z * Math.sin(yawRad), height + y, -x * Math.sin(yawRad) + z * Math.cos(yawRad));
		}
	}

	private static final ResourceLocation ROOTED_TO_GROUND = KingdomKeys.rl("fire_barrier_rooted");
	private static final AttributeModifier NO_MOVEMENT = new AttributeModifier(ROOTED_TO_GROUND, -1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
	private static final List<Holder<Attribute>> ROOTED_ATTRIBUTES = List.of(Attributes.MOVEMENT_SPEED, Attributes.JUMP_STRENGTH);

	public static boolean isRootedToGround(Player player) {
		AttributeInstance speed = player.getAttribute(Attributes.MOVEMENT_SPEED);
		return speed != null && speed.hasModifier(ROOTED_TO_GROUND);
	}

	public static boolean isBusy(@Nullable Player player) {
		return player != null && !player.level().getEntitiesOfClass(FireBarrierEntity.class, player.getBoundingBox().inflate(1D), barrier -> barrier.tier.keepsBusy && barrier.getOwner() == player).isEmpty();
	}

	@SubscribeEvent
	public static void attack(AttackEntityEvent event) {
		if (isBusy(event.getEntity())) {
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public static void useItem(PlayerInteractEvent.RightClickItem event) {
		cancelIfBusy(event);
	}

	@SubscribeEvent
	public static void useBlock(PlayerInteractEvent.RightClickBlock event) {
		cancelIfBusy(event);
	}

	@SubscribeEvent
	public static void useEntity(PlayerInteractEvent.EntityInteract event) {
		cancelIfBusy(event);
	}

	@SubscribeEvent
	public static void hitBlock(PlayerInteractEvent.LeftClickBlock event) {
		cancelIfBusy(event);
	}

	private static <T extends PlayerInteractEvent & ICancellableEvent> void cancelIfBusy(T event) {
		if (isBusy(event.getEntity())) {
			event.setCanceled(true);
		}
	}

	final Tier tier;
	private boolean rootedToGround;

	public FireBarrierEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
		super(type, world);
		this.tier = Tier.of(type);
		setNoGravity(true);
	}

	public FireBarrierEntity(Level world, LivingEntity player, float dmgMult, Tier tier) {
		super(tier.type.get(), player, world);
		this.tier = tier;
		this.dmgMult = dmgMult;
		setDamageType(KKDamageTypes.FIRE);
		setNoGravity(true);
		setPos(player.getX(), player.getY(), player.getZ());
	}

	// Not a projectile at all: it stays on the caster, so none of the flying, colliding tick runs
	@Override
	public void tick() {
		Entity owner = getOwner();
		if (owner == null || !owner.isAlive() || tickCount > tier.duration) {
			if (!level().isClientSide) {
				discard();
			}
			return;
		}

		if (tier.roots && !rootedToGround && !level().isClientSide && owner instanceof LivingEntity living) {
			setRooted(living, true);
		}

		setPos(owner.getX(), owner.getY(), owner.getZ());
		setDeltaMovement(Vec3.ZERO);

		double turned = tickCount * tier.spin;
		for (Orbit orbit : tier.orbits) {
			Vec3 at = position().add(orbit.at(turned));
			if (level().isClientSide) {
				if (tier.column > 0) {
					drawColumn(at);
				} else {
					drawBall(at);
				}
			} else {
				burn(at);
			}
		}
	}

	// Unloading and the caster leaving included
	@Override
	public void remove(RemovalReason reason) {
		if (rootedToGround && getOwner() instanceof LivingEntity living) {
			setRooted(living, false);
		}
		super.remove(reason);
	}

	// Attributes reach the client on their own, where the movement is decided
	private void setRooted(LivingEntity caster, boolean root) {
		rootedToGround = root;
		for (Holder<Attribute> attribute : ROOTED_ATTRIBUTES) {
			AttributeInstance instance = caster.getAttribute(attribute);
			if (instance != null) {
				instance.removeModifier(ROOTED_TO_GROUND);
				if (root) {
					instance.addTransientModifier(NO_MOVEMENT);
				}
			}
		}
	}

	private void drawBall(Vec3 at) {
		SimpleParticleType flame = ModParticles.SHORT_FLAME.get();
		for (int p = 0; p < tier.particles; p++) {
			level().addParticle(flame, at.x + (random.nextDouble() - 0.5D) * 0.4D, at.y + (random.nextDouble() - 0.5D) * 0.4D, at.z + (random.nextDouble() - 0.5D) * 0.4D, 0, 0.01D, 0);
		}
	}

	private void drawColumn(Vec3 at) {
		Level level = level();
		SimpleParticleType flame = ModParticles.SHORT_FLAME.get();
		double twist = tickCount * 0.6D;
		for (double h = 0; h < tier.column; h += 0.15D) {
			double angle = twist + h * 4;
			double dx = Math.cos(angle) * 0.3D, dz = Math.sin(angle) * 0.3D;
			level.addParticle(flame, at.x + dx, at.y + h, at.z + dz, 0, 0.01D, 0);
			level.addParticle(flame, at.x - dx, at.y + h, at.z - dz, 0, 0.01D, 0);
			if (random.nextInt(3) == 0) {
				level.addParticle(flame, at.x, at.y + h, at.z, 0, 0.03D, 0);
			}
		}
		if (random.nextInt(3) == 0) {
			level.addParticle(ParticleTypes.LARGE_SMOKE, at.x, at.y + tier.column, at.z, 0, 0.04D, 0);
		}
	}

	private void burn(Vec3 at) {
		double r = tier.hitRadius;
		AABB box = tier.column > 0 ? new AABB(at.x - r, at.y, at.z - r, at.x + r, at.y + tier.column, at.z + r) : new AABB(at, at).inflate(r);
		// Each target's own invulnerability keeps it from being burnt every tick it stands in the flames
		for (LivingEntity e : ProjectileHits.in(level(), box, getOwner())) {
			e.removeEffect(ModMobEffects.FREEZE);
			e.setRemainingFireTicks(Math.max(e.getRemainingFireTicks(), tier.fireTicks));
			damageEntity(e);
		}
	}
}

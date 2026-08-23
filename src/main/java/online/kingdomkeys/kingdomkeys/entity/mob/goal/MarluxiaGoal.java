package online.kingdomkeys.kingdomkeys.entity.mob.goal;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.entity.mob.MarluxiaEntity;
import online.kingdomkeys.kingdomkeys.entity.organization.PetalWaveEntity;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class MarluxiaGoal extends Goal {

	public static final int STATE_IDLE = 0, STATE_ARMORED = 1, STATE_REAP = 2, STATE_FINISHER = 3;

	// % of HP left to start adding the armor
	private static final float ARMOR_PHASE_AT = 0.65F;

	// He stands untouchable for the first few seconds so the entrance can play out.
	private static final int INTRO_TICKS = 100;
	// Breathing room between specials, so the fight isn't wall to wall scripted attacks.
	private static final int SPECIAL_COOLDOWN = 140;

	private static final int ARMOR_RISE_END = 26;
	private static final int ARMOR_HANG_END = 34;
	private static final int ARMOR_SLAM_SPEED = -3;
	// Failsafe in case he somehow never touches down.
	private static final int ARMOR_SLAM_TIMEOUT = 70;
	private static final int ARMOR_HOLD_TICKS = 200;

	private static final int ARMOUR_RING_TICKS = PetalWaveEntity.WAVE_TICKS;
	private static final double ARMOUR_RING_SPEED = PetalWaveEntity.WAVE_SPEED;

	private static final int REAP_STRIKES = 4;
	private static final int REAP_INTERVAL = 25;
	// Warning particles first, then the blink, then the swing. The gap between the last two is the window the player gets to punish him in - land a hit in it and the whole chain drops.
	private static final int REAP_TELEGRAPH = 10;
	private static final int REAP_LAUNCH_AT = 22;
	// Tuned so the target is still in the air when the next blow comes round: at this strength they hang for a little over the interval
	private static final double REAP_LAUNCH = 1.6;
	private static final double REAP_JUGGLE = 1.4;
	// Generous, because by the follow-ups he's swinging at someone several blocks off the ground.
	private static final float REAP_RANGE = 5.5F;

	private static final int FINISHER_TICKS = 300; // Should be 11 bursts
	private static final int COLUMN_INTERVAL = 26;

	private static final int COLUMN_SPREAD = 2;
	private static final int COLUMN_TELEGRAPH = 18;
	private static final int COLUMN_HEIGHT = 5;

	private static final double COLUMN_RADIUS = 0.75;
	private static final float COLUMN_DAMAGE = 6F;

	private final MarluxiaEntity marluxia;

	private int attackTicks;
	private int cooldown = SPECIAL_COOLDOWN;
	private int armorHeldFor;
	private boolean slamLanded;
	private int slamStart;
	private int slamTick;
	private boolean finisherUsed;
	private boolean finisherDropping;
	// Health at the moment he teleports in, used to track if it got first before the player did
	private float reapHealthAtBlink;
	private Vec3 slamPoint = Vec3.ZERO;

	private final List<Column> columns = new ArrayList<>();

	private record Column(BlockPos pos, int erupts) {
	}

	public MarluxiaGoal(MarluxiaEntity marluxia) {
		this.marluxia = marluxia;
		setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
	}

	@Override
	public boolean canUse() {
		LivingEntity target = marluxia.getTarget();
		if (target == null || !target.isAlive() || marluxia.tickCount < INTRO_TICKS) {
			return false;
		}
		// The finisher is put into the state by the damage handler, so pick it straight up.
		return marluxia.getState() == STATE_FINISHER || cooldown <= 0;
	}

	@Override
	public boolean canContinueToUse() {
		LivingEntity target = marluxia.getTarget();
		if (target == null || !target.isAlive()) {
			return false;
		}
		return marluxia.getState() != STATE_IDLE;
	}

	@Override
	public boolean isInterruptable() {
		return false;
	}

	@Override
	public boolean requiresUpdateEveryTick() {
		return true;
	}

	@Override
	public void start() {
		// Already mid-finisher: the damage handler started it, don't overwrite it with a fresh attack.
		if (marluxia.getState() == STATE_FINISHER) {
			return;
		}

		attackTicks = 0;
		armorHeldFor = 0;
		slamLanded = false;
		slamStart = 0;
		finisherDropping = false;
		columns.clear();

		if (inArmorPhase() && marluxia.getRandom().nextFloat() < 0.5F) {
			marluxia.setState(STATE_ARMORED);
		} else {
			marluxia.setState(STATE_REAP);
		}
	}

	@Override
	public void stop() {
		marluxia.setState(STATE_IDLE);
		marluxia.setNoGravity(false);
		marluxia.setInvulnerable(false);
		cooldown = SPECIAL_COOLDOWN;
		columns.clear();

		if (marluxia.getTarget() == null) {
			breakArmor();
		}
	}

	@Override
	public void tick() {
		LivingEntity target = marluxia.getTarget();
		if (target == null) {
			return;
		}

		marluxia.getLookControl().setLookAt(target, 30F, 30F);
		attackTicks++;

		switch (marluxia.getState()) {
			case STATE_ARMORED -> tickArmor(target);
			case STATE_REAP -> tickReap(target);
			case STATE_FINISHER -> tickFinisher(target);
			default -> {
			}
		}
	}

	public void tickCooldown() {
		if (cooldown > 0) {
			cooldown--;
		}
		if (armorHeldFor > 0 && --armorHeldFor == 0) {
			breakArmor();
		}
	}

	// Armor
	private void tickArmor(LivingEntity target) {
		if (!slamLanded) {
			if (attackTicks < ARMOR_RISE_END) {
				marluxia.setNoGravity(true);
				marluxia.setDeltaMovement(0, 0.32, 0);
				marluxia.setInvulnerable(true);
				telegraphRise();
				return;
			}

			if (attackTicks < ARMOR_HANG_END) {
				// A beat at the apex: this is the tell that the slam is coming.
				marluxia.setDeltaMovement(0, 0, 0);
				marluxia.teleportTo(target.getX(), marluxia.getY(), target.getZ());
				slamStart = attackTicks;
				return;
			}
		}

		// Attack finished. Dropping to idle lets the goal stand down and the melee goal take over with the armor still on
		if (tickSlam(true)) {
			marluxia.setState(STATE_IDLE);
		}
	}

	// The drop itself, shared by the armor attack and the closing beat of the finisher: falls fast, and on impact throws out the expanding wave. Returns true once the wave has finished playing
	private boolean tickSlam(boolean grantArmor) {
		if (!slamLanded) {
			marluxia.setNoGravity(false);
			marluxia.setDeltaMovement(0, ARMOR_SLAM_SPEED, 0);

			if (marluxia.onGround() || attackTicks - slamStart > ARMOR_SLAM_TIMEOUT) {
				slamLanded = true;
				slamTick = attackTicks;
				slamPoint = marluxia.position();

				if (grantArmor) {
					// The armor goes on at the moment of impact, not on the way up, so the glow reads as the result of the slam.
					marluxia.setArmored(true);
					marluxia.addEffect(new MobEffectInstance(MobEffects.GLOWING, ARMOR_HOLD_TICKS, 0, false, false, true));
					armorHeldFor = ARMOR_HOLD_TICKS;
				}

				// Vulnerable again the moment he commits - the armor is damage reduction from here, not immunity, and fire strips it outright.
				marluxia.setInvulnerable(false);
				marluxia.level().playSound(null, marluxia.blockPosition(), SoundEvents.GENERIC_EXPLODE.value(), SoundSource.HOSTILE, 1F, 0.7F);
				spawnWave();
			}
			return false;
		}

		int sinceSlam = attackTicks - slamTick;
		if (sinceSlam <= ARMOUR_RING_TICKS) {
			expandRing(sinceSlam * ARMOUR_RING_SPEED);
			return false;
		}
		return true;
	}

	private void spawnWave() {
		marluxia.level().addFreshEntity(PetalWaveEntity.wave(marluxia.level(), slamPoint));
	}

	private void telegraphRise() {
		if (!(marluxia.level() instanceof ServerLevel level)) {
			return;
		}
		level.sendParticles(petals(), marluxia.getX(), marluxia.getY() + 1, marluxia.getZ(), 4, 0.4, 0.6, 0.4, 0.01);
		// Blossom shedding off him as he goes up.
		level.sendParticles(ParticleTypes.CHERRY_LEAVES, marluxia.getX(), marluxia.getY() + 1.4, marluxia.getZ(), 3, 0.6, 0.7, 0.6, 0.01);
	}

	private void expandRing(double radius) {
		AABB edge = new AABB(slamPoint.x, slamPoint.y, slamPoint.z, slamPoint.x, slamPoint.y + 2, slamPoint.z).inflate(radius, 1, radius);
		AABB inner = edge.deflate(1.6, 0, 1.6);

		for (LivingEntity victim : marluxia.level().getEntitiesOfClass(LivingEntity.class, edge)) {
			if (victim == marluxia || inner.contains(victim.position())) {
				continue;
			}
			marluxia.doHurtTarget(victim);
		}
	}

	// Called by the entity when fire lands on him mid-armor, and by the timeout above.
	public void breakArmor() {
		if (!marluxia.isArmored()) {
			return;
		}
		marluxia.setArmored(false);
		marluxia.removeEffect(MobEffects.GLOWING);
		armorHeldFor = 0;

		// The armor doesn't smoulder away, it bursts: fire scatters it as petals.
		if (marluxia.level() instanceof ServerLevel level) {
			level.sendParticles(ParticleTypes.CHERRY_LEAVES, marluxia.getX(), marluxia.getY() + 1, marluxia.getZ(), 40, 0.6, 0.9, 0.6, 0.12);
			level.sendParticles(petals(), marluxia.getX(), marluxia.getY() + 1, marluxia.getZ(), 50, 0.7, 1.0, 0.7, 0.1);
		}
	}


	// Reap
	// Only way to cancel is to hit him before he hits you
	private void tickReap(LivingEntity target) {
		int step = attackTicks % REAP_INTERVAL;
		int strike = attackTicks / REAP_INTERVAL;

		if (strike >= REAP_STRIKES) {
			marluxia.setState(STATE_IDLE);
			return;
		}

		// After the opening pop he has to hang in the air with them, otherwise he drops away between
		// blinks and the follow-ups all miss.
		marluxia.setNoGravity(strike > 0);
		if (strike > 0) {
			marluxia.setDeltaMovement(Vec3.ZERO);
		}

		if (step == 1) {
			markStrike(target, strike);
			return;
		}

		if (step == REAP_TELEGRAPH) {
			Vec3 spot = strikeSpot(target, strike);
			marluxia.teleportTo(spot.x, spot.y, spot.z);
			marluxia.setDeltaMovement(Vec3.ZERO);
			marluxia.level().playSound(null, marluxia.blockPosition(), ModSounds.portal.get(), SoundSource.HOSTILE, 0.8F, 1.3F);
			// Opens the punish window.
			reapHealthAtBlink = marluxia.getHealth();
			return;
		}

		if (step > REAP_TELEGRAPH && step <= REAP_LAUNCH_AT) {
			if (marluxia.getHealth() < reapHealthAtBlink) {
				cancelReap();
				return;
			}

			if (step == REAP_LAUNCH_AT && marluxia.distanceTo(target) <= REAP_RANGE) {
				marluxia.doHurtTarget(target);
				launch(target, strike == 0 ? REAP_LAUNCH : REAP_JUGGLE);
			}
		}
	}

	// Hit during the window: he's knocked out of the chain and back to fighting normally, which also
	// starts the special cooldown over via stop().
	private void cancelReap() {
		marluxia.setState(STATE_IDLE);
		marluxia.setNoGravity(false);
		marluxia.level().playSound(null, marluxia.blockPosition(), ModSounds.portal.get(), SoundSource.HOSTILE, 0.9F, 0.6F);

		if (marluxia.level() instanceof ServerLevel level) {
			level.sendParticles(ParticleTypes.CHERRY_LEAVES, marluxia.getX(), marluxia.getY() + 1, marluxia.getZ(), 14, 0.5, 0.7, 0.5, 0.06);
		}
	}

	// Where the next blow comes from: behind the target, at their height. Using a flattened look
	// vector keeps him beside them rather than under their feet once they're airborne.
	private Vec3 strikeSpot(LivingEntity target, int strike) {
		Vec3 look = target.getLookAngle();
		Vec3 flat = new Vec3(look.x, 0, look.z);
		flat = flat.lengthSqr() < 1.0E-4 ? new Vec3(0, 0, 1) : flat.normalize();

		Vec3 behind = target.position().subtract(flat.scale(1.5));
		return new Vec3(behind.x, target.getY() + (strike == 0 ? 0.1 : 0), behind.z);
	}

	private void launch(LivingEntity target, double power) {
		Vec3 movement = target.getDeltaMovement();
		target.setDeltaMovement(movement.x * 0.4, power, movement.z * 0.4);
		target.hurtMarked = true;
	}

	private void markStrike(LivingEntity target, int strike) {
		if (!(marluxia.level() instanceof ServerLevel level)) {
			return;
		}
		Vec3 spot = strikeSpot(target, strike);
		level.sendParticles(petals(), spot.x, spot.y + 1, spot.z, 20, 0.3, 0.8, 0.3, 0.02);
		level.sendParticles(ParticleTypes.CHERRY_LEAVES, spot.x, spot.y + 1.2, spot.z, 6, 0.4, 0.6, 0.4, 0.02);
	}

	//Finisher
	public void beginFinisher() {
		finisherUsed = true;
		attackTicks = 0;
		slamLanded = false;
		finisherDropping = false;
		columns.clear();
		marluxia.setState(STATE_FINISHER);
		marluxia.setArmored(false);
		marluxia.setNoGravity(true);
		marluxia.setInvulnerable(true);
		marluxia.level().playSound(null, marluxia.blockPosition(), ModSounds.portal.get(), SoundSource.HOSTILE, 1.2F, 0.6F);
	}

	public boolean hasUsedFinisher() {
		return finisherUsed;
	}

	private void endFinisher() {
		marluxia.setState(STATE_IDLE);
		marluxia.setNoGravity(false);
		marluxia.setInvulnerable(false);
		finisherDropping = false;
		columns.clear();
		cooldown = SPECIAL_COOLDOWN;

		if (marluxia.level() instanceof ServerLevel level) {
			level.sendParticles(ParticleTypes.LARGE_SMOKE, marluxia.getX(), marluxia.getY() + 1, marluxia.getZ(), 40, 0.6, 1.0, 0.6, 0.05);
		}
	}

	// Rides on top of the player and plants columns of light around them, the only way out is to keep moving off the marked ground.
	private void tickFinisher(LivingEntity target) {
		if (finisherDropping) {
			if (tickSlam(false)) {
				endFinisher();
			}
			return;
		}

		if (attackTicks > FINISHER_TICKS) {
			finisherDropping = true;
			slamLanded = false;
			slamStart = attackTicks;
			columns.clear();
			marluxia.setNoGravity(false);
			return;
		}

		marluxia.setNoGravity(true);
		marluxia.teleportTo(target.getX(), target.getY() + 2.2, target.getZ());
		marluxia.setDeltaMovement(Vec3.ZERO);

		RandomSource random = marluxia.getRandom();

		if (marluxia.level() instanceof ServerLevel level) {
			level.sendParticles(petals(), marluxia.getX() - 0.5 + random.nextDouble(), marluxia.getY(), marluxia.getZ() - 0.5 + random.nextDouble(), 2, 0.2, 0.2, 0.2, 0.01);
			level.sendParticles(ParticleTypes.CHERRY_LEAVES, marluxia.getX(), marluxia.getY(), marluxia.getZ(), 2, 0.7, 0.3, 0.7, 0.01);
		}

		if (attackTicks % COLUMN_INTERVAL == 0) {
			BlockPos spot = target.blockPosition().offset(random.nextInt(COLUMN_SPREAD * 2 + 1) - COLUMN_SPREAD, 0, random.nextInt(COLUMN_SPREAD * 2 + 1) - COLUMN_SPREAD);
			columns.add(new Column(spot, attackTicks + COLUMN_TELEGRAPH));

			marluxia.level().addFreshEntity(PetalWaveEntity.mark(marluxia.level(), ringCentre(spot), (float) COLUMN_RADIUS, COLUMN_TELEGRAPH));
		}

		tickColumns();
	}

	private void tickColumns() {
		if (!(marluxia.level() instanceof ServerLevel level)) {
			return;
		}

		columns.removeIf(column -> {
			if (attackTicks < column.erupts()) {
				return false;
			}

			erupt(level, column.pos());
			level.playSound(null, column.pos(), SoundEvents.BEACON_POWER_SELECT, SoundSource.HOSTILE, 0.8F, 1.6F);

			AABB pillar = new AABB(column.pos()).inflate(COLUMN_RADIUS, 0, COLUMN_RADIUS).expandTowards(0, COLUMN_HEIGHT, 0);
			for (LivingEntity victim : level.getEntitiesOfClass(LivingEntity.class, pillar)) {
				if (victim != marluxia) {
					victim.hurt(victim.damageSources().indirectMagic(marluxia, marluxia), COLUMN_DAMAGE);
				}
			}
			return true;
		});
	}

	// Middle of the block's top face, which is where the ring is centred.
	private static Vec3 ringCentre(BlockPos pos) {
		return new Vec3(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
	}

	// What comes out of the marked ring: a burst of petals blown straight up
	private void erupt(ServerLevel level, BlockPos pos) {
		double x = pos.getX() + 0.5;
		double y = pos.getY();
		double z = pos.getZ() + 0.5;
		RandomSource random = marluxia.getRandom();

		// Fills the column's shape.
		level.sendParticles(petals(), x, y + COLUMN_HEIGHT * 0.45, z, 70, COLUMN_RADIUS * 0.5, COLUMN_HEIGHT * 0.35, COLUMN_RADIUS * 0.5, 0);

		// The blast itself.
		for (int i = 0; i < 26; i++) {
			double spread = COLUMN_RADIUS * 0.35;
			level.sendParticles(ParticleTypes.END_ROD, x + (random.nextDouble() - 0.5) * COLUMN_RADIUS, y + 0.1, z + (random.nextDouble() - 0.5) * COLUMN_RADIUS, 0, (random.nextDouble() - 0.5) * spread, 0.55 + random.nextDouble() * 0.35, (random.nextDouble() - 0.5) * spread, 1);
		}

		// Blossom left drifting down afterwards.
		level.sendParticles(ParticleTypes.CHERRY_LEAVES, x, y + COLUMN_HEIGHT * 0.8, z, 20, COLUMN_RADIUS * 0.7, COLUMN_HEIGHT * 0.2, COLUMN_RADIUS * 0.7, 0.05);
	}

	// --- Helpers ---------------------------------------------------------------------------------

	private boolean inArmorPhase() {
		return marluxia.getHealth() <= marluxia.getMaxHealth() * ARMOR_PHASE_AT;
	}

	private static DustParticleOptions petals() {
		return new DustParticleOptions(new Vector3f(1F, 0.45F, 0.8F), 1.2F);
	}
}

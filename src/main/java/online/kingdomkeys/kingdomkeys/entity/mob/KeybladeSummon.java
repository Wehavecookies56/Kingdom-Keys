package online.kingdomkeys.kingdomkeys.entity.mob;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;

public class KeybladeSummon {
	private static final int FASTEST = 8, SLOWEST = 70;

	private static final int ATTEMPT_MIN = 10, ATTEMPT_MAX = 25;

	private static final int ATTEMPT_SPARKS = 10, SUMMON_SPARKS = 60;

	public static final int PUNCHING_LEVEL = 10;

	public static final int MASTERED = -1;

	/** The same object twice: once as something in the world, once as something that calls a keyblade. */
	private final Mob owner;
	private final KeybladeWielder wielder;

	private int ticks;
	private int nextAttempt;

	public <T extends Mob & KeybladeWielder> KeybladeSummon(T owner) {
		this.owner = owner;
		this.wielder = owner;
	}

	private int rollAttemptGap() {
		return ATTEMPT_MIN + owner.getRandom().nextInt(ATTEMPT_MAX - ATTEMPT_MIN + 1);
	}

	public boolean hasKeyblade() {
		return !owner.getMainHandItem().isEmpty();
	}

	public boolean isSummoning() {
		return ticks > 0;
	}

	public static boolean standsItsGround(int level) {
		return level == MASTERED || level >= PUNCHING_LEVEL;
	}

	public static int timeFor(int level) {
		if (level == MASTERED) {
			return 0;
		}

		int rank = Mth.clamp(level, ApprenticeEntity.MIN_LEVEL, ApprenticeEntity.MAX_LEVEL);
		float learned = (rank - ApprenticeEntity.MIN_LEVEL) / (float) (ApprenticeEntity.MAX_LEVEL - ApprenticeEntity.MIN_LEVEL);

		return Math.max(1, Math.round(Mth.lerp(learned, SLOWEST, FASTEST)));
	}

	public void begin() {
		if (hasKeyblade() || isSummoning()) {
			return;
		}

		ticks = timeFor(wielder.callingRank());
		if (ticks <= 0) {
			land();
			return;
		}

		nextAttempt = rollAttemptGap();
		callOut(ATTEMPT_SPARKS, 0.25F, 0.8F);
	}

	public void tick() {
		if (ticks <= 0) {
			return;
		}

		if (--ticks <= 0) {
			land();
			return;
		}

		// Another go, and another handful of sparks that come to nothing
		if (--nextAttempt <= 0) {
			nextAttempt = rollAttemptGap();
			callOut(ATTEMPT_SPARKS, 0.25F, 0.8F);
		}
	}

	/** It arrives. */
	private void land() {
		ItemStack called = wielder.keybladeToCall();

		if (called.isEmpty()) {
			return;
		}

		owner.setItemSlot(EquipmentSlot.MAINHAND, called);
		owner.setDropChance(EquipmentSlot.MAINHAND, 0.0F);
		callOut(SUMMON_SPARKS, 0.5F, 1.1F);
	}

	public void dismiss() {
		ticks = 0;

		if (hasKeyblade()) {
			owner.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
			owner.playSound(ModSounds.unsummon.get(), 0.4F, 1.2F);
		}
	}

	public void cancel() {
		ticks = 0;
	}

	private void callOut(int sparks, float volume, float pitch) {
		owner.playSound(ModSounds.summon.get(), volume, pitch);

		if (!(owner.level() instanceof ServerLevel server)) {
			return;
		}

		Vec3 hand = new Vec3(0.4D, 0.0D, -0.38D).yRot((float) Math.toRadians(-owner.yBodyRot));
		server.sendParticles(ParticleTypes.FIREWORK, owner.getX() - hand.x, owner.getY() + 1.0D, owner.getZ() - hand.z, sparks, 0.0D, 0.0D, 0.0D, 0.2D);
	}
}

package online.kingdomkeys.kingdomkeys.shotlock.minigame;

import net.minecraft.util.Mth;

import java.util.Locale;

public enum ShotlockMinigameType {
	MASHING,
	TIMING,
	KEYS;

	private static final ShotlockMinigameType[] VALUES = values();

	public static ShotlockMinigameType byId(int id) {
		if (id < 0 || id >= VALUES.length) {
			return MASHING;
		}
		return VALUES[id];
	}

	// Used for the JSON files
	public String getId() {
		return switch (this) {
			case MASHING -> MASH_ID;
			case TIMING -> TIMING_ID;
			case KEYS -> KEYS_ID;
		};
	}

	public static final String MASH_ID = "mash";
	//* Mash variant for dashing Shotlocks: every press charges the target instead of throwing shots.
	public static final String MASH_DASH_ID = "mash_dash";
	public static final String TIMING_ID = "timing";
	//* Timing variant for Shotlocks whose attack is one big shot: each hit repeats that shot instead of a volley.
	public static final String TIMING_CANNON_ID = "timing_cannon";
	public static final String KEYS_ID = "keys";
	public static final String NONE_ID = "none";

	public static ShotlockMinigameType parse(String id) {
		if (id == null || id.isBlank()) {
			return null;
		}
		return switch (id.toLowerCase(Locale.ROOT).trim()) {
			case MASH_ID, MASH_DASH_ID -> MASHING;
			case TIMING_ID, TIMING_CANNON_ID -> TIMING;
			case KEYS_ID -> KEYS;
			default -> null;
		};
	}

	public static boolean isDashVariant(String id) {
		return id != null && MASH_DASH_ID.equalsIgnoreCase(id.trim());
	}

	public static boolean isCannonVariant(String id) {
		return id != null && TIMING_CANNON_ID.equalsIgnoreCase(id.trim());
	}

	// Adjustments

	// Min level for shotlocks to follow up with a minigame.
	public static final int MIN_LEVEL = 2;

	// MASHING
	// Ticks the minigame lasts for.
	public static final int MASHING_MAX_TICKS = 100;
	// Delay between presses
	public static final int MASHING_PRESS_DELAY = 5;
	// Idle timeout ticks
	public static final int MASHING_IDLE_CANCEL = 40;
	// Shots per press
	public static final int MASHING_SHOTS_PER_PRESS = 4;

	//TIMING / KEYS
	// Max rounds
	public static final int MAX_ROUNDS = 6;

	// Ticks the round lasts for
	public static final int KEYS_ROUND_TICKS = 40;
	// Answer within this many ticks for a perfect, later still counts but hits softer
	public static final int KEYS_PERFECT_TICKS = 12;

	// Ticks for the ring to close
	public static final int TIMING_ROUND_TICKS = 30;
	// Shots per perfect hit, good gives 50% of it, bad 25%
	public static final int TIMING_PERFECT_SHOTS = 8;

	// Radius of the fixed target ring.
	public static final float RING_TARGET_RADIUS = 30F;
	// Half-thickness of the target ring.
	public static final float RING_TARGET_HALF = 5F;
	// Half-thickness of the shrinking ring.
	public static final float RING_MOVING_HALF = 2F;
	// Where the shrinking ring starts and ends.
	public static final float RING_START_RADIUS = 56F;
	public static final float RING_END_RADIUS = 18F;

	// Radius of the shrinking ring at a given point through the round.
	public static float movingRingRadius(float progress) {
		progress = Mth.clamp(progress, 0F, 1F);
		return Mth.lerp(progress, RING_START_RADIUS, RING_END_RADIUS);
	}

	// Grades a click on the timing ring. 2 = perfect (ring is fully inside target), 1 = good (not perfectly inside), 0 = bad (no contact at all).
	public static int gradeTimingRadius(float radius) {
		float targetInner = RING_TARGET_RADIUS - RING_TARGET_HALF;
		float targetOuter = RING_TARGET_RADIUS + RING_TARGET_HALF;
		float movingInner = radius - RING_MOVING_HALF;
		float movingOuter = radius + RING_MOVING_HALF;

		if (movingInner >= targetInner && movingOuter <= targetOuter) {
			return 2;
		}
		if (movingOuter >= targetInner && movingInner <= targetOuter) {
			return 1;
		}
		return 0;
	}

	// Scaling by level
	// How many rounds this minigame runs at the given Shotlock item level.
	public int roundsForLevel(int level) {
		if (this == MASHING) {
			return 1; // one continuous mash phase, the level buys time instead of rounds
		}
		return Mth.clamp(level, 1, MAX_ROUNDS);
	}

	// How long a single round lasts at the given level.
	public int roundTicksForLevel(int level) {
		return switch (this) {
			case MASHING -> Math.min(40 + level * 12, MASHING_MAX_TICKS);
			case TIMING -> TIMING_ROUND_TICKS;
			case KEYS -> KEYS_ROUND_TICKS;
		};
	}
}

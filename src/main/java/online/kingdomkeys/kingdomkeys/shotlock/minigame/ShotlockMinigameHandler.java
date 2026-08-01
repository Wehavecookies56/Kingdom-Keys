package online.kingdomkeys.kingdomkeys.shotlock.minigame;

import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.entity.shotlock.BaseShotlockCoreEntity;
import online.kingdomkeys.kingdomkeys.entity.shotlock.MinigameShotEntity;
import online.kingdomkeys.kingdomkeys.item.ShotlockItem;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCShotlockMinigameState;
import online.kingdomkeys.kingdomkeys.shotlock.Shotlock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ShotlockMinigameHandler {

	private static final Map<UUID, ShotlockSession> SESSIONS = new HashMap<>();

	// Grade sent to the client when there's nothing to report yet.
	public static final int RESULT_NONE = -2;
	public static final int RESULT_MISS = -1;

	private static final float MASHING_SPIRAL_STEP = 10F;
	private static final float MASHING_SHOT_DAMAGE = 0.25F;

	private static final float TIMING_SHOT_DAMAGE = 0.5F;
	private static final float SLAM_DAMAGE = 1.5F;
	// A dashing mash press lands a single hit rather than four shots, so it's worth more each
	private static final float DASH_DAMAGE = 0.8F;

	// How hard the WASD slam lands, by how fast the prompt was answered. Timing shots already scale their count by grade, so this is the equivalent for keys, which was previously hit-or-miss.
	private static final float KEYS_PERFECT_POWER = 1F;
	private static final float KEYS_GOOD_POWER = 0.6F;
	private static final float KEYS_MISS_POWER = 0.35F;

	// How far from the caster we look for the core the Shotlock just spawned.
	private static final double CORE_SCAN_RADIUS = 16D;
	// Delay used when a Shotlock has no core at all to wait on (Ultima Cannon).
	private static final int DEFAULT_WAIT_TICKS = 40;
	// Slack after the core's own lifetime, so the last shots have landed before the HUD appears.
	private static final int WAIT_GRACE_TICKS = 20;

	private static final int WAIT_HARD_CAP = 60;

	private static class ShotlockSession {
		ShotlockMinigameType type;
		// MASHING only: this Shotlock charges its targets instead of throwing shots.
		boolean dashMash;
		ResourceKey<DamageType> element;
		// Lifted off the Shotlock's own core so the follow-up shots look like the originals.
		BaseShotlockCoreEntity.ShotStyle style = new BaseShotlockCoreEntity.ShotStyle();
		float damage;
		List<Integer> targetIds = new ArrayList<>();
		// Rotates through the locked-on targets so a chain doesn't hammer the same one every time.
		int targetCursor;

		// The minigame doesn't open until the Shotlock barrage itself is done. We sit in this phase watching the cores the Shotlock spawned and only start once they're all gone.
		boolean waiting = true;
		boolean scannedForCores;
		int waitTicks;
		int waitTimeout = WAIT_HARD_CAP;
		List<Integer> coreIds = new ArrayList<>();
		// True for the dashing Shotlocks, which must stay free to move during their own barrage.
		boolean coreMovesCaster;

		int round;
		int totalRounds;
		int roundDuration;
		int roundTicks;
		int payload;
		boolean resolved;

		// MASHING only: when the last accepted press happened, for the delay and the idle cancel.
		int lastPressTick;
		int presses;

		// Whether we flipped the invulnerability flag ourselves, and what it was before.
		boolean madeInvulnerable;
		boolean wasInvulnerable;
	}

	// Lifecycle
	public static void start(Player player, Shotlock shotlock, List<Entity> targets) {
		if (player.level().isClientSide || !(player instanceof ServerPlayer serverPlayer) || shotlock == null) {
			return;
		}

		if (!minigamesEnabled() || SESSIONS.containsKey(player.getUUID())) {
			return; // one at a time - a second Shotlock mid-minigame doesn't stack another on top
		}

		int level = getEquippedLevel(player, shotlock);
		if (level < ShotlockMinigameType.MIN_LEVEL) {
			return;
		}

		// Which minigame this Shotlock runs comes from its datapack entry, not from code.
		ShotlockMinigameType type = shotlock.getMinigameType();
		if (type == null) {
			return; // this Shotlock has no follow-up configured
		}

		ShotlockSession shotlockSession = new ShotlockSession();
		shotlockSession.type = type;
		shotlockSession.dashMash = shotlock.minigameUsesDash();
		shotlockSession.element = shotlock.getElement();
		shotlockSession.damage = shotlock.getDamage(player);

		// Placeholder styling until the core scan finds the real thing a tick from now.
		shotlockSession.style.colour = ShotlockMinigameAttacks.shotColour(shotlockSession.element);
		shotlockSession.style.element = shotlockSession.element;
		shotlockSession.totalRounds = type.roundsForLevel(level);
		shotlockSession.roundDuration = type.roundTicksForLevel(level);

		if (targets != null) {
			for (Entity target : targets) {
				if (target != null) {
					shotlockSession.targetIds.add(target.getId());
				}
			}
		}

		SESSIONS.put(serverPlayer.getUUID(), shotlockSession);
	}

	private static void tickWaiting(ServerPlayer player, ShotlockSession shotlockSession) {
		shotlockSession.waitTicks++;

		if (!shotlockSession.scannedForCores) {
			shotlockSession.scannedForCores = true;
			scanForCores(player, shotlockSession);

			if (!shotlockSession.coreMovesCaster) {
				PacketHandler.sendTo(new SCShotlockMinigameState(shotlockSession.type.ordinal(), -1, shotlockSession.totalRounds, 0, 0, RESULT_NONE), player);
			}
		}

		// Only "gone" if there was something to wait on in the first place - with no cores at all we
		// fall through to the timeout instead of starting on the very first tick.
		boolean coresGone = !shotlockSession.coreIds.isEmpty();
		for (int id : shotlockSession.coreIds) {
			Entity core = player.level().getEntity(id);
			if (core != null && core.isAlive()) {
				coresGone = false;
				break;
			}
		}

		if (coresGone || shotlockSession.waitTicks >= shotlockSession.waitTimeout) {
			shotlockSession.waiting = false;
			applyInvulnerability(player, shotlockSession);
			beginRound(player, shotlockSession, 1, RESULT_NONE);
		}
	}

	private static void scanForCores(ServerPlayer player, ShotlockSession shotlockSession) {
		AABB box = player.getBoundingBox().inflate(CORE_SCAN_RADIUS);
		List<BaseShotlockCoreEntity> cores = player.level().getEntitiesOfClass(BaseShotlockCoreEntity.class, box, core -> core.isAlive() && core.getCaster() != null && core.getCaster().getUUID().equals(player.getUUID()));

		int longest = 0;
		for (BaseShotlockCoreEntity core : cores) {
			shotlockSession.coreIds.add(core.getId());
			longest = Math.max(longest, core.getMaxTicks());
			shotlockSession.coreMovesCaster |= core.movesCaster();
		}

		if (!cores.isEmpty()) {
			shotlockSession.style = cores.get(0).getShotStyle();
		}

		// No core to wait on (Ultima Cannon fires its shots directly) - just give the volley a beat.
		int timeout = cores.isEmpty() ? DEFAULT_WAIT_TICKS : longest + WAIT_GRACE_TICKS;
		shotlockSession.waitTimeout = Math.min(timeout, WAIT_HARD_CAP);
	}

	private static void applyInvulnerability(ServerPlayer player, ShotlockSession shotlockSession) {
		if (shotlockSession.madeInvulnerable) {
			return;
		}
		shotlockSession.madeInvulnerable = true;
		shotlockSession.wasInvulnerable = player.isInvulnerable();
		player.setInvulnerable(true);
		player.invulnerableTime = 20;
	}

	private static void clearInvulnerability(Player player, ShotlockSession shotlockSession) {
		if (!shotlockSession.madeInvulnerable) {
			return;
		}
		shotlockSession.madeInvulnerable = false;
		player.setInvulnerable(shotlockSession.wasInvulnerable);
	}

	private static void beginRound(ServerPlayer player, ShotlockSession shotlockSession, int round, int lastResult) {
		shotlockSession.round = round;
		shotlockSession.roundTicks = 0;
		shotlockSession.resolved = false;
		shotlockSession.lastPressTick = 0;
		shotlockSession.payload = shotlockSession.type == ShotlockMinigameType.KEYS ? player.level().random.nextInt(4) : 0;

		PacketHandler.sendTo(new SCShotlockMinigameState(shotlockSession.type.ordinal(), round, shotlockSession.totalRounds, shotlockSession.roundDuration, shotlockSession.payload, lastResult), player);
		player.level().playSound(null, player.blockPosition(), ModSounds.menu_move.get(), SoundSource.PLAYERS, 0.6F, 1.4F);
	}

	private static void end(ServerPlayer player, ShotlockSession shotlockSession, int lastResult) {
		SESSIONS.remove(player.getUUID());
		clearInvulnerability(player, shotlockSession);
		PacketHandler.sendTo(new SCShotlockMinigameState(shotlockSession.type.ordinal(), 0, shotlockSession.totalRounds, 0, 0, lastResult), player);
	}

	// Drops a session without touching the client - for logout, where there's no client left.
	public static void forget(Player player) {
		ShotlockSession shotlockSession = SESSIONS.remove(player.getUUID());
		if (shotlockSession != null) {
			clearInvulnerability(player, shotlockSession);
		}
	}

	public static boolean isActive(Player player) {
		return SESSIONS.containsKey(player.getUUID());
	}

	// Input
	public static void onInput(ServerPlayer player, int round, int value) {
		ShotlockSession shotlockSession = SESSIONS.get(player.getUUID());
		if (shotlockSession == null || shotlockSession.waiting || shotlockSession.round != round) {
			return; // stale click from a round that's already been resolved, or the barrage is still going
		}

		switch (shotlockSession.type) {
			case MASHING -> onMashingPress(player, shotlockSession);
			case TIMING -> onTimingClick(player, shotlockSession, value);
			case KEYS -> onKeyPress(player, shotlockSession, value);
		}
	}

	private static void onMashingPress(ServerPlayer player, ShotlockSession shotlockSession) {
		if (shotlockSession.presses > 0 && shotlockSession.roundTicks - shotlockSession.lastPressTick < ShotlockMinigameType.MASHING_PRESS_DELAY) {
			return; // held button / autoclicker cadence, ignore it
		}
		shotlockSession.lastPressTick = shotlockSession.roundTicks;
		shotlockSession.presses++;

		if (shotlockSession.dashMash) {
			ShotlockMinigameAttacks.dash(player, pickTarget(player, shotlockSession), shotlockSession.element, shotlockSession.damage * DASH_DAMAGE);
			player.level().playSound(null, player.blockPosition(), ModSounds.portal.get(), SoundSource.PLAYERS, 0.7F, 1.5F);
			return;
		}

		// Each burst is nudged round a few degrees from the last so a run of presses winds outward as
		// a spiral instead of stamping the same cross over and over.
		float spiral = (shotlockSession.presses - 1) * MASHING_SPIRAL_STEP;
		MinigameShotEntity.spawnBurst(player, pickTarget(player, shotlockSession), shotlockSession.damage * MASHING_SHOT_DAMAGE, shotlockSession.style, ShotlockMinigameType.MASHING_SHOTS_PER_PRESS, spiral);
		player.level().playSound(null, player.blockPosition(), ModSounds.laser.get(), SoundSource.PLAYERS, 0.7F, 1.5F);
	}

	private static void onTimingClick(ServerPlayer player, ShotlockSession shotlockSession, int value) {
		if (shotlockSession.resolved) {
			return;
		}
		shotlockSession.resolved = true;

		int grade = Mth.clamp(value, 0, 2);
		fireTimingShots(player, shotlockSession, grade);

		if (grade == 0) {
			end(player, shotlockSession, 0); // a bad ring ends the chain, as asked
		} else {
			advance(player, shotlockSession, grade);
		}
	}

	private static void onKeyPress(ServerPlayer player, ShotlockSession shotlockSession, int value) {
		if (shotlockSession.resolved) {
			return;
		}
		shotlockSession.resolved = true;

		boolean correct = value == shotlockSession.payload;
		Entity target = pickTarget(player, shotlockSession);

		boolean perfect = correct && shotlockSession.roundTicks <= ShotlockMinigameType.KEYS_PERFECT_TICKS;
		float power = !correct ? KEYS_MISS_POWER : perfect ? KEYS_PERFECT_POWER : KEYS_GOOD_POWER;

		ShotlockMinigameAttacks.slam(player, target, shotlockSession.element, shotlockSession.damage * SLAM_DAMAGE, power);
		player.level().playSound(null, player.blockPosition(), perfect ? ModSounds.levelup.get() : ModSounds.laser.get(), SoundSource.PLAYERS, 0.8F, perfect ? 1.6F : 1.2F);

		if (correct) {
			advance(player, shotlockSession, perfect ? 2 : 1);
		} else {
			end(player, shotlockSession, 0);
		}
	}

	// Moves to the next round, or wraps the minigame up if that was the last one.
	private static void advance(ServerPlayer player, ShotlockSession shotlockSession, int lastResult) {
		if (shotlockSession.round >= shotlockSession.totalRounds) {
			end(player, shotlockSession, lastResult);
		} else {
			beginRound(player, shotlockSession, shotlockSession.round + 1, lastResult);
		}
	}

	private static void fireTimingShots(ServerPlayer player, ShotlockSession shotlockSession, int grade) {
		int shots = switch (grade) {
			case 2 -> ShotlockMinigameType.TIMING_PERFECT_SHOTS;
			case 1 -> ShotlockMinigameType.TIMING_PERFECT_SHOTS / 2;
			default -> Math.max(1, ShotlockMinigameType.TIMING_PERFECT_SHOTS / 4);
		};

		// Rounds get the same treatment, just stepped per round rather than per press.
		float spiral = (shotlockSession.round - 1) * MASHING_SPIRAL_STEP;
		MinigameShotEntity.spawnBurst(player, pickTarget(player, shotlockSession), shotlockSession.damage * TIMING_SHOT_DAMAGE,
				shotlockSession.style, shots, spiral);

		player.level().playSound(null, player.blockPosition(), grade == 2 ? ModSounds.levelup.get() : ModSounds.laser.get(),
				SoundSource.PLAYERS, 0.8F, grade == 2 ? 1.6F : 1.2F);
	}

	// Ticking

	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent.Post event) {
		Player player = event.getEntity();
		if (player.level().isClientSide || !(player instanceof ServerPlayer serverPlayer)) {
			return;
		}

		ShotlockSession shotlockSession = SESSIONS.get(player.getUUID());
		if (shotlockSession == null) {
			return;
		}

		if (!player.isAlive() || player.isRemoved()) {
			// end() allows the player to move again
			end(serverPlayer, shotlockSession, RESULT_NONE);
			return;
		}

		if (shotlockSession.waiting) {
			tickWaiting(serverPlayer, shotlockSession);
			return;
		}

		shotlockSession.roundTicks++;

		switch (shotlockSession.type) {
			case MASHING -> {
				boolean idledOut = shotlockSession.roundTicks - shotlockSession.lastPressTick >= ShotlockMinigameType.MASHING_IDLE_CANCEL;
				if (shotlockSession.roundTicks >= shotlockSession.roundDuration || idledOut) {
					end(serverPlayer, shotlockSession, RESULT_NONE);
				}
			}
			case TIMING -> {
				if (!shotlockSession.resolved && shotlockSession.roundTicks > shotlockSession.roundDuration) {
					shotlockSession.resolved = true;
					fireTimingShots(serverPlayer, shotlockSession, 0);
					end(serverPlayer, shotlockSession, RESULT_MISS);
				}
			}
			case KEYS -> {
				if (!shotlockSession.resolved && shotlockSession.roundTicks > shotlockSession.roundDuration) {
					shotlockSession.resolved = true;
					ShotlockMinigameAttacks.slam(serverPlayer, pickTarget(serverPlayer, shotlockSession), shotlockSession.element, shotlockSession.damage * SLAM_DAMAGE, 0.35F);
					end(serverPlayer, shotlockSession, RESULT_MISS);
				}
			}
		}
	}

	@SubscribeEvent
	public void onLogout(PlayerEvent.PlayerLoggedOutEvent event) {
		forget(event.getEntity());
	}

	// Helpers
	private static Entity pickTarget(Player player, ShotlockSession shotlockSession) {
		int count = shotlockSession.targetIds.size();
		for (int i = 0; i < count; i++) {
			int index = Math.floorMod(shotlockSession.targetCursor + i, count);
			Entity entity = player.level().getEntity(shotlockSession.targetIds.get(index));
			if (entity != null && entity.isAlive()) {
				shotlockSession.targetCursor = index + 1;
				return entity;
			}
		}
		return null;
	}

	private static int getEquippedLevel(Player player, Shotlock shotlock) {
		PlayerData playerData = PlayerData.get(player);
		if (playerData == null) {
			return 1;
		}

		ItemStack equipped = playerData.getEquippedShotlock();
		if (equipped != null && equipped.getItem() instanceof ShotlockItem shotlockItem
				&& shotlockItem.getShotlock().equals(shotlock.getRegistryName())) {
			return shotlockItem.getLocalLevel(equipped);
		}
		return 1;
	}

	private static boolean minigamesEnabled() {
		return ModConfigs.SERVER_SPEC.isLoaded() && ModConfigs.SERVER.shotlockMinigames.get();
	}
}

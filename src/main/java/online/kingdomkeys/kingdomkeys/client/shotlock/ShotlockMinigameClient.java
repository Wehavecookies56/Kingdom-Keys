package online.kingdomkeys.kingdomkeys.client.shotlock;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSShotlockMinigameInput;
import online.kingdomkeys.kingdomkeys.network.stc.SCShotlockMinigameState;
import online.kingdomkeys.kingdomkeys.shotlock.minigame.ShotlockMinigameType;

// Minigame, updated everytime a packet sends data
public class ShotlockMinigameClient {

	public static boolean active;
	//Prevent the player from moving
	public static boolean movementLocked;
	public static ShotlockMinigameType type = ShotlockMinigameType.MASHING;
	public static int round;
	public static int totalRounds;
	public static int roundDuration;
	// KEYS: prompted direction, 0=W 1=A 2=S 3=D.
	public static int WASDRequest;

	// Ticks into the current round, and the previous value so the HUD can interpolate.
	public static int ticks;
	public static int prevTicks;

	// True once we've clicked this round, so a second click doesn't double-send.
	public static boolean clicked;

	// MASHING: local echo of the press cadence, purely so the HUD can pulse
	public static int lastPressTick = -100;
	public static int presses;

	// Last grade the server reported, and how long the flash has left.
	public static int lastResult = -2;
	public static int flashTicks;

	// Edge detection for the WASD prompts - movement is locked, so we read the keys ourselves.
	private static final boolean[] KEY_WAS_DOWN = new boolean[4];

	private static int lockWatchdog;
	private static final int LOCK_WATCHDOG_TICKS = 200;

	public static void apply(SCShotlockMinigameState state) {
		if (state.lastResult() != -2) {
			lastResult = state.lastResult();
			flashTicks = 10;
		}

		if (state.isEnd()) {
			stop(false);
			return;
		}

		if (state.isBarrage()) {
			// The Shotlock is still firing. Pin the player, but there's nothing to draw or press yet.
			movementLocked = true;
			lockWatchdog = 0;
			active = false;
			return;
		}

		active = true;
		movementLocked = true;
		lockWatchdog = 0;
		type = ShotlockMinigameType.byId(state.minigame());
		round = state.round();
		totalRounds = state.totalRounds();
		roundDuration = Math.max(1, state.roundDuration());
		WASDRequest = state.payload();
		ticks = 0;
		prevTicks = 0;
		clicked = false;
		lastPressTick = -100;

		if (round <= 1) {
			presses = 0;
		}

		refreshKeyStates();
	}

	public static void stop(boolean clearFlash) {
		active = false;
		movementLocked = false;
		lockWatchdog = 0;
		round = 0;
		ticks = 0;
		prevTicks = 0;
		clicked = false;
		if (clearFlash) {
			lastResult = -2;
			flashTicks = 0;
		}
	}

	public static void tick() {
		if (flashTicks > 0) {
			flashTicks--;
		}

		if (!movementLocked) {
			return;
		}

		Minecraft minecraft = Minecraft.getInstance();
		if (minecraft.player == null || minecraft.level == null || minecraft.player.isDeadOrDying()) {
			stop(true);
			return;
		}

		if (++lockWatchdog > LOCK_WATCHDOG_TICKS) {
			stop(true);
			return;
		}

		if (!active) {
			return; // barrage phase: locked, but nothing to animate
		}

		prevTicks = ticks;
		ticks++;

		if (ticks > roundDuration + 10) {
			ticks = roundDuration + 10;
		}

		if (type == ShotlockMinigameType.KEYS) {
			pollDirectionKeys(minecraft);
		}
	}

	// Ticks into the current round, smoothed for rendering.
	public static float elapsed(float partialTicks) {
		return prevTicks + (ticks - prevTicks) * partialTicks;
	}

	// How far through the current round we are, smoothed for rendering.
	public static float progress(float partialTicks) {
		return Math.min(elapsed(partialTicks) / roundDuration, 1F);
	}

	//region input

	// The attack key was pressed. Returns true if the minigame consumed it, in which case the click should not also swing the Keyblade.
	public static boolean onAttack() {
		if (!active) {
			return false;
		}

		switch (type) {
			case MASHING -> {
				// The server enforces the real delay; this only stops us flooding it.
				if (ticks - lastPressTick < ShotlockMinigameType.MASHING_PRESS_DELAY) {
					return true;
				}
				lastPressTick = ticks;
				presses++;
				PacketHandler.sendToServer(new CSShotlockMinigameInput(round, 0));
			}
			case TIMING -> {
				if (clicked) {
					return true;
				}
				clicked = true;
				float radius = ShotlockMinigameType.movingRingRadius(progress(0F));
				PacketHandler.sendToServer(new CSShotlockMinigameInput(round, ShotlockMinigameType.gradeTimingRadius(radius)));
			}
			case KEYS -> {
				// Attacking does nothing here, but it shouldn't leak into the world either.
			}
		}

		return true;
	}

	private static void pollDirectionKeys(Minecraft minecraft) {
		KeyMapping[] mappings = {
				minecraft.options.keyUp,
				minecraft.options.keyLeft,
				minecraft.options.keyDown,
				minecraft.options.keyRight
		};

		for (int i = 0; i < mappings.length; i++) {
			boolean down = mappings[i].isDown();
			boolean pressed = down && !KEY_WAS_DOWN[i];
			KEY_WAS_DOWN[i] = down;

			if (pressed && !clicked) {
				clicked = true;
				PacketHandler.sendToServer(new CSShotlockMinigameInput(round, i));
			}
		}
	}

	// Snapshots which direction keys are already held when a round opens, so a key the player never
	// let go of doesn't instantly answer the next prompt.
	private static void refreshKeyStates() {
		Minecraft minecraft = Minecraft.getInstance();
		if (minecraft.options == null) {
			return;
		}

		KEY_WAS_DOWN[0] = minecraft.options.keyUp.isDown();
		KEY_WAS_DOWN[1] = minecraft.options.keyLeft.isDown();
		KEY_WAS_DOWN[2] = minecraft.options.keyDown.isDown();
		KEY_WAS_DOWN[3] = minecraft.options.keyRight.isDown();
	}
	//region end
}

package online.kingdomkeys.kingdomkeys.client.gui.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import online.kingdomkeys.kingdomkeys.client.shotlock.ShotlockMinigameClient;
import online.kingdomkeys.kingdomkeys.shotlock.minigame.ShotlockMinigameType;
import org.joml.Matrix4f;

public class ShotlockMinigameGui extends OverlayBase {

	public static final ShotlockMinigameGui INSTANCE = new ShotlockMinigameGui();

	private static final int COLOUR_TARGET_RING = 0xFFFFD75A;
	private static final int COLOUR_MOVING_RING = 0xFFFFFFFF;
	private static final int COLOUR_PROMPT_BG = 0xB0101828;
	private static final int COLOUR_PROMPT_BORDER = 0xFF6E7FA8;

	private static final String[] KEY_LABELS = {"W", "A", "S", "D"};

	// Mash ring: radius it settles on, how far it swings either side of that, and the cycle length.
	private static final float MASH_BASE_RADIUS = 30F;
	private static final float MASH_PULSE_AMPLITUDE = 8F;
	private static final float MASH_PULSE_PERIOD = ShotlockMinigameType.MASHING_PRESS_DELAY;
	private static final float MASH_RING_HALF = 2.5F;

	private ShotlockMinigameGui() {
		super();
	}

	@Override
	public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
		super.render(guiGraphics, deltaTracker);
		if(minecraft != null && minecraft.options.hideGui){
			return;
		}
		if (minecraft.player == null) {
			return;
		}

		int screenWidth = minecraft.getWindow().getGuiScaledWidth();
		int screenHeight = minecraft.getWindow().getGuiScaledHeight();
		int centreX = screenWidth / 2;
		int centreY = screenHeight / 2;
		float partialTicks = deltaTracker.getGameTimeDeltaPartialTick(false);

		// The last grade of a run arrives together with the packet that ends it, so the flash has to
		// outlive the minigame itself or the final PERFECT would never be seen.
		if (!ShotlockMinigameClient.active) {
			renderResultFlash(guiGraphics, centreX, centreY);
			return;
		}

		switch (ShotlockMinigameClient.type) {
			case MASHING -> renderMashing(guiGraphics, centreX, centreY, screenHeight, partialTicks);
			case TIMING -> renderTiming(guiGraphics, centreX, centreY, partialTicks);
			case KEYS -> renderKeys(guiGraphics, centreX, centreY, screenHeight, partialTicks);
		}

		renderRoundCounter(guiGraphics, centreX, centreY);
		renderResultFlash(guiGraphics, centreX, centreY);
	}

	// --- Minigame 1: mash ---------------------------------------------------------------------

	private void renderMashing(GuiGraphics guiGraphics, int centreX, int centreY, int screenHeight, float partialTicks) {
		// A ring that breathes in and out by the same amount around a fixed radius. The symmetry is
		// the point: the timing ring only ever shrinks, so a circle that comes back out again can't
		// be mistaken for one you're supposed to be waiting on.
		float wave = (float) Math.sin((ShotlockMinigameClient.elapsed(partialTicks) / MASH_PULSE_PERIOD) * Math.PI * 2D);
		float radius = MASH_BASE_RADIUS + wave * MASH_PULSE_AMPLITUDE;

		// Whitest at the peak of the expansion, back to the base gold at the trough. An accepted
		// press also drives it white, so the ring acknowledges every hit.
		int sincePress = ShotlockMinigameClient.ticks - ShotlockMinigameClient.lastPressTick;
		float pressGlow = sincePress < ShotlockMinigameType.MASHING_PRESS_DELAY
				? 1F - (sincePress / (float) ShotlockMinigameType.MASHING_PRESS_DELAY)
				: 0F;
		float whiteness = Math.max((wave + 1F) * 0.5F, pressGlow);

		drawRing(guiGraphics, centreX, centreY, radius, MASH_RING_HALF, lerpColour(COLOUR_TARGET_RING, 0xFFFFFFFF, whiteness));

		// The prompt sits inside the ring rather than above it, so the circle reads as a frame around
		// the word instead of as a target to hit.
		String prompt = Component.translatable("gui.shotlock.minigame.mash").getString();
		drawCentered(guiGraphics, prompt, centreX, centreY - font.lineHeight / 2, lerpColour(COLOUR_TARGET_RING, 0xFFFFFFFF, whiteness));

		String count = Component.translatable("gui.shotlock.minigame.hits", ShotlockMinigameClient.presses).getString();
		drawCentered(guiGraphics, count, centreX, centreY + (int) (MASH_BASE_RADIUS + MASH_PULSE_AMPLITUDE) + 8, 0xFFFFFFFF);

		drawTimerBar(guiGraphics, centreX, screenHeight - 60, partialTicks);
	}

	// Blends two ARGB colours.
	private static int lerpColour(int from, int to, float t) {
		t = Mth.clamp(t, 0F, 1F);
		int a = Math.round(Mth.lerp(t, (from >>> 24) & 0xFF, (to >>> 24) & 0xFF));
		int r = Math.round(Mth.lerp(t, (from >> 16) & 0xFF, (to >> 16) & 0xFF));
		int g = Math.round(Mth.lerp(t, (from >> 8) & 0xFF, (to >> 8) & 0xFF));
		int b = Math.round(Mth.lerp(t, from & 0xFF, to & 0xFF));
		return (a << 24) | (r << 16) | (g << 8) | b;
	}

	// --- Minigame 2: timing ring --------------------------------------------------------------

	private void renderTiming(GuiGraphics guiGraphics, int centreX, int centreY, float partialTicks) {
		drawRing(guiGraphics, centreX, centreY, ShotlockMinigameType.RING_TARGET_RADIUS, ShotlockMinigameType.RING_TARGET_HALF, COLOUR_TARGET_RING);

		if (ShotlockMinigameClient.clicked) {
			return; // already clicked, don't keep the ring shrinking past the answer
		}

		float radius = ShotlockMinigameType.movingRingRadius(ShotlockMinigameClient.progress(partialTicks));
		drawRing(guiGraphics, centreX, centreY, radius, ShotlockMinigameType.RING_MOVING_HALF, COLOUR_MOVING_RING);
	}

	// --- Minigame 3: WASD prompts -------------------------------------------------------------

	private void renderKeys(GuiGraphics guiGraphics, int centreX, int centreY, int screenHeight, float partialTicks) {
		int prompted = Mth.clamp(ShotlockMinigameClient.WASDRequest, 0, 3);

		// The big prompt in the middle of the screen.
		drawKeyBox(guiGraphics, centreX - 14, centreY - 14, 28, KEY_LABELS[prompted], true);

		// The little WASD cluster near the bottom, with the live one lit up.
		int clusterY = screenHeight - 72;
		int size = 16;
		int gap = 2;
		drawKeyBox(guiGraphics, centreX - size / 2, clusterY, size, "W", prompted == 0);
		drawKeyBox(guiGraphics, centreX - size / 2 - size - gap, clusterY + size + gap, size, "A", prompted == 1);
		drawKeyBox(guiGraphics, centreX - size / 2, clusterY + size + gap, size, "S", prompted == 2);
		drawKeyBox(guiGraphics, centreX - size / 2 + size + gap, clusterY + size + gap, size, "D", prompted == 3);

		float perfectFraction = ShotlockMinigameClient.roundDuration <= 0 ? 0F
				: ShotlockMinigameType.KEYS_PERFECT_TICKS / (float) ShotlockMinigameClient.roundDuration;
		drawTimerBar(guiGraphics, centreX, clusterY - 12, partialTicks, perfectFraction);
	}

	private void drawKeyBox(GuiGraphics guiGraphics, int x, int y, int size, String label, boolean highlighted) {
		guiGraphics.fill(x, y, x + size, y + size, COLOUR_PROMPT_BG);

		int border = highlighted ? COLOUR_TARGET_RING : COLOUR_PROMPT_BORDER;
		guiGraphics.fill(x, y, x + size, y + 1, border);
		guiGraphics.fill(x, y + size - 1, x + size, y + size, border);
		guiGraphics.fill(x, y, x + 1, y + size, border);
		guiGraphics.fill(x + size - 1, y, x + size, y + size, border);

		int textColour = highlighted ? 0xFFFFFFFF : 0xFF8E9BB8;
		int textX = x + (size - font.width(label)) / 2;
		int textY = y + (size - font.lineHeight) / 2 + 1;
		guiGraphics.drawString(font, label, textX, textY, textColour, true);
	}

	// --- Shared bits --------------------------------------------------------------------------

	private void renderRoundCounter(GuiGraphics guiGraphics, int centreX, int centreY) {
		if (ShotlockMinigameClient.totalRounds <= 1) {
			return;
		}

		String text = ShotlockMinigameClient.round + " / " + ShotlockMinigameClient.totalRounds;
		drawCentered(guiGraphics, text, centreX, centreY - 68, 0xFFB9C6E0);
	}

	private void renderResultFlash(GuiGraphics guiGraphics, int centreX, int centreY) {
		if (ShotlockMinigameClient.flashTicks <= 0) {
			return;
		}

		String key = switch (ShotlockMinigameClient.lastResult) {
			case 2 -> "gui.shotlock.minigame.perfect";
			case 1 -> "gui.shotlock.minigame.good";
			case 0 -> "gui.shotlock.minigame.bad";
			default -> "gui.shotlock.minigame.miss";
		};

		drawCentered(guiGraphics, Component.translatable(key).getString(), centreX, centreY + 58, COLOUR_MOVING_RING);
	}

	private void drawTimerBar(GuiGraphics guiGraphics, int centreX, int y, float partialTicks) {
		drawTimerBar(guiGraphics, centreX, y, partialTicks, 0F);
	}

	private void drawTimerBar(GuiGraphics guiGraphics, int centreX, int y, float partialTicks, float perfectFraction) {
		int width = 80;
		int height = 3;
		int x = centreX - width / 2;

		guiGraphics.fill(x - 1, y - 1, x + width + 1, y + height + 1, COLOUR_PROMPT_BG);

		float progress = ShotlockMinigameClient.progress(partialTicks);
		int filled = (int) (width * Mth.clamp(1F - progress, 0F, 1F));

		if (filled > 0) {
			boolean stillPerfect = perfectFraction > 0F && progress <= perfectFraction;
			if (stillPerfect) {
				guiGraphics.fillGradient(x, y, x + filled, y + height, 0xFF7CFF9E, 0xFF2FA85A);
			} else {
				guiGraphics.fillGradient(x, y, x + filled, y + height, 0xFFFF9000, 0xFFC81400);
			}
		}

		// The bar drains right to left, so the perfect window runs out once its right edge passes here.
		if (perfectFraction > 0F) {
			int notch = x + (int) (width * Mth.clamp(1F - perfectFraction, 0F, 1F));
			guiGraphics.fill(notch, y - 2, notch + 1, y + height + 2, 0xFFFFFFFF);
		}
	}

	private void drawCentered(GuiGraphics guiGraphics, String text, int centreX, int y, int colour) {
		guiGraphics.drawString(font, text, centreX - font.width(text) / 2, y, colour, true);
	}

	// Rings are a proper tesselated annulus rather than a run of fill squares. The old
	// approach anchored each square by its top-left corner, which pushed the whole ring down and to
	// the right by half its thickness and left visible stair-stepping; going through the buffer
	// keeps the geometry on real floats, so it's both centred and smooth.
	private void drawRing(GuiGraphics guiGraphics, float centreX, float centreY, float radius, float halfThickness, int colour) {
		if (radius <= 0.5F) {
			return;
		}

		float inner = Math.max(0F, radius - halfThickness);
		float outer = radius + halfThickness;

		float a = (colour >>> 24) / 255F;
		float r = ((colour >> 16) & 0xFF) / 255F;
		float g = ((colour >> 8) & 0xFF) / 255F;
		float b = (colour & 0xFF) / 255F;

		// Enough segments that the flat edge of a segment never strays more than a fraction of a
		// pixel from the true circle, with a floor so tiny rings still look round.
		int segments = Mth.clamp((int) (radius * 3F), 48, 180);

		Matrix4f pose = guiGraphics.pose().last().pose();

		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.disableCull();
		RenderSystem.setShader(GameRenderer::getPositionColorShader);

		BufferBuilder buffer = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

		for (int i = 0; i < segments; i++) {
			double a0 = (Math.PI * 2D * i) / segments;
			double a1 = (Math.PI * 2D * (i + 1)) / segments;

			float cos0 = (float) Math.cos(a0);
			float sin0 = (float) Math.sin(a0);
			float cos1 = (float) Math.cos(a1);
			float sin1 = (float) Math.sin(a1);

			buffer.addVertex(pose, centreX + cos0 * inner, centreY + sin0 * inner, 0F).setColor(r, g, b, a);
			buffer.addVertex(pose, centreX + cos1 * inner, centreY + sin1 * inner, 0F).setColor(r, g, b, a);
			buffer.addVertex(pose, centreX + cos1 * outer, centreY + sin1 * outer, 0F).setColor(r, g, b, a);
			buffer.addVertex(pose, centreX + cos0 * outer, centreY + sin0 * outer, 0F).setColor(r, g, b, a);
		}

		BufferUploader.drawWithShader(buffer.build());

		RenderSystem.enableCull();
		RenderSystem.disableBlend();
	}

	@SubscribeEvent
	public void clientTick(ClientTickEvent.Post event) {
		if (minecraft.isPaused()) {
			return;
		}
		ShotlockMinigameClient.tick();
	}
}

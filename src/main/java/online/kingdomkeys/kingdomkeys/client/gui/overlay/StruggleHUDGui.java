package online.kingdomkeys.kingdomkeys.client.gui.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.lib.Struggle;

import java.util.List;
import java.util.stream.Collectors;

/**
 * KH2-style "ORBS" counter + round timer, shown to anyone within {@link #SPECTATE_RANGE} blocks of an
 * in-progress Struggle match (combatants and spectators alike), so bystanders can follow the fight too.
 * With exactly 2 combatants (DUEL/TOURNAMENT) they're shown in the classic left/right layout; with more
 * (FFA) they're listed down the right side instead.
 */
public class StruggleHUDGui extends OverlayBase {

	public static final StruggleHUDGui INSTANCE = new StruggleHUDGui();

	private static final ResourceLocation ORB_TEXTURE = KingdomKeys.rl("textures/entity/struggle_orb.png");
	private static final int ORB_SIZE = 20; // on-screen display size
	private static final int ORB_TEXTURE_RESOLUTION = 16; // actual struggle_orb.png size
	private static final double SPECTATE_RANGE = 50.0;

	private StruggleHUDGui() {
		super();
	}

	// Milliseconds interpolation
	private String lastTimedStruggle = null;
	private int lastSyncedSeconds = -1;
	private long lastSyncedGameTime = 0;

	@Override
	public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
		super.render(guiGraphics, deltaTracker);
		Player player = minecraft.player;
		if (player == null)
			return;

		Struggle struggle = findNearbyMatch(player);
		if (struggle == null)
			return;

		List<Struggle.Participant> combatants = struggle.getActiveCombatantIds().stream().map(struggle::getParticipant).filter(java.util.Objects::nonNull).collect(Collectors.toList());
		if (combatants.size() < 2)
			return;

		int screenWidth = minecraft.getWindow().getGuiScaledWidth();
		int margin = 12;
		int y = 12;

		drawTimer(guiGraphics, struggle, screenWidth / 2, y);

		if (combatants.size() == 2) {
			drawSide(guiGraphics, combatants.get(0), margin, y, true);
			drawSide(guiGraphics, combatants.get(1), screenWidth - margin, y, false);
		} else {
			// FFA with more than 2 fighters - list everyone down the right side instead.
			int listY = y;
			for (Struggle.Participant participant : combatants) {
				drawSide(guiGraphics, participant, screenWidth - margin, listY, false);
				listY += ORB_SIZE + font.lineHeight + 8;
			}
		}
	}

	/** The nearest in-progress Struggle whose arena is within {@link #SPECTATE_RANGE} blocks, if any. */
	private Struggle findNearbyMatch(Player player) {
		WorldData worldData = WorldData.getClient();
		Struggle nearest = null;
		double nearestDistSqr = SPECTATE_RANGE * SPECTATE_RANGE;

		for (Struggle struggle : worldData.getStruggles()) {
			if (!struggle.isInProgress() || struggle.getC1() == null || struggle.getC2() == null) continue;

			double centerX = (struggle.getC1().getX() + struggle.getC2().getX()) / 2.0;
			double centerY = (struggle.getC1().getY() + struggle.getC2().getY()) / 2.0;
			double centerZ = (struggle.getC1().getZ() + struggle.getC2().getZ()) / 2.0;
			double distSqr = player.distanceToSqr(centerX, centerY, centerZ);

			if (distSqr <= nearestDistSqr) {
				nearest = struggle;
				nearestDistSqr = distSqr;
			}
		}
		return nearest;
	}

	private void drawTimer(GuiGraphics gui, Struggle struggle, int centerX, int y) {
		int seconds = Math.max(0, struggle.getRoundSecondsLeft());
		long gameTime = minecraft.level.getGameTime();

		if (!struggle.getName().equals(lastTimedStruggle) || seconds != lastSyncedSeconds) {
			lastTimedStruggle = struggle.getName();
			lastSyncedSeconds = seconds;
			lastSyncedGameTime = gameTime;
		}

		long elapsedMs = (gameTime - lastSyncedGameTime) * 50L; // 1 tick = 50ms
		long msRemaining = Math.max(0, seconds * 1000L - elapsedMs);

		long minutes = msRemaining / 60000L;
		long secs = (msRemaining % 60000L) / 1000L;
		long centis = (msRemaining % 1000L) / 10L;

		MutableComponent timeText = Component.literal(String.format("%02d'%02d\"%02d", minutes, secs, centis));
		gui.drawString(font,timeText.withStyle(ClientUtils.KK_Font_EXP), centerX - font.width(timeText) / 2, y, 0xFFD900);
	}

	/**
	 * @param anchorX for the left side, the left edge of the whole widget; for the right side, the
	 *                right edge of the whole widget (everything is laid out growing away from it).
	 * @param fromLeft true = icon then number (grows rightward), false = number then icon (grows leftward)
	 */
	private void drawSide(GuiGraphics gui, Struggle.Participant participant, int anchorX, int y, boolean fromLeft) {
		int color = colorFor(participant);
		String scoreText = String.valueOf(participant.getScore());
		int textWidth = font.width(scoreText);

		String label = combatantsLabel(participant);

		int iconX = fromLeft ? anchorX : anchorX - ORB_SIZE;
		int textX = fromLeft ? anchorX + ORB_SIZE + 4 : anchorX - ORB_SIZE - 10 - textWidth;
		int widgetCenter = fromLeft ? anchorX + (ORB_SIZE + 4 + textWidth) / 2 : anchorX - (ORB_SIZE + 4 + textWidth) / 2;

		drawCenteredString(gui, font, label, widgetCenter, y, 0xFFFFFF);

		int iconY = y + font.lineHeight + 2;

		RenderSystem.setShaderColor(((color >> 16) & 0xFF) / 255F, ((color >> 8) & 0xFF) / 255F, (color & 0xFF) / 255F, 1F);
		this.blit(gui, ORB_TEXTURE, iconX, iconY, ORB_SIZE, ORB_SIZE, 0, 0, ORB_TEXTURE_RESOLUTION, ORB_TEXTURE_RESOLUTION, ORB_TEXTURE_RESOLUTION, ORB_TEXTURE_RESOLUTION);
		RenderSystem.setShaderColor(1F, 1F, 1F, 1F);

		gui.drawString(font, Component.literal(scoreText).withStyle(ClientUtils.KK_Font_EXP), textX, iconY + (ORB_SIZE / 2) - (font.lineHeight / 2), color);
	}

	private String combatantsLabel(Struggle.Participant participant) {
		return participant.getUsername();
	}

	private int colorFor(Struggle.Participant participant) {
		Player player = minecraft.level == null ? null : minecraft.level.getPlayerByUUID(participant.getUUID());
		if (player == null) return 0xFFFFFF;
		PlayerData playerData = PlayerData.get(player);
		return playerData == null ? 0xFFFFFF : playerData.getNotifColor();
	}
}
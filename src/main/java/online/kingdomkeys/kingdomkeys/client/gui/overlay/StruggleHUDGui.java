package online.kingdomkeys.kingdomkeys.client.gui.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.lib.Struggle;


public class StruggleHUDGui extends OverlayBase {

	public static final StruggleHUDGui INSTANCE = new StruggleHUDGui();

	private static final ResourceLocation ORB_TEXTURE = KingdomKeys.rl("textures/entity/struggle_orb.png");
	private static final int ORB_SIZE = 20;

	private StruggleHUDGui() {
		super();
	}

	@Override
	public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
		super.render(guiGraphics, deltaTracker);
		Player player = minecraft.player;
		if (player == null) return;

		WorldData worldData = WorldData.getClient();
		Struggle struggle = worldData.getStruggleFromParticipant(player.getUUID());
		if (struggle == null || !struggle.isInProgress() || struggle.getParticipants().size() < 2) return;

		Struggle.Participant left = struggle.getParticipants().get(0);
		Struggle.Participant right = struggle.getParticipants().get(1);

		int screenWidth = minecraft.getWindow().getGuiScaledWidth();
		int margin = 12;
		int y = 12;

		drawSide(guiGraphics, left, margin, y, true);
		drawSide(guiGraphics, right, screenWidth - margin, y, false);
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

		String label = "ORBS";
		int labelWidth = font.width(label);

		int iconX = fromLeft ? anchorX : anchorX - ORB_SIZE;
		int textX = fromLeft ? anchorX + ORB_SIZE + 4 : anchorX - ORB_SIZE - 4 - textWidth;
		int widgetCenter = fromLeft ? anchorX + (ORB_SIZE + 4 + textWidth) / 2 : anchorX - (ORB_SIZE + 4 + textWidth) / 2;

		drawCenteredString(gui, font, label, widgetCenter, y, 0xFFFFFF);

		int iconY = y + font.lineHeight + 2;

		RenderSystem.setShaderColor(((color >> 16) & 0xFF) / 255F, ((color >> 8) & 0xFF) / 255F, (color & 0xFF) / 255F, 1F);
		this.blit(gui, ORB_TEXTURE, iconX, iconY, 0, 0, ORB_SIZE, ORB_SIZE);
		RenderSystem.setShaderColor(1F, 1F, 1F, 1F);

		gui.drawString(font, scoreText, textX, iconY + (ORB_SIZE / 2) - (font.lineHeight / 2), color);
	}

	private int colorFor(Struggle.Participant participant) {
		Player player = minecraft.level == null ? null : minecraft.level.getPlayerByUUID(participant.getUUID());
		if (player == null) return 0xFFFFFF;
		PlayerData playerData = PlayerData.get(player);
		return playerData == null ? 0xFFFFFF : playerData.getNotifColor();
	}
}

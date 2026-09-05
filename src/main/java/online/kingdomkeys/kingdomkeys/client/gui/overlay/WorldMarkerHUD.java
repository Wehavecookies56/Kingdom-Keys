package online.kingdomkeys.kingdomkeys.client.gui.overlay;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import online.kingdomkeys.kingdomkeys.client.render.WorldMapRenderer;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.entity.GummiShipEntity;
import online.kingdomkeys.kingdomkeys.world.worldmap.GummiWorld;
import online.kingdomkeys.kingdomkeys.world.worldmap.GummiWorldLoader;
import org.joml.Matrix4f;
import org.joml.Vector4f;

import java.util.Locale;

public class WorldMarkerHUD extends OverlayBase {

	public static final WorldMarkerHUD INSTANCE = new WorldMarkerHUD();

	// How far in from the edge an off-screen arrow sits
	private static final int EDGE_INSET = 14;

	private static final int RING_RADIUS = 5;
	private static final int ARROW_SIZE = 5;
	private static final int LABEL_GAP = 3;

	private static final int LABEL = 0xFFCFD8E8;

	/** The colour comes from the world's own data, so only the opacity is decided here. */
	private static final int SOLID = 0xFF000000;
	private static final int FAINT = 0x99000000;

	@Override
	public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
		super.render(guiGraphics, deltaTracker);

		if (minecraft == null || minecraft.options.hideGui) {
			return;
		}

		Player player = minecraft.player;

		// Only render on a ship, TODO only when ship has a radar?
		if (player == null || !(player.getVehicle() instanceof GummiShipEntity)) {
			return;
		}

		Matrix4f modelView = WorldMapRenderer.modelViewMatrix();
		Matrix4f projection = WorldMapRenderer.projectionMatrix();
		Vec3 camera = WorldMapRenderer.cameraPosition();

		// Everything comes from the frame the star map just drew, so there is nothing to point at until it has drawn one
		if (modelView == null || projection == null || camera == null) {
			return;
		}

		PlayerData playerData = PlayerData.get(player);
		if (playerData == null) {
			return;
		}

		int width = minecraft.getWindow().getGuiScaledWidth();
		int height = minecraft.getWindow().getGuiScaledHeight();
		Matrix4f combined = new Matrix4f(projection).mul(modelView);

		for (GummiWorld world : GummiWorldLoader.all().values()) {
			if (!playerData.knowsWorld(world)) {
				continue;
			}

			drawMarker(world, combined, camera, width, height);
		}
	}

	private void drawMarker(GummiWorld world, Matrix4f combined, Vec3 camera, int width, int height) {
		Vec3 at = world.worldmapPosition();
		double distance = at.distanceTo(camera);

		Vector4f clip = new Vector4f((float) (at.x - camera.x), (float) (at.y - camera.y), (float) (at.z - camera.z), 1F);
		clip.mul(combined);

		boolean behind = clip.w <= 0F;
		float x;
		float y;

		if (behind) {
			x = width * 0.5F - (clip.x / clip.w) * 0.5F * width;
			y = height * 0.5F + (clip.y / clip.w) * 0.5F * height;
		} else {
			x = (clip.x / clip.w * 0.5F + 0.5F) * width;
			y = (1F - (clip.y / clip.w * 0.5F + 0.5F)) * height;
		}

		boolean offScreen = behind || x < EDGE_INSET || x > width - EDGE_INSET || y < EDGE_INSET || y > height - EDGE_INSET;

		if (offScreen) {
			drawEdgeArrow(x, y, width, height, world, distance);
		} else {
			drawOnScreen((int) x, (int) y, world, distance);
		}
	}

	private void drawOnScreen(int x, int y, GummiWorld world, double distance) {
		int colour = SOLID | world.markerColour();
		ring(x, y, RING_RADIUS, colour);

		String name = nameOf(world);
		String range = distanceOf(distance);

		drawCenteredString(guiGraphics, font, name, x, y + RING_RADIUS + LABEL_GAP, colour);
		drawCenteredString(guiGraphics, font, range, x, y + RING_RADIUS + LABEL_GAP + font.lineHeight, LABEL);
	}

	private void drawEdgeArrow(float x, float y, int width, int height, GummiWorld world, double distance) {
		float centreX = width * 0.5F;
		float centreY = height * 0.5F;

		float dx = x - centreX;
		float dy = y - centreY;
		if (Math.abs(dx) < 1.0E-4F && Math.abs(dy) < 1.0E-4F) {
			dy = 1F;
		}

		// How far the ray can be stretched before it leaves the box, taking whichever side it meets first
		float limitX = (width * 0.5F - EDGE_INSET) / Math.max(Math.abs(dx), 1.0E-4F);
		float limitY = (height * 0.5F - EDGE_INSET) / Math.max(Math.abs(dy), 1.0E-4F);
		float scale = Math.min(limitX, limitY);

		int edgeX = Mth.floor(centreX + dx * scale);
		int edgeY = Mth.floor(centreY + dy * scale);

		arrow(edgeX, edgeY, dx, dy, SOLID | world.markerColour());

		String range = distanceOf(distance);
		drawCenteredString(guiGraphics, font, range, edgeX, edgeY + ARROW_SIZE + LABEL_GAP, FAINT | world.markerColour());
	}

	// A hollow square standing in for a circle, drawn from four lines so no texture is needed
	private void ring(int x, int y, int radius, int colour) {
		guiGraphics.fill(x - radius, y - radius, x + radius + 1, y - radius + 1, colour);
		guiGraphics.fill(x - radius, y + radius, x + radius + 1, y + radius + 1, colour);
		guiGraphics.fill(x - radius, y - radius, x - radius + 1, y + radius + 1, colour);
		guiGraphics.fill(x + radius, y - radius, x + radius + 1, y + radius + 1, colour);
	}

	private void arrow(int x, int y, float dx, float dy, int colour) {
		boolean horizontal = Math.abs(dx) > Math.abs(dy);

		for (int step = 0; step < ARROW_SIZE; step++) {
			int thickness = ARROW_SIZE - step;

			if (horizontal) {
				int column = dx > 0 ? x + step : x - step;
				guiGraphics.fill(column, y - thickness, column + 1, y + thickness, colour);
			} else {
				int row = dy > 0 ? y + step : y - step;
				guiGraphics.fill(x - thickness, row, x + thickness, row + 1, colour);
			}
		}
	}

	private static String nameOf(GummiWorld world) {
		String path = world.dimension().location().getPath();
		String key = "kingdomkeys.worldmap.world." + path;

		if (I18n.exists(key)) {
			return I18n.get(key);
		}

		StringBuilder name = new StringBuilder();
		for (String word : path.split("_")) {
			if (word.isEmpty()) {
				continue;
			}
			if (!name.isEmpty()) {
				name.append(' ');
			}
			name.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1));
		}
		return name.toString();
	}

	private static String distanceOf(double distance) {
		if (distance < 1000) {
			return Mth.floor(distance) + "m";
		}
		return String.format(Locale.ROOT, "%.1fkm", distance / 1000D);
	}
}

package online.kingdomkeys.kingdomkeys.client.gui.elements;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.Mth;
import org.joml.Matrix4f;

import java.util.List;

/**
 * A polygon with one corner per axis, starting at the top and going clockwise, filled as far along each
 * axis as its value reaches towards its cap. Each corner gets its label and value written beside it.
 */
public class RadarChart {
	public record Axis(String label, int value, int max) {
		float reach() {
			return max <= 0 ? 0F : Mth.clamp((float) value / max, 0F, 1F);
		}
	}

	private static final int LABEL_GAP = 3;

	private final int radius;
	private List<Axis> axes = List.of();
	private int gridColour = 0xFF2C5F8A, fillColour = 0x8040B8FF, edgeColour = 0xFF9CE6FF;
	private int labelColour = 0xA0A0A0, valueColour = 0xFFFFFF;

	private float[] dirX = new float[0], dirY = new float[0];
	// Bounds of the polygon and every label, around the centre
	private float left, right, top, bottom;

	public RadarChart(int radius) {
		this.radius = radius;
	}

	public RadarChart setAxes(List<Axis> axes) {
		this.axes = axes;
		dirX = new float[axes.size()];
		dirY = new float[axes.size()];
		for (int i = 0; i < axes.size(); i++) {
			double angle = -Math.PI / 2D + Math.PI * 2D * i / axes.size();
			dirX[i] = (float) Math.cos(angle);
			dirY[i] = (float) Math.sin(angle);
		}
		return this;
	}

	public RadarChart setColours(int grid, int fill, int edge) {
		this.gridColour = grid;
		this.fillColour = fill;
		this.edgeColour = edge;
		return this;
	}

	public RadarChart setTextColours(int label, int value) {
		this.labelColour = label;
		this.valueColour = value;
		return this;
	}

	public int getWidth(Font font) {
		measure(font);
		return Mth.ceil(2 * Math.max(-left, right));
	}

	public int getHeight(Font font) {
		measure(font);
		return Mth.ceil(bottom - top);
	}

	/** Draws the chart centred in a box of the given width whose top edge is y */
	public void render(GuiGraphics gui, Font font, int x, int y, int width) {
		if (axes.size() < 3) {
			return;
		}
		measure(font);
		float cx = x + width / 2F;
		float cy = y - top;

		// Whatever is queued underneath has to be drawn before the chart goes on top of it
		gui.flush();
		drawShape(gui.pose().last().pose(), cx, cy);

		int lineHeight = font.lineHeight + 1;
		for (int i = 0; i < axes.size(); i++) {
			Axis axis = axes.get(i);
			String[] lines = {axis.label(), String.valueOf(axis.value())};
			float labelTop = cy + labelTop(i, lineHeight);
			for (int l = 0; l < lines.length; l++) {
				gui.drawString(font, lines[l], Math.round(cx + labelLeft(i, font.width(lines[l]))), Math.round(labelTop) + l * lineHeight, l == 0 ? labelColour : valueColour, false);
			}
		}
	}

	private boolean onSide(int i) {
		return Math.abs(dirX[i]) > 0.1F;
	}

	private float labelLeft(int i, int textWidth) {
		float vx = dirX[i] * radius;
		if (!onSide(i)) {
			return vx - textWidth / 2F;
		}
		return dirX[i] > 0 ? vx + LABEL_GAP : vx - LABEL_GAP - textWidth;
	}

	private float labelTop(int i, int lineHeight) {
		float vy = dirY[i] * radius;
		if (onSide(i)) {
			return vy - lineHeight;
		}
		return dirY[i] < 0 ? vy - LABEL_GAP - lineHeight * 2 : vy + LABEL_GAP;
	}

	private void measure(Font font) {
		int lineHeight = font.lineHeight + 1;
		left = top = -radius;
		right = bottom = radius;
		for (int i = 0; i < axes.size(); i++) {
			Axis axis = axes.get(i);
			int textWidth = Math.max(font.width(axis.label()), font.width(String.valueOf(axis.value())));
			float lx = labelLeft(i, textWidth), ly = labelTop(i, lineHeight);
			left = Math.min(left, lx);
			right = Math.max(right, lx + textWidth);
			top = Math.min(top, ly);
			bottom = Math.max(bottom, ly + lineHeight * 2);
		}
	}

	private void drawShape(Matrix4f pose, float cx, float cy) {
		int corners = axes.size();
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.setShader(GameRenderer::getPositionColorShader);

		BufferBuilder grid = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
		for (int i = 0; i < corners; i++) {
			int next = (i + 1) % corners;
			line(grid, pose, cx, cy, cx + dirX[i] * radius, cy + dirY[i] * radius, gridColour);
			for (float ring : new float[]{0.5F, 1F}) {
				float r = radius * ring;
				line(grid, pose, cx + dirX[i] * r, cy + dirY[i] * r, cx + dirX[next] * r, cy + dirY[next] * r, gridColour);
			}
		}
		BufferUploader.drawWithShader(grid.buildOrThrow());

		BufferBuilder fill = Tesselator.getInstance().begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
		fill.addVertex(pose, cx, cy, 0F).setColor(fillColour);
		for (int i = 0; i <= corners; i++) {
			int c = i % corners;
			float r = radius * axes.get(c).reach();
			fill.addVertex(pose, cx + dirX[c] * r, cy + dirY[c] * r, 0F).setColor(fillColour);
		}
		BufferUploader.drawWithShader(fill.buildOrThrow());

		BufferBuilder edge = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
		for (int i = 0; i < corners; i++) {
			int next = (i + 1) % corners;
			float r = radius * axes.get(i).reach(), rNext = radius * axes.get(next).reach();
			line(edge, pose, cx + dirX[i] * r, cy + dirY[i] * r, cx + dirX[next] * rNext, cy + dirY[next] * rNext, edgeColour);
		}
		// Every value at zero leaves no edge to draw
		MeshData mesh = edge.build();
		if (mesh != null) {
			BufferUploader.drawWithShader(mesh);
		}

		RenderSystem.disableBlend();
	}

	// A one pixel wide line as a thin quad
	private static void line(BufferBuilder buffer, Matrix4f pose, float x1, float y1, float x2, float y2, int colour) {
		float dx = x2 - x1, dy = y2 - y1;
		float length = Mth.sqrt(dx * dx + dy * dy);
		if (length < 0.001F) {
			return;
		}
		float nx = -dy / length * 0.5F, ny = dx / length * 0.5F;
		buffer.addVertex(pose, x1 + nx, y1 + ny, 0F).setColor(colour);
		buffer.addVertex(pose, x2 + nx, y2 + ny, 0F).setColor(colour);
		buffer.addVertex(pose, x2 - nx, y2 - ny, 0F).setColor(colour);
		buffer.addVertex(pose, x1 - nx, y1 - ny, 0F).setColor(colour);
	}
}

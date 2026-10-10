package online.kingdomkeys.kingdomkeys.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.Util;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.ReceivingLevelScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

import java.util.function.BooleanSupplier;

public class RODTransitionScreen extends ReceivingLevelScreen {
	private static final long SWALLOW_MS = 900L, FADE_MS = 250L, TIMEOUT_MS = 30000L;

	private static final int SEGMENTS = 96;
	private static final int TENDRILS = 9, TENDRIL_POINTS = 24;
	private static final int MOTES = 70;

	private final BooleanSupplier levelReceived;
	private final long createdAt = Util.getMillis();
	private long arrivedAt = -1L;

	// The last frame of the world being left, since by the time this opens the client already holds the new one
	@Nullable
	private ResourceLocation snapshot;
	private int snapshotWidth, snapshotHeight;

	public RODTransitionScreen(BooleanSupplier levelReceived, Reason reason) {
		super(levelReceived, reason);
		this.levelReceived = levelReceived;

		try {
			NativeImage image = Screenshot.takeScreenshot(Minecraft.getInstance().getMainRenderTarget());
			snapshotWidth = image.getWidth();
			snapshotHeight = image.getHeight();
			snapshot = Minecraft.getInstance().getTextureManager().register("kingdomkeys_darkness_snapshot", new DynamicTexture(image));
		} catch (Exception e) {
			snapshot = null;
		}
	}

	@Override
	public void removed() {
		super.removed();

		if (snapshot != null) {
			Minecraft.getInstance().getTextureManager().release(snapshot);
			snapshot = null;
		}
	}

	@Override
	public void tick() {
		long now = Util.getMillis();

		// It always gets to finish swallowing, however quickly the world turns up
		if (arrivedAt < 0L && (levelReceived.getAsBoolean() && now - createdAt >= SWALLOW_MS || now - createdAt >= TIMEOUT_MS)) {
			arrivedAt = now;
		}

		if (arrivedAt >= 0L && now - arrivedAt >= FADE_MS) {
			onClose();
		}
	}

	@Override
	public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		long now = Util.getMillis();
		float seconds = (now - createdAt) / 1000F;
		float closing = smooth(Mth.clamp((now - createdAt) / (float) SWALLOW_MS, 0F, 1F));
		float fade = arrivedAt < 0L ? 1F : 1F - smooth(Mth.clamp((now - arrivedAt) / (float) FADE_MS, 0F, 1F));

		float cx = width / 2F, cy = height / 2F;
		float reach = (float) Math.sqrt(cx * cx + cy * cy) + 8F;
		float hole = reach * (1F - closing);

		guiGraphics.flush();

		// Until the dark lets go, nothing of the new world shows: the hole still looks out on the old one
		if (arrivedAt < 0L) {
			if (snapshot != null) {
				guiGraphics.blit(snapshot, 0, 0, width, height, 0F, 0F, snapshotWidth, snapshotHeight, snapshotWidth, snapshotHeight);
			} else {
				guiGraphics.fill(0, 0, width, height, 0xFF000000);
			}
			guiGraphics.flush();
		}

		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.disableCull();
		RenderSystem.setShader(GameRenderer::getPositionColorShader);

		Matrix4f pose = guiGraphics.pose().last().pose();

		drawLight(pose, cx, cy, hole, closing, fade);
		drawDark(pose, cx, cy, hole, reach, seconds, fade);
		drawTendrils(pose, cx, cy, hole, reach, seconds, closing, fade);
		drawMotes(pose, cx, cy, reach, seconds, fade);

		RenderSystem.enableCull();
		RenderSystem.disableBlend();
	}

	// What is left of the light: a pale glow at the heart of the hole, dimming as it shrinks
	private void drawLight(Matrix4f pose, float cx, float cy, float hole, float closing, float fade) {
		if (hole <= 0.5F) {
			return;
		}

		float alpha = 0.55F * (1F - closing) * fade;
		BufferBuilder fan = Tesselator.getInstance().begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
		fan.addVertex(pose, cx, cy, 0F).setColor(0.85F, 0.8F, 1F, alpha);

		for (int i = 0; i <= SEGMENTS; i++) {
			double angle = Math.PI * 2D * i / SEGMENTS;
			fan.addVertex(pose, cx + (float) Math.cos(angle) * hole, cy + (float) Math.sin(angle) * hole, 0F).setColor(0.35F, 0.15F, 0.55F, 0F);
		}

		BufferUploader.drawWithShader(fan.buildOrThrow());
	}

	// The dark itself, with a ragged edge that keeps writhing as it closes
	private void drawDark(Matrix4f pose, float cx, float cy, float hole, float reach, float seconds, float fade) {
		float edge = 18F + hole * 0.25F;
		BufferBuilder strip = Tesselator.getInstance().begin(VertexFormat.Mode.TRIANGLE_STRIP, DefaultVertexFormat.POSITION_COLOR);

		for (int i = 0; i <= SEGMENTS; i++) {
			double angle = Math.PI * 2D * i / SEGMENTS;
			float wobble = 1F + 0.14F * (float) Math.sin(angle * 5D + seconds * 3D) + 0.07F * (float) Math.sin(angle * 11D - seconds * 5D);
			float inner = Math.max(0F, hole * wobble);
			float cos = (float) Math.cos(angle), sin = (float) Math.sin(angle);

			strip.addVertex(pose, cx + cos * inner, cy + sin * inner, 0F).setColor(0.16F, 0.03F, 0.24F, 0F);
			strip.addVertex(pose, cx + cos * (inner + edge), cy + sin * (inner + edge), 0F).setColor(0F, 0F, 0F, fade);
		}

		BufferUploader.drawWithShader(strip.buildOrThrow());

		strip = Tesselator.getInstance().begin(VertexFormat.Mode.TRIANGLE_STRIP, DefaultVertexFormat.POSITION_COLOR);

		for (int i = 0; i <= SEGMENTS; i++) {
			double angle = Math.PI * 2D * i / SEGMENTS;
			float wobble = 1F + 0.14F * (float) Math.sin(angle * 5D + seconds * 3D) + 0.07F * (float) Math.sin(angle * 11D - seconds * 5D);
			float inner = Math.max(0F, hole * wobble) + edge;
			float cos = (float) Math.cos(angle), sin = (float) Math.sin(angle);

			strip.addVertex(pose, cx + cos * inner, cy + sin * inner, 0F).setColor(0F, 0F, 0F, fade);
			strip.addVertex(pose, cx + cos * reach * 1.5F, cy + sin * reach * 1.5F, 0F).setColor(0F, 0F, 0F, fade);
		}

		BufferUploader.drawWithShader(strip.buildOrThrow());
	}

	// Arms of shadow reaching in ahead of the edge and curling towards the middle
	private void drawTendrils(Matrix4f pose, float cx, float cy, float hole, float reach, float seconds, float closing, float fade) {
		float tip = Math.max(0F, hole * 0.35F);

		for (int t = 0; t < TENDRILS; t++) {
			double base = Math.PI * 2D * t / TENDRILS + seconds * 0.35D;
			float thickness = 10F + 6F * (float) Math.sin(t * 1.7D);
			BufferBuilder strip = Tesselator.getInstance().begin(VertexFormat.Mode.TRIANGLE_STRIP, DefaultVertexFormat.POSITION_COLOR);

			for (int p = 0; p <= TENDRIL_POINTS; p++) {
				float along = p / (float) TENDRIL_POINTS;
				float radius = Mth.lerp(along, reach, tip);
				double curl = base + along * along * 2.2D + 0.12D * Math.sin(seconds * 2.5D + t + along * 6D);
				float half = thickness * (1F - along * 0.9F) * 0.5F;
				float alpha = (0.95F - along * 0.4F) * fade * (0.4F + 0.6F * closing);

				float x = cx + (float) Math.cos(curl) * radius, y = cy + (float) Math.sin(curl) * radius;
				float nx = -(float) Math.sin(curl) * half, ny = (float) Math.cos(curl) * half;

				strip.addVertex(pose, x - nx, y - ny, 0F).setColor(0.07F, 0.01F, 0.11F, alpha);
				strip.addVertex(pose, x + nx, y + ny, 0F).setColor(0.07F, 0.01F, 0.11F, alpha);
			}

			BufferUploader.drawWithShader(strip.buildOrThrow());
		}
	}

	// Specks of light being dragged down into the middle
	private void drawMotes(Matrix4f pose, float cx, float cy, float reach, float seconds, float fade) {
		BufferBuilder quads = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

		for (int m = 0; m < MOTES; m++) {
			float phase = (m * 0.6180339F + seconds * (0.25F + (m % 7) * 0.03F)) % 1F;
			float radius = reach * (1F - phase);
			double angle = m * 2.399D + phase * phase * 4D + seconds * 0.4D;
			float x = cx + (float) Math.cos(angle) * radius, y = cy + (float) Math.sin(angle) * radius;
			float size = 1F + (m % 3) * 0.6F;
			float alpha = Mth.sin(phase * Mth.PI) * 0.8F * fade;

			quads.addVertex(pose, x - size, y - size, 0F).setColor(0.55F, 0.3F, 0.85F, alpha);
			quads.addVertex(pose, x - size, y + size, 0F).setColor(0.55F, 0.3F, 0.85F, alpha);
			quads.addVertex(pose, x + size, y + size, 0F).setColor(0.55F, 0.3F, 0.85F, alpha);
			quads.addVertex(pose, x + size, y - size, 0F).setColor(0.55F, 0.3F, 0.85F, alpha);
		}

		BufferUploader.drawWithShader(quads.buildOrThrow());
	}

	private static float smooth(float t) {
		return t * t * (3F - 2F * t);
	}
}

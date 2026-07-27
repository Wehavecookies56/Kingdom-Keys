package online.kingdomkeys.kingdomkeys.client.gui.elements;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import online.kingdomkeys.kingdomkeys.client.model.armor.CrownModel;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSSetCrownOffset;
import org.joml.Matrix4f;

public class CrownPositionWidget extends AbstractWidget {
	private static final float RANGE = 8F;

	private boolean dragging;

	private static final int GRID_COLOUR = 0x90505050;
	private static final int HANDLE_COLOUR = 0xC0FFD900;

	private CrownModel crownModel;

	public CrownPositionWidget(int x, int y, int size) {
		super(x, y, size, size, Component.empty());
	}

	private PlayerData data() {
		return PlayerData.get(Minecraft.getInstance().player);
	}

	@Override
	protected void renderWidget(GuiGraphics gui, int mouseX, int mouseY, float partialTick) {
		int cx = getX() + width / 2, cy = getY() + height / 2;
		gui.fill(getX(), getY(), getX() + width, getY() + height, 0xFF101010);

		renderHeadAndCrown(gui, cx, cy);

		gui.renderOutline(getX(), getY(), width, height, isHovered() ? 0xFFFFFFFF : 0xFF808080);

		gui.pose().pushPose();
		{
			gui.pose().translate(0, 0, 300);
			gui.drawString(Minecraft.getInstance().font, "|", cx, getY() + height - 15, 0xFFAAAAAA, false);
			gui.drawString(Minecraft.getInstance().font, "V", cx - 2, getY() + height - 9, 0xFFAAAAAA, false);

			int size = 5;
			gui.fill(cx - size, cy, cx + size + 1, cy + 1, GRID_COLOUR);
			gui.fill(cx, cy - size, cx + 1, cy + size + 1, GRID_COLOUR);

			int dx = cx + Math.round(data().getCrownOffsetX() / RANGE * (width / 2F));
			int dy = cy - Math.round(data().getCrownOffsetZ() / RANGE * (height / 2F));
			gui.fill(dx - size, dy - size, dx + size + 1, dy + size + 1, HANDLE_COLOUR);
		}
		gui.pose().popPose();
	}

	private void renderHeadAndCrown(GuiGraphics gui, int cx, int cy) {
		if (!(Minecraft.getInstance().player instanceof AbstractClientPlayer acp))
			return;
		PoseStack pose = gui.pose();

		pose.pushPose();
		{
			pose.translate(cx, cy, 100);
			float scale = width * 2F;
			pose.scale(scale, -scale, scale);
			pose.mulPose(Axis.XP.rotationDegrees(-90F));

			MultiBufferSource.BufferSource scalpBuffer = Minecraft.getInstance().renderBuffers().bufferSource();
			VertexConsumer scalp = scalpBuffer.getBuffer(RenderType.entityCutoutNoCull(acp.getSkin().texture()));

			drawScalpQuad(scalp, pose, -0.5F, 8F / 64F, 16F / 64F);   // base layer, u 8..16
			drawScalpQuad(scalp, pose, -0.501F, 40F / 64F, 48F / 64F); // hat overlay, nudged to avoid z-fighting
			scalpBuffer.endBatch();

			String variant = data().getCrown();
			if (!variant.isEmpty()) {
				pose.pushPose();
				{
					pose.scale(0.5F, 0.5F, 0.5F);
					pose.translate(data().getCrownOffsetX() / 16F, -1.001F + data().getCrownOffsetY() / 16F, data().getCrownOffsetZ() / 16F);
					crown().root.yRot = net.minecraft.util.Mth.DEG_TO_RAD * data().getCrownRotationY();
					crown().root.xRot = net.minecraft.util.Mth.DEG_TO_RAD * data().getCrownRotationX();
					crown().root.zRot = net.minecraft.util.Mth.DEG_TO_RAD * data().getCrownRotationZ();

					ResourceLocation tex = KingdomKeys.rl("textures/models/crown/" + variant + ".png");
					MultiBufferSource.BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();
					crown().renderToBuffer(pose, buffer.getBuffer(RenderType.entityCutoutNoCull(tex)), 0xF000F0, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
					buffer.endBatch();
				}
				pose.popPose();
			}
		}
		pose.popPose();
	}

	private CrownModel crown() {
		if (crownModel == null) {
			crownModel = new CrownModel(Minecraft.getInstance().getEntityModels().bakeLayer(CrownModel.LAYER_LOCATION));
		}
		return crownModel;
	}

	private void drawScalpQuad(com.mojang.blaze3d.vertex.VertexConsumer buffer, PoseStack pose, float y, float u0, float u1) {
		Matrix4f m = pose.last().pose();
		float h = 0.25F;   // half of the 8-model-unit face, in blocks
		float v0 = 8F / 64F, v1 = 0F;
		vertex(buffer, m, -h, y, -h, u0, v0);
		vertex(buffer, m, -h, y,  h, u0, v1);
		vertex(buffer, m,  h, y,  h, u1, v1);
		vertex(buffer, m,  h, y, -h, u1, v0);
	}

	private static void vertex(VertexConsumer buffer, Matrix4f m, float x, float y, float z, float u, float v) {
		buffer.addVertex(m, x, y, z).setColor(255, 255, 255, 255).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xF000F0).setNormal(0F, -1F, 0F);
	}

	private void moveTo(double mouseX, double mouseY) {
		float x = (float) (mouseX - (getX() + width / 2.0)) / (width / 2F) * RANGE;
		float z = -(float) (mouseY - (getY() + height / 2.0)) / (height / 2F) * RANGE;

		x = Math.round(Mth.clamp(x, -RANGE, RANGE) * 4F) / 4F;
		z = Math.round(Mth.clamp(z, -RANGE, RANGE) * 4F) / 4F;
		data().setCrownOffset(x, data().getCrownOffsetY(), z);
	}

	@Override
	public void onClick(double mouseX, double mouseY) {
		dragging = true;
		moveTo(mouseX, mouseY);
	}

	@Override
	protected void onDrag(double mouseX, double mouseY, double dragX, double dragY) {
		moveTo(mouseX, mouseY);
	}

	@Override
	public void onRelease(double mouseX, double mouseY) {
		if (dragging) {
			dragging = false;
			PacketHandler.sendToServer(new CSSetCrownOffset(data().getCrownOffsetX(), data().getCrownOffsetY(), data().getCrownOffsetZ(), data().getCrownRotationX(), data().getCrownRotationY(), data().getCrownRotationZ()));
		}
	}

	@Override
	protected void updateWidgetNarration(NarrationElementOutput output) {
		output.add(NarratedElementType.TITLE, Component.translatable("kingdomkeys.gui.config.crown_position"));
	}
}

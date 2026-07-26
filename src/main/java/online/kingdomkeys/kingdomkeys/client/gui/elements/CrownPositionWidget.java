package online.kingdomkeys.kingdomkeys.client.gui.elements;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSSetCrownOffset;

public class CrownPositionWidget extends AbstractWidget {
	private static final float RANGE = 8F;

	private PlayerData playerData;
	private boolean dragging;

	public CrownPositionWidget(int x, int y, int size) {
		super(x, y, size, size, Component.empty());
		this.playerData = PlayerData.get(Minecraft.getInstance().player);
	}

	@Override
	protected void renderWidget(GuiGraphics gui, int mouseX, int mouseY, float partialTick) {
		this.playerData = PlayerData.get(Minecraft.getInstance().player);
		int size = 5;
		// Head box
		gui.fill(getX(), getY(), getX() + width, getY() + height, 0xFF202020);
		gui.renderOutline(getX(), getY(), width, height, isHovered() ? 0xFFFFFFFF : 0xFF808080);
		// Centre crosshair, so "default position" is visible at a glance
		int cx = getX() + width / 2, cy = getY() + height / 2;


		gui.drawString(Minecraft.getInstance().font, "|", cx, getY() + height - 15, 0xFF707070, false);
		gui.drawString(Minecraft.getInstance().font, "V", cx - 2, getY() + height - 9, 0xFF707070, false);
		int dx = cx + Math.round(playerData.getCrownOffsetX() / RANGE * (width / 2F));
		int dy = cy - Math.round(playerData.getCrownOffsetZ() / RANGE * (height / 2F));
		gui.fill(dx - size, dy - size, dx + size+1, dy + size+1, 0xFFFFD900);

		gui.fill(cx - size, cy, cx + size+1, cy + 1, 0xFF505050);
		gui.fill(cx, cy - size, cx + 1, cy + size+1, 0xFF505050);
	}

	private void moveTo(double mouseX, double mouseY) {
		float x = (float) (mouseX - (getX() + width / 2.0)) / (width / 2F) * RANGE;
		float z = -(float) (mouseY - (getY() + height / 2.0)) / (height / 2F) * RANGE;

		x = Math.round(Mth.clamp(x, -RANGE, RANGE) * 4F) / 4F;
		z = Math.round(Mth.clamp(z, -RANGE, RANGE) * 4F) / 4F;
		playerData.setCrownOffset(x, z);
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
			PacketHandler.sendToServer(new CSSetCrownOffset(playerData.getCrownOffsetX(), playerData.getCrownOffsetZ(), playerData.getCrownRotationX(), playerData.getCrownRotationY(), playerData.getCrownRotationZ()));
		}
	}

	@Override
	protected void updateWidgetNarration(NarrationElementOutput output) {
		output.add(NarratedElementType.TITLE, Component.translatable("kingdomkeys.gui.config.crown_position"));
	}
}

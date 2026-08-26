package online.kingdomkeys.kingdomkeys.client.gui.elements;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class CrownHeightSlider extends AbstractWidget {
	private static final float RANGE = 8F;

	private final Supplier<Float> getter;
	private final Consumer<Float> setter;
	private final Runnable onCommit;
	private boolean dragging;

	public CrownHeightSlider(int x, int y, int width, int height, Supplier<Float> getter, Consumer<Float> setter, Runnable onCommit) {
		super(x, y, width, height, Component.empty());
		this.getter = getter;
		this.setter = setter;
		this.onCommit = onCommit;
	}

	@Override
	protected void renderWidget(GuiGraphics gui, int mouseX, int mouseY, float partialTick) {
		gui.fill(getX(), getY(), getX() + width, getY() + height, 0xFF202020);
		gui.renderOutline(getX(), getY(), width, height, isHovered() ? 0xFFFFFFFF : 0xFF808080);

		int mid = getY() + height / 2;
		gui.fill(getX() + 1, mid, getX() + width - 1, mid + 1, 0xFF505050);

		float v = Mth.clamp(getter.get(), -RANGE, RANGE);
		int hy = mid + Math.round(v / RANGE * (height / 2F - 2));
		gui.fill(getX() + 1, hy - 1, getX() + width - 1, hy + 2, 0xFFFFD900);

		String label = String.format("%.2f", v);
		gui.drawString(Minecraft.getInstance().font, label, getX() + width / 2 - Minecraft.getInstance().font.width(label) / 2, getY() - 10, 0xFFAAAAAA, false);
	}

	private void moveTo(double mouseY) {
		int mid = getY() + height / 2;
		float v = (float) (mouseY - mid) / (height / 2F - 2) * RANGE;
		v = Math.round(Mth.clamp(v, -RANGE, RANGE) * 4F) / 4F;   // quarter-unit steps
		setter.accept(v);
	}

	@Override
	public void onClick(double mouseX, double mouseY) {
		dragging = true;
		moveTo(mouseY);
	}

	@Override
	protected void onDrag(double mouseX, double mouseY, double dragX, double dragY) {
		moveTo(mouseY);
	}

	@Override
	public void onRelease(double mouseX, double mouseY) {
		if (dragging) {
			dragging = false;
			onCommit.run();
		}
	}

	@Override
	protected void updateWidgetNarration(NarrationElementOutput output) {
		output.add(NarratedElementType.TITLE, Component.translatable("kingdomkeys.gui.config.crown_height"));
	}
}

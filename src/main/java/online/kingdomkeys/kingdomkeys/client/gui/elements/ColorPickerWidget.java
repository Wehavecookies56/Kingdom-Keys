package online.kingdomkeys.kingdomkeys.client.gui.elements;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

import java.awt.Color;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ColorPickerWidget extends AbstractWidget {

	private static final int HUE_STRIP_WIDTH = 10;
	private static final int GAP = 3;
	private static final int HUE_BANDS = 48;

	private final Supplier<Integer> getter;
	private final Consumer<Integer> setter;
	private final Runnable onCommit;

	private float hue, saturation, brightness;
	private boolean draggingSquare, draggingHue;

	public ColorPickerWidget(int x, int y, int width, int height, Supplier<Integer> getter, Consumer<Integer> setter, Runnable onCommit) {
		super(x, y, width, height, Component.empty());
		this.getter = getter;
		this.setter = setter;
		this.onCommit = onCommit;
		readFromSource();
	}

	private void readFromSource() {
		Color c = new Color(getter.get() & 0xFFFFFF);
		float[] hsb = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
		this.hue = hsb[0];
		this.saturation = hsb[1];
		this.brightness = hsb[2];
	}

	private void push() {
		setter.accept(Color.HSBtoRGB(hue, saturation, brightness) & 0xFFFFFF);
	}

	private int squareWidth() {
		return width - HUE_STRIP_WIDTH - GAP;
	}

	@Override
	protected void renderWidget(GuiGraphics gui, int mouseX, int mouseY, float partialTick) {
		if ((Color.HSBtoRGB(hue, saturation, brightness) & 0xFFFFFF) != (getter.get() & 0xFFFFFF)) {
			readFromSource();
		}

		int sqW = squareWidth();
		int sqX = getX(), sqY = getY();

		for (int i = 0; i < sqW; i++) {
			float sat = (float) i / (sqW - 1);
			int top = Color.HSBtoRGB(hue, sat, 1.0F) | 0xFF000000;
			gui.fillGradient(sqX + i, sqY, sqX + i + 1, sqY + height, top, 0xFF000000);
		}
		gui.renderOutline(sqX - 1, sqY - 1, sqW + 2, height + 2, 0xFF000000);

		// Hue strip
		int hueX = sqX + sqW + GAP;
		for (int i = 0; i < HUE_BANDS; i++) {
			int y0 = sqY + height * i / HUE_BANDS;
			int y1 = sqY + height * (i + 1) / HUE_BANDS;
			int a = Color.HSBtoRGB((float) i / HUE_BANDS, 1F, 1F) | 0xFF000000;
			int b = Color.HSBtoRGB((float) (i + 1) / HUE_BANDS, 1F, 1F) | 0xFF000000;
			gui.fillGradient(hueX, y0, hueX + HUE_STRIP_WIDTH, y1 + 1, a, b);
		}
		gui.renderOutline(hueX - 1, sqY - 1, HUE_STRIP_WIDTH + 2, height + 2, 0xFF000000);

		// Handles
		int hx = sqX + Math.round(saturation * (sqW - 1));
		int hy = sqY + Math.round((1F - brightness) * (height - 1));
		gui.renderOutline(hx - 2, hy - 2, 5, 5, 0xFFFFFFFF);
		gui.renderOutline(hx - 3, hy - 3, 7, 7, 0xFF000000);

		int hueY = sqY + Math.round(hue * (height - 1));
		gui.fill(hueX - 2, hueY, hueX + HUE_STRIP_WIDTH + 2, hueY + 1, 0xFFFFFFFF);

		// Preview swatch under the square
		gui.fill(sqX, sqY + height + 3, sqX + sqW, sqY + height + 12, 0xFF000000 | (getter.get() & 0xFFFFFF));
		gui.renderOutline(sqX - 1, sqY + height + 2, sqW + 2, 11, 0xFF000000);
	}

	private void handle(double mouseX, double mouseY) {
		int sqW = squareWidth();
		if (draggingSquare) {
			saturation = Mth.clamp((float) (mouseX - getX()) / (sqW - 1), 0F, 1F);
			brightness = 1F - Mth.clamp((float) (mouseY - getY()) / (height - 1), 0F, 1F);
			push();
		} else if (draggingHue) {
			hue = Mth.clamp((float) (mouseY - getY()) / (height - 1), 0F, 1F);
			push();
		}
	}

	@Override
	public void onClick(double mouseX, double mouseY) {
		int sqW = squareWidth();
		draggingSquare = mouseX < getX() + sqW;
		draggingHue = !draggingSquare;
		handle(mouseX, mouseY);
	}

	@Override
	protected void onDrag(double mouseX, double mouseY, double dragX, double dragY) {
		handle(mouseX, mouseY);
	}

	@Override
	public void onRelease(double mouseX, double mouseY) {
		if (draggingSquare || draggingHue) {
			draggingSquare = draggingHue = false;
			onCommit.run();
		}
	}

	@Override
	protected void updateWidgetNarration(NarrationElementOutput output) {
		output.add(NarratedElementType.TITLE, Component.translatable("kingdomkeys.gui.config.color_picker"));
	}
}

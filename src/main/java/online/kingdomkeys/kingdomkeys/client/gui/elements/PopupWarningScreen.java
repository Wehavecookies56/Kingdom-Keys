package online.kingdomkeys.kingdomkeys.client.gui.elements;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class PopupWarningScreen extends Screen {
	private static final int BOX_WIDTH = 200;
	private static final int BUTTON_WIDTH = 60;
	private static final int PADDING = 14;

	// Same tone the vanilla screens use
	private static final int DIM = 0xA0101010;

	// Clear of the 100-150 range GUI item rendering uses
	private static final int OVERLAY_Z = 300;

	private static final int BUTTON_PADDING = 22;
	private static final int BUTTON_GAP = 4;

	private final Screen parent;
	private final Component body;
	private final Color colour;

	// Null means this is just a warning
	private final Runnable onConfirm;

	private String confirmLabel = Strings.Gui_Menu_Accept;
	private String cancelLabel = Strings.Gui_Menu_Cancel;

	private MenuBox box;

	/** Just a warning */
	public PopupWarningScreen(Screen parent, Component title, Component body, Color colour) {
		this(parent, title, body, colour, null);
	}

	/** Confirm and cancel */
	public PopupWarningScreen(Screen parent, Component title, Component body, Color colour, Runnable onConfirm) {
		super(title);
		this.parent = parent;
		this.body = body;
		this.colour = colour;
		this.onConfirm = onConfirm;
	}

	public PopupWarningScreen labels(String confirmLabel, String cancelLabel) {
		this.confirmLabel = confirmLabel;
		this.cancelLabel = cancelLabel;
		return this;
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	@Override
	protected void init() {
		super.init();

		int textWidth = BOX_WIDTH - PADDING * 2;
		int lines = font.split(body, textWidth).size();
		int boxHeight = PADDING * 2 + font.lineHeight * 2 + font.lineHeight * lines + 26;

		int boxX = (width - BOX_WIDTH) / 2;
		int boxY = (height - boxHeight) / 2;

		box = new MenuBox(boxX, boxY, BOX_WIDTH, boxHeight, 0.9F, colour);

		int rendered = BUTTON_WIDTH + BUTTON_PADDING;
		int buttonY = boxY + boxHeight - PADDING - 12;
		int confirmX = boxX + BOX_WIDTH - rendered - 5;

		if (onConfirm != null) {
			MenuButton cancel = new MenuButton(confirmX - rendered - BUTTON_GAP, buttonY, BUTTON_WIDTH, cancelLabel, MenuButton.ButtonType.ROUNDBUTTON, b -> onClose());
			cancel.setCenterText(true);
			addRenderableWidget(cancel);
		}

		// Runs the action first: onClose puts the parent back, and the action may well want to leave a different screen up instead.
		MenuButton confirm = new MenuButton(confirmX, buttonY, BUTTON_WIDTH, confirmLabel, MenuButton.ButtonType.ROUNDBUTTON, b -> {
			if (onConfirm != null) {
				onConfirm.run();
			}

			onClose();
		});
		confirm.setCenterText(true);
		addRenderableWidget(confirm);
	}

	@Override
	public void render(@NotNull GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
		if (parent != null) {
			// -1,-1: the cursor belongs to this box now, nothing behind should light up under it
			parent.render(gui, -1, -1, partialTicks);
		}

		gui.flush();
		RenderSystem.enableDepthTest();
		RenderSystem.clear(GL11.GL_DEPTH_BUFFER_BIT, Minecraft.ON_OSX);

		gui.pose().pushPose();
		{
			gui.pose().translate(0, 0, OVERLAY_Z);

			gui.fill(0, 0, width, height, DIM);
			box.renderWidget(gui, mouseX, mouseY, partialTicks);

			int textX = box.getX() + PADDING;
			int textY = box.getY() + PADDING;

			gui.drawCenteredString(font, title, width / 2, textY, 0xFFFF2222);

			List<FormattedCharSequence> split = font.split(body, BOX_WIDTH - PADDING * 2);
			for (int i = 0; i < split.size(); i++) {
				gui.drawString(font, split.get(i), textX, textY + font.lineHeight * 2 + font.lineHeight * i, 0xFFFFFF);
			}

			for (Renderable renderable : renderables) {
				renderable.render(gui, mouseX, mouseY, partialTicks);
			}
		}
		gui.pose().popPose();
	}

	@Override
	public void resize(@NotNull Minecraft minecraft, int width, int height) {
		if (parent != null) {
			parent.resize(minecraft, width, height);
		}

		super.resize(minecraft, width, height);
	}

	@Override
	public void onClose() {
		minecraft.setScreen(parent);
	}
}

package online.kingdomkeys.kingdomkeys.client.gui.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.lib.Strings;

import java.util.ArrayDeque;
import java.util.Deque;

public class InformationGui extends OverlayBase {
	public static final InformationGui INSTANCE = new InformationGui();
	private static final ResourceLocation TEXTURE = KingdomKeys.rl("textures/gui/levelup.png");

	private static final int TEX_W = 256, TEX_H = 256;

	private static final int CAP_L_U = 0, CAP_L_W = 11;
	private static final int TAB_U = 11, TAB_W = 58;
	private static final int SLANT_U = 69, SLANT_W = 13;
	private static final int BAND_U = 82, BAND_W = 163;
	private static final int CAP_R_U = 245, CAP_R_W = 11;

	private static final int SRC_H = 51;

	private static final int HEIGHT = 32;

	private static final int MARGIN_TOP = 4;

	private static final int TAB_TEXT_Y = 4;
	private static final int BAND_TOP = 15, BAND_BOTTOM = 30;
	private static final int TEXT_X = 5;

	private static final float BAND_WIDTH = 0.33F;

	private static final float TYPING_SPEED = 1F;
	private static final int HOLD = 50;
	private static final float SCROLL = 0.0035F;

	private static final float FRAME = 0.5F;

	private static final int TITLE_COLOR = 0xFFFFE24B;
	private static final int TEXT_COLOR = 0xFFFFFFFF;

	private final Deque<Component> queue = new ArrayDeque<>();

	private String current;
	private int ticks;
	private int typing;
	private int life;

	private InformationGui() {
		super();
	}

	/**
	 * Queued rather than replaced, so two things worth saying both get said.
	 */
	public static void show(Component message) {
		if (message != null) {
			INSTANCE.queue.add(message);
		}
	}

	public static void show(String translationKey) {
		show(Component.translatable(translationKey));
	}

	public static void clear() {
		INSTANCE.queue.clear();
		INSTANCE.current = null;
		INSTANCE.ticks = 0;
	}

	private static int scaled(int source) {
		return Math.max(1, Math.round(source * (HEIGHT / (float) SRC_H)));
	}

	private static float speed(int guiWidth) {
		return Math.max(1F, guiWidth * SCROLL);
	}

	@SubscribeEvent
	public void clientTick(ClientTickEvent.Post event) {
		Minecraft mc = Minecraft.getInstance();

		if (mc.isPaused()) {
			return;
		}

		if (current == null) {
			if (queue.isEmpty()) {
				return;
			}

			current = ClientUtils.fillTokens(queue.poll().getString());
			ticks = 0;
			typing = (int) Math.ceil(current.length() / TYPING_SPEED);
			life = typing + HOLD + Math.round((TEXT_X + mc.font.width(current)) / speed(mc.getWindow().getGuiScaledWidth())) + 1;

			mc.getSoundManager().play(SimpleSoundInstance.forUI(ModSounds.information.get(), 1.0F, 1.0F));
			return;
		}

		if (++ticks >= life) {
			current = null;
			ticks = 0;
		}
	}

	@Override
	public void render(GuiGraphics gui, DeltaTracker deltaTracker) {
		super.render(gui, deltaTracker);

		if (current == null || minecraft == null || minecraft.options.hideGui) {
			return;
		}

		Component title = Component.translatable(Strings.Information_Title).withStyle(ClientUtils.KK_Font_EXP);

		int capL = scaled(CAP_L_W);
		int slant = scaled(SLANT_W);
		int capR = scaled(CAP_R_W);

		int tab = font.width(title) + 1;
		int band = Math.max(1, Math.round(gui.guiWidth() * BAND_WIDTH) - capL - tab - slant - capR);

		int y = MARGIN_TOP;
		int x = 0;

		RenderSystem.enableBlend();
		RenderSystem.setShaderColor(FRAME, FRAME, FRAME, 1F);

		x = column(gui, x, y, capL, CAP_L_U, CAP_L_W);
		x = column(gui, x, y, tab, TAB_U, TAB_W);
		x = column(gui, x, y, slant, SLANT_U, SLANT_W);
		x = column(gui, x, y, band, BAND_U, BAND_W);

		int end = column(gui, x, y, capR, CAP_R_U, CAP_R_W);

		RenderSystem.setShaderColor(1F, 1F, 1F, 1F);

		gui.drawString(font, title, TEXT_X, y + TAB_TEXT_Y, TITLE_COLOR, false);

		// Typed out first, then held, then crawled. No partial tick: a letter is there or it is not
		String message = current.substring(0, Math.min(current.length(), Math.round((ticks + 1) * TYPING_SPEED)));

		float age = ticks + deltaTracker.getGameTimeDeltaPartialTick(false);
		float crawl = Math.max(0F, age - typing - HOLD) * speed(gui.guiWidth());

		// Clipped to the hole it lives in, so a long line arrives from off the end of the band
		gui.enableScissor(2, y + BAND_TOP, end - 3, y + BAND_BOTTOM);
		gui.drawString(font, message, TEXT_X - Math.round(crawl), 2 + y + (BAND_TOP + BAND_BOTTOM - font.lineHeight) / 2, TEXT_COLOR, false);
		gui.disableScissor();
	}

	private int column(GuiGraphics gui, int x, int y, int width, int u, int uWidth) {
		blit(gui, TEXTURE, x, y, width, HEIGHT, u, 0, uWidth, SRC_H, TEX_W, TEX_H);
		return x + width;
	}
}

package online.kingdomkeys.kingdomkeys.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.client.render.CameraFrame;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.lib.Constants;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSDialogueAnswer;

import java.util.ArrayList;
import java.util.List;

public class DialogueScreen extends Screen {

	private static final ResourceLocation TEXTURE = KingdomKeys.rl("textures/gui/dialogue.png");

	private static final int GLOVE_ROOM = ClientUtils.GLOVE_W + (int) ClientUtils.SWAY_X + 2;
	private static final int TEX_W = 16, TEX_H = 32;

	private static final int CORNER = 8;

	private static final int TOP_V = 0, BOTTOM_V = 8;
	private static final int SIDE_U = 0, SIDE_V = 16;
	private static final int MIDDLE_U = 0, MIDDLE_V = 17;
	private static final int EDGE_U = 8;

	/** Hangs off the bottom, pointing down and to the left. */
	private static final int TAIL_U = 8, TAIL_V = 16, TAIL_W = 8, TAIL_H = 11;

	/** How far the tail is pushed up into the bubble so the join does not show. */
	private static final int TAIL_OVERLAP = 2;

	private static final int PADDING = 7;
	private static final int LINE_GAP = 2;
	private static final int ANSWER_GAP = 12;

	/** Widest a bubble gets before the text wraps, as a share of the screen. */
	private static final float MAX_WIDTH = 0.4F;

	private static final int TEXT = 0xFF3A2A1E;
	private static final int TEXT_DIM = 0xFF8A7866;

	/** Characters a tick, so a line reads at about the speed you would say it. */
	private static final float TYPING_SPEED = 1.4F;

	private final int speakerId;
	private final List<String> lines;
	private final List<String> answers;

	private int line;
	private int typed;
	private int selected;

	private final List<Bubble> answerBubbles = new ArrayList<>();

	private record Bubble(int x, int y, int width, int height) {
		boolean isHovering(double mouseX, double mouseY) {
			return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
		}
	}

	public DialogueScreen(int speakerId, List<String> lines, List<String> answers) {
		super(Component.empty());
		this.speakerId = speakerId;
		this.lines = lines;
		this.answers = answers;
	}

	private boolean lineDone() {
		return typed >= text(line).length();
	}

	private boolean onLastLine() {
		return line >= lines.size() - 1;
	}

	private String text(int index) {
		return index < 0 || index >= lines.size() ? "" : ClientUtils.fillTokens(Component.translatable(lines.get(index)).getString(), speaker());
	}

	private Entity speaker() {
		return minecraft == null || minecraft.level == null ? null : minecraft.level.getEntity(speakerId);
	}

	@Override
	public void tick() {
		if (!lineDone()) {
			typed = Math.min(text(line).length(), typed + Math.max(1, Math.round(TYPING_SPEED)));
		}
	}

	@Override
	public void render(GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
		Minecraft mc = Minecraft.getInstance();

		Entity speaker = mc.level == null ? null : mc.level.getEntity(speakerId);

		if (speaker == null) {
			onClose();
			return;
		}

		int maxWidth = (int) (width * MAX_WIDTH);

		// Just above the head, so the bubble never covers the face
		Vec3 head = speaker.position().add(0D, speaker.getBbHeight() + 0.4D, 0D);
		Vec3 screen = CameraFrame.toScreen(head, width, height);

		if (screen == null) {
			return;
		}

		String said = text(line);
		String shown = said.substring(0, Math.min(said.length(), typed));
		List<String> wrapped = wrap(shown, maxWidth);

		int bubbleWidth = Math.max(widest(wrap(said, maxWidth)), 24) + PADDING * 2;
		int bubbleHeight = wrap(said, maxWidth).size() * (font.lineHeight + LINE_GAP) - LINE_GAP + PADDING * 2;

		int bubbleX = Mth.clamp((int) screen.x - bubbleWidth / 2, 4, width - bubbleWidth - 4);
		int bubbleY = Mth.clamp((int) screen.y - bubbleHeight - TAIL_H, 4, height - bubbleHeight - 4);

		boolean behind = screen.z < 0D;

		bubble(gui, bubbleX, bubbleY, bubbleWidth, bubbleHeight);

		if (!behind) {
			tail(gui, (int) screen.x, bubbleY + bubbleHeight, bubbleX + bubbleWidth / 2);
		}

		int textY = bubbleY + PADDING;
		for (String row : wrapped) {
			gui.drawString(font, row, bubbleX + PADDING, textY, TEXT, false);
			textY += font.lineHeight + LINE_GAP;
		}

		answerBubbles.clear();

		if (onLastLine() && lineDone() && !answers.isEmpty()) {
			drawAnswers(gui, mouseX, mouseY);
		}
	}

	private void drawAnswers(GuiGraphics gui, int mouseX, int mouseY) {
		Entity speaker = speaker();
		List<String> texts = answers.stream().map(key -> ClientUtils.fillTokens(Component.translatable(key).getString(), speaker)).toList();

		int rowHeight = font.lineHeight + LINE_GAP * 2;
		int bubbleWidth = widest(texts) + GLOVE_ROOM + PADDING * 2;
		int bubbleHeight = texts.size() * rowHeight + PADDING * 2;

		int x = width / 2 - bubbleWidth / 2;
		int y = height - bubbleHeight - ANSWER_GAP - 20;

		bubble(gui, x, y, bubbleWidth, bubbleHeight);

		for (int i = 0; i < texts.size(); i++) {
			answerBubbles.add(new Bubble(x, y + PADDING + i * rowHeight, bubbleWidth, rowHeight));
		}

		for (int i = 0; i < answerBubbles.size(); i++) {
			if (answerBubbles.get(i).isHovering(mouseX, mouseY)) {
				selected = i;
				break;
			}
		}

		for (int i = 0; i < texts.size(); i++) {
			int rowY = answerBubbles.get(i).y();

			// Drifting side to side in step with the one in the menus
			if (i == selected) {
				gui.pose().pushPose();
				{
					gui.pose().translate(x + PADDING + ClientUtils.gloveSway(), rowY + (rowHeight - ClientUtils.GLOVE_H) / 2F, 0);
					gui.blit(Constants.MENU_TEXTURE, 0, 0, ClientUtils.GLOVE_U, ClientUtils.GLOVE_V, ClientUtils.GLOVE_W, ClientUtils.GLOVE_H);
				}
				gui.pose().popPose();
			}

			gui.drawString(font, texts.get(i), x + PADDING + GLOVE_ROOM, rowY + LINE_GAP, i == selected ? TEXT : TEXT_DIM, false);
		}
	}

	private void bubble(GuiGraphics gui, int x, int y, int w, int h) {
		int midW = Math.max(0, w - CORNER * 2);
		int midH = Math.max(0, h - CORNER * 2);

		int right = x + w - CORNER;
		int bottom = y + h - CORNER;

		// The two corners the texture has, and the same two read backwards for the other side
		gui.blit(TEXTURE, x, y, CORNER, CORNER, 0, TOP_V, CORNER, CORNER, TEX_W, TEX_H);
		gui.blit(TEXTURE, right, y, CORNER, CORNER, CORNER, TOP_V, -CORNER, CORNER, TEX_W, TEX_H);
		gui.blit(TEXTURE, x, bottom, CORNER, CORNER, 0, BOTTOM_V, CORNER, CORNER, TEX_W, TEX_H);
		gui.blit(TEXTURE, right, bottom, CORNER, CORNER, CORNER, BOTTOM_V, -CORNER, CORNER, TEX_W, TEX_H);

		// Top and bottom stretched from one column, the sides from one row
		gui.blit(TEXTURE, x + CORNER, y, midW, CORNER, EDGE_U, TOP_V, 1, CORNER, TEX_W, TEX_H);
		gui.blit(TEXTURE, x + CORNER, bottom, midW, CORNER, EDGE_U, BOTTOM_V, 1, CORNER, TEX_W, TEX_H);
		gui.blit(TEXTURE, x, y + CORNER, CORNER, midH, SIDE_U, SIDE_V, CORNER, 1, TEX_W, TEX_H);
		gui.blit(TEXTURE, right, y + CORNER, CORNER, midH, SIDE_U + CORNER, SIDE_V, -CORNER, 1, TEX_W, TEX_H);

		// Middle, from the one pixel of it that is kept
		gui.blit(TEXTURE, x + CORNER, y + CORNER, midW, midH, MIDDLE_U, MIDDLE_V, 1, 1, TEX_W, TEX_H);
	}

	/** Hangs off the bottom towards the speaker, mirrored when they are off to the right. */
	private void tail(GuiGraphics gui, int towards, int bottom, int middle) {
		boolean right = towards > middle;

		// Kept under the bubble it belongs to, however far off the speaker has wandered
		int x = Mth.clamp(towards - TAIL_W / 2, middle - 40, middle + 40);
		int y = bottom - TAIL_OVERLAP;

		// A negative width in texture coordinates is what mirrors it; the quad does not move
		if (right) {
			gui.blit(TEXTURE, x, y, TAIL_W, TAIL_H, TAIL_U + TAIL_W, TAIL_V, -TAIL_W, TAIL_H, TEX_W, TEX_H);
		} else {
			gui.blit(TEXTURE, x, y, TAIL_W, TAIL_H, TAIL_U, TAIL_V, TAIL_W, TAIL_H, TEX_W, TEX_H);
		}
	}

	private List<String> wrap(String text, int maxWidth) {
		List<String> rows = new ArrayList<>();
		font.getSplitter().splitLines(text, maxWidth - PADDING * 2, net.minecraft.network.chat.Style.EMPTY)
				.forEach(formatted -> rows.add(formatted.getString()));

		if (rows.isEmpty()) {
			rows.add("");
		}

		return rows;
	}

	private int widest(List<String> rows) {
		return rows.stream().mapToInt(font::width).max().orElse(0);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (button != 0) {
			return super.mouseClicked(mouseX, mouseY, button);
		}

		// Clicking through the writing finishes the line rather than skipping it
		if (!lineDone()) {
			typed = text(line).length();
			return true;
		}

		if (!onLastLine()) {
			line++;
			typed = 0;
			return true;
		}

		if (answers.isEmpty()) {
			close();
			return true;
		}

		for (int i = 0; i < answerBubbles.size(); i++) {
			if (answerBubbles.get(i).isHovering(mouseX, mouseY)) {
				answer(i);
				return true;
			}
		}

		return true;
	}

	@Override
	public boolean keyPressed(int key, int scanCode, int modifiers) {
		boolean choosing = onLastLine() && lineDone() && !answers.isEmpty();

		if (choosing && (key == 265 || key == 264)) { // up, down
			selected = Math.floorMod(selected + (key == 265 ? -1 : 1), answers.size());
			playSound(ModSounds.menu_move.get());
			return true;
		}

		if (key == 32) {
			if (!choosing) {
				mouseClicked(0, 0, 0);
			}
			return true;
		}

		if (key == 257) { // enter
			if (choosing) {
				answer(selected);
			} else {
				mouseClicked(0, 0, 0);
			}
			return true;
		}

		return super.keyPressed(key, scanCode, modifiers);
	}

	private void answer(int index) {
		playSound(ModSounds.menu_select.get());
		PacketHandler.sendToServer(new CSDialogueAnswer(index));

		// The server decides what comes next: either another of these arrives or nothing does
		Minecraft.getInstance().setScreen(null);
	}

	private void close() {
		playSound(ModSounds.menu_back.get());
		onClose();
	}

	/**
	 * Walking away has to be said out loud, since the speaker is held still until the server hears
	 * that this is over. Out of range of any answer, which the server reads as being dropped.
	 */
	@Override
	public void onClose() {
		PacketHandler.sendToServer(new CSDialogueAnswer(-1));
		super.onClose();
	}

	private void playSound(SoundEvent sound) {
		Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(sound, 1.0F, 1.0F));
	}

	@Override
	public void renderBackground(GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}
}

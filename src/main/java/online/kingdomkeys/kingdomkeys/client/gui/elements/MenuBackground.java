package online.kingdomkeys.kingdomkeys.client.gui.elements;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButtonBase;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.handler.InputHandler;
import online.kingdomkeys.kingdomkeys.lib.Constants;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.lib.Struggle;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.kingdomkeys.kingdomkeys.util.Utils.OrgMember;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.CastleOblivionHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class MenuBackground extends Screen {
	public Player player;
	public PlayerData playerData;

	int selected;

	String tip = null;
	protected Color color;
	protected Component title;

	protected Component dimension;
	protected Component biome;

	public boolean shouldCloseOnMenu;

	public MenuBackground(String name, Color rgb) {
		super(Component.translatable(name));
		minecraft = Minecraft.getInstance();
		selected = -1;
		this.color = rgb;
		this.title = super.title;

		this.player = minecraft.player;
		this.playerData = PlayerData.get(this.player);
	}

	protected boolean isCheckScreen = false;

	public void setPlayerData(Player player, PlayerData playerData) {
		this.player = player;
		this.playerData = playerData;
		this.isCheckScreen = true;
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		InputConstants.Key mouseKey = InputConstants.getKey(keyCode, scanCode);
		if (super.keyPressed(keyCode, scanCode, modifiers)) {
			return true;
		} else if (InputHandler.Keybinds.OPENMENU.getKeybind().isActiveAndMatches(mouseKey) && shouldCloseOnMenu) {
			//Close screen if already open and pushed this key. Example copied from keyPressed of ContainerScreen
			Minecraft mc = Minecraft.getInstance();
			mc.level.playSound(mc.player, mc.player.blockPosition(), ModSounds.menu_back.get(), SoundSource.MASTER, 1.0f, 1.0f);
			this.onClose();
			return true;
		}
		return false;
	}

	public boolean drawPlayerInfo;

	public MenuBar bottomLeftBar, bottomRightBar, topLeftBar, topRightBar;
	public static float tooltipPosX;
	public static float tooltipPosY;


	protected float topBarHeight = (float)height * 0.17F;
	protected float bottomBarHeight;
	protected float topLeftBarWidth;
	protected float topRightBarWidth;
	protected float topGap;
	protected float bottomLeftBarWidth;
	protected float bottomRightBarWidth;
	protected float bottomGap;
	protected float middleHeight;

	public boolean drawSeparately = false;

	//GUIs variables
	protected float buttonPosX;
	protected int buttonPosY;
	protected float buttonWidth;

	public ItemStack reward = ItemStack.EMPTY;
	public String rewardTitle = "";
	public boolean showRewardPopup = false;
	public int rewardPopupTicks;


	public void showReward(ItemStack stack, String title) {
		this.reward = stack.copy();
		this.showRewardPopup = true;
		this.rewardPopupTicks = 0;
		this.rewardTitle = title;
	}

	@Override
	public void tick() {
		if (showRewardPopup) {
			rewardPopupTicks++;
		}
		if (!isCheckScreen) {
			playerData = PlayerData.get(player);
		}

		super.tick();
	}


	@Override
	public Component getTitle() {
		return title;
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (showRewardPopup) {
			showRewardPopup = false;
			player.playSound(ModSounds.menu_back.get(), 1.0f, 1.0f);
			return true;
		}
		return super.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		if (showRewardPopup) return false;
		return super.mouseReleased(mouseX, mouseY, button);
	}

	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
		if (showRewardPopup) return false;
		return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
		if (showRewardPopup) return false;
		return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
	}

	//Separate method to render buttons in a different order
	public void drawMenuBackground(GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
		drawBars(gui, mouseX, mouseY, partialTicks);
		RenderSystem.setShaderColor(1, 1, 1, 1);
		drawMunnyTime(gui);
		drawBiomeDim(gui);
		drawTip(gui);

		// TITLE
		gui.pose().pushPose();
		{
			int sw = Minecraft.getInstance().getWindow().getGuiScaledWidth();
			Component titleComponent = Component.literal(Utils.translateToLocal(getTitle().getString().toUpperCase())).withStyle(ClientUtils.KK_Font_EXP);
			int textX = 5;
			int textY = 10;
			int textWidth = minecraft.font.width(titleComponent);

			float scale = 1.5F;
			gui.pose().scale(scale, scale, 1F);
			gui.drawString(minecraft.font, titleComponent, textX, textY, 0xFF9900);

			int scaledWidth = (int) Math.ceil(textWidth * scale);

			int textOffset = textX - topLeftBar.getPosX();
			int borderPadding = 20;

			topLeftBar.width = textOffset + scaledWidth + borderPadding;
			topRightBar.posX = (int) (topLeftBar.posX + topLeftBar.getWidth() + topGap);
			topRightBar.width = sw;
		}
		gui.pose().popPose();
	}

	@Override
	protected void renderBlurredBackground(float pPartialTick) {

	}

	@Override
	public void render(@NotNull GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
		if (showRewardPopup) mouseX = mouseY = 0;

		this.renderBackground(gui, mouseX, mouseY, partialTicks);
		if (!drawSeparately)
			drawMenuBackground(gui, mouseX, mouseY, partialTicks);

		for (Renderable renderable : this.renderables) {
			renderable.render(gui, mouseX, mouseY, partialTicks);
		}

		if (showRewardPopup) {
			renderRewardPopup(gui, mouseX, mouseY);
		}
	}

	private void clearButtons() {
		for(Renderable btn : renderables) {
			if(btn instanceof MenuButtonBase) {
				((MenuButtonBase) btn).setSelected(false);
			}
		}
	}

	protected void renderRewardPopup(GuiGraphics gui, int mouseX, int mouseY) {
		gui.pose().pushPose();
		{
			boolean isRare = rewardTitle.equals(Strings.Gui_Menu_Items_Melding_RareItemAcquired);
			gui.pose().translate(0, 0, 300);
			int popupWidth = 160;
			int popupHeight = 180;

			int x = (width - popupWidth) / 2;
			int y = (height - popupHeight) / 2;

			gui.fill(0, 0, width, height, 0xAA000000);
			gui.fill(x, y, x + popupWidth, y + popupHeight, isRare ? 0xFF9c7406 : 0xFF202040);
			gui.fill(x + 2, y + 2, x + popupWidth - 2, y + popupHeight - 2, isRare ? 0xFFd49c02 :0xFF404080);

			gui.drawCenteredString(minecraft.font, Component.translatable(rewardTitle).withStyle(ClientUtils.KK_Font_EXP), width / 2, y + 10, 0xFFFF55);

			float animTicks = rewardPopupTicks + minecraft.getTimer().getGameTimeDeltaPartialTick(false);

			float duration = 20F;
			float t = Math.min(animTicks / duration, 1F);

			float scale;

			if (t == 0F) {
				scale = 0F;
			} else {
				float c4 = (float) (2 * Math.PI / 3);
				scale = (float) (Math.pow(2, -10 * t) * Math.sin((t * 10 - 0.75F) * c4) + 1);
			}

			scale *= 8F;

			float rotationT = Math.min(animTicks / 6F, 1F);
			float rotation = (1F - rotationT) * 360F;

			int itemCenterX = width / 2;
			int itemCenterY = y + (popupHeight / 2);

			int frameSize = 66;

			gui.fill(itemCenterX - frameSize, itemCenterY - frameSize, itemCenterX + frameSize, itemCenterY + frameSize, 0xCC000000);

			PoseStack pose = gui.pose();

			pose.pushPose();
			{
				pose.translate(itemCenterX, itemCenterY, 200);
				pose.mulPose(Axis.ZP.rotationDegrees(rotation));
				pose.scale(scale, scale, scale);
				gui.renderItem(reward, -8, -8);
			}
			pose.popPose();

			gui.drawCenteredString(minecraft.font, reward.getHoverName(), width / 2, y + popupHeight - 15, 0xFFFFFF);
		}
		gui.pose().popPose();
	}

	public void drawBars(GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
		renderBackground(gui, mouseX, mouseY, partialTicks);

		float r = color.getRed() / 255F, g = color.getGreen() / 255F, b = color.getBlue() / 255F;
		RenderSystem.setShaderColor(r,g,b, 1.0F);
		RenderSystem.enableBlend();
		gui.blit(menubg, 0, 0, width, height, 0, 0, width, height, 4, 4);
		RenderSystem.disableBlend();
		topLeftBar.draw(gui);
		topRightBar.draw(gui);
		bottomLeftBar.draw(gui);
		bottomRightBar.draw(gui);
	}

	public void drawBiomeDim(GuiGraphics gui) {
		if(player == null)
			return;
		if (CastleOblivionHandler.inInterior(player)) {
			setLocationNames(Component.translatable("kingdomkeys.castle_oblivion.name").withStyle(ClientUtils.KK_Font_MENU).withStyle(ChatFormatting.UNDERLINE), Component.literal("???").withStyle(ClientUtils.KK_Font_MENU).withStyle(ChatFormatting.UNDERLINE));
		}
		gui.pose().pushPose();
		{
			Component biomeText;
			Component dimText;
			if (dimension == null && biome == null) {
				String dimension = this.player.level().dimension().location().getPath().toUpperCase().replaceAll("_", " ");
				ResourceLocation biomeLoc = KingdomKeys.rl(printBiome(this.minecraft.level.getBiome(this.player.blockPosition())));

				String biome = "biome." + biomeLoc.getNamespace() + "." + biomeLoc.getPath();
				if (Language.getInstance().has(biome)) {
					biome = Utils.translateToLocal(biome);
				} else {
					biome = biomeLoc.toString();
				}
				dimText = Component.literal(dimension).withStyle(ClientUtils.KK_Font_MENU).withStyle(ChatFormatting.UNDERLINE);
				biomeText = Component.literal(biome).withStyle(ClientUtils.KK_Font_MENU).withStyle(ChatFormatting.UNDERLINE);
			} else {
				biomeText = biome;
				dimText = dimension;
				biome = null;
				dimension = null;
			}
			gui.drawString(minecraft.font, dimText, width - minecraft.font.width(dimText) - 5, 10, 0xF58B33);
			gui.drawString(minecraft.font, biomeText, width - minecraft.font.width(biomeText) - 5, 20, 0xF58B33);
		}
		gui.pose().popPose();
	}

	public void drawMunnyTime(GuiGraphics gui) {
		gui.pose().pushPose();
		{
			float scale = 1F;

			gui.pose().translate(0.0F, 12, 1F);

			int y = (int) (topBarHeight + middleHeight + 1);
			int maxWidth = 0;

			long seconds = minecraft.level.getDayTime() / 20;
			long h = seconds / 3600;
			long m = seconds % 3600 / 60;
			long s = seconds % 60;

			String time = String.format("%02d:%02d:%02d", h, m, s);

			Component[] lines = new Component[]{
					Component.literal(Utils.translateToLocal(Strings.Gui_Menu_Main_Synthesis_Tier) + ": " + Utils.getTierFromInt(playerData.getSynthLevel())).withStyle(ClientUtils.KK_Font_EXP),
					Component.literal(Utils.translateToLocal(Strings.Gui_Menu_Main_Munny) + ": " + Utils.getFormattedNumber(playerData.getMunny())).withStyle(ClientUtils.KK_Font_EXP),
					Component.literal(Utils.translateToLocal(Strings.Gui_Menu_Main_Hearts) + ": " + Utils.getFormattedNumber(playerData.getHearts())).withStyle(ClientUtils.KK_Font_EXP),
					Component.literal(Utils.translateToLocal(Strings.Gui_Menu_Main_Time) + ": " + getWorldHours(minecraft.level) + ":" + getWorldMinutes(minecraft.level)).withStyle(ClientUtils.KK_Font_EXP),
					Component.literal(Utils.translateToLocal(Strings.Gui_Menu_Main_Time_Spent) + ": " + time).withStyle(ClientUtils.KK_Font_EXP)
			};

			int[] colors = new int[]{
					0xFFFF00,
					0xF66627,
					playerData.getAlignment() == OrgMember.NONE ? 0x888888 : 0xFF3333,
					0xFFFFFF,
					0x42ceff
			};

			for (int i = 0; i < lines.length; i++) {
				Component line = lines[i];
				gui.drawString(minecraft.font, line, 5, y, colors[i]);
				maxWidth = Math.max(maxWidth, minecraft.font.width(line));
				y += minecraft.font.lineHeight;
			}

			int scaledWidth = (int) Math.ceil(maxWidth * scale);

			int textOffset = 5 - bottomLeftBar.getPosX();
			int sw = Minecraft.getInstance().getWindow().getGuiScaledWidth();

			bottomLeftBar.setWidth(textOffset + scaledWidth + 15);
			bottomRightBar.posX = (int) (bottomLeftBar.width + bottomGap) - 12;
			bottomRightBar.width = sw;

			tooltipPosX = bottomRightBar.getPosX() + 15;
			tooltipPosY = bottomRightBar.getPosY() + 15;

		}
		gui.pose().popPose();
	}

	public void drawTip (GuiGraphics gui) {
		tip = null;

		for(Renderable btn : renderables) {
			if(btn instanceof MenuButtonBase) {
				if(((MenuButtonBase) btn).isHoveredOrFocused()) {
					selected = -1;
					clearButtons();

					if(btn instanceof MenuButton && ((MenuButton) btn).visible) {
						tip = ((MenuButton) btn).getTip();
					}
				}
			}
		}

		if(tip != null) {
			gui.pose().pushPose();
			{
				ClientUtils.drawSplitString(gui, Utils.translateToLocal(tip), (int) tooltipPosX, (int) tooltipPosY, (int) (width * 0.6F), 0x44BBFF);
			}
			gui.pose().popPose();
		}

	}

	public static final ResourceLocation menubg = KingdomKeys.rl("textures/gui/menu/menu_background.png");

	public static String getWorldMinutes(Level world) {
		int time = (int) Math.abs((world.getGameTime() + 6000) % 24000);
		if ((time % 1000) * 6 / 100 < 10)
			return "0" + (time % 1000) * 6 / 100;
		else
			return Integer.toString((time % 1000) * 6 / 100);
	}

	public static int getWorldHours(Level world) {
		int time = (int) Math.abs((world.getGameTime() + 6000) % 24000);
		return (int) (time / 1000F);
	}

	public void init() {
		topBarHeight = (float) height * 0.17F;
		bottomBarHeight = (float) height * 0.23F;
		topLeftBarWidth = (float) width * 0.175F;
		topRightBarWidth = (float) (width) * 0.82F;
		topGap = (float) width * 0.005F;
		bottomLeftBarWidth = (float) width * 0.304F;
		bottomRightBarWidth = (float) width * 0.6875F;
		bottomGap = (float) width * 0.0085F;
		middleHeight = (float) height * 0.6F;
		topLeftBar = new MenuBar(-10, -10, (int) topLeftBarWidth + 10, (int) topBarHeight + 10, true);
		topRightBar = new MenuBar((int) (topLeftBarWidth + topGap), -10, (int) topRightBarWidth + 10, (int) topBarHeight + 10, true);
		bottomLeftBar = new MenuBar(-10, (int) (topBarHeight + middleHeight), (int) bottomLeftBarWidth + 10, (int) bottomBarHeight + 10, false);
		bottomRightBar = new MenuBar((int) (bottomLeftBarWidth + bottomGap), (int) (topBarHeight + middleHeight), (int) bottomRightBarWidth + 10, (int) bottomBarHeight + 10, false);

		buttonPosX = (float) width * 0.03F;
		buttonPosY = (int)topBarHeight+5;
		buttonWidth = ((float)width * 0.1744F)-22;

		tooltipPosX = bottomRightBar.getPosX() + 15;
		tooltipPosY = bottomRightBar.getPosY() + 15;
	}

	public void drawParty(@Nullable WorldData worldData, GuiGraphics gui) {
		if(worldData == null || worldData.getPartyFromMember(this.player.getUUID()) == null) {
			int count = CastleOblivionHandler.inInterior(getMinecraft().player) ? 3 : 1;
			drawPlayer(gui, count, 0, this.player.getUUID(), this.player.getDisplayName().getString());
		} else {
			Party party =  worldData.getPartyFromMember(this.player.getUUID());
			for(int i=0;i<party.getMembers().size();i++) {
				Party.Member member = party.getMembers().get(i);
				drawPlayer(gui, party.getMembers().size(), i, member.getUUID(), member.getUsername(), member.isPlayer());
			}
		}
	}

	/** Same idea as {@link #drawParty}, but for the active members of a Struggle match anchored at boardPos. */
	public void drawStruggle(@Nullable WorldData worldData, GuiGraphics gui, BlockPos boardPos) {
		Struggle struggle = worldData == null ? null : worldData.getStruggleFromBlockPos(boardPos);
		if (struggle == null || struggle.getParticipants().isEmpty()) {
			drawPlayer(gui, 1, 0, this.player.getUUID(), this.player.getDisplayName().getString());
			return;
		}

		if (struggle.getMode() == Struggle.Mode.TOURNAMENT && !struggle.getBracket().isEmpty()) {
			drawTournamentBracket(gui, struggle);
			return;
		}

		List<Struggle.Participant> participants = struggle.getParticipants();
		if (participants.size() == 2) {
			drawVersus(gui, participants.get(0), participants.get(1));
			return;
		}

		for (int i = 0; i < participants.size(); i++) {
			Struggle.Participant participant = participants.get(i);
			drawPlayer(gui, participants.size(), i, participant.getUUID(), participant.getUsername());
		}
	}

	private int layoutColumns(int count) {
		boolean multiRow = count > 5;
		return multiRow ? (int) Math.ceil(count / 2.0) : count;
	}

	/** Same grid math {@link #drawPlayer} uses to place a model, exposed so other layouts (VS, bracket
	 * "next up" row) can line other elements (like a "VS" label) up with it exactly. */
	private float[] computeLayoutPosition(int count, int order) {
		boolean multiRow = count > 5;
		int columns = layoutColumns(count);
		int row;
		int col;

		if (!multiRow) {
			row = 1;
			col = order;
		} else {
			row = order / columns;
			col = order % columns;
		}

		float layoutLeft = width * 0.2F;
		float layoutWidth = width * 0.8F;
		float playerHeight = height * 0.45F;
		float scale = Math.max(1.0F - (columns * 0.08F), 0.55F);

		float minSpacing = playerHeight * 0.65F * scale;
		float layoutSpacing = layoutWidth / Math.max(columns, 1);

		float spacingX = Math.max(layoutSpacing, minSpacing);
		float spacingY = height * 0.20F;

		float usedWidth = (columns - 1) * spacingX;
		if (multiRow)
			usedWidth += spacingX * 0.5F;

		float startX = layoutLeft + (layoutWidth - usedWidth) / 2F;

		float playerPosX = startX + (col * spacingX);
		if (multiRow && row == 1)
			playerPosX += spacingX * 0.5F;
		float playerPosY = (height * 0.45F) + (row * spacingY);

		return new float[]{playerPosX, playerPosY};
	}

	/** Two combatants side by side with a "VS" between them, so it's obvious who's fighting who. */
	private void drawVersus(GuiGraphics gui, Struggle.Participant left, Struggle.Participant right) {
		drawPlayer(gui, 2, 0, left.getUUID(), left.getUsername());
		drawPlayer(gui, 2, 1, right.getUUID(), right.getUsername());

		float[] leftPos = computeLayoutPosition(2, 0);
		float[] rightPos = computeLayoutPosition(2, 1);
		int centerX = (int) ((leftPos[0] + rightPos[0]) / 2F);
		int centerY = (int) ((leftPos[1] + rightPos[1]) / 2F);

		gui.drawCenteredString(minecraft.font, "VS", centerX, centerY, 0xFFD900);
	}

	private void drawTournamentBracket(GuiGraphics gui, Struggle struggle) {
		List<List<UUID>> bracket = struggle.getBracket();
		if (bracket.isEmpty())
			return;

		int rounds = bracket.size();
		float layoutLeft = width * 0.12F;
		float layoutRight = width * 0.88F;
		float layoutTop = height * 0.22F;
		float layoutBottom = height * 0.82F;
		float roundSpacing = rounds > 1 ? (layoutRight - layoutLeft) / (rounds - 1) : 0;

		// Precompute every slot's Y position up front, since the connecting lines need to know both
		// this round's and the next round's positions at once.
		float[][] slotY = new float[rounds][];
		for (int r = 0; r < rounds; r++) {
			int slotCount = bracket.get(r).size();
			float spacing = (layoutBottom - layoutTop) / slotCount;
			slotY[r] = new float[slotCount];
			for (int i = 0; i < slotCount; i++) {
				slotY[r][i] = layoutTop + spacing * (i + 0.5F);
			}
		}

		int lineColor = 0x66FFFFFF;
		for (int r = 0; r < rounds - 1; r++) {
			float x1 = layoutLeft + roundSpacing * r;
			float xElbow = x1 + roundSpacing * 0.5F;
			float x2 = layoutLeft + roundSpacing * (r + 1);
			List<UUID> round = bracket.get(r);
			for (int i = 0; i < round.size(); i += 2) {
				float y1 = slotY[r][i];
				float y2 = slotY[r][i + 1];
				float yMid = (y1 + y2) / 2F;
				gui.fill((int) x1, (int) y1, (int) xElbow, (int) y1 + 1, lineColor);
				gui.fill((int) x1, (int) y2, (int) xElbow, (int) y2 + 1, lineColor);
				gui.fill((int) xElbow, (int) Math.min(y1, y2), (int) xElbow + 1, (int) Math.max(y1, y2), lineColor);
				gui.fill((int) xElbow, (int) yMid, (int) x2, (int) yMid + 1, lineColor);
			}
		}

		Set<UUID> eliminated = bracketEliminated(bracket);
		for (int r = 0; r < rounds; r++) {
			List<UUID> round = bracket.get(r);
			float x = layoutLeft + roundSpacing * r;
			for (int i = 0; i < round.size(); i++) {
				UUID id = round.get(i);
				if (id == null) continue;
				Struggle.Participant participant = struggle.getParticipant(id);
				String name = participant != null ? participant.getUsername() : "?";
				int color = eliminated.contains(id) ? 0x808080 : 0xFFFFFF;
				gui.drawString(minecraft.font, name, (int) x + 2, (int) slotY[r][i] - minecraft.font.lineHeight / 2, color);
			}
		}

		int[] next = bracketNextMatch(bracket);
		if (next != null) {
			float x = layoutLeft + roundSpacing * next[0] + roundSpacing * 0.5F;
			float y = (slotY[next[0]][next[1]] + slotY[next[0]][next[1] + 1]) / 2F;
			gui.drawCenteredString(minecraft.font, "VS", (int) x, (int) y - minecraft.font.lineHeight / 2, 0xFFD900);
		}
	}

	/** A participant is eliminated if a completed pair they were in decided someone else as the winner. */
	private Set<UUID> bracketEliminated(List<List<UUID>> bracket) {
		Set<UUID> eliminated = new HashSet<>();
		for (int r = 0; r < bracket.size() - 1; r++) {
			List<UUID> round = bracket.get(r);
			List<UUID> nextRound = bracket.get(r + 1);
			for (int i = 0; i < round.size(); i += 2) {
				UUID a = round.get(i);
				UUID b = round.get(i + 1);
				if (a == null || b == null) continue;
				UUID winner = nextRound.get(i / 2);
				if (winner == null) continue;
				if (!winner.equals(a)) eliminated.add(a);
				if (!winner.equals(b)) eliminated.add(b);
			}
		}
		return eliminated;
	}

	/** The first pair with two real players whose winner hasn't been decided yet - mirrors the same
	 * search StruggleHandler does server-side to pick the next match to fight. */
	private int[] bracketNextMatch(List<List<UUID>> bracket) {
		for (int r = 0; r < bracket.size() - 1; r++) {
			List<UUID> round = bracket.get(r);
			List<UUID> nextRound = bracket.get(r + 1);
			for (int i = 0; i < round.size(); i += 2) {
				UUID a = round.get(i);
				UUID b = round.get(i + 1);
				if (a != null && b != null && nextRound.get(i / 2) == null) {
					return new int[]{r, i};
				}
			}
		}
		return null;
	}

	public void drawPlayer(GuiGraphics gui, int count, int order, UUID memberUUID, String memberUsername) {
		drawPlayer(gui, count, order, memberUUID, memberUsername, true);
	}

	/**
	 * @param isPlayer used for MP or DRIVe refills, if it's not a player it shouldn't do anything to them
	 */
	public void drawPlayer(GuiGraphics gui, int count, int order, UUID memberUUID, String memberUsername, boolean isPlayer) {
		PoseStack matrixStack = gui.pose();

		int columns = layoutColumns(count);
		float scale = Math.max(1.0F - (columns * 0.08F), 0.55F);
		float playerHeight = height * 0.45F;
		float[] pos = computeLayoutPosition(count, order);
		float playerPosX = pos[0];
		float playerPosY = pos[1];

		String level = "LV: N/A";
		String hp = "HP: N/A";
		String mp = "MP: N/A";

		LivingEntity member;

		if (isPlayer) {
			Player player = Utils.getPlayerByName(minecraft.level, memberUsername);

			if(player == null) {
				GameProfile profile = new GameProfile(memberUUID, memberUsername);
				player = new RemotePlayer(Minecraft.getInstance().level, profile);
			} else {
				PlayerData playerData = PlayerData.get(player);
				if(playerData != null) {
					level = Utils.translateToLocal(Strings.Gui_Menu_Status_Level)+": "+ playerData.getLevel();
					hp = Utils.translateToLocal(Strings.Gui_Menu_Status_HP)+": " + (int) player.getHealth() + "/" + (int) player.getMaxHealth();
					mp = Utils.translateToLocal(Strings.Gui_Menu_Status_MP)+": " + (int) playerData.getMP() + "/" + (int) playerData.getMaxMP();
				}
			}

			member = player;
		} else {
			member = Utils.getPartyEntity(minecraft.level, memberUUID);

			if(member != null) {
				hp = Utils.translateToLocal(Strings.Gui_Menu_Status_HP)+": " + (int) member.getHealth() + "/" + (int) member.getMaxHealth();
			}
		}

		int infoBoxWidth = (int)(70 * (0.75F + scale * 0.25F));
		int infoBoxPosX = (int)playerPosX - 16 - (infoBoxWidth / 2);
		int infoBoxPosY = (int)playerPosY - 24;

		matrixStack.pushPose();
		{
			matrixStack.translate(playerPosX, playerPosY, 0);
			matrixStack.scale(scale, scale, 1F);
			matrixStack.translate(-playerPosX, -playerPosY, 0);

			RenderSystem.setShaderColor(1F,1F,1F,1F);

			if(member != null) {
				if (isPlayer) {
					ClientUtils.renderEntity(gui.pose(), (int)playerPosX, (int)playerPosY, (int)playerHeight/2, 0,0, member);
				} else {
					// playerHeight is sized for a player, so anything taller or shorter is brought back to that height
					// first. Otherwise a Mega-Shadow fills the screen and a Shadow is a speck
					float fit = playerHeight / 2F * (1.8F / Math.max(member.getBbHeight(), 0.1F)) / Math.max(member.getScale(), 0.01F);
					LivingEntity posing = member;

					ClientUtils.facingCamera(posing, () -> ClientUtils.renderEntity(gui.pose(), (int)playerPosX, (int)playerPosY, (int) fit, 0,0, posing));
				}
			}

			RenderSystem.setShaderColor(1F,1F,1F,0.75F);

			matrixStack.pushPose();
			{
				RenderSystem.setShaderColor(1,1,1,1);
				matrixStack.translate(9,1,100);

				RenderSystem.enableBlend();

				gui.blit(Constants.MENU_TEXTURE, infoBoxPosX, infoBoxPosY, 123,67,11,22);
				gui.blit(Constants.MENU_TEXTURE, infoBoxPosX + 11, infoBoxPosY, infoBoxWidth, 22, 135,67,1,22,256,256);
				gui.blit(Constants.MENU_TEXTURE, infoBoxPosX + 11 + infoBoxWidth, infoBoxPosY,137,67,3,22);

				gui.blit(Constants.MENU_TEXTURE, infoBoxPosX, infoBoxPosY + 22,123,90,3,35);
				gui.blit(Constants.MENU_TEXTURE, infoBoxPosX + 3, infoBoxPosY + 22, infoBoxWidth+8, 35,127,90,1,35,256,256);
				gui.blit(Constants.MENU_TEXTURE, infoBoxPosX + 3 + infoBoxWidth + 8, infoBoxPosY + 22,129,90,3,35);

				RenderSystem.disableBlend();
			}
			matrixStack.popPose();

			// stats
			matrixStack.pushPose();
			{
				matrixStack.translate(10,2,100);

				matrixStack.pushPose();
				{
					matrixStack.translate(infoBoxPosX + 10, infoBoxPosY + ((22 / 2) - minecraft.font.lineHeight / 2), 1);
					gui.drawString(minecraft.font, memberUsername,0,0,0xFFFFFF);
				}
				matrixStack.popPose();

				gui.drawString(minecraft.font, level, infoBoxPosX + 5, infoBoxPosY + 26, 0xFFD900);
				gui.drawString(minecraft.font, hp, infoBoxPosX + 5, infoBoxPosY + 26 + minecraft.font.lineHeight, 0x00FF00);
				gui.drawString(minecraft.font, mp, infoBoxPosX + 5, infoBoxPosY + 26 + minecraft.font.lineHeight * 2, 0x4444FF);
			}
			matrixStack.popPose();
		}
		matrixStack.popPose();
	}

	private static String printBiome(Holder<Biome> biomeHolder) {
		return biomeHolder.unwrap().map((biomeKey) -> biomeKey.location().toString(), (biome) -> "[unregistered " + biome + "]");
	}

	public void setLocationNames(Component dimension, Component biome) {
		if (dimension != null && biome != null) {
			this.dimension = dimension;
			this.biome = biome;
		}
	}
}
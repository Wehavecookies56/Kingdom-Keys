package online.kingdomkeys.kingdomkeys.client.gui.elements;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.core.Holder;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
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
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.kingdomkeys.kingdomkeys.util.Utils.OrgMember;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.CastleOblivionHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.UUID;

public class MenuBackground extends Screen {
	public Player player;
	public PlayerData playerData;
	
	public static final ResourceLocation PLAYER_BOX_TEXTURE = ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/gui/menu/menu_button.png");
	int selected;
	
	String tip = null;
	protected Color color;
	protected Component title;

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

	public void setPlayerData(Player player, PlayerData playerData) {
		this.player = player;
		this.playerData = playerData;
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

	@Override
	public Component getTitle() {
		return title;
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
		this.renderBackground(gui, mouseX, mouseY, partialTicks);
		if (!drawSeparately)
			drawMenuBackground(gui, mouseX, mouseY, partialTicks);

        for (Renderable renderable : this.renderables) {
            renderable.render(gui, mouseX, mouseY, partialTicks);
        }
	}

	private void clearButtons() {
		for(Renderable btn : renderables) {
			if(btn instanceof MenuButtonBase) {
				((MenuButtonBase) btn).setSelected(false);
			}
		}
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
		gui.pose().pushPose();
		{
			String dimension = this.player.level().dimension().location().getPath().toUpperCase().replaceAll("_", " ");
			ResourceLocation biomeLoc = ResourceLocation.parse(printBiome(this.minecraft.level.getBiome(this.player.blockPosition())));

			String biome = "biome." + biomeLoc.getNamespace() + "." + biomeLoc.getPath();
			if (Language.getInstance().has(biome)) {
				biome = Utils.translateToLocal(biome);
			} else {
				biome = biomeLoc.toString();
			}
			Component text = Component.literal(dimension).withStyle(ClientUtils.KK_Font_MENU).withStyle(ChatFormatting.UNDERLINE);
			gui.drawString(minecraft.font, text, width - minecraft.font.width(text) - 5, 10, 0xF58B33);
			text = Component.literal(biome).withStyle(ClientUtils.KK_Font_MENU).withStyle(ChatFormatting.UNDERLINE);
			gui.drawString(minecraft.font, text, width - minecraft.font.width(text) - 5, 20, 0xF58B33);
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

	public static final ResourceLocation menu = ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/gui/menu/menu_button.png");
	public static final ResourceLocation menubg = ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/gui/menu/menu_background.png");

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
			Party.Member m = new Party.Member(this.player.getUUID(), this.player.getDisplayName().getString());
			drawPlayer(gui, null,0, m);
		} else {
			Party party =  worldData.getPartyFromMember(this.player.getUUID());
			for(int i=0;i<party.getMembers().size();i++) {
				Party.Member member = party.getMembers().get(i);
				drawPlayer(gui, party, i, member);
			}
		}
	}

	public void drawPlayer(GuiGraphics gui,@Nullable Party party, int order, Party.Member member) {
		PoseStack matrixStack = gui.pose();
		int count =  party == null ? CastleOblivionHandler.inInterior(getMinecraft().player) ? 3 : 1 : party.getMembers().size(); //Map space

		boolean multiRow = count > 5;

		int columns;
		int row;
		int col;

		if (!multiRow) {
			columns = count;
			row = 1;
			col = order;
		} else {
			columns = (int)Math.ceil(count / 2.0);
			row = order / columns;
			col = order % columns;
		}
		float layoutLeft = width * 0.2F;
		float layoutWidth = width * 0.8F;
		float layoutRight = layoutLeft + layoutWidth;
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

		Player player = Utils.getPlayerByName(minecraft.level, member.getUsername());

		String level = "LV: N/A";
		String hp = "HP: N/A";
		String mp = "MP: N/A";

		if(player == null) {
			UUID uuid = member.getUUID();
			String name = member.getUsername();

			GameProfile profile = new GameProfile(uuid, name);
			player = new RemotePlayer(Minecraft.getInstance().level, profile);
		} else {
			PlayerData playerData = PlayerData.get(player);
			if(playerData != null) {
				level = Utils.translateToLocal(Strings.Gui_Menu_Status_Level)+": "+ playerData.getLevel();
				hp = Utils.translateToLocal(Strings.Gui_Menu_Status_HP)+": " + (int) player.getHealth() + "/" + (int) player.getMaxHealth();
				mp = Utils.translateToLocal(Strings.Gui_Menu_Status_MP)+": " + (int) playerData.getMP() + "/" + (int) playerData.getMaxMP();
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

			if(member != null && player != null) {
				ClientUtils.renderEntity(gui.pose(), (int)playerPosX, (int)playerPosY, (int)playerHeight/2, 0,0, player);
			}

			RenderSystem.setShaderColor(1F,1F,1F,0.75F);

			matrixStack.pushPose();
			{
				RenderSystem.setShaderColor(1,1,1,1);
				matrixStack.translate(9,1,100);

				RenderSystem.enableBlend();

				gui.blit(PLAYER_BOX_TEXTURE, infoBoxPosX, infoBoxPosY, 123,67,11,22);

				for(int i=0;i<infoBoxWidth;i++)
					gui.blit(PLAYER_BOX_TEXTURE, infoBoxPosX + 11 + i, infoBoxPosY, 135,67,2,22);

				gui.blit(PLAYER_BOX_TEXTURE, infoBoxPosX + 11 + infoBoxWidth, infoBoxPosY,137,67,3,22);
				gui.blit(PLAYER_BOX_TEXTURE, infoBoxPosX, infoBoxPosY + 22,123,90,3,35);

				for(int i=0;i<infoBoxWidth+8;i++)
					gui.blit(PLAYER_BOX_TEXTURE, infoBoxPosX + 3 + i, infoBoxPosY + 22,127,90,2,35);

				gui.blit(PLAYER_BOX_TEXTURE, infoBoxPosX + 3 + infoBoxWidth + 8, infoBoxPosY + 22,129,90,3,35);

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
					gui.drawString(minecraft.font, member.getUsername(),0,0,0xFFFFFF);
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

	private static String printBiome(Holder<Biome> p_205375_) {
	      return p_205375_.unwrap().map((p_205377_) -> p_205377_.location().toString(), (p_205367_) -> "[unregistered " + p_205367_ + "]");
	   }
}

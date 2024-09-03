package online.kingdomkeys.kingdomkeys.client.gui.elements;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Holder;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButtonBase;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.handler.InputHandler;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.kingdomkeys.kingdomkeys.util.Utils.OrgMember;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class MenuBackground extends Screen {

	int selected;
	
	String tip = null;
	Color color;
	protected Component title;

	public boolean shouldCloseOnMenu;
	
	public MenuBackground(String name, Color rgb) {
		super(Component.translatable(name));
		minecraft = Minecraft.getInstance();
		selected = -1;
		this.color = rgb;
		this.title = super.title;
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
		drawBars(gui);
		RenderSystem.setShaderColor(1, 1, 1, 1);
		drawMunnyTime(gui);
		drawBiomeDim(gui);
		drawTip(gui);

		gui.pose().pushPose();
		{
			gui.pose().scale(1.3F,1.3F, 1F);
			gui.drawString(minecraft.font, Utils.translateToLocal(getTitle().getString()), 2, 10, 0xFF9900);
		}
		gui.pose().popPose();
	}

	@Override
	public void render(@NotNull GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
		if (!drawSeparately)
			drawMenuBackground(gui, mouseX, mouseY, partialTicks);
		super.render(gui, mouseX, mouseY, partialTicks);
	}

	private void clearButtons() {
		for(Renderable btn : renderables) {
			if(btn instanceof MenuButtonBase) {
				((MenuButtonBase) btn).setSelected(false);
			}
		}
	}

	public void drawBars(GuiGraphics gui) {
		renderBackground(gui);
		int sh = Minecraft.getInstance().getWindow().getGuiScaledHeight();
		int sw = Minecraft.getInstance().getWindow().getGuiScaledWidth();

		float r = color.getRed() / 255F, g = color.getGreen() / 255F, b = color.getBlue() / 255F;
		RenderSystem.setShaderColor(r,g,b, 1.0F);
		// RenderSystem.enableAlpha();
		RenderSystem.enableBlend();
		PoseStack matrixStack = gui.pose();
		for (int i = 0; i < sh; i += 3) {
			matrixStack.pushPose();
			matrixStack.translate(0, i, 0);
			matrixStack.scale(sw, 1, 1);
			gui.blit(menu, 0, 0, 77, 92, 1, 1);
			matrixStack.popPose();
		}
		RenderSystem.disableBlend();
		topLeftBar.draw(gui);
		topRightBar.draw(gui);
		bottomLeftBar.draw(gui);
		bottomRightBar.draw(gui);
	}

	public void drawBiomeDim(GuiGraphics gui) {
		gui.pose().pushPose();
		{
			String dimension = minecraft.player.level().dimension().location().getPath().toUpperCase().replaceAll("_", " ");
			ResourceLocation biomeLoc = new ResourceLocation(printBiome(this.minecraft.level.getBiome(minecraft.player.blockPosition())));

			String biome = "biome." + biomeLoc.getNamespace() + "." + biomeLoc.getPath();
			if (Language.getInstance().has(biome)) {
				biome = Utils.translateToLocal(biome);
			} else {
				biome = biomeLoc.toString();
			}
			String text = dimension + " | " + biome;
			gui.drawString(minecraft.font, text, width - minecraft.font.width(text) - 5, 5, 0xF58B33);
		}
		gui.pose().popPose();
	}

	public void drawMunnyTime(GuiGraphics gui) {
		gui.pose().pushPose();
		{
			gui.pose().scale(1.05F, 1.05F, 1F);
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);
			int y = (int) (topBarHeight + middleHeight +1);
			gui.drawString(minecraft.font, Utils.translateToLocal(Strings.Gui_Menu_Main_Synthesis_Tier) + ": "+ Utils.getTierFromInt(playerData.getSynthLevel()), 5, y, 0xFFFF00);
			y+= minecraft.font.lineHeight;
			gui.drawString(minecraft.font, Utils.translateToLocal(Strings.Gui_Menu_Main_Munny) + ": " + playerData.getMunny(), 5, y, 0xF66627);
			y+= minecraft.font.lineHeight;
			gui.drawString(minecraft.font, Utils.translateToLocal(Strings.Gui_Menu_Main_Hearts) + ": " + playerData.getHearts(), 5, y, playerData.getAlignment() == OrgMember.NONE ? 0x888888 : 0xFF3333);
			y+= minecraft.font.lineHeight;
			gui.drawString(minecraft.font, Utils.translateToLocal(Strings.Gui_Menu_Main_Time) + ": " + getWorldHours(minecraft.level) + ":" + getWorldMinutes(minecraft.level), 5, y, 0xFFFFFF);
			long seconds = minecraft.level.getDayTime() / 20;
			long h = seconds / 3600;
			long m = seconds % 3600 / 60;
			long s = seconds % 3600 % 60;

			String sec = s < 10 ? 0 + "" + s : s + "";
			String min = m < 10 ? 0 + "" + m : m + "";
			String hou = h < 10 ? 0 + "" + h : h + "";
			String time = hou + ":" + min + ":" + sec;
			y+= minecraft.font.lineHeight;

			gui.drawString(minecraft.font, Utils.translateToLocal(Strings.Gui_Menu_Main_Time_Spent) + ": " + time, 5, y, 0x42ceff);
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
				ClientUtils.drawSplitString(gui, Utils.translateToLocal(tip), (int) tooltipPosX, (int) tooltipPosY, (int) (width * 0.6F), 0xFF9900);
			}
			gui.pose().popPose();
		}
		
	}

	public static final ResourceLocation menu = new ResourceLocation(KingdomKeys.MODID, "textures/gui/menu/menu_button.png");

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
	
	private static String printBiome(Holder<Biome> p_205375_) {
	      return p_205375_.unwrap().map((p_205377_) -> {
	         return p_205377_.location().toString();
	      }, (p_205367_) -> {
	         return "[unregistered " + p_205367_ + "]";
	      });
	   }

}

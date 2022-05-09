package online.kingdomkeys.kingdomkeys.client.gui.elements;

import java.awt.Color;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.Holder;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.TranslatableComponent;
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

public class MenuBackground extends Screen {

	int selected;
	
	String tip = null;
	Color color;
	
	public MenuBackground(String name, Color rgb) {
		super(new TranslatableComponent(name));
		minecraft = Minecraft.getInstance();
		selected = -1;
		this.color = rgb;
	}
	//TODO Make menus work with arrow keys?
	
	//public static final int UP = Keybinds.SCROLL_UP.getKeybind().getKey().getKeyCode();
	//public static final int DOWN = Keybinds.SCROLL_DOWN.getKeybind().getKey().getKeyCode();

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		InputConstants.Key mouseKey = InputConstants.getKey(keyCode, scanCode);
		if (super.keyPressed(keyCode, scanCode, modifiers)) {
			return true;
		} else if (InputHandler.Keybinds.OPENMENU.getKeybind().isActiveAndMatches(mouseKey)) {
			//Close screen if already open and pushed this key. Example copied from keyPressed of ContainerScreen
			Minecraft mc = Minecraft.getInstance();
			mc.level.playSound(mc.player, mc.player.blockPosition(), ModSounds.menu_back.get(), SoundSource.MASTER, 1.0f, 1.0f);
			this.onClose();
			return true;
		}
		return false;
	}

	/*@Override
	public boolean keyPressed(int keyId, int p_keyPressed_2_, int p_keyPressed_3_) {
		System.out.println(keyId+" "+p_keyPressed_2_+" "+p_keyPressed_3_);
		//minecraft.gameSettings.keyBindForward.getKey().getKeyCode()
		if(selected >= 0 && selected <= buttons.size() -1) {
			BaseKKGuiButton oldBtn = (BaseKKGuiButton)buttons.get(selected);
			oldBtn.setSelected(false);
		}
		
		if(keyId == DOWN) {
			selected++;
			if(selected > buttons.size()-1) {
				selected = 0;
			}
			
			if(selected >= 0 && selected <= buttons.size() -1) {
				while(!buttons.get(selected).active) {
					selected++;
				}
				BaseKKGuiButton btn = (BaseKKGuiButton)buttons.get(selected);
				if(btn.active)
					btn.setSelected(true);
				minecraft.world.playSound(minecraft.player, minecraft.player.getPosition(), ModSounds.menu_move.get(), SoundCategory.MASTER, 1.0f, 1.0f);

				
			}
			
		}
		
		if(keyId == UP) {
			selected--;
			if(selected < 0) {
				selected = buttons.size()-1;
			}
			while(!buttons.get(selected).active) {
				selected--;
				if(selected == -1)
					selected = buttons.size()-1;
				if(buttons.get(selected) instanceof BaseKKGuiButton)
					break;
			}
			BaseKKGuiButton btn = (BaseKKGuiButton)buttons.get(selected);
			if(btn.active)
				btn.setSelected(true);
			minecraft.world.playSound(minecraft.player, minecraft.player.getPosition(), ModSounds.menu_move.get(), SoundCategory.MASTER, 1.0f, 1.0f);

		}
		
		if(keyId == 262) {
			if(selected > -1) {
				BaseKKGuiButton btn = (BaseKKGuiButton)buttons.get(selected);
				btn.onPress();
			}
		}
		
		System.out.println(selected);

		return super.keyPressed(keyId, p_keyPressed_2_, p_keyPressed_3_);
	}*/
	
	public boolean drawPlayerInfo;

	MenuBar bottomLeftBar, bottomRightBar, topLeftBar, topRightBar;

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

	//Separate method to render buttons in a different order
	public void drawMenuBackground(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		drawBars(matrixStack);
		drawMunnyTime(matrixStack);
		drawBiomeDim(matrixStack);
		drawTip(matrixStack);
		//RenderHelper.disableStandardItemLighting();
		//drawBackground(width, height, drawPlayerInfo);

		matrixStack.pushPose();
		{
			matrixStack.scale(1.3F,1.3F, 1F);
			drawString(matrixStack, minecraft.font, Utils.translateToLocal(getTitle().getString()), 2, 10, 0xFF9900);
		}
		matrixStack.popPose();
	}

	@Override
	public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		if (!drawSeparately)
			drawMenuBackground(matrixStack, mouseX, mouseY, partialTicks);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
	}

	private void clearButtons() {
		for(Widget btn : renderables) {
			if(btn instanceof MenuButtonBase) {
				((MenuButtonBase) btn).setSelected(false);
			}
		}
			
	}

	public void drawBars(PoseStack matrixStack) {
		renderBackground(matrixStack);
		int sh = Minecraft.getInstance().getWindow().getGuiScaledHeight();
		int sw = Minecraft.getInstance().getWindow().getGuiScaledWidth();

		for (int i = 0; i < sh; i += 3) {
			matrixStack.pushPose();
			RenderSystem.setShader(GameRenderer::getPositionTexShader);
			RenderSystem.setShaderColor(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, 1.0F);
			// RenderSystem.enableAlpha();
			RenderSystem.enableBlend();
			RenderSystem.setShaderTexture(0, menu);
			matrixStack.translate(0, i, 0);
			matrixStack.scale(sw, 1, 1);
			blit(matrixStack, 0, 0, 77, 92, 1, 1);
			RenderSystem.disableBlend();
			matrixStack.popPose();
		}
		topLeftBar.draw(matrixStack);
		topRightBar.draw(matrixStack);
		bottomLeftBar.draw(matrixStack);
		bottomRightBar.draw(matrixStack);
	}

	public void drawBiomeDim(PoseStack matrixStack) {
		matrixStack.pushPose();
		{
			String dimension = minecraft.player.level.dimension().location().getPath().toUpperCase().replaceAll("_", " ");
			ResourceLocation biomeLoc = new ResourceLocation(printBiome(this.minecraft.level.getBiome(minecraft.player.blockPosition())));

			String biome = "biome." + biomeLoc.getNamespace() + "." + biomeLoc.getPath();
			if (Language.getInstance().has(biome)) {
				biome = Utils.translateToLocal(biome);
			} else {
				biome = biomeLoc.toString();
			}
			String text = dimension + " | " + biome;
			drawString(matrixStack, minecraft.font, text, width - minecraft.font.width(text) - 5, 5, 0xF58B33);
		}
		matrixStack.popPose();
	}

	public void drawMunnyTime(PoseStack matrixStack) {
		matrixStack.pushPose();
		{
			matrixStack.scale(1.05F, 1.05F, 1F);
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);
			int y = (int) (topBarHeight + middleHeight +1);
			drawString(matrixStack,minecraft.font, Utils.translateToLocal("Synthesis Tier") + ": "+ Utils.getTierFromInt(playerData.getSynthLevel()), 5, y, 0xFFFF00);
			y+= minecraft.font.lineHeight;
			drawString(matrixStack,minecraft.font, Utils.translateToLocal(Strings.Gui_Menu_Main_Munny) + ": " + playerData.getMunny(), 5, y, 0xF66627);
			y+= minecraft.font.lineHeight;
			drawString(matrixStack,minecraft.font, Utils.translateToLocal(Strings.Gui_Menu_Main_Hearts) + ": " + playerData.getHearts(), 5, y, playerData.getAlignment() == OrgMember.NONE ? 0x888888 : 0xFF3333);
			y+= minecraft.font.lineHeight;
			drawString(matrixStack,minecraft.font, Utils.translateToLocal(Strings.Gui_Menu_Main_Time) + ": " + getWorldHours(minecraft.level) + ":" + getWorldMinutes(minecraft.level), 5, y, 0xFFFFFF);
			long seconds = minecraft.level.getDayTime() / 20;
			long h = seconds / 3600;
			long m = seconds % 3600 / 60;
			long s = seconds % 3600 % 60;

			String sec = s < 10 ? 0 + "" + s : s + "";
			String min = m < 10 ? 0 + "" + m : m + "";
			String hou = h < 10 ? 0 + "" + h : h + "";
			String time = hou + ":" + min + ":" + sec;
			y+= minecraft.font.lineHeight;

			drawString(matrixStack, minecraft.font, Utils.translateToLocal(Strings.Gui_Menu_Main_Time_Spent) + ": " + time, 5, y, 0x42ceff);
		}
		matrixStack.popPose();
	}
	
	public void drawTip (PoseStack matrixStack) {
		tip = null;

		for(Widget btn : renderables) {
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
			matrixStack.pushPose();
			{
				matrixStack.scale(1.1F, 1.1F, 1F);
				matrixStack.translate(0, -5, 0);
				//minecraft.fontRenderer.drawSplitString(keyblade.getDescription(), (int) tooltipPosX + 3, (int) tooltipPosY + 3, (int) (parent.width * 0.46875F), 0x43B5E9);
				ClientUtils.drawSplitString(minecraft.font, Utils.translateToLocal(tip), (int) (bottomLeftBarWidth + bottomGap), (int) (topBarHeight + middleHeight), (int) (width * 0.5F), 0xFF9900);
			}
			matrixStack.popPose();
		}
		
	}

	public static final ResourceLocation optionsBackground = new ResourceLocation(KingdomKeys.MODID, "textures/gui/menubg.png");
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
	}

	protected void drawBackground(int screenWidth, int screenHeight, boolean drawPlayer) {
		// Minecraft.getInstance().renderEngine.bindTexture(optionsBackground);
	}
	
	private static String printBiome(Holder<Biome> p_205375_) {
	      return p_205375_.unwrap().map((p_205377_) -> {
	         return p_205377_.location().toString();
	      }, (p_205367_) -> {
	         return "[unregistered " + p_205367_ + "]";
	      });
	   }

}

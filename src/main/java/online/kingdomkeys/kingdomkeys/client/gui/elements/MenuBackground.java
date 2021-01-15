package online.kingdomkeys.kingdomkeys.client.gui.elements;

import java.awt.Color;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButtonBase;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.kingdomkeys.kingdomkeys.util.Utils.OrgMember;

public class MenuBackground extends Screen {

	int selected;
	
	String tip = null;
	Color color;
	
	public MenuBackground(String name, Color rgb) {
		super(new TranslationTextComponent(name));
		minecraft = Minecraft.getInstance();
		selected = -1;
		this.color = rgb;
	}
	//TODO Make menus work with arrow keys?
	
	//public static final int UP = Keybinds.SCROLL_UP.getKeybind().getKey().getKeyCode();
	//public static final int DOWN = Keybinds.SCROLL_DOWN.getKeybind().getKey().getKeyCode();

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
	public void drawMenuBackground(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		drawBars(matrixStack);
		drawMunnyTime(matrixStack);
		drawBiomeDim(matrixStack);
		drawTip(matrixStack);
		//RenderHelper.disableStandardItemLighting();
		//drawBackground(width, height, drawPlayerInfo);
		

		matrixStack.push();
		{
			matrixStack.scale(1.3F,1.3F, 1F);
			drawString(matrixStack, minecraft.fontRenderer, Utils.translateToLocal(getTitle().getString()), 2, 10, 0xFF9900);
		}
		matrixStack.pop();
	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		if (!drawSeparately)
			drawMenuBackground(matrixStack, mouseX, mouseY, partialTicks);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
	}

	private void clearButtons() {
		for(Widget btn : buttons) {
			if(btn instanceof MenuButtonBase) {
				((MenuButtonBase) btn).setSelected(false);
			}
		}
			
	}

	public void drawBars(MatrixStack matrixStack) {
		renderBackground(matrixStack);
		int sh = Minecraft.getInstance().getMainWindow().getScaledHeight();
		int sw = Minecraft.getInstance().getMainWindow().getScaledWidth();

		for (int i = 0; i < sh; i += 3) {
			matrixStack.push();
			RenderSystem.color3f(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F);
			// RenderSystem.enableAlpha();
			RenderSystem.enableBlend();
			minecraft.textureManager.bindTexture(menu);
			matrixStack.translate(0, i, 0);
			matrixStack.scale(sw, 1, 1);
			blit(matrixStack, 0, 0, 77, 92, 1, 1);
			RenderSystem.disableBlend();
			matrixStack.pop();
		}
		topLeftBar.draw(matrixStack);
		topRightBar.draw(matrixStack);
		bottomLeftBar.draw(matrixStack);
		bottomRightBar.draw(matrixStack);
	}

	public void drawBiomeDim(MatrixStack matrixStack) {
		matrixStack.push();
		{
			String text = minecraft.player.world.getDimensionKey().getLocation().getPath().toString().toUpperCase() + " | " + minecraft.player.world.getBiome(minecraft.player.getPosition()).getRegistryName();
			drawString(matrixStack, minecraft.fontRenderer, text, width - minecraft.fontRenderer.getStringWidth(text) - 5, 5, 0xF58B33);
		}
		matrixStack.pop();
	}

	public void drawMunnyTime(MatrixStack matrixStack) {
		matrixStack.push();
		{
			matrixStack.scale(1.1F, 1.1F, 1F);
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);
			drawString(matrixStack,minecraft.fontRenderer, Utils.translateToLocal(Strings.Gui_Menu_Main_Munny) + ": " + playerData.getMunny(), 5, (int) (topBarHeight + middleHeight) - 7, 0xF66627);
			drawString(matrixStack,minecraft.fontRenderer, Utils.translateToLocal(Strings.Gui_Menu_Main_Hearts) + ": " + playerData.getHearts(), 5, (int) (topBarHeight + middleHeight) + minecraft.fontRenderer.FONT_HEIGHT - 7, playerData.getAlignment() == OrgMember.NONE ? 0x888888 : 0xFF3333);
			drawString(matrixStack,minecraft.fontRenderer, Utils.translateToLocal(Strings.Gui_Menu_Main_Time) + ": " + getWorldHours(minecraft.world) + ":" + getWorldMinutes(minecraft.world), 5, (int) (topBarHeight + middleHeight) + minecraft.fontRenderer.FONT_HEIGHT * 2 - 7, 0xFFFFFF);
			long seconds = minecraft.world.getDayTime() / 20;
			long h = seconds / 3600;
			long m = seconds % 3600 / 60;
			long s = seconds % 3600 % 60;

			String sec = s < 10 ? 0 + "" + s : s + "";
			String min = m < 10 ? 0 + "" + m : m + "";
			String hou = h < 10 ? 0 + "" + h : h + "";
			String time = hou + ":" + min + ":" + sec;
			drawString(matrixStack, minecraft.fontRenderer, Utils.translateToLocal(Strings.Gui_Menu_Main_Time_Spent) + ": " + time, 5, (int) (topBarHeight + middleHeight) + (minecraft.fontRenderer.FONT_HEIGHT * 3) - 7, 0x42ceff);
		}
		matrixStack.pop();
	}
	
	public void drawTip (MatrixStack matrixStack) {
		tip = null;

		int i = 0;
		for(Widget btn : buttons) {
			if(btn instanceof MenuButtonBase) {
				i++;
				if(btn.isHovered()) {
					selected = -1;
					clearButtons();

					if(btn instanceof MenuButton && btn.visible) {
						tip = ((MenuButton) btn).getTip();
					}
				}
			}
		}
		
		if(tip != null) {
			RenderSystem.pushMatrix();
			{
				RenderSystem.scalef(1.1F, 1.1F, 1F);
				RenderSystem.translatef(0, -5, 0);
				//minecraft.fontRenderer.drawSplitString(keyblade.getDescription(), (int) tooltipPosX + 3, (int) tooltipPosY + 3, (int) (parent.width * 0.46875F), 0x43B5E9);
				Utils.drawSplitString(minecraft.fontRenderer, Utils.translateToLocal(tip), (int) (bottomLeftBarWidth + bottomGap), (int) (topBarHeight + middleHeight), (int) (width * 0.5F), 0xFF9900);
			}
			RenderSystem.popMatrix();
		}
		
	}

	public static final ResourceLocation optionsBackground = new ResourceLocation(KingdomKeys.MODID, "textures/gui/menubg.png");
	public static final ResourceLocation menu = new ResourceLocation(KingdomKeys.MODID, "textures/gui/menu/menu_button.png");

	public static String getWorldMinutes(World world) {
		int time = (int) Math.abs((world.getGameTime() + 6000) % 24000);
		if ((time % 1000) * 6 / 100 < 10)
			return "0" + (time % 1000) * 6 / 100;
		else
			return Integer.toString((time % 1000) * 6 / 100);
	}

	public static int getWorldHours(World world) {
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

}

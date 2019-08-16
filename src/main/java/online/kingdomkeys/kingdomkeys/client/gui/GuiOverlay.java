package online.kingdomkeys.kingdomkeys.client.gui;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.ILevelCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.lib.Utils;

public class GuiOverlay extends Screen {
	public GuiOverlay() {
		super(new TranslationTextComponent(""));
	}

	public static boolean showExp;
	public static boolean showMunny;
	public static boolean showLevelUp;
	public static boolean showDriveLevelUp;
	//public static WorldTeleporter teleport;
	public static String driveForm = "none";
	public static long timeExp;
	public static long timeMunny;
	public static long timeLevelUp;
	public static long timeDriveLevelUp;
	public static int munnyGet;
	int levelSeconds = 6;

	int width;
	int sHeight;

	Minecraft mc;
	ILevelCapabilities STATS;


	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public void RenderGameOverlayEvent(RenderGameOverlayEvent event) {
		if (event.getType() == ElementType.TEXT) {
			mc = Minecraft.getInstance();

			width = mc.mainWindow.getScaledWidth();
			sHeight = mc.mainWindow.getScaledHeight();

			STATS = ModCapabilities.get(mc.player);
			//DRIVE = mc.player.getCapability(ModCapabilities.DRIVE_STATE, null);

			// Experience
			if (showExp) {
				showExp();
			}

			// Munny
			if (showMunny) {
				showMunny();
			}

			// Level Up
			if (showLevelUp) {
				showLevelUp(event);
			}

			// Drive form level up
			if (showDriveLevelUp) {
				//showDriveLevelUp(event);
			}
			
			/*if(teleport != null) {
				showTeleport();
			}*/
		}
	}

	/*private void showTeleport() {
		String text = teleport.toName;
		GlStateManager.pushMatrix();
		{
			GlStateManager.translate(width/2-(mc.fontRenderer.getStringWidth(text)*2)/2, sHeight - sHeight/6, 1);
			GlStateManager.scale(2, 2, 2);
			mc.fontRenderer.drawStringWithShadow(text, 0,0, 0xFFFFFF);
		}
		GlStateManager.popMatrix();
	}*/

	private void showExp() {
		String reqExp = String.valueOf(STATS.getExpNeeded(STATS.getLevel(), STATS.getExperience()));
		mc.fontRenderer.drawString("Next LV", 5, 5, 0xFFFFFF);
		mc.fontRenderer.drawString(reqExp, 5, 5 + mc.fontRenderer.FONT_HEIGHT, 0xFFFFFF);
		//System.out.println("\nStart time: "+timeExp+"\nActual time:"+System.currentTimeMillis()/1000+"\nEnd Time:   "+(timeExp + 4));
		if (System.currentTimeMillis()/1000 > (timeExp + 4))
			showExp = false;
	}

	private void showMunny() {
		if (!showExp) { // If no exp is being display print it at the top
			GL11.glPushMatrix();
			GL11.glTranslatef(1, 1, 0);
			mc.fontRenderer.drawString("Munny Get!", 5, 5, 0xFFFFFF);
			mc.fontRenderer.drawString(munnyGet + "", 5, 5 + mc.fontRenderer.FONT_HEIGHT, 0xFFFFFF);
			GL11.glPopMatrix();
		} else { // If exp is being displayed print it below it
			mc.fontRenderer.drawString("Munny Get!", 5, 5 + mc.fontRenderer.FONT_HEIGHT + 10, 0xFFFFFF);
			mc.fontRenderer.drawString(munnyGet + "", 5, 5 + (mc.fontRenderer.FONT_HEIGHT * 2) + 10, 0xFFFFFF);
		}
		if (timeMunny + 4 <= (int) mc.world.getGameTime() / 1000)
			showMunny = false;
	}

	private void showLevelUp(RenderGameOverlayEvent event) {
		ResourceLocation texture = new ResourceLocation(KingdomKeys.MODID, "textures/gui/levelup.png");
		GL11.glPushMatrix();
		{
			int height = (mc.fontRenderer.FONT_HEIGHT - 3) * STATS.getMessages().size();
			GL11.glEnable(GL11.GL_BLEND);
			//GL11.glColor4ub((byte) MainConfig.client.hud.interfaceColour[0], (byte) MainConfig.client.hud.interfaceColour[1], (byte) MainConfig.client.hud.interfaceColour[2], (byte) 255);
			GL11.glColor4ub((byte)255,(byte)255,(byte)255, (byte) 255);

			// Top
			mc.textureManager.bindTexture(texture);
			GL11.glPushMatrix();
			{
				GL11.glTranslatef((width - 153.6f - 2), 0, 0);
				GL11.glScalef(0.6f, 0.6f, 1);
				blit(0, 0, 0, 0, 256, 36);
			}
			GL11.glPopMatrix();

//			showText("LEVEL UP!" + TextFormatting.ITALIC, width - ((mc.fontRenderer.getStringWidth("LEVEL UP!")) * 0.75f) - 115, 4, 0, 0.75f, 0.75f, 1, Color.decode(String.format("#%02x%02x%02x", (byte) MainConfig.client.hud.interfaceColour[0], (byte) MainConfig.client.hud.interfaceColour[1], (byte) MainConfig.client.hud.interfaceColour[2])).hashCode());
			showText("LEVEL UP!" + TextFormatting.ITALIC, width - ((mc.fontRenderer.getStringWidth("LEVEL UP!")) * 0.75f) - 115, 4, 0, 0.75f, 0.75f, 1, Color.decode(String.format("#%02x%02x%02x", (byte)255,(byte)255,(byte)255)).hashCode());
			showText("LV.", width - ((mc.fontRenderer.getStringWidth("LV. ")) * 0.75f) - 90, 4, 0, 0.75f, 0.75f, 1, 0xE3D000);
			showText("" + STATS.getLevel(), width - 256.0f * 0.75f + ((mc.fontRenderer.getStringWidth("999")) * 0.75f) + 88, 4, 0, 0.75f, 0.75f, 1, 0xFFFFFF);
			showText(mc.player.getDisplayName().getString(), width - ((mc.fontRenderer.getStringWidth(mc.player.getDisplayName().getString())) * 0.75f) - 7, 4, 0, 0.75f, 0.75f, 1, 0xFFFFFF);

			// Half
			//GL11.glColor4ub((byte) MainConfig.client.hud.interfaceColour[0], (byte) MainConfig.client.hud.interfaceColour[1], (byte) MainConfig.client.hud.interfaceColour[2], (byte) 255);
			GL11.glColor4ub((byte)255,(byte)255,(byte)255, (byte) 255);

			GL11.glPushMatrix();
			{
				mc.textureManager.bindTexture(texture);
				GL11.glTranslatef((width - 256.0f * 0.6f - 2), 36.0f * 0.6f, 0);
				GL11.glScalef(0.6f, height, 1);
				blit(0, 0, 0, 36, 256, 1);
			}
			GL11.glPopMatrix();

			// Bottom
			//GL11.glColor4ub((byte) MainConfig.client.hud.interfaceColour[0], (byte) MainConfig.client.hud.interfaceColour[1], (byte) MainConfig.client.hud.interfaceColour[2], (byte) 255);
			GL11.glColor4ub((byte)255,(byte)255,(byte)255, (byte) 255);

			GL11.glPushMatrix();
			{
				mc.textureManager.bindTexture(texture);
				GL11.glTranslatef((width - 256.0f * 0.6f - 2), height + (36.0f * 0.6f), 0);
				GL11.glScalef(0.6f, 0.6f, 1);
				blit(0, 0, 0, 37, 256, 14);
			}
			GL11.glPopMatrix();

			// Text
			//GL11.glColor4ub((byte) MainConfig.client.hud.interfaceColour[0], (byte) MainConfig.client.hud.interfaceColour[1], (byte) MainConfig.client.hud.interfaceColour[2], (byte) 255);
			GL11.glColor4ub((byte)255,(byte)255,(byte)255, (byte) 255);
			//System.out.println(STATS.getMessages());
			for (int i = 0; i < STATS.getMessages().size(); i++) {
				String message = STATS.getMessages().get(i).toString();
				showText(Utils.translateToLocal(message), (width - 256.0f * 0.8f + (mc.fontRenderer.getStringWidth("Maximum HP Increased!")) * 0.8f) - 35, mc.fontRenderer.FONT_HEIGHT * 0.8f * i + 23, 0, 0.8f, 0.8f, 1, 0xFFFFFF);
			}
			GL11.glColor4ub((byte) 255, (byte) 255, (byte) 255, (byte) 255);
		}
		GL11.glPopMatrix();
		
		if (System.currentTimeMillis()/1000 > (timeLevelUp + levelSeconds))
			showLevelUp = false;
	}

	/*private void showDriveLevelUp(RenderGameOverlayEvent event) {
		byte[] driveColor = getDriveFormColor();

		ResourceLocation texture = new ResourceLocation(KingdomKeys.MODID, "textures/gui/levelup.png");

		GL11.glPushMatrix();
		{
			int height = (mc.fontRenderer.FONT_HEIGHT - 3) * DRIVE.getMessages().size();
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glColor4ub(driveColor[0], driveColor[1], driveColor[2], (byte) 255);

			// Top
			mc.renderEngine.bindTexture(texture);
			GL11.glPushMatrix();
			{
				GL11.glTranslatef(2, sHeight / 3, 0);
				GL11.glScalef(0.6f, 0.6f, 1);
				drawTexturedModalRect(0, 0, 0, 0, 256, 36);
			}
			GL11.glPopMatrix();

			showText("LEVEL UP!", 6, sHeight / 3 + 5, 0, 0.75F, 0.75F, 1, Color.decode(String.format("#%02x%02x%02x", (byte) MainConfig.client.hud.interfaceColour[0], (byte) MainConfig.client.hud.interfaceColour[1], (byte) MainConfig.client.hud.interfaceColour[2])).hashCode());
			showText("LV.", 2 + (mc.fontRenderer.getStringWidth("LV. ") * 0.75f) + 38, sHeight / 3 + 4, 0, 0.75f, 0.75f, 1, 0xE3D000);
			showText("" + DRIVE.getDriveLevel(driveForm), 2 * 0.75f + (mc.fontRenderer.getStringWidth("999") * 0.75f) + 50, sHeight / 3 + 4, 0, 0.75f, 0.75f, 1, 0xFFFFFF);
			showText(Utils.translateToLocal(driveForm), 150 - (mc.fontRenderer.getStringWidth(Utils.translateToLocal(driveForm)) * 0.75f), sHeight / 3 + 4, 0, 0.75f, 0.75f, 1, 0xFFFFFF);

			// Half
			GL11.glColor4ub(driveColor[0], driveColor[1], driveColor[2], (byte) 255);
			GL11.glPushMatrix();
			{
				mc.renderEngine.bindTexture(texture);
				GL11.glTranslatef(2.0f * 0.6f + 1, sHeight / 3 + 36.0f * 0.6f, 0);
				GL11.glScalef(0.6f, height, 1);
				drawTexturedModalRect(0, 0, 0, 36, 256, 1);
			}
			GL11.glPopMatrix();

			// Bottom
			GL11.glColor4ub(driveColor[0], driveColor[1], driveColor[2], (byte) 255);
			GL11.glPushMatrix();
			{
				mc.renderEngine.bindTexture(texture);
				GL11.glTranslatef(2, sHeight / 3 + height + (36.0f * 0.6f), 0);
				GL11.glScalef(0.6f, 0.6f, 1);
				drawTexturedModalRect(0, 0, 0, 37, 256, 14);
			}
			GL11.glPopMatrix();

			// Text
			GL11.glColor4ub(driveColor[0], driveColor[1], driveColor[2], (byte) 255);
			for (int i = 0; i < DRIVE.getMessages().size(); i++) {
				String message = DRIVE.getMessages().get(i).toString();
				showText(Utils.translateToLocalFormatted(message), 2 * 0.8f + 15, sHeight / 3 + mc.fontRenderer.FONT_HEIGHT * 0.8f * i + 21, 0, 0.8f, 0.8f, 1, 0xFFFFFF);
			}
			GL11.glColor4ub((byte) 255, (byte) 255, (byte) 255, (byte) 255);
		}
		GL11.glPopMatrix();
		if (timeDriveLevelUp + levelSeconds <= (int) Minecraft.getSystemTime() / 1000)
			showDriveLevelUp = false;
	}*/

	private void showText(String text, float tX, float tY, float tZ, float sX, float sY, float sZ, int color) {
		GL11.glPushMatrix();
		{
			GL11.glTranslatef(tX, tY, tZ);
			GL11.glScalef(sX, sY, sZ);
			drawString(mc.fontRenderer, text, 0, 0, color);
		}
		GL11.glPopMatrix();
	}

	private byte[] getDriveFormColor() {
		byte[] driveColor = new byte[] { (byte) 255, (byte) 255, (byte) 255 };

		switch (driveForm) {
		case Strings.Form_Valor:
			driveColor[0] = (byte) 255;
			driveColor[1] = (byte) 0;
			driveColor[2] = (byte) 0;
			break;

		case Strings.Form_Wisdom:
			driveColor[0] = (byte) 0;
			driveColor[1] = (byte) 0;
			driveColor[2] = (byte) 255;
			break;

		case Strings.Form_Limit:
			driveColor[0] = (byte) 153;
			driveColor[1] = (byte) 76;
			driveColor[2] = (byte) 255;
			break;

		case Strings.Form_Master:
			driveColor[0] = (byte) 255;
			driveColor[1] = (byte) 185;
			driveColor[2] = (byte) 25;
			break;

		case Strings.Form_Final:
			driveColor[0] = (byte) 230;
			driveColor[1] = (byte) 230;
			driveColor[2] = (byte) 230;
			break;
		}
		return driveColor;
	}
}
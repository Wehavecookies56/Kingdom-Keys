package online.kingdomkeys.kingdomkeys.client.gui;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.systems.RenderSystem;

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
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.util.Utils;
//TODO replace GL11 stuff
public class GuiOverlay extends Screen {
	public GuiOverlay() {
		super(new TranslationTextComponent(""));
		minecraft = Minecraft.getInstance();
	}

	public static boolean showExp;
	public static boolean showMunny;
	public static boolean showLevelUp;
	public static boolean showDriveLevelUp;
	//public static WorldTeleporter teleport;
	public static String driveForm = "";
	public static long timeExp;
	public static long timeMunny;
	public static long timeLevelUp;
	public static long timeDriveLevelUp;
	public static int munnyGet;
	int levelSeconds = 6;

	int width;
	int sHeight;

	IPlayerCapabilities playerData;


	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public void RenderGameOverlayEvent(RenderGameOverlayEvent event) {
		if (event.getType() == ElementType.TEXT) {

			width = minecraft.getMainWindow().getScaledWidth();
			sHeight = minecraft.getMainWindow().getScaledHeight();

			playerData = ModCapabilities.getPlayer(minecraft.player);

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
				showDriveLevelUp(event);
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
			GlStateManager.translate(width/2-(minecraft.fontRenderer.getStringWidth(text)*2)/2, sHeight - sHeight/6, 1);
			GlStateManager.scale(2, 2, 2);
			minecraft.fontRenderer.drawStringWithShadow(text, 0,0, 0xFFFFFF);
		}
		GlStateManager.popMatrix();
	}*/

	private void showExp() {
		if(playerData != null) {
			String reqExp = String.valueOf(playerData.getExpNeeded(playerData.getLevel(), playerData.getExperience()));
			minecraft.fontRenderer.drawString("Next LV", 5, 5, 0xFFFFFF);
			minecraft.fontRenderer.drawString(reqExp, 5, 5 + minecraft.fontRenderer.FONT_HEIGHT, 0xFFFFFF);
			//System.out.println("\nStart time: "+timeExp+"\nActual time:"+System.currentTimeMillis()/1000+"\nEnd Time:   "+(timeExp + 4));
			if (System.currentTimeMillis()/1000 > (timeExp + 4))
				showExp = false;
		}
	}

	private void showMunny() {
		if (!showExp) { // If no exp is being display print it at the top
			RenderSystem.pushMatrix();{
			RenderSystem.translatef(1, 1, 0);
			minecraft.fontRenderer.drawString("Munny Get!", 5, 5, 0xFFFFFF);
			minecraft.fontRenderer.drawString(munnyGet + "", 5, 5 + minecraft.fontRenderer.FONT_HEIGHT, 0xFFFFFF);
			}
			RenderSystem.popMatrix();
		} else { // If exp is being displayed print it below it
			minecraft.fontRenderer.drawString("Munny Get!", 5, 5 + minecraft.fontRenderer.FONT_HEIGHT + 10, 0xFFFFFF);
			minecraft.fontRenderer.drawString(munnyGet + "", 5, 5 + (minecraft.fontRenderer.FONT_HEIGHT * 2) + 10, 0xFFFFFF);
		}
		if (System.currentTimeMillis()/1000 > (timeMunny + 4))
			showMunny = false;
	}

	private void showLevelUp(RenderGameOverlayEvent event) {
		ResourceLocation texture = new ResourceLocation(KingdomKeys.MODID, "textures/gui/levelup.png");
		GL11.glPushMatrix();
		{
			int height = (minecraft.fontRenderer.FONT_HEIGHT - 3) * playerData.getMessages().size();
			GL11.glEnable(GL11.GL_BLEND);
			//GL11.glColor4ub((byte) MainConfig.client.hud.interfaceColour[0], (byte) MainConfig.client.hud.interfaceColour[1], (byte) MainConfig.client.hud.interfaceColour[2], (byte) 255);
			GL11.glColor4ub((byte)255,(byte)255,(byte)255, (byte) 255);

			// Top
			minecraft.textureManager.bindTexture(texture);
			GL11.glPushMatrix();
			{
				GL11.glTranslatef((width - 153.6f - 2), 0, 0);
				GL11.glScalef(0.6f, 0.6f, 1);
				blit(0, 0, 0, 0, 256, 36);
			}
			GL11.glPopMatrix();

//			showText("LEVEL UP!" + TextFormatting.ITALIC, width - ((minecraft.fontRenderer.getStringWidth("LEVEL UP!")) * 0.75f) - 115, 4, 0, 0.75f, 0.75f, 1, Color.decode(String.format("#%02x%02x%02x", (byte) MainConfig.client.hud.interfaceColour[0], (byte) MainConfig.client.hud.interfaceColour[1], (byte) MainConfig.client.hud.interfaceColour[2])).hashCode());
			showText("LEVEL UP!" + TextFormatting.ITALIC, width - ((minecraft.fontRenderer.getStringWidth("LEVEL UP!")) * 0.75f) - 115, 4, 0, 0.75f, 0.75f, 1, Color.decode(String.format("#%02x%02x%02x", (byte)255,(byte)255,(byte)255)).hashCode());
			showText("LV.", width - ((minecraft.fontRenderer.getStringWidth("LV. ")) * 0.75f) - 90, 4, 0, 0.75f, 0.75f, 1, 0xE3D000);
			showText("" + playerData.getLevel(), width - 256.0f * 0.75f + ((minecraft.fontRenderer.getStringWidth("999")) * 0.75f) + 88, 4, 0, 0.75f, 0.75f, 1, 0xFFFFFF);
			showText(minecraft.player.getDisplayName().getString(), width - ((minecraft.fontRenderer.getStringWidth(minecraft.player.getDisplayName().getString())) * 0.75f) - 7, 4, 0, 0.75f, 0.75f, 1, 0xFFFFFF);

			// Half
			//GL11.glColor4ub((byte) MainConfig.client.hud.interfaceColour[0], (byte) MainConfig.client.hud.interfaceColour[1], (byte) MainConfig.client.hud.interfaceColour[2], (byte) 255);
			GL11.glColor4ub((byte)255,(byte)255,(byte)255, (byte) 255);

			GL11.glPushMatrix();
			{
				minecraft.textureManager.bindTexture(texture);
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
				minecraft.textureManager.bindTexture(texture);
				GL11.glTranslatef((width - 256.0f * 0.6f - 2), height + (36.0f * 0.6f), 0);
				GL11.glScalef(0.6f, 0.6f, 1);
				blit(0, 0, 0, 37, 256, 14);
			}
			GL11.glPopMatrix();

			// Text
			//GL11.glColor4ub((byte) MainConfig.client.hud.interfaceColour[0], (byte) MainConfig.client.hud.interfaceColour[1], (byte) MainConfig.client.hud.interfaceColour[2], (byte) 255);
			GL11.glColor4ub((byte)255,(byte)255,(byte)255, (byte) 255);
			//System.out.println(STATS.getMessages());
			for (int i = 0; i < playerData.getMessages().size(); i++) {
				String message = playerData.getMessages().get(i).toString();
				showText(Utils.translateToLocal(message), (width - 256.0f * 0.8f + (minecraft.fontRenderer.getStringWidth("Maximum HP Increased!")) * 0.8f) - 35, minecraft.fontRenderer.FONT_HEIGHT * 0.8f * i + 23, 0, 0.8f, 0.8f, 1, 0xFFFFFF);
			}
			GL11.glColor4ub((byte) 255, (byte) 255, (byte) 255, (byte) 255);
		}
		GL11.glPopMatrix();
		
		if (System.currentTimeMillis()/1000 > (timeLevelUp + levelSeconds))
			showLevelUp = false;
	}

	private void showDriveLevelUp(RenderGameOverlayEvent event) {
		if(playerData == null)
			return;

		byte[] driveColor = getDriveFormColor(); //TODO set color in the registry

		ResourceLocation texture = new ResourceLocation(KingdomKeys.MODID, "textures/gui/levelup.png");
		GL11.glPushMatrix();
		{
			
			int heightBase = (minecraft.fontRenderer.FONT_HEIGHT - 3) * (playerData.getMessages().size()+0);
			int heightDF = (minecraft.fontRenderer.FONT_HEIGHT - 3) * playerData.getDFMessages().size();
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glColor4ub(driveColor[0], driveColor[1], driveColor[2], (byte) 255);
//Base Abilities
			GL11.glPushMatrix();
			{
				// Top
				minecraft.textureManager.bindTexture(texture);
				GL11.glColor4ub((byte) 100,(byte) 100,(byte) 100, (byte) 255);
				GL11.glPushMatrix();
				{
					GL11.glTranslatef(2, sHeight / 3, 0);
					GL11.glScalef(0.6f, 0.6f, 1);
					blit(0, 0, 0, 51, 256, 36);
				}
				GL11.glPopMatrix();
	
				showText(minecraft.player.getDisplayName().getFormattedText(), 140 - (minecraft.fontRenderer.getStringWidth(minecraft.player.getDisplayName().getFormattedText()) * 0.75f), sHeight / 3 + 4, 0, 0.75f, 0.75f, 1, 0xFFFFFF);
	
				// Half
				GL11.glColor4ub((byte) 100,(byte) 100,(byte) 100, (byte) 255);
				GL11.glPushMatrix();
				{
					minecraft.textureManager.bindTexture(texture);
					GL11.glTranslatef(2, sHeight / 3 + 21, 0);
					GL11.glScalef(0.6f, heightBase+1, 1);
					blit(0, 0, 0, 51+36, 256, 1);
				}
				GL11.glPopMatrix();
	
				// Text
				GL11.glColor4ub((byte) 100,(byte) 100,(byte) 100, (byte) 255);
				for (int i = 0; i < playerData.getMessages().size(); i++) {
					String message = playerData.getMessages().get(i).toString();
					showText(Utils.translateToLocalFormatted(message), 2 * 1f + 35, sHeight / 3 + minecraft.fontRenderer.FONT_HEIGHT * 1.1F * i + 21, 0, 0.8f, 0.8f, 1, 0xFFFFFF);
				}
				
				// Bottom
				GL11.glColor4ub((byte) 100,(byte) 100,(byte) 100, (byte) 255);
				GL11.glPushMatrix();
				{
					minecraft.textureManager.bindTexture(texture);
					GL11.glTranslatef(2, sHeight / 3 + 22 + heightBase, 0);
					GL11.glScalef(0.6f, 0.6f, 1);
					blit(0, 0, 0, 51+37, 244, 14);
				}
				GL11.glPopMatrix();
				
				// Icon
				GL11.glColor4ub((byte) 200,(byte) 200,(byte) 200, (byte) 255);
				GL11.glPushMatrix();
				{
					minecraft.textureManager.bindTexture(texture);
					GL11.glTranslatef(4.5F, sHeight / 3+6, 0);
					GL11.glScalef(0.6f, 0.6f, 1);
					blit(0, 0, 0, 102, 43, 36);
				}
				GL11.glPopMatrix();
			}
			GL11.glPopMatrix();
			
//Form Abilities
			GL11.glPushMatrix();
			{
				// Top
				minecraft.textureManager.bindTexture(texture);
				GL11.glColor4ub(driveColor[0], driveColor[1], driveColor[2], (byte) 255);
				GL11.glPushMatrix();
				{
					GL11.glTranslatef(2, sHeight / 3 + 29 + heightBase, 0);
					GL11.glScalef(0.6f, 0.6f, 1);
					blit(0, 0, 0, 51, 256, 36);
				}
				GL11.glPopMatrix();
	
				//showText("LEVEL UP!", 6, sHeight / 3 + 29 + heightBase + 5, 0, 0.75F, 0.75F, 1,0xFFFFFF);//TODO Color.decode(String.format("#%02x%02x%02x", (byte) MainConfig.client.hud.interfaceColour[0], (byte) MainConfig.client.hud.interfaceColour[1], (byte) MainConfig.client.hud.interfaceColour[2])).hashCode());
				showText("LV.", 2 + (minecraft.fontRenderer.getStringWidth("LV. ") * 0.75f) + 20, sHeight / 3 + 29 + heightBase + 4, 0, 0.75f, 0.75f, 1, 0xE3D000);
				//System.out.println(playerData.getDriveFormLevel(driveForm));
				showText("" + playerData.getDriveFormLevel(driveForm), 2 * 0.75f + (minecraft.fontRenderer.getStringWidth("999") * 0.75f) + 32, sHeight / 3 + 29 + heightBase + 4, 0, 0.75f, 0.75f, 1, 0xFFFFFF);
				showText(Utils.translateToLocal(driveForm.substring(driveForm.indexOf(":")+1)), 140 - (minecraft.fontRenderer.getStringWidth(Utils.translateToLocal(driveForm.substring(driveForm.indexOf(":")+1))) * 0.75f), sHeight / 3 + 29 + heightBase + 4, 0, 0.75f, 0.75f, 1, 0xFFFFFF);
				
				
				// Half
				GL11.glColor4ub(driveColor[0], driveColor[1], driveColor[2], (byte) 255);
				GL11.glPushMatrix();
				{
					minecraft.textureManager.bindTexture(texture);
					GL11.glTranslatef(2, sHeight / 3 + 50 + heightBase, 0);
					GL11.glScalef(0.6f, heightDF, 1);
					blit(0, 0, 0, 51+36, 256, 1);
				}
				GL11.glPopMatrix();
	
				// Bottom
				GL11.glColor4ub(driveColor[0], driveColor[1], driveColor[2], (byte) 255);
				GL11.glPushMatrix();
				{
					minecraft.textureManager.bindTexture(texture);
					GL11.glTranslatef(2, sHeight / 3 + 50 + heightBase + heightDF, 0);

					//GL11.glTranslatef(2, sHeight / 3 + heightDF + (36.0f * 0.6f), 0);
					GL11.glScalef(0.6f, 0.6f, 1);
					blit(0, 0, 0, 51+37, 244, 14);
				}
				GL11.glPopMatrix();
				
				// Icon
				GL11.glColor4ub(driveColor[0], driveColor[1], driveColor[2], (byte) 255);
				GL11.glPushMatrix();
				{
					minecraft.textureManager.bindTexture(texture);
					GL11.glTranslatef(4.5F, sHeight / 3 + 34 + heightBase, 0);
					GL11.glScalef(0.6f, 0.6f, 1);
					blit(0, 0, 0, 102, 43, 36);
				}
				GL11.glPopMatrix();
	
				// Text
				GL11.glColor4ub(driveColor[0], driveColor[1], driveColor[2], (byte) 255);
				for (int i = 0; i < playerData.getDFMessages().size(); i++) {
					String message = playerData.getDFMessages().get(i).toString();
					showText(Utils.translateToLocalFormatted(message), 2 * 1f + 35, sHeight / 3 + minecraft.fontRenderer.FONT_HEIGHT * 1.1F * i + 54, 0, 0.8f, 0.8f, 1, 0xFFFFFF);
				}
				GL11.glColor4ub((byte) 255, (byte) 255, (byte) 255, (byte) 255);
			
			}
			GL11.glPopMatrix();
		}
		GL11.glPopMatrix();
		
		if (System.currentTimeMillis()/1000 > (timeDriveLevelUp + levelSeconds))
			showDriveLevelUp = false;

	}

	private void showText(String text, float tX, float tY, float tZ, float sX, float sY, float sZ, int color) {
		GL11.glPushMatrix();
		{
			GL11.glTranslatef(tX, tY, tZ);
			GL11.glScalef(sX, sY, sZ);
			drawString(minecraft.fontRenderer, text, 0, 0, color);
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
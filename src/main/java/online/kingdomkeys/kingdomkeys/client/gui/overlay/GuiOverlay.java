package online.kingdomkeys.kingdomkeys.client.gui.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class GuiOverlay extends OverlayBase {

	public static final GuiOverlay INSTANCE = new GuiOverlay();
	public static boolean showExp;
	public static boolean showMunny;
	//public static boolean showLevelUp;
	public static List<LevelUpData> levelUpList = new ArrayList<LevelUpData>();
	public static boolean showDriveLevelUp;
	//public static WorldTeleporter teleport;
	public static String driveForm = "";
	public static UUID playerWhoLevels = Util.NIL_UUID;
	public static long timeExp;
	public static long timeMunny;
	public static long timeLevelUp;
	public static long timeDriveLevelUp;
	public static int munnyGet;
	int levelSeconds = 6;

	int width;
	int sHeight;

	IPlayerCapabilities playerData;

	ResourceLocation levelUpTexture = new ResourceLocation(KingdomKeys.MODID, "textures/gui/levelup.png");
	ResourceLocation menuTexture = new ResourceLocation(KingdomKeys.MODID, "textures/gui/menu/menu_button.png");

	public static class LevelUpData{
		public String playerName; //In case player is unloaded from the client
		public UUID playerUUID;
		public int lvl;
		public int prevNotifTicks, notifTicks;
		public boolean sliding = true;
		public long timeLevelUp;
		public List<String> messages = new ArrayList<String>();
		public int color;
	}
	
	@Override
	public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int width, int height) {
		super.render(gui, guiGraphics, partialTick, width, height);
		this.width = minecraft.getWindow().getGuiScaledWidth();
		sHeight = minecraft.getWindow().getGuiScaledHeight();

		playerData = ModCapabilities.getPlayer(minecraft.player);
		if(playerData != null) {
			// Experience
			if (showExp) {
				showExp(guiGraphics);
			}

			// Munny
			if (showMunny) {
				showMunny(guiGraphics);
			}

			// Level Up
			int lvlCounter = 0;
			Iterator<LevelUpData> it = levelUpList.iterator();
			while(it.hasNext()) {
				showLevelUp(guiGraphics, partialTick, lvlCounter++);

				LevelUpData actual = it.next();
				if (System.currentTimeMillis()/1000 > (actual.timeLevelUp + levelSeconds)) {
					it.remove();
				}
			}

			// Drive form level up
			if (showDriveLevelUp) {
				showDriveLevelUp(guiGraphics, partialTick);
			}
		}
	}
	
	private void showExp(GuiGraphics gui) {
		if(playerData != null) {
			String reqExp = String.valueOf(playerData.getExpNeeded(playerData.getLevel(), playerData.getExperience()));
			drawString(gui, minecraft.font, Utils.translateToLocal(Strings.Stats_LevelNext), 5, 5, 0xFFFFFF);
			drawString(gui, minecraft.font, reqExp, 5, 5 + minecraft.font.lineHeight, 0xFFFFFF);

			if (System.currentTimeMillis()/1000 > (timeExp + 4))
				showExp = false;
		}
	}

	private void showMunny(GuiGraphics gui) {
		PoseStack matrixStack = gui.pose();
		int heightOffsetText = 0;
		int heightOffsetNum = 0;

		if (!showExp) { // If no exp is being display print it at the top
			matrixStack.pushPose();
			{
				matrixStack.translate(1, 1, 0);

				heightOffsetText = 0;
				heightOffsetNum = minecraft.font.lineHeight;
			}
			matrixStack.popPose();
		} else { // If exp is being displayed print it below it
			heightOffsetText = minecraft.font.lineHeight + 10;
			heightOffsetNum = (minecraft.font.lineHeight * 2) + 10;
		}

		drawString(guiGraphics, minecraft.font, Utils.translateToLocal(Strings.Stats_MunnyGet), 5, 5 + heightOffsetText, 0xFFFFFF);
		drawString(guiGraphics, minecraft.font, munnyGet + "", 5, 5 + heightOffsetNum, 0xFFFFFF);

		if (System.currentTimeMillis()/1000 > (timeMunny + 4))
			showMunny = false;
	}
	
	private void showLevelUp(GuiGraphics gui, float partialTick, int actual) {
		if(actual >= levelUpList.size())
			return;
		LevelUpData levelData = levelUpList.get(actual);
		if(levelData == null)
			return;

		int[] notifColor = Utils.getRGBFromDec(levelData.color);
		String name = levelData.playerName;
		int lvl = levelData.lvl;

		PoseStack matrixStack = gui.pose();		
		matrixStack.pushPose();
		{
			int totalSpace = 0;

			for(int i = 0;i<actual;i++) {
				totalSpace += 36*0.6F;

				totalSpace += (int)(minecraft.font.lineHeight * 1.2f) * (levelUpList.get(i).messages.size());
			
				totalSpace += 18*0.6F;
			}
			
			matrixStack.translate(0, totalSpace , 0);

			float notifXPos = levelData.prevNotifTicks + (levelData.notifTicks - levelData.prevNotifTicks) * partialTick;
			if(notifXPos <= -155)
				notifXPos = -155;
			
			matrixStack.translate(notifXPos + 155, 4, 0);

			int height = (int)(minecraft.font.lineHeight * 1.2f) * (levelData.messages.size());
			RenderSystem.enableBlend();
			RenderSystem.setShaderColor(notifColor[0] / 255F, notifColor[1] / 255F, notifColor[2] / 255F, 1F);

			// Top
			matrixStack.pushPose();
			{
				matrixStack.translate((width - 153.6f - 2), 0, 0);
				matrixStack.scale(0.6f, 0.6f, 1);
				blit(gui, levelUpTexture, 0, 0, 0, 0, 256, 36);
			}
			matrixStack.popPose();

			RenderSystem.setShaderColor(1, 1, 0, 1F);
			showText(matrixStack, "LEVEL UP!" + ChatFormatting.ITALIC, width - ((minecraft.font.width("LEVEL UP!")) * 0.75f) - 115, 4, 0, 0.75f, 0.75f, 1, Color.decode(String.format("#%02x%02x%02x", (byte)255,(byte)255,(byte)255)).hashCode());
			RenderSystem.setShaderColor(1, 1, 1, 1F);
			showText(matrixStack, "LV.", width - ((minecraft.font.width("LV. ")) * 0.75f) - 90, 4, 0, 0.75f, 0.75f, 1, 0xE3D000);
			showText(matrixStack, "" + lvl, width - 256.0f * 0.75f + ((minecraft.font.width("999")) * 0.75f) + 88, 4, 0, 0.75f, 0.75f, 1, 0xFFFFFF);
			showText(matrixStack, name, width - ((minecraft.font.width(name)) * 0.75f) - 7, 4, 0, 0.75f, 0.75f, 1, 0xFFFFFF);

			// Half
			RenderSystem.setShaderColor(notifColor[0] / 255F, notifColor[1] / 255F, notifColor[2] / 255F, 1F);

			matrixStack.pushPose();
			{
				matrixStack.translate((width - 256.0f * 0.6f - 2), 36.0f * 0.6f, 0);
				matrixStack.scale(0.6f, height, 1);
				blit(gui, levelUpTexture, 0, 0, 0, 36, 256, 1);
			}
			matrixStack.popPose();

			// Bottom
			RenderSystem.setShaderColor(notifColor[0] / 255F, notifColor[1] / 255F, notifColor[2] / 255F, 1F);

			matrixStack.pushPose();
			{
				matrixStack.translate((width - 256.0f * 0.6f - 2), height + (36.0f * 0.6f), 0);
				matrixStack.scale(0.6f, 0.6f, 1);
				blit(gui, levelUpTexture, 0, 0, 0, 37, 256, 14);
			}
			matrixStack.popPose();

			// Text
			RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
			for (int i = 0; i < levelData.messages.size(); i++) {
				String message = levelData.messages.get(i).toString();
				float x = (width - 256.0f * 0.8f + (minecraft.font.width("Maximum HP Increased!")) * 0.8f) - 35;
				float y = minecraft.font.lineHeight * 1.2f * i + 23;
				if(message.startsWith("A_")) {
					blit(gui, menuTexture, (int)x, (int)y-2, 74, 102, 12, 12);
					message = message.replace("A_", "");
					x += 13;
				}
				
				if(message.startsWith("S_")) {
					blit(gui, menuTexture, (int)x, (int)y-2, 100, 102, 12, 12);
					message = message.replace("S_", "");
					x += 13;
				}
				
				if(message.startsWith("M_")) {
					blit(gui, menuTexture, (int)x, (int)y-2, 87, 115, 12, 12);
					message = message.replace("M_", "");
					x += 13;
				}
				
				if(message.startsWith("C_")) {
					blit(gui, menuTexture, (int)x, (int)y-2, 87, 129, 12, 12);
					message = message.replace("C_", "");
					x += 13;
				}
				
				if(message.startsWith("R_")) {
					blit(gui, menuTexture, (int)x, (int)y-2, 101, 129, 12, 12);
					message = message.replace("R_", "");
					x += 13;
				}
				
				showText(matrixStack, Utils.translateToLocal(message), x, y, 0, 0.8f, 0.8f, 1, 0xFFFFFF);
			}
			RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
		}
		matrixStack.popPose();
		
		
	}

	private void showDriveLevelUp(GuiGraphics gui, float partialTick) {
		PoseStack matrixStack = gui.pose();
		if(playerData == null || driveForm == null)
			return;

		DriveForm drive = ModDriveForms.registry.get().getValue(new ResourceLocation(driveForm));
		float[] driveColor = drive.getDriveColor();

		matrixStack.pushPose();
		{
			float driveNotifXPos = prevDriveNotifTicks + (driveNotifTicks - prevDriveNotifTicks) * partialTick;
			if(driveNotifXPos > 155)
				driveNotifXPos = 155;
			
			matrixStack.translate(driveNotifXPos - 155, 4, 0);
			int heightBase = (int) (minecraft.font.lineHeight * 1.1F) * (playerData.getBFMessages().size());
			int heightDF = (int) (minecraft.font.lineHeight * 1.1F) * playerData.getDFMessages().size();
			RenderSystem.enableBlend();
			RenderSystem.setShaderColor(driveColor[0], driveColor[1], driveColor[2], 1F);

//Base Abilities
			matrixStack.pushPose();
			{
				// Top
				RenderSystem.setShaderColor(0.4F, 0.4F, 0.4F, 1F);
				matrixStack.pushPose();
				{
					matrixStack.translate(2, sHeight / 3, 0);
					matrixStack.scale(0.6f, 0.6f, 1);
					blit(gui, levelUpTexture, 0, 0, 0, 51, 256, 36);
				}
				matrixStack.popPose();
				
				RenderSystem.setShaderColor(1,1,1, 1F);
				showText(matrixStack, minecraft.player.getDisplayName().getString(), 140 - (minecraft.font.width(minecraft.player.getDisplayName().getString()) * 0.75f), sHeight / 3 + 4, 0, 0.75f, 0.75f, 1, 0xFFFFFF);
	
				// Half
				RenderSystem.setShaderColor(0.4F, 0.4F, 0.4F, 1F);
				matrixStack.pushPose();
				{
					matrixStack.translate(2, sHeight / 3 + 21, 0);
					matrixStack.scale(0.6f, heightBase+1, 1);
					blit(gui, levelUpTexture, 0, 0, 0, 51+36, 256, 1);
				}
				matrixStack.popPose();
	
				// Bottom
				RenderSystem.setShaderColor(0.4F, 0.4F, 0.4F, 1F);
				matrixStack.pushPose();
				{
					matrixStack.translate(2, sHeight / 3 + 22 + heightBase, 0);
					matrixStack.scale(0.6f, 0.6f, 1);
					blit(gui, levelUpTexture, 0, 0, 0, 51+37, 256, 14);
				}
				matrixStack.popPose();
				
				// Text
				RenderSystem.setShaderColor(0.4F, 0.4F, 0.4F, 1F);
				for (int i = 0; i < playerData.getBFMessages().size(); i++) {
					String message = playerData.getBFMessages().get(i);
					float x = 33;
					float y = sHeight / 3 + minecraft.font.lineHeight * 1.1F * i + 23;
					if(message.startsWith("A_")) {
						RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
						blit(gui, menuTexture, (int)x, (int)y-3, 74, 102, 12, 12);
						message = message.replace("A_", "");
						x += 13;
					}

					showText(matrixStack, Utils.translateToLocalFormatted(message), x, y, 0, 0.8f, 0.8f, 1, 0xFFFFFF);
				}
				
				// Icon
				RenderSystem.setShaderColor(0.8F, 0.8F, 0.8F, 1F);
				matrixStack.pushPose();
				{
					matrixStack.translate(4.5F, sHeight / 3+6, 0);
					matrixStack.scale(0.6f, 0.6f, 1);
					blit(gui, levelUpTexture, 0, 0, 0, 102, 43, 36);
				}
				matrixStack.popPose();

			}
			matrixStack.popPose();
			
//Form Abilities	
			matrixStack.pushPose();
			{
				// Top
				RenderSystem.setShaderColor(driveColor[0], driveColor[1], driveColor[2], 1F);
				matrixStack.pushPose();
				{
					matrixStack.translate(2, sHeight / 3 + 29 + heightBase, 0);
					matrixStack.scale(0.6f, 0.6f, 1);
					blit(gui, levelUpTexture, 0, 0, 0, 51, 256, 36);
				}
				matrixStack.popPose();
				
				String formName = Utils.translateToLocal(ModDriveForms.registry.get().getValue(new ResourceLocation(driveForm)).getTranslationKey());
				RenderSystem.setShaderColor(1,1,1, 1F);
				showText(matrixStack, "LV.", 2 + (minecraft.font.width("LV. ") * 0.75f) + 20, sHeight / 3 + 29 + heightBase + 4, 0, 0.75f, 0.75f, 1, 0xE3D000);
				showText(matrixStack, "" + playerData.getDriveFormLevel(driveForm), 2 * 0.75f + (minecraft.font.width("999") * 0.75f) + 32, sHeight / 3 + 29 + heightBase + 4, 0, 0.75f, 0.75f, 1, 0xFFFFFF);
				showText(matrixStack, formName, 140 - (minecraft.font.width(formName) * 0.75f), sHeight / 3 + 29 + heightBase + 4, 0, 0.75f, 0.75f, 1, 0xFFFFFF);
				
				// Half
				RenderSystem.setShaderColor(driveColor[0], driveColor[1], driveColor[2], 1F);
				matrixStack.pushPose();
				{
					matrixStack.translate(2, sHeight / 3 + 50 + heightBase, 0);
					matrixStack.scale(0.6f, heightDF, 1);
					blit(gui, levelUpTexture, 0, 0, 0, 51+36, 256, 1);
				}
				matrixStack.popPose();
				
				// Bottom
				RenderSystem.setShaderColor(driveColor[0], driveColor[1], driveColor[2], 1F);
				matrixStack.pushPose();
				{
					matrixStack.translate(2, sHeight / 3 + 50 + heightBase + heightDF, 0);
					matrixStack.scale(0.6f, 0.6f, 1);
					blit(gui, levelUpTexture, 0, 0, 0, 51+37, 256, 14);
				}
				matrixStack.popPose();
				
				// Text
				matrixStack.pushPose();
				{
					matrixStack.translate(0, sHeight / 3 + 50 + heightBase, 0);

					RenderSystem.setShaderColor(driveColor[0], driveColor[1], driveColor[2], 1F);
					for (int i = 0; i < playerData.getDFMessages().size(); i++) {
						String message = playerData.getDFMessages().get(i);
						
						float x = 33;
						float y = minecraft.font.lineHeight * 1.1F * i; 
						RenderSystem.setShaderColor(1F, 1F, 1F, 1F);

						if(message.startsWith("A_")) {
							blit(gui, menuTexture, (int)x, (int)y-2, 74, 102, 12, 12);
							message = message.replace("A_", "");
							x += 13;
						}
						showText(matrixStack, Utils.translateToLocalFormatted(message), x, y, 0, 0.8f, 0.8f, 1, 0xFFFFFF);
					}
					RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
				}
				matrixStack.popPose();
				
				// Icon
				RenderSystem.setShaderColor(driveColor[0], driveColor[1], driveColor[2], 1F);
				matrixStack.pushPose();
				{
					matrixStack.translate(4.5F, sHeight / 3 + 34 + heightBase, 0);
					matrixStack.scale(0.6f, 0.6f, 1);
					blit(gui, levelUpTexture, 0, 0, 0, 102, 43, 36);
				}
				matrixStack.popPose();
			}
			matrixStack.popPose();
		}
		RenderSystem.setShaderColor(1,1,1,1);

		matrixStack.popPose();
		
		if (System.currentTimeMillis()/1000 > (timeDriveLevelUp + levelSeconds))
			showDriveLevelUp = false;

	}

	private void showText(PoseStack matrixStack, String text, float tX, float tY, float tZ, float sX, float sY, float sZ, int color) {
		matrixStack.pushPose();
		{
			matrixStack.translate(tX, tY, tZ);
			matrixStack.scale(sX, sY, sZ);
			drawString(guiGraphics, minecraft.font, text, 0, 0, color);
		}
		matrixStack.popPose();
	}
	
	public static float notifTicks = 0;
	public static float prevNotifTicks = 0;
	
	public static float driveNotifTicks = 0;
	public static float prevDriveNotifTicks = 0;
	
	@SubscribeEvent
	public void ClientTick(TickEvent.ClientTickEvent event) {
		if(event.phase != Phase.END || Minecraft.getInstance().isPaused()) {
			return;
		}
		
		for(LevelUpData notif : levelUpList) {
			if(notif.sliding) {//If sliding in
				notif.prevNotifTicks = notif.notifTicks;
				notif.notifTicks -= 50;
			} else {// Stationary
				if (notif.notifTicks <= -150) {
					notif.prevNotifTicks = 0;
					notif.notifTicks = 0;
				}
			}
		}
		
		if(showDriveLevelUp) {
			prevDriveNotifTicks = driveNotifTicks;
			driveNotifTicks += 50;
		} else {
			if (driveNotifTicks > 150) {
				prevDriveNotifTicks = 0;
				driveNotifTicks = 0;
			}
		}
	}
}
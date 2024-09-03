package online.kingdomkeys.kingdomkeys.client.gui.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.util.Utils;

//TODO cleanup + comments
public class PlayerPortraitGui extends OverlayBase {

	public static final PlayerPortraitGui INSTANCE = new PlayerPortraitGui();

	private PlayerPortraitGui() {
		super();
	}

	@Override
	public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int width, int height) {
		super.render(gui, guiGraphics, partialTick, width, height);
		// if (!MainConfig.displayGUI() || !minecraft.player.getCapability(ModCapabilities.PLAYER_STATS, null).getHudMode())
		// return;
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);
		int screenWidth = minecraft.getWindow().getGuiScaledWidth();
		int screenHeight = minecraft.getWindow().getGuiScaledHeight();
		RenderSystem.setShaderColor(1, 1, 1, 1);
		ResourceLocation skin = minecraft.player.getSkinTextureLocation();
		RenderSystem.setShaderTexture(0, skin);
		float scale = 0.5f;
		switch (minecraft.options.guiScale().get()) {
			case 3 -> scale = 0.48F;
			case 2 -> scale = 0.32F;
			case 1 -> scale = 0.16F;
		}

		if (playerData != null) {
			if (playerData.getActiveDriveForm().equals(Strings.Form_Anti)) {
				RenderSystem.setShaderColor(0.2F, 0.2F, 0.2F, 1F);
			}

			if(Utils.isPlayerLowHP(minecraft.player)) {
				RenderSystem.setShaderColor(1F, 0.5F, 0.5F, 1F);
			}

			PoseStack poseStack = guiGraphics.pose();

			poseStack.pushPose();
			{
				//3D render
				float playerHeight = (height/2F) * scale;
				float playerPosX = width * (1-(scale/10));
				float playerPosY = height + (playerHeight*0.95F);
				poseStack.pushPose();
				{
					if(minecraft.player == null)
						return;

					Player player = Minecraft.getInstance().player;
					ItemStack stack = player.getInventory().getItem(player.getInventory().selected);
					player.getInventory().setItem(player.getInventory().selected, new ItemStack(Items.AIR));
					ClientUtils.renderPlayerNoAnims(poseStack, (int) playerPosX + ModConfigs.playerSkinXPos, (int) playerPosY+ ModConfigs.playerSkinYPos, (int) playerHeight, 0,0, player);
					player.getInventory().setItem(player.getInventory().selected, stack);
				}
				poseStack.popPose();
				
				
				//2D render
				/*poseStack.translate(-5 + ModConfigs.playerSkinXPos, -1 + ModConfigs.playerSkinYPos, 0);

				// HEAD
				int headWidth = 32;
				int headHeight = 32;
				float headPosX = 16;
				float headPosY = 32;
				float scaledHeadPosX = headPosX * scale;
				float scaledHeadPosY = headPosY * scale;

				poseStack.pushPose();
				{
					poseStack.translate((screenWidth - headWidth * scale) - scaledHeadPosX, (screenHeight - headHeight * scale) - scaledHeadPosY, 0);
					poseStack.scale(scale, scale, scale);
					this.blit(poseStack, 0, 0, 32, 32, headWidth, headHeight);
				}
				poseStack.popPose();

				// HAT
				if(minecraft.options.isModelPartEnabled(PlayerModelPart.HAT)){
					int hatWidth = 32;
					int hatHeight = 32;
					float hatPosX = 16;
					float hatPosY = 32;
					float scaledHatPosX = hatPosX * scale;
					float scaledHatPosY = hatPosY * scale;

					poseStack.pushPose();
					{
						poseStack.translate((screenWidth - hatWidth * scale) - scaledHatPosX, (screenHeight - hatHeight * scale) - scaledHatPosY, 0);
						poseStack.scale(scale, scale, scale);
						this.blit(poseStack, 0, 0, 160, 32, hatWidth, hatHeight);
					}
					poseStack.popPose();
				}
				// BODY
				int bodyWidth = 32;
				int bodyHeight = 64;
				float bodyPosX = 16;
				float bodyPosY = -32;
				float scaledBodyPosX = bodyPosX * scale;
				float scaledBodyPosY = bodyPosY * scale;

				poseStack.pushPose();
				{
					poseStack.translate((screenWidth - bodyWidth * scale) - scaledBodyPosX, (screenHeight - bodyHeight * scale) - scaledBodyPosY, 0);
					poseStack.scale(scale, scale, scale);
					this.blit(poseStack, 0, 0, 80, 80, bodyWidth, bodyHeight);
				}
				poseStack.popPose();

				// JACKET
				if(minecraft.options.isModelPartEnabled(PlayerModelPart.JACKET)){
					poseStack.pushPose();
					{
						poseStack.translate((screenWidth - bodyWidth * scale) - scaledBodyPosX, (screenHeight - bodyHeight * scale) - scaledBodyPosY, 0);
						poseStack.scale(scale, scale, scale);
						this.blit(poseStack, 0, 0, 80, 148, bodyWidth, bodyHeight);
					}
					poseStack.popPose();
				}
				// ARMS
				int armWidth = 16;
				int armHeight = 64;
				float armRPosX = 48;
				float armRPosY = -32;
				float scaledArmRPosX = armRPosX * scale;
				float scaledArmRPosY = armRPosY * scale;
				float armLPosX = 0;
				float armLPosY = -32;
				float scaledArmLPosX = armLPosX * scale;
				float scaledArmLPosY = armLPosY * scale;
				poseStack.pushPose();
				{
					poseStack.translate((screenWidth - armWidth * scale) - scaledArmRPosX, (screenHeight - armHeight * scale) - scaledArmRPosY, 0);
					poseStack.scale(scale, scale, scale);
					this.blit(poseStack, 0, 0, 176, 80, armWidth, armHeight);
				}
				poseStack.popPose();


				poseStack.pushPose();
				{
					poseStack.translate((screenWidth - armWidth * scale) - scaledArmLPosX, (screenHeight - armHeight * scale) - scaledArmLPosY, 0);
					poseStack.scale(scale, scale, scale);
					this.blit(poseStack, 0, 0, 176, 80, armWidth, armHeight);
				}
				poseStack.popPose();

				RenderSystem.setShaderColor(100.0F, 1.0F, 1.0F, 1.0F);

				// GLOVES
				int gloveWidth = 16;
				int gloveHeight = 64;
				float gloveRPosX = 48;
				float gloveRPosY = -32;
				float scaledgloveRPosX = gloveRPosX * scale;
				float scaledgloveRPosY = gloveRPosY * scale;
				float gloveLPosX = 0;
				float gloveLPosY = -32;
				float scaledgloveLPosX = gloveLPosX * scale;
				float scaledgloveLPosY = gloveLPosY * scale;

				if(minecraft.options.isModelPartEnabled(PlayerModelPart.RIGHT_SLEEVE)){
					poseStack.pushPose();
					{
						poseStack.translate((screenWidth - gloveWidth * scale) - scaledgloveRPosX, (screenHeight - gloveHeight * scale) - scaledgloveRPosY, 0);
						poseStack.scale(scale, scale, scale);
						this.blit(poseStack, 0, 0, 176, 150, gloveWidth, gloveHeight);
					}
					poseStack.popPose();
				}

				if(minecraft.options.isModelPartEnabled(PlayerModelPart.LEFT_SLEEVE)){
					poseStack.pushPose();
					{
						poseStack.translate((screenWidth - gloveWidth * scale) - scaledgloveLPosX, (screenHeight - gloveHeight * scale) - scaledgloveLPosY, 0);
						poseStack.scale(scale, scale, scale);
						this.blit(poseStack, 0, 0, 176, 150, gloveWidth, gloveHeight);
					}
					poseStack.popPose();
				}
				RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

				if (!playerData.getActiveDriveForm().equals(DriveForm.NONE.toString()) && !playerData.getActiveDriveForm().equals(Strings.Form_Anti)) {
					String driveName = playerData.getActiveDriveForm().substring(playerData.getActiveDriveForm().indexOf("_") + 1);
					ResourceLocation texture = new ResourceLocation(KingdomKeys.MODID, "textures/models/armor/" + driveName + ".png");
					RenderSystem.setShaderTexture(0, texture);
					RenderSystem.setShaderColor(1, 1, 1, 1);

					poseStack.pushPose();
					{
						poseStack.translate((screenWidth - 32 * scale) - 16 * scale, (screenHeight - 80 * scale) - -48 * scale, 0);
						poseStack.scale(2, 1, 1);
						poseStack.scale(0.5f, 0.5f, 0.5f);
						poseStack.scale(scale, scale, scale);
						this.blit(poseStack, 0, 0, 80, 140, 32, 80);
					}
					poseStack.popPose();

					poseStack.pushPose();
					{
						poseStack.translate((screenWidth - 16 * scale) - 48 * scale, (screenHeight - 80 * scale) - -48 * scale, 0);
						poseStack.scale(2, 1, 1);
						poseStack.scale(0.5f, 0.5f, 0.5f);
						poseStack.scale(scale, scale, scale);
						this.blit(poseStack, 0, 0, 64, 140, 16, 80);
					}
					poseStack.popPose();

					poseStack.pushPose();
					{
						poseStack.translate((screenWidth - 16 * scale) - 0 * scale, (screenHeight - 80 * scale) - -48 * scale, 0);
						poseStack.scale(2, 1, 1);
						poseStack.scale(0.5f, 0.5f, 0.5f);
						poseStack.scale(scale, scale, scale);
						this.blit(poseStack, 0, 0, 112, 140, 16, 80);
					}
					poseStack.popPose();

				}*/
			}
			poseStack.popPose();
		}
	}
}

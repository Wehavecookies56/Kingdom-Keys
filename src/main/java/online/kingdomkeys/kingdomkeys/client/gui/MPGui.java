package online.kingdomkeys.kingdomkeys.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.lib.Constants;

//TODO cleanup + comments
public class MPGui extends Screen {
	int guiWidth = 173;
	int mpBarWidth;
	int guiHeight = 10;
	int noborderguiwidth = 171;
	IPlayerCapabilities playerData;
	int counter = 0;

	public MPGui() {
		super(new TranslationTextComponent(""));
		minecraft = Minecraft.getInstance();
	}

	@SubscribeEvent
	public void onRenderOverlayPost(RenderGameOverlayEvent event) {
		// if (!MainConfig.displayGUI() || !player.getCapability(ModCapabilities.PLAYER_STATS, null).getHudMode())
		// return;
		PlayerEntity player = minecraft.player;
		MatrixStack matrixStack = event.getMatrixStack();

		if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {
			minecraft.textureManager.bindTexture(new ResourceLocation(KingdomKeys.MODID, "textures/gui/mpbar.png"));

			int screenWidth = minecraft.getMainWindow().getScaledWidth();
			int screenHeight = minecraft.getMainWindow().getScaledHeight();

			float scale = 1f;
			switch (minecraft.gameSettings.guiScale) {
			case Constants.SCALE_AUTO:
				scale = 0.85F;
				break;
			}
			float scaleactor = 1F;
			playerData = ModCapabilities.getPlayer(player);
			if(playerData == null || playerData.getMaxMP() <= 0)
				return;
			
			mpBarWidth = (int) (playerData.getMP() * scaleactor);
			int mpBarMaxWidth = (int) (playerData.getMaxMP() * scaleactor);
			matrixStack.push();// MP Background
			{
				RenderSystem.enableBlend();
				matrixStack.translate(ModConfigs.mpXPos, ModConfigs.mpYPos, 0);

				matrixStack.push();// MP Background
				{
					matrixStack.translate((screenWidth - mpBarMaxWidth * scale) - 80 * scale, (screenHeight - guiHeight * scale) - 9 * scale, 0);
					matrixStack.scale(scale, scale / 1.3F, scale);
					drawMPBarBack(matrixStack, 0, 0, mpBarMaxWidth, scale);
				}
				matrixStack.pop();
				
				matrixStack.push();// MP Bar
				{
					matrixStack.translate((screenWidth - ((int) mpBarWidth) * scale) - 80 * scale, (screenHeight - (guiHeight) * scale) - 9 * scale, 0);
					matrixStack.scale(scale, scale / 1.3F, scale);
					drawMPBarTop(matrixStack, 0, 0, (int) Math.ceil(mpBarWidth), scale);
				}
				matrixStack.pop();
				RenderSystem.disableBlend();
			}
			matrixStack.pop();
		}
	}

	public void drawMPBarBack(MatrixStack matrixStack, int posX, int posY, int width, float scale) {
		minecraft.textureManager.bindTexture(new ResourceLocation(KingdomKeys.MODID, "textures/gui/mpbar.png"));
		matrixStack.push();
		{
			// Left Margin
			matrixStack.push();
			{
				matrixStack.translate(scale * posX, scale * posY, 0);
				matrixStack.scale(scale, scale, 0);
				blit(matrixStack, 0, 0, 0, 0, 2, 12);
			}
			matrixStack.pop();

			// Background
			matrixStack.push();
			{
				matrixStack.translate((posX + 2) * scale, posY * scale, 0);
				matrixStack.scale(width, scale, 0);
				int v = playerData.getRecharge() ? 8 : 2;
				blit(matrixStack, 0, 0, v, 0, 1, 12);

			}
			matrixStack.pop();

			// Right Margin
			matrixStack.push();
			{
				matrixStack.translate((posX + 2) * scale + width, scale * posY, 0);
				matrixStack.scale(scale, scale, 0);
				blit(matrixStack, 0, 0, 3, 0, 2, 12);
			}
			matrixStack.pop();

			// MP Icon
			matrixStack.push();
			{
				int v = playerData.getRecharge() ? 45 : 32;
				matrixStack.translate((posX + 2) * scale + width + 1, scale * posY, 0);
				matrixStack.scale(scale * 0.8F, scale, 1);
				blit(matrixStack, 0, 0, 0, v, 23, 12);
			}
			matrixStack.pop();
		}
		matrixStack.pop();

	}

	public void drawMPBarTop(MatrixStack matrixStack, int posX, int posY, int width, float scale) {
		minecraft.textureManager.bindTexture(new ResourceLocation(KingdomKeys.MODID, "textures/gui/mpbar.png"));
		matrixStack.push();
		{
			matrixStack.translate((posX + 2) * scale, (posY + 2) * scale, 0);
			matrixStack.scale(width, scale, 0);
			int v = playerData.getRecharge() ? 22 : 12;
			blit(matrixStack, 0, 0, 2, v, 1, 8);
		}
		matrixStack.pop();

	}
}

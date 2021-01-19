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
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.lib.Constants;

//TODO cleanup + comments
public class HPGui extends Screen {
	int hpBarWidth;
	int guiHeight = 10;

	int counter = 0;

	public HPGui() {
		super(new TranslationTextComponent(""));
		minecraft = Minecraft.getInstance();
	}

	@SubscribeEvent
	public void onRenderOverlayPost(RenderGameOverlayEvent event) {
		// if (!MainConfig.displayGUI() || !player.getCapability(ModCapabilities.PLAYER_STATS, null).getHudMode())
		// return;
		PlayerEntity player = minecraft.player;
		MatrixStack matrixStack = event.getMatrixStack();
		if (event.getType().equals(RenderGameOverlayEvent.ElementType.HEALTH) && event.isCancelable()) {
			if (!ModConfigs.hpShowHearts) {
				event.setCanceled(true);
			}
		}
		if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {
			minecraft.textureManager.bindTexture(new ResourceLocation(KingdomKeys.MODID, "textures/gui/hpbar.png"));
			int screenWidth = minecraft.getMainWindow().getScaledWidth();
			int screenHeight = minecraft.getMainWindow().getScaledHeight();

			float scale = 1f;
			switch (minecraft.gameSettings.guiScale) {
			case Constants.SCALE_AUTO:
				scale = 0.85F;
				break;
			}
			float scaleFactor = 1.5F;

			hpBarWidth = (int) (player.getHealth() * scaleFactor);
			int hpBarMaxWidth = (int) (player.getMaxHealth() * scaleFactor);

			matrixStack.push();
			{
				RenderSystem.enableBlend();
				matrixStack.translate(ModConfigs.hpXPos, ModConfigs.hpYPos, 0);
				matrixStack.push();
				{
					matrixStack.translate((screenWidth - hpBarMaxWidth * scale) - 8 * scale, (screenHeight - guiHeight * scale) - 2 * scale, 0);
					matrixStack.scale(scale, scale, scale);
					drawHPBarBack(matrixStack, 0, 0, hpBarMaxWidth, scale);
				}
				matrixStack.pop();
	
				matrixStack.push();
				{
					matrixStack.translate((screenWidth - (hpBarWidth) * scale) - 8 * scale, (screenHeight - (guiHeight) * scale) - 1 * scale - 0.1F, 0);
					matrixStack.scale(scale, scale, scale);
					drawHPBarTop(matrixStack, 0, 0, (int) Math.ceil(hpBarWidth), scale, player);
				}
				matrixStack.pop();
				RenderSystem.disableBlend();
			}
			matrixStack.pop();
		}
	}

	public void drawHPBarBack(MatrixStack matrixStack, int posX, int posY, int width, float scale) {
		minecraft.textureManager.bindTexture(new ResourceLocation(KingdomKeys.MODID, "textures/gui/hpbar.png"));
		matrixStack.push();
		{
			// Left
			matrixStack.push();
			{
				matrixStack.translate(scale * posX, scale * posY, 0);
				matrixStack.scale(scale, scale, 0);
				blit(matrixStack, 0, 0, 0, 0, 2, 12);
			}
			matrixStack.pop();

			// Middle
			matrixStack.push();
			{
				matrixStack.translate((posX + 2) * scale, posY * scale, 0);
				matrixStack.scale(width, scale, 0);
				blit(matrixStack, 0, 0, 2, 0, 1, 12);
			}
			matrixStack.pop();

			// Right
			matrixStack.push();
			{
				matrixStack.translate((posX + 2) * scale + width, scale * posY, 0);
				matrixStack.scale(scale, scale, 0);
				blit(matrixStack, 0, 0, 3, 0, 2, 12);
			}
			matrixStack.pop();
		}
		matrixStack.pop();

	}

	public void drawHPBarTop(MatrixStack matrixStack, int posX, int posY, int width, float scale, PlayerEntity player) {
		minecraft.textureManager.bindTexture(new ResourceLocation(KingdomKeys.MODID, "textures/gui/hpbar.png"));
		matrixStack.push();
		{
			matrixStack.translate((posX + 2) * scale, (posY + 2) * scale, 0);
			matrixStack.scale(width, scale, 0);
			int v = player.getHealth() >= player.getMaxHealth() / 4 ? 12 : 22;
			blit(matrixStack, 0, -1, 2, v, 1, 8);
		}
		matrixStack.pop();

	}

}

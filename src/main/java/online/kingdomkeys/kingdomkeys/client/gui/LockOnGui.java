package online.kingdomkeys.kingdomkeys.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.handler.InputHandler;
import online.kingdomkeys.kingdomkeys.lib.Strings;

public class LockOnGui extends Screen {
	int guiWidth = 256;
	int guiHeight = 256;

	int hpGuiWidth = 173;
	float hpBarWidth;
	int hpPerBar;
	int hpBars;
	int currentBar;

	int hpGuiHeight = 10;
	int noborderguiwidth = 171;

	float scale;

	public LockOnGui() {
		super(new TranslationTextComponent(""));
		minecraft = Minecraft.getInstance();
	}

	@SubscribeEvent
	public void onRenderOverlayPost(RenderGameOverlayEvent event) {
		PlayerEntity player = minecraft.player;
		// if
		// (!Minecraft.getInstance().player.getCapability(ModCapabilities.PLAYER_STATS,
		// null).getHudMode())
		// return;
		MatrixStack matrixStack = event.getMatrixStack();
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
		if (playerData != null) {
			Entity target = InputHandler.lockOn;
			if (target == null) {
				return;
			} else {
				if (event.getType() == RenderGameOverlayEvent.ElementType.CROSSHAIRS) {
					event.setCanceled(true);
				}
				
				if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {
					float size = 6;

					minecraft.textureManager.bindTexture(new ResourceLocation(KingdomKeys.MODID, "textures/gui/lockon_0.png"));

					int screenWidth = minecraft.getMainWindow().getScaledWidth();
					int screenHeight = minecraft.getMainWindow().getScaledHeight();

					scale = 0.75F;

					// Icon
					matrixStack.push();
					{
						matrixStack.translate((screenWidth / 2) - (guiWidth / 2) * scale / size - 0.5F, (screenHeight / 2) - (guiHeight / 2) * scale / size - 0.5F, 0);
						matrixStack.scale(scale / size, scale / size, scale / size);
						this.blit(matrixStack, 0, 0, 0, 0, guiWidth, guiHeight);

						minecraft.textureManager.bindTexture(new ResourceLocation(KingdomKeys.MODID, "textures/gui/lockon_1.png"));
						matrixStack.translate(guiWidth / 2, guiWidth / 2, 0);
						matrixStack.rotate(Vector3f.ZP.rotation(player.ticksExisted % 360 * 10));
						matrixStack.translate(-guiWidth / 2, -guiWidth / 2, 0);
						this.blit(matrixStack, 0, 0, 0, 0, guiWidth, guiHeight);
					}
					matrixStack.pop();

					minecraft.textureManager.bindTexture(new ResourceLocation(KingdomKeys.MODID, "textures/gui/hpbar.png"));

					matrixStack.push();

					int[] scan = playerData.getEquippedAbilityLevel(Strings.scan);
					// If ability level > 0 and amount of equipped is > 0
					if (target != null && scan[0] > 0 && scan[1] > 0) {
						drawString(matrixStack, minecraft.fontRenderer, target.getName().getString(), screenWidth - minecraft.fontRenderer.getStringWidth(target.getName().getString()), 15, 0xFFFFFF);
						drawHPBar(event, (LivingEntity) target);
					}

					matrixStack.scale(scale, scale, scale);
					RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
					matrixStack.pop();
				}
			}
		}
	}

	public void drawHPBar(RenderGameOverlayEvent event, LivingEntity target) {
		Minecraft mc = Minecraft.getInstance();
		int screenWidth = minecraft.getMainWindow().getScaledWidth();
		MatrixStack matrixStack = event.getMatrixStack();

		//int screenHeight = minecraft.getMainWindow().getScaledHeight();
		// if (!Minecraft.getInstance().player.getCapability(ModCapabilities.PLAYER_STATS, null).getHudMode())
		// return;
		if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {
			minecraft.textureManager.bindTexture(new ResourceLocation(KingdomKeys.MODID, "textures/gui/hpbar.png"));

			float oneHeart = (noborderguiwidth / target.getMaxHealth());
			//int currHealth = noborderguiwidth - (int) (oneHeart * target.getHealth());

			hpPerBar = 20;
			int widthMultiplier = 10;

			if (target.getMaxHealth() % hpPerBar == 0) {
				hpBars = (int) (target.getMaxHealth() / hpPerBar);
			} else {
				hpBars = (int) (target.getMaxHealth() / hpPerBar) + 1;
			}

			if (target.getHealth() % hpPerBar == 0) {
				currentBar = (int) (target.getHealth() / hpPerBar);
			} else {
				currentBar = (int) (target.getHealth() / hpPerBar) + 1;
			}

			int oneBar = (int) (target.getMaxHealth() > hpPerBar ? hpPerBar : target.getMaxHealth());// (int) (target.getMaxHealth() / hpBars);

			if (target.getHealth() % hpPerBar == 0 && target.getHealth() != 0) {
				hpBarWidth = oneBar * widthMultiplier;
			} else {
				hpBarWidth = (float) (Math.ceil(target.getHealth() % hpPerBar) * widthMultiplier);
			}

			int hpBarMaxWidth;
			// Background HP width
			if (target.getMaxHealth() >= hpPerBar) {
				hpBarMaxWidth = oneBar * widthMultiplier;
			} else {
				hpBarMaxWidth = (int) (Math.ceil(target.getMaxHealth() % hpPerBar) * widthMultiplier);
			}

			// System.out.println(target.getHealth());
			matrixStack.push();
			{
				matrixStack.translate((screenWidth - hpBarMaxWidth * scale) - 2 * scale * 2, 1, 0);
				matrixStack.scale(scale, scale, scale);
				drawHPBarBack(matrixStack, 0, 0, hpBarMaxWidth, scale);
			}
			matrixStack.pop();
			matrixStack.push();
			{
				matrixStack.translate((screenWidth - (hpBarWidth) * scale) - 2 * scale * 2, 1, 0);
				matrixStack.scale(scale, scale, scale);
				drawHPBarTop(matrixStack, 0, 0, (int) Math.ceil(hpBarWidth), scale);
			}
			matrixStack.pop();
		}
	}

	public void drawHPBarBack(MatrixStack matrixStack, int posX, int posY, int width, float scale) {
		minecraft.textureManager.bindTexture(new ResourceLocation(KingdomKeys.MODID, "textures/gui/hpbar.png"));
		
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
				blit(matrixStack, 0, 0, 2, 0, 1, 12);
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

			// HP Icon
			matrixStack.push();
			{
				matrixStack.translate(posX + width - 14, posY + 9, 0);
				matrixStack.scale(scale, scale, 0);
				blit(matrixStack, 1, 0, 0, 32, 23, 12);
			}
			matrixStack.pop();

			// HP Bars
			for (int i = 0; i < hpBars - 1; i++) {
				matrixStack.push();
				{
					matrixStack.translate(posX + width - 14 - (11 * (i + 1)), posY + 9, 0);
					matrixStack.scale(scale, scale, 0);
					blit(matrixStack, 0, 0, 0, 46, 17, 12);
				}
				matrixStack.pop();
			}
		}
		matrixStack.pop();

	}

	public void drawHPBarTop(MatrixStack matrixStack, int posX, int posY, int width, float scale) {
		Minecraft.getInstance().textureManager.bindTexture(new ResourceLocation(KingdomKeys.MODID, "textures/gui/hpbar.png"));
		matrixStack.push();
		{
			// HP Bar
			matrixStack.push();
			{
				matrixStack.translate((posX + 2) * scale, (posY + 2) * scale, 0);
				matrixStack.scale(width, scale, 0);
				blit(matrixStack, 0, 0, 2, 12, 1, 8);
			}
			matrixStack.pop();

			// HP Bars
			for (int i = 0; i < currentBar - 1; i++) {
				matrixStack.push();
				{
					matrixStack.translate(posX + width - 14 - (11 * (i + 1)), posY + 9, 0);
					matrixStack.scale(scale, scale, 0);
					blit(matrixStack, 2, 2, 2, 62, 16, 8);
				}
				matrixStack.pop();
			}
		}
		matrixStack.pop();
	}
}

package online.kingdomkeys.kingdomkeys.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
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
	}

	@SubscribeEvent
	public void onRenderOverlayPost(RenderGameOverlayEvent event) {
		Minecraft mc = Minecraft.getInstance();
		PlayerEntity player = mc.player;
		// if
		// (!Minecraft.getInstance().player.getCapability(ModCapabilities.PLAYER_STATS,
		// null).getHudMode())
		// return;
		IPlayerCapabilities props = ModCapabilities.get(player);
		if (props != null) {
			Entity target = InputHandler.lockOn;
			if (target == null) {
				return;
			} else {
				if (event.getType() == RenderGameOverlayEvent.ElementType.CROSSHAIRS) {
					event.setCanceled(true);
				}
				
				if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {
					float size = 6;

					mc.textureManager.bindTexture(new ResourceLocation(KingdomKeys.MODID, "textures/gui/lockon_0.png"));

					int screenWidth = mc.getMainWindow().getScaledWidth();
					int screenHeight = mc.getMainWindow().getScaledHeight();

					scale = 0.75F;

					// Icon
					RenderSystem.pushMatrix();
					{
						RenderSystem.translatef((screenWidth / 2) - (guiWidth / 2) * scale / size - 0.5F, (screenHeight / 2) - (guiHeight / 2) * scale / size - 0.5F, 0);
						RenderSystem.scalef(scale / size, scale / size, scale / size);
						this.blit(0, 0, 0, 0, guiWidth, guiHeight);

						mc.textureManager.bindTexture(new ResourceLocation(KingdomKeys.MODID, "textures/gui/lockon_1.png"));
						RenderSystem.translated(guiWidth / 2, guiWidth / 2, 0);
						RenderSystem.rotatef(player.ticksExisted % 360 * 10, 0, 0, 1);
						RenderSystem.translated(-guiWidth / 2, -guiWidth / 2, 0);
						this.blit(0, 0, 0, 0, guiWidth, guiHeight);
					}
					RenderSystem.popMatrix();

					mc.textureManager.bindTexture(new ResourceLocation(KingdomKeys.MODID, "textures/gui/hpbar.png"));

					RenderSystem.pushMatrix();

					int[] scan = props.getEquippedAbilityLevel(Strings.scan);
					// If ability level > 0 and amount of equipped is > 0
					if (target != null && scan[0] > 0 && scan[1] > 0) {
						this.drawString(mc.fontRenderer, target.getName().getFormattedText(), screenWidth - mc.fontRenderer.getStringWidth(target.getName().getFormattedText()), 15, 0xFFFFFF);
						drawHPBar(event, (LivingEntity) target);
					}

					RenderSystem.scalef(scale, scale, scale);
					RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
					RenderSystem.popMatrix();
				}
			}
		}
	}

	public void drawHPBar(RenderGameOverlayEvent event, LivingEntity target) {
		Minecraft mc = Minecraft.getInstance();
		int screenWidth = mc.getMainWindow().getScaledWidth();
		int screenHeight = mc.getMainWindow().getScaledHeight();
		// if (!Minecraft.getInstance().player.getCapability(ModCapabilities.PLAYER_STATS, null).getHudMode())
		// return;
		if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {
			mc.textureManager.bindTexture(new ResourceLocation(KingdomKeys.MODID, "textures/gui/hpbar.png"));

			float oneHeart = (noborderguiwidth / target.getMaxHealth());
			int currHealth = noborderguiwidth - (int) (oneHeart * target.getHealth());

			hpPerBar = 10;
			int widthMultiplier = 20;

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
			RenderSystem.pushMatrix();
			{
				RenderSystem.translatef((screenWidth - hpBarMaxWidth * scale) - 2 * scale * 2, 1, 0);
				RenderSystem.scalef(scale, scale, scale);
				drawHPBarBack(0, 0, hpBarMaxWidth, scale);
			}
			RenderSystem.popMatrix();
			RenderSystem.pushMatrix();
			{
				RenderSystem.translatef((screenWidth - (hpBarWidth) * scale) - 2 * scale * 2, 1, 0);
				RenderSystem.scalef(scale, scale, scale);
				drawHPBarTop(0, 0, (int) Math.ceil(hpBarWidth), scale);
			}
			RenderSystem.popMatrix();
		}
	}

	public void drawHPBarBack(int posX, int posY, int width, float scale) {
		Minecraft.getInstance().textureManager.bindTexture(new ResourceLocation(KingdomKeys.MODID, "textures/gui/hpbar.png"));
		RenderSystem.pushMatrix();
		{
			// Left Margin
			RenderSystem.pushMatrix();
			{
				RenderSystem.translatef(scale * posX, scale * posY, 0);
				RenderSystem.scalef(scale, scale, 0);
				blit(0, 0, 0, 0, 2, 12);
			}
			RenderSystem.popMatrix();

			// Background
			RenderSystem.pushMatrix();
			{
				RenderSystem.translatef((posX + 2) * scale, posY * scale, 0);
				RenderSystem.scalef(width, scale, 0);
				blit(0, 0, 2, 0, 1, 12);
			}
			RenderSystem.popMatrix();

			// Right Margin
			RenderSystem.pushMatrix();
			{
				RenderSystem.translatef((posX + 2) * scale + width, scale * posY, 0);
				RenderSystem.scalef(scale, scale, 0);
				blit(0, 0, 3, 0, 2, 12);
			}
			RenderSystem.popMatrix();

			// HP Icon
			RenderSystem.pushMatrix();
			{
				RenderSystem.translatef(posX + width - 14, posY + 9, 0);
				RenderSystem.scalef(scale, scale, 0);
				blit(1, 0, 0, 32, 23, 12);
			}
			RenderSystem.popMatrix();

			// HP Bars
			for (int i = 0; i < hpBars - 1; i++) {
				RenderSystem.pushMatrix();
				{
					RenderSystem.translatef(posX + width - 14 - (11 * (i + 1)), posY + 9, 0);
					RenderSystem.scalef(scale, scale, 0);
					blit(0, 0, 0, 46, 17, 12);
				}
				RenderSystem.popMatrix();
			}
		}
		RenderSystem.popMatrix();

	}

	public void drawHPBarTop(int posX, int posY, int width, float scale) {
		Minecraft.getInstance().textureManager.bindTexture(new ResourceLocation(KingdomKeys.MODID, "textures/gui/hpbar.png"));
		RenderSystem.pushMatrix();
		{
			// HP Bar
			RenderSystem.pushMatrix();
			{
				RenderSystem.translatef((posX + 2) * scale, (posY + 2) * scale, 0);
				RenderSystem.scalef(width, scale, 0);
				blit(0, 0, 2, 12, 1, 8);
			}
			RenderSystem.popMatrix();

			// HP Bars
			for (int i = 0; i < currentBar - 1; i++) {
				RenderSystem.pushMatrix();
				{
					RenderSystem.translatef(posX + width - 14 - (11 * (i + 1)), posY + 9, 0);
					RenderSystem.scalef(scale, scale, 0);
					blit(2, 2, 2, 62, 16, 12);
				}
				RenderSystem.popMatrix();
			}
		}
		RenderSystem.popMatrix();
	}
}

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
	int BarHP;
	int hpBars;
	int currentBar;

	int hpGuiHeight = 10;
	int noborderguiwidth = 171;

	int max = 23;
	int i = max;
	int multiplier = 4;
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
		if(props != null) {
			if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {
				Entity target = InputHandler.lockOn;
				if (target == null)
					return;
				float size = 6;

				mc.textureManager.bindTexture(new ResourceLocation(KingdomKeys.MODID, "textures/gui/lockon.png"));

				i--;

				if (i <= 0)
					i = max * multiplier;

				int screenWidth = mc.getMainWindow().getScaledWidth();
				int screenHeight = mc.getMainWindow().getScaledHeight();

				scale = 0.75F;

				// Icon
				if (target != null) {
					RenderSystem.pushMatrix();
					{
						RenderSystem.translatef((screenWidth / 2) - (guiWidth / 2) * scale / size, (screenHeight / 2) - (guiHeight / 2) * scale / size, 0);
						RenderSystem.scalef(scale / size, scale / size, scale / size);
					//	RenderSystem.rotatef(player.ticksExisted % 360 * 10,0,0,1);
						this.blit(-5,5, 0, 0, guiWidth, guiHeight);
					}
					RenderSystem.popMatrix();
				}

				mc.textureManager.bindTexture(new ResourceLocation(KingdomKeys.MODID, "textures/gui/hpbar.png"));

				RenderSystem.pushMatrix();
				
				int[] scan = props.getEquippedAbilityLevel(Strings.scan);
				//If ability level > 0 and amount of equipped is > 0
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

	public void drawHPBar(RenderGameOverlayEvent event, LivingEntity target) {
		Minecraft mc = Minecraft.getInstance();
		int screenWidth = mc.getMainWindow().getScaledWidth();
		int screenHeight = mc.getMainWindow().getScaledHeight();
		// if
		// (!Minecraft.getInstance().player.getCapability(ModCapabilities.PLAYER_STATS,
		// null).getHudMode())
		// return;
		if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {
			mc.textureManager.bindTexture(new ResourceLocation(KingdomKeys.MODID, "textures/gui/hpbar.png"));

			float oneHeart = (noborderguiwidth / target.getMaxHealth());
			int currHealth = noborderguiwidth - (int) (oneHeart * target.getHealth());

			BarHP = 25;

			// If the max health is not divisible by BarHP reduce it
			while (target.getMaxHealth() % BarHP != 0) {
				BarHP--;
				if (BarHP == 1) {// If it's not divisible by 0 set it to the entity health
					BarHP = (int) target.getMaxHealth();
					break;
				}
			}

			// Number of HP bars (returns 1 more but it gets removed after)
			hpBars = (int) target.getMaxHealth() / BarHP;

			// Current HP Bar
			currentBar = (int) target.getHealth() / BarHP;

			if (target.getHealth() % BarHP != 0) // If there is module it will have it's hp bar
				currentBar++;

			int oneBar = (int) (target.getMaxHealth() / hpBars);

			if (target.getMaxHealth() == target.getHealth()) {
				hpBarWidth = oneBar * 10;
			} else {
				hpBarWidth = (float) (Math.ceil(target.getHealth() % oneBar) * 10);
			}

			// Background HP width
			int hpBarMaxWidth = (int) (target.getMaxHealth() * 10 / hpBars);

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
				blit(0, 0, 0, 0, 2, 10);
			}
			RenderSystem.popMatrix();

			// Background
			RenderSystem.pushMatrix();
			{
				RenderSystem.translatef((posX + 2) * scale, posY * scale, 0);
				RenderSystem.scalef(width, scale, 0);
				blit(0, 0, 2, 0, 1, 10);
			}
			RenderSystem.popMatrix();

			// Right Margin
			RenderSystem.pushMatrix();
			{
				RenderSystem.translatef((posX + 2) * scale + width, scale * posY, 0);
				RenderSystem.scalef(scale, scale, 0);
				blit(0, 0, 3, 0, 2, 10);
			}
			RenderSystem.popMatrix();

			// HP Icon
			RenderSystem.pushMatrix();
			{
				RenderSystem.translatef(posX + width - 14, posY + 7, 0);
				RenderSystem.scalef(scale, scale, 0);
				blit(1, 0, 0, 32, 23, 12);
			}
			RenderSystem.popMatrix();

			// HP Bars
			for (int i = 0; i < hpBars - 1; i++) {
				RenderSystem.pushMatrix();
				{
					RenderSystem.translatef(posX + width - 14 - (11 * (i + 1)), posY + 7, 0);
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
				blit(0, 0, 2, 12, 1, 6);
			}
			RenderSystem.popMatrix();

			// HP Bars
			for (int i = 0; i < currentBar - 1; i++) {
				RenderSystem.pushMatrix();
				{
					RenderSystem.translatef(posX + width - 14 - (11 * (i + 1)), posY + 7, 0);
					RenderSystem.scalef(scale, scale, 0);
					blit(2, 2, 2, 62, 16, 12);
				}
				RenderSystem.popMatrix();
			}
		}
		RenderSystem.popMatrix();
	}
}

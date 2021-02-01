package online.kingdomkeys.kingdomkeys.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.handler.InputHandler;
import online.kingdomkeys.kingdomkeys.lib.Strings;

public class LockOnGui extends Screen {
	int guiWidth = 256;
	int guiHeight = 256;

	int hpGuiWidth = 173;
	float hpBarWidth;
	float missingHpBarWidth;
	float hpPerBar;
	int hpBars;
	int currentBar;
	int oldBar;

	int hpGuiHeight = 10;
	int noborderguiwidth = 171;

	float lockOnScale;
	float hpBarScale;

	private float targetHealth;
	private long lastSystemTime;
	private float lastTargetHealth;

	public LockOnGui() {
		super(new TranslationTextComponent(""));
		minecraft = Minecraft.getInstance();
	}

	@SubscribeEvent
	public void onRenderOverlayPost(RenderGameOverlayEvent event) {
		PlayerEntity player = minecraft.player;
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

					lockOnScale = ModConfigs.lockOnIconScale/100F;
					hpBarScale = ModConfigs.lockOnHPScale/100F;

					// Icon
					RenderSystem.pushMatrix();
					{
						RenderSystem.translatef((screenWidth / 2) - (guiWidth / 2) * lockOnScale / size - 0.5F, (screenHeight / 2) - (guiHeight / 2) * lockOnScale / size - 0.5F, 0);
						RenderSystem.scalef(lockOnScale / size, lockOnScale / size, lockOnScale / size);
						this.blit(0, 0, 0, 0, guiWidth, guiHeight);

						minecraft.textureManager.bindTexture(new ResourceLocation(KingdomKeys.MODID, "textures/gui/lockon_1.png"));
						RenderSystem.translated(guiWidth / 2, guiWidth / 2, 0);
						RenderSystem.rotatef(player.ticksExisted % 360 * 10, 0, 0, 1);
						RenderSystem.translated(-guiWidth / 2, -guiWidth / 2, 0);
						this.blit(0, 0, 0, 0, guiWidth, guiHeight);
					}
					RenderSystem.popMatrix();

					minecraft.textureManager.bindTexture(new ResourceLocation(KingdomKeys.MODID, "textures/gui/hpbar.png"));

					RenderSystem.pushMatrix();

					int[] scan = playerData.getEquippedAbilityLevel(Strings.scan);
					// If ability level > 0 and amount of equipped is > 0
					if (target != null && scan[0] > 0 && scan[1] > 0) {
						RenderSystem.pushMatrix();
						{
							RenderSystem.enableBlend();
							RenderSystem.translatef(ModConfigs.lockOnXPos, ModConfigs.lockOnYPos, 0);
							drawString(minecraft.fontRenderer, target.getName().getString(), screenWidth - minecraft.fontRenderer.getStringWidth(target.getName().getString()), (int) (26*hpBarScale), 0xFFFFFF);
							drawHPBar(event, (LivingEntity) target);
							RenderSystem.disableBlend();

						}
						RenderSystem.popMatrix();
					}

					RenderSystem.scalef(hpBarScale, hpBarScale, hpBarScale);
					RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
					RenderSystem.popMatrix();
				}
			}
		}
	}

	public void drawHPBar(RenderGameOverlayEvent event, LivingEntity target) {
		int screenWidth = minecraft.getMainWindow().getScaledWidth();
		if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {
			minecraft.textureManager.bindTexture(new ResourceLocation(KingdomKeys.MODID, "textures/gui/hpbar.png"));

			hpPerBar = 20;
			float widthMultiplier = 10 * hpBarScale;

			float targetHealth = Math.min(target.getHealth(), target.getMaxHealth());

			if (target.getMaxHealth() % hpPerBar == 0) {
				hpBars = (int) (target.getMaxHealth() / hpPerBar);
			} else {
				hpBars = (int) (target.getMaxHealth() / hpPerBar) + 1;
			}

			if (targetHealth % hpPerBar == 0) {
				currentBar = (int) (targetHealth / hpPerBar);
			} else {
				currentBar = (int) (targetHealth / hpPerBar) + 1;
			}

			float firstBar =  (target.getMaxHealth() > hpPerBar ? target.getMaxHealth() % hpPerBar : target.getMaxHealth());
			float oneBar = (target.getMaxHealth() > hpPerBar ? hpPerBar : target.getMaxHealth());// (int) (target.getMaxHealth() / hpBars);

			if (targetHealth % hpPerBar == 0 && targetHealth != 0) {
				hpBarWidth = oneBar * widthMultiplier;
			} else {
				hpBarWidth = (float) ((targetHealth % hpPerBar) * widthMultiplier);
			}

			float i = (targetHealth);
			long j = Util.milliTime();
			if (i < this.targetHealth && target.hurtResistantTime > 0) {
				this.lastSystemTime = j;
			} else if (i > this.targetHealth && target.hurtResistantTime > 0) {
				this.lastSystemTime = j;
			}

			if ((j - this.lastSystemTime > 1000L || this.targetHealth < targetHealth)) { // If 1 second since last attack has passed update variables
				this.targetHealth = i;
				this.lastTargetHealth = i;
				this.lastSystemTime = j;
				oldBar = currentBar;
				missingHpBarWidth = 0;
			}

			//Basically get the Max of the hp bar or 0 (so weird values don't show up) and then out of that a max of that and the missing hp of the bar(so it doesn't go above the limit)
			missingHpBarWidth = targetHealth % hpPerBar == 0 ? 0 : Math.min(Math.max(((lastTargetHealth - targetHealth)),0), hpPerBar - targetHealth % hpPerBar) % hpPerBar * widthMultiplier;
			float hpBarMaxWidth;
			
			// Background HP width
			if (target.getMaxHealth() >= hpPerBar) {
				if(targetHealth + hpPerBar > target.getMaxHealth() && currentBar == (int) (target.getMaxHealth() / hpPerBar)+1) {
					hpBarMaxWidth = (firstBar * widthMultiplier);
				}else{
					hpBarMaxWidth = (oneBar * widthMultiplier);
				}
			} else { //Target has less than 20 hp
				hpBarMaxWidth = (target.getMaxHealth() % hpPerBar) * widthMultiplier;
			}

			RenderSystem.pushMatrix();
			{
				drawHPBarBack( (screenWidth - hpBarMaxWidth - 4 * hpBarScale), 0 * hpBarScale, hpBarMaxWidth, hpBarScale);
				drawHPBarTop( (screenWidth - hpBarWidth - 2 * hpBarScale), 2 * hpBarScale, hpBarWidth, hpBarScale);
				drawHPBars( (screenWidth - hpBarMaxWidth - 4 * hpBarScale), 0 * hpBarScale, hpBarMaxWidth, hpBarScale, target);
				drawDamagedHPBarTop( (screenWidth - hpBarWidth - missingHpBarWidth - 2 * hpBarScale), 2 * hpBarScale, missingHpBarWidth, hpBarScale, target);
				drawDamagedHPBars( (screenWidth - hpBarMaxWidth - 4 * hpBarScale), 0 * hpBarScale, hpBarMaxWidth, hpBarScale, target);
			}
			RenderSystem.popMatrix();			
		}
	}

	public void drawHPBarBack(float posX, float posY, float width, float scale) {
		minecraft.textureManager.bindTexture(new ResourceLocation(KingdomKeys.MODID, "textures/gui/hpbar.png"));
		RenderSystem.pushMatrix();
		{
			RenderSystem.translatef(posX, posY, 0);

			// Left Margin
			RenderSystem.pushMatrix();
			{
				RenderSystem.scalef(scale, scale, 0);
				blit(0, 0, 0, 0, 2, 12);
			}
			RenderSystem.popMatrix();

			// Background
			RenderSystem.pushMatrix();
			{
				RenderSystem.translatef(2*scale, 0, 0);
				RenderSystem.scalef(width, scale, 0);
				blit(0, 0, 2, 0, 1, 12);
			}
			RenderSystem.popMatrix();

			// Right Margin
			RenderSystem.pushMatrix();
			{
				RenderSystem.translatef(2 * scale + width, 0, 0);
				RenderSystem.scalef(scale, scale, 0);
				blit(0, 0, 3, 0, 2, 12);
			}
			RenderSystem.popMatrix();

			// HP Icon
			RenderSystem.pushMatrix();
			{
				RenderSystem.translatef(width - 20*scale, 12*scale, 0);
				RenderSystem.scalef(scale, scale, 0);
				blit(1, 0, 0, 32, 23, 12);
			}
			RenderSystem.popMatrix();

			// HP Bars
			for (int i = 0; i < hpBars - 1; i++) {
				RenderSystem.pushMatrix();
				{
					RenderSystem.translatef(width - 19*scale - (17*scale * (i + 1)), 12*scale, 0);
					RenderSystem.scalef(scale, scale, 0);
					blit(0, 0, 0, 46, 17, 12);
				}
				RenderSystem.popMatrix();
			}
		}
		RenderSystem.popMatrix();

	}

	public void drawHPBarTop(float posX, float posY, float width, float scale) {
		minecraft.textureManager.bindTexture(new ResourceLocation(KingdomKeys.MODID, "textures/gui/hpbar.png"));
		RenderSystem.pushMatrix();
		{
			// HP Bar
			RenderSystem.pushMatrix();
			{
				RenderSystem.translatef(posX, posY, 0);
				RenderSystem.scalef(width, scale, 0);
				blit(0, 0, 2, 12, 1, 8);
			}
			RenderSystem.popMatrix();
		}
		RenderSystem.popMatrix();
	}

	private void drawHPBars(float posX, float posY, float width, float scale, LivingEntity target) {
		// HP Bars
		if(target.isAlive()) {
			// HP Bars
			for (int i = 1; i < currentBar; i++) {
				RenderSystem.pushMatrix();
				{
					RenderSystem.translatef(posX + width - 17 * scale - (17 * scale * i) - 2 * scale, posY + 12 * scale, 0);
					RenderSystem.scalef(scale, scale, 0);
					blit(0, 0, 0, 60, 17, 12);
				}
				RenderSystem.popMatrix();
			}
		}
	}
	
	private void drawDamagedHPBarTop(float posX, float posY, float width, float scale, LivingEntity target) {
		minecraft.textureManager.bindTexture(new ResourceLocation(KingdomKeys.MODID, "textures/gui/hpbar.png"));
		RenderSystem.pushMatrix();
		{
			// HP Bar
			RenderSystem.pushMatrix();
			{				
				RenderSystem.translatef(posX, posY, 0);
				RenderSystem.scalef(width, scale, 0);
				blit(0, 0, 2, 22, 1, 8);
			}
			RenderSystem.popMatrix();
			
		}
		RenderSystem.popMatrix();
	}
	
	private void drawDamagedHPBars(float posX, float posY, float width, float scale, LivingEntity target) {
		// HP Bars
		for (int i = currentBar; i < oldBar; i++) {
			if(target.isAlive()) {
				RenderSystem.pushMatrix();
				{ 
					RenderSystem.translatef(posX + width - 17 * scale - (17 * scale * i) - 2 * scale, posY + 12 * scale, 0);
					RenderSystem.scalef(scale, scale, 0);
					blit(0, 0, 17, 60, 17, 12);
				}
				RenderSystem.popMatrix();
			}
		}
	}
}

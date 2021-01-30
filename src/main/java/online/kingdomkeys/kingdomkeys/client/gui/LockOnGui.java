package online.kingdomkeys.kingdomkeys.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.vector.Vector3f;
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

	float scale;
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
						matrixStack.rotate(Vector3f.ZP.rotation((player.ticksExisted % 360) * 0.2F));
						matrixStack.translate(-guiWidth / 2, -guiWidth / 2, 0);
						this.blit(matrixStack, 0, 0, 0, 0, guiWidth, guiHeight);
					}
					matrixStack.pop();

					minecraft.textureManager.bindTexture(new ResourceLocation(KingdomKeys.MODID, "textures/gui/hpbar.png"));

					matrixStack.push();

					int[] scan = playerData.getEquippedAbilityLevel(Strings.scan);
					// If ability level > 0 and amount of equipped is > 0
					if (target != null && scan[0] > 0 && scan[1] > 0) {
						matrixStack.push();
						{
							RenderSystem.enableBlend();
							matrixStack.translate(ModConfigs.lockOnXPos, ModConfigs.lockOnYPos, 0);
							drawString(matrixStack, minecraft.fontRenderer, target.getName().getString(), screenWidth - minecraft.fontRenderer.getStringWidth(target.getName().getString()), 16, 0xFFFFFF);
							drawHPBar(event, (LivingEntity) target);
							RenderSystem.disableBlend();
						}
						matrixStack.pop();
					}

					matrixStack.scale(scale, scale, scale);
					RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
					matrixStack.pop();
				}
			}
		}
	}

	public void drawHPBar(RenderGameOverlayEvent event, LivingEntity target) {
		int screenWidth = minecraft.getMainWindow().getScaledWidth();
		MatrixStack matrixStack = event.getMatrixStack();

		if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {
			minecraft.textureManager.bindTexture(new ResourceLocation(KingdomKeys.MODID, "textures/gui/hpbar.png"));

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

			float oneBar = (target.getMaxHealth() > hpPerBar ? hpPerBar : target.getMaxHealth());// (int) (target.getMaxHealth() / hpBars);

			if (target.getHealth() % hpPerBar == 0 && target.getHealth() != 0) {
				hpBarWidth = oneBar * widthMultiplier;
			} else {
				hpBarWidth = (float) ((target.getHealth() % hpPerBar) * widthMultiplier);
			}

			float i = (target.getHealth());
			long j = Util.milliTime();
			if (i < this.targetHealth && target.hurtResistantTime > 0) {
				this.lastSystemTime = j;
			} else if (i > this.targetHealth && target.hurtResistantTime > 0) {
				this.lastSystemTime = j;
			}

			if ((j - this.lastSystemTime > 1000L || this.targetHealth < target.getHealth())) { // If 1 second since last attack has passed update variables
				this.targetHealth = i;
				this.lastTargetHealth = i;
				this.lastSystemTime = j;
				oldBar = currentBar;
				missingHpBarWidth = 0;
			}

			//Basically get the Max of the hp bar or 0 (so weird values don't show up) and then out of that a max of that and the missing hp of the bar(so it doesn't go above the limit)
			missingHpBarWidth = target.getHealth() % hpPerBar == 0 ? 0 : Math.min(Math.max(((lastTargetHealth - target.getHealth())),0), hpPerBar - target.getHealth() % hpPerBar) % hpPerBar * widthMultiplier;
			float hpBarMaxWidth;
			
			// Background HP width
			if (target.getMaxHealth() >= hpPerBar) {
				hpBarMaxWidth = oneBar * widthMultiplier;
			} else {
				hpBarMaxWidth = (target.getMaxHealth() % hpPerBar) * widthMultiplier;
			}

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
				drawHPBarTop(matrixStack, 0, 0, hpBarWidth, scale);
			}
			matrixStack.pop();
			matrixStack.push(); // Red portion of the bar
			{			
				matrixStack.translate((screenWidth - (hpBarWidth + missingHpBarWidth) * scale) - 2 * scale * 2, 1, 0);
				matrixStack.scale(scale, scale, scale);
				drawDamagedHPBarTop(matrixStack, 0, 0, missingHpBarWidth, scale, target);
			}
			matrixStack.pop();
			matrixStack.push(); // Red bars
			{
				matrixStack.translate((screenWidth - hpBarMaxWidth * scale) - 2 * scale * 2, 1, 0);
				matrixStack.scale(scale, scale, scale);
				drawDamagedHPBars(matrixStack, 0, 0, hpBarMaxWidth, scale, target);
			}
			matrixStack.pop();	
		}
	}

	public void drawHPBarBack(MatrixStack matrixStack, int posX, int posY, float width, float scale) {
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

	public void drawHPBarTop(MatrixStack matrixStack, int posX, int posY, float width, float scale) {
		minecraft.textureManager.bindTexture(new ResourceLocation(KingdomKeys.MODID, "textures/gui/hpbar.png"));
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
			for (int i = 1; i < currentBar; i++) {
				matrixStack.push();
				{
					matrixStack.translate(posX + width - 14 - (11 * (i)), posY + 9, 0);
					matrixStack.scale(scale, scale, 0);
					blit(matrixStack, 2, 2, 2, 62, 13, 8);
				}
				matrixStack.pop();
			}
		}
		matrixStack.pop();
	}
	
	private void drawDamagedHPBarTop(MatrixStack matrixStack, int posX, int posY, float width, float scale, LivingEntity target) {
		minecraft.textureManager.bindTexture(new ResourceLocation(KingdomKeys.MODID, "textures/gui/hpbar.png"));
		matrixStack.push();
		{
			// HP Bar
			matrixStack.push();
			{				
				matrixStack.translate((posX + 2) * scale, (posY + 2) * scale, 0);
				matrixStack.scale(width, scale, 0);
				blit(matrixStack, 0, 0, 2, 22, 1, 8);
			}
			matrixStack.pop();
			
		}
		matrixStack.pop();
	}
	
	private void drawDamagedHPBars(MatrixStack matrixStack, int posX, int posY, float width, float scale, LivingEntity target) {
		// HP Bars
		if(target.isAlive()) {
			for (int i = currentBar; i < oldBar; i++) {
				matrixStack.push();
				{
					matrixStack.translate(posX + width - 14 - (11 * i)-0.01F, posY + 9, 0);
					matrixStack.scale(scale, scale, 0);
					blit(matrixStack, 2, 2, 17, 62, 13, 8);
				}
				matrixStack.pop();
			}
		}
	}
}

package online.kingdomkeys.kingdomkeys.client.gui.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.Util;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.entity.GummiShipEntity;
import online.kingdomkeys.kingdomkeys.lib.Constants;
import online.kingdomkeys.kingdomkeys.util.Utils;

//TODO cleanup + comments
public class HPGui extends OverlayBase {

	public static final HPGui INSTANCE = new HPGui();
	float hpBarWidth, missingHpBarWidth, missingGummiHpBarWidth;
	int guiHeight = 10;

	private float playerHealth, gummiHealth;
	private long lastSystemTime, lastGummiSystemTime;
	private float lastPlayerHealth, lastGummiHealth;

	final ResourceLocation texture = ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/gui/hpbar.png");

	private HPGui() {
		super();
	}

	@Override
	public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
		super.render(guiGraphics, deltaTracker);
		Player player = minecraft.player;
		int screenWidth = minecraft.getWindow().getGuiScaledWidth();
		int screenHeight = minecraft.getWindow().getGuiScaledHeight();
		RenderSystem.setShaderColor(1, 1, 1, 1);

		float scale = 1f;
		switch (minecraft.options.guiScale().get()) {
			case Constants.SCALE_AUTO:
				scale = 0.85F;
				break;
		}
		float scaleFactor = 1.5F * ModConfigs.hpXScale/100F;

		hpBarWidth = (player.getHealth() * scaleFactor);

		int hpBarMaxWidth = (int) (player.getMaxHealth() * scaleFactor);

		float i = player.getHealth();
		long j = Util.getMillis();
		if (i < this.playerHealth && player.invulnerableTime > 0) {
			this.lastSystemTime = j;
		} else if (i > this.playerHealth && player.invulnerableTime > 0) {
			this.lastSystemTime = j;
		}

		missingHpBarWidth = Math.max(((lastPlayerHealth - player.getHealth()) * scaleFactor),0);

		if (j - this.lastSystemTime > 1000L || this.playerHealth < player.getHealth()) { // If 1 second since last attack has passed update variables
			this.playerHealth = i;
			this.lastPlayerHealth = i;
			this.lastSystemTime = j;
		}

		PoseStack poseStack = guiGraphics.pose();

		poseStack.pushPose();
		{
			RenderSystem.enableBlend();
			poseStack.translate(ModConfigs.hpXPos, ModConfigs.hpYPos, 0);
			//Player HP
			poseStack.pushPose();
			{
				poseStack.translate((screenWidth - hpBarMaxWidth * scale) - 8 * scale, (screenHeight - guiHeight * scale) - 2 * scale, 0);
				poseStack.scale(scale, scale, scale);
				drawHPBarBack(guiGraphics, 0, 0, hpBarMaxWidth, scale, player.getHealth(), player.getMaxHealth());
			}
			poseStack.popPose();

			poseStack.pushPose();
			{
				poseStack.translate((screenWidth - (hpBarWidth) * scale) - 8 * scale, (screenHeight - (guiHeight) * scale) - 1 * scale - 0.1F, 0);
				poseStack.scale(scale, scale, scale);
				drawHPBarTop(guiGraphics, 0, 0, hpBarWidth, scale);
			}
			poseStack.popPose();
			poseStack.pushPose(); // Red portion of the bar
			{
				poseStack.translate((screenWidth - (hpBarWidth + missingHpBarWidth) * scale) - 8 * scale, (screenHeight - (guiHeight) * scale) - 1 * scale - 0.1F, 0);
				poseStack.scale(scale, scale, scale);
				drawDamagedHPBarTop(guiGraphics, 0, 0, missingHpBarWidth, scale);
			}
			poseStack.popPose();

			//Ship HP
			if(player.getVehicle() instanceof GummiShipEntity gummi && gummi.shipStats != null) {
				float gummiHpBarWidth = (gummi.shipStats.armour() - gummi.getDamage()) * scaleFactor;
				poseStack.translate(0, -10, 0);

				poseStack.pushPose();
				{
					poseStack.translate((screenWidth - gummiHpBarWidth * scale) - 8 * scale, (screenHeight - guiHeight * scale) - 2 * scale, 0);
					poseStack.scale(scale, scale, scale);
					drawHPBarBack(guiGraphics, 0, 0, gummiHpBarWidth, scale, gummi.getDamage(), gummi.shipStats.armour());
				}
				poseStack.popPose();

				poseStack.pushPose();
				{
					poseStack.translate((screenWidth - (gummiHpBarWidth) * scale) - 8 * scale, (screenHeight - (guiHeight) * scale) - 1 * scale - 0.1F, 0);
					poseStack.scale(scale, scale, scale);
					drawHPBarTop(guiGraphics, 0, 0, gummiHpBarWidth, scale);
				}
				poseStack.popPose();
				poseStack.pushPose(); // Red portion of the bar
				{
					float gummiHP = gummi.shipStats.armour()-gummi.getDamage();
					missingGummiHpBarWidth = Math.max(((lastGummiHealth - gummiHP) * scaleFactor),0);
					j = Util.getMillis();
					if (gummiHP < gummiHealth && gummi.invulnerableTime > 0) {
						this.lastGummiSystemTime = j;
					} else if (i > this.gummiHealth && gummi.invulnerableTime > 0) {
						this.lastGummiSystemTime = j;
					}
					if (j - this.lastGummiSystemTime > 1000L || this.gummiHealth < gummi.shipStats.armour()) { // If 1 second since last attack has passed update variables
						this.gummiHealth = gummiHP;
						this.lastGummiHealth = gummiHP;
						this.lastGummiSystemTime = j;
					}

					poseStack.translate((screenWidth - (gummiHpBarWidth + missingGummiHpBarWidth) * scale) - 8 * scale, (screenHeight - (guiHeight) * scale) - 1 * scale - 0.1F, 0);
					poseStack.scale(scale, scale, scale);
					drawDamagedHPBarTop(guiGraphics, 0, 0, missingGummiHpBarWidth, scale);
				}
				poseStack.popPose();
			}
			RenderSystem.disableBlend();
		}
		poseStack.popPose();
	}

	public void drawHPBarBack(GuiGraphics gui, int posX, int posY, float width, float scale, float hp, float maxHP) {
		PoseStack matrixStack = gui.pose();
		matrixStack.pushPose();
		{
			// Left
			matrixStack.pushPose();
			{
				matrixStack.translate(scale * posX, scale * posY, 0);
				matrixStack.scale(scale, scale, 0);
				blit(gui, texture, 0, 0, 0, 0, 2, 12);
			}
			matrixStack.popPose();

			// Middle
			matrixStack.pushPose();
			{
				matrixStack.translate((posX + 2) * scale, posY * scale, 0);
				matrixStack.scale(width, scale, 0);
				int v = Utils.isLowHP(hp,maxHP) ? 8 : 2;
				blit(gui, texture, 0, 0, v, 0, 1, 12);
			}
			matrixStack.popPose();

			// Right
			matrixStack.pushPose();
			{
				matrixStack.translate((posX + 2) * scale + width, scale * posY, 0);
				matrixStack.scale(scale, scale, 0);
				blit(gui, texture, 0, 0, 3, 0, 2, 12);
			}
			matrixStack.popPose();
		}
		matrixStack.popPose();

	}

	public void drawHPBarTop(GuiGraphics gui, int posX, int posY, float width, float scale) {
		PoseStack matrixStack = gui.pose();
		matrixStack.pushPose();
		{
			matrixStack.translate((posX + 2) * scale, (posY + 2) * scale, 0);
			matrixStack.scale(width, scale, 0);
			blit(gui, texture, 0, -1, 2, 12, 1, 8);
		}
		matrixStack.popPose();

	}
	
	public void drawDamagedHPBarTop(GuiGraphics gui, int posX, int posY, float width, float scale) {
		PoseStack matrixStack = gui.pose();
		matrixStack.pushPose();
		{
			matrixStack.translate((posX + 2) * scale, (posY + 2) * scale, 0);
			matrixStack.scale(width, scale, 0);
			blit(gui, texture,0, -1, 2, 22, 1, 8);
		}
		matrixStack.popPose();
	}

}

package online.kingdomkeys.kingdomkeys.client.gui.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.data.GlobalData;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.effects.ModMobEffects;
import online.kingdomkeys.kingdomkeys.handler.InputHandler;
import online.kingdomkeys.kingdomkeys.util.Utils;

@EventBusSubscriber(value = Dist.CLIENT)
public class LockOnGui extends OverlayBase {

	public static final LockOnGui INSTANCE = new LockOnGui();
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
	float s1;
	LivingEntity lastTrackedTarget;
	private float targetHealth;
	private long lastSystemTime;
	private float lastTargetHealth;

	final ResourceLocation texture = KingdomKeys.rl("textures/gui/hpbar.png");

	private LockOnGui() {
		super();
	}

	@SubscribeEvent
	public static void renderOverlay(RenderGuiLayerEvent.Pre event) {
		if(Minecraft.getInstance().level == null)
			return;

		if (InputHandler.lockOn != null && !ModConfigs.SERVER.softLockOnMode.get() && event.getName().equals(VanillaGuiLayers.CROSSHAIR)) {
			event.setCanceled(true);
		}
	}

	@Override
	public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
		super.render(guiGraphics, deltaTracker);
		if(minecraft != null && minecraft.options.hideGui){
			return;
		}
		Player player = minecraft.player;
		PlayerData playerData = PlayerData.get(player);
		if (playerData != null) {
			LivingEntity target = InputHandler.lockOn;
			if (target == null) {
				missingHpBarWidth = 0;
            } else {
				if(player.distanceTo(target) > InputHandler.lockOnReach() || player.hasEffect(ModMobEffects.KO)){
					InputHandler.lockOn = null;
					return;
				}

				float size = 6;

				int screenWidth = minecraft.getWindow().getGuiScaledWidth();
				int screenHeight = minecraft.getWindow().getGuiScaledHeight();

				lockOnScale = ModConfigs.lockOnIconScale/100F;

				PoseStack poseStack = guiGraphics.pose();

                if(!ModConfigs.SERVER.softLockOnMode.get()) {
                    // Icon
                    poseStack.pushPose();
                    {
                        poseStack.translate((screenWidth / 2) - (guiWidth / 2) * lockOnScale / size - 0.5F, (screenHeight / 2) - (guiHeight / 2) * lockOnScale / size - 0.5F, 0);
                        poseStack.scale(lockOnScale / size, lockOnScale / size, lockOnScale / size);
                        this.blit(guiGraphics, KingdomKeys.rl("textures/gui/lockon_0.png"), 0, 0, 0, 0, guiWidth, guiHeight);

                        poseStack.translate(guiWidth / 2, guiWidth / 2, 0);
                        float partialTicks = Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(false);
                        poseStack.mulPose(Axis.ZP.rotationDegrees((float) Math.toDegrees((player.tickCount+ partialTicks % 360) * ModConfigs.lockOnIconRotation / 100F)));
                        poseStack.translate(-guiWidth / 2, -guiWidth / 2, 0);
                        this.blit(guiGraphics, KingdomKeys.rl("textures/gui/lockon_1.png"), 0, 0, 0, 0, guiWidth, guiHeight);
                    }
                    poseStack.popPose();
                }
				poseStack.pushPose();
                //HP Bar and name
				if(target != null && playerData.isAbilityEquipped(ModAbilities.SCAN)) {
					ClientUtils.LOCKON_ELEMENT.applyTransform(guiGraphics, screenWidth, screenHeight);
					RenderSystem.enableBlend();
					if (!ModConfigs.lockOnHideLabels) {
						Component targetName = target.getName();
						GlobalData targetData = GlobalData.getClient(target);
						if (targetData != null && targetData.getLevel() > 0) {
							targetName = Component.translatable(target.getDisplayName().getString() + " Lv." + Utils.getLevelColor(player, targetData.getLevel()) + targetData.getLevel() + ChatFormatting.RESET);
						}
						float scale = 1.42F;
						guiGraphics.pose().pushPose();
						{
							guiGraphics.pose().scale(scale, scale, scale);
							drawString(guiGraphics, minecraft.font, targetName, ClientUtils.LOCKON_ELEMENT.width - minecraft.font.width(targetName) - 50, 19, 0xFFFFFF);
						}
						guiGraphics.pose().popPose();
					}
					drawHPBar(guiGraphics, target);
					RenderSystem.disableBlend();
					ClientUtils.LOCKON_ELEMENT.endTransform(guiGraphics);
				}

				poseStack.popPose();
			}
		}
	}

	public void drawHPBar(GuiGraphics gui, LivingEntity target) {
		int offset = 165;

		hpPerBar = ModConfigs.lockOnHpPerBar;
		float widthMultiplier = 10;

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
			hpBarWidth = (targetHealth % hpPerBar) * widthMultiplier;
		}

		float i = (targetHealth);
		long j = Util.getMillis();
		if (i < this.targetHealth && target.invulnerableTime > 0) {
			this.lastSystemTime = j;
		} else if (i > this.targetHealth && target.invulnerableTime > 0) {
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
		if (ModConfigs.lockOnKH1Bar) {
			drawKH1HPBar(gui, offset, oneBar * widthMultiplier, target);
			return;
		}

		float hpBarMaxWidth, bgHPBarMaxWidth = 0;

		// Background HP width
		if (target.getMaxHealth() >= hpPerBar) {
			if(targetHealth + hpPerBar > target.getMaxHealth() && currentBar == hpBars) { //If it's first bar
				hpBarMaxWidth = (firstBar * widthMultiplier);
				bgHPBarMaxWidth = hpPerBar * widthMultiplier;
			} else if(currentBar == 1) {//If it's the last bar
				hpBarMaxWidth = (oneBar * widthMultiplier);
				bgHPBarMaxWidth = 0;
			} else { //Middle bar in entity with hp > 20
				hpBarMaxWidth = (oneBar * widthMultiplier);
				bgHPBarMaxWidth = hpPerBar * widthMultiplier;
			}
		} else { //Target has less than 20 hp
			hpBarMaxWidth = (target.getMaxHealth() % hpPerBar) * widthMultiplier;
		}

		gui.pose().pushPose();
		{
			drawHPBarBack(gui, (offset - hpBarMaxWidth - 4 ), 0 , hpBarMaxWidth, 1, (offset - bgHPBarMaxWidth - 4 ), bgHPBarMaxWidth);
			drawHPBarTop(gui, (offset - hpBarWidth - 2 ), 2 , hpBarWidth, 1);
			drawDamagedHPBarTop(gui, (offset - hpBarWidth - missingHpBarWidth - 2 ), 2 , missingHpBarWidth, 1, target);
			drawHPBars(gui, (offset - hpBarMaxWidth - 4 ), 0 , hpBarMaxWidth, 1, target);
			drawDamagedHPBars(gui, (offset - hpBarMaxWidth - 4 ), 0 , hpBarMaxWidth, 1, target);
		}
		gui.pose().popPose();
	}

	// Kingdom Hearts 1: green is the last bar, the ones before it go up through these and then start over without it
	private static final int[] KH1_BAR_COLOURS = {0xFF3FC23F, 0xFFD8C21E, 0xFFE07A16, 0xFFE04AB8, 0xFF8A3FD8, 0xFF3C64E0, 0xFF2EC4D8};

	private static int kh1BarColour(int bar) {
		return bar <= 1 ? KH1_BAR_COLOURS[0] : KH1_BAR_COLOURS[1 + (bar - 2) % (KH1_BAR_COLOURS.length - 1)];
	}

	private static int shade(int colour, float factor) {
		int r = Math.min(255, (int) (((colour >> 16) & 0xFF) * factor));
		int g = Math.min(255, (int) (((colour >> 8) & 0xFF) * factor));
		int b = Math.min(255, (int) ((colour & 0xFF) * factor));
		return 0xFF000000 | (r << 16) | (g << 8) | b;
	}

	// Every bar is the same length and stacked on the next one, whose colour shows through as the current one drains
	private void drawKH1HPBar(GuiGraphics gui, int offset, float fullWidth, LivingEntity target) {
		PoseStack matrixStack = gui.pose();
		float left = offset - fullWidth - 4;

		matrixStack.pushPose();
		{
			matrixStack.translate(left, 0, 0);
			blit(gui, texture, 0, 0, 0, 0, 2, 12);
			// Black like the border, which is all that is left once the green bar runs out
			matrixStack.pushPose();
			{
				matrixStack.translate(2, 0, 0);
				matrixStack.scale(fullWidth, 1, 1);
				gui.fill(0, 0, 1, 12, 0xFF000000);
			}
			matrixStack.popPose();
			matrixStack.pushPose();
			{
				matrixStack.translate(2 + fullWidth, 0, 0);
				blit(gui, texture, 0, 0, 3, 0, 2, 12);
			}
			matrixStack.popPose();

			if (!ModConfigs.lockOnHideLabels) {
				matrixStack.pushPose();
				{
					matrixStack.translate(fullWidth - 20, 12, 0);
					blit(gui, texture, 1, 0, 0, 32, 23, 12);
				}
				matrixStack.popPose();
			}
		}
		matrixStack.popPose();

		if (!target.isAlive() || currentBar <= 0) {
			return;
		}

		float fillLeft = left + 2;
		if (currentBar > 1) {
			drawKH1Fill(gui, fillLeft, fullWidth, kh1BarColour(currentBar - 1));
		}
		float width = Math.min(hpBarWidth, fullWidth);
		float healthLeft = fillLeft + fullWidth - width;
		drawKH1Fill(gui, healthLeft, width, kh1BarColour(currentBar));
		float missing = Math.min(missingHpBarWidth, fullWidth - width);
		drawDamagedHPBarTop(gui, healthLeft - missing, 2, missing, 1, target);

		// A black notch where the current health ends
		if (width > 0 && width < fullWidth) {
			PoseStack pose = gui.pose();
			pose.pushPose();
			{
				pose.translate(healthLeft - 2, 2, 0);
				gui.fill(0, 0, 2, 8, 0xFF000000);
			}
			pose.popPose();
		}
	}

	private void drawKH1Fill(GuiGraphics gui, float x, float width, int colour) {
		if (width <= 0) {
			return;
		}
		PoseStack matrixStack = gui.pose();
		matrixStack.pushPose();
		{
			matrixStack.translate(x, 2, 0);
			matrixStack.scale(width, 1, 1);
			gui.fillGradient(0, 0, 1, 8, shade(colour, 1.25F), shade(colour, 0.7F));
		}
		matrixStack.popPose();
	}

	public void drawHPBarBack(GuiGraphics gui, float posX, float posY, float width, float scale, float bgPosX, float bgHPBarMaxWidth) {
		PoseStack matrixStack = gui.pose();
		matrixStack.pushPose();
		{
			matrixStack.translate(posX, posY, 0);

			//Green bg bar render
			matrixStack.pushPose();
			{
				matrixStack.translate(bgPosX - posX, posY, 0);
				// Left Margin
				matrixStack.pushPose();
				{
					matrixStack.scale(scale, scale, 0);
					blit(gui, texture, 0, 0, 0, 0, 2, 12);
				}
				matrixStack.popPose();

				// Background
				matrixStack.pushPose(); //Empty bg (last bar)
				{
					matrixStack.translate(2*scale, 0, 0);
					matrixStack.scale(bgHPBarMaxWidth, scale, 0);
					blit(gui, texture, 0, 0, 14, 0, 1, 12);
				}
				matrixStack.popPose();

				// Right Margin
				matrixStack.pushPose();
				{
					matrixStack.translate(2 * scale + bgHPBarMaxWidth, 0, 0);
					matrixStack.scale(scale, scale, 0);
					blit(gui, texture, 0, 0, 3, 0, 2, 12);
				}
				matrixStack.popPose();
			}
			matrixStack.popPose();

			//Normal bar render
			// Left Margin
			matrixStack.pushPose();
			{
				matrixStack.scale(scale, scale, 0);
				blit(gui, texture, 0, 0, 0, 0, 2, 12);
			}
			matrixStack.popPose();

			// Background
			matrixStack.pushPose();
			{
				matrixStack.translate(2*scale, 0, 0);
				matrixStack.scale(width, scale, 0);
				blit(gui, texture, 0, 0, 2, 0, 1, 12);
			}
			matrixStack.popPose();

			// Right Margin
			matrixStack.pushPose();
			{
				matrixStack.translate(2 * scale + width, 0, 0);
				matrixStack.scale(scale, scale, 0);
				blit(gui, texture, 0, 0, 3, 0, 2, 12);
			}
			matrixStack.popPose();
			
			// HP Icon
			if (!ModConfigs.lockOnHideLabels) {
				matrixStack.pushPose();
				{
					matrixStack.translate(width - 20*scale, 12*scale, 0);
					matrixStack.scale(scale, scale, 0);
					blit(gui, texture, 1, 0, 0, 32, 23, 12);
				}
				matrixStack.popPose();
			}

			// HP Bars
			for (int i = 0; i < hpBars - 1; i++) {
				matrixStack.pushPose();
				{
					matrixStack.translate(width - 19*scale - (17*scale * (i + 1)), 12*scale, 0);
					matrixStack.scale(scale, scale, 0);
					blit(gui, texture, 0, 0, 0, 46, 17, 12);
				}
				matrixStack.popPose();
			}
		}
		matrixStack.popPose();

	}

	public void drawHPBarTop(GuiGraphics gui, float posX, float posY, float width, float scale) {
		PoseStack matrixStack = gui.pose();
		// HP Bar
		matrixStack.pushPose();
		{
			matrixStack.translate(posX, posY, 0);
			matrixStack.scale(width, scale, 0);
			blit(gui, texture, 0, 0, 2, 12, 1, 8);
		}
		matrixStack.popPose();
	}
	
	private void drawHPBars(GuiGraphics gui, float posX, float posY, float width, float scale, LivingEntity target) {
		PoseStack matrixStack = gui.pose();
		// HP Bars
		if(target.isAlive()) {
			// HP Bars
			for (int i = 1; i < currentBar; i++) {
				matrixStack.pushPose();
				{
					matrixStack.translate(posX + width - 17 * scale - (17 * scale * i) - 2 * scale, posY + 12 * scale, 0);
					matrixStack.scale(scale, scale, 0);
					blit(gui, texture, 0, 0, 0, 60, 17, 12);
				}
				matrixStack.popPose();
			}
		}
	}
	
	private void drawDamagedHPBarTop(GuiGraphics gui, float posX, float posY, float width, float scale, LivingEntity target) {
		PoseStack matrixStack = gui.pose();
		matrixStack.pushPose();
		{
			// HP Bar
			matrixStack.pushPose();
			{				
				matrixStack.translate(posX, posY, 0);
				matrixStack.scale(width, scale, 0);
				blit(gui, texture, 0, 0, 2, 22, 1, 8);
			}
			matrixStack.popPose();
			
		}
		matrixStack.popPose();
	}
	
	private void drawDamagedHPBars(GuiGraphics gui, float posX, float posY, float width, float scale, LivingEntity target) {
		PoseStack matrixStack = gui.pose();
		// HP Bars
		if(target.isAlive()) {
			for (int i = currentBar; i < oldBar; i++) {
				matrixStack.pushPose();
				{
					matrixStack.translate(posX + width - 17 * scale - (17 * scale * i) - 2 * scale, posY + 12 * scale, 0);
					matrixStack.scale(scale, scale, 0);
					blit(gui, texture, 0, 0, 17, 60, 17, 12);
				}
				matrixStack.popPose();
			}
		}
	}
}

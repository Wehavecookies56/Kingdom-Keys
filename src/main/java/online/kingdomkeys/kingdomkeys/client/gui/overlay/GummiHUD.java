package online.kingdomkeys.kingdomkeys.client.gui.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.CameraType;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.entity.GummiShipEntity;
import online.kingdomkeys.kingdomkeys.lib.Constants;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.text.DecimalFormat;

public class GummiHUD extends OverlayBase {

	public static boolean handledCamera = false;
	public static CameraType prevCamera = CameraType.FIRST_PERSON;
	public static final GummiHUD INSTANCE = new GummiHUD();

	private float displayedGummiHP, realGummiHP;

	private long gummiDelayEnd = 0;

	final ResourceLocation hpTexture = ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/gui/hpbar.png");

	private GummiHUD() {
		super();
	}

	private static final DecimalFormat df = new DecimalFormat("0.00");

	@Override
	public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
		super.render(guiGraphics, deltaTracker);
		Player player = minecraft.player;
		if(player.getVehicle() instanceof GummiShipEntity ship){
			if(!handledCamera && ModConfigs.auto3rdPersonShip){
				// Store and swap camera if needed
				prevCamera = minecraft.options.getCameraType();
				minecraft.options.setCameraType(CameraType.THIRD_PERSON_BACK);
				handledCamera = true;
			}
			int screenWidth = minecraft.getWindow().getGuiScaledWidth() - 10;

			int x = screenWidth, y = 1;
			GummiShipEntity.ShipStats stats = ship.shipStats;
			if(stats != null) {
				float deltaX = (float) (ship.getX() - ship.xOld);
				float deltaY = (float) (ship.getY() - ship.yOld);
				float deltaZ = (float) (ship.getZ() - ship.zOld);
				float speed = (float) Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2) + Math.pow(deltaZ, 2));

                String text = "Fuel: "+ship.getFuel()+" / "+ship.getMaxFuel();
                drawString(guiGraphics, minecraft.font, text, x-font.width(text), 10 * y++, 0xFFFFFF);
				text = "Speed: "+ df.format(speed * 20)+"m/s";
				drawString(guiGraphics, minecraft.font, text, x-font.width(text), 10 * y++, 0xFFFFFF);
				text = "Engine power: "+(int) Math.abs(ship.currentSpeed * 100)+" / "+ (int) (stats.speed() * 100);
				drawString(guiGraphics, minecraft.font, text, x-font.width(text), 10 * y++, 0xFFFFFF);
				text = "Armor: " + stats.armour();
				drawString(guiGraphics, minecraft.font, text, x-font.width(text), 10 * y++, 0xFFFFFF);
				text = "Num. of weapons: " + stats.firepower().size();
				drawString(guiGraphics, minecraft.font, text, x-font.width(text), 10 * y++, 0xFFFFFF);

				//if radar is present
				x = screenWidth/2;
				y = 1;
				text = ship.structure.getName();
				drawString(guiGraphics, minecraft.font, text, x-(font.width(text)/2), 10 * y++, 0xAA0000);
				text = "Coords: " + (int) ship.getX()+", "+(int) ship.getY()+", "+(int) ship.getZ();
				drawString(guiGraphics, minecraft.font, text, x-(font.width(text)/2), 10 * y++, 0xFFFFFF);
				text = "Facing: " + ship.getDirection();
				drawString(guiGraphics, minecraft.font, text, x-(font.width(text)/2), 10 * y++, 0xFFFFFF);

				drawHP(ship, deltaTracker);
			}

			x = 10;
			y = 1;

			drawString(guiGraphics, minecraft.font, minecraft.options.keyUp.getKey().getDisplayName().getString()+": FORWARD", x, 10*y++, ship.inputForward ? 0xAA0000 : 0xFFFFFF);
			drawString(guiGraphics, minecraft.font, minecraft.options.keyDown.getKey().getDisplayName().getString()+": BACKWARDS", x, 10*y++, ship.inputBackward ? 0xAA0000 : 0xFFFFFF);
			drawString(guiGraphics, minecraft.font, minecraft.options.keyLeft.getKey().getDisplayName().getString()+": LEFT", x, 10*y++, ship.inputLeft ? 0xAA0000 : 0xFFFFFF);
			drawString(guiGraphics, minecraft.font, minecraft.options.keyRight.getKey().getDisplayName().getString()+": RIGHT", x, 10*y++, ship.inputRight ? 0xAA0000 : 0xFFFFFF);
			drawString(guiGraphics, minecraft.font, minecraft.options.keyJump.getKey().getDisplayName().getString()+": UP", x, 10*y++, ship.inputUp ? 0xAA0000 : 0xFFFFFF);
			drawString(guiGraphics, minecraft.font, minecraft.options.keySprint.getKey().getDisplayName().getString()+": DOWN", x, 10*y++, ship.inputDown ? 0xAA0000 : 0xFFFFFF);
		} else {
			//Restore camera if needed
			if(handledCamera && ModConfigs.auto3rdPersonShip){
				minecraft.options.setCameraType(prevCamera);
				handledCamera = false;
			}

		}
	}

	private void drawHP(GummiShipEntity ship, DeltaTracker deltaTracker) {
		//HP
		PoseStack poseStack = guiGraphics.pose();
		GummiShipEntity.ShipStats stats = ship.shipStats;


		float partialTick = deltaTracker.getGameTimeDeltaPartialTick(false);

		int screenWidth = minecraft.getWindow().getGuiScaledWidth();
		int screenHeight = minecraft.getWindow().getGuiScaledHeight();
		float scale = 1f;
		if (minecraft.options.guiScale().get() == Constants.SCALE_AUTO)
			scale = 0.85F;

		float scaleFactor = 1.5F * ModConfigs.hpXScale / 100F;
		RenderSystem.enableBlend();
		RenderSystem.setShaderColor(1, 1, 1, 1);
		poseStack.pushPose();
		{
			poseStack.translate(ModConfigs.hpXPos, ModConfigs.hpYPos, 0);

			long now = net.minecraft.Util.getMillis();

			// Gummi
			float gummiHP = stats.armour() - ship.getDamage();
			float gummiMaxHP = 100;

			float normalizedHP = (gummiHP / stats.armour()) * gummiMaxHP;

			if (realGummiHP == 0) {
				realGummiHP = normalizedHP;
				displayedGummiHP = normalizedHP;
			}

			if (normalizedHP < realGummiHP) {
				gummiDelayEnd = now + 1000;
			}
			realGummiHP = normalizedHP;

			if (now > gummiDelayEnd) {
				displayedGummiHP = Mth.lerp(0.05F * partialTick, displayedGummiHP, realGummiHP);
			}

			poseStack.translate(0, 5, 0);
			drawHPBars(guiGraphics, poseStack, screenWidth, screenHeight, scale, scaleFactor, displayedGummiHP, realGummiHP, gummiMaxHP);

		}
		poseStack.popPose();
		RenderSystem.disableBlend();
	}

	public void drawHPBars(GuiGraphics gui, PoseStack poseStack, int screenWidth, int screenHeight, float scale, float scaleFactor, float displayedHP, float realHP, float maxHP) {
		float maxBarWidth = maxHP * scaleFactor;
		float realBarWidth = realHP * scaleFactor;
		float displayedBarWidth = displayedHP * scaleFactor;
		float missingWidth = Math.max(displayedBarWidth - realBarWidth, 0);

		// Background & outline
		poseStack.pushPose();
		int guiHeight = 10;
		{
			poseStack.translate((screenWidth - maxBarWidth * scale) - 8 * scale, (screenHeight - guiHeight * scale) - 2 * scale, 0);
			poseStack.scale(scale, scale, scale);
			drawHPBarBack(gui, 0, 0, maxBarWidth, scale, realHP, maxHP);
		}
		poseStack.popPose();

		// Green HP
		poseStack.pushPose();
		{
			poseStack.translate((screenWidth - realBarWidth * scale) - 8 * scale, (screenHeight - guiHeight * scale) - 1 * scale, 0);
			poseStack.scale(scale, scale, scale);
			RenderSystem.setShaderColor(0.3F, 0.6F, 0.3F, 1);
			drawHPBarTop(gui, 0, 0, realBarWidth, scale);
			RenderSystem.setShaderColor(1, 1, 1, 1);
		}
		poseStack.popPose();

		// Red
		if (missingWidth > 0.5F) {
			poseStack.pushPose();
			{
				poseStack.translate((screenWidth - (realBarWidth + missingWidth) * scale) - 8 * scale, (screenHeight - guiHeight * scale) - 1 * scale, 0);
				poseStack.scale(scale, scale, scale);
				drawDamagedHPBarTop(gui, 0, 0, missingWidth, scale);
			}
			poseStack.popPose();
		}
	}

	public void drawHPBarBack(GuiGraphics gui, int posX, int posY, float width, float scale, float hp, float maxHP) {
		PoseStack matrixStack = gui.pose();
		matrixStack.pushPose();
		{
			matrixStack.translate(scale * posX, scale * posY, 0);
			matrixStack.scale(scale, scale, 0);
			blit(gui, hpTexture, 0, 0, 0, 0, 2, 12);
		}
		matrixStack.popPose();

		matrixStack.pushPose();
		{
			matrixStack.translate((posX + 2) * scale, posY * scale, 0);
			matrixStack.scale(width, scale, 0);
			int v = Utils.isLowHP(hp, maxHP) ? 8 : 2;
			blit(gui, hpTexture, 0, 0, v, 0, 1, 12);
		}
		matrixStack.popPose();

		matrixStack.pushPose();
		{
			matrixStack.translate((posX + 2) * scale + width, scale * posY, 0);
			matrixStack.scale(scale, scale, 0);
			blit(gui, hpTexture, 0, 0, 3, 0, 2, 12);
		}
		matrixStack.popPose();
	}

	public void drawHPBarTop(GuiGraphics gui, int posX, int posY, float width, float scale) {
		PoseStack matrixStack = gui.pose();
		matrixStack.pushPose();
		{
			matrixStack.translate((posX + 2) * scale, (posY + 2) * scale, 0);
			matrixStack.scale(width, scale, 0);
			blit(gui, hpTexture, 0, -1, 2, 12, 1, 8);
		}
		matrixStack.popPose();
	}

	public void drawDamagedHPBarTop(GuiGraphics gui, int posX, int posY, float width, float scale) {
		PoseStack matrixStack = gui.pose();
		matrixStack.pushPose();
		{
			matrixStack.translate((posX + 2) * scale, (posY + 2) * scale, 0);
			matrixStack.scale(width, scale, 0);
			blit(gui, hpTexture, 0, -1, 2, 22, 1, 8);
		}
		matrixStack.popPose();
	}
}

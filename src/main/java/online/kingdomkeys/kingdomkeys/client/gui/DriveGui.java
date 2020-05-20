package online.kingdomkeys.kingdomkeys.client.gui;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

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
import online.kingdomkeys.kingdomkeys.lib.Constants;

//TODO cleanup + comments
public class DriveGui extends Screen {
	Minecraft mc = Minecraft.getInstance();

	ResourceLocation texture = new ResourceLocation(KingdomKeys.MODID, "textures/gui/drivebar.png");

	int maxDrive = 1000;
	int maxLength = 100;
	int maxBars = 9;
	double guiLength = 47D;
	double oneValue = (guiLength / 100D);
	double currDrive;
	double currForm;

	static final int R = 0, G = 1, B = 2;
	int[] colors = { 255, 255, 255 };
	// String nextColor = "r";
	static final float CONS = 0.005F;
	float decimalColor = 0F;

	public DriveGui() {
		super(new TranslationTextComponent(""));
	}

	/**
	 * Gets the bar currently in
	 * 
	 * @param dp    drive points
	 * @param level max level unlocked (the one which increases when leveling the drive forms up to level 7
	 * @return
	 */
	public int getCurrBar(double dp, int level) {
		int bar = (int) dp / 100;
		if (bar > level)
			bar = level;
		return bar;
	}

	private boolean doChange = false;

	private int timeLastChange = (int) System.currentTimeMillis();

	@SubscribeEvent
	public void onRenderOverlayPost(RenderGameOverlayEvent event) {
		/*
		 * if (!MainConfig.displayGUI()) return; if
		 * (!mc.player.getCapability(ModCapabilities.PLAYER_STATS, null).getHudMode())
		 * return;
		 */
		IPlayerCapabilities props = ModCapabilities.get(mc.player);
		if (props != null) {
			double dp = props.getDP();
			//double fp = 0;

			currDrive = (float) ((oneValue * dp) - getCurrBar(dp, (int) props.getMaxDP() / 100) * guiLength);
			// if (STATE.getInDrive())
			//currForm = (float) ((oneValue * fp) - getCurrBar(fp, STATE.getFormGaugeLevel(STATE.getActiveDriveName())) * guiLength);

			if (dp == props.getMaxDP()) {
				currDrive = guiLength;
			}

			/*
			 * if (event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR) { if
			 * (!STATE.getActiveDriveName().equals("none") &&
			 * !STATE.getActiveDriveName().equals(Strings.Form_Anti)) {
			 * event.setCanceled(true); } }
			 */
			if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {
				int guiWidth = 95;
				int guiBarWidth = 83;
				int guiHeight = 18;
				int screenWidth = mc.getMainWindow().getScaledWidth();
				int screenHeight = mc.getMainWindow().getScaledHeight();
				PlayerEntity player = mc.player;

				mc.textureManager.bindTexture(texture);

				float scale = 0.65f;
				switch (mc.gameSettings.guiScale) {
				case Constants.SCALE_AUTO:
					scale = 0.85f;
					break;
				case Constants.SCALE_NORMAL:
					scale = 0.85f;
					break;
				default:
					scale = 0.65f;
					break;
				}
				float posX = 52 * scale;
				float posY = 20 * scale + 2;

				GL11.glPushMatrix();
				{
					GL11.glTranslatef(-20.3F, -2, 1);

					// Background
					GL11.glPushMatrix();
					{
						GL11.glTranslatef((screenWidth - guiWidth * scale) - posX, (screenHeight - guiHeight * scale) - posY, 0);
						GL11.glScalef(scale, scale, scale);

						if (props.getDriveForm().equals("")) {
							this.blit(15, 6, 0, 0, guiWidth, guiHeight);
						} else {
							this.blit(15, 6, 98, 0, guiWidth, guiHeight);
						}
					}
					GL11.glPopMatrix();

					// Yellow meter
					GL11.glPushMatrix();
					{
						GL11.glTranslatef((screenWidth - guiWidth * scale) + (guiWidth - guiBarWidth) * scale + (24 * scale) - posX, (screenHeight - guiHeight * scale) - (2 * scale) - posY, 0);
						GL11.glScalef(scale, scale, scale);
						if (props.getDriveForm().equals("")) {
							this.blit(14, 6, 0, 18, (int) currDrive, guiHeight);
						} else {
							this.blit(16, 6, 98, 18, (int) currForm, guiHeight);
						}
					}
					GL11.glPopMatrix();

					// Level Number
					GL11.glPushMatrix();
					{
						GL11.glTranslatef((screenWidth - guiWidth * scale) + (85 * scale) - posX, (screenHeight - guiHeight * scale) - (2 * scale) - posY, 0);
						GL11.glScalef(scale, scale, scale);

						int numPos = props.getDriveForm().equals("") ? getCurrBar(dp, (int) props.getMaxDP() / 100) * 10 : 97;// + (getCurrBar(fp, STATE.getFormGaugeLevel(STATE.getActiveDriveName())) * 10);
						// int numPos = getCurrBar(dp, 9) * 10;
						this.blit(14, 6, numPos, 38, 8, guiHeight);
					}
					GL11.glPopMatrix();

					// MAX Icon
					if (props.getDP() >= props.getMaxDP() && props.getDriveForm().equals("")) {
						GL11.glPushMatrix();
						{
							if (doChange) {
								decimalColor += CONS;
								if (decimalColor >= 1)
									decimalColor = 0;
								Color c = Color.getHSBColor(decimalColor, 1F, 1F);
								GL11.glColor3ub((byte) c.getRed(), (byte) c.getGreen(), (byte) c.getBlue());

								timeLastChange = (int) System.currentTimeMillis();
								doChange = false;
							} else {
								if (timeLastChange + 1 < (int) System.currentTimeMillis()) {
									doChange = true;
								}
							}

							GL11.glTranslatef(((screenWidth - guiWidth * scale) + (10 * scale)), ((screenHeight - guiHeight * scale) - (10 * scale)), 0);
							GL11.glScalef(scale, scale, scale);
							blit(0, -3, 0, 57, 30, guiHeight);
							GL11.glColor3ub((byte) 255, (byte) 255, (byte) 255);
						}
						GL11.glPopMatrix();
					}
				}
				GL11.glPopMatrix();
			}
		}
	}

	/**
	 * Add to color based on the letter
	 * 
	 * @param color R, G or B (position of the colors array)
	 */
	private void add(int color) {
		if (colors[color] <= 255 - CONS) {
			colors[color] += CONS;
		}
	}

	/**
	 * Substract to color based on the letter
	 * 
	 * @param color R, G or B (position of the colors array)
	 */
	private void sub(int color) {
		if (colors[color] - CONS >= 0) {
			colors[color] -= CONS;
		}
	}
}

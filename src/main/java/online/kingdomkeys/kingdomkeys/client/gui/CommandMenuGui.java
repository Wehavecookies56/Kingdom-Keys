package online.kingdomkeys.kingdomkeys.client.gui;

import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
import online.kingdomkeys.kingdomkeys.handler.EntityEvents;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.magic.ModMagics;

//TODO cleanup
public class CommandMenuGui extends Screen {

	public CommandMenuGui() {
		super(new TranslationTextComponent(""));
	}

	Minecraft mc = Minecraft.getInstance();

	public static final int TOP = 5, ATTACK = 4, MAGIC = 3, ITEMS = 2, DRIVE = 1;

	// int selected = ATTACK;

	int TOP_WIDTH = 70;
	int TOP_HEIGHT = 15;

	int MENU_WIDTH = 71;
	int MENU_HEIGHT = 15;

	int iconWidth = 10;

	int textX = 0;

	/*
	 * public static List<PortalCoords> portalCommands; public static List<String>
	 * driveCommands, spells, items; public static List<Ability> attackCommands;
	 */

	public static final int SUB_MAIN = 0, SUB_MAGIC = 1, SUB_ITEMS = 2, SUB_DRIVE = 3, SUB_PORTALS = 4, SUB_ATTACKS = 5;

	public static final int NONE = 0;
	public static int selected = ATTACK;
	public static int submenu = 0, magicselected = 0, potionselected = 0, driveselected = 0, portalSelected = 0, attackSelected = 0;

	ResourceLocation texture = new ResourceLocation(KingdomKeys.MODID, "textures/gui/commandmenu.png");

	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void onRenderOverlayPost(RenderGameOverlayEvent event) {
		// if (mc.player.getCapability(ModCapabilities.PLAYER_STATS, null).getHudMode())
		// {
		if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {// && !mc.ingameGUI.getChatGUI().getChatOpen()) {
			GL11.glPushMatrix();
			{
				drawCommandMenu(mc.getMainWindow().getScaledWidth(), mc.getMainWindow().getScaledHeight());
			}
			GL11.glPopMatrix();
		}
		// }
	}

	int alpha = 255;
	float scale = 1.05f;
	byte[] orgColor = { (byte) 200, (byte) 200, (byte) 200 };
	byte[] attackMenuColor = { (byte) 255, (byte) 200, (byte) 60 };
	byte[] portalMenuColor = { (byte) 100, (byte) 100, (byte) 100 };
	byte[] combatModeColor = { (byte) 255, (byte) 0, (byte) 0 };
	byte[] normalModeColor = { (byte) 10, (byte) 60, (byte) 255 };
	byte[] magicMenuColor = { (byte) 100, (byte) 0, (byte) 255 };
	byte[] itemsMenuColor = { (byte) 70, (byte) 255, (byte) 80 };
	byte[] driveMenuColor = { (byte) 0, (byte) 255, (byte) 255 };

	private void paintWithColorArray(byte[] array, byte alpha) {
		if (EntityEvents.isHostiles)
			GL11.glColor4ub(combatModeColor[0], combatModeColor[1], combatModeColor[2], (byte) alpha);
		else {
			/*
			 * if (Minecraft.getMinecraft().player.getCapability(ModCapabilities.
			 * ORGANIZATION_XIII, null).getMember() != Utils.OrgMember.NONE && array ==
			 * normalModeColor) { GL11.glColor4ub(orgColor[0], orgColor[1], orgColor[2],
			 * (byte) alpha); } else {
			 */
			GL11.glColor4ub(normalModeColor[0], normalModeColor[1], normalModeColor[2], (byte) alpha);
			// }
		}

	}

	public void drawCommandMenu(int width, int height) {
		drawTop(width, height);
		drawAttack(width, height);
		drawMagic(width, height);
		drawItems(width, height);
		drawDrive(width, height);
		if (submenu == SUB_MAGIC) {
			drawSubMagic(width, height);
		}
		if (submenu == SUB_DRIVE) {
			drawSubDrive(width, height);
		}
	}

	private void drawSubMagic(int width, int height) {
		IPlayerCapabilities props = ModCapabilities.get(mc.player);
		if (props != null && props.getMagicsList() != null && !props.getMagicsList().isEmpty()) {
			// MAGIC TOP
			GL11.glPushMatrix();
			{
				GL11.glColor4ub((byte) 255, (byte) 255, (byte) 255, (byte) alpha);
				mc.textureManager.bindTexture(texture);
				GL11.glTranslatef(5, (height - MENU_HEIGHT * scale * (props.getMagicsList().size() + 1)), 0);
				GL11.glScalef(scale, scale, scale);
				int v = 0;
				if (submenu == SUB_MAGIC) {

					GL11.glColor4ub(magicMenuColor[0], magicMenuColor[1], magicMenuColor[2], (byte) alpha);

					blit(0, 0, 0, 0, TOP_WIDTH, TOP_HEIGHT);
					// drawTexturedModalRect(0, 0, 0, 0+extraY, TOP_WIDTH, TOP_HEIGHT);
					drawString(mc.fontRenderer, /*Utils.translateToLocal(Strings.Gui_CommandMenu_Magic_Title)*/"MAGIC", 6, 4, 0xFFFFFF);
				}
			}
			GL11.glPopMatrix();
			for (int i = 0; i < props.getMagicsList().size(); i++) {
				GL11.glPushMatrix();
				{
					GL11.glColor4ub((byte) 255, (byte) 255, (byte) 255, (byte) alpha);
					int u;
					int v;
					int x;
					x = (magicselected == i) ? 10 : 5;

					mc.textureManager.bindTexture(texture);
					GL11.glTranslatef(x, (height - MENU_HEIGHT * scale * (props.getMagicsList().size() - i)), 0);
					GL11.glScalef(scale, scale, scale);
					if (submenu == SUB_MAGIC) {
						v = 0;

						paintWithColorArray(magicMenuColor, (byte) alpha);
						if (magicselected == i) {
							// drawTexturedModalRect(0, 0, TOP_WIDTH, 15+extraY, TOP_WIDTH + MENU_WIDTH, v +
							// MENU_HEIGHT);
							textX = 11;

							// Draw slot
							blit(5, 0, TOP_WIDTH, MENU_HEIGHT, TOP_WIDTH, v + MENU_HEIGHT);

							GL11.glColor4ub((byte) 255, (byte) 255, (byte) 255, (byte) alpha);

							// Draw Icon
							blit(60, 2, 140 + selected * iconWidth - iconWidth, 18, iconWidth, iconWidth);

						} else { // Not selected
							textX = 6;
							blit(0, 0, TOP_WIDTH, 0, TOP_WIDTH, v + MENU_HEIGHT);
						}

						String magic = props.getMagicsList().get(i);
						int cost = ModMagics.registry.getValue(new ResourceLocation(magic)).getCost();
						int colour = props.getMP() > cost ? 0xFFFFFF : 0xFF9900;
						if(props.getRecharge()) {
							colour = 0x888888;
						}
						magic = magic.substring(magic.indexOf(":")+1);
						/*int level = mc.player.getCapability(ModCapabilities.MAGIC_STATE, null).getMagicLevel(magic);
						String magicName = Constants.getMagicName(magic, level);*/
						drawString(mc.fontRenderer, /*Utils.translateToLocal(magicName)*/magic, textX, 4, colour);
						GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
					}
				}
				GL11.glPopMatrix();
			}
		}
	}

	public void drawTop(int width, int height) {
		GL11.glPushMatrix();
		{
			GL11.glColor4ub((byte) 255, (byte) 255, (byte) 255, (byte) alpha);
			mc.textureManager.bindTexture(texture);
			GL11.glTranslatef(0, (height - MENU_HEIGHT * scale * TOP), 0);
			GL11.glScalef(scale, scale, scale);
			if (submenu != 0)
				GL11.glColor4ub((byte) 80, (byte) 80, (byte) 80, (byte) alpha);

			textX = 0;
			paintWithColorArray(normalModeColor, (byte) alpha);
			blit(0, 0, 0, 0, TOP_WIDTH, TOP_HEIGHT);
			if (submenu == 0) {
				drawString(mc.fontRenderer, I18n.format("COMMANDS"), 6, 4, 0xFFFFFF);
			}
		}
		GL11.glPopMatrix();
	}

	public void drawAttack(int width, int height) {
		GL11.glPushMatrix();
		{
			GL11.glColor4ub((byte) 255, (byte) 255, (byte) 255, (byte) alpha);
			mc.textureManager.bindTexture(texture);

			int u;
			int v = 0;
			int x = 0;

			GL11.glTranslatef(x, (height - MENU_HEIGHT * scale * ATTACK), 0);
			GL11.glScalef(scale, scale, scale);

			if (submenu != 0)
				GL11.glColor4ub((byte) 80, (byte) 80, (byte) 80, (byte) alpha);

			if (selected == ATTACK) { // Selected
				textX = 5;
				paintWithColorArray(normalModeColor, (byte) alpha);

				// Draw slot
				blit(5, 0, TOP_WIDTH, MENU_HEIGHT, TOP_WIDTH, v + MENU_HEIGHT);

				GL11.glColor4ub((byte) 255, (byte) 255, (byte) 255, (byte) alpha);

				// Draw Icon
				// if (organization) {
				// drawTexturedModalRect(60, 2, 140 + ((selected + 1) * iconWidth) - iconWidth,
				// 18, iconWidth, iconWidth);
				// } else {
				blit(60, 2, 140 + (selected * iconWidth) - iconWidth, 18, iconWidth, iconWidth);
				// }

			} else { // Not selected
				textX = 0;
				paintWithColorArray(normalModeColor, (byte) alpha);
				blit(0, 0, TOP_WIDTH, 0, TOP_WIDTH, v + MENU_HEIGHT);
			}

			if (submenu == 0) {
				drawString(mc.fontRenderer, I18n.format("Attack"), 6 + textX, 4, 0xFFFFFF);

				/*
				 * if(Minecraft.getInstance().player.getCapability(ModCapabilities.
				 * ORGANIZATION_XIII, null).getMember() == Utils.OrgMember.XIGBAR) {
				 * if(player.getHeldItemMainhand() != null) {
				 * if(player.getHeldItemMainhand().getItem() instanceof ArrowgunsItem) {
				 * ItemStack weapon = player.getHeldItemMainhand(); if(weapon.hasTagCompound())
				 * { if(weapon.getTagCompound().hasKey("ammo")) { int ammo =
				 * weapon.getTagCompound().getInteger("ammo"); drawString(mc.fontRenderer,
				 * ammo+"", textX+TOP_WIDTH, 4, 0xFFFFFF); } }
				 * 
				 * } } }
				 */
			}

		}
		GL11.glPopMatrix();
	}

	public void drawMagic(int width, int height) {
		GL11.glPushMatrix();
		{
			GL11.glColor4ub((byte) 255, (byte) 255, (byte) 255, (byte) alpha);
			mc.textureManager.bindTexture(texture);

			int u;
			int v = 0;
			int x = 0;

			GL11.glTranslatef(x, (height - MENU_HEIGHT * scale * MAGIC), 0);
			GL11.glScalef(scale, scale, scale);

			if (submenu != 0) {
				GL11.glColor4ub((byte) 80, (byte) 80, (byte) 80, (byte) alpha);
			}
			
			if (selected == MAGIC) { // Selected
				textX = 5;
				paintWithColorArray(normalModeColor, (byte) alpha);
				// Draw slot
				blit(5, 0, TOP_WIDTH, MENU_HEIGHT, TOP_WIDTH, v + MENU_HEIGHT);
				GL11.glColor4ub((byte) 255, (byte) 255, (byte) 255, (byte) alpha);
				blit(60, 2, 140 + (selected * iconWidth) - iconWidth, 18, iconWidth, iconWidth);

			} else { // Not selected
				textX = 0;
				paintWithColorArray(normalModeColor, (byte) alpha);
				blit(0, 0, TOP_WIDTH, 0, TOP_WIDTH, v + MENU_HEIGHT);
			}

			if (submenu == 0) {
				drawString(mc.fontRenderer, I18n.format("Magic"), 6 + textX, 4, 0xFFFFFF);
			}

		}
		GL11.glPopMatrix();
	}

	public void drawItems(int width, int height) {
		GL11.glPushMatrix();
		{
			GL11.glColor4ub((byte) 255, (byte) 255, (byte) 255, (byte) alpha);
			mc.textureManager.bindTexture(texture);

			int u;
			int v = 0;
			int x = 0;

			GL11.glTranslatef(x, (height - MENU_HEIGHT * scale * ITEMS), 0);
			GL11.glScalef(scale, scale, scale);

			if (submenu != 0)
				GL11.glColor4ub((byte) 80, (byte) 80, (byte) 80, (byte) alpha);

			if (selected == ITEMS) { // Selected
				textX = 5;
				paintWithColorArray(normalModeColor, (byte) alpha);
				// Draw slot
				blit(5, 0, TOP_WIDTH, MENU_HEIGHT, TOP_WIDTH, v + MENU_HEIGHT);
				GL11.glColor4ub((byte) 255, (byte) 255, (byte) 255, (byte) alpha);
				blit(60, 2, 140 + (selected * iconWidth) - iconWidth, 18, iconWidth, iconWidth);

			} else { // Not selected
				textX = 0;
				paintWithColorArray(normalModeColor, (byte) alpha);
				blit(0, 0, TOP_WIDTH, 0, TOP_WIDTH, v + MENU_HEIGHT);
			}

			if (submenu == 0) {
				drawString(mc.fontRenderer, I18n.format("Items"), 6 + textX, 4, 0xFFFFFF);
			}

		}
		GL11.glPopMatrix();
	}

	public void drawDrive(int width, int height) {
		GL11.glPushMatrix();
		{
			GL11.glColor4ub((byte) 255, (byte) 255, (byte) 255, (byte) alpha);
			mc.textureManager.bindTexture(texture);

			int u;
			int v = 0;
			int x = 0;

			GL11.glTranslatef(x, (height - MENU_HEIGHT * scale * DRIVE), 0);
			GL11.glScalef(scale, scale, scale);

			if (submenu != 0)
				GL11.glColor4ub((byte) 80, (byte) 80, (byte) 80, (byte) alpha);

			if (selected == DRIVE) { // Selected
				textX = 5;
				paintWithColorArray(normalModeColor, (byte) alpha);
				// Draw slot
				blit(5, 0, TOP_WIDTH, MENU_HEIGHT, TOP_WIDTH, v + MENU_HEIGHT);
				GL11.glColor4ub((byte) 255, (byte) 255, (byte) 255, (byte) alpha);
				blit(60, 2, 140 + (selected * iconWidth) - iconWidth, 18, iconWidth, iconWidth);

			} else { // Not selected
				textX = 0;
				paintWithColorArray(normalModeColor, (byte) alpha);
				blit(0, 0, TOP_WIDTH, 0, TOP_WIDTH, v + MENU_HEIGHT);
			}

			if (submenu == 0 && ModCapabilities.get(mc.player) != null) {
				String text = ModCapabilities.get(mc.player).getActiveDriveForm().equals("")?"Drive":"Revert";
				int color = ModCapabilities.get(mc.player).getActiveDriveForm().equals(Strings.Form_Anti) ? 0x888888 : 0xFFFFFF;
				drawString(mc.fontRenderer, I18n.format(text), 6 + textX, 4, color);
			}
		}
		GL11.glPopMatrix();
	}

	private void drawSubDrive(int width, int height) {
		IPlayerCapabilities props = ModCapabilities.get(mc.player);

		if (props != null && props.getDriveFormsMap() != null && !props.getDriveFormsMap().isEmpty()) {
			// DRIVE TOP
			GL11.glPushMatrix();
			{
				GL11.glColor4ub((byte) 255, (byte) 255, (byte) 255, (byte) alpha);
				mc.textureManager.bindTexture(texture);
				GL11.glTranslatef(5, (height - MENU_HEIGHT * scale * (props.getDriveFormsMap().size() + 1)), 0);
				GL11.glScalef(1.25f, scale, scale);
				if (submenu == SUB_DRIVE) {

					GL11.glColor4ub((byte) 0, (byte) 255, (byte) 255, (byte) alpha);

					blit(0, 0, 0, 0, TOP_WIDTH, TOP_HEIGHT);
				}
			}
			GL11.glPopMatrix();

			GL11.glPushMatrix();
			{
				GL11.glColor4ub((byte) 255, (byte) 255, (byte) 255, (byte) alpha);
				GL11.glTranslatef(5, (height - MENU_HEIGHT * scale * (props.getDriveFormsMap().size() + 1)), 0);
				GL11.glScalef(scale, scale, scale);
				if (submenu == SUB_DRIVE)
					drawString(mc.fontRenderer, /* Utils.translateToLocal(Strings.Gui_CommandMenu_Drive_Title) */ "Drive Forms", 6, 4, 0xFFFFFF);
			}
			GL11.glPopMatrix();

			for (int i = 0; i < props.getDriveFormsMap().size(); i++) {
				String formName = (String) props.getDriveFormsMap().keySet().toArray()[i];
				int cost = ModDriveForms.registry.getValue(new ResourceLocation(formName)).getCost();
				int color = props.getDP() >= cost ? 0xFFFFFF : 0x888888;
				formName = formName.substring(formName.indexOf(":") + 1);
				
				GL11.glPushMatrix();
				{
					GL11.glColor4ub((byte) 255, (byte) 255, (byte) 255, (byte) alpha);
					int u;
					int v;
					int x;
					x = (driveselected == i) ? 10 : 5;
					v = (driveselected == i) ? MENU_HEIGHT : 0;

					mc.textureManager.bindTexture(texture);
					GL11.glTranslatef(x, (height - MENU_HEIGHT * scale * (props.getDriveFormsMap().size() - i)), 0);
					GL11.glScalef(1.25f, scale, scale);

					if (submenu == SUB_DRIVE) {
						v = 0;
						paintWithColorArray(driveMenuColor, (byte) alpha);
						if (driveselected == i) {
							textX = 11;

							// Draw slot
							blit(5, 0, TOP_WIDTH, MENU_HEIGHT, TOP_WIDTH, v + MENU_HEIGHT);

							GL11.glColor4ub((byte) 255, (byte) 255, (byte) 255, (byte) alpha);

							// Draw Icon
							blit(60, 2, 140 + selected * iconWidth - iconWidth, 18, iconWidth, iconWidth);

						} else { // Not selected
							textX = 6;
							blit(0, 0, TOP_WIDTH, 0, TOP_WIDTH, v + MENU_HEIGHT);
						}
					}
				}
				GL11.glPopMatrix();

				GL11.glPushMatrix();
				{
					GL11.glColor4ub((byte) 80, (byte) 80, (byte) 80, (byte) alpha);
					int x;
					x = (driveselected == i) ? 10 : 5;
					GL11.glTranslatef(x, (height - MENU_HEIGHT * scale * (props.getDriveFormsMap().size() - i)), 0);
					GL11.glScalef(scale, scale, scale);
					if (submenu == SUB_DRIVE) {						
						drawString(mc.fontRenderer, formName, textX, 4, color);
					}
					GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

				}
				GL11.glPopMatrix();
			}
		}
	}
}

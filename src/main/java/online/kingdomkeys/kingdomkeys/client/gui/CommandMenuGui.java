package online.kingdomkeys.kingdomkeys.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.ExtendedWorldData;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
import online.kingdomkeys.kingdomkeys.handler.EntityEvents;
import online.kingdomkeys.kingdomkeys.lib.Party.Member;
import online.kingdomkeys.kingdomkeys.lib.PortalCoords;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.lib.Utils;
import online.kingdomkeys.kingdomkeys.magic.ModMagics;

//TODO cleanup
public class CommandMenuGui extends Screen {

	public CommandMenuGui() {
		super(new TranslationTextComponent(""));
		minecraft = Minecraft.getInstance();
	}


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

	public static final int SUB_MAIN = 0, SUB_MAGIC = 1, SUB_ITEMS = 2, SUB_DRIVE = 3, SUB_PORTALS = 4, SUB_ATTACKS = 5, SUB_TARGET = 6;

	public static final int NONE = 0;
	public static int selected = ATTACK, targetSelected = 0;;
	public static int submenu = 0, magicSelected = 0, potionSelected = 0, driveSelected = 0, portalSelected = 0, attackSelected = 0;

	ResourceLocation texture = new ResourceLocation(KingdomKeys.MODID, "textures/gui/commandmenu.png");

	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void onRenderOverlayPost(RenderGameOverlayEvent event) {
		// if (minecraft.player.getCapability(ModCapabilities.PLAYER_STATS, null).getHudMode()) {
		if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {// && !minecraft.ingameGUI.getChatGUI().getChatOpen()) {
			RenderSystem.pushMatrix();
			{
				drawCommandMenu(minecraft.getMainWindow().getScaledWidth(), minecraft.getMainWindow().getScaledHeight());
			}
			RenderSystem.popMatrix();
		}
		// }
	}

	float alpha = 1F;
	float scale = 1.05f;
	float[] orgColor = { 0.8F, 0.8F, 0.8F };
	float[] attackMenuColor = { 1F, 0.8F, 0.7F };
	float[] portalMenuColor = { 0.4F, 0.4F, 0.4F };
	float[] combatModeColor = { 1F, 0F, 0F };
	float[] normalModeColor = { 0.04F, 0.2F, 1F };
	float[] magicMenuColor = { 0.4F, 0F, 1F };
	float[] itemsMenuColor = { 0.3F, 1F, 0.3F };
	float[] driveMenuColor = { 0F, 1F, 1F };

	private void paintWithColorArray(float[] array, float alpha) {
		if (EntityEvents.isHostiles)
			RenderSystem.color4f(combatModeColor[0], combatModeColor[1], combatModeColor[2], (byte) alpha);
		else {
			/*
			 * if (Minecraft.getMinecraft().player.getCapability(ModCapabilities.
			 * ORGANIZATION_XIII, null).getMember() != Utils.OrgMember.NONE && array ==
			 * normalModeColor) { RenderSystem.color4ub(orgColor[0], orgColor[1], orgColor[2],
			 * (byte) alpha); } else {
			 */
			RenderSystem.color4f(normalModeColor[0], normalModeColor[1], normalModeColor[2], (byte) alpha);
			// }
		}

	}

	public void drawCommandMenu(int width, int height) {
		drawTop(width, height);
		drawAttack(width, height);
		drawMagic(width, height);
		drawItems(width, height);
		drawDrive(width, height);
		
		if(ModCapabilities.get(minecraft.player) != null) {
			if (submenu == SUB_PORTALS) {
				drawSubPortals(width, height);
			}
			if (submenu == SUB_MAGIC) {
				drawSubMagic(width, height);
			}
			if (submenu == SUB_DRIVE) {
				drawSubDrive(width, height);
			}
			if (submenu == SUB_TARGET) {
				drawSubTargetSelector(width, height);
			}
		}
	}
	
	private void drawSubTargetSelector(int width, int height) {
		ExtendedWorldData worldData = ExtendedWorldData.get(minecraft.player.world);
		//Title
		RenderSystem.pushMatrix();
		{
			paintWithColorArray(normalModeColor, alpha);
			minecraft.textureManager.bindTexture(texture);
			RenderSystem.translatef(5, (height - MENU_HEIGHT * scale * (worldData.getPartyFromMember(minecraft.player.getUniqueID()).getMembers().size() + 1)), 0);
			RenderSystem.scalef(scale, scale, scale);
			if (submenu == SUB_TARGET) {
				blit(0, 0, 0, 0, TOP_WIDTH, TOP_HEIGHT);
				drawString(minecraft.fontRenderer, Utils.translateToLocal("TARGET"), 6, 4, 0xFFFFFF);
			}
		}
		RenderSystem.popMatrix();

		for (int i = 0; i < worldData.getPartyFromMember(minecraft.player.getUniqueID()).getMembers().size(); i++) {
			RenderSystem.pushMatrix();
			{
				RenderSystem.color4f(1F, 1F, 1F, alpha);
				int u;
				int v;
				int x;
				x = (portalSelected == i) ? 10 : 5;

				minecraft.textureManager.bindTexture(texture);
				RenderSystem.translatef(x, (height - MENU_HEIGHT * scale * (worldData.getPartyFromMember(minecraft.player.getUniqueID()).getMembers().size() - i)), 0);
				RenderSystem.scalef(scale, scale, scale);
				if (submenu == SUB_TARGET) {
					v = 0;
					paintWithColorArray(portalMenuColor, alpha);

					if (targetSelected == i) {
						textX = 11;

						// Draw slot
						blit(5, 0, TOP_WIDTH, MENU_HEIGHT, TOP_WIDTH, v + MENU_HEIGHT);

						RenderSystem.color4f(1, 1F, 1F, alpha);

						// Draw Icon
						blit(60, 2, 140 + ((selected + 1) * iconWidth) - iconWidth, 18, iconWidth, iconWidth);

					} else { // Not selected
						textX = 6;	
						blit(0, 0, TOP_WIDTH, 0, TOP_WIDTH, v + MENU_HEIGHT);
					}
					// colour = Constants.getCost(spells.get(i)) < STATS.getMP() ? 0xFFFFFF :
					// 0xFF9900;

					Member member = worldData.getPartyFromMember(minecraft.player.getUniqueID()).getMembers().get(i);
					// String magicName = Constants.getMagicName(magic, level);
					drawString(minecraft.fontRenderer, member.getUsername(), textX, 4, 0xFFFFFF);
					RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
				}
			}
			RenderSystem.popMatrix();
		}
	}

	private void drawSubMagic(int width, int height) {
		IPlayerCapabilities props = ModCapabilities.get(minecraft.player);
		if (props != null && props.getMagicsList() != null && !props.getMagicsList().isEmpty()) {
			// MAGIC TOP
			RenderSystem.pushMatrix();
			{
				RenderSystem.color4f(1F, 1F, 1F, (byte) alpha);
				minecraft.textureManager.bindTexture(texture);
				RenderSystem.translatef(5, (height - MENU_HEIGHT * scale * (props.getMagicsList().size() + 1)), 0);
				RenderSystem.scalef(scale, scale, scale);
				int v = 0;
				if (submenu == SUB_MAGIC) {

					RenderSystem.color4f(magicMenuColor[0], magicMenuColor[1], magicMenuColor[2], (byte) alpha);

					blit(0, 0, 0, 0, TOP_WIDTH, TOP_HEIGHT);
					// drawTexturedModalRect(0, 0, 0, 0+extraY, TOP_WIDTH, TOP_HEIGHT);
					drawString(minecraft.fontRenderer, /*Utils.translateToLocal(Strings.Gui_CommandMenu_Magic_Title)*/"MAGIC", 6, 4, 0xFFFFFF);
				}
			}
			RenderSystem.popMatrix();
			for (int i = 0; i < props.getMagicsList().size(); i++) {
				RenderSystem.pushMatrix();
				{
					RenderSystem.color4f(1F, 1F, 1F, (byte) alpha);
					int u;
					int v;
					int x;
					x = (magicSelected == i) ? 10 : 5;

					minecraft.textureManager.bindTexture(texture);
					RenderSystem.translatef(x, (height - MENU_HEIGHT * scale * (props.getMagicsList().size() - i)), 0);
					RenderSystem.scalef(scale, scale, scale);
					if (submenu == SUB_MAGIC) {
						v = 0;

						paintWithColorArray(magicMenuColor, (byte) alpha);
						if (magicSelected == i) {
							// drawTexturedModalRect(0, 0, TOP_WIDTH, 15+extraY, TOP_WIDTH + MENU_WIDTH, v +
							// MENU_HEIGHT);
							textX = 12;

							// Draw slot
							blit(5, 0, TOP_WIDTH, MENU_HEIGHT, TOP_WIDTH, v + MENU_HEIGHT);

							RenderSystem.color4f(1F, 1F, 1F, (byte) alpha);

							// Draw Icon
							blit(60, 2, 140 + selected * iconWidth - iconWidth, 18, iconWidth, iconWidth);

						} else { // Not selected
							textX = 6;
							blit(0, 0, TOP_WIDTH, 0, TOP_WIDTH, v + MENU_HEIGHT);
						}

						String magic = props.getMagicsList().get(i);
						int cost = ModMagics.registry.getValue(new ResourceLocation(magic)).getCost();
						int colour = props.getMP() > cost ? 0xFFFFFF : 0xFF9900;
						
						if(props.getMaxMP() == 0 || props.getRecharge() || cost > props.getMaxMP() && cost < 300) {
							colour = 0x888888;
						}
						
						magic = magic.substring(magic.indexOf(":")+1);
						/*int level = minecraft.player.getCapability(ModCapabilities.MAGIC_STATE, null).getMagicLevel(magic);
						String magicName = Constants.getMagicName(magic, level);*/
						drawString(minecraft.fontRenderer, Utils.translateToLocal(magic), textX, 4, colour);
						RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
					}
				}
				RenderSystem.popMatrix();
			}
		}
	}

	public void drawTop(int width, int height) {
		RenderSystem.pushMatrix();
		{
			RenderSystem.color4f(1F, 1F, 1F, (byte) alpha);
			minecraft.textureManager.bindTexture(texture);
			RenderSystem.translatef(0, (height - MENU_HEIGHT * scale * TOP), 0);
			RenderSystem.scalef(scale, scale, scale);
			if (submenu != 0)
				RenderSystem.color4f(0.3F, 0.3F, 0.3F, (byte) alpha);

			textX = 0;
			paintWithColorArray(normalModeColor, (byte) alpha);
			blit(0, 0, 0, 0, TOP_WIDTH, TOP_HEIGHT);
			if (submenu == 0) {
				drawString(minecraft.fontRenderer, I18n.format("COMMANDS"), 6, 4, 0xFFFFFF);
			}
		}
		RenderSystem.popMatrix();
	}

	public void drawAttack(int width, int height) {
		RenderSystem.pushMatrix();
		{
			RenderSystem.color4f(1F, 1F, 1F, (byte) alpha);
			minecraft.textureManager.bindTexture(texture);

			int u;
			int v = 0;
			int x = 0;

			RenderSystem.translatef(x, (height - MENU_HEIGHT * scale * ATTACK), 0);
			RenderSystem.scalef(scale, scale, scale);

			if (submenu != 0)
				RenderSystem.color4f(0.3F, 0.3F, 0.3F, (byte) alpha);

			if (selected == ATTACK) { // Selected
				textX = 5;
				paintWithColorArray(normalModeColor, (byte) alpha);

				// Draw slot
				blit(5, 0, TOP_WIDTH, MENU_HEIGHT, TOP_WIDTH, v + MENU_HEIGHT);

				RenderSystem.color4f(1F, 1F, 1F, (byte) alpha);

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
				drawString(minecraft.fontRenderer, I18n.format("Attack"), 6 + textX, 4, 0xFFFFFF);

				/*
				 * if(Minecraft.getInstance().player.getCapability(ModCapabilities.
				 * ORGANIZATION_XIII, null).getMember() == Utils.OrgMember.XIGBAR) {
				 * if(player.getHeldItemMainhand() != null) {
				 * if(player.getHeldItemMainhand().getItem() instanceof ArrowgunsItem) {
				 * ItemStack weapon = player.getHeldItemMainhand(); if(weapon.hasTagCompound())
				 * { if(weapon.getTagCompound().hasKey("ammo")) { int ammo =
				 * weapon.getTagCompound().getInteger("ammo"); drawString(minecraft.fontRenderer,
				 * ammo+"", textX+TOP_WIDTH, 4, 0xFFFFFF); } }
				 * 
				 * } } }
				 */
			}

		}
		RenderSystem.popMatrix();
	}

	public void drawMagic(int width, int height) {
		RenderSystem.pushMatrix();
		{
			RenderSystem.color4f(1F, 1F, 1F, (byte) alpha);
			minecraft.textureManager.bindTexture(texture);

			int u;
			int v = 0;
			int x = 0;

			RenderSystem.translatef(x, (height - MENU_HEIGHT * scale * MAGIC), 0);
			RenderSystem.scalef(scale, scale, scale);

			if (submenu != 0) {
				RenderSystem.color4f(0.3F, 0.3F, 0.3F, (byte) alpha);
			}
			
			if (selected == MAGIC) { // Selected
				textX = 5;
				paintWithColorArray(normalModeColor, (byte) alpha);
				// Draw slot
				blit(5, 0, TOP_WIDTH, MENU_HEIGHT, TOP_WIDTH, v + MENU_HEIGHT);
				RenderSystem.color4f(1F, 1F, 1F, (byte) alpha);
				blit(60, 2, 140 + (selected * iconWidth) - iconWidth, 18, iconWidth, iconWidth);

			} else { // Not selected
				textX = 0;
				paintWithColorArray(normalModeColor, (byte) alpha);
				blit(0, 0, TOP_WIDTH, 0, TOP_WIDTH, v + MENU_HEIGHT);
			}

			if (submenu == 0) {
				drawString(minecraft.fontRenderer, I18n.format("Magic"), 6 + textX, 4, 0xFFFFFF);
			}

		}
		RenderSystem.popMatrix();
	}

	public void drawItems(int width, int height) {
		RenderSystem.pushMatrix();
		{
			RenderSystem.color4f(1F, 1F, 1F, (byte) alpha);
			minecraft.textureManager.bindTexture(texture);

			int u;
			int v = 0;
			int x = 0;

			RenderSystem.translatef(x, (height - MENU_HEIGHT * scale * ITEMS), 0);
			RenderSystem.scalef(scale, scale, scale);

			if (submenu != 0)
				RenderSystem.color4f(0.3F, 0.3F, 0.3F, (byte) alpha);

			if (selected == ITEMS) { // Selected
				textX = 5;
				paintWithColorArray(normalModeColor, (byte) alpha);
				// Draw slot
				blit(5, 0, TOP_WIDTH, MENU_HEIGHT, TOP_WIDTH, v + MENU_HEIGHT);
				RenderSystem.color4f(1F, 1F, 1F, (byte) alpha);
				blit(60, 2, 140 + (selected * iconWidth) - iconWidth, 18, iconWidth, iconWidth);

			} else { // Not selected
				textX = 0;
				paintWithColorArray(normalModeColor, (byte) alpha);
				blit(0, 0, TOP_WIDTH, 0, TOP_WIDTH, v + MENU_HEIGHT);
			}

			if (submenu == 0) {
				drawString(minecraft.fontRenderer, I18n.format("Items"), 6 + textX, 4, 0xFFFFFF);
			}

		}
		RenderSystem.popMatrix();
	}

	public void drawDrive(int width, int height) {
		RenderSystem.pushMatrix();
		{
			RenderSystem.color4f(1F, 1F, 1F, (byte) alpha);
			minecraft.textureManager.bindTexture(texture);

			int u;
			int v = 0;
			int x = 0;

			RenderSystem.translatef(x, (height - MENU_HEIGHT * scale * DRIVE), 0);
			RenderSystem.scalef(scale, scale, scale);

			if (submenu != 0)
				RenderSystem.color4f(0.3F, 0.3F, 0.3F, alpha);

			if (selected == DRIVE) { // Selected
				textX = 5;
				paintWithColorArray(normalModeColor, alpha);
				// Draw slot
				blit(5, 0, TOP_WIDTH, MENU_HEIGHT, TOP_WIDTH, v + MENU_HEIGHT);
				RenderSystem.color4f(1F, 1F, 1F, alpha);
				blit(60, 2, 140 + (selected * iconWidth) - iconWidth, 18, iconWidth, iconWidth);

			} else { // Not selected
				textX = 0;
				paintWithColorArray(normalModeColor, alpha);
				blit(0, 0, TOP_WIDTH, 0, TOP_WIDTH, v + MENU_HEIGHT);
			}

			if (submenu == 0 && ModCapabilities.get(minecraft.player) != null) {
				String text = ModCapabilities.get(minecraft.player).getActiveDriveForm().equals("")?"Drive":"Revert";
				int color = ModCapabilities.get(minecraft.player).getActiveDriveForm().equals(Strings.Form_Anti) ? 0x888888 : 0xFFFFFF;
				drawString(minecraft.fontRenderer, I18n.format(text), 6 + textX, 4, color);
			}
		}
		RenderSystem.popMatrix();
	}
	public void drawSubPortals(int width, int height) {
		if (ModCapabilities.get(minecraft.player).getPortalList() != null && !ModCapabilities.get(minecraft.player).getPortalList().isEmpty()) {
			// PORTAL TOP
			RenderSystem.pushMatrix();
			{
				paintWithColorArray(portalMenuColor, alpha);
				minecraft.textureManager.bindTexture(texture);
				RenderSystem.translatef(5, (height - MENU_HEIGHT * scale * (ModCapabilities.get(minecraft.player).getPortalList().size() + 1)), 0);
				RenderSystem.scalef(scale, scale, scale);
				if (submenu == SUB_PORTALS) {
					blit(0, 0, 0, 0, TOP_WIDTH, TOP_HEIGHT);
					drawString(minecraft.fontRenderer, Utils.translateToLocal(Strings.Gui_CommandMenu_Portals_Title), 6, 4, 0xFFFFFF);
				}
			}
			RenderSystem.popMatrix();
	
			for (int i = 0; i < ModCapabilities.get(minecraft.player).getPortalList().size(); i++) {
				RenderSystem.pushMatrix();
				{
					RenderSystem.color4f(1F, 1F, 1F, alpha);
					int u;
					int v;
					int x;
					x = (portalSelected == i) ? 10 : 5;
	
					minecraft.textureManager.bindTexture(texture);
					RenderSystem.translatef(x, (height - MENU_HEIGHT * scale * (ModCapabilities.get(minecraft.player).getPortalList().size() - i)), 0);
					RenderSystem.scalef(scale, scale, scale);
					if (submenu == SUB_PORTALS) {
						v = 0;
						paintWithColorArray(portalMenuColor, alpha);
	
						if (portalSelected == i) {
							textX = 11;
	
							// Draw slot
							blit(5, 0, TOP_WIDTH, MENU_HEIGHT, TOP_WIDTH, v + MENU_HEIGHT);
	
							RenderSystem.color4f(1, 1F, 1F, alpha);
	
							// Draw Icon
							blit(60, 2, 140 + ((selected + 1) * iconWidth) - iconWidth, 18, iconWidth, iconWidth);
	
						} else { // Not selected
							textX = 6;	
							blit(0, 0, TOP_WIDTH, 0, TOP_WIDTH, v + MENU_HEIGHT);
						}
						// colour = Constants.getCost(spells.get(i)) < STATS.getMP() ? 0xFFFFFF :
						// 0xFF9900;
	
						PortalCoords portal = ModCapabilities.get(minecraft.player).getPortalList().get(i);
						// String magicName = Constants.getMagicName(magic, level);
						drawString(minecraft.fontRenderer, Utils.translateToLocal(portal.getShortCoords() + ""), textX, 4, 0xFFFFFF);
						RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
					}
				}
				RenderSystem.popMatrix();
			}
		}
	}

	private void drawSubDrive(int width, int height) {
		IPlayerCapabilities props = ModCapabilities.get(minecraft.player);

		if (props != null && props.getDriveFormsMap() != null && !props.getDriveFormsMap().isEmpty()) {
			// DRIVE TOP
			RenderSystem.pushMatrix();
			{
				RenderSystem.color4f(1F, 1F, 1F, alpha);
				minecraft.textureManager.bindTexture(texture);
				RenderSystem.translatef(5, (height - MENU_HEIGHT * scale * (props.getDriveFormsMap().size() + 1)), 0);
				RenderSystem.scalef(1.25f, scale, scale);
				if (submenu == SUB_DRIVE) {
					RenderSystem.color4f(0, 1F, 1F, alpha);
					blit(0, 0, 0, 0, TOP_WIDTH, TOP_HEIGHT);
				}
			}
			RenderSystem.popMatrix();

			RenderSystem.pushMatrix();
			{
				RenderSystem.color4f(1F, 1F, 1F, (byte) alpha);
				RenderSystem.translatef(5, (height - MENU_HEIGHT * scale * (props.getDriveFormsMap().size() + 1)), 0);
				RenderSystem.scalef(scale, scale, scale);
				if (submenu == SUB_DRIVE)
					drawString(minecraft.fontRenderer, /* Utils.translateToLocal(Strings.Gui_CommandMenu_Drive_Title) */ "Drive Forms", 6, 4, 0xFFFFFF);
			}
			RenderSystem.popMatrix();

			for (int i = 0; i < props.getDriveFormsMap().size(); i++) {
				String formName = (String) props.getDriveFormsMap().keySet().toArray()[i];
				int cost = ModDriveForms.registry.getValue(new ResourceLocation(formName)).getDriveCost();
				int color = props.getDP() >= cost ? 0xFFFFFF : 0x888888;
				formName = formName.substring(formName.indexOf(":") + 1);
				
				RenderSystem.pushMatrix();
				{
					RenderSystem.color4f(1F, 1F, 1F, (byte) alpha);
					int u;
					int v;
					int x;
					x = (driveSelected == i) ? 10 : 5;
					v = (driveSelected == i) ? MENU_HEIGHT : 0;

					minecraft.textureManager.bindTexture(texture);
					RenderSystem.translatef(x, (height - MENU_HEIGHT * scale * (props.getDriveFormsMap().size() - i)), 0);
					RenderSystem.scalef(1.25f, scale, scale);

					if (submenu == SUB_DRIVE) {
						v = 0;
						paintWithColorArray(driveMenuColor, (byte) alpha);
						if (driveSelected == i) {
							textX = 11;

							// Draw slot
							blit(5, 0, TOP_WIDTH, MENU_HEIGHT, TOP_WIDTH, v + MENU_HEIGHT);

							RenderSystem.color4f(1F, 1F, 1F, (byte) alpha);

							// Draw Icon
							blit(60, 2, 140 + selected * iconWidth - iconWidth, 18, iconWidth, iconWidth);

						} else { // Not selected
							textX = 6;
							blit(0, 0, TOP_WIDTH, 0, TOP_WIDTH, v + MENU_HEIGHT);
						}
					}
				}
				RenderSystem.popMatrix();

				RenderSystem.pushMatrix();
				{
					RenderSystem.color4f(0.3F, 0.3F, 0.3F, alpha);
					int x;
					x = (driveSelected == i) ? 10 : 5;
					RenderSystem.translatef(x, (height - MENU_HEIGHT * scale * (props.getDriveFormsMap().size() - i)), 0);
					RenderSystem.scalef(scale, scale, scale);
					if (submenu == SUB_DRIVE) {						
						drawString(minecraft.fontRenderer, Utils.translateToLocal(formName), textX, 4, color);
					}
					RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);

				}
				RenderSystem.popMatrix();
			}
		}
	}
}

package online.kingdomkeys.kingdomkeys.client.gui;

import java.awt.Color;
import java.util.LinkedHashMap;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.IWorldCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
import online.kingdomkeys.kingdomkeys.handler.EntityEvents;
import online.kingdomkeys.kingdomkeys.item.organization.ArrowgunItem;
import online.kingdomkeys.kingdomkeys.lib.Party.Member;
import online.kingdomkeys.kingdomkeys.lib.PortalData;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.kingdomkeys.kingdomkeys.magic.ModMagic;
import online.kingdomkeys.kingdomkeys.util.Utils;

//TODO cleanup
public class CommandMenuGui extends Screen {

	public CommandMenuGui() {
		super(new TranslationTextComponent(""));
		minecraft = Minecraft.getInstance();
	}


	public static final int TOP = 5, ATTACK = 4, MAGIC = 3, ITEMS = 2, DRIVE = 1;

	int TOP_WIDTH = 70;
	int TOP_HEIGHT = 15;
	int MENU_WIDTH = 71;
	int MENU_HEIGHT = 15;
	int iconWidth = 10;
	int textX = 0;

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
	//		{ color when seen fully, SubMenu when it should be seen fully }
	float[] normalModeColor = { 0.04F, 0.2F, 1F, SUB_MAIN };
	float[] portalMenuColor = { 0.8F, 0.8F, 0.8F, SUB_PORTALS};
	float[] combatModeColor = { 1F, 0F, 0F, SUB_MAIN };
	float[] magicMenuColor = { 0.4F, 0F, 1F, SUB_MAGIC };
	float[] itemsMenuColor = { 0.3F, 1F, 0.3F, SUB_ITEMS };
	float[] driveMenuColor = { 0F, 1F, 1F, SUB_DRIVE };
	float[] targetModeColor = { 0.04F, 0.2F, 1F, SUB_TARGET};


	private int getColor(int colour, int sub) {
		if(submenu == sub) {
			return colour;
		} else {
			Color c = Color.decode(String.valueOf(colour));
			return c.darker().darker().getRGB();
		}
	}
	
	private void paintWithColorArray(float[] array, float alpha) {
		if (EntityEvents.isHostiles) { //Red
			if(submenu == array[3]) {
				RenderSystem.color4f(combatModeColor[0], combatModeColor[1], combatModeColor[2], alpha);
			} else {
				RenderSystem.color4f(combatModeColor[0] / 2, array[1] / 2, combatModeColor[2] / 2, alpha);
			}
		} else { //Blue/color
			if (ModCapabilities.getPlayer(Minecraft.getInstance().player).getAlignment() != Utils.OrgMember.NONE && array == normalModeColor) {
				if (submenu == array[3]) {
					RenderSystem.color4f(orgColor[0], orgColor[1], orgColor[2], alpha);
				} else {
					RenderSystem.color4f(orgColor[0] / 2, orgColor[1] / 2, orgColor[2] / 2, alpha);
				}
			} else {
				if (submenu == array[3]) {
					RenderSystem.color4f(array[0], array[1], array[2], alpha);
				} else {
					RenderSystem.color4f(array[0] / 2, array[1] / 2, array[2] / 2, alpha);
				}
			}
		}
	}

	public void drawCommandMenu(int width, int height) {
		if(ModCapabilities.getPlayer(minecraft.player) != null) {
			drawTop(width, height);
			drawAttack(width, height);
			drawMagic(width, height);
			drawItems(width, height);
			drawDrive(width, height);
		
			if (submenu == SUB_PORTALS) {
				drawSubPortals(width, height);
			}
			if (submenu == SUB_MAGIC || submenu == SUB_TARGET) {
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
		IWorldCapabilities worldData = ModCapabilities.getWorld(minecraft.world);
		if(worldData.getPartyFromMember(minecraft.player.getUniqueID()) == null) {
			submenu = SUB_MAGIC;
			return;
		}

		//Title
		RenderSystem.pushMatrix();
		{
			paintWithColorArray(targetModeColor, alpha);
			minecraft.textureManager.bindTexture(texture);
			RenderSystem.translatef(20, (height - MENU_HEIGHT * scale * (worldData.getPartyFromMember(minecraft.player.getUniqueID()).getMembers().size() + 1)), 0);
			RenderSystem.scalef(scale, scale, scale);
			blit(0, 0, 0, 0, TOP_WIDTH, TOP_HEIGHT);
			drawString(minecraft.fontRenderer, Utils.translateToLocal("TARGET"), 5 + ModConfigs.cmTextXOffset, 4, 0xFFFFFF);
			
		}
		RenderSystem.popMatrix();

		for (int i = 0; i < worldData.getPartyFromMember(minecraft.player.getUniqueID()).getMembers().size(); i++) {
			RenderSystem.pushMatrix();
			{
				RenderSystem.color4f(1F, 1F, 1F, alpha);
				int u;
				int v;
				int x;
				x = 20;

				minecraft.textureManager.bindTexture(texture);
				RenderSystem.translatef(x, (height - MENU_HEIGHT * scale * (worldData.getPartyFromMember(minecraft.player.getUniqueID()).getMembers().size() - i)), 0);
				RenderSystem.scalef(scale, scale, scale);
				if (submenu == SUB_TARGET) {
					v = 0;
					paintWithColorArray(targetModeColor, alpha);

					if (targetSelected == i) {
						textX = 10 + ModConfigs.cmTextXOffset;

						// Draw slot
						blit(5, 0, TOP_WIDTH, MENU_HEIGHT, TOP_WIDTH, v + MENU_HEIGHT);

						RenderSystem.color4f(1, 1F, 1F, alpha);

						// Draw Icon
						blit(60, 2, 140 + ((selected + 1) * iconWidth) - iconWidth, 18, iconWidth, iconWidth);

					} else { // Not selected
						textX = 5 + ModConfigs.cmTextXOffset;
						blit(0, 0, TOP_WIDTH, 0, TOP_WIDTH, v + MENU_HEIGHT);
					}
					// colour = Constants.getCost(spells.get(i)) < STATS.getMP() ? 0xFFFFFF :
					// 0xFF9900;

					Member member = worldData.getPartyFromMember(minecraft.player.getUniqueID()).getMembers().get(i);
					// String magicName = Constants.getMagicName(magic, level);
					if(minecraft.world.getPlayerByUuid(member.getUUID()) != null) {
						drawString(minecraft.fontRenderer, member.getUsername(), textX, 4, 0xFFFFFF);
					} else {
						drawString(minecraft.fontRenderer, member.getUsername(), textX, 4, 0x888888);
					}
					RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
				}
			}
			RenderSystem.popMatrix();
		}
	}

	public void drawTop(int width, int height) {
		RenderSystem.pushMatrix();
		{
			RenderSystem.color4f(1F, 1F, 1F, alpha);
			minecraft.textureManager.bindTexture(texture);
			RenderSystem.translatef(0, (height - MENU_HEIGHT * scale * TOP), 0);
			RenderSystem.scalef(scale, scale, scale);
			textX = 0;
			paintWithColorArray(normalModeColor, alpha);
			blit(0, 0, 0, 0, TOP_WIDTH, TOP_HEIGHT);
			if (ModConfigs.cmHeaderTextVisible)
				drawString(minecraft.fontRenderer, Utils.translateToLocal(Strings.Gui_CommandMenu_Command), 6, 4, getColor(0xFFFFFF, SUB_MAIN));
		}
		RenderSystem.popMatrix();
	}

	public void drawAttack(int width, int height) {
		RenderSystem.pushMatrix();
		{
			RenderSystem.color4f(1F, 1F, 1F, alpha);
			minecraft.textureManager.bindTexture(texture);

			int v = 0;
			int x = 0;

			RenderSystem.translatef(x, (height - MENU_HEIGHT * scale * ATTACK), 0);
			RenderSystem.scalef(scale, scale, scale);

			paintWithColorArray(normalModeColor, alpha);
			ClientPlayerEntity player = Minecraft.getInstance().player;
			if (selected == ATTACK) { // Selected
				textX = 5 + ModConfigs.cmTextXOffset;
				blit(5, 0, TOP_WIDTH, MENU_HEIGHT, TOP_WIDTH, v + MENU_HEIGHT);
				RenderSystem.color4f(1F, 1F, 1F, alpha);

				// Draw Icon
				if (ModCapabilities.getPlayer(player).getAlignment() != Utils.OrgMember.NONE) {
					blit(60, 2, 140 + ((selected + 1) * iconWidth) - iconWidth, 18, iconWidth, iconWidth);
				} else {
					blit(60, 2, 140 + (selected * iconWidth) - iconWidth, 18, iconWidth, iconWidth);
				}

			} else { // Not selected
				textX = ModConfigs.cmTextXOffset;
				blit(0, 0, TOP_WIDTH, 0, TOP_WIDTH, v + MENU_HEIGHT);
			}

			drawString(minecraft.fontRenderer, Utils.translateToLocal(Strings.Gui_CommandMenu_Attack), 6 + textX, 4, getColor(0xFFFFFF,SUB_MAIN));
			
			
			if (player.getHeldItemMainhand() != null) {
				if (player.getHeldItemMainhand().getItem() instanceof ArrowgunItem) {
					ItemStack weapon = player.getHeldItemMainhand();
					if (weapon.hasTag()) {
						if (weapon.getTag().contains("ammo")) {
							int ammo = weapon.getTag().getInt("ammo");
							drawString(minecraft.fontRenderer, ammo + "", textX + TOP_WIDTH, 4, 0xFFFFFF);
						}
					}

				}
			}			 
		}
		RenderSystem.popMatrix();
	}

	public void drawMagic(int width, int height) {
		RenderSystem.pushMatrix();
		{
			RenderSystem.color4f(1F, 1F, 1F, alpha);
			minecraft.textureManager.bindTexture(texture);

			int v = 0;
			int x = 0;

			RenderSystem.translatef(x, (height - MENU_HEIGHT * scale * MAGIC), 0);
			RenderSystem.scalef(scale, scale, scale);

			if (submenu != 0) {
				RenderSystem.color4f(0.3F, 0.3F, 0.3F, alpha);
			}
			
			paintWithColorArray(normalModeColor, alpha);
			if (selected == MAGIC) { // Selected
				textX = 5 + ModConfigs.cmTextXOffset;
				// Draw slot
				blit(5, 0, TOP_WIDTH, MENU_HEIGHT, TOP_WIDTH, v + MENU_HEIGHT);
				RenderSystem.color4f(1F, 1F, 1F, alpha);
				blit(60, 2, 140 + (selected * iconWidth) - iconWidth, 18, iconWidth, iconWidth);
			} else { // Not selected
				textX = ModConfigs.cmTextXOffset;
				blit(0, 0, TOP_WIDTH, 0, TOP_WIDTH, v + MENU_HEIGHT);
			}
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);

			drawString(minecraft.fontRenderer, Utils.translateToLocal(Strings.Gui_CommandMenu_Magic), 6 + textX, 4, playerData.getMagicList().isEmpty() || playerData.getMaxMP() == 0 ? 0x888888 :getColor(0xFFFFFF,SUB_MAIN));
			

		}
		RenderSystem.popMatrix();
	}

	public void drawItems(int width, int height) {
		RenderSystem.pushMatrix();
		{
			RenderSystem.color4f(1F, 1F, 1F, alpha);
			minecraft.textureManager.bindTexture(texture);

			int u;
			int v = 0;
			int x = 0;

			RenderSystem.translatef(x, (height - MENU_HEIGHT * scale * ITEMS), 0);
			RenderSystem.scalef(scale, scale, scale);

			if (submenu != 0)
				RenderSystem.color4f(0.3F, 0.3F, 0.3F, alpha);

			paintWithColorArray(normalModeColor, alpha);
			if (selected == ITEMS) { // Selected
				textX = 5 + ModConfigs.cmTextXOffset;
				// Draw slot
				blit(5, 0, TOP_WIDTH, MENU_HEIGHT, TOP_WIDTH, v + MENU_HEIGHT);
				RenderSystem.color4f(1F, 1F, 1F, alpha);
				blit(60, 2, 140 + (selected * iconWidth) - iconWidth, 18, iconWidth, iconWidth);
			} else { // Not selected
				textX = ModConfigs.cmTextXOffset;
				blit(0, 0, TOP_WIDTH, 0, TOP_WIDTH, v + MENU_HEIGHT);
			}

			drawString(minecraft.fontRenderer, Utils.translateToLocal(Strings.Gui_CommandMenu_Items), 6 + textX, 4, getColor(0xFFFFFF,SUB_MAIN));

		}
		RenderSystem.popMatrix();
	}

	public void drawDrive(int width, int height) {
		RenderSystem.pushMatrix();
		{
			RenderSystem.color4f(1F, 1F, 1F, alpha);
			minecraft.textureManager.bindTexture(texture);

			int v = 0;
			int x = 0;

			RenderSystem.translatef(x, (height - MENU_HEIGHT * scale * DRIVE), 0);
			RenderSystem.scalef(scale, scale, scale);

			if (submenu != 0)
				RenderSystem.color4f(0.3F, 0.3F, 0.3F, alpha);

			paintWithColorArray(normalModeColor, alpha);
			if (selected == DRIVE) { // Selected
				textX = 5 + ModConfigs.cmTextXOffset;
				// Draw slot
				blit(5, 0, TOP_WIDTH, MENU_HEIGHT, TOP_WIDTH, v + MENU_HEIGHT);
				RenderSystem.color4f(1F, 1F, 1F, alpha);
				blit(60, 2, 140 + (selected * iconWidth) - iconWidth, 18, iconWidth, iconWidth);
			} else { // Not selected
				textX = ModConfigs.cmTextXOffset;
				blit(0, 0, TOP_WIDTH, 0, TOP_WIDTH, v + MENU_HEIGHT);
			}

			IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);
			if (playerData != null) {
				String text = playerData.getActiveDriveForm().equals(DriveForm.NONE.toString())?".drive":".revert";
				int color = playerData.getActiveDriveForm().equals(Strings.Form_Anti) || playerData.getDriveFormMap().size() <= 1 ? 0x888888 : 0xFFFFFF;
				drawString(minecraft.fontRenderer, Utils.translateToLocal("gui.commandmenu"+text), 6 + textX, 4, getColor(color,SUB_MAIN));
			}
		}
		RenderSystem.popMatrix();
	}
	public void drawSubPortals(int width, int height) {
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);
		if (playerData.getPortalList() != null && !playerData.getPortalList().isEmpty()) {
			// PORTAL TOP
			RenderSystem.pushMatrix();
			{
				paintWithColorArray(portalMenuColor, alpha);
				minecraft.textureManager.bindTexture(texture);
				RenderSystem.translatef(10, (height - MENU_HEIGHT * scale * (playerData.getPortalList().size() + 1)), 0);
				RenderSystem.scalef(scale, scale, scale);
				blit(0, 0, 0, 0, TOP_WIDTH, TOP_HEIGHT);
				drawString(minecraft.fontRenderer, Utils.translateToLocal(Strings.Gui_CommandMenu_Portals_Title), 6, 4, 0xFFFFFF);
			}
			RenderSystem.popMatrix();
	
			for (int i = 0; i < playerData.getPortalList().size(); i++) {
				RenderSystem.pushMatrix();
				{
					RenderSystem.color4f(1F, 1F, 1F, alpha);
					int u;
					int v;
					int x;
					x = (portalSelected == i) ? 15 : 10;
	
					minecraft.textureManager.bindTexture(texture);
					RenderSystem.translatef(x, (height - MENU_HEIGHT * scale * (playerData.getPortalList().size() - i)), 0);
					RenderSystem.scalef(scale, scale, scale);
					if (submenu == SUB_PORTALS) {
						v = 0;
						paintWithColorArray(portalMenuColor, alpha);
	
						if (portalSelected == i) {
							textX = 10 + ModConfigs.cmTextXOffset;
	
							// Draw slot
							blit(5, 0, TOP_WIDTH, MENU_HEIGHT, TOP_WIDTH, v + MENU_HEIGHT);
	
							RenderSystem.color4f(1, 1F, 1F, alpha);
	
							// Draw Icon
							blit(60, 2, 140 + ((selected + 1) * iconWidth) - iconWidth, 18, iconWidth, iconWidth);
	
						} else { // Not selected
							textX = 5 + ModConfigs.cmTextXOffset;
							blit(0, 0, TOP_WIDTH, 0, TOP_WIDTH, v + MENU_HEIGHT);
						}
						// colour = Constants.getCost(spells.get(i)) < STATS.getMP() ? 0xFFFFFF :
						// 0xFF9900;
	
						PortalData portal = playerData.getPortalList().get(i);
						// String magicName = Constants.getMagicName(magic, level);
						drawString(minecraft.fontRenderer, Utils.translateToLocal(portal.getShortCoords() + ""), textX, 4, 0xFFFFFF);
						RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
					}
				}
				RenderSystem.popMatrix();
			}
		}
	}

	private void drawSubMagic(int width, int height) {
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);
		if (playerData != null && playerData.getMagicList() != null && !playerData.getMagicList().isEmpty()) {
			// MAGIC TOP
			RenderSystem.pushMatrix();
			{
				//	RenderSystem.color4f(1F, 1F, 1F, alpha);
				minecraft.textureManager.bindTexture(texture);
				RenderSystem.translatef(10, (height - MENU_HEIGHT * scale * (playerData.getMagicList().size() + 1)), 0);
				RenderSystem.scalef(scale, scale, scale);
				paintWithColorArray(magicMenuColor, alpha);
				blit(0, 0, 0, 0, TOP_WIDTH, TOP_HEIGHT);
				// drawTexturedModalRect(0, 0, 0, 0+extraY, TOP_WIDTH, TOP_HEIGHT);
				drawString(minecraft.fontRenderer, Utils.translateToLocal(Strings.Gui_CommandMenu_Magic_Title), 6, 4, getColor(0xFFFFFF,SUB_MAGIC));

			}
			RenderSystem.popMatrix();
			for (int i = 0; i < playerData.getMagicList().size(); i++) {
				RenderSystem.pushMatrix();
				{
					RenderSystem.color4f(1F, 1F, 1F, alpha);
					int u;
					int v;
					int x;
					x = 10;

					minecraft.textureManager.bindTexture(texture);
					RenderSystem.translatef(x, (height - MENU_HEIGHT * scale * (playerData.getMagicList().size() - i)), 0);
					RenderSystem.scalef(scale, scale, scale);
					v = 0;

					paintWithColorArray(magicMenuColor, alpha);
					if (magicSelected == i) {
						// drawTexturedModalRect(0, 0, TOP_WIDTH, 15+extraY, TOP_WIDTH + MENU_WIDTH, v +
						// MENU_HEIGHT);
						textX = 10 + ModConfigs.cmTextXOffset;

						// Draw slot
						blit(5, 0, TOP_WIDTH, MENU_HEIGHT, TOP_WIDTH, v + MENU_HEIGHT);

						RenderSystem.color4f(1F, 1F, 1F, alpha);

						// Draw Icon
						blit(60, 2, 140 + selected * iconWidth - iconWidth, 18, iconWidth, iconWidth);

					} else { // Not selected
						textX = 5 + ModConfigs.cmTextXOffset;
						blit(0, 0, TOP_WIDTH, 0, TOP_WIDTH, v + MENU_HEIGHT);
					}

					String magic = playerData.getMagicList().get(i);
					Magic magicInstance = ModMagic.registry.getValue(new ResourceLocation(magic));
					int cost = magicInstance.getCost();
					int colour = playerData.getMP() > cost ? 0xFFFFFF : 0xFF9900;

					if(playerData.getMaxMP() == 0 || playerData.getRecharge() || cost > playerData.getMaxMP() && cost < 300) {
						colour = 0x888888;
					}

					magic = magicInstance.getTranslationKey();

					drawString(minecraft.fontRenderer, Utils.translateToLocal(magic), textX, 4, getColor(colour, SUB_MAGIC));
					RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);

				}
				RenderSystem.popMatrix();
			}
		}
	}

	private void drawSubDrive(int width, int height) {
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);

		LinkedHashMap<String, int[]> forms = Utils.getSortedDriveForms(playerData.getDriveFormMap());
		forms.remove(DriveForm.NONE.toString());
		
		if (playerData != null && forms != null && !forms.isEmpty()) {
			// DRIVE TOP
			RenderSystem.pushMatrix();
			{
				paintWithColorArray(driveMenuColor, alpha);
				minecraft.textureManager.bindTexture(texture);
				RenderSystem.translatef(10, (height - MENU_HEIGHT * scale * (forms.size()+1)), 0);
				RenderSystem.scalef(scale, scale, scale);
				blit(0, 0, 0, 0, TOP_WIDTH, TOP_HEIGHT);
				drawString(minecraft.fontRenderer, Utils.translateToLocal(Strings.Gui_CommandMenu_Drive_Title), 5 + ModConfigs.cmTextXOffset, 4, 0xFFFFFF);
			}
			RenderSystem.popMatrix();

			for (int i = 0; i < forms.size(); i++) {
				String formName = (String) forms.keySet().toArray()[i];
				if (!formName.equals(DriveForm.NONE.toString())) {
					DriveForm form = ModDriveForms.registry.getValue(new ResourceLocation(formName));
					int cost = form.getDriveCost();
					int color = playerData.getDP() >= cost ? 0xFFFFFF : 0x888888;
					formName = form.getTranslationKey();

					RenderSystem.pushMatrix();
					{
						RenderSystem.color4f(1F, 1F, 1F, alpha);
						int u;
						int v;
						int x;
						x = (driveSelected == i) ? 15 : 10;
						v = (driveSelected == i) ? MENU_HEIGHT : 0;

						minecraft.textureManager.bindTexture(texture);
						RenderSystem.translatef(x, (height - MENU_HEIGHT * scale * (forms.size() - i)), 0);
						RenderSystem.scalef(scale, scale, scale);

						if (submenu == SUB_DRIVE) {
							v = 0;
							paintWithColorArray(driveMenuColor, alpha);
							if (driveSelected == i) {
								textX = 10 + ModConfigs.cmTextXOffset;

								// Draw slot
								blit(5, 0, TOP_WIDTH, MENU_HEIGHT, TOP_WIDTH, v + MENU_HEIGHT);

								RenderSystem.color4f(1F, 1F, 1F, alpha);

								// Draw Icon
								blit(60, 2, 140 + selected * iconWidth - iconWidth, 18, iconWidth, iconWidth);

							} else { // Not selected
								textX = 5 + ModConfigs.cmTextXOffset;
								blit(0, 0, TOP_WIDTH, 0, TOP_WIDTH, v + MENU_HEIGHT);
							}
						}
						RenderSystem.color4f(0.3F, 0.3F, 0.3F, alpha);
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
}

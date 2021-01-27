package online.kingdomkeys.kingdomkeys.client.gui;

import java.awt.Color;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

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
import online.kingdomkeys.kingdomkeys.limit.Limit;
import online.kingdomkeys.kingdomkeys.limit.ModLimits;
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.kingdomkeys.kingdomkeys.magic.ModMagic;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.kingdomkeys.kingdomkeys.util.Utils.OrgMember;

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

	public static final int SUB_MAIN = 0, SUB_MAGIC = 1, SUB_ITEMS = 2, SUB_DRIVE = 3, SUB_PORTALS = 4, SUB_ATTACKS = 5, SUB_TARGET = 6, SUB_LIMIT = 7;

	public static final int NONE = 0;
	public static int selected = ATTACK, targetSelected = 0;;
	public static int submenu = 0, magicSelected = 0, potionSelected = 0, driveSelected = 0, portalSelected = 0, attackSelected = 0, limitSelected = 0;

	ResourceLocation texture = new ResourceLocation(KingdomKeys.MODID, "textures/gui/commandmenu.png");

	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void onRenderOverlayPost(RenderGameOverlayEvent event) {
		if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {// && !minecraft.ingameGUI.getChatGUI().getChatOpen()) {
			RenderSystem.pushMatrix();
			{
				drawCommandMenu(minecraft.getMainWindow().getScaledWidth(), minecraft.getMainWindow().getScaledHeight());
			}
			RenderSystem.popMatrix();
		}
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
	float[] limitMenuColor = { 1F, 1F, 0F, SUB_LIMIT };
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
			if (ModCapabilities.getPlayer(minecraft.player).getAlignment() != Utils.OrgMember.NONE && array == normalModeColor) {
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
			RenderSystem.pushMatrix();
			{
				RenderSystem.translated(ModConfigs.cmXPos, 0, 0);
				drawMain(width, height);
			
				if (submenu == SUB_PORTALS) {
					drawSubPortals(width, height);
				}
				if (submenu == SUB_MAGIC || submenu == SUB_TARGET) {
					drawSubMagic(width, height);
				}
				if (submenu == SUB_DRIVE) {
					drawSubDrive(width, height);
				}
				if (submenu == SUB_LIMIT) {
					drawSubLimits(width, height);
				}
				if (submenu == SUB_TARGET) {
					drawSubTargetSelector(width, height);
				}
			}
			RenderSystem.popMatrix();
		}
	}
	
	private String getCommandMenuName(int i) {
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);
		switch(i) {
		case ATTACK:
			return playerData.getAlignment() == OrgMember.NONE ? Strings.Gui_CommandMenu_Attack : Strings.Gui_CommandMenu_Portal;
		case MAGIC:
			return Strings.Gui_CommandMenu_Magic;
		case ITEMS:
			return Strings.Gui_CommandMenu_Items;
		case DRIVE:
			return playerData.getAlignment() == OrgMember.NONE ? (playerData.getActiveDriveForm().equals(DriveForm.NONE.toString()) ? Strings.Gui_CommandMenu_Drive : Strings.Gui_CommandMenu_Drive_Revert) : Strings.Gui_CommandMenu_Limit;
		}
		return "";
	}
	
	public void drawMain(int width, int height) {
		RenderSystem.enableBlend();
		//Header
		RenderSystem.pushMatrix();
		{
			RenderSystem.color4f(1F, 1F, 1F, alpha);
			minecraft.textureManager.bindTexture(texture);
			RenderSystem.translatef(0, (height - MENU_HEIGHT * scale * TOP), 0);
			RenderSystem.scalef(scale, scale, scale);
			textX = 0;
			paintWithColorArray(normalModeColor, alpha);
			drawHeader(Strings.Gui_CommandMenu_Command, SUB_MAIN);
		}
		RenderSystem.popMatrix();
		
		//Slots
		RenderSystem.pushMatrix();
		{
			int x = 0;
			for(int i = 1; i <= ATTACK; i++) {
				RenderSystem.pushMatrix();
				{
					minecraft.textureManager.bindTexture(texture);

					RenderSystem.translatef(x, (height - MENU_HEIGHT * scale * i), 0);
					RenderSystem.scalef(scale, scale, scale);
		
					paintWithColorArray(normalModeColor, alpha);
					ClientPlayerEntity player = minecraft.player;
					if (selected == i) { // Selected
						textX = (int) (10 * ModConfigs.cmXScale / 100D) + ModConfigs.cmTextXOffset;
						drawSelectedSlot();
						if (selected == ATTACK && ModCapabilities.getPlayer(player).getAlignment() != Utils.OrgMember.NONE) {
							drawIcon(selected+1, SUB_MAIN);
						} else {
							drawIcon(selected, SUB_MAIN);
						}
		
					} else { // Not selected
						textX = (int) (5 * ModConfigs.cmXScale / 100D) + ModConfigs.cmTextXOffset;
						drawUnselectedSlot();
					}
					
					IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);
					int color = getColor(0xFFFFFF,SUB_MAIN);
					if(i == MAGIC) {
						color = playerData.getMagicList().isEmpty() || playerData.getMaxMP() == 0 || playerData.getActiveDriveForm().equals(Strings.Form_Valor) ? 0x888888 : getColor(0xFFFFFF,SUB_MAIN);
					}
					if(i == ITEMS) {
						color = getColor(0x888888,SUB_MAIN);
					}
					if(i == DRIVE) {//If it's an org member / in antiform / has no drive unlocked be gray
						color = 0x888888;
						if(playerData.getAlignment() != OrgMember.NONE) {
							Limit limit = null;
					        for(Limit val : ModLimits.registry.getValues()) {
					        	if(val.getOwner() == playerData.getAlignment()) {
					        		limit = val;
					        		break;
					        	}
					        }

							if(limit != null) {
								if(playerData.getDP() >= limit.getLevels().get(0)) {
									color = getColor(0xFFFFFF,SUB_MAIN);
								}
							}
						} else {
							if(playerData.getActiveDriveForm().equals(Strings.Form_Anti) || playerData.getDriveFormMap().size() <= 1) {
								color = 0x888888;
							} else {
								color = getColor(0xFFFFFF,SUB_MAIN);
							}
						}
					}
					
					drawString(minecraft.fontRenderer, Utils.translateToLocal(getCommandMenuName(i)), textX, 4, color);
					
					if(i == ATTACK) {
						if (player.getHeldItemMainhand() != null && player.getHeldItemMainhand().getItem() instanceof ArrowgunItem) {
							ItemStack weapon = player.getHeldItemMainhand();
							if (weapon.hasTag() && weapon.getTag().contains("ammo")) {
								int ammo = weapon.getTag().getInt("ammo");
								drawString(minecraft.fontRenderer, ammo + "", (int) (TOP_WIDTH * (ModConfigs.cmXScale / 100D) + (TOP_WIDTH * (ModConfigs.cmXScale / 100D)) * 0.10), 4, 0xFFFFFF);
							}
						}
					}
				}
				RenderSystem.popMatrix();
			}
			
		}
		RenderSystem.popMatrix();
		
		RenderSystem.disableBlend();
	}
	
	private void drawSubTargetSelector(int width, int height) {
		RenderSystem.enableBlend();
		IWorldCapabilities worldData = ModCapabilities.getWorld(minecraft.world);
		if(worldData.getPartyFromMember(minecraft.player.getUniqueID()) == null) {
			submenu = SUB_MAGIC;
			return;
		}
		double x = 20 * ModConfigs.cmSubXOffset / 100D;

		//Title
		RenderSystem.pushMatrix();
		{
			paintWithColorArray(targetModeColor, alpha);
			minecraft.textureManager.bindTexture(texture);
			RenderSystem.translated(x, (height - MENU_HEIGHT * scale * (worldData.getPartyFromMember(minecraft.player.getUniqueID()).getMembers().size() + 1)), 0);
			RenderSystem.scalef(scale, scale, scale);
			drawHeader("TARGET", SUB_TARGET);
		}
		RenderSystem.popMatrix();

		for (int i = 0; i < worldData.getPartyFromMember(minecraft.player.getUniqueID()).getMembers().size(); i++) {
			RenderSystem.pushMatrix();
			{
				RenderSystem.color4f(1F, 1F, 1F, alpha);
				
				minecraft.textureManager.bindTexture(texture);
				RenderSystem.translated(x, (height - MENU_HEIGHT * scale * (worldData.getPartyFromMember(minecraft.player.getUniqueID()).getMembers().size() - i)), 0);
				RenderSystem.scalef(scale, scale, scale);
				if (submenu == SUB_TARGET) {
					
					paintWithColorArray(targetModeColor, alpha);

					if (targetSelected == i) {
						textX = (int) (10 * ModConfigs.cmXScale / 100D) + ModConfigs.cmTextXOffset;

						// Draw slot
						drawSelectedSlot();
						RenderSystem.color4f(1F, 1F, 1F, alpha);

						// Draw Icon
						drawIcon(selected, SUB_TARGET);

					} else { // Not selected
						textX = (int) (5 * ModConfigs.cmXScale / 100D) + ModConfigs.cmTextXOffset;
						drawUnselectedSlot();
					}

					Member member = worldData.getPartyFromMember(minecraft.player.getUniqueID()).getMembers().get(i);
					int color = minecraft.world.getPlayerByUuid(member.getUUID()) != null && minecraft.player.getDistance(minecraft.world.getPlayerByUuid(member.getUUID())) < ModConfigs.partyRangeLimit ? 0xFFFFFF : 0x888888 ;
					drawString(minecraft.fontRenderer, member.getUsername(), (int)(textX + (ModConfigs.cmXScale / 100F)), 4, color);					
				}
			}
			RenderSystem.popMatrix();
		}
		RenderSystem.disableBlend();
	}

	private void drawHeader(String text, int subMenu) {
		RenderSystem.pushMatrix();
		{
			RenderSystem.scalef(ModConfigs.cmXScale / 100F, 1, 1);
			blit(0, 0, 0, 0, TOP_WIDTH, TOP_HEIGHT);
		}
		RenderSystem.popMatrix();
		if(ModConfigs.cmHeaderTextVisible) {
			drawString(minecraft.fontRenderer, Utils.translateToLocal(text), (int) (5 * ModConfigs.cmXScale / 100D) + ModConfigs.cmTextXOffset, 4, getColor(0xFFFFFF,subMenu));
		}
	}

	private void drawSelectedSlot() {
		RenderSystem.enableBlend();
		RenderSystem.pushMatrix();
		{
			RenderSystem.scalef(ModConfigs.cmXScale / 100F, 1, 1);
			blit(5, 0, TOP_WIDTH, MENU_HEIGHT, TOP_WIDTH, MENU_HEIGHT);
		}
		RenderSystem.popMatrix();
		RenderSystem.disableBlend();
	}


	private void drawUnselectedSlot() {
		RenderSystem.enableBlend();
		RenderSystem.pushMatrix();
		{
			RenderSystem.scalef(ModConfigs.cmXScale / 100F, 1, 1);
			blit(0, 0, TOP_WIDTH, 0, TOP_WIDTH, 0 + MENU_HEIGHT);
		}
		RenderSystem.popMatrix();
		RenderSystem.disableBlend();
	}

	/**
	 * 
	 * @param selected
	 * @param submenu where it should be fully visible (otherwise will be darker)
	 */
	private void drawIcon(int selected, int subMenu) {
		RenderSystem.enableBlend();
		if(subMenu == submenu) {
			RenderSystem.color4f(1F, 1F, 1F, alpha);
		} else {
			RenderSystem.color4f(0.5F, 0.5F, 0.5F, alpha);
		}
		blit((int) (TOP_WIDTH * (ModConfigs.cmXScale / 100D) - (TOP_WIDTH * (ModConfigs.cmXScale / 100D)) * 0.15), 2, 140 + (selected * iconWidth) - iconWidth, 18, iconWidth, iconWidth);
		RenderSystem.disableBlend();
	}


	public void drawTop(int width, int height) {
		RenderSystem.enableBlend();
		RenderSystem.pushMatrix();
		{
			RenderSystem.color4f(1F, 1F, 1F, alpha);
			minecraft.textureManager.bindTexture(texture);
			RenderSystem.translatef(0, (height - MENU_HEIGHT * scale * TOP), 0);
			RenderSystem.scalef(scale, scale, scale);
			textX = 0;
			paintWithColorArray(normalModeColor, alpha);
			drawHeader(Strings.Gui_CommandMenu_Command, SUB_MAIN);
		}
		RenderSystem.popMatrix();
		RenderSystem.disableBlend();
	}

	public void drawSubPortals(int width, int height) {
		RenderSystem.enableBlend();
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);

		IWorldCapabilities worldData = ModCapabilities.getWorld(minecraft.player.world);

        List<UUID> portals = worldData.getAllPortalsFromOwnerID(minecraft.player.getUniqueID());
        
		if (portals != null && !portals.isEmpty()) {			// PORTAL TOP
			double x = 10 * ModConfigs.cmSubXOffset / 100D;
			RenderSystem.pushMatrix();
			{
				paintWithColorArray(portalMenuColor, alpha);
				minecraft.textureManager.bindTexture(texture);
				RenderSystem.translated(x, (height - MENU_HEIGHT * scale * (portals.size() + 1)), 0);
				RenderSystem.scalef(scale, scale, scale);
				drawHeader(Strings.Gui_CommandMenu_Portals_Title, SUB_PORTALS);
			}
			RenderSystem.popMatrix();
	
			for (int i = 0; i < portals.size(); i++) {
				RenderSystem.pushMatrix();
				{
					RenderSystem.color4f(1F, 1F, 1F, alpha);
	
					minecraft.textureManager.bindTexture(texture);
					RenderSystem.translated(x, (height - MENU_HEIGHT * scale * (portals.size() - i)), 0);
					RenderSystem.scalef(scale, scale, scale);
					if (submenu == SUB_PORTALS) {
						paintWithColorArray(portalMenuColor, alpha);
	
						if (portalSelected == i) {
							textX = (int) (10 * ModConfigs.cmXScale / 100D) + ModConfigs.cmTextXOffset;
							drawSelectedSlot();
							drawIcon(selected, SUB_PORTALS);
						} else { // Not selected
							textX = (int) (5 * ModConfigs.cmXScale / 100D) + ModConfigs.cmTextXOffset;
							drawUnselectedSlot();
						}
						
						UUID portalUUID = portals.get(i);
						PortalData portal = worldData.getPortalFromUUID(portalUUID);
						if(portal != null)
							drawString(minecraft.fontRenderer, Utils.translateToLocal(portal.getName()), textX, 4, 0xFFFFFF);
						RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
					}
				}
				RenderSystem.popMatrix();
			}
		}
		RenderSystem.disableBlend();
	}

	private void drawSubMagic(int width, int height) {
		RenderSystem.enableBlend();
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);
		if (playerData != null && playerData.getMagicList() != null && !playerData.getMagicList().isEmpty()) {
			// MAGIC TOP
			double x = 10 * ModConfigs.cmSubXOffset / 100D;
			RenderSystem.pushMatrix();
			{
				minecraft.textureManager.bindTexture(texture);
				RenderSystem.translated(x, (height - MENU_HEIGHT * scale * (playerData.getMagicList().size() + 1)), 0);
				RenderSystem.scalef(scale, scale, scale);
				paintWithColorArray(magicMenuColor, alpha);
				drawHeader(Strings.Gui_CommandMenu_Magic_Title, SUB_MAGIC);
			}
			RenderSystem.popMatrix();
			for (int i = 0; i < playerData.getMagicList().size(); i++) {
				RenderSystem.pushMatrix();
				{
					RenderSystem.color4f(1F, 1F, 1F, alpha);

					minecraft.textureManager.bindTexture(texture);
					RenderSystem.translated(x, (height - MENU_HEIGHT * scale * (playerData.getMagicList().size() - i)), 0);
					RenderSystem.scalef(scale, scale, scale);

					paintWithColorArray(magicMenuColor, alpha);
					if (magicSelected == i) {
						// drawTexturedModalRect(0, 0, TOP_WIDTH, 15+extraY, TOP_WIDTH + MENU_WIDTH, v +
						// MENU_HEIGHT);
						textX = (int) (10 * ModConfigs.cmXScale / 100D) + ModConfigs.cmTextXOffset;

						// Draw slot
						drawSelectedSlot();

						RenderSystem.color4f(1F, 1F, 1F, alpha);

						// Draw Icon
						drawIcon(selected, SUB_MAGIC);

					} else { // Not selected
						textX = (int) (5 * ModConfigs.cmXScale / 100D) + ModConfigs.cmTextXOffset;
						drawUnselectedSlot();
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
		RenderSystem.disableBlend();
	}

	private void drawSubDrive(int width, int height) {
		RenderSystem.enableBlend();
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);

		LinkedHashMap<String, int[]> forms = Utils.getSortedDriveForms(playerData.getDriveFormMap());
		forms.remove(DriveForm.NONE.toString());

		double x = 10 * ModConfigs.cmSubXOffset / 100D;

		if (playerData != null && forms != null && !forms.isEmpty()) {
			// DRIVE TOP
			RenderSystem.pushMatrix();
			{
				paintWithColorArray(driveMenuColor, alpha);
				minecraft.textureManager.bindTexture(texture);
				RenderSystem.translated(x, (height - MENU_HEIGHT * scale * (forms.size()+1)), 0);
				RenderSystem.scalef(scale, scale, scale);
				drawHeader(Strings.Gui_CommandMenu_Drive_Title, SUB_DRIVE);
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
						minecraft.textureManager.bindTexture(texture);
						RenderSystem.translated(x, (height - MENU_HEIGHT * scale * (forms.size() - i)), 0);
						RenderSystem.scalef(scale, scale, scale);

						if (submenu == SUB_DRIVE) {
							paintWithColorArray(driveMenuColor, alpha);
							if (driveSelected == i) {
								textX = (int) (10 * ModConfigs.cmXScale / 100D) + ModConfigs.cmTextXOffset;

								// Draw slot
								drawSelectedSlot();

								RenderSystem.color4f(1F, 1F, 1F, alpha);

								// Draw Icon
								drawIcon(selected, SUB_DRIVE);

							} else { // Not selected
								textX = (int) (5 * ModConfigs.cmXScale / 100D) + ModConfigs.cmTextXOffset;
								drawUnselectedSlot();
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
		RenderSystem.disableBlend();
	}
	
	private void drawSubLimits(int width, int height) {
		RenderSystem.enableBlend();
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);
		Limit limit = null;
		for(Limit val : ModLimits.registry.getValues()) {
        	if(val.getOwner() == playerData.getAlignment()) {
        		limit = val;
        	}
        }		
		
		if (playerData != null && limit != null && !limit.getLevels().isEmpty()) {
			// Limit TOP
			double x = 10 * ModConfigs.cmSubXOffset / 100D;
			RenderSystem.pushMatrix();
			{
				minecraft.textureManager.bindTexture(texture);
				RenderSystem.translated(x, (height - MENU_HEIGHT * scale * (limit.getLevels().size() + 1)), 0);
				RenderSystem.scalef(scale, scale, scale);
				paintWithColorArray(limitMenuColor, alpha);
				drawHeader("Limit", SUB_LIMIT);
			}
			RenderSystem.popMatrix();
			
			for (int i = 0; i < limit.getLevels().size(); i++) {
				RenderSystem.pushMatrix();
				{
					RenderSystem.color4f(1F, 1F, 1F, alpha);

					minecraft.textureManager.bindTexture(texture);
					RenderSystem.translated(x, (height - MENU_HEIGHT * scale * (limit.getLevels().size() - i)), 0);
					RenderSystem.scalef(scale, scale, scale);

					paintWithColorArray(limitMenuColor, alpha);
					if (limitSelected == i) {
						textX = (int) (10 * ModConfigs.cmXScale / 100D) + ModConfigs.cmTextXOffset;
						drawSelectedSlot();

						RenderSystem.color4f(1F, 1F, 1F, alpha);
						drawIcon(selected, SUB_LIMIT);
					} else { // Not selected
						textX = (int) (5 * ModConfigs.cmXScale / 100D) + ModConfigs.cmTextXOffset;
						drawUnselectedSlot();
					}

					int limitCost = limit.getLevels().get(i);
					int colour = playerData.getDP() >= limitCost ? 0xFFFFFF : 0x888888;

					String name = limit.getTranslationKey();

					drawString(minecraft.fontRenderer, Utils.translateToLocal(name)+" ("+limitCost+")", textX, 4, getColor(colour, SUB_LIMIT));
					RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);

				}
				RenderSystem.popMatrix();
			}
		}
		RenderSystem.disableBlend();
	}
}

package online.kingdomkeys.kingdomkeys.client.gui;

import java.awt.Color;
import java.util.LinkedHashMap;

import com.mojang.blaze3d.matrix.MatrixStack;
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
import online.kingdomkeys.kingdomkeys.config.ClientConfig;
import online.kingdomkeys.kingdomkeys.config.CommonConfig;
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
			event.getMatrixStack().push();
			{
				drawCommandMenu(event.getMatrixStack(),minecraft.getMainWindow().getScaledWidth(), minecraft.getMainWindow().getScaledHeight());
			}
			event.getMatrixStack().pop();
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
	
	private void paintWithColorArray(MatrixStack matrixStack, float[] array, float alpha) {
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

	public void drawCommandMenu(MatrixStack matrixStack, int width, int height) {
		if(ModCapabilities.getPlayer(minecraft.player) != null) {
			drawTop(matrixStack, width, height);			
			drawAttack(matrixStack, width, height);
			drawMagic(matrixStack, width, height);
			drawItems(matrixStack, width, height);
			drawDrive(matrixStack, width, height);
		
			if (submenu == SUB_PORTALS) {
				drawSubPortals(matrixStack, width, height);
			}
			if (submenu == SUB_MAGIC || submenu == SUB_TARGET) {
				drawSubMagic(matrixStack, width, height);
			}
			if (submenu == SUB_DRIVE) {
				drawSubDrive(matrixStack, width, height);
			}
			if (submenu == SUB_TARGET) {
				drawSubTargetSelector(matrixStack, width, height);
			}
			
			RenderSystem.disableBlend();

		}
	}
	
	private void drawSubTargetSelector(MatrixStack matrixStack, int width, int height) {
		IWorldCapabilities worldData = ModCapabilities.getWorld(minecraft.world);
		if(worldData.getPartyFromMember(minecraft.player.getUniqueID()) == null) {
			submenu = SUB_MAGIC;
			return;
		}

		//Title
		matrixStack.push();
		{
			paintWithColorArray(matrixStack, targetModeColor, alpha);
			minecraft.textureManager.bindTexture(texture);
			matrixStack.translate(20, (height - MENU_HEIGHT * scale * (worldData.getPartyFromMember(minecraft.player.getUniqueID()).getMembers().size() + 1)), 0);
			matrixStack.scale(scale, scale, scale);
			blit(matrixStack, 0, 0, 0, 0, TOP_WIDTH, TOP_HEIGHT);
			drawString(matrixStack, minecraft.fontRenderer, Utils.translateToLocal("TARGET"), 5 + ModConfigs.cmTextXOffset, 4, 0xFFFFFF);
			
		}
		matrixStack.pop();

		for (int i = 0; i < worldData.getPartyFromMember(minecraft.player.getUniqueID()).getMembers().size(); i++) {
			matrixStack.push();
			{
				//RenderSystem.color4f(1F, 1F, 1F, alpha);
				int u;
				int v;
				int x;
				x = 20;

				minecraft.textureManager.bindTexture(texture);
				matrixStack.translate(x, (height - MENU_HEIGHT * scale * (worldData.getPartyFromMember(minecraft.player.getUniqueID()).getMembers().size() - i)), 0);
				matrixStack.scale(scale, scale, scale);
				if (submenu == SUB_TARGET) {
					v = 0;
					paintWithColorArray(matrixStack, targetModeColor, alpha);

					if (targetSelected == i) {
						textX = 10 + ModConfigs.cmTextXOffset;

						// Draw slot
						blit(matrixStack, 5, 0, TOP_WIDTH, MENU_HEIGHT, TOP_WIDTH, v + MENU_HEIGHT);

						//RenderSystem.color4f(1, 1F, 1F, alpha);
						//RenderSystem.enableBlend();
						// Draw Icon
						blit(matrixStack, 60, 2, 140 + ((selected + 1) * iconWidth) - iconWidth, 18, iconWidth, iconWidth);
						//RenderSystem.disableBlend();
					} else { // Not selected
						textX = 5 + ModConfigs.cmTextXOffset;
						blit(matrixStack, 0, 0, TOP_WIDTH, 0, TOP_WIDTH, v + MENU_HEIGHT);
					}
					// colour = Constants.getCost(spells.get(i)) < STATS.getMP() ? 0xFFFFFF :
					// 0xFF9900;

					Member member = worldData.getPartyFromMember(minecraft.player.getUniqueID()).getMembers().get(i);
					// String magicName = Constants.getMagicName(magic, level);
	            	if(minecraft.world.getPlayerByUuid(member.getUUID()) != null && minecraft.player.getDistance(minecraft.world.getPlayerByUuid(member.getUUID())) < ModConfigs.partyRangeLimit) {
						drawString(matrixStack, minecraft.fontRenderer, member.getUsername(), textX, 4, 0xFFFFFF);
					} else {
						drawString(matrixStack, minecraft.fontRenderer, member.getUsername(), textX, 4, 0x888888);
					}
					RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
				}
			}
			matrixStack.pop();
		}
	}

	

	public void drawTop(MatrixStack matrixStack, int width, int height) {
		RenderSystem.enableBlend();
		matrixStack.push();
		{
			RenderSystem.color4f(1F, 1F, 1F, alpha);
			minecraft.textureManager.bindTexture(texture);
			matrixStack.translate(0, (height - MENU_HEIGHT * scale * TOP), 0);
			matrixStack.scale(scale, scale, scale);
			textX = 0;
			paintWithColorArray(matrixStack, normalModeColor, alpha);
			blit(matrixStack, 0, 0, 0, 0, TOP_WIDTH, TOP_HEIGHT);
			
			if(ModConfigs.cmHeaderTextVisible)
				drawString(matrixStack, minecraft.fontRenderer, Utils.translateToLocal(Strings.Gui_CommandMenu_Command), 6, 4, getColor(0xFFFFFF,SUB_MAIN));
		}
		matrixStack.pop();
		RenderSystem.disableBlend();

	}

	public void drawAttack(MatrixStack matrixStack, int width, int height) {
		RenderSystem.enableBlend();
		matrixStack.push();
		{
			RenderSystem.color4f(1F, 1F, 1F, alpha);
			minecraft.textureManager.bindTexture(texture);

			int v = 0;
			int x = 0;

			matrixStack.translate(x, (height - MENU_HEIGHT * scale * ATTACK), 0);
			matrixStack.scale(scale, scale, scale);

			paintWithColorArray(matrixStack, normalModeColor, alpha);
			ClientPlayerEntity player = minecraft.player;
			if (selected == ATTACK) { // Selected
				textX = 5 + ModConfigs.cmTextXOffset;
				blit(matrixStack, 5, 0, TOP_WIDTH, MENU_HEIGHT, TOP_WIDTH, v + MENU_HEIGHT);
				RenderSystem.color4f(1F, 1F, 1F, alpha);

				// Draw Icon
				if (ModCapabilities.getPlayer(player).getAlignment() != Utils.OrgMember.NONE) {
					blit(matrixStack, 60, 2, 140 + ((selected + 1) * iconWidth) - iconWidth, 18, iconWidth, iconWidth);
				} else {
					blit(matrixStack, 60, 2, 140 + (selected * iconWidth) - iconWidth, 18, iconWidth, iconWidth);
				}

			} else { // Not selected
				textX = ModConfigs.cmTextXOffset;
				blit(matrixStack, 0, 0, TOP_WIDTH, 0, TOP_WIDTH, v + MENU_HEIGHT);
			}

			drawString(matrixStack, minecraft.fontRenderer, Utils.translateToLocal(Strings.Gui_CommandMenu_Attack), 6 + textX, 4, getColor(0xFFFFFF,SUB_MAIN));
			
			
			if (player.getHeldItemMainhand() != null) {
				if (player.getHeldItemMainhand().getItem() instanceof ArrowgunItem) {
					ItemStack weapon = player.getHeldItemMainhand();
					if (weapon.hasTag()) {
						if (weapon.getTag().contains("ammo")) {
							int ammo = weapon.getTag().getInt("ammo");
							drawString(matrixStack, minecraft.fontRenderer, ammo + "", textX + TOP_WIDTH, 4, 0xFFFFFF);
						}
					}

				}
			}			 
		}
		matrixStack.pop();
		RenderSystem.disableBlend();
	}

	public void drawMagic(MatrixStack matrixStack, int width, int height) {
		RenderSystem.enableBlend();

		matrixStack.push();
		{
			RenderSystem.color4f(1F, 1F, 1F, alpha);
			minecraft.textureManager.bindTexture(texture);

			int v = 0;
			int x = 0;

			matrixStack.translate(x, (height - MENU_HEIGHT * scale * MAGIC), 0);
			matrixStack.scale(scale, scale, scale);

			if (submenu != 0) {
				RenderSystem.color4f(0.3F, 0.3F, 0.3F, alpha);
			}
			
			paintWithColorArray(matrixStack, normalModeColor, alpha);
			if (selected == MAGIC) { // Selected
				textX = 5 + ModConfigs.cmTextXOffset;
				// Draw slot
				blit(matrixStack, 5, 0, TOP_WIDTH, MENU_HEIGHT, TOP_WIDTH, v + MENU_HEIGHT);
				RenderSystem.color4f(1F, 1F, 1F, alpha);
				//RenderSystem.enableBlend();
				blit(matrixStack, 60, 2, 140 + (selected * iconWidth) - iconWidth, 18, iconWidth, iconWidth);
				//RenderSystem.disableBlend();
			} else { // Not selected
				textX = ModConfigs.cmTextXOffset;
				blit(matrixStack, 0, 0, TOP_WIDTH, 0, TOP_WIDTH, v + MENU_HEIGHT);
			}
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);

			drawString(matrixStack, minecraft.fontRenderer, Utils.translateToLocal(Strings.Gui_CommandMenu_Magic), 6 + textX, 4, playerData.getMagicList().isEmpty() || playerData.getMaxMP() == 0 ? 0x888888 :getColor(0xFFFFFF,SUB_MAIN));

		}
		matrixStack.pop();
		
		RenderSystem.disableBlend();
	}

	public void drawItems(MatrixStack matrixStack, int width, int height) {
		RenderSystem.enableBlend();
		matrixStack.push();
		{
			RenderSystem.color4f(1F, 1F, 1F, alpha);
			minecraft.textureManager.bindTexture(texture);

			int u;
			int v = 0;
			int x = 0;

			matrixStack.translate(x, (height - MENU_HEIGHT * scale * ITEMS), 0);
			matrixStack.scale(scale, scale, scale);

			if (submenu != 0)
				RenderSystem.color4f(0.3F, 0.3F, 0.3F, alpha);

			paintWithColorArray(matrixStack, normalModeColor, alpha);
			if (selected == ITEMS) { // Selected
				textX = 5 + ModConfigs.cmTextXOffset;
				// Draw slot
				blit(matrixStack, 5, 0, TOP_WIDTH, MENU_HEIGHT, TOP_WIDTH, v + MENU_HEIGHT);
				RenderSystem.color4f(1F, 1F, 1F, alpha);
				blit(matrixStack, 60, 2, 140 + (selected * iconWidth) - iconWidth, 18, iconWidth, iconWidth);
			} else { // Not selected
				textX = ModConfigs.cmTextXOffset;
				blit(matrixStack, 0, 0, TOP_WIDTH, 0, TOP_WIDTH, v + MENU_HEIGHT);
			}

			drawString(matrixStack, minecraft.fontRenderer, Utils.translateToLocal(Strings.Gui_CommandMenu_Items), 6 + textX, 4, getColor(0xFFFFFF,SUB_MAIN));

		}
		matrixStack.pop();
		RenderSystem.disableBlend();
	}

	public void drawDrive(MatrixStack matrixStack, int width, int height) {
		RenderSystem.enableBlend();
		matrixStack.push();
		{
			RenderSystem.color4f(1F, 1F, 1F, alpha);
			minecraft.textureManager.bindTexture(texture);

			int v = 0;
			int x = 0;

			matrixStack.translate(x, (height - MENU_HEIGHT * scale * DRIVE), 0);
			matrixStack.scale(scale, scale, scale);

			if (submenu != 0)
				RenderSystem.color4f(0.3F, 0.3F, 0.3F, alpha);

			paintWithColorArray(matrixStack, normalModeColor, alpha);
			if (selected == DRIVE) { // Selected
				textX = 5 + ModConfigs.cmTextXOffset;
				// Draw slot
				blit(matrixStack, 5, 0, TOP_WIDTH, MENU_HEIGHT, TOP_WIDTH, v + MENU_HEIGHT);
				RenderSystem.color4f(1F, 1F, 1F, alpha);
				blit(matrixStack, 60, 2, 140 + (selected * iconWidth) - iconWidth, 18, iconWidth, iconWidth);
			} else { // Not selected
				textX = ModConfigs.cmTextXOffset;
				blit(matrixStack, 0, 0, TOP_WIDTH, 0, TOP_WIDTH, v + MENU_HEIGHT);
			}

			IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);
			if (playerData != null) {
				String text = playerData.getActiveDriveForm().equals(DriveForm.NONE.toString())?".drive":".revert";
				int color = playerData.getActiveDriveForm().equals(Strings.Form_Anti) || playerData.getDriveFormMap().size() <= 1 ? 0x888888 : 0xFFFFFF;
				drawString(matrixStack, minecraft.fontRenderer, Utils.translateToLocal("gui.commandmenu"+text), 6 + textX, 4, getColor(color,SUB_MAIN));
			}
		}
		matrixStack.pop();
		RenderSystem.disableBlend();
	}
	public void drawSubPortals(MatrixStack matrixStack, int width, int height) {
		RenderSystem.enableBlend();
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);
		if (playerData.getPortalList() != null && !playerData.getPortalList().isEmpty()) {
			// PORTAL TOP
			matrixStack.push();
			{
				paintWithColorArray(matrixStack, portalMenuColor, alpha);
				minecraft.textureManager.bindTexture(texture);
				matrixStack.translate(10, (height - MENU_HEIGHT * scale * (playerData.getPortalList().size() + 1)), 0);
				matrixStack.scale(scale, scale, scale);
				blit(matrixStack, 0, 0, 0, 0, TOP_WIDTH, TOP_HEIGHT);
				drawString(matrixStack, minecraft.fontRenderer, Utils.translateToLocal(Strings.Gui_CommandMenu_Portals_Title), 6, 4, 0xFFFFFF);
			}
			matrixStack.pop();
	
			for (int i = 0; i < playerData.getPortalList().size(); i++) {
				matrixStack.push();
				{
					RenderSystem.color4f(1F, 1F, 1F, alpha);
					int u;
					int v;
					int x;
					x = (portalSelected == i) ? 15 : 10;
	
					minecraft.textureManager.bindTexture(texture);
					matrixStack.translate(x, (height - MENU_HEIGHT * scale * (playerData.getPortalList().size() - i)), 0);
					matrixStack.scale(scale, scale, scale);
					if (submenu == SUB_PORTALS) {
						v = 0;
						paintWithColorArray(matrixStack, portalMenuColor, alpha);
	
						if (portalSelected == i) {
							textX = 10 + ModConfigs.cmTextXOffset;
	
							// Draw slot
							blit(matrixStack, 5, 0, TOP_WIDTH, MENU_HEIGHT, TOP_WIDTH, v + MENU_HEIGHT);
	
							RenderSystem.color4f(1, 1F, 1F, alpha);
	
							// Draw Icon
							blit(matrixStack, 60, 2, 140 + ((selected + 1) * iconWidth) - iconWidth, 18, iconWidth, iconWidth);
	
						} else { // Not selected
							textX = 5 + ModConfigs.cmTextXOffset;
							blit(matrixStack, 0, 0, TOP_WIDTH, 0, TOP_WIDTH, v + MENU_HEIGHT);
						}
						// colour = Constants.getCost(spells.get(i)) < STATS.getMP() ? 0xFFFFFF :
						// 0xFF9900;
	
						PortalData portal = playerData.getPortalList().get(i);
						// String magicName = Constants.getMagicName(magic, level);
						drawString(matrixStack, minecraft.fontRenderer, Utils.translateToLocal(portal.getShortCoords() + ""), textX, 4, 0xFFFFFF);
						RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
					}
				}
				matrixStack.pop();
			}
		}
		RenderSystem.disableBlend();
	}
	
	private void drawSubMagic(MatrixStack matrixStack, int width, int height) {
		RenderSystem.enableBlend();
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);
		if (playerData != null && playerData.getMagicList() != null && !playerData.getMagicList().isEmpty()) {
			// MAGIC TOP
			matrixStack.push();
			{
			//	RenderSystem.color4f(1F, 1F, 1F, alpha);
				minecraft.textureManager.bindTexture(texture);
				matrixStack.translate(10, (height - MENU_HEIGHT * scale * (playerData.getMagicList().size() + 1)), 0);
				matrixStack.scale(scale, scale, scale);
				paintWithColorArray(matrixStack, magicMenuColor, alpha);
				blit(matrixStack, 0, 0, 0, 0, TOP_WIDTH, TOP_HEIGHT);
				// drawTexturedModalRect(0, 0, 0, 0+extraY, TOP_WIDTH, TOP_HEIGHT);
				drawString(matrixStack, minecraft.fontRenderer, Utils.translateToLocal(Strings.Gui_CommandMenu_Magic_Title), 5 + ModConfigs.cmTextXOffset, 4, getColor(0xFFFFFF,SUB_MAGIC));
				
			}
			matrixStack.pop();
			for (int i = 0; i < playerData.getMagicList().size(); i++) {
				matrixStack.push();
				{
					RenderSystem.color4f(1F, 1F, 1F, alpha);
					int u;
					int v;
					int x;
					x = 10;

					minecraft.textureManager.bindTexture(texture);
					matrixStack.translate(x, (height - MENU_HEIGHT * scale * (playerData.getMagicList().size() - i)), 0);
					matrixStack.scale(scale, scale, scale);
						v = 0;

						paintWithColorArray(matrixStack, magicMenuColor, alpha);
						if (magicSelected == i) {
							// drawTexturedModalRect(0, 0, TOP_WIDTH, 15+extraY, TOP_WIDTH + MENU_WIDTH, v +
							// MENU_HEIGHT);
							textX = 10 + ModConfigs.cmTextXOffset;

							// Draw slot
							blit(matrixStack, 5, 0, TOP_WIDTH, MENU_HEIGHT, TOP_WIDTH, v + MENU_HEIGHT);

							RenderSystem.color4f(1F, 1F, 1F, alpha);

							// Draw Icon
							blit(matrixStack, 60, 2, 140 + selected * iconWidth - iconWidth, 18, iconWidth, iconWidth);

						} else { // Not selected
							textX = 5 + ModConfigs.cmTextXOffset;
							blit(matrixStack, 0, 0, TOP_WIDTH, 0, TOP_WIDTH, v + MENU_HEIGHT);
						}

						String magic = playerData.getMagicList().get(i);
						Magic magicInstance = ModMagic.registry.getValue(new ResourceLocation(magic));
						int cost = magicInstance.getCost();
						int colour = playerData.getMP() > cost ? 0xFFFFFF : 0xFF9900;
						
						if(playerData.getMaxMP() == 0 || playerData.getRecharge() || cost > playerData.getMaxMP() && cost < 300) {
							colour = 0x888888;
						}
						
						magic = magicInstance.getTranslationKey();
						
						drawString(matrixStack, minecraft.fontRenderer, Utils.translateToLocal(magic), textX, 4, getColor(colour, SUB_MAGIC));
						RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
					
				}
				matrixStack.pop();
			}
		}
		RenderSystem.disableBlend();
	}

	private void drawSubDrive(MatrixStack matrixStack, int width, int height) {
		RenderSystem.enableBlend();
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);

		LinkedHashMap<String, int[]> forms = Utils.getSortedDriveForms(playerData.getDriveFormMap());
		forms.remove(DriveForm.NONE.toString());
		
		if (playerData != null && forms != null && !forms.isEmpty()) {
			// DRIVE TOP
			matrixStack.push();
			{
				paintWithColorArray(matrixStack, driveMenuColor, alpha);
				minecraft.textureManager.bindTexture(texture);
				matrixStack.translate(10, (height - MENU_HEIGHT * scale * (forms.size()+1)), 0);
				matrixStack.scale(scale, scale, scale);
				blit(matrixStack, 0, 0, 0, 0, TOP_WIDTH, TOP_HEIGHT);
				drawString(matrixStack, minecraft.fontRenderer, Utils.translateToLocal(Strings.Gui_CommandMenu_Drive_Title), 5 + ModConfigs.cmTextXOffset, 4, 0xFFFFFF);
			}
			matrixStack.pop();

			for (int i = 0; i < forms.size(); i++) {
				String formName = (String) forms.keySet().toArray()[i];
				if (!formName.equals(DriveForm.NONE.toString())) {
					DriveForm form = ModDriveForms.registry.getValue(new ResourceLocation(formName));
					int cost = form.getDriveCost();
					int color = playerData.getDP() >= cost ? 0xFFFFFF : 0x888888;
					formName = form.getTranslationKey();

					matrixStack.push();
					{
						RenderSystem.color4f(1F, 1F, 1F, alpha);
						int u;
						int v;
						int x;
						x = (driveSelected == i) ? 15 : 10;
						v = (driveSelected == i) ? MENU_HEIGHT : 0;

						minecraft.textureManager.bindTexture(texture);
						matrixStack.translate(x, (height - MENU_HEIGHT * scale * (forms.size() - i)), 0);
						matrixStack.scale(scale, scale, scale);

						if (submenu == SUB_DRIVE) {
							v = 0;
							paintWithColorArray(matrixStack, driveMenuColor, alpha);
							if (driveSelected == i) {
								textX = 10 + ModConfigs.cmTextXOffset;

								// Draw slot
								blit(matrixStack, 5, 0, TOP_WIDTH, MENU_HEIGHT, TOP_WIDTH, v + MENU_HEIGHT);

								RenderSystem.color4f(1F, 1F, 1F, alpha);

								// Draw Icon
								blit(matrixStack, 60, 2, 140 + selected * iconWidth - iconWidth, 18, iconWidth, iconWidth);

							} else { // Not selected
								textX = 5 + ModConfigs.cmTextXOffset;
								blit(matrixStack, 0, 0, TOP_WIDTH, 0, TOP_WIDTH, v + MENU_HEIGHT);
							}
						}
					
						RenderSystem.color4f(0.3F, 0.3F, 0.3F, alpha);
						matrixStack.scale(scale, scale, scale);
						if (submenu == SUB_DRIVE) {
							drawString(matrixStack, minecraft.fontRenderer, Utils.translateToLocal(formName), textX, 4, color);
						}
						RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);

					}
					matrixStack.pop();
				}
			}
		}
		RenderSystem.disableBlend();
	}
}

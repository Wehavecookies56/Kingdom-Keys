package online.kingdomkeys.kingdomkeys.client.gui.overlay;

import java.awt.Color;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.IWorldCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
import online.kingdomkeys.kingdomkeys.handler.EntityEvents;
import online.kingdomkeys.kingdomkeys.handler.InputHandler;
import online.kingdomkeys.kingdomkeys.handler.KeyboardHelper;
import online.kingdomkeys.kingdomkeys.item.organization.ArrowgunItem;
import online.kingdomkeys.kingdomkeys.lib.Party.Member;
import online.kingdomkeys.kingdomkeys.lib.PortalData;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.limit.Limit;
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.kingdomkeys.kingdomkeys.magic.ModMagic;
import online.kingdomkeys.kingdomkeys.reactioncommands.ModReactionCommands;
import online.kingdomkeys.kingdomkeys.reactioncommands.ReactionCommand;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.kingdomkeys.kingdomkeys.util.Utils.OrgMember;

//TODO cleanup
public class CommandMenuGui extends OverlayBase {

	public static final CommandMenuGui INSTANCE = new CommandMenuGui();
	public static final int TOP = 5, ATTACK = 4, MAGIC = 3, ITEMS = 2, DRIVE = 1;

	int TOP_WIDTH = 70;
	int TOP_HEIGHT = 15;
	int MENU_WIDTH = 71;
	int MENU_HEIGHT = 15;
	int iconWidth = 10;
	int textX = 0;

	public static final int SUB_MAIN = 0, SUB_MAGIC = 1, SUB_ITEMS = 2, SUB_DRIVE = 3, SUB_PORTALS = 4, SUB_ATTACKS = 5, SUB_TARGET = 6, SUB_LIMIT = 7;

	public static final int NONE = 0;
	public static int selected = ATTACK, targetSelected = 0;
	public static int submenu = 0, magicSelected = 0, potionSelected = 0, driveSelected = 0, portalSelected = 0, attackSelected = 0, limitSelected = 0, itemSelected = 0, reactionSelected = 0;

	ResourceLocation texture = new ResourceLocation(KingdomKeys.MODID, "textures/gui/commandmenu.png");

	private CommandMenuGui() {
		super();
	}

	@Override
	public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int width, int height) {
		super.render(gui, guiGraphics, partialTick, width, height);
		textX = (int) (5 * ModConfigs.cmXScale / 100D) + ModConfigs.cmTextXOffset;
		drawCommandMenu(guiGraphics, Minecraft.getInstance().getWindow().getGuiScaledWidth(), Minecraft.getInstance().getWindow().getGuiScaledHeight());
	}

	float alpha = 1F;
	float scale = 1.05f;
	float[] orgColor = { 0.8F, 0.8F, 0.8F };
	//		{ color when seen fully, SubMenu when it should be seen fully }
	float[] normalModeColor = { 0.04F, 0.2F, 1F, SUB_MAIN };
	float[] portalMenuColor = { 0.8F, 0.8F, 0.8F, SUB_PORTALS};
	float[] combatModeColor = { 1F, 0.8F, 0F, SUB_MAIN };
	float[] bossModeColor = { 1F, 0F, 0F, SUB_MAIN };
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

	private void paintWithColorArray(GuiGraphics gui, float[] array, float alpha) {
		if (submenu == 0) { //Menu on top should always be of the original color
			if (EntityEvents.isBoss) { // Red
				RenderSystem.setShaderColor(bossModeColor[0], bossModeColor[1], bossModeColor[2], alpha);
			} else if (EntityEvents.isHostiles) { // Yellow
				if(ModConfigs.cmClassicColors) {
					RenderSystem.setShaderColor(bossModeColor[0], bossModeColor[1], bossModeColor[2], alpha);
				} else {
					RenderSystem.setShaderColor(combatModeColor[0], combatModeColor[1], combatModeColor[2], alpha);
				}
				// Blue/color
			} else if (ModCapabilities.getPlayer(minecraft.player).getAlignment() != Utils.OrgMember.NONE && array == normalModeColor) { // Org gray
				RenderSystem.setShaderColor(orgColor[0], orgColor[1], orgColor[2], alpha);
			} else { // Blue
				RenderSystem.setShaderColor(array[0], array[1], array[2], alpha);
			}
		} else { //Submenu ?
			if (EntityEvents.isBoss) { // Red
				if (submenu == array[3]) {
					RenderSystem.setShaderColor(array[0], array[1], array[2], alpha);
				} else {
					RenderSystem.setShaderColor(bossModeColor[0] / 2, bossModeColor[1] / 2, bossModeColor[2] / 2, alpha);
				}
			} else if (EntityEvents.isHostiles) { // Yellow
				if (submenu == array[3]) {
					RenderSystem.setShaderColor(array[0], array[1], array[2], alpha);
				} else {
					if(ModConfigs.cmClassicColors) {
						RenderSystem.setShaderColor(bossModeColor[0], bossModeColor[1], bossModeColor[2], alpha);
					} else {
						RenderSystem.setShaderColor(combatModeColor[0], combatModeColor[1], combatModeColor[2], alpha);
					}
				}
				// Blue/color
			} else if (ModCapabilities.getPlayer(minecraft.player).getAlignment() != Utils.OrgMember.NONE && array == normalModeColor) { // Org gray
				if (submenu == array[3]) {
					RenderSystem.setShaderColor(orgColor[0], orgColor[1], orgColor[2], alpha);
				} else {
					RenderSystem.setShaderColor(orgColor[0] / 2, orgColor[1] / 2, orgColor[2] / 2, alpha);
				}
			} else { // Blue
				if (submenu == array[3]) {
					RenderSystem.setShaderColor(array[0], array[1], array[2], alpha);
				} else {
					RenderSystem.setShaderColor(array[0] / 2, array[1] / 2, array[2] / 2, alpha);
				}
			}
		}
	}

	public void drawCommandMenu(GuiGraphics gui, int width, int height) {
		if(ModCapabilities.getPlayer(minecraft.player) != null) {
			gui.pose().pushPose();
			{
				gui.pose().translate(ModConfigs.cmXPos, 0, 0);
				drawMain(gui, width, height);
			
				if (submenu == SUB_PORTALS) {
					drawSubPortals(gui, width, height);
				}
				if (submenu == SUB_MAGIC || submenu == SUB_TARGET && selected == MAGIC) {
					drawSubMagic(gui, width, height);
				}
				if (submenu == SUB_ITEMS || submenu == SUB_TARGET && selected == ITEMS) {
					drawSubItems(gui, width, height);
				}
				if (submenu == SUB_DRIVE) {
					drawSubDrive(gui, width, height);
				}
				if (submenu == SUB_LIMIT) {
					drawSubLimits(gui, width, height);
				}
				if (submenu == SUB_TARGET) {
					drawSubTargetSelector(gui, width, height);
				}
				if (submenu == SUB_MAIN && KeyboardHelper.isScrollActivatorDown()) {
					IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);
					int i = 0;
					for (Entry<Integer, String> entry : playerData.getShortcutsMap().entrySet()) {
						String[] data = entry.getValue().split(",");
						Magic magic = ModMagic.registry.get().getValue(new ResourceLocation(data[0]));
						double cost = magic.getCost(Integer.parseInt(data[1]), minecraft.player);
						int colour = playerData.getMP() > cost ? 0xFFFFFF : 0xFF9900;
						
						if(playerData.isAbilityEquipped(Strings.extraCast) && cost > playerData.getMP() && playerData.getMP() > 1 && cost < 300) {
							colour = 0xFFFFFF;
						}
	                	DriveForm form = ModDriveForms.registry.get().getValue(new ResourceLocation(playerData.getActiveDriveForm()));

						if(playerData.getMaxMP() == 0 || playerData.getRecharge() || cost > playerData.getMaxMP() && cost < 300 || cost < 300 && cost >= playerData.getMP() && playerData.isAbilityEquipped(Strings.mpSafety) || playerData.getMagicCasttimeTicks() > 0 || playerData.getMagicCooldownTicks() > 0 || !form.canUseMagic()) {
							colour = 0x888888;
						}

						drawString(guiGraphics, minecraft.font, Utils.translateToLocal(InputHandler.Keybinds.SCROLL_ACTIVATOR.keybinding.getKey().getName())+" + " + (entry.getKey() + 1) + ": " + Utils.translateToLocal(magic.getTranslationKey(Integer.parseInt(data[1]))), (int) (5 * ModConfigs.cmXScale / 100D) + ModConfigs.cmTextXOffset, 4 + i * 10, colour);
						i++;
					}

				}
			}
			gui.pose().popPose();
		}
	}
		
	private void drawHeader(GuiGraphics gui, String text, int subMenu) {
		PoseStack matrixStack = gui.pose();
		matrixStack.pushPose();
		{
			matrixStack.scale(ModConfigs.cmXScale / 100F, 1, 1);
			blit(gui, texture, 0, 0, 0, 0, TOP_WIDTH, TOP_HEIGHT);
			RenderSystem.setShaderColor(1,1,1, alpha);
			blit(gui, texture, 0, 0, 0, 30, TOP_WIDTH, TOP_HEIGHT);
		}
		matrixStack.popPose();
		
		if(ModConfigs.cmHeaderTextVisible) {
			drawString(gui, minecraft.font, Utils.translateToLocal(text), (int) (5 * ModConfigs.cmXScale / 100D) + ModConfigs.cmTextXOffset, 4, getColor(0xFFFFFF,subMenu));
		}
	}
	
	private void drawSelectedSlot(GuiGraphics gui) {
		RenderSystem.enableBlend();
		PoseStack matrixStack = gui.pose();
		matrixStack.pushPose();
		{
			matrixStack.scale(ModConfigs.cmXScale / 100F, 1, 1);
			blit(gui, texture, ModConfigs.cmSelectedXOffset, 0, TOP_WIDTH, MENU_HEIGHT, TOP_WIDTH, MENU_HEIGHT);
			RenderSystem.setShaderColor(1,1,1, alpha);
			blit(gui, texture, ModConfigs.cmSelectedXOffset, 0, TOP_WIDTH, MENU_HEIGHT+30, TOP_WIDTH, MENU_HEIGHT);

		}
		matrixStack.popPose();	
		RenderSystem.disableBlend();
	}

	private void drawUnselectedSlot(GuiGraphics gui) {
		PoseStack matrixStack = gui.pose();
		RenderSystem.enableBlend();
		matrixStack.pushPose();
		{
			matrixStack.scale(ModConfigs.cmXScale / 100F, 1, 1);
			blit(gui, texture, 0, 0, TOP_WIDTH, 0, TOP_WIDTH, 0 + MENU_HEIGHT);
			RenderSystem.setShaderColor(1,1,1, alpha);
			blit(gui, texture, 0, 0, TOP_WIDTH, 30, TOP_WIDTH, 0 + MENU_HEIGHT);

		}
		matrixStack.popPose();	
		RenderSystem.disableBlend();
	}

	private void drawIcon(GuiGraphics gui, int selected, int subMenu) {
		RenderSystem.enableBlend();
		if(subMenu == submenu) {
			RenderSystem.setShaderColor(1F, 1F, 1F, alpha);
		} else {
			RenderSystem.setShaderColor(0.5F, 0.5F, 0.5F, alpha);
		}
		blit(gui, texture, (int) (TOP_WIDTH * (ModConfigs.cmXScale / 100D) - (TOP_WIDTH * (ModConfigs.cmXScale / 100D)) * 0.15) + ModConfigs.cmSelectedXOffset - 5, 2, 140 + (selected * iconWidth) - iconWidth, 18, iconWidth, iconWidth);
		RenderSystem.setShaderColor(1,1,1, alpha);
		blit(gui, texture, (int) (TOP_WIDTH * (ModConfigs.cmXScale / 100D) - (TOP_WIDTH * (ModConfigs.cmXScale / 100D)) * 0.15) + ModConfigs.cmSelectedXOffset - 5, 2, 140 + (selected * iconWidth) - iconWidth, 18 + 30, iconWidth, iconWidth);


		RenderSystem.disableBlend();

	}
	
	private String getCommandMenuName(int i) {
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);
		switch(i) {
		case ATTACK:
			return playerData.getAlignment() == OrgMember.NONE ? Strings.Gui_CommandMenu_Attack : Strings.Gui_CommandMenu_Portal;
		case MAGIC:
			return playerData.getMagicsMap().size() > 0 ? Strings.Gui_CommandMenu_Magic : "???";
		case ITEMS:
			return Strings.Gui_CommandMenu_Items;
		case DRIVE:
			if(playerData.getAlignment() == OrgMember.NONE) {
				return playerData.getVisibleDriveForms().size() > 1 ? (playerData.getActiveDriveForm().equals(DriveForm.NONE.toString()) ? Strings.Gui_CommandMenu_Drive : Strings.Gui_CommandMenu_Drive_Revert) : "???";
			} else {
				return Strings.Gui_CommandMenu_Limit;
			}
		}
		return "";
	}
	
	public void drawMain(GuiGraphics gui, int width, int height) {
		PoseStack matrixStack = gui.pose();
		RenderSystem.enableBlend();
		//Header
		matrixStack.pushPose();
		{
			RenderSystem.setShaderColor(1F, 1F, 1F, alpha);
			matrixStack.translate(0, (height - MENU_HEIGHT * scale * TOP), 0);
			matrixStack.scale(scale, scale, scale);
			paintWithColorArray(gui, normalModeColor, alpha);
			drawHeader(gui, Strings.Gui_CommandMenu_Command, SUB_MAIN);
		}
		matrixStack.popPose();
		
		//Reaction command
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);
		List<String> list = playerData.getReactionCommands();
		
		for(int i = 0; i < list.size(); i++) {
			matrixStack.pushPose();
			{
				float shade = i == reactionSelected ? 1F : 0.4F;
				RenderSystem.setShaderColor(shade,shade,shade, alpha);
				matrixStack.translate(0, (height - MENU_HEIGHT * scale * TOP - (15*scale)*i), 1);
				matrixStack.scale(scale, scale, scale);
				matrixStack.pushPose();
				{
					matrixStack.scale(ModConfigs.cmXScale / 75F, 1, 1);
					blit(gui, texture, 0, 0, 0, 15, TOP_WIDTH, TOP_HEIGHT);
				}
				matrixStack.popPose();
				
				ReactionCommand command = ModReactionCommands.registry.get().getValue(new ResourceLocation(list.get(i)));
				drawString(gui, minecraft.font, Utils.translateToLocal(command.getTranslationKey()), (int) (5 * ModConfigs.cmXScale / 100D) + ModConfigs.cmTextXOffset, 4, 0xFFFFFF);
			}
			matrixStack.popPose();
		}
		
		//Slots
		matrixStack.pushPose();
		{
			int x = 0;
			for(int i = 1; i <= ATTACK; i++) {
				matrixStack.pushPose();
				{
					matrixStack.translate(x, (height - MENU_HEIGHT * scale * i), 0);
					matrixStack.scale(scale, scale, scale);
		
					paintWithColorArray(gui, normalModeColor, alpha);
					LocalPlayer player = minecraft.player;
					textX = (int) (5 * ModConfigs.cmXScale / 100D) + ModConfigs.cmTextXOffset;

					if (selected == i) { // Selected
						textX += ModConfigs.cmSelectedXOffset;
						drawSelectedSlot(gui);
						if (selected == ATTACK && ModCapabilities.getPlayer(player).getAlignment() != Utils.OrgMember.NONE) {
							drawIcon(gui, selected+1, SUB_MAIN);
						} else {
							drawIcon(gui, selected, SUB_MAIN);
						}
					} else { // Not selected
						drawUnselectedSlot(gui);
					}
		
					int color = getColor(0xFFFFFF,SUB_MAIN);
					if(i == MAGIC) {
	                	DriveForm form = ModDriveForms.registry.get().getValue(new ResourceLocation(playerData.getActiveDriveForm()));
						color = playerData.getMagicsMap().isEmpty() || ModConfigs.magicDisplayedInCommandMenu.isEmpty() || playerData.getMaxMP() == 0 || playerData.getMagicCasttimeTicks() > 0 || playerData.getMagicCooldownTicks() > 0 || playerData.getRecharge() || form != null && !form.canUseMagic() ? 0x888888 : getColor(0xFFFFFF,SUB_MAIN);
					}
					if(i == ITEMS) {
						color = getColor(Utils.getEquippedItems(playerData.getEquippedItems()).size() > 0 ? 0xFFFFFF : 0x888888,SUB_MAIN);
					}
					if (i == DRIVE) {// If it's an org member / in antiform / has no drive unlocked be gray
						if (playerData.getAlignment() != OrgMember.NONE) {
							// Checks for limit obtaining in the future?
							color = playerData.getLimitCooldownTicks() <= 0 && playerData.getDP() >= Utils.getMinimumDPForLimit(player) ? getColor(0xFFFFFF, SUB_MAIN) : getColor(0x888888, SUB_MAIN);
						} else { //if is antiform and in battle is gray                                                      if has no drive forms unlocked                      if player is in base form AND the DP is not enough to get in a form 
							if ((playerData.getActiveDriveForm().equals(Strings.Form_Anti) && !playerData.isAbilityEquipped(Strings.darkDomination) && EntityEvents.isHostiles) || playerData.getDriveFormMap().size() <= (playerData.isAbilityEquipped(Strings.darkDomination) ? 2 : 3) || (playerData.getActiveDriveForm().equals(DriveForm.NONE.toString()) && playerData.getDP() < Utils.getMinimumDPForDrive(playerData))) {
								color = getColor(0x888888, SUB_MAIN);
							} else {
								color = getColor(0xFFFFFF, SUB_MAIN);
							}
						}
					}
					
					drawString(gui, minecraft.font, Utils.translateToLocal(getCommandMenuName(i)), textX, 4, color);
					
					if(i == ATTACK) {
						if (player.getMainHandItem() != null && player.getMainHandItem().getItem() instanceof ArrowgunItem) {
							ItemStack weapon = player.getMainHandItem();
							if (weapon.hasTag() && weapon.getTag().contains("ammo")) {
								int ammo = weapon.getTag().getInt("ammo");
								drawString(gui, minecraft.font, ammo + "", (int) (TOP_WIDTH * (ModConfigs.cmXScale / 100D) + (TOP_WIDTH * (ModConfigs.cmXScale / 100D)) * 0.10), 4, 0xFFFFFF);
							}
						}
					}
				}
				matrixStack.popPose();
			}
			
		}
		matrixStack.popPose();
		
		RenderSystem.disableBlend();
	}

	public void drawSubPortals(GuiGraphics gui, int width, int height) {
		PoseStack matrixStack = gui.pose();
		RenderSystem.enableBlend();
		IWorldCapabilities worldData = ModCapabilities.getWorld(minecraft.player.level());

        List<UUID> portals = worldData.getAllPortalsFromOwnerID(minecraft.player.getUUID());
        
		if (portals != null && !portals.isEmpty()) {
			// PORTAL TOP
			double x = 10 * ModConfigs.cmSubXOffset / 100D;
			matrixStack.pushPose();
			{
				paintWithColorArray(gui, portalMenuColor, alpha);
				matrixStack.translate(x, (height - MENU_HEIGHT * scale * (portals.size() + 1)), 1);
				matrixStack.scale(scale, scale, scale);
				drawHeader(gui, Strings.Gui_CommandMenu_Portals_Title, SUB_PORTALS);
			}
			matrixStack.popPose();
			if (submenu == SUB_PORTALS) {
				for (int i = 0; i < portals.size(); i++) {
					matrixStack.pushPose();
					{
						matrixStack.translate(x, (height - MENU_HEIGHT * scale * (portals.size() - i)), 1);
						matrixStack.scale(scale, scale, scale);
						paintWithColorArray(gui, portalMenuColor, alpha);
						textX = (int) (5 * ModConfigs.cmXScale / 100D) + ModConfigs.cmTextXOffset;

						if (portalSelected == i) {
							//System.out.println(textX);
							textX += ModConfigs.cmSelectedXOffset;
							drawSelectedSlot(gui);
							drawIcon(gui, selected, SUB_PORTALS);
						} else { // Not selected
							drawUnselectedSlot(gui);
						}
						//System.out.println(textX);
						UUID portalUUID = portals.get(i);
						PortalData portal = worldData.getPortalFromUUID(portalUUID);
						if(portal != null)
							drawString(gui, minecraft.font, Utils.translateToLocal(portal.getName()), textX, 4, 0xFFFFFF);
						}
					matrixStack.popPose();
				}
			}
		}
		RenderSystem.disableBlend();
	}
	
	private void drawSubTargetSelector(GuiGraphics gui, int width, int height) {
		PoseStack matrixStack = gui.pose();
		RenderSystem.enableBlend();
		IWorldCapabilities worldData = ModCapabilities.getWorld(minecraft.level);
		if(worldData == null || worldData.getPartyFromMember(minecraft.player.getUUID()) == null) {
			submenu = SUB_MAIN;
			return;
		}
		double x = 20 * ModConfigs.cmSubXOffset / 100D;

		//Title
		matrixStack.pushPose();
		{
			paintWithColorArray(gui, targetModeColor, alpha);
			matrixStack.translate(x, (height - MENU_HEIGHT * scale * (worldData.getPartyFromMember(minecraft.player.getUUID()).getMembers().size() + 1)), 2);
			matrixStack.scale(scale, scale, scale);
			drawHeader(gui, "TARGET", SUB_TARGET);
		}
		matrixStack.popPose();

		for (int i = 0; i < worldData.getPartyFromMember(minecraft.player.getUUID()).getMembers().size(); i++) {
			matrixStack.pushPose();
			{
				matrixStack.translate(x, (height - MENU_HEIGHT * scale * (worldData.getPartyFromMember(minecraft.player.getUUID()).getMembers().size() - i)), 2);
				matrixStack.scale(scale, scale, scale);
				if (submenu == SUB_TARGET) {
					paintWithColorArray(gui, targetModeColor, alpha);
					if (targetSelected == i) {
						textX += ModConfigs.cmSelectedXOffset;
						drawSelectedSlot(gui);
						drawIcon(gui, selected, SUB_TARGET);
					} else { // Not selected
						textX = (int) (5 * ModConfigs.cmXScale / 100D) + ModConfigs.cmTextXOffset;
						drawUnselectedSlot(gui);
					}
					Member member = worldData.getPartyFromMember(minecraft.player.getUUID()).getMembers().get(i);
	            	if(minecraft.level.getPlayerByUUID(member.getUUID()) != null && minecraft.player.distanceTo(minecraft.level.getPlayerByUUID(member.getUUID())) < ModConfigs.partyRangeLimit) {
						drawString(gui, minecraft.font, member.getUsername(), textX, 4, 0xFFFFFF);
					} else {
						drawString(gui, minecraft.font, member.getUsername(), textX, 4, 0x888888);
					}
				}
			}
			matrixStack.popPose();
		}
		RenderSystem.disableBlend();
	}

	private void drawSubMagic(GuiGraphics gui, int width, int height) {
		PoseStack matrixStack = gui.pose();
		RenderSystem.enableBlend();
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);
		//LinkedHashMap<String, int[]> magics = Utils.getSortedMagics(playerData.getMagicsMap());
		List<String> magics = ModConfigs.magicDisplayedInCommandMenu.stream().filter(magic -> playerData.getMagicsMap().containsKey(magic)).toList();

		if (playerData != null && magics != null && !magics.isEmpty()) {
			// MAGIC TOP
			double x = 10 * ModConfigs.cmSubXOffset / 100D;
			matrixStack.pushPose();
			{
				matrixStack.translate(x, (height - MENU_HEIGHT * scale * (magics.size() + 1)), 1);
				matrixStack.scale(scale, scale, scale);
				paintWithColorArray(gui, magicMenuColor, alpha);
				drawHeader(gui, Strings.Gui_CommandMenu_Magic_Title, SUB_MAGIC);
			}
			matrixStack.popPose();
			
			for (int i = 0; i < magics.size(); i++) {
				matrixStack.pushPose();
				{
					matrixStack.translate(x, (height - MENU_HEIGHT * scale * (magics.size() - i)), 1);
					matrixStack.scale(scale, scale, scale);
					paintWithColorArray(gui, magicMenuColor, alpha);
					textX = (int) (5 * ModConfigs.cmXScale / 100D) + ModConfigs.cmTextXOffset;

					if (magicSelected == i) {
						textX += ModConfigs.cmSelectedXOffset;
						drawSelectedSlot(gui);
						drawIcon(gui, selected, SUB_MAGIC);
					} else { // Not selected
						drawUnselectedSlot(gui);
					}

					String magic = magics.get(i);
					int magicLevel = playerData.getMagicLevel(new ResourceLocation(magic));
					Magic magicInstance = ModMagic.registry.get().getValue(new ResourceLocation(magic));
					int[] mag = playerData.getMagicsMap().get(magic);
					double cost = magicInstance.getCost(mag[0], minecraft.player);
					int colour = playerData.getMP() > cost ? 0xFFFFFF : 0xFF9900;

					if(playerData.isAbilityEquipped(Strings.extraCast) && cost > playerData.getMP() && playerData.getMP() > 1 && cost < 300) {
						colour = 0xFFFFFF;
					}

					if(playerData.getMaxMP() == 0 || playerData.getRecharge() || cost > playerData.getMaxMP() && cost < 300 || playerData.getMagicCasttimeTicks() > 0 || playerData.getMagicCooldownTicks() > 0) {
						colour = 0x888888;
					}

					magic = magicInstance.getTranslationKey(magicLevel);

					drawString(gui, minecraft.font, Utils.translateToLocal(magic), textX, 4, getColor(colour, SUB_MAGIC));
				}
				matrixStack.popPose();
			}
		}
		RenderSystem.disableBlend();
	}
	
	private void drawSubItems(GuiGraphics gui, int width, int height) {
		PoseStack matrixStack = gui.pose();
		RenderSystem.enableBlend();
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);

		Map<Integer, ItemStack> items = Utils.getEquippedItems(playerData.getEquippedItems());
		
		double x = 10 * ModConfigs.cmSubXOffset / 100D;
		
		if (playerData != null && items != null && !items.isEmpty()) {
			// DRIVE TOP
			matrixStack.pushPose();
			{
				paintWithColorArray(gui, itemsMenuColor, alpha);
				matrixStack.translate(x, (height - MENU_HEIGHT * scale * (items.size()+1)), 1);
				matrixStack.scale(scale, scale, scale);
				drawHeader(gui, Strings.Gui_CommandMenu_Items_Title, SUB_ITEMS);
			}
			matrixStack.popPose();
			
			int c = 0;
			for(Entry<Integer, ItemStack> entry : items.entrySet()) {
				ItemStack itemStack = entry.getValue();

				matrixStack.pushPose();
				{
					matrixStack.translate(x, (height - MENU_HEIGHT * scale * (items.size() - c)), 1);
					matrixStack.scale(scale, scale, scale);
					paintWithColorArray(gui, itemsMenuColor, alpha);
					textX = (int) (5 * ModConfigs.cmXScale / 100D) + ModConfigs.cmTextXOffset;

					if (itemSelected == c) {
						textX += ModConfigs.cmSelectedXOffset;
						drawSelectedSlot(gui);
						drawIcon(gui, selected, SUB_ITEMS);
					} else { // Not selected
						drawUnselectedSlot(gui);
					}
					drawString(gui, minecraft.font, Utils.translateToLocal(itemStack.getDescriptionId()), textX, 4, 0xFFFFFF);
					
				}
				matrixStack.popPose();
				c++;
			}
		}
		RenderSystem.disableBlend();
	}

	private void drawSubDrive(GuiGraphics gui, int width, int height) {
		PoseStack matrixStack = gui.pose();
		RenderSystem.enableBlend();
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);

		LinkedHashMap<String, int[]> forms = Utils.getSortedDriveForms(playerData.getDriveFormMap(), playerData.getVisibleDriveForms());
		if(!playerData.isAbilityEquipped(Strings.darkDomination)) {
			forms.remove(Strings.Form_Anti);
		}
		forms.remove(DriveForm.NONE.toString());
		forms.remove(DriveForm.SYNCH_BLADE.toString());
		
		double x = 10 * ModConfigs.cmSubXOffset / 100D;
		
		if (playerData != null && forms != null && !forms.isEmpty()) {
			// DRIVE TOP
			matrixStack.pushPose();
			{
				paintWithColorArray(gui, driveMenuColor, alpha);
				matrixStack.translate(x, (height - MENU_HEIGHT * scale * (forms.size()+1)), 1);
				matrixStack.scale(scale, scale, scale);
				drawHeader(gui, Strings.Gui_CommandMenu_Drive_Title, SUB_DRIVE);
			}
			matrixStack.popPose();

			for (int i = 0; i < forms.size(); i++) {
				String formName = (String) forms.keySet().toArray()[i];
				if (!formName.equals(DriveForm.NONE.toString()) && !formName.equals(DriveForm.SYNCH_BLADE.toString())) {
					DriveForm form = ModDriveForms.registry.get().getValue(new ResourceLocation(formName));
					int cost = form.getDriveCost();
					int color = playerData.getDP() >= cost ? 0xFFFFFF : 0x888888;
					formName = form.getTranslationKey();

					matrixStack.pushPose();
					{
						matrixStack.translate(x, (height - MENU_HEIGHT * scale * (forms.size() - i)), 1);
						matrixStack.scale(scale, scale, scale);

						if (submenu == SUB_DRIVE) {
							textX = (int) (5 * ModConfigs.cmXScale / 100D) + ModConfigs.cmTextXOffset;

							paintWithColorArray(gui, driveMenuColor, alpha);
							if (driveSelected == i) {
								textX += ModConfigs.cmSelectedXOffset;
								drawSelectedSlot(gui);
								drawIcon(gui, selected, SUB_DRIVE);
							} else { // Not selected
								drawUnselectedSlot(gui);
							}
						}
					
						if (submenu == SUB_DRIVE) {
							drawString(gui, minecraft.font, Utils.translateToLocal(formName), textX, 4, color);
						}
					}
					matrixStack.popPose();
				}
			}
		}
		RenderSystem.disableBlend();
	}
	
	private void drawSubLimits(GuiGraphics gui, int width, int height) {
		PoseStack matrixStack = gui.pose();
		RenderSystem.enableBlend();
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);
		LinkedHashMap<String, int[]> forms = Utils.getSortedDriveForms(playerData.getDriveFormMap(), playerData.getVisibleDriveForms());

		List<Limit> limits = Utils.getSortedLimits(Utils.getPlayerLimitAttacks(minecraft.player));
		
		if (playerData != null && limits != null && !limits.isEmpty()) {
			// Limit TOP
			double x = 10 * ModConfigs.cmSubXOffset / 100D;
			matrixStack.pushPose();
			{
				matrixStack.translate(x, (height - MENU_HEIGHT * scale * (limits.size() + 1)), 1);
				matrixStack.scale(scale, scale, scale);
				paintWithColorArray(gui, limitMenuColor, alpha);
				drawHeader(gui, Strings.Gui_CommandMenu_Limit_Title, SUB_LIMIT);
			}
			matrixStack.popPose();
			
			for (int i = 0; i < limits.size(); i++) {
				matrixStack.pushPose();
				{
					matrixStack.translate(x, (height - MENU_HEIGHT * scale * (limits.size() - i)), 1);
					matrixStack.scale(scale, scale, scale);

					paintWithColorArray(gui, limitMenuColor, alpha);
					textX = (int) (5 * ModConfigs.cmXScale / 100D) + ModConfigs.cmTextXOffset;

					if (limitSelected == i) {
						textX += ModConfigs.cmSelectedXOffset;
						drawSelectedSlot(gui);
						drawIcon(gui, selected, SUB_LIMIT);
					} else { // Not selected
						drawUnselectedSlot(gui);
					}

					int limitCost = limits.get(i).getCost();
					int color = playerData.getDP() >= limitCost ? 0xFFFFFF : 0x888888;

					String name = limits.get(i).getTranslationKey();
					matrixStack.scale(scale*0.7F, scale*0.9F, scale);
					drawString(gui, minecraft.font, Utils.translateToLocal(name), (int)(textX * scale * 1.3F), 4, getColor(color, SUB_LIMIT));
					matrixStack.scale(scale*1.3F, scale*1.1F, scale);
					drawString(gui, minecraft.font, limitCost/100 + "", (int) (TOP_WIDTH * (ModConfigs.cmXScale / 100D)  + textX), 4, getColor(0x00FFFF, SUB_LIMIT));

				}
				matrixStack.popPose();
			}
		}
		RenderSystem.disableBlend();
	}
}

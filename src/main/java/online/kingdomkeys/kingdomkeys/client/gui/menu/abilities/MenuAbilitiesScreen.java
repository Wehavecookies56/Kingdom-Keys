package online.kingdomkeys.kingdomkeys.client.gui.menu.abilities;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.NeoForge;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.ability.Ability;
import online.kingdomkeys.kingdomkeys.ability.Ability.AbilityType;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.api.event.AbilityEvent;
import online.kingdomkeys.kingdomkeys.api.item.IKeychain;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBox;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuAbilitiesButton;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton.ButtonType;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuScrollBar;
import online.kingdomkeys.kingdomkeys.client.gui.menu.MenuScreen;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
import online.kingdomkeys.kingdomkeys.lib.Constants;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSOpenMenu;
import online.kingdomkeys.kingdomkeys.network.cts.CSSetEquippedAbilityPacket;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.kingdomkeys.kingdomkeys.util.Utils.OrgMember;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class MenuAbilitiesScreen extends MenuBackground {
	ResourceLocation form = DriveForm.NONE;

	LinkedHashMap<ResourceLocation, int[]> abilitiesMap;
    List<MenuAbilitiesButton> abilities = new ArrayList<>();

	MenuBox box;
	MenuButton back, playerButton;
	
	List<MenuButton> driveSelector = new ArrayList<>();

	int itemsPerPage;

	Ability hoveredAbility;
	int hoveredIndex;
	AbilityType hoveredType;

	MenuScrollBar scrollBar;

	public MenuAbilitiesScreen() {
		super(Strings.Gui_Menu_Main_Button_Abilities, new Color(0,0,255));
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double deltaX, double deltaY) {
		scrollBar.mouseScrolled(mouseX, mouseY, deltaX, deltaY);
		return false;
	}

	protected void action(String string) {
		switch (string) {
		case "back":
			Minecraft.getInstance().setScreen(new MenuScreen(playerData));
			break;
		default:
			form = KingdomKeys.rl(string);
			init();
			break;
		}
		updateButtons();

	}
	
	private void action(Ability ability, int index) {
		int apCost = ability.getAPCost();

		if (!playerData.isAbilityEquipped(ability.getRegistryName(), index)) {
			if (Utils.getConsumedAP(playerData) + apCost > playerData.getMaxAP(true)) {
				return;
			}
		}
		boolean cancelled;
		if (playerData.isAbilityEquipped(ability.getRegistryName(), index)) {
			cancelled = NeoForge.EVENT_BUS.post(new AbilityEvent.Unequip(ability, index, Minecraft.getInstance().player, true)).isCanceled();
		} else {
			cancelled = NeoForge.EVENT_BUS.post(new AbilityEvent.Equip(ability, index, Minecraft.getInstance().player, true)).isCanceled();
		}
		if (!cancelled) {
			playerData.equipAbilityToggle(ability.getRegistryName(), index);
			PacketHandler.sendToServer(new CSSetEquippedAbilityPacket(ability.getRegistryName(), index));
		}
		updateButtons();
	}

	private void updateButtons() {
        for (MenuAbilitiesButton button : abilities) { //Somehow buttons get disabled so we reenable them all and allow the later check to calculate AP
            button.active = true;
        }
		
		playerButton.active = !form.equals(DriveForm.NONE); //If form is empty we assume it's the player stats view
        for (MenuButton menuButton : driveSelector) {//Iterate through all the buttons to update their state
            menuButton.active = !form.equals(KingdomKeys.rl(menuButton.getData())) && playerData.getDriveFormMap().containsKey(KingdomKeys.rl(menuButton.getData())); //If the form stored in class is the same as the button name (handling prefix and such) and you have that form unlocked
            menuButton.setSelected(!menuButton.active); //Set it selected if it's not active (so it renders a bit to the right)
        }

	}

	int scrollTop, scrollBot;

	@Override
	public void init() {
		super.init();

		renderables.clear();
		children().clear();
		abilities.clear();

		float boxPosX = (float) width * 0.2F;
		float boxWidth = (float) width * 0.5F;
		box = new MenuBox((int) boxPosX, (int) topBarHeight, (int) boxWidth, (int) middleHeight, 0.6F,new Color(4, 4, 68));

		int buttonPosX = (int) (boxPosX * 1.3F);
		int buttonPosY = (int) topBarHeight + 5;
		int buttonWidth = (int) (boxWidth * 0.46F);

		scrollTop = (int) topBarHeight;
		scrollBot = (int) (scrollTop + middleHeight);

		abilitiesMap = Utils.getSortedAbilities(playerData.getAbilityMap());

		if (form.equals(DriveForm.NONE)) {
			int i = 0;
			for (i = 0; i < abilitiesMap.size(); i++) {
				ResourceLocation abilityName = (ResourceLocation) abilitiesMap.keySet().toArray()[i];
				Ability ability = ModAbilities.registry.get(abilityName);

				int level = abilitiesMap.get(abilityName)[0];
				if (level == 0 || ability.getType() == AbilityType.GROWTH) {
					abilities.add(new MenuAbilitiesButton(buttonPosX, buttonPosY, buttonWidth, abilityName.toString(), ability.getType(), (e) -> {
						action(ability, 0);
					}, player, playerData));
				} else {
					for (int j = 0; j < level; j++) {
						int finalJ = j;
						abilities.add(new MenuAbilitiesButton(buttonPosX, buttonPosY, buttonWidth, abilityName.toString(), finalJ, ability.getType(), (e) -> {
							action(ability, finalJ);
						}, player, playerData));
					}
				}
				abilities.get(i).visible = false;
			}
			
			//Main keyblade
			if(playerData.getAlignment() == OrgMember.NONE) {
				if(!ItemStack.matches(playerData.getEquippedKeychain(DriveForm.NONE), ItemStack.EMPTY)){
					List<ResourceLocation> abilitiesList = Utils.getKeybladeAbilitiesAtLevel(playerData.getEquippedKeychain(DriveForm.NONE).getItem(), ((IKeychain) playerData.getEquippedKeychain(DriveForm.NONE).getItem()).toSummon().getKeybladeLevel(playerData.getEquippedKeychain(DriveForm.NONE)));
					for(ResourceLocation a : abilitiesList) {
						Ability ability = ModAbilities.registry.get(a);
						if(ability != null) {
							MenuAbilitiesButton aa = new MenuAbilitiesButton(buttonPosX, buttonPosY, buttonWidth, ability.getRegistryName().toString(), AbilityType.WEAPON, (e) -> { }, player, playerData);
							abilities.add(aa);
							aa.visible = false;
						}
					}
				}
			} else {// If org member
				if (!ItemStack.matches(playerData.getEquippedWeapon(), ItemStack.EMPTY)) {
					List<ResourceLocation> abilitiesList;
					if (playerData.getAlignment() == OrgMember.ROXAS) {
						abilitiesList = Utils.getKeybladeAbilitiesAtLevel(playerData.getEquippedWeapon().getItem(), 0);
					} else { //any member but roxas or none
						abilitiesList = Utils.getOrgWeaponAbilities(playerData.getEquippedWeapon().getItem());
					}
				
					for (ResourceLocation a : abilitiesList) {
						Ability ability = ModAbilities.registry.get(a);
						if (ability != null) { //Add weapon ability display
							MenuAbilitiesButton aa = new MenuAbilitiesButton(buttonPosX, buttonPosY, buttonWidth, ability.getRegistryName().toString(), AbilityType.WEAPON, (e) -> {
							}, player, playerData);
							abilities.add(aa);
							aa.visible = false;
							
							//If synch blade do it again
							if(playerData.getAbilityMap().containsKey(ModAbilities.SYNCH_BLADE.location()) && playerData.getAbilityMap().get(ModAbilities.SYNCH_BLADE.location())[1] > 0) { //Org synch blade
								MenuAbilitiesButton aaa = new MenuAbilitiesButton(buttonPosX, buttonPosY, buttonWidth, ability.getRegistryName().toString(), AbilityType.WEAPON, (e) -> {
								}, player, playerData);
								abilities.add(aaa);
								aaa.visible = false;
							}
						}
					}
				}
			}
			
			//Synch blade Keyblade
			if (playerData.noFormActive()){
				if(playerData.getAbilityMap().containsKey(ModAbilities.SYNCH_BLADE.location()) && playerData.getAbilityMap().get(ModAbilities.SYNCH_BLADE.location())[1] > 0 && !ItemStack.matches(playerData.getEquippedKeychain(DriveForm.SYNCH_BLADE), ItemStack.EMPTY)) {
					List<ResourceLocation> abilitiesList = Utils.getKeybladeAbilitiesAtLevel(playerData.getEquippedKeychain(DriveForm.SYNCH_BLADE).getItem(), ((IKeychain) playerData.getEquippedKeychain(DriveForm.SYNCH_BLADE).getItem()).toSummon().getKeybladeLevel(playerData.getEquippedKeychain(DriveForm.SYNCH_BLADE)));
					for (ResourceLocation a : abilitiesList) {
						Ability ability = ModAbilities.registry.get(a);
						if (ability != null) {
							MenuAbilitiesButton aa = new MenuAbilitiesButton(buttonPosX, buttonPosY, buttonWidth,  ability.getRegistryName().toString(), AbilityType.WEAPON, (e) -> {
							}, player, playerData);
							abilities.add(aa);
							aa.visible = false;
						}
					}
				}
			} else { // Form keyblade abilities
				if (ModDriveForms.registry.containsKey(playerData.getActiveDriveForm()) && ModDriveForms.registry.get(playerData.getActiveDriveForm()).hasKeychain()) {
					if (playerData.getDriveFormMap().containsKey(playerData.getActiveDriveForm()) && playerData.getEquippedKeychains().containsKey(playerData.getActiveDriveForm()) && !ItemStack.matches(playerData.getEquippedKeychain(playerData.getActiveDriveForm()), ItemStack.EMPTY)) {
						ItemStack itemStack = playerData.getEquippedKeychain(playerData.getActiveDriveForm());
						List<ResourceLocation> abilitiesList = Utils.getKeybladeAbilitiesAtLevel(itemStack.getItem(), ((IKeychain) itemStack.getItem()).toSummon().getKeybladeLevel(itemStack));
						for (ResourceLocation a : abilitiesList) {
							Ability ability = ModAbilities.registry.get(a);
							if (ability != null) {
								MenuAbilitiesButton aa = new MenuAbilitiesButton(buttonPosX, buttonPosY, buttonWidth, ability.getRegistryName().toString(), AbilityType.WEAPON, (e) -> {
								}, player, playerData);
								abilities.add(aa);
								aa.visible = false;
							}
						}
					}
				}
			}
			
			List<ResourceLocation> abilitiesList = Utils.getAccessoriesAbilities(playerData);
			for (ResourceLocation a : abilitiesList) {
				Ability ability = ModAbilities.registry.get(a);
				if (ability != null) {
					MenuAbilitiesButton aa = new MenuAbilitiesButton(buttonPosX, buttonPosY, buttonWidth, ability.getRegistryName().toString(), AbilityType.ACCESSORY, (e) -> {
					}, player, playerData);
					abilities.add(aa);
					aa.visible = false;
				}
			}
			
		} else { //Drive form displays with disabled and equipped buttons
			//Display list of abilities in the drive form data
			DriveForm driveForm = ModDriveForms.registry.get(form);
			if(driveForm.getBaseGrowthAbilities()) { //If the selected drive form inherits base form growth abilities
				for (int i = 0; i < abilitiesMap.size(); i++) {
					ResourceLocation abilityName = (ResourceLocation) abilitiesMap.keySet().toArray()[i];
					Ability ability = ModAbilities.registry.get(abilityName);

					int level = abilitiesMap.get(abilityName)[0];
					if (level == 0 || ability.getType() == AbilityType.GROWTH) {
						MenuAbilitiesButton aa = new MenuAbilitiesButton(buttonPosX, buttonPosY, buttonWidth, abilityName.toString(), ability.getType(), (e) -> {
						}, player, playerData);

						abilities.add(aa);
						aa.visible = false;
						aa.isVisual = true;

					}
				}
			} else { //If form doesn't inherit base form (common thing for kh2's)
				if(driveForm.getDriveFormData().getDFLevelUpAbilities() != null) {
					driveForm.getDFAbilityForLevel(1).ifPresent(growth -> {
						Ability ab = ModAbilities.registry.get(growth);
						if (ab != null) {
							MenuAbilitiesButton aa = new MenuAbilitiesButton(buttonPosX, buttonPosY, buttonWidth, growth.toString(), ab.getType(), (e) -> {
							}, player, playerData);
							abilities.add(aa);
							aa.visible = false;
							aa.isVisual = true;
						}
					});
				}
			}
			
			if(driveForm.getDriveFormData().getAbilities() != null) {
				for (ResourceLocation a : driveForm.getDriveFormData().getAbilities()) {
					Ability ability = ModAbilities.registry.get(a);
					if (ability != null) {
						MenuAbilitiesButton aa = new MenuAbilitiesButton(buttonPosX, buttonPosY, buttonWidth, ability.getRegistryName().toString(), ability.getType(), (e) -> {
						}, player, playerData);
						abilities.add(aa);
						aa.visible = false;
						aa.isVisual = true;
					}
				}
			}
			//Main keyblade
			if(!ItemStack.matches(playerData.getEquippedKeychain(DriveForm.NONE), ItemStack.EMPTY)){
				List<ResourceLocation> abilitiesList = Utils.getKeybladeAbilitiesAtLevel(playerData.getEquippedKeychain(DriveForm.NONE).getItem(), ((IKeychain) playerData.getEquippedKeychain(DriveForm.NONE).getItem()).toSummon().getKeybladeLevel(playerData.getEquippedKeychain(DriveForm.NONE)));
				for(ResourceLocation a : abilitiesList) {
					Ability ability = ModAbilities.registry.get(a);
					if(ability != null) {
						MenuAbilitiesButton aa = new MenuAbilitiesButton(buttonPosX, buttonPosY, buttonWidth, ability.getRegistryName().toString(), AbilityType.WEAPON, (e) -> { }, player, playerData);
						abilities.add(aa);
						aa.visible = false;
					}
				}
			}
			// Selected Drive form 
			if (ModDriveForms.registry.containsKey(form) && ModDriveForms.registry.get(form).hasKeychain()) {
				if (playerData.getDriveFormMap().containsKey(form) && playerData.getEquippedKeychains().containsKey(form)) {
					ItemStack itemStack = playerData.getEquippedKeychain(form);
					if(!ItemStack.matches(itemStack, ItemStack.EMPTY)){
						List<ResourceLocation> abilitiesList = Utils.getKeybladeAbilitiesAtLevel(itemStack.getItem(), ((IKeychain) itemStack.getItem()).toSummon().getKeybladeLevel(itemStack));
						for (ResourceLocation a : abilitiesList) {
							Ability ability = ModAbilities.registry.get(a);
							if (ability != null) {
								MenuAbilitiesButton aa = new MenuAbilitiesButton(buttonPosX, buttonPosY, buttonWidth, ability.getRegistryName().toString(), AbilityType.WEAPON, (e) -> {
								}, player, playerData);
								abilities.add(aa);
								aa.visible = false;
							}
						}
					}
				}
			}
		}
		
		abilities.forEach(this::addWidget);
		itemsPerPage = (int) (middleHeight / 19);
		
		addRenderableWidget(playerButton = new MenuButton((int)this.buttonPosX, this.buttonPosY, (int)this.buttonWidth, minecraft.player.getDisplayName().getString(), MenuButton.ButtonType.BUTTON, b -> {action(DriveForm.NONE.toString());}));
		List<ResourceLocation> forms = new ArrayList<>(Utils.getSortedDriveForms(playerData.getDriveFormMap(), Utils.getVisibleDriveForms(minecraft.player)).keySet());
		forms.remove(DriveForm.NONE);
		forms.remove(DriveForm.SYNCH_BLADE);
		forms.remove(ModDriveForms.ANTI.location());

		int k = 0;
		for (k = 0; k < forms.size(); k++) {
			ResourceLocation formName = forms.get(k);
			String name = ModDriveForms.registry.get(formName).getTranslationKey();
			MenuButton b = new MenuButton((int) this.buttonPosX + 10, this.buttonPosY + ((1+k) * 18), (int) this.buttonWidth-10, Utils.translateToLocal(name), ButtonType.SUBBUTTON, (e) -> {
				action(formName.toString());
			});
			b.setData(formName.toString());
			driveSelector.add(b);
			addRenderableWidget(b);
		}

        addRenderableWidget(back = new MenuButton((int)this.buttonPosX, this.buttonPosY + ((1+k) * 18), (int)this.buttonWidth, Component.translatable(Strings.Gui_Menu_Back).getString(), MenuButton.ButtonType.BUTTON, b -> action("back")));
		scrollBar = new MenuScrollBar((int) (boxPosX + boxWidth - 17), scrollTop, scrollBot, (int) middleHeight, 0, true);
		addRenderableWidget(scrollBar);
		
		updateButtons();
	}

	@Override
	public void render(GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
		box.renderWidget(gui, mouseX, mouseY, partialTicks);
		super.render(gui, mouseX, mouseY, partialTicks);
		drawAP(gui);

		for (int i = 0; i < abilities.size(); i++) {
			if (abilities.get(i) != null) {
				abilities.get(i).visible = true;
				abilities.get(i).active = false;
				abilities.get(i).setY((int) (topBarHeight) + (i) * 19 + 2); // 6 = offset
			}
		}

		if(!abilities.isEmpty()) {
			int listHeight = (abilities.get(abilities.size() - 1).getY() + 20) - abilities.get(0).getY() + 3;
			scrollBar.setContentHeight(listHeight);
		}

		gui.enableScissor(0, (int) topBarHeight, width, (int) (topBarHeight + middleHeight));

        for (MenuAbilitiesButton menuAbilitiesButton : abilities) {
            if (menuAbilitiesButton != null) {
                menuAbilitiesButton.setY((int) (menuAbilitiesButton.getY() - scrollBar.scrollOffset));
                if (menuAbilitiesButton.getY() < scrollBot && menuAbilitiesButton.getY() >= scrollTop - 20) {
                    menuAbilitiesButton.active = true;
                    String abilityName = menuAbilitiesButton.getText();
                    Ability ability = ModAbilities.registry.get(KingdomKeys.rl(abilityName));

                    if (ability.getAPCost() > playerData.getMaxAP(true) - Utils.getConsumedAP(playerData)) {
                        menuAbilitiesButton.active = menuAbilitiesButton.equipped;
                    }

                    if (menuAbilitiesButton.abilityType == AbilityType.WEAPON || menuAbilitiesButton.abilityType == AbilityType.ACCESSORY || form.equals(DriveForm.NONE) && playerData.isAbilityEquipped(KingdomKeys.rl(menuAbilitiesButton.getText()), abilitiesMap.get(KingdomKeys.rl(menuAbilitiesButton.getText()))[0])) {
                        menuAbilitiesButton.active = true;
                    }

                    menuAbilitiesButton.render(gui, mouseX, mouseY, partialTicks);
                }
            }
        }
		gui.disableScissor();

		playerButton.render(gui, mouseX, mouseY, partialTicks);
		back.render(gui, mouseX, mouseY, partialTicks);
		if(hoveredAbility != null) {
			renderSelectedData(gui, mouseX, mouseY, partialTicks);
		}
	}

	protected void renderSelectedData(GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
		ClientUtils.drawSplitString(gui, Component.translatable(hoveredAbility.getTranslationKey().replace(".name", ".desc")).getString(), (int) tooltipPosX, (int) tooltipPosY, (int) (width * 0.6F), 0x00FFFF);
	}
	
	private void drawAP(GuiGraphics gui) {
		PoseStack matrixStack = gui.pose();
		int consumedAP = Utils.getConsumedAP(playerData);
		int maxAP = playerData.getMaxAP(true);
		hoveredAbility = null;
		
		//Get all the abilities and set their text
		for (int i = 0; i < abilities.size(); i++) {
			ResourceLocation abilityName = KingdomKeys.rl(abilities.get(i).getText());
			Ability ability = ModAbilities.registry.get(abilityName);

			String lvl = "";
			if (ability.getType() == AbilityType.GROWTH) {
				DriveForm df = ModDriveForms.registry.get(playerData.getActiveDriveForm());
				int level = (form.equals(DriveForm.NONE) || df.getBaseGrowthAbilities() ? playerData.getEquippedAbilityLevel(abilityName)[0] : playerData.getEquippedAbilityLevel(abilityName)[0]+1);
				lvl += "_" + level;
			}
			String abilityTranslationKey = ability.getTranslationKey();
			String text = Utils.translateToLocal(new StringBuilder(abilityTranslationKey).insert(abilityTranslationKey.lastIndexOf('.'), lvl).toString());

			if (abilities.get(i) instanceof MenuAbilitiesButton) {
				MenuAbilitiesButton button = getMenuAbilitiesButton(i, text, ability);
				if (button.isHovered()) {
					hoveredAbility = ability;
					hoveredIndex = button.index;
					hoveredType = button.abilityType;
				}
			}
		}

		int screenWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
		int screenHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();

		int barWidth = (int)(width * 0.2F);
		int posX = screenWidth - barWidth;
		int posY = screenHeight - 100;
		float scale = 1F;

		// Global
		matrixStack.pushPose();
		{
			matrixStack.translate((posX - 2) * scale - 20, posY * scale - 10, 0);
			RenderSystem.setShaderColor(1, 1, 1, 1);

			gui.blit(Constants.MENU_TEXTURE, 0, 0, 143, 67, 7, 25); // Left
			gui.blit(Constants.MENU_TEXTURE, 7, 0, barWidth, 25, 151, 67, 1, 25,256,256); // Middle
			gui.blit(Constants.MENU_TEXTURE, 7 + barWidth, 0, 153, 67, 7, 25); // Right

			gui.blit(Constants.MENU_TEXTURE, 7, 17, barWidth, 25, 161, 67, 1, 25,256,256); // Bar Background

			int requiredAP = (hoveredAbility != null) ? hoveredAbility.getAPCost() : 0;

			if(hoveredType != AbilityType.WEAPON && hoveredType != AbilityType.ACCESSORY) {
				if (hoveredAbility != null && playerData.isAbilityEquipped(hoveredAbility.getRegistryName(), hoveredIndex)) { // If hovering an equipped ability
					requiredAP *= -1;
					// Bar going to decrease (dark yellow section when hovering equipped ability)
					int percent = (consumedAP) * barWidth / maxAP;
					gui.blit(Constants.MENU_TEXTURE, 7, 17, percent, 5, 165, 67, 1, 5,256,256);
				} else {
					if(consumedAP + requiredAP <= playerData.getMaxAP(true)) {
						// Bar going to increase (blue section when hovering unequipped ability)
						int percent = (consumedAP + requiredAP) * barWidth / maxAP;
						gui.blit(Constants.MENU_TEXTURE, 7, 17, percent, 5, 167, 67, 1, 5,256,256);
					}
				}
			}

			// Foreground
			matrixStack.pushPose();
			{
				int percent = (consumedAP) * barWidth / maxAP;
				if (requiredAP < 0)
					percent = (consumedAP + requiredAP) * barWidth / maxAP;
				gui.blit(Constants.MENU_TEXTURE, 7, 17, percent, 5, 163, 67, 1, 5,256,256);
			}
			matrixStack.popPose();

			// AP Text
			matrixStack.pushPose();
			{
				matrixStack.scale(scale * 1.3F, scale * 1.1F, 0);
				gui.drawString(minecraft.font, Utils.translateToLocal(Strings.Gui_Menu_Status_AP)+": " + consumedAP + "/" + maxAP, 16, 5, 0xFFFFFF);
			}
			matrixStack.popPose();
			
		}
		matrixStack.popPose();
	}

	private @NotNull MenuAbilitiesButton getMenuAbilitiesButton(int i, String text, Ability ability) {
		MenuAbilitiesButton button = abilities.get(i);

				/*if (ability.getAPCost() > playerData.getMaxAP(true) - consumedAP) {
					button.active = button.equipped;
				}
				
				if (button.abilityType == AbilityType.WEAPON || button.abilityType == AbilityType.ACCESSORY || form.equals(DriveForm.NONE.toString()) && playerData.isAbilityEquipped(abilities.get(i).getText(), abilitiesMap.get(abilities.get(i).getText())[0])) {
					button.active = true;
				}*/

		button.setMessage(Component.translatable(text));
		button.setAP(ability.getAPCost());
		return button;
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
		scrollBar.mouseClicked(mouseX, mouseY, mouseButton);
		if (mouseButton == 1) {
			PacketHandler.sendToServer(new CSOpenMenu());
		}
		return super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public boolean mouseReleased(double pMouseX, double pMouseY, int pButton) {
		scrollBar.mouseReleased(pMouseX, pMouseY, pButton);
		return super.mouseReleased(pMouseX, pMouseY, pButton);
	}

	@Override
	public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
		scrollBar.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
		return super.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
	}


}

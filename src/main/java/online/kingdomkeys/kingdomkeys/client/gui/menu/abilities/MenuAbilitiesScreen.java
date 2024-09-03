package online.kingdomkeys.kingdomkeys.client.gui.menu.abilities;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.ability.Ability;
import online.kingdomkeys.kingdomkeys.ability.Ability.AbilityType;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.api.event.AbilityEvent;
import online.kingdomkeys.kingdomkeys.api.item.IKeychain;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.client.gui.GuiHelper;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBox;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuAbilitiesButton;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton.ButtonType;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuScrollBar;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSSetEquippedAbilityPacket;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.kingdomkeys.kingdomkeys.util.Utils.OrgMember;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class MenuAbilitiesScreen extends MenuBackground {
	String form = DriveForm.NONE.toString();

	IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);
	LinkedHashMap<String, int[]> abilitiesMap;
    List<MenuAbilitiesButton> abilities = new ArrayList<>();

	MenuBox box;
	//Button prev, next;
	MenuButton back, playerButton;
	
	List<MenuButton> driveSelector = new ArrayList<>();

	int page = 0;
	int itemsPerPage;

	Ability hoveredAbility;
	int hoveredIndex;
	AbilityType hoveredType;

	MenuScrollBar scrollBar;

	final ResourceLocation texture = new ResourceLocation(KingdomKeys.MODID, "textures/gui/menu/menu_button.png");
	
	public MenuAbilitiesScreen() {
		super(Strings.Gui_Menu_Main_Button_Abilities, new Color(0,0,255));
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
		scrollBar.mouseScrolled(mouseX, mouseY, delta);
		return false;
	}

	protected void action(String string) {
		switch (string) {
		case "back":
			GuiHelper.openMenu();
			break;
		default:
			form = string;
			init();
			break;
		}
		updateButtons();

	}
	
	private void action(Ability ability, int index) {
		String abilityName = ability.getRegistryName().toString();
		int apCost = ModAbilities.registry.get().getValue(new ResourceLocation(abilityName)).getAPCost();

		if (!playerData.isAbilityEquipped(abilityName, index)) {
			if (Utils.getConsumedAP(playerData) + apCost > playerData.getMaxAP(true)) {
				return;
			}
		}
		boolean cancelled;
		if (playerData.isAbilityEquipped(abilityName, index)) {
			cancelled = MinecraftForge.EVENT_BUS.post(new AbilityEvent.Unequip(ModAbilities.registry.get().getValue(new ResourceLocation(abilityName)), index, Minecraft.getInstance().player, true));
		} else {
			cancelled = MinecraftForge.EVENT_BUS.post(new AbilityEvent.Equip(ModAbilities.registry.get().getValue(new ResourceLocation(abilityName)), index, Minecraft.getInstance().player, true));
		}
		if (!cancelled) {
			playerData.equipAbilityToggle(abilityName, index);
			PacketHandler.sendToServer(new CSSetEquippedAbilityPacket(abilityName, index));
		}
		updateButtons();
	}

	private void updateButtons() {
        for (MenuAbilitiesButton button : abilities) { //Somehow buttons get disabled so we reenable them all and allow the later check to calculate AP
            button.active = true;
        }
		
		playerButton.active = !form.equals(DriveForm.NONE.toString()); //If form is empty we assume it's the player stats view
        for (MenuButton menuButton : driveSelector) {//Iterate through all the buttons to update their state
            menuButton.active = !form.equals(menuButton.getData()) && playerData.getDriveFormMap().containsKey(menuButton.getData()); //If the form stored in class is the same as the button name (handling prefix and such) and you have that form unlocked
            menuButton.setSelected(!menuButton.active); //Set it selected if it's not active (so it renders a bit to the right)
        }

	}

	int scrollTop, scrollBot;

	@Override
	public void init() {
		super.width = width;
		super.height = height;
		super.init();

		renderables.clear();
		children().clear();
		abilities.clear();

		float boxPosX = (float) width * 0.2F;
		float boxWidth = (float) width * 0.5F;
		box = new MenuBox((int) boxPosX, (int) topBarHeight, (int) boxWidth, (int) middleHeight, new Color(4, 4, 68));

		int buttonPosX = (int) (boxPosX * 1.3F);
		int buttonPosY = (int) topBarHeight + 5;
		int buttonWidth = (int) (boxWidth * 0.46F);

		scrollTop = (int) topBarHeight;
		scrollBot = (int) (scrollTop + middleHeight);

		abilitiesMap = Utils.getSortedAbilities(playerData.getAbilityMap());

		if (form.equals(DriveForm.NONE.toString())) {			
			int i = 0;
			for (i = 0; i < abilitiesMap.size(); i++) {
				String abilityName = (String) abilitiesMap.keySet().toArray()[i];
				Ability ability = ModAbilities.registry.get().getValue(new ResourceLocation(abilityName));

				int level = abilitiesMap.get(abilityName)[0];
				if (level == 0 || ability.getType() == AbilityType.GROWTH) {
					abilities.add(new MenuAbilitiesButton((int) buttonPosX, buttonPosY, (int) buttonWidth, abilityName, ability.getType(), (e) -> {
						action(ability, 0);
					}));
				} else {
					for (int j = 0; j < level; j++) {
						int finalJ = j;
						abilities.add(new MenuAbilitiesButton((int) buttonPosX, buttonPosY, (int) buttonWidth, abilityName, finalJ, ability.getType(), (e) -> {
							action(ability, finalJ);
						}));
					}
				}
				abilities.get(i).visible = false;
			}
			
			//Main keyblade
			if(playerData.getAlignment() == OrgMember.NONE) {
				if(!ItemStack.matches(playerData.getEquippedKeychain(DriveForm.NONE), ItemStack.EMPTY)){
					List<String> abilitiesList = Utils.getKeybladeAbilitiesAtLevel(playerData.getEquippedKeychain(DriveForm.NONE).getItem(), ((IKeychain) playerData.getEquippedKeychain(DriveForm.NONE).getItem()).toSummon().getKeybladeLevel(playerData.getEquippedKeychain(DriveForm.NONE)));
					for(String a : abilitiesList) {
						Ability ability = ModAbilities.registry.get().getValue(new ResourceLocation(a));
						if(ability != null) {
							MenuAbilitiesButton aa = new MenuAbilitiesButton((int) buttonPosX, buttonPosY, (int) buttonWidth, ability.getRegistryName().toString(), AbilityType.WEAPON, (e) -> { });
							abilities.add(aa);
							aa.visible = false;
						}
					}
				}
			} else {// If org member
				if (!ItemStack.matches(playerData.getEquippedWeapon(), ItemStack.EMPTY)) {
					List<String> abilitiesList;
					if (playerData.getAlignment() == OrgMember.ROXAS) {
						abilitiesList = Utils.getKeybladeAbilitiesAtLevel(playerData.getEquippedWeapon().getItem(), 0);
					} else { //any member but roxas or none
						abilitiesList = Utils.getOrgWeaponAbilities(playerData.getEquippedWeapon().getItem());
					}
				
					for (String a : abilitiesList) {
						Ability ability = ModAbilities.registry.get().getValue(new ResourceLocation(a));
						if (ability != null) { //Add weapon ability display
							MenuAbilitiesButton aa = new MenuAbilitiesButton((int) buttonPosX, buttonPosY, (int) buttonWidth, ability.getRegistryName().toString(), AbilityType.WEAPON, (e) -> {
							});
							abilities.add(aa);
							aa.visible = false;
							
							//If synch blade do it again
							if(playerData.getAbilityMap().containsKey(Strings.synchBlade) && playerData.getAbilityMap().get(Strings.synchBlade)[1] > 0) { //Org synch blade
								MenuAbilitiesButton aaa = new MenuAbilitiesButton((int) buttonPosX, buttonPosY, (int) buttonWidth, ability.getRegistryName().toString(), AbilityType.WEAPON, (e) -> {
								});
								abilities.add(aaa);
								aaa.visible = false;
							}
						}
					}
				}
			}
			
			//Synch blade Keyblade
			if (playerData.getActiveDriveForm().equals(DriveForm.NONE.toString())){
				if(playerData.getAbilityMap().containsKey(Strings.synchBlade) && playerData.getAbilityMap().get(Strings.synchBlade)[1] > 0 && !ItemStack.matches(playerData.getEquippedKeychain(DriveForm.SYNCH_BLADE), ItemStack.EMPTY)) {
					List<String> abilitiesList = Utils.getKeybladeAbilitiesAtLevel(playerData.getEquippedKeychain(DriveForm.SYNCH_BLADE).getItem(), ((IKeychain) playerData.getEquippedKeychain(DriveForm.SYNCH_BLADE).getItem()).toSummon().getKeybladeLevel(playerData.getEquippedKeychain(DriveForm.SYNCH_BLADE)));
					for (String a : abilitiesList) {
						Ability ability = ModAbilities.registry.get().getValue(new ResourceLocation(a));
						if (ability != null) {
							MenuAbilitiesButton aa = new MenuAbilitiesButton((int) buttonPosX, buttonPosY, (int) buttonWidth,  ability.getRegistryName().toString(), AbilityType.WEAPON, (e) -> {
							});
							abilities.add(aa);
							aa.visible = false;
						}
					}
				}
			} else { // Form keyblade abilities
				if (ModDriveForms.registry.get().containsKey(new ResourceLocation(playerData.getActiveDriveForm())) && ModDriveForms.registry.get().getValue(new ResourceLocation(playerData.getActiveDriveForm())).hasKeychain()) {
					//System.out.println(!ItemStack.matches(playerData.getEquippedKeychain(DriveForm.SYNCH_BLADE), ItemStack.EMPTY));
					if (playerData.getDriveFormMap().containsKey(playerData.getActiveDriveForm()) && playerData.getEquippedKeychains().containsKey(new ResourceLocation(playerData.getActiveDriveForm())) && !ItemStack.matches(playerData.getEquippedKeychain(new ResourceLocation(playerData.getActiveDriveForm())), ItemStack.EMPTY)) {
						ItemStack itemStack = playerData.getEquippedKeychain(new ResourceLocation(playerData.getActiveDriveForm()));
						List<String> abilitiesList = Utils.getKeybladeAbilitiesAtLevel(itemStack.getItem(), ((IKeychain) itemStack.getItem()).toSummon().getKeybladeLevel(itemStack));
						for (String a : abilitiesList) {
							Ability ability = ModAbilities.registry.get().getValue(new ResourceLocation(a));
							if (ability != null) {
								MenuAbilitiesButton aa = new MenuAbilitiesButton((int) buttonPosX, buttonPosY, (int) buttonWidth, ability.getRegistryName().toString(), AbilityType.WEAPON, (e) -> {
								});
								abilities.add(aa);
								aa.visible = false;
							}
						}
					}
				}
			}
			
			List<String> abilitiesList = Utils.getAccessoriesAbilities(playerData);
			for (String a : abilitiesList) {
				Ability ability = ModAbilities.registry.get().getValue(new ResourceLocation(a));
				if (ability != null) {
					MenuAbilitiesButton aa = new MenuAbilitiesButton((int) buttonPosX, buttonPosY, (int) buttonWidth, ability.getRegistryName().toString(), AbilityType.ACCESSORY, (e) -> {
					});
					abilities.add(aa);
					aa.visible = false;
				}
			}
			
		} else { //Drive form displays with disabled and equipped buttons
			//Display list of abilities in the drive form data
			DriveForm driveForm = ModDriveForms.registry.get().getValue(new ResourceLocation(form));
			//TODO                  AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
			if(driveForm.getBaseGrowthAbilities()) { //If the selected drive form inherits base form growth abilities
				for (int i = 0; i < abilitiesMap.size(); i++) {
					String abilityName = (String) abilitiesMap.keySet().toArray()[i];
					Ability ability = ModAbilities.registry.get().getValue(new ResourceLocation(abilityName));

					int level = abilitiesMap.get(abilityName)[0];
					if (level == 0 || ability.getType() == AbilityType.GROWTH) {
						MenuAbilitiesButton aa = new MenuAbilitiesButton((int) buttonPosX, buttonPosY, (int) buttonWidth, abilityName, ability.getType(), (e) -> {
						});
						//System.out.println(level);

						abilities.add(aa);
						aa.visible = false;
						aa.isVisual = true;

					}
				}
			} else { //If form doesn't inherit base form (common thing for kh2's)
				if(driveForm.getDriveFormData().getDFLevelUpAbilities() != null) {
					String growth = driveForm.getDFAbilityForLevel(1);
					Ability ab = ModAbilities.registry.get().getValue(new ResourceLocation(growth));
					if (ab != null) {
						MenuAbilitiesButton aa = new MenuAbilitiesButton((int) buttonPosX, buttonPosY, (int) buttonWidth, growth, ab.getType(), (e) -> {
						});
						abilities.add(aa);
						aa.visible = false;
						aa.isVisual = true;
					}
				}
			}
			
			if(driveForm.getDriveFormData().getAbilities() != null) {
				for (String a : driveForm.getDriveFormData().getAbilities()) {
					Ability ability = ModAbilities.registry.get().getValue(new ResourceLocation(a));
					if (ability != null) {
						MenuAbilitiesButton aa = new MenuAbilitiesButton((int) buttonPosX, buttonPosY, (int) buttonWidth, ability.getRegistryName().toString(), ability.getType(), (e) -> {
						});
						abilities.add(aa);
						aa.visible = false;
						aa.isVisual = true;
					}
				}
			}
			//Main keyblade
			if(!ItemStack.matches(playerData.getEquippedKeychain(DriveForm.NONE), ItemStack.EMPTY)){
				List<String> abilitiesList = Utils.getKeybladeAbilitiesAtLevel(playerData.getEquippedKeychain(DriveForm.NONE).getItem(), ((IKeychain) playerData.getEquippedKeychain(DriveForm.NONE).getItem()).toSummon().getKeybladeLevel(playerData.getEquippedKeychain(DriveForm.NONE)));
				for(String a : abilitiesList) {
					Ability ability = ModAbilities.registry.get().getValue(new ResourceLocation(a));
					if(ability != null) {
						MenuAbilitiesButton aa = new MenuAbilitiesButton((int) buttonPosX, buttonPosY, (int) buttonWidth, ability.getRegistryName().toString(), AbilityType.WEAPON, (e) -> { });
						abilities.add(aa);
						aa.visible = false;
					}
				}
			}
			// Selected Drive form 
			if (ModDriveForms.registry.get().containsKey(new ResourceLocation(form)) && ModDriveForms.registry.get().getValue(new ResourceLocation(form)).hasKeychain()) {
				if (playerData.getDriveFormMap().containsKey(form) && playerData.getEquippedKeychains().containsKey(new ResourceLocation(form))) {
					ItemStack itemStack = playerData.getEquippedKeychain(new ResourceLocation(form));
					if(!ItemStack.matches(itemStack, ItemStack.EMPTY)){
						List<String> abilitiesList = Utils.getKeybladeAbilitiesAtLevel(itemStack.getItem(), ((IKeychain) itemStack.getItem()).toSummon().getKeybladeLevel(itemStack));
						for (String a : abilitiesList) {
							Ability ability = ModAbilities.registry.get().getValue(new ResourceLocation(a));
							if (ability != null) {
								MenuAbilitiesButton aa = new MenuAbilitiesButton((int) buttonPosX, buttonPosY, (int) buttonWidth, ability.getRegistryName().toString(), AbilityType.WEAPON, (e) -> {
								});
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
		List<String> forms = new ArrayList<>(Utils.getSortedDriveForms(playerData.getDriveFormMap(), playerData.getVisibleDriveForms()).keySet());
		forms.remove(DriveForm.NONE.toString());
		forms.remove(DriveForm.SYNCH_BLADE.toString());
		forms.remove(Strings.Form_Anti);

		int k = 0;
		for (k = 0; k < forms.size(); k++) {
			String formName = forms.get(k);
			String name = ModDriveForms.registry.get().getValue(new ResourceLocation(formName)).getTranslationKey();
			MenuButton b = new MenuButton((int) this.buttonPosX + 10, this.buttonPosY + ((1+k) * 18), (int) this.buttonWidth-10, Utils.translateToLocal(name), ButtonType.SUBBUTTON, (e) -> {
				action(formName);
			});
			b.setData(formName);
			driveSelector.add(b);
			addRenderableWidget(b);
		}

        addRenderableWidget(back = new MenuButton((int)this.buttonPosX, this.buttonPosY + ((1+k) * 18), (int)this.buttonWidth, Component.translatable(Strings.Gui_Menu_Back).getString(), MenuButton.ButtonType.BUTTON, b -> action("back")));
		scrollBar = new MenuScrollBar((int) (boxPosX + boxWidth - 17), scrollTop, scrollBot, (int) middleHeight, 0);
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

		if(abilities.isEmpty())
			return;
		int listHeight = (abilities.get(abilities.size()-1).getY()+20) - abilities.get(0).getY() + 3;
		scrollBar.setContentHeight(listHeight);

		gui.enableScissor(0, (int) topBarHeight, width, (int) (topBarHeight + middleHeight));

		for (int i = 0; i < abilities.size(); i++) {
			if (abilities.get(i) != null) {
				abilities.get(i).setY((int) (abilities.get(i).getY() - scrollBar.scrollOffset));
				if (abilities.get(i).getY() < scrollBot && abilities.get(i).getY() >= scrollTop-20) {
					abilities.get(i).active =true;;
										String abilityName = abilities.get(i).getText();
					Ability ability = ModAbilities.registry.get().getValue(new ResourceLocation(abilityName));

					if (ability.getAPCost() > playerData.getMaxAP(true) - Utils.getConsumedAP(playerData)) {
						abilities.get(i).active = abilities.get(i).equipped;
					}
					
					if (abilities.get(i).abilityType == AbilityType.WEAPON || abilities.get(i).abilityType == AbilityType.ACCESSORY || form.equals(DriveForm.NONE.toString()) && playerData.isAbilityEquipped(abilities.get(i).getText(), abilitiesMap.get(abilities.get(i).getText())[0])) {
						abilities.get(i).active = true;
					}

					abilities.get(i).render(gui, mouseX, mouseY, partialTicks);
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
			String abilityName = abilities.get(i).getText();
			Ability ability = ModAbilities.registry.get().getValue(new ResourceLocation(abilityName));

			String lvl = "";
			if (ability.getType() == AbilityType.GROWTH) {
				DriveForm df = ModDriveForms.registry.get().getValue(new ResourceLocation(playerData.getActiveDriveForm()));
				int level = (form.equals(DriveForm.NONE.toString()) || df.getBaseGrowthAbilities() ? playerData.getEquippedAbilityLevel(abilityName)[0] : playerData.getEquippedAbilityLevel(abilityName)[0]+1);
				lvl += "_" + level;
			}
			abilityName = ability.getTranslationKey();
			String text = Utils.translateToLocal(new StringBuilder(abilityName).insert(abilityName.lastIndexOf('.'), lvl).toString());

			if (abilities.get(i) instanceof MenuAbilitiesButton) {
				MenuAbilitiesButton button = (MenuAbilitiesButton) abilities.get(i);

				/*if (ability.getAPCost() > playerData.getMaxAP(true) - consumedAP) {
					button.active = button.equipped;
				}
				
				if (button.abilityType == AbilityType.WEAPON || button.abilityType == AbilityType.ACCESSORY || form.equals(DriveForm.NONE.toString()) && playerData.isAbilityEquipped(abilities.get(i).getText(), abilitiesMap.get(abilities.get(i).getText())[0])) {
					button.active = true;
				}*/

				button.setMessage(Component.translatable(text));
				button.setAP(ability.getAPCost());
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

			// Left
			matrixStack.pushPose();
			{
				RenderSystem.setShaderColor(1, 1, 1, 1);
				gui.blit(texture, 0, 0, 143, 67, 7, 25);
			}
			matrixStack.popPose();

			// Middle
			matrixStack.pushPose();
			{
				RenderSystem.setShaderColor(1, 1, 1, 1);
				for (int j = 0; j < barWidth; j++)
					gui.blit(texture, 7 + j, 0, 151, 67, 1, 25);
			}
			matrixStack.popPose();
			// Right
			matrixStack.pushPose();
			{
				RenderSystem.setShaderColor(1, 1, 1, 1);
				gui.blit(texture, 7 + barWidth, 0, 153, 67, 7, 25);
			}
			matrixStack.popPose();

			// Bar Background
			matrixStack.pushPose();
			{
				RenderSystem.setShaderColor(1, 1, 1, 1);
				for (int j = 0; j < barWidth; j++)
					gui.blit(texture, j + 7, 17, 161, 67, 1, 25);
			}
			matrixStack.popPose();

			int requiredAP = (hoveredAbility != null) ? hoveredAbility.getAPCost() : 0;

			if(hoveredType != AbilityType.WEAPON && hoveredType != AbilityType.ACCESSORY) {
				if (hoveredAbility != null && playerData.isAbilityEquipped(hoveredAbility.getRegistryName().toString(), hoveredIndex)) { // If hovering an equipped ability
					requiredAP *= -1;
	
					// Bar going to decrease (dark yellow section when hovering equipped ability)
					matrixStack.pushPose();
					{
						int percent = (consumedAP) * barWidth / maxAP;
						matrixStack.pushPose();
						// RenderSystem.color(1, 1, 1,);
						for (int j = 0; j < percent; j++)
							gui.blit(texture, j + 7, 17, 165, 67, 1, 5);
						matrixStack.popPose();
	
					}
					matrixStack.popPose();
				} else {
					if(consumedAP + requiredAP <= playerData.getMaxAP(true)) {
						// Bar going to increase (blue section when hovering unequipped ability)
						matrixStack.pushPose();
						{
							int percent = (consumedAP + requiredAP) * barWidth / maxAP;
							matrixStack.pushPose();
							for (int j = 0; j < percent; j++)
								gui.blit(texture, j + 7, 17, 167, 67, 1, 5);
							matrixStack.popPose();
						}
						matrixStack.popPose();
					}
				}
			}
			RenderSystem.setShaderColor(1, 1, 1, 1F);

			// Foreground
			matrixStack.pushPose();
			{
				int percent = (consumedAP) * barWidth / maxAP;
				if (requiredAP < 0)
					percent = (consumedAP + requiredAP) * barWidth / maxAP;
				
				for (int j = 0; j < percent; j++)
					gui.blit(texture, j + 7, 17, 163, 67, 1, 5);
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

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
		scrollBar.mouseClicked(mouseX, mouseY, mouseButton);
		if (mouseButton == 1) {
			GuiHelper.openMenu();
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

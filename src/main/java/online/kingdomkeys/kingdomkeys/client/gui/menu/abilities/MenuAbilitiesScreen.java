package online.kingdomkeys.kingdomkeys.client.gui.menu.abilities;

import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TranslationTextComponent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.ability.Ability;
import online.kingdomkeys.kingdomkeys.ability.Ability.AbilityType;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.api.item.IKeychain;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.gui.GuiHelper;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBox;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuAbilitiesButton;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton.ButtonType;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSSetEquippedAbilityPacket;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class MenuAbilitiesScreen extends MenuBackground {
	String form = DriveForm.NONE.toString();

	IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);
	LinkedHashMap<String, int[]> abilitiesMap;
    List<MenuAbilitiesButton> abilities = new ArrayList<>();

	MenuBox box;
	Button prev, next;
	MenuButton back, playerButton;
	
	List<MenuButton> driveSelector = new ArrayList<>();

	int page = 0;
	int itemsPerPage;

	Ability hoveredAbility;
	int hoveredIndex;
	AbilityType hoveredType;
	
	public MenuAbilitiesScreen() {
		super(Strings.Gui_Menu_Main_Button_Abilities, new Color(0,0,255));
	}
	
	protected void action(String string) {
		switch (string) {
		case "prev":
			page--;
			minecraft.world.playSound(minecraft.player, minecraft.player.getPosition(), ModSounds.menu_in.get(), SoundCategory.MASTER, 1.0f, 1.0f);
			break;
		case "next":
			page++;
			minecraft.world.playSound(minecraft.player, minecraft.player.getPosition(), ModSounds.menu_in.get(), SoundCategory.MASTER, 1.0f, 1.0f);
			break;
		case "back":
			GuiHelper.openMenu();
			break;
		default:
			form = string;
			page = 0;
			init();
			break;
		}
		updateButtons();

	}
	
	private void action(Ability ability, int index) {
		String abilityName = ability.getRegistryName().toString();
		int apCost = ModAbilities.registry.getValue(new ResourceLocation(abilityName)).getAPCost();

		if (!playerData.isAbilityEquipped(abilityName, index)) {
			if (Utils.getConsumedAP(playerData) + apCost > playerData.getMaxAP(true)) {
				return;
			}
		}
		playerData.equipAbilityToggle(abilityName, index);
		PacketHandler.sendToServer(new CSSetEquippedAbilityPacket(abilityName, index));
		updateButtons();
	}

	private void updateButtons() {
		for(int i = 0; i < abilities.size(); i++) { //Somehow buttons get disabled so we reenable them all and allow the later check to calculate AP
			MenuAbilitiesButton button = abilities.get(i);
			button.active = true;
		}
		
		playerButton.active = !form.equals(DriveForm.NONE.toString()); //If form is empty we assume it's the player stats view
		for(int i = 0; i < driveSelector.size();i++) {//Iterate through all the buttons to update their state
			driveSelector.get(i).active = !form.equals(driveSelector.get(i).getData()) && playerData.getDriveFormMap().containsKey(driveSelector.get(i).getData()); //If the form stored in class is the same as the button name (handling prefix and such) and you have that form unlocked
			driveSelector.get(i).setSelected(!driveSelector.get(i).active); //Set it selected if it's not active (so it renders a bit to the right)
		}

	}

	@Override
	public void init() {
		super.width = width;
		super.height = height;
		super.init();

		buttons.clear();
		children.clear();
		abilities.clear();
		
		float boxPosX = (float) width * 0.2F;
		float topBarHeight = (float) height * 0.17F;
		float boxWidth = (float) width * 0.5F;
		float middleHeight = (float) height * 0.6F;
		box = new MenuBox((int) boxPosX, (int) topBarHeight, (int) boxWidth, (int) middleHeight, new Color(4, 4, 68));

		int buttonPosX = (int) (boxPosX * 1.4F);
		int buttonPosY = (int) topBarHeight + 5;
		int buttonWidth = (int) (boxWidth * 0.46F);
		
		abilitiesMap = Utils.getSortedAbilities(playerData.getAbilityMap());

		if (form.equals(DriveForm.NONE.toString())) {			
			int i = 0;
			for (i = 0; i < abilitiesMap.size(); i++) {
				String abilityName = (String) abilitiesMap.keySet().toArray()[i];
				Ability ability = ModAbilities.registry.getValue(new ResourceLocation(abilityName));

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
			if(!ItemStack.areItemStacksEqual(playerData.getEquippedKeychain(DriveForm.NONE), ItemStack.EMPTY)){
				List<String> abilitiesList = Utils.getKeybladeAbilitiesAtLevel(playerData.getEquippedKeychain(DriveForm.NONE).getItem(), ((IKeychain) playerData.getEquippedKeychain(DriveForm.NONE).getItem()).toSummon().getKeybladeLevel(playerData.getEquippedKeychain(DriveForm.NONE)));
				for(String a : abilitiesList) {
					Ability ability = ModAbilities.registry.getValue(new ResourceLocation(a));
					if(ability != null) {
						MenuAbilitiesButton aa = new MenuAbilitiesButton((int) buttonPosX, buttonPosY, (int) buttonWidth, ability.getRegistryName().toString(), AbilityType.WEAPON, (e) -> { });
						abilities.add(aa);
						aa.visible = false;
					}
				}
			}
			
			//Synch blade Keyblade
			if (playerData.getActiveDriveForm().equals(DriveForm.NONE.toString())){
				if(playerData.getAbilityMap().containsKey(Strings.synchBlade) && playerData.getAbilityMap().get(Strings.synchBlade)[1] > 0 && !ItemStack.areItemStacksEqual(playerData.getEquippedKeychain(DriveForm.SYNCH_BLADE), ItemStack.EMPTY)) {
					List<String> abilitiesList = Utils.getKeybladeAbilitiesAtLevel(playerData.getEquippedKeychain(DriveForm.SYNCH_BLADE).getItem(), ((IKeychain) playerData.getEquippedKeychain(DriveForm.SYNCH_BLADE).getItem()).toSummon().getKeybladeLevel(playerData.getEquippedKeychain(DriveForm.SYNCH_BLADE)));
					for (String a : abilitiesList) {
						Ability ability = ModAbilities.registry.getValue(new ResourceLocation(a));
						if (ability != null) {
							MenuAbilitiesButton aa = new MenuAbilitiesButton((int) buttonPosX, buttonPosY, (int) buttonWidth,  ability.getRegistryName().toString(), AbilityType.WEAPON, (e) -> {
							});
							abilities.add(aa);
							aa.visible = false;
						}
					}
				}
			} else { // Form keyblade abilities
				if (ModDriveForms.registry.containsKey(new ResourceLocation(playerData.getActiveDriveForm())) && ModDriveForms.registry.getValue(new ResourceLocation(playerData.getActiveDriveForm())).hasKeychain()) {
					if (playerData.getDriveFormMap().containsKey(playerData.getActiveDriveForm()) && playerData.getEquippedKeychains().containsKey(new ResourceLocation(playerData.getActiveDriveForm()))) {
						ItemStack itemStack = playerData.getEquippedKeychain(new ResourceLocation(playerData.getActiveDriveForm()));
						List<String> abilitiesList = Utils.getKeybladeAbilitiesAtLevel(itemStack.getItem(), ((IKeychain) itemStack.getItem()).toSummon().getKeybladeLevel(itemStack));
						for (String a : abilitiesList) {
							Ability ability = ModAbilities.registry.getValue(new ResourceLocation(a));
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
			
		} else { //Drive form displays with disabled and equipped buttons
			
			//Display base abilities not able to be modified
			/*int i = 0;
			for (i = 0; i < abilitiesMap.size(); i++) {
				String abilityName = (String) abilitiesMap.keySet().toArray()[i];
				Ability ability = ModAbilities.registry.getValue(new ResourceLocation(abilityName));
				/*if(ability.getType() == AbilityType.GROWTH) { //Add only the growth ability
					if(ability.getRegistryName().toString().equals(ModDriveForms.registry.getValue(new ResourceLocation(form)).getDFAbilityForLevel(1))) {
						abilities.add(new MenuAbilitiesButton((int) buttonPosX, buttonPosY, (int) buttonWidth, abilityName, ability.getType(), (e) -> { }));
						abilities.get(i).visible = false;
					}
				}
				if(ability.getType() == AbilityType.SUPPORT && !ability.getRegistryName().toString().equals(Strings.synchBlade)) { //Exclude base player's synch blade to prevent it being shown twice on forms
					int level = abilitiesMap.get(abilityName)[0];
					if (level == 0 || ability.getType() == AbilityType.GROWTH) {
						abilities.add(new MenuAbilitiesButton((int) buttonPosX, buttonPosY, (int) buttonWidth, abilityName, ability.getType(), (e) -> { }));
					} else {
						for (int j = 0; j < level; j++) {
							int finalJ = j;
							abilities.add(new MenuAbilitiesButton((int) buttonPosX, buttonPosY, (int) buttonWidth, abilityName, finalJ, ability.getType(), (e) -> { }));
						}
					}
					abilities.get(i).visible = false;
				}
			}*/
			
			//Display list of abilities in the drive form data
			DriveForm driveForm = ModDriveForms.registry.getValue(new ResourceLocation(form));			
			
			String growth = driveForm.getDFAbilityForLevel(1);
			Ability ab = ModAbilities.registry.getValue(new ResourceLocation(growth));
			if (ab != null) {
				/*int level = playerData.getEquippedAbilityLevel(growth)[0];
				String lvl = "_" + (level+1);
				
				String abName = ab.getTranslationKey();
				String text = new StringBuilder(abName).insert(abName.lastIndexOf('.'), lvl).toString();
				System.out.println(text);*/
				
				MenuAbilitiesButton aa = new MenuAbilitiesButton((int) buttonPosX, buttonPosY, (int) buttonWidth, growth, ab.getType(), (e) -> {
				});
				abilities.add(aa);
				aa.visible = false;
				aa.isVisual = true;
			}
			
			for (String a : driveForm.getDriveFormData().getAbilities()) {
				Ability ability = ModAbilities.registry.getValue(new ResourceLocation(a));
				if (ability != null) {
					MenuAbilitiesButton aa = new MenuAbilitiesButton((int) buttonPosX, buttonPosY, (int) buttonWidth, ability.getRegistryName().toString(), ability.getType(), (e) -> {
					});
					abilities.add(aa);
					aa.visible = false;
					aa.isVisual = true;
				}
			}
			
			//Main keyblade
			if(!ItemStack.areItemStacksEqual(playerData.getEquippedKeychain(DriveForm.NONE), ItemStack.EMPTY)){
				List<String> abilitiesList = Utils.getKeybladeAbilitiesAtLevel(playerData.getEquippedKeychain(DriveForm.NONE).getItem(), ((IKeychain) playerData.getEquippedKeychain(DriveForm.NONE).getItem()).toSummon().getKeybladeLevel(playerData.getEquippedKeychain(DriveForm.NONE)));
				for(String a : abilitiesList) {
					Ability ability = ModAbilities.registry.getValue(new ResourceLocation(a));
					if(ability != null) {
						MenuAbilitiesButton aa = new MenuAbilitiesButton((int) buttonPosX, buttonPosY, (int) buttonWidth, ability.getRegistryName().toString(), AbilityType.WEAPON, (e) -> { });
						abilities.add(aa);
						aa.visible = false;
					}
				}
			}
			// Selected Drive form 
			if (ModDriveForms.registry.containsKey(new ResourceLocation(form)) && ModDriveForms.registry.getValue(new ResourceLocation(form)).hasKeychain()) {
				if (playerData.getDriveFormMap().containsKey(form) && playerData.getEquippedKeychains().containsKey(new ResourceLocation(form))) {
					ItemStack itemStack = playerData.getEquippedKeychain(new ResourceLocation(form));
					if(!ItemStack.areItemStacksEqual(itemStack, ItemStack.EMPTY)){
						List<String> abilitiesList = Utils.getKeybladeAbilitiesAtLevel(itemStack.getItem(), ((IKeychain) itemStack.getItem()).toSummon().getKeybladeLevel(itemStack));
						for (String a : abilitiesList) {
							Ability ability = ModAbilities.registry.getValue(new ResourceLocation(a));
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
		
		abilities.forEach(this::addButton);
		itemsPerPage = (int) (middleHeight / 19);
		
		addButton(playerButton = new MenuButton((int)this.buttonPosX, this.buttonPosY, (int)this.buttonWidth, minecraft.player.getDisplayName().getString(), MenuButton.ButtonType.BUTTON, b -> {action(DriveForm.NONE.toString());}));
		List<String> forms = new ArrayList<>(Utils.getSortedDriveForms(playerData.getDriveFormMap()).keySet());
		forms.remove(DriveForm.NONE.toString());
		forms.remove(DriveForm.SYNCH_BLADE.toString());

		int k = 0;
		for (k = 0; k < forms.size(); k++) {
			String formName = forms.get(k);
			String name = ModDriveForms.registry.getValue(new ResourceLocation(formName)).getTranslationKey();
			MenuButton b = new MenuButton((int) this.buttonPosX + 10, this.buttonPosY + ((1+k) * 18), (int) this.buttonWidth-10, Utils.translateToLocal(name), ButtonType.SUBBUTTON, (e) -> {
				action(formName);
			});
			b.setData(formName);
			driveSelector.add(b);
			addButton(b);
		}

        addButton(back = new MenuButton((int)this.buttonPosX, this.buttonPosY + ((1+k) * 18), (int)this.buttonWidth, new TranslationTextComponent(Strings.Gui_Menu_Back).getString(), MenuButton.ButtonType.BUTTON, b -> action("back")));

		addButton(prev = new Button((int) buttonPosX + 10, (int)(height * 0.1F), 30, 20, new TranslationTextComponent(Utils.translateToLocal("<--")), (e) -> {
			action("prev");
		}));
		addButton(next = new Button((int) buttonPosX + 10 + 76, (int)(height * 0.1F), 30, 20, new TranslationTextComponent(Utils.translateToLocal("-->")), (e) -> { //MenuButton((int) buttonPosX, button_statsY + (0 * 18), (int) 100, Utils.translateToLocal(Strings.Gui_Synthesis_Materials_Deposit), ButtonType.BUTTON, (e) -> { //
			action("next");
		}));
		
		prev.visible = false;
		next.visible = false;
		
		updateButtons();
	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		box.draw(matrixStack);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		drawAP(matrixStack);
		
		prev.visible = page > 0;
		next.visible = page < abilities.size() / itemsPerPage;

		//Page renderer
		matrixStack.push();
		{
			matrixStack.translate(prev.x+ prev.getWidth() + 5, (height * 0.15) - 18, 1);
			drawString(matrixStack, minecraft.fontRenderer, Utils.translateToLocal("Page: " + (page + 1)), 0, 10, 0xFF9900);
		}
		matrixStack.pop();

		for (int i = 0; i < abilities.size(); i++) {
			abilities.get(i).visible = false;
		}

		for (int i = page * itemsPerPage; i < page * itemsPerPage + itemsPerPage; i++) {
			if (i < abilities.size()) {
				if (abilities.get(i) != null) {
					abilities.get(i).visible = true;
					abilities.get(i).y = (int) (topBarHeight) + (i % itemsPerPage) * 19 + 2; // 6 = offset
					abilities.get(i).render(matrixStack, mouseX, mouseY, partialTicks);
				}
			}
		}
		
		prev.render(matrixStack, mouseX,  mouseY,  partialTicks);
		next.render(matrixStack, mouseX,  mouseY,  partialTicks);
		playerButton.render(matrixStack, mouseX, mouseY, partialTicks);
		back.render(matrixStack, mouseX, mouseY, partialTicks);
		if(hoveredAbility != null) {
			renderSelectedData(mouseX, mouseY, partialTicks);
		}
	}

	protected void renderSelectedData(int mouseX, int mouseY, float partialTicks) {
		float tooltipPosX = width * 0.22F;
		float tooltipPosY = height * 0.77F;
		Utils.drawSplitString(font, new TranslationTextComponent(hoveredAbility.getTranslationKey().replace(".name", ".desc")).getString(), (int) tooltipPosX + 60, (int) tooltipPosY + 15, (int) (width * 0.6F), 0x00FFFF);
	}
	
	private void drawAP(MatrixStack matrixStack) {
		int consumedAP = Utils.getConsumedAP(playerData);
		int maxAP = playerData.getMaxAP(true);
		hoveredAbility = null;
		
		//Get all the abilities and set their text
		for (int i = 0; i < abilities.size(); i++) {
			String abilityName = abilities.get(i).getText();
			Ability ability = ModAbilities.registry.getValue(new ResourceLocation(abilityName));

			String lvl = "";
			if (ability.getType() == AbilityType.GROWTH) {
				int level = (form.equals(DriveForm.NONE.toString()) ? playerData.getEquippedAbilityLevel(abilityName)[0] : playerData.getEquippedAbilityLevel(abilityName)[0]+1);
				lvl += "_" + level;
			}
			abilityName = ability.getTranslationKey();
			String text = Utils.translateToLocal(new StringBuilder(abilityName).insert(abilityName.lastIndexOf('.'), lvl).toString());
			//System.out.println(buttons.get(i).getMessage().getString());
			if (buttons.get(i) instanceof MenuAbilitiesButton) {
				MenuAbilitiesButton button = (MenuAbilitiesButton) buttons.get(i);

				if (ability.getAPCost() > playerData.getMaxAP(true) - consumedAP) {
					button.active = button.equipped;
				}
				
				if (button.abilityType == AbilityType.WEAPON || form.equals(DriveForm.NONE.toString()) && playerData.isAbilityEquipped(abilities.get(i).getText(), abilitiesMap.get(abilities.get(i).getText())[0])) {
					button.active = true;
				}

				button.setMessage(new TranslationTextComponent(text));
				button.setAP(ability.getAPCost());
				if (button.isHovered()) {
					hoveredAbility = ability;
					hoveredIndex = button.index;
					hoveredType = button.abilityType;
				}
			}
		}

		int screenWidth = Minecraft.getInstance().getMainWindow().getScaledWidth();
		int screenHeight = Minecraft.getInstance().getMainWindow().getScaledHeight();

		int barWidth = (int)(width * 0.2F);
		int posX = screenWidth - barWidth;
		int posY = screenHeight - 100;
		float scale = 1F;
		
		minecraft.textureManager.bindTexture(new ResourceLocation(KingdomKeys.MODID, "textures/gui/menu/menu_button.png"));

		// Global
		matrixStack.push();
		{
			matrixStack.translate((posX - 2) * scale - 20, posY * scale - 10, 0);

			// Left
			matrixStack.push();
			{
				RenderSystem.color3f(1, 1, 1);
				blit(matrixStack, 0, 0, 143, 67, 7, 25);
			}
			matrixStack.pop();

			// Middle
			matrixStack.push();
			{
				RenderSystem.color3f(1, 1, 1);
				for (int j = 0; j < barWidth; j++)
					blit(matrixStack, 7 + j, 0, 151, 67, 1, 25);
			}
			matrixStack.pop();
			// Right
			matrixStack.push();
			{
				RenderSystem.color3f(1, 1, 1);
				blit(matrixStack, 7 + barWidth, 0, 153, 67, 7, 25);
			}
			matrixStack.pop();

			// Bar Background
			matrixStack.push();
			{
				RenderSystem.color3f(1, 1, 1);
				for (int j = 0; j < barWidth; j++)
					blit(matrixStack, j + 7, 17, 161, 67, 1, 25);
			}
			matrixStack.pop();

			int requiredAP = (hoveredAbility != null) ? hoveredAbility.getAPCost() : 0;

			if(hoveredType != AbilityType.WEAPON) {
				if (hoveredAbility != null && playerData.isAbilityEquipped(hoveredAbility.getRegistryName().toString(),hoveredIndex)) { // If hovering an equipped ability
					requiredAP *= -1;
	
					// Bar going to decrease (dark yellow section when hovering equipped ability)
					matrixStack.push();
					{
						int percent = (consumedAP) * barWidth / maxAP;
						matrixStack.push();
						// RenderSystem.color(1, 1, 1,);
						for (int j = 0; j < percent; j++)
							blit(matrixStack, j + 7, 17, 165, 67, 1, 5);
						matrixStack.pop();
	
					}
					matrixStack.pop();
				} else {
					if(consumedAP + requiredAP <= playerData.getMaxAP(true)) {
						// Bar going to increase (blue section when hovering unequipped ability)
						matrixStack.push();
						{
							int percent = (consumedAP + requiredAP) * barWidth / maxAP;
							matrixStack.push();
							for (int j = 0; j < percent; j++)
								blit(matrixStack, j + 7, 17, 167, 67, 1, 5);
							matrixStack.pop();
						}
						matrixStack.pop();
					}
				}
			}
			RenderSystem.color4f(1, 1, 1, 1F);

			// Foreground
			matrixStack.push();
			{
				int percent = (consumedAP) * barWidth / maxAP;
				if (requiredAP < 0)
					percent = (consumedAP + requiredAP) * barWidth / maxAP;
				
				for (int j = 0; j < percent; j++)
					blit(matrixStack, j + 7, 17, 163, 67, 1, 5);
			}
			matrixStack.pop();

			// AP Text
			matrixStack.push();
			{
				matrixStack.scale(scale * 1.3F, scale * 1.1F, 0);
				drawString(matrixStack, minecraft.fontRenderer, Utils.translateToLocal(Strings.Gui_Menu_Status_AP)+": " + consumedAP + "/" + maxAP, 16, 5, 0xFFFFFF);
			}
			matrixStack.pop();
			
		}
		matrixStack.pop();
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
		if (mouseButton == 1) {
			GuiHelper.openMenu();
		}
		return super.mouseClicked(mouseX, mouseY, mouseButton);
	}
}

package online.kingdomkeys.kingdomkeys.client.gui.menu.abilities;

import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.ability.Ability;
import online.kingdomkeys.kingdomkeys.ability.Ability.AbilityType;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.gui.GuiHelper;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBox;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuAbilitiesButton;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.client.gui.menu.MenuScreen;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.item.KeybladeItem;
import online.kingdomkeys.kingdomkeys.item.KeychainItem;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSSetEquippedAbilityPacket;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class MenuAbilitiesScreen extends MenuBackground {
	IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);
	LinkedHashMap<String, int[]> abilitiesMap;
    List<MenuAbilitiesButton> abilities = new ArrayList<>();

	MenuBox box;
	Button prev, next;
	MenuButton back;
	
	int page = 0;
	int itemsPerPage;

	Ability hoveredAbility;
	
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
		}

	}

	@Override
	public void init() {
		abilitiesMap = Utils.getSortedAbilities(playerData.getAbilityMap());
		super.width = width;
		super.height = height;
		super.init();

		buttons.clear();
		children.clear();
		abilities.clear();
		
		float boxPosX = (float) width * 0.2F;
		float topBarHeight = (float) height * 0.17F;
		float boxWidth = (float) width * 0.45F;
		float middleHeight = (float) height * 0.6F;
		box = new MenuBox((int) boxPosX, (int) topBarHeight, (int) boxWidth, (int) middleHeight, new Color(4, 4, 68));

		int buttonPosX = (int) boxPosX + 10;
		int buttonPosY = (int) topBarHeight + 5;
		int buttonWidth = 100;
		int i = 0;
		

		for (i = 0; i < abilitiesMap.size(); i++) {
			String abilityName = (String) abilitiesMap.keySet().toArray()[i];
			Ability ability = ModAbilities.registry.getValue(new ResourceLocation(abilityName));
			String path = new ResourceLocation(abilityName).getPath();
			abilities.add(new MenuAbilitiesButton((int) buttonPosX, buttonPosY, (int) buttonWidth, Utils.translateToLocal(path), ability.getType(), (e) -> {
				action(ability);
			}));
			
			abilities.get(i).visible = false;
		}
		
		abilities.forEach(this::addButton);

        addButton(back = new MenuButton((int)this.buttonPosX, this.buttonPosY, (int)this.buttonWidth, new TranslationTextComponent(Strings.Gui_Menu_Back).getFormattedText(), MenuButton.ButtonType.BUTTON, b -> GuiHelper.openMenu()));

		addButton(prev = new Button((int) buttonPosX + 10, (int)(height * 0.1F), 30, 20, Utils.translateToLocal("<--"), (e) -> {
			action("prev");
		}));
		addButton(next = new Button((int) buttonPosX + 10 + 76, (int)(height * 0.1F), 30, 20, Utils.translateToLocal("-->"), (e) -> { //MenuButton((int) buttonPosX, button_statsY + (0 * 18), (int) 100, Utils.translateToLocal(Strings.Gui_Synthesis_Materials_Deposit), ButtonType.BUTTON, (e) -> { //
			action("next");
		}));
		
		prev.visible = false;
		next.visible = false;
		
		
		itemsPerPage = (int) (middleHeight / 19);

	}

	private void action(Ability ability) {
		String abilityName = ability.getRegistryName().toString();
		int apCost = ModAbilities.registry.getValue(new ResourceLocation(abilityName)).getAPCost();
		int lvlIncrease = 0;
		if (playerData.isAbilityEquipped(abilityName)) { // If ability is equipped, unequip
			// MinecraftForge.EVENT_BUS.post(new AbilityEvent.Unequip(mc.player, ability));
			lvlIncrease = -1;
		} else { // If ability is unequipped, equip
			// MinecraftForge.EVENT_BUS.post(new AbilityEvent.Equip(mc.player, ability));
			if (Utils.getConsumedAP(playerData) + apCost > playerData.getMaxAP()) {
				System.out.println("Not enough AP");
				return;
			} else {
				lvlIncrease = 1;
			}
		}
		
		if(lvlIncrease != 0) {
			playerData.addEquippedAbilityLevel(abilityName, lvlIncrease);
			PacketHandler.sendToServer(new CSSetEquippedAbilityPacket(abilityName, lvlIncrease));
			init();
		}
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		box.draw();
		super.render(mouseX, mouseY, partialTicks);
		drawAP();
		
		prev.visible = page > 0;
		next.visible = page < abilities.size() / itemsPerPage;

		//Page renderer
		RenderSystem.pushMatrix();
		{
			RenderSystem.translated(prev.x+ prev.getWidth() + 5, (height * 0.15) - 18, 1);
			drawString(minecraft.fontRenderer, Utils.translateToLocal("Page: " + (page + 1)), 0, 10, 0xFF9900);
		}
		RenderSystem.popMatrix();

		for (int i = 0; i < abilities.size(); i++) {
			abilities.get(i).visible = false;
		}

		for (int i = page * itemsPerPage; i < page * itemsPerPage + itemsPerPage; i++) {
			if (i < abilities.size()) {
				if (abilities.get(i) != null) {
					abilities.get(i).visible = true;
					abilities.get(i).y = (int) (topBarHeight) + (i % itemsPerPage) * 19 + 2; // 6 = offset
					abilities.get(i).render(mouseX, mouseY, partialTicks);
				}
			}
		}
		
		prev.render(mouseX,  mouseY,  partialTicks);
		next.render(mouseX,  mouseY,  partialTicks);
		back.render(mouseX, mouseY, partialTicks);
		if(hoveredAbility != null) {
			renderSelectedData(mouseX, mouseY, partialTicks);
		}
	}

	protected void renderSelectedData(int mouseX, int mouseY, float partialTicks) {
		float tooltipPosX = width * 0.22F;
		float tooltipPosY = height * 0.77F;
		minecraft.fontRenderer.drawSplitString(new TranslationTextComponent(hoveredAbility.getTranslationKey().replace(".name", ".desc")).getFormattedText(), (int) tooltipPosX + 60, (int) tooltipPosY + 15, (int) (width * 0.6F), 0x00FFFF);
	}
	
	private void drawAP() {
		int consumedAP = Utils.getConsumedAP(playerData);
		int maxAP = playerData.getMaxAP();
		hoveredAbility = null;
		
		//Get all the abilities and set their text
		for (int i = 0; i < abilitiesMap.size(); i++) {
			String abilityName = (String) abilitiesMap.keySet().toArray()[i];
			Ability ability = ModAbilities.registry.getValue(new ResourceLocation(abilityName));

			abilities.get(i).equipped = playerData.isAbilityEquipped(abilityName);			

			String lvl = "";
			if(ability.getType() == AbilityType.GROWTH) {
				int level = (playerData.getEquippedAbilityLevel(abilityName)[0]);
     			lvl+= "_"+level;
			}
			abilityName = ability.getTranslationKey();
			String text = Utils.translateToLocal(new StringBuilder(abilityName).insert(abilityName.lastIndexOf('.'), lvl).toString());
			if(buttons.get(i) instanceof MenuAbilitiesButton) {
				MenuAbilitiesButton button = (MenuAbilitiesButton) buttons.get(i);
				
				if (ability.getAPCost() > playerData.getMaxAP() - consumedAP) {
					button.active = false;
				}
				
				if(playerData.isAbilityEquipped(ability.getRegistryName().toString())) {
					button.active = true;
				}
				
				button.setMessage(text);
				button.setAP(ability.getAPCost());
	
				if (button.isHovered()) {
					hoveredAbility = ability;
				}
			}
		}

		int screenWidth = Minecraft.getInstance().getMainWindow().getScaledWidth();
		int screenHeight = Minecraft.getInstance().getMainWindow().getScaledHeight();

		int barWidth = 100;
		int posX = screenWidth - barWidth;
		int posY = screenHeight - 100;
		float scale = 1F;
		
		minecraft.textureManager.bindTexture(new ResourceLocation(KingdomKeys.MODID, "textures/gui/menu/menu_button.png"));

		// Global
		RenderSystem.pushMatrix();
		{
			RenderSystem.translatef((posX - 2) * scale - 20, posY * scale - 10, 0);

			// Left
			RenderSystem.pushMatrix();
			{
				RenderSystem.color3f(1, 1, 1);
				blit(0, 0, 143, 67, 7, 25);
			}
			RenderSystem.popMatrix();

			// Middle
			RenderSystem.pushMatrix();
			{
				RenderSystem.color3f(1, 1, 1);
				for (int j = 0; j < barWidth; j++)
					blit(7 + j, 0, 151, 67, 1, 25);
			}
			RenderSystem.popMatrix();
			// Right
			RenderSystem.pushMatrix();
			{
				RenderSystem.color3f(1, 1, 1);
				blit(7 + barWidth, 0, 153, 67, 7, 25);
			}
			RenderSystem.popMatrix();

			// Bar Background
			RenderSystem.pushMatrix();
			{
				RenderSystem.color3f(1, 1, 1);
				for (int j = 0; j < barWidth; j++)
					blit(j + 7, 17, 161, 67, 1, 25);
			}
			RenderSystem.popMatrix();

			int requiredAP = (hoveredAbility != null) ? hoveredAbility.getAPCost() : 0;

			if (hoveredAbility != null && playerData.isAbilityEquipped(hoveredAbility.getRegistryName().toString())) { // If hovering an equipped ability
				requiredAP *= -1;

				// Bar going to decrease (dark yellow section when hovering equipped ability)
				RenderSystem.pushMatrix();
				{
					int percent = (consumedAP) * barWidth / maxAP;
					RenderSystem.pushMatrix();
					// RenderSystem.color(1, 1, 1,);
					for (int j = 0; j < percent; j++)
						blit(j + 7, 17, 165, 67, 1, 5);
					RenderSystem.popMatrix();

				}
				RenderSystem.popMatrix();
			} else {
				if(consumedAP + requiredAP <= playerData.getMaxAP()) {
					// Bar going to increase (blue section when hovering unequipped ability)
					RenderSystem.pushMatrix();
					{
						int percent = (consumedAP + requiredAP) * barWidth / maxAP;
						RenderSystem.pushMatrix();
						for (int j = 0; j < percent; j++)
							blit(j + 7, 17, 167, 67, 1, 5);
						RenderSystem.popMatrix();
					}
					RenderSystem.popMatrix();
				}
			}
			RenderSystem.color4f(1, 1, 1, 1F);

			// Foreground
			RenderSystem.pushMatrix();
			{
				int percent = (consumedAP) * barWidth / maxAP;
				if (requiredAP < 0)
					percent = (consumedAP + requiredAP) * barWidth / maxAP;

				// System.out.println(ap);
				for (int j = 0; j < percent; j++)
					blit(j + 7, 17, 163, 67, 1, 5);
			}
			RenderSystem.popMatrix();

			// AP Text
			RenderSystem.pushMatrix();
			{
				RenderSystem.scalef(scale * 1.3F, scale * 1.1F, 0);
				drawString(minecraft.fontRenderer, Utils.translateToLocal(Strings.Gui_Menu_Status_AP)+": " + consumedAP + "/" + maxAP, 16, 5, 0xFFFFFF);
			}
			RenderSystem.popMatrix();
			
		}
		RenderSystem.popMatrix();
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
		if (mouseButton == 1) {
			GuiHelper.openMenu();
		}
		return super.mouseClicked(mouseX, mouseY, mouseButton);
	}
}

package online.kingdomkeys.kingdomkeys.client.gui.menu;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.ability.Ability;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.gui.GuiHelper;
import online.kingdomkeys.kingdomkeys.client.gui.menu.GuiMenuButton.ButtonType;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.packet.CSSetEquippedAbilityPacket;

public class GuiMenu_Abilities extends GuiMenu_Background {
	IPlayerCapabilities props = ModCapabilities.get(minecraft.player);
	GuiMenuAbilitiesButton[] abilities = new GuiMenuAbilitiesButton[props.getAbilitiesMap().size()];

	public GuiMenu_Abilities(String name) {
		super(name);
		minecraft = Minecraft.getInstance();
	}

	@Override
	public void init() {
		super.width = width;
		super.height = height;
		super.init();

		buttons.clear();

		int buttonPosX = 130;
		float topBarHeight = (float) height * 0.17F;
		int buttonPosY = (int) topBarHeight + 5;
		int buttonWidth = 100;
		int i = 0;
		for (i = 0; i < props.getAbilitiesMap().size(); i++) {
			String abilityName = (String) props.getAbilitiesMap().keySet().toArray()[i];
			Ability ability = ModAbilities.registry.getValue(new ResourceLocation(abilityName));

			addButton(abilities[i] = new GuiMenuAbilitiesButton((int) buttonPosX, buttonPosY + (i * 18), (int) buttonWidth, abilityName.substring(abilityName.indexOf(":") + 1), ability.getType(), (e) -> {
				action(ability);
			}));
		}
	}

	private void action(Ability ability) {
		String abilityName = ability.getRegistryName().toString();

		int apCost = ModAbilities.registry.getValue(new ResourceLocation(abilityName)).getAPCost();
		int lvlIncrease = 0;
		if (props.getEquippedAbilityLevel(abilityName)[1] > 0) { // If ability is > 0 = equipped, time to unequip
			// MinecraftForge.EVENT_BUS.post(new AbilityEvent.Unequip(mc.player, ability));
			lvlIncrease = -1;
		} else { // If ability is <= 0 equip
			// MinecraftForge.EVENT_BUS.post(new AbilityEvent.Equip(mc.player, ability));
			if (props.getConsumedAP() + apCost > props.getMaxAP()) {
				System.out.println("Not enough AP");
			} else {
				lvlIncrease = 1;
			}
		}
		if(lvlIncrease > 0)
			props.setConsumedAP(props.getConsumedAP() + apCost);
		else 
			props.setConsumedAP(props.getConsumedAP() - apCost);
		props.addEquippedAbilityLevel(abilityName, lvlIncrease);
		PacketHandler.sendToServer(new CSSetEquippedAbilityPacket(abilityName, lvlIncrease));
		init();
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		super.render(mouseX, mouseY, partialTicks);
		drawAP();
		// fill(125, ((-140 / 16) + 75) + 10, 200, ((-140 / 16) + 75) + 20, 0xFFFFFF);
	}

	/**
	 * 
	 */
	private void drawAP() {
		Ability hoveredAbility = null;
		int i = 0;
		//Get all the abilities and set their text
		for (i = 0; i < props.getAbilitiesMap().size(); i++) {
			String abilityName = (String) props.getAbilitiesMap().keySet().toArray()[i];
			Ability ability = ModAbilities.registry.getValue(new ResourceLocation(abilityName));

			String text = "";
			if (props.getEquippedAbilityLevel(abilityName)[1] > 0) {
				text = "O "; // Has to unequip
			} else {
				text = "  "; // Has to equip
			}
			text += abilityName.substring(abilityName.indexOf(":")+1);
			GuiMenuAbilitiesButton button = (GuiMenuAbilitiesButton) buttons.get(i);
			if (ability.getAPCost() > props.getMaxAP() - props.getConsumedAP() && !(props.getEquippedAbilityLevel(abilityName)[1] > 0)) {
				button.active = false;
			}
			button.setMessage(text);
			button.setAP(ability.getAPCost());

			if (button.isHovered())
				hoveredAbility = ability;
		}

		int screenWidth = Minecraft.getInstance().getMainWindow().getScaledWidth();
		int screenHeight = Minecraft.getInstance().getMainWindow().getScaledHeight();

		int barWidth = 100;
		int posX = screenWidth - barWidth;
		int posY = screenHeight - 100;
		float scale = 1F;

		int consumedAP = props.getConsumedAP();
		int maxAP = props.getMaxAP();
		
		minecraft.textureManager.bindTexture(new ResourceLocation(KingdomKeys.MODID, "textures/gui/menu/menu_button.png"));

		// Global
		GL11.glPushMatrix();
		{
			GL11.glTranslatef((posX - 2) * scale - 20, posY * scale - 10, 0);

			// Left
			GL11.glPushMatrix();
			{
				GL11.glColor3f(1, 1, 1);
				blit(0, 0, 143, 67, 7, 25);
			}
			GL11.glPopMatrix();

			// Middle
			GL11.glPushMatrix();
			{
				GL11.glColor3f(1, 1, 1);
				for (int j = 0; j < barWidth; j++)
					blit(7 + j, 0, 151, 67, 1, 25);
			}
			GL11.glPopMatrix();
			// Right
			GL11.glPushMatrix();
			{
				GL11.glColor3f(1, 1, 1);
				blit(7 + barWidth, 0, 153, 67, 7, 25);
			}
			GL11.glPopMatrix();

			// Bar Background
			GL11.glPushMatrix();
			{
				GL11.glColor3f(1, 1, 1);
				for (int j = 0; j < barWidth; j++)
					blit(j + 7, 17, 161, 67, 1, 25);
			}
			GL11.glPopMatrix();

			int requiredAP = (hoveredAbility != null) ? hoveredAbility.getAPCost() : 0;

			if (hoveredAbility != null && props.getEquippedAbilityLevel(hoveredAbility.getRegistryName().toString())[1] > 0) { // If hovering an equipped ability
				requiredAP *= -1;

				// Bar going to decrease (dark yellow section when hovering equipped ability)
				GL11.glPushMatrix();
				{
					int percent = (consumedAP) * barWidth / maxAP;
					GlStateManager.pushMatrix();
					// GlStateManager.color(1, 1, 1,);
					for (int j = 0; j < percent; j++)
						blit(j + 7, 17, 165, 67, 1, 5);
					GlStateManager.popMatrix();

				}
				GL11.glPopMatrix();
			} else {
				// Bar going to increase (blue section when hovering unequipped ability)
				GL11.glPushMatrix();
				{
					int percent = (consumedAP + requiredAP) * barWidth / maxAP;
					GlStateManager.pushMatrix();
					// GlStateManager.color(1, 1, 1,0.5F);
					for (int j = 0; j < percent; j++)
						blit(j + 7, 17, 167, 67, 1, 5);
					GlStateManager.popMatrix();
				}
				GL11.glPopMatrix();
			}
			GlStateManager.color4f(1, 1, 1, 1F);

			// Foreground
			GL11.glPushMatrix();
			{
				int percent = (consumedAP) * barWidth / maxAP;
				if (requiredAP < 0)
					percent = (consumedAP + requiredAP) * barWidth / maxAP;

				// System.out.println(ap);
				for (int j = 0; j < percent; j++)
					blit(j + 7, 17, 163, 67, 1, 5);
			}
			GL11.glPopMatrix();

			// AP Text
			GL11.glPushMatrix();
			{
				GL11.glScalef(scale * 1.3F, scale, 0);
				drawString(minecraft.fontRenderer, "AP: " + consumedAP + "/" + maxAP, 16, 5, 0xFFFFFF);
			}
			GL11.glPopMatrix();
			
		}
		GL11.glPopMatrix();
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
		if (mouseButton == 1) {
			GuiHelper.openMenu();
		}
		return super.mouseClicked(mouseX, mouseY, mouseButton);
	}
}

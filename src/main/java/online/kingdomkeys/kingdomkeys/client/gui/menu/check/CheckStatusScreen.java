package online.kingdomkeys.kingdomkeys.client.gui.menu.check;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBox;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuColourBox;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton.ButtonType;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.util.Utils;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CheckStatusScreen extends MenuBackground {
	ResourceLocation form = DriveForm.NONE;

	Button stats_player, stats_ability, stats_equipment;
	List<MenuButton> dfStats = new ArrayList<>();

	MenuColourBox path, level, totalExp, nextLevel, hp, mp, ap, driveGauge, str, mag, def, fRes, bRes, tRes, aRes, lRes, wRes, dRes, dfLevel, dfExp, dfNextLevel, dfFormGauge;

	MenuBox box;

	public CheckStatusScreen(PlayerData playerData, Player player) {
		super(Strings.Gui_Menu_Status, new Color(0,0,255));
		drawPlayerInfo = false;
		setPlayerData(player, playerData);
	}

	protected void action(String string) {
		if (string.equals("equipment"))
			Minecraft.getInstance().setScreen(new CheckEquipmentScreen(playerData, player));
		else if (string.equals("abilities"))
			Minecraft.getInstance().setScreen(new CheckAbilitiesScreen(playerData, player));
		else
			form = KingdomKeys.rl(string);

		updateButtons();
	}

	private void updateButtons() {
		stats_player.active = !form.equals(DriveForm.NONE); //If form is empty we assume it's the player stats view
		for (MenuButton dfStat : dfStats) {//Iterate through all the buttons to update their state
			dfStat.active = !form.equals(KingdomKeys.rl(dfStat.getData())) && playerData.getDriveFormMap().containsKey(KingdomKeys.rl(dfStat.getData())); //If the form stored in class is the same as the button name (handling prefix and such) and you have that form unlocked
			dfStat.setSelected(!dfStat.active); //Set it selected if it's not active (so it renders a bit to the right)
		}

		//Select the widgets to show depending on the selected button
		boolean base = form.equals(DriveForm.NONE);
		dfLevel.visible = dfExp.visible = dfNextLevel.visible = dfFormGauge.visible = !base;
		level.visible = totalExp.visible = nextLevel.visible = hp.visible = mp.visible = ap.visible = driveGauge.visible = base;

		if(!base) {
			int remainingExp = playerData.getDriveFormLevel(form) == ModDriveForms.registry.get(form).getMaxLevel() ? 0 : ModDriveForms.registry.get(form).getLevelUpCost(playerData.getDriveFormLevel(form) + 1) - playerData.getDriveFormExp(form);
			dfLevel.setValue("" + playerData.getDriveFormLevel(form));
			dfExp.setValue("" + playerData.getDriveFormExp(form));
			dfNextLevel.setValue("" + remainingExp);
			dfFormGauge.setValue("" + (2 + playerData.getDriveFormLevel(form)));
		}
	}

	@Override
	public void init() {
		super.init();
		this.renderables.clear();

		int button_statsY = (int) topBarHeight + 5;
		int button_stats_formsY = button_statsY + 18;

		float buttonPosX = (float) width * 0.03F;
		float subButtonPosX = buttonPosX + 10;

		float buttonWidth = ((float) width * 0.1744F)- 20;
		float subButtonWidth = buttonWidth - 10;

		float dataWidth = ((float) width * 0.1744F)-10;

		box = new MenuBox((int) (subButtonPosX + buttonWidth + 40)-10, (int) topBarHeight, (int) dataWidth*4+10, (int) middleHeight,1.0F,new Color(255,255,100));
		int col1X = box.getX() + 10;
		int col2X = box.getX() + box.getWidth()/2 + 5;

		addRenderableWidget(stats_player = new MenuButton((int) buttonPosX, button_statsY, (int) buttonWidth, this.player.getDisplayName().getString(), ButtonType.BUTTON, (e) -> { action(DriveForm.NONE.toString()); }));

		int i;

		List<ResourceLocation> forms = new ArrayList<>(Utils.getSortedDriveForms(playerData.getDriveFormMap(), Utils.getVisibleDriveForms(this.player)).keySet());
		forms.remove(DriveForm.NONE);
		forms.remove(DriveForm.SYNCH_BLADE);
		forms.remove(ModDriveForms.ANTI.location());

		for (i = 0; i < forms.size(); i++) {
			ResourceLocation formName = forms.get(i);
			String name = ModDriveForms.registry.get(formName).getTranslationKey();
			MenuButton b = new MenuButton((int) subButtonPosX, button_stats_formsY + (i * 18), (int) subButtonWidth, Utils.translateToLocal(name), ButtonType.SUBBUTTON, (e) -> {
				action(formName.toString());
			});
			b.setData(formName.toString());
			dfStats.add(b);
			addRenderableWidget(b);
		}
		addRenderableWidget(stats_equipment = new MenuButton((int) buttonPosX, button_stats_formsY + (i++ * 18), (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Menu_Items_Equipment), ButtonType.BUTTON, (e) -> { action("equipment"); }));
		addRenderableWidget(stats_ability = new MenuButton((int) buttonPosX, button_stats_formsY + (i * 18), (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Menu_Status_Abilities), ButtonType.BUTTON, (e) -> { action("abilities"); }));

		//Stats
		int c = 0;
		int spacer = 14;
		addRenderableWidget(path = new MenuColourBox(col1X,  button_statsY + (c++* spacer), box.getWidth() - 20, Utils.translateToLocal(Strings.Gui_Menu_Status_Choice),playerData.getChosen().toString(), 0x880088));

		addRenderableWidget(level = new MenuColourBox(col1X, button_statsY + (c++* spacer), box.getWidth()/2 - 10, Utils.translateToLocal(Strings.Gui_Menu_Status_Level),"" + playerData.getLevel(), 0x000088));
		addRenderableWidget(totalExp = new MenuColourBox(col1X,  button_statsY + (c++* spacer), box.getWidth()/2 - 10, Utils.translateToLocal(Strings.Gui_Menu_Status_TotalExp),"" + playerData.getExperience(), 0x000088));
		addRenderableWidget(nextLevel = new MenuColourBox(col1X,  button_statsY + (c++* spacer), box.getWidth()/2 - 10, Utils.translateToLocal(Strings.Gui_Menu_Status_NextLevel),"" + playerData.getExpNeeded(playerData.getLevel(), playerData.getExperience()), 0x000088));

		addRenderableWidget(hp = new MenuColourBox(col1X,  button_statsY + (c++* spacer), box.getWidth()/2 - 10, Utils.translateToLocal(Strings.Gui_Menu_Status_HP),"" + (int) this.player.getMaxHealth(), 0x008800));
		addRenderableWidget(mp = new MenuColourBox(col1X,  button_statsY + (c++* spacer), box.getWidth()/2 - 10, Utils.translateToLocal(Strings.Gui_Menu_Status_MP),"" + (int) playerData.getMaxMP(), 0x008800));
		addRenderableWidget(ap = new MenuColourBox(col1X,  button_statsY + (c++* spacer), box.getWidth()/2 - 10, Utils.translateToLocal(Strings.Gui_Menu_Status_AP), Utils.getConsumedAP(playerData)+"/"+playerData.getMaxAP(true), 0x008800));
		addRenderableWidget(driveGauge = new MenuColourBox(col1X,  button_statsY + (c++* spacer), box.getWidth()/2 - 10, Utils.translateToLocal(Strings.Gui_Menu_Status_DriveGauge),"" + (int) playerData.getMaxDP()/100, 0x008800));

		c=1;
		addRenderableWidget(str = new MenuColourBox(col2X,  button_statsY + (c++* spacer), box.getWidth()/2 - 15, Utils.translateToLocal(Strings.Gui_Menu_Status_Strength),"" + playerData.getStrength(true), 0x880000));
		addRenderableWidget(mag = new MenuColourBox(col2X,  button_statsY + (c++* spacer), box.getWidth()/2 - 15, Utils.translateToLocal(Strings.Gui_Menu_Status_Magic),"" + playerData.getMagic(true), 0x880000));
		addRenderableWidget(def = new MenuColourBox(col2X,  button_statsY + (c++* spacer), box.getWidth()/2 - 15, Utils.translateToLocal(Strings.Gui_Menu_Status_Defense),"" + playerData.getDefense(true), 0x880000));

		addRenderableWidget(fRes = new MenuColourBox(col2X,  button_statsY + (c++* spacer), box.getWidth()/2 - 15, Utils.translateToLocal(Strings.Gui_Menu_Status_FireRes),Utils.getArmorsStat(playerData, "fire")+"%", 0x887700));
		addRenderableWidget(bRes = new MenuColourBox(col2X,  button_statsY + (c++* spacer), box.getWidth()/2 - 15, Utils.translateToLocal(Strings.Gui_Menu_Status_BlizzardRes),Utils.getArmorsStat(playerData, "ice")+"%", 0x887700));
		addRenderableWidget(wRes = new MenuColourBox(col2X,  button_statsY + (c++* spacer), box.getWidth()/2 - 15, Utils.translateToLocal(Strings.Gui_Menu_Status_WaterRes),Utils.getArmorsStat(playerData, "water")+"%", 0x887700));
		addRenderableWidget(tRes = new MenuColourBox(col2X,  button_statsY + (c++* spacer), box.getWidth()/2 - 15, Utils.translateToLocal(Strings.Gui_Menu_Status_ThunderRes),Utils.getArmorsStat(playerData, "lightning")+"%", 0x887700));
		addRenderableWidget(aRes = new MenuColourBox(col2X,  button_statsY + (c++* spacer), box.getWidth()/2 - 15, Utils.translateToLocal(Strings.Gui_Menu_Status_AirRes),Utils.getArmorsStat(playerData, "air")+"%", 0x887700));
		addRenderableWidget(lRes = new MenuColourBox(col2X,  button_statsY + (c++* spacer), box.getWidth()/2 - 15, Utils.translateToLocal(Strings.Gui_Menu_Status_LightRes),Utils.getArmorsStat(playerData, "light")+"%", 0x887700));
		addRenderableWidget(dRes = new MenuColourBox(col2X,  button_statsY + (c++* spacer), box.getWidth()/2 - 15, Utils.translateToLocal(Strings.Gui_Menu_Status_DarkRes),Utils.getArmorsStat(playerData, "darkness")+"%", 0x887700));

		//Drive Form specific data elements
		c=1;
		// Value not set here as this is generic for every form
		addRenderableWidget(dfLevel = new MenuColourBox(col1X,  button_statsY + (c++* spacer), box.getWidth()/2 - 10,Utils.translateToLocal(Strings.Gui_Menu_Status_FormLevel),"", 0x000088));
		addRenderableWidget(dfExp = new MenuColourBox(col1X,  button_statsY + (c++* spacer), box.getWidth()/2 - 10, Utils.translateToLocal(Strings.Gui_Menu_Status_TotalExp), "", 0x000088));
		addRenderableWidget(dfNextLevel = new MenuColourBox(col1X,  button_statsY + (c++* spacer), box.getWidth()/2 - 10, Utils.translateToLocal(Strings.Gui_Menu_Status_NextLevel), "", 0x000088));
		addRenderableWidget(dfFormGauge = new MenuColourBox(col1X,  button_statsY + (c++* spacer), box.getWidth()/2 - 10, Utils.translateToLocal(Strings.Gui_Menu_Status_FormGauge), "", 0x008800));

		updateButtons();
	}

	@Override
	public void render(@NotNull GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
		box.render(gui, mouseX, mouseY, partialTicks);
		super.render(gui, mouseX, mouseY, partialTicks);
		gui.pose().pushPose();
		{
			float scale = 1.5F;
			gui.pose().scale(scale, scale, 1);
			gui.drawString(minecraft.font, Component.literal("["+playerData.getUnion().getDescriptionKey()+"]").withStyle(ClientUtils.KK_Font_EXP), (int) (topLeftBar.getWidth() / scale + topGap) + 5, 10, 0xFF9900);
		}
		gui.pose().popPose();
	}

}

package online.kingdomkeys.kingdomkeys.client.gui.menu.status;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.gui.GuiHelper;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuColourBox;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton.ButtonType;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class MenuStatusScreen extends MenuBackground {

	String form = DriveForm.NONE.toString();
	
	final IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);

	Button stats_player, stats_back;
	List<MenuButton> dfStats = new ArrayList<>();

	MenuColourBox level, totalExp, nextLevel, hp, mp, ap, driveGauge, str, mag, def, fRes, bRes, tRes, dRes, dfLevel, dfExp, dfNextLevel, dfFormGauge;

	MenuColourBox[] playerWidgets = { level, totalExp, nextLevel, hp, mp, ap, driveGauge, str, mag, def, fRes, bRes, tRes, dRes };

	MenuColourBox[] dfWidgets = { dfLevel, dfExp, dfNextLevel, dfFormGauge };
	
	public MenuStatusScreen() {
		super(Strings.Gui_Menu_Status, new Color(0,0,255));
		drawPlayerInfo = false;
	}

	protected void action(String string) {
		if (string.equals("back"))
			GuiHelper.openMenu();
		else
			form = string;

		updateButtons();
	}

	private void updateButtons() {
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);
		//String rlForm = KingdomKeys.MODID+":"+form; //form has no prefix (kingdomkeys:)

		stats_player.active = !form.equals(DriveForm.NONE.toString()); //If form is empty we assume it's the player stats view
		for(int i = 0; i < dfStats.size();i++) {//Iterate through all the buttons to update their state
			dfStats.get(i).active = !form.equals(dfStats.get(i).getData()) && playerData.getDriveFormMap().containsKey(dfStats.get(i).getData()); //If the form stored in class is the same as the button name (handling prefix and such) and you have that form unlocked
			dfStats.get(i).setSelected(!dfStats.get(i).active); //Set it selected if it's not active (so it renders a bit to the right)
		}
		
		//Select the widgets to show depending on the selected button
		if (form.equals(DriveForm.NONE.toString())) {
			form = DriveForm.NONE.toString();
			dfLevel.visible = false;
			dfExp.visible = false;
			dfNextLevel.visible = false;
			dfFormGauge.visible = false;
			
			level.visible = true;
			totalExp.visible = true;
			nextLevel.visible = true;
			hp.visible = true;
			mp.visible = true;
			ap.visible = true;
			driveGauge.visible = true;
			/*str.visible = true;
			mag.visible = true;
			def.visible = true;
			fRes.visible = true;
			bRes.visible = true;
			tRes.visible = true;
			dRes.visible = true;	*/		
		} else {
			dfLevel.visible = true;
			dfExp.visible = true;
			dfNextLevel.visible = true;
			dfFormGauge.visible = true;
			
			level.visible = false;
			totalExp.visible = false;
			nextLevel.visible = false;
			hp.visible = false;
			mp.visible = false;
			ap.visible = false;
			driveGauge.visible = false;
			/*str.visible = false;
			mag.visible = false;
			def.visible = false;
			fRes.visible = false;
			bRes.visible = false;
			tRes.visible = false;
			dRes.visible = false;*/
		
			 
			int remainingExp = playerData.getDriveFormLevel(form) == ModDriveForms.registry.getValue(new ResourceLocation(form)).getMaxLevel() ? 0 : ModDriveForms.registry.getValue(new ResourceLocation(form)).getLevelUpCost(playerData.getDriveFormLevel(form)+1) - playerData.getDriveFormExp(form);
			dfLevel.setValue("" + playerData.getDriveFormLevel(form));
			dfExp.setValue(""+playerData.getDriveFormExp(form));
			dfNextLevel.setValue(""+remainingExp);
			dfFormGauge.setValue(""+(2 + playerData.getDriveFormLevel(form)));
		}
		
		//updateScreen();
	}

	@Override
	public void init() {
		super.init();
		this.buttons.clear();

		float topBarHeight = (float) height * 0.17F;
		int button_statsY = (int) topBarHeight + 5;
		int button_stats_playerY = button_statsY;
		int button_stats_formsY = button_stats_playerY + 18;

		float buttonPosX = (float) width * 0.03F;
		float subButtonPosX = buttonPosX + 10;

		float buttonWidth = ((float) width * 0.1744F)- 20;
		float subButtonWidth = buttonWidth - 10;


		float dataWidth = ((float) width * 0.1744F)-10;

		int col1X = (int) (subButtonPosX + buttonWidth + 40), col2X=(int) (col1X + dataWidth * 2)+10 ;

		addButton(stats_player = new MenuButton((int) buttonPosX, button_stats_playerY, (int) buttonWidth, minecraft.player.getDisplayName().getString(), ButtonType.BUTTON, (e) -> { action(DriveForm.NONE.toString()); }));

		int i = 0;
		for (i = 0; i < playerData.getDriveFormMap().size()-1; i++) {
			List<String> forms = new ArrayList<>(Utils.getSortedDriveForms(playerData.getDriveFormMap()).keySet());
			forms.remove(DriveForm.NONE.toString());
			String formName = forms.get(i);
			//if (!formName.equals(DriveForm.NONE.toString())) {
				String name = ModDriveForms.registry.getValue(new ResourceLocation(formName)).getTranslationKey();
				MenuButton b = new MenuButton((int) subButtonPosX, button_stats_formsY + (i * 18), (int) subButtonWidth, Utils.translateToLocal(name), ButtonType.SUBBUTTON, (e) -> {
					action(formName);
				});
				b.setData(formName);
				dfStats.add(b);
				addButton(b);
			//}
		}
		addButton(stats_back = new MenuButton((int) buttonPosX, button_stats_formsY + (i * 18), (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Menu_Back), ButtonType.BUTTON, (e) -> { action("back"); }));
		
		//Stats
		int c = 0;
		int spacer = 14;
		
		addButton(level = new MenuColourBox(col1X, button_statsY + (c++* spacer), (int) dataWidth*2, Utils.translateToLocal(Strings.Gui_Menu_Status_Level),"" + playerData.getLevel(), 0x000088));
		addButton(totalExp = new MenuColourBox(col1X,  button_statsY + (c++* spacer), (int) dataWidth*2, Utils.translateToLocal(Strings.Gui_Menu_Status_TotalExp),"" + playerData.getExperience(), 0x000088));
		addButton(nextLevel = new MenuColourBox(col1X,  button_statsY + (c++* spacer), (int) dataWidth*2, Utils.translateToLocal(Strings.Gui_Menu_Status_NextLevel),"" + playerData.getExpNeeded(playerData.getLevel(), playerData.getExperience()), 0x000088));
		
		addButton(hp = new MenuColourBox(col1X,  button_statsY + (c++* spacer), (int) dataWidth*2, Utils.translateToLocal(Strings.Gui_Menu_Status_HP),"" + (int) minecraft.player.getMaxHealth(), 0x008800));
		addButton(mp = new MenuColourBox(col1X,  button_statsY + (c++* spacer), (int) dataWidth*2, Utils.translateToLocal(Strings.Gui_Menu_Status_MP),"" + (int) playerData.getMaxMP(), 0x008800));
		addButton(ap = new MenuColourBox(col1X,  button_statsY + (c++* spacer), (int) dataWidth*2, Utils.translateToLocal(Strings.Gui_Menu_Status_AP),"" + Utils.getConsumedAP(playerData)+"/"+playerData.getMaxAP(), 0x008800));
		addButton(driveGauge = new MenuColourBox(col1X,  button_statsY + (c++* spacer), (int) dataWidth*2, Utils.translateToLocal(Strings.Gui_Menu_Status_DriveGauge),"" + (int) playerData.getMaxDP()/100, 0x008800));
		
		c=0;
		addButton(str = new MenuColourBox(col2X,  button_statsY + (c++* spacer), (int) dataWidth*2, Utils.translateToLocal(Strings.Gui_Menu_Status_Strength),"" + playerData.getStrength(), 0x880000));
		addButton(mag = new MenuColourBox(col2X,  button_statsY + (c++* spacer), (int) dataWidth*2, Utils.translateToLocal(Strings.Gui_Menu_Status_Magic),"" + playerData.getMagic(), 0x880000));
		addButton(def = new MenuColourBox(col2X,  button_statsY + (c++* spacer), (int) dataWidth*2, Utils.translateToLocal(Strings.Gui_Menu_Status_Defense),"" + playerData.getDefense(), 0x880000));
		
		addButton(fRes = new MenuColourBox(col2X,  button_statsY + (c++* spacer), (int) dataWidth*2, Utils.translateToLocal(Strings.Gui_Menu_Status_FireRes),"0%", 0x887700));
		addButton(bRes = new MenuColourBox(col2X,  button_statsY + (c++* spacer), (int) dataWidth*2, Utils.translateToLocal(Strings.Gui_Menu_Status_BlizzardRes),"0%", 0x887700));
		addButton(tRes = new MenuColourBox(col2X,  button_statsY + (c++* spacer), (int) dataWidth*2, Utils.translateToLocal(Strings.Gui_Menu_Status_ThunderRes),"0%", 0x887700));
		addButton(dRes = new MenuColourBox(col2X,  button_statsY + (c++* spacer), (int) dataWidth*2, Utils.translateToLocal(Strings.Gui_Menu_Status_DarkRes),"0%", 0x887700));
		
		//Drive Form specific data elements
		c=0; 
		// Value not set here as this is generic for every form
		addButton(dfLevel = new MenuColourBox(col1X,  button_statsY + (c++* spacer), (int) dataWidth*2,Utils.translateToLocal(Strings.Gui_Menu_Status_FormLevel),"", 0x000088));
		addButton(dfExp = new MenuColourBox(col1X,  button_statsY + (c++* spacer), (int) dataWidth*2, Utils.translateToLocal(Strings.Gui_Menu_Status_TotalExp), "", 0x000088));
		addButton(dfNextLevel = new MenuColourBox(col1X,  button_statsY + (c++* spacer), (int) dataWidth*2, Utils.translateToLocal(Strings.Gui_Menu_Status_NextLevel), "", 0x000088));
		addButton(dfFormGauge = new MenuColourBox(col1X,  button_statsY + (c++* spacer), (int) dataWidth*2, Utils.translateToLocal(Strings.Gui_Menu_Status_FormGauge), "", 0x008800));
		
		updateButtons();
	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		fill(matrixStack, 125, ((-140 / 16) + 75) + 10, 200, ((-140 / 16) + 75) + 20, 0xFFFFFF);
		
		super.render(matrixStack, mouseX, mouseY, partialTicks);
	
	}
	
}

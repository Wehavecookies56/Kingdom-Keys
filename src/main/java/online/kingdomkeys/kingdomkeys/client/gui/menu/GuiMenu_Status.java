package online.kingdomkeys.kingdomkeys.client.gui.menu;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.gui.GuiHelper;
import online.kingdomkeys.kingdomkeys.client.gui.menu.GuiMenuButton.ButtonType;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.lib.Utils;

public class GuiMenu_Status extends GuiMenu_Background {

	String form = "";
	
	final IPlayerCapabilities props = ModCapabilities.get(minecraft.player);

	Button stats_player, stats_back;
	GuiMenuButton[] dfStats = new GuiMenuButton[props.getDriveFormsMap().size()];

	GuiMenuColoredElement level, totalExp, nextLevel, hp, mp, ap, driveGauge, str, mag, def, fRes, bRes, tRes, dRes, dfLevel, dfExp, dfNextLevel, dfFormGauge;

	GuiMenuColoredElement[] playerWidgets = { level, totalExp, nextLevel, hp, mp, ap, driveGauge, str, mag, def, fRes, bRes, tRes, dRes };

	GuiMenuColoredElement[] dfWidgets = { dfLevel, dfExp, dfNextLevel, dfFormGauge };
	
	public GuiMenu_Status(String name) {
		super(name);
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
		IPlayerCapabilities props = ModCapabilities.get(minecraft.player);
		String rlForm = KingdomKeys.MODID+":"+form; //form has no prefix (kingdomkeys:)

		stats_player.active = !form.equals(""); //If form is empty we assume it's the player stats view
		for(int i = 0; i < dfStats.length;i++) {//Iterate through all the buttons to update their state
			dfStats[i].active = !rlForm.equals(KingdomKeys.MODID+":"+dfStats[i].getData()) && props.getDriveFormsMap().containsKey(KingdomKeys.MODID+":"+dfStats[i].getData()); //If the form stored in class is the same as the button name (handling prefix and such) and you have that form unlocked
			dfStats[i].setSelected(!dfStats[i].active); //Set it selected if it's not active (so it renders a bit to the right)
		}
		
		//Select the widgets to show depending on the selected button
		if (form.equals("")) {
			form = "";
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
			
			/*switch (selected) {
			case STATS_VALOR:
				form = Strings.Form_Valor;
				break;
			case STATS_WISDOM:
				form = Strings.Form_Wisdom;
				break;
			case STATS_LIMIT:
				form = Strings.Form_Limit;
				break;
			case STATS_MASTER:
				form = Strings.Form_Master;
				break;
			case STATS_FINAL:
				form = Strings.Form_Final;
				break;
			}*/
			 
			int remainingExp = props.getDriveFormLevel(rlForm) == ModDriveForms.registry.getValue(new ResourceLocation(rlForm)).getMaxLevel() ? 0 : ModDriveForms.registry.getValue(new ResourceLocation(rlForm)).getLevelUpCost(props.getDriveFormLevel(rlForm)+1) - props.getDriveFormExp(rlForm);
			dfLevel.setValue("" + props.getDriveFormLevel(rlForm));
			dfExp.setValue(""+props.getDriveFormExp(rlForm));
			dfNextLevel.setValue(""+remainingExp);
			dfFormGauge.setValue(""+(2 + props.getDriveFormLevel(rlForm)));
		}
		
		//updateScreen();
	}

	@Override
	public void init() {
		super.width = width;
		super.height = height;
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

		addButton(stats_player = new GuiMenuButton((int) buttonPosX, button_stats_playerY, (int) buttonWidth, minecraft.player.getDisplayName().getFormattedText(), ButtonType.BUTTON, (e) -> { action(""); }));

		int i = 0;
		for (i = 0; i < props.getDriveFormsMap().size(); i++) {
			String formName = (String) props.getDriveFormsMap().keySet().toArray()[i];
			String name = formName.substring(formName.indexOf(":")+1);
			addButton(dfStats[i] = new GuiMenuButton((int) subButtonPosX, button_stats_formsY + (i * 18), (int) subButtonWidth, Utils.translateToLocal(formName.substring(formName.indexOf(":")+1)), ButtonType.SUBBUTTON, (e) -> { action(name); }));
			dfStats[i].setData(name);
		}
		addButton(stats_back = new GuiMenuButton((int) buttonPosX, button_stats_formsY + (i * 18), (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Menu_Status_Button_Back), ButtonType.BUTTON, (e) -> { action("back"); }));
		
		//Stats
		int c = 0;
		int spacer = 14;
		
		addButton(level = new GuiMenuColoredElement(col1X, button_statsY + (c++* spacer), (int) dataWidth*2, Utils.translateToLocal(Strings.Gui_Menu_Status_Level),"" + props.getLevel(), 0x000088));
		addButton(totalExp = new GuiMenuColoredElement(col1X,  button_statsY + (c++* spacer), (int) dataWidth*2, Utils.translateToLocal(Strings.Gui_Menu_Status_TotalExp),"" + props.getExperience(), 0x000088));
		addButton(nextLevel = new GuiMenuColoredElement(col1X,  button_statsY + (c++* spacer), (int) dataWidth*2, Utils.translateToLocal(Strings.Gui_Menu_Status_NextLevel),"" + props.getExpNeeded(props.getLevel(), props.getExperience()), 0x000088));
		
		addButton(hp = new GuiMenuColoredElement(col1X,  button_statsY + (c++* spacer), (int) dataWidth*2, Utils.translateToLocal(Strings.Gui_Menu_Status_HP),"" + (int) minecraft.player.getMaxHealth(), 0x008800));
		addButton(mp = new GuiMenuColoredElement(col1X,  button_statsY + (c++* spacer), (int) dataWidth*2, Utils.translateToLocal(Strings.Gui_Menu_Status_MP),"" + (int) props.getMaxMP(), 0x008800));
		addButton(ap = new GuiMenuColoredElement(col1X,  button_statsY + (c++* spacer), (int) dataWidth*2, Utils.translateToLocal(Strings.Gui_Menu_Status_AP),"" + props.getConsumedAP()+"/"+props.getMaxAP(), 0x008800));
		addButton(driveGauge = new GuiMenuColoredElement(col1X,  button_statsY + (c++* spacer), (int) dataWidth*2, Utils.translateToLocal(Strings.Gui_Menu_Status_DriveGauge),"" + (int) props.getMaxDP()/100, 0x008800));
		
		c=0;
		addButton(str = new GuiMenuColoredElement(col2X,  button_statsY + (c++* spacer), (int) dataWidth*2, Utils.translateToLocal(Strings.Gui_Menu_Status_Strength),"" + props.getStrength(), 0x880000));
		addButton(mag = new GuiMenuColoredElement(col2X,  button_statsY + (c++* spacer), (int) dataWidth*2, Utils.translateToLocal(Strings.Gui_Menu_Status_Magic),"" + props.getMagic(), 0x880000));
		addButton(def = new GuiMenuColoredElement(col2X,  button_statsY + (c++* spacer), (int) dataWidth*2, Utils.translateToLocal(Strings.Gui_Menu_Status_Defense),"" + props.getDefense(), 0x880000));
		
		addButton(fRes = new GuiMenuColoredElement(col2X,  button_statsY + (c++* spacer), (int) dataWidth*2, Utils.translateToLocal(Strings.Gui_Menu_Status_FireRes),"0%", 0x887700));
		addButton(bRes = new GuiMenuColoredElement(col2X,  button_statsY + (c++* spacer), (int) dataWidth*2, Utils.translateToLocal(Strings.Gui_Menu_Status_BlizzardRes),"0%", 0x887700));
		addButton(tRes = new GuiMenuColoredElement(col2X,  button_statsY + (c++* spacer), (int) dataWidth*2, Utils.translateToLocal(Strings.Gui_Menu_Status_ThunderRes),"0%", 0x887700));
		addButton(dRes = new GuiMenuColoredElement(col2X,  button_statsY + (c++* spacer), (int) dataWidth*2, Utils.translateToLocal(Strings.Gui_Menu_Status_DarkRes),"0%", 0x887700));
		
		//Drive Form specific data elements
		c=0; 
		// Value not set here as this is generic for every form
		addButton(dfLevel = new GuiMenuColoredElement(col1X,  button_statsY + (c++* spacer), (int) dataWidth*2,Utils.translateToLocal(Strings.Gui_Menu_Status_FormLevel),"", 0x000088));
		addButton(dfExp = new GuiMenuColoredElement(col1X,  button_statsY + (c++* spacer), (int) dataWidth*2, Utils.translateToLocal(Strings.Gui_Menu_Status_TotalExp), "", 0x000088));
		addButton(dfNextLevel = new GuiMenuColoredElement(col1X,  button_statsY + (c++* spacer), (int) dataWidth*2, Utils.translateToLocal(Strings.Gui_Menu_Status_NextLevel), "", 0x000088));
		addButton(dfFormGauge = new GuiMenuColoredElement(col1X,  button_statsY + (c++* spacer), (int) dataWidth*2, Utils.translateToLocal(Strings.Gui_Menu_Status_FormGauge), "", 0x008800));
		
		updateButtons();
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		fill(125, ((-140 / 16) + 75) + 10, 200, ((-140 / 16) + 75) + 20, 0xFFFFFF);
		
		super.render(mouseX, mouseY, partialTicks);
	
	}
	
}

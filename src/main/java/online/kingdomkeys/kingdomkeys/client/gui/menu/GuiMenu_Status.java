package online.kingdomkeys.kingdomkeys.client.gui.menu;

import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.gui.GuiHelper;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
import online.kingdomkeys.kingdomkeys.lib.Strings;

public class GuiMenu_Status extends GuiMenu_Bars {

	final int STATS_PLAYER = 0, STATS_VALOR = 1, STATS_WISDOM = 2, STATS_LIMIT = 3, STATS_MASTER = 4, STATS_FINAL = 5, STATS_BACK = 6;

	int selected = 0;
	String form;

	Button stats_player, stats_valor, stats_wisdom, stats_limit, stats_master, stats_final, stats_back;

	GuiMenuColoredElement level, totalExp, nextLevel, hp, mp, ap, driveGauge, str, mag, def, fRes, bRes, tRes, dRes, dfLevel, dfExp, dfNextLevel, dfFormGauge;

	GuiMenuColoredElement[] playerWidgets = { level, totalExp, nextLevel, hp, mp, ap, driveGauge, str, mag, def, fRes, bRes, tRes, dRes };

	GuiMenuColoredElement[] dfWidgets = { dfLevel, dfExp, dfNextLevel, dfFormGauge };
	
	public GuiMenu_Status(String name) {
		super(name);
		drawPlayerInfo = false;
	}

	protected void action(int btnID) {
		if (btnID == STATS_BACK)
			GuiHelper.openMenu();
		else
			selected = btnID;

		updateButtons();
	}

	private void updateButtons() {
		IPlayerCapabilities props = ModCapabilities.get(minecraft.player);

		stats_player.active = selected != STATS_PLAYER;
		stats_valor.active = selected != STATS_VALOR && props.getDriveFormLevel(Strings.Form_Valor) > 0;
		stats_wisdom.active = selected != STATS_WISDOM && props.getDriveFormLevel(Strings.Form_Wisdom) > 0;
		stats_limit.active = selected != STATS_LIMIT && props.getDriveFormLevel(Strings.Form_Limit) > 0;
		stats_master.active = selected != STATS_MASTER && props.getDriveFormLevel(Strings.Form_Master) > 0;
		stats_final.active = selected != STATS_FINAL && props.getDriveFormLevel(Strings.Form_Final) > 0;
		
		//Select the widgets to show depending on the selected button
		if (selected == STATS_PLAYER) {
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
			
			switch (selected) {
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
			}
			 
			int remainingExp = props.getDriveFormLevel(form) == 7 ? 0 : ModDriveForms.registry.getValue(new ResourceLocation(form)).getLevelUpCost(props.getDriveFormLevel(form)+1) - props.getDriveFormExp(form);
			dfLevel.setValue("" + props.getDriveFormLevel(form));
			dfExp.setValue(""+props.getDriveFormExp(form));
			dfNextLevel.setValue(""+remainingExp);
			dfFormGauge.setValue(""+(2 + props.getDriveFormLevel(form)));
		}
		
		
		
		//updateScreen();
	}

	@Override
	public void init() {
		super.width = width;
		super.height = height;
		super.init();
		this.buttons.clear();
		final IPlayerCapabilities props = ModCapabilities.get(minecraft.player);
		selected = 0;

		float topBarHeight = (float) height * 0.17F;
		int button_statsY = (int) topBarHeight + 5;
		int button_stats_playerY = button_statsY;
		int button_stats_valorY = button_stats_playerY + 18;
		int button_stats_wisdomY = button_stats_valorY + 18;
		int button_stats_limitY = button_stats_wisdomY + 18;
		int button_stats_masterY = button_stats_limitY + 18;
		int button_stats_finalY = button_stats_masterY + 18;
		int button_stats_backY = button_stats_finalY + 18;

		float buttonPosX = (float) width * 0.03F;
		float subButtonPosX = buttonPosX + 10;

		float buttonWidth = ((float) width * 0.1744F)- 20;

		float dataWidth = ((float) width * 0.1744F)-10;

		int col1X = (int) (subButtonPosX + buttonWidth + 40), col2X=(int) (col1X + dataWidth * 2)+10 ;

		addButton(stats_player = new GuiMenuButton((int) buttonPosX, button_stats_playerY, (int) buttonWidth, minecraft.player.getDisplayName().getFormattedText(), (e) -> { action(STATS_PLAYER); }));
		addButton(stats_valor = new GuiMenuButton((int) subButtonPosX, button_stats_valorY, (int) buttonWidth, "Valor", (e) -> { action(STATS_VALOR); }));
		addButton(stats_wisdom = new GuiMenuButton((int) subButtonPosX, button_stats_wisdomY, (int) buttonWidth, "Wisdom", (e) -> { action(STATS_WISDOM); }));
		addButton(stats_limit = new GuiMenuButton((int) subButtonPosX, button_stats_limitY, (int) buttonWidth, "Limit", (e) -> { action(STATS_LIMIT); }));
		addButton(stats_master = new GuiMenuButton((int) subButtonPosX, button_stats_masterY, (int) buttonWidth, "Master", (e) -> { action(STATS_MASTER); }));
		addButton(stats_final = new GuiMenuButton((int) subButtonPosX, button_stats_finalY, (int) buttonWidth, "Final", (e) -> { action(STATS_FINAL); }));
		addButton(stats_back = new GuiMenuButton((int) buttonPosX, button_stats_backY, (int) buttonWidth, "Back", (e) -> { action(STATS_BACK); }));
		
		//Stats
		int c = 0;
		int spacer = 14;
		
		addButton(level = new GuiMenuColoredElement(col1X, button_statsY + (c++* spacer), (int) dataWidth*2, "Level","" + props.getLevel(), 0x000088));
		addButton(totalExp = new GuiMenuColoredElement(col1X,  button_statsY + (c++* spacer), (int) dataWidth*2, "Total Experience","" + props.getExperience(), 0x000088));
		addButton(nextLevel = new GuiMenuColoredElement(col1X,  button_statsY + (c++* spacer), (int) dataWidth*2, "Next Level","" + props.getExpNeeded(props.getLevel(), props.getExperience()), 0x000088));
		
		addButton(hp = new GuiMenuColoredElement(col1X,  button_statsY + (c++* spacer), (int) dataWidth*2, "HP","" + (int) minecraft.player.getMaxHealth(), 0x008800));
		addButton(mp = new GuiMenuColoredElement(col1X,  button_statsY + (c++* spacer), (int) dataWidth*2, "MP","" + (int) props.getMaxMP(), 0x008800));
		addButton(ap = new GuiMenuColoredElement(col1X,  button_statsY + (c++* spacer), (int) dataWidth*2, "AP","" + props.getConsumedAP()+"/"+props.getMaxAP(), 0x008800));
		addButton(driveGauge = new GuiMenuColoredElement(col1X,  button_statsY + (c++* spacer), (int) dataWidth*2, "Drive Gauge","" + (int) props.getMaxDP()/100, 0x008800));
		
		c=0;
		addButton(str = new GuiMenuColoredElement(col2X,  button_statsY + (c++* spacer), (int) dataWidth*2, "Strength","" + props.getStrength(), 0x880000));
		addButton(mag = new GuiMenuColoredElement(col2X,  button_statsY + (c++* spacer), (int) dataWidth*2, "Magic","" + props.getMagic(), 0x880000));
		addButton(def = new GuiMenuColoredElement(col2X,  button_statsY + (c++* spacer), (int) dataWidth*2, "Defense","" + props.getDefense(), 0x880000));
		
		addButton(fRes = new GuiMenuColoredElement(col2X,  button_statsY + (c++* spacer), (int) dataWidth*2, "Fire Resistance","0%", 0x887700));
		addButton(bRes = new GuiMenuColoredElement(col2X,  button_statsY + (c++* spacer), (int) dataWidth*2, "Blizzard Resistance","0%", 0x887700));
		addButton(tRes = new GuiMenuColoredElement(col2X,  button_statsY + (c++* spacer), (int) dataWidth*2, "Thunder Resistance","0%", 0x887700));
		addButton(dRes = new GuiMenuColoredElement(col2X,  button_statsY + (c++* spacer), (int) dataWidth*2, "Dark Resistance","0%", 0x887700));
		
		//Drive Form specific data elements
		c=0;
		addButton(dfLevel = new GuiMenuColoredElement(col1X,  button_statsY + (c++* spacer), (int) dataWidth*2,"Level","", 0x000088));
		addButton(dfExp = new GuiMenuColoredElement(col1X,  button_statsY + (c++* spacer), (int) dataWidth*2, "Experience", "", 0x000088));
		addButton(dfNextLevel = new GuiMenuColoredElement(col1X,  button_statsY + (c++* spacer), (int) dataWidth*2, "Next Level", "", 0x000088));
		addButton(dfFormGauge = new GuiMenuColoredElement(col1X,  button_statsY + (c++* spacer), (int) dataWidth*2, "Form Gauge", "", 0x008800));
		
		updateButtons();
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		float topBarHeight = (float) height * 0.17F;
		int button_statsY = (int) topBarHeight + 5;

		drawBars();
        drawMunnyTime();
        drawBiomeDim();
		super.render(mouseX, mouseY, partialTicks);
	
		//fill(125, ((-140 / 16) + 75) + 10, 200, ((-140 / 16) + 75) + 20, 0xFFFFFF);
	}

}

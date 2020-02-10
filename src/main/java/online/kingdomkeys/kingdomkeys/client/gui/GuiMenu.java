package online.kingdomkeys.kingdomkeys.client.gui;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.ILevelCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;


class GuiMenu extends Screen {

	private Minecraft mc;

	public GuiMenu() {
		super(new TranslationTextComponent(""));

		//background = new GuiMenu_Bars(Strings.Gui_Menu);
		mc = Minecraft.getInstance();
	}

	final int ITEMS = 0, ABILITIES = 1, CUSTOMIZE = 2, PARTY = 3, STATUS = 4, JOURNAL = 5, CONFIG = 6;
	final int SUBMENU_MAIN = 0, SUBMENU_ITEMS = 1;

	Button items, abilities, customize, party, status, journal, config;

	//GuiMenu_Bars background;

	static int munny;

	int submenuIndex = SUBMENU_MAIN;

	protected void action(int buttonID) {
		System.out.println("ASD");

		switch (buttonID) {
		case ITEMS:
			mc.displayGuiScreen(new GuiMenu_Items());
			break;
		/*
		 * case CUSTOMIZE: GuiHelper.openCustomize(); break; case STATUS:
		 * GuiHelper.openStatus(); break; case JOURNAL: GuiHelper.openReports(); break;
		 * case CONFIG: GuiHelper.openMenu_Config(); break; case ABILITIES:
		 * mc.displayGuiScreen(new GuiAbilities()); break;
		 */
		}
		updateButtons();
	}

	@Override
	public void init() {
		super.init();
		//background.width = width;
		//background.height = height;
		//background.init();
		float topBarHeight = (float) height * 0.17F;
		int button_itemsY = (int) topBarHeight + 5;
		int button_abilitiesY = button_itemsY + 19;
		int button_customizeY = button_abilitiesY + 19;
		int button_partyY = button_customizeY + 19;
		int button_statusY = button_partyY + 19;
		int button_journalY = button_statusY + 19;
		int button_configY = button_journalY + 19;
		float buttonPosX = (float) width * 0.1526F;
		float buttonWidth = ((float) width * 0.1744F) - 22;

		/*buttons.add(items = new GuiMenuButton(ITEMS, (int) buttonPosX, button_itemsY, (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Menu_Main_Button_Items)));
        buttons.add(abilities = new GuiMenuButton(ABILITIES, (int) buttonPosX, button_abilitiesY, (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Menu_Main_Button_Abilities)));
        buttons.add(customize = new GuiMenuButton(CUSTOMIZE, (int) buttonPosX, button_customizeY, (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Menu_Main_Button_Customize)));
        buttons.add(party = new GuiMenuButton(PARTY, (int) buttonPosX, button_partyY, (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Menu_Main_Button_Party)));
        buttons.add(status = new GuiMenuButton(STATUS, (int) buttonPosX, button_statusY, (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Menu_Main_Button_Status)));
        buttons.add(journal = new GuiMenuButton(JOURNAL, (int) buttonPosX, button_journalY, (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Menu_Main_Button_Journal)));
        buttons.add(config = new GuiMenuButton(CONFIG, (int) buttonPosX, button_configY, (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Menu_Main_Button_Config)));

		*/updateButtons();
	}

	private void updateButtons() {
		switch (submenuIndex) {
		case SUBMENU_MAIN:
			this.items.visible = true;
			abilities.visible = true;
			customize.visible = true;
			party.visible = true;
			status.visible = true;
			journal.visible = true;
			config.visible = true;
			party.active = false;
			journal.active = true;
			customize.active = false;
			break;
		case SUBMENU_ITEMS:
			items.visible = false;
			abilities.visible = false;
			customize.visible = false;
			party.visible = false;
			status.visible = false;
			journal.visible = false;
			config.visible = false;
			break;
		}
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		/*background.drawBars();
		background.drawMunnyTime();
		background.drawBiomeDim();*/
		drawPlayer();
		super.render(mouseX, mouseY, partialTicks);
	}

	public void drawPlayer() {
		float playerWidth = width * 0.1041F;
		float playerHeight = height * 0.4481F;
		float playerPosX = width * 0.5229F;
		float playerPosY = height * 0.7518F;
		GL11.glPushMatrix();
		{
			PlayerEntity player = mc.player;
			// player.getSwingProgress(1);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			//GuiInventory.drawEntityOnScreen((int) playerPosX, (int) playerPosY, (int) playerHeight / 2, 0, 0, player);
			// GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.75F);
		}
		GL11.glPopMatrix();
		GL11.glPushMatrix();
		
		GL11.glColor3f(1, 1, 1);
		GL11.glTranslatef(1, 1, 100);
		//GL11.glEnableAlphaTest();
	//	GL11.glEnableBlend();
		mc.getRenderManager().textureManager.bindTexture(new ResourceLocation(KingdomKeys.MODID, "textures/gui/menu/menu_button.png"));
		float infoBoxWidth = ((float) width * 0.1385F) - 14;
		float infoBoxPosX = (float) width * 0.4354F;
		float infoBoxPosY = (float) height * 0.624F;
		/*drawTexturedModalRect(infoBoxPosX, infoBoxPosY, 123, 67, 11, 22);
		for (int i = 0; i < infoBoxWidth; i++) {
			drawTexturedModalRect(infoBoxPosX + 11 + i, infoBoxPosY, 135, 67, 1, 22);
		}
		drawTexturedModalRect(infoBoxPosX + 11 + infoBoxWidth, infoBoxPosY, 137, 67, 3, 22);
		drawTexturedModalRect(infoBoxPosX, infoBoxPosY + 22, 123, 90, 3, 47);
		for (int i = 0; i < infoBoxWidth + 8; i++) {
			drawTexturedModalRect(infoBoxPosX + 3 + i, infoBoxPosY + 22, 127, 90, 1, 47);
		}
		drawTexturedModalRect(infoBoxPosX + 3 + infoBoxWidth + 8, infoBoxPosY + 22, 129, 90, 3, 47);*/
		
		GL11.glPopMatrix();
		GL11.glPushMatrix();
		{
			GL11.glTranslatef(1, 1, 100);
			ILevelCapabilities props = ModCapabilities.get(mc.player);
			if (props != null) {
				GL11.glPushMatrix();
				{
					GL11.glTranslatef((int) infoBoxPosX + 8, (int) infoBoxPosY + ((22 / 2) - (mc.fontRenderer.FONT_HEIGHT / 2)), 1);
					// GL11.glscale(0.75F, 0.75F, 1);
					drawString(mc.fontRenderer, mc.player.getDisplayName().toString(), 0, 0, 0xFFFFFF);
				}
				GL11.glPopMatrix();
				drawString(mc.fontRenderer, "LV: " + props.getLevel(), (int) infoBoxPosX + 4, (int) (infoBoxPosY + 26), 0xFFD900);
				drawString(mc.fontRenderer, "HP: " + (int) mc.player.getHealth() + "/" + (int) mc.player.getMaxHealth(), (int) infoBoxPosX + 4, (int) (infoBoxPosY + 26) + mc.fontRenderer.FONT_HEIGHT, 0x00FF00);
				drawString(mc.fontRenderer, "MP: " + (int) props.getMP() + "/" + (int) props.getMaxMP(), (int) infoBoxPosX + 4, (int) (infoBoxPosY + 26) + (mc.fontRenderer.FONT_HEIGHT * 2), 0x4444FF);
			}
		}
		GL11.glPopMatrix();
	}

}

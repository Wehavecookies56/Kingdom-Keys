package online.kingdomkeys.kingdomkeys.client.gui.menu.party;

import java.awt.Color;

import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.SoundCategory;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.IWorldCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton.ButtonType;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSPartyCreate;

public class GuiMenu_Party_Create extends MenuBackground {

	boolean priv = false;
	byte pSize = Party.PARTY_LIMIT;
	
	TextFieldWidget tfName;
	Button togglePriv, accept, size;
	MenuButton back;
		
	final IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);
	IWorldCapabilities worldData;
	
	Party party;
		
	public GuiMenu_Party_Create() {
		super(Strings.Gui_Menu_Party_Create, new Color(0,0,255));
		drawPlayerInfo = true;
		worldData = ModCapabilities.getWorld(minecraft.world);
	}

	protected void action(String string) {
		switch(string) {
		case "back":
			minecraft.world.playSound(minecraft.player, minecraft.player.getPosition(), ModSounds.menu_in.get(), SoundCategory.MASTER, 1.0f, 1.0f);
			minecraft.displayGuiScreen(new GuiMenu_Party_None());
			break;
		case "togglePriv":
			priv = !priv;
			break;
		case "accept":
			if(!tfName.getText().equals("")) { //Accept Party creation
				Party localParty = new Party(tfName.getText(), minecraft.player.getUniqueID(), minecraft.player.getName().getFormattedText(), priv, Byte.parseByte(size.getMessage()));
				PacketHandler.sendToServer(new CSPartyCreate(localParty));
				
				minecraft.world.playSound(minecraft.player, minecraft.player.getPosition(), ModSounds.menu_in.get(), SoundCategory.MASTER, 1.0f, 1.0f);
				minecraft.displayGuiScreen(new GuiMenu_Party_Leader());
			}
			break;
		case "size":
			if(pSize == Party.PARTY_LIMIT) {
				pSize = 2;
			} else {
				pSize++;
			}
			size.setMessage(pSize+"");
			break;
		}
		
		updateButtons();
	}

	private void updateButtons() {
		//IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);
		togglePriv.setMessage(priv ? Utils.translateToLocal(Strings.Gui_Menu_Party_Create_Accessibility_Private) : Utils.translateToLocal(Strings.Gui_Menu_Party_Create_Accessibility_Public));

		
		//TBName
		togglePriv.visible = true;
		accept.visible = true;
		tfName.visible = true;
		size.visible = true;
	}

	@Override
	public void init() {
		//TODO request packet to sync other players data
		super.init();
		this.buttons.clear();
		
		party = worldData.getPartyFromMember(minecraft.player.getUniqueID());
		
		float topBarHeight = (float) height * 0.17F;
		int button_statsY = (int) topBarHeight + 5;
		float buttonPosX = (float) width * 0.03F;
		float buttonWidth = ((float) width * 0.1744F) - 20;


		addButton(togglePriv = new Button((int) (width*0.25)-2, button_statsY + (3 * 18), 100, 20, "", (e) -> { action("togglePriv"); }));
		addButton(accept = new Button((int) (width*0.25)-2, button_statsY + (5 * 18), (int) 100, 20, Utils.translateToLocal(Strings.Gui_Menu_Accept), (e) -> { action("accept"); }));
		addButton(back = new MenuButton((int) buttonPosX, button_statsY + (0 * 18), (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Menu_Back), ButtonType.BUTTON, (e) -> { action("back"); }));
		addButton(size = new Button((int) (width * 0.25 - 2 + 100 + 4), button_statsY + (3 * 18), (int) 20, 20, Party.PARTY_LIMIT+"", (e) -> { action("size"); }));
		
		addButton(tfName = new TextFieldWidget(minecraft.fontRenderer, (int)(width*0.25), (int)(height*0.25), 100, 15, ""));
		
		updateButtons();
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		//System.out.println(phase);
		//fill(125, ((-140 / 16) + 75) + 10, 200, ((-140 / 16) + 75) + 20, 0xFFFFFF);
		super.render(mouseX, mouseY, partialTicks);
		worldData = ModCapabilities.getWorld(minecraft.world);
		party = worldData.getPartyFromMember(minecraft.player.getUniqueID());
		
		int buttonX = (int)(width*0.25);
		
		drawString(minecraft.fontRenderer, Utils.translateToLocal(Strings.Gui_Menu_Party_Create_Name), buttonX, (int)(height * 0.2), 0xFFFFFF);
		drawString(minecraft.fontRenderer, Utils.translateToLocal(Strings.Gui_Menu_Party_Create_Accessibility), buttonX, (int)(height * 0.35), 0xFFFFFF);
	}
	
}

package online.kingdomkeys.kingdomkeys.client.gui.menu.party;

import java.awt.Color;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.SoundCategory;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.IWorldCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.gui.menu.GuiMenuButton;
import online.kingdomkeys.kingdomkeys.client.gui.menu.GuiMenu_Background;
import online.kingdomkeys.kingdomkeys.client.gui.menu.GuiMenuButton.ButtonType;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.lib.Utils;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSPartySettings;

public class GuiMenu_Party_Settings extends GuiMenu_Background {

	boolean priv = false;
	byte pSize = Party.PARTY_LIMIT;
	
	Button togglePriv, accept, size;
	GuiMenuButton back;
		
	final IPlayerCapabilities props = ModCapabilities.get(minecraft.player);
	IWorldCapabilities worldData;
	
	Party party;
		
	public GuiMenu_Party_Settings(String name) {
		super(name, new Color(0,0,255));
		drawPlayerInfo = true;
		worldData = ModCapabilities.getWorld(minecraft.world);
	}

	protected void action(String string) {
		switch(string) {
		case "back":
			minecraft.world.playSound(minecraft.player, minecraft.player.getPosition(), ModSounds.menu_in.get(), SoundCategory.MASTER, 1.0f, 1.0f);
			minecraft.displayGuiScreen(new GuiMenu_Party_Leader());
			break;
		case "togglePriv":
			priv = !priv;
			break;
		case "accept":
			//Accept Party creation
			//Party localParty = worldData.getPartyFromName(name);
			party.setPriv(priv);
			party.setSize(pSize);
			PacketHandler.sendToServer(new CSPartySettings(party));
			
			minecraft.world.playSound(minecraft.player, minecraft.player.getPosition(), ModSounds.menu_in.get(), SoundCategory.MASTER, 1.0f, 1.0f);
			minecraft.displayGuiScreen(new GuiMenu_Party_Leader());
			
			break;
		case "size":
			if(pSize == Party.PARTY_LIMIT) {
				pSize = 1;
			} else {
				pSize++;
			}
			size.setMessage(pSize+"");
			break;
		}
		
		updateButtons();
	}

	private void updateButtons() {
		//IPlayerCapabilities props = ModCapabilities.get(minecraft.player);
		togglePriv.setMessage(priv ? "Private" : "Public");

		
		//TBName
		togglePriv.visible = true;
		accept.visible = true;
		size.visible = true;
	}

	@Override
	public void init() {
		//TODO request packet to sync other players data
		super.width = width;
		super.height = height;
		super.init();
		this.buttons.clear();
		
		party = worldData.getPartyFromMember(minecraft.player.getUniqueID());
		priv = party.getPriv();
		pSize = party.getSize();
		
		float topBarHeight = (float) height * 0.17F;
		int button_statsY = (int) topBarHeight + 5;
		float buttonPosX = (float) width * 0.03F;
		float buttonWidth = ((float) width * 0.1744F) - 20;


		addButton(togglePriv = new Button((int) (width*0.25)-2, button_statsY + (1 * 18), 100, 20, Utils.translateToLocal("Private/public"), (e) -> { action("togglePriv"); }));
		addButton(size = new Button((int) (width * 0.25 - 2 + 100 + 4), button_statsY + (1 * 18), (int) 20, 20, pSize+"", (e) -> { action("size"); }));
		addButton(accept = new Button((int) (width*0.25)-2, button_statsY + (3 * 18), (int) 130, 20, Utils.translateToLocal("Accept"), (e) -> { action("accept"); }));
		//addButton(cancel = new Button((int) buttonPosX, button_statsY + (6 * 18), (int) buttonWidth, 20, Utils.translateToLocal("Cancel"), (e) -> { action("cancel"); }));
		addButton(back = new GuiMenuButton((int) buttonPosX, button_statsY + (0 * 18), (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Menu_Status_Button_Back), ButtonType.BUTTON, (e) -> { action("back"); }));
		
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
		
		RenderSystem.pushMatrix();
		{
			RenderSystem.scaled(1.5,1.5, 1);
			drawString(minecraft.fontRenderer, "SETTINGS", 2, 10, 0xFF9900);
			drawString(minecraft.fontRenderer, "["+party.getMembers().size()+"/"+party.getSize()+"] "+party.getName(), (int) (topLeftBarWidth + topGap) + 5, 10, 0xFF9900);
		}
		RenderSystem.popMatrix();
		drawString(minecraft.fontRenderer, "Accessibility and limit", buttonX, (int)(height * 0.2), 0xFFFFFF);
	}
	
}

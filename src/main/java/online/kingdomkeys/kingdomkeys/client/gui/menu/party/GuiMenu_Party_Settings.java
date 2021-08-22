package online.kingdomkeys.kingdomkeys.client.gui.menu.party;

import java.awt.Color;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TranslationTextComponent;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.IWorldCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton.ButtonType;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSPartySettings;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class GuiMenu_Party_Settings extends MenuBackground {

	boolean priv = false, friendlyFire = false;
	byte pSize = Party.PARTY_LIMIT;
	
	Button togglePriv, toggleFF, accept, size;
	MenuButton back;
		
	final IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);
	IWorldCapabilities worldData;
	
	Party party;
		
	public GuiMenu_Party_Settings() {
		super(Strings.Gui_Menu_Party_Leader_Settings, new Color(0,0,255));
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
		case "ff":
			friendlyFire = !friendlyFire;
			break;
		case "accept":
			party.setPriv(priv);
			party.setSize(pSize);
			party.setFriendlyFire(friendlyFire);
			PacketHandler.sendToServer(new CSPartySettings(party));
			
			minecraft.world.playSound(minecraft.player, minecraft.player.getPosition(), ModSounds.menu_in.get(), SoundCategory.MASTER, 1.0f, 1.0f);
			minecraft.displayGuiScreen(new GuiMenu_Party_Leader());
			
			break;
		case "size":
			if(pSize == Party.PARTY_LIMIT) {
				pSize = 2;
			} else {
				pSize++;
			}
			size.setMessage(new TranslationTextComponent(pSize+""));
			break;
		}
		
		updateButtons();
	}

	private void updateButtons() {
		//IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);
		togglePriv.setMessage(priv ? new TranslationTextComponent(Utils.translateToLocal(Strings.Gui_Menu_Party_Create_Accessibility_Private)) : new TranslationTextComponent(Utils.translateToLocal(Strings.Gui_Menu_Party_Create_Accessibility_Public)));
		toggleFF.setMessage(new TranslationTextComponent(friendlyFire+""));// ? new TranslationTextComponent(Utils.translateToLocal("FF")) : new TranslationTextComponent(Utils.translateToLocal(Strings.Gui_Menu_Party_Create_Accessibility_Public)));

		
		//TBName
		togglePriv.visible = true;
		toggleFF.visible = true;
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
		if(party != null) {
			priv = party.getPriv();
			pSize = party.getSize();
			friendlyFire = party.getFriendlyFire();
			
			float topBarHeight = (float) height * 0.17F;
			int button_statsY = (int) topBarHeight + 5;
			float buttonPosX = (float) width * 0.03F;
			float buttonWidth = ((float) width * 0.1744F) - 20;
	
			addButton(togglePriv = new Button((int) (width*0.25)-2, button_statsY + (1 * 18), 100, 20, new TranslationTextComponent(""), (e) -> { action("togglePriv"); }));
			addButton(size = new Button((int) (width * 0.25 - 2 + 100 + 4), button_statsY + (1 * 18), (int) 20, 20, new TranslationTextComponent(pSize+""), (e) -> { action("size"); }));
			addButton(toggleFF = new Button((int) (width*0.25)-2, button_statsY + (3 * 18), 100, 20, new TranslationTextComponent(""), (e) -> { action("ff"); }));
			addButton(accept = new Button((int) (width*0.25)-2, button_statsY + (5 * 18), (int) 130, 20, new TranslationTextComponent(Utils.translateToLocal(Strings.Gui_Menu_Accept)), (e) -> { action("accept"); }));
			addButton(back = new MenuButton((int) buttonPosX, button_statsY + (0 * 18), (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Menu_Back), ButtonType.BUTTON, (e) -> { action("back"); }));			
		}
		
		updateButtons();
	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		
		//fill(125, ((-140 / 16) + 75) + 10, 200, ((-140 / 16) + 75) + 20, 0xFFFFFF);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		worldData = ModCapabilities.getWorld(minecraft.world);
		party = worldData.getPartyFromMember(minecraft.player.getUniqueID());
		
		int buttonX = (int)(width*0.25);
		drawString(matrixStack, minecraft.fontRenderer, Utils.translateToLocal(Strings.Gui_Menu_Party_Create_Accessibility), buttonX, (int)(height * 0.21), 0xFFFFFF);
		drawString(matrixStack, minecraft.fontRenderer, Utils.translateToLocal("Friendly Fire"), buttonX, (int)(height * 0.21) + 38, 0xFFFFFF);
	}
	
}

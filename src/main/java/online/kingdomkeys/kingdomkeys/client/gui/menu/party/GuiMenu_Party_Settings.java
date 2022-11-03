package online.kingdomkeys.kingdomkeys.client.gui.menu.party;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundSource;
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

import java.awt.*;

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
		worldData = ModCapabilities.getWorld(minecraft.level);
	}

	protected void action(String string) {
		switch(string) {
		case "back":
			minecraft.level.playSound(minecraft.player, minecraft.player.blockPosition(), ModSounds.menu_in.get(), SoundSource.MASTER, 1.0f, 1.0f);
			minecraft.setScreen(new GuiMenu_Party_Leader());
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
			
			minecraft.level.playSound(minecraft.player, minecraft.player.blockPosition(), ModSounds.menu_in.get(), SoundSource.MASTER, 1.0f, 1.0f);
			minecraft.setScreen(new GuiMenu_Party_Leader());
			
			break;
		case "size":
			if(pSize == Party.PARTY_LIMIT) {
				pSize = 2;
			} else {
				pSize++;
			}
			size.setMessage(new TranslatableComponent(pSize+""));
			break;
		}
		
		updateButtons();
	}

	private void updateButtons() {
		//IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);
		togglePriv.setMessage(priv ? new TranslatableComponent(Utils.translateToLocal(Strings.Gui_Menu_Party_Create_Accessibility_Private)) : new TranslatableComponent(Utils.translateToLocal(Strings.Gui_Menu_Party_Create_Accessibility_Public)));
		toggleFF.setMessage(new TranslatableComponent(friendlyFire+""));// ? new TranslationTextComponent(Utils.translateToLocal("FF")) : new TranslationTextComponent(Utils.translateToLocal(Strings.Gui_Menu_Party_Create_Accessibility_Public)));

		
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
		this.renderables.clear();
		
		party = worldData.getPartyFromMember(minecraft.player.getUUID());
		if(party != null) {
			priv = party.getPriv();
			pSize = party.getSize();
			friendlyFire = party.getFriendlyFire();
			
			float topBarHeight = (float) height * 0.17F;
			int button_statsY = (int) topBarHeight + 5;
			float buttonPosX = (float) width * 0.03F;
			float buttonWidth = ((float) width * 0.1744F) - 20;
	
			addRenderableWidget(togglePriv = new Button((int) (width*0.25)-2, button_statsY + (1 * 18), 100, 20, new TranslatableComponent(""), (e) -> { action("togglePriv"); }));
			addRenderableWidget(size = new Button((int) (width * 0.25 - 2 + 100 + 4), button_statsY + (1 * 18), (int) 20, 20, new TranslatableComponent(pSize+""), (e) -> { action("size"); }));
			addRenderableWidget(toggleFF = new Button((int) (width*0.25)-2, button_statsY + (3 * 18), 100, 20, new TranslatableComponent(""), (e) -> { action("ff"); }));
			addRenderableWidget(accept = new Button((int) (width*0.25)-2, button_statsY + (5 * 18), (int) 130, 20, new TranslatableComponent(Utils.translateToLocal(Strings.Gui_Menu_Accept)), (e) -> { action("accept"); }));
			addRenderableWidget(back = new MenuButton((int) buttonPosX, button_statsY + (0 * 18), (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Menu_Back), ButtonType.BUTTON, (e) -> { action("back"); }));
		}
		
		updateButtons();
	}

	@Override
	public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		
		//fill(125, ((-140 / 16) + 75) + 10, 200, ((-140 / 16) + 75) + 20, 0xFFFFFF);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		worldData = ModCapabilities.getWorld(minecraft.level);
		party = worldData.getPartyFromMember(minecraft.player.getUUID());
		
		int buttonX = (int)(width*0.25);
		drawString(matrixStack, minecraft.font, Utils.translateToLocal(Strings.Gui_Menu_Party_Create_Accessibility), buttonX, (int)(height * 0.21), 0xFFFFFF);
		drawString(matrixStack, minecraft.font, Utils.translateToLocal("Friendly Fire"), buttonX, (int)(height * 0.21) + 38, 0xFFFFFF);
	}
	
}

package online.kingdomkeys.kingdomkeys.client.gui.menu.struggle;

import java.awt.Color;

import org.jetbrains.annotations.NotNull;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.IWorldCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton.ButtonType;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.lib.Struggle;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSStruggleSettings;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class StruggleSettings extends MenuBackground {

	
	boolean priv = false;
	byte pSize = Struggle.PARTICIPANTS_LIMIT;
	float dmgMult = 1;
	BlockPos boardPos;
	
	Button togglePriv, accept, size;
	MenuButton back;
		
	final IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);
	IWorldCapabilities worldData;
	
	Struggle struggle;
		
	public StruggleSettings(BlockPos pos) {
		super(Strings.Gui_Menu_Party_Leader_Settings, new Color(252, 173, 3));
		drawPlayerInfo = true;
		worldData = ModCapabilities.getWorld(minecraft.level);
		boardPos = pos;
	}

	protected void action(String string) {
		switch(string) {
		case "back":
			minecraft.level.playSound(minecraft.player, minecraft.player.blockPosition(), ModSounds.menu_in.get(), SoundSource.MASTER, 1.0f, 1.0f);
			minecraft.setScreen(new MenuStruggle(boardPos));
			break;
		case "togglePriv":
			priv = !priv;
			break;
		case "dmg_mult":
			dmgMult = 1;//TODO
			break;
		case "accept":
			//struggle.setPriv(priv);
			struggle.setSize(pSize);
			struggle.setDamageMult(dmgMult);
			PacketHandler.sendToServer(new CSStruggleSettings(struggle));
			
			minecraft.level.playSound(minecraft.player, minecraft.player.blockPosition(), ModSounds.menu_in.get(), SoundSource.MASTER, 1.0f, 1.0f);
			minecraft.setScreen(new MenuStruggle(boardPos));
			
			break;
		case "size":
			if(pSize == Struggle.PARTICIPANTS_LIMIT) {
				pSize = 2;
			} else {
				pSize++;
			}
			size.setMessage(Component.translatable(pSize+""));
			break;
		}
		
		updateButtons();
	}

	private void updateButtons() {
		//IPlayerCapabilities playerData = ModCapabilities.getPlayer(minecraft.player);
		togglePriv.setMessage(priv ? Component.translatable(Utils.translateToLocal(Strings.Gui_Menu_Party_Create_Accessibility_Private)) : Component.translatable(Utils.translateToLocal(Strings.Gui_Menu_Party_Create_Accessibility_Public)));
		//toggleFF.setMessage(Component.translatable(friendlyFire+""));// ? new TranslationTextComponent(Utils.translateToLocal("FF")) : new TranslationTextComponent(Utils.translateToLocal(Strings.Gui_Menu_Party_Create_Accessibility_Public)));

		
		//TBName
		togglePriv.visible = true;
		//toggleFF.visible = true;
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
		
		//Get struggle from name stored in the block
		struggle = worldData.getStruggleFromBlockPos(boardPos);
		if(struggle != null) {
			//priv = struggle.getPriv();
			pSize = struggle.getSize();
			dmgMult = struggle.getDamageMult();
			
			float topBarHeight = (float) height * 0.17F;
			int button_statsY = (int) topBarHeight + 5;
			float buttonPosX = (float) width * 0.03F;
			float buttonWidth = ((float) width * 0.1744F) - 20;
	
			addRenderableWidget(togglePriv = Button.builder(Component.translatable(""), (e) -> {
				action("togglePriv");
			}).bounds((int) (width*0.25)-2, button_statsY + (1 * 18), 100, 20).build());
			
			addRenderableWidget(size = Button.builder(Component.translatable(""), (e) -> {
				action("size");
			}).bounds((int) (width * 0.25 - 2 + 100 + 4), button_statsY + (1 * 18), (int) 20, 20).build());
			
			/*addRenderableWidget(toggleFF = Button.builder(Component.translatable(""), (e) -> {
				action("ff");
			}).bounds((int) (width*0.25)-2, button_statsY + (3 * 18), 100, 20).build());
			*/
			addRenderableWidget(accept = Button.builder(Component.translatable(""), (e) -> {
				action("accept");
			}).bounds((int) (width*0.25)-2, button_statsY + (5 * 18), (int) 130, 20).build());
			
			addRenderableWidget(back = new MenuButton((int) buttonPosX, button_statsY + (0 * 18), (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Menu_Back), ButtonType.BUTTON, (e) -> { action("back"); }));
		}
		
		updateButtons();
	}

	@Override
	public void render(@NotNull GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
		
		//fill(125, ((-140 / 16) + 75) + 10, 200, ((-140 / 16) + 75) + 20, 0xFFFFFF);
		super.render(gui, mouseX, mouseY, partialTicks);
		worldData = ModCapabilities.getWorld(minecraft.level);
		struggle = worldData.getStruggleFromBlockPos(boardPos);
		
		int buttonX = (int)(width*0.25);
		gui.drawString(minecraft.font, Utils.translateToLocal(Strings.Gui_Menu_Party_Create_Accessibility), buttonX, (int)(height * 0.21), 0xFFFFFF);
		gui.drawString(minecraft.font, Utils.translateToLocal("Friendly Fire"), buttonX, (int)(height * 0.21) + 38, 0xFFFFFF);
	}
	
}

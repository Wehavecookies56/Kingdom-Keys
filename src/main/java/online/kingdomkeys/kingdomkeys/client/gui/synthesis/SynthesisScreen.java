package online.kingdomkeys.kingdomkeys.client.gui.synthesis;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.gui.IPlayerDataRequester;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton.ButtonType;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.item.KeychainItem;
import online.kingdomkeys.kingdomkeys.item.BagItem;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.lib.Tags;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSCloseMoogleGUI;
import online.kingdomkeys.kingdomkeys.synthesis.shop.ShopListRegistry;
import online.kingdomkeys.kingdomkeys.util.Utils;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class SynthesisScreen extends MenuBackground implements IPlayerDataRequester {
		
	MenuButton synthesise, forge, materials, shop;

	PlayerData playerData;
	
	String invFile = ModConfigs.SERVER.projectorHasShop.get() ? "kingdomkeys:default" : "";
	int moogle = -1;

	String name;

	public SynthesisScreen(PlayerData playerData, String name) {
		super(!name.isEmpty() ? name : Strings.Gui_Synthesis, new Color(0,255,0));
		drawPlayerInfo = true;
		this.playerData = playerData;
	}
	
	public SynthesisScreen(PlayerData playerData, String inv, String name, int moogle) {
		this(playerData, name == null || name.isEmpty() ? Strings.Gui_Synthesis : Component.translatable(Strings.Gui_Synthesis_Moogle_Name, name).getString());
		this.moogle = moogle;
		this.name = name;
		if (ShopListRegistry.getInstance().containsKey(ResourceLocation.parse(inv)) || inv.isEmpty())
			this.invFile = inv;
		else {
			KingdomKeys.LOGGER.error("The Shop '" + inv + "' does not exist or didn't get registered");
			this.invFile = "";
		}
	}

	protected void action(String string) {
		switch(string) {
		case "synthesise":
			minecraft.level.playSound(minecraft.player, minecraft.player.blockPosition(), ModSounds.menu_in.get(), SoundSource.MASTER, 1.0f, 1.0f);
			minecraft.setScreen(new SynthesisCreateScreen(playerData, this));
			break;
		case "forge":
			minecraft.level.playSound(minecraft.player, minecraft.player.blockPosition(), ModSounds.menu_in.get(), SoundSource.MASTER, 1.0f, 1.0f);
			minecraft.setScreen(new SynthesisForgeScreen(playerData, this));
			break;
		case "materials":
			minecraft.level.playSound(minecraft.player, minecraft.player.blockPosition(), ModSounds.menu_in.get(), SoundSource.MASTER, 1.0f, 1.0f);
			minecraft.setScreen(new SynthesisMaterialScreen(playerData, this));
			break;
		case "shop":
			minecraft.level.playSound(minecraft.player, minecraft.player.blockPosition(), ModSounds.menu_in.get(), SoundSource.MASTER, 1.0f, 1.0f);
			minecraft.setScreen(new ShopScreen(playerData, this));
			break;
		}
	}

	@Override
	public void onClose() {
		if (moogle != -1) {
			PacketHandler.sendToServer(new CSCloseMoogleGUI(moogle));
		}
		super.onClose();
	}

	@Override
	public void init() {
		super.init();
		this.renderables.clear();
				
		float topBarHeight = (float) height * 0.17F;
		int button_statsY = (int) topBarHeight + 5;
		float buttonPosX = (float) width * 0.03F;
		float buttonWidth = ((float) width * 0.1744F) - 20;

		int pos = 0;
		if(invFile != null && !invFile.equals(""))
			addRenderableWidget(shop = new MenuButton((int) buttonPosX, button_statsY + (pos++ * 18), (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Shop), ButtonType.BUTTON, (e) -> { action("shop"); }));

		addRenderableWidget(synthesise = new MenuButton((int) buttonPosX, button_statsY + (pos++ * 18), (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Synthesis_Synthesise), ButtonType.BUTTON, (e) -> { action("synthesise"); }));
		addRenderableWidget(forge = new MenuButton((int) buttonPosX, button_statsY + (pos++ * 18), (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Synthesis_Forge), ButtonType.BUTTON, (e) -> { action("forge"); }));
		addRenderableWidget(materials = new MenuButton((int) buttonPosX, button_statsY + (pos++ * 18), (int) buttonWidth, Utils.translateToLocal(Strings.Gui_Synthesis_Materials), ButtonType.BUTTON, (e) -> { action("materials"); }));

		boolean hasKeychain = false;
		boolean hasMaterial = false;
		Player player = Minecraft.getInstance().player;
		for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
			if (!hasKeychain && player.getInventory().getItem(i).getItem() instanceof KeychainItem) {
				hasKeychain = true;
			}
			if (!hasMaterial && player.getInventory().getItem(i).is(Tags.MATERIALS)) {
				hasMaterial = true;
			}

			//Requires player to open it to sync with the client but it works
			if(player.getInventory().getItem(i).getItem() instanceof BagItem){
				IItemHandler bagInv = player.getInventory().getItem(i).getCapability(Capabilities.ItemHandler.ITEM);
				for (int j = 0; j < bagInv.getSlots(); j++) { //Check bag slots
					ItemStack bagItem = bagInv.getStackInSlot(j);
					if (!ItemStack.matches(bagItem, ItemStack.EMPTY) && bagItem.is(Tags.MATERIALS)) { //If current bag slot is filled
						hasMaterial = true;
					}
				}
			}
		}

		for (ItemStack stack : playerData.getEquippedKeychains().values()) {
            if (!stack.isEmpty()) {
                hasKeychain = true;
                break;
            }
		}

		if (playerData.getKnownRecipeList().isEmpty()) {
			synthesise.active = false;
		}
		if (!hasKeychain) {
			forge.active = false;
		}
		if (playerData.getMaterialMap().isEmpty() && !hasMaterial) {
			materials.active = false;
		}
	}

	@Override
	public void render(@NotNull GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
		
		//fill(125, ((-140 / 16) + 75) + 10, 200, ((-140 / 16) + 75) + 20, 0xFFFFFF);
		super.render(gui, mouseX, mouseY, partialTicks);
	}
	
	@Override
	public boolean isPauseScreen() {
		return false;
	}

	@Override
	public void updatePlayerData(PlayerData playerData) {
		this.playerData = playerData;
	}
}

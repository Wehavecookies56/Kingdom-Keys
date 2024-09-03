package online.kingdomkeys.kingdomkeys.client.gui.container;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.data.ModData;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.HiddenButton;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.item.ModComponents;
import online.kingdomkeys.kingdomkeys.menu.SynthesisBagMenu;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSUpgradeSynthesisBagPacket;
import online.kingdomkeys.kingdomkeys.util.Utils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SynthesisBagScreen extends AbstractContainerScreen<SynthesisBagMenu> {

	private static final String textureBase = "textures/gui/synthesis_bag_";
	int[] texHeight = { 140, 176, 212, 248 };
	int bagLevel = 0;
	HiddenButton upgradeButton;

	public SynthesisBagScreen(SynthesisBagMenu container, Inventory playerInv, Component title) {
		super(container, playerInv, title);
		minecraft = Minecraft.getInstance();
	}

	@Override
	protected void init() {;
		bagLevel = menu.bag.getOrDefault(ModComponents.SYNTH_BAG_LEVEL, 0);
		this.imageHeight = texHeight[bagLevel];
		this.imageWidth = 193;
		addRenderableWidget(upgradeButton = new HiddenButton((width - imageWidth) / 2 + imageWidth - 20, (height / 2) - (imageHeight / 2) + 17, 18, 18, (e) -> {
			upgrade();
		}));
		
		super.init();
	}
	
	private void upgrade() {
		if (bagLevel < 3) {
			if(PlayerData.get(minecraft.player).getMunny() >= Utils.getBagCosts(bagLevel)) {
				PacketHandler.sendToServer(new CSUpgradeSynthesisBagPacket());
				onClose();
			}
		}
	}

	@Override
	public void render(@NotNull GuiGraphics gui, int x, int y, float partialTicks) {
		ItemStack stack = Utils.getItemInAnyHand(minecraft.player, ModItems.synthesisBag.get()); //Get if the player is holding a synth bag in any hand
		//If stack is null or empty or anything other than synth bag it means it's not holding one, close the gui
		if(stack == null || stack.isEmpty() || stack.getItem() != ModItems.synthesisBag.get()) {
			onClose();
		}

		this.renderBackground(gui, x, y, partialTicks);
		super.render(gui, x, y, partialTicks);
		this.renderTooltip(gui, x, y);
		List<Component> list = new ArrayList<>();
		upgradeButton.visible = bagLevel < 3;
		
		if(upgradeButton.visible) {
			if (x >= upgradeButton.getX() && x <= upgradeButton.getX() + upgradeButton.getWidth()) {
				if (y >= upgradeButton.getY() && y <= upgradeButton.getY() + upgradeButton.getHeight()) {
					list.add(Component.translatable("gui.synthesisbag.upgrade"));					
					list.add(Component.translatable(ChatFormatting.YELLOW+ Component.translatable("gui.synthesisbag.munny").getString()+": "+Utils.getBagCosts(bagLevel)));
					if(PlayerData.get(minecraft.player).getMunny() < Utils.getBagCosts(bagLevel)) {
						list.add(Component.translatable(ChatFormatting.RED+ Component.translatable("gui.synthesisbag.notenoughmunny").getString()));
					}
					gui.renderTooltip(font, list, Optional.empty(), x, y);
				}
			}
		}
		
	}

	@Override
	protected void renderLabels(GuiGraphics gui, int mouseX, int mouseY) {
		String s = title.getString()+ " LV." + (bagLevel + 1);
		gui.drawString(font, s, imageWidth / 2 -17 / 2 - font.width(s) / 2, 5, 4210752, false);
	}

	@Override
	protected void renderBg(@NotNull GuiGraphics gui, float partialTicks, int mouseX, int mouseY) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

		int xPos = (width - imageWidth) / 2;
		int yPos = (height / 2) - (imageHeight / 2);
		gui.blit(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, textureBase + bagLevel + ".png"), xPos, yPos, 0, 0, imageWidth, imageHeight);
	}

}

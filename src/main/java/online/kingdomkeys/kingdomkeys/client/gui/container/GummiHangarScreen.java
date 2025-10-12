package online.kingdomkeys.kingdomkeys.client.gui.container;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.gui.widget.ExtendedButton;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.block.GummiHangarBlock;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.HiddenButton;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.entity.GummiShipEntity;
import online.kingdomkeys.kingdomkeys.lib.GummiStructure;
import online.kingdomkeys.kingdomkeys.menu.GummiHangarMenu;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSCreateGummiShip;
import online.kingdomkeys.kingdomkeys.network.cts.CSEditGummiShip;
import online.kingdomkeys.kingdomkeys.network.cts.CSImportExportGummiShip;
import online.kingdomkeys.kingdomkeys.util.Utils;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class GummiHangarScreen extends AbstractContainerScreen<GummiHangarMenu> {
	private static final DecimalFormat df = new DecimalFormat("0.00", DecimalFormatSymbols.getInstance(Locale.US));
	private static final ResourceLocation texture = ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/gui/gummi_hangar.png");

	public GummiHangarScreen(GummiHangarMenu container, Inventory inventory, Component title) {
		super(container, inventory, title);
		this.imageWidth = 193;
		this.imageHeight = 212;
	}

	ExtendedButton build, editShip, imp, exp;
	EditBox name;
	GummiStructure structure;

	HiddenButton upgradeButton;

	@Override
	protected void init() {
		super.init();
		addRenderableWidget(upgradeButton = new HiddenButton((width - imageWidth) / 2 + imageWidth - 20, (height / 2) - (imageHeight / 2) + 17, 18, 18, (e) -> {
			upgrade();
		}));
		addRenderableWidget(name = new EditBox(font, leftPos+((imageWidth - upgradeButton.getWidth())/2) - 50, topPos + 16, 100, 20, Component.empty()));

		addRenderableWidget(imp = new ExtendedButton(name.getX(), name.getY() + name.getHeight()+1, name.getWidth()/2, 18, Component.translatable("IMPORT"), p -> {
			PacketHandler.sendToServer(new CSImportExportGummiShip(name.getValue(), menu.containerId, false));
		}));
		addRenderableWidget(exp = new ExtendedButton(name.getX()+name.getWidth()/2, name.getY() + name.getHeight()+1, name.getWidth()/2, 18, Component.translatable("EXPORT"), p -> {
			PacketHandler.sendToServer(new CSImportExportGummiShip(name.getValue(), menu.containerId, true));
		}));

		addRenderableWidget(build = new ExtendedButton(leftPos + imageWidth - 180, topPos + 97, 70, 20, Component.translatable("BUILD GUMMI"), p -> {
			BlockPos origin = menu.TE.getBlockPos();
			BlockState hangar = minecraft.level.getBlockState(origin);

			if(Utils.getAmountOfGummiShipsInBuildPlate(minecraft.level,origin,hangar.getValue(GummiHangarBlock.FACING),hangar.getValue(GummiHangarBlock.SIZE)) == 0){
				PacketHandler.sendToServer(new CSCreateGummiShip(name.getValue(), menu.containerId));
				minecraft.setScreen(null);
			}
		}));
		addRenderableWidget(editShip = new ExtendedButton(build.getX()+build.getWidth()+10, topPos + 97, 70, 20, Component.translatable("EDIT GUMMI"), p -> {
			PacketHandler.sendToServer(new CSEditGummiShip(name.getValue(), menu.containerId));
			minecraft.setScreen(null);
		}));
	}

	private void upgrade() {
		/*if (bagLevel < 3) {
			if(PlayerData.get(minecraft.player).getMunny() >= Utils.getBagCosts(bagLevel)) {
				PacketHandler.sendToServer(new CSUpgradeSynthesisBagPacket());
				onClose();
			}
		}*/
	}

	public void updateShip(){
		BlockPos origin = menu.TE.getBlockPos();
		BlockState hangar = minecraft.level.getBlockState(origin);

		structure = Utils.getGummiStructureWithFacing(minecraft.level,origin,hangar.getValue(GummiHangarBlock.FACING),hangar.getValue(GummiHangarBlock.SIZE));
	}
	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if (name.isFocused()) {
			if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
				onClose();
			}
			if (keyCode == GLFW.GLFW_KEY_ENTER) {
				name.setFocused(false);
			}
			return name.keyPressed(keyCode, scanCode, modifiers);
		} else {
			return super.keyPressed(keyCode, scanCode, modifiers);
		}
	}

	@Override
	public void render(@NotNull GuiGraphics gui, int mouseX, int mouseY, float partialTick) {
		this.renderBackground(gui, mouseX, mouseY, partialTick);
		super.render(gui, mouseX, mouseY, partialTick);
		this.renderTooltip(gui, mouseX, mouseY);

		List<Component> list = new ArrayList<>();
		upgradeButton.visible = true;//bagLevel < 3;

		BlockPos origin = menu.TE.getBlockPos();
		BlockState hangar = minecraft.level.getBlockState(origin);

		if(Utils.getAmountOfGummiShipsInBuildPlate(minecraft.level,origin,hangar.getValue(GummiHangarBlock.FACING),hangar.getValue(GummiHangarBlock.SIZE)) != 0){
			if (mouseX >= build.getX() && mouseX <= build.getX() + build.getWidth()) {
				if (mouseY >= build.getY() && mouseY <= build.getY() + build.getHeight()) {
					list.add(Component.translatable(ChatFormatting.DARK_RED + Component.translatable("There's already a Gummi Ship in the building area").getString()));
					gui.renderTooltip(font, list, Optional.empty(), mouseX, mouseY);
				}
			}
		}
		if(upgradeButton.visible) {
			if (mouseX >= upgradeButton.getX() && mouseX <= upgradeButton.getX() + upgradeButton.getWidth()) {
				if (mouseY >= upgradeButton.getY() && mouseY <= upgradeButton.getY() + upgradeButton.getHeight()) {
					list.add(Component.translatable("gui.synthesisbag.upgrade"));
					int currentCost = Utils.getHangarCosts(0);
					list.add(Component.translatable(ChatFormatting.YELLOW+ Component.translatable("gui.synthesisbag.munny").getString()+": "+currentCost));
					if(PlayerData.get(minecraft.player).getMunny() < currentCost) {
						list.add(Component.translatable(ChatFormatting.RED+ Component.translatable("gui.synthesisbag.notenoughmunny").getString()));
					}
					gui.renderTooltip(font, list, Optional.empty(), mouseX, mouseY);
				}
			}
		}
	}

	@Override
	protected void renderLabels(@NotNull GuiGraphics gui, int mouseX, int mouseY) {
		gui.drawString(font, Component.translatable(this.title.getString()).getString(), 8.0F, 6.0F, 4210752, false);
		gui.drawString(font, this.playerInventoryTitle.getString(), 8F, (float) (this.imageHeight - 94), 4210752, false);
		updateShip();
		if(structure != null){
			GummiShipEntity.ShipStats stats = Utils.getShipStats(structure);
			int x = 10;
			int y = this.imageHeight-164;
			String effSpeed = df.format(stats.getEffectiveSpeed()).equals("NaN")? "0": df.format(stats.getEffectiveSpeed()) ;
			gui.drawString(font, "Power: " + stats.speed(), x, y+=10, 4210752, false);
			//gui.drawString(font, "Firepower: ", imageWidth / 2, y, 4210752, false);
			gui.drawString(font, "Weight: " + stats.weight(), x, y+=10, 4210752, false);
			gui.drawString(font, "Eff. Speed: " + effSpeed, x, y+=10, 4210752, false);
			gui.drawString(font, "Seats: " + stats.passengerSlots().size(), x, y+=10, 4210752, false);

			BlockPos origin = menu.TE.getBlockPos();
			BlockState hangar = minecraft.level.getBlockState(origin);

            build.active = Utils.getAmountOfGummiShipsInBuildPlate(minecraft.level, origin, hangar.getValue(GummiHangarBlock.FACING), hangar.getValue(GummiHangarBlock.SIZE)) == 0;
		}
	}

	@Override
	protected void renderBg(@NotNull GuiGraphics gui, float partialTicks, int mouseX, int mouseY) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

		int xPos = (width - imageWidth) / 2;
		int yPos = (height / 2) - (imageHeight / 2);
		gui.blit(texture, xPos, yPos, 0, 0, imageWidth, imageHeight);
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		if (!(mouseX >= name.getX() && mouseX <= name.getX() + name.getWidth() && mouseY >= name.getY() && mouseY <= name.getY() + name.getHeight())) {
			name.setFocused(false);
		}
		return super.mouseReleased(mouseX, mouseY, button);
	}
}

package online.kingdomkeys.kingdomkeys.client.gui.container;

import com.mojang.blaze3d.systems.RenderSystem;
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
import online.kingdomkeys.kingdomkeys.block.GummiEditorBlock;
import online.kingdomkeys.kingdomkeys.entity.GummiShipEntity;
import online.kingdomkeys.kingdomkeys.lib.GummiStructure;
import online.kingdomkeys.kingdomkeys.menu.GummiEditorMenu;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSCreateGummiShip;
import online.kingdomkeys.kingdomkeys.network.cts.CSEditGummiShip;
import online.kingdomkeys.kingdomkeys.util.Utils;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

public class GummiEditorScreen extends AbstractContainerScreen<GummiEditorMenu> {

	private static final ResourceLocation texture = ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/gui/gummi_editor.png");

	public GummiEditorScreen(GummiEditorMenu container, Inventory inventory, Component title) {
		super(container, inventory, title);
		this.imageHeight = 186;
	}

	ExtendedButton create, editShip;
	EditBox name;
	GummiStructure structure;

	@Override
	protected void init() {
		super.init();
		addRenderableWidget(create = new ExtendedButton(leftPos + imageWidth - 70, topPos + 80, 60, 15, Component.translatable("CREATE"), p -> {
			PacketHandler.sendToServer(new CSCreateGummiShip(name.getValue(), menu.containerId));
		}));
		addRenderableWidget(editShip = new ExtendedButton(leftPos + imageWidth - 70, topPos + 60, 60, 15, Component.translatable("EDIT GUMMI"), p -> {
			PacketHandler.sendToServer(new CSEditGummiShip(name.getValue(), menu.containerId));
		}));
		addRenderableWidget(name = new EditBox(font, leftPos, topPos + 80, 100, 20, Component.empty()));
	}

	public void updateShip(){
		BlockPos origin = menu.TE.getBlockPos();
		int size = 7;
		BlockState hangar = minecraft.level.getBlockState(origin);

		structure = Utils.getGummiStructureWithFacing(minecraft.level,origin,hangar.getValue(GummiEditorBlock.FACING),size);
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
	}

	@Override
	protected void renderLabels(@NotNull GuiGraphics gui, int mouseX, int mouseY) {
		gui.drawString(font, this.title.getString(), 8.0F, 6.0F, 4210752, false);
		gui.drawString(font, this.playerInventoryTitle.getString(), 8.0F, (float) (this.imageHeight - 96 + 2), 4210752, false);
		updateShip();
		if(structure != null){
			GummiShipEntity.ShipStats stats = Utils.getShipStats(structure);
			gui.drawString(font, "Speed: " + stats.speed(), 8.0F, 16, 4210752, false);
			gui.drawString(font, "Weight: " + stats.weight(), 8.0F, 26, 4210752, false);
			gui.drawString(font, "Seats: " + stats.passengerSlots().size(), 8.0F, 36, 4210752, false);
		}

		// super.drawGuiContainerForegroundLayer(matrixStack, mouseX, mouseY);
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

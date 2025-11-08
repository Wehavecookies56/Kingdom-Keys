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
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractFurnaceMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.gui.widget.ExtendedButton;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.block.GummiHangarBlock;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.HiddenButton;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.entity.GummiShipEntity;
import online.kingdomkeys.kingdomkeys.item.ModComponents;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.kingdomkeys.kingdomkeys.lib.GummiStructure;
import online.kingdomkeys.kingdomkeys.menu.GummiHangarMenu;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.*;
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
		this.imageWidth = 176;
		this.imageHeight = 212;
	}

	ExtendedButton build, editShip, imp, exp, moveShipFW,moveShipBW,moveShipLeft,moveShipRight,moveShipUp,moveShipDown, showLines;
	EditBox name;
	GummiStructure structure;

	HiddenButton upgradeButton;

	@Override
	protected void init() {
		super.init();
		int xPos = (width - imageWidth) / 2;
		addRenderableWidget(upgradeButton = new HiddenButton(xPos+imageWidth-3, (height / 2) - (imageHeight / 2) + 15, 17, 21, texture,176,0, (e) -> {
			upgrade();
		}));
		addRenderableWidget(name = new EditBox(font, leftPos+((imageWidth - upgradeButton.getWidth())/2) - 50, topPos + 16, 100, 20, Component.literal(menu.TE.getLastShipName())));

		name.setValue((menu.TE.getLastShipName()));
		addRenderableWidget(imp = new ExtendedButton(name.getX(), name.getY() + name.getHeight()+1, name.getWidth()/2, 18, Component.translatable("IMPORT"), p -> {
			PacketHandler.sendToServer(new CSImportExportGummiShip(name.getValue(), menu.containerId, false));
		}));
		addRenderableWidget(exp = new ExtendedButton(name.getX()+name.getWidth()/2, name.getY() + name.getHeight()+1, name.getWidth()/2, 18, Component.translatable("EXPORT"), p -> {
			PacketHandler.sendToServer(new CSImportExportGummiShip(name.getValue(), menu.containerId, true));
		}));

		addRenderableWidget(build = new ExtendedButton(leftPos + imageWidth - 162, topPos + 97, 70, 20, Component.translatable("BUILD GUMMI"), p -> {
			BlockPos origin = menu.TE.getBlockPos();
			BlockState hangar = minecraft.level.getBlockState(origin);

			if(Utils.getAmountOfGummiShipsInBuildPlate(minecraft.level,origin,hangar.getValue(GummiHangarBlock.FACING),GummiHangarBlock.getSize(hangar.getValue(GummiHangarBlock.LEVEL))) == 0){
				PacketHandler.sendToServer(new CSBuildGummiShip(name.getValue(), menu.containerId));
				// When we build a ship from blocks to entity we want to clear the name
				menu.TE.setLastShipName("");
				name.setValue("");
				onClose();
			}
		}));
		addRenderableWidget(editShip = new ExtendedButton(build.getX()+build.getWidth()+10, topPos + 97, 70, 20, Component.translatable("EDIT GUMMI"), p -> {
			BlockPos origin = menu.TE.getBlockPos();
			BlockState hangar = minecraft.level.getBlockState(origin);
			GummiShipEntity gummi = Utils.getGummiShipInBuildPlate(minecraft.level, origin, hangar.getValue(GummiHangarBlock.FACING), GummiHangarBlock.getSize(hangar.getValue(GummiHangarBlock.LEVEL)));
			if(gummi != null) {
				GummiStructure struct = gummi.structure;
				int size = GummiHangarBlock.getSize(hangar.getValue(GummiHangarBlock.LEVEL));

				if (struct.getWidth() <= size) {
					PacketHandler.sendToServer(new CSEditGummiShip(name.getValue(), menu.containerId));
					// When we change from entity to blocks we want to set the textbox name with the struc name
					name.setValue(struct.getName());
					menu.TE.setLastShipName(struct.getName());
					onClose();
				}
			}
		}));

        addRenderableWidget(showLines = new ExtendedButton(editShip.getX(), topPos + 117, editShip.getWidth(), 10, Component.translatable("Area: "+menu.TE.getBlockState().getValue(GummiHangarBlock.SHOW_LINES)), p -> {
            PacketHandler.sendToServer(new CSShowHangarLinesPacket(menu.containerId));
            showLines.setMessage(Component.translatable("Area:").append(" "+menu.TE.getBlockState().getValue(GummiHangarBlock.SHOW_LINES).next()));
        }));

		int x = editShip.getX();
		int y = topPos + 76;
		addRenderableWidget(moveShipDown = new ExtendedButton(x, y, 20, 10, Component.translatable("⤓"), p -> {
			PacketHandler.sendToServer(new CSMoveGummiShipPacket("DOWN", menu.containerId));
		}));
		addRenderableWidget(moveShipFW = new ExtendedButton(x + 21, y, 20, 10, Component.translatable("↑"), p -> {
			PacketHandler.sendToServer(new CSMoveGummiShipPacket("FORWARD", menu.containerId));
		}));
		addRenderableWidget(moveShipUp = new ExtendedButton(x + 42, y, 20, 10, Component.translatable("⤒"), p -> {
			PacketHandler.sendToServer(new CSMoveGummiShipPacket("UP", menu.containerId));
		}));

		y += 11;
		addRenderableWidget(moveShipLeft = new ExtendedButton(x, y, 20, 10, Component.translatable("←"), p -> {
			PacketHandler.sendToServer(new CSMoveGummiShipPacket("LEFT", menu.containerId));
		}));
		addRenderableWidget(moveShipBW = new ExtendedButton(x+21, y, 20, 10, Component.translatable("↓"), p -> {
			PacketHandler.sendToServer(new CSMoveGummiShipPacket("BACKWARD", menu.containerId));
		}));
		addRenderableWidget(moveShipRight = new ExtendedButton(x + 42, y, 20, 10, Component.translatable("→"), p -> {
			PacketHandler.sendToServer(new CSMoveGummiShipPacket("RIGHT", menu.containerId));
		}));
	}

	private void upgrade() {
		BlockPos origin = menu.TE.getBlockPos();
		BlockState hangar = minecraft.level.getBlockState(origin);
		int level = hangar.getValue(GummiHangarBlock.LEVEL);
		if (level < 3) {
			if(PlayerData.get(minecraft.player).getMunny() >= Utils.getHangarCosts(level)) {
				PacketHandler.sendToServer(new CSUpgradeGummiHangarPacket(menu.containerId));
				onClose();
			}
		}
	}

	public void updateShip(){
		BlockPos origin = menu.TE.getBlockPos();
		BlockState hangar = minecraft.level.getBlockState(origin);

		structure = Utils.getGummiStructureWithFacing(minecraft.player.getUUID(), name.getValue(), minecraft.level,origin,hangar.getValue(GummiHangarBlock.FACING),GummiHangarBlock.getSize(hangar.getValue(GummiHangarBlock.LEVEL)));
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
		BlockPos origin = menu.TE.getBlockPos();
		BlockState hangar = minecraft.level.getBlockState(origin);

		upgradeButton.visible = hangar.getValue(GummiHangarBlock.LEVEL) < 3;

		int size = GummiHangarBlock.getSize(hangar.getValue(GummiHangarBlock.LEVEL));
		if(Utils.getAmountOfGummiShipsInBuildPlate(minecraft.level,origin,hangar.getValue(GummiHangarBlock.FACING),size) != 0){
			if (mouseX >= build.getX() && mouseX <= build.getX() + build.getWidth()) {
				if (mouseY >= build.getY() && mouseY <= build.getY() + build.getHeight()) {
					list.add(Component.translatable(ChatFormatting.DARK_RED + Component.translatable("There's already a Gummi Ship in the building area").getString()));
					gui.renderTooltip(font, list, Optional.empty(), mouseX, mouseY);
				}
			}
		}

		GummiShipEntity gummi = Utils.getGummiShipInBuildPlate(minecraft.level, origin, hangar.getValue(GummiHangarBlock.FACING), size);
		if(gummi != null) {
			GummiStructure struct = gummi.structure;
			if (struct.getWidth() > GummiHangarBlock.getSize(hangar.getValue(GummiHangarBlock.LEVEL))) {
				if (mouseX >= editShip.getX() && mouseX <= editShip.getX() + editShip.getWidth()) {
					if (mouseY >= editShip.getY() && mouseY <= editShip.getY() + editShip.getHeight()) {
						list.add(Component.translatable(ChatFormatting.DARK_RED + Component.translatable("There's a Gummi Ship too big for the hangar in the building area").getString()));
						gui.renderTooltip(font, list, Optional.empty(), mouseX, mouseY);
					}
				}
			}
		}

		if(isHoveringButton(moveShipFW,mouseX,mouseY)){
			list.add(Component.translatable(ChatFormatting.WHITE + Component.translatable("Move ship forward").getString()));
			gui.renderTooltip(font, list, Optional.empty(), mouseX, mouseY);
		}
		if(isHoveringButton(moveShipBW,mouseX,mouseY)){
			list.add(Component.translatable(ChatFormatting.WHITE + Component.translatable("Move ship backwards").getString()));
			gui.renderTooltip(font, list, Optional.empty(), mouseX, mouseY);
		}
		if(isHoveringButton(moveShipLeft,mouseX,mouseY)){
			list.add(Component.translatable(ChatFormatting.WHITE + Component.translatable("Move ship to the left").getString()));
			gui.renderTooltip(font, list, Optional.empty(), mouseX, mouseY);
		}
		if(isHoveringButton(moveShipRight,mouseX,mouseY)){
			list.add(Component.translatable(ChatFormatting.WHITE + Component.translatable("Move ship to the right").getString()));
			gui.renderTooltip(font, list, Optional.empty(), mouseX, mouseY);
		}
		if(isHoveringButton(moveShipUp,mouseX,mouseY)){
			list.add(Component.translatable(ChatFormatting.WHITE + Component.translatable("Move ship higher").getString()));
			gui.renderTooltip(font, list, Optional.empty(), mouseX, mouseY);
		}
		if(isHoveringButton(moveShipDown,mouseX,mouseY)){
			list.add(Component.translatable(ChatFormatting.WHITE + Component.translatable("Move ship lower").getString()));
			gui.renderTooltip(font, list, Optional.empty(), mouseX, mouseY);
		}

		if (mouseX >= imp.getX() && mouseX <= imp.getX() + imp.getWidth()) {
			if (mouseY >= imp.getY() && mouseY <= imp.getY() + imp.getHeight()) {
				ItemStack stack = menu.TE.inventory.get().getStackInSlot(0);

				if(stack.is(ModItems.gummiShipBlueprint.get())){
					GummiStructure struct = stack.get(ModComponents.GUMMI_STRUCTURE);

					if(struct != null && struct.getWidth() > size){
						list.add(Component.translatable(ChatFormatting.DARK_RED + Component.translatable("The blueprint is too big for this hangar").getString()));
					}
				} else {
					list.add(Component.translatable(ChatFormatting.DARK_RED + Component.translatable("You need to place a blueprint to import it").getString()));
				}
				gui.renderTooltip(font, list, Optional.empty(), mouseX, mouseY);

			}
		}

		if (mouseX >= exp.getX() && mouseX <= exp.getX() + exp.getWidth()) {
			if (mouseY >= exp.getY() && mouseY <= exp.getY() + exp.getHeight()) {
				ItemStack stack = menu.TE.inventory.get().getStackInSlot(0);

				if(stack.is(ModItems.gummiShipBlueprint.get())){
					if(name.getValue().equals("")){
						list.add(Component.translatable(ChatFormatting.DARK_RED + Component.translatable("You need to specify the blueprint name").getString()));
					}
				} else {
					list.add(Component.translatable(ChatFormatting.DARK_RED + Component.translatable("You need to place a blueprint to export it").getString()));
				}
				gui.renderTooltip(font, list, Optional.empty(), mouseX, mouseY);

			}
		}

		if(upgradeButton.visible) {
			if (mouseX >= upgradeButton.getX() && mouseX <= upgradeButton.getX() + upgradeButton.getWidth()) {
				if (mouseY >= upgradeButton.getY() && mouseY <= upgradeButton.getY() + upgradeButton.getHeight()) {
					list.add(Component.translatable("gui.synthesisbag.upgrade"));
					int currentCost = Utils.getHangarCosts(hangar.getValue(GummiHangarBlock.LEVEL));
					list.add(Component.translatable(ChatFormatting.YELLOW+ Component.translatable("gui.synthesisbag.munny").getString()+": "+currentCost));
					if(PlayerData.get(minecraft.player).getMunny() < currentCost) {
						list.add(Component.translatable(ChatFormatting.RED+ Component.translatable("gui.synthesisbag.notenoughmunny").getString()));
					}
					gui.renderTooltip(font, list, Optional.empty(), mouseX, mouseY);
				}
			}
		}
	}

	public boolean isHoveringButton(ExtendedButton button, int mouseX, int mouseY) {
		return mouseX >= button.getX() && mouseX <= button.getX() + button.getWidth() && mouseY >= button.getY() && mouseY <= button.getY() + button.getHeight();
	}

	@Override
	protected void renderLabels(@NotNull GuiGraphics gui, int mouseX, int mouseY) {
		BlockState hangar = minecraft.level.getBlockState(menu.TE.getBlockPos());
		int size = GummiHangarBlock.getSize(hangar.getValue(GummiHangarBlock.LEVEL));
		gui.drawString(font, Component.translatable(this.title.getString()).getString()+" Lv."+(hangar.getValue(GummiHangarBlock.LEVEL)+1)+" ["+size+"x"+size+"x"+size+"]", 8.0F, 6.0F, 4210752, false);
		gui.drawString(font, this.playerInventoryTitle.getString(), 8F, (float) (this.imageHeight - 94), 4210752, false);
		updateShip();
		if(structure != null){
			GummiShipEntity.ShipStats stats = Utils.getShipStats(structure);
			int x = 10;
			int y = this.imageHeight-164;
			String effSpeed = df.format(stats.getEffectiveSpeed()).equals("NaN") ? "0" : df.format(stats.getEffectiveSpeed());
			gui.drawString(font, "Power: " + stats.speed(), x, y+=10, 4210752, false);
			gui.drawString(font, "Firepower: "+ stats.firepower().size(), imageWidth / 2, y, 4210752, false);
			gui.drawString(font, "Weight: " + stats.weight(), x, y+=10, 4210752, false);
			gui.drawString(font, "Armor: "+ stats.armour(), imageWidth / 2, y, 4210752, false);
			gui.drawString(font, "Eff. Speed: " + effSpeed, x, y+=10, 4210752, false);
			gui.drawString(font, "Seats: " + stats.passengerSlots().size(), x, y+=10, 4210752, false);

            gui.drawString(font, "Burn time: " + menu.TE.burnTime, x-100, y+=10, 0xFFFFFF, false);
            gui.drawString(font, "Max Burn time: "+menu.TE.maxBurnTime, x-100, y+=10, 0xFFFFFF, false);
            gui.drawString(font, "Energy: "+menu.TE.storedEnergy, x-100, y+=10, 0xFFFFFF, false);

			BlockPos origin = menu.TE.getBlockPos();
			ItemStack stack = menu.TE.inventory.get().getStackInSlot(0);
			imp.active = stack.is(ModItems.gummiShipBlueprint.get());
			exp.active = stack.is(ModItems.gummiShipBlueprint.get()) && !name.getValue().equals("");

			if(stack.is(ModItems.gummiShipBlueprint.get())){
				GummiStructure struct = stack.get(ModComponents.GUMMI_STRUCTURE);
				imp.active = struct != null && struct.getWidth() <= size;
			}

            build.active = Utils.getAmountOfGummiShipsInBuildPlate(minecraft.level, origin, hangar.getValue(GummiHangarBlock.FACING), size) == 0;

			GummiShipEntity gummi = Utils.getGummiShipInBuildPlate(minecraft.level, origin, hangar.getValue(GummiHangarBlock.FACING), size);
			if(gummi != null) {
				GummiStructure struct = gummi.structure;
				if (struct.getWidth() > size) {
					editShip.active = false;
				}
			}

            gui.pose().pushPose();
            {
                if(menu.TE.maxBurnTime > 0) {
                    System.out.println("BURN TIME: "+menu.TE.burnTime);
                    System.out.println("MAX BURN: "+ menu.TE.maxBurnTime);
                    float progress = ((float) menu.TE.burnTime / menu.TE.maxBurnTime) * 14;
                    int v = (int) progress+1;
                    blit(gui, ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/gui/gummi_hangar.png"), 152, 39 + 14 - v, 242, 14 - v, 14, v);
                }
            }
            gui.pose().popPose();

        }
	}

    public void blit(GuiGraphics gui, ResourceLocation texture, int x, int y, int u, int v, int uwidth, int vheight) {
        gui.blit(texture, x, y, u ,v, uwidth, vheight);
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

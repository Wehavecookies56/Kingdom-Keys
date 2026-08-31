package online.kingdomkeys.kingdomkeys.client.gui.container;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.client.gui.widget.ExtendedButton;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.block.gummi.GummiHangarBlock;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.HiddenButton;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.entity.GummiShipEntity;
import online.kingdomkeys.kingdomkeys.item.GummiShipBlueprintItem;
import online.kingdomkeys.kingdomkeys.item.ModComponents;
import online.kingdomkeys.kingdomkeys.lib.GummiStructure;
import online.kingdomkeys.kingdomkeys.menu.GummiHangarMenu;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.*;
import online.kingdomkeys.kingdomkeys.util.Utils;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class GummiHangarScreen extends AbstractContainerScreen<GummiHangarMenu> {
	private static final DecimalFormat df = new DecimalFormat("0.00", DecimalFormatSymbols.getInstance(Locale.US));
	private static final ResourceLocation texture = KingdomKeys.rl("textures/gui/gummi_hangar.png");

	/** Stats column sitting beside the window rather than on top of it */
	private static final int PANEL_WIDTH = 104;
	/** Clears the upgrade tab, which sticks 17 pixels out of the right edge */
	private static final int PANEL_GAP_RIGHT = 20;
	private static final int PANEL_GAP_LEFT = 4;
	private static final int PANEL_PADDING = 5;
	private static final int STATS_TOP = 6;

	/** Twice the old twelve, which is what the four file buttons needed to stop being cramped */
	private static final int BUTTON_HEIGHT = 16;
	/** Trimmed from twenty so name plus two button rows land exactly on the move arrows' top edge */
	private static final int NAME_HEIGHT = 16;
	/** Square, for the buttons that are a symbol rather than a word */
	private static final int ICON_SIZE = 16;

	public GummiHangarScreen(GummiHangarMenu container, Inventory inventory, Component title) {
		super(container, inventory, title);
		this.imageWidth = 176;
		this.imageHeight = 212;
	}

	ExtendedButton build, editShip, imp, exp, saveFile, loadFile, moveShipFW,moveShipBW,moveShipLeft,moveShipRight,moveShipUp,moveShipDown, showLines, autoBuild;
	EditBox name;
	GummiStructure structure;

	HiddenButton upgradeButton;

	private void saveToFile() {
		ItemStack blueprint = menu.getItems().getFirst();
		GummiStructure struct = blueprint.get(ModComponents.GUMMI_STRUCTURE);

		if (struct == null) {
			say(Component.translatable("container.gummi_hangar.nothing_to_save"));
			return;
		}

		String shipName = name.getValue().isBlank() ? struct.getName() : name.getValue();

		try {
			ClientUtils.saveGummiShip(shipName, struct);
			say(Component.translatable("container.gummi_hangar.file_saved", ClientUtils.gummiShipFileName(shipName)));
		} catch (IOException e) {
			KingdomKeys.LOGGER.error("Could not save gummi ship {}", shipName, e);
			say(Component.translatable("container.gummi_hangar.file_unwritable"));
		}
	}

	private void loadFromFile() {
		minecraft.setScreen(new GummiShipFilesScreen(this, menu.containerId));
	}

	private void say(Component message) {
		if (minecraft != null && minecraft.player != null) {
			minecraft.player.displayClientMessage(message, false);
		}
	}

	@Override
	protected void init() {
		super.init();
		int xPos = (width - imageWidth) / 2;
		addRenderableWidget(upgradeButton = new HiddenButton(xPos+imageWidth-3, (height / 2) - (imageHeight / 2) + 15, 17, 21, texture,176,0, (e) -> {
			upgrade();
		}));
		addRenderableWidget(name = new EditBox(font, leftPos+((imageWidth - upgradeButton.getWidth())/2) - 50, topPos + 16, 100, NAME_HEIGHT, Component.literal(menu.TE.getLastShipName())));

		name.setValue((menu.TE.getLastShipName()));

        // Sits in the margin left of the name column, which is otherwise empty
        addRenderableWidget(autoBuild = new ExtendedButton(name.getX() - ICON_SIZE - 2, name.getY() + name.getHeight(), ICON_SIZE, ICON_SIZE, autoBuildLabel(), p -> {
            PacketHandler.sendToServer(new CSToggleHangarBuildPacket(menu.containerId));
        }));

		addRenderableWidget(imp = new ExtendedButton(name.getX(), name.getY() + name.getHeight(), name.getWidth()/2, BUTTON_HEIGHT, Component.translatable("container.gummi_hangar.import"), p -> {
			PacketHandler.sendToServer(new CSImportExportGummiShip(name.getValue(), menu.containerId, false));
		}));
		addRenderableWidget(exp = new ExtendedButton(name.getX()+name.getWidth()/2, name.getY() + name.getHeight(), name.getWidth()/2, BUTTON_HEIGHT, Component.translatable("container.gummi_hangar.export"), p -> {
			PacketHandler.sendToServer(new CSImportExportGummiShip(name.getValue(), menu.containerId, true));
		}));

		addRenderableWidget(saveFile = new ExtendedButton(name.getX(), imp.getY() + imp.getHeight(), name.getWidth()/2, BUTTON_HEIGHT, Component.translatable("container.gummi_hangar.save_file"), p -> saveToFile()));
		addRenderableWidget(loadFile = new ExtendedButton(name.getX()+name.getWidth()/2, imp.getY() + imp.getHeight(), name.getWidth()/2, BUTTON_HEIGHT, Component.translatable("container.gummi_hangar.load_file"), p -> loadFromFile()));

		addRenderableWidget(build = new ExtendedButton(leftPos + imageWidth - 162, topPos + 101, 70, 16, Component.translatable("container.gummi_hangar.build"), p -> {
			BlockPos origin = menu.TE.getBlockPos();
			BlockState hangar = minecraft.level.getBlockState(origin);

			if(Utils.getAmountOfGummiShipsInBuildPlate(minecraft.level,origin,hangar.getValue(GummiHangarBlock.FACING),GummiHangarBlock.getSize(hangar.getValue(GummiHangarBlock.LEVEL))) == 0){
				PacketHandler.sendToServer(new CSBuildGummiShip(name.getValue(), menu.containerId));
				name.setValue("");
				onClose();
			}
		}));
		addRenderableWidget(editShip = new ExtendedButton(build.getX()+build.getWidth()+10, topPos + 101, 70, 16, Component.translatable("container.gummi_hangar.edit"), p -> {
			BlockPos origin = menu.TE.getBlockPos();
			BlockState hangar = minecraft.level.getBlockState(origin);
            int size = GummiHangarBlock.getSize(hangar.getValue(GummiHangarBlock.LEVEL));
            GummiShipEntity gummi = Utils.getGummiShipInBuildPlate(minecraft.level, origin, hangar.getValue(GummiHangarBlock.FACING), size);
			if(gummi != null) {
				GummiStructure struct = gummi.structure;

				// Measured by the room its blocks take up, so a ship that grew a size by visiting a bigger
				// hangar can still come back down to the one it was built in
				if (Utils.fitsInHangar(struct, size)) {
					PacketHandler.sendToServer(new CSEditGummiShip(name.getValue(), menu.containerId));
					menu.TE.setLastShipName(struct.getName());
					onClose();
				}
			}
		}));

        addRenderableWidget(showLines = new ExtendedButton(build.getX(), build.getY() - BUTTON_HEIGHT, editShip.getWidth(), BUTTON_HEIGHT, Component.translatable("kingdomkeys.gummi.hangar.area_value", menu.TE.getBlockState().getValue(GummiHangarBlock.SHOW_LINES).getDisplayName()), p -> {
            PacketHandler.sendToServer(new CSShowHangarLinesPacket(menu.containerId));
            showLines.setMessage(Component.translatable("kingdomkeys.gummi.hangar.area_value", menu.TE.getBlockState().getValue(GummiHangarBlock.SHOW_LINES).next().getDisplayName()));
        }));

		int x = editShip.getX();
		int y = topPos + 80;
		addRenderableWidget(moveShipDown = new ExtendedButton(x, y, 20, 10, Component.literal("⤓"), p -> {
			PacketHandler.sendToServer(new CSMoveGummiShipPacket("DOWN", menu.containerId));
		}));
		addRenderableWidget(moveShipFW = new ExtendedButton(x + 21, y, 20, 10, Component.literal("↑"), p -> {
			PacketHandler.sendToServer(new CSMoveGummiShipPacket("FORWARD", menu.containerId));
		}));
		addRenderableWidget(moveShipUp = new ExtendedButton(x + 42, y, 20, 10, Component.literal("⤒"), p -> {
			PacketHandler.sendToServer(new CSMoveGummiShipPacket("UP", menu.containerId));
		}));

		y += 11;
		addRenderableWidget(moveShipLeft = new ExtendedButton(x, y, 20, 10, Component.literal("←"), p -> {
			PacketHandler.sendToServer(new CSMoveGummiShipPacket("LEFT", menu.containerId));
		}));
		addRenderableWidget(moveShipBW = new ExtendedButton(x+21, y, 20, 10, Component.literal("↓"), p -> {
			PacketHandler.sendToServer(new CSMoveGummiShipPacket("BACKWARD", menu.containerId));
		}));
		addRenderableWidget(moveShipRight = new ExtendedButton(x + 42, y, 20, 10, Component.literal("→"), p -> {
			PacketHandler.sendToServer(new CSMoveGummiShipPacket("RIGHT", menu.containerId));
		}));
	}

	private void upgrade() {
		BlockPos origin = menu.TE.getBlockPos();
		BlockState hangar = minecraft.level.getBlockState(origin);
		int level = hangar.getValue(GummiHangarBlock.LEVEL);
		if (level < 4) {
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


    private Component autoBuildLabel() {
        return Component.literal(menu.TE.isBuilding() ? "\u23F8" : "\u25B6");
    }

    private boolean hasContainer(BlockPos origin) {
        for (Direction side : Direction.values()) {
            if (minecraft.level.getCapability(Capabilities.ItemHandler.BLOCK, origin.relative(side), side.getOpposite()) != null) {
                return true;
            }
        }

        return false;
    }

	@Override
	public void render(@NotNull GuiGraphics gui, int mouseX, int mouseY, float partialTick) {
		this.renderBackground(gui, mouseX, mouseY, partialTick);
		super.render(gui, mouseX, mouseY, partialTick);
		this.renderTooltip(gui, mouseX, mouseY);

		List<Component> list = new ArrayList<>();
		BlockPos origin = menu.TE.getBlockPos();
		BlockState hangar = minecraft.level.getBlockState(origin);

		upgradeButton.visible = hangar.getValue(GummiHangarBlock.LEVEL) < 4;

        autoBuild.setMessage(autoBuildLabel());
        autoBuild.active = ModConfigs.SERVER.gummiHangarAutoBuild.get() && hasContainer(origin);

        if (isHoveringButton(autoBuild, mouseX, mouseY)) {
            List<Component> autoBuildTip = new ArrayList<>();
            autoBuildTip.add(Component.translatable("container.gummi_hangar.autobuild"));
            autoBuildTip.add(Component.literal(ChatFormatting.GRAY + Component.translatable("container.gummi_hangar.autobuild.tooltip").getString()));

            if (!ModConfigs.SERVER.gummiHangarAutoBuild.get()) {
                autoBuildTip.add(Component.literal(ChatFormatting.DARK_RED + Component.translatable("container.gummi_hangar.autobuild.disabled").getString()));
            } else if (!hasContainer(origin)) {
                autoBuildTip.add(Component.literal(ChatFormatting.DARK_RED + Component.translatable("container.gummi_hangar.autobuild.nochest").getString()));
            }

            gui.renderTooltip(font, autoBuildTip, Optional.empty(), mouseX, mouseY);
        }

		int size = GummiHangarBlock.getSize(hangar.getValue(GummiHangarBlock.LEVEL));
		if(Utils.getAmountOfGummiShipsInBuildPlate(minecraft.level,origin,hangar.getValue(GummiHangarBlock.FACING),size) != 0){
			if (mouseX >= build.getX() && mouseX <= build.getX() + build.getWidth()) {
				if (mouseY >= build.getY() && mouseY <= build.getY() + build.getHeight()) {
					list.add(Component.translatable(ChatFormatting.DARK_RED + Component.translatable("container.gummi_hangar.gummifound").getString()));
					gui.renderTooltip(font, list, Optional.empty(), mouseX, mouseY);
				}
			}
		}

		GummiShipEntity gummi = Utils.getGummiShipInBuildPlate(minecraft.level, origin, hangar.getValue(GummiHangarBlock.FACING), size);
		if(gummi != null) {
			GummiStructure struct = gummi.structure;
			if (!Utils.fitsInHangar(struct, GummiHangarBlock.getSize(hangar.getValue(GummiHangarBlock.LEVEL)))) {
				if (mouseX >= editShip.getX() && mouseX <= editShip.getX() + editShip.getWidth()) {
					if (mouseY >= editShip.getY() && mouseY <= editShip.getY() + editShip.getHeight()) {
						list.add(Component.translatable(ChatFormatting.DARK_RED + Component.translatable("container.gummi_hangar.gummitoobig").getString()));
						gui.renderTooltip(font, list, Optional.empty(), mouseX, mouseY);
					}
				}
			}
		}

        if(isHoveringButton(build,mouseX,mouseY)){
            if(name.getValue().isEmpty()){
                list.add(Component.translatable(ChatFormatting.DARK_RED + Component.translatable("container.gummi_hangar.noname").getString()));
                gui.renderTooltip(font, list, Optional.empty(), mouseX, mouseY);
            }
        }

		if(isHoveringButton(moveShipFW,mouseX,mouseY)){
			list.add(Component.translatable(ChatFormatting.WHITE + Component.translatable("container.gummi_hangar.moveshipfw").getString()));
			gui.renderTooltip(font, list, Optional.empty(), mouseX, mouseY);
		}
		if(isHoveringButton(moveShipBW,mouseX,mouseY)){
			list.add(Component.translatable(ChatFormatting.WHITE + Component.translatable("container.gummi_hangar.moveshipbw").getString()));
			gui.renderTooltip(font, list, Optional.empty(), mouseX, mouseY);
		}
		if(isHoveringButton(moveShipLeft,mouseX,mouseY)){
			list.add(Component.translatable(ChatFormatting.WHITE + Component.translatable("container.gummi_hangar.moveshipleft").getString()));
			gui.renderTooltip(font, list, Optional.empty(), mouseX, mouseY);
		}
		if(isHoveringButton(moveShipRight,mouseX,mouseY)){
			list.add(Component.translatable(ChatFormatting.WHITE + Component.translatable("container.gummi_hangar.moveshipright").getString()));
			gui.renderTooltip(font, list, Optional.empty(), mouseX, mouseY);
		}
		if(isHoveringButton(moveShipUp,mouseX,mouseY)){
			list.add(Component.translatable(ChatFormatting.WHITE + Component.translatable("container.gummi_hangar.moveshiphigher").getString()));
			gui.renderTooltip(font, list, Optional.empty(), mouseX, mouseY);
		}
		if(isHoveringButton(moveShipDown,mouseX,mouseY)){
			list.add(Component.translatable(ChatFormatting.WHITE + Component.translatable("container.gummi_hangar.moveshiplower").getString()));
			gui.renderTooltip(font, list, Optional.empty(), mouseX, mouseY);
		}

		if (mouseX >= imp.getX() && mouseX <= imp.getX() + imp.getWidth()) {
			if (mouseY >= imp.getY() && mouseY <= imp.getY() + imp.getHeight()) {
				ItemStack stack = menu.TE.inventory.get().getStackInSlot(0);

				if(GummiShipBlueprintItem.isBlueprint(stack)){
					GummiStructure struct = stack.get(ModComponents.GUMMI_STRUCTURE);

					if(struct != null && !Utils.fitsInHangar(struct, size)){
						list.add(Component.translatable(ChatFormatting.DARK_RED + Component.translatable("container.gummi_hangar.blueprinttoobig").getString()));
					}
				} else {
					list.add(Component.translatable(ChatFormatting.DARK_RED + Component.translatable("container.gummi_hangar.noblueprintimp").getString()));
				}
				gui.renderTooltip(font, list, Optional.empty(), mouseX, mouseY);

			}
		}

		if (mouseX >= exp.getX() && mouseX <= exp.getX() + exp.getWidth()) {
			if (mouseY >= exp.getY() && mouseY <= exp.getY() + exp.getHeight()) {
				ItemStack stack = menu.TE.inventory.get().getStackInSlot(0);

				if(GummiShipBlueprintItem.isBlueprint(stack)){
					if(name.getValue().equals("")){
						list.add(Component.translatable(ChatFormatting.DARK_RED + Component.translatable("container.gummi_hangar.noblueprintname").getString()));
					}
				} else {
					list.add(Component.translatable(ChatFormatting.DARK_RED + Component.translatable("container.gummi_hangar.noblueprintexp").getString()));
				}
				gui.renderTooltip(font, list, Optional.empty(), mouseX, mouseY);

			}
		}

		if (isHoveringButton(saveFile, mouseX, mouseY)) {
			ItemStack stack = menu.TE.inventory.get().getStackInSlot(0);

			if (!GummiShipBlueprintItem.isBlueprint(stack)) {
				list.add(Component.translatable(ChatFormatting.DARK_RED + Component.translatable("container.gummi_hangar.noblueprintsave").getString()));
			} else if (stack.get(ModComponents.GUMMI_STRUCTURE) == null) {
				list.add(Component.translatable(ChatFormatting.DARK_RED + Component.translatable("container.gummi_hangar.nothing_to_save").getString()));
			}
			gui.renderTooltip(font, list, Optional.empty(), mouseX, mouseY);
		}

		if (isHoveringButton(loadFile, mouseX, mouseY)) {
			ItemStack stack = menu.TE.inventory.get().getStackInSlot(0);

			if (!GummiShipBlueprintItem.isBlueprint(stack)) {
				list.add(Component.translatable(ChatFormatting.DARK_RED + Component.translatable("container.gummi_hangar.noblueprintload").getString()));
			}
			gui.renderTooltip(font, list, Optional.empty(), mouseX, mouseY);
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

        if (isHoveringEnergyBar(mouseX, mouseY)) {
            list.add(Component.translatable(Utils.getFormattedNumber(menu.getEnergy()) + " / " + Utils.getFormattedNumber(menu.getMaxEnergy())+" FE"));
            gui.renderTooltip(font, list, Optional.empty(), mouseX, mouseY);
        }
	}

	public boolean isHoveringButton(ExtendedButton button, int mouseX, int mouseY) {
		return mouseX >= button.getX() && mouseX <= button.getX() + button.getWidth() && mouseY >= button.getY() && mouseY <= button.getY() + button.getHeight();
	}

	private void drawStatsPanel(GuiGraphics gui, GummiShipEntity.ShipStats stats) {
		String effectiveSpeed = df.format(stats.getEffectiveSpeed());
		if (effectiveSpeed.equals("NaN")) {
			effectiveSpeed = "0";
		}

		String[][] rows = {
				{Utils.translateToLocal("container.gummi_hangar.power"), String.valueOf((int) stats.speed())},
				{Utils.translateToLocal("container.gummi_hangar.effectivespeed"), effectiveSpeed},
				{Utils.translateToLocal("container.gummi_hangar.mobility"), String.valueOf(stats.mobility())},
				{Utils.translateToLocal("container.gummi_hangar.weight"), String.valueOf(stats.weight())},
				{Utils.translateToLocal("container.gummi_hangar.armor"), String.valueOf(stats.armour())},
				{Utils.translateToLocal("container.gummi_hangar.firepower"), String.valueOf(stats.firepower().size())},
				{Utils.translateToLocal("container.gummi_hangar.seats"), String.valueOf(stats.passengerSlots().size())}
		};

		boolean fitsLeft = leftPos - PANEL_GAP_LEFT - PANEL_WIDTH >= 0;
		int x = fitsLeft ? -PANEL_GAP_LEFT - PANEL_WIDTH : imageWidth + PANEL_GAP_RIGHT;
		int y = STATS_TOP;
		int height = PANEL_PADDING * 2 + rows.length * (font.lineHeight + 2) - 2;

		gui.fill(x, y, x + PANEL_WIDTH, y + height, 0xC0000000);
		gui.renderOutline(x, y, PANEL_WIDTH, height, 0xFF555555);

		int line = y + PANEL_PADDING;
		for (String[] row : rows) {
			gui.drawString(font, row[0], x + PANEL_PADDING, line, 0xA0A0A0, false);
			gui.drawString(font, row[1], x + PANEL_WIDTH - PANEL_PADDING - font.width(row[1]), line, 0xFFFFFF, false);
			line += font.lineHeight + 2;
		}
	}

	@Override
	protected void renderLabels(@NotNull GuiGraphics gui, int mouseX, int mouseY) {
		BlockState hangar = minecraft.level.getBlockState(menu.TE.getBlockPos());
		int size = GummiHangarBlock.getSize(hangar.getValue(GummiHangarBlock.LEVEL));
        String tier = Utils.getHangarSizeFromLevel(hangar.getValue(GummiHangarBlock.LEVEL));
		gui.drawString(font, Component.translatable(this.title.getString()).getString()+" "+tier+" ["+size+"x"+size+"x"+size+"]", 8.0F, 6.0F, 4210752, false);
		gui.drawString(font, this.playerInventoryTitle.getString(), 8F, (float) (this.imageHeight - 94), 4210752, false);
		updateShip();
		if(structure != null){
			drawStatsPanel(gui, Utils.getShipStats(structure));

			BlockPos origin = menu.TE.getBlockPos();
			ItemStack stack = menu.TE.inventory.get().getStackInSlot(0);
			imp.active = GummiShipBlueprintItem.isBlueprint(stack);
			exp.active = GummiShipBlueprintItem.isBlueprint(stack) && !name.getValue().equals("");

			// Loading only needs somewhere to put the ship; saving needs one that already holds a ship
			loadFile.active = GummiShipBlueprintItem.isBlueprint(stack);
			saveFile.active = loadFile.active && stack.get(ModComponents.GUMMI_STRUCTURE) != null;

			if(GummiShipBlueprintItem.isBlueprint(stack)){
				GummiStructure struct = stack.get(ModComponents.GUMMI_STRUCTURE);
				imp.active = struct != null && Utils.fitsInHangar(struct, size);
			}

            build.active = Utils.getAmountOfGummiShipsInBuildPlate(minecraft.level, origin, hangar.getValue(GummiHangarBlock.FACING), size) == 0 && !name.getValue().isEmpty();

			GummiShipEntity gummi = Utils.getGummiShipInBuildPlate(minecraft.level, origin, hangar.getValue(GummiHangarBlock.FACING), size);
			if(gummi != null) {
				GummiStructure struct = gummi.structure;
				if (!Utils.fitsInHangar(struct, size)) {
					editShip.active = false;
				}
			}

            gui.pose().pushPose();
            {
                if(menu.TE.maxBurnTime > 0) {
                    float progress = ((float) menu.TE.burnTime / menu.TE.maxBurnTime) * 14;
                    int v = (int) progress+1;
                    blit(gui, KingdomKeys.rl("textures/gui/gummi_hangar.png"), 152, 39 + 14 - v, 242, 14 - v, 14, v);
                }
            }
            gui.pose().popPose();

            gui.pose().pushPose();
            {

                float scaleX = 0.8F;
                float scaleY = 1.5F;

                float val = menu.getEnergy();
                float max = menu.getMaxEnergy();
                float fill = Mth.clamp(val / max, 0F, 1F);

                ResourceLocation tex = KingdomKeys.rl("textures/gui/hpbar.png");

                gui.pose().translate(165.5, 79, 1);
                gui.pose().scale(scaleX, scaleY, 1);

                // --- Top ---
                gui.pose().pushPose();
                {
                    gui.pose().scale(2F / 3F, 1F, 1F);
                    this.blit(gui, tex, 0, 0, 0, 72, 12, 2);
                }
                gui.pose().popPose();

                // --- Middle ---
                gui.pose().pushPose();
                {
                    gui.pose().translate(0, 1, 0);
                    gui.pose().scale(2F / 3F, 28F, 1F);
                    this.blit(gui, tex, 0, 0, 0, 74, 12, 1);
                }
                gui.pose().popPose();

                // --- Bottom ---
                gui.pose().pushPose();
                {
                    gui.pose().translate(0, 29, 0);
                    gui.pose().scale(2F / 3F, 1F, 1F);
                    this.blit(gui, tex, 0, 0, 0, 72, 12, 2);
                }
                gui.pose().popPose();

                gui.pose().pushPose();
                {
                    float barHeight = 29F;
                    float scaledHeight = barHeight * fill;

                    gui.pose().translate(0, 30 - scaledHeight, 0);
                    gui.pose().scale(2F/3F, scaledHeight, 1F);
                    this.blit(gui, tex, 0, 0, 0, 78, 12, 1);
                }
                gui.pose().popPose();
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

    private boolean isHoveringEnergyBar(double mouseX, double mouseY) {
        final float BAR_OFFSET_X = 165.5F;
        final float BAR_OFFSET_Y = 79F;

        final float BAR_WIDTH  = 12F;
        final float BAR_HEIGHT = 32F; // top(2) + mid(28) + bottom(2)

        final float scaleX = 0.8F;
        final float scaleY = 1.5F;

        double originX = leftPos + BAR_OFFSET_X;
        double originY = topPos  + BAR_OFFSET_Y;

        double localX = (mouseX - originX) / scaleX;
        double localY = (mouseY - originY) / scaleY;

        return localX >= 0 && localX <= BAR_WIDTH && localY >= 0 && localY <= BAR_HEIGHT;
    }


}

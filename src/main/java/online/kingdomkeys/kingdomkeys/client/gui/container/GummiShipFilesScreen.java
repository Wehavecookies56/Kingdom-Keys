package online.kingdomkeys.kingdomkeys.client.gui.container;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.NbtIo;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.lib.GummiStructure;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSLoadGummiShipFile;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class GummiShipFilesScreen extends MenuBackground {

	private static final int CELL = 64;
	private static final int GAP = 4;
	private static final int COLUMNS = 4;
	private static final int ROWS = 3;
	private static final int LABEL = 10;

	/** Room for the ship to spin inside its cell without clipping the corners */
	private static final float FIT = 0.75F;
	private static final long SPIN_PERIOD = 9000L;
	/** Where a ship rests when it isn't the hovered one: a three quarter view showing front, side and top */
	private static final float RESTING_ANGLE = 45F;

	private static final long MAX_UNPACKED = 2097152L;

	private final Screen parent;
	private final int containerID;
	private final List<Saved> ships = new ArrayList<>();
	private int scrollRow;

	public GummiShipFilesScreen(Screen parent, int containerID) {
		super("container.gummi_hangar.load_file", Color.CYAN);
		this.parent = parent;
		this.containerID = containerID;
		this.shouldCloseOnMenu = true;
	}

	@Override
	public void init() {
		super.init();
		ships.clear();
		Path folder = ClientUtils.gummiShipFolder();

		if (!Files.isDirectory(folder)) {
			return;
		}

		try (Stream<Path> files = Files.list(folder)) {
			files.filter(file -> file.getFileName().toString().endsWith(".nbt")).sorted().forEach(this::read);
		} catch (IOException e) {
			KingdomKeys.LOGGER.error("Could not list saved gummi ships", e);
		}
	}

	private void read(Path file) {
		try {
			byte[] data = Files.readAllBytes(file);
			CompoundTag tag = NbtIo.readCompressed(new ByteArrayInputStream(data), NbtAccounter.create(MAX_UNPACKED));
			GummiStructure structure = new GummiStructure(minecraft.level.registryAccess(), tag);

			String name = file.getFileName().toString().replaceFirst("\\.nbt$", "");
			ships.add(new Saved(name, data, structure, shell(structure)));
		} catch (Exception e) {
			KingdomKeys.LOGGER.warn("Skipping unreadable gummi ship file {}", file, e);
		}
	}

	private static List<BlockPos> shell(GummiStructure structure) {
		BlockState[][][] blocks = structure.getBlocks();
		List<BlockPos> visible = new ArrayList<>();

		for (int x = 0; x < structure.getWidth(); x++) {
			for (int y = 0; y < structure.getHeight(); y++) {
				for (int z = 0; z < structure.getDepth(); z++) {
					if (!present(blocks, x, y, z)) {
						continue;
					}

					// Ignore buried blocks to make render lighter (only ignores solid ones)
					boolean buried = opaque(blocks, x - 1, y, z) && opaque(blocks, x + 1, y, z)
							&& opaque(blocks, x, y - 1, z) && opaque(blocks, x, y + 1, z)
							&& opaque(blocks, x, y, z - 1) && opaque(blocks, x, y, z + 1);

					if (!buried) {
						visible.add(new BlockPos(x, y, z));
					}
				}
			}
		}

		return visible;
	}

	private static BlockState at(BlockState[][][] blocks, int x, int y, int z) {
		if (x < 0 || y < 0 || z < 0 || x >= blocks.length || y >= blocks[x].length || z >= blocks[x][y].length) {
			return null;
		}
		return blocks[x][y][z];
	}

	private static boolean present(BlockState[][][] blocks, int x, int y, int z) {
		BlockState state = at(blocks, x, y, z);
		return state != null && !state.isAir();
	}

	private static boolean opaque(BlockState[][][] blocks, int x, int y, int z) {
		BlockState state = at(blocks, x, y, z);
		return state != null && !state.isAir() && state.canOcclude();
	}

	@Override
	public void render(@NotNull GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
		drawMenuBackground(gui, mouseX, mouseY, partialTicks);
		gui.drawCenteredString(font, title, width / 2, gridTop() - 24, 0xFFFFFF);

		if (ships.isEmpty()) {
			gui.drawCenteredString(font, Component.translatable("container.gummi_hangar.no_saved_ships"), width / 2, height / 2, 0xA0A0A0);
			return;
		}

		int first = scrollRow * COLUMNS;
		float spin = (System.currentTimeMillis() % SPIN_PERIOD) / (float) SPIN_PERIOD * 360F;

		for (int slot = 0; slot < COLUMNS * ROWS && first + slot < ships.size(); slot++) {
			Saved ship = ships.get(first + slot);
			int x = cellX(slot);
			int y = cellY(slot);
			boolean hovered = hovering(mouseX, mouseY, x, y);

			gui.fill(x, y, x + CELL, y + CELL + LABEL, hovered ? 0x50FFFFFF : 0x60000000);
			gui.renderOutline(x, y, CELL, CELL + LABEL, hovered ? 0xFFFFFFFF : 0xFF404040);

			renderShip(gui, ship, x, y, hovered ? spin : RESTING_ANGLE);

			// Which hangar it needs and how big it is, which is what decides whether it can be loaded at all
			gui.drawCenteredString(font, size(ship.structure()), x + CELL / 2, y + 2, 0xC0C0C0);
			ClientUtils.drawScrollingString(gui, font, Component.literal(ship.name()), x + 2, x + CELL - 2, y + CELL + 1, 0xFFFFFF, true);
		}

		if (ships.size() > COLUMNS * ROWS) {
			int lastRow = (ships.size() - 1) / COLUMNS;
			gui.drawCenteredString(font, Component.literal((scrollRow + 1) + " / " + (lastRow + 1)), width / 2, gridTop() + ROWS * (CELL + LABEL + GAP) + 4, 0xA0A0A0);
		}
	}

	private static String size(GummiStructure structure) {
		int side = Math.max(structure.getWidth(), Math.max(structure.getHeight(), structure.getDepth()));
		int level = Math.max(0, Mth.ceil((side - 5) / 2F));

		return "Lv" + (level + 1) + " " + side + "³";
	}

	private void renderShip(GuiGraphics gui, Saved ship, int x, int y, float spin) {
		GummiStructure structure = ship.structure();
		BlockState[][][] blocks = structure.getBlocks();

		int width = structure.getWidth();
		int height = structure.getHeight();
		int depth = structure.getDepth();
		float scale = CELL * FIT / Math.max(width, Math.max(height, depth));

		PoseStack pose = gui.pose();
		pose.pushPose();
		{
			pose.translate(x + CELL / 2F, y + CELL / 2F, 100F);
			pose.scale(scale, -scale, scale);
			pose.mulPose(Axis.XP.rotationDegrees(30F));
			pose.mulPose(Axis.YP.rotationDegrees(spin));
			pose.translate(-width / 2F, -height / 2F, -depth / 2F);

			Lighting.setupFor3DItems();
			MultiBufferSource.BufferSource buffers = gui.bufferSource();

			for (BlockPos at : ship.visible()) {
				pose.pushPose();
				pose.translate(at.getX(), at.getY(), at.getZ());
				minecraft.getBlockRenderer().renderSingleBlock(blocks[at.getX()][at.getY()][at.getZ()], pose, buffers, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, ModelData.EMPTY, Sheets.translucentCullBlockSheet());
				pose.popPose();
			}

			gui.flush();
		}
		pose.popPose();
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		int first = scrollRow * COLUMNS;

		for (int slot = 0; slot < COLUMNS * ROWS && first + slot < ships.size(); slot++) {
			if (hovering((int) mouseX, (int) mouseY, cellX(slot), cellY(slot))) {
				choose(ships.get(first + slot));
				return true;
			}
		}

		return super.mouseClicked(mouseX, mouseY, button);
	}

	private void choose(Saved ship) {
		if (ship.data().length > CSLoadGummiShipFile.MAX_BYTES) {
			minecraft.player.displayClientMessage(Component.translatable(Strings.WarningFileTooBig), false);
			return;
		}

		PacketHandler.sendToServer(new CSLoadGummiShipFile(ship.name(), ship.data(), containerID));
		minecraft.setScreen(parent);
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double deltaX, double deltaY) {
		int lastRow = ships.isEmpty() ? 0 : (ships.size() - 1) / COLUMNS;
		scrollRow = Mth.clamp(scrollRow - (int) Math.signum(deltaY), 0, Math.max(0, lastRow - ROWS + 1));
		return true;
	}

	@Override
	public void onClose() {
		minecraft.setScreen(parent);
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	private int gridLeft() {
		return (width - (COLUMNS * CELL + (COLUMNS - 1) * GAP)) / 2;
	}

	private int gridTop() {
		return (int) topBarHeight + 10;
	}

	private int cellX(int slot) {
		return gridLeft() + (slot % COLUMNS) * (CELL + GAP);
	}

	private int cellY(int slot) {
		return gridTop() + (slot / COLUMNS) * (CELL + LABEL + GAP);
	}

	private boolean hovering(int mouseX, int mouseY, int x, int y) {
		return mouseX >= x && mouseX < x + CELL && mouseY >= y && mouseY < y + CELL + LABEL;
	}

	/** One file, kept unpacked for the preview and raw for sending on */
	private record Saved(String name, byte[] data, GummiStructure structure, List<BlockPos> visible) {
	}
}

package online.kingdomkeys.kingdomkeys.client.gui;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.block.SavePointBlock;
import online.kingdomkeys.kingdomkeys.client.ScreenshotManager;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuBackground;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.*;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.entity.block.SavepointTileEntity;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSCreateSavePoint;
import online.kingdomkeys.kingdomkeys.network.cts.CSSavePointTP;
import online.kingdomkeys.kingdomkeys.network.cts.CSToggleFavouriteSavePoint;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.kingdomkeys.kingdomkeys.world.SavePointStorage;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nullable;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class SavePointScreen extends MenuBackground {

    SavepointTileEntity tileEntity;
    public Map<UUID, Pair<SavePointStorage.SavePoint, Instant>> savePoints;
    public Map<UUID, Screenshot> savePointScreenshots = new HashMap<>();
    boolean create;

    public UUID hovered = null;

    private final int SAVE = 0, RENAME = 1, RETAKE = 2;

    int sorting = 0, ordering = 0;

    int gridLeft, gridRight;
    int favouritesSeparatorY = -1;
    private static final int SEPARATOR_GAP = 10;

    final List<SavePointFavouriteButton> favouriteButtons = new ArrayList<>();

    EditBoxLength nameField;
    MenuButton save;
    MenuScrollBar bar;
    DropDownButton sortDropDown;
    DropDownButton orderDropDown;
    CheckboxButton setGlobal;

    SavePointExtrasButton rename, retake;

    SavePointStorage.SavePointType type;

    SavePointStorage.SavePoint current;

    public SavePointScreen(SavepointTileEntity tileEntity, Map<UUID, Pair<SavePointStorage.SavePoint, Instant>> savePoints, boolean create) {
        super(create ? Strings.Gui_Save_Creation_Title : savePoints.get(tileEntity.getID()).getFirst().name(), Color.green);
        this.tileEntity = tileEntity;
        type = tileEntity.getBlockState().getValue(SavePointBlock.TIER);
        this.savePoints = savePoints;
        this.create = create;
        savePoints.forEach((uuid, savePoint) -> {
            savePointScreenshots.put(uuid, new Screenshot(Minecraft.getInstance().getTextureManager(), KingdomKeys.rl("save_points/" + uuid)));
        });
        if (!create) {
            current = savePoints.get(tileEntity.getID()).getFirst();
            if (ScreenshotManager.getScreenshotFile(current.name(), current.id()) == null) {
                ScreenshotManager.screenshot(current.name(), current.id());
            }
            loadSavePointScreenshots();
        }
        shouldCloseOnMenu = false;
    }

    public void updateSavePointsFromServer(Map<UUID, Pair<SavePointStorage.SavePoint, Instant>> savePoints) {
        if (!savePoints.containsKey(tileEntity.getID())) {
            onClose();
        }
        if (!create) {
            this.current = savePoints.get(tileEntity.getID()).getFirst();
            this.savePoints = savePoints;
            renderables.clear();
            children().clear();
            init();
            updateButtons();
            savePoints.forEach((uuid, savePoint) -> {
                savePointScreenshots.put(uuid, new Screenshot(Minecraft.getInstance().getTextureManager(), KingdomKeys.rl("save_points/" + uuid)));
            });
            loadSavePointScreenshots();
        }
    }

    private void renderScrolled(GuiGraphics gui, Renderable renderable, int mouseX, int mouseY, float partialTicks) {
        gui.enableScissor(0, (int) this.topBarHeight, width, (int) (this.topBarHeight + this.middleHeight));
        renderable.render(gui, mouseX, mouseY, partialTicks);
        gui.disableScissor();
    }

    @Override
    public void render(@NotNull GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
        if (!ScreenshotManager.isTakingScreenshot()) {
            drawMenuBackground(gui, mouseX, mouseY, partialTicks);
            boolean mouseOverCurrent = false;
            for(Renderable renderable : this.renderables) {
                if (renderable instanceof SavePointButton savePointButton) {
                    if (savePointButton.getDestination().equals(tileEntity.getID())) {
                       mouseOverCurrent = savePointButton.isMouseOverInactive(mouseX, mouseY);
                    }
                    renderScrolled(gui, renderable, mouseX, mouseY, partialTicks);
                } else if (renderable instanceof SavePointFavouriteButton || renderable == rename || renderable == retake || renderable instanceof DropDownButton) {
                } else {
                    renderable.render(gui, mouseX, mouseY, partialTicks);
                }
            }
            if (favouritesSeparatorY >= 0) {
                // Clipped and scrolled to match the buttons, so it stays glued between the two blocks.
                gui.enableScissor(0, (int) this.topBarHeight, width, (int) (this.topBarHeight + this.middleHeight));
                int lineY = favouritesSeparatorY - (bar != null ? (int) bar.scrollOffset : 0);
                gui.fill(gridLeft, lineY, gridRight, lineY + 1, 0x66FFFFFF);
                gui.disableScissor();
            }

            // Manually render after due to Z issues
            if (rename != null) {
                rename.visible = mouseOverCurrent;
            }
            if (retake != null) {
                retake.visible = mouseOverCurrent;
            }
            for(Renderable renderable : this.renderables) {
                if (renderable instanceof SavePointFavouriteButton favouriteButton) {
                    favouriteButton.visible = favouriteButton.getDestination().equals(hovered) || isFavourite(favouriteButton.getDestination());
                    renderScrolled(gui, favouriteButton, mouseX, mouseY, partialTicks);
                } else if (renderable == rename || renderable == retake) {
                    renderScrolled(gui, renderable, mouseX, mouseY, partialTicks);
                }
            }

            if (orderDropDown != null && sortDropDown != null) {
                orderDropDown.render(gui, mouseX, mouseY, partialTicks);
                gui.pose().translate(0, 0, 1);
                sortDropDown.render(gui, mouseX, mouseY, partialTicks);
            }
        }
        if (create) {
            String text = Utils.translateToLocal(Strings.Gui_Save_Creation_Prompt);
            gui.drawString(minecraft.font, text, (width/2) - (minecraft.font.width(text)/2), (height/2) - (minecraft.font.lineHeight/2) - 60, Color.WHITE.getRGB());
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (create && save.active && keyCode == GLFW.GLFW_KEY_ENTER) {
            action(SAVE);
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void onClose() {
        savePointScreenshots.forEach((uuid, screenshot) -> {
            screenshot.close();
        });
        super.onClose();
    }

    private void action(int id) {
        switch (id) {
            case SAVE -> {
                if (!nameField.getValue().isEmpty()) {
                    if (savePointScreenshots.containsKey(tileEntity.getID())) {
                        File currentName = getSavePointScreenshots().get(tileEntity.getID());
                        if (currentName != null) {
                            if (currentName.delete()) {
                                KingdomKeys.LOGGER.info("Deleted save point screenshot: {}", currentName.getName());
                            } else {
                                KingdomKeys.LOGGER.error("Failed to delete save point screenshot: {}", currentName.getName());
                            }
                        }
                    }
                    Player player = Minecraft.getInstance().player;
                    PacketHandler.sendToServer(new CSCreateSavePoint(tileEntity, nameField.getValue(), player, setGlobal.isChecked()));
                    create = false;
                    nameField.visible = false;
                    save.visible = false;
                    setGlobal.visible = ModConfigs.SERVER.savepointGlobal.get() || minecraft.player.isCreative();
                    ScreenshotManager.screenshot(nameField.getValue(), tileEntity.getID());
                    title = Component.literal(nameField.getValue());
                }
            }
            case RENAME -> {
                create = true;
                sortDropDown = null;
                renderables.clear();
                children().clear();
                init();
            }
            case RETAKE -> ScreenshotManager.screenshot(savePoints.get(tileEntity.getID()).getFirst().name(), tileEntity.getID());
        }
        updateButtons();
    }

    private void updateButtons() {
        if (create) {
            save.active = !nameField.getValue().isEmpty();
        }
    }

    public void clickSavePoint(UUID id) {
        PacketHandler.sendToServer(new CSSavePointTP(tileEntity.getID(), id));
        onClose();
    }

    public void loadSavePointScreenshots() {
        long timeStarted = System.currentTimeMillis();
        KingdomKeys.LOGGER.debug("Loading screenshots...");
        Map<UUID, File> files = getSavePointScreenshots();
        KingdomKeys.LOGGER.debug("Got files in {}ms", System.currentTimeMillis() - timeStarted);
        if (!files.isEmpty()) {
            files.forEach((uuid, file) -> {
                try (InputStream inputStream = Files.newInputStream(file.toPath())) {
                    long timeStartedReading = System.currentTimeMillis();
                    savePointScreenshots.get(uuid).upload(NativeImage.read(inputStream));
                    KingdomKeys.LOGGER.debug("Read image for {} in {}ms", file.getName(), System.currentTimeMillis() - timeStartedReading);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
        KingdomKeys.LOGGER.debug("Screenshots loaded in {}ms", System.currentTimeMillis() - timeStarted);
    }

    Map<UUID, File> getSavePointScreenshots() {
        Map<UUID, File> fileMap = new HashMap<>();
        Path screenshotsDir = Paths.get(Minecraft.getInstance().gameDirectory.getPath(), "kingdomkeys/save_points/");
        if (!savePoints.isEmpty() && Files.exists(screenshotsDir)) {
            File[] fileArray = screenshotsDir.toFile().listFiles();
            if (fileArray != null && fileArray.length > 0) {
                Map<UUID, File> files = Arrays.stream(fileArray).collect(Collectors.toMap(file -> {
                    String fileName = file.getName();
                    int uuidIndex = fileName.lastIndexOf("_")+1;
                    String uuidString = fileName.substring(uuidIndex, fileName.length()-4);
                    return UUID.fromString(uuidString);
                }, file -> file, (file, file2) -> {
                    String[] fileNameSplit = file.getName().split("_");
                    UUID uuid = UUID.fromString(fileNameSplit[1].substring(0, fileNameSplit[1].length()-4)); //duplicate was found so uuid will be the same for both files
                    String savePointName = savePoints.get(uuid).getFirst().name();
                    //only need to check the name for one of them
                    File toDelete = savePointName.equals(fileNameSplit[0]) ? file2 : file;
                    if (toDelete.delete()) {
                        KingdomKeys.LOGGER.info("Deleted old save point screenshot under previous name {}", toDelete.getName());
                    }
                    return !savePointName.equals(fileNameSplit[0]) ? file2 : file;
                }));
                if (!files.isEmpty()) {
                    files.forEach((uuid, file) -> {
                        if (savePoints.containsKey(uuid)) {
                            String nameNoInvalid = savePoints.get(uuid).getFirst().name().replaceAll("[\\\\/:*?\"<>|]", "_").toLowerCase();
                            if (file.isFile() && file.getName().equals(nameNoInvalid + "_" + uuid.toString() + ".png")) {
                                fileMap.put(uuid, file);
                            }
                        }
                    });
                }
            }
        }
        return fileMap;
    }

    @Override
    public void init() {
        super.init();
        if (create) {
            addRenderableWidget(nameField = new EditBoxLength(Minecraft.getInstance().font, (width/2) - 50, (height/2) - 10 - 40, 100, 20, 32, Component.empty()){
                @Override
                public boolean charTyped(char pCodePoint, int pModifiers) {
                    boolean b = super.charTyped(pCodePoint, pModifiers);
                    updateButtons();
                    return b;
                }

                @Override
                public void deleteChars(int pNum) {
                    super.deleteChars(pNum);
                    updateButtons();
                }
            });
            addRenderableWidget(setGlobal = new CheckboxButton((width/2) - 60, (height/2) - 14, Strings.Gui_Save_Creation_Global, false, Strings.Gui_Save_Creation_Global_Desc, Color.WHITE.getRGB()));
            addRenderableWidget(save = new MenuButton((width/2) - 60, (height/2), 100, Utils.translateToLocal(Strings.Gui_Save_Creation_Accept), MenuButton.ButtonType.BUTTON, press -> action(SAVE)));
            //Will be visible if the config is enabled or if the player is in creative
            setGlobal.visible = ModConfigs.SERVER.savepointGlobal.get() || minecraft.player.isCreative();

        } else {
            init(recent, 0);
        }
        updateButtons();
    }

    final int recent = 0, name = 1, dimension = 2, owner = 3;

    public Comparator<? super Map.Entry<UUID, Pair<SavePointStorage.SavePoint, Instant>>> getSortedList(int sorting) {
        return switch (sorting) {
            case recent -> Comparator.comparing((Map.Entry<UUID, Pair<SavePointStorage.SavePoint, Instant>> uuidPairEntry) -> uuidPairEntry.getValue().getSecond()).reversed();
            case name -> Comparator.comparing((Map.Entry<UUID, Pair<SavePointStorage.SavePoint, Instant>> uuidPairEntry) -> uuidPairEntry.getValue().getFirst().name().toLowerCase()).thenComparing((Map.Entry<UUID, Pair<SavePointStorage.SavePoint, Instant>> uuidPairEntry) -> uuidPairEntry.getValue().getFirst().name());
            case dimension -> Comparator.comparing(uuidPairEntry -> uuidPairEntry.getValue().getFirst().dimension());
            case owner -> Comparator.comparing(uuidPairEntry -> uuidPairEntry.getValue().getFirst().owner().getSecond());
            default -> Map.Entry.comparingByKey();
        };
    }

    public void init(int sorting, int ordering) {
        int elementHeight = (font.lineHeight * 5) + 4;
        int elementWidth = (int) (elementHeight * (16F/9F));
        int maxRowWidth = (int) (width/1.5F);
        int elementsPerRow = (int) Math.max(1, (float) maxRowWidth/(elementWidth+2));

        favouriteButtons.clear();
        gridLeft = (width / 2) - (((elementWidth + 2) * elementsPerRow) / 2);
        gridRight = gridLeft + ((elementWidth + 2) * elementsPerRow) - 2;

        Comparator<? super Map.Entry<UUID, Pair<SavePointStorage.SavePoint, Instant>>> comparator = getSortedList(sorting);
        comparator = ordering == 0 ? comparator : comparator.reversed();
        List<UUID> sortedList = savePoints.entrySet().stream().filter(uuidPairEntry -> !uuidPairEntry.getKey().equals(tileEntity.getID())).sorted(comparator).map(Map.Entry::getKey).collect(Collectors.toList());
        sortedList.add(0, tileEntity.getID());

        // Fav. are shown on top.
        List<UUID> pinned = sortedList.stream().filter(this::isFavourite).collect(Collectors.toList());
        List<UUID> rest = sortedList.stream().filter(uuid -> !isFavourite(uuid)).collect(Collectors.toList());

        favouritesSeparatorY = -1;
        int bottom = layoutBlock(pinned, 0, elementsPerRow, elementWidth, elementHeight);
        if (bottom > 0) {
            // Only worth a divider if something was actually laid out above it.
            favouritesSeparatorY = (int) (topBarHeight + 2 + bottom + (SEPARATOR_GAP / 2));
            bottom += SEPARATOR_GAP;
        }
        bottom = layoutBlock(rest, bottom, elementsPerRow, elementWidth, elementHeight);

        addRenderableWidget(sortDropDown = new DropDownButton(gridLeft - 62, (int) topBarHeight, 60, font.lineHeight, List.of(Component.translatable(Strings.Gui_Save_Sorting_ByRecent), Component.translatable(Strings.Gui_Save_Sorting_ByName), Component.translatable(Strings.Gui_Save_Sorting_ByDimension), Component.translatable(Strings.Gui_Save_Sorting_ByOwner)), Component.translatable(Strings.Gui_Save_Main_Sort)));
        addRenderableWidget(orderDropDown = new DropDownButton(gridLeft - 72, (int) topBarHeight + font.lineHeight + 4, 70, font.lineHeight, List.of(Component.translatable(Strings.Gui_Save_Sorting_Ascending), Component.translatable(Strings.Gui_Save_Sorting_Descending)), Component.empty()));
        sortDropDown.setSelected(sorting);
        orderDropDown.setSelected(ordering);
        this.sorting = sorting;
        this.ordering = ordering;
        addRenderableWidget(bar = new MenuScrollBar(gridLeft + ((elementWidth + 2) * elementsPerRow), (int) topBarHeight, (int) (topBarHeight + middleHeight), (int) middleHeight, bottom + 2, true));
    }

    /**
     * Individual block of savepoints (either fav. or non fav)
     * @param ids
     * @param yOffset
     * @param elementsPerRow
     * @param elementWidth
     * @param elementHeight
     * @return
     */
    private int layoutBlock(List<UUID> ids, int yOffset, int elementsPerRow, int elementWidth, int elementHeight) {
        int column = 0;
        int row = 0;
        boolean any = false;

        for (UUID uuid : ids) {
            SavePointStorage.SavePoint savePoint = savePoints.get(uuid).getFirst();
            if (!((type == SavePointStorage.SavePointType.WARP && savePoint.type() == SavePointStorage.SavePointType.WARP) || savePoint.dimension() == tileEntity.getLevel().dimension())) {
                continue;
            }

            if (column == elementsPerRow) {
                column = 0;
                row++;
            }

            int yPos = (int) (this.topBarHeight + 2 + yOffset + ((elementHeight + 2) * row));
            int xPos = gridLeft + ((elementWidth + 2) * column);

            SavePointButton button = new SavePointButton(this, xPos, yPos, elementWidth, elementHeight, Component.literal(savePoint.name()), uuid);

            int starWidth = SavePointFavouriteButton.width();
            if (uuid.equals(tileEntity.getID())) {
                button.active = false;
                int extrasX = xPos + starWidth;
                int bwidth = elementWidth - starWidth;
                int retakeOffset = 0;
                if (current.owner().getFirst().equals(Minecraft.getInstance().player.getUUID())) {
                    bwidth /= 2;
                    retakeOffset = bwidth + 1;
                    addRenderableWidget(rename = new SavePointExtrasButton(extrasX, yPos, bwidth, Component.translatable(Strings.Gui_Save_Main_Rename), pButton -> action(RENAME)));
                }
                addRenderableWidget(retake = new SavePointExtrasButton(extrasX + retakeOffset, yPos, bwidth, Component.translatable(Strings.Gui_Save_Main_Retake), pButton -> action(RETAKE)));
            }

            SavePointFavouriteButton favourite = new SavePointFavouriteButton(this, xPos, yPos, uuid);
            favouriteButtons.add(favourite);
            addRenderableWidget(favourite);

            addRenderableWidget(button);
            column++;
            any = true;
        }

        return any ? yOffset + ((elementHeight + 2) * (row + 1)) : yOffset;
    }

    public boolean isFavourite(UUID id) {
        PlayerData playerData = PlayerData.get(Minecraft.getInstance().player);
        return playerData != null && playerData.isFavouriteSavePoint(id);
    }

    public void toggleFavourite(UUID id) {
        PlayerData playerData = PlayerData.get(Minecraft.getInstance().player);
        if (playerData == null) {
            return;
        }

        // Applied locally first so the list reshuffles on the click rather than on the sync coming back.
        playerData.toggleFavouriteSavePoint(id);
        PacketHandler.sendToServer(new CSToggleFavouriteSavePoint(id));

        renderables.clear();
        children().clear();
        init(sorting, ordering);
        updateScroll(bar);
    }

    public void updateScroll(MenuScrollBar bar) {
        if (bar == null) {
            return;
        }
        for (Renderable renderable : this.renderables) {
            if (renderable instanceof SavePointButton button) {
                button.offsetY = (int) bar.scrollOffset;
            }
            if (renderable instanceof SavePointFavouriteButton favouriteButton) {
                favouriteButton.offsetY = (int) bar.scrollOffset;
            }
        }
        if (rename != null) {
            rename.offsetY = (int) bar.scrollOffset;
        }
        if (retake != null) {
            retake.offsetY = (int) bar.scrollOffset;
        }
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if (bar != null) {
            bar.mouseClicked(pMouseX, pMouseY, pButton);
        }
        if (sortDropDown != null && orderDropDown != null) {
            if (sortDropDown.isOpen() && !orderDropDown.isOpen()) {
                return sortDropDown.mouseClicked(pMouseX, pMouseY, pButton);
            }
            if (!sortDropDown.isOpen() && orderDropDown.isOpen()) {
                return orderDropDown.mouseClicked(pMouseX, pMouseY, pButton);
            }
        }
        if ((pMouseY > topBarHeight + middleHeight || pMouseY < topBarHeight) && !create) {
            return false;
        } else {
            // Pins get first refusal. They sit inside their own tile, and the tile's click warps you
            // away, so whichever of the two the widget list happens to reach first would decide it.
            // Copied because toggling rebuilds the list from under us.
            for (SavePointFavouriteButton favourite : List.copyOf(favouriteButtons)) {
                if (favourite.visible && favourite.active && favourite.mouseClicked(pMouseX, pMouseY, pButton)) {
                    return true;
                }
            }
            return super.mouseClicked(pMouseX, pMouseY, pButton);
        }
    }

    @Override
    public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
        if (bar != null) {
            bar.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
            updateScroll(bar);
        }
        return super.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
    }

    @Override
    public boolean mouseReleased(double pMouseX, double pMouseY, int pButton) {
        if (bar != null) {
            bar.mouseReleased(pMouseX, pMouseY, pButton);
        }
        if (sortDropDown != null && orderDropDown != null) {
            orderDropDown.active = !sortDropDown.isOpen();
            sortDropDown.active = !orderDropDown.isOpen();
            if (sortDropDown.getSelected() != sorting || orderDropDown.getSelected() != ordering) {
                renderables.clear();
                children().clear();
                init(sortDropDown.getSelected(), orderDropDown.getSelected());
            }
        }
        return super.mouseReleased(pMouseX, pMouseY, pButton);
    }

    @Override
    public boolean mouseScrolled(double pMouseX, double pMouseY, double deltaX, double deltaY) {
        if (bar != null) {
            bar.mouseScrolled(pMouseX, pMouseY, deltaX, deltaY);
            updateScroll(bar);
        }
        return super.mouseScrolled(pMouseX, pMouseY, deltaX, deltaY);
    }

    public static class Screenshot implements AutoCloseable {
        private static final ResourceLocation MISSING_LOCATION = null;
        private final TextureManager textureManager;
        private final ResourceLocation textureLocation;

        @Nullable
        private DynamicTexture texture;
        private boolean closed;
        private Screenshot(TextureManager pTextureManager, ResourceLocation pTextureLocation) {
            this.textureManager = pTextureManager;
            this.textureLocation = pTextureLocation;
        }

        public void upload(NativeImage pImage) {
            if (pImage.getWidth() % 16 == 0 && pImage.getHeight() % 9 == 0) {
                try {
                    this.checkOpen();
                    if (this.texture == null) {
                        this.texture = new DynamicTexture(pImage);
                    } else {
                        this.texture.setPixels(pImage);
                        this.texture.upload();
                    }

                    this.textureManager.register(this.textureLocation, this.texture);
                } catch (Throwable throwable) {
                    pImage.close();
                    this.clear();
                    throw throwable;
                }
            } else {
                pImage.close();
                throw new IllegalArgumentException("Icon must be " + ScreenshotManager.width + "x" + ScreenshotManager.height + ", but was " + pImage.getWidth() + "x" + pImage.getHeight());
            }
        }

        public void clear() {
            this.checkOpen();
            if (this.texture != null) {
                this.textureManager.release(this.textureLocation);
                this.texture.close();
                this.texture = null;
            }

        }

        public ResourceLocation textureLocation() {
            return this.texture != null ? this.textureLocation : MISSING_LOCATION;
        }

        public void close() {
            this.clear();
            this.closed = true;
        }

        private void checkOpen() {
            if (this.closed) {
                throw new IllegalStateException("Icon already closed");
            }
        }
    }
}

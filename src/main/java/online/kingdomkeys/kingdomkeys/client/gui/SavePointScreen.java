package online.kingdomkeys.kingdomkeys.client.gui;

import com.mojang.blaze3d.platform.NativeImage;
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
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.EditBoxLength;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuScrollBar;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.SavePointButton;
import online.kingdomkeys.kingdomkeys.entity.block.SavepointTileEntity;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSCreateSavePoint;
import online.kingdomkeys.kingdomkeys.network.cts.CSSavePointTP;
import online.kingdomkeys.kingdomkeys.world.SavePointStorage;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import javax.annotation.Nullable;

public class SavePointScreen extends MenuBackground {

    SavepointTileEntity tileEntity;
    public Map<UUID, SavePointStorage.SavePoint> savePoints;
    public Map<UUID, Screenshot> savePointScreenshots = new HashMap<>();
    boolean create;

    public UUID hovered = null;

    private final int SAVE = 0;

    EditBoxLength nameField;
    MenuButton save;
    MenuScrollBar bar;
    float scrollOffset;

    SavePointStorage.SavePointType type;

    public SavePointScreen(SavepointTileEntity tileEntity, Map<UUID, SavePointStorage.SavePoint> savePoints, boolean create) {
        super("", Color.green);
        this.tileEntity = tileEntity;
        type = ((SavePointBlock)tileEntity.getBlockState().getBlock()).getType();
        this.savePoints = savePoints;
        this.create = create;
        savePoints.forEach((uuid, savePoint) -> {
            savePointScreenshots.put(uuid, new Screenshot(Minecraft.getInstance().textureManager, new ResourceLocation(KingdomKeys.MODID, "save_points/" + uuid)));
        });
        if (!create) {
            loadSavePointScreenshots();
        }
    }

    public void updateSavePointsFromServer(boolean tileEntityExists, Map<UUID, SavePointStorage.SavePoint> savePoints) {
        if (!tileEntityExists) {
            onClose();
        }
        this.savePoints = savePoints;
        init();
        updateButtons();
        savePoints.forEach((uuid, savePoint) -> {
            savePointScreenshots.put(uuid, new Screenshot(Minecraft.getInstance().textureManager, new ResourceLocation(KingdomKeys.MODID, "save_points/" + uuid)));
        });
        loadSavePointScreenshots();
    }

    @Override
    public void render(@NotNull GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
        if (!ScreenshotManager.isTakingScreenshot()) {
            drawMenuBackground(gui, mouseX, mouseY, partialTicks);
            for(Renderable renderable : this.renderables) {
                if (renderable instanceof SavePointButton) {
                    gui.enableScissor(0, (int) this.topBarHeight, width, (int) (this.topBarHeight + this.middleHeight));
                    renderable.render(gui, mouseX, mouseY, partialTicks);
                    gui.disableScissor();
                } else {
                    renderable.render(gui, mouseX, mouseY, partialTicks);
                }
            }
        }
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
                    Player player = Minecraft.getInstance().player;
                    PacketHandler.sendToServer(new CSCreateSavePoint(tileEntity, nameField.getValue(), player));
                    create = false;
                    nameField.visible = false;
                    save.visible = false;
                    ScreenshotManager.screenshot(nameField.getValue(), tileEntity.getID());
                }
            }
        }
        updateButtons();
    }

    private void updateButtons() {
        if (create) {
            if (nameField.getValue().isEmpty()) {
                save.active = false;
            } else {
                save.active = true;
            }
        } else {

        }
    }

    public void clickSavePoint(UUID id) {
        PacketHandler.sendToServer(new CSSavePointTP(tileEntity.getID(), id));
        onClose();
    }

    public void loadSavePointScreenshots() {
        long timeStarted = System.currentTimeMillis();
        KingdomKeys.LOGGER.info("Loading screenshots...");
        Map<UUID, File> files = getSavePointScreenshots();
        KingdomKeys.LOGGER.info("Got files in {}ms", System.currentTimeMillis() - timeStarted);
        if (!files.isEmpty()) {
            files.forEach((uuid, file) -> {
                try (InputStream inputStream = Files.newInputStream(file.toPath())) {
                    long timeStartedReading = System.currentTimeMillis();
                    savePointScreenshots.get(uuid).upload(NativeImage.read(inputStream));
                    KingdomKeys.LOGGER.info("Read image for {} in {}ms", file.getName(), System.currentTimeMillis() - timeStartedReading);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
        KingdomKeys.LOGGER.info("Screenshots loaded in {}ms", System.currentTimeMillis() - timeStarted);
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
                }, file -> file));
                if (!files.isEmpty()) {
                    files.forEach((uuid, file) -> {
                        if (savePoints.containsKey(uuid)) {
                            String nameNoInvalid = savePoints.get(uuid).name().replaceAll("[\\\\/:*?\"<>|]", "_").toLowerCase();
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
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (nameField != null && nameField.isFocused() && keyCode != GLFW.GLFW_KEY_ESCAPE) {
            return false;
        } else {
            return super.keyPressed(keyCode, scanCode, modifiers);
        }
    }

    @Override
    public void init() {
        super.init();
        if (create) {
            addRenderableWidget(nameField = new EditBoxLength(Minecraft.getInstance().font, 0, 0, 100, 20, 32, Component.empty()){
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
            addRenderableWidget(save = new MenuButton(0, 40, 100, "Save", MenuButton.ButtonType.BUTTON, press -> action(SAVE)));
        } else {
            int elementHeight = (font.lineHeight * 5) + 4;
            int elementWidth = (int) (elementHeight * (16F/9F));
            int elementsPerRow = 3; //TODO maybe calculate how many can fit on the screen, need to leave room on left and right though
            int column = 0;
            int row = 0;
            int yPos = 0;
            for (Map.Entry<UUID, SavePointStorage.SavePoint> entry : savePoints.entrySet()) {
                if (type == SavePointStorage.SavePointType.WARP || entry.getValue().dimension() == tileEntity.getLevel().dimension()) {
                    if (column == elementsPerRow) {
                        column = 0;
                        row++;
                    }
                    yPos = (int) (this.topBarHeight + 2 + ((elementHeight + 2) * row));
                    addRenderableWidget(new SavePointButton(this, (width/2) - (((elementWidth + 2) * elementsPerRow) / 2) + ((elementWidth + 2) * column), yPos, elementWidth, elementHeight, Component.literal(entry.getValue().name()), entry.getKey()));
                    column++;
                }
            }
            addRenderableWidget(bar = new MenuScrollBar((width/2) - (((elementWidth + 2) * elementsPerRow) / 2) + ((elementWidth + 2) * elementsPerRow), (int) topBarHeight, (int) (topBarHeight + middleHeight), (int) middleHeight, (int) (yPos - topBarHeight) + elementHeight+2));
        }
        updateButtons();
    }

    public void updateScroll(MenuScrollBar bar) {
        for (Renderable renderable : this.renderables) {
            if (renderable instanceof SavePointButton button) {
                button.offsetY = (int) bar.scrollOffset;
            }
        }
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if (bar != null) {
            bar.mouseClicked(pMouseX, pMouseY, pButton);
        }
        return super.mouseClicked(pMouseX, pMouseY, pButton);
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
        return super.mouseReleased(pMouseX, pMouseY, pButton);
    }

    @Override
    public boolean mouseScrolled(double pMouseX, double pMouseY, double pDelta) {
        if (bar != null) {
            bar.mouseScrolled(pMouseX, pMouseY, pDelta);
            updateScroll(bar);
        }
        return super.mouseScrolled(pMouseX, pMouseY, pDelta);
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

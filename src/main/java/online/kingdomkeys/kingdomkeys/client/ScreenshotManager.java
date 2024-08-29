package online.kingdomkeys.kingdomkeys.client;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderFrameEvent;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.gui.SavePointScreen;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@EventBusSubscriber(value = Dist.CLIENT, bus = EventBusSubscriber.Bus.GAME)
public class ScreenshotManager {

    private static boolean takeScreenshot = false;
    private static String name;
    private static UUID uuid;
    public static int width = 320;
    public static int height = 180;

    public static String getFileNameString(String savePointName, UUID savePointID) {
        String nameNoInvalid = savePointName.replaceAll("[\\\\/:*?\"<>|]", "_").toLowerCase();
        return nameNoInvalid + "_" + savePointID.toString() + ".png";
    }

    public static File getScreenshotFile(String savePointName, UUID savePointID) {
        Path screenshotsDir = Paths.get(Minecraft.getInstance().gameDirectory.getPath(), "kingdomkeys/save_points/");
        String fileName = ScreenshotManager.getFileNameString(savePointName, savePointID);
        File screenshotFile = new File(screenshotsDir.toFile(), fileName);
        if (screenshotFile.exists() && screenshotFile.isFile()) {
            return screenshotFile;
        } else {
            return null;
        }
    }

    public static void screenshot(String savePointName, UUID savePointID) {
        name = savePointName;
        uuid = savePointID;
        takeScreenshot = true;
        Minecraft.getInstance().options.hideGui = true;
    }

    public static boolean isTakingScreenshot() {
        return takeScreenshot;
    }

    private static void takeScreenshot(String savePointName, UUID savePointID) {
        Path p = Paths.get(Minecraft.getInstance().gameDirectory.getPath(), "kingdomkeys/save_points");
        String fileName = getFileNameString(savePointName, savePointID);
        File fileToCreate = new File(p.toFile(), fileName);
        try {
            Files.createDirectories(p);
        } catch (IOException e) {
            e.printStackTrace();
        }
        NativeImage image = Screenshot.takeScreenshot(Minecraft.getInstance().getMainRenderTarget());
        int i = image.getWidth();
        int j = image.getHeight();
        int k = 0;
        int l = 0;
        if (i > j) {
            k = (i - j) / 2;
            i = j;
        } else {
            l = (j - i) / 2;
            j = i;
        }
        NativeImage resized = new NativeImage(width, height, false);
        image.resizeSubRectTo(k, l, i, j, resized);
        try {
            resized.writeToFile(fileToCreate);
            KingdomKeys.LOGGER.info("Saved save point screenshot " + fileName);
        } catch (IOException ioexception) {
            KingdomKeys.LOGGER.warn("Couldn't save screenshot", (Throwable)ioexception);
        } finally {
            image.close();
        }
    }

    @SubscribeEvent
    public static void renderTick(RenderFrameEvent.Post event) {
        if (Minecraft.getInstance().level != null) {
            if (takeScreenshot) {
                takeScreenshot(name, uuid);
                takeScreenshot = false;
                Minecraft.getInstance().options.hideGui = false;
                if (Minecraft.getInstance().screen != null) {
                    if (Minecraft.getInstance().screen instanceof SavePointScreen savePointScreen) {
                        savePointScreen.loadSavePointScreenshots();
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void renderOverlays(RenderGuiLayerEvent.Pre event) {
        event.setCanceled(isTakingScreenshot());
    }

}

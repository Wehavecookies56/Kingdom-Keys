package online.kingdomkeys.kingdomkeys.client;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
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

@EventBusSubscriber(value = Dist.CLIENT)
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

        // Crop to the thumbnail's own aspect ratio rather than to a square. Squaring it off first and
        // then scaling that into a 16:9 thumbnail stretched everything sideways - taking the widest
        // centre cut that already has the right shape means the scale down is uniform.
        int sourceWidth = image.getWidth();
        int sourceHeight = image.getHeight();
        float targetAspect = (float) width / (float) height;

        int cropWidth, cropHeight;
        if ((float) sourceWidth / (float) sourceHeight > targetAspect) {
            // Wider than the thumbnail, so keep the full height and trim the sides.
            cropHeight = sourceHeight;
            cropWidth = Math.round(sourceHeight * targetAspect);
        } else {
            // Taller than the thumbnail, so keep the full width and trim above and below.
            cropWidth = sourceWidth;
            cropHeight = Math.round(sourceWidth / targetAspect);
        }

        int cropX = (sourceWidth - cropWidth) / 2;
        int cropY = (sourceHeight - cropHeight) / 2;

        NativeImage resized = new NativeImage(width, height, false);
        image.resizeSubRectTo(cropX, cropY, cropWidth, cropHeight, resized);
        try {
            resized.writeToFile(fileToCreate);
            KingdomKeys.LOGGER.info("Saved save point screenshot " + fileName);
        } catch (IOException ioexception) {
            KingdomKeys.LOGGER.warn("Couldn't save screenshot", ioexception);
        } finally {
            image.close();
            resized.close();
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

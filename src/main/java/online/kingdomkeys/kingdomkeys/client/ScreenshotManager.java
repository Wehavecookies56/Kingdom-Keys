package online.kingdomkeys.kingdomkeys.client;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.gui.SavePointScreen;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
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
    public static void renderTick(TickEvent.RenderTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
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

    }

    @SubscribeEvent
    public static void renderOverlays(RenderGuiOverlayEvent.Pre event) {
        event.setCanceled(isTakingScreenshot());
    }

}

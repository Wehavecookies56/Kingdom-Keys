package online.kingdomkeys.kingdomkeys.client.gui.elements;

import com.google.common.collect.Lists;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.neoforged.neoforge.common.ModConfigSpec;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.gui.overlay.HUDAnchorPosition;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;

import java.util.ArrayList;
import java.util.List;

public class HUDElement {
    public String name;
    public HUDAnchorPosition anchor;

    public float x;
    public float y;

    public float scaleX = 1f;
    public float scaleY = 1f;

    public float rotation = 0f;

    public int width;
    public int height;

    public boolean selected;

    public double[] originalValues = new double[8];

    //ModConfigSpec.ConfigValue<List<? extends Float>> configdOption;

    public HUDElement(String name){
        this.name = name;
        List<? extends Number> configOption = ModConfigs.getHUDData(name);

        for (int i = 0; i < configOption.size(); i++) {
            originalValues[i] = configOption.get(i).floatValue();
        }

        restoreDefaultValues();
    }
    public HUDElement(String name, HUDAnchorPosition anchor, float x, float y, int width, int height) {
        this.name = name;
        this.anchor = anchor;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        originalValues[0] = x;
        originalValues[1] = y;
        originalValues[2] = width;
        originalValues[3] = height;
    }

    public HUDElement setScale(float scaleX, float scaleY) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;

        originalValues[4] = scaleX;
        originalValues[5] = scaleY;
        return this;
    }

    public boolean isMouseOver(double mouseX, double mouseY) {
        int w = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        int h = Minecraft.getInstance().getWindow().getGuiScaledHeight();

        float px = getPixelX(w);
        float py = getPixelY(h);

        return mouseX >= px && mouseX <= px + width * scaleX && mouseY >= py && mouseY <= py + height * scaleY;
    }

    public void renderEditorBox(GuiGraphics guiGraphics) {
        int w = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        int h = Minecraft.getInstance().getWindow().getGuiScaledHeight();

        float px = getPixelX(w);
        float py = getPixelY(h);

        float sw = width * scaleX;
        float sh = height * scaleY;

        int x1 = (int) px;
        int y1 = (int) py;
        int x2 = (int) (px + sw);
        int y2 = (int) (py + sh);

        guiGraphics.fill(x1, y1, x2, y2, 0x55000000);

        if(selected) {
            float cellW = sw / 3f;
            float cellH = sh / 3f;

            for (int gx = 0; gx < 3; gx++) {
                for (int gy = 0; gy < 3; gy++) {

                    int cx1 = (int) (px + gx * cellW);
                    int cy1 = (int) (py + gy * cellH);
                    int cx2 = (int) (cx1 + cellW);
                    int cy2 = (int) (cy1 + cellH);

                    if (isAnchorCell(gx, gy)) {
                        guiGraphics.fill(cx1, cy1, cx2, cy2, 0xAAFF0000);
                    }
                }
            }

            /*
            for (int i = 1; i < 3; i++) {
                int vx = (int) (px + i * cellW);
                int hy = (int) (py + i * cellH);

                guiGraphics.fill(vx, y1, vx + 1, y2, 0xFFFFFFFF);
                guiGraphics.fill(x1, hy, x2, hy + 1, 0xFFFFFFFF);
            }
*/
            guiGraphics.fill(x1, y1-1, x2, y1, 0xFFFFFFFF);
            guiGraphics.fill(x1, y2 - 1, x2, y2, 0xFFFFFFFF);
            guiGraphics.fill(x1, y1, x1 + 1, y2, 0xFFFFFFFF);
            guiGraphics.fill(x2 - 1, y1, x2, y2, 0xFFFFFFFF);
        }
    }


    //Relative (gotta change HUDEditorScreen too)
    /*public float getPixelX(int screenWidth) {
        return switch (anchor) {
            case TOP_LEFT, CENTER_LEFT, BOTTOM_LEFT -> x * screenWidth;
            case TOP_RIGHT, CENTER_RIGHT, BOTTOM_RIGHT -> screenWidth - (x * screenWidth) - width * scaleX;
            case TOP_CENTER, CENTER, BOTTOM_CENTER -> (screenWidth / 2f) + (x * screenWidth) - (width * scaleX / 2f);
        };
    }

    public float getPixelY(int screenHeight) {
        return switch (anchor) {
            case TOP_LEFT, TOP_CENTER, TOP_RIGHT -> y * screenHeight;
            case BOTTOM_LEFT, BOTTOM_CENTER, BOTTOM_RIGHT -> screenHeight - (y * screenHeight) - height * scaleY;
            case CENTER_LEFT, CENTER, CENTER_RIGHT -> (screenHeight / 2f) + (y * screenHeight) - (height * scaleY / 2f);
        };
    }*/

    //Absolute
    public float getPixelX(int screenWidth) {
        return switch (anchor) {
            case TOP_LEFT, CENTER_LEFT, BOTTOM_LEFT -> x;
            case TOP_RIGHT, CENTER_RIGHT, BOTTOM_RIGHT -> screenWidth - x - width * scaleX;
            case TOP_CENTER, CENTER, BOTTOM_CENTER -> (screenWidth / 2f) + x - (width * scaleX / 2f);
        };
    }

    public float getPixelY(int screenHeight) {
        return switch (anchor) {
            case TOP_LEFT, TOP_CENTER, TOP_RIGHT -> y;
            case BOTTOM_LEFT, BOTTOM_CENTER, BOTTOM_RIGHT -> screenHeight - y - height * scaleY;
            case CENTER_LEFT, CENTER, CENTER_RIGHT -> (screenHeight / 2f) + y - (height * scaleY / 2f);
        };
    }

    public float getScaledWidth() {
        return width * scaleX;
    }

    public float getScaledHeight() {
        return height * scaleY;
    }

    /**
     * Returns if the cell corresponds with the anchor
     * @param gx
     * @param gy
     * @return
     */
    private boolean isAnchorCell(int gx, int gy) {
        return switch (anchor) {
            case TOP_LEFT -> gx == 0 && gy == 0;
            case TOP_CENTER -> gx == 1 && gy == 0;
            case TOP_RIGHT -> gx == 2 && gy == 0;

            case CENTER_LEFT -> gx == 0 && gy == 1;
            case CENTER -> gx == 1 && gy == 1;
            case CENTER_RIGHT -> gx == 2 && gy == 1;

            case BOTTOM_LEFT -> gx == 0 && gy == 2;
            case BOTTOM_CENTER -> gx == 1 && gy == 2;
            case BOTTOM_RIGHT -> gx == 2 && gy == 2;
        };
    }

    public void applyTransform(GuiGraphics guiGraphics, int screenWidth, int screenHeight, int z) {
        float px = getPixelX(screenWidth);
        float py = getPixelY(screenHeight);
/*
        float w = width * scaleX;
        float h = height * scaleY;

        float pivotX = switch (anchor) {
            case TOP_LEFT, CENTER_LEFT, BOTTOM_LEFT -> px;
            case TOP_CENTER, CENTER, BOTTOM_CENTER -> px + w / 2f;
            case TOP_RIGHT, CENTER_RIGHT, BOTTOM_RIGHT -> px + w;
        };

        float pivotY = switch (anchor) {
            case TOP_LEFT, TOP_CENTER, TOP_RIGHT -> py;
            case CENTER_LEFT, CENTER, CENTER_RIGHT -> py + h / 2f;
            case BOTTOM_LEFT, BOTTOM_CENTER, BOTTOM_RIGHT -> py + h;
        };*/

        guiGraphics.pose().pushPose();

        /*guiGraphics.pose().translate(px, py, z);
        guiGraphics.pose().mulPose(com.mojang.math.Axis.ZP.rotationDegrees(rotation));
        guiGraphics.pose().scale(scaleX, scaleY, 1);
        guiGraphics.pose().translate(-px, -py, 0);
        */
        float centerX = width * scaleX / 2f;
        float centerY = height * scaleY / 2f;

        guiGraphics.pose().translate(px + centerX, py + centerY, 0);
        guiGraphics.pose().mulPose(Axis.ZP.rotationDegrees(rotation));
        guiGraphics.pose().scale(scaleX, scaleY, 1);
        guiGraphics.pose().translate(-width / 2f, -height / 2f, 0);

    }

    public void endTransform(GuiGraphics guiGraphics) {
        guiGraphics.pose().popPose();
    }

    public void restoreDefaultValues(){
        ArrayList<Float> defaultValues = getDefaultValues(name);
        x = defaultValues.get(0);
        y = defaultValues.get(1);
        width = defaultValues.get(2).intValue();
        height = defaultValues.get(3).intValue();
        scaleX = defaultValues.get(4);
        scaleY = defaultValues.get(5);
        rotation = defaultValues.get(6);
        anchor = HUDAnchorPosition.values()[defaultValues.get(7).intValue()];
    }

    public void saveConfig(){
       /* if(configOption == null) {
            KingdomKeys.LOGGER.warn("No config option has been linked with "+name+" HUDElement");
            return;
        }*/
        KingdomKeys.LOGGER.warn("Saving config for "+name);
        List<Float> values = new ArrayList<>(List.of(x,y,(float)width,(float)height,scaleX,scaleY,rotation,(float)anchor.ordinal()));
        ModConfigs.setHUDData(name,values);
    }

    public static ArrayList<Float> getDefaultValues(String name){
        return switch(name){
            case "HP" -> Lists.newArrayList(13.8F, 3.8F, 916F, 254F,0.2F,0.2F, 0F, 8F);
            case "MP" -> Lists.newArrayList(54.2F, 8.6F, 142F, 12F,0.7F,0.5F, 0F, 8F);
            case "CM" -> Lists.newArrayList(5F, 5F, 70F, 75F, 1F,1F, 0F, 6F);
            case "Drive" -> Lists.newArrayList(53.7F, 14.6F, 95F, 18F, 0.8F, 0.8F, 0F, 8F);
            case "Focus" -> Lists.newArrayList(3F, 27.5F, 66F, 40F, 1F, 1F, 0F, 8F);

            default -> throw new IllegalStateException("Unexpected default HUD value: " + name);
        };

    }

    @Override
    public String toString() {
        return name+": x-"+x+" y-"+y+" width:"+width+" height:"+height;
    }
}
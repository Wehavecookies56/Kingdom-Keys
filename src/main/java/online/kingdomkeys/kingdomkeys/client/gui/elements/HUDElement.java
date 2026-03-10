package online.kingdomkeys.kingdomkeys.client.gui.elements;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.gui.overlay.HUDAnchorPosition;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;

import java.io.InputStreamReader;
import java.io.Reader;
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

    public float[] configValues = new float[8];

    public HUDElement(String name){
        this.name = name;
    }

    public void loadFromConfig(){
        //Apply config options
        List<? extends Number> configOption = ModConfigs.getHUDData(name);

        for (int i = 0; i < configOption.size(); i++) {
            configValues[i] = configOption.get(i).floatValue();
        }
        applyValues(configValues);
    }

    public void applyValues(float[] configValues){
        this.x = configValues[0];
        this.y = configValues[1];
        this.width = (int) configValues[2];
        this.height = (int) configValues[3];
        this.scaleX = configValues[4];
        this.scaleY = configValues[5];
        this.rotation = configValues[6];
        this.anchor = HUDAnchorPosition.values()[(int)configValues[7]];
    }

    public <T extends HUDElement> T setScale(float scaleX, float scaleY) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;

        configValues[4] = scaleX;
        configValues[5] = scaleY;
        return (T) this;
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

            guiGraphics.fill(x1, y1-1, x2, y1, 0xFFFFFFFF);
            guiGraphics.fill(x1, y2 - 1, x2, y2, 0xFFFFFFFF);
            guiGraphics.fill(x1, y1, x1 + 1, y2, 0xFFFFFFFF);
            guiGraphics.fill(x2 - 1, y1, x2, y2, 0xFFFFFFFF);
        }
    }

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

    public void applyTransform(GuiGraphics guiGraphics, int screenWidth, int screenHeight) {
        float px = getPixelX(screenWidth);
        float py = getPixelY(screenHeight);

        guiGraphics.pose().pushPose();

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
        KingdomKeys.LOGGER.warn("Saving config for "+name);
        List<Float> values = new ArrayList<>(List.of(x,y,(float)width,(float)height,scaleX,scaleY,rotation,(float)anchor.ordinal()));
        ModConfigs.setHUDData(name,values);
    }

    public JsonObject loadDefaultsFromJson() {
        ArrayList<Float> defaults = getDefaultValues(name);

        float defX = defaults.get(0);
        float defY = defaults.get(1);
        int defWidth = defaults.get(2).intValue();
        int defHeight = defaults.get(3).intValue();
        float defScaleX = defaults.get(4);
        float defScaleY = defaults.get(5);
        float defRotation = defaults.get(6);
        HUDAnchorPosition defAnchor = HUDAnchorPosition.values()[defaults.get(7).intValue()];

        try {
            ResourceLocation rl = ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "hud/" + name.toLowerCase() + ".json");
            Resource resource = Minecraft.getInstance().getResourceManager().getResourceOrThrow(rl);
            KingdomKeys.LOGGER.info("Found RP config for "+name);
            try (Reader reader = new InputStreamReader(resource.open())) {
                JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();

                x = json.has("xPos") ? json.get("xPos").getAsFloat() : defX;
                y = json.has("yPos") ? json.get("yPos").getAsFloat() : defY;

                width = json.has("width") ? json.get("width").getAsInt() : defWidth;
                height = json.has("height") ? json.get("height").getAsInt() : defHeight;

                scaleX = json.has("xScale") ? json.get("xScale").getAsFloat() : defScaleX;
                scaleY = json.has("yScale") ? json.get("yScale").getAsFloat() : defScaleY;

                rotation = json.has("rotation") ? json.get("rotation").getAsFloat() : defRotation;

                anchor = json.has("anchor") ? HUDAnchorPosition.valueOf(json.get("anchor").getAsString()) : defAnchor;

                return json;
            }

        } catch (Exception e) {
            KingdomKeys.LOGGER.warn("Couldn't find HUD defaults for {} in a Resource Pack, using hardcoded defaults", name);

            x = defX;
            y = defY;
            width = defWidth;
            height = defHeight;
            scaleX = defScaleX;
            scaleY = defScaleY;
            rotation = defRotation;
            anchor = defAnchor;
            return null;
        }
    }

    public static ArrayList<Float> getDefaultValues(String name){
        return switch(name){
            case "HP" -> Lists.newArrayList(13.8F, 3.8F, 916F, 254F,0.2F,0.2F, 0F, 8F);
            case "MP" -> Lists.newArrayList(54.2F, 8.6F, 142F, 12F,0.7F,0.5F, 0F, 8F);
            case "CM" -> Lists.newArrayList(5F, 5F, 70F, 75F, 1F,1F, 0F, 6F);
            case "RC" -> Lists.newArrayList(5F, 80F, 100F, 16F, 1F, 1F, 0F, 6F);
            case "Drive" -> Lists.newArrayList(53.7F, 14.6F, 95F, 18F, 0.8F, 0.8F, 0F, 8F);
            case "Focus" -> Lists.newArrayList(3F, 27F, 66F, 40F, 1F, 1F, 0F, 8F);
            case "Party" -> Lists.newArrayList(3F, 0F, 25F, 120F, 1F, 1F, 0F, 5F);
            case "LockOn" -> Lists.newArrayList(2F, 2F, 166F, 40F, 0.7F, 0.7F, 0F, 2F);
            case "Portrait" -> Lists.newArrayList(28F, 18F, 32F, 32F, 0.7F, 0.7F, 0F, 8F);
            default -> throw new IllegalStateException("Unexpected default HUD value: " + name);
        };
    }

    @Override
    public String toString() {
        return name+": x-"+x+" y-"+y+" width:"+width+" height:"+height;
    }
}
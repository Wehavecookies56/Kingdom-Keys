package online.kingdomkeys.kingdomkeys.client.gui.elements;

import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import online.kingdomkeys.kingdomkeys.client.gui.overlay.HUDAnchorPosition;

public class HUDElement {
    public HUDAnchorPosition anchor;

    public float x;
    public float y;

    public float scaleX = 1f;
    public float scaleY = 1f;

    public float rotation = 0f;

    public int width;
    public int height;

    public boolean selected;


    public HUDElement(HUDAnchorPosition anchor, float x, float y, int width, int height) {
        this.anchor = anchor;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public HUDElement setScale(float scaleX, float scaleY) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
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
}
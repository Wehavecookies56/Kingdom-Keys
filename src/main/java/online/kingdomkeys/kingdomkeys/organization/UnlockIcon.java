package online.kingdomkeys.kingdomkeys.organization;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class UnlockIcon {

    private ResourceLocation iconTexture;
    private int u, v, width, height;
    //If this is false, iconTexture, u, v, width, height should not be used
    private boolean useCustomTexture;

    public UnlockIcon(boolean useCustomTexture) {
        this.useCustomTexture = useCustomTexture;
    }

    public UnlockIcon(ResourceLocation iconTexture, int u, int v, int width, int height) {
        this.iconTexture = iconTexture;
        this.u = u;
        this.v = v;
        this.width = width;
        this.height = height;
        useCustomTexture = true;
    }

    public ResourceLocation getIconTexture() {
        return iconTexture;
    }

    public void setIconTexture(ResourceLocation iconTexture) {
        this.iconTexture = iconTexture;
    }

    public int getU() {
        return u;
    }

    public void setU(int u) {
        this.u = u;
    }

    public int getV() {
        return v;
    }

    public void setV(int v) {
        this.v = v;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @OnlyIn(Dist.CLIENT)
    public void render(AbstractGui gui, int posX, int posY, float partialTicks) {
        Minecraft mc = Minecraft.getInstance();
        mc.getTextureManager().bindTexture(iconTexture);
        gui.blit(posX, posY, u, v, width, height);
    }

}

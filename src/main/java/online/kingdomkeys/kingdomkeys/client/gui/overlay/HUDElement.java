package online.kingdomkeys.kingdomkeys.client.gui.overlay;

import java.awt.Color;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.resources.ResourceLocation;

/**
 * Widget Class for elements to display on the HUD e.g. Command Menu, HP bar, etc.
 * The X and Y position from {@link net.minecraft.client.gui.components.Widget} shouldn't be changed outside of here as that is handled by {@link HUDElement#anchorElement()} for manually modifying the position use {@link HUDElement#setAnchoredPosition(int, int)}
 */
public abstract class HUDElement extends AbstractWidget {

    //MC Instance for convenience
    protected Minecraft mcInstance;
    //The area of the screen to anchor the element to
    protected HUDAnchorPosition anchor;
    //Use these values to offset the element from the anchor
    protected int anchoredPositionX, anchoredPositionY;
    //Values stored for anything timed based like animations
    protected float currentTickPos, previousTickPos;

    public HUDElement(int anchoredPositionX, int anchoredPositionY, int elementWidth, int elementHeight, HUDAnchorPosition anchor, String name) {
        //Constructed with 0s as the position is later changed, the dimensions should be set manually
        super(0, 0, elementWidth, elementHeight, Component.translatable(name));
        this.anchoredPositionX = anchoredPositionX;
        this.anchoredPositionY = anchoredPositionY;
        this.width = elementWidth;
        this.height = elementHeight;
        this.anchor = anchor;
        mcInstance = Minecraft.getInstance();
    }

    /**
     * Use this to draw the element
     */
    public abstract void drawElement(PoseStack matrixStack, float partialTicks);

    /**
     * Use this for the initialisation of the element
     */
    public abstract void initElement();

    /**
     * Sets both anchored positions
     * @param anchoredPositionX
     * @param anchoredPositionY
     */
    public void setAnchoredPosition(int anchoredPositionX, int anchoredPositionY) {
        this.anchoredPositionX = anchoredPositionX;
        this.anchoredPositionY = anchoredPositionY;
        anchorElement();
    }

    /**
     * Sets just the X anchored position
     * @param anchoredPositionX
     */
    public void setAnchoredPositionX(int anchoredPositionX) {
        this.anchoredPositionX = anchoredPositionX;
        anchorElement();
    }

    /**
     * Sets just the Y anchored position
     * @param anchoredPositionY
     */
    public void setAnchoredPositionY(int anchoredPositionY) {
        this.anchoredPositionY = anchoredPositionY;
        anchorElement();
    }


    private void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    private void setPositionX(int x) {
        setPosition(x, this.y);
    }

    private void setPositionY(int y) {
        setPosition(this.x, y);
    }

    /**
     * Sets the width and height of the element, then anchors the element using the new dimensions
     * @param width
     * @param height
     */
    protected void setDimensions(int width, int height) {
        setWidth(width);
        setHeight(height);
        anchorElement();
    }

    /**
     * Wrapper method for {@link net.minecraft.client.gui.components.AbstractWidget#drawString(PoseStack, Font, String, int, int, int)}
     * @param string The string to draw
     * @param posX The X position relative to the position of the element
     * @param posY The Y position relative to the position of the element
     * @param colour The text colour
     */
    public void drawString(PoseStack matrixStack, String string, int posX, int posY, Color colour) {
        drawString(matrixStack, mcInstance.font, string, this.x + posX, this.y + posY, colour.getRGB());
    }

    @Override
    public void blit(PoseStack matrixStack, int posX, int posY, int texU, int texV, int texWidth, int texHeight) {
        super.blit(matrixStack, this.x + posX, this.y + posY, texU, texV, texWidth, texHeight);
    }

    /**
     * Wrapper method to bind a texture and draw using {@link HUDElement#blit(PoseStack, int, int, int, int, int, int)}
     * @param texture the texture resource location
     * @param posX X position to draw to
     * @param posY Y position to draw to
     * @param texU texture U position
     * @param texV texture V position
     * @param texWidth texture width
     * @param texHeight texture height
     */
    public void bindAndBlit(PoseStack matrixStack, ResourceLocation texture, int posX, int posY, int texU, int texV, int texWidth, int texHeight) {
    	RenderSystem.setShaderTexture(0,texture);
        blit(matrixStack, posX, posY, texU, texV, texWidth, texHeight);
    }

    /**
     * Positions the element using the {@link HUDElement#anchor} using {@link Window#getGuiScaledHeight()} and {@link Window#getGuiScaledWidth()} for positioning relative to screen size
     * The anchored positions are added to the position
     * The element width and height are taken into account
     */
    public final void anchorElement() {
        setPosition(0, 0);
        switch (this.anchor) {
            case TOP_LEFT:
                setPosition(this.x + anchoredPositionX, this.y + anchoredPositionY);
                break;
            case TOP_CENTER:
                setPosition(this.x + (mcInstance.getWindow().getGuiScaledWidth()/2) - (getWidth()/2) + anchoredPositionX, this.y + anchoredPositionY);
                break;
            case TOP_RIGHT:
                setPosition(mcInstance.getWindow().getGuiScaledWidth() - getWidth() + anchoredPositionX, this.y + anchoredPositionY);
                break;
            case BOTTOM_LEFT:
                setPosition(this.x + anchoredPositionX, mcInstance.getWindow().getGuiScaledHeight() - getHeight() + anchoredPositionY);
                break;
            case BOTTOM_CENTER:
                setPosition(this.x + (mcInstance.getWindow().getGuiScaledWidth()/2) - (getWidth()/2) + anchoredPositionX, mcInstance.getWindow().getGuiScaledHeight() - getHeight() + anchoredPositionY);
                break;
            case BOTTOM_RIGHT:
                setPosition(mcInstance.getWindow().getGuiScaledWidth() - getWidth() + anchoredPositionX, mcInstance.getWindow().getGuiScaledHeight() - getHeight() + anchoredPositionY);
                break;
            case CENTER_LEFT:
                setPosition(this.x + anchoredPositionX, this.y + (mcInstance.getWindow().getGuiScaledHeight()/2) - (getHeight()/2) + anchoredPositionY);
                break;
            case CENTER:
                setPosition(this.x + (mcInstance.getWindow().getGuiScaledWidth()/2) - (getWidth()/2) + anchoredPositionX, this.y + (mcInstance.getWindow().getGuiScaledHeight()/2) - (getHeight()/2) + anchoredPositionY);
                break;
            case CENTER_RIGHT:
                setPosition(mcInstance.getWindow().getGuiScaledWidth() - getWidth() + anchoredPositionX, this.y + (mcInstance.getWindow().getGuiScaledHeight()/2) - (getHeight()/2) + anchoredPositionY);
                break;
        }
    }

    /**
     * Render the hud element here, might be redundant as drawElement can currently be called instead
     * @param partialTicks the partial ticks from the render event
     */
    public void render(PoseStack matrixStack, float partialTicks) {
        drawElement(matrixStack, partialTicks);
    }
    public abstract void tick();

}

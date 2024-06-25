package online.kingdomkeys.kingdomkeys.client.gui.elements.buttons;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import online.kingdomkeys.kingdomkeys.client.gui.SavePointScreen;
import online.kingdomkeys.kingdomkeys.entity.block.SavepointTileEntity;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.kingdomkeys.kingdomkeys.world.SavePointStorage;

import java.awt.*;
import java.util.UUID;

public class SavePointButton extends ScrollableButtonBase {
    SavePointScreen parent;
    UUID destination;

    public SavePointButton(SavePointScreen parent, int pX, int pY, int pWidth, int pHeight, Component pMessage, UUID destination) {
        super(new Builder(pMessage, pButton -> parent.clickSavePoint(destination)).bounds(pX, pY, pWidth, pHeight));
        this.parent = parent;
        this.destination = destination;
    }

    public UUID getDestination() {
        return destination;
    }

    @Override
    protected void renderWidget(GuiGraphics gui, int pMouseX, int pMouseY, float pPartialTick) {
        isHovered = isMouseOver(pMouseX, pMouseY);
        int labelHeight = Minecraft.getInstance().font.lineHeight + 2;
        if (visible) {
            if (!active || isHovered) {
                gui.fill(getX()-1, getY()-1, getX() + getWidth()+1, getY() + getHeight()+1, Color.GREEN.getRGB());
            } else {
                gui.fill(getX(), getY(), getX() + getWidth(), getY() + getHeight(), Color.GREEN.getRGB());
            }
            if (!parent.savePointScreenshots.isEmpty()) {
                SavePointScreen.Screenshot screenshot = parent.savePointScreenshots.get(destination);
                if (screenshot != null && screenshot.textureLocation() != null) {
                    gui.blit(screenshot.textureLocation(), getX(), getY(), 0, 0, getWidth(), getHeight(), getWidth(), getHeight());
                }
            }
            if (isHovered) {
                SavePointStorage.SavePoint sPoint = parent.savePoints.get(destination).getFirst();
                if(sPoint == null)
                    return;

                if(Minecraft.getInstance().level.getBlockEntity(sPoint.pos()) instanceof SavepointTileEntity savepoint){
                    Tooltip tooltip = Tooltip.create(Component.literal(
                      "UUID: "+savepoint.getID()+
                            "\nDimension: "+sPoint.dimension().location()+
                            "\nOwner: "+sPoint.owner().getSecond()+
                            "\nHealing: "+ Utils.getSavepointPercent(savepoint.getHeal())+
                            "%\nFood: "+Utils.getSavepointPercent(savepoint.getHunger())+
                            "%\nMagic: "+Utils.getSavepointPercent(savepoint.getMagic())+
                            "%\nFocus: "+Utils.getSavepointPercent(savepoint.getFocus())+
                            "%\nDrive: "+Utils.getSavepointPercent(savepoint.getDrive())+"%"));
                    setTooltip(tooltip);
                }
                drawLabel(getMessage(), gui, labelHeight);

            }
            if (parent.hovered == null || !parent.hovered.equals(destination)) {
                drawDark(gui, Color.BLACK);
            }
        }
        if (!active) {
            //gui.drawCenteredString(Minecraft.getInstance().font, Component.translatable("You are here"), getX() + (getWidth() / 2), getY() + (getHeight() - (height/2) - (Minecraft.getInstance().font.lineHeight/2)) + 1, Color.WHITE.getRGB());
            drawLabel(Component.translatable(Strings.Gui_Save_Main_CurrentPosition), gui, labelHeight);
        }
    }

    public void drawDark(GuiGraphics gui, Color colour) {
        gui.setColor(1, 1, 1, 0.4F);
        gui.fill(getX(), getY(), getX() + getWidth(), getY() + getHeight(), colour.getRGB());
        gui.setColor(1, 1, 1, 1F);
    }

    public void drawLabel(Component text, GuiGraphics gui, int labelHeight) {
        gui.setColor(1, 1, 1, 0.5F);
        gui.fill(getX(), getY() + (getHeight() - labelHeight), getX() + getWidth(), getY() + getHeight(), Color.BLACK.getRGB());
        gui.setColor(1, 1, 1, 1);
        if (Minecraft.getInstance().font.width(getMessage().getVisualOrderText()) > getWidth()) {
            gui.enableScissor(getX(), getY() + (getHeight() - labelHeight), getX() + getWidth(), getY() + getHeight());
            gui.drawString(Minecraft.getInstance().font, text, getX() + 1, getY() + (getHeight() - labelHeight) + 1, Color.WHITE.getRGB());
            gui.disableScissor();
        } else {
            gui.drawCenteredString(Minecraft.getInstance().font, text, getX() + (getWidth() / 2), getY() + (getHeight() - labelHeight) + 1, Color.WHITE.getRGB());
        }
    }

    public boolean isMouseOverInactive(double pMouseX, double pMouseY) {
        return !this.active && this.visible && pMouseX >= (double)this.getX() && pMouseY >= (double)this.getY() && pMouseX < (double)(this.getX() + this.width) && pMouseY < (double)(this.getY() + this.height);
    }

    @Override
    public boolean isMouseOver(double pMouseX, double pMouseY) {
        boolean hovered = super.isMouseOver(pMouseX, pMouseY);
        if (hovered) {
            parent.hovered = destination;
        } else if (parent.hovered != null && parent.hovered.equals(destination)) {
            parent.hovered = null;
        }
        return hovered;
    }
}

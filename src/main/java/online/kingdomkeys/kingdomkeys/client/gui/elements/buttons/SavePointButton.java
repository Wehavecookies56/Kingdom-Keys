package online.kingdomkeys.kingdomkeys.client.gui.elements.buttons;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import online.kingdomkeys.kingdomkeys.client.gui.SavePointScreen;
import online.kingdomkeys.kingdomkeys.entity.block.SavepointTileEntity;
import online.kingdomkeys.kingdomkeys.world.SavePointStorage;

import java.awt.*;
import java.util.UUID;

public class SavePointButton extends Button {
    SavePointScreen parent;
    UUID destination;
    public int offsetY;

    public SavePointButton(SavePointScreen parent, int pX, int pY, int pWidth, int pHeight, Component pMessage, UUID destination) {
        super(new Builder(pMessage, pButton -> parent.clickSavePoint(destination)).bounds(pX, pY, pWidth, pHeight));
        this.parent = parent;
        this.destination = destination;
    }

    @Override
    public int getY() {
        return super.getY() - offsetY;
    }

    @Override
    protected void renderWidget(GuiGraphics gui, int pMouseX, int pMouseY, float pPartialTick) {
        isHovered = isMouseOver(pMouseX, pMouseY);
        if (visible && active) {
            int labelHeight = Minecraft.getInstance().font.lineHeight + 2;
            gui.fill(getX(), getY(), getX() + getWidth(), getY() + getHeight(), Color.GREEN.getRGB());
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
                            "\nHealing: "+Math.round(100 - (((savepoint.getHeal()-1) /(20F-1F)) * 100F))+
                            "%\nFood: "+Math.round(100 - (((savepoint.getHunger()-1) /(20F-1F)) * 100F))+
                            "%\nMagic: "+Math.round(100 - (((savepoint.getMagic()-1) /(20F-1F)) * 100F))+
                            "%\nFocus: "+Math.round(100 - (((savepoint.getFocus()-1) /(20F-1F)) * 100F))+
                            "%\nDrive: "+Math.round(100 - (((savepoint.getDrive()-1) /(20F-1F)) * 100F))+"%"));
                    setTooltip(tooltip);
                }

                gui.setColor(1, 1, 1, 0.5F);
                gui.fill(getX(), getY() + (getHeight() - labelHeight), getX() + getWidth(), getY() + getHeight(), Color.BLACK.getRGB());
                gui.setColor(1, 1, 1, 1);
                if (Minecraft.getInstance().font.width(getMessage().getVisualOrderText()) > getWidth()) {
                    gui.enableScissor(getX(), getY() + (getHeight() - labelHeight), getX() + getWidth(), getY() + getHeight());
                    gui.drawString(Minecraft.getInstance().font, getMessage(), getX() + 1, getY() + (getHeight() - labelHeight) + 1, Color.WHITE.getRGB());
                    gui.disableScissor();
                } else {
                    gui.drawCenteredString(Minecraft.getInstance().font, getMessage(), getX() + (getWidth() / 2), getY() + (getHeight() - labelHeight) + 1, Color.WHITE.getRGB());
                }

            }
            if (parent.hovered == null || !parent.hovered.equals(destination)) {
                gui.setColor(1, 1, 1, 0.4F);
                gui.fill(getX(), getY(), getX() + getWidth(), getY() + getHeight(), Color.BLACK.getRGB());
                gui.setColor(1, 1, 1, 1F);
            }
        }
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

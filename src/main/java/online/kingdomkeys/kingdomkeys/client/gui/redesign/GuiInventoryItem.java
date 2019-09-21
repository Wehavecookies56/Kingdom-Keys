package uk.co.wehavecookies56.kk.client.gui.redesign;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import uk.co.wehavecookies56.kk.api.menu.ItemCategoryRegistry;
import uk.co.wehavecookies56.kk.client.gui.GuiStock;
import uk.co.wehavecookies56.kk.client.sound.ModSounds;
import uk.co.wehavecookies56.kk.api.menu.IItemCategory;
import uk.co.wehavecookies56.kk.api.menu.ItemCategory;
import uk.co.wehavecookies56.kk.common.lib.Reference;

public class GuiInventoryItem extends GuiButton {

    GuiStock parent;
    ItemStack item;
    boolean selected;

    public GuiInventoryItem(GuiStock parent, ItemStack item, int x, int y) {
        super(0, x, y, "");
        this.parent = parent;
        this.item = item;
        width = (int)(parent.width * 0.3255F);
        height = 14;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        hovered = mouseX > x && mouseY >= y && mouseX < x + width && mouseY < y + height;
        GlStateManager.color(1,1,1,1);
        if (visible) {
            Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(Reference.MODID, "textures/gui/menu/menu_button.png"));
            if (selected || hovered) {
                GlStateManager.pushMatrix();
                GlStateManager.enableBlend();
                GlStateManager.enableAlpha();
                GlStateManager.translate(x+0.6F, y, 0);
                GlStateManager.scale(0.5F, 0.5F, 1);
                drawTexturedModalRect(0, 0, 27, 0, 17, 28);
                for (int i = 0; i < (width*2)-(17*2); i++) {
                    drawTexturedModalRect(17+i, 0, 45, 0, 1, 28);
                }
                drawTexturedModalRect((width*2)-17, 0, 47, 0, 17, 28);
                GlStateManager.popMatrix();
            }
            ItemCategory category = ItemCategory.MISC;
            if (item.getItem() instanceof IItemCategory) {
                category = ((IItemCategory) item.getItem()).getCategory();
            } else if (ItemCategoryRegistry.hasCategory(item.getItem())) {
                category = ItemCategoryRegistry.getCategory(item.getItem());
            }
            GlStateManager.pushMatrix();
            GlStateManager.translate(x+3, y+2, 0);
            GlStateManager.scale(0.5F, 0.5F, 1);
            drawTexturedModalRect(0, 0, category.getU(), category.getV(), 20, 20);
            GlStateManager.popMatrix();
            drawString(mc.fontRenderer, item.getDisplayName(), x+15, y+3, 0xFFFFFF);
            String count = "x"+item.getCount()+" ";
            drawString(mc.fontRenderer, count, x+width-mc.fontRenderer.getStringWidth(count), y+3, 0xFFFFFF);
        }
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        if (visible && enabled) {
            if (mouseX >= x && mouseX <= x + width) {
                if (mouseY >= y && mouseY <= y + height) {
                    float truePos = (mouseY - y); //scrollOffset
                    //36 = element height, need to change to be actual height
                    int index = (int)(truePos) / 36;
                    //parent.itemSelected = (int) (truePos) / 36;

                    parent.selected = this.item;
                    playPressSound(mc.getSoundHandler());
                    selected = true;
                    return true;
                }
            }
        }
        selected = false;
        return false;
    }

    @Override
    public void playPressSound(SoundHandler soundHandlerIn) {
        soundHandlerIn.playSound(PositionedSoundRecord.getMasterRecord(ModSounds.select, 1.0F));
    }
}

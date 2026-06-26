package online.kingdomkeys.kingdomkeys.client.gui.elements.buttons;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.client.gui.castle_oblivion.RoomSynthesisScreen;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.item.card.CardCategory;
import online.kingdomkeys.kingdomkeys.item.card.MapCardItem;
import online.kingdomkeys.kingdomkeys.util.Utils;

import javax.annotation.ParametersAreNonnullByDefault;

public class CardSelectButton extends MenuButtonBase {
    public ItemStack stack;
    public Item card;

    Minecraft minecraft;
    RoomSynthesisScreen parent;

    public CardSelectButton(int x, int y, int widthIn, int heightIn, ItemStack stack, RoomSynthesisScreen roomSynthesisScreen, Button.OnPress onPress) {
        super(x, y, widthIn, heightIn, Utils.translateToLocal(""), onPress);
        minecraft = Minecraft.getInstance();
        this.stack = stack;
        card = stack.getItem();
        parent=roomSynthesisScreen;
    }

    @ParametersAreNonnullByDefault
    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        //if(!isSelected())
        isHovered = mouseX > getX() + 1 && mouseY >= getY() + 1 && mouseX < getX() + width - 1 && mouseY < getY() + height - 1;
        active = parent.te.cardMatchesCriteria(stack);
        if(isHovered()) {
            selected = false;
        }

        PoseStack matrixStack = guiGraphics.pose();

        matrixStack.pushPose();
        {
            if(visible) {
                matrixStack.translate(getX(), getY(), 0);
                matrixStack.scale(3,3, 1);
                if(isHovered && active) {
                    matrixStack.scale(1.15F,1.15F, 1);
                    matrixStack.translate(-1,-2, 20);
                }

                ClientUtils.drawItemAsIcon(stack, guiGraphics.pose(), 0,0, 16);
                int color = active ? 0xFFDD00 : 0xAAAAAA;

                if (card instanceof MapCardItem mapCardItem) {
                    matrixStack.pushPose();
                    {
                        matrixStack.translate(9, 11.5, 150);
                        matrixStack.scale(0.5F, 0.6F, 1);
                        if (mapCardItem.getCategory() != CardCategory.YELLOW && mapCardItem.getCategory() != CardCategory.RGB) {
                            guiGraphics.drawString(minecraft.font, Component.literal("" + mapCardItem.getCardValue(stack)).withStyle(ClientUtils.KK_Font_EXP), 0, 0, color);
                        }
                    }
                    matrixStack.popPose();
                }
                matrixStack.translate(9.5, 10, 150);
                matrixStack.scale(0.3F,0.3F, 1);
                guiGraphics.drawString(minecraft.font, "x"+stack.getCount(), -21, 11, 0xFFFFFF);
            }
        }
        matrixStack.popPose();
    }

    @Override
    public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
        if (isHovered && active)
            return super.mouseClicked(p_mouseClicked_1_, p_mouseClicked_3_, p_mouseClicked_5_);
        else
            return false;
    }

    public boolean isHovered() {
        return isHovered;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public void playDownSound(SoundManager soundHandlerIn) {
        soundHandlerIn.play(SimpleSoundInstance.forUI(ModSounds.menu_select.get(), 1.0F, 1.0F));
    }


}
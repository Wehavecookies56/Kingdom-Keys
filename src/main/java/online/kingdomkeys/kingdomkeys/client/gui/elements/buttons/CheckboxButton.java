package online.kingdomkeys.kingdomkeys.client.gui.elements.buttons;

import org.jetbrains.annotations.NotNull;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;


public class CheckboxButton extends AbstractButton {

    final ResourceLocation TEXTURE = new ResourceLocation(KingdomKeys.MODID + ":textures/gui/checkbox.png");

    private boolean checked;

    public CheckboxButton(int xIn, int yIn, String msg, boolean checked) {
        super(xIn, yIn, 10, 10, Component.translatable(msg));
        this.checked = checked;
    }

    @Override
    public void onPress() {
        this.checked = !checked;
    }

    @Override
    public void render(@NotNull GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
        if (visible) {
            gui.blit(TEXTURE, getX(), getY(), 0, 0, 10, 10);
            if (checked) {
                gui.blit(TEXTURE, getX(), getY(), 10, 0, 10, 10);
            }
            gui.drawString(Minecraft.getInstance().font, getMessage().getString(), getX() + width + 3, getY() + 2, 4210752, false);
        }
    }

    @Override
    public int getWidth() {
        return super.getWidth() + 3 + Minecraft.getInstance().font.width(getMessage());
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

	@Override
	protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {
		
	}
}

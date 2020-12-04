package online.kingdomkeys.kingdomkeys.client.gui.elements.buttons;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.AbstractButton;
import net.minecraft.util.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;


public class CheckboxButton extends AbstractButton {

    private boolean checked;

    public CheckboxButton(int xIn, int yIn, String msg, boolean checked) {
        super(xIn, yIn, 10, 10, msg);
        this.checked = checked;
    }

    @Override
    public void onPress() {
        this.checked = !checked;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        if (visible) {
            Minecraft.getInstance().textureManager.bindTexture(new ResourceLocation(KingdomKeys.MODID + ":textures/gui/checkbox.png"));
            blit(x, y, 0, 0, 10, 10);
            if (checked) {
                blit(x, y, 10, 0, 10, 10);
            }
            Minecraft.getInstance().fontRenderer.drawString(getMessage(), x + width + 3, y + 2, 4210752);
        }
    }

    @Override
    public int getWidth() {
        return super.getWidth() + 3 + Minecraft.getInstance().fontRenderer.getStringWidth(getMessage());
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}

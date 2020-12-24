package online.kingdomkeys.kingdomkeys.client.gui.elements;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import online.kingdomkeys.kingdomkeys.client.gui.elements.buttons.MenuButton;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSTravelToSoA;
import online.kingdomkeys.kingdomkeys.util.Utils;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class MenuPopup extends Screen {

    MenuButton ok, cancel;

    enum Action {
        OK, CANCEL
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    public abstract void OK();

    public abstract void CANCEL();

    public abstract List<String> getTextToDisplay();

    @Nonnull
    public String OKString() {
        return "OK";
    }

    @Nonnull
    public String CANCELString() {
        return "CANCEL";
    }

    private void buttonAction(Action a) {
        switch (a) {
            case OK:
                OK();
                break;
            case CANCEL:
                CANCEL();
                break;
        }
    }

    public MenuPopup() {
        super(new TranslationTextComponent(""));
    }

    int[] alpha;
    int[] timer;

    @Override
    protected void init() {
        super.init();
        this.addButton(ok = new MenuButton(0, 40, 40, Utils.translateToLocal(OKString()), MenuButton.ButtonType.BUTTON, (p)->buttonAction(Action.OK)));
        this.addButton(cancel = new MenuButton(50, 40, 40, Utils.translateToLocal(CANCELString()), MenuButton.ButtonType.BUTTON, (p)->buttonAction(Action.CANCEL)));
        ok.visible = false;
        ok.active = false;
        cancel.visible = false;
        cancel.active = false;
        alpha = new int[getTextToDisplay().size()];
        timer = new int[getTextToDisplay().size()];
        timer[0] = titleDisplayTime + titleFadeIn;
        Arrays.fill(alpha, 0);
    }

    protected void renderTextBackground(FontRenderer fontRendererIn, int yIn, int stringWidthIn) {
        int i = Minecraft.getInstance().gameSettings.getTextBackgroundColor(0.0F);
        if (i != 0) {
            int j = -stringWidthIn / 2;
            fill(j - 2, yIn - 2, j + stringWidthIn + 2, yIn + 9 + 2, i);
        }

    }

    int scaledWidth;
    int scaledHeight;

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        super.render(mouseX, mouseY, partialTicks);
        float startY = -10.0F;
        float lineGap = 5F;
        //for (int i = 0; i < getTextToDisplay().size(); i++) {
        //    drawCenteredString(font, Utils.translateToLocal(getTextToDisplay().get(i)), 0, (int) (startY + (i * font.FONT_HEIGHT) + gap), 0xFFFFFF);
        //}

        this.scaledWidth = Minecraft.getInstance().getMainWindow().getScaledWidth();
        this.scaledHeight = Minecraft.getInstance().getMainWindow().getScaledHeight();

        RenderSystem.pushMatrix();
        RenderSystem.translatef((float) (this.scaledWidth / 2), (float) (this.scaledHeight / 2) - ((startY + ((getTextToDisplay().size()-1) * (font.FONT_HEIGHT + 3))) / 2F), 0.0F);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.pushMatrix();
        RenderSystem.scalef(2.0F, 2.0F, 2.0F);

        for (int i = 0; i < getTextToDisplay().size(); i++) {
            float f4 = (float) timer[i] - partialTicks;
            if (timer[i] > this.titleDisplayTime) {
                float f5 = (float) (this.titleFadeIn + this.titleDisplayTime) - f4;
                alpha[i] = (int) (f5 * 255.0F / (float) this.titleFadeIn);
            }
            if (timer[i] == -1) alpha[i] = 255;
            alpha[i] = MathHelper.clamp(alpha[i], 0, 255);
            if (alpha[i] > 8) {
                int l1 = alpha[i] << 24 & -16777216;
                int i2 = font.getStringWidth(Utils.translateToLocal(getTextToDisplay().get(i)));
                this.renderTextBackground(font, (int) (startY + (i * (font.FONT_HEIGHT + 3))), i2);
                font.drawStringWithShadow(Utils.translateToLocal(getTextToDisplay().get(i)), (float) (-i2 / 2), startY + (i * (font.FONT_HEIGHT + 3)), 16777215 | l1);
            }
        }

        RenderSystem.popMatrix();
        RenderSystem.disableBlend();
        RenderSystem.popMatrix();
    }

    int titleDisplayTime = 35;
    int titleFadeIn = 10;
    int currentTimer = 0;

    @Override
    public boolean keyPressed(int p_keyPressed_1_, int p_keyPressed_2_, int p_keyPressed_3_) {
        if (GLFW.GLFW_KEY_SPACE == p_keyPressed_1_) {
            timer[currentTimer] = -1;
            if (currentTimer+1 == timer.length) {
                ok.visible = true;
                ok.active = true;
                cancel.visible = true;
                cancel.active = true;
            } else {
                timer[currentTimer + 1] = titleDisplayTime + titleFadeIn;
                currentTimer++;
            }
        }
        return super.keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_);
    }

    @Override
    public void tick() {
        for(int i = 0; i < timer.length; i++) {
            if (timer[i] > 0) {
                --timer[i];
                if (timer[i] <= 0) {
                    if (i != timer.length-1) {
                        timer[i] = -1;
                        timer[i+1] = titleDisplayTime + titleFadeIn;
                        currentTimer = i+1;
                    } else {
                        ok.visible = true;
                        ok.active = true;
                        cancel.visible = true;
                        cancel.active = true;
                    }
                }
            }
        }

        super.tick();
    }
}

package online.kingdomkeys.kingdomkeys.client.gui.elements;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.util.GuiStringBuilder;

import java.util.function.Consumer;
import java.util.function.Function;

public class TextBox extends AbstractWidget {

    private static final ResourceLocation texture = KingdomKeys.rl("text_box");

    int minHeight;
    int hPadding;
    int vPadding;
    boolean centered;
    boolean autoExpand;
    int maxLineWidth;
    boolean textShadow;
    private final Image image;

    public record Vec2i(int x, int y) {}
    public record ImageProperties(int x, int y, int width, int height) {}
    public record Image(ImageProperties properties, Consumer<Render> render) {}
    public record Render(GuiGraphics guiGraphics, int x, int y, int width, int height, ImageProperties properties) {}

    private TextBox(TextBoxBuilder builder) {
        super(builder.x, builder.y, builder.width, builder.minHeight, builder.text);
        this.minHeight = builder.minHeight;
        this.hPadding = builder.hPadding;
        this.vPadding = builder.vPadding;
        this.centered = builder.centered;
        this.autoExpand = builder.autoExpandHeight;
        this.maxLineWidth = builder.maxLineWidth;
        this.textShadow = builder.textShadow;
        this.image = builder.image;
    }

    public static TextBoxBuilder create(int x, int y, int width, int minHeight) {
        return new TextBoxBuilder(x, y, width, minHeight);
    }

    public int getMinHeight() {
        return minHeight;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        ClientUtils.blitSprite(guiGraphics, texture, getX(), getY(), getWidth(), getHeight(), true);
        if (image != null) {
            //int offsetX = centered ? (getWidth() / 2) - (image / 2) : 0;
            //int offsetY = centered ? (getHeight() / 2) - (image.height / 2) : 0;
            image.render.accept(new Render(guiGraphics, getX() + hPadding, getY() + vPadding, getWidth() - (hPadding * 2), getHeight() - (vPadding * 2), image.properties));
            //this.setWidth(imageSize.x);
            //this.setHeight(imageSize.y);
        } else {
            int offset = 0;
            int lineWidth = getWidth() - (hPadding * 2);
            if (lineWidth > maxLineWidth && maxLineWidth != 0) {
                lineWidth = maxLineWidth;
                offset = (getWidth() / 2) - (lineWidth / 2);
            }
            int height = Math.max(GuiStringBuilder.create(getMessage(), getX() + hPadding + offset, getY() + vPadding).centered(centered).shadow(textShadow).wordWrap(lineWidth).draw(guiGraphics) + (vPadding * 2), minHeight);
            if (autoExpand) {
                setHeight(height);
            }
        }
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {}

    public static class TextBoxBuilder {

        private final int x, y, width, minHeight;
        private Component text;
        private boolean centered, textShadow, autoExpandHeight;
        private int maxLineWidth, hPadding, vPadding;
        private Image image;

        private TextBoxBuilder(int x, int y, int width, int minHeight) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.minHeight = minHeight;
            this.text = Component.empty();
            this.centered = false;
            this.textShadow = false;
            this.autoExpandHeight = false;
            this.maxLineWidth = 0;
            this.hPadding = 5;
            this.vPadding = 5;
            this.image = null;
        }

        public TextBoxBuilder text(Component text) {
            this.text = text;
            return this;
        }

        public TextBoxBuilder image(ImageProperties properties, Consumer<Render> render) {
            this.image = new Image(properties, render);
            return this;
        }

        public TextBoxBuilder centered() {
            this.centered = true;
            return this;
        }

        public TextBoxBuilder textShadow() {
            this.textShadow = true;
            return this;
        }

        public TextBoxBuilder autoExpand() {
            this.autoExpandHeight = true;
            return this;
        }

        public TextBoxBuilder maxLineWidth(int maxLineWidth) {
            this.maxLineWidth = maxLineWidth;
            return this;
        }

        public TextBoxBuilder padding(int hPadding, int vPadding) {
            this.hPadding = hPadding;
            this.vPadding = vPadding;
            return this;
        }

        public TextBoxBuilder hPadding(int hPadding) {
            this.hPadding = hPadding;
            return this;
        }

        public TextBoxBuilder vPadding(int vPadding) {
            this.vPadding = vPadding;
            return this;
        }

        public TextBox build() {
            return new TextBox(this);
        }
    }
}

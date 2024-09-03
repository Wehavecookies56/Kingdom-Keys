package online.kingdomkeys.kingdomkeys.client.gui.elements.buttons;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.api.item.ItemCategory;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuFilterBar;
import org.jetbrains.annotations.NotNull;

public class MenuFilterButton extends Button {

    public ItemCategory category;
    int iconSize = 20;
    MenuFilterBar parent;

    final ResourceLocation texture = new ResourceLocation(KingdomKeys.MODID, "textures/gui/menu/menu_button.png");


    public MenuFilterButton(MenuFilterBar parent, int x, int y, ItemCategory category) {
		super(new Builder(Component.translatable(""),b -> parent.onClickFilter(category)).bounds(x,y,26,15));		
        this.parent = parent;
        this.category = category;
    }

    public MenuFilterButton(MenuFilterBar parent, int x, int y, String text) {
        this(parent, x, y, (ItemCategory) null);
        this.setMessage(Component.translatable(text));
    }

    @Override
    public void render(@NotNull GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
        Minecraft mc = Minecraft.getInstance();
        Font fr = mc.font;
        float scale = 0.5F;
        isHovered = mouseX > getX() && mouseY >= getY() && mouseX < getX() + width && mouseY < getY() + height;
        if (visible) {
            float centreX = getX() + ((width - (iconSize / 2F)) * scale);
            float centreY = getY() + ((height -(iconSize / 2F)) * scale);

            ClientUtils.blitScaled(texture, gui, getX(), getY(), 66, 0, 52, 30, scale);
            if (getMessage().getString().isEmpty() && category != null) {
                ClientUtils.blitScaled(texture, gui, centreX, centreY, category.getU(), category.getV(), iconSize, iconSize, scale);
            } else {
                float textCentreX = getX() + ((width * scale) - ((fr.width(getMessage()) * 0.75F) / 2));
                float textCentreY = getY() + ((height * scale) - ((fr.lineHeight * 0.75F) / 2));
                ClientUtils.drawStringScaled(gui, textCentreX, textCentreY, getMessage().getString(), 0xFFFFFF, 0.75F);
            }
        }
    }
}

package online.kingdomkeys.kingdomkeys.client.gui.elements.buttons;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.api.item.ItemCategory;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuFilterBar;

public class MenuFilterButton extends Button {

    public ItemCategory category;
    int iconSize = 20;
    MenuFilterBar parent;


    public MenuFilterButton(MenuFilterBar parent, int x, int y, ItemCategory category) {
        super(x, y, 26, 15, new TranslatableComponent(""), b -> parent.onClickFilter(category));
        this.parent = parent;
        this.category = category;
    }

    public MenuFilterButton(MenuFilterBar parent, int x, int y, String text) {
        this(parent, x, y, (ItemCategory) null);
        this.setMessage(new TranslatableComponent(text));
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        Minecraft mc = Minecraft.getInstance();
        Font fr = mc.font;
        float scale = 0.5F;
        isHovered = mouseX > x && mouseY >= y && mouseX < x + width && mouseY < y + height;
        if (visible) {
            float centreX = x + ((width - (iconSize / 2F)) * scale);
            float centreY = y + ((height -(iconSize / 2F)) * scale);
            RenderSystem.setShaderTexture(0, new ResourceLocation(KingdomKeys.MODID, "textures/gui/menu/menu_button.png"));

            ClientUtils.blitScaled(matrixStack, this, x, y, 66, 0, 52, 30, scale);
            if (getMessage().getString().isEmpty() && category != null) {
                ClientUtils.blitScaled(matrixStack, this, centreX, centreY, category.getU(), category.getV(), iconSize, iconSize, scale);
            } else {
                float textCentreX = x + ((width * scale) - ((fr.width(getMessage()) * 0.75F) / 2));
                float textCentreY = y + ((height * scale) - ((fr.lineHeight * 0.75F) / 2));
                ClientUtils.drawStringScaled(matrixStack, this, textCentreX, textCentreY, getMessage().getString(), 0xFFFFFF, 0.75F);
                RenderSystem.setShaderTexture(0, new ResourceLocation(KingdomKeys.MODID, "textures/gui/menu/menu_button.png"));
            }
        }
    }
}

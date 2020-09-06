package online.kingdomkeys.kingdomkeys.client.gui.elements.buttons;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.api.item.IItemCategory;
import online.kingdomkeys.kingdomkeys.api.item.ItemCategory;
import online.kingdomkeys.kingdomkeys.api.item.ItemCategoryRegistry;
import online.kingdomkeys.kingdomkeys.client.gui.menu.items.MenuStockScreen;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class MenuStockItem extends Button {

    MenuStockScreen parent;
    ItemStack stack;
    boolean selected;

    public MenuStockItem(MenuStockScreen parent, ItemStack stack, int x, int y) {
        super(x, y, (int)(parent.width * 0.3255F), 14, "", b -> {

        });
        this.parent = parent;
        this.stack = stack;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        isHovered = mouseX > x && mouseY >= y && mouseX < x + width && mouseY < y + height;
        RenderSystem.color4f(1, 1, 1, 1);
        if (visible) {
            Minecraft mc = Minecraft.getInstance();
            mc.getTextureManager().bindTexture(new ResourceLocation(KingdomKeys.MODID, "textures/gui/menu/menu_button.png"));
            if (isHovered) {
                RenderSystem.pushMatrix();
                {
                    RenderSystem.enableBlend();
                    RenderSystem.enableAlphaTest();
                    RenderSystem.translatef(x + 0.6F, y, 0);
                    float scale = 0.5F;
                    RenderSystem.scalef(scale, scale, 1);
                    blit(0, 0, 27, 0, 18, 28);
                    for (int i = 0; i < (width * (1 / scale)) - (17 * (1 / scale)); i++) {
                        blit(17 + i, 0, 45, 0, 2, 28);
                    }
                    blit((int)(width * (1 / scale)) - 17, 0, 47, 0, 17, 28);
                }
                RenderSystem.popMatrix();
            }
            ItemCategory category = Utils.getCategoryForStack(stack);
            RenderSystem.pushMatrix();
            {
                RenderSystem.translatef(x + 3, y + 2, 0);
                float scale = 0.5F;
                int categorySize = 20;
                RenderSystem.scalef(scale, scale, 1);
                blit(0, 0, category.getU(), category.getV(), categorySize, categorySize);
            }
            RenderSystem.popMatrix();
            drawString(mc.fontRenderer, stack.getDisplayName().getString(), x + 15, y + 3, 0xFFFFFF);
            String count = new TranslationTextComponent("x%s ", stack.getCount()).getUnformattedComponentText();
            drawString(mc.fontRenderer, count, x + width - mc.fontRenderer.getStringWidth(count), y + 3, 0xF8F711);
        }
    }

    @Override
    public void playDownSound(SoundHandler soundHandler) {
        soundHandler.play(SimpleSound.master(ModSounds.menu_select.get(), 1.0F, 1.0F));
    }
}



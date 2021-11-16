package online.kingdomkeys.kingdomkeys.client.gui.elements.buttons;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.api.item.ItemCategory;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuFilterable;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class MenuStockItem extends Button {

	MenuFilterable parent;
    ItemStack stack;
    boolean selected, showAmount;
    String customName = null;

    public MenuStockItem(MenuFilterable parent, ItemStack stack, int x, int y, int width, boolean showAmount) {
        super(x, y, width, 14, new TranslationTextComponent(""), b -> {
        	parent.action(stack);
        });
        this.parent = parent;
        this.stack = stack;
        this.showAmount = showAmount;
    }
    
    public MenuStockItem(MenuFilterable parent, ItemStack stack, int x, int y, int width, boolean showAmount, String customName) {
		this(parent,stack,x,y,width,showAmount);
		this.customName = customName;
	}

	@Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        isHovered = mouseX > x && mouseY >= y && mouseX < x + width && mouseY < y + height;
        RenderSystem.color4f(1, 1, 1, 1);
        if (visible) {
            Minecraft mc = Minecraft.getInstance();
            mc.getTextureManager().bindTexture(new ResourceLocation(KingdomKeys.MODID, "textures/gui/menu/menu_button.png"));
            if (isHovered || parent.selected == stack) {
                matrixStack.push();
                {
                    RenderSystem.enableBlend();
                    
                    matrixStack.translate(x + 0.6F, y, 0);
                    float scale = 0.5F;
                    matrixStack.scale(scale, scale, 1);
                    blit(matrixStack, 0, 0, 27, 0, 18, 28);
                    for (int i = 0; i < (width * (1 / scale)) - (17 * (1 / scale)); i++) {
                        blit(matrixStack, 17 + i, 0, 45, 0, 2, 28);
                    }
                    blit(matrixStack, (int)(width * (1 / scale)) - 17, 0, 47, 0, 17, 28);
                }
                matrixStack.pop();
            }
            ItemCategory category = Utils.getCategoryForStack(stack);
            matrixStack.push();
            {
                matrixStack.translate(x + 3, y + 2, 0);
                float scale = 0.5F;
                int categorySize = 20;
                matrixStack.scale(scale, scale, 1);
                blit(matrixStack, 0, 0, category.getU(), category.getV(), categorySize, categorySize);
            }
            matrixStack.pop();
            drawString(matrixStack, mc.fontRenderer, customName == null ? stack.getDisplayName().getString() : customName, x + 15, y + 3, 0xFFFFFF); //If it's a keychain it will show the keyblade name

            if(showAmount) {
	            String count = new TranslationTextComponent("x%s ", stack.getCount()).getString();
	            drawString(matrixStack, mc.fontRenderer, count, x + width - mc.fontRenderer.getStringWidth(count), y + 3, 0xF8F711);
            }
        }
    }

    @Override
    public void playDownSound(SoundHandler soundHandler) {
   		soundHandler.play(SimpleSound.master(ModSounds.menu_select.get(), 1.0F, 1.0F));
    }
}



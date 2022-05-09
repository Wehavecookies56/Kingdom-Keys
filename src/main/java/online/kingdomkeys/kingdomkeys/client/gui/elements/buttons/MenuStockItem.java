package online.kingdomkeys.kingdomkeys.client.gui.elements.buttons;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.api.item.ItemCategory;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuFilterable;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class MenuStockItem extends Button {

	MenuFilterable parent;
    ResourceLocation rl;
    ItemStack stack;
    boolean selected, showAmount;
    String customName = null;

    public MenuStockItem(MenuFilterable parent, ResourceLocation rl, ItemStack displayStack, int x, int y, int width, boolean showAmount) {
        super(x, y, width, 14, new TranslatableComponent(""), b -> {
        	parent.action(rl, displayStack);
        });
        this.parent = parent;
        this.rl = rl;
        this.showAmount = showAmount;
        this.stack = displayStack;
    }

    public MenuStockItem(MenuFilterable parent, ItemStack stack, int x, int y, int width, boolean showAmount) {
        super(x, y, width, 14, new TranslatableComponent(""), b -> {
            parent.action(stack.getItem().getRegistryName(), stack);
        });
        this.parent = parent;
        this.rl = stack.getItem().getRegistryName();
        this.stack = stack;
        this.showAmount = showAmount;
    }
    
    public MenuStockItem(MenuFilterable parent, ResourceLocation rl, ItemStack displayStack, int x, int y, int width, boolean showAmount, String customName) {
		this(parent,rl,displayStack,x,y,width,showAmount);
		this.customName = customName;
	}

    public MenuStockItem(MenuFilterable parent, ItemStack stack, int x, int y, int width, boolean showAmount, String customName) {
        this(parent,stack,x,y,width,showAmount);
        this.customName = customName;
    }

	@Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        isHovered = mouseX > x && mouseY >= y && mouseX < x + width && mouseY < y + height;
        RenderSystem.setShaderColor(1, 1, 1, 1);
        if (visible) {
            Minecraft mc = Minecraft.getInstance();
            RenderSystem.setShaderTexture(0, new ResourceLocation(KingdomKeys.MODID, "textures/gui/menu/menu_button.png"));
    		if (parent.selectedItemStack == null) {
                parent.selectedItemStack = new ItemStack(ForgeRegistries.ITEMS.getValue(parent.selectedRL));
            }
            if (isHovered || parent.selectedItemStack == stack) {
                matrixStack.pushPose();
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
                matrixStack.popPose();
            }
            ItemCategory category = Utils.getCategoryForStack(stack);
            matrixStack.pushPose();
            {
                matrixStack.translate(x + 3, y + 2, 0);
                float scale = 0.5F;
                int categorySize = 20;
                matrixStack.scale(scale, scale, 1);
                blit(matrixStack, 0, 0, category.getU(), category.getV(), categorySize, categorySize);
            }
            matrixStack.popPose();
            drawString(matrixStack, mc.font, customName == null ? stack.getHoverName().getString() : customName, x + 15, y + 3, 0xFFFFFF); //If it's a keychain it will show the keyblade name

            if(showAmount) {
	            String count = new TranslatableComponent("x%s ", stack.getCount()).getString();
	            drawString(matrixStack, mc.font, count, x + width - mc.font.width(count), y + 3, 0xF8F711);
            }
        }
    }

    @Override
    public void playDownSound(SoundManager soundHandler) {
   		soundHandler.play(SimpleSoundInstance.forUI(ModSounds.menu_select.get(), 1.0F, 1.0F));
    }
}



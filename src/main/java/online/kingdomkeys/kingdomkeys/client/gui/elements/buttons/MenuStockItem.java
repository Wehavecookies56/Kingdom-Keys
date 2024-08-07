package online.kingdomkeys.kingdomkeys.client.gui.elements.buttons;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
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
    public int offsetY;

    final ResourceLocation texture = new ResourceLocation(KingdomKeys.MODID, "textures/gui/menu/menu_button.png");

    public MenuStockItem(MenuFilterable parent, ResourceLocation rl, ItemStack displayStack, int x, int y, int width, boolean showAmount) {
    	super(new Builder(Component.literal(""), b -> {
            parent.action(rl, displayStack);
        }).bounds(x, y, width, 14));

        this.parent = parent;
        this.rl = rl;
        this.showAmount = showAmount;
        this.stack = displayStack;
    }

    public MenuStockItem(MenuFilterable parent, ItemStack stack, int x, int y, int width, boolean showAmount) {
    	super(new Builder(Component.literal(""), b -> {
            parent.action(ForgeRegistries.ITEMS.getKey(stack.getItem()), stack);
        }).bounds(x, y, width, 14));

    	this.parent = parent;
        this.rl = ForgeRegistries.ITEMS.getKey(stack.getItem());
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
    public int getY() {
        return super.getY() - offsetY;
    }

	@Override
    public void render(GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
        PoseStack matrixStack = gui.pose();
		isHovered = mouseX > getX() && mouseY >= getY() && mouseX < getX() + width && mouseY < getY() + height;
        RenderSystem.setShaderColor(1, 1, 1, 1);
        if (visible) {
            Minecraft mc = Minecraft.getInstance();
    		if (parent.selectedItemStack == null) {
                parent.selectedItemStack = new ItemStack(ForgeRegistries.ITEMS.getValue(parent.selectedRL));
            }
            if (isHovered || parent.selectedItemStack == stack) {
                matrixStack.pushPose();
                {
                    RenderSystem.enableBlend();
                    
                    matrixStack.translate(getX() + 0.6F, getY(), 0);
                    float scale = 0.5F;
                    matrixStack.scale(scale, scale, 1);
                    gui.blit(texture, 0, 0, 27, 0, 18, 28);
                    gui.blit(texture, 16, 0, (int) ((width * (1 / scale)) - (17 * (1 / scale)))+1, 28, 47, 0, 2, 28, 256, 256);
                    gui.blit(texture, (int)(width * (1 / scale)) - 18, 0, 47, 0, 17, 28);
                }
                matrixStack.popPose();
            }
            ItemCategory category = Utils.getCategoryForStack(stack);
            matrixStack.pushPose();
            {
                matrixStack.translate(getX() + 3, getY() + 2, 0);
                float scale = 0.5F;
                int categorySize = 20;
                matrixStack.scale(scale, scale, 1);
                gui.blit(texture, 0, 0, category.getU(), category.getV(), categorySize, categorySize);
            }
            matrixStack.popPose();
            gui.drawString(mc.font, customName == null ? stack.getHoverName().getString() : customName, getX() + 15, getY() + 3, 0xFFFFFF); //If it's a keychain it will show the keyblade name

            if(showAmount) {
	            String count = Component.translatable("x%s ", stack.getCount()).getString();
	            gui.drawString(mc.font, count, getX() + width - mc.font.width(count), getY() + 3, 0xF8F711);
            }
        }
    }

    @Override
    public void playDownSound(SoundManager soundHandler) {
   		soundHandler.play(SimpleSoundInstance.forUI(ModSounds.menu_select.get(), 1.0F, 1.0F));
    }
}



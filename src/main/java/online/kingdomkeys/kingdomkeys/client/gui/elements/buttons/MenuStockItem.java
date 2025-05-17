package online.kingdomkeys.kingdomkeys.client.gui.elements.buttons;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.api.item.ItemCategory;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.gui.elements.MenuFilterable;
import online.kingdomkeys.kingdomkeys.client.gui.synthesis.ShopScreen;
import online.kingdomkeys.kingdomkeys.client.gui.synthesis.SynthesisCreateScreen;
import online.kingdomkeys.kingdomkeys.client.gui.synthesis.SynthesisForgeScreen;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.item.KeybladeItem;
import online.kingdomkeys.kingdomkeys.item.KeychainItem;
import online.kingdomkeys.kingdomkeys.synthesis.recipe.Recipe;
import online.kingdomkeys.kingdomkeys.synthesis.recipe.RecipeRegistry;
import online.kingdomkeys.kingdomkeys.synthesis.shop.ShopItem;
import online.kingdomkeys.kingdomkeys.synthesis.shop.ShopList;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

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

            ChatFormatting color = ChatFormatting.WHITE;
            IPlayerCapabilities playerData = ModCapabilities.getPlayer(mc.player);

            boolean displayTick = false;
            if(parent instanceof SynthesisCreateScreen){
                displayTick = true;
                color = ChatFormatting.DARK_GRAY;

                if(RecipeRegistry.getInstance().containsKey(rl)){
                    Recipe recipe = RecipeRegistry.getInstance().getValue(rl);
                    if((recipe.getTier() <= playerData.getSynthLevel() || !ModConfigs.requireSynthTier) && playerData.getMunny() >= recipe.getCost()) {
                        color = ChatFormatting.WHITE;

                        for (Map.Entry<Item, Integer> m : recipe.getMaterials().entrySet()) {
                            if (playerData.getMaterialAmount(m.getKey()) < m.getValue()) {
                                color = ChatFormatting.DARK_GRAY;
                            }
                        }
                    }

                }
            }

            if(parent instanceof SynthesisForgeScreen){
                displayTick = true;
                color = ChatFormatting.DARK_GRAY;
                if(stack.getItem() instanceof KeychainItem kcItem) {
                    KeybladeItem item = kcItem.getKeyblade();
                    if(item.getKeybladeLevel(stack) >= item.getMaxLevel()){
                        color = ChatFormatting.GOLD;
                    } else {
                        Iterator<Map.Entry<Item, Integer>> itMats = item.data.getLevelData(item.getKeybladeLevel(stack)).getMaterialList().entrySet().iterator();
                        color = ChatFormatting.WHITE;
                        while (itMats.hasNext()) { //Check if the player has the materials
                            Map.Entry<Item, Integer> m = itMats.next();

                            if (playerData.getMaterialAmount(m.getKey()) < m.getValue()) {
                                color = ChatFormatting.DARK_GRAY;
                            }
                        }
                    }
                }
            }

            if(parent instanceof ShopScreen shop){
                displayTick = true;
                ShopList shopList = shop.getShopList();
                for(ShopItem item : shopList.getList()){
                    if(rl.equals(Utils.getItemRegistryName(item.getResult()))){
                        if(item.getCost() > playerData.getMunny()) {
                            color = ChatFormatting.DARK_GRAY;
                        } else {
                            color = ChatFormatting.WHITE;
                        }
                        break;
                    }
                }
                // System.out.println(shopList.getList().;
            }
            gui.drawString(mc.font,color+(customName == null ? stack.getHoverName().getString() : customName), getX() + 15, getY() + 3, 0xFFFFFF); //If it's a keychain it will show the keyblade name

            if(displayTick) {
                Set<String> recipeList = playerData.getSynthesisedRecipes();
                if(recipeList.contains(rl.toString())) {
                    gui.drawString(mc.font, "✔", (int)(getWidth() * 1.49), getY() + 3, 0x00FF00); //If it's a keychain it will show the keyblade name
                }
            }
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



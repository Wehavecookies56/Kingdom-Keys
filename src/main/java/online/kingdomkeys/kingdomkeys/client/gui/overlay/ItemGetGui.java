package online.kingdomkeys.kingdomkeys.client.gui.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Axis;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.api.item.IItemCategory;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.client.gui.elements.TextBox;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.lib.Constants;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.util.GuiStringBuilder;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.*;

public class ItemGetGui extends OverlayBase {

    public static final ItemGetGui INSTANCE = new ItemGetGui();

    private static final ResourceLocation glow = KingdomKeys.rl("textures/gui/org/glow.png");
    private static Queue<ItemStack> items = new ArrayDeque<>();
    private float ticks = 0;
    private ItemStack current;

    private int titleY;
    private int spacing;
    int titleWidth = 300;
    int titleHeight = 30;
    int screenHeightWhenAdjusted;
    TextBox box = TextBox.create(0, 0, 300, 10).autoExpand().padding(20, 15).build();
    TextBox imageBox = TextBox.create(0, 60, 150, 150).image(new TextBox.ImageProperties(0, 0, 0, 0), render -> {
        float itemWidth = (render.width() / 1.3F);
        float itemHeight = (render.height() / 1.3F);

        float glowWidth = (render.width() / 0.9F);
        float glowHeight = (render.height() / 0.9F);

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(render.x() + (render.width() / 2F) - (glowWidth / 2F), render.y()  + (render.height() / 2F) - (glowHeight / 2F), 0);
        guiGraphics.pose().scale((1F/256F) * glowWidth, (1F/256F) * glowHeight, 1);
        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(198/255F, 140/255F, 13/255F, 1);
        guiGraphics.blit(glow, 0, 0, 256, 256, 0, 0, 256, 256, 256, 256);
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.disableBlend();
        guiGraphics.pose().popPose();

        float animTicks = ticks + minecraft.getTimer().getGameTimeDeltaPartialTick(false);

        float growDuration = 10F;
        float growT = Math.min(animTicks / growDuration, 1F);

        float duration = 20F;
        float t = Math.min(animTicks / duration, 1F);

        float scale;

        if (t == 0F) {
            scale = 0F;
        } else {
            float c4 = (float) (2 * Math.PI / 3);
            scale = (float) (Math.pow(2, -10 * t) * Math.sin((t * 10 - 0.75F) * c4) + 1);
        }

        float rotationT = Math.min(animTicks / 6F, 1F);
        float rotation = (1F - rotationT) * 360F;

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(8 * ((1F/16F) * itemWidth), 8 * ((1F/16F) * itemHeight), 0);
        guiGraphics.pose().translate(render.x() + (render.width() / 2F) - (itemWidth / 2F), render.y()  + (render.height() / 2F) - (itemHeight / 2F), 0);
        guiGraphics.pose().mulPose(Axis.ZP.rotationDegrees(rotation));
        guiGraphics.pose().scale((((1F/16F) * itemWidth) + scale) * growT, (((1F/16F) * itemHeight) + scale) * growT, 1);
        guiGraphics.renderItem(current, -8, -8);
        guiGraphics.pose().popPose();
    }).padding(0, 0).build();

    public MutableComponent toolTipToSingleComponent(ItemStack stack) {
        List<String> components = new ArrayList<>(stack.getTooltipLines(Item.TooltipContext.of(minecraft.level), minecraft.player, Minecraft.getInstance().options.advancedItemTooltips ? TooltipFlag.ADVANCED : TooltipFlag.NORMAL).stream().map(Component::getString).toList());
        components.removeFirst();
        StringBuilder output = new StringBuilder();
        Iterator<String> iterator = components.iterator();
        while (iterator.hasNext()) {
            output.append(iterator.next());
            if (iterator.hasNext()) {
                output.append("\n");
            }
        }
        return Component.literal(output.toString());
    }

    public void renderTitle(GuiGraphics guiGraphics, int screenWidth, int screenHeight) {
        int titleX = (screenWidth / 2) - (titleWidth / 2);
        int u = 205;
        int v = 63;
        int endWidth = 14;
        int lineWidth = titleWidth / 2;
        //box
        guiGraphics.blit(Constants.MENU_TEXTURE, titleX, titleY, u, v, endWidth, titleHeight);
        guiGraphics.blit(Constants.MENU_TEXTURE, titleX + endWidth, titleY, titleWidth - (endWidth * 2), titleHeight, u + endWidth + 1, v, 1, titleHeight, 256, 256);
        guiGraphics.blit(Constants.MENU_TEXTURE, titleX + titleWidth - endWidth, titleY, u + endWidth + 3, v, endWidth, titleHeight);
        //obtained text
        GuiStringBuilder.create(Component.translatable(Strings.Gui_ItemGet_Obtained).withStyle(ClientUtils.KK_Font_EXP).withColor(0xfff200), titleX + (titleWidth / 2), titleY + 3).centered().draw(guiGraphics);
        //underline
        RenderSystem.enableBlend();
        guiGraphics.blit(Constants.MENU_TEXTURE, titleX + (titleWidth / 2) - (lineWidth / 2), titleY + font.lineHeight + 3, lineWidth, 1, 144, 101, 68, 1, 256, 256);
        RenderSystem.disableBlend();

        //name text
        String name = current.getDisplayName().getString();
        GuiStringBuilder.create(Component.literal(name.substring(1, name.length()-1)).withStyle(ClientUtils.KK_Font_MENU), titleX + (titleWidth / 2), titleY + titleHeight - font.lineHeight - 3).centered().draw(guiGraphics);
    }

    @Override
    public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        if (current != null) {
            super.render(guiGraphics, deltaTracker);
            int screenWidth = minecraft.getWindow().getGuiScaledWidth();
            int screenHeight = minecraft.getWindow().getGuiScaledHeight();
            int defaultSpacing = 10;
            int defaultTitleY = 20;
            int currentHeight = titleHeight + spacing + imageBox.getHeight() + spacing + box.getHeight();
            if (currentHeight > screenHeight) {
                int oldTitleY = titleY;
                screenHeightWhenAdjusted = screenHeight;
                int difference = currentHeight - screenHeight;
                titleY = Math.max(1, oldTitleY - difference);
                if (titleY == 1) {
                    difference -= oldTitleY;
                    spacing = Math.max(1, spacing - difference);
                }
            }
            if (screenHeightWhenAdjusted != screenHeight) {
                screenHeightWhenAdjusted = screenHeight;
                spacing = defaultSpacing;
                int totalHeight = titleHeight + defaultSpacing + imageBox.getHeight() + defaultSpacing + box.getHeight();
                titleY = (screenHeight / 2) - (totalHeight / 2);
            }
            imageBox.setX((screenWidth / 2) - (imageBox.getWidth() / 2));
            imageBox.setY(titleY + titleHeight + spacing);
            box.setX((screenWidth / 2) - (box.getWidth() / 2));
            box.setY(imageBox.getY() + imageBox.getHeight() + spacing);
            box.render(guiGraphics, 0, 0, deltaTracker.getGameTimeDeltaPartialTick(true));
            imageBox.render(guiGraphics, 0, 0, deltaTracker.getGameTimeDeltaPartialTick(true));
            renderTitle(guiGraphics, screenWidth, screenHeight);
        }
        guiGraphics.flush();
        guiGraphics.managed = false;
    }

    public void addItemsToDisplay(List<ItemStack> stacks, boolean mini) {
        if (!mini) {
            items.addAll(stacks);
        }
    }

    public void clearItems() {
        if (!items.isEmpty()) {
            items = new ArrayDeque<>();
            current = null;
        }
    }

    public void tick() {
        if (current != null) {
            ticks++;
            if (ticks > 200) {
                click();
            }
        } else if (!items.isEmpty()) {
            current = items.poll();
            ticks = 0;
            Minecraft.getInstance().player.playSound(ModSounds.itemget.get(), 1, 1);
            String desc = Utils.createDescriptionKey(current);
            if (current.getItem() instanceof IItemCategory iItemCategory) {
                desc = iItemCategory.getDescriptionKey(current);
            }
            Component translatedDesc = Component.translatable(desc).withStyle(ClientUtils.KK_Font_MENU);
            if (translatedDesc.getString().startsWith("item.")) {
                box.setMessage(toolTipToSingleComponent(current).withStyle(ClientUtils.KK_Font_MENU));
            } else {
                box.setMessage(translatedDesc);
            }
        }
    }

    public void click() {
        if (current != null && Minecraft.getInstance().screen == null) {
            current = null;
        }
    }
}

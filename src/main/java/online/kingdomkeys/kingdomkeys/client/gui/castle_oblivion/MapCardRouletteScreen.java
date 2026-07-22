package online.kingdomkeys.kingdomkeys.client.gui.castle_oblivion;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.item.card.MapCardItem;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSGiveMapCard;

import java.util.ArrayList;
import java.util.List;

public class MapCardRouletteScreen extends Screen {

    private static final ResourceLocation OUTLINE = KingdomKeys.rl("textures/gui/co/card_outline.png");

    public static class CardIcon {
        ItemStack card;
        float x, y;

        public CardIcon(ItemStack card) {
            this.card = card;
        }

        public void render(GuiGraphics guiGraphics) {
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate((int) x - 8, (int) y - 8, 0);
            guiGraphics.pose().scale(4, 4, 1);
            guiGraphics.renderItem(card, 0, 0);
            guiGraphics.pose().translate(10, 12, 151);
            guiGraphics.pose().scale(0.5F, 0.5F, 1);
            guiGraphics.drawString(Minecraft.getInstance().font, MapCardItem.getCardValue(card)+"", 0, 0, 0xFFFFFF);
            guiGraphics.pose().popPose();
        }

        public void setPos(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }

    List<CardIcon> cards = new ArrayList<>();

    int currentIndex;

    int ticks;

    final int interval = 3;

    float outlineX, outlineY;
    boolean stopped;

    //Construct with cards from server
    public MapCardRouletteScreen(List<ItemStack> cards) {
        super(Component.translatable("kingdomkeys.gui.co.map_card_roulette.title"));
        if (cards.isEmpty()) {
            KingdomKeys.LOGGER.error("Tried to open Map Card Roulette with no cards");
            onClose();
        }
        cards.forEach(stack -> this.cards.add(new CardIcon(stack)));
    }

    @Override
    protected void init() {
        int screenWidth = minecraft.screen.width;
        int screenHeight = minecraft.screen.height;

        int radius = 180;
        int centerX = 0;
        int centerY = screenHeight - (radius / 2);

        for (int i = 0; i < cards.size(); ++i) {
            float cX = centerX + radius * Mth.cos(2 * Mth.PI * i / cards.size());
            float cY = centerY + radius * Mth.sin(2 * Mth.PI * i / cards.size());
            cards.get(i).setPos(cX, cY);
        }

        if (!cards.isEmpty()) {
            outlineX = cards.getFirst().x;
            outlineY = cards.getFirst().y;
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        cards.forEach(cardIcon -> cardIcon.render(guiGraphics));
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate((int) outlineX - 8, (int) outlineY - 8, 1000);
        guiGraphics.pose().scale(2, 2, 0);
        guiGraphics.blit(OUTLINE, 0, 0, 0, 0, 32, 32, 32, 32);
        guiGraphics.pose().popPose();

        int screenWidth = minecraft.screen.width;
        int screenHeight = minecraft.screen.height;

        int radius = 180;
        int centerX = 0;
        int centerY = screenHeight - (radius / 2);

        for (int i = 0; i < cards.size(); ++i) {
            int shiftedIndex = i + currentIndex+1;
            if (shiftedIndex >= cards.size()) {
                shiftedIndex = shiftedIndex - cards.size();
            }
            float cX = centerX + radius * Mth.cos(2 * Mth.PI * shiftedIndex / cards.size());
            float cY = centerY + radius * Mth.sin(2 * Mth.PI * shiftedIndex / cards.size());
            cards.get(i).setPos(cX, cY);
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        stop();
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            stop();
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    public void stop() {
        stopped = true;
        KingdomKeys.LOGGER.debug("Current: {}, {}", currentIndex, cards.get(cards.size() - 1 -  currentIndex).card);
        PacketHandler.sendToServer(new CSGiveMapCard(cards.get(cards.size() - 1 - currentIndex).card));
        onClose();
    }

    @Override
    public void tick() {
        if (!stopped) {
            ticks++;
            if (ticks % interval == 0) {
                minecraft.level.playLocalSound(minecraft.player, ModSounds.menu_move.get(), SoundSource.NEUTRAL, 1, 1);
                currentIndex++;
                if (currentIndex == cards.size()) {
                    currentIndex = 0;
                }
                //KingdomKeys.LOGGER.debug("Current: {}, {}", currentIndex, cards.get(cards.size() - 1 -  currentIndex).card);
            }
        }
    }
}

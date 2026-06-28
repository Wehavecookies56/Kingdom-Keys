package online.kingdomkeys.kingdomkeys.item.card;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import online.kingdomkeys.kingdomkeys.item.ICreativeTab;
import online.kingdomkeys.kingdomkeys.item.ModComponents;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.RoomType;

import java.util.List;
import java.util.function.Supplier;

public class MapCardItem extends Item implements ICreativeTab {
    CardCategory category;

    private final Supplier<RoomType> type;
    boolean hasRandomValue;
    private KeycardType keycardType;
    private boolean wip;

    public MapCardItem(Supplier<RoomType> type, CardCategory category) {
        this(type, category, false);
    }

    public MapCardItem(Supplier<RoomType> type, CardCategory category, boolean wip) {
        super(new Properties().fireResistant());
        this.wip = wip;
        this.type = type;
        this.category = category;
        this.hasRandomValue = true;
    }

    public MapCardItem(Supplier<RoomType> type, KeycardType keycardType) {
        super(new Properties().fireResistant());
        this.type = type;
        this.category = CardCategory.YELLOW;
        this.hasRandomValue = false;
        this.keycardType = keycardType;
    }

    public RoomType getRoomType() {
        return type.get();
    }

    public CardCategory getCategory() {
        return category;
    }

    public boolean hasRandomValue() {
        return hasRandomValue;
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        if (!pStack.has(ModComponents.CARD_VALUE)) {
            generateValue(pStack);
        } else if (hasRandomValue) {
            pStack.set(DataComponents.ITEM_NAME, Component.translatable("item.mapcard.prefix", getCardValue(pStack), Component.translatable("item." + BuiltInRegistries.ITEM.getKey(this).getNamespace() + "." + BuiltInRegistries.ITEM.getKey(this).getPath())).setStyle(Style.EMPTY.withItalic(false)));
        }
        super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);
    }

    private void generateValue(ItemStack stack) {
        stack.set(ModComponents.CARD_VALUE, hasRandomValue ? Utils.randomWithRange(0, 9) : keycardType.ordinal());
    }

    public static int getCardValue(ItemStack stack) {
        if (stack.has(ModComponents.CARD_VALUE)) {
            return stack.get(ModComponents.CARD_VALUE);
        }
        return -1;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        if (player.isCreative() && player.isCrouching()) {
            ItemStack card = player.getItemInHand(usedHand);
            if (card.getItem() instanceof MapCardItem mapCardItem) {
                if (mapCardItem.getCategory() != CardCategory.YELLOW && mapCardItem.getCategory() != CardCategory.RGB) {
                    int value = MapCardItem.getCardValue(card);
                    if (value == -1) {
                        card.set(ModComponents.CARD_VALUE, 0);
                    } else {
                        value = value == 9 ? 0 : ++value;
                        card.set(ModComponents.CARD_VALUE, value);
                    }
                }
            }
        }
        return super.use(level, player, usedHand);
    }

    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext tooltipContext, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if (wip) {
            pTooltipComponents.add(Component.translatable("DOES NOT WORK YET").withStyle(ChatFormatting.RED));
        }
        if (type != null && hasRandomValue) {
            RoomType inst = type.get();
            pTooltipComponents.add(Component.translatable("Size: " + inst.getSize().getStars()).withStyle(ChatFormatting.YELLOW));
            pTooltipComponents.add(Component.translatable("Enemies: " + inst.getEnemies().getStars()).withStyle(ChatFormatting.YELLOW));
        }
        super.appendHoverText(pStack, tooltipContext, pTooltipComponents, pIsAdvanced);
    }

    @Override
    public Tab getTab() {
        return Tab.CARDS;
    }

    public boolean isWIP() {
        return wip;
    }
}

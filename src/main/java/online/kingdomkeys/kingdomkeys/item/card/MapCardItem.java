package online.kingdomkeys.kingdomkeys.item.card;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import online.kingdomkeys.kingdomkeys.item.ModComponents;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.RoomType;

import java.util.List;
import java.util.function.Supplier;

public class MapCardItem extends Item {

    private final Supplier<RoomType> type;

    public MapCardItem(Supplier<RoomType> type) {
        super(new Properties().fireResistant());
        this.type = type;
    }

    public RoomType getRoomType() {
        return type.get();
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        if (!pStack.has(ModComponents.CARD_VALUE)) {
            generateValue(pStack);
        } else {
            pStack.set(DataComponents.ITEM_NAME, Component.translatable("item.mapcard.prefix", getCardValue(pStack), Component.translatable("item." + BuiltInRegistries.ITEM.getKey(this).getNamespace() + "." + BuiltInRegistries.ITEM.getKey(this).getPath())).setStyle(Style.EMPTY.withItalic(false)));
        }
        super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);
    }

    private void generateValue(ItemStack stack) {
        stack.set(ModComponents.CARD_VALUE, Utils.randomWithRange(0, 9));
    }

    public int getCardValue(ItemStack stack) {
        if (stack.has(ModComponents.CARD_VALUE)) {
            return stack.get(ModComponents.CARD_VALUE);
        }
        return -1;
    }

    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext tooltipContext, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if (type != null) {
            pTooltipComponents.add(Component.translatable("Size: " + type.get().getProperties().getSize().ordinal()));
            pTooltipComponents.add(Component.translatable("Enemies: " + type.get().getProperties().getEnemies().ordinal()));
        }
        super.appendHoverText(pStack, tooltipContext, pTooltipComponents, pIsAdvanced);
    }
}

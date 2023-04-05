package online.kingdomkeys.kingdomkeys.item.card;

import java.util.List;
import java.util.function.Supplier;

import org.jetbrains.annotations.Nullable;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.RoomType;

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
        if (pStack.getTag() == null) {
            pStack.setTag(new CompoundTag());
        } else if (pStack.getTag().get("value") == null) {
            generateValue(pStack);
        } else {
            pStack.setHoverName(Component.translatable("item.mapcard.prefix", getCardValue(pStack), Component.translatable("item." + ForgeRegistries.ITEMS.getKey(this).getNamespace() + "." + ForgeRegistries.ITEMS.getKey(this).getPath())).setStyle(Style.EMPTY.withItalic(false)));
        }
        super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);
    }

    private void generateValue(ItemStack stack) {
        if (stack.getTag() != null) {
            stack.getTag().putInt("value", Utils.randomWithRange(0, 9));
        }
    }

    public int getCardValue(ItemStack stack) {
        if (stack.getTag() != null) {
            if (stack.getTag().get("value") != null) {
                return stack.getTag().getInt("value");
            }
        }
        return -1;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if (type != null) {
            pTooltipComponents.add(Component.translatable("Size: " + type.get().getProperties().getSize().ordinal()));
            pTooltipComponents.add(Component.translatable("Enemies: " + type.get().getProperties().getEnemies().ordinal()));
        }
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }
}

package online.kingdomkeys.kingdomkeys.item.card;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.ModRoomTypes;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.RoomProperties;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.RoomType;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MinglingWorldsMapCardItem extends MapCardItem {
    public MinglingWorldsMapCardItem() {
        super(null);
    }

    @Override
    public RoomType getRoomType() {
        List<RoomType> types = ModRoomTypes.registry.stream().filter(roomType -> roomType.getProperties().getCategory() != RoomProperties.RoomCategory.SPECIAL).toList();
        return types.get(Utils.randomWithRange(0, types.size()-1));
    }

    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext context, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("Size: ?"));
        pTooltipComponents.add(Component.translatable("Enemies: ?"));
        super.appendHoverText(pStack, context, pTooltipComponents, pIsAdvanced);
    }

}

package online.kingdomkeys.kingdomkeys.item.card;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import online.kingdomkeys.kingdomkeys.item.ICreativeTab;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.floor.FloorType;

import java.util.List;
import java.util.function.Supplier;

public class WorldCardItem extends Item implements ICreativeTab {

    private final Supplier<FloorType> floorType;
    //TODO Remove when we've done all of them
    public boolean implemented;

    public WorldCardItem(Supplier<FloorType> floorType, boolean implemented) {
        super(new Properties());
        this.floorType = floorType;
        this.implemented = implemented;
    }

    public WorldCardItem(Supplier<FloorType> floorType) {
        this(floorType, false);
    }

    public FloorType getFloorType() {
        return floorType.get();
    }

    @Override
    public Tab getTab() {
        return Tab.CARDS;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        //TODO Remove when all floors are done
        if (stack.getItem() instanceof WorldCardItem worldCardItem && !worldCardItem.implemented) {
            tooltipComponents.add(Component.literal("DO NOT USE, NOT FUNCTIONAL YET").withStyle(ChatFormatting.RED));
        }
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}

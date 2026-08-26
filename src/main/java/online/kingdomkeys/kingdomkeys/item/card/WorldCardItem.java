package online.kingdomkeys.kingdomkeys.item.card;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import online.kingdomkeys.kingdomkeys.item.ICreativeTab;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.floor.FloorType;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry.ModFloorTypes;

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
            tooltipComponents.add(Component.translatable("kingdomkeys.card.not_functional").withStyle(ChatFormatting.RED));
        }
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        return super.useOn(context);
    }

    public record WorldCard(ResourceLocation floorType) {
        public static final Codec<WorldCard> CODEC = ResourceLocation.CODEC.xmap(WorldCard::new, WorldCard::floorType);
        public static final StreamCodec<ByteBuf, WorldCard> STREAM_CODEC = ResourceLocation.STREAM_CODEC.map(WorldCard::new, WorldCard::floorType);

        public FloorType getFloorType() {
            return ModFloorTypes.registry.get().getValue(floorType);
        }
    }
}

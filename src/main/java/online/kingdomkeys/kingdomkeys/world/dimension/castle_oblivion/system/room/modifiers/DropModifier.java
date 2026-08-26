package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.modifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ItemStack;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry.ModRoomModifiers;

public class DropModifier implements RoomModifier {

    boolean replaceCard;
    ItemStack item;
    int chance;

    public static final MapCodec<DropModifier> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    ItemStack.CODEC.fieldOf("item").forGetter(DropModifier::getItem),
                    Codec.BOOL.optionalFieldOf("replace_card", false).forGetter(DropModifier::replaceCard),
                    Codec.intRange(1, 100).optionalFieldOf("chance", 100).forGetter(DropModifier::getChance)
            ).apply(instance, DropModifier::new)
    );

    public DropModifier(ItemStack item, boolean replaceCard, int chance) {
        this.replaceCard = replaceCard;
        this.item = item;
        this.chance = chance;
    }

    public boolean replaceCard() {
        return replaceCard;
    }

    public ItemStack getItem() {
        return item;
    }

    public int getChance() {
        return chance;
    }

    @Override
    public MapCodec<? extends RoomModifier> codec() {
        return CODEC;
    }

    @Override
    public RoomModifierType<? extends RoomModifier> type() {
        return ModRoomModifiers.DROP.get();
    }
}

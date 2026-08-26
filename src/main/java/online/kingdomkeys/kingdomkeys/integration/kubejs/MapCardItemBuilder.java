package online.kingdomkeys.kingdomkeys.integration.kubejs;

import dev.latvian.mods.kubejs.error.KubeRuntimeException;
import dev.latvian.mods.kubejs.item.ItemBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import online.kingdomkeys.kingdomkeys.item.card.CardCategory;
import online.kingdomkeys.kingdomkeys.item.card.MapCardItem;
import online.kingdomkeys.kingdomkeys.lib.ModTags;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry.ModRoomTypes;

public class MapCardItemBuilder extends ItemBuilder {

    ResourceLocation roomType;
    CardCategory category;

    public static final ResourceLocation[] MAP_CARD_TAGS = {
            ModTags.MAP_CARD.location(),
    };

    public MapCardItemBuilder(ResourceLocation id) {
        super(id);
        tag(MAP_CARD_TAGS);
        this.roomType = null;

    }

    public MapCardItemBuilder roomType(ResourceLocation location, CardCategory category) {
        this.roomType = location;
        this.category = category;
        return this;
    }

    @Override
    public Item createObject() {
        if (roomType == null) {
            throw new KubeRuntimeException("Missing room type for map card");
        }
        return new MapCardItem(() -> ModRoomTypes.registry.get().getValue(roomType), category);
    }
}

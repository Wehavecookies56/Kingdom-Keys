package online.kingdomkeys.kingdomkeys.integration.kubejs;

import dev.latvian.mods.kubejs.error.KubeRuntimeException;
import dev.latvian.mods.kubejs.item.ItemBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import online.kingdomkeys.kingdomkeys.item.card.WorldCardItem;
import online.kingdomkeys.kingdomkeys.lib.ModTags;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry.ModFloorTypes;

public class WorldCardItemBuilder extends ItemBuilder {

    ResourceLocation floorType;

    public static final ResourceLocation[] WORLD_CARD_TAGS = {
            ModTags.WORLD_CARD.location(),
    };

    public WorldCardItemBuilder(ResourceLocation id) {
        super(id);
        floorType = null;
        tag(WORLD_CARD_TAGS);
    }

    public WorldCardItemBuilder floorType(ResourceLocation resourceLocation) {
        this.floorType = resourceLocation;
        return this;
    }

    @Override
    public Item createObject() {
        if (floorType == null) {
            throw new KubeRuntimeException("Missing floor type for world card");
        }
        return new WorldCardItem(() -> ModFloorTypes.registry.get().getValue(floorType), true);
    }
}

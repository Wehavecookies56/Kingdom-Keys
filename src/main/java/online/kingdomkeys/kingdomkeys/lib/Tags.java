package online.kingdomkeys.kingdomkeys.lib;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.List;

public class Tags {

    public static final TagKey<Item>
            MATERIALS = TagKey.create(Registries.ITEM, new ResourceLocation(KingdomKeys.MODID, "synthesis/materials")),

            XEMNAS = TagKey.create(Registries.ITEM, new ResourceLocation(KingdomKeys.MODID, "organization/xemnas")),
            XIGBAR = TagKey.create(Registries.ITEM, new ResourceLocation(KingdomKeys.MODID, "organization/xigbar")),
            XALDIN = TagKey.create(Registries.ITEM, new ResourceLocation(KingdomKeys.MODID, "organization/xaldin")),
            VEXEN = TagKey.create(Registries.ITEM, new ResourceLocation(KingdomKeys.MODID, "organization/vexen")),
            LEXAEUS = TagKey.create(Registries.ITEM, new ResourceLocation(KingdomKeys.MODID, "organization/lexaeus")),
            ZEXION = TagKey.create(Registries.ITEM, new ResourceLocation(KingdomKeys.MODID, "organization/zexion")),
            SAIX = TagKey.create(Registries.ITEM, new ResourceLocation(KingdomKeys.MODID, "organization/saix")),
            AXEL = TagKey.create(Registries.ITEM, new ResourceLocation(KingdomKeys.MODID, "organization/axel")),
            DEMYX = TagKey.create(Registries.ITEM, new ResourceLocation(KingdomKeys.MODID, "organization/demyx")),
            LUXORD = TagKey.create(Registries.ITEM, new ResourceLocation(KingdomKeys.MODID, "organization/luxord")),
            MARLUXIA = TagKey.create(Registries.ITEM, new ResourceLocation(KingdomKeys.MODID, "organization/marluxia")),
            LARXENE = TagKey.create(Registries.ITEM, new ResourceLocation(KingdomKeys.MODID, "organization/larxene")),
            ROXAS = TagKey.create(Registries.ITEM, new ResourceLocation(KingdomKeys.MODID, "organization/roxas"));

    public static TagKey<Item> getTagForMember(Utils.OrgMember member) {
        return switch (member) {
            case AXEL -> AXEL;
            case DEMYX -> DEMYX;
            case LARXENE -> LARXENE;
            case LEXAEUS -> LEXAEUS;
            case LUXORD -> LUXORD;
            case MARLUXIA -> MARLUXIA;
            case ROXAS -> ROXAS;
            case SAIX -> SAIX;
            case VEXEN -> VEXEN;
            case XALDIN -> XALDIN;
            case XEMNAS -> XEMNAS;
            case XIGBAR -> XIGBAR;
            case ZEXION -> ZEXION;
            case NONE -> null;
        };
    }

    public static List<Item> getItemsInTag(Level level, TagKey<Item> tag) {
        return level.registryAccess().lookupOrThrow(Registries.ITEM).getOrThrow(tag).stream().map(Holder::value).toList();
    }

    public static Item getFirstItemInTag(Level level, TagKey<Item> tag) {
        List<Item> items = getItemsInTag(level, tag);
        if (items != null && !items.isEmpty()) {
            return getItemsInTag(level, tag).get(0);
        }
        KingdomKeys.LOGGER.error("Tried to get item from empty or non existent tag {}", tag.location().toString());
        return null;
    }

}

package online.kingdomkeys.kingdomkeys.lib;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.List;

public class ModTags {

    public static final TagKey<EntityType<?>>
            EMBLEM_HEARTLESS = entity("emblem_heartless"),
            HEARTLESS = entity("heartless"),
            NOBODY = entity("nobody"),
            CO_BOTTOMLESS_DARKNESS = entity("castle_oblivion/bottomless_darkness"),
            CO_WHITE_ROOM = entity("castle_oblivion/white_room"),
            CO_BLACK_ROOM = entity("castle_oblivion/black_room"),
            CO_REGULAR_ENEMIES = entity("castle_oblivion/regular_enemies"),
            CO_STRONG_ENEMIES = entity("castle_oblivion/strong_enemies");

    public static final TagKey<Item>
            MATERIALS = item("synthesis/materials"),

            XEMNAS = item("organization/xemnas"),
            XIGBAR = item("organization/xigbar"),
            XALDIN = item("organization/xaldin"),
            VEXEN = item("organization/vexen"),
            LEXAEUS = item("organization/lexaeus"),
            ZEXION = item("organization/zexion"),
            SAIX = item("organization/saix"),
            AXEL = item("organization/axel"),
            DEMYX = item("organization/demyx"),
            LUXORD = item("organization/luxord"),
            MARLUXIA = item("organization/marluxia"),
            LARXENE = item("organization/larxene"),
            ROXAS = item("organization/roxas"),

            KEYBLADES = item("keyblades"),
            KEYCHAINS = item("keychains"),
            MAGICS = item("magics"),
            DRIVES = item("drives"),
            ORG = item("org_weapons"),
            PAULDRONS = item("pauldrons"),
            ACCESSORIES = item("accessories"),
            ARMORS = item("armors"),
            MUSIC_DISCS = item("music_discs"),
            SYNTHESIS_MATERIAL = item("synthesis_material"),
            MAP_CARD = item("map_card"),
            WORLD_CARD = item("world_card"),
            KEY_CARD = item("key_card"),
            BIOME_MEMORY = item("biome_memory"),

            GUMMI_BLOCK_CUBE = item("gummi_block_cube"),
            GUMMI_BLOCK_WEDGE = item("gummi_block_wedge"),
            GUMMI_BLOCK_PYRAMID = item("gummi_block_pyramid"),
            GUMMI_BLOCK_CYLINDER = item("gummi_block_cylinder"),
            GUMMI_BLOCK_PIE = item("gummi_block_pie"),
            GUMMI_BLOCK_ROUND_CORNER = item("gummi_block_round_corner"),
            GUMMI_BLOCK_CONE = item("gummi_block_cone"),
            GUMMI_BLOCK_DOME = item("gummi_block_dome"),

            GUMMI_SHELL_BLOCK_CUBE = item("gummi_shell_block_cube"),
            GUMMI_SHELL_BLOCK_WEDGE = item("gummi_shell_block_wedge"),
            GUMMI_SHELL_BLOCK_PYRAMID = item("gummi_shell_block_pyramid"),
            GUMMI_SHELL_BLOCK_CYLINDER = item("gummi_shell_block_cylinder"),
            GUMMI_SHELL_BLOCK_PIE = item("gummi_shell_block_pie"),
            GUMMI_SHELL_BLOCK_ROUND_CORNER = item("gummi_shell_block_round_corner"),
            GUMMI_SHELL_BLOCK_CONE = item("gummi_shell_block_cone"),
            GUMMI_SHELL_BLOCK_DOME = item("gummi_shell_block_dome"),

            GUMMI_DISPEL_BLOCK_CUBE = item("gummi_dispel_block_cube"),
            GUMMI_DISPEL_BLOCK_WEDGE = item("gummi_dispel_block_wedge"),
            GUMMI_DISPEL_BLOCK_PYRAMID = item("gummi_dispel_block_pyramid"),
            GUMMI_DISPEL_BLOCK_CYLINDER = item("gummi_dispel_block_cylinder"),
            GUMMI_DISPEL_BLOCK_PIE = item("gummi_dispel_block_pie"),
            GUMMI_DISPEL_BLOCK_ROUND_CORNER = item("gummi_dispel_block_round_corner"),
            GUMMI_DISPEL_BLOCK_CONE = item("gummi_dispel_block_cone"),
            GUMMI_DISPEL_BLOCK_DOME = item("gummi_dispel_block_dome"),

            GUMMI_BLOCK_BUBBLE = item("gummi_block_bubble"),

            GUMMI_BLOCK_AERO_TRIANGLE = item("gummi_block_aero_triangle"),
            GUMMI_BLOCK_AERO_SQUARE = item("gummi_block_aero_square");

    public static final List<TagKey<Item>>
            GUMMI_BLOCK_KEYS = List.of(GUMMI_BLOCK_CUBE, GUMMI_BLOCK_WEDGE, GUMMI_BLOCK_PYRAMID, GUMMI_BLOCK_CYLINDER, GUMMI_BLOCK_PIE, GUMMI_BLOCK_ROUND_CORNER, GUMMI_BLOCK_CONE, GUMMI_BLOCK_DOME, GUMMI_BLOCK_AERO_SQUARE, GUMMI_BLOCK_AERO_TRIANGLE),
            GUMMI_SHELL_BLOCK_KEYS = List.of(GUMMI_SHELL_BLOCK_CUBE, GUMMI_SHELL_BLOCK_WEDGE, GUMMI_SHELL_BLOCK_PYRAMID, GUMMI_SHELL_BLOCK_CYLINDER, GUMMI_SHELL_BLOCK_PIE, GUMMI_SHELL_BLOCK_ROUND_CORNER, GUMMI_SHELL_BLOCK_CONE, GUMMI_SHELL_BLOCK_DOME),
            GUMMI_DISPEL_BLOCK_KEYS = List.of(GUMMI_DISPEL_BLOCK_CUBE, GUMMI_DISPEL_BLOCK_WEDGE, GUMMI_DISPEL_BLOCK_PYRAMID, GUMMI_DISPEL_BLOCK_CYLINDER, GUMMI_DISPEL_BLOCK_PIE, GUMMI_DISPEL_BLOCK_ROUND_CORNER, GUMMI_DISPEL_BLOCK_CONE, GUMMI_DISPEL_BLOCK_DOME),
            GUMMI_DIFFERENT_BLOCK_KEYS = List.of(GUMMI_BLOCK_BUBBLE);

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

    public static List<? extends EntityType<?>> getEntitiesInTag(Level level, TagKey<EntityType<?>> tag) {
        return level.registryAccess().lookupOrThrow(Registries.ENTITY_TYPE).getOrThrow(tag).stream().map(Holder::value).toList();
    }

    public static Item getFirstItemInTag(Level level, TagKey<Item> tag) {
        List<Item> items = getItemsInTag(level, tag);
        if (items != null && !items.isEmpty()) {
            return getItemsInTag(level, tag).get(0);
        }
        KingdomKeys.LOGGER.error("Tried to get item from empty or non existent tag {}", tag.location().toString());
        return null;
    }

    private static TagKey<Item> item(String pName) {
        return TagKey.create(Registries.ITEM, KingdomKeys.rl(pName));
    }

    private static TagKey<EntityType<?>> entity(String pName) {
        return TagKey.create(Registries.ENTITY_TYPE, KingdomKeys.rl(pName));
    }

}

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

    public static final TagKey<Item>
            MATERIALS = bind("synthesis/materials"),

            XEMNAS = bind("organization/xemnas"),
            XIGBAR = bind("organization/xigbar"),
            XALDIN = bind("organization/xaldin"),
            VEXEN = bind("organization/vexen"),
            LEXAEUS = bind("organization/lexaeus"),
            ZEXION = bind("organization/zexion"),
            SAIX = bind("organization/saix"),
            AXEL = bind("organization/axel"),
            DEMYX = bind("organization/demyx"),
            LUXORD = bind("organization/luxord"),
            MARLUXIA = bind("organization/marluxia"),
            LARXENE = bind("organization/larxene"),
            ROXAS = bind("organization/roxas"),

            KEYBLADES = bind("keyblades"),
            KEYCHAINS = bind("keychains"),
            MAGICS = bind("magics"),
            DRIVES = bind("drives"),
            ORG = bind("org_weapons"),
            PAULDRONS = bind("pauldrons"),
            ACCESSORIES = bind("accessories"),
            ARMORS = bind("armors"),
            MUSIC_DISCS = bind("music_discs"),
            SYNTHESIS_MATERIAL = bind("synthesis_material"),
            MAP_CARD = bind("map_card"),
            WORLD_CARD = bind("world_card"),
            KEY_CARD = bind("key_card"),
            BIOME_MEMORY = bind("biome_memory"),

            GUMMI_BLOCK_CUBE = bind("gummi_block_cube"),
            GUMMI_BLOCK_WEDGE = bind("gummi_block_wedge"),
            GUMMI_BLOCK_PYRAMID = bind("gummi_block_pyramid"),
            GUMMI_BLOCK_CYLINDER = bind("gummi_block_cylinder"),
            GUMMI_BLOCK_PIE = bind("gummi_block_pie"),
            GUMMI_BLOCK_ROUND_CORNER = bind("gummi_block_round_corner"),
            GUMMI_BLOCK_CONE = bind("gummi_block_cone"),
            GUMMI_BLOCK_DOME = bind("gummi_block_dome"),

            GUMMI_SHELL_BLOCK_CUBE = bind("gummi_shell_block_cube"),
            GUMMI_SHELL_BLOCK_WEDGE = bind("gummi_shell_block_wedge"),
            GUMMI_SHELL_BLOCK_PYRAMID = bind("gummi_shell_block_pyramid"),
            GUMMI_SHELL_BLOCK_CYLINDER = bind("gummi_shell_block_cylinder"),
            GUMMI_SHELL_BLOCK_PIE = bind("gummi_shell_block_pie"),
            GUMMI_SHELL_BLOCK_ROUND_CORNER = bind("gummi_shell_block_round_corner"),
            GUMMI_SHELL_BLOCK_CONE = bind("gummi_shell_block_cone"),
            GUMMI_SHELL_BLOCK_DOME = bind("gummi_shell_block_dome"),

            GUMMI_DISPEL_BLOCK_CUBE = bind("gummi_dispel_block_cube"),
            GUMMI_DISPEL_BLOCK_WEDGE = bind("gummi_dispel_block_wedge"),
            GUMMI_DISPEL_BLOCK_PYRAMID = bind("gummi_dispel_block_pyramid"),
            GUMMI_DISPEL_BLOCK_CYLINDER = bind("gummi_dispel_block_cylinder"),
            GUMMI_DISPEL_BLOCK_PIE = bind("gummi_dispel_block_pie"),
            GUMMI_DISPEL_BLOCK_ROUND_CORNER = bind("gummi_dispel_block_round_corner"),
            GUMMI_DISPEL_BLOCK_CONE = bind("gummi_dispel_block_cone"),
            GUMMI_DISPEL_BLOCK_DOME = bind("gummi_dispel_block_dome"),

            GUMMI_BLOCK_BUBBLE = bind("gummi_block_bubble"),

            GUMMI_BLOCK_AERO_TRIANGLE = bind("gummi_block_aero_triangle"),
            GUMMI_BLOCK_AERO_SQUARE = bind("gummi_block_aero_square");

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

    private static TagKey<Item> bind(String pName) {
        return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, pName));
    }

}

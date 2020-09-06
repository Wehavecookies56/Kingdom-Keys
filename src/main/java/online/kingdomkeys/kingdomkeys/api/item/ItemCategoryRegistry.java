package online.kingdomkeys.kingdomkeys.api.item;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.HashMap;

public class ItemCategoryRegistry {

    public static HashMap<String, ItemCategory> categories = new HashMap<>();

    static {
        register(Items.WOODEN_SWORD, ItemCategory.TOOL);
        register(Items.WOODEN_AXE, ItemCategory.TOOL);
        register(Items.WOODEN_HOE, ItemCategory.TOOL);
        register(Items.WOODEN_PICKAXE, ItemCategory.TOOL);
        register(Items.WOODEN_SHOVEL, ItemCategory.TOOL);

        register(Items.STONE_SWORD, ItemCategory.TOOL);
        register(Items.STONE_AXE, ItemCategory.TOOL);
        register(Items.STONE_HOE, ItemCategory.TOOL);
        register(Items.STONE_PICKAXE, ItemCategory.TOOL);
        register(Items.STONE_SHOVEL, ItemCategory.TOOL);

        register(Items.IRON_SWORD, ItemCategory.TOOL);
        register(Items.IRON_AXE, ItemCategory.TOOL);
        register(Items.IRON_HOE, ItemCategory.TOOL);
        register(Items.IRON_PICKAXE, ItemCategory.TOOL);
        register(Items.IRON_SHOVEL, ItemCategory.TOOL);

        register(Items.GOLDEN_SWORD, ItemCategory.TOOL);
        register(Items.GOLDEN_AXE, ItemCategory.TOOL);
        register(Items.GOLDEN_HOE, ItemCategory.TOOL);
        register(Items.GOLDEN_PICKAXE, ItemCategory.TOOL);
        register(Items.GOLDEN_SHOVEL, ItemCategory.TOOL);

        register(Items.DIAMOND_SWORD, ItemCategory.TOOL);
        register(Items.DIAMOND_AXE, ItemCategory.TOOL);
        register(Items.DIAMOND_HOE, ItemCategory.TOOL);
        register(Items.DIAMOND_PICKAXE, ItemCategory.TOOL);
        register(Items.DIAMOND_SHOVEL, ItemCategory.TOOL);

        register(Items.FLINT_AND_STEEL, ItemCategory.TOOL);
        register(Items.SHEARS, ItemCategory.TOOL);
        register(Items.COMPASS, ItemCategory.TOOL);
        register(Items.CLOCK, ItemCategory.TOOL);
        register(Items.FISHING_ROD, ItemCategory.TOOL);
        register(Items.LEAD, ItemCategory.TOOL);
        register(Items.MAP, ItemCategory.TOOL);
        register(Items.FILLED_MAP, ItemCategory.TOOL);
        register(Blocks.TNT, ItemCategory.TOOL);
        register(Blocks.TORCH, ItemCategory.TOOL);
        register(Items.BUCKET, ItemCategory.TOOL);
        register(Items.WATER_BUCKET, ItemCategory.TOOL);
        register(Items.LAVA_BUCKET, ItemCategory.TOOL);
        register(Items.TRIDENT, ItemCategory.TOOL);

        register(Items.LEATHER_HELMET, ItemCategory.EQUIPMENT);
        register(Items.LEATHER_CHESTPLATE, ItemCategory.EQUIPMENT);
        register(Items.LEATHER_LEGGINGS, ItemCategory.EQUIPMENT);
        register(Items.LEATHER_BOOTS, ItemCategory.EQUIPMENT);

        register(Items.IRON_HELMET, ItemCategory.EQUIPMENT);
        register(Items.IRON_CHESTPLATE, ItemCategory.EQUIPMENT);
        register(Items.IRON_LEGGINGS, ItemCategory.EQUIPMENT);
        register(Items.IRON_BOOTS, ItemCategory.EQUIPMENT);

        register(Items.GOLDEN_HELMET, ItemCategory.EQUIPMENT);
        register(Items.GOLDEN_CHESTPLATE, ItemCategory.EQUIPMENT);
        register(Items.GOLDEN_LEGGINGS, ItemCategory.EQUIPMENT);
        register(Items.GOLDEN_BOOTS, ItemCategory.EQUIPMENT);

        register(Items.DIAMOND_HELMET, ItemCategory.EQUIPMENT);
        register(Items.DIAMOND_CHESTPLATE, ItemCategory.EQUIPMENT);
        register(Items.DIAMOND_LEGGINGS, ItemCategory.EQUIPMENT);
        register(Items.DIAMOND_BOOTS, ItemCategory.EQUIPMENT);

        register(Items.ELYTRA, ItemCategory.EQUIPMENT);

        register(Items.CARROT, ItemCategory.CONSUMABLE);
        register(Items.BREAD, ItemCategory.CONSUMABLE);
        register(Items.APPLE, ItemCategory.CONSUMABLE);
        register(Items.GOLDEN_APPLE, ItemCategory.CONSUMABLE);
        register(Items.GOLDEN_CARROT, ItemCategory.CONSUMABLE);
        register(Items.BEETROOT, ItemCategory.CONSUMABLE);
        register(Items.BEETROOT_SOUP, ItemCategory.CONSUMABLE);
        register(Items.ROTTEN_FLESH, ItemCategory.CONSUMABLE);
        register(Items.SPIDER_EYE, ItemCategory.CONSUMABLE);
        register(Items.MUSHROOM_STEW, ItemCategory.CONSUMABLE);
        register(Items.MILK_BUCKET, ItemCategory.CONSUMABLE);
        register(Items.BEEF, ItemCategory.CONSUMABLE);
        register(Items.COOKED_BEEF, ItemCategory.CONSUMABLE);
        register(Items.PORKCHOP, ItemCategory.CONSUMABLE);
        register(Items.COOKED_PORKCHOP, ItemCategory.CONSUMABLE);
        register(Items.RABBIT, ItemCategory.CONSUMABLE);
        register(Items.COOKED_RABBIT, ItemCategory.CONSUMABLE);
        register(Items.CHICKEN, ItemCategory.CONSUMABLE);
        register(Items.COOKED_CHICKEN, ItemCategory.CONSUMABLE);
        register(Items.MUTTON, ItemCategory.CONSUMABLE);
        register(Items.COOKED_MUTTON, ItemCategory.CONSUMABLE);
        register(ItemTags.FISHES, ItemCategory.CONSUMABLE);
        register(Items.COOKIE, ItemCategory.CONSUMABLE);
        register(Items.POTATO, ItemCategory.CONSUMABLE);
        register(Items.BAKED_POTATO, ItemCategory.CONSUMABLE);
        register(Items.POISONOUS_POTATO, ItemCategory.CONSUMABLE);
        register(Items.CAKE, ItemCategory.CONSUMABLE);
        register(Items.CHORUS_FRUIT, ItemCategory.CONSUMABLE);
        register(Items.MELON, ItemCategory.CONSUMABLE);
        register(Items.GLISTERING_MELON_SLICE, ItemCategory.CONSUMABLE);
        register(Items.RABBIT_STEW, ItemCategory.CONSUMABLE);
        register(Items.POTION, ItemCategory.CONSUMABLE);

        register(Items.DIRT, ItemCategory.BUILDING);
        register(Items.GRASS, ItemCategory.BUILDING);
        register(Items.GRASS_PATH, ItemCategory.BUILDING);
        register(Tags.Items.STONE, ItemCategory.BUILDING);
        register(Tags.Items.COBBLESTONE, ItemCategory.BUILDING);
        register(Tags.Items.SAND, ItemCategory.BUILDING);
        register(Tags.Items.SANDSTONE, ItemCategory.BUILDING);
        register(Items.BRICKS, ItemCategory.BUILDING);
        register(Tags.Items.NETHERRACK, ItemCategory.BUILDING);
        register(Items.OBSIDIAN, ItemCategory.BUILDING);
        register(ItemTags.PLANKS, ItemCategory.BUILDING);
        register(ItemTags.LOGS, ItemCategory.BUILDING);
        register(ItemTags.WOOL, ItemCategory.BUILDING);
        register(Items.DIAMOND_BLOCK, ItemCategory.BUILDING);
        register(Items.IRON_BLOCK, ItemCategory.BUILDING);
        register(Items.GOLD_BLOCK, ItemCategory.BUILDING);
        register(Items.COAL_BLOCK, ItemCategory.BUILDING);
        register(Items.CRAFTING_TABLE, ItemCategory.BUILDING);
        register(Items.FURNACE, ItemCategory.BUILDING);
        register(Tags.Items.CHESTS, ItemCategory.BUILDING);
        register(Tags.Items.GLASS, ItemCategory.BUILDING);
        register(Tags.Items.GLASS_PANES, ItemCategory.BUILDING);
        register(Items.CLAY, ItemCategory.BUILDING);
        register(Items.OAK_DOOR, ItemCategory.BUILDING);
        register(Items.ACACIA_DOOR, ItemCategory.BUILDING);
        register(Items.SPRUCE_DOOR, ItemCategory.BUILDING);
        register(Items.BIRCH_DOOR, ItemCategory.BUILDING);
        register(Items.JUNGLE_DOOR, ItemCategory.BUILDING);
        register(Items.OAK_TRAPDOOR, ItemCategory.BUILDING);
        register(Items.STONE_BRICKS, ItemCategory.BUILDING);
        register(Items.SPONGE, ItemCategory.BUILDING);
        register(Items.SOUL_SAND, ItemCategory.BUILDING);
        register(Items.GLOWSTONE, ItemCategory.BUILDING);
        register(Items.SNOW, ItemCategory.BUILDING);
        register(Tags.Items.FENCES, ItemCategory.BUILDING);
        register(Items.IRON_BARS, ItemCategory.BUILDING);
        register(Items.IRON_DOOR, ItemCategory.BUILDING);
        register(Items.IRON_TRAPDOOR, ItemCategory.BUILDING);
        register(Items.OAK_PRESSURE_PLATE, ItemCategory.BUILDING);
        register(Items.STONE_PRESSURE_PLATE, ItemCategory.BUILDING);
        register(Items.LIGHT_WEIGHTED_PRESSURE_PLATE, ItemCategory.BUILDING);
        register(Items.HEAVY_WEIGHTED_PRESSURE_PLATE, ItemCategory.BUILDING);
        register(Items.REDSTONE_BLOCK, ItemCategory.BUILDING);
        register(Items.REDSTONE_LAMP, ItemCategory.BUILDING);
        register(Items.SLIME_BLOCK, ItemCategory.BUILDING);
        register(Items.PISTON, ItemCategory.BUILDING);
        register(Items.STICKY_PISTON, ItemCategory.BUILDING);
        register(Items.RAIL, ItemCategory.BUILDING);
        register(Items.ACTIVATOR_RAIL, ItemCategory.BUILDING);
        register(Items.DETECTOR_RAIL, ItemCategory.BUILDING);
        register(Items.POWERED_RAIL, ItemCategory.BUILDING);
        register(Items.STONE_SLAB, ItemCategory.BUILDING);
        register(Items.SMOOTH_STONE_SLAB, ItemCategory.BUILDING);
        register(BlockTags.WOODEN_SLABS, ItemCategory.BUILDING);
        register(Items.MAGMA_BLOCK, ItemCategory.BUILDING);
        register(Items.SEA_LANTERN, ItemCategory.BUILDING);
        register(ItemTags.SIGNS, ItemCategory.BUILDING);
        register(ItemTags.BEDS, ItemCategory.BUILDING);
        register(Items.ITEM_FRAME, ItemCategory.BUILDING);
        register(ItemTags.ANVIL, ItemCategory.BUILDING);
        register(Items.ENCHANTING_TABLE, ItemCategory.BUILDING);
        register(Tags.Items.END_STONES, ItemCategory.BUILDING);
        register(Items.BOOKSHELF, ItemCategory.BUILDING);
        register(Items.BONE_BLOCK, ItemCategory.BUILDING);
        register(Items.END_STONE_BRICKS, ItemCategory.BUILDING);
        register(Items.END_PORTAL_FRAME, ItemCategory.BUILDING);
        register(Items.COBWEB, ItemCategory.BUILDING);
        register(Items.SPAWNER, ItemCategory.BUILDING);
        register(Items.LILY_PAD, ItemCategory.BUILDING);
        
    }

    public static void register(Item item, ItemCategory category) {
        categories.put(item.getRegistryName().toString(), category);
    }

    public static void register(Block block, ItemCategory category) {
        categories.put(block.getRegistryName().toString(), category);
    }

    public static <T extends IForgeRegistryEntry<T>> void register(Tag<T> tag, ItemCategory category) {
        tag.getAllElements().forEach(t -> {
            categories.put(t.getRegistryName().toString(), category);
        });
    }

    public static boolean hasCategory(Item item) {
        return categories.containsKey(item.getRegistryName().toString());
    }

    public static ItemCategory getCategory(Item item) {
        return categories.get(item.getRegistryName().toString());
    }

}
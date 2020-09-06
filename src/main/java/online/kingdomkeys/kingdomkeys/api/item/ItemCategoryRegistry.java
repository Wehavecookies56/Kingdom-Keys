package online.kingdomkeys.kingdomkeys.api.item;

import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.AxeItem;
import net.minecraft.item.BowItem;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.HoeItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.SwordItem;
import net.minecraft.tags.Tag;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class ItemCategoryRegistry {

    public static HashMap<String, ItemCategory> categories = new HashMap<>();

    static {
    	ForgeRegistries.BLOCKS.forEach(block -> {
    		register(block, ItemCategory.BUILDING);
    	});
    	
    	ForgeRegistries.ITEMS.forEach(item -> {
    		if(item instanceof SwordItem || item instanceof PickaxeItem || item instanceof ShovelItem || item instanceof HoeItem || item instanceof AxeItem || item instanceof CrossbowItem || item instanceof BowItem) {
    			register(item, ItemCategory.TOOL);
    		} else if(item.isFood()) {
    			register(item, ItemCategory.CONSUMABLE);
    		} else if(item instanceof ArmorItem) {
    			register(item, ItemCategory.EQUIPMENT);
    		}
    		
    	});

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
package online.kingdomkeys.kingdomkeys.api.item;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.HashMap;

//TODO probably replace with data map
public class ItemCategoryRegistry {

    public static HashMap<String, ItemCategory> categories = new HashMap<>();

    static {
        BuiltInRegistries.BLOCK.forEach(block -> {
    		register(block, ItemCategory.BUILDING);
    	});
    	
    	BuiltInRegistries.ITEM.forEach(item -> {
    		if(item instanceof SwordItem || item instanceof ShieldItem || item instanceof PickaxeItem || item instanceof ShovelItem || item instanceof HoeItem || item instanceof AxeItem || item instanceof CrossbowItem || item instanceof BowItem) {
    			register(item, ItemCategory.TOOL);
    		} else if(item.getDefaultInstance().getFoodProperties(null) != null || item instanceof PotionItem) {
    			register(item, ItemCategory.CONSUMABLE);
    		} else if(item instanceof ArmorItem || item instanceof ElytraItem) {
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
        categories.put(BuiltInRegistries.ITEM.getKey(item).toString(), category);
    }

    public static void register(Block block, ItemCategory category) {
        categories.put(BuiltInRegistries.BLOCK.getKey(block).toString(), category);
    }

    //TODO work with tags better
    /**
    public static <T extends IForgeRegistryEntry<T>> void register(SetTag<T> tag, ItemCategory category) {
        tag.getValues().forEach(t -> {
            categories.put(t.getRegistryName().toString(), category);
        });
    }**/

    public static boolean hasCategory(Item item) {
        return categories.containsKey(BuiltInRegistries.ITEM.getKey(item).toString());
    }

    public static ItemCategory getCategory(Item item) {
        return categories.get(BuiltInRegistries.ITEM.getKey(item).toString());
    }

}
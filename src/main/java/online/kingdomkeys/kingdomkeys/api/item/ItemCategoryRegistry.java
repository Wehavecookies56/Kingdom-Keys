package online.kingdomkeys.kingdomkeys.api.item;

import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;

public class ItemCategoryRegistry {

    public static HashMap<String, ItemCategory> categories = new HashMap<>();

    static {
    	ForgeRegistries.BLOCKS.forEach(block -> {
    		register(block, ItemCategory.BUILDING);
    	});
    	
    	ForgeRegistries.ITEMS.forEach(item -> {
    		if(item instanceof SwordItem || item instanceof ShieldItem || item instanceof PickaxeItem || item instanceof ShovelItem || item instanceof HoeItem || item instanceof AxeItem || item instanceof CrossbowItem || item instanceof BowItem) {
    			register(item, ItemCategory.TOOL);
    		} else if(item.isEdible() || item instanceof PotionItem) {
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
        categories.put(item.getRegistryName().toString(), category);
    }

    public static void register(Block block, ItemCategory category) {
        categories.put(block.getRegistryName().toString(), category);
    }

    //TODO work with tags better
    /**
    public static <T extends IForgeRegistryEntry<T>> void register(SetTag<T> tag, ItemCategory category) {
        tag.getValues().forEach(t -> {
            categories.put(t.getRegistryName().toString(), category);
        });
    }**/

    public static boolean hasCategory(Item item) {
        return categories.containsKey(item.getRegistryName().toString());
    }

    public static ItemCategory getCategory(Item item) {
        return categories.get(item.getRegistryName().toString());
    }

}
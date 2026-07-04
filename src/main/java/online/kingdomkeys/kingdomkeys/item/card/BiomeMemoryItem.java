package online.kingdomkeys.kingdomkeys.item.card;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import online.kingdomkeys.kingdomkeys.item.ICreativeTab;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class BiomeMemoryItem extends Item implements ICreativeTab {

	public BiomeMemoryItem(Properties properties, ResourceKey<Biome> biome) {
		super(properties);
		if (biome != null) {
			Utils.MEMORY_BY_BIOME.put(biome, this);
		}
	}

	@Override
	public Tab getTab() {
		return Tab.CARDS;
	}
}

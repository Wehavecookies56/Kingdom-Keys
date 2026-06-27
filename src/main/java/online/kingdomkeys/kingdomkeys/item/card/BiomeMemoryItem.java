package online.kingdomkeys.kingdomkeys.item.card;

import net.minecraft.world.item.Item;
import online.kingdomkeys.kingdomkeys.item.ICreativeTab;

public class BiomeMemoryItem extends Item implements ICreativeTab {

	public BiomeMemoryItem(Properties properties) {
		super(properties);
	}

	@Override
	public Tab getTab() {
		return Tab.CARDS;
	}
}

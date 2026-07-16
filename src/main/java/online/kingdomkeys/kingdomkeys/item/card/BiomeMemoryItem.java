package online.kingdomkeys.kingdomkeys.item.card;

import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.fml.loading.FMLEnvironment;
import online.kingdomkeys.kingdomkeys.item.ICreativeTab;
import online.kingdomkeys.kingdomkeys.lib.ModTags;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCDisplayGivenItems;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.List;

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

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
		if (!level.isClientSide() && !FMLEnvironment.production) {
			List<ItemStack> testList = ModTags.getItemsInTag(level, ModTags.KEYBLADES).stream().map(ItemStack::new).toList();
			Utils.giveItems((ServerPlayer) player, testList.toArray(new ItemStack[0]));
		}
		return super.use(level, player, usedHand);
	}
}

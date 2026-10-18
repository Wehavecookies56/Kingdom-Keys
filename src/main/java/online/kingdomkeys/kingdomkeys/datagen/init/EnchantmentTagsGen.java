package online.kingdomkeys.kingdomkeys.datagen.init;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EnchantmentTagsProvider;
import net.minecraft.tags.EnchantmentTags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.enchantment.ModEnchantments;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class EnchantmentTagsGen extends EnchantmentTagsProvider {

	public EnchantmentTagsGen(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
		super(output, lookupProvider, KingdomKeys.MODID, existingFileHelper);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		// Enchanting table, loot and villager trades
		tag(EnchantmentTags.NON_TREASURE).add(ModEnchantments.VANQUISH);
	}
}

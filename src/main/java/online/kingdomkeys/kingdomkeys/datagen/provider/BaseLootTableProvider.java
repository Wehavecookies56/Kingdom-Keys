package online.kingdomkeys.kingdomkeys.datagen.provider;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import online.kingdomkeys.kingdomkeys.datagen.init.LootTables;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class BaseLootTableProvider extends LootTableProvider {
    public BaseLootTableProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> providerCompletableFuture) {
        super(pOutput, Set.of(), List.of(new SubProviderEntry(LootTables::new, LootContextParamSets.BLOCK)), providerCompletableFuture);
    }
}

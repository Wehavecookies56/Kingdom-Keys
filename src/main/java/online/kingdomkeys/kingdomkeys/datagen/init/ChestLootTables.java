package online.kingdomkeys.kingdomkeys.datagen.init;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.TagEntry;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.lib.ModTags;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class ChestLootTables implements LootTableSubProvider {

    private HolderLookup.Provider lookupProvider;

    Map<ResourceLocation, LootTable.Builder> lootTables;

    public ChestLootTables(HolderLookup.Provider lookupProvider) {
        this.lookupProvider = lookupProvider;
        lootTables = new HashMap<>();
    }

    public void build() {
        add(KingdomKeys.rl("castle_oblivion/standard_treasure"),
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1))
                                .add(TagEntry.expandTag(ModTags.MAGICS))));
    }

    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> consumer) {
        build();
        lootTables.forEach((location, builder) -> consumer.accept(ResourceKey.create(Registries.LOOT_TABLE, location), builder));
    }

    public void add(ResourceLocation location, LootTable.Builder builder) {
        lootTables.put(location, builder);
    }
}
